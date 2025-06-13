<script setup>
import RecipeCard from '@/components/ui/RecipeCard.vue';
import SelectDropDown from '@/components/ui/SelectDropDown.vue';
import { ConditonalType } from '@/constans/conditionalType';
import { getCategory, getCategoryAll } from '@/services/categoryService';
import { getRecipeSearch, getRecipeSearchIngredient } from '@/services/recipeService';
import { useUserStore } from '@/stores/user';
import { errorMessages } from '@/utils/messages/errorMessages';
import { onMounted, reactive, ref, watch } from 'vue';
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

// 조건 데이터
const conditionalType = ref(ConditonalType.POPULAR);

// 조건 토글
const selectConditionalType = (type) => {
    conditionalType.value = type;
    handleSearch(currentPage.value, false); // 정렬 변경 후 재검색
};

// 검색 실행
const handleSearch = async (page = 0, isReplace = true) => {
    if (isReplace) {
        // 쿼리스트링에 검색조건 반영
        router.replace({
            query: {
                keyword: keyword.value || '',
                conditionalType: conditionalType.value,
                page,
            },
        });
    }

    try {
        const res = await getRecipeSearchIngredient(keyword.value, conditionalType.value, page);

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
    if (searchData.value && (currentPageGroup.value + 1) * 10 < searchData.value.page.totalPages) {
        const newPage = (currentPageGroup.value + 1) * 10;
        handleSearch(newPage);
        window.scrollTo(0, 0);
    }
};

// 초기 셋팅
onMounted(() => {
    const page = parseInt(route.query.page) || 0;
    keyword.value = route.query.keyword || '';
    conditionalType.value = route.query.conditionalType || ConditonalType.POPULAR;

    handleSearch(page);
});
</script>

<template>
    <div class="search-container">
        <section class="search-bar-wrap">
            <div class="search-bar">
                <v-text-field
                    v-model="keyword"
                    type="text"
                    variant="outlined"
                    density="compact"
                    hide-details="auto"
                    placeholder="재료를 입력하세요."
                    append-inner-icon="mdi-magnify"
                    aria-autocomplete="false"
                    @click:append-inner="handleSearch()"
                    @keyup.enter="handleSearch()"
                />
            </div>
            <div class="search-rule">
                <p>
                    ⓘ 여러개의 재료 입력 시에 각 재료는
                    <strong class="search-rule-strong">띄어쓰기</strong>로 구분해주세요.
                </p>
            </div>

            <div class="conditional-box">
                <div>
                    총 <span class="recipe-count">{{ searchData?.page.totalElements }}</span
                    >개의 결과
                </div>
                <div>
                    <button
                        class="conditional-btn"
                        :class="{
                            active: conditionalType === ConditonalType.POPULAR,
                        }"
                        @click="selectConditionalType(ConditonalType.POPULAR)"
                        v-if="searchData?.content.length > 0"
                    >
                        인기순
                    </button>
                    <button
                        class="conditional-btn latest-btn"
                        :class="{
                            active: conditionalType === ConditonalType.LATEST,
                        }"
                        @click="selectConditionalType(ConditonalType.LATEST)"
                        v-if="searchData?.content.length > 0"
                    >
                        최신순
                    </button>
                </div>
            </div>
        </section>

        <section class="search-recipe-list" v-if="searchData?.content.length > 0">
            <div>
                <ul class="recipe-card-wrap">
                    <li v-for="(item, index) in searchData?.content" :key="index">
                        <RecipeCard :recipeData="item" />
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
                    에 대한 검색결과가 존재하지 않습니다.
                </p>
            </div>
        </div>
    </div>
</template>

<style lang="scss" scoped>
.search-container {
    width: 69rem;
    margin: 0 auto;
    padding-top: 4rem;
    padding-bottom: 4rem;
    margin-bottom: 10rem;

    .search-bar-wrap {
        text-align: center;
        border-bottom: 1px solid rgb(224, 224, 224);

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

        .search-rule {
            margin-top: 1rem;
            color: rgb(77, 77, 77);

            .search-rule-strong {
                color: red;
            }
        }

        .conditional-box {
            margin-top: 3rem;
            margin-bottom: 1rem;
            display: flex;
            justify-content: space-between;
            margin-right: 1rem;
            gap: 2rem;

            .recipe-count {
                color: rgb(147, 112, 98);
                font-weight: 600;
            }

            .conditional-btn {
                color: #a89d94;

                &.active {
                    color: rgb(147, 112, 98);
                    font-weight: bold;
                }
            }

            .latest-btn {
                margin-left: 2rem;
            }
        }
    }

    .search-recipe-list {
        margin-top: 2rem;

        .recipe-card-wrap {
            display: grid;
            grid-template-columns: repeat(4, 1fr);
            gap: 4rem 1.5rem;
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

        .keyword-box {
            width: 50rem;
            .keyword {
                color: green;
            }
        }
    }
}
</style>