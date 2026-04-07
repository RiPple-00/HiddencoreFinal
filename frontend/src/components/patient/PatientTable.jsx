const getStatusLabel = (status) => {
  switch (status) {
    case "STABLE":
      return "안정";
    case "DISCHARGE_SOON":
      return "퇴원예정";
    case "FOCUSED_CARE":
      return "집중관찰";
    case "DANGER":
      return "주의";
    case "DISCHARGED":
      return "퇴원";
    default:
      return "-";
  }
};

const getStatusClassName = (status) => {
  switch (status) {
    case "STABLE":
      return "bg-green-100 text-green-700";
    case "DISCHARGE_SOON":
      return "bg-blue-100 text-blue-700";
    case "FOCUSED_CARE":
      return "bg-orange-100 text-orange-700";
    case "DANGER":
      return "bg-red-100 text-red-600";
    case "DISCHARGED":
      return "bg-gray-400 text-white";
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
                onClick={() => onRowClick(patient.patientId)}
                className="cursor-pointer border-t border-slate-100 hover:bg-slate-50"
              >
                <td className="px-8 py-7 font-mono text-[18px] text-slate-500 text-center">
                  {patient.patientId}
                </td>
                <td className="px-8 py-7 text-[18px] font-semibold text-slate-800 text-center">
                  {patient.name}
                </td>
                <td className="px-8 py-7 text-[18px] text-slate-700 text-center ">
                  {patient.gender} / {patient.age} {/*나이 DTO 에서 계산해서 가져올꺼임*/}
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