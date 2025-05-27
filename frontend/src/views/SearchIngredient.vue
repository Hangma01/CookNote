<script setup>
import SearchRecipeCard from '@/components/search/SearchRecipeCard.vue';
import SelectDropDown from '@/components/ui/SelectDropDown.vue';
import { getCategory, getCategoryAll } from '@/services/categoryService';
import { getRecipeSearch, getRecipeSearchIngredient } from '@/services/recipeService';
import { useUserStore } from '@/stores/user';
import { errorMessages } from '@/utils/messages/errorMessages';
import { onMounted, reactive, ref, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';

// 화면 전환
const router = useRouter()
const route = useRoute()

// 검색 데이터
const searchData = ref()

// 검색어
const keyword = ref(null)

const handleSearchReq = () => {
    if (!keyword.value || keyword.value.trim() === '') {
        keyword.value=''
        alert(errorMessages.SEARCH_INGREDIENT_EMPTY_VALUE_ERROR_MESSAGE);
        return;  // 검색 진행하지 않고 함수 종료
    }

    handleSearch()
}

// 검색 실행
const handleSearch = async(page = 0) => {

    if (!keyword.value || keyword.value.trim() === '') {    
        return; 
    }
    // 쿼리스트링에 검색조건 반영
    router.replace({
        query: {
            keyword: keyword.value || '',
            page,
        }
    });

    try {
        const res = await getRecipeSearchIngredient(
              keyword.value
            , page
        )
    
        searchData.value = res.data
    } catch (e) {
        console.log(e)
        if (e.response && e.response?.data?.message) {
            alert(e.response.data.message) 
        } else {
            alert(errorMessages.BADREQUEST)
        }
    }
};

const goToPage = (page) => {
    handleSearch(page);
};


// 초기 셋팅
onMounted( () => {
    const page = parseInt(route.query.page) || 0;
    keyword.value = route.query.keyword || '';

    handleSearch(page)
})
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
                    @click:append-inner="handleSearchReq()"
                    @keyup.enter="handleSearchReq()"
			/>
            </div>
            <div class="search-rule">
                <p>ⓘ 여러개의 재료 입력 시에 각 재료는 <strong class="search-rule-strong">띄어쓰기</strong>로 구분해주세요.</p>
            </div>
        </section> 

        <section class="search-recipe-list">
            <div>
                <ul class="recipe-card-wrap">
                    <li
                        v-for="(item, index) in searchData?.content"
                        :key="index"
                    >
                        <SearchRecipeCard :recipeData = "item" />
                    </li>
                </ul>
            </div>

            <div class="pagination" v-if="searchData?.page?.totalPages > 1">
                <button
                    v-for="n in searchData?.page?.totalPages"
                    :key="n"
                    :class="{ active: searchData?.page?.number === n - 1 }"
                    @click="goToPage(n - 1)"
                    >
                    {{ n }}
                </button>
            </div>
        </section>
    </div>
    
</template>

<style lang="scss" scoped>
.search-container {
    width: 69rem;
    margin: 0 auto;
    padding-top: 4rem;
    padding-bottom: 4rem;


    .search-bar-wrap {
        text-align: center;
        padding-bottom: 3rem;
        border-bottom: 1px solid rgb(224,224,224);
        
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
            }
        }
    }   
}

</style>