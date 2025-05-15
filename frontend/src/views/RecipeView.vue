<script setup>
import MainHeader from '@/components/header/mainHeader/MainHeader.vue';

</script>

<template>
    <MainHeader />
    <div class="recipe-content">
        <router-view />
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