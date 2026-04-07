function PatientSearchBar({ keyword, setKeyword }) {
  return (
    <div className="flex items-center justify-between gap-4">
      <input
        type="text"
        value={keyword}
        onChange={(e) => setKeyword(e.target.value)}
        placeholder="환자 이름, ID 검색"
        className="h-12 w-full rounded-xl border border-slate-300 px-4 text-sm outline-none focus:border-blue-500"
      />
    </div>
  );
}

export default PatientSearchBar;