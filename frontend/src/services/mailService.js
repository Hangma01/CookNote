import api from '@/api/axios'


// 인증 번호 메일 보내기기
export const sendAuthCode = async (email) => {
    const res = await api.post(`/mail/send/authcode`, { 'email': email });
    return res;
};

// 인증 번호 검증
export const verifyAuthCode = async (email, authCode) => {
    const res = await api.post(`/mail/verify`,
        { 'email': email, 'authCode': authCode },
        {
            headers: {
                'Accept': 'application/json'  // 응답 형식으로 JSON을 명시적으로 요청
            }
        });
    return res;
};