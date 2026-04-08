import axios from "axios";

const API_BASE = "http://localhost:8080/api/patients";

const patientApi = {
  getPatients: async (params) => {
    return axios.get(API_BASE, { params });
  },

  getPatientById: async (patientId) => {
    return axios.get(`${API_BASE}/${patientId}`);
  },

  createPatient: async (patientData) => {
    return axios.post(API_BASE, patientData);
  },

  updatePatient: async (patientId, patientData) => {
    return axios.put(`${API_BASE}/${patientId}`, patientData);
  },

  deletePatient: async (patientId) => {
    return axios.delete(`${API_BASE}/${patientId}`);
  },
};

export default patientApi;