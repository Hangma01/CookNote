<script setup>
import { getUserFollow, userCancleFollow, userFollow } from '@/services/userService';
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import { errorMessages } from 'vue/compiler-sfc';

const userFollowData = ref(null)

// 화면 전환
const router = useRouter()

const loadUserFollow = async () => {
    try {
        const res = await getUserFollow(); // page 파라미터 넘김

        userFollowData.value = res.data
        console.log(res.data)
    } catch (e) {        
        if (e.response && e.response?.data?.message) {
            alert(e.response.data.message) 
        } else {
            alert(errorMessages.BADREQUEST)
        }

        // router.push({ name : 'mainPage'})
    }
};

// 팔로우 하기
const handleFollow = async (followId) => {
    try {
        await userFollow(followId)
        loadUserFollow()
    } catch (e) {
        if (e.response && e.response?.data?.message) {
            alert(e.response.data.message) 
        } else {
            alert(errorMessages.BADREQUEST)
        }

        window.location.reload()
    }
}

// 팔로잉 취소
const handleCancleFollow = async (followId) => {
    try {
        await userCancleFollow(followId)
        loadUserFollow()
    } catch (e) {
        if (e.response && e.response?.data?.message) {
            alert(e.response.data.message) 
        } else {
            alert(errorMessages.BADREQUEST)
        }

        window.location.reload()
    }
}


onMounted(async () => {
    await loadUserFollow()
})
</script>

<template>
    <div class="follow-content">
        <div class="follow-wrap">
            <div class="follower-box">
                <div class="title">팔로워</div>
                <div class="follower-wrap">
                    <ul>
                        <li class="user-list"
                            v-for="(item, index) in userFollowData?.follower" :key="index">
                            <div class="user-image">
                                <img :src="item.followerProfileImage" class="image"/>
                            </div>
                            <div class="user-nickname">
                                <span>
                                    {{ item.followerNickname }}
                                </span>
                            </div>
                            <div v-if="item.followingBack">
                                맞팔중
                            </div>
                            <button v-else @click="handleFollow(item.followerId)">
                                팔로우
                            </button>
                        </li>
                    </ul>
                </div>
            </div>

            <div class="following-box">
                <div class="title">팔로잉</div>
                <div class="following-wrap">
                    <ul>
                        <li class="user-list"
                            v-for="(item, index) in userFollowData?.following" :key="index">
                            <div class="user-image">
                                <img :src="item.followingProfileImage" class="image"/>
                            </div>
                            <div class="user-nickname">
                                <span>
                                    {{ item.followingNickname }}
                                </span>
                            </div>
                            <button @click="handleCancleFollow(item.followingId)">
                                <span>팔로우 취소</span>
                            </button>
                        </li>
                    </ul>
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

    .follower-box, .following-box {
        width: 21rem;

        .title {
            font-size: 1.2rem;
            font-weight: bold;
        }

        .follower-wrap, .following-wrap {
            border: 1px solid black;
            margin-top: 2rem;
            padding: 1rem 0.2rem;

            .user-list {
                .user-image {
                    float: left;
                    width: 3rem;
                    height: 3rem;

                    .image{
                        width: 100%;
                        height: 100%;
                    }
                }
                .user-nickname {
                    float: left;
                }
            }
        }

    }
}
</style>