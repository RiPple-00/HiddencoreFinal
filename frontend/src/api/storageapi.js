import api from "./index";

const storageApi = {
  // 1. 환자 조회
  getPatients: (params) => {
    return api.get(`/storage/patients`, { params });
  },

  // 2. 청구서 조회
  getInvoices: (params) => {
    return api.get(`/storage/invoices`, { params });
  },

  // 3. 수납 처리
  processPayment: (paymentData) => {
    const body = { ...paymentData };
    if (body.paidAt === "" || body.paidAt == null) {
      delete body.paidAt;
    }
    if (body.memo === "" || body.memo == null) {
      delete body.memo;
    }
    return api.post(`/storage/payments`, body);
  },

  // 4. 미수납 잔액 수정
  updateOutstandingBalance: (balanceData) => {
    return api.put(`/storage/outstanding-balance`, balanceData);
  },

  // 5. 수납 내역 조회
  getPaymentHistories: (params) => {
    return api.get(`/storage/payments`, { params });
  },

  // 6. 연체 내역 조회
  getOverdueHistories: (params) => {
    return api.get(`/storage/overdue`, { params });
  },

  // 7. 영수증 / 납부확인서 출력
  printReceipt: (printData) => {
    return api.post(`/storage/receipts/print`, printData);
  },

  // 8. 수납 취소
  cancelPayment: (cancelData) => {
    return api.post(`/storage/payments/cancel`, cancelData);
  },

  // 9. 알람 요청
  sendAlarm: (alarmData) => {
    return api.post(`/storage/alarms`, alarmData);
  },

};

export default storageApi;