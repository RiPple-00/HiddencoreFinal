import React, { useRef, useState } from "react";
import {
    ActivityIndicator,
    Alert,
    Button,
    StyleSheet,
    Text,
    TouchableOpacity,
    View,
} from "react-native";
import { CameraView, useCameraPermissions } from "expo-camera";
import GuardianBottomTab from "../../components/guardian/basic/GuardianBottomTab";
import api from "../../api";

export default function MedicationQrScanPage({ navigation }) {
    const [permission, requestPermission] = useCameraPermissions();
    const [scanned, setScanned] = useState(false);
    const [saving, setSaving] = useState(false);
    const [qrText, setQrText] = useState("");
    const [savedMedication, setSavedMedication] = useState(null);
    const scanLockRef = useRef(false);

    const handleQrScanned = async ({ data }) => {
        if (scanLockRef.current) return;

        scanLockRef.current = true;
        setScanned(true);
        setSaving(true);
        setQrText(data);
        setSavedMedication(null);

        console.log("QR 원문:", data);

        try {
            const res = await api.post("/api/medications/scan", {
                patientId: 1,
                guardianId: 1,
                qrRawData: data,
            });

            console.log("복약 저장 성공:", res.data);

            setSavedMedication(res.data);

            Alert.alert(
                "저장 완료",
                `${res.data.prescriptionDate} 처방전이 저장되었습니다.`
            );
        } catch (e) {
            console.log("복약 저장 실패:", e?.response?.data || e.message);

            const message =
                e?.response?.data?.message ||
                e?.response?.data?.detail ||
                "처방전 저장에 실패했습니다.";

            Alert.alert("저장 실패", message);

            scanLockRef.current = false;
            setScanned(false);
        } finally {
            setSaving(false);
        }
    };

    if (!permission) {
        return (
            <View style={styles.center}>
                <Text>카메라 권한 확인 중...</Text>
            </View>
        );
    }

    if (!permission.granted) {
        return (
            <View style={styles.center}>
                <Text>처방전 QR 스캔을 위해 카메라 권한이 필요합니다.</Text>
                <Button title="카메라 권한 허용" onPress={() => {
                    scanLockRef.current = false;
                    setScanned(false);
                    setSaving(false);
                    setQrText("");
                    setSavedMedication(null);
                }} />
            </View>
        );
    }

    return (
        <View style={styles.container}>
            <CameraView
                style={styles.camera}
                facing="back"
                barcodeScannerSettings={{
                    barcodeTypes: ["qr"],
                }}
                onBarcodeScanned={scanned || saving ? undefined : handleQrScanned}
            />

            <View style={styles.topBar}>
                <TouchableOpacity
                    style={styles.backButton}
                    onPress={() => navigation.goBack()}
                >
                    <Text style={styles.backText}>‹</Text>
                </TouchableOpacity>

                <Text style={styles.headerTitle}>처방전 QR 스캔</Text>

                <View style={styles.rightBlank} />
            </View>

            <View style={styles.scanGuideContainer}>
                <View style={styles.scanBox}>
                    <View style={[styles.corner, styles.topLeft]} />
                    <View style={[styles.corner, styles.topRight]} />
                    <View style={[styles.corner, styles.bottomLeft]} />
                    <View style={[styles.corner, styles.bottomRight]} />
                </View>

                <Text style={styles.guideText}>
                    처방전 QR 코드를 네모 안에 맞춰주세요
                </Text>
            </View>

            <View style={styles.bottomBox}>
                <Text style={styles.resultTitle}>
                    {savedMedication
                        ? "처방전 저장 완료"
                        : qrText
                            ? "처방전 저장 중"
                            : "QR을 스캔해주세요"}
                </Text>

                {saving ? (
                    <View style={styles.loadingRow}>
                        <ActivityIndicator />
                        <Text style={styles.loadingText}>처방전 정보를 저장하는 중...</Text>
                    </View>
                ) : (
                    <Text style={styles.resultText} numberOfLines={4}>
                        {savedMedication
                            ? `${savedMedication.prescriptionDate}\n${savedMedication.medicineSummary}`
                            : qrText
                                ? qrText
                                : "카메라를 처방전 QR 코드에 가까이 가져가세요."}
                    </Text>
                )}

                {scanned && (
                    <TouchableOpacity
                        style={styles.retryButton}
                        onPress={() => {
                            setScanned(false);
                            setSaving(false);
                            setQrText("");
                            setSavedMedication(null);
                        }}
                    >
                        <Text style={styles.retryText}>다시 스캔</Text>
                    </TouchableOpacity>
                )}
            </View>

            <View style={styles.bottomTabWrap}>
                <GuardianBottomTab
                    navigation={navigation}
                    currentTab="MedicationQrScan"
                />
            </View>
        </View>
    );
}

const BOX_SIZE = 240;

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: "#000",
    },

    camera: {
        flex: 1,
    },

    center: {
        flex: 1,
        alignItems: "center",
        justifyContent: "center",
    },

    topBar: {
        position: "absolute",
        top: 45,
        left: 0,
        right: 0,
        height: 50,
        paddingHorizontal: 18,
        flexDirection: "row",
        alignItems: "center",
        justifyContent: "space-between",
        zIndex: 20,
        elevation: 20,
    },

    backButton: {
        width: 42,
        height: 42,
        borderRadius: 21,
        backgroundColor: "rgba(0,0,0,0.45)",
        alignItems: "center",
        justifyContent: "center",
    },

    backText: {
        color: "#fff",
        fontSize: 36,
        lineHeight: 38,
        fontWeight: "300",
    },

    headerTitle: {
        color: "#fff",
        fontSize: 18,
        fontWeight: "700",
    },

    rightBlank: {
        width: 42,
    },

    scanGuideContainer: {
        position: "absolute",
        top: "28%",
        left: 0,
        right: 0,
        alignItems: "center",
        zIndex: 10,
        elevation: 10,
    },

    scanBox: {
        width: BOX_SIZE,
        height: BOX_SIZE,
        borderWidth: 2,
        borderColor: "rgba(255, 216, 77, 0.55)",
        borderRadius: 18,
        backgroundColor: "rgba(0,0,0,0.08)",
        position: "relative",
    },

    corner: {
        position: "absolute",
        width: 46,
        height: 46,
        borderColor: "#FFD84D",
        zIndex: 20,
        elevation: 20,
    },

    topLeft: {
        top: 0,
        left: 0,
        borderTopWidth: 5,
        borderLeftWidth: 5,
        borderTopLeftRadius: 12,
    },

    topRight: {
        top: 0,
        right: 0,
        borderTopWidth: 5,
        borderRightWidth: 5,
        borderTopRightRadius: 12,
    },

    bottomLeft: {
        bottom: 0,
        left: 0,
        borderBottomWidth: 5,
        borderLeftWidth: 5,
        borderBottomLeftRadius: 12,
    },

    bottomRight: {
        bottom: 0,
        right: 0,
        borderBottomWidth: 5,
        borderRightWidth: 5,
        borderBottomRightRadius: 12,
    },

    guideText: {
        marginTop: 22,
        color: "#fff",
        fontSize: 15,
        fontWeight: "600",
        backgroundColor: "rgba(0,0,0,0.45)",
        paddingHorizontal: 16,
        paddingVertical: 8,
        borderRadius: 20,
    },

    bottomBox: {
        position: "absolute",
        left: 20,
        right: 20,
        bottom: 105,
        backgroundColor: "rgba(0,0,0,0.7)",
        padding: 16,
        borderRadius: 16,
        zIndex: 20,
        elevation: 20,
    },

    resultTitle: {
        color: "white",
        fontSize: 17,
        fontWeight: "700",
        marginBottom: 10,
    },

    resultText: {
        color: "white",
        fontSize: 13,
        lineHeight: 19,
        marginBottom: 12,
    },

    loadingRow: {
        flexDirection: "row",
        alignItems: "center",
        gap: 8,
        marginTop: 8,
        marginBottom: 12,
    },

    loadingText: {
        color: "white",
        fontSize: 13,
    },

    retryButton: {
        marginTop: 12,
        backgroundColor: "#FFD84D",
        paddingVertical: 10,
        borderRadius: 12,
        alignItems: "center",
    },

    retryText: {
        color: "#3A2A00",
        fontSize: 14,
        fontWeight: "800",
    },

    bottomTabWrap: {
        position: "absolute",
        left: 0,
        right: 0,
        bottom: 0,
        zIndex: 30,
        elevation: 30,
    },
});