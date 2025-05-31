<template>
    <div class="user-auth-wrap">
        <RouterView />
    </div>
</template>

<script setup>
import { RouterView } from 'vue-router';
</script>

<style scoped>
.user-auth-wrap {
    margin: 0 auto;
    width: 23rem;
}
</style>