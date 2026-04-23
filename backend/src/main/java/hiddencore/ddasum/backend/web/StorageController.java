package hiddencore.ddasum.backend.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hiddencore.ddasum.backend.service.StorageService.Storage;
import hiddencore.ddasum.backend.web.dto.StorageDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Storage", description = "수납/청구 API")
@RestController
@RequestMapping("/api/storage")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StorageController {

	private final Storage storageService;

	@Operation(summary = "환자 조회", description = "수납 업무 대상 환자를 조회합니다.")
	@PostMapping("/patients/search")
	public ResponseEntity<StorageDto.PatientSearchResponse> searchPatients(
			@Valid @RequestBody StorageDto.PatientSearchRequest request) {
		return ResponseEntity.ok(storageService.searchPatients(request));
	}

	@Operation(summary = "청구서 조회", description = "청구 문서 목록을 조회합니다.")
	@PostMapping("/documents/search")
	public ResponseEntity<StorageDto.InvoiceSearchResponse> searchInvoices(
			@Valid @RequestBody StorageDto.InvoiceSearchRequest request) {
		return ResponseEntity.ok(storageService.searchInvoices(request));
	}

	@Operation(summary = "수납 처리", description = "문서 기준으로 수납을 처리합니다.")
	@PostMapping("/payments")
	public ResponseEntity<StorageDto.PaymentProcessResponse> processPayment(
			@Valid @RequestBody StorageDto.PaymentProcessRequest request) {
		return ResponseEntity.ok(storageService.processPayment(request));
	}

	@Operation(summary = "미수납 잔액 수정", description = "문서의 미수납 잔액을 수정합니다.")
	@PutMapping("/documents/outstanding-balance")
	public ResponseEntity<StorageDto.UpdateOutstandingBalanceResponse> updateOutstandingBalance(
			@Valid @RequestBody StorageDto.UpdateOutstandingBalanceRequest request) {
		return ResponseEntity.ok(storageService.updateOutstandingBalance(request));
	}

	@Operation(summary = "수납 내역 조회", description = "수납 처리 이력을 조회합니다.")
	@PostMapping("/payments/history")
	public ResponseEntity<StorageDto.PaymentHistorySearchResponse> getPaymentHistories(
			@Valid @RequestBody StorageDto.PaymentHistorySearchRequest request) {
		return ResponseEntity.ok(storageService.getPaymentHistories(request));
	}

	@Operation(summary = "연체 내역 조회", description = "연체 문서 내역을 조회합니다.")
	@PostMapping("/overdue/search")
	public ResponseEntity<StorageDto.OverdueHistorySearchResponse> searchOverdueHistories(
			@Valid @RequestBody StorageDto.OverdueHistorySearchRequest request) {
		return ResponseEntity.ok(storageService.searchOverdueHistories(request));
	}

	@Operation(summary = "영수증/납부확인서 출력", description = "영수증 또는 납부확인서 발급을 요청합니다.")
	@PostMapping("/documents/receipt")
	public ResponseEntity<StorageDto.ReceiptPrintResponse> printReceipt(
			@Valid @RequestBody StorageDto.ReceiptPrintRequest request) {
		return ResponseEntity.ok(storageService.printReceipt(request));
	}

	@Operation(summary = "수납 취소", description = "수납을 취소합니다.")
	@PostMapping("/payments/{paymentId}/cancel")
	public ResponseEntity<StorageDto.CancelPaymentResponse> cancelPayment(
			@PathVariable Long paymentId,
			@Valid @RequestBody StorageDto.CancelPaymentRequest request) {
		request.setPaymentId(paymentId);
		return ResponseEntity.ok(storageService.cancelPayment(request));
	}

	@Operation(summary = "알람 요청", description = "수납 관련 알람을 요청합니다.")
	@PostMapping("/alarms")
	public ResponseEntity<StorageDto.AlarmResponse> requestAlarm(
			@Valid @RequestBody StorageDto.AlarmRequest request) {
		return ResponseEntity.ok(storageService.requestAlarm(request));
	}

	@Operation(summary = "담당자 권한 확인", description = "담당자의 업무 권한을 확인합니다.")
	@GetMapping("/staff/{staffUserId}/permissions")
	public ResponseEntity<StorageDto.StaffPermissionCheckResponse> checkPermission(
			@PathVariable Long staffUserId,
			StorageDto.StaffPermissionCheckRequest request) {
		request.setStaffUserId(staffUserId);
		return ResponseEntity.ok(storageService.checkPermission(request));
	}
}
