import { useEffect, useMemo, useState } from "react";
import { Link } from "react-router-dom";
import mealApi from "../api/mealApi";

const tabs = ["아침", "점심", "저녁"];

const mealTypeMap = {
  아침: "BREAKFAST",
  점심: "LUNCH",
  저녁: "DINNER",
};

const defaultMealData = {
  아침: {
    name: "등록된 식단 없음",
    items: "오늘 등록된 아침 식단이 없습니다.",
    special: false,
  },
  점심: {
    name: "등록된 식단 없음",
    items: "오늘 등록된 점심 식단이 없습니다.",
    special: false,
  },
  저녁: {
    name: "등록된 식단 없음",
    items: "오늘 등록된 저녁 식단이 없습니다.",
    special: false,
  },
};

const formatToday = () => {
  const today = new Date();
  const year = today.getFullYear();
  const month = String(today.getMonth() + 1).padStart(2, "0");
  const day = String(today.getDate()).padStart(2, "0");

  return `${year}-${month}-${day}`;
};

const splitMenuItems = (menu) => {
  if (!menu) return [];

  return String(menu)
    .split(/,|\r?\n/)
    .map((item) => item.trim())
    .filter(Boolean);
};

export default function MealCardPage() {
  const [activeTab, setActiveTab] = useState("점심");
  const [mealData, setMealData] = useState(defaultMealData);
  const [loading, setLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState("");

  const todayKey = useMemo(() => formatToday(), []);

  useEffect(() => {
    const loadMeals = async () => {
      setLoading(true);
      setErrorMessage("");

      try {
        const response = await mealApi.getMealsByDate(todayKey);
        const rows = response.data ?? [];

        const nextMealData = { ...defaultMealData };

        tabs.forEach((tab) => {
          const mealRow = rows.find((row) => row.mealType === mealTypeMap[tab]);
          const menuItems = splitMenuItems(mealRow?.menu);

          if (menuItems.length > 0) {
            nextMealData[tab] = {
              name: menuItems[0],
              items: menuItems.join(", "),
              special: false,
            };
          }
        });

        setMealData(nextMealData);
      } catch (error) {
        console.error("식단 조회 실패:", error);
        setMealData(defaultMealData);
        setErrorMessage("식단을 불러오지 못했습니다. 엑셀 업로드 후 다시 확인하세요.");
      } finally {
        setLoading(false);
      }
    };

    loadMeals();
  }, [todayKey]);

  const meal = mealData[activeTab];

  return (
    <div style={{ padding: "40px", fontFamily: "'Noto Sans KR', sans-serif" }}>
      <div style={{
        background: "#fff", borderRadius: 20, padding: "28px",
        maxWidth: 400, boxShadow: "0 2px 12px rgba(0,0,0,0.06)"
      }}>
        {/* 탭 */}
        <div style={{ display: "flex", gap: 20, marginBottom: 20 }}>
          {tabs.map(tab => (
            <button key={tab} onClick={() => setActiveTab(tab)} style={{
              background: "none", border: "none",
              borderBottom: activeTab === tab ? "2px solid #1a9e75" : "2px solid transparent",
              color: activeTab === tab ? "#1a9e75" : "#94a3b8",
              fontWeight: activeTab === tab ? 700 : 400,
              fontSize: 15, cursor: "pointer", paddingBottom: 4,
              fontFamily: "inherit", transition: "all 0.15s"
            }}>{tab}</button>
          ))}
        </div>

        <p style={{ fontSize: 12, color: "#94a3b8", margin: "0 0 12px" }}>
          {todayKey} 식단
        </p>

        {/* 메뉴 이름 */}
        <h2 style={{ fontSize: 22, fontWeight: 800, color: "#1e293b", margin: "0 0 10px" }}>
          <Link
            to="/calendar"
            style={{
              color: "inherit",
              textDecoration: "none",
              cursor: "pointer",
              display: "inline-block",
              width: "100%",
            }}
            title="캘린더로 이동"
          >
            {loading ? "식단 불러오는 중..." : meal.name}
          </Link>
        </h2>

        {/* 구성 */}
        <p style={{ fontSize: 14, color: "#64748b", lineHeight: 1.7, margin: "0 0 16px", whiteSpace: "pre-line" }}>
          {loading ? "업로드된 식단 정보를 확인하고 있습니다." : meal.items}
        </p>

        {errorMessage && (
          <p style={{ fontSize: 13, color: "#dc2626", margin: 0 }}>
            {errorMessage}
          </p>
        )}
          
        
      </div>
    </div>
  );
}