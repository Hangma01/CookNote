<script setup>

</script>

<template>
    <ul class="user-menu">
        <li class="user-menu-item">
            <router-link :to="{ name: 'myPage' }">
                <font-awesome-icon :icon="['far', 'user']" style="color: #454F5B;" />
            </router-link>
        </li>

        <li class="user-menu-item">
            <router-link to="/add-recipe">
                <font-awesome-icon :icon="['fas', 'pen-to-square']" style="color: #454F5B;" />
            </router-link>
        </li>
    </ul>
</template>


<style lang="scss" scoped>
.user-menu{
    display: flex;
    gap: 30px;

    .user-menu-item{
        width: 3rem;
        font-size: 1.7rem;
        border-radius: 100%;
        display: flex;
        justify-content: center;
        align-items: center;

    }

}
</style>