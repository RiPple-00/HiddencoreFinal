import api from './index';

// 회원 API 서비스
const getAllMembers = () => api.get('/members');

const memberApi = {
    getMember: (id) => api.get(`/members/${id}`),

    getAllMembers,
    /** 이전 member.js 호환 이름 */
    getAllMember: getAllMembers,

    updateMember: (id, body) => api.put(`/members/${id}`, body),

    deleteMember: (id) => api.delete(`/members/${id}`),
};

export default memberApi;
