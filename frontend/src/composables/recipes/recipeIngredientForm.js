import { generateId } from '@/utils/commonFunction'
import { ref } from 'vue'

export const recipeIngredientForm = () => {
  const ingredients = ref([
                        { id: generateId(), name: '', quantity: '' , remark: ''},
                        { id: generateId(), name: '', quantity: '' , remark: ''},
                        { id: generateId(), name: '', quantity: '' , remark: ''},
                      ])

  const addIngredient = () => {
    if (ingredients.value.length < 50) {
      ingredients.value.push({ name: '', quantity: '' })
    }
  }

  const removeIngredient = (index) => {
    if (ingredients.value.length > 1) {
      ingredients.value.splice(index, 1)
    }
  }

  const validation = () => {

    if (ingredients.value.some(item => {
      return (!item.name?.trim() || !item.quantity?.trim());
    })) {
      return '재료와 수량을 모두 입력하세요'
    }

    return true;
  }

  const getData = () => {
    return {
      ingredients: ingredients.value.map(item => ({
        name: item.remark,
        quantity: item.quantity,
        remark: item.remark || null
      }))
    }
  }

  return {
    ingredients,
    addIngredient,
    removeIngredient,
    validation,
    getData,
  }
}