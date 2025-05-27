<script setup>
import { commonInputHangle, generateId } from '@/utils/commonFunction';
import { reactive, watch } from 'vue';

const props = defineProps({ 
  originalRecipeData: { // 레시피 수정 시 오는 데이터
    type: Object
  },
})

const ingredients = reactive([
                        { id: generateId(), name: '', quantity: '' , remark: ''},
                        { id: generateId(), name: '', quantity: '' , remark: ''},
                        { id: generateId(), name: '', quantity: '' , remark: ''},
                      ])


watch(() => props.originalRecipeData, (newVal) => {
    ingredients.splice(0, ingredients.length, 
      ...newVal.recipeIngredients.map(item => ({
        id: generateId(),
        name: item.name,
        quantity: item.quantity,
        remark: item.remark
      }))
    )
})                      

  const addIngredient = () => {
    if (ingredients.length < 40) {
      ingredients.push({ name: '', quantity: '' })
    }
  }

  const removeIngredient = (index) => {
    if (ingredients.length > 1) {
      ingredients.splice(index, 1)
    }
  }

  const validation = () => {

    if (ingredients.some(item => {
      return (!item.name?.trim() || !item.quantity?.trim());
    })) {
      return '재료와 수량을 모두 입력하세요'
    }

    return true;
  }

  const getData = () => {
    return {
      ingredients: ingredients.map(item => ({
        name: item.name.trim(),
        quantity: item.quantity.trim(),
        remark: item.remark?.trim() || null
      }))
    }
  }

// 부모가 사용할 수 있게 expose
defineExpose({
  validation,
  getData
})


// 재료 이름 20자 제한 (한글)
const handleItemNameInput = (e, item) => commonInputHangle(e, 20, (value) => item.name = value)

// 재료 수량 10자 제한 (한글)
const handleItemQuantityInput = (e, item) => commonInputHangle(e, 10, (value) => item.quantity = value)

// 재료 비고 20자 제한 (한글)
const handleItemRemarkInput = (e, item) => commonInputHangle(e, 20, (value) => item.remark = value)

</script>

<template>
<h2 class="sub-title">
	<span>재료정보</span>

	<p class="required">ⓘ 재료, 수량은 필수 입력 항목입니다.</p>
</h2>

 	<ul class="ingredient-list">
    	<li v-for="(item, index) in ingredients" :key="item.id"  class="ingredient-item-wrap">
      		<div class="ingredient-item-box">
        		<div class="ingredient-item-filed">
					<span class="ingredient-item-title">재료</span>
					<v-text-field
						v-model="item.name"
						@input="handleItemNameInput($event, item)"
						type="text"
						placeholder="20자 이내로 작성해주세요."
						variant="outlined"
						density="compact"
						hide-details=true
					/>
        		</div>

				<div class="ingredient-item-filed">
					<span class="ingredient-item-title">수량</span>
					<v-text-field
						v-model="item.quantity"
						@input="handleItemQuantityInput($event, item)"
						type="text"
						placeholder="10자 이내로 작성해주세요."
						variant="outlined"
						density="compact"
						hide-details=true
					/>
				</div>

				<div class="ingredient-item-filed ">
					<span class="ingredient-item-title">비고</span>
					<v-text-field
						v-model="item.remark"
						@input="handleItemRemarkInput($event, item)"
						type="text"
						placeholder="20자 이내로 작성해주세요."
						variant="outlined"
						density="compact"
						hide-details=true
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

	<div class="add-btn-wrap" v-if="ingredients.length < 40">
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


	.required {
		font-size: 0.8rem;
		color: #777;
		font-weight: 400;
	}
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

      	.ingredient-item-filed.required::before {
        	position: absolute;
    	    content: '*';
        	color: red;
        	margin-right: 0.25rem;
        	font-weight: bold;
        	left: -0.7rem;
      	}

      	.ingredient-item-filed {
        	position: relative;
        	display: flex;
        	align-items: center;
        	width: 18rem;
        

        	.ingredient-item-title {
            	margin-right: 1rem;
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