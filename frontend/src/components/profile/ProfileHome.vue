<script setup>
import { getUserProfile } from '@/services/userService';
import { useUserStore } from '@/stores/user';
import { loadProfile } from '@/utils/commonFunction';
import { errorMessages } from '@/utils/messages/errorMessages';
import { computed, onMounted } from 'vue';
import { RouterView, useRoute, useRouter } from 'vue-router';

// 화면 전환
const router = useRouter();

// 유저 스토어
const userStore = useUserStore();

// 파리미터 값 가져오기
const route = useRoute();

// 메뉴 정의 배열
const menus = [
    { label: '레시피', icon: ['fas', 'utensils'], name: 'profileRecipe' },
    { label: '북마크', icon: ['fas', 'bookmark'], name: 'profileBookmark' },
    { label: '댓글', icon: ['fas', 'message'], name: 'profileComment' },
    { label: '좋아요', icon: ['fas', 'heart'], name: 'profileLike' },
    { label: '팔로우', icon: ['fas', 'user'], name: 'profileFollow' },
];

// 현재 활성 경로인지 체크하는 함수
const isActive = (menuName) => route.name === menuName;
onMounted(async () => {
    loadProfile(false);
});
</script>

<template>
    <ul class="home-menu-list">
        <li v-for="menu in menus" :key="menu.name">
            <router-link :to="{ name: menu.name }" :class="['menu-btn', { selected: isActive(menu.name) }]">
                <font-awesome-icon :icon="menu.icon" />
                <span>{{ menu.label }}</span>
            </router-link>
        </li>
    </ul>

    <RouterView />
</template>


<style lang="scss" scoped>
.home-menu-list {
    display: flex;
    justify-content: space-between;

    .menu-btn {
        width: 8.7rem;
        height: 4rem;
        font-size: 1.1rem;
        display: flex;
        gap: 1rem;
        border: 1px solid rgb(200, 200, 200);
        border-radius: 0.5rem;
        align-items: center;
        justify-content: center;
        color: #777777;
        background-color: rgb(244, 240, 239);
    }

    .menu-btn.selected {
        background-color: #c09370;
        border: 1px solid #a57954;
        color: white;
    }
}
</style>