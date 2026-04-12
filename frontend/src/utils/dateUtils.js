/**
 * 날짜 표시: `Date` 인스턴스는 캘린더용 `YYYY-MM-DD`, 그 외(ISO 문자열 등)는 게시판 표시용 `YYYY.MM.DD`.
 */
export const formatDate = (dateOrStr) => {
  if (dateOrStr === undefined || dateOrStr === null || dateOrStr === '') {
    return '-';
  }
  const date =
    dateOrStr instanceof Date ? dateOrStr : new Date(dateOrStr);
  if (Number.isNaN(date.getTime())) return '-';
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');

  if (dateOrStr instanceof Date) {
    return `${year}-${month}-${day}`;
  }
  return `${year}.${month}.${day}`;
};

// 캘린더 등 — 시간 표시 (HH:mm)
export const formatTime = (date) => {
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  return `${hours}:${minutes}`;
};

// 캘린더 등 — 월 표시 (YYYY년 MM월)
export const formatMonthTitle = (date) => {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  return `${year}년 ${month}월`;
};

// 캘린더 등 — 날짜 입력값을 `Date` 객체로 변환
export const toDate = (value) => {
  if (!value) return null;

  if (typeof value === 'string') {
    const d = new Date(value);
    return Number.isNaN(d.getTime()) ? null : d;
  }

  // 배열 형태: [year, month, day, hour?, minute?, second?]
  if (Array.isArray(value)) {
    const [y, m, d, hh = 0, mm = 0, ss = 0] = value;
    const dt = new Date(y, (m ?? 1) - 1, d ?? 1, hh, mm, ss);
    return Number.isNaN(dt.getTime()) ? null : dt;
  }

  // 객체 형태: { year, month, day, hour?, minute?, second? } 또는 { y, m, d, hh?, mm?, ss? }
  if (typeof value === 'object') {
    const y = value.year ?? value.y;
    const m = value.monthValue ?? value.month ?? value.m;
    const d = value.dayOfMonth ?? value.day ?? value.d;
    const hh = value.hour ?? 0;
    const mm = value.minute ?? 0;
    const ss = value.second ?? 0;
    if (!y || !m || !d) return null;
    const dt = new Date(y, m - 1, d, hh, mm, ss);
    return Number.isNaN(dt.getTime()) ? null : dt;
  }

  return null;
};

// 캘린더 등 — 날짜 입력값을 `YYYY-MM-DD` 문자열로 변환
export const toDateString = (value) => {
  const d = toDate(value);
  return d ? formatDate(d) : null;
};

/**
 * 위젯 등 — 상대 시간 (게시판)
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

  return formatDate(dateStr);
};
