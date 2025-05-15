<script setup>
import Logo from "@/components/logo/Logo.vue";
import SearchBar from "./SearchBar.vue";
import UserMenu from "./UserMenu.vue";
import NavMenu from "./NavMenu.vue";

</script>

<template>
    <header>
        <div class="header-bottom">
            <router-link :to="{ name: 'mainPage' }">
                <Logo />
            </router-link>
            <SearchBar />
            <UserMenu />
        </div>
        
        <nav class="nav">
            <NavMenu />
        </nav>
    </header>
</template>


<style lang="scss" scoped>
.header-bottom {
    display: flex;
    justify-content: space-between;
    align-items: end;
    width: 70rem;
    margin: 0 auto;
    padding-top: 0.8rem;
    padding-bottom: 2rem;
}

.nav{
    background-color:#c09370 ;
    height: 3rem;
}
</style>