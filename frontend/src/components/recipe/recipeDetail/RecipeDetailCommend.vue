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
    <div class="recipe-detail-comment-section">
        <div class="section-title">
            <p>댓글 6</p>
        </div>

        <div class="recipe-seqs">
            <div
                v-for="(item) in recipeSeqs"
                :key="item.step"
                class="seq-item"
            >
                <div class="seq-image-box">
                    <img :src="item.image" class="seq-image"/>
                </div>

                <div class="step-box">
                    <div class="step">Step {{ item.step }}</div>

                    <div class="seq-content">
                        {{ item.description }}
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>


<style lang="scss" scoped>
.recipe-detail-comment-section {
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