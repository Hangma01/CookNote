<script setup>
import { computed } from 'vue';
import { RouterView, useRoute } from 'vue-router';

const route = useRoute()

const routerKey = computed(() => {
  // meta에 forceRemount가 있을 경우에만 key 부여
  return route.meta.forceRemount ? `${route.name}-${route.params.recipeId || 'new'}` : undefined
})
</script>

<template>
    <div class="recipe-content" :key="routerKey">
        <RouterView />
    </div>
</template>

<style scoped>
.recipe-content {
    display: flex;
    width: 70rem;
    margin: 0 auto;
    padding-top: 0.8rem;
    padding-bottom: 2rem;
    flex-direction: column;
}
</style>