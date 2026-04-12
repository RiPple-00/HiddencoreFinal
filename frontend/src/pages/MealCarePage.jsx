import { useState } from "react";
import { Link } from "react-router-dom";

const tabs = ["아침", "점심", "저녁"];

const mealData = {
  아침: {
    name: "오트밀 & 제철 과일",
    items: "오트밀, 바나나, 블루베리, 삶은달걀, 두유",
    special: false,
  },
  점심: {
    name: "영양 한우 불고기 & 쌈채소",
    items: "흑미밥, 소고기무국, 고등어구이,\n시금치나물, 포기김치, 과일샐러드",
    special: false,
  },
  저녁: {
    name: "닭가슴살 & 채소구이",
    items: "잡곡밥, 닭가슴살구이, 된장국,\n브로콜리볶음, 깍두기, 요거트",
    special: false,
  },
};

export default function MealCardPage() {
  const [activeTab, setActiveTab] = useState("점심");

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
            {meal.name}
          </Link>
        </h2>

        {/* 구성 */}
        <p style={{ fontSize: 14, color: "#64748b", lineHeight: 1.7, margin: "0 0 16px", whiteSpace: "pre-line" }}>
          {meal.items}
        </p>
          
        
      </div>
    </div>
  );
}