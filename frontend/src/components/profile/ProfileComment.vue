<script setup>
import { commentDelete, getCommentUser } from '@/services/commentService';
import { onBeforeUnmount } from 'vue';
import { onMounted, ref } from 'vue';
import { reactive } from 'vue';
import { errorMessages } from 'vue/compiler-sfc';
import ProfileRecipeCard from './ProfileRecipeCard.vue';
import dayjs from '@/plugin/dayjs';

const activeCommentMenuMap = reactive({}); // 게시글 메뉴 활성화
const commentItemRefs = ref({}); // 외부 클릭 시 게시글 메뉴 닫히기

// 레시피 데이터
const commentData = ref(null);

// 페이징
const currentPage = ref(0);
const currentPageGroup = ref(0);

function relativeTime(date) {
    return dayjs(date).fromNow();
}

// 메뉴 토글
const toggleCommentMenu = (event, commentId) => {
    event.stopPropagation();
    activeCommentMenuMap[commentId] = !activeCommentMenuMap[commentId];
    closeCommentMenu(event);
};

// 외부 클릭 시 메뉴 닫기
const closeCommentMenu = (event) => {
    for (const commentId in activeCommentMenuMap) {
        const refElement = commentItemRefs.value[commentId];

        if (activeCommentMenuMap[commentId] && refElement && !refElement.contains(event.target)) {
            activeCommentMenuMap[commentId] = false;
        }
    }
};

const setCommentMenuRef = (el, commentId) => {
    if (el) {
        commentItemRefs.value[commentId] = el;
    } else {
        delete commentItemRefs.value[commentId];
    }
};

// 댓글 삭제하기
const handleCommentDelete = async (commentId) => {
    const proceed = confirm('댓글을 정말 삭제하시겠습니까?');

    if (proceed) {
        try {
            await commentDelete(commentId);

            // 현재 페이지와 탭 정보 저장
            const currentPageValue = currentPage.value;
            const res = loadCommentUser(currentPageValue);

            const content = res.data?.content || [];

            if (content.length === 0 && currentPageValue > 0) {
                // 현재 페이지에 댓글이 더 이상 없고, 이전 페이지가 있다면 이전 페이지로 이동
                await loadCommentUser(currentPage.value - 1);
            } else {
                commentData.value = res.data;
                currentPage.value = currentPageValue;
                currentPageGroup.value = Math.floor(currentPageValue / 10);
            }
        } catch (e) {
            alert('댓글을 삭제하지 못했습니다.');
        }
    } else {
        activeCommentMenuMap[commentId] = !activeCommentMenuMap[commentId];
    }
};

// 데이터 가져오기
const loadCommentUser = async (page = 0) => {
    try {
        const res = await getCommentUser(page); // page 파라미터 넘김

        commentData.value = res.data;
        currentPage.value = page;
        currentPageGroup.value = Math.floor(page / 10);
    } catch (e) {
        if (e.response && e.response?.data?.message) {
            alert(e.response.data.message);
        } else {
            alert(errorMessages.BADREQUEST);
        }

        router.push({ name: 'mainPage' });
    }
};

// 페이지 그룹 시작 번호 (0부터)
const pageGroupStart = () => currentPageGroup.value * 10;

// 페이지 그룹 끝 번호 (총 페이지 수보다 크지 않게)
const pageGroupEnd = () => {
    if (!commentData.value) return 0;
    return Math.min(pageGroupStart() + 10, commentData.value.page.totalPages);
};

// 페이지 이동
const goToPage = (page) => {
    loadCommentUser(page);
};

// 이전 10페이지 그룹
const prevPageGroup = () => {
    if (currentPageGroup.value > 0) {
        const newPage = (currentPageGroup.value - 1) * 10;
        loadCommentUser(newPage);
    }
};

// 다음 10페이지 그룹
const nextPageGroup = () => {
    if (commentData.value && (currentPageGroup.value + 1) * 10 < commentData.value.page.totalPages) {
        const newPage = (currentPageGroup.value + 1) * 10;
        loadCommentUser(newPage);
    }
};

onMounted(async () => {
    try {
        loadCommentUser();
    } catch (e) {
        if (e.response && e.response?.data?.message) {
            alert(e.response.data.message);
        } else {
            alert(errorMessages.BADREQUEST);
        }

        router.push({ name: 'mainPage' });
    }

    document.addEventListener('click', closeCommentMenu);
});

onBeforeUnmount(() => {
    document.removeEventListener('click', closeCommentMenu);
});
</script>

<template>
    <div class="like-title">
        <p>댓글 수 {{ commentData?.page?.totalElements }}</p>
    </div>
    <div class="line"></div>

    <div v-if="commentData?.content.length > 0">
        <ul>
            <li v-for="item in commentData?.content" :key="item.commentId">
                <div class="recipe-content">
                    <ProfileRecipeCard :recipeData="item" />

                    <div class="menu-wrap">
                        <div class="menu-icon" @click="toggleCommentMenu($event, item.commentId)">
                            <font-awesome-icon :icon="['fas', 'ellipsis-vertical']" />
                        </div>

                        <div class="menu" v-if="activeCommentMenuMap[item.commentId]" :ref="(el) => setCommentMenuRef(el, item.commentId)">
                            <button @click="handleCommentDelete(item.commentId)" class="btn">삭제</button>
                        </div>
                    </div>
                </div>

                <div class="comment-content">
                    <div class="content">
                        <p>{{ item.privateAdmin ? '관리자에 의해 삭제되었습니다.' : item.commentContent }}</p>
                    </div>

                    <div class="content-date">
                        <span>{{ relativeTime(item.commentCreateAt) }}</span>
                    </div>
                </div>
            </li>
        </ul>

        <div class="pagination" v-if="commentData?.page?.totalPages > 1">
            <!-- 이전 10개 페이지 그룹 버튼 -->
            <button v-if="commentData.page.totalPages > 10 && currentPageGroup > 0" @click="prevPageGroup">&lt;&lt;</button>

            <!-- 현재 페이지 그룹에 해당하는 페이지 버튼들 -->
            <button
                v-for="n in pageGroupEnd() - pageGroupStart()"
                :key="n + pageGroupStart()"
                :class="{ active: commentData.page.number === n + pageGroupStart() - 1 }"
                @click="goToPage(n + pageGroupStart() - 1)"
            >
                {{ n + pageGroupStart() }}
            </button>

            <!-- 다음 10개 페이지 그룹 버튼 -->
            <button v-if="commentData.page.totalPages > 10 && (currentPageGroup + 1) * 10 < commentData.page.totalPages" @click="nextPageGroup">
                &gt;&gt;
            </button>
        </div>
    </div>

    <div v-else class="non-comment">
        <p>조회된 댓글이 없습니다.</p>
    </div>
</template>


<style lang="scss" scoped>
.like-title {
    margin-top: 2rem;
    font-weight: 500;
    margin-bottom: 1rem;
    padding-left: 0.2rem;
}

.line {
    border-bottom: 2px solid;
    color: #c09370;
}

.recipe-content {
    display: flex;
    justify-content: space-between;
    margin-top: 2rem;
    padding-bottom: 2rem;

    .menu-wrap {
        color: rgb(124, 124, 124);
        position: relative;

        .menu-icon {
            cursor: pointer;
            width: 1.5rem;
            text-align: center;
        }

        .menu {
            position: absolute;
            background-color: white;
            width: 5rem;
            top: 2rem;
            left: -1rem;
            text-align: center;
            border: 1px solid rgb(124, 124, 124);
            border-radius: 0.5rem;
            z-index: 100;
            padding-top: 0.2rem;
            padding-bottom: 0.2rem;
            color: black;

            .btn {
                width: 100%;
            }
        }
    }
}

.comment-content {
    border-bottom: 1px solid rgb(224, 224, 224);
    padding-bottom: 2rem;

    .content {
        font-size: 0.9rem;
        white-space: pre-wrap;
    }

    .content-date {
        margin-top: 2rem;
        font-size: 0.8rem;
        color: #999999;
    }
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

.non-comment {
    height: 30rem;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    font-size: 1.5rem;
    color: #333333;
}
</style>