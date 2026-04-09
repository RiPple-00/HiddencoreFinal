import { Routes, Route, Link, useNavigate } from 'react-router-dom';
import { useEffect, useRef, useState } from 'react';
import HomePage from './pages/HomePage';
import SignUpPage from './pages/SignUpPage';
import LoginPage from './pages/LoginPage';
import SchedulePage from './pages/SchedulePage';
import { useAuth } from './contexts/AutoContext.jsx';

function App() {
  const navigate = useNavigate();
  const { user, isAuthenticated, logout } = useAuth();
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const menuRef = useRef(null);

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

  return (
    <div className="min-h-screen bg-orange-50">
      {/* 헤더 */}
      <header className="bg-gradient-to-r from-orange-100 to-amber-100 border-b-2 border-orange-200 shadow-sm">
        <div className="max-w-7xl mx-auto px-4 py-4">
          <nav className="flex items-center justify-between">
            {/* 로고 */}
            <Link to="/" className="text-2xl font-bold text-orange-700">
              따숨🏥
            </Link>

            {/* 메뉴 */}
            <div className="flex items-center gap-6">
              <Link
                to="/schedule"
                className="text-gray-600 hover:text-blue-600 transition"
              >
                캘린더
              </Link>

              {isAuthenticated ? (
                <div className="relative" ref={menuRef}>
                  <button
                    type="button"
                    className="px-3 py-2 rounded-lg bg-white border border-orange-200 text-gray-700 hover:bg-orange-50 transition"
                    onClick={() => setIsMenuOpen((v) => !v)}
                  >
                    {user?.username || user?.userId || '프로필'}
                  </button>
                  {isMenuOpen ? (
                    <div className="absolute right-0 mt-2 w-40 bg-white border border-orange-200 rounded-lg shadow-md overflow-hidden">
                      <button
                        type="button"
                        className="w-full text-left px-4 py-2 hover:bg-orange-50"
                        onClick={handleLogout}
                      >
                        로그아웃
                      </button>
                    </div>
                  ) : null}
                </div>
              ) : (
                <>
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
                </>
              )}
            </div>
          </nav>
        </div>
      </header>

      {/* 메인 콘텐츠 */}
      <main className="max-w-7xl mx-auto px-4 py-8">
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/signup" element={<SignUpPage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/schedule" element={<SchedulePage />} />
        </Routes>
      </main>
    </div>
  );
}

export default App;