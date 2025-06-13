

<script setup>
const props = defineProps({
    soloRecipeData: Object,
});
</script>

<template>
    <section>
        <div class="section-tag solo-recipe-container">
            <div class="section-title">
                <h1>SOLO RECIPES</h1>
                <h2>혼먹 자취생들을 위한 추천 레시피</h2>
            </div>

            <div>
                <ul class="cardlist">
                    <li v-for="item in props?.soloRecipeData" :key="item.recipeId" class="card">
                        <router-link :to="{ name: 'recipeDetail', params: { recipeId: item.recipeId } }">
                            <img :src="item.recipeThumbnail" class="image" />
                        </router-link>

                        <div class="title">
                            <span>{{ item.recipeTitle }}</span>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
    </section>
</template>

<style lang="scss" scoped>
.solo-recipe-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 5.5rem 10rem;

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
        grid-template-columns: 23.5% 23.5% 49%;
        grid-template-rows: 1fr 1fr;
        margin: auto;
        gap: 20px;
        height: 27rem;
    }

    .card {
        height: 100%;
        overflow: hidden; // 넘치는 이미지 잘라냄
        position: relative;
        transition: transform 0.3s ease;

        .title {
            position: absolute;
            bottom: 10px;
            left: 10px;
            color: white; // 배경색 대비를 위해
            font-size: 1.2rem;
            font-weight: bold;
            padding: 0.5rem;
            border-radius: 0.3rem;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
            width: 100%;
            text-shadow: rgba(0, 0, 0, 0.25) 0px 0.2rem 0.2rem;
            pointer-events: none;
        }
    }

    .card:hover {
        transform: scale(1.03);
    }

    .cardlist > .card:nth-of-type(1) {
        grid-area: 1 / 1 / 2 / 2;
        background-color: rgb(249, 255, 232);
    }

    .cardlist > .card:nth-of-type(2) {
        grid-area: 1 / 2 / 2 / 3;
        background-color: rebeccapurple;
    }

    .cardlist > .card:nth-of-type(3) {
        grid-area: 1 / 3 / 3 / 4;
        background-color: saddlebrown;
    }

    .cardlist > .card:nth-of-type(4) {
        grid-area: 2 / 1 / 3 / 3;
        background-color: rgb(249, 255, 232);
    }

    .image {
        width: 100%;
        height: 100%;
        object-fit: fill;
        border: 1px solid rgba(147, 112, 98, 0.6);
    }
}
</style>