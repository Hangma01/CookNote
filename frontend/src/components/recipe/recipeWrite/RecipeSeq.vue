<script setup>
import ImageUploader from '@/components/image/ImageUploader.vue'
import { ref } from 'vue'
import { generateId } from '@/utils/commonFunction'

const seqs = ref([
                        { id:generateId() , step: 1, description: '', image: null },
                        { id: generateId(), step: 2, description: '', image: null },
                        { id: generateId(), step: 3, description: '', image: null },
                      ])

const addRecipeSeq = () => {
  if (seqs.value.length < 50) {
    seqs.value.push({ 
      id: generateId(),
      step: seqs.value.length + 1, 
      description: '' ,
      image: null
    })
  }
}



const removeIngredient = (index) => {
  if (ingredients.value.length > 1) {
    ingredients.value.splice(index, 1)
  }
}
const removeSeq = (index) => {
  if (seqs.value.length > 1) {
    seqs.value.splice(index, 1)
    seqs.value.forEach((item, i) => item.step = i + 1)
  }
}

const getData = () => {
  return {
    seqs: seqs.value.map(item => ({
      step: item.step,
      description: item.description,
      image: item.image
    }))
  }
}

// 부모가 사용할 수 있게 expose
defineExpose({
  seqs
})
</script>

<template>
<h2 class="sub-title">요리순서</h2>

  <ul class="seq-list">
    <li v-for="(item, index) in seqs" :key="item.id"  class="seq-item-wrap">
      <div class="seq-item-box">
        <span class="seq-item-title">STEP {{ index + 1}}</span>
        <div class="seq-item-filed">
          <textarea
            v-model="item.description"
            placeholder="예) 청경체"      
            class="textarea-filed"
          ></textarea>
        </div>

        <div class="image-preview-wrap">
          <ImageUploader v-model="item.image" size-class="recipe-seq"/>
        </div>
      </div>


      <div class="seq-item-remove">
        <button 
        type="button"
        v-if="seqs.length > 1" 
        @click="removeSeq(index)" 
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