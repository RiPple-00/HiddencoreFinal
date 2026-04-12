import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import CalendarHeader from "../components/page/CalendarHeader";
import CalendarGrid from "../components/page/CalendarGrid";
import mealApi from "../api/mealApi";

function CalendarPage() {
  const today = new Date();
  const [year, setYear] = useState(today.getFullYear());
  const [month, setMonth] = useState(today.getMonth() + 1);
  const [filter, setFilter] = useState("ALL");
  const [selectedDate, setSelectedDate] = useState("");
  const [schedules, setSchedules] = useState([]);
  const navigate = useNavigate();

  const handleDateClick = (dateString) => {
    setSelectedDate(dateString);
    navigate(`/meal-type/${dateString}`, { state: { selectedDate: dateString } });
  };

  const handlePrevMonth = () => {
    if (month === 1) {
      setYear((prev) => prev - 1);
      setMonth(12);
      return;
    }
    setMonth((prev) => prev - 1);
  };

  const handleNextMonth = () => {
    if (month === 12) {
      setYear((prev) => prev + 1);
      setMonth(1);
      return;
    }
    setMonth((prev) => prev + 1);
  };

  useEffect(() => {
    const startDate = `${year}-${String(month).padStart(2, "0")}-01`;
    const endDay = new Date(year, month, 0).getDate();
    const endDate = `${year}-${String(month).padStart(2, "0")}-${String(endDay).padStart(2, "0")}`;

    mealApi
      .getMealsByRange(startDate, endDate)
      .then((response) => {
        const mapped = response.data.map((meal) => {
          const typeLabel = meal.mealType === "BREAKFAST"
            ? "아침"
            : meal.mealType === "LUNCH"
              ? "점심"
              : meal.mealType === "DINNER"
                ? "저녁"
                : meal.mealType;

          // ✅ 더 짧은 텍스트 표시 (첫 번째 메뉴만, 최대 15자)
          const menuPreview = meal.menu
            ? meal.menu.split(",")[0].trim().substring(0, 15)
            : "식단";

          const title = `${typeLabel} ${menuPreview}`;

          return {
            scheduleId: meal.mealPlanId ?? `${meal.mealDate}-${meal.mealType}-${meal.dietType}`,
            title,
            scheduledAt: meal.mealDate,
            type: "PERSONAL",
          };
        });

        setSchedules(mapped);
      })
      .catch((error) => {
        console.error("캘린더 식단 조회 실패", error);
        setSchedules([]);
      });
  }, [year, month]);

  return (
    <div className="w-full mx-auto">
      <div className="bg-white rounded-3xl p-6 shadow-sm border border-gray-200">
        <div className="mb-6">
          <div className="flex items-center justify-between gap-3">
            <div>
              <h1 className="text-2xl font-bold text-slate-900">캘린더</h1>
              <p className="mt-2 text-sm text-slate-600">
                식단 계획을 확인하거나 날짜를 선택해보세요.
              </p>
            </div>
            <Link
              to="/meal-upload"
              className="rounded-lg bg-blue-600 px-4 py-2 text-sm font-semibold text-white hover:bg-blue-700"
            >
              식단 등록
            </Link>
          </div>
        </div>

        <CalendarHeader
          year={year}
          month={month}
          filter={filter}
          onPrevMonth={handlePrevMonth}
          onNextMonth={handleNextMonth}
          onFilterChange={setFilter}
        />

        <div className="mt-6">
          <CalendarGrid
            year={year}
            month={month}
            schedules={schedules}
            selectedDate={selectedDate}
            onDateClick={handleDateClick}
          />
        </div>
      </div>
    </div>
  );
}

export default CalendarPage;
