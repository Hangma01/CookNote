<script setup>
</script>

<template>
    <ul class="nav-menu">
        <li class="nav-menu-item">
            <router-link :to="{ name: 'search', query: { keyword: '', categorycuisine: 0, categorypurpose: 3, page: 0 } }">
                <div class="menu-item">
                    <span>혼밥 레시피</span>
                </div>
            </router-link>
        </li>

        <li class="nav-menu-item">
            <router-link :to="{ name: 'search', query: { keyword: '', categorycuisine: 0, categorypurpose: 1, page: 0 } }">
                <div class="menu-item">
                    <span>다이어트 레시피</span>
                </div>
            </router-link>
        </li>

        <li class="nav-menu-item">
            <router-link :to="{ name: 'searchIngredient' }">
                <div class="menu-item">
                    <span>재료 검색</span>
                </div>
            </router-link>
        </li>

        <li class="nav-menu-item">
            <router-link :to="{ name: 'searchChef' }">
                <div class="menu-item">
                    <span>쉐프 검색</span>
                </div>
            </router-link>
        </li>

        <li class="nav-menu-item">
            <router-link :to="{ name: 'followFeed' }">
                <div class="menu-item">
                    <span>팔로우 레시피</span>
                </div>
            </router-link>
        </li>
    </ul>
</template>


<style lang="scss" scoped>
.nav-menu {
    display: flex;
    gap: 3rem;
    width: 70rem;
    height: 100%;
    margin: 0 auto;
    box-sizing: border-box;

    .nav-menu-item {
        position: relative;
        font-size: 1rem;
        font-weight: 500;
        cursor: pointer;
        width: 7rem;
        text-align: center;

        .menu-item {
            height: 100%;
            display: flex;
            align-items: center;
            justify-content: center;
            color: #424242;
        }

        .menu-item:hover {
            color: #bf917e;
        }
    }
}
</style>