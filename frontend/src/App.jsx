import { Routes, Route } from "react-router-dom";
import HomePage from "./pages/HomePage";
import SignUpPage from "./pages/SignUpPage";
import LoginPage from "./pages/LoginPage";
import BedRoomPage from "./pages/BedRoomPage";
import PatientListPage from "./pages/patient/PatientListPage";
import PatientDetailPage from "./pages/patient/PatientDetailPage";
import PatientCreatePage from "./pages/patient/PatientCreatePage";

function MainLayout({ children }) {
  return <main className="mx-auto max-w-7xl px-4 py-8">{children}</main>;
}

function App() {
  return (
    <div>
      <Routes>
        <Route path="/bedroompage/:room" element={<BedRoomPage />} />
        <Route
          path="/"
          element={
            <MainLayout>
              <HomePage />
            </MainLayout>
          }
        />
        <Route
          path="/signup"
          element={
            <MainLayout>
              <SignUpPage />
            </MainLayout>
          }
        />
        <Route
          path="/login"
          element={
            <MainLayout>
              <LoginPage />
            </MainLayout>
          }
        />
        {/* feature/patient: 환자 목록·상세·등록 (페이지 내부에 TopNavBar 포함) */}
        <Route path="/patients" element={<PatientListPage />} />
        <Route path="/patients/:patientId" element={<PatientDetailPage />} />
        <Route path="/patients/new" element={<PatientCreatePage />} />
      </Routes>
    </div>
  );
}

export default App;
