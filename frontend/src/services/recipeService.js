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

// 레시피 좋아요 데이터 가져오기
export const getRecipeLike = async (page = 0) => {
    return await privateAPI.get(`/recipe/like?page=${page}`)    
}

// 레시피 좋아요 생성
export const recipeLikeInsert = async (recipeId) => {
    return await privateAPI.post(`/recipe/like`, { recipeId })
}

// 레시피 좋아요 삭제
export const recipeLikeDelete = async (recipeId) => {
    return await privateAPI.delete(`/recipe/like`, { params: { recipeId } })
}

// 레시피 북마크 데이터 가져오기
export const getRecipeBookmark = async (page = 0) => {
    return await privateAPI.get(`/recipe/bookmark?page=${page}`)    
}

// 레시피 북마크 생성
export const recipeBookmarkInsert = async (recipeId) => {
    return await privateAPI.post(`/recipe/bookmark`, { recipeId })
}

// 레시피 북마크 삭제
export const recipeBookmarkDelete = async (recipeId) => {
    return await privateAPI.delete(`/recipe/bookmark`, { params: { recipeId } })
}

// 공개 호스트 레시피 가져오기
export const getHostRecipePublic = async (hostId, page = 0) => {
    return await publicAPI.get(`/recipe/public/host?hostId=${hostId}&page=${page}`)
}

// 공개 본인 레시피 가져오기
export const getRecipePublic = async (page = 0) => {
    return await privateAPI.get(`/recipe/public?page=${page}`)
}

// 비공개 본인 레시피 가져오기
export const getRecipePrivate = async (page = 0) => {
    return await privateAPI.get(`/recipe/private?page=${page}`)
}


// 레시피 검색
export const getRecipeSearch = async (keyword, categoryCuisineId, categoryPurposeId, conditionalType, page) => {
    return await publicAPI.get('/recipe/search', {
        params: {
            keyword: keyword || null,
            categoryCuisineId: categoryCuisineId,
            categoryPurposeId: categoryPurposeId,
            conditionalType: conditionalType,
            page: page,
        }
    });
}

// 레시피 재료 검색
export const getRecipeSearchIngredient = async (keyword, conditionalType, page) => {
    return await publicAPI.get('/recipe/search/ingredient', {
        params: {
            keyword: keyword || null,
            conditionalType: conditionalType,
            page: page,
        }
    });
}


// 팔로잉항 사용자들의 피드 검색
export const getRecipesOfFollowingUsers = async (page) => {
    return await privateAPI.get(`/recipe/following?page=${page}`)
}


// 팔로잉한 특정 사용자 피드 검색
export const getRecipesOfFollowingUser = async (followingId, page) => {
    return await privateAPI.get(`/recipe/following/${followingId}?page=${page}`)
}

// 레시피 좋아요한 갯수 가져오기
export const getRecipeLikeCount = async (recipeId) => {
    return await publicAPI.get(`/recipe/like/count?recipeId=${recipeId}`)
}

// 레시피 추천 가져오기 - 메인
export const getRecommentRecipe = async () => {
    return await publicAPI.get(`/recipe/recomment`)
}

// 레시피 혼밥 가져오기 - 메인
export const getSoloMealRecipe = async () => {
    return await publicAPI.get(`/recipe/soloMeal`)
}