// 상태 코드 -> 한글 라벨 변환
// Pending -> 승인 대기, Approved -> 승인, Rejected -> 반려

export function getVisitStatusLabel(status) {
  switch (status) {
    case "PENDING":
      return "승인 대기";
    case "APPROVED":
      return "승인";
    case "REJECTED":
      return "반려";
    default:
      return status;
  }
}
