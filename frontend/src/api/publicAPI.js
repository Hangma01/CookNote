import axios from 'axios';

const publicAPI = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL, // 실제 서버 주소로 바꾸기
});

export default publicAPI;
