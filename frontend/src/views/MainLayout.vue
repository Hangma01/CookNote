<script setup>
import Footer from '@/components/footer/Footer.vue';
import MainHeader from '@/components/header/mainHeader/MainHeader.vue';
</script>

<template>
    <MainHeader />
    <RouterView class="view" />
    <Footer />
</template>

<style scoped>
.view {
    margin-bottom: 10rem;
}
</style>