<script setup>
import { useUserStore } from '@/stores/user';
import { commonValues } from '@/utils/commonValues';
import dayjs from '@/plugin/dayjs';

const props = defineProps({
    recipeData: {
        // 레시피 수정 시 오는 데이터
        type: Object,
    },
});

function relativeTime(date) {
    return dayjs(date).fromNow();
}

// 유저 스토어
const userStore = useUserStore();
const userId = userStore.getUserId;
</script>

<template>
    <router-link :to="{ name: 'recipeDetail', params: { recipeId: props.recipeData?.recipeId } }">
        <div class="recipe-card">
            <div class="recipe-image-box">
                <img :src="props.recipeData?.recipeThumbnail" class="recipe-image" />
            </div>

            <div class="recipe-info">
                <div class="recipe-title">
                    <span>{{ props.recipeData?.recipeTitle }}</span>
                </div>

                <div class="date">
                    <span>{{ relativeTime(props.recipeData?.createAt) }}</span>
                </div>

                <div class="recipe-sub-info">
                    <router-link
                        :to="
                            props.recipeData?.writerId === userId
                                ? { name: 'profileRecipe' }
                                : { name: 'profileHost', params: { hostId: props.recipeData?.writerId } }
                        "
                        @click.stop
                        class="writer-info"
                    >
                        <div class="writer-profile-image-box">
                            <img :src="props.recipeData?.writerProfileImage" class="writer-profile-image" />
                        </div>

                        <div class="writer-nickname">
                            <span>{{ props.recipeData?.writerNickname }}</span>
                        </div>
                    </router-link>

                    <div class="recipe-stats">
                        <div>
                            <font-awesome-icon :icon="['fas', 'heart']" class="like-icon" />
                            <span>{{ props.recipeData?.recipeLikeCount }}</span>
                        </div>

                        <div>
                            <font-awesome-icon :icon="['far', 'message']" class="comment-icon" />
                            <span>{{ props.recipeData?.recipeCommentCount }}</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </router-link>
</template>


<style lang="scss" scoped>
.recipe-card {
    transition: transform 0.3s ease;

    .recipe-image-box {
        width: 16rem;
        height: 13rem;

        .recipe-image {
            width: 100%;
            height: 100%;
            border: 1px solid rgba(147, 112, 98, 0.6);
        }
    }

    .recipe-info {
        padding: 0 0.8rem;

        .recipe-title {
            margin-top: 0.5rem;
            color: rgb(30, 30, 30);
            height: 2.8rem;
            display: -webkit-box;
            -webkit-line-clamp: 2;
            line-clamp: 2;
            -webkit-box-orient: vertical;
            word-break: break-word;
            overflow: hidden;
            line-height: 1.4rem;
            font-size: 0.95rem;
            font-weight: 500;
            letter-spacing: 0.05rem;
        }

        .date {
            color: rgb(144, 144, 144);
            margin-top: 0.2rem;
            font-size: 0.8rem;
            text-align: right;
        }

        .recipe-sub-info {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-top: 1rem;

            .writer-info {
                display: flex;
                align-items: center;
                gap: 0.5rem;

                .writer-profile-image-box {
                    height: 1.5rem;
                    width: 1.5rem;

                    .writer-profile-image {
                        width: 100%;
                        height: 100%;
                        border-radius: 100%;
                    }
                }
                .writer-nickname {
                    width: 7.5rem;
                    overflow: hidden;
                    text-overflow: ellipsis;
                    font-size: 0.9rem;
                    color: rgb(55, 55, 55);
                    letter-spacing: 0.05rem;
                }
            }

            .recipe-stats {
                display: flex;
                gap: 0.5rem;

                .like-icon {
                    color: #f99090;
                }

                .comment-icon {
                    color: rgb(147, 112, 98);
                }

                .like-icon,
                .comment-icon {
                    margin-right: 0.3rem;
                }
            }
        }
    }
}

.recipe-card:hover {
    transform: scale(1.05);
}
</style>


