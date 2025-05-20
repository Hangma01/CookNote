import privateAPI from "@/api/privateAPI";

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