import { useEffect, useMemo, useState } from "react"; //useMemo: 계산결과 기억하기

import PatientTable from "../../components/patient/PatientTable";
import TopNavBar from "../../components/bedroom/TopNavBar";
import { useNavigate } from "react-router-dom";
import patientApi from "../../api/patientApi";

export default function PatientListPage() {
  const [patients, setPatients] = useState([]);

  const [keyword, setKeyword] = useState(""); //검색 할떄 쓰는거
  const navigate = useNavigate();
  const [statusFilter, setStatusFilter] = useState(""); //그 상태 필터
  const [quickFilter, setQuickFilter] = useState(""); // 미배정만 쓸때

  useEffect(() => {
    fetchPatients();
  }, []);

  const fetchPatients = async () => {
    try {
      const response = await patientApi.getPatients();

      setPatients(Array.isArray(response.data) ? response.data : []);
    } catch (error) {
      console.error("환자 목록 조회 실패", error);
      console.log("error response:", error.response);
      setPatients([]);
    }
  };


  const summary = useMemo(() => { //상단에 요약 카드 계산 로직
    const patientList = Array.isArray(patients) ? patients : [];

    return {
      admittedCount: patientList.length,

      dischargeSoonCount: patientList.filter(
        (patient) => patient.patientStatus === "DISCHARGE"
      ).length,

      unassignedCount: patientList.filter(
        (patient) => (!patient.building || !patient.room)
      ).length
    };
  }, [patients]);

  const filteredPatients = useMemo(() => {
    const lowerKeyword = keyword.toLowerCase(); //id랑 이름 검색창 로직

    return (Array.isArray(patients) ? patients : []).filter((patient) => {
      const matchesKeyword =
        patient.name?.toLowerCase().includes(lowerKeyword) ||
        String(patient.patientId).includes(lowerKeyword);

      const matchesStatus =
        statusFilter === "" || patient.patientStatus === statusFilter; //이거는 상태 필터링

      const matchesQuickFilter =
        quickFilter === "" ||
        (quickFilter === "UNASSIGNED" && (!patient.building || !patient.room));

      return matchesKeyword && matchesStatus && matchesQuickFilter; //이거는 둘다 매치 됐을때
    });
  }, [patients, keyword, statusFilter, quickFilter]);

  const handleGoCreatePage = () => {
    navigate("/patients/new");
  };

  return (
    <>
      <TopNavBar activeNav="patients" />
      <div className="min-h-screen bg-slate-50">
        <div className="mx-auto max-w-[1440px] px-6 py-6">
      {/* 맨위 제목이랑 환자등록 버튼 */}
      <div className="rounded-[28px] border border-slate-200 bg-white p-8 shadow-sm">
        <div className="mb-8 flex items-center justify-between">
          <h1 className="text-3xl font-bold text-slate-900">환자 조회</h1>
          <button
            onClick={handleGoCreatePage}
            className="rounded-xl bg-blue-600 px-5 py-3 text-sm font-semibold text-white hover:bg-blue-700"
          >
            신규 환자 등록
          </button>
        </div>

        {/* 환자 수 카드 세걔 */}
        <div className="mb-6 grid gap-4 md:grid-cols-3">
          <div className="rounded-2xl border border-slate-200 bg-slate-50 px-6 py-5">
            <p className="text-sm font-medium text-slate-500">현재 입원 환자</p>
            <p className="mt-3 text-3xl font-bold text-slate-900">
              {summary.admittedCount}
            </p>
          </div>

          <div className="rounded-2xl border border-slate-200 bg-slate-50 px-6 py-5">
            <p className="text-sm font-medium text-slate-500">금일 퇴원 예정</p>
            <p className="mt-3 text-3xl font-bold text-slate-900">
              {summary.dischargeSoonCount}
            </p>
          </div>

          <div
            onClick={() =>
              setQuickFilter((prev) => (prev === "UNASSIGNED" ? "" : "UNASSIGNED"))
            }
            className={`cursor-pointer rounded-2xl border px-6 py-5 ${quickFilter === "UNASSIGNED"
                ? "border-red-400 bg-red-50"
                : "border-slate-200 bg-slate-50"
              }`}
          >
            <p className="text-sm font-medium text-slate-500">병실 미배정 환자</p>
            <p className="mt-3 text-3xl font-bold text-slate-900">
              {summary.unassignedCount}
            </p>
          </div>
        </div>

        {/* 검색창, 드롭다운 상태 체크 */}
        <div className="mb-6 flex gap-3">
          <input
            type="text"
            value={keyword}
            onChange={(e) => setKeyword(e.target.value)}
            placeholder="환자 이름, ID 검색"
            className="flex-1 rounded-xl border border-slate-200 px-4 py-3 text-sm outline-none focus:border-blue-500"
          />

          <select
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value)}
            className="w-48 rounded-xl border border-slate-200 bg-white px-4 py-3 text-sm text-slate-700 outline-none focus:border-blue-500"
          >
            <option value="">전체 상태</option>
            <option value="STABLE">안정</option>
            <option value="MONITORING">집중관찰</option>
            <option value="DISCHARGE">퇴원예정</option>
            <option value="POSTOPERATIVE">수술후</option>
            <option value="CRITICAL">위험</option>
            <option value="DISCHARGED">퇴원완료</option>
          </select>
        </div>

        <div className="mt-6 gap-6 xl:grid-cols-[minmax(0,1fr)_340px]">
          <div className="space-y-6">
            <div className="flex justify-center">
              <div className="w-full max-w-[1120px]">
                <PatientTable patients={filteredPatients} />
              </div>
            </div>
          </div>


        </div>

      </div>
        </div>
      </div>

    </>
  );
}

