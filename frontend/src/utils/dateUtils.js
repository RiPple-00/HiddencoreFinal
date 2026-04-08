/**
 * 날짜 문자열을 "YYYY.MM.DD" 형식으로 변환
 * table 모드 날짜 표시에 사용
 * @param {string} dateStr - ISO 날짜 문자열
 * @returns {string} "2024.05.20"
 */
export const formatDate = (dateStr) => {
  if (!dateStr) return '-';
  const date = new Date(dateStr);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  return `${year}.${month}.${day}`;
};

/**
 * 날짜 문자열을 상대적 시간으로 변환
 * widget 모드 날짜 표시에 사용
 * @param {string} dateStr - ISO 날짜 문자열
 * @returns {string} "방금 전" | "N분 전" | "N시간 전" | "어제" | "YYYY.MM.DD"
 */
export const formatRelativeTime = (dateStr) => {
  if (!dateStr) return '-';

  const now = new Date();
  const date = new Date(dateStr);
  const diffMs = now - date;
  const diffMin = Math.floor(diffMs / (1000 * 60));
  const diffHour = Math.floor(diffMs / (1000 * 60 * 60));
  const diffDay = Math.floor(diffMs / (1000 * 60 * 60 * 24));

  if (diffMin < 1) return '방금 전';
  if (diffMin < 60) return `${diffMin}분 전`;
  if (diffHour < 24) return `${diffHour}시간 전`;
  if (diffDay === 1) return '어제';

  // 이틀 이상이면 날짜로 표시
  return formatDate(dateStr);
};
