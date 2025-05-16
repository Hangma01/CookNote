import { generateId } from '@/utils/commonFunction'
import { ref } from 'vue'

export const recipeSetForm = (recipeStatus) => {
  const selectedStatus = ref(recipeStatus)

  const validation = () => {
    if (!selectedStatus.value) return '설정을 선택해주세요.'

    return true;
  }

  const getData = () => {
    return {
      data: selectedStatus.value,
    }
  }

  return {
    selectedStatus,
    validation,
    getData,
  }
}