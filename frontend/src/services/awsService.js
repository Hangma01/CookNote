import privateAPI from "@/api/privateAPI";

// 이미지 저장
export const s3Upload = async (formData) => {
    const res = await privateAPI.post(`/s3/upload`, formData)

    return res;
}

// 이미지 삭제
export const s3Delete = async (imageAddress) => {
    const res = await privateAPI.delete(`/s3/delete`, { params: { imageAddress } })

    return res;
}

// 이미지 복사
export const s3Convert = async (convertImages) => {
    const res = await privateAPI.post(`/s3/convert`, convertImages)

    return res;
}