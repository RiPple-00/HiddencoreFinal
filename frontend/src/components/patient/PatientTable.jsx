const genderLabel = (g) => {
  if (g == null) return "-";
  if (typeof g !== "string") return String(g);
  if (g === "MALE") return "남";
  if (g === "FEMALE") return "여";
  if (g === "OTHER") return "기타";
  return g;
};

/** Patient 도메인 PatientStatus enum 과 동일 */
const getStatusLabel = (status) => {
  switch (status) {
    case "STABLE":
      return "안정";
    case "MONITORING":
      return "집중관찰";
    case "DISCHARGE":
      return "퇴원예정";
    case "POSTOPERATIVE":
      return "수술후";
    case "CRITICAL":
      return "위험";
    case "DISCHARGED":
      return "퇴원완료";
    default:
      return "-";
  }
};

const getStatusClassName = (status) => {
  switch (status) {
    case "STABLE":
      return "bg-green-100 text-green-700";
    case "MONITORING":
      return "bg-orange-100 text-orange-700";
    case "DISCHARGE":
      return "bg-blue-100 text-blue-700";
    case "POSTOPERATIVE":
      return "bg-purple-100 text-purple-700";
    case "CRITICAL":
      return "bg-red-100 text-red-600";
    case "DISCHARGED":
      return "bg-slate-200 text-slate-700";
    default:
      return "bg-slate-100 text-slate-500";
  }
};

export default function PatientTable({ patients, onRowClick }) {
  return (
    <div className="overflow-hidden rounded-2xl border border-slate-200">
      <table className="w-full border-collapse bg-white">
        <thead className="bg-slate-100 text-sm font-semibold text-slate-500">
          <tr>
            <th className="px-8 py-5 text-center">환자 ID</th>
            <th className="px-8 py-5 text-center">성명</th>
            <th className="px-8 py-5 text-center">성별/나이</th>
            <th className="px-8 py-5 text-center">병동/병실</th>
            <th className="px-8 py-5 text-center">생년월일</th>
            <th className="px-8 py-5 text-center">상태</th>
          </tr>
        </thead>

        <tbody>
          {patients.length === 0 ? (
            <tr>
              <td colSpan={6} className="px-8 py-12 text-center text-sm text-slate-400">
                검색 결과가 없습니다.
              </td>
            </tr>
          ) : (
            patients.map((patient) => (
              
              <tr
                key={patient.patientId}
                onClick={() => onRowClick?.(patient.patientId)}
                className={`border-t border-slate-100 ${onRowClick ? "cursor-pointer hover:bg-slate-50" : ""}`}
              >
                <td className="px-8 py-7 font-mono text-[18px] text-slate-500 text-center">
                  {patient.patientId}
                </td>
                <td className="px-8 py-7 text-[18px] font-semibold text-slate-800 text-center">
                  {patient.name}
                </td>
                <td className="px-8 py-7 text-[18px] text-slate-700 text-center ">
                  {genderLabel(patient.gender)} / {patient.age}
                </td>
                <td className="px-8 py-7 text-[18px] text-slate-700 text-center">
                  {patient.building || ""} - {patient.room || ""}
                </td>
                <td className="px-8 py-7 text-[18px] font-semibold text-slate-800 text-center">
                  {patient.birthDate}
                </td>
                <td className="px-8 py-7 text-center">
                  <span
                    className={`inline-flex rounded-full px-4 py-2 text-sm font-semibold ${getStatusClassName(
                      patient.patientStatus
                    )}`}
                  >
                    {getStatusLabel(patient.patientStatus)}
                  </span>
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
}