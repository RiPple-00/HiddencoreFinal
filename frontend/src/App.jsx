import { Routes, Route, Link, useLocation, useNavigate } from 'react-router-dom';
import SignUpPage from './pages/SignUpPage';
import LoginPage from './pages/LoginPage';
import MealEditPage from './pages/MealEditPage';
import MealCarePage from './pages/MealCarePage';
import CalendarPage from './pages/CalendarPage';
import MealType from './pages/MealTypePage';
import MealUploadPage from './pages/MealUploadPage';

function App(){
  const navigate = useNavigate();
  const location = useLocation();

  const handleGoBack = () => {
    if (window.history.length > 1) {
      navigate(-1);
      return;
    }
    navigate('/');
  };

  return(
  <div className='min-h-screen bg-orange-50'>
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
                            <Link to="/login" className="text-gray-600 hover:text-blue-600 transition"
                            >
                                로그인
                            </Link>
                            <Link to="/signup" className="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 transition"
                            >
                                회원가입
                            </Link>
                        </div>
        </nav>
        </div>
      </header>

  {/* 메인 콘텐츠 - 라우팅 <Routes> -> 메인 */}
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
                    <Route path="/" element={<MealCarePage />} />
                    <Route path="/signup" element={<SignUpPage />} />
                    <Route path="/login" element={<LoginPage />} />
                    <Route path="/meal-care" element={<MealCarePage />} />
                    <Route path="/calendar" element={<CalendarPage />} />
                    <Route path="/meal-edit" element={<MealEditPage />} />
                    <Route path="/meal-edit/:date" element={<MealEditPage />} />
                    <Route path="/admin/menu/edit/:id" element={<MealEditPage />} />
                    <Route path="/meal-type" element={<MealType />} />
                    <Route path="/meal-type/:date" element={<MealType />} />
                    <Route path="/meal-upload" element={<MealUploadPage />} />
                 

                </Routes>
            </main>
        </div>

  )

}
export default App