import api from "./index";

const patientApi = {
  getPatients: (params) => {
    return api.get(`/patients`, { params });
  },

  getPatientById: (patientId) => {
    return api.get(`/patients/${patientId}`);
  },

  createPatient: (patientData) => {
    return api.post(`/patients`, patientData);
  },

  updatePatient: (patientId, patientData) => {
    return api.put(`/patients/${patientId}`, patientData);
  },

  deletePatient: (patientId) => {
    return api.delete(`/patients/${patientId}`);
  },
};

export default patientApi;
