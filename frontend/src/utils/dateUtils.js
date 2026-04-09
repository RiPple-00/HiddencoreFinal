

// 날짜 포맷 전용
export const formatDate = (date) => {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
}

// 시간 포맷 전용
export const formatTime = (date) => {
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  return `${hours}:${minutes}`;
}

export const formatMonthTitle = (date) => {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  return `${year}년 ${month}월`;
}

// 백엔드(LocalDateTime)가 string/array/object 어떤 형태든 Date로 변환
export const toDate = (value) => {
  if (!value) return null;

  // ISO string (예: "2024-10-01T09:00:00")
  if (typeof value === 'string') {
    const d = new Date(value);
    return Number.isNaN(d.getTime()) ? null : d;
  }

  // Jackson timestamp array (예: [2024,10,1,9,0,0] or [2024,10,1,9,0])
  if (Array.isArray(value)) {
    const [y, m, d, hh = 0, mm = 0, ss = 0] = value;
    const dt = new Date(y, (m ?? 1) - 1, d ?? 1, hh, mm, ss);
    return Number.isNaN(dt.getTime()) ? null : dt;
  }

  // object fallback (예: {year, monthValue, dayOfMonth, hour, minute, second})
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

export const toDateString = (value) => {
  const d = toDate(value);
  return d ? formatDate(d) : null;
};
