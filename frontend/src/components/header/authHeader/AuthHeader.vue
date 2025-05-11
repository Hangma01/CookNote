<script setup>
import Logo from "../Logo.vue";
</script>

<template>
    <header class="user-header">
        <div class="user-logo">
            <Logo/>
        </div>
    </header>
</template>


<style lang="scss" scoped>
.user-header {
    margin-bottom: 2rem;

    .user-logo {
        padding-top: 6rem;
        text-align: center;
    }
}


</style>