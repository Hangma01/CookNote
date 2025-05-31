<script setup>
import { useUserStore } from '@/stores/user';
import { ref } from 'vue';
import { userAddFollow, userCancleFollow } from '../../services/userService';

const props = defineProps({
    chefData: Object,
    index: Number,
});

// 유저 스토어
const userStore = useUserStore();
const isLoggedIn = userStore.getIsLoggedIn;
const userId = userStore.getUserId;

const isFollowing = ref(props.chefData.following);

const toggleFollow = async (userId) => {
    try {
        if (isFollowing.value) {
            await userCancleFollow(userId);
        } else {
            await userAddFollow(userId);
        }

        isFollowing.value = !isFollowing.value;
    } catch (error) {
        alert('잠시 후 다시 시도해주세요.');
    }
};
</script>

<template>
    <div class="chef-card">
        <div class="rank">
            <span>
                {{ index + 1 }}
            </span>
        </div>

        <router-link :to="{ name: 'profileHost', params: { hostId: props.chefData?.userId } }">
            <div class="chef-image-box">
                <img :src="props.chefData?.profileImage" class="chef-image" />
            </div>
        </router-link>

        <div class="chef-info-box">
            <div class="chef-info">
                <router-link :to="{ name: 'profileHost', params: { hostId: props.chefData?.userId } }">
                    <span class="chef-nickname">{{ props.chefData?.nickname }}</span>
                </router-link>

                <button class="follow-btn" @click="toggleFollow(props.chefData?.userId)" v-if="isLoggedIn">
                    {{ isFollowing ? '팔로우 취소' : '팔로우' }}
                </button>
            </div>

            <div class="chef-info-count">
                <div>
                    <font-awesome-icon :icon="['fas', 'pen-to-square']" />
                    <span class="count">{{ props.chefData?.recipeCount }}</span>
                </div>
                <div>
                    <font-awesome-icon :icon="['fas', 'user']" />
                    <span class="count">{{ props.chefData?.followerCount }}</span>
                </div>

                <div>
                    <font-awesome-icon :icon="['fas', 'bookmark']" />
                    <span class="count">{{ props.chefData?.bookmarkCount }}</span>
                </div>

                <div>
                    <font-awesome-icon :icon="['fas', 'heart']" />
                    <span class="count">{{ props.chefData?.likeCount }}</span>
                </div>
            </div>
        </div>
    </div>
</template>


<style lang="scss" scoped>
.chef-card {
    display: flex;
    align-items: center;
    padding-left: 2rem;

    .rank {
        font-size: 2rem;
        font-weight: bold;
        margin-right: 2rem;
        width: 3rem;
    }
    .chef-image-box {
        width: 4rem;
        height: 4rem;

        .chef-image {
            width: 100%;
            height: 100%;
            border: 1px solid rgba(147, 112, 98, 0.6);
            border-radius: 100%;
        }
    }

    .chef-info-box {
        margin-left: 2rem;

        .chef-info {
            margin-bottom: 0.4rem;

            .chef-nickname {
                color: #de4830;
                font-size: 1.15rem;
                font-weight: 500;
            }

            .follow-btn {
                margin-left: 1rem;
                border: 1px solid #ccc;
                padding: 0.1rem 0.6rem;
                border-radius: 0.5rem;
                color: #333;
                font-size: 0.9rem;
            }
        }

        .chef-info-count {
            display: flex;
            gap: 2rem;
            color: #aaa;
            font-size: 0.9rem;

            .count {
                margin-left: 0.4rem;
                font-weight: 500;
            }
        }
    }
}
</style>


