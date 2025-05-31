<script setup>
import { getDetailRecipe } from '@/services/recipeService';
import { onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

// 화면 전환
const router = useRouter();

const props = defineProps({
    recipeIngredients: {
        type: Object,
    },
});
</script>

<template>
    <div class="recipe-detail-ingredient-section">
        <div class="section-title">
            <p>재료</p>
        </div>

        <div class="item-grid">
            <ul v-for="(item, index) in props.recipeIngredients" :key="index" class="item-box">
                <li class="item-list">
                    <div class="ingredients-item">
                        <div>
                            <span>{{ item.name }}</span>
                        </div>

                        <div class="igredients-quantity">
                            <span>{{ item.quantity }}</span>
                        </div>
                    </div>

                    <div class="igredients-remark" v-if="item.remark">
                        <font-awesome-icon :icon="['far', 'circle-check']" class="remark-icon" />
                        <span>{{ item.remark }}</span>
                    </div>
                </li>
            </ul>
        </div>
    </div>
</template>


<style lang="scss" scoped>
.recipe-detail-ingredient-section {
    margin-top: 5rem;

    .section-title {
        font-size: 1.2rem;
        font-weight: bold;
        border-bottom: 2px solid rgb(147, 112, 98);
        padding-bottom: 1rem;
        margin-bottom: 0.7em;
    }

    .item-grid {
        column-count: 2;
        column-gap: 2rem;
    }

    .item-box {
        break-inside: avoid;
        padding: 0 0.3rem;
        padding-top: 0.5rem;
        font-size: 0.85rem;
        border-bottom: 1px solid rgb(189, 189, 189);

        .item-list {
            padding-bottom: 0.5rem;

            .ingredients-item {
                display: flex;
                justify-content: space-between;

                .igredients-quantity {
                    color: rgb(117, 117, 117);
                }
            }
            .igredients-remark {
                font-size: 0.8rem;
                color: rgb(117, 117, 117);

                .remark-icon {
                    padding-right: 0.3rem;
                    padding-top: 0.2rem;
                }
            }
        }
    }
}
</style>