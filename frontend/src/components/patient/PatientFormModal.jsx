import { useState } from "react";

// 신규환자 등록 컴포넌트
export default function PatientFormModal({ initialData, onClose, onSubmit }) {
  const [formData, setFormData] = useState({
    name: initialData?.name || "",
    gender: initialData?.gender || "",
    birthDate: initialData?.birthDate || "",
    admissionDate: initialData?.admissionDate || "",
    admissionStatus: initialData?.admissionStatus || "",
    dietType: initialData?.dietType || "",
    memo: initialData?.memo || "",
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    if (
      !formData.name ||
      !formData.gender ||
      !formData.birthDate ||
      !formData.admissionDate
    ) {
      alert("필수값을 입력해주세요.");
      return;
    }

    onSubmit(formData);
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/30">
      <div className="w-full max-w-xl rounded-2xl bg-white p-6 shadow-xl">
        <div className="mb-6 flex items-center justify-between">
          <h2 className="text-xl font-bold text-slate-900">
            {initialData ? "환자 수정" : "환자 등록"}
          </h2>
          <button
            onClick={onClose}
            className="text-slate-400 hover:text-slate-700"
          >
            ✕
          </button>
        </div>

        <form onSubmit={handleSubmit} className="grid grid-cols-2 gap-4">
          <input
            name="name"
            value={formData.name}
            onChange={handleChange}
            placeholder="이름"
            className="rounded-xl border px-4 py-3"
          />

          <select
            name="gender"
            value={formData.gender}
            onChange={handleChange}
            className="rounded-xl border px-4 py-3"
          >
            <option value="">성별 선택</option>
            <option value="남">남</option>
            <option value="여">여</option>
            <option value="기타">기타</option>
            
          </select>

          <div className="flex flex-col gap-2">
            <span className="pl-2 text-sm text-slate-600">생년월일</span>
            <input
              type="date"
              name="birthDate"
              value={formData.birthDate}
              onChange={handleChange}
              className="rounded-xl border px-4 py-3"
            />
          </div>

         <div className="flex flex-col gap-2">
            <span className="pl-2 text-sm text-slate-600">입원일</span>
            <input
              type="date"
              name="admissionDate"
              value={formData.admissionDate}
              onChange={handleChange}
              className="rounded-xl border px-4 py-3"
            />
          </div>

          <input
            name="admissionStatus"
            value={formData.admissionStatus}
            onChange={handleChange}
            placeholder="입원 병명"
            className="rounded-xl border px-4 py-3"
          />

          <input
            name="dietType"
            value={formData.dietType}
            onChange={handleChange}
            placeholder="식이 타입"
            className="rounded-xl border px-4 py-3"
          />
          <textarea
            name="memo"
            value={formData.memo}
            onChange={handleChange}
            placeholder="기타 메모"
            className="col-span-2 rounded-xl border px-4 py-3 h-32 resize-none"
          />

          <div className="col-span-2 mt-4 flex justify-end gap-3">
            <button
              type="button"
              onClick={onClose}
              className="rounded-xl border border-slate-300 px-4 py-2"
            >
              취소
            </button>
            <button
              type="submit"
              className="rounded-xl bg-blue-600 px-4 py-2 font-semibold text-white hover:bg-blue-700"
            >
              저장
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
