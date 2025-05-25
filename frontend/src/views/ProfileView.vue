<script setup>
import ProfileInfo from '@/components/profile/ProfileInfo.vue';

</script>


<template>
    <div class="profile-container">
        <div class="profile-wrap">
            <section class="profile-menu">
                <RouterView />
            </section>
            <section class="profile">
                <ProfileInfo />
            </section>
        </div>
    </div>
</template>


<style lang="scss" scoped>
.profile-container {
    display: flex;
    justify-content: space-between;
    align-items: center;
    width: 69rem;
    margin: 0 auto;
    padding-top: 3rem;
    padding-bottom: 2rem;

    .profile-wrap {
        width: 100%;
        display: flex;
        gap: 2rem;

        .profile-menu {
            flex: 4;
        }
    
        .profile {
            flex: 2;
        }

    }
    
}
</style>