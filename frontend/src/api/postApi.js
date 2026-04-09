import api from "./index";

const boardApi = {
  // 프로그램 리스트 불러오기
  getPostsByBoard: (boardId, page = 0, size = 10) => {
    return api.get(`/posts/board/${boardId}`, { params: { page, size } });
  },

  // 프로그램 게시글 등록하기
  getPost: (id) => {
    return api.get(`/posts/${id}`);
  },

  // 프로그램 게시글 등록하기
  createPost: (postData) => {
    return api.post("/posts", postData);
  },

  // 프로그램 게시글 수정하기
  updatePost: (id, postData) => {
    return api.put(`/posts/${id}`, postData);
  },

  // 프로그램 게시글 삭제하기
  deletePost: (id) => {
    return api.delete(`/posts/${id}`);
  },
};

export default postApi;
