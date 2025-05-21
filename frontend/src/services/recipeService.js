import privateAPI from "@/api/privateAPI";
import publicAPI from "@/api/publicAPI";

// 레시피 저장
export const saveRecipe = async (formValues) => {
    const res = await privateAPI.post(`/recipe`, formValues)

    return res;
}

// 레시피 원본 데이터 가져오기
export const getOriRecipe = async (recipeId) => {
    const res = await privateAPI.get(`/recipe/edit/${recipeId}`)

    return res;
}

// 레시피 업데이트
export const editRecipe = async (formValues) => {
    const res = await privateAPI.patch(`/recipe/edit`, formValues)

    return res;
}

// 레시피 상세 조회
export const getDetailRecipe = async (recipeId) => {
    const res = await publicAPI.get(`/recipe/${recipeId}`)

    return res;
}