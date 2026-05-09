import React, { useState } from "react";
import { SafeAreaView, View, Text, ScrollView, TouchableOpacity, Modal, Pressable } from "react-native";
import Svg, { Circle } from "react-native-svg";
import { styles } from "../../styles/guardianChecklist.styles";

/* ===== 데이터 (실제 프로젝트에서는 API로 교체) ===== */
const SUMMARY = {
  date: "2024.05.20 (월)",
  percent: 80, total: 26, done: 22,
  normal: 16, warn: 4, danger: 6,
};

const DETAILS = {
  "meal-dinner":  { name: "식사 섭취량", icon: "🍴", level: "danger", desc: "저녁 식사를 평소의 절반 이하만 드셨습니다. 입맛이 없다고 하시며 식사 중 자주 멈추셨습니다. 보조 식품 보충이 필요해 보입니다." },
  "water-dinner": { name: "수분 섭취량", icon: "💧", level: "danger", desc: "오늘 하루 종일 수분 섭취량이 평소의 절반 이하로 매우 적습니다. 빨대를 사용해도 삼키기 어려워하시며, 입술이 많이 건조한 상태입니다." },
  "edema":        { name: "부종 양상",   icon: "🦶", level: "danger", desc: "양쪽 발목 부위에 어제보다 부종이 심해졌습니다. 손가락으로 눌렀을 때 자국이 오래 남는 함요부종이 관찰됩니다." },
  "fall":         { name: "낙상 유무",   icon: "⚠️", level: "danger", desc: "오전 10시경 화장실 이동 중 균형을 잃고 넘어지셨습니다. 큰 외상은 없으나 우측 무릎에 가벼운 타박상이 있습니다. 추가 관찰이 필요합니다." },
  "defecation":   { name: "배변",       icon: "🚻", level: "warn",   desc: "평소보다 변이 가볍고 횟수도 적습니다. 수분 섭취 부족과 연관이 있을 수 있어 주의 관찰이 필요합니다." },
};

const FILTERS = [
  { key: "all", label: "전체" },
  { key: "normal", label: "정상" },
  { key: "warn", label: "주의" },
  { key: "danger", label: "이상" },
  { key: "empty", label: "기록없음" },
];

/* ===== 부품 컴포넌트 ===== */

// 상태 핀 (정상 / 주의 / 이상)
function StatusPill({ level = "normal", onPress, full = false }) {
  const labels = { normal: "정상", warn: "주의", danger: "이상" };
  const bgStyles = { normal: styles.pillNormal, warn: styles.pillWarn, danger: styles.pillDanger };
  const txtStyles = { normal: styles.pillTextNormal, warn: styles.pillTextWarn, danger: styles.pillTextDanger };
  const clickable = (level === "danger" || level === "warn") && onPress;

  const Wrap = clickable ? TouchableOpacity : View;
  return (
    <Wrap style={[styles.pill, bgStyles[level], full && styles.pillFull]} onPress={clickable ? onPress : undefined}>
      <Text style={[styles.pillText, txtStyles[level]]}>{labels[level]}</Text>
    </Wrap>
  );
}

// 단일 상태 행 (위생, 상태 안전 등)
function Row({ label, level = "normal", detailKey, onDetailPress, filter }) {
  if (filter && filter !== "all" && filter !== level) return null;
  return (
    <View style={styles.row}>
      <Text style={styles.rowLabel}>{label}</Text>
      <StatusPill level={level} onPress={detailKey ? () => onDetailPress(detailKey) : undefined} full />
    </View>
  );
}

// 메타값 + 상태 행 (배뇨/배변)
function MetaRow({ label, meta, level = "normal", detailKey, onDetailPress, filter }) {
  if (filter && filter !== "all" && filter !== level) return null;
  return (
    <View style={styles.row}>
      <Text style={styles.rowLabel}>{label}</Text>
      <View style={styles.metaRight}>
        <Text style={styles.metaText}>{meta}</Text>
        <StatusPill level={level} onPress={detailKey ? () => onDetailPress(detailKey) : undefined} full />
      </View>
    </View>
  );
}

// 그리드 행 (식사: 아침/점심/저녁)
function GridRow({ label, states, onDetailPress, filter }) {
  const visible = !filter || filter === "all" ? true : states.some(s => s.level === filter);
  if (!visible) return null;
  return (
    <View style={styles.gridRow}>
      <Text style={[styles.rowLabel, styles.gridRowLabel]}>{label}</Text>
      {states.map((s, i) => (
        <View key={i} style={styles.gridCell}>
          <StatusPill level={s.level} onPress={s.key ? () => onDetailPress(s.key) : undefined} />
        </View>
      ))}
    </View>
  );
}

// 섹션 헤더 (번호 + 아이콘 + 제목 + 시간)
function SectionHeader({ num, icon, title, time }) {
  return (
    <View style={styles.sectionHeader}>
      <View style={styles.sectionNum}><Text style={styles.sectionNumText}>{num}</Text></View>
      <Text style={styles.sectionIcon}>{icon}</Text>
      <Text style={styles.sectionTitle}>{title}</Text>
      <Text style={styles.sectionTime}>{time}</Text>
    </View>
  );
}

// 그리드 컬럼 헤더 (아침/점심/저녁)
function GridColumnHeader({ columns }) {
  return (
    <View style={styles.gridColHeader}>
      <Text style={styles.gridColHeaderText}>항목</Text>
      {columns.map(c => (
        <View key={c} style={styles.gridCell}>
          <Text style={styles.gridColHeaderTextCenter}>{c}</Text>
        </View>
      ))}
    </View>
  );
}

// 진행률 원형 차트
function ProgressRing({ percent, size = 78, strokeWidth = 8 }) {
  const r = (size - strokeWidth) / 2;
  const circ = 2 * Math.PI * r;
  const offset = circ * (1 - percent / 100);
  return (
    <View style={[styles.ring, { width: size, height: size }]}>
      <Svg width={size} height={size} style={{ transform: [{ rotate: "-90deg" }] }}>
        <Circle cx={size/2} cy={size/2} r={r} stroke="#FAEEDA" strokeWidth={strokeWidth} fill="none" />
        <Circle cx={size/2} cy={size/2} r={r} stroke="#D85A30" strokeWidth={strokeWidth} fill="none"
          strokeLinecap="round" strokeDasharray={circ} strokeDashoffset={offset} />
      </Svg>
      <View style={styles.ringText}>
        <Text style={styles.ringPercent}>{percent}%</Text>
        <Text style={styles.ringLabel}>완료</Text>
      </View>
    </View>
  );
}

// 오늘 체크 현황 카드
function StatusCard() {
  const stats = [
    { label: "정상", value: SUMMARY.normal, dotStyle: styles.dotNormal },
    { label: "주의", value: SUMMARY.warn,   dotStyle: styles.dotWarn },
    { label: "이상", value: SUMMARY.danger, dotStyle: styles.dotDanger },
  ];
  return (
    <View style={styles.statusCard}>
      <Text style={styles.statusCardTitle}>오늘 체크 현황</Text>
      <Text style={styles.statusCardDate}>{SUMMARY.date}</Text>
      <View style={styles.statusCardBody}>
        <ProgressRing percent={SUMMARY.percent} />
        <View style={styles.statusCardStats}>
          <Text style={styles.statusSummaryText}>
            전체 {SUMMARY.total}개 항목 중 <Text style={styles.statusSummaryBold}>{SUMMARY.done}개 완료</Text>
          </Text>
          <View style={styles.statsRow}>
            {stats.map(s => (
              <View key={s.label} style={styles.statItem}>
                <View style={[styles.dot, s.dotStyle]} />
                <Text style={styles.statText}>{s.label} {s.value}</Text>
              </View>
            ))}
          </View>
        </View>
      </View>
    </View>
  );
}

// 필터 탭
function FilterTabs({ value, onChange }) {
  return (
    <ScrollView horizontal showsHorizontalScrollIndicator={false} contentContainerStyle={styles.filterTabs}>
      {FILTERS.map(f => {
        const active = value === f.key;
        return (
          <TouchableOpacity key={f.key} onPress={() => onChange(f.key)}
            style={[styles.filterTab, active && styles.filterTabActive]}>
            <Text style={[styles.filterTabText, active && styles.filterTabTextActive]}>{f.label}</Text>
          </TouchableOpacity>
        );
      })}
    </ScrollView>
  );
}

// 상세 사유 모달
function DetailModal({ detail, onClose }) {
  return (
    <Modal visible={!!detail} transparent animationType="fade" onRequestClose={onClose}>
      <Pressable style={styles.modalOverlay} onPress={onClose}>
        <Pressable style={styles.modalCard} onPress={() => {}}>
          {detail && (
            <>
              <View style={styles.modalHead}>
                <Text style={styles.modalTitle}>
                  {detail.level === "warn" ? "주의 항목 상세 사유" : "이상 항목 상세 사유"}
                </Text>
                <TouchableOpacity onPress={onClose}>
                  <Text style={styles.modalCloseIcon}>×</Text>
                </TouchableOpacity>
              </View>
              <View style={styles.modalItemRow}>
                <Text style={styles.modalItemIcon}>{detail.icon}</Text>
                <Text style={styles.modalItemName}>{detail.name}</Text>
                <StatusPill level={detail.level} full />
              </View>
              <Text style={styles.modalDesc}>{detail.desc}</Text>
              <TouchableOpacity style={styles.modalConfirm} onPress={onClose}>
                <Text style={styles.modalConfirmText}>확인</Text>
              </TouchableOpacity>
            </>
          )}
        </Pressable>
      </Pressable>
    </Modal>
  );
}

// 하단 네비게이션
function BottomNav() {
  return (
    <View style={styles.bottomNav}>
      <TouchableOpacity style={styles.bottomItem}>
        <Text style={styles.bottomIcon}>🏠</Text>
        <Text style={styles.bottomLabel}>홈</Text>
      </TouchableOpacity>
      <TouchableOpacity style={styles.bottomItem}>
        <Text style={styles.bottomIcon}>📆</Text>
        <Text style={styles.bottomLabel}>달력</Text>
      </TouchableOpacity>
      <TouchableOpacity style={styles.bottomItem}>
        <Text style={styles.bottomIcon}>💵</Text>
        <Text style={styles.bottomLabel}>수납</Text>
      </TouchableOpacity>
      <TouchableOpacity style={styles.bottomItem}>
        <View style={styles.bottomActiveBg}>
          <Text style={styles.bottomIcon}>✅</Text>
        </View>
        <Text style={[styles.bottomLabel, styles.bottomLabelActive]}>실시간</Text>
      </TouchableOpacity>
      <TouchableOpacity style={styles.bottomItem}>
        <Text style={styles.bottomIcon}>💬</Text>
        <Text style={styles.bottomLabel}>챗봇</Text>
      </TouchableOpacity>
    </View>
  );
}

/* ===== 메인 페이지 ===== */
export default function GuardianChecklistPage({ navigation }) {
  const [filter, setFilter] = useState("all");
  const [detailKey, setDetailKey] = useState(null);
  const detail = detailKey ? DETAILS[detailKey] : null;

  return (
    <SafeAreaView style={styles.safeArea}>
      <View style={styles.container}>

        {/* 헤더 */}
        <View style={styles.header}>
          <View style={styles.logoRow}>
            <Text style={styles.logoIcon}>♥</Text>
            <Text style={styles.logoText}>마음돌봄</Text>
          </View>
          <View style={styles.headerIcons}>
            <Text style={styles.iconText}>🔔</Text>
            <Text style={styles.iconText}>👤</Text>
          </View>
        </View>

        {/* 인사말 */}
        <View style={styles.greeting}>
          <Text style={styles.greetingTitle}>
            안녕하세요, 보호자님 <Text style={styles.greetingHeart}>♥</Text>
          </Text>
          <Text style={styles.greetingSub}>오늘 우리 가족의 돌봄 기록을 확인해보세요.</Text>
        </View>

        <ScrollView contentContainerStyle={styles.scrollContent} showsVerticalScrollIndicator={false}>

          <StatusCard />
          <FilterTabs value={filter} onChange={setFilter} />

          {/* 1. 식사 */}
          <View style={styles.card}>
            <SectionHeader num={1} icon="🍴" title="식사" time="7:07 / 12:03 / 14:39" />
            <GridColumnHeader columns={["아침", "점심", "저녁"]} />
            <GridRow label="식사 섭취량" filter={filter} onDetailPress={setDetailKey}
              states={[{ level: "normal" }, { level: "normal" }, { level: "danger", key: "meal-dinner" }]} />
            <GridRow label="수분 섭취량" filter={filter} onDetailPress={setDetailKey}
              states={[{ level: "normal" }, { level: "normal" }, { level: "danger", key: "water-dinner" }]} />
            <GridRow label="사례 여부" filter={filter} onDetailPress={setDetailKey}
              states={[{ level: "normal" }, { level: "normal" }, { level: "normal" }]} />
          </View>

          {/* 2. 위생·청결 */}
          <View style={styles.card}>
            <SectionHeader num={2} icon="🧼" title="위생·청결" time="14:20" />
            <Row label="구강 청결도" filter={filter} />
            <Row label="환자 물품 정리" filter={filter} />
            <Row label="목욕 여부" filter={filter} />
          </View>

          {/* 3. 상태 안전 */}
          <View style={styles.card}>
            <SectionHeader num={3} icon="🛡️" title="상태 안전" time="상시 기록" />
            <Row label="부종 양상" level="danger" detailKey="edema" filter={filter} onDetailPress={setDetailKey} />
            <Row label="통증 유무" filter={filter} />
            <Row label="낙상 유무" level="danger" detailKey="fall" filter={filter} onDetailPress={setDetailKey} />
          </View>

          {/* 4. 배뇨 및 배변 */}
          <View style={styles.card}>
            <SectionHeader num={4} icon="👣" title="배뇨 및 배변" time="상시 기록" />
            <MetaRow label="배뇨" meta="5회" level="normal" filter={filter} />
            <MetaRow label="배변" meta="1회 · 가벼움" level="warn" detailKey="defecation"
              filter={filter} onDetailPress={setDetailKey} />
          </View>

        </ScrollView>

        <BottomNav />
        <DetailModal detail={detail} onClose={() => setDetailKey(null)} />

      </View>
    </SafeAreaView>
  );
}