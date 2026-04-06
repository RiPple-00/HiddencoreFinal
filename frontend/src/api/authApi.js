import api from './index'; //http://localhost:8080/api

// 인증 API 서비스
const authApi = {

    // 회원가입
    signUp: (userData) => {
        return api.post('/auth/signup', userData);
    },

    // 로그인
    login: (Credential) => {
        return api.post('/auth/login', Credential);
    },

    // 사용자명 중복 체크
    checkUsername: (username) => {
        return api.get('/auth/check-username',{ 
            params: {username}
        });
    },

    // 이메일 중복
    checkEmail: (email) => {
        return api.get('/auth/check-email',{
            params:{email}

        });
    }

};
export default authApi;