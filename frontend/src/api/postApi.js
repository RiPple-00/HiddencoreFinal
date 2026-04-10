import api from './index';




const boardApi = {

    // 리스트 불러오기
     getPostByBoard: (boardId, page=0 , size= 10) => 
        api.get(`/posts/board/${boardId}`, {params: {page, size}}),

    // 게시글 상세
     getPost: (id) => {
        return api.get(`/posts/${id}`)
    },

    // 프로그램 게시글 등록하기

    


}

export default boardApi