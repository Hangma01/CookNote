import publicAPI from '@/api/publicAPI';

// 로그인 요청
export const login = async (formValues) => {
    return await publicAPI.post(`/login`, formValues, {
        withCredentials: true,
    });
};

// 로그아웃 요청
export const logout = async () => {
    await publicAPI.post(
        `/logout`,
        {},
        {
            withCredentials: true,
        }
    );
};

// 아이디 중복 체크
export const existsId = async (id) => {
    return await publicAPI.post(`/auth/existsId`, { id });
};

// 닉네임 중복 체크
export const existsNickname = async (nickname) => {
    return await publicAPI.post(`/auth/existsNickname`, { nickname });
};

// 이메일 중복 체크
export const existsEmail = async (email) => {
    return await publicAPI.post(`/auth/existsEmail`, { email });
};

// 회원 가입
export const userJoin = async (formValues) => {
    return await publicAPI.post(`/auth/join`, formValues);
};

// 아이디 찾기 - 요청
export const userFindIdAuth = async (formValues) => {
    return await publicAPI.post(`/auth/findId`, formValues);
};

// 아이디 찾기
export const userFindId = async (name, email) => {
    return await publicAPI.get(`/auth/findId?name=${name}&email=${email}`);
};

// 비밀번호 찾기 - 요청
export const userFindPwAuth = async (formValues) => {
    return await publicAPI.post(`/auth/findPw`, formValues);
};

// 비밀번호 찾기 - 재설정
export const userFindPwReset = async (formValues) => {
    return await publicAPI.patch(`/auth/findPw`, formValues);
};
