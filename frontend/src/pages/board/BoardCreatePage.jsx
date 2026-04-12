import { useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import postApi from '../../api/postApi';
import { POST_STATUS, stringifyTargetRoles, stringifyAttachmentUrls } from '../../utils/boardUtils';
// CHECK!!! AuthContext 수정 완료 후 주석 해제
// import { useAuth } from '../../contexts/AuthContext';
import CreateSidebar from '../../components/board/create/CreateSidebar';
import CreateForm from '../../components/board/create/CreateForm';
import NoticePanel from '../../components/board/create/panel/NoticePanel';
import ProgramPanel from '../../components/board/create/panel/ProgramPanel';
// import PostPanel from '../../components/board/create/panel/PostPanel'; // CHECK!!! 일반 게시글 추후 추가

// 타입별 초기 패널 상태
const INITIAL_PANEL = {
  NOTICE: {
    isPinned: false,
    publishType: 'IMMEDIATE',
    reservationAt: '',
    targetRoles: ['DOCTOR'],
    authorName: null,
    updatedAt: null,
  },
  PROGRAM: {
    isPinned: false,
    publishType: 'IMMEDIATE',
    reservationAt: '',
    startAt: '',
    endAt: '',
    capacity: 30,
    authorName: null,
    updatedAt: null,
  },
  // GENERAL: { ... }, // CHECK!!! 일반 게시글 추후 추가
};

/**
 * publishType(UI 상태) → PostStatus(도메인 enum) 변환
 * IMMEDIATE → ACTIVE
 * SCHEDULED → RESERVE
 * 임시저장   → INACTIVE
 */
const resolveStatus = (publishType, isDraft) => {
  if (isDraft) return POST_STATUS.INACTIVE;
  if (publishType === 'SCHEDULED') return POST_STATUS.RESERVE;
  return POST_STATUS.ACTIVE;
};

/**
 * 게시글 작성 페이지
 * URL: /facilities/:facilityId/board/create
 */
const BoardCreatePage = () => {
  const navigate = useNavigate();
  const { facilityId } = useParams();

  // CHECK!!! AuthContext 수정 완료 후 아래 주석 해제 후 사용
  // const { user } = useAuth();
  const user = null; // 임시: AuthContext 연동 전

  const [postType, setPostType] = useState('NOTICE');
  const [formState, setFormState] = useState({
    title: '',
    content: '',
    attachmentUrls: [],
  });

  // 패널 초기화 시 authorName에 로그인 유저 이름 주입
  const buildInitialPanel = (type) => ({
    ...INITIAL_PANEL[type],
    authorName: user?.name ?? null, // CHECK!!! user 응답 필드명 확인 필요 (name / nickname 등)
  });

  const [panelState, setPanelState] = useState(buildInitialPanel('NOTICE'));
  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleTypeChange = (type) => {
    setPostType(type);
    setPanelState(buildInitialPanel(type));
  };

  const handleFormChange = (partial) => {
    setFormState((prev) => ({ ...prev, ...partial }));
  };

  const handlePanelChange = (partial) => {
    setPanelState((prev) => ({ ...prev, ...partial }));
  };

  const buildRequestData = (isDraft) => ({
    type: postType,
    title: formState.title,
    content: formState.content,
    attachmentUrls: stringifyAttachmentUrls(formState.attachmentUrls),
    status: resolveStatus(panelState.publishType, isDraft),
    isPinned: panelState.isPinned,
    reservationAt: panelState.publishType === 'SCHEDULED' ? panelState.reservationAt : null,
    ...(postType === 'NOTICE' && {
      targetRoles: stringifyTargetRoles(panelState.targetRoles),
    }),
    ...(postType === 'PROGRAM' && {
      startAt: panelState.startAt,
      endAt: panelState.endAt,
      capacity: panelState.capacity,
    }),
  });

  const handleSaveDraft = async () => {
    try {
      // CHECK!!! user?.userId 로 교체 필요 - AuthContext 연동 후
      await postApi.createPost(facilityId, user?.userId ?? null, buildRequestData(true));
    } catch {
      // 에러는 Axios 인터셉터에서 toast로 처리
    }
  };

  const handleSubmit = async () => {
    if (!formState.title.trim()) {
      alert('제목을 입력해주세요.');
      return;
    }
    setIsSubmitting(true);
    try {
      // CHECK!!! user?.userId 로 교체 필요 - AuthContext 연동 후
      await postApi.createPost(facilityId, user?.userId ?? null, buildRequestData(false));
      navigate(`/facilities/${facilityId}/board`);
    } catch {
      // 에러는 Axios 인터셉터에서 toast로 처리
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleCancel = () => navigate(`/facilities/${facilityId}/board`);

  const renderPanel = () => {
    const commonProps = {
      panelState,
      onPanelChange: handlePanelChange,
      onSaveDraft: handleSaveDraft,
      onSubmit: handleSubmit,
      onCancel: handleCancel,
      isSubmitting,
    };
    if (postType === 'NOTICE')  return <NoticePanel  {...commonProps} />;
    if (postType === 'PROGRAM') return <ProgramPanel {...commonProps} />;
    // if (postType === 'GENERAL') return <PostPanel {...commonProps} />; // CHECK!!! 추후 추가
    return null;
  };

  return (
    <div className="flex gap-6 max-w-6xl mx-auto px-4 py-6">
      <CreateSidebar
        postType={postType}
        onTypeChange={handleTypeChange}
        facilityId={facilityId}
        title={formState.title}
        content={formState.content}
      />
      <CreateForm
        postType={postType}
        title={formState.title}
        content={formState.content}
        attachmentUrls={formState.attachmentUrls}
        facilityId={facilityId}
        onChange={handleFormChange}
      />
      {renderPanel()}
    </div>
  );
};

export default BoardCreatePage;
