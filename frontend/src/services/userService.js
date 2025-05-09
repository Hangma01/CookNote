import api from '@/api/axios'


// 아이디 중복 체크
export const checkUserId = async (userId) => {
    const res = await api.get(`/user/check-user-id?user_id=${userId}`);
    return res;
};

// 닉네임 중복 체크
export const checkNickname = async (nickname) => {
    const res = await api.get(`/user/check-nickname?nickname=${nickname}`);
    return res;
};

// 이메일 중복 체크
export const checkEmail = async (email) => {
    const res = await api.get(`/user/check-email?email=${email}`);
    return res;
};

// 회원 가입
export const userJoin = async (formValues) => {
    const res = await api.post(`/user/join`, formValues)
    return res;
} 

// 아이디 찾기 - 요청
export const userFindIdAuth = async (formValues) => {
    const res = await api.post(`/user/find-id/auth`, formValues)
    return res;
}

// 아이디 찾기 - 검증
export const userFindId = async (name, email) => {
    const res = await api.post(`/user/find-id`)
    return res;
}