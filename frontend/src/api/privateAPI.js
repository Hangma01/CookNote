import { useUserStore } from '@/stores/user';
import axios, { HttpStatusCode } from 'axios'
import { useRouter } from 'vue-router';

// 화면 전환
const router = useRouter();

// 유저 스토어
const userStore = useUserStore();


const privateAPI = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL, // 실제 서버 주소로 바꾸기
    withCredentials: true,
})


// 요청 인터셉터
privateAPI.interceptors.request.use(
  (config) => {

    return config;
  },
  (error) => Promise.reject(error)  // 요청을 보내기 전 단계에서 에러가 발생했을 때 실행 됨
);

// 응답 인터셉터: access token 만료시 리프레시
privateAPI.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;
    const isTokenExpired =
      error.response?.status === HttpStatusCode.Unauthorized &&
      error.response?.data?.message === ACCESS_TOKEN_EXPIRED_MESSAGE;

    if (isTokenExpired && !originalRequest._retry) {
      originalRequest._retry = true;

      try {
        const res = await axios.post(
          `${import.meta.env.VITE_API_BASE_URL}/auth/reissue`,
          {},
          { withCredentials: true }
        );

        const newAccessToken = res.headers['authorization'];
        if (!newAccessToken) {
          throw new Error('No token returned');
        }

        return privateAPI(originalRequest); // 재요청
      } catch (e) {
        // 유저 스토어 삭제
        alert(e.response?.data?.message);
        userStore.logout();
        router.push('/login');
        return Promise.reject(err);
      }
    }

    return Promise.reject(error);
  }
);


export default privateAPI