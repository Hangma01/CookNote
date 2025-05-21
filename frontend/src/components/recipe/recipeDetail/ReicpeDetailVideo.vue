<script setup>
import { getDetailRecipe } from "@/services/recipeService";
import { onMounted, ref } from "vue";
import { useRoute, useRouter } from "vue-router";


// 화면 전환
const router = useRouter();


const props = defineProps({ 
    recipeDetailData: { 
        type: Object    
    },
})

</script>

<template>
    <div class="recipe-detail-video-section">
        <div class="section-title">
            <p>동영상</p>
        </div>
    </div>
</template>


<style lang="scss" scoped>
.recipe-detail-video-section {
    margin-top: 5rem;

    .section-title {
        font-size: 1.2rem;
        font-weight: bold;
        border-bottom: 2px solid rgb(147, 112, 98);
        padding-bottom: 1rem;
        margin-bottom: 0.7em
    }
}
</style>