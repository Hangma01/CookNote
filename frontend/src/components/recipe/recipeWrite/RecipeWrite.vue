<script setup>
import RecipeInfo from "./RecipeInfo.vue";
import ReciepIngredient from "./ReciepIngredient.vue";
import RecipeSeq from "./RecipeSeq.vue";
import { ref } from "vue";

const recipeInfoRef = ref(null)
const recipeIngredienRef = ref(null)
const recipeSeqRef = ref(null)

const handleRecipeSave = (type) => {
  const info = recipeInfoRef.value?.getData?.()
  const ingredients = recipeIngredienRef.value?.getData?.()
  const seq = recipeSeqRef.value?.seqs

  // 예시: 모든 정보 통합
  const payload = {
    title: info.title,
    description: info.desciption,
    videoId: info.videoId,
    serving: info.serving,
    level: info.level,
    categories: info.categories,
    thumbnail: info.thumbnail
  }
  console.log(payload)
}
</script>

<template>
  <v-form ref="formRef" @submit.prevent="handleRecipeSave">
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
      
      
      <div class="write-btn-wrap">
        <div>
          <button class="write-btn cancle-btn" @click="history.back()">작성 취소</button>
        </div>

        <div>
          <button class="write-btn private-save-btn"  @click.prevent="handleRecipeSave('private')">저장</button>
          <button class="write-btn public-save-btn" @click.prevent="handleRecipeSave('public')">저장 후 공개하기</button>
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
    justify-content: space-between;

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

    .private-save-btn {
      background-color: #c09370;
      border: 1px solid #a57954;
      color: white;
      margin-right: 1.5rem;
    }

    .public-save-btn {
      width: 12rem;
      background-color: #a57954;
      border: 1px solid #c09370;
      color: white;
    }
    

  }
}


</style>