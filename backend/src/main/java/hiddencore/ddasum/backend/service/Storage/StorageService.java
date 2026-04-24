package hiddencore.ddasum.backend.service.Storage;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import hiddencore.ddasum.backend.domain.Document;
import hiddencore.ddasum.backend.domain.Document.DocumentStatus;
import hiddencore.ddasum.backend.domain.Location;
import hiddencore.ddasum.backend.domain.Patient;
import hiddencore.ddasum.backend.domain.Users;
import hiddencore.ddasum.backend.repository.StorageDB.StorageRepository;
import hiddencore.ddasum.backend.web.dto.StorageDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StorageService {

	private final StorageRepository storageRepository;

	public StorageDto.PatientSearchResponse searchPatients(StorageDto.PatientSearchRequest request) {
		List<Patient> patients = storageRepository.findPatients(request);
		long totalCount = storageRepository.countPatients(request);

		return StorageDto.PatientSearchResponse.builder()
				.facilityId(request.getFacilityId())
				.page(request.getPage())
				.size(request.getSize())
				.totalCount(totalCount)
				.patients(patients.stream().map(this::toPatientSummary).toList())
				.build();
	}

	public StorageDto.InvoiceSearchResponse searchInvoices(StorageDto.InvoiceSearchRequest request) {
		List<Document> documents = storageRepository.findDocuments(request);
		long totalCount = storageRepository.countDocuments(request);

		return StorageDto.InvoiceSearchResponse.builder()
				.page(request.getPage())
				.size(request.getSize())
				.totalCount(totalCount)
				.invoices(documents.stream().map(this::toInvoiceItem).toList())
				.build();
	}

	@Transactional
	public StorageDto.PaymentProcessResponse processPayment(StorageDto.PaymentProcessRequest request) {
		Document document = getDocument(request.getDocumentId());
		document.setStatus(DocumentStatus.PAID);
		document.setApprovedAt(resolveDateTime(request.getPaidAt()));
		document.setIssuedAt(resolveDateTime(request.getPaidAt()));
		appendContent(document, buildPaymentMemo(request));
		storageRepository.saveDocument(document);

		return StorageDto.PaymentProcessResponse.builder()
				.paymentId(document.getDocumentId())
				.documentId(request.getDocumentId())
				.paidAmount(request.getAmount())
				.outstandingBalance(null)
				.documentStatus(document.getStatus())
				.processedAt(resolveDateTime(request.getPaidAt()))
				.build();
	}

	@Transactional
	public StorageDto.UpdateOutstandingBalanceResponse updateOutstandingBalance(StorageDto.UpdateOutstandingBalanceRequest request) {
		Document document = getDocument(request.getDocumentId());
		appendContent(document, buildOutstandingBalanceMemo(request));
		storageRepository.saveDocument(document);

		return StorageDto.UpdateOutstandingBalanceResponse.builder()
				.documentId(request.getDocumentId())
				.previousOutstandingBalance(null)
				.newOutstandingBalance(request.getNewOutstandingBalance())
				.updatedByUserId(request.getUpdatedByUserId())
				.updatedAt(LocalDateTime.now())
				.build();
	}

	public StorageDto.PaymentHistorySearchResponse getPaymentHistories(StorageDto.PaymentHistorySearchRequest request) {
		StorageDto.InvoiceSearchRequest invoiceSearchRequest = StorageDto.InvoiceSearchRequest.builder()
				.facilityId(request.getFacilityId())
				.patientId(request.getPatientId())
				.documentStatus(DocumentStatus.PAID)
				.billingStartDate(request.getPaymentStartDate())
				.billingEndDate(request.getPaymentEndDate())
				.page(request.getPage())
				.size(request.getSize())
				.build();

		List<Document> documents = storageRepository.findDocuments(invoiceSearchRequest);
		long totalCount = storageRepository.countDocuments(invoiceSearchRequest);

		return StorageDto.PaymentHistorySearchResponse.builder()
				.page(request.getPage())
				.size(request.getSize())
				.totalCount(totalCount)
				.paymentHistories(documents.stream()
						.map(document -> toPaymentHistoryItem(document, request.getPaymentMethod()))
						.toList())
				.build();
	}

	public StorageDto.OverdueHistorySearchResponse searchOverdueHistories(StorageDto.OverdueHistorySearchRequest request) {
		LocalDateTime 기준일 = request.getOverdueAsOfDate() != null
				? request.getOverdueAsOfDate().atStartOfDay()
				: LocalDateTime.now();
		StorageDto.InvoiceSearchRequest invoiceSearchRequest = StorageDto.InvoiceSearchRequest.builder()
				.facilityId(request.getFacilityId())
				.patientId(request.getPatientId())
				.documentStatus(DocumentStatus.PAYMENT_PENDING)
				.page(request.getPage())
				.size(request.getSize())
				.build();

		List<Document> documents = storageRepository.findDocuments(invoiceSearchRequest).stream()
				.filter(document -> document.getRequestedAt() != null && document.getRequestedAt().isBefore(기준일))
				.toList();
		return StorageDto.OverdueHistorySearchResponse.builder()
				.page(request.getPage())
				.size(request.getSize())
				.totalCount((long) documents.size())
				.overdueHistories(documents.stream().map(document -> toOverdueHistoryItem(document, 기준일)).toList())
				.build();
	}

	@Transactional
	public StorageDto.ReceiptPrintResponse printReceipt(StorageDto.ReceiptPrintRequest request) {
		Document document = getDocument(request.getPaymentId());
		document.setStatus(DocumentStatus.ISSUED);
		document.setIssuedAt(LocalDateTime.now());
		storageRepository.saveDocument(document);

		return StorageDto.ReceiptPrintResponse.builder()
				.documentId(document.getDocumentId())
				.paymentId(request.getPaymentId())
				.documentType(request.getDocumentType())
				.fileUrl(document.getFileUrls())
				.issuedAt(document.getIssuedAt())
				.build();
	}

	@Transactional
	public StorageDto.CancelPaymentResponse cancelPayment(StorageDto.CancelPaymentRequest request) {
		Document document = getDocument(request.getPaymentId());
		document.setStatus(DocumentStatus.CANCELLED);
		appendContent(document, "[수납취소] " + request.getCancelReason());
		storageRepository.saveDocument(document);

		return StorageDto.CancelPaymentResponse.builder()
				.paymentId(request.getPaymentId())
				.documentStatus(document.getStatus())
				.cancelledAt(LocalDateTime.now())
				.restoredOutstandingBalance(null)
				.build();
	}

	@Transactional
	public StorageDto.AlarmResponse requestAlarm(StorageDto.AlarmRequest request) {
		int requestedCount = request.getReceiverUserIds() != null ? request.getReceiverUserIds().size() : 0;
		return StorageDto.AlarmResponse.builder()
				.notificationIds(Collections.emptyList())
				.requestedCount(requestedCount)
				.requestedAt(LocalDateTime.now())
				.build();
	}

	public StorageDto.StaffPermissionCheckResponse checkPermission(StorageDto.StaffPermissionCheckRequest request) {
		Users staff = storageRepository.findStaff(request.getStaffUserId(), request.getFacilityId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "담당자를 찾을 수 없습니다."));
		boolean hasPermission = hasPermission(staff, request.getPermissionCode());

		return StorageDto.StaffPermissionCheckResponse.builder()
				.staffUserId(request.getStaffUserId())
				.facilityId(request.getFacilityId())
				.permissionCode(request.getPermissionCode())
				.hasPermission(hasPermission)
				.roleName(staff.getRole().name())
				.message(hasPermission ? "권한이 확인되었습니다." : "권한이 없습니다.")
				.build();
	}

	private StorageDto.PatientSummary toPatientSummary(Patient patient) {
		return StorageDto.PatientSummary.builder()
				.patientId(patient.getPatientId())
				.name(patient.getName())
				.gender(patient.getGender() != null ? patient.getGender().name() : null)
				.birthDate(patient.getBirthDate())
				.roomName(toRoomName(patient.getLocationId()))
				.admissionStatus(patient.getAdmissionStatus())
				.admissionDate(patient.getAdmissionDate())
				.dischargeDate(patient.getDischargeDate())
				.build();
	}

	private StorageDto.InvoiceItem toInvoiceItem(Document document) {
		Patient patient = document.getPatientId();
		return StorageDto.InvoiceItem.builder()
				.documentId(document.getDocumentId())
				.title(document.getTitle())
				.patientId(patient != null ? patient.getPatientId() : null)
				.patientName(patient != null ? patient.getName() : null)
				.billingDate(document.getRequestedAt() != null ? document.getRequestedAt().toLocalDate() : null)
				.dueDate(document.getApprovedAt() != null ? document.getApprovedAt().toLocalDate() : null)
				.billedAmount(null)
				.paidAmount(null)
				.outstandingBalance(null)
				.documentType(document.getType())
				.documentStatus(document.getStatus())
				.build();
	}

	private StorageDto.PaymentHistoryItem toPaymentHistoryItem(Document document, StorageDto.PaymentMethod paymentMethod) {
		Patient patient = document.getPatientId();
		return StorageDto.PaymentHistoryItem.builder()
				.paymentId(document.getDocumentId())
				.documentId(document.getDocumentId())
				.patientId(patient != null ? patient.getPatientId() : null)
				.patientName(patient != null ? patient.getName() : null)
				.amount(extractAmount(document.getContent()))
				.paymentMethod(paymentMethod)
				.documentStatus(document.getStatus())
				.processedByUserId(document.getRequesterUserId() != null ? document.getRequesterUserId().getUserId() : null)
				.paidAt(document.getIssuedAt())
				.build();
	}

	private StorageDto.OverdueHistoryItem toOverdueHistoryItem(Document document, LocalDateTime 기준일) {
		Patient patient = document.getPatientId();
		LocalDateTime requestedAt = document.getRequestedAt();
		long overdueDays = requestedAt == null ? 0L : java.time.Duration.between(requestedAt, 기준일).toDays();
		return StorageDto.OverdueHistoryItem.builder()
				.documentId(document.getDocumentId())
				.title(document.getTitle())
				.patientId(patient != null ? patient.getPatientId() : null)
				.patientName(patient != null ? patient.getName() : null)
				.dueDate(requestedAt != null ? requestedAt.toLocalDate() : null)
				.overdueDays(Math.max(overdueDays, 0L))
				.overdueAmount(null)
				.documentStatus(document.getStatus())
				.build();
	}

	private Document getDocument(Long documentId) {
		return storageRepository.findDocument(documentId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "문서를 찾을 수 없습니다."));
	}

	private LocalDateTime resolveDateTime(LocalDateTime dateTime) {
		return dateTime != null ? dateTime : LocalDateTime.now();
	}

	private String toRoomName(Location location) {
		if (location == null) {
			return null;
		}
		StringBuilder builder = new StringBuilder();
		if (location.getBuilding() != null && !location.getBuilding().isBlank()) {
			builder.append(location.getBuilding()).append(' ');
		}
		if (location.getRoom() != null && !location.getRoom().isBlank()) {
			builder.append(location.getRoom());
		}
		if (location.getBed() != null && !location.getBed().isBlank()) {
			if (builder.length() > 0) {
				builder.append(' ');
			}
			builder.append(location.getBed());
		}
		String value = builder.toString().trim();
		return value.isEmpty() ? null : value;
	}

	private void appendContent(Document document, String appendedText) {
		String currentContent = document.getContent();
		if (currentContent == null || currentContent.isBlank()) {
			document.setContent(appendedText);
			return;
		}
		document.setContent(currentContent + System.lineSeparator() + appendedText);
	}

	private String buildPaymentMemo(StorageDto.PaymentProcessRequest request) {
		return "[수납처리] amount=" + request.getAmount()
				+ ", method=" + request.getPaymentMethod()
				+ ", processedByUserId=" + request.getProcessedByUserId()
				+ (request.getMemo() != null ? ", memo=" + request.getMemo() : "");
	}

	private String buildOutstandingBalanceMemo(StorageDto.UpdateOutstandingBalanceRequest request) {
		return "[미수납잔액수정] balance=" + request.getNewOutstandingBalance()
				+ ", updatedByUserId=" + request.getUpdatedByUserId()
				+ ", reason=" + request.getReason();
	}
	
	private BigDecimal extractAmount(String content) {
		if (content == null || !content.contains("amount=")) {
			return null;
		}
		String[] tokens = content.split("amount=");
		String lastToken = tokens[tokens.length - 1];
		String numeric = lastToken.split("[,\\s]")[0].trim();
		try {
			return new BigDecimal(numeric);
		} catch (NumberFormatException exception) {
			return null;
		}
	}

	private boolean hasPermission(Users staff, String permissionCode) {
		if (permissionCode == null || permissionCode.isBlank()) {
			return false;
		}
		return switch (staff.getRole()) {
			case ADMIN -> true;
			case DOCTOR -> !"PAYMENT_CANCEL".equalsIgnoreCase(permissionCode);
			case CAREGIVER -> "PATIENT_VIEW".equalsIgnoreCase(permissionCode)
					|| "PAYMENT_HISTORY_VIEW".equalsIgnoreCase(permissionCode)
					|| "OVERDUE_VIEW".equalsIgnoreCase(permissionCode);
			case GUARDIAN -> false;
		};
	}
}