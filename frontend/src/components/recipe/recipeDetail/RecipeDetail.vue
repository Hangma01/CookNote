<script setup>
import { getDetailRecipe } from "@/services/recipeService";
import { onMounted, ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import RecipeDetailInfo from "./RecipeDetailInfo.vue";
import ReicpeDetailVideo from "./ReicpeDetailVideo.vue";
import RecipeDetailIngredient from "./RecipeDetailIngredient.vue";
import RecipeDetailSeq from "./RecipeDetailSeq.vue";
import RecipeDetailCommend from "./RecipeDetailCommend.vue";
import { getComments } from "@/services/commentService";
import { errorMessages } from "@/utils/messages/errorMessages";

// 화면 전환
const router = useRouter();

// RecipeId 찾기
const route = useRoute()
const recipeId = route.params.recipeId || null

const recipeDetailData = ref(null);
const commentsData = ref([]);
const currentPage = ref(0);

// 레시피 작성 / 수정 시 가져올 데이터
onMounted(async () => {
    try {
        const [recipeDetailDataRes, commentsDataRes] = await Promise.all([
                getDetailRecipe(recipeId),
                getComments(recipeId,0),
            ]);
        recipeDetailData.value = recipeDetailDataRes.data;

        commentsData.value = commentsDataRes.data
    } catch (e) {
        if (e.response && e.response?.data?.message) {
            alert(e.response.data.message)
            router.back()  
        } else {
            router.replace({name:'notFound'})
        }
    }
    
})

// 페이지별 데이터 가져오기
const loadPage = async (page = 0) => {
  try {
    const res = await getComments(recipeId, page);
    commentsData.value = res.data;
    currentPage.value = res.data.page.number;
  } catch (e) {
    if (e.response && e.response?.data?.message) {
        alert(e.response.data.message)
    } else {
        alert(errorMessages.BADREQUEST)
    }
  }
};

// 댓글 작성시 새로고침
const refreshComments = () => {
  loadPage();
};

</script>

<template>
    <div class="recipe-detail-container">
        <section>
            <RecipeDetailInfo :recipeDetailData = "recipeDetailData" :recipeId = "recipeId"/>
        </section>
            
        <section>
            <ReicpeDetailVideo :youtubeVideoId = "recipeDetailData?.videoId"/>
        </section>
            
        <section>
            <RecipeDetailIngredient :recipeIngredients = "recipeDetailData?.recipeIngredients"/>
        </section>

        <section>
            <RecipeDetailSeq :recipeSeqs = "recipeDetailData?.recipeSeqs"/>
        </section>

        <section>
            <RecipeDetailCommend 
            :commentsData = "commentsData" 
            :recipeId = "recipeId" 
            :recipeWriterId = "recipeDetailData?.writerUserId"
            :requesterId = "recipeDetailData?.requesterId"
            @refreshComments="refreshComments"
            @changePage="loadPage"/>
        </section>
    </div>
</template>


<style lang="scss" scoped>
.recipe-detail-container {
    width: 45rem;
    margin: auto;
    margin-bottom: 6rem;
    margin-top: 3rem;
}


</style>