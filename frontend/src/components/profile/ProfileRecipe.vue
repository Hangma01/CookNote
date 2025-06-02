<script setup>
import { computed, onBeforeUnmount, onMounted, reactive, ref, watch } from 'vue';
import ProfileRecipeCard from './ProfileRecipeCard.vue';
import { errorMessages } from '@/utils/messages/errorMessages';
import { useRoute, useRouter } from 'vue-router';
import { deleteRecipe, getRecipePrivate, getRecipePublic } from '@/services/recipeService';
import { useUserStore } from '@/stores/user';
import { commonValues } from '@/utils/commonValues';
import { loadProfile } from '@/utils/commonFunction';

// 화면 전환
const router = useRouter();
const route = useRoute();

// 유저 스토어
const userStore = useUserStore();
const userProfile = computed(() => userStore.getProfile);

const activeTab = ref(commonValues.PUBLIC_TEXT);

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

// 공개 레시피 가져오기
const loadRecipPublic = async (page = 0) => {
    try {
        const res = await getRecipePublic(page); // page 파라미터 넘김

        recipeData.value = res.data;
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

// 비공개 레시피 가져오기
const loadRecipPrivate = async (page = 0) => {
    try {
        const res = await getRecipePrivate(page); // page 파라미터 넘김
        recipeData.value = res.data;

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

// 레시피 삭제
const handleRecipeDelete = async (recipeId) => {
    const proceed = confirm('레시피를 정말 삭제하시겠습니까?');
    if (proceed) {
        try {
            await deleteRecipe(recipeId);

            loadProfile(false);

            // 현재 페이지와 탭 정보 저장
            const currentPageValue = currentPage.value;
            const isPublic = activeTab.value === commonValues.PUBLIC_TEXT;

            // 현재 페이지의 레시피 목록 다시 가져오기
            const res = isPublic ? await getRecipePublic(currentPageValue) : await getRecipePrivate(currentPageValue);

            const content = res.data?.content || [];

            if (content.length === 0 && currentPageValue > 0) {
                // 현재 페이지에 아이템이 없고, 이전 페이지가 있다면 이전 페이지로 이동
                if (isPublic) {
                    await loadRecipPublic(currentPageValue - 1);
                } else {
                    await loadRecipPrivate(currentPageValue - 1);
                }
            } else {
                // 현재 페이지 유지하며 상태 수동 설정
                recipeData.value = res.data;
                currentPage.value = currentPageValue;
                currentPageGroup.value = Math.floor(currentPageValue / 10);
            }
        } catch (e) {
            if (e.response && e.response?.data?.message) {
                alert(e.response.data.message);
            } else {
                alert(errorMessages.BADREQUEST);
            }
            router.push({ name: 'mainPage' });
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
    if (activeTab.value === commonValues.PUBLIC_TEXT) {
        loadRecipPublic(page);
    } else if (activeTab.value === commonValues.PRIVATE_TEXT) {
        loadRecipPrivate(page);
    }
};

// 이전 10페이지 그룹
const prevPageGroup = () => {
    if (currentPageGroup.value > 0) {
        const newPage = (currentPageGroup.value - 1) * 10;
        if (activeTab.value === commonValues.PUBLIC_TEXT) {
            loadRecipPublic(newPage);
        } else if (activeTab.value === commonValues.PRIVATE_TEXT) {
            loadRecipPrivate(newPage);
        }
    }
};
// 다음 10페이지 그룹
const nextPageGroup = () => {
    if (recipeData.value && (currentPageGroup.value + 1) * 10 < recipeData.value.page.totalPages) {
        const newPage = (currentPageGroup.value + 1) * 10;
        if (activeTab.value === commonValues.PUBLIC_TEXT) {
            loadRecipPublic(newPage);
        } else if (activeTab.value === commonValues.PRIVATE_TEXT) {
            loadRecipPrivate(newPage);
        }
    }
};

onMounted(async () => {
    const tabFromQuery = route.query.tab;

    if (tabFromQuery === commonValues.PRIVATE_TEXT || tabFromQuery === commonValues.PUBLIC_TEXT) {
        activeTab.value = tabFromQuery;
    }

    try {
        if (activeTab.value === commonValues.PUBLIC_TEXT) {
            loadRecipPublic();
        } else if (activeTab.value === commonValues.PRIVATE_TEXT) {
            loadRecipPrivate();
        }
    } catch (e) {
        if (e.response && e.response?.data?.message) {
            alert(e.response.data.message);
        } else {
            alert(errorMessages.BADREQUEST);
        }

        window.location.reload();
    }

    document.addEventListener('click', closeRecipeMenu);
});

onBeforeUnmount(() => {
    document.removeEventListener('click', closeRecipeMenu);
});

const updateTab = (tab) => {
    activeTab.value = tab;
    router.replace({ query: { tab } });
};

watch(activeTab, (newTab) => {
    if (newTab === commonValues.PUBLIC_TEXT) {
        loadRecipPublic();
    } else if (newTab === commonValues.PRIVATE_TEXT) {
        loadRecipPrivate();
    }
});
</script>

<template>
    <ul class="nav">
        <li :class="['nav-item', { selected: activeTab === commonValues.PUBLIC_TEXT }]" @click="updateTab(commonValues.PUBLIC_TEXT)">
            공개({{ userProfile?.recipePublicCount || 0 }})
        </li>

        <li :class="['nav-item', { selected: activeTab === commonValues.PRIVATE_TEXT }]" @click="updateTab(commonValues.PRIVATE_TEXT)">
            비공개({{ userProfile?.recipePrivateCount || 0 }})
        </li>
    </ul>
    <div v-if="!(userProfile?.recipePublicCount === 0 && userProfile?.recipePrivateCount === 0)">
        <ul>
            <li class="recipe-content" v-for="item in recipeData?.content" :key="item.recipeId">
                <ProfileRecipeCard :recipeData="item" :recipeStatus="activeTab" />

                <div class="menu-wrap">
                    <div class="menu-icon" @click="toggleRecipeMenu($event, item.recipeId)">
                        <font-awesome-icon :icon="['fas', 'ellipsis-vertical']" />
                    </div>

                    <div class="menu" v-if="activeRecipeMenuMap[item.recipeId]" :ref="(el) => setRecipeMenuRef(el, item.recipeId)">
                        <router-link :to="{ name: 'recipeEdit', params: { recipeId: item.recipeId } }">
                            <div v-if="item.privateAdmin !== true" class="btn">수정</div>
                        </router-link>

                        <button @click="handleRecipeDelete(item.recipeId)" :class="[item.privateAdmin !== true ? 'btn' : 'btn-solo']">삭제</button>
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

    <div
        v-if="
            (activeTab === commonValues.PUBLIC_TEXT && userProfile?.recipePublicCount === 0) ||
            (activeTab === commonValues.PRIVATE_TEXT && userProfile?.recipePrivateCount === 0)
        "
        class="non-recipe"
    >
        <p>작성한 레시피가 없습니다.</p>
        <p>새로운 레시피를 작성해보세요.</p>

        <router-link :to="{ name: 'recipeWrite' }">
            <div class="router-write">
                <font-awesome-icon :icon="['fas', 'pen-to-square']" style="color: #ffffff" class="write-icon" />
                <span>작성하기</span>
            </div>
        </router-link>
    </div>
</template>


<style lang="scss" scoped>
.nav {
    display: flex;
    border-bottom: 1px solid #ccc;
    margin-top: 2rem;

    .nav-item {
        padding: 0.7rem 2rem;
        cursor: pointer;
        position: relative;
        margin-bottom: -1px; // 하단 border 없애기 위해 올려줌
    }

    .nav-item.selected {
        background-color: #fff;
        border: 1px solid #ccc;
        border-bottom: none;
        border-radius: 0.5rem 0.5rem 0 0;
    }
}

.recipe-content {
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
            border: 1px solid rgb(200, 200, 200);
            border-radius: 0.5rem;
            z-index: 100;
            padding-top: 0.1rem;
            padding-bottom: 0.2rem;
            color: black;

            .btn {
                width: 100%;
            }

            .btn:hover {
                background-color: #c09370;
            }

            .btn-solo:hover {
                background-color: white;
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

.non-recipe {
    height: 30rem;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    font-size: 1.5rem;
    color: #333333;
    .router-write {
        margin-top: 2rem;
        border: 1px solid black;
        padding: 1rem 2rem;
        border-radius: 0.5rem;
        background-color: #c09370;
        border: 1px solid #a57954;
        color: white;
        .write-icon {
            margin-right: 1rem;
        }
    }
}
</style>