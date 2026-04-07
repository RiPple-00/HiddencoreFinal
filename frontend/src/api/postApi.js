import api from './index';

//인증 API 서비스
const postApi = {

    // 게시글 작성
    createPost: (boardId, authorId, data) =>
        api.post('/posts', data, { params: { boardId, authorId }}),
    
    // 게시판별 목록
    getPostsByBoard: ( boardtype=all, page=0, size=10 ) =>
        api.get(`/posts/board/${boardtype}`, { params: { page, size }}),
    
    // 게시글 상세
    getPost: (id) => api.get(`/posts/${id}`),

    // 게시글 검색
    searchPosts: (boardId, keyword, page, pageSize) => 
        api.get(`/boards/${boardId}/posts/search`, {
        params: { keyword, page, size: pageSize }}),

};

export default postApi;