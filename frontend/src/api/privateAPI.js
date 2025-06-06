import { useUserStore } from '@/stores/user';
import { commonValues } from '@/utils/commonValues';
import axios, { HttpStatusCode } from 'axios';
import publicAPI from './publicAPI';
import { errorMessages } from '@/utils/messages/errorMessages';

// 유저 스토어
const userStore = useUserStore();

const privateAPI = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL, // 실제 서버 주소로 바꾸기
});

// 요청 인터셉터
privateAPI.interceptors.request.use(
    (config) => {
        const accessToken = userStore.getAccessToken;

        if (accessToken) {
            config.headers[commonValues.AUTHORIZATION_HEADER] = accessToken;
        }

        return config;
    },
    (error) => Promise.reject(error) // 요청을 보내기 전 단계에서 에러가 발생했을 때 실행 됨
);

// 응답 인터셉터: access token 만료시 리프레시
privateAPI.interceptors.response.use(
    (response) => response,
    async (error) => {
        const originalRequest = error.config;
        const isTokenExpired =
            error.response?.status === HttpStatusCode.Unauthorized && error.response?.data?.message === errorMessages.ACCESS_TOKEN_EXPIRED_MESSAGE;
        const isBlackList =
            error.response?.status === HttpStatusCode.Unauthorized && error.response?.data?.message === errorMessages.BALACKLIST_EXCEPTION_MESSAGE;

        //if (isTokenExpired && !originalRequest._retry) {
        if (isTokenExpired) {
            //  originalRequest._retry = true;

            try {
                console.log('accessToken 재발급 요청');
                const res = await publicAPI.post(`/auth/reissue`, {}, { withCredentials: true });

                const newAccessToken = res.headers['authorization'];

                userStore.setNewAccessToken(newAccessToken);
                return privateAPI(originalRequest); // 재요청
            } catch (e) {
                // 유저 스토어 삭제
                alert(e.response?.data?.message);
                userStore.logout();
                window.location.href = '/login';
                return;
            }
        } else if (isBlackList) {
            userStore.logout();
            window.location.href = '/login';
            return;
        }

        return Promise.reject(error);
    }
);

export default privateAPI;
