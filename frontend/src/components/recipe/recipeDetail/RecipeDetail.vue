<script setup>
import { getDetailRecipe } from "@/services/recipeService";
import { onMounted, ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import RecipeDetailInfo from "./RecipeDetailInfo.vue";
import ReicpeDetailVideo from "./ReicpeDetailVideo.vue";
import RecipeDetailIngredient from "./RecipeDetailIngredient.vue";
import RecipeDetailSeq from "./RecipeDetailSeq.vue";
import RecipeDetailCommend from "./RecipeDetailCommend.vue";

// 화면 전환
const router = useRouter();

// RecipeId 찾기
const route = useRoute()
const recipeId = route.params.recipeId || null

const recipeDetailData = ref(null);


// 레시피 작성 / 수정 시 가져올 데이터
onMounted(async () => {
    try {
        const res = await getDetailRecipe(recipeId)
        recipeDetailData.value = res.data;
        console.log(res.data)
    } catch (e) {
        router.push({name:'notFound'})
    }
    
})
</script>

<template>
    <div class="recipe-detail-container">
        <section>
            <RecipeDetailInfo :recipeDetailData = "recipeDetailData" />
        </section>
            
        <section>
            <ReicpeDetailVideo />
        </section>
            
        <section>
            <RecipeDetailIngredient :recipeIngredients = "recipeDetailData?.recipeIngredients"/>
        </section>

        <section>
            <RecipeDetailSeq :recipeSeqs = "recipeDetailData?.recipeSeqs"/>
        </section>

        <section>
            <RecipeDetailCommend />
        </section>
    </div>
</template>


<style lang="scss" scoped>
.recipe-detail-container {
    width: 43rem;
    margin: auto;
    margin-bottom: 6rem;
    margin-top: 3rem;
}


</style>