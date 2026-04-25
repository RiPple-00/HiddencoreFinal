// 날짜/시간 포맷 함수
// 완료 페이지, 목록 페이지, 상세 페이지에서 날짜 보기 좋게 사용
export function formatVisitDate(dateString) {
  if (!dateString) return "-";
  const date = new Date(dateString);
  const options = { year: "numeric", month: "2-digit", day: "2-digit", hour: "2-digit", minute: "2-digit" };
  return date.toLocaleDateString("ko-KR", options).replace(/\./g, "-").replace(/ /g, " ");
}

