<script setup>
import ImageUploader from '@/components/ui/ImageUploader.vue'
import { commonInputHangle, generateId } from '@/utils/commonFunction';
import { reactive, ref, watch } from 'vue';

const props = defineProps({ 
  originalRecipeData: { // 레시피 수정 시 오는 데이터
    type: Object
  },
})

const recipeSeq = reactive([
                    { id: generateId() , step: 1, description: '', image: null },
                    { id: generateId(), step: 2, description: '', image: null },
                    { id: generateId(), step: 3, description: '', image: null },
                  ])

const recipeTip = ref('')

watch(() => props.originalRecipeData, (newVal) => {
  recipeSeq.splice(0, recipeSeq.length, 
    ...newVal.recipeSeqs.map(item => ({
      id: generateId(),
      step: item.step,
      description: item.description,
      image: item.image
    }))
  )

  recipeTip.value = newVal.tip
})  

const addRecipeSeq = () => {
  if (recipeSeq.length < 50) {
    recipeSeq.push({ 
      id: generateId(),
      step: recipeSeq.length + 1, 
      description: '' ,
      image: null
    })
  }
}

const removeRecipeSeq = (index) => {
  if (recipeSeq.length > 1) {
    recipeSeq.splice(index, 1)
    recipeSeq.forEach((item, i) => item.step = i + 1)
  }
}

const validation = () => {

  for (const item of recipeSeq) {
    if (item.description.trim().length < 5) return '요리순서는 최소 5자 이상 작성해주세요.';
    else if (!item.image) return '요리순서 이미지를 넣어주세요.';
  }

  return true;
}

const getData = () => {
  return {
    seqs: recipeSeq.map(item => ({
      step: item.step,
      description: item.description,
      image: item.image
    })),
    tip: recipeTip.value.trim() ? recipeTip.value : null
  }
}


// 부모가 사용할 수 있게 expose
defineExpose({
  validation,
  getData
})


// 재료 수량 250자 제한 (한글)
const handleItemDescriptionInput = (e, item) => commonInputHangle(e, 250, (value) => item.description = value)

const handleTipInput = (e) => commonInputHangle(e, 400, (value) => recipeTip.value = value)

</script>

<template>
<h2 class="sub-title">요리순서</h2>

  <ul class="seq-list">
    <li v-for="(item, index) in recipeSeq" :key="item.id"  class="seq-item-wrap">
      <div class="seq-item-box">
        <span class="seq-item-title">STEP {{ item.step }}</span>
        <div class="seq-item-filed">
          <v-textarea
            v-model="item.description"
            @input="handleItemDescriptionInput($event, item)"
            placeholder="예) 청경체"
            rows="7"
            no-resize
            variant="outlined"
            density="compact"
            hide-details=true
          ></v-textarea>
        </div>

        <div class="image-preview-wrap">
          <ImageUploader v-model="item.image" size-class="recipe-seq"/>
        </div>
      </div>


      <div class="seq-item-remove">
        <button 
        type="button"
        v-if="recipeSeq.length > 3" 
        @click="removeRecipeSeq(index)" 
        class="remove-button"
        >
          <font-awesome-icon :icon="['fas', 'xmark']"/>  
      </button>
      </div>
      
    </li>
  </ul>

  <div class="add-btn-wrap">
    <button type="button" @click="addRecipeSeq" class="add-btn">
      <font-awesome-icon :icon="['fas', 'plus']" class="add-icon"/>
      <span>순서 추가하기</span>
    </button>
  </div>


  <div class="label-title">
    <p>요리 팁 / 주의사항 </p>
  </div>
  
  <div class="seq-filed">
    <v-textarea
        v-model="recipeTip"
        placeholder="예) 된장찌개 끓이기"
        @input="handleTipInput($event, item)"
        rows="4"  
        no-resize
        variant="outlined"
        density="compact"
        hide-details=true
    ></v-textarea>
  </div>

</template>

<style lang="scss" scoped>
.sub-title {
  font-size: 1.2rem;
  color: #c09370;
  padding-bottom: 1.5rem;
}

.seq-list {
  margin-bottom: 2rem;
  

  .seq-item-wrap {
    display: flex;
    gap: 2rem;
    align-items: center;
    margin-bottom: 2rem;

    .seq-item-box {
      display: flex;
      gap: 2em;

      .seq-item-title {
        margin-right: 1rem;

      }

      .seq-item-filed {
        display: flex;
        width: 33.6rem;

        .textarea-filed {
        width: 100%;
        border: 1px solid #e7e7e7;
        border-radius: 0.5rem;
        padding: 0.5rem;
        resize: none;
        }

      }
    }

    .image-preview-wrap{
      width: 13rem;
      margin-left: 2rem;
    }

    .seq-item-remove {
      font-size: 1.4rem;
    }
  }

  .remove-button {
    background-color: transparent;
    border: none;
    font-weight: bold;
    color: #8c8c8c;
    cursor: pointer;
    font-size: 1.7rem;
  }
}

.label-title {
  padding-bottom: 0.6rem;
}

.seq-filed{
    padding-bottom: 2rem;
    
  .textarea-filed {
    width: 100%;
    border: 1px solid #e7e7e7;
    border-radius: 0.5rem;
    padding: 0.5rem;
    resize: none;
  }
}



.add-btn-wrap {
  text-align: center;
  border: none;
  margin-bottom: 1rem;
  color: #3c3c3c;
  font-size: 1.1rem;

  .add-btn {
    padding: 1rem;
  }
  
  .add-icon {
    margin-right: 1rem;
  }
}





</style>