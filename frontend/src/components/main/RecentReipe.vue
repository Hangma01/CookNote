<script setup>
import RecipeCard from '../ui/RecipeCard.vue';

const props = defineProps({
    recentRecipeData: Object,
});
</script>

<template>
    <section>
        <div class="section-tag recent-recipe-container">
            <div>
                <div class="section-title">
                    <h1>RECENT RECIPES</h1>
                </div>

                <div>
                    <ul class="cardlist">
                        <li v-for="item in props?.recentRecipeData" :key="item.recipeId" class="card">
                            <RecipeCard :recipeData="item" />
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </section>
</template>

<style lang="scss" scoped>
.recent-recipe-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 5.5rem 10rem;
    background-color: rgb(244, 240, 239);
    .section-title {
        margin-bottom: 5.5rem;

        h1 {
            font-size: 1.8rem;
            line-height: 2.8rem;
            letter-spacing: 0.32rem;
            text-align: center;
        }
        h2 {
            font-weight: normal;
            font-family: 'Noto Serif KR', serif, Helvetica, 'Helvetica Neue', Arial;
            font-size: 1.3rem;
            text-align: center;
            margin-top: 2rem;
        }
    }

    .cardlist {
        display: grid;
        width: 70rem;
        grid-template-columns: repeat(4, 1fr);
        row-gap: 4rem;
        column-gap: 2rem;
        margin: auto;
    }
}
</style>