<script setup>
</script>

<template>
    <!-- 추천 -->
    <section class="section-tag recommend-recipe-container">
        <div class="section-title">
            <h1>RECIPES FOR YOU</h1>
            <h2>다양한 레시피를 둘러보세요.</h2>
        </div>

        <div class="cardlist">
            <div>
                <img src="https://cooknote98.s3.ap-northeast-2.amazonaws.com/Recipe/Thumbnails/e28af3e7-d5eea474627c05b529719c37a400c60ca1.jpg" class="image"/>
            </div>

            <div>

            </div>

            <div>

            </div>

            <div>

            </div>
        </div>
    </section> 

    <!-- 베스트 레시피 -->
    <section>
        <div class="section-tag  best-recipe-container">
            <div>
                <div class="section-title">
                    <h1>BEST RECIPES</h1>
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
    <section>
        <div class="section-tag  solo-recipe-container">
            <div class="section-title">
                <h1>SOLO RECIPES</h1>
            </div>
        </div>
    </section>
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

.recommend-recipe-container {

    .cardlist {
        display: grid;
        width: 70rem;
        grid-template-columns: 49% 23.5% 23.5%;
        grid-template-rows: 1fr 1fr;
        margin: auto;
        gap: 20px;
        height: 25rem;
        margin-top: 6rem;
    }

    .cardlist > div:nth-of-type(1) {
        grid-area: 1 / 1 / 3 / 2;
        background-color: rgb(249, 255, 232);
    }

    .cardlist > div:nth-of-type(2) {
        grid-area: 1 / 2 / 2 / 3;
        background-color: rebeccapurple;
    }

    .cardlist > div:nth-of-type(3) {
        grid-area: 1 / 3 / 2 / 4;
        background-color: saddlebrown;
    }

    .cardlist > div:nth-of-type(4) {
        grid-area: 2 / 2 / 3 / 4;
        background-color:  rgb(249, 255, 232);;

        // rgb(232, 255, 232)
        //  rgb(249, 255, 232);
    }
}
</style>