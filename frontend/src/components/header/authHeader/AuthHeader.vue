<script setup>
import Logo from "../Logo.vue";
</script>

<template>
    <header class="user-header">
        <h1 class="user-logo">
            <Logo />
        </h1>
    </header>
</template>


<style lang="scss" scoped>
.user-header {
    margin-bottom: 2rem;

    .user-logo {
        padding-top: 4rem;
    }
}


</style>