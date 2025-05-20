<script setup>
import RecipeInfo from "./RecipeInfo.vue";
import ReciepIngredient from "./ReciepIngredient.vue";
import RecipeSet from "./RecipeSet.vue";
import RecipeSeq from "./RecipeSeq.vue";
import { onMounted, ref } from "vue";
import { debounce } from "lodash";
import { commonValues } from "@/utils/commonValues";
import { getOriRecipe, editRecipe, saveRecipe } from '@/services/recipeService';
import { errorMessages } from "@/utils/messages/errorMessages";
import { useRoute, useRouter } from "vue-router";
import { getCategoryAll } from "@/services/categoryService";

// 화면 전환
const router = useRouter();

// 컴포넌트 별 Ref => 자식 데이터를 가져오기 위함
const recipeInfoRef = ref(null)
const recipeIngredienRef = ref(null)
const recipeSeqRef = ref(null)
const recipeSetRef = ref(null)

// 카테고리 데이터
const categories = ref(null)

// 레시피 수정 데이터
const originalRecipeData = ref(null)

// 수정 모드 인지 체크
const route = useRoute()
const recipeId = route.params.recipeId || null
const isEditMode = ref(!!recipeId) 

// 레시피 저장
const handleRecipeSave = debounce (async () => {

    // // 유효성 검사
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
            categoryCuisineId: recipeInfo.categories.cuisine,
            categoryPurposeId: recipeInfo.categories.purpose,
            recipeIngredients: recipeIngredients.ingredients,
            recipeSeqs: recipeSeq.seqs,
            status: recipeSet.data.toUpperCase(),
        }

        // 수정 시 원본 이미지 데이터 넣기기
        if (isEditMode.value) {
            formValues.recipeId = recipeId
            formValues.originalThumbnail = originalRecipeData.value.thumbnail
            formValues.originalRecipeSeqs = originalRecipeData.value.recipeSeqs
        }

        console.log(formValues)

        //서버에 전송
        try {
            if (isEditMode.value) {
                const editRes = await editRecipe(formValues)
            } else {
                const saveRes = await saveRecipe(formValues)
            }
            router.replace({ name: 'mainPage'});
        } catch (e) {
            if (e.response && e.response?.data?.message) {
                alert(e.response.data.message)  
            } else {
                alert(errorMessages.BADREQUEST)
            }
            window.location.reload();
        } 
  }
}, commonValues.defaultDebounce)

// 레시피 작성 / 수정 시 가져올 데이터
onMounted(async () => {

    if (isEditMode.value) { // 레시피 수정시요청 데이터터  
        try {
            const res = await getOriRecipe(recipeId)
            originalRecipeData.value = res.data
            categories.value = res.data.recipeCategories
        } catch (e) {
            if (e.response && e.response?.data?.message) {
                alert(e.response.data.message)  
            } else {
                alert(errorMessages.BADREQUEST)
            }
            router.push({ name: "mainPage" }) // 나중에 마이페이지로 이동해야함
        }
    } else {  // 레시피 작성시 요청 데이터
        try {
            const res = await getCategoryAll()
            console.log(res)
            categories.value = res.data
        } catch (e) {
            if (e.response && e.response?.data?.message) {
                alert(e.response.data.message)  
            } else {
                alert(errorMessages.BADREQUEST)
            }
            router.push({ name: "mainPage" })
        }
    }
})
</script>

<template>
    <v-form ref="formRef" @submit.prevent>
        <div class="recipe-write-container">
            <h1 class="title"> 레시피 작성 </h1>

            <div class="recipe-wrap">
                <section>
                    <RecipeInfo ref="recipeInfoRef" :originalRecipeData = "originalRecipeData" :categories="categories" :isEditMode="isEditMode" />
                </section>
            </div>
        
            <div class="recipe-wrap">
                <section>
                    <ReciepIngredient ref="recipeIngredienRef" :originalRecipeData = "originalRecipeData"/>
                </section>
            </div>

            <div class="recipe-wrap">
                <section>
                    <RecipeSeq ref="recipeSeqRef" :originalRecipeData = "originalRecipeData"/>
                </section>
            </div>
      
            <div class="recipe-wrap">
                <section>
                    <RecipeSet ref="recipeSetRef" :originalRecipeData = "originalRecipeData"/>
                </section>
            </div>

            <div class="write-btn-wrap">
                <div>
                    <button type="button" class="write-btn cancle-btn" @click="history.back()">작성 취소</button>
                </div>
            
                <div>
                    <button type="button" class="write-btn save-btn"  @click.prevent="handleRecipeSave()">저장</button>
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