<script setup>
import RecipeCard from '@/components/ui/RecipeCard.vue';
import SelectDropDown from '@/components/ui/SelectDropDown.vue';
import { getCategory, getCategoryAll } from '@/services/categoryService';
import { getRecipeSearch, getRecipeSearchIngredient, getRecipesOfFollowingUser, getRecipesOfFollowingUsers } from '@/services/recipeService';
import { getFollowingLatestForRecipe } from '@/services/userService';
import { useUserStore } from '@/stores/user';
import { errorMessages } from '@/utils/messages/errorMessages';
import { onMounted, reactive, ref, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';

// 화면 전환
const router = useRouter();
const route = useRoute();

// 받을 데이터
const recipesData = ref(null);
const followingUsers = ref(null);

// 페이징
const currentPage = ref(0);
const currentPageGroup = ref(0);

const selectedFollowingId = ref(null);

const handleUserClick = async (followingId, page = 0) => {
    try {
        if (followingId === selectedFollowingId.value) {
            selectedFollowingId.value = null;
            const res = await getRecipesOfFollowingUsers(page);
            currentPage.value = page;
            currentPageGroup.value = Math.floor(page / 10);
            recipesData.value = res.data;
        } else {
            selectedFollowingId.value = followingId;
            const res = await getRecipesOfFollowingUser(followingId, page);
            recipesData.value = res.data;
            currentPage.value = page;
            currentPageGroup.value = Math.floor(page / 10);
        }
    } catch (e) {
        if (e.response && e.response?.data?.message) {
            alert(e.response.data.message);
        } else {
            alert(errorMessages.BADREQUEST);
        }

        window.location.reload();
    }
};

// 페이지 그룹 시작 번호 (0부터)
const pageGroupStart = () => currentPageGroup.value * 10;

// 페이지 그룹 끝 번호 (총 페이지 수보다 크지 않게)
const pageGroupEnd = () => {
    if (!recipesData.value) return 0;
    return Math.min(pageGroupStart() + 10, recipesData.value.page.totalPages);
};

// 페이지 이동
const goToPage = (page) => {
    handleUserClick(selectedFollowingId.value, page);
};

// 이전 10페이지 그룹
const prevPageGroup = () => {
    if (currentPageGroup.value > 0) {
        const newPage = (currentPageGroup.value - 1) * 10;
        handleUserClick(selectedFollowingId.value, newPage);
    }
};
// 다음 10페이지 그룹
const nextPageGroup = () => {
    if (recipesData.value && (currentPageGroup.value + 1) * 10 < recipesData.value.page.totalPages) {
        const newPage = (currentPageGroup.value + 1) * 10;
        handleUserClick(selectedFollowingId.value, newPage);
    }
};

// 초기 셋팅
onMounted(async () => {
    const page = parseInt(route.query.page) || 0;

    try {
        const [getRecipesOfFollowingUsersRes, getFollowingLatestForRecipeRes] = await Promise.all([
            getRecipesOfFollowingUsers(page),
            getFollowingLatestForRecipe(),
        ]);

        recipesData.value = getRecipesOfFollowingUsersRes.data;
        followingUsers.value = getFollowingLatestForRecipeRes.data;
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
    <div class="follow-feed-container">
        <section class="following-user-section">
            <div><h1>팔로우</h1></div>
            <ul class="following-user-wrap">
                <li v-for="item in followingUsers" :key="item.followingId" @click="handleUserClick(item.followingId)" class="following-user-box">
                    <div class="following-info">
                        <div class="following-profile-image-box">
                            <img
                                :src="item.profileImage"
                                :class="[
                                    'following-profile-image',
                                    {
                                        active: selectedFollowingId === item.followingId,
                                    },
                                ]"
                            />
                        </div>
                        <span
                            :class="[
                                'nickname',
                                {
                                    active: selectedFollowingId === item.followingId,
                                },
                            ]"
                            >{{ item.nickname }}</span
                        >
                    </div>
                </li>
            </ul>
        </section>

        <section class="feed-recipe-list" v-if="recipesData?.content.length > 0">
            <div>
                <ul class="recipe-card-wrap">
                    <li v-for="(item, index) in recipesData?.content" :key="index">
                        <RecipeCard :recipeData="item" />
                    </li>
                </ul>
            </div>

            <div class="pagination" v-if="recipesData?.page?.totalPages > 1">
                <!-- 이전 10개 페이지 그룹 버튼 -->
                <button v-if="recipesData?.page.totalPages > 10 && currentPageGroup > 0" @click="prevPageGroup">&lt;&lt;</button>

                <!-- 현재 페이지 그룹에 해당하는 페이지 버튼들 -->
                <button
                    v-for="n in pageGroupEnd() - pageGroupStart()"
                    :key="n + pageGroupStart()"
                    :class="{
                        active: recipesData?.page.number === n + pageGroupStart() - 1,
                    }"
                    @click="goToPage(n + pageGroupStart() - 1)"
                >
                    {{ n + pageGroupStart() }}
                </button>

                <!-- 다음 10개 페이지 그룹 버튼 -->
                <button v-if="recipesData?.page.totalPages > 10 && (currentPageGroup + 1) * 10 < recipesData?.page.totalPages" @click="nextPageGroup">
                    &gt;&gt;
                </button>
            </div>
        </section>

        <div v-else class="keyword-non">
            <div class="keyword-box">
                <p>작성한 레시피가 없습니다.</p>
            </div>
        </div>
    </div>
</template>

<style lang="scss" scoped>
.follow-feed-container {
    width: 69rem;
    margin: 0 auto;
    padding-top: 4rem;
    padding-bottom: 4rem;

    .following-user-section {
        .following-user-wrap {
            display: flex;
            overflow-x: scroll; // 항상 공간 확보
            margin-top: 1rem;

            // 크롬, 엣지, 사파리
            &::-webkit-scrollbar {
                height: 6px;
            }

            &::-webkit-scrollbar-track {
                background: transparent;
            }

            &::-webkit-scrollbar-thumb {
                background-color: rgba(150, 150, 150, 0.5); // 항상 보이게
                border-radius: 4px;
            }

            .following-user-box {
                cursor: pointer;
                width: 8rem;
                min-width: 8rem;
                margin-top: 1rem;

                .following-info {
                    text-align: center;
                    .following-profile-image-box {
                        margin-bottom: 1rem;

                        .following-profile-image {
                            width: 5rem;
                            height: 5rem;
                            border-radius: 100%;

                            &.active {
                                border: 5px solid rgba(23, 170, 255, 0.836);
                            }
                        }
                    }

                    .nickname {
                        letter-spacing: 1px;
                        color: rgb(30, 30, 30);
                        display: -webkit-box;
                        -webkit-line-clamp: 1;
                        line-clamp: 1;
                        -webkit-box-orient: vertical;
                        word-break: break-word;
                        overflow: hidden;
                        line-height: 1.4rem;
                        font-size: 0.95rem;
                        font-weight: 500;
                        letter-spacing: 0.05rem;
                        width: 6rem;
                        margin: auto;

                        &.active {
                            color: rgb(170, 81, 45);
                            font-weight: bold;
                        }
                    }
                }
            }
        }
    }

    .feed-recipe-list {
        margin-top: 3rem;

        .recipe-card-wrap {
            display: grid;
            grid-template-columns: repeat(4, 1fr);
            gap: 4rem 1.5rem;
            border-top: 1px solid rgb(224, 224, 224);
            padding-top: 3rem;
        }

        .pagination {
            margin-top: 3rem;
            text-align: center;

            button {
                margin: 0 0.25rem;
                padding: 0.3rem 0.6rem;
                border: 1px solid #ccc;
                background-color: white;
                cursor: pointer;

                &.active {
                    background-color: #a89d94;
                    color: white;
                    font-weight: bold;
                }

                &:disabled {
                    cursor: not-allowed;
                    opacity: 0.5;
                }
            }
        }
    }

    .keyword-non {
        display: flex;
        align-items: center;
        justify-content: center;
        height: 30rem;
        font-size: 2rem;
        word-break: break-all; /* 긴 단어 줄바꿈 */
        padding-top: 10rem;
        text-align: center;

        .keyword-box {
            width: 50rem;
            .keyword {
                color: green;
            }
        }
    }
}
</style>