import Input from '../../Input';
import FileUpload from './FileUpload';

// 타입별 타이틀 / 부제목
const FORM_META = {
  NOTICE: {
    title: '공지사항 작성',
    subtitle: '공지사항을 작성할 수 있습니다. 작성 후 게시 설정을 확인하세요.',
  },
  PROGRAM: {
    title: '프로그램 작성',
    subtitle: '환자를 위한 신규 프로그램을 등록합니다.',
  },
  // GENERAL: { title: '게시글 작성', subtitle: '' }, // CHECK!!! 추후 추가
};

/**
 * 게시글 작성 중앙 본문 영역 - 모든 타입 공통
 * 제목 / 본문 / 첨부파일은 타입에 무관하게 동일
 */
const CreateForm = ({ postType, title, content, attachmentUrls, facilityId, onChange }) => {
  const meta = FORM_META[postType] ?? { title: '', subtitle: '' };

  return (
    <div className="flex-1 min-w-0">
      <p className="text-sm text-gray-400 mb-1">게시판 &gt; 새 게시글 작성</p>
      <h1 className="text-2xl font-bold text-teal-800 mb-1">{meta.title}</h1>
      {meta.subtitle && (
        <p className="text-sm text-gray-500 mb-6">{meta.subtitle}</p>
      )}

      <div className="bg-white border border-gray-200 rounded-lg p-6">

        {/* 제목 - Input 컴포넌트 사용 */}
        <Input
          label="제목"
          type="text"
          value={title}
          onChange={(e) => onChange({ title: e.target.value })}
          placeholder="제목을 입력하세요"
          className="text-lg"
        />

        <hr className="border-gray-100 my-5" />

        {/* 본문 - textarea는 Input 컴포넌트가 지원하지 않으므로 직접 사용 */}
        <div className="mb-5">
          <label className="block text-sm font-medium text-gray-700 mb-2">본문</label>
          <textarea
            value={content}
            onChange={(e) => onChange({ content: e.target.value })}
            placeholder="내용을 입력하세요."
            rows={12}
            className="
              w-full px-4 py-2
              border border-gray-300 rounded-lg
              focus:ring-2 focus:ring-blue-500 focus:border-transparent
              outline-none transition
              text-sm text-gray-700 placeholder-gray-300
              resize-none
            "
          />
        </div>

        {/* 첨부파일 */}
        <FileUpload
          facilityId={facilityId}
          attachmentUrls={attachmentUrls}
          onChange={(urls) => onChange({ attachmentUrls: urls })}
        />
      </div>
    </div>
  );
};

export default CreateForm;
