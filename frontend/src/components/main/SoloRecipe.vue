

<script setup>
import { Swiper, SwiperSlide } from 'swiper/vue';
import { Navigation } from 'swiper/modules'; 
import 'swiper/css';
import 'swiper/css/navigation';
import RecipeCard from '../ui/RecipeCard.vue';

const props = defineProps({ 
    soloMealRecipeData: Object ,  
})

</script>

<template>
    <section>
        <div class="section-tag  solo-recipe-container">
            <div class="section-title">
                <h1>SOLO RECIPES</h1>
                <h2>자취생을 위한 추천 레시피</h2>
            </div>
            
            <div class="solo-recipe-slide">
                 <Swiper
                :modules="[Navigation]"
                :slides-per-view="4"
                navigation
                class="solo-swiper"
                :watch-overflow="false" 
                >
                    <SwiperSlide
                        v-for="(item,index) in soloMealRecipeData"
                        :key="item.recipeId"
                    >
                        <v-card  height="320" width="250">
                            <RecipeCard :recipeData="item" />
                        </v-card>
                    </SwiperSlide>
                </Swiper>
            </div>
        </div>
    </section>
    
</template>

<style lang="scss" scoped>
.solo-recipe-container{
    
    background-color: rgb(255, 249, 222);
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
}


.section-tag { 
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 5.5rem 10rem;

    .solo-recipe-slide {
        width: 79rem;
        margin-top: 6rem;

        .solo-swiper {
            padding-left: 5rem;  /* 왼쪽 버튼과 슬라이드 사이 여백 */
            padding-right: 3.5rem;
        }

        // Swiper 버튼 위치 조정
        ::v-deep(.swiper-button-prev),
        ::v-deep(.swiper-button-next) {
            color: rgb(147, 112, 98); // 버튼 색상
            top: 50%;
            transform: translateY(-50%);
            z-index: 100;
        }

        ::v-deep(.swiper-button-prev.swiper-button-disabled),
        ::v-deep(.swiper-button-next.swiper-button-disabled) {
        opacity: 0;      /* 비활성화 시 완전 투명 */
        pointer-events: none;  /* 클릭 방지 */
        }

    }
}

</style>