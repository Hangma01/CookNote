<script setup>
import { getDetailRecipe, getRecipeLikeCount } from '@/services/recipeService';
import { onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import RecipeDetailInfo from './RecipeDetailInfo.vue';
import ReicpeDetailVideo from './ReicpeDetailVideo.vue';
import RecipeDetailIngredient from './RecipeDetailIngredient.vue';
import RecipeDetailSeq from './RecipeDetailSeq.vue';
import RecipeDetailCommend from './RecipeDetailCommend.vue';
import { getComments, getRepliesCount } from '@/services/commentService';
import { errorMessages } from '@/utils/messages/errorMessages';
import RecipeTip from './RecipeTip.vue';
import { getCategoryReportReason } from '@/services/categoryService';
import ReportModal from '@/components/ui/ReportModal.vue';
import { HttpStatusCode } from 'axios';

// 화면 전환
const router = useRouter();

// RecipeId 찾기
const route = useRoute();
const recipeId = route.params.recipeId || null;

// 데이터 저장
const recipeDetailData = ref(null);
const commentsData = ref([]);
const categoryReportReason = ref(null);
const currentPage = ref(0);
const showReportModal = ref(false);
const repliesCountData = ref(null);
const recipeLikeCount = ref(0);

// 어떤 게시글/댓글에 대한 신고인지 저장
const reportType = ref(null);

// 어떤 게시글/댓글에 대한 신고 Id 저장
const targetId = ref(null);
const reportedId = ref(null);

// 모달 열기 함수
const openReportModal = (type, id, reported) => {
    reportType.value = type;
    targetId.value = id;
    reportedId.value = reported;
    showReportModal.value = true;
};

// 모달 닫기 함수
const closeReportModal = () => {
    showReportModal.value = false;
};

// 초기 값 가져오기
onMounted(async () => {
    try {
        const [recipeDetailDataRes, commentsDataRes, categoryReportReasonRes, repliesCountRes, recipeLikeCountRes] = await Promise.all([
            getDetailRecipe(recipeId),
            getComments(recipeId, 0),
            getCategoryReportReason(),
            getRepliesCount(recipeId),
            getRecipeLikeCount(recipeId),
        ]);

        recipeDetailData.value = recipeDetailDataRes.data;
        commentsData.value = commentsDataRes.data;
        categoryReportReason.value = categoryReportReasonRes.data;
        repliesCountData.value = repliesCountRes.data;
        recipeLikeCount.value = recipeLikeCountRes.data;
    } catch (e) {
        if (e.response && e.response?.data?.message) {
            if (e.status === HttpStatusCode.NotFound) {
                router.push({ name: 'notFound' });
            } else {
                alert(e.response.data.message);
                router.back();
            }
        } else {
            alert(errorMessages.BADREQUEST);
            router.back();
        }
    }
});

// 페이지별 데이터 가져오기
const loadPage = async (page = 0) => {
    try {
        const res = await getComments(recipeId, page);
        commentsData.value = res.data;
        currentPage.value = res.data.page.number;
    } catch (e) {
        if (e.response && e.response?.data?.message) {
            alert(e.response.data.message);
        } else {
            alert(errorMessages.BADREQUEST);
        }
    }
};

// 댓글 작성시 새로고침
const refreshComments = () => {
    loadPage();
};

// 답글 작성시 새로고침
const refreshReply = async () => {
    try {
        const res = await getRepliesCount(recipeId);
        repliesCountData.value = res.data;
    } catch (e) {
        if (e.response && e.response?.data?.message) {
            alert(e.response.data.message);
        } else {
            alert(errorMessages.BADREQUEST);
        }

        router.push({ name: 'mainPage' });
    }
};

// 좋아요 클릭 시 새로고침
const refreshLike = async () => {
    try {
        const res = await getRecipeLikeCount(recipeId);
        recipeLikeCount.value = res.data;
    } catch (e) {
        if (e.response && e.response?.data?.message) {
            alert(e.response.data.message);
        } else {
            alert(errorMessages.BADREQUEST);
        }

        router.push({ name: 'mainPage' });
    }
};
</script>

<template>
    <div class="recipe-detail-container">
        <section>
            <RecipeDetailInfo
                :recipeDetailData="recipeDetailData"
                :recipeId="recipeId"
                :recipeLikeCount="recipeLikeCount"
                @refreshLike="refreshLike"
                @openReportModal="openReportModal"
            />
        </section>

        <section>
            <ReicpeDetailVideo :youtubeVideoId="recipeDetailData?.videoId" />
        </section>

        <section>
            <RecipeDetailIngredient :recipeIngredients="recipeDetailData?.recipeIngredients" />
        </section>

        <section>
            <RecipeDetailSeq :recipeSeqs="recipeDetailData?.recipeSeqs" />
        </section>

        <section>
            <RecipeTip :recipeTip="recipeDetailData?.tip" />
        </section>

        <section>
            <RecipeDetailCommend
                :commentsData="commentsData"
                :recipeId="recipeId"
                :recipeWriterId="recipeDetailData?.writerId"
                :requesterId="recipeDetailData?.requesterId"
                :repliesCountData="repliesCountData"
                @refreshComments="refreshComments"
                @refreshReply="refreshReply"
                @changePage="loadPage"
                @openReportModal="openReportModal"
            />
        </section>
    </div>

    <ReportModal
        v-if="showReportModal"
        :categoryReportReason="categoryReportReason"
        :reportType="reportType"
        :targetId="targetId"
        :reportedId="reportedId"
        @closeReportModal="closeReportModal"
    />
</template>


<style lang="scss" scoped>
.recipe-detail-container {
    width: 45rem;
    margin: auto;
    margin-bottom: 10rem;
    margin-top: 3rem;
}
</style>