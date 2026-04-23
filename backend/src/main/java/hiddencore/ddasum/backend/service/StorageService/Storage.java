package hiddencore.ddasum.backend.service.StorageService;

import java.time.LocalDateTime;
import java.util.Collections;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hiddencore.ddasum.backend.domain.Document.DocumentStatus;
import hiddencore.ddasum.backend.web.dto.StorageDto;

@Service
@Transactional(readOnly = true)
public class Storage {

	public StorageDto.PatientSearchResponse searchPatients(StorageDto.PatientSearchRequest request) {
		return StorageDto.PatientSearchResponse.builder()
				.facilityId(request.getFacilityId())
				.page(request.getPage())
				.size(request.getSize())
				.totalCount(0L)
				.patients(Collections.emptyList())
				.build();
	}

	public StorageDto.InvoiceSearchResponse searchInvoices(StorageDto.InvoiceSearchRequest request) {
		return StorageDto.InvoiceSearchResponse.builder()
				.page(request.getPage())
				.size(request.getSize())
				.totalCount(0L)
				.invoices(Collections.emptyList())
				.build();
	}

	@Transactional
	public StorageDto.PaymentProcessResponse processPayment(StorageDto.PaymentProcessRequest request) {
		return StorageDto.PaymentProcessResponse.builder()
				.paymentId(null)
				.documentId(request.getDocumentId())
				.paidAmount(request.getAmount())
				.outstandingBalance(null)
				.documentStatus(DocumentStatus.PAID)
				.processedAt(LocalDateTime.now())
				.build();
	}

	@Transactional
	public StorageDto.UpdateOutstandingBalanceResponse updateOutstandingBalance(StorageDto.UpdateOutstandingBalanceRequest request) {
		return StorageDto.UpdateOutstandingBalanceResponse.builder()
				.documentId(request.getDocumentId())
				.previousOutstandingBalance(null)
				.newOutstandingBalance(request.getNewOutstandingBalance())
				.updatedByUserId(request.getUpdatedByUserId())
				.updatedAt(LocalDateTime.now())
				.build();
	}

	public StorageDto.PaymentHistorySearchResponse getPaymentHistories(StorageDto.PaymentHistorySearchRequest request) {
		return StorageDto.PaymentHistorySearchResponse.builder()
				.page(request.getPage())
				.size(request.getSize())
				.totalCount(0L)
				.paymentHistories(Collections.emptyList())
				.build();
	}

	public StorageDto.OverdueHistorySearchResponse searchOverdueHistories(StorageDto.OverdueHistorySearchRequest request) {
		return StorageDto.OverdueHistorySearchResponse.builder()
				.page(request.getPage())
				.size(request.getSize())
				.totalCount(0L)
				.overdueHistories(Collections.emptyList())
				.build();
	}

	@Transactional
	public StorageDto.ReceiptPrintResponse printReceipt(StorageDto.ReceiptPrintRequest request) {
		return StorageDto.ReceiptPrintResponse.builder()
				.documentId(null)
				.paymentId(request.getPaymentId())
				.documentType(request.getDocumentType())
				.fileUrl(null)
				.issuedAt(LocalDateTime.now())
				.build();
	}

	@Transactional
	public StorageDto.CancelPaymentResponse cancelPayment(StorageDto.CancelPaymentRequest request) {
		return StorageDto.CancelPaymentResponse.builder()
				.paymentId(request.getPaymentId())
				.documentStatus(DocumentStatus.CANCELLED)
				.cancelledAt(LocalDateTime.now())
				.restoredOutstandingBalance(null)
				.build();
	}

	@Transactional
	public StorageDto.AlarmResponse requestAlarm(StorageDto.AlarmRequest request) {
		return StorageDto.AlarmResponse.builder()
				.notificationIds(Collections.emptyList())
				.requestedCount(request.getReceiverUserIds().size())
				.requestedAt(LocalDateTime.now())
				.build();
	}

	public StorageDto.StaffPermissionCheckResponse checkPermission(StorageDto.StaffPermissionCheckRequest request) {
		return StorageDto.StaffPermissionCheckResponse.builder()
				.staffUserId(request.getStaffUserId())
				.facilityId(request.getFacilityId())
				.permissionCode(request.getPermissionCode())
				.hasPermission(false)
				.roleName(null)
				.message("권한 확인 로직을 구현해야 합니다.")
				.build();
	}
}
