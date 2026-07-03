package com.ddasum.app.data.remote.dto.medication

/**
 * 백엔드 MedicationService.getMedicationDetail()을 그대로 반영 (GET /api/medications/{id}).
 * 서버 쪽 MedicationDetail 행은 HIRA/MFDS/DUR 보강 필드를 ~35개 갖고 있지만,
 * 여기서는 화면에 표시할 부분집합만 모델링한다 — 나머지 JSON 필드는 Gson이 알아서
 * 무시하기 때문에 필드를 전부 옮기지 않아도 이 DTO는 여전히 정확하게 동작한다.
 */
data class MedicationDetailResponseDto(
    val medicationId: Long,
    val patientId: Long,
    val guardianId: Long?,
    val prescriptionDate: String,
    val medicineSummary: String?,
    val details: List<MedicationDetailItemDto>
)

data class MedicationDetailItemDto(
    val medicationDetailId: Long,
    val itemSeq: String?,
    val medicineName: String?,
    val itemName: String?,
    val manufacturerName: String?,
    val unit: String?,
    val effect: String?,
    val useMethod: String?,
    val warning: String?,
    val caution: String?,
    val interaction: String?,
    val sideEffect: String?,
    val durInfoFound: Boolean?,
    val durWarningCount: Int?
)
