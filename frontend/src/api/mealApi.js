import axios from "axios";

const API = axios.create({
  baseURL: "http://localhost:8080/api",
});

API.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");

  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  return config;
});

export default {
  bulkUpsertMeals: (data) => API.post("/meals/bulk", data),
  getMealsByDate: (date) => API.get("/meals/by-date", {
    params: { date },
  }),
  getMealsByRange: (startDate, endDate) => API.get("/meals/by-range", {
    params: { startDate, endDate },
  }),
};