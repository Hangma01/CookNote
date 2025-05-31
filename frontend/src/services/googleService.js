import privateAPI from '@/api/privateAPI';

export const getYoutubeThumbnail = async (videoId) => {
    return await privateAPI.get(`/youtube?videoId=${videoId}`);
};
