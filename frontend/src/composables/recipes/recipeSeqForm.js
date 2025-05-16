import { generateId } from '@/utils/commonFunction'
import { ref } from 'vue'

export const recipeSeqForm = () => {
  const recipeSeq = ref([
                    { id: generateId() , step: 1, description: '', image: null },
                    { id: generateId(), step: 2, description: '', image: null },
                    { id: generateId(), step: 3, description: '', image: null },
                  ])
  
  const recipeTip = ref('')

  const addRecipeSeq = () => {
    if (recipeSeq.value.length < 50) {
      recipeSeq.value.push({ 
        id: generateId(),
        step: recipeSeq.value.length + 1, 
        description: '' ,
        image: null
      })
    }
  }

  const removeRecipeSeq = (index) => {
    if (recipeSeq.value.length > 1) {
      recipeSeq.value.splice(index, 1)
      recipeSeq.value.forEach((item, i) => item.step = i + 1)
    }
  }

  const validation = () => {

    if (recipeSeq.value.some(item => {
      return (!item.description.trim());
    })) {
      return '요리순서 설명을 입력해주세요.'
    }

    return true;
  }

  const getData = () => {
    return {
      seqs: recipeSeq.value.map(item => ({
        step: item.step,
        description: item.description,
        image: item.image
      })),
      tip: recipeTip.value
    }
  }

  return {
    recipeSeq,
    recipeTip,
    addRecipeSeq,
    removeRecipeSeq,
    validation,
    getData,
  }
}