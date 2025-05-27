<script setup>
import { commonValues } from '@/utils/commonValues';


const props = defineProps({ 
    recipeData: { // 레시피 수정 시 오는 데이터
        type: Object    
    },
    recipeStatus: {
        type: String,
        default: commonValues.PUBLIC_TEXT
    }
})
</script>

<template>
    <div class="recipe-image">
        <router-link
            :to="{ name: 'recipeDetail', params: { recipeId: props.recipeData?.recipeId } }"
        >
            <img :src="props.recipeData?.thumbnail" class="image" />
        </router-link>
    </div>
    <div class="recipe-info">
        <div class="recipe-title">
            <router-link 
                :to="{name : 'recipeDetail', params: { recipeId: props.recipeData?.recipeId } }">
                <span>{{ props.recipeData?.title }}</span>
            </router-link>
        </div>

        <div class="recipe-summary">
            <p>{{ props.recipeData?.description }}</p>
        </div>

        <div class="recipe-date">
            <span>{{ props.recipeData?.createAt }}</span>
        </div>
    </div>          
</template>


<style lang="scss" scoped>

.recipe-image {
    
    border: 1px solid rgb(144, 144, 144);
    flex: 0.8;
    height: 7rem;

    .image {
        width: 100%;
        height: 100%;
    }
}
.recipe-info {
    flex: 2.5;
    margin-left: 1rem;

    .recipe-title {
        font-size: 1.1rem;
        color: #333333;
        margin-bottom: 0.4rem;
        overflow: hidden;
        text-overflow: ellipsis;
        word-break: break-word;
        display: -webkit-box;
        -webkit-line-clamp: 1;
        -webkit-box-orient: vertical;
        font-weight: bold;
    }


    .recipe-summary {
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        line-clamp: 2;
        -webkit-box-orient: vertical;
        word-break: break-word;
        font-size: 0.8rem;
        color: #777777;
        margin-bottom: 0.3rem;
        line-height: 1.2rem;
    }

    .recipe-date {
        font-size: 0.8rem;
        color: #999999;
    }
}

</style>


