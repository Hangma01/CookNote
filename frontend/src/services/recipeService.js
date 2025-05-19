import privateAPI from "@/api/privateAPI";

// 레시피 저장
export const saveRecipe = async (formValues) => {
  const res = await privateAPI.post(`/recipe`, formValues)

  return res;
}

// 레시피 수정
export const getRecipeEdit = async (recipeId) => {
  const res = await privateAPI.get(`/recipe/edit/${recipeId}`)

  return res;
}