import privateAPI from '@/api/privateAPI';

// 이미지 저장
export const s3Upload = async (formData) => {
    return await privateAPI.post(`/s3/upload`, formData);
};

// 이미지 삭제
export const s3Delete = async (imageAddress) => {
    return await privateAPI.delete(`/s3/delete`, { params: { imageAddress } });
};

// 이미지 복사
export const s3Convert = async (convertImages) => {
    return await privateAPI.post(`/s3/convert`, convertImages);
};
