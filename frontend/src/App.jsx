import { Routes, Route, Link, useLocation, useNavigate } from 'react-router-dom';
import { useEffect, useRef, useState } from 'react';
import HomePage from './pages/HomePage';
import SignUpPage from './pages/SignUpPage';
import LoginPage from './pages/LoginPage';
import SchedulePage from './pages/SchedulePage';
import BoardListPage from './pages/board/BoardListPage';
import BoardDetailPage from './pages/board/BoardDetailPage';
import BoardCreatePage from './pages/board/BoardCreatePage';
import BoardUserPostsPage from './pages/board/BoardUserPostsPage';
import MealEditPage from './pages/MealEditPage';
import MealCarePage from './pages/MealCarePage';
import CalendarPage from './pages/CalendarPage';
import MealTypePage from './pages/MealTypePage';
import MealUploadPage from './pages/MealUploadPage';
import BedRoomPage from './pages/BedRoomPage';
import PatientListPage from './pages/patient/PatientListPage';
import PatientDetailPage from './pages/patient/PatientDetailPage';
import WardPage from './pages/WardPage';
import GuardianMainPage from './pages/guardian/GuardianMainPage.jsx';

import { useAuth } from './contexts/AutoContext.jsx';



function App() {
  const navigate = useNavigate();
  const location = useLocation();
  const { user, isAuthenticated, logout } = useAuth();
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const menuRef = useRef(null);

  const facilityId = user?.facilityId ?? 1;

  useEffect(() => {
    const onDocClick = (e) => {
      if (!menuRef.current) return;
      if (!menuRef.current.contains(e.target)) setIsMenuOpen(false);
    };
    document.addEventListener('click', onDocClick);
    return () => document.removeEventListener('click', onDocClick);
  }, []);

  const handleLogout = () => {
    logout();
    setIsMenuOpen(false);
    navigate('/');
  };

  const handleGoBack = () => {
    if (window.history.length > 1) {
      navigate(-1);
      return;
    }
    navigate('/');
  };

  return (
    <div className="min-h-screen bg-orange-50">
      <header className="bg-gradient-to-r from-orange-100 to-amber-100 border-b-2 border-orange-200 shadow-sm">
        <div className="max-w-7xl mx-auto px-4 py-4">
          <nav className="flex items-center justify-between">
            {/* CHECK!!! TOP NAV로 통합예정 */}
            <Link to="/" className="text-2xl font-bold text-orange-700">
              따숨🏥
            </Link>
            <div className="flex flex-wrap items-center gap-4 md:gap-6">
              <Link to="/schedule" className="text-gray-600 hover:text-blue-600 transition">캘린더</Link>
              <Link to="/meal-care" className="text-gray-600 hover:text-blue-600 transition">식단</Link>
              <Link to={`/facilities/${facilityId}/board`} className="text-gray-600 hover:text-blue-600 transition">게시판</Link>
              <Link to="/patients" className="text-gray-600 hover:text-blue-600 transition">환자</Link>
              {isAuthenticated ? (
                <div className="relative" ref={menuRef}>
                  <button
                    type="button"
                    className="px-3 py-2 rounded-lg bg-white border border-orange-200 text-gray-700 hover:bg-orange-50 transition"
                    onClick={() => setIsMenuOpen((v) => !v)}
                  >
                    {user?.username || user?.userId || '프로필'}
                  </button>
                  {isMenuOpen && (
                    <div className="absolute right-0 mt-2 w-40 bg-white border border-orange-200 rounded-lg shadow-md overflow-hidden z-10">
                      <button
                        type="button"
                        className="w-full text-left px-4 py-2 hover:bg-orange-50"
                        onClick={handleLogout}
                      >
                        로그아웃
                      </button>
                    </div>
                  )}
                </div>
              ) : (
                <>
                  <Link to="/login" className="text-gray-600 hover:text-blue-600 transition">로그인</Link>
                  <Link to="/signup" className="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 transition">회원가입</Link>
                </>
              )}
            </div>
          </nav>
        </div>
      </header>

      <main className="max-w-7xl mx-auto px-4 py-8">
        {location.pathname !== '/' && (
          <div className="mb-4">
            <button
              type="button"
              onClick={handleGoBack}
              className="rounded-lg border border-orange-200 bg-white px-3 py-2 text-sm font-semibold text-orange-700 hover:bg-orange-50"
            >
              ← 뒤로가기
            </button>
          </div>
        )}
        <Routes>
          {/* <Route path="/" element={<WardPage />} /> */}
          <Route path="/" element={<GuardianMainPage />} />
 
          <Route path="/signup" element={<SignUpPage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/schedule" element={<SchedulePage />} />
          <Route path="/meal-care" element={<MealCarePage />} />
          <Route path="/calendar" element={<CalendarPage />} />
          <Route path="/meal-edit" element={<MealEditPage />} />
          <Route path="/meal-edit/:date" element={<MealEditPage />} />
          <Route path="/admin/menu/edit/:id" element={<MealEditPage />} />
          <Route path="/meal-type" element={<MealTypePage />} />
          <Route path="/meal-type/:date" element={<MealTypePage />} />
          <Route path="/meal-upload" element={<MealUploadPage />} />
          <Route path="/facilities/:facilityId/board" element={<BoardListPage />} />
          <Route path="/facilities/:facilityId/board/create" element={<BoardCreatePage />} />
          <Route path="/facilities/:facilityId/board/history" element={<BoardUserPostsPage variant="history" />} />
          <Route path="/facilities/:facilityId/board/draft" element={<BoardUserPostsPage variant="draft" />} />
          <Route path="/facilities/:facilityId/board/:postId" element={<BoardDetailPage />} />
          <Route path="/bedroompage/:room" element={<BedRoomPage />} />
          <Route path="/patients" element={<PatientListPage />} />
          <Route path="/patients/:patientId" element={<PatientDetailPage />} />
        </Routes>
      </main>
    </div>
  );
}

export default App;