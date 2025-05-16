<script setup>
import RecipeInfo from "./RecipeInfo.vue";
import ReciepIngredient from "./ReciepIngredient.vue";
import RecipeSet from "./RecipeSet.vue";
import RecipeSeq from "./RecipeSeq.vue";
import { ref } from "vue";
import { debounce } from "lodash";
import { commonValues } from "@/utils/commonValues";
import { saveRecipe } from '@/services/recipeService';
import { errorMessages } from "@/utils/messages/errorMessages";
import { toFormData } from "@/utils/toFormData";


const recipeInfoRef = ref(null)
const recipeIngredienRef = ref(null)
const recipeSeqRef = ref(null)
const recipeSetRef = ref(null)

// 레시피 저장
const handleRecipeSave = debounce (async () => {

  // 유효성 검사
  const recipeInfoValidResult = recipeInfoRef.value.validation()
  const recipeIngredientsValidResult =  recipeIngredienRef.value.validation()
  const recipeSeqValidResult = recipeSeqRef.value.validation();
  const recipeSetValidResult = recipeSetRef.value.validation();

  if(recipeInfoValidResult !== true) {
    alert(recipeInfoValidResult)
  } else if (recipeIngredientsValidResult !== true) {
    alert(recipeIngredientsValidResult)  
  } else if (recipeSeqValidResult !== true) {
    alert(recipeSeqValidResult)
  } else if (recipeSetValidResult !== true) {
    alert(recipeSetValidResult)
  } else {
    // 데이터 가져오기
    const recipeInfo = recipeInfoRef.value.getData()
    const recipeIngredients = recipeIngredienRef.value.getData()
    const recipeSeq = recipeSeqRef.value.getData()
    const recipeSet = recipeSetRef.value.getData()

    const formValues = {
      title: recipeInfo.title,
      description: recipeInfo.description,
      thumbnail: recipeInfo.thumbnail,
      videoId: recipeInfo.videoId,
      serving: recipeInfo.serving,
      duration: recipeInfo.duration,
      level: recipeInfo.level,
      tip: recipeSeq.tip,
      categories: recipeInfo.categories,
      ingredients: recipeIngredients.ingredients,
      recipeSeq: recipeSeq.seqs,
      status: recipeSet.data.toUpperCase(),
    }

    console.log(formValues)
    // 서버에 전송
    try {
      const res = await saveRecipe(formValues)
      console.log(res)
    } catch (e) {
      console.log(e)
      alert(errorMessages.LOGIN_ERROR)
    } 
  }
}, commonValues.defaultDebounce)

</script>

<template>
  <v-form ref="formRef">
    <div class="recipe-write-container">
      <h1 class="title"> 레시피 작성 </h1>

      <div class="recipe-wrap">
        <section>
          <RecipeInfo ref="recipeInfoRef" />
        </section>
      </div>
        
      <div class="recipe-wrap">
        <section>
            <ReciepIngredient ref="recipeIngredienRef" />
          </section>

      </div>

      <div class="recipe-wrap">
        <section>
            <RecipeSeq ref="recipeSeqRef" />
          </section>
      </div>
      
      <div class="recipe-wrap">
        <section>
          <RecipeSet ref="recipeSetRef" />
        </section>
      </div>
      
      <div class="write-btn-wrap">
        <div>
          <button class="write-btn cancle-btn" @click="history.back()">작성 취소</button>
        </div>

        <div>
          <button class="write-btn save-btn"  @click.prevent="handleRecipeSave()">저장</button>
        </div>
      </div>  
    </div>
    
  </v-form>
</template>


<style lang="scss" scoped>
.recipe-write-container {
  display: flex;
  flex-direction: column;
  gap: 4rem;

  .title {
    font-size: 1.4rem;
    margin-top: 1rem;
    font-size: 1.4rem;
  }
  
  .recipe-wrap {
    border: 3px solid #f1f1f2;
    border-radius: 1rem;
    padding: 1rem 2.5rem;
  }

  .write-btn-wrap{
    display: flex;
    justify-content: end;
    gap: 2rem;

    .cancle-btn {
      height: 5rem;
      border: 1px solid #a9a9a9;
      border-radius: 1rem;
    }

    .write-btn {
      width: 8rem;
      height: 3.5rem;
      border: 1px solid;
      border-radius: 1rem;
      font-size: 1.1rem;
    }

    .save-btn {
      background-color: #c09370;
      border: 1px solid #a57954;
      color: white;
      margin-right: 1.5rem;
    }
    
  }
}


</style>