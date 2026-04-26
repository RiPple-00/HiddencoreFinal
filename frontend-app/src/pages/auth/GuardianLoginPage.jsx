import React, { useState } from "react";
import { Alert, StyleSheet, Text, TextInput, TouchableOpacity, View } from "react-native";
import api from "../../api";

export default function GuardianLoginPage({ navigation }) {
  const [loginId, setLoginId] = useState("guardian001");
  const [password, setPassword] = useState("1234");
  const [loading, setLoading] = useState(false);

  const onSubmit = async () => {
    if (!loginId || !password) {
      Alert.alert("안내", "아이디와 비밀번호를 입력해 주세요.");
      return;
    }
    try {
      setLoading(true);
      await api.post("/api/auth/guardian/login", { loginId, password });
      navigation.replace("GuardianMain");
    } catch (e) {
      const message = e?.response?.data?.message || "로그인에 실패했습니다.";
      Alert.alert("로그인 실패", message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>보호자 로그인</Text>
      <TextInput
        style={styles.input}
        placeholder="아이디"
        autoCapitalize="none"
        value={loginId}
        onChangeText={setLoginId}
      />
      <TextInput
        style={styles.input}
        placeholder="비밀번호"
        secureTextEntry
        value={password}
        onChangeText={setPassword}
      />
      <TouchableOpacity style={styles.button} onPress={onSubmit} disabled={loading}>
        <Text style={styles.buttonText}>{loading ? "로그인 중..." : "로그인"}</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: "center",
    padding: 24,
    backgroundColor: "#fff",
  },
  title: {
    fontSize: 24,
    fontWeight: "700",
    marginBottom: 18,
    textAlign: "center",
  },
  input: {
    borderWidth: 1,
    borderColor: "#d1d5db",
    borderRadius: 8,
    paddingHorizontal: 12,
    paddingVertical: 10,
    marginBottom: 10,
  },
  button: {
    backgroundColor: "#0ea5e9",
    borderRadius: 8,
    paddingVertical: 12,
    marginTop: 6,
  },
  buttonText: {
    color: "#fff",
    textAlign: "center",
    fontWeight: "700",
  },
});
