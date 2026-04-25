import axios from "axios";
import Constants from "expo-constants";
import { NativeModules, Platform } from "react-native";

/**
 * Metro가 올라가 있는 PC의 LAN IP를 추출해 같은 머신의 Spring(기본 8081)에 붙습니다.
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
  const fromEnv = process.env.EXPO_PUBLIC_API_BASE_URL?.trim();
  if (fromEnv) {
    return fromEnv.replace(/\/$/, "");
  }

  const port = process.env.EXPO_PUBLIC_API_PORT?.trim() || "8081";

  const hostFromScript = parseHostFromScriptURL();
  if (hostFromScript) {
    return `http://${hostFromScript}:${port}`;
  }

  const dh =
    Constants.expoGoConfig?.debuggerHost ||
    Constants.manifest?.debuggerHost ||
    Constants.manifest2?.extra?.expoGo?.debuggerHost;
  if (typeof dh === "string" && dh.includes(":")) {
    return `http://${dh.split(":")[0]}:${port}`;
  }

  if (Platform.OS === "android") {
    return `http://10.0.2.2:${port}`;
  }
  return `http://127.0.0.1:${port}`;
}

const baseURL = resolveApiBaseUrl();

const api = axios.create({
  baseURL,
  timeout: 25000,
  headers: {
    "Content-Type": "application/json",
  },
});

if (__DEV__) {
  // eslint-disable-next-line no-console
  console.log("[api] baseURL =", baseURL);
}

export default api;
