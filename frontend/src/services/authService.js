import api from '@/api/axios'


// 아이디 중복 체크
export const checkUserId = async (userId) => {
  const res = await api.get(`/auth/check-user-id?user_id=${userId}`);
  return res;
};

// 닉네임 중복 체크
export const checkNickname = async (nickname) => {
  const res = await api.get(`/auth/check-nickname?nickname=${nickname}`);
  return res;
};

// 이메일 중복 체크
export const checkEmail = async (email) => {
  const res = await api.get(`/auth/check-email?email=${email}`);
  return res;
};

// 회원 가입
export const userJoin = async (formValues) => {
  const res = await api.post(`/auth/join`, formValues)
  return res;
}

// 아이디 찾기 - 요청
export const userFindIdAuth = async (formValues) => {
  const res = await api.post(`/auth/find-id`, formValues)
  return res;
}

// 아이디 찾기
export const userFindId = async (name, email) => {
  const res = await api.get(`/auth/find-id?name=${name}&email=${email}`)
  return res;
}

// 비밀번호 찾기 - 요청
export const userFindPwAuth = async (formValues) => {
  const res = await api.post(`/auth/find-pw`, formValues)
  return res;
}

// 비밀번호 찾기 - 재설정
export const userFindPwRest = async (formValues) => {
  const res = await api.post(`/auth/find-pw/reset`, formValues)
  return res;
}

