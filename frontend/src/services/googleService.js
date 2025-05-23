import axios from "axios"

export const youtubeApi = async (url) => {
    return await axios.get(url)
}