import { useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
<<<<<<< HEAD
import postApi from '../../api/postApi';
import { POST_STATUS, stringifyTargetRoles, stringifyAttachmentUrls } from '../../utils/boardUtils';
// CHECK!!! AuthContext 수정 완료 후 주석 해제
// import { useAuth } from '../../contexts/AuthContext';
=======
import toast from 'react-hot-toast';
import postApi from '../../api/postApi';
import {
  POST_STATUS,
  stringifyTargetRoles,
  stringifyAttachmentUrls,
  BOARD_UI_TO_POST_TYPE,
  dateOnlyToLocalDateTimeString,
} from '../../utils/boardUtils';
import { useAuth } from '../../contexts/AutoContext.jsx';
>>>>>>> 1039d55f3b99df2abaf1450d9e2f351e3b91d9bf
import CreateSidebar from '../../components/board/create/CreateSidebar';
import CreateForm from '../../components/board/create/CreateForm';
import NoticePanel from '../../components/board/create/panel/NoticePanel';
import ProgramPanel from '../../components/board/create/panel/ProgramPanel';
<<<<<<< HEAD
// import PostPanel from '../../components/board/create/panel/PostPanel'; // CHECK!!! 일반 게시글 추후 추가

// 타입별 초기 패널 상태
=======

>>>>>>> 1039d55f3b99df2abaf1450d9e2f351e3b91d9bf
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
<<<<<<< HEAD
  // GENERAL: { ... }, // CHECK!!! 일반 게시글 추후 추가
};

/**
 * publishType(UI 상태) → PostStatus(도메인 enum) 변환
 * IMMEDIATE → ACTIVE
 * SCHEDULED → RESERVE
 * 임시저장   → INACTIVE
 */
=======
};

>>>>>>> 1039d55f3b99df2abaf1450d9e2f351e3b91d9bf
const resolveStatus = (publishType, isDraft) => {
  if (isDraft) return POST_STATUS.INACTIVE;
  if (publishType === 'SCHEDULED') return POST_STATUS.RESERVE;
  return POST_STATUS.ACTIVE;
};

<<<<<<< HEAD
=======
const emptyToNull = (v) => (v === '' || v == null ? null : v);

>>>>>>> 1039d55f3b99df2abaf1450d9e2f351e3b91d9bf
/**
 * 게시글 작성 페이지
 * URL: /facilities/:facilityId/board/create
 */
const BoardCreatePage = () => {
  const navigate = useNavigate();
  const { facilityId } = useParams();
<<<<<<< HEAD

  // CHECK!!! AuthContext 수정 완료 후 아래 주석 해제 후 사용
  // const { user } = useAuth();
  const user = null; // 임시: AuthContext 연동 전
=======
  const { user } = useAuth();

  /** 백엔드 `@RequestParam Long userId`(회원 PK) — 로그인 응답의 `id` */
  const authorPk = user?.id != null ? Number(user.id) : null;
>>>>>>> 1039d55f3b99df2abaf1450d9e2f351e3b91d9bf

  const [postType, setPostType] = useState('NOTICE');
  const [formState, setFormState] = useState({
    title: '',
    content: '',
    attachmentUrls: [],
  });

<<<<<<< HEAD
  // 패널 초기화 시 authorName에 로그인 유저 이름 주입
  const buildInitialPanel = (type) => ({
    ...INITIAL_PANEL[type],
    authorName: user?.name ?? null, // CHECK!!! user 응답 필드명 확인 필요 (name / nickname 등)
  });

  const [panelState, setPanelState] = useState(buildInitialPanel('NOTICE'));
=======
  const buildInitialPanel = (type) => ({
    ...INITIAL_PANEL[type],
    authorName: user?.username ?? null,
  });

  const [panelState, setPanelState] = useState(() => buildInitialPanel('NOTICE'));
>>>>>>> 1039d55f3b99df2abaf1450d9e2f351e3b91d9bf
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

<<<<<<< HEAD
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
=======
  const buildRequestData = (isDraft) => {
    const domainType = BOARD_UI_TO_POST_TYPE[postType];
    if (!domainType) {
      throw new Error('지원하지 않는 게시판 유형입니다.');
    }

    const rawTitle = formState.title.trim();
    const rawContent = formState.content.trim();
    const title =
      isDraft && !rawTitle ? '(제목 없음)' : rawTitle;
    const content =
      isDraft && !rawContent ? '(내용 없음)' : rawContent;

    return {
      type: domainType,
      title,
      content,
      attachmentUrls: stringifyAttachmentUrls(formState.attachmentUrls),
      status: resolveStatus(panelState.publishType, isDraft),
      isPinned: panelState.isPinned,
      reservationAt:
        panelState.publishType === 'SCHEDULED'
          ? emptyToNull(panelState.reservationAt)
          : null,
      ...(postType === 'NOTICE' && {
        targetRoles: stringifyTargetRoles(panelState.targetRoles),
      }),
      ...(postType === 'PROGRAM' && {
        startAt: dateOnlyToLocalDateTimeString(panelState.startAt, false),
        endAt: dateOnlyToLocalDateTimeString(panelState.endAt, true),
        capacity: (() => {
          const n = Number(panelState.capacity);
          return Number.isFinite(n) && n >= 0 ? Math.floor(n) : 30;
        })(),
      }),
    };
  };

  const handleSaveDraft = async () => {
    if (authorPk == null || Number.isNaN(authorPk)) {
      toast.error('임시 저장하려면 로그인해 주세요.');
      return;
    }
    try {
      await postApi.createPost(facilityId, authorPk, buildRequestData(true));
      toast.success('임시 저장되었습니다.');
      navigate(`/facilities/${facilityId}/board`);
>>>>>>> 1039d55f3b99df2abaf1450d9e2f351e3b91d9bf
    } catch {
      // 에러는 Axios 인터셉터에서 toast로 처리
    }
  };

  const handleSubmit = async () => {
    if (!formState.title.trim()) {
      alert('제목을 입력해주세요.');
      return;
    }
<<<<<<< HEAD
    setIsSubmitting(true);
    try {
      // CHECK!!! user?.userId 로 교체 필요 - AuthContext 연동 후
      await postApi.createPost(facilityId, user?.userId ?? null, buildRequestData(false));
=======
    if (!formState.content.trim()) {
      alert('내용을 입력해주세요.');
      return;
    }
    if (authorPk == null || Number.isNaN(authorPk)) {
      toast.error('게시글을 등록하려면 로그인해 주세요.');
      return;
    }
    setIsSubmitting(true);
    try {
      await postApi.createPost(facilityId, authorPk, buildRequestData(false));
      toast.success('게시글이 등록되었습니다.');
>>>>>>> 1039d55f3b99df2abaf1450d9e2f351e3b91d9bf
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
<<<<<<< HEAD
    if (postType === 'NOTICE')  return <NoticePanel  {...commonProps} />;
    if (postType === 'PROGRAM') return <ProgramPanel {...commonProps} />;
    // if (postType === 'GENERAL') return <PostPanel {...commonProps} />; // CHECK!!! 추후 추가
=======
    if (postType === 'NOTICE') return <NoticePanel {...commonProps} />;
    if (postType === 'PROGRAM') return <ProgramPanel {...commonProps} />;
>>>>>>> 1039d55f3b99df2abaf1450d9e2f351e3b91d9bf
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
