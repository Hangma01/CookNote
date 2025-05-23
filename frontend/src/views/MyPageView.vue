<script setup>
import MyPageUserProfile from '@/components/mypage/MyPageUserProfile.vue';
import { RouterView } from 'vue-router';
</script>


<template>

    <div class="mypage-container">
        <div class="mypage-wrap">
            <section class="mypage-menu">
                <div>fwefew</div>
                <RouterView />
            </section>
            <section class="mypage-user-profile">
                <div>fwefw</div>
                <!-- <MyPageUserProfile /> -->
            </section>
            

        </div>
        
    </div>
    

</template>


<style lang="scss" scoped>
.mypage-container {
    display: flex;
    justify-content: space-between;
    align-items: center;
    width: 69rem;
    margin: 0 auto;
    padding-top: 1.5rem;
    padding-bottom: 2rem;

    .mypage-wrap {
        width: 100%;
        display: flex;
        gap: 2rem;

        .mypage-menu {
            flex: 4;
        }
    
        .mypage-user-profile {
            flex: 2;
        }

    }
    
}
</style>