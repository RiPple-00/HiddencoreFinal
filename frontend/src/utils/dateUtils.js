export const toDate = (value) => {
  if (!value && value !== 0) return null;
  if (value instanceof Date) return value;
  if (typeof value === "number") return new Date(value);
  const normalized = String(value).trim();
  const parsed = new Date(normalized);
  return Number.isNaN(parsed.getTime()) ? null : parsed;
};

export const formatDate = (date) => {
  const parsed = toDate(date);
  if (!parsed) return "";
  const year = parsed.getFullYear();
  const month = String(parsed.getMonth() + 1).padStart(2, "0");
  const day = String(parsed.getDate()).padStart(2, "0");
  return `${year}-${month}-${day}`;
};
