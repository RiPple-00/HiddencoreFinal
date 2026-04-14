import api from './index';

const postApi = {
  getPostList: (facilityId, type = null, page = 0, size = 20) =>
    api.get(`/facilities/${facilityId}/posts`, {
      params: { type, page, size },
    }),

  getPost: (facilityId, postId) =>
    api.get(`/facilities/${facilityId}/posts/${postId}`),

  searchPosts: (facilityId, type, searchType, keyword, page = 0, size = 20) =>
    api.get(`/facilities/${facilityId}/posts/search`, {
      params: { type, searchType, keyword, page, size },
    }),

  createPost: (facilityId, userId, data) =>
    api.post(`/facilities/${facilityId}/posts`, data, {
      params: { userId },
    }),

  updatePost: (facilityId, postId, userId, data) =>
    api.put(`/facilities/${facilityId}/posts/${postId}`, data, {
      params: { userId },
    }),

  deletePost: (facilityId, postId, userId) =>
    api.delete(`/facilities/${facilityId}/posts/${postId}`, {
      params: { userId },
    }),

  getMyPosts: (facilityId, userId, type = null, page = 0, size = 20) =>
    api.get(`/facilities/${facilityId}/posts/my`, {
      params: { userId, page, size, ...(type != null ? { type } : {}) },
    }),

  getMyDrafts: (facilityId, userId, type = null, page = 0, size = 20) =>
    api.get(`/facilities/${facilityId}/posts/draft`, {
      params: { userId, page, size, ...(type != null ? { type } : {}) },
    }),

  uploadFile: (facilityId, file) => {
    const formData = new FormData();
    formData.append('file', file);
    return api.post(`/facilities/${facilityId}/posts/upload`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
  },
};

export default postApi;
