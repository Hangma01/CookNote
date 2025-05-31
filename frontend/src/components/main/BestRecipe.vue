나의 말:
<script setup>
import Carousel from '../ui/Carousel.vue';
import RecipeCard from '../ui/RecipeCard.vue';

const props = defineProps({
    bestRecipeData: Object,
});
</script>

<template>
    <section>
        <div class="best-recipe-container">
            <div class="section-title">
                <h1>BEST RECIPES</h1>
            </div>

            <Carousel :items="bestRecipeData" :visibleCount="4" class="carousel">
                <template #slide="{ item }">
                    <div class="slide-item">
                        <RecipeCard :recipeData="item" />
                    </div>
                </template>
            </Carousel>
        </div>
    </section>
</template>

<style lang="scss" scoped>
.best-recipe-container {
    background-color: rgb(255, 249, 222);
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 5.5rem 5rem;

    .section-title {
        h1 {
            font-size: 1.8rem;
            line-height: 2.8rem;
            letter-spacing: 0.32rem;
            text-align: center;
        }
    }
    .carousel {
        margin-top: 6rem;
    }
}
</style>