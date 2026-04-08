import { useEffect, useMemo, useState } from "react"; //useMemo: 계산결과 기억하기
import PatientSearchBar from "../../components/patient/PatientSearchBar";
import PatientTable from "../../components/patient/PatientTable";
// import PatientFormModal from "../../components/patient/PatientFormModal";
import { useNavigate } from "react-router-dom";
import patientApi from "../../api/patientApi";


export default function PatientListPage() {
  const [patients, setPatients] = useState([]);
  // useState(initialPatients) -> useState([])
  const [keyword, setKeyword] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    fetchPatients();
  }, []);

  const fetchPatients = async () => {
    try {
      const response = await patientApi.getPatients();

      console.log("status:", response.status);
      console.log("data:", response.data);
      console.log("isArray:", Array.isArray(response.data));

      setPatients(Array.isArray(response.data) ? response.data : []);
    } catch (error) {
      console.error("환자 목록 조회 실패", error);
      console.log("error response:", error.response);
      setPatients([]);
    }
  };


  const filteredPatients = useMemo(() => {
    const lowerKeyword = keyword.toLowerCase();

    return (Array.isArray(patients) ? patients : []).filter((patient) => {
      return (
        patient.name?.toLowerCase().includes(lowerKeyword) ||
        String(patient.patientId).includes(lowerKeyword)
      );
    });
  }, [patients, keyword]);

  const handleGoCreatePage = () => {
    navigate("/patients/new");
  };

  const handleGoDetailPage = (patientId) => {
    navigate(`/patients/${patientId}`);
  };

  return (
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

      <PatientSearchBar keyword={keyword} setKeyword={setKeyword} />

      <div className="mt-6">
        <PatientTable
          patients={filteredPatients}
          onRowClick={handleGoDetailPage}
        />
      </div>
    </div>
  );
}
