const visitors = [
  { id: 1, name: "김한솔", relation: "아들", time: "14:30" },
  { id: 2, name: "박준수", relation: "배우자", time: "종료" },
];

function VisitorsPanel() {
  return (
    <section className="rounded-3xl border border-slate-200 bg-white p-5 shadow-sm">
      <h3 className="mb-4 text-xl font-bold text-slate-900">오늘의 면회객</h3>

      <div className="space-y-3">
        {visitors.map((v) => (
          <div
            key={v.id}
            className="flex items-center justify-between rounded-2xl bg-slate-50 p-4"
          >
            <div>
              <p className="font-bold text-slate-900">{v.name}</p>
              <p className="text-sm text-slate-500">관계: {v.relation}</p>
            </div>
            <span className="rounded-lg bg-blue-100 px-3 py-1 text-sm font-semibold text-blue-700">
              {v.time}
            </span>
          </div>
        ))}
      </div>
    </section>
  );
}

export default VisitorsPanel;