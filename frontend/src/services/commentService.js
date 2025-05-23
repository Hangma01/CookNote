import privateAPI from "@/api/privateAPI"
import publicAPI from "@/api/publicAPI";

// 레시피 카테고리 가져오기
export const getComments = async (recipeId, page) => {
    return await publicAPI.get(`/comment?recipeId=${recipeId}&page=${page}`
)}

export const commentInsert = async (content, recipeId) => {
    return await privateAPI.post(`/comment`, {
        'content': content,
        'recipeId': recipeId
    })
}