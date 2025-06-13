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

let isRefreshing = false; // 리프레시 요청이 진행 중인지를 체크
let failedQueue = []; // 리프레시 대기 중인 요청들을 저장할 큐

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

            if (isRefreshing) {
                // 리프레시 중일 경우, 리프레시가 끝날 때까지 대기
                return new Promise((resolve, reject) => {
                    failedQueue.push({ resolve, reject }); // 대기 큐에 요청 추가
                }).then((config) => {
                    return privateAPI(originalRequest); // 대기 후 원래 요청 재시도
                });
            }

            isRefreshing = true;

            try {
                console.log('accessToken 재발급 요청');
                const res = await publicAPI.post(`/auth/reissue`, {}, { withCredentials: true });

                const newAccessToken = res.headers['authorization'];
                userStore.setNewAccessToken(newAccessToken);

                // 모든 대기 중인 요청을 처리
                failedQueue.forEach(({ resolve }) => resolve());
                failedQueue = [];

                return privateAPI(originalRequest); // 재요청
            } catch (e) {
                // 유저 스토어 삭제
                alert(e.response?.data?.message);
                userStore.logout();
                window.location.href = '/login';
                return;
            } finally {
                isRefreshing = false;
                if (failedQueue.length > 0) {
                    failedQueue.forEach(({ reject }) => reject(error)); // 실패한 요청 처리
                    failedQueue = []; // 큐 비우기
                }
            }
        } else if (isBlackList) {
            console.log('블랙리스트');
            userStore.logout();
            window.location.href = '/login';
            return;
        }

        return Promise.reject(error);
    }
);

export default privateAPI;
