<script setup>

</script>

<template>
    <ul class="nav-menu">
        <li class="nav-menu-item">
            <div class="menu-item menu-category">
                음식 카테고리
            </div>
            
            <div class="nav-category-item">
                <ul>
                    <router-link :to="{ name: 'recipesSearch',  query: { categorytype: 1 } }">
                        <li class="categoy-item">
                            한식
                        </li>
                    </router-link>

                    <router-link :to="{ name: 'recipesSearch',  query: { categorytype: 2 } }">
                        <li class="categoy-item">
                            중식
                        </li>
                    </router-link>
                </ul>
            </div>
           
        </li>

        <li class="nav-menu-item">
            <router-link :to="{ name: 'recipesSearch',  query: { serving: 1 } }">
                <div class="menu-item">
                    혼밥 레시피
                </div>
            </router-link>
        </li>

        <li class="nav-menu-item">
            <router-link :to="{ name: 'recipesSearch',  query: { categorypurpose: 2 } }">
                <div class="menu-item">
                    다이어트 레시피
                </div>
            </router-link>
        </li>

        <li class="nav-menu-item">
            <div class="menu-item">
                재료 검색
            </div>
        </li>

        <li class="nav-menu-item">
            <div class="menu-item">
                쉐프 검색
            </div>
        </li>

        <li class="nav-menu-item">
            <div class="menu-item">
                팔로워 레시피
            </div>
        </li>
    </ul>
    
</template>


<style lang="scss" scoped>
.nav-menu{
    display: flex;
    gap: 3rem;
    width: 75rem;
    height: 100%;
    margin: 0 auto;
    box-sizing: border-box;

    .nav-menu-item {
        position: relative;
        font-size: 1rem;
        cursor: pointer;
        width: 7rem;
        text-align: center;
        
        .menu-item {
            height: 100%;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .nav-category-item {
            position: absolute;
            width: 100%;
            border: 1px solid #eee;
            background-color: #fff;
            border-bottom-right-radius: 0.5rem;
            border-bottom-left-radius: 0.5rem;
            padding-bottom: 0.3rem;

            .categoy-item {
                display: flex;
                justify-content: center;
                align-items: center;
                padding: 6px 0;
                
            }

            .categoy-item:hover{
                background-color:#c09370;
            }
        }

        .menu-category:hover + .nav-category-item {
            display: block;
        }

        .nav-category-item:hover {
            display: block;
        }
    }
}
</style>