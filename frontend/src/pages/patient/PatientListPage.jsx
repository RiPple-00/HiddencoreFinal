import { useMemo, useState } from "react"; //useMemo: 계산결과 기억하기
import PatientSearchBar from "../../components/patient/PatientSearchBar";
import PatientTable from "../../components/patient/PatientTable";
import PatientFormModal from "../../components/patient/PatientFormModal";
import { useNavigate } from "react-router-dom";

const initialPatients = [
  //임시 테스트 나주엥 지움
  {
    patientId: "2024001",
    name: "박서준",
    gender: "남",
    age: 45,
    building: "A",
    room: "301",
    birthDate: "2026-01-01",
    patientStatus: "STABLE",
  },

  {
    patientId: "2024002",
    name: "김미나",
    gender: "여",
    age: 32,
     building: "B",
    room: "301",
    birthDate: "2026-01-03",
    patientStatus: "DISCHARGE_SOON",
  },

  {
    patientId: "2024002",
    name: "홍길동",
    gender: "여",
    age: 55,
     building: "",
    room: "",
    birthDate: "2026-01-03",
    patientStatus: "DISCHARGE",
  },
];

export default function PatientListPage() {
  const [patients, setPatients] = useState(initialPatients);
  // useState(initialPatients) -> useState([])
  const [keyword, setKeyword] = useState("");
  const navigate = useNavigate();
  /*
  useEffect(() => {
  fetchPatients();
}, []);

const fetchPatients = async () => {
  try {
    const response = await patientApi.getPatients();
    setPatients(response.data.content || response.data);
  } catch (error) {
    console.error("환자 목록 조회 실패", error);
  }
};
*/

  const filteredPatients = useMemo(() => {
    //검색부분
    const lowerKeyword = keyword.toLowerCase(); //영어일때 다 소문자로 변환

    return patients.filter((patient) => {
      return (
        patient.name.toLowerCase().includes(lowerKeyword) ||
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
