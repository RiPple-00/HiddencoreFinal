// 연락처 자동 하이픈 함수

export function formatPhoneNumber(phoneNumber) {
  if (!phoneNumber) return "-";
  const cleaned = ("" + phoneNumber).replace(/\D/g, "");
  const match = cleaned.match(/^(\d{2,3})(\d{3,4})(\d{4})$/);
    if (match) {
        return `${match[1]}-${match[2]}-${match[3]}`;
    }
  return phoneNumber;
}
