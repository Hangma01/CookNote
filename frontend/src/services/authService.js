import publicAPI from '@/api/publicAPI'


// 로그인 요청
export const login = async (formValues) => {
  const res = await publicAPI.post(`/login`, formValues, {
    withCredentials: true
  })

  return res;
}

// 로그아웃 요청
export const logout = async () => {
  const res = await publicAPI.post(`/logout`, {}, {
    withCredentials: true
  })
}

// 아이디 중복 체크
export const existsId = async (id) => {
  const res = await publicAPI.get(`/auth/existsId?id=${id}`);
  return res;
};

// 닉네임 중복 체크
export const existsNickname = async (nickname) => {
  const res = await publicAPI.get(`/auth/existsNickname?nickname=${nickname}`);
  return res;
};

// 이메일 중복 체크
export const existsEmail = async (email) => {
  const res = await publicAPI.get(`/auth/existsEmail?email=${email}`);
  return res;
};

// 회원 가입
export const userJoin = async (formValues) => {
  const res = await publicAPI.post(`/auth/join`, formValues)
  return res;
}

// 아이디 찾기 - 요청
export const userFindIdAuth = async (formValues) => {
  const res = await publicAPI.post(`/auth/findId`, formValues)
  return res;
}

// 아이디 찾기
export const userFindId = async (name, email) => {
  const res = await publicAPI.get(`/auth/findId?name=${name}&email=${email}`)
  return res;
}

// 비밀번호 찾기 - 요청
export const userFindPwAuth = async (formValues) => {
  const res = await publicAPI.post(`/auth/findPw`, formValues)
  return res;
}

// 비밀번호 찾기 - 재설정
export const userFindPwReset = async (formValues) => {
  const res = await publicAPI.patch(`/auth/findPw`, formValues)
  return res;
}

