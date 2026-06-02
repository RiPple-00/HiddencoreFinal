import React, { useCallback, useEffect, useMemo, useRef, useState } from "react";
import {
  ActivityIndicator,
  View,
  ScrollView,
  TouchableOpacity,
} from "react-native";
import { SafeAreaView } from "react-native-safe-area-context";
import Text from "@/components/Text";
import { Ionicons, MaterialCommunityIcons } from "@expo/vector-icons";
import {
  fetchGuardianLinkedPatients,
  fetchGuardianWeeklyReport,
} from "../../api/careChecklistApi";
 
const FALLBACK_DAILY_RATES = [
  { day: "월", rate: 0 },
  { day: "화", rate: 0 },
  { day: "수", rate: 0 },
  { day: "목", rate: 0 },
  { day: "금", rate: 0 },
  { day: "토", rate: 0 },
  { day: "일", rate: 0 },
];

const DAYS = ["월", "화", "수", "목", "금", "토", "일"];
const DAY_LABELS = ["일", "월", "화", "수", "목", "금", "토"];

const FALLBACK_CHECKLIST_ROWS = [
  { label: "식사 도움", percent: 0, percentStyle: "warn", dailyComments: ["-", "-", "-", "-", "-", "-", "-"] },
  { label: "개인 위생 관리", percent: 0, percentStyle: "warn", dailyComments: ["-", "-", "-", "-", "-", "-", "-"] },
  { label: "상태 안정화", percent: 0, percentStyle: "warn", dailyComments: ["-", "-", "-", "-", "-", "-", "-"] },
  { label: "배변 관리", percent: 0, percentStyle: "warn", dailyComments: ["-", "-", "-", "-", "-", "-", "-"] },
];

function todayStr() {
  const d = new Date();
  const y = d.getFullYear();
  const m = String(d.getMonth() + 1).padStart(2, "0");
  const day = String(d.getDate()).padStart(2, "0");
  return `${y}-${m}-${day}`;
}

function dateToStr(d) {
  const y = d.getFullYear();
  const m = String(d.getMonth() + 1).padStart(2, "0");
  const day = String(d.getDate()).padStart(2, "0");
  return `${y}-${m}-${day}`;
}

function addDays(base, delta) {
  const d = new Date(base);
  d.setDate(d.getDate() + delta);
  return d;
}

function prettyDate(v) {
  if (!v) return "-";
  const s = String(v);
  return s.replace(/-/g, ".");
}

function parseDateLike(value) {
  if (!value) return null;
  if (value instanceof Date) {
    return new Date(value.getFullYear(), value.getMonth(), value.getDate());
  }

  const s = String(value);
  const m = s.match(/^(\d{4})-(\d{2})-(\d{2})$/);
  if (m) {
    return new Date(Number(m[1]), Number(m[2]) - 1, Number(m[3]));
  }

  const parsed = new Date(s);
  if (Number.isNaN(parsed.getTime())) return null;
  return new Date(parsed.getFullYear(), parsed.getMonth(), parsed.getDate());
}

function dayLabelFromDate(value) {
  const d = parseDateLike(value);
  if (!d) return null;
  return DAY_LABELS[d.getDay()];
}

function startOfWeekMonday(dateValue) {
  const d = new Date(dateValue);
  const day = d.getDay();
  const deltaToMonday = day === 0 ? -6 : 1 - day;
  d.setDate(d.getDate() + deltaToMonday);
  return new Date(d.getFullYear(), d.getMonth(), d.getDate());
}

export default function ReportPage({ navigation }) {
  const [expandedRows, setExpandedRows] = useState({});
  const [sectionOpen, setSectionOpen] = useState({
    checklist: false,
    program: false,
    prescription: false,
  });
  const [patientId, setPatientId] = useState(null);
  const [report, setReport] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const latestReportRequestIdRef = useRef(0);
  const [weekOffset, setWeekOffset] = useState(0);
  const toggleRow = (index) =>
    setExpandedRows((prev) => ({ ...prev, [index]: !prev[index] }));
  const toggleSection = (key) =>
    setSectionOpen((prev) => ({ ...prev, [key]: !prev[key] }));

  const anchorWeekStart = useMemo(() => {
    const now = new Date();
    const today = new Date(now.getFullYear(), now.getMonth(), now.getDate());
    return startOfWeekMonday(today);
  }, []);

  const selectedStartDate = useMemo(
    () => addDays(anchorWeekStart, weekOffset * 7),
    [anchorWeekStart, weekOffset]
  );

  const selectedEndDate = useMemo(
    () => addDays(selectedStartDate, 6),
    [selectedStartDate]
  );

  const weekStartStr = useMemo(() => dateToStr(selectedStartDate), [selectedStartDate]);
  const weekEndStr = useMemo(() => dateToStr(selectedEndDate), [selectedEndDate]);

  const canGoNextWeek = useMemo(() => {
    return weekOffset < 0;
  }, [weekOffset]);

  const goPrevWeek = () => setWeekOffset((prev) => prev - 1);
  const goNextWeek = () => {
    if (!canGoNextWeek) return;
    setWeekOffset((prev) => Math.min(prev + 1, 0));
  };

  useEffect(() => {
    let cancelled = false;
    (async () => {
      try {
        const res = await fetchGuardianLinkedPatients();
        if (cancelled) return;
        const linked = res.data ?? [];
        const primary = linked.find((p) => p.primary) ?? linked[0];
        if (!primary) {
          setError("연결된 환자가 없습니다.");
          setLoading(false);
          return;
        }
        setPatientId(primary.patientId);
      } catch {
        if (!cancelled) {
          setError("환자 정보를 불러오지 못했습니다.");
          setLoading(false);
        }
      }
    })();
    return () => {
      cancelled = true;
    };
  }, []);

  const refreshWeeklyReport = useCallback(async ({ showLoading = false } = {}) => {
    if (patientId == null) return;

    const requestId = latestReportRequestIdRef.current + 1;
    latestReportRequestIdRef.current = requestId;

    try {
      if (showLoading) setLoading(true);
      setError(null);
      const res = await fetchGuardianWeeklyReport(patientId, weekStartStr, weekEndStr);
      if (requestId !== latestReportRequestIdRef.current) return;
      setReport(res.data ?? null);
    } catch {
      if (requestId !== latestReportRequestIdRef.current) return;
      setError("주간 보고서를 불러오지 못했습니다.");
    } finally {
      if (showLoading && requestId === latestReportRequestIdRef.current) {
        setLoading(false);
      }
    }
  }, [patientId, weekStartStr, weekEndStr]);

  useEffect(() => {
    if (patientId == null) return;
    refreshWeeklyReport({ showLoading: true });
  }, [patientId, refreshWeeklyReport]);

  useEffect(() => {
    if (patientId == null) return;

    const timerId = setInterval(() => {
      refreshWeeklyReport({ showLoading: false });
    }, 3000);

    return () => {
      clearInterval(timerId);
    };
  }, [patientId, refreshWeeklyReport]);

  const dailyRates = useMemo(() => {
    if (!report?.dailyRates?.length) return FALLBACK_DAILY_RATES;
    return report.dailyRates.map((d) => ({
      day: dayLabelFromDate(d.date) ?? d.day,
      rate: d.rate ?? 0,
    }));
  }, [report]);

  const weekDayLabels = useMemo(() => {
    const start = parseDateLike(report?.periodStart) ?? selectedStartDate;
    return Array.from({ length: 7 }, (_, i) => dayLabelFromDate(addDays(start, i)) ?? DAYS[i]);
  }, [report?.periodStart, selectedStartDate]);

  const checklistData = useMemo(() => {
    if (!report?.checklistRows?.length) return FALLBACK_CHECKLIST_ROWS;
    return report.checklistRows.map((r) => ({
      label: r.label,
      percent: `${r.percent ?? 0}%`,
      percentStyle: r.percentStyle,
      dailyComments: Array.from(
        { length: 7 },
        (_, i) => (Array.isArray(r.dailyComments) ? r.dailyComments[i] : null) ?? "-"
      ),
    }));
  }, [report]);

  const mealMissingCount = useMemo(() => ({
    morning: report?.mealMissingCount?.morning ?? 0,
    lunch: report?.mealMissingCount?.lunch ?? 0,
    dinner: report?.mealMissingCount?.dinner ?? 0,
    total: report?.mealMissingCount?.total ?? 0,
  }), [report]);

  const overallRate = report?.overallRate ?? 0;
  const riskLevel = overallRate >= 90 ? "안정" : overallRate >= 75 ? "주의" : "위험";
  const riskClass = riskLevel === "위험"
    ? "w-[90px] h-[90px] rounded-2xl border border-error-primary justify-center items-center bg-error-secondary"
    : riskLevel === "주의"
      ? "w-[90px] h-[90px] rounded-2xl border border-guardian-text-secondary justify-center items-center bg-[#FFF7D6]"
      : "w-[90px] h-[90px] rounded-2xl border border-success-primary justify-center items-center bg-success-secondary";
  const riskIconColor = riskLevel === "위험" ? "#ED584C" : riskLevel === "주의" ? "#FCC101" : "#34A853";
  const riskTextClass = riskLevel === "위험" ? "mt-[6px] text-error-primary font-bold text-base"
    : riskLevel === "주의" ? "mt-[6px] text-guardian-text-secondary font-bold text-base"
      : "mt-[6px] text-success-primary font-bold text-base";
  const reportStart = prettyDate(report?.periodStart);
  const reportEnd = prettyDate(report?.periodEnd);
  const aiComments = report?.aiComments?.length ? report.aiComments : ["데이터가 충분하지 않아 기본 안내를 표시합니다."];
  const programSection = report?.programSection ?? {
    activityTitle: "실내 가드닝 활동",
    activityDescription: "작은 식물을 심고 물을 주며 소근육 자극 및 심리적 안정감을 도와주었습니다.",
    effects: [
      "소근육 조절 능력 및 손가락 민첩성 향상 관찰",
      "식물과의 교감을 통해 심리적 평온함 유지 및 사회적 유대감 형성",
    ],
    recommendations: [
      "소화 기능 저하로 식욕이 감소된 상태입니다. 누워서 하는 가벼운 스트레칭 위주 활동을 권장합니다.",
      "간단한 색칠화 또는 쓰기 활동을 추가하면 인지 자극과 집중력 유지에 효과적입니다.",
      "가벼운 복부 마사지와 수분 보충 활동을 프로그램에 포함하면 도움이 됩니다.",
    ],
  };

  return (
    <SafeAreaView
      className="flex-1 bg-guardian-bg-secondary"
      edges={["bottom", "left", "right"]}
    >
      <ScrollView className="flex-1 px-4" showsVerticalScrollIndicator={false}>

        {/* 헤더 */}
        <View className="flex-row justify-between items-center mt-[10px] mb-[18px]">
          <View className="flex-row items-center">
            <TouchableOpacity
              onPress={() => navigation?.goBack?.()}
              activeOpacity={0.7}
              className="w-[34px] h-[34px] rounded-full items-center justify-center bg-background-neutral mr-[6px]"
            >
              <Ionicons name="chevron-back" size={20} color="#503115" />
            </TouchableOpacity>
            <Text className="text-xl font-bold text-guardian-text-primary">정기 보고서</Text>
          </View>
          <View className="w-[34px]" />
        </View>

        <View className="bg-background-neutral rounded-[14px] px-[12px] py-[10px] mb-[12px]">
          <View className="flex-row items-center justify-between">
            <TouchableOpacity onPress={goPrevWeek} activeOpacity={0.7}>
              <Text className="text-[13px] font-bold text-guardian-text-primary">이전 주</Text>
            </TouchableOpacity>

            <Text className="text-[13px] font-bold text-guardian-text-neutral">
              {prettyDate(weekStartStr)} ~ {prettyDate(weekEndStr)}
            </Text>

            <TouchableOpacity onPress={goNextWeek} disabled={!canGoNextWeek} activeOpacity={0.7}>
              <Text className={`text-[13px] font-bold ${canGoNextWeek ? "text-guardian-text-primary" : "text-guardian-text-neutral opacity-40"}`}>
                다음 주
              </Text>
            </TouchableOpacity>
          </View>

        </View>

        {loading ? (
          <View className="bg-background-neutral rounded-[20px] p-[18px] mb-[18px] items-c
          .enter">
            <ActivityIndicator color="#503115" />
            <Text className="mt-2 text-guardian-text-neutral">주간 보고서 불러오는 중...</Text>
          </View>
        ) : null}

        {error ? (
          <View className="bg-error-secondary rounded-[20px] p-[14px] mb-[18px]">
            <Text className="text-error-primary">{error}</Text>
          </View>
        ) : null}

        {/* 환자 카드 */}
        <View className="bg-background-neutral rounded-[20px] p-[18px] mb-[18px]">
          <View className="flex-row items-center">
            <View className="w-[70px] h-[70px] rounded-full bg-guardian-button-secondary" />

            <View className="flex-1 ml-[14px]">
              <Text className="text-xl font-bold text-guardian-text-primary mb-[6px]">
                {report?.patientName ?? "환자"} 어르신
              </Text>
              <View className="flex-row items-center gap-1">
                <Ionicons name="document-text-outline" size={14} color="#503115" />
                <Text className="text-[13px] text-guardian-text-neutral">{reportStart}~</Text>
              </View>
              <Text className="text-[13px] text-guardian-text-neutral mt-[3px]">
                {reportEnd}
              </Text>
              <View className="mt-[10px] bg-guardian-button-secondary self-start rounded-full px-[10px] py-[5px]">
                <Text className="text-xs text-guardian-text-primary font-bold">주간 보고서</Text>
              </View>
            </View>

            <View className={riskClass}>
              <MaterialCommunityIcons name={riskLevel === "안정" ? "shield-check-outline" : "alert-outline"} size={28} color={riskIconColor} />
              <Text className={riskTextClass}>{riskLevel}</Text>
            </View>
          </View>
        </View>

        {/* AI 분석 요약 */}
        <View className="bg-background-neutral rounded-[20px] p-[18px] mb-[18px]">
          <View className="flex-row justify-between items-center mb-3">
            <Text className="text-lg font-bold text-guardian-text-primary">✨ AI 분석 요약</Text>
          </View>

          <Text className="text-[14px] leading-[22px] text-guardian-text-neutral mb-[18px]">
            {report?.summaryText ?? "이번 주 동안 수집된 체크리스트 기반 보고서입니다."}
          </Text>

          <View className="flex-row flex-wrap justify-between">
            {[
              { emoji: "🍽️", title: "식사", desc: `${checklistData[0]?.percent ?? "0%"} 달성` },
              { emoji: "🪥", title: "위생", desc: `${checklistData[1]?.percent ?? "0%"} 관리` },
              { emoji: "🚻", title: "배변", desc: `${checklistData[2]?.percent ?? "0%"} 관리` },
              { emoji: "🟩", title: "환자 상태", desc: riskLevel },
            ].map(({ emoji, title, desc }) => (
              <View key={title} className="w-[48%] bg-guardian-bg-secondary rounded-2xl py-[18px] items-center mb-3">
                <Text className="text-[22px]">{emoji}</Text>
                <Text className="mt-2 font-bold text-guardian-text-primary">{title}</Text>
                <Text className="mt-1 text-[13px] text-guardian-text-neutral">{desc}</Text>
              </View>
            ))}
          </View>

          {/* AI 코멘트 */}
          <View className="bg-guardian-button-secondary rounded-[14px] p-[14px] mt-[6px]">
            <Text className="text-guardian-text-primary font-bold mb-[10px]">💡 AI 분석 코멘트</Text>

            {aiComments.map((line, i) => (
              <View key={i} className="flex-row gap-2 mb-[10px]">
                <Text className="text-[14px] mt-[1px]">💬</Text>
                <Text className="flex-1 text-guardian-text-neutral leading-5 text-[13px]">
                  {line}
                </Text>
              </View>
            ))}
          </View>
        </View>

        {/* 체크리스트 */}
        <View className="bg-background-neutral rounded-[20px] p-[18px] mb-[18px]">
          <TouchableOpacity
            className="flex-row justify-between items-center"
            activeOpacity={0.7}
            onPress={() => toggleSection("checklist")}
          >
            <Text className="text-lg font-bold text-guardian-text-primary">
              1. 요양사 체크리스트 요약
            </Text>
            <Ionicons
              name={sectionOpen.checklist ? "chevron-up" : "chevron-down"}
              size={18}
              color="#503115"
            />
          </TouchableOpacity>

          {sectionOpen.checklist && (
            <>
              {/* 수행률 원 */}
              <View className="w-[130px] h-[130px] rounded-full border-[10px] border-guardian-button-primary justify-center items-center self-center my-5">
                <Text className="text-[28px] font-extrabold text-guardian-text-primary">{overallRate}%</Text>
                <Text className="text-guardian-text-neutral">수행률</Text>
              </View>

              {/* 일별 수행률 */}
              <View className="mb-4">
                <Text className="text-[14px] font-bold text-guardian-text-primary mb-3">
                    일별 수행률 추이
                </Text>
                <View className="flex-row justify-between items-end h-[100px] bg-guardian-bg-secondary rounded-[14px] px-[10px] pt-[10px]">
                  {dailyRates.map((item) => {
                    const barColor =
                      item.rate >= 90 ? "#34A853"
                      : item.rate >= 75 ? "#FCC101"
                      : "#ED584C";
                    return (
                      <View key={item.day} className="flex-1 items-center justify-end h-full">
                        <Text className="text-[9px] text-guardian-text-neutral mb-1 font-bold">
                          {item.rate}%
                        </Text>
                        <View className="w-4 h-[60px] bg-guardian-button-secondary rounded-lg justify-end overflow-hidden">
                          <View style={{ width: "100%", height: `${item.rate}%`, backgroundColor: barColor, borderRadius: 8 }} />
                        </View>
                        <Text className="mt-[6px] mb-2 text-xs font-bold text-guardian-text-neutral">
                          {item.day}
                        </Text>
                      </View>
                    );
                  })}
                </View>
                <View className="flex-row gap-3 mt-[10px]">
                  <Text className="text-[11px] text-guardian-text-neutral">🟢 90% 이상</Text>
                  <Text className="text-[11px] text-guardian-text-neutral">🟡 75~89%</Text>
                  <Text className="text-[11px] text-guardian-text-neutral">🔴 75% 미만</Text>
                </View>
              </View>

              <View className="bg-guardian-bg-secondary rounded-[14px] px-[12px] py-[10px] mb-2">
                <View className="flex-row items-center justify-between mb-[8px]">
                  <Text className="text-[13px] font-bold text-guardian-text-primary">
                    식사 미기입 횟수
                  </Text>
                  <Text className="text-[12px] text-guardian-text-neutral">
                    총 {mealMissingCount.total}회
                  </Text>
                </View>
                <View className="flex-row gap-2">
                  {[
                    { label: "아침", value: mealMissingCount.morning },
                    { label: "점심", value: mealMissingCount.lunch },
                    { label: "저녁", value: mealMissingCount.dinner },
                  ].map((slot) => (
                    <View key={slot.label} className="flex-1 bg-background-neutral rounded-xl py-[10px] items-center">
                      <Text className="text-[12px] text-guardian-text-neutral">{slot.label}</Text>
                      <Text className="mt-[4px] text-[16px] font-bold text-guardian-text-primary">{slot.value}회</Text>
                    </View>
                  ))}
                </View>
              </View>

              {/* 테이블 */}
              <View className="mt-[10px]">
                {checklistData.map((item, index) => {
                  const isExpanded = !!expandedRows[index];
                  const uniqueComments = [...new Set(item.dailyComments.filter((c) => c !== "-"))];
                  const missingComments = uniqueComments.filter((c) => c.includes("미기입"));
                  const summaryComment =
                    missingComments.length > 0
                      ? (missingComments.length === 1
                          ? missingComments[0]
                          : `${missingComments[0]} 외 ${missingComments.length - 1}건`)
                      : uniqueComments.length === 0
                        ? "-"
                        : uniqueComments.length === 1
                          ? uniqueComments[0]
                          : `${uniqueComments[0]} 외 ${uniqueComments.length - 1}건`;
                  const hasMultiple =
                    uniqueComments.length > 1 ||
                    (uniqueComments.length === 1 && item.dailyComments.some((c) => c === "-"));

                  const percentClass =
                    item.percentStyle === "success" ? "text-success-primary font-bold"
                    : item.percentStyle === "warn"    ? "text-guardian-text-secondary font-bold"
                    : "text-error-primary font-bold";

                  return (
                    <View key={index}>
                      <TouchableOpacity
                        className="flex-row justify-between py-3 border-b border-guardian-bg-secondary"
                        onPress={() => toggleRow(index)}
                        activeOpacity={0.7}
                      >
                        <Text className="flex-1 text-guardian-text-primary">{item.label}</Text>
                        <Text className={percentClass}>{item.percent}</Text>
                        <View className="flex-1 flex-row justify-end items-center gap-1">
                          <Text className="text-right text-guardian-text-neutral text-xs" numberOfLines={1}>
                            {summaryComment}
                          </Text>
                          {hasMultiple && (
                            <Text className="text-[10px] text-guardian-text-neutral">
                              {isExpanded ? "▲" : "▼"}
                            </Text>
                          )}
                        </View>
                      </TouchableOpacity>

                      {isExpanded && (
                        <View className="bg-guardian-bg-secondary rounded-xl px-[14px] py-[10px] mb-1 gap-[6px]">
                          {weekDayLabels.map((day, di) => (
                            <View key={di} className="flex-row items-center gap-3">
                              <Text className="w-5 text-[13px] font-bold text-guardian-text-secondary">
                                {day}
                              </Text>
                              <Text
                                className={`text-[13px] flex-1 ${item.dailyComments[di] === "-"
                                  ? "text-guardian-button-primary opacity-40"
                                  : item.label === "상태 안정화" && String(item.dailyComments[di]).includes("컨디션 이상 징후")
                                    ? "text-error-primary font-bold"
                                    : "text-guardian-text-neutral"}`}
                              >
                                {item.dailyComments[di]}
                              </Text>
                            </View>
                          ))}
                        </View>
                      )}
                    </View>
                  );
                })}
              </View>
            </>
          )}
        </View>

        {/* 프로그램 활동 */}
        <View className="bg-background-neutral rounded-[20px] p-[18px] mb-[18px]">
          <TouchableOpacity
            className="flex-row justify-between items-center"
            activeOpacity={0.7}
            onPress={() => toggleSection("program")}
          >
            <Text className="text-lg font-bold text-guardian-text-primary">
              2. 프로그램 활동 및 증진 효과
            </Text>
            <Ionicons
              name={sectionOpen.program ? "chevron-up" : "chevron-down"}
              size={18}
              color="#503115"
            />
          </TouchableOpacity>

          {sectionOpen.program && (
            <>
              <View className="bg-guardian-bg-secondary rounded-2xl p-4 mt-4">
                <Text className="font-bold text-guardian-text-primary mb-2">{programSection.activityTitle}</Text>
                <Text className="text-guardian-text-neutral leading-5">
                  {programSection.activityDescription}
                </Text>
              </View>

              {programSection.effects.map((effect, idx) => (
                <View key={`${effect}-${idx}`} className="bg-guardian-bg-secondary rounded-[14px] p-[14px] mt-[14px]">
                  <Text className="font-bold text-guardian-text-primary mb-[6px]">효과 {idx + 1}</Text>
                  <Text className="text-guardian-text-neutral leading-5">{effect}</Text>
                </View>
              ))}

              {/* AI 추천 */}
              <View className="bg-success-secondary rounded-[14px] p-[14px] mt-4"
                style={{ borderLeftWidth: 3, borderLeftColor: "#34A853" }}
              >
                <Text className="text-success-primary font-bold text-[14px] mb-3">
                   AI 다음 주 프로그램 추천
                </Text>
                {programSection.recommendations.map((rec, i, arr) => (
                  <View key={`${rec}-${i}`} className={`flex-row gap-[10px] ${i < arr.length - 1 ? "mb-3" : ""}`}>
                    <Text className="text-[18px] mt-[1px]">•</Text>
                    <View className="flex-1">
                      <Text className="font-bold text-guardian-text-primary text-[13px] mb-[3px]">추천 {i + 1}</Text>
                      <Text className="text-[13px] text-guardian-text-neutral leading-5">{rec}</Text>
                    </View>
                  </View>
                ))}
              </View>
            </>
          )}
        </View>

        {/* 처방전 요약 */}
        <View className="bg-background-neutral rounded-[20px] p-[18px] mb-[18px]">
          <TouchableOpacity
            className="flex-row justify-between items-center"
            activeOpacity={0.7}
            onPress={() => toggleSection("prescription")}
          >
            <Text className="text-lg font-bold text-guardian-text-primary">
              3. 처방전 요약
            </Text>
            <Ionicons
              name={sectionOpen.prescription ? "chevron-up" : "chevron-down"}
              size={18}
              color="#503115"
            />
          </TouchableOpacity>

          {sectionOpen.prescription && (
            <View className="bg-guardian-bg-secondary rounded-[14px] p-[14px] mt-4">
              <Text className="text-guardian-text-neutral leading-5">
                등록된 처방전 요약 데이터가 없습니다.
              </Text>
            </View>
          )}
        </View>

        <View style={{ height: 40 }} />
      </ScrollView>
    </SafeAreaView>
  );
}