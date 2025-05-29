<script setup>

const props = defineProps({ 
    recommentRecipeData: Object ,  
})



</script>

<template>
    <section class="section-tag recommend-recipe-container">
        <div class="section-title">
            <h1>Recipe FOR YOU</h1>
            <h2>추천 레시피를 둘러보세요.</h2>
            
        </div>
        
        <div>
            <ul class="cardlist">
                <li 
                v-for="(item) in props?.recommentRecipeData"
                :key="item.recipeId"
                class="card"
                >
                    <router-link :to="{ name : 'recipeDetail',  params: { recipeId: item.recipeId } }">                        
                        <img :src="item.recipeThumbnail" class="image"/>
                    </router-link>     

                    <div class="title">
                        <span>{{ item.recipeTitle }}</span>
                    </div>
                </li>
            </ul>
        </div>
    </section> 
</template>

<style lang="scss" scoped>


.section-title{

    h1 {
        font-size: 1.8rem;
        line-height: 2.8rem;
        letter-spacing: 0.32rem;
        text-align: center;
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

.recommend-recipe-container {

    .cardlist {
        display: grid;
        width: 70rem;
        grid-template-columns: 49% 23.5% 23.5%;
        grid-template-rows: 1fr 1fr;
        margin: auto;
        gap: 20px;
        height: 27rem;
        margin-top: 6rem;
    }

    .card{
        height: 100%;
        overflow: hidden; // 넘치는 이미지 잘라냄
        position: relative;

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
    .cardlist > .card:nth-of-type(1) {
        grid-area: 1 / 1 / 3 / 2;
        background-color: rgb(249, 255, 232);
    }

    .cardlist > .card:nth-of-type(2) {
        grid-area: 1 / 2 / 2 / 3;
        background-color: rebeccapurple;
    }

    .cardlist > .card:nth-of-type(3) {
        grid-area: 1 / 3 / 2 / 4;
        background-color: saddlebrown;
    }

    .cardlist > .card:nth-of-type(4) {
        grid-area: 2 / 2 / 3 / 4;
        background-color:  rgb(249, 255, 232);;
    }

    .image {
        width: 100%;
        height: 100%;
        object-fit: fill;
        border: 1px solid rgba(147, 112, 98, 0.6);
    }
}
</style>