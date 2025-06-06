<script setup>
import { getUserFollow, userAddFollow, userCancleFollow } from '@/services/userService';
import { loadProfile } from '@/utils/commonFunction';
import { onMounted, ref } from 'vue';
import { errorMessages } from 'vue/compiler-sfc';

const userFollowData = ref(null);

const loadUserFollow = async () => {
    try {
        const res = await getUserFollow(); // page 파라미터 넘김

        userFollowData.value = res.data;

        loadProfile(false);
    } catch (e) {
        if (e.response && e.response?.data?.message) {
            alert(e.response.data.message);
        } else {
            alert(errorMessages.BADREQUEST);
        }

        window.location.reload();
    }
};

// 팔로우 하기
const handleAddFollow = async (followId) => {
    try {
        await userAddFollow(followId);
        loadUserFollow();
    } catch (e) {
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
        loadUserFollow();
    } catch (e) {
        if (e.response && e.response?.data?.message) {
            alert(e.response.data.message);
        } else {
            alert(errorMessages.BADREQUEST);
        }

        window.location.reload();
    }
};

onMounted(async () => {
    await loadUserFollow();
});
</script>

<template>
    <div class="follow-content">
        <div class="follow-wrap">
            <div class="follower-box">
                <div class="title">팔로워</div>
                <div class="follower-wrap">
                    <ul v-if="userFollowData?.follower.length > 0">
                        <li class="user-list" v-for="(item, index) in userFollowData?.follower" :key="index">
                            <div class="user-info">
                                <router-link :to="{ name: 'profileHost', params: { hostId: item.followerId } }">
                                    <div class="user-image">
                                        <img :src="item.followerProfileImage" class="image" />
                                    </div>
                                </router-link>

                                <router-link :to="{ name: 'profileHost', params: { hostId: item.followerId } }">
                                    <div class="user-nickname">
                                        <span>
                                            {{ item.followerNickname }}
                                        </span>
                                    </div>
                                </router-link>
                            </div>

                            <div v-if="item.followingBack" class="isFollowBack">맞팔중</div>
                            <button v-else @click="handleAddFollow(item.followerId)" class="action-btn">팔로우</button>
                        </li>
                    </ul>

                    <div v-else class="non-follower">
                        <p>팔로워한 한 쉐프가 없습니다.</p>
                    </div>
                </div>
            </div>

            <div class="following-box">
                <div class="title">팔로잉</div>
                <div class="following-wrap">
                    <ul v-if="userFollowData?.following.length > 0">
                        <li class="user-list" v-for="(item, index) in userFollowData?.following" :key="index">
                            <div class="user-info">
                                <router-link :to="{ name: 'profileHost', params: { hostId: item.followingId } }">
                                    <div class="user-image">
                                        <img :src="item.followingProfileImage" class="image" />
                                    </div>
                                </router-link>

                                <router-link :to="{ name: 'profileHost', params: { hostId: item.followingId } }">
                                    <div class="user-nickname">
                                        <span>
                                            {{ item.followingNickname }}
                                        </span>
                                    </div>
                                </router-link>
                            </div>

                            <button @click="handleCancleFollow(item.followingId)" class="action-btn">
                                <span>팔로우 취소</span>
                            </button>
                        </li>
                    </ul>

                    <div v-else class="non-following">
                        <p>팔로잉을 한 쉐프가 없습니다.</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>


<style lang="scss" scoped>
.follow-wrap {
    margin-top: 3rem;
    display: flex;
    justify-content: space-between;

    .follower-box,
    .following-box {
        width: 21rem;

        .title {
            font-size: 1.2rem;
            font-weight: bold;
        }

        .follower-wrap,
        .following-wrap {
            border: 1px solid rgb(200, 200, 200);
            height: 30rem;
            border-radius: 0.5rem;
            margin-top: 2rem;
            padding: 1rem 0.5rem;
            overflow-y: scroll; // 항상 공간 확보

            // 크롬, 엣지, 사파리
            &::-webkit-scrollbar {
                width: 8px;
            }

            &::-webkit-scrollbar-track {
                background: transparent;
            }

            &::-webkit-scrollbar-thumb {
                background-color: rgba(150, 150, 150, 0.5); // 항상 보이게
                border-radius: 4px;
            }

            .user-list {
                display: flex;
                justify-content: space-between;
                align-items: center;
                border-bottom: 1px solid rgb(200, 200, 200);
                padding-top: 0.4rem;
                padding-bottom: 0.3rem;

                .user-info {
                    display: flex;
                    align-items: center;
                    .user-image {
                        width: 2.5rem;
                        height: 2.5rem;

                        .image {
                            width: 100%;
                            height: 100%;
                            border-radius: 100%;
                        }
                    }
                    .user-nickname {
                        margin-left: 1rem;
                    }
                }

                .isFollowBack {
                    padding: 0.2rem 0.5rem;
                    border: 1px solid rgb(200, 200, 200);
                    border-radius: 0.5rem;
                    color: #777777;
                    background-color: rgb(244, 240, 239);
                }

                .action-btn {
                    background-color: #007bff;
                    border: 1px solid;
                    color: white;
                    padding: 0.2rem 0.5rem;
                    border-radius: 0.5rem;
                }
            }

            .non-follower,
            .non-following {
                height: 27rem;
                display: flex;
                flex-direction: column;
                justify-content: center;
                align-items: center;
                font-size: 1.3rem;
                color: #333333;
            }
        }
    }
}
</style>