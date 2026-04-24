import {
  Bell,
  Menu,
  FileText,
  FilePenLine,
  CalendarCheck,
  CalendarDays,
  CreditCard,
  Activity,
  MessageSquareText,
  ChevronRight,
  CheckCircle2,
} from "lucide-react";

export default function GuardianMainPage() {
  const quickMenus = [
    { id: 1, icon: <FileText size={32} />, label: "보고서 확인" },
    { id: 2, icon: <FilePenLine size={32} />, label: "동의서 확인" },
    { id: 3, icon: <CalendarCheck size={32} />, label: "면회 신청" },
  ];

  const notices = [
    {
      id: 1,
      title: "춘계 보호자 간담회 안내",
      date: "2024.03.28",
    },
    {
      id: 2,
      title: "면회 예약 시스템 점검 안내",
      date: "2024.03.25",
    },
  ];

  const bottomMenus = [
    { id: 1, icon: <Activity size={24} />, label: "홈", active: true },
    { id: 2, icon: <CalendarDays size={24} />, label: "달력" },
    { id: 3, icon: <CreditCard size={24} />, label: "수납" },
    { id: 4, icon: <CheckCircle2 size={24} />, label: "실시간" },
    { id: 5, icon: <MessageSquareText size={24} />, label: "챗봇" },
  ];

  return (
    <div className="min-h-screen bg-[#F5F6F8] text-slate-800">
      <div className="mx-auto max-w-[430px] bg-[#F5F6F8] pb-28">
        {/* Header */}
        <header className="flex items-center justify-between px-6 pt-8 pb-6">
          <div className="flex items-center gap-3">
            <div className="flex h-11 w-11 items-center justify-center rounded-xl bg-white shadow-sm">
              <img
                src="/logo192.png"
                alt="따숨 로고"
                className="h-7 w-7 object-contain"
              />
            </div>
            <h1 className="text-[28px] font-extrabold tracking-tight text-[#0B4EA2]">
              따숨
            </h1>
          </div>

          <div className="flex items-center gap-5 text-[#0B4EA2]">
            <button className="transition hover:scale-105">
              <Bell size={28} />
            </button>
            <button className="transition hover:scale-105">
              <Menu size={30} />
            </button>
          </div>
        </header>

        {/* Gallery */}
        <section className="px-6">
          <h2 className="mb-5 text-[24px] font-bold text-[#0B4EA2]">
            활동 갤러리
          </h2>

          <div className="relative overflow-hidden rounded-[32px] bg-white shadow-sm">
            <img
              src="https://images.unsplash.com/photo-1516574187841-cb9cc2ca948b?auto=format&fit=crop&w=1200&q=80"
              alt="활동 갤러리"
              className="h-[285px] w-full object-cover"
            />
            <div className="absolute bottom-5 right-5 rounded-full bg-black/35 px-3 py-1 text-sm font-medium text-white">
              1/8
            </div>
          </div>
        </section>

        {/* Quick Menu */}
        <section className="mt-10 px-6">
          <div className="grid grid-cols-3 gap-4">
            {quickMenus.map((menu) => (
              <button
                key={menu.id}
                className="flex h-[155px] flex-col items-center justify-center rounded-[28px] border border-[#BFD2EC] bg-white text-[#0B4EA2] shadow-sm transition hover:-translate-y-1"
              >
                <div className="mb-5">{menu.icon}</div>
                <span className="text-[18px] font-semibold leading-snug text-slate-900">
                  {menu.label}
                </span>
              </button>
            ))}
          </div>
        </section>

        {/* Meal Section */}
        <section className="mt-10 px-6">
          <div className="rounded-[32px] border border-[#BFD2EC] bg-white p-6 shadow-sm">
            <div className="mb-6 flex items-center justify-between gap-4">
              <h2 className="text-[24px] font-bold text-[#0B4EA2]">
                오늘의 식단
              </h2>

              <div className="flex rounded-full bg-slate-100 p-1">
                <button className="rounded-full px-5 py-2 text-lg font-semibold text-slate-500">
                  아침
                </button>
                <button className="rounded-full bg-white px-5 py-2 text-lg font-bold text-[#0B4EA2] shadow-sm">
                  점심
                </button>
                <button className="rounded-full px-5 py-2 text-lg font-semibold text-slate-500">
                  저녁
                </button>
              </div>
            </div>

            <div className="flex gap-5">
              <img
                src="https://images.unsplash.com/photo-1547592180-85f173990554?auto=format&fit=crop&w=800&q=80"
                alt="오늘의 식단"
                className="h-[140px] w-[140px] rounded-[24px] object-cover"
              />

              <div className="flex flex-1 flex-col justify-center">
                <p className="mb-3 text-[18px] font-bold text-[#0B4EA2]">
                  메인 메뉴: 전복죽
                </p>
                <p className="text-[17px] leading-8 text-slate-700">
                  계란찜, 시금치 나물, 백김치
                  <br />
                  후식용 계절 과일
                </p>

                <div className="mt-5 inline-flex w-fit items-center gap-2 rounded-xl bg-[#DCEBFF] px-4 py-2 text-[16px] font-bold text-[#0B4EA2]">
                  <CheckCircle2 size={18} />
                  식사 완료 (12:30)
                </div>
              </div>
            </div>
          </div>
        </section>

        {/* Notice */}
        <section className="mt-10 px-6">
          <div className="mb-5 flex items-center justify-between">
            <h2 className="text-[24px] font-bold text-[#0B4EA2]">공지사항</h2>
            <button className="text-[16px] font-bold text-[#0B4EA2]">
              전체보기
            </button>
          </div>

          <div className="space-y-4">
            {notices.map((notice) => (
              <button
                key={notice.id}
                className="flex w-full items-center justify-between rounded-[28px] border border-[#BFD2EC] bg-white px-8 py-7 text-left shadow-sm transition hover:-translate-y-0.5"
              >
                <div>
                  <p className="text-[20px] font-bold text-[#0B4EA2]">
                    {notice.title}
                  </p>
                  <p className="mt-2 text-[16px] text-[#6B8FC3]">
                    {notice.date}
                  </p>
                </div>
                <ChevronRight className="text-slate-300" size={30} />
              </button>
            ))}
          </div>
        </section>
      </div>

      {/* Bottom Navigation */}
      <nav className="fixed bottom-0 left-1/2 z-50 w-full max-w-[430px] -translate-x-1/2 rounded-t-[32px] border border-[#BFD2EC] bg-white px-4 py-3 shadow-[0_-8px_30px_rgba(0,0,0,0.06)]">
        <div className="grid grid-cols-5">
          {bottomMenus.map((menu) => (
            <button
              key={menu.id}
              className="flex flex-col items-center justify-center gap-1 py-2"
            >
              <div
                className={
                  menu.active ? "text-[#0B4EA2]" : "text-slate-500"
                }
              >
                {menu.icon}
              </div>
              <span
                className={`text-[15px] font-semibold ${
                  menu.active ? "text-[#0B4EA2]" : "text-slate-600"
                }`}
              >
                {menu.label}
              </span>
            </button>
          ))}
        </div>
      </nav>
    </div>
  );
}