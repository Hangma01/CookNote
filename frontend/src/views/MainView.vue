<script setup>
import RecommnetRecipe from '@/components/main/RecommnetRecipe.vue';
import SoloRecipe from '@/components/main/SoloRecipe.vue';
import { getRecommentRecipe, getSoloMealRecipe } from '@/services/recipeService';
import { onMounted, ref } from 'vue';


const recommentRecipeData = ref(null)
const soloMealRecipeData = ref(null)

onMounted(async () => {
    try {
        
        const [
                recommentRecipeDataRes,
                soloMealRecipeDataRes,    
        ] = await Promise.all([
                getRecommentRecipe(),
                getSoloMealRecipe()
        ]);
        recommentRecipeData.value = recommentRecipeDataRes.data
        soloMealRecipeData.value = soloMealRecipeDataRes.data
        console.log(soloMealRecipeDataRes)
    } catch (e) {
        console.log(e)
    }
})


</script>

<template>
    <!-- 추천 -->
    <RecommnetRecipe :recommentRecipeData = "recommentRecipeData" />

    <SoloRecipe :soloMealRecipeData = "soloMealRecipeData" />
    <!-- 베스트 레시피 -->
    <section>
        <div class="section-tag  best-recipe-container">
            <div>
                <div class="section-title">
                    <h1>RECENT RECIPES</h1>
                </div>
            </div>
        </div>
    </section>

    <!-- 다이어트 레시피 -->
    <section>
        <div class="section-tag  diet-recipe-container">
            <div class="section-title">
                <h1>DIET RECIPES</h1>
            </div>
        </div>
    </section>

    <!-- 혼밥 레시피 -->

</template>

<style lang="scss" scoped>

.image {
    width: 100%;
    height: 100%;
}
.section-title{

    h1 {
        font-size: 1.8rem;
        line-height: 2.8rem;
        letter-spacing: 0.32rem;
    }
    h2 {
        font-weight: normal;
        font-family: "Noto Serif KR", serif, Helvetica, "Helvetica Neue", Arial;
        font-size: 1.3rem;
        text-align: center;
        margin-top: 2rem;
    }
}

.section-tag { 
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 5.5rem 10rem;
}


</style>