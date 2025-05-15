<script setup>
import { ref } from 'vue'

const ingredients = ref([
                        { name: '', quantity: '' , remark: ''},
                        { name: '', quantity: '' , remark: ''},
                        { name: '', quantity: '' , remark: ''},
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

// ✅ 부모에 데이터를 넘기기 위해 expose
defineExpose({
  getIngredients: () => ingredients.value
})
</script>

<template>
<h2 class="sub-title">재료정보</h2>

  <ul class="ingredient-list">
    <li v-for="(item, index) in ingredients" :key="index"  class="ingredient-item-wrap">
      <div class="ingredient-item-box">
        <div class="ingredient-item-filed">
          <span class="ingredient-item-title">재료</span>
          <v-text-field
            v-model="item.name "
            type="text"
            label=""
            placeholder="예) 청경체"
            variant="outlined"
            density="compact"
            hide-details=true
          />
        </div>

        <div class="ingredient-item-filed">
          <span class="ingredient-item-title">수량</span>
                    <v-text-field
            v-model="item.amount "
            type="text"
            label=""
            placeholder="200g"
            variant="outlined"
            density="compact"
            hide-details=true
          />
        </div>

        <div class="ingredient-item-filed">
          <span class="ingredient-item-title">비고</span>
          <v-text-field
            v-model="item.amount "
            type="text"
            label=""
            placeholder="예) 비고"
            variant="outlined"
            density="compact"
            hide-details=true
          />
        </div>
      </div>


      <div class="ingredient-item-remove">
        <button 
        type="button"
        v-if="ingredients.length > 1" 
        @click="removeIngredient(index)" 
        class="remove-button"
        >
          <font-awesome-icon :icon="['fas', 'xmark']"/>  
      </button>
      </div>
      
    </li>
  </ul>

  <div class="add-btn-wrap">
    <button type="button" @click="addIngredient" class="add-btn">
      <font-awesome-icon :icon="['fas', 'plus']" class="add-icon"/>
      <span>재료 추가하기</span>
    </button>
  </div>

</template>

<style lang="scss" scoped>
.sub-title {
  font-size: 1.2rem;
  color: #c09370;
  padding-bottom: 1.5rem;
}

.ingredient-list {
  margin-bottom: 2rem;

  .ingredient-item-wrap {
    display: flex;
    gap: 1rem;
    align-items: center;
    margin-bottom: 1rem;

    .ingredient-item-box {
      display: flex;
      gap: 2rem;

      .ingredient-item-title {
        margin-right: 1rem;
      }

      .ingredient-item-filed {
        display: flex;
        align-items: center;
        width: 18rem;
      }
    }

    .ingredient-item-remove {
      font-size: 1.4rem;
    }
  }

  .remove-button {
    background-color: transparent;
    border: none;
    font-weight: bold;
    color: #8c8c8c;
    cursor: pointer;
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