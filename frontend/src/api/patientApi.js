import axios from "axios";

const API_BASE = "/api/patients";

const patientApi = {
  getPatients: async (params) => {
    return axios.get(API_BASE, { params }); //patient 목록 전체
  },

  getPatientById: async (patientId) => {
    return axios.get(`${API_BASE}/${patientId}`);//patient 상세 조회
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