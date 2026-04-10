import api from './index';

const boardApi = {

    // 프로그램 리스트 불러오기
    getPostsByBoard: (boardId, page = 0, size = 10) => 
        api.get(`/posts/board/${boardId}`, {params: {page, size}}

    ),

    // 프로그램 게시글 등록
    getPost:(id) => api.get(`/posts/${id}`)

};

export default boardApi;