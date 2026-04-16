import { useState } from "react";
import * as XLSX from "xlsx";
import { useNavigate } from "react-router-dom";
import mealApi from "../api/mealApi";

function MealExcelUploader() {
  const [mealData, setMealData] = useState([]);
  const [dragOver, setDragOver] = useState(false);
  const [saving, setSaving] = useState(false);
  const [statusMessage, setStatusMessage] = useState("");
  const [saveSuccess, setSaveSuccess] = useState(false);
  const navigate = useNavigate();

  const currentUser = JSON.parse(localStorage.getItem("user") || "{}");
  const parsedAdminId = Number(currentUser.id);
  const parsedFacilityId = Number(currentUser.facilityId);
  const adminId = Number.isFinite(parsedAdminId) && parsedAdminId > 0 ? parsedAdminId : 1;
  const facilityId = Number.isFinite(parsedFacilityId) && parsedFacilityId > 0 ? parsedFacilityId : 1;

  // 📌 파일 업로드
  const handleFileUpload = (file) => {
    if (!file) return;

    const reader = new FileReader();

    reader.onload = (event) => {
      const binary = event.target.result;
      const workbook = XLSX.read(binary, { type: "binary" });

      const sheet = workbook.Sheets[workbook.SheetNames[0]];
      const rows = XLSX.utils.sheet_to_json(sheet, {
        header: 1,
        raw: true,
        defval: "",
      });

      const parsed = parseMealSheet(rows);
      setMealData(parsed);
    };

    reader.readAsBinaryString(file);
  };

  //  드롭
  const onDrop = (e) => {
    e.preventDefault();
    setDragOver(false);
    handleFileUpload(e.dataTransfer.files?.[0]);
  };

  // "384/14" 형식 파싱 → { calorie: 384, protein: 14 }
  const parseNutrientString = (value) => {
    if (!value) return { calorie: 0, protein: 0 };
    const str = String(value).trim();
    const match = str.match(/(\d+(?:\.\d+)?)\s*\/\s*(\d+(?:\.\d+)?)/);
    if (match) {
      return {
        calorie: Math.round(Number(match[1])),
        protein: Math.round(Number(match[2])),
      };
    }
    return { calorie: 0, protein: 0 };
  };

  //  핵심 파싱
  const parseMealSheet = (rows) => {
    const temp = [];

    for (let r = 0; r < rows.length; r++) {
      const row = rows[r] || [];

      for (let c = 0; c < row.length; c++) {
        const cell = row[c];

        if (!isValidDate(cell)) continue;

        // 아침/점심/저녁 영양값: dateRow+7, +14, +21
        const bf = parseNutrientString(rows[r + 7]?.[c]);
        const lu = parseNutrientString(rows[r + 14]?.[c]);
        const di = parseNutrientString(rows[r + 21]?.[c]);

        temp.push({
          date: normalizeDate(cell),
          breakfast: collect(rows, r + 1, r + 6, c),
          lunch: collect(rows, r + 8, r + 13, c),
          dinner: collect(rows, r + 15, r + 20, c),
          breakfastCalorie: bf.calorie,
          breakfastProtein: bf.protein,
          lunchCalorie: lu.calorie,
          lunchProtein: lu.protein,
          dinnerCalorie: di.calorie,
          dinnerProtein: di.protein,
        });
      }
    }

    // 중복 날짜 병합
    const merged = {};

    temp.forEach((item) => {
      if (!merged[item.date]) {
        merged[item.date] = { ...item, breakfast: [...item.breakfast], lunch: [...item.lunch], dinner: [...item.dinner] };
      } else {
        const m = merged[item.date];
        m.breakfast.push(...item.breakfast);
        m.lunch.push(...item.lunch);
        m.dinner.push(...item.dinner);
        if (!m.breakfastCalorie && item.breakfastCalorie) { m.breakfastCalorie = item.breakfastCalorie; m.breakfastProtein = item.breakfastProtein; }
        if (!m.lunchCalorie && item.lunchCalorie) { m.lunchCalorie = item.lunchCalorie; m.lunchProtein = item.lunchProtein; }
        if (!m.dinnerCalorie && item.dinnerCalorie) { m.dinnerCalorie = item.dinnerCalorie; m.dinnerProtein = item.dinnerProtein; }
      }
    });

    return Object.values(merged);
  };

  // 📌 날짜 판별
  const isValidDate = (cell) => {
    if (!cell) return false;

    if (typeof cell === "number" && cell > 40000) return true;

    if (typeof cell === "string") {
      return /\d{4}[-./]\d{1,2}[-./]\d{1,2}/.test(cell);
    }

    return false;
  };

  // 📌 날짜 변환
  const normalizeDate = (value) => {
    if (typeof value === "number") {
      const d = new Date((value - 25569) * 86400 * 1000);
      return d.toISOString().slice(0, 10);
    }

    if (typeof value === "string") {
      return value.replace(/[./]/g, "-").replace(/\s/g, "");
    }

    return "";
  };

  // 📌 메뉴 수집
  const collect = (rows, start, end, col) => {
    const arr = [];

    for (let i = start; i <= end; i++) {
      const v = rows[i]?.[col];
      if (v) arr.push(String(v).trim());
    }

    return arr;
  };

  // 📌 초기화
  const handleReset = () => {
    setMealData([]);
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 p-10">
      <div className="max-w-6xl mx-auto">

        {/* 헤더 */}
        <div className="mb-8">
          <h1 className="text-4xl font-extrabold text-gray-800">
            식단 업로드
          </h1>
          <p className="text-gray-500 mt-2">
            엑셀 파일을 업로드하면 자동으로 식단이 정리됩니다
          </p>
        </div>

        {/* 업로드 */}
        <div
          onDragOver={(e) => {
            e.preventDefault();
            setDragOver(true);
          }}
          onDragLeave={() => setDragOver(false)}
          onDrop={onDrop}
          className={`rounded-2xl border-2 border-dashed p-12 text-center shadow-lg transition
          ${dragOver
              ? "border-blue-500 bg-blue-50 scale-105"
              : "border-gray-300 bg-white"
            }`}
        >
          <div className="text-5xl mb-4">📂</div>
          <p className="text-xl font-semibold text-gray-700">
            파일을 드래그하거나 클릭하세요
          </p>
          <p className="text-sm text-gray-400 mt-1">
            Excel (.xlsx) 파일만 지원
          </p>

          <input
            type="file"
            accept=".xlsx,.xls"
            onChange={(e) => handleFileUpload(e.target.files[0])}
            className="mt-6"
          />
        </div>

        {/* 결과 */}
        {mealData.length > 0 && (
          <div className="mt-10 bg-white rounded-2xl shadow-lg p-6">

            <div className="flex justify-between items-center mb-4">
              <h2 className="text-xl font-bold text-gray-800">
                📅 식단 미리보기
              </h2>
              <span className="text-sm text-gray-500">
                총 {mealData.length}일
              </span>
            </div>

            <div className="overflow-x-auto rounded-lg">
              <table className="w-full text-sm">
                <thead className="bg-indigo-50 text-indigo-700">
                  <tr>
                    <th className="p-3 text-left">날짜</th>
                    <th className="p-3">아침</th>
                    <th className="p-3 text-orange-600">아침 열량/단백질</th>
                    <th className="p-3">점심</th>
                    <th className="p-3 text-orange-600">점심 열량/단백질</th>
                    <th className="p-3">저녁</th>
                    <th className="p-3 text-orange-600">저녁 열량/단백질</th>
                  </tr>
                </thead>

                <tbody>
                  {mealData.map((day, i) => (
                    <tr key={i} className="border-b hover:bg-gray-50">
                      <td className="p-3 font-semibold">{day.date}</td>
                      <td className="p-3 text-center">{day.breakfast.join(", ")}</td>
                      <td className="p-3 text-center text-orange-600 font-medium">{day.breakfastCalorie}kcal / {day.breakfastProtein}g</td>
                      <td className="p-3 text-center">{day.lunch.join(", ")}</td>
                      <td className="p-3 text-center text-orange-600 font-medium">{day.lunchCalorie}kcal / {day.lunchProtein}g</td>
                      <td className="p-3 text-center">{day.dinner.join(", ")}</td>
                      <td className="p-3 text-center text-orange-600 font-medium">{day.dinnerCalorie}kcal / {day.dinnerProtein}g</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>

            {/* 버튼 */}
            <div className="mt-6 flex justify-end gap-3">
              <button
                onClick={handleReset}
                className="px-4 py-2 rounded-lg bg-gray-200 hover:bg-gray-300"
              >
                초기화
              </button>

              <button
                onClick={async () => {
                  const payload = mealData.flatMap((day) => {
                    const date = day.date;
                    const rows = [];
                    const mealTypes = [
                      { type: "BREAKFAST", items: day.breakfast },
                      { type: "LUNCH", items: day.lunch },
                      { type: "DINNER", items: day.dinner },
                    ];

                    mealTypes.forEach(({ type, items }) => {
                      const menu = items.filter(Boolean).map(String).join(", ");
                      if (menu) {
                        const calorie = type === "BREAKFAST" ? day.breakfastCalorie : type === "LUNCH" ? day.lunchCalorie : day.dinnerCalorie;
                        const protein = type === "BREAKFAST" ? day.breakfastProtein : type === "LUNCH" ? day.lunchProtein : day.dinnerProtein;
                        rows.push({
                          mealDate: date,
                          mealType: type,
                          dietType: "GENERAL",
                          menu,
                          calorie: calorie ?? 0,
                          protein: protein ?? 0,
                        });
                      }
                    });

                    return rows;
                  });

                  if (!payload.length) {
                    setStatusMessage("저장할 식단 데이터가 없습니다.");
                    return;
                  }

                  setSaving(true);
                  setStatusMessage("");

                  try {
                    if (!Number.isFinite(adminId) || adminId <= 0) {
                      setStatusMessage("❌ 사용자 정보가 올바르지 않습니다. 다시 로그인 후 시도해주세요.");
                      setSaveSuccess(false);
                      return;
                    }

                    await mealApi.bulkUpsertMeals({ facilityId, adminId, rows: payload });
                    setMealData([]);
                    setSaveSuccess(true);
                    setStatusMessage("✅ 식단을 정상적으로 저장했습니다!");
                    
                    // ✅ 3초 후 캘린더로 자동 이동
                    setTimeout(() => {
                      navigate("/calendar");
                    }, 3000);
                  } catch (error) {
                    console.error(error);
                    const serverMessage = error?.response?.data?.message;
                    const status = error?.response?.status;
                    if (serverMessage) {
                      setStatusMessage(`❌ 저장 실패 (${status ?? "오류"}): ${serverMessage}`);
                    } else {
                      setStatusMessage("❌ 식단 저장 중 오류가 발생했습니다. 다시 시도해주세요.");
                    }
                    setSaveSuccess(false);
                  } finally {
                    setSaving(false);
                  }
                }}
                disabled={saving}
                className="px-5 py-2 rounded-lg bg-indigo-600 text-white hover:bg-indigo-700 shadow disabled:cursor-not-allowed disabled:opacity-60"
              >
                {saving ? "저장 중..." : "저장"}
              </button>
            </div>
            {statusMessage && (
              <div className={`mt-4 p-3 rounded-lg flex items-center justify-between ${
                saveSuccess 
                  ? "bg-green-50 border border-green-200" 
                  : "bg-red-50 border border-red-200"
              }`}>
                <p className={`text-sm ${saveSuccess ? "text-green-700" : "text-red-700"}`}>
                  {statusMessage}
                </p>
                {saveSuccess && (
                  <button
                    onClick={() => navigate("/calendar")}
                    className="ml-3 px-3 py-1 rounded bg-green-600 text-white text-sm hover:bg-green-700 whitespace-nowrap"
                  >
                    지금 이동
                  </button>
                )}
              </div>
            )}

          </div>
        )}
      </div>
    </div>
  );
}

export default MealExcelUploader;