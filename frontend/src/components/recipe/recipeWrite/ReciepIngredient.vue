<script setup>
import { recipeIngredientForm } from '@/composables/recipes/recipeIngredientForm';


const { 
  ingredients, 
  addIngredient, 
  removeIngredient, 
  validation, 
  getData, 
} = recipeIngredientForm();

// 부모가 사용할 수 있게 expose
defineExpose({
  validation,
  getData
})
</script>

<template>
<h2 class="sub-title">재료정보</h2>

  <ul class="ingredient-list">
    <li v-for="(item, index) in ingredients" :key="item.id"  class="ingredient-item-wrap">
      <div class="ingredient-item-box">
        <div class="ingredient-item-filed">
          <span class="ingredient-item-title">재료</span>
          <input
            v-model="item.name "
            type="text"
            placeholder="예) 청경체"
            class="input-filed"
          />
        </div>

        <div class="ingredient-item-filed">
          <span class="ingredient-item-title">수량</span>
            <input
            v-model="item.quantity "
            type="text"
            placeholder="200g"
            class="input-filed"
          />
        </div>

        <div class="ingredient-item-filed">
          <span class="ingredient-item-title">비고</span>
          <input
            v-model="item.remark "
            type="text"
            placeholder="예) 비고"
            class="input-filed"
          />
        </div>
      </div>


      <div class="ingredient-item-remove">
        <button 
        type="button"
        v-if="ingredients.length > 3" 
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


      .ingredient-item-filed {
        display: flex;
        align-items: center;
        width: 18rem;

        .ingredient-item-title {
        margin-right: 1rem;
      }


        .input-filed {
        border: 1px solid #e7e7e7;
        border-radius: 0.5rem;
        padding: 0.5rem;
      }

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