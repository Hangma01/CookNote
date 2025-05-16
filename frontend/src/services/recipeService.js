import privateAPI from "@/api/privateAPI";

// 레시피 저장
export const saveRecipe = async (formValues) => {
  const res = await privateAPI.post(`/recipe`, formValues, {
    withCredentials: true
  })

  return res;
}