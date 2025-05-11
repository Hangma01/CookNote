<template>
    <div class="user-auth-wrap">
        <router-view />
    </div>
</template>

<script setup>
import authHeader from '../components/header/authHeader/AuthHeader.vue'
</script>

<style scoped>
.user-auth-wrap{
    margin: 0 auto;
    width: 22rem;
}
</style>