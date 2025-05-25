<script setup>
import { getHostRecipePublic } from '@/services/recipeService';
import { getHostProfile, getHostProfileLoggedIn } from '@/services/userService';
import { useUserStore } from '@/stores/user';
import { errorMessages } from '@/utils/messages/errorMessages';
import { onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import ProfileRecipeCard from './ProfileRecipeCard.vue';


// 화면 전환
const router = useRouter()

// 유저 스토어
const userStore = useUserStore();
const isLoggedIn = userStore.isLoggedIn;

// 프로파일 데이터
const profile = ref(null)

// 공개 레시피 데이터
const hostRecipePublicData = ref(null)
const recipPublicPage = ref(0)

// 파리미터 값 가져오기
const route = useRoute();
const hostId = route.params.hostId;

// 공개 레시피 가져오기
const loadRecipPublic = async (page = 0) => {
  try {
    const res = await getHostRecipePublic(hostId, page); // page 파라미터 넘김

    hostRecipePublicData.value = res.data

    // 현재 페이지 저장
    recipPublicPage.value = res.data.page.number;;
    
    
  } catch (e) {        
    if (e.response && e.response?.data?.message) {
        alert(e.response.data.message) 
    } else {
        alert(errorMessages.BADREQUEST)
    }

    router.push({ name : 'mainPage'})
  }
};

const goToPage = (page) => {
    loadRecipPublic(page);
};

onMounted(async () => {
    try {
        const isHostProfile = ref(true)
    
        const [hostProfileRes, hostRecipePublicDataRes] = await Promise.all([
            getHostProfileLoggedIn(hostId),
            getHostRecipePublic(hostId)
        ]);

        profile.value = hostProfileRes.data
        hostRecipePublicData.value = hostRecipePublicDataRes.data
        
        if(isLoggedIn) {
            if(userStore.getUserId == hostId) {
                isHostProfile.value = false
            }
        }

        userStore.setProfile({
                              ...profile.value
                            , isHostProfile: isHostProfile.value
                            })
    } catch (e) {
        if (e.response && e.response?.data?.message) {
            alert(e.response.data.message) 
        } else {
            alert(errorMessages.BADREQUEST)
        }

        router.push({ name : 'mainPage'})
    }
    
})
</script>

<template>
    <div class="recipe-title">
        <p>공개 레시피 수 {{ profile?.recipePublicCount }}</p>
    </div>
    <div class="line"></div>
    <div>
        <ul>
            <li 
                class="recipe-content"
                v-for="(item,index) in hostRecipePublicData?.content"
                :key="index"
            >
                <ProfileRecipeCard :recipeData = "item" />
            </li>
        </ul>

        <div class="pagination" v-if="hostRecipePublicData?.page?.totalPages > 1">
            <button
                v-for="n in hostRecipePublicData?.page?.totalPages"
                :key="n"
                :class="{ active: hostRecipePublicData?.page?.number === n - 1 }"
                @click="goToPage(n - 1)"
                >
                {{ n }}
            </button>
        </div>
    </div>        
</template>


<style lang="scss" scoped>
.recipe-title {
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
    border-bottom: 1px solid rgb(224, 224, 224);
}

.pagination {
    margin-top: 1rem;
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
</style>