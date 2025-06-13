<script setup>
import ChefCard from '@/components/ui/ChefCard.vue';
import { getChefSearch, getChefSearchLoggined } from '@/services/userService';
import { useUserStore } from '@/stores/user';

import { errorMessages } from '@/utils/messages/errorMessages';
import { onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

// 화면 전환
const router = useRouter();
const route = useRoute();

// 검색 데이터
const searchData = ref();

// 페이징
const currentPage = ref(0);
const currentPageGroup = ref(0);

// 검색어
const keyword = ref(null);
const serchedFlag = ref(false);
const serchedKeyword = ref(null);

// 유저 스토어
const userStore = useUserStore();
const isLoggedIn = userStore.isLoggedIn;
const userId = userStore.getUserId;

// 검색 실행
const handleSearch = async (page = 0) => {
    // 쿼리스트링에 검색조건 반영
    router.replace({
        query: {
            keyword: keyword.value || '',
            page,
        },
    });

    try {
        let res = null;
        if (isLoggedIn) {
            res = await getChefSearchLoggined(keyword.value, page);
        } else {
            res = await getChefSearch(keyword.value, page);
        }

        searchData.value = res.data;
        serchedFlag.value = true;
        serchedKeyword.value = keyword.value;
        currentPage.value = page;
        currentPageGroup.value = Math.floor(page / 10);
    } catch (e) {
        serchedFlag.value = true;
        serchedKeyword.value = keyword.value;
        if (e.response && e.response?.data?.message) {
            alert(e.response.data.message);
        } else {
            alert(errorMessages.BADREQUEST);
        }
    }
};

// 페이지 그룹 시작 번호 (0부터)
const pageGroupStart = () => currentPageGroup.value * 10;

// 페이지 그룹 끝 번호 (총 페이지 수보다 크지 않게)
const pageGroupEnd = () => {
    if (!searchData.value) return 0;
    return Math.min(pageGroupStart() + 10, searchData.value.page.totalPages);
};

// 페이지 이동
const goToPage = (page) => {
    handleSearch(page);
    window.scrollTo(0, 0);
};

// 이전 10페이지 그룹
const prevPageGroup = () => {
    if (currentPageGroup.value > 0) {
        const newPage = (currentPageGroup.value - 1) * 10;
        handleSearch(newPage);
        window.scrollTo(0, 0);
    }
};

// 다음 10페이지 그룹
const nextPageGroup = () => {
    if (reportsData.value && (currentPageGroup.value + 1) * 10 < reportsData.value.page.totalPages) {
        const newPage = (currentPageGroup.value + 1) * 10;
        handleSearch(newPage);
        window.scrollTo(0, 0);
    }
};

// 초기 셋팅
onMounted(() => {
    const page = parseInt(route.query.page) || 0;
    keyword.value = route.query.keyword || '';

    handleSearch(page);
});
</script>

<template>
    <div class="chef-search-container">
        <section class="search-bar-wrap">
            <div class="search-bar">
                <v-text-field
                    v-model="keyword"
                    type="text"
                    variant="outlined"
                    density="compact"
                    hide-details="auto"
                    placeholder="쉐프를 검색해 보세요."
                    append-inner-icon="mdi-magnify"
                    @click:append-inner="handleSearch()"
                    @keyup.enter="handleSearch()"
                />
            </div>
        </section>

        <section class="search-chef-list" v-if="searchData?.content.length > 0">
            <div>
                <ul>
                    <li v-for="(item, index) in searchData?.content" :key="item.userId" class="chef-card-wrap">
                        <ChefCard :chefData="item" :index="index + currentPage * 10" />
                    </li>
                </ul>
            </div>

            <div class="pagination" v-if="searchData?.page?.totalPages > 1">
                <!-- 이전 10개 페이지 그룹 버튼 -->
                <button v-if="searchData?.page.totalPages > 10 && currentPageGroup > 0" @click="prevPageGroup">&lt;&lt;</button>

                <!-- 현재 페이지 그룹에 해당하는 페이지 버튼들 -->
                <button
                    v-for="n in pageGroupEnd() - pageGroupStart()"
                    :key="n + pageGroupStart()"
                    :class="{
                        active: searchData?.page.number === n + pageGroupStart() - 1,
                    }"
                    @click="goToPage(n + pageGroupStart() - 1)"
                >
                    {{ n + pageGroupStart() }}
                </button>

                <!-- 다음 10개 페이지 그룹 버튼 -->
                <button v-if="searchData?.page.totalPages > 10 && (currentPageGroup + 1) * 10 < searchData?.page.totalPages" @click="nextPageGroup">
                    &gt;&gt;
                </button>
            </div>
        </section>

        <div v-else-if="serchedFlag" class="keyword-non">
            <div class="keyword-box">
                <p>
                    <span class="keyword">'{{ serchedKeyword || '전체' }}'</span>
                    쉐프에 대한 검색결과가 존재하지 않습니다.
                </p>
            </div>
        </div>
    </div>
</template>

<style lang="scss" scoped>
.chef-search-container {
    width: 50rem;
    margin: 0 auto;
    padding-top: 4rem;
    padding-bottom: 4rem;
    margin-bottom: 10rem;

    .search-bar-wrap {
        text-align: center;
        margin-bottom: 3rem;

        .search-bar {
            width: 30rem;
            margin: auto;
        }
        .category-wrap {
            margin-top: 2rem;
            display: flex;
            width: 20rem;
            margin: auto;
            margin-top: 1rem;
            gap: 2rem;

            .category {
                width: 3rem;
            }
        }
    }

    .search-chef-list {
        margin-top: 6rem;
        border-top: 1px solid rgb(224, 224, 224);

        .chef-card-wrap {
            border-bottom: 1px solid rgb(224, 224, 224);
            padding-bottom: 1rem;
            padding-top: 1rem;
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
        font-size: 2rem;
        word-break: break-all; /* 긴 단어 줄바꿈 */
        padding-top: 10rem;

        .keyword-box {
            width: 50rem;
            .keyword {
                color: green;
            }
        }
    }
}
</style>