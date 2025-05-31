<script setup>
import { getHostRecipePublic } from '@/services/recipeService';
import { getHostProfile, getHostProfileLoggedIn } from '@/services/userService';
import { useUserStore } from '@/stores/user';
import { errorMessages } from '@/utils/messages/errorMessages';
import { onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import ProfileRecipeCard from './ProfileRecipeCard.vue';

// 화면 전환
const router = useRouter();

// 유저 스토어
const userStore = useUserStore();
const isLoggedIn = userStore.isLoggedIn;

// 프로파일 데이터
const profile = ref(null);

// 페이징
const currentPage = ref(0);
const currentPageGroup = ref(0);

// 공개 레시피 데이터
const hostRecipePublicData = ref(null);

// 파리미터 값 가져오기
const route = useRoute();
const hostId = route.params.hostId;

const scrollToTop = () => {
    window.scrollTo({ top: 0 });
};

// 공개 레시피 가져오기
const loadRecipPublic = async (page = 0) => {
    try {
        const res = await getHostRecipePublic(hostId, page); // page 파라미터 넘김
        hostRecipePublicData.value = res.data;

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
    if (!hostRecipePublicData.value) return 0;
    return Math.min(pageGroupStart() + 10, hostRecipePublicData.value.page.totalPages);
};

// 페이지 이동
const goToPage = (page) => {
    scrollToTop();
    loadRecipPublic(page);
};

// 이전 10페이지 그룹
const prevPageGroup = () => {
    if (currentPageGroup.value > 0) {
        const newPage = (currentPageGroup.value - 1) * 10;
        scrollToTop();
        loadRecipPublic(newPage);
    }
};

// 다음 10페이지 그룹
const nextPageGroup = () => {
    if (hostRecipePublicData.value && (currentPageGroup.value + 1) * 10 < hostRecipePublicData.value.page.totalPages) {
        const newPage = (currentPageGroup.value + 1) * 10;
        scrollToTop();
        loadRecipPublic(newPage);
    }
};

onMounted(async () => {
    try {
        const isHostProfile = ref(true);

        const [hostProfileRes, hostRecipePublicDataRes] = await Promise.all([getHostProfileLoggedIn(hostId), getHostRecipePublic(hostId)]);

        profile.value = hostProfileRes.data;
        hostRecipePublicData.value = hostRecipePublicDataRes.data;

        if (isLoggedIn) {
            if (userStore.getUserId == hostId) {
                isHostProfile.value = false;
            }
        }

        userStore.setProfile({
            ...profile.value,
            isHostProfile: isHostProfile.value,
        });
    } catch (e) {
        if (e.response && e.response?.data?.message) {
            alert(e.response.data.message);
        } else {
            alert(errorMessages.BADREQUEST);
        }

        router.push({ name: 'mainPage' });
    }
});
</script>

<template>
    <div class="recipe-title">
        <p>공개 레시피 수 {{ profile?.recipePublicCount }}</p>
    </div>
    <div class="line"></div>
    <div>
        <ul>
            <li class="recipe-content" v-for="(item, index) in hostRecipePublicData?.content" :key="index">
                <ProfileRecipeCard :recipeData="item" />
            </li>
        </ul>

        <div class="pagination" v-if="hostRecipePublicData?.page?.totalPages > 1">
            <!-- 이전 10개 페이지 그룹 버튼 -->
            <button v-if="hostRecipePublicData.page.totalPages > 10 && currentPageGroup > 0" @click="prevPageGroup">&lt;&lt;</button>

            <!-- 현재 페이지 그룹에 해당하는 페이지 버튼들 -->
            <button
                v-for="n in pageGroupEnd() - pageGroupStart()"
                :key="n + pageGroupStart()"
                :class="{ active: hostRecipePublicData.page.number === n + pageGroupStart() - 1 }"
                @click="goToPage(n + pageGroupStart() - 1)"
            >
                {{ n + pageGroupStart() }}
            </button>

            <!-- 다음 10개 페이지 그룹 버튼 -->
            <button
                v-if="hostRecipePublicData.page.totalPages > 10 && (currentPageGroup + 1) * 10 < hostRecipePublicData.page.totalPages"
                @click="nextPageGroup"
            >
                &gt;&gt;
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
</style>