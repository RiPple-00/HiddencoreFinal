import api from "./index";

// 인증 API 서비스
const postApi = {
  // 게시판 조회 (type 없으면 전체, 있으면 해당 타입만)
  getPostList: (facilityId, type = null, page = 0, size = 20) =>
    api.get(`/facilities/${facilityId}/posts`, {
      params: { type, page, size },
    }),

  // 단건 상세 조회
  getPost: (facilityId, postId) =>
    api.get(`/facilities/${facilityId}/posts/${postId}`),

  // 검색 (searchType: 'title' | 'content' | 'all')
  searchPosts: (facilityId, type, searchType, keyword, page = 0, size = 20) =>
    api.get(`/facilities/${facilityId}/posts/search`, {
      params: { type, searchType, keyword, page, size },
    }),

  // 게시글 CRUD

  // 게시글 생성
  createPost: (facilityId, userId, data) =>
    api.post(`/facilities/${facilityId}/posts`, data, {
      params: { userId },
    }),

  // 게시글 수정
  updatePost: (facilityId, postId, userId, data) =>
    api.put(`/facilities/${facilityId}/posts/${postId}`, data, {
      params: { userId },
    }),

  // 게시글 삭제
  deletePost: (facilityId, postId, userId) =>
    api.delete(`/facilities/${facilityId}/posts/${postId}`, {
      params: { userId },
    }),

  // 마이페이지

  // 작성 기록 (type 없으면 전체)
  getMyPosts: (facilityId, userId, type = null, page = 0, size = 20) =>
    api.get(`/facilities/${facilityId}/posts/my`, {
      params: { userId, page, size, ...(type != null ? { type } : {}) },
    }),

  // 임시저장 (type 없으면 전체)
  getMyDrafts: (facilityId, userId, type = null, page = 0, size = 20) =>
    api.get(`/facilities/${facilityId}/posts/draft`, {
      params: { userId, page, size, ...(type != null ? { type } : {}) },
    }),

  // 백엔드 업로드
  uploadFile: (facilityId, file) => {
    const formData = new FormData();
    formData.append("file", file);
    return api.post(`/facilities/${facilityId}/posts/upload`, formData, {
      headers: { "Content-Type": "multipart/form-data" },
    });
  },
};

export default postApi;
