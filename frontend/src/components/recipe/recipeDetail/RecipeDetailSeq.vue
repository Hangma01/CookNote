<script setup>
import { getDetailRecipe } from '@/services/recipeService';
import { onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

// 화면 전환
const router = useRouter();

const props = defineProps({
    recipeSeqs: {
        type: Object,
    },
});
</script>

<template>
    <div class="recipe-detail-seq-section">
        <div class="section-title">
            <p>레시피 순서</p>
        </div>

        <div class="recipe-seqs">
            <ul v-for="item in props.recipeSeqs" :key="item.step">
                <li class="seq-item">
                    <div class="seq-image-box">
                        <img :src="item.image" class="seq-image" />
                    </div>

                    <div class="step-box">
                        <div class="step">Step {{ item.step }}</div>

                        <div class="seq-content">
                            {{ item.description }}
                        </div>
                    </div>
                </li>
            </ul>
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

    .recipe-seqs {
        margin-top: 2rem;

        .seq-item {
            margin-bottom: 4rem;
            display: flex;
            gap: 2rem;
        }

        .seq-image-box {
            height: 13rem;
            flex: 1.3;

            .seq-image {
                width: 100%;
                height: 100%;
                border-radius: 1rem;
                border: 1px solid rgb(224, 224, 224);
            }
        }

        .step-box {
            flex: 2;
            .step {
                font-size: 1.1rem;
                padding-bottom: 0.5rem;
                font-weight: 500;
                border-bottom: 1px solid rgb(224, 224, 224);
            }

            .seq-content {
                margin-top: 1rem;
                font-size: 0.9rem;
                word-break: break-all;
                white-space: pre-wrap;
            }
        }
    }
}
</style>