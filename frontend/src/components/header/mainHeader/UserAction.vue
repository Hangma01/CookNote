<script setup>
import privateAPI from '@/api/privateAPI';
import { logout } from '@/services/authService';
import { useUserStore } from '@/stores/user';
import { onBeforeUnmount, onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';

// 화면 전환
const router = useRouter();

// 유저 스토어
const userStore = useUserStore();
const isLoggedIn = userStore.isLoggedIn;

// 마이 페이지 아이템 이벤트
const showProfileItem = ref(false);
const profileItemRef = ref(null);

// 외부 클릭 시 마이 페이지 아이템 창 닫기
const closeProfileItem = (event) => {
    if (showProfileItem && !profileItemRef.value.contains(event.target)) {
        showProfileItem.value = false;
    }
};

// 마이 페이지 아이템 창 토글
const toggleProfileItem = (event) => {
    showProfileItem.value = !showProfileItem.value;
    event.stopPropagation();
};

const toggleShowProfileItem = () => {
    showProfileItem.value = false;
};

// 로그아웃
const handleLogout = async () => {
    try {
        const res = await logout();
    } catch (e) {}
    userStore.logout();
    await router.push({ name: 'login' });
};

// 전체 페이지 클릭 이벤트를 추가
onMounted(() => {
    document.addEventListener('click', closeProfileItem);
});

// 컴포넌트가 언마운트 될 때 이벤트 리스너 제거
onBeforeUnmount(() => {
    document.removeEventListener('click', closeProfileItem);
});
</script>

<template>
    <ul class="user-menu">
        <li class="user-menu-item">
            <router-link :to="{ name: 'search' }">
                <font-awesome-icon :icon="['fas', 'magnifying-glass']" style="color: #454f5b" />
            </router-link>
        </li>

        <li class="user-menu-item">
            <router-link :to="{ name: 'login' }" v-if="!isLoggedIn">
                <font-awesome-icon :icon="['far', 'user']" style="color: #454f5b" />
            </router-link>

            <div v-if="isLoggedIn" @click="toggleProfileItem" class="profile-icon">
                <font-awesome-icon :icon="['far', 'user']" style="color: #454f5b" />
            </div>

            <div class="user-menu-profile-item" v-show="showProfileItem" @click="toggleShowProfileItem" ref="profileItemRef">
                <ul>
                    <router-link :to="{ name: 'profileRecipe' }">
                        <li class="profile-item">마이페이지</li>
                    </router-link>

                    <router-link :to="{ name: 'profileEdit' }">
                        <li class="profile-item">회원정보 수정</li>
                    </router-link>

                    <router-link :to="{ name: 'profileReport' }">
                        <li class="profile-item">신고</li>
                    </router-link>

                    <li class="profile-item" @click="handleLogout">로그아웃</li>
                </ul>
            </div>
        </li>

        <li class="user-menu-item">
            <router-link :to="{ name: 'recipeWrite' }">
                <font-awesome-icon :icon="['fas', 'pen-to-square']" style="color: #454f5b" />
            </router-link>
        </li>
    </ul>
</template>


<style lang="scss" scoped>
.user-menu {
    display: flex;
    gap: 30px;

    .user-menu-item {
        width: 3rem;
        font-size: 1.4rem;
        border-radius: 100%;
        align-items: center;
        position: relative;

        .user-menu-profile-item {
            position: absolute;
            font-size: 1rem;
            width: 8rem;
            border: 1px solid #eee;
            background-color: #fff;
            border-bottom-right-radius: 0.5rem;
            border-bottom-left-radius: 0.5rem;
            padding-bottom: 0.3rem;
            left: -3.1rem;
            margin-top: 1rem;

            .profile-item {
                display: flex;
                justify-content: center;
                align-items: center;
                padding: 6px 0;
                cursor: pointer;
            }

            .profile-item:hover {
                background-color: #c09370;
            }
        }
        .profile-icon {
            cursor: pointer;
        }
    }
}
</style>