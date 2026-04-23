package hiddencore.ddasum.backend.web.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import hiddencore.ddasum.backend.domain.Document.DocumentStatus;
import hiddencore.ddasum.backend.domain.Document.DocumentType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class StorageDto {

	// 1. 환자 조회
	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class PatientSearchRequest {
		@NotNull(message = "facilityId는 필수입니다")
		private Long facilityId;

		private String keyword;
		private String admissionStatus;
		private LocalDate admissionStartDate;
		private LocalDate admissionEndDate;

		@Builder.Default
		@Min(value = 0, message = "page는 0 이상이어야 합니다")
		private Integer page = 0;
        
		@Builder.Default
		@Min(value = 1, message = "size는 1 이상이어야 합니다")
		private Integer size = 20;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class PatientSummary {
		private Long patientId;
		private String name;
		private String gender;
		private LocalDate birthDate;
		private String roomName;
		private String admissionStatus;
		private LocalDate admissionDate;
		private LocalDate dischargeDate;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class PatientSearchResponse {
		private Long facilityId;
		private Integer page;
		private Integer size;
		private Long totalCount;
		private List<PatientSummary> patients;
	}

	// 2. 청구서 조회
	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class InvoiceSearchRequest {
		private Long facilityId;
		private Long patientId;
		private Long documentId;
		private DocumentStatus documentStatus;
		private DocumentType documentType;
		private LocalDate billingStartDate;
		private LocalDate billingEndDate;

		@Builder.Default
		@Min(value = 0, message = "page는 0 이상이어야 합니다")
		private Integer page = 0;

		@Builder.Default
		@Min(value = 1, message = "size는 1 이상이어야 합니다")
		private Integer size = 20;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class InvoiceItem {
		private Long documentId;
		private String title;
		private Long patientId;
		private String patientName;
		private LocalDate billingDate;
		private LocalDate dueDate;
		private BigDecimal billedAmount;
		private BigDecimal paidAmount;
		private BigDecimal outstandingBalance;
		private DocumentType documentType;
		private DocumentStatus documentStatus;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class InvoiceSearchResponse {
		private Integer page;
		private Integer size;
		private Long totalCount;
		private List<InvoiceItem> invoices;
	}

	// 3. 수납 처리
	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class PaymentProcessRequest {
		@NotNull(message = "documentId는 필수입니다")
		private Long documentId;

		@NotNull(message = "patientId는 필수입니다")
		private Long patientId;

		@NotNull(message = "amount는 필수입니다")
		@DecimalMin(value = "0.01", message = "amount는 0보다 커야 합니다")
		private BigDecimal amount;

		@NotNull(message = "paymentMethod는 필수입니다")
		private PaymentMethod paymentMethod;

		private LocalDateTime paidAt;
		private Long processedByUserId;

		@Size(max = 500, message = "memo는 500자 이하여야 합니다")
		private String memo;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class PaymentProcessResponse {
		private Long paymentId;
		private Long documentId;
		private BigDecimal paidAmount;
		private BigDecimal outstandingBalance;
		private DocumentStatus documentStatus;
		private LocalDateTime processedAt;
	}

	// 4. 미수납 잔액 수정
	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class UpdateOutstandingBalanceRequest {
		@NotNull(message = "documentId는 필수입니다")
		private Long documentId;

		@NotNull(message = "newOutstandingBalance는 필수입니다")
		@DecimalMin(value = "0.00", message = "newOutstandingBalance는 0 이상이어야 합니다")
		private BigDecimal newOutstandingBalance;

		@NotBlank(message = "reason은 필수입니다")
		@Size(max = 500, message = "reason은 500자 이하여야 합니다")
		private String reason;

		@NotNull(message = "updatedByUserId는 필수입니다")
		private Long updatedByUserId;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class UpdateOutstandingBalanceResponse {
		private Long documentId;
		private BigDecimal previousOutstandingBalance;
		private BigDecimal newOutstandingBalance;
		private Long updatedByUserId;
		private LocalDateTime updatedAt;
	}

	// 5. 수납 내역 조회
	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class PaymentHistorySearchRequest {
		private Long facilityId;
		private Long patientId;
		private LocalDate paymentStartDate;
		private LocalDate paymentEndDate;
		private PaymentMethod paymentMethod;

		@Builder.Default
		@Min(value = 0, message = "page는 0 이상이어야 합니다")
		private Integer page = 0;

		@Builder.Default
		@Min(value = 1, message = "size는 1 이상이어야 합니다")
		private Integer size = 20;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class PaymentHistoryItem {
		private Long paymentId;
		private Long documentId;
		private Long patientId;
		private String patientName;
		private BigDecimal amount;
		private PaymentMethod paymentMethod;
		private DocumentStatus documentStatus;
		private Long processedByUserId;
		private LocalDateTime paidAt;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class PaymentHistorySearchResponse {
		private Integer page;
		private Integer size;
		private Long totalCount;
		private List<PaymentHistoryItem> paymentHistories;
	}

	// 6. 연체 내역 조회
	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class OverdueHistorySearchRequest {
		private Long facilityId;
		private Long patientId;
		private LocalDate overdueAsOfDate;

		@Builder.Default
		@Min(value = 0, message = "page는 0 이상이어야 합니다")
		private Integer page = 0;

		@Builder.Default
		@Min(value = 1, message = "size는 1 이상이어야 합니다")
		private Integer size = 20;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class OverdueHistoryItem {
		private Long documentId;
		private String title;
		private Long patientId;
		private String patientName;
		private LocalDate dueDate;
		private Long overdueDays;
		private BigDecimal overdueAmount;
		private DocumentStatus documentStatus;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class OverdueHistorySearchResponse {
		private Integer page;
		private Integer size;
		private Long totalCount;
		private List<OverdueHistoryItem> overdueHistories;
	}

	// 7. 영수증 / 납부확인서 출력
	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ReceiptPrintRequest {
		@NotNull(message = "paymentId는 필수입니다")
		private Long paymentId;

		@NotNull(message = "patientId는 필수입니다")
		private Long patientId;

		@NotNull(message = "documentType은 필수입니다")
		private PrintDocumentType documentType;

		@NotNull(message = "requestedByUserId는 필수입니다")
		private Long requestedByUserId;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ReceiptPrintResponse {
		private Long documentId;
		private Long paymentId;
		private PrintDocumentType documentType;
		private String fileUrl;
		private LocalDateTime issuedAt;
	}

	// 8. 수납 취소
	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class CancelPaymentRequest {
		@NotNull(message = "paymentId는 필수입니다")
		private Long paymentId;

		@NotBlank(message = "cancelReason은 필수입니다")
		@Size(max = 500, message = "cancelReason은 500자 이하여야 합니다")
		private String cancelReason;

		@NotNull(message = "cancelledByUserId는 필수입니다")
		private Long cancelledByUserId;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class CancelPaymentResponse {
		private Long paymentId;
		private DocumentStatus documentStatus;
		private LocalDateTime cancelledAt;
		private BigDecimal restoredOutstandingBalance;
	}

	// 9. 알람 요청
	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class AlarmRequest {
		@NotEmpty(message = "receiverUserIds는 최소 1명 이상이어야 합니다")
		private List<Long> receiverUserIds;

		@NotBlank(message = "title은 필수입니다")
		@Size(max = 200, message = "title은 200자 이하여야 합니다")
		private String title;

		@NotBlank(message = "message는 필수입니다")
		@Size(max = 1000, message = "message는 1000자 이하여야 합니다")
		private String message;

		@NotNull(message = "requesterUserId는 필수입니다")
		private Long requesterUserId;

		private String refType;
		private Long refId;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class AlarmResponse {
		private List<Long> notificationIds;
		private Integer requestedCount;
		private LocalDateTime requestedAt;
	}

	// 10. 담당자 권한 확인
	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class StaffPermissionCheckRequest {
		@NotNull(message = "staffUserId는 필수입니다")
		private Long staffUserId;

		@NotNull(message = "facilityId는 필수입니다")
		private Long facilityId;

		@NotBlank(message = "permissionCode는 필수입니다")
		private String permissionCode;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class StaffPermissionCheckResponse {
		private Long staffUserId;
		private Long facilityId;
		private String permissionCode;
		private Boolean hasPermission;
		private String roleName;
		private String message;
	}

	public enum PaymentMethod {
		CASH,
		CARD,
		BANK_TRANSFER,
		AUTO_WITHDRAWAL,
		ETC
	}

	public enum PrintDocumentType {
		RECEIPT,
		PAYMENT_CONFIRMATION
	}
}
