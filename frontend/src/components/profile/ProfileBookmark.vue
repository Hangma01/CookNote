<script setup>
import { onBeforeUnmount, onMounted, reactive, ref } from 'vue';
import { getRecipeBookmark, recipeBookmarkDelete } from '@/services/recipeService';
import { errorMessages } from '@/utils/messages/errorMessages';
import ProfileRecipeCard from './ProfileRecipeCard.vue';

const activeRecipeMenuMap = reactive({}); // 게시글 메뉴 활성화
const recipeItemRefs = ref({}); // 외부 클릭 시 게시글 메뉴 닫히기

// 레시피 데이터
const recipeData = ref(null);

// 페이징
const currentPage = ref(0);
const currentPageGroup = ref(0);

// 메뉴 토글
const toggleRecipeMenu = (event, recipeId) => {
    event.stopPropagation();
    activeRecipeMenuMap[recipeId] = !activeRecipeMenuMap[recipeId];
    closeRecipeMenu(event);
};

// 외부 클릭 시 메뉴 닫기
const closeRecipeMenu = (event) => {
    for (const recipeId in activeRecipeMenuMap) {
        const refElement = recipeItemRefs.value[recipeId];

        if (activeRecipeMenuMap[recipeId] && refElement && !refElement.contains(event.target)) {
            activeRecipeMenuMap[recipeId] = false;
        }
    }
};

const setRecipeMenuRef = (el, recipeId) => {
    if (el) {
        recipeItemRefs.value[recipeId] = el;
    } else {
        delete recipeItemRefs.value[recipeId];
    }
};

// 데이터 가져오기
const loadRecipBookmark = async (page = 0) => {
    try {
        const res = await getRecipeBookmark(page); // page 파라미터 넘김

        recipeData.value = res.data;
        currentPage.value = page;
        currentPageGroup.value = Math.floor(page / 10);
        return res;
    } catch (e) {
        if (e.response && e.response?.data?.message) {
            alert(e.response.data.message);
        } else {
            alert(errorMessages.BADREQUEST);
        }

        router.push({ name: 'mainPage' });
    }
};

// 북마크 삭제하기
const handleBookmarkDelete = async (recipeId) => {
    const proceed = confirm('북마크를 정말 삭제하시겠습니까?');

    if (proceed) {
        try {
            await recipeBookmarkDelete(recipeId);

            // 현재 페이지와 탭 정보 저장
            const currentPageValue = currentPage.value;

            const res = await loadRecipBookmark(currentPageValue);

            const content = res?.data?.content || [];

            if (content.length === 0 && currentPageValue > 0) {
                // 현재 페이지에 댓글이 더 이상 없고, 이전 페이지가 있다면 이전 페이지로 이동
                await loadRecipBookmark(currentPage.value - 1);
            } else {
                recipeData.value = res.data;
                currentPage.value = currentPageValue;
                currentPageGroup.value = Math.floor(currentPageValue / 10);
            }
        } catch (e) {
            alert('북마크를 삭제하지 못했습니다.');
        }
    } else {
        activeRecipeMenuMap[recipeId] = !activeRecipeMenuMap[recipeId];
    }
};

// 페이지 그룹 시작 번호 (0부터)
const pageGroupStart = () => currentPageGroup.value * 10;

// 페이지 그룹 끝 번호 (총 페이지 수보다 크지 않게)
const pageGroupEnd = () => {
    if (!recipeData.value) return 0;
    return Math.min(pageGroupStart() + 10, recipeData.value.page.totalPages);
};

// 페이지 이동
const goToPage = (page) => {
    loadRecipBookmark(page);
    window.scrollTo(0, 0);
};

// 이전 10페이지 그룹
const prevPageGroup = () => {
    if (currentPageGroup.value > 0) {
        const newPage = (currentPageGroup.value - 1) * 10;
        loadRecipBookmark(newPage);
        window.scrollTo(0, 0);
    }
};
// 다음 10페이지 그룹
const nextPageGroup = () => {
    if (recipeData.value && (currentPageGroup.value + 1) * 10 < recipeData.value.page.totalPages) {
        const newPage = (currentPageGroup.value + 1) * 10;
        loadRecipBookmark(newPage);
        window.scrollTo(0, 0);
    }
};

onMounted(async () => {
    loadRecipBookmark();

    document.addEventListener('click', closeRecipeMenu);
});

onBeforeUnmount(() => {
    document.removeEventListener('click', closeRecipeMenu);
});
</script>

<template>
    <div class="bookmark-title">
        <p>북마크 수 {{ recipeData?.page?.totalElements }}</p>
    </div>
    <div class="line"></div>

    <div v-if="recipeData?.content.length > 0">
        <ul>
            <li class="recipe-bookmark-content" v-for="item in recipeData?.content" :key="item.recipeId">
                <ProfileRecipeCard :recipeData="item" />

                <div class="menu-wrap">
                    <div class="menu-icon" @click="toggleRecipeMenu($event, item.recipeId)">
                        <font-awesome-icon :icon="['fas', 'ellipsis-vertical']" />
                    </div>

                    <div class="menu" v-if="activeRecipeMenuMap[item.recipeId]" :ref="(el) => setRecipeMenuRef(el, item.recipeId)">
                        <button @click="handleBookmarkDelete(item.recipeId)" class="btn">삭제</button>
                    </div>
                </div>
            </li>
        </ul>

        <div class="pagination" v-if="recipeData?.page?.totalPages > 1">
            <!-- 이전 10개 페이지 그룹 버튼 -->
            <button v-if="recipeData.page.totalPages > 10 && currentPageGroup > 0" @click="prevPageGroup">&lt;&lt;</button>

            <!-- 현재 페이지 그룹에 해당하는 페이지 버튼들 -->
            <button
                v-for="n in pageGroupEnd() - pageGroupStart()"
                :key="n + pageGroupStart()"
                :class="{ active: recipeData.page.number === n + pageGroupStart() - 1 }"
                @click="goToPage(n + pageGroupStart() - 1)"
            >
                {{ n + pageGroupStart() }}
            </button>

            <!-- 다음 10개 페이지 그룹 버튼 -->
            <button v-if="recipeData.page.totalPages > 10 && (currentPageGroup + 1) * 10 < recipeData.page.totalPages" @click="nextPageGroup">
                &gt;&gt;
            </button>
        </div>
    </div>

    <div v-else class="non-bookmark">
        <p>조회된 북마크가 없습니다.</p>
    </div>
</template>


<style lang="scss" scoped>
.bookmark-title {
    margin-top: 2rem;
    font-weight: 500;
    margin-bottom: 1rem;
    padding-left: 0.2rem;
}

.line {
    border-bottom: 2px solid;
    color: #c09370;
}

.recipe-bookmark-content {
    display: flex;
    justify-content: space-between;
    margin-top: 2rem;
    padding-bottom: 2rem;
    border-bottom: 1px solid rgb(224, 224, 224);

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

.non-bookmark {
    height: 30rem;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    font-size: 1.5rem;
    color: #333333;
}
</style>