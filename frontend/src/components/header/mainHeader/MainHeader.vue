<script setup>
import Logo from "../Logo.vue";
import SearchBar from "./SearchBar.vue";
import UserMenu from "./UserMenu.vue";
import NavMenu from "./NavMenu.vue";
</script>

<template>
    <header>
        <div class="header-bottom">
            <Logo />
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
    align-items: center;
    width: 75rem;
    margin: 0 auto;
    padding: 2rem 0;
}

.nav{
    background-color: #8B5E3C ;
    height: 4rem;
}
</style>