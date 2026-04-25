import api from "./index";

export const getNotices = () => api.get("/api/notices");

export const getMeals = () => api.get("/api/meals");

export const getPatient = () => api.get("/api/patient");
