import privateAPI from "@/api/privateAPI"
import publicAPI from "@/api/publicAPI"

// 레시피 카테고리 전부 가져오기
export const getCategoryAll = async () => {
  return await privateAPI.get(`/category/all`)
}

// 레시피 카테고리 가져오기
export const getCategory = async () => {
  return await publicAPI.get(`/category`)
}