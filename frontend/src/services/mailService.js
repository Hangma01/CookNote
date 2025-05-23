import publicAPI from '@/api/publicAPI'


// 인증 번호 메일 보내기기
export const sendMailAuthCode = async (email) => {
    return await publicAPI.post(`/mail/sendAuthcode`, { 'email': email });
};

// 인증 번호 검증
export const verifyMailAuthCode = async (email, authCode) => {
    return await publicAPI.post(`/mail/verify`,
        { 'email': email, 'authCode': authCode },
        {
            headers: {
                'Accept': 'application/json'  // 응답 형식으로 JSON을 명시적으로 요청
            }
        });
};

export const deleteMailAuthCode = async (email) => {
    return await publicAPI.delete(`/mail/authCode`, { params: { email } });
}