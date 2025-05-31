<script setup>
import { RouterView } from 'vue-router';
</script>

<template>
    <div class="recipe-content">
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