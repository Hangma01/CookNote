<script setup>
import { ReportType } from '@/constans/reportType';
import { recipeBookmarkDelete, recipeBookmarkInsert, recipeLikeDelete, recipeLikeInsert } from '@/services/recipeService';
import { useUserStore } from '@/stores/user';
import { loginCheck } from '@/utils/commonFunction';
import { commonValues } from '@/utils/commonValues';
import { errorMessages } from '@/utils/messages/errorMessages';
import { debounce } from 'lodash';
import { ref, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import dayjs from '@/plugin/dayjs';

const props = defineProps({
    recipeDetailData: Object,
    recipeId: String,
    recipeLikeCount: Number,
});

// 화면 전환
const router = useRouter();

// 유저 스토어
const userStore = useUserStore();
const isLoggedIn = userStore.isLoggedIn;
const userId = userStore.getUserId;

// 좋아요 상태
const isLike = ref(false);

// 북마크 상태
const isBookmark = ref();

// 부모 이벤트
const emit = defineEmits(['openReportModal', 'refreshLike']);

function relativeTime(date) {
    return dayjs(date).fromNow();
}

// 좋아요 클릭
const handleRecipeLike = async () => {
    if (isLoggedIn) {
        try {
            if (isLike.value) {
                const res = await recipeLikeDelete(props.recipeId);
            } else {
                const res = await recipeLikeInsert(props.recipeId);
            }
            emit('refreshLike');
            isLike.value = !isLike.value;
        } catch (e) {
            if (e.response && e.response?.data?.message) {
                alert(e.response.data.message);
            } else {
                alert(errorMessages.BADREQUEST);
            }
        }
    } else {
        loginCheck(router);
    }
};

// 북마크 클릭
const handleRecipeBookmark = async () => {
    if (isLoggedIn) {
        try {
            if (isBookmark.value) {
                await recipeBookmarkDelete(props.recipeId);
            } else {
                await recipeBookmarkInsert(props.recipeId);
            }

            isBookmark.value = !isBookmark.value;
        } catch (e) {
            console.log(e);
            if (e.response && e.response?.data?.message) {
                alert(e.response.data.message);
            } else {
                alert(errorMessages.BADREQUEST);
            }
        }
    } else {
        loginCheck(router);
    }
};

// 신고 클릭
const handleRecipeReport = async (recipeId) => {
    if (isLoggedIn) {
        emit('openReportModal', ReportType.RECIPE, recipeId, props.recipeDetailData?.writerId);
    } else {
        loginCheck(router);
    }
};

watch(
    () => props.recipeDetailData,
    (newVal) => {
        isLike.value = newVal?.liked ?? false;
        isBookmark.value = newVal?.bookmarked ?? false;
    }
),
    { immediate: true };
</script>

<template>
    <div class="recipe-detail-info-section">
        <div class="thumbnail-wrap">
            <img :src="props.recipeDetailData?.thumbnail" class="thumbnail" alt="thumbnail" v-if="props.recipeDetailData" />
            <div class="writer-profile">
                <div class="writer-profile-image-box" v-if="props.recipeDetailData">
                    <router-link
                        :to="
                            userId === props.recipeDetailData?.writerId
                                ? { name: 'profileRecipe', query: { tab: props.recipeDetailData?.status } }
                                : { name: 'profileHost', params: { hostId: props.recipeDetailData?.writerId } }
                        "
                    >
                        <img :src="props.recipeDetailData?.writerProfileImage" class="writer-profile-image" alt="writer-profile" />
                    </router-link>
                </div>

                <div class="writer-nickname">
                    <span>{{ props.recipeDetailData?.writerNickname }}</span>
                </div>
            </div>
        </div>

        <div class="recipe-header">
            <div class="recipe-title">
                <p>{{ props.recipeDetailData?.title }}</p>
                <span class="creat-date">{{ relativeTime(props.recipeDetailData?.createAt) }}</span>
            </div>

            <div class="recipe-user-aciton" v-if="!props.recipeDetailData?.author">
                <div :class="{ 'action like-box': true, liked: isLike }" @click="handleRecipeLike">
                    <font-awesome-icon :icon="['fas', 'heart']" v-if="isLike" class="action-icon like-icon" />
                    <font-awesome-icon :icon="['far', 'heart']" v-else class="action-icon like-icon" />
                    <span class="like-count">{{ props?.recipeLikeCount }}</span>
                </div>

                <div>
                    <div :class="{ 'action bookmark-box': true, bookmarked: isBookmark }" @click="handleRecipeBookmark">
                        <font-awesome-icon :icon="['fas', 'bookmark']" v-if="isBookmark" class="action-icon bookmark-icon" />
                        <font-awesome-icon :icon="['far', 'bookmark']" v-else class="action-icon bookmark-icon" />
                        <p class="icon-text">북마크</p>
                    </div>
                    <div class="action" @click="handleRecipeReport(props?.recipeId)">
                        <font-awesome-icon :icon="['fas', 'triangle-exclamation']" class="action-icon" />
                        <p class="icon-text">신고</p>
                    </div>
                </div>
            </div>
        </div>

        <div class="recipe-description">
            <p>{{ props.recipeDetailData?.description }}</p>
        </div>

        <div class="recipe-summary-info">
            <div class="recipe-summary-item">
                <font-awesome-icon :icon="['fas', 'users']" class="summary-icon" />
                <span>{{ props.recipeDetailData?.servingLabel }}</span>
            </div>

            <div class="recipe-summary-item">
                <font-awesome-icon :icon="['far', 'clock']" class="summary-icon" />
                <span>{{ props.recipeDetailData?.durationLabel }}</span>
            </div>

            <div class="recipe-summary-item">
                <font-awesome-icon :icon="['fas', 'ranking-star']" class="summary-icon" />
                <span>{{ props.recipeDetailData?.levelLabel }}</span>
            </div>
        </div>
    </div>
</template>


<style lang="scss" scoped>
.recipe-detail-info-section {
    .thumbnail-wrap {
        position: relative;
        width: 100%;
        height: 28rem;

        .thumbnail {
            border: 1px solid rgb(224, 224, 224);
            width: 100%;
            height: 100%;
        }

        .writer-profile {
            width: 100%;
            position: absolute;
            bottom: -5.8rem;
            display: flex;
            flex-direction: column;
            align-items: center;
            gap: 1rem;

            .writer-profile-image-box {
                width: 7rem;
                height: 7rem;
                border: 1px solid rgb(224, 224, 224);
                border-radius: 100%;
                background-color: aliceblue;

                .writer-profile-image {
                    width: 100%;
                    height: 100%;
                    border-radius: 100%;
                }
            }
        }
    }

    .recipe-header {
        margin-top: 8rem;

        .recipe-title {
            font-size: 1.5rem;
            font-weight: bold;
            flex: 5.5;
            .creat-date {
                font-size: 0.7rem;
                color: rgb(117, 117, 117);
            }
        }

        .recipe-user-aciton {
            display: flex;
            justify-content: space-between;
            gap: 0.8rem;
            flex: 1;
            .action {
                text-align: center;
                font-size: 0.6rem;
                line-height: 14px;
                letter-spacing: -0.4px;
                font-weight: 500;
                color: rgb(189, 189, 189);
                cursor: pointer;
                float: left;

                .action-icon {
                    padding-top: 0.8rem;
                    width: 1.4rem;
                    height: 1.4rem;
                }

                .icon-text {
                    margin-top: 0.3rem;
                }
            }

            .bookmark-box {
                margin-right: 1rem;
            }

            .like-box {
                display: flex;
                align-items: center;
                gap: 0.8rem;
                border: 1px solid #f99090;
                height: 2.2rem;
                margin-top: 1rem;
                padding-bottom: 0.6rem;
                padding-left: 0.7rem;
                padding-right: 1rem;
                border-radius: 2rem;

                .like-icon {
                    width: 1.6rem;
                    height: 1.6rem;
                }

                .like-count {
                    padding-top: 0.7rem;
                    text-align: center;
                    font-size: 1.2rem;
                    color: #f99090;
                }
            }

            .action.liked {
                color: #f99090;
            }

            .action.bookmarked {
                color: rgb(147, 112, 98);
            }
        }
    }

    .recipe-description {
        margin-top: 2rem;
        color: rgb(117, 117, 117);
        font-weight: normal;
        white-space: pre-wrap;
    }

    .recipe-summary-info {
        padding: 0 6rem;
        margin-top: 5rem;
        margin-left: auto;
        margin-right: auto;
        display: flex;
        justify-content: space-between;

        .recipe-summary-item {
            display: flex;
            flex-direction: column;
            align-items: center;
            font-size: 0.9rem;
            gap: 0.7rem;
            color: rgb(117, 117, 117);

            .summary-icon {
                font-size: 1.8rem;
            }
        }
    }
}
</style>