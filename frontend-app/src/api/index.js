import axios from "axios";
import AsyncStorage from "@react-native-async-storage/async-storage";
import Constants from "expo-constants";
import { NativeModules, Platform } from "react-native";

export const ACCESS_TOKEN_KEY = "accessToken";

const API_PORT = "8080";

export async function persistAccessToken(token) {
  if (token) await AsyncStorage.setItem(ACCESS_TOKEN_KEY, token);
  else await AsyncStorage.removeItem(ACCESS_TOKEN_KEY);
}

/**
 * Metro/Expo가 8081일 수 있어 Spring API 기본 포트는 8080(application.yml server.port)에 맞춥니다.
 * 맞지 않으면 루트에 .env 에 EXPO_PUBLIC_API_BASE_URL=http://PC_IP:포트
 */
function parseHostFromScriptURL() {
  try {
    const url = NativeModules?.SourceCode?.scriptURL;
    if (!url || typeof url !== "string") return null;
    const { hostname } = new URL(url);
    if (!hostname || hostname === "localhost" || hostname === "127.0.0.1") {
      return null;
    }
    return hostname;
  } catch {
    return null;
  }
}

export function resolveApiBaseUrl() {
  const port = API_PORT;

  // 실제 기기 Expo Go 실행 시 Metro 주소에서 PC IP 추출
  const hostFromScript = parseHostFromScriptURL();
  if (hostFromScript) {
    return `http://${hostFromScript}:${port}`;
  }

  // Expo Go debuggerHost에서 PC IP 추출
  const dh =
    Constants.expoGoConfig?.debuggerHost ||
    Constants.manifest?.debuggerHost ||
    Constants.manifest2?.extra?.expoGo?.debuggerHost;

  if (typeof dh === "string" && dh.includes(":")) {
    return `http://${dh.split(":")[0]}:${port}`;
  }

  // Android 에뮬레이터
  if (Platform.OS === "android") {
    return `http://10.0.2.2:${port}`;
  }

  // 웹 실행
  if (
    Platform.OS === "web" &&
    typeof window !== "undefined" &&
    window.location?.hostname
  ) {
    const h = window.location.hostname;
    return `http://${h}:${port}`;
  }

  // iOS 시뮬레이터 / 기타
  return `http://127.0.0.1:${port}`;
}

const baseURL = resolveApiBaseUrl();

const api = axios.create({
  baseURL,
  timeout: 120000,
  headers: {
    "Content-Type": "application/json",
  },
});

api.interceptors.request.use(async (config) => {
  try {
    const token = await AsyncStorage.getItem(ACCESS_TOKEN_KEY);
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
  } catch {
    /* ignore */
  }
  return config;
});

if (__DEV__) {
  // eslint-disable-next-line no-console
  console.log("[api] baseURL =", baseURL);
}

export async function saveAccessToken(token) {
  await persistAccessToken(token);
}

export async function getAccessToken() {
  return AsyncStorage.getItem(ACCESS_TOKEN_KEY);
}

/** JWT payload (role, facilityId 등). base64url 디코딩. */
export function decodeJwtPayload(token) {
  if (!token || typeof token !== "string") return null;
  const parts = token.split(".");
  if (parts.length < 2) return null;
  try {
    let base64 = parts[1].replace(/-/g, "+").replace(/_/g, "/");
    while (base64.length % 4) base64 += "=";
    if (typeof atob === "undefined") return null;
    const raw = atob(base64);
    return JSON.parse(raw);
  } catch {
    return null;
  }
}

export async function clearAccessToken() {
  await AsyncStorage.removeItem(ACCESS_TOKEN_KEY);
}

export default api;
