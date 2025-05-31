
<script setup>
import { getUserProfile, userDelete } from '@/services/userService';
import { useUserStore } from '@/stores/user';
import { commonMessage } from '@/utils/messages/commonMessage';
import { errorMessages } from '@/utils/messages/errorMessages';
import { onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';

// 화면 전환
const router = useRouter();

// 유저 스토어
const userStore = useUserStore();

// 파리미터 값 가져오기
const route = useRoute();

// 메뉴 정의 배열
const menus = [
    { label: '신고 내역', name: 'profileReport' },
    { label: '제재 내역', name: 'profileSanction' },
];

// 현재 활성 경로인지 체크하는 함수
const isActive = (menuName) => route.name === menuName;

onMounted(async () => {
    try {
        const res = await getUserProfile();

        userStore.setProfile({
            ...res.data,
            isHostProfile: false,
        });
    } catch (e) {
        if (e.response && e.response?.data?.message) {
            alert(e.response.data.message);
        } else {
            alert(errorMessages.BADREQUEST);
        }

        router.push({ name: 'mainPage' });
    }
});
</script>

<template>
    <ul class="report-menu-list">
        <li v-for="menu in menus" :key="menu.name">
            <router-link :to="{ name: menu.name }" :class="['menu-btn', { selected: isActive(menu.name) }]">
                <span>{{ menu.label }}</span>
            </router-link>
        </li>
    </ul>
    <RouterView />
</template>

<style lang="scss" scoped>
.report-menu-list {
    display: flex;
    gap: 2rem;

    .menu-btn {
        width: 12rem;
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