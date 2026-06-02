import { StyleSheet } from "react-native";

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

    historyButton: {
        marginTop: 10,
        backgroundColor: "rgba(255,255,255,0.18)",
        paddingVertical: 10,
        borderRadius: 12,
        alignItems: "center",
        borderWidth: 1,
        borderColor: "rgba(255,255,255,0.25)",
    },

    historyText: {
        color: "#FFFFFF",
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

export default styles;