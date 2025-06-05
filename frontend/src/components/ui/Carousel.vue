<script setup>
import { ref, computed } from 'vue';

const props = defineProps({
    items: {
        type: Array,
        required: true,
    },
    visibleCount: {
        type: Number,
        default: 4,
    },
});

// totalSlides를 computed로 처리, items가 null이면 빈 배열로 취급
const totalSlides = computed(() => (props.items ? props.items.length : 0));

const currentIndex = ref(0);

const prevSlide = () => {
    if (currentIndex.value > 0) {
        currentIndex.value = Math.max(0, currentIndex.value - props.visibleCount);
    }
};

const nextSlide = () => {
    if (currentIndex.value < totalSlides.value - props.visibleCount) {
        currentIndex.value = Math.min(totalSlides.value - props.visibleCount, currentIndex.value + props.visibleCount);
    }
};

const visibleSlides = computed(() => {
    const slides = [];
    if (!props.items || totalSlides.value === 0) return slides;

    for (let i = 0; i < props.visibleCount; i++) {
        const idx = currentIndex.value + i;
        if (idx >= totalSlides.value) break;
        slides.push(props.items[idx]);
    }
    return slides;
});
</script>

<template>
    <div class="simple-carousel">
        <button @click="prevSlide" class="arrow prev" :disabled="currentIndex === 0">
            <font-awesome-icon :icon="['fas', 'angle-left']" />
        </button>

        <div class="slides-wrapper">
            <div v-for="(item, idx) in visibleSlides" :key="idx" class="slide-wrapper">
                <slot name="slide" :item="item"></slot>
            </div>
        </div>

        <button @click="nextSlide" class="arrow next" :disabled="currentIndex >= totalSlides - visibleCount">
            <font-awesome-icon :icon="['fas', 'angle-right']" />
        </button>
    </div>
</template>

<style lang="scss" scoped>
.simple-carousel {
    display: flex;
    align-items: center;
    justify-content: center;
    position: relative;
    width: 75rem;

    .slides-wrapper {
        display: flex;
        width: 100%;
        gap: 2rem;

        .slide-wrapper {
            background-color: white;
            flex: 0 0 auto;
            max-width: 16rem; /* 슬라이드 아이템 너비 */
            padding-bottom: 1rem;
            border-radius: 0.8rem;
        }
    }

    .arrow {
        font-size: 3rem;
        background: none;
        border: none;
        cursor: pointer;
        user-select: none;
        padding: 0 1rem;
        color: #333;
        transition: color 0.3s;
    }

    .arrow:hover {
        color: #555;
    }

    .arrow:disabled {
        color: #ccc;
        cursor: default;
    }
}
</style>
