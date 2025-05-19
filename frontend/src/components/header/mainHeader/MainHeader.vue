<script setup>
import { defineAsyncComponent } from "vue"

const Logo = defineAsyncComponent(() => import("@/components/logo/Logo.vue"))
const SearchBar = defineAsyncComponent(() => import("./SearchBar.vue"))
const UserMenu = defineAsyncComponent(() => import("./UserMenu.vue"))
const NavMenu = defineAsyncComponent(() => import("./NavMenu.vue"))

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
    padding-top: 1.5rem;
    padding-bottom: 2rem;
}

.nav{
    background-color:#c09370 ;
    height: 3rem;
}
</style>