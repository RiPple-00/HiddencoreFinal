import { Routes, Route } from 'react-router-dom';
import HomePage from './pages/HomePage';
import SignUpPage from './pages/SignUpPage';
import LoginPage from './pages/LoginPage';
import BedRoomPage from './pages/BedRoomPage';

function MainLayout({ children }) {
  return <main className="mx-auto max-w-7xl px-4 py-8">{children}</main>;
}

function App() {
  return (
    <div>
      <Routes>
        <Route path="/bedroompage/:room" element={<BedRoomPage />} />
        <Route path="/"
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
      </Routes>
    </div>
  );
}
export default App