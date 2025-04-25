// src/lib/axios.js
import axios from 'axios'

const api = axios.create({
    baseURL: 'http://localhost:8080/api', // 실제 서버 주소로 바꾸기
})

export default api
