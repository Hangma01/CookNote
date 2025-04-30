import api from '@/api/axios'

export default {
    async existsLoginId(id) {
        const res = await api.get(`http://localhost:8080/api/user/exists/${id}`)

        return res
    },
};
