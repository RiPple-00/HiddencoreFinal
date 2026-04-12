import { Routes, Route, Link } from "react-router-dom";
import HomePage from "./pages/HomePage";
import SignUpPage from "./pages/SignUpPage";
import LoginPage from "./pages/LoginPage";
import PatientListPage from "./pages/patient/PatientListPage"
import PatientDetailPage from './pages/patient/PatientDetailPage'
import PatientCreatePage from './pages/patient/PatientCreatePage'


function App() {
  return (
    <div className="min-h-screen bg-orange-50">
      {/* 헤더 */}
      <header className="bg-gradient-to-r from-orange-100 to-amber-100 border-b-2 border-orange-200 shadow-sm">
        <div className="max-w-7xl mx-auto px-4 py-4">
          <nav className="flex items-center justify-between">
            {/* 로고  Link -> <a> */}
            <Link to="/" className="text-2xl font-bold text-orange-700">
              따숨🏥
            </Link>
            {/* 메뉴 */}
            <div className="flex items-center gap-6">
              <Link
                to="/login"
                className="text-gray-600 hover:text-blue-600 transition"
              >
                로그인
              </Link>
              <Link
                to="/signup"
                className="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 transition"
              >
                회원가입
              </Link>
              <Link
                to="/patients"
                className="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 transition"
              >
                환자 조회
              </Link>
          
            </div>
          </nav>
        </div>
      </header>
      {/* 메인 콘텐츠 - 라우팅 <Routes> -> 메인 */}
      <main className="flex-1 flex justify-center px-6">
        <Routes>
          <Route path="/" element={<HomePage />} />{" "}
          {/*<Routes> -> 페이지 이동 경로 </Routes>*/}
          <Route path="/signup" element={<SignUpPage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/patients" element={<PatientListPage />}/>
          <Route path="/patients/:patientId" element={<PatientDetailPage />} />
          <Route path="/patients/new" element={<PatientCreatePage/>}/>
        </Routes>

        
      </main>
    </div>
  );
}
export default App;
