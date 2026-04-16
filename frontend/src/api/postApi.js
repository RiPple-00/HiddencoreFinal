import api from './index';

const boardApi = {

    //프로그램 리스트 불러오기
    getPostsByBoard: (boardId, page=0, size=10) => {
        return api.get(`/posts/board/${boardId}`, {params : {page, size}})
    },

    //게시글 상세
    getPost : (id) => {
        return api.get(`/posts/${id}`)
    },

    //프로그램 게시글 등록하기

};

export default boardApi;