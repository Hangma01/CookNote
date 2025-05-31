<script setup>
import { getDetailRecipe } from '@/services/recipeService';
import { onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

// 화면 전환
const router = useRouter();

const props = defineProps({
    recipeTip: {
        type: String,
    },
});
</script>

<template>
    <div class="recipe-detail-seq-section" v-if="props?.recipeTip">
        <div class="section-title">
            <p>요리팁</p>
        </div>

        <div class="recipe-tip">
            <p>
                {{ props?.recipeTip }}
            </p>
        </div>
    </div>
</template>


<style lang="scss" scoped>
.recipe-detail-seq-section {
    margin-top: 5rem;

    .section-title {
        font-size: 1.2rem;
        font-weight: bold;
        border-bottom: 2px solid rgb(147, 112, 98);
        padding-bottom: 1rem;
        margin-bottom: 0.7em;
    }

    .recipe-tip {
        white-space: pre-wrap;
        font-size: 0.9rem;
    }
}
</style>