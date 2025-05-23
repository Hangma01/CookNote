<script setup>
import { getDetailRecipe } from "@/services/recipeService";
import { onMounted, ref } from "vue";
import { useRoute, useRouter } from "vue-router";


// 화면 전환
const router = useRouter();


const props = defineProps({ 
    youtubeVideoId: { 
        type: String    
    },
})

const youtubeEmbedUrl = `https://www.youtube.com/embed/${props.youtubeVideoId}`

</script>

<template>
    <div class="recipe-detail-video-section">
        <div class="section-title">
            <p>동영상</p>
        </div>
        <p>{{ youtubeEmbedUrl }}</p>
        <div>
            <iframe 
            width="560" 
            height="315" 
            src="https://www.youtube.com/embed/JVQaQBsCbrE" 
            title="YouTube video player" 
            frameborder="0" 
            allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
            referrerpolicy="strict-origin-when-cross-origin" 
            allowfullscreen></iframe>
            <iframe :src="youtubeEmbedUrl" frameborder="0"  allowfullscreen clipboard-write encrypted-media gyroscope picture-in-picture></iframe>
        </div>
    </div>
</template>


<style lang="scss" scoped>
.recipe-detail-video-section {
    margin-top: 5rem;

    .section-title {
        font-size: 1.2rem;
        font-weight: bold;
        border-bottom: 2px solid rgb(147, 112, 98);
        padding-bottom: 1rem;
        margin-bottom: 0.7em
    }
}
</style>