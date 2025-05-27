<script setup>
import RecipeCard from '@/components/ui/RecipeCard.vue';
import SelectDropDown from '@/components/ui/SelectDropDown.vue';
import { getCategory, getCategoryAll } from '@/services/categoryService';
import { getRecipeSearch } from '@/services/recipeService';
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

// 카테고리 데이터
const categories = ref(null)


const formValues = reactive({
    categories: {
        cuisine: { id: 0, type: '종류' },
        purpose: { id: 0, type: '목적' }
    }
})

const handleSearchReq = () => {
    
    if (!keyword.value || keyword.value.trim() === '') {
        keyword.value=''
        alert(errorMessages.SEARCH_EMPTY_VALUE_ERROR_MESSAGE);
        return;  // 검색 진행하지 않고 함수 종료
    }
    
    handleSearch()
}

// 검색 실행
const handleSearch = async(page = 0) => {
    // 쿼리스트링에 검색조건 반영
    router.replace({
        query: {
            keyword: keyword.value || '',
            categorycuisine: formValues.categories.cuisine.id,
            categorypurpose: formValues.categories.purpose.id,
            page,
        }
    });


    try {
        const res = await getRecipeSearch(
              keyword.value
            , formValues.categories.cuisine.id
            , formValues.categories.purpose.id
            , page
        )
        searchData.value = res.data
    } catch (e) {
        if (e.response && e.response?.data?.message) {
            alert(e.response.data.message) 
        } else {
            alert(errorMessages.BADREQUEST)
        }
    }
};

// 쿼리 기반으로 드롭다운 초기값 설정
const initCategoryFromQuery = () => {
    const cuisineId = parseInt(route.query.categorycuisine);
    const purposeId = parseInt(route.query.categorypurpose);
    keyword.value = route.query.keyword || '';

    if (!isNaN(cuisineId) && categories.value?.categoryCuisineList) {
        const cuisine = categories.value.categoryCuisineList.find(c => c.id === cuisineId);
        if (cuisine) {
            formValues.categories.cuisine = cuisine
        }
    }

    if (!isNaN(purposeId) && categories.value?.categoryPurposeList) {
        const purpose = categories.value.categoryPurposeList.find(p => p.id === purposeId);
        if (purpose) {
            formValues.categories.purpose = purpose
        }
    }

};


const goToPage = (page) => {
    handleSearch(page);
};

watch(() => formValues.categories.cuisine.id, () => {
    handleSearch()
})

watch(() => formValues.categories.purpose.id, () => {
    handleSearch()
})


watch(
  () => route.query,
  (newQuery, oldQuery) => {
    // 쿼리 변경 시 검색 조건 초기화 또는 검색 실행
    initCategoryFromQuery()
  }
)

// 초기 셋팅
onMounted(async () => {
    const page = parseInt(route.query.page) || 0;
    try {
        const categoriesRes = await getCategory()
        categories.value = {
                            categoryCuisineList: [{ id: 0, type: '종류' }, ...categoriesRes.data.categoryCuisineList],
                            categoryPurposeList: [{ id: 0, type: '목적' }, ...categoriesRes.data.categoryPurposeList],
        }

        // 카테고리 로딩 후 쿼리 반영
        initCategoryFromQuery();
    } catch (e) {
        if (e.response && e.response?.data?.message) {
            alert(e.response.data.message)  
        } else {
            alert(errorMessages.BADREQUEST)
        }
    }

    handleSearch(page);
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
                    placeholder="검색어를 입력하세요."
                    append-inner-icon="mdi-magnify"
                    aria-autocomplete="false"
                    @click:append-inner="handleSearchReq()"
                    @keyup.enter="handleSearchReq()"
			/>
            </div>

            <div class="category-wrap">
                <SelectDropDown
                    v-model="formValues.categories.cuisine"
                    :items="categories?.categoryCuisineList"
                    item-value="id"
                    item-title="type"
                    label="종류"
                    class="category"
                />
                <SelectDropDown
                    v-model="formValues.categories.purpose"
                    :items="categories?.categoryPurposeList"
                    item-value="id"
                    item-title="type"
                    label="종류"
                    class="category"
                />
            </div>
        </section> 

        <section class="search-recipe-list">
            <div >
                <ul class="recipe-card-wrap">
                    <li
                        v-for="(item, index) in searchData?.content"
                        :key="index"
                    >
                        <RecipeCard :recipeData = "item" />
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