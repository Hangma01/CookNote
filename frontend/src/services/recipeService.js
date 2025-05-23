import privateAPI from "@/api/privateAPI";
import publicAPI from "@/api/publicAPI";
import { useUserStore } from "@/stores/user";


// 유저 스토어
const userStore = useUserStore();

// 레시피 저장
export const saveRecipe = async (formValues) => {
    return await privateAPI.post(`/recipe`, formValues)
}

// 레시피 삭제
export const deleteRecipe = async (recipeId) => {
    return await privateAPI.delete(`/recipe`, { params: { recipeId } })
}

// 레시피 원본 데이터 가져오기
export const getOriRecipe = async (recipeId) => {
    return await privateAPI.get(`/recipe/edit/${recipeId}`)
}

// 레시피 업데이트
export const editRecipe = async (formValues) => {
    return await privateAPI.patch(`/recipe/edit`, formValues)
}

// 레시피 상세 조회
export const getDetailRecipe = async (recipeId) => {
    const isLoggedIn = userStore.getIsLoggedIn;
    
    if(isLoggedIn) {
        return await privateAPI.get(`/recipe/${recipeId}`)
    } else {
        return await publicAPI.get(`/recipe/${recipeId}`)
    }
}

// 레시피 좋아요 생성
export const recipeLikeInsert = async (recipeId) => {
    return await privateAPI.post(`/recipe/like`, { recipeId })
}

// 레시피 좋아요 삭제
export const recipeLikeDelete = async (recipeId) => {
    return await privateAPI.delete(`/recipe/like`, { params: { recipeId } })
}

// 레시피 북마크 생성
export const recipeBookmarkInsert = async (recipeId) => {
    return await privateAPI.post(`/recipe/bookmark`, { recipeId })
}

// 레시피 북마크 삭제
export const recipeBookmarkDelete = async (recipeId) => {
    return await privateAPI.delete(`/recipe/bookmark`, { params: { recipeId } })
}