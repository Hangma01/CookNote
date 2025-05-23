import privateAPI from "@/api/privateAPI"

// 레시피 카테고리 가져오기
export const getCategoryAll = async () => {
  return await privateAPI.get(`/category/all`)
}