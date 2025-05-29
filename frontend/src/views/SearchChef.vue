<script setup>
import ChefCard from '@/components/ui/ChefCard.vue';
import { getChefSearch, getChefSearchLoggined } from '@/services/userService';
import { useUserStore } from '@/stores/user';

import { errorMessages } from '@/utils/messages/errorMessages';
import { onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

// 화면 전환
const router = useRouter()
const route = useRoute()

// 검색 데이터
const searchData = ref()

// 검색어
const keyword = ref(null)

// 유저 스토어
const userStore = useUserStore();
const isLoggedIn = userStore.isLoggedIn;
const userId = userStore.getUserId;

// 검색 요청 시
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

    // 쿼리스트링에 검색조건 반영
    router.replace({
        query: {
            keyword: keyword.value || '',
            page,
        }
    });

    try {
        let res = null;
        if(isLoggedIn) {
            res = await getChefSearchLoggined(
                keyword.value, 
                page
            )
        } else {
            res = await getChefSearch(
                keyword.value, 
                page
            )
        }
        console.log(res.data)
        searchData.value = res.data
    } catch (e) {
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
                    @click:append-inner="handleSearchReq()"
                    @keyup.enter="handleSearchReq()"
			/>
            </div>
        </section> 

        <section class="search-chef-list">
            <div>
                <ul>
                    <li
                        v-for="(item, index) in searchData?.content"
                        :key="index"
                        class="chef-card-wrap"
                    >   
                        <ChefCard :chefData = "item" :index = "index"/>
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
.chef-search-container {
    width: 50rem;
    margin: 0 auto;
    padding-top: 4rem;
    padding-bottom: 4rem;


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
        border-top: 1px solid rgb(224,224,224);
        
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
            }
        }
    }   
}

</style>