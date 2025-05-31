<script setup>
import router from '@/router';
import { userAddFollow, userCancleFollow, getHostProfileLoggedIn } from '@/services/userService';
import { useUserStore } from '@/stores/user';
import { errorMessages } from '@/utils/messages/errorMessages';
import { computed, onUnmounted } from 'vue';

// 유저 스토어
const userStore = useUserStore();
const isLoggedIn = userStore.getIsLoggedIn;
const userProfile = computed(() => userStore.getProfile);

// 팔로우 하기
const handleAddFollow = async (followId) => {
    try {
        await userAddFollow(followId);
        const res = await getHostProfileLoggedIn(followId);
        userStore.setProfile({
            ...res.data,
            isHostProfile: true,
        });
    } catch (e) {
        console.log(e);
        if (e.response && e.response?.data?.message) {
            alert(e.response.data.message);
        } else {
            alert(errorMessages.BADREQUEST);
        }
        window.location.reload();
    }
};

// 팔로잉 취소
const handleCancleFollow = async (followId) => {
    try {
        await userCancleFollow(followId);
        const res = await getHostProfileLoggedIn(followId);
        userStore.setProfile({
            ...res.data,
            isHostProfile: true,
        });
    } catch (e) {
        if (e.response && e.response?.data?.message) {
            alert(e.response.data.message);
        } else {
            alert(errorMessages.BADREQUEST);
        }

        window.location.reload();
    }
};

// 로그인 하기
const handleLogin = () => {
    router.push({ name: 'login' });
};

onUnmounted(() => {
    userStore.setProfile(null);
});
</script>

<template>
    <div class="user-profile-container">
        <div class="user-profile-info">
            <div class="profile-image-box">
                <img :src="userProfile?.profileImage" alt="이미지" class="profile-image" />
            </div>
            <div class="user-nickname">
                <span>{{ userProfile?.nickname }}</span>
            </div>
        </div>

        <div class="user-sub-info">
            <div class="user-sub-info-box">
                <div class="user-sub-info-title">
                    <span>레시피 수</span>
                </div>

                <div class="user-sub-info-count">
                    <span>{{ userProfile?.recipePublicCount + userProfile?.recipePrivateCount || 0 }}</span>
                </div>
            </div>

            <div class="user-sub-info-box">
                <div class="user-sub-info-title">
                    <span>팔로워 수</span>
                </div>

                <div class="user-sub-info-count">
                    <span>{{ userProfile?.followerCount || 0 }}</span>
                </div>
            </div>

            <div class="user-sub-info-box">
                <div class="user-sub-info-title">
                    <span>팔로잉 수</span>
                </div>

                <div class="user-sub-info-count">
                    <span>{{ userProfile?.followingCount || 0 }}</span>
                </div>
            </div>
        </div>

        <div class="action-follow" v-if="userProfile?.isHostProfile">
            <div v-if="isLoggedIn">
                <v-btn class="action-btn" v-if="userProfile?.follow" @click="handleCancleFollow(userProfile?.hostId)"> 팔로우 취소 </v-btn>

                <v-btn class="action-btn" v-else @click="handleAddFollow(userProfile?.hostId)"> 팔로우 </v-btn>
            </div>

            <v-btn class="action-btn" v-else @click="handleLogin"> 로그인 후 팔로우하기 </v-btn>
        </div>
    </div>
</template>


<style lang="scss" scoped>
.user-profile-container {
    width: 100%;
    border: 1px solid rgb(200, 200, 200);
    border-radius: 0.5rem;

    .user-profile-info {
        display: flex;
        flex-direction: column;
        align-items: center;
        padding-top: 1rem;

        .profile-image-box {
            width: 8rem;
            height: 8rem;

            .profile-image {
                width: 100%;
                height: 100%;
                border-radius: 100%;
                border: 1px solid rgb(200, 200, 200);
            }
        }

        .user-nickname {
            margin-top: 1rem;
            font-size: 1.3rem;
        }
    }

    .user-sub-info {
        display: flex;
        justify-content: space-around;
        margin-top: 2rem;

        .user-sub-info-box {
            text-align: center;

            .user-sub-info-count {
                margin-top: 0.5rem;
            }
        }

        margin-bottom: 2rem;
    }

    .action-follow {
        margin-top: 2rem;
        margin-bottom: 2rem;
        padding: 0 1.8rem;

        .action-btn {
            width: 100%;
            font-weight: bold;
            font-size: 1.1rem;
            background-color: #007bff;
            color: white;
        }
    }
}
</style>