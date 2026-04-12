import { useState } from "react";
import patientApi from "../../api/patientApi";
import { useNavigate } from "react-router-dom";

export default function PatientCreatePage() {
  const [formData, setFormData] = useState({
    name: "",
    gender: "",
    birthDate: "",
    address: "",
    bloodType: "",
    admissionDate: "",
    building: "",
    room: "",
    bed: "",
    memo: "",
  });

  const handleChange = (e) => {
    const { name, value } = e.target;

    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      //   console.log("전송 데이터:", formData);

      await patientApi.createPatient(formData);

      alert("환자 등록 완료");
      navigate("/patients");
    } catch (error) {
      //   console.error("환자 등록 실패", error);
      alert("환자 등록 실패");
    }
  };

  const handleCancel = () => {
    navigate("/patients");
  };

  const navigate = useNavigate(); //페이지 이동
  navigate("/patients");

  return (
    <div className="max-w-3xl mx-auto p-6">
      <h1 className="text-2xl font-bold mb-6">환자 등록</h1>

      <form
        className="grid grid-cols-1 md:grid-cols-2 gap-4"
      >
        <div>
          <label className="block mb-1 font-medium">이름</label>
          <input
            type="text"
            name="name"
            value={formData.name}
            onChange={handleChange}
            className="w-full border rounded-lg px-3 py-2"
          />
        </div>

        <div>
          <label className="block mb-1 font-medium">성별</label>
          <select
            name="gender"
            value={formData.gender}
            onChange={handleChange}
            className="w-full border rounded-lg px-3 py-2"
          >
            <option value="">선택</option>
            <option value="MALE">남성</option>
            <option value="FEMALE">여성</option>
            <option value="OTHER">기타</option>
          </select>
        </div>

        <div>
          <label className="block mb-1 font-medium">생년월일</label>
          <input
            type="date"
            name="birthDate"
            value={formData.birthDate}
            onChange={handleChange}
            className="w-full border rounded-lg px-3 py-2"
          />
        </div>

        <div>
          <label className="block mb-1 font-medium">혈액형</label>
          <select
            name="bloodType"
            value={formData.bloodType}
            onChange={handleChange}
            className="w-full border rounded-lg px-3 py-2"
          >
            <option value="">선택</option>
            <option value="A_POSITIVE">A+</option>
            <option value="A_NEGATIVE">A-</option>
            <option value="B_POSITIVE">B+</option>
            <option value="B_NEGATIVE">B-</option>
            <option value="AB_POSITIVE">AB+</option>
            <option value="AB_NEGATIVE">AB-</option>
            <option value="O_POSITIVE">O+</option>
            <option value="O_NEGATIVE">O-</option>
          </select>
        </div>

        <div className="md:col-span-2">
          <label className="block mb-1 font-medium">주소</label>
          <input
            type="text"
            name="address"
            value={formData.address}
            onChange={handleChange}
            className="w-full border rounded-lg px-3 py-2"
          />
        </div>

        <div>
          <label className="block mb-1 font-medium">입원일</label>
          <input
            type="date"
            name="admissionDate"
            value={formData.admissionDate}
            onChange={handleChange}
            className="w-full border rounded-lg px-3 py-2"
          />
        </div>

        <div>
          <label className="block mb-1 font-medium">건물</label>
          <input
            type="text"
            name="building"
            value={formData.building}
            onChange={handleChange}
            className="w-full border rounded-lg px-3 py-2"
            placeholder="A동"
          />
        </div>

        <div>
          <label className="block mb-1 font-medium">호실</label>
          <input
            type="text"
            name="room"
            value={formData.room}
            onChange={handleChange}
            className="w-full border rounded-lg px-3 py-2"
            placeholder="301호"
          />
        </div>

        <div>
          <label className="block mb-1 font-medium">병상</label>
          <input
            type="text"
            name="bed"
            value={formData.bed}
            onChange={handleChange}
            className="w-full border rounded-lg px-3 py-2"
            placeholder="1번"
          />
        </div>

        <div className="md:col-span-2">
          <label className="block mb-1 font-medium">기타 정보</label>
          <textarea
            name="memo"
            value={formData.memo || ""}
            onChange={handleChange}
            className="w-full border rounded-lg px-3 py-2"
            placeholder="알레르기, 특이사항 등"
          />
        </div>

        <div className="md:col-span-2 flex justify-end gap-2 mt-4">
          <button type="cancel" onClick={handleCancel} className="px-4 py-2 rounded-lg border">
            취소
          </button>
          <button
            type="submit" onClick={handleSubmit}
            className="px-4 py-2 rounded-lg bg-blue-600 text-white"
          >
            등록
          </button>
        </div>
      </form>
    </div>
  );
}
