import axios from "axios";

/**
 * axios 인스턴스
 *
 * baseURL 주의:
 * - 안드로이드 에뮬레이터: http://10.0.2.2:8080/api
 * - iOS 시뮬레이터:        http://localhost:8080/api
 * - 실기기 (Expo Go):     http://[PC의 IP]:8080/api
 */
const api = axios.create({
  baseURL: "http://localhost:8080/api",
  timeout: 10000,
});

export default api;