import api from '@/api/axios'


// 아이디 중복 체크
export const checkLoginId = async (id) => {
    const res = await api.get(`http://localhost:8080/api/user/check-login-id?login_id=${id}`);
    return res;
};

// 닉네임 중복 체크
export const checkNickname = async (nickname) => {
    const res = await api.get(`http://localhost:8080/api/user/check-nickname?nickname=${nickname}`);
    return res;
};

// 이메일 중복 체크
export const checkEmail = async (email) => {
    const res = await api.get(`http://localhost:8080/api/user/check-email?email=${email}`);
    return res;
};