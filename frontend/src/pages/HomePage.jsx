import { useNavigate } from "react-router-dom";

function HomePage() {
  const navigate = useNavigate();

  
  return (
    
    <div className="flex min-h-screen items-center justify-center bg-slate-100">
      <button
        onClick={() => navigate("/bedroompage/303")}
        className="rounded-2xl bg-blue-700 px-6 py-4 text-lg font-semibold text-white hover:bg-blue-800"
      >
        형이 만든 병실 컴포넌트
      </button>
    </div>
  );
}

export default HomePage;