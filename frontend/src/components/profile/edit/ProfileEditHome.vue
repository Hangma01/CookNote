
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
    { label: '회원정보 수정', name: 'profileEdit' },
    { label: '비밀번호 변경', name: 'profilePwEdit' },
    { label: '회원탈퇴', name: 'profileDelete' },
];

// 현재 활성 경로인지 체크하는 함수
const isActive = (menuName) => route.name === menuName;

// 회원 탈퇴
const confirmUserDelete = async () => {
    const proceed = confirm(commonMessage.USER_DELETE_ASK_MESSAGE);
    if (proceed) {
        try {
            await userDelete();
            alert(commonMessage.USER_DELETE_SUCCESS_MESSAGE);
            userStore.logout();
            router.replace({ name: 'login' });
        } catch (e) {
            if (e.response && e.response?.data?.message) {
                alert(e.response.data.message);
            } else {
                alert(errorMessages.BADREQUEST);
            }
        }
    }
};
onMounted(async () => {
    try {
        const res = await getUserProfile();

        userStore.setProfile({
            ...res.data,
            isHostProfile: false,
        });
    } catch (e) {
        console.log(e);
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
    <ul class="edit-menu-list">
        <li v-for="menu in menus" :key="menu.name">
            <component
                :is="menu.name === 'profileDelete' ? 'button' : 'router-link'"
                :to="menu.name !== 'profileDelete' ? { name: menu.name } : undefined"
                :class="['menu-btn', { selected: isActive(menu.name) }]"
                @click="menu.name === 'profileDelete' ? confirmUserDelete() : null"
            >
                <span>{{ menu.label }}</span>
            </component>
        </li>
    </ul>

    <RouterView />
</template>

<style lang="scss" scoped>
.edit-menu-list {
    display: flex;
    justify-content: space-between;

    .menu-btn {
        width: 14rem;
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