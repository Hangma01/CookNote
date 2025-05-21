<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";


// 화면 전환
const router = useRouter();

// 좋아요 상태
const isLike = ref(false);

// 북마크 상태
const isBookmark = ref(false);

const props = defineProps({ 
    recipeDetailData: { 
        type: Object    
    },
})

</script>

<template>
    <div class="recipe-detail-info-section">
        <div class="thumbnail-wrap">
            <img :src="recipeDetailData?.thumbnail" alt="thumbnail" class="thumbnail"/>
            
            <div class="writer-profile">
                <div class="writer-profile-image-box">
                     <img :src="recipeDetailData?.writerProfileImage" alt="writerProfileImage" class="writer-profile-image"/>
                </div>

                <div class="writer-nickname">
                    <span>{{ recipeDetailData?.writerNickname }}</span>
                </div>
            </div>
        </div>

        <div class="recipe-header">
            <div class="recipe-title">
                <p>{{ recipeDetailData?.title }}</p>
                <span class="creat-date">{{ recipeDetailData?.createAt }}</span>
            </div>
            
            <div class="recipe-user-aciton">
                <div :class="{'action' : true, 'liked' : isLike}" >
                    <font-awesome-icon :icon="['fas', 'heart']" v-if="isLike" class="action-icon" />
                    <font-awesome-icon :icon="['far', 'heart']" v-else class="action-icon" />
                    <p class="icon-text">좋아요</p>
                </div>
                <div :class="{'action' : true, 'bookmarked' : isBookmark }">
                    <font-awesome-icon :icon="['fas', 'bookmark']" v-if="isBookmark" class="action-icon" />
                    <font-awesome-icon :icon="['far', 'bookmark']" v-else class="action-icon" />
                    <p class="icon-text">책갈피</p>
                </div>

                <div class="action">
                    <font-awesome-icon :icon="['fas', 'triangle-exclamation']" class="action-icon"/>
                    <p class="icon-text">신고</p>
                </div>
            </div>
        </div>

        <div class="recipe-description">
            <p>{{ recipeDetailData?.description }}</p>
        </div>

        <div class="recipe-summary-info">
            <div class="recipe-summary-item">
                <font-awesome-icon :icon="['fas', 'users']" class="summary-icon" />
                <span>{{ recipeDetailData?.servingLabel }}</span>
            </div>

            
            <div class="recipe-summary-item">
                <font-awesome-icon :icon="['far', 'clock']" class="summary-icon" />
                <span>{{ recipeDetailData?.durationLabel }}</span>
            </div>

            
            <div class="recipe-summary-item">
                <font-awesome-icon :icon="['fas', 'ranking-star']" class="summary-icon" />
                <span>{{ recipeDetailData?.levelLabel }}</span>
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
            border: 1px solid rgb(224, 224, 224);;
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
        display: flex;
        justify-content: space-between;
        margin-top: 8rem;
        padding-right: 1rem;

        .recipe-title {
            font-size: 1.5rem;    
            font-weight: bold;

            .creat-date {
                font-size: 0.7rem;
                color: rgb(117, 117, 117);
            }
        }

        .recipe-user-aciton {
            display: flex;
            gap: 0.8rem;
            
            .action {
                text-align: center;
                font-size: 0.6rem;
                line-height: 14px;
                letter-spacing: -0.4px;
                font-weight: 500;
                color: rgb(189, 189, 189);
        
                .action-icon {
                    padding-top: 0.8rem;
                    width: 1.4rem;
                    height: 1.4rem;
                    
                }

                .icon-text {
                    margin-top: 0.3rem;
                }
            }

            .action.liked, .action.bookmarked {
                color: rgb(147, 112, 98);
            }
        }


    }

    .recipe-description {
        margin-top: 2rem;
        color: rgb(117, 117, 117);
        font-weight: normal;
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