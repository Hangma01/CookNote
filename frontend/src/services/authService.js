import publicAPI from '@/api/publicAPI'


// 로그인 요청
export const login = async (formValues) => {
  const res = await publicAPI.post(`/login`, formValues,{
    withCredentials: true
  })
  
  return res;
}

// 아이디 중복 체크
export const existsUserId = async (userId) => {
  const res = await publicAPI.get(`/auth/exists/id?user_id=${userId}`);
  return res;
};

// 닉네임 중복 체크
export const existsNickname = async (nickname) => {
  const res = await publicAPI.get(`/auth/exists/nickname?nickname=${nickname}`);
  return res;
};

// 이메일 중복 체크
export const existsEmail = async (email) => {
  const res = await publicAPI.get(`/auth/exists/email?email=${email}`);
  return res;
};

// 회원 가입
export const userJoin = async (formValues) => {
  const res = await publicAPI.post(`/auth/join`, formValues)
  return res;
}

// 아이디 찾기 - 요청
export const userFindIdAuth = async (formValues) => {
  const res = await publicAPI.post(`/auth/find-id`, formValues)
  return res;
}

// 아이디 찾기
export const userFindId = async (name, email) => {
  const res = await publicAPI.get(`/auth/find-id?name=${name}&email=${email}`)
  return res;
}

// 비밀번호 찾기 - 요청
export const userFindPwAuth = async (formValues) => {
  const res = await publicAPI.post(`/auth/find-pw`, formValues)
  return res;
}

// 비밀번호 찾기 - 재설정
export const userFindPwReset = async (formValues) => {
  const res = await publicAPI.patch(`/auth/find-pw`, formValues)
  return res;
}

