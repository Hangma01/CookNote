<script setup>
import BestRecipe from '@/components/main/BestRecipe.vue';
import RecentReipe from '@/components/main/RecentReipe.vue';
import RecommnetRecipe from '@/components/main/RecommnetRecipe.vue';
import SoloRecipe from '@/components/main/SoloRecipe.vue';
import { getBestRecipe, getRecentRecipe, getRecommentRecipe, getSoloRecipe } from '@/services/recipeService';
import { onMounted, ref } from 'vue';

const recommentRecipeData = ref(null);
const bestRecipeData = ref(null);
const soloRecipeData = ref(null);
const recentRecipeData = ref(null);

onMounted(async () => {
    try {
        const [recommentRecipeDataRes, bestRecipeDataRes, soloRecipeDataRes, recentRecipeDataRes] = await Promise.all([
            getRecommentRecipe(),
            getBestRecipe(),
            getSoloRecipe(),
            getRecentRecipe(),
        ]);
        recommentRecipeData.value = recommentRecipeDataRes.data;
        bestRecipeData.value = bestRecipeDataRes.data;
        soloRecipeData.value = soloRecipeDataRes.data;
        recentRecipeData.value = recentRecipeDataRes.data;
    } catch (e) {
        console.log(e);
    }
});
</script>

<template>
    <!-- 추천 레시피-->
    <RecommnetRecipe :recommentRecipeData="recommentRecipeData" />

    <!-- 베스트 레시피 -->
    <BestRecipe :bestRecipeData="bestRecipeData" />

    <!-- 혼먹 레시피 -->
    <SoloRecipe :soloRecipeData="soloRecipeData" />

    <!-- 최신 레시피피 -->
    <RecentReipe :recentRecipeData="recentRecipeData" />
</template>

<style lang="scss" scoped>
.image {
    width: 100%;
    height: 100%;
}
.section-title {
    h1 {
        font-size: 1.8rem;
        line-height: 2.8rem;
        letter-spacing: 0.32rem;
    }
    h2 {
        font-weight: normal;
        font-family: 'Noto Serif KR', serif, Helvetica, 'Helvetica Neue', Arial;
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