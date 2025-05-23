<script setup>const props = defineProps({ 

youtubeVideoId: { 
        type: String    
    },
})

</script>

<template>
    <div class="recipe-detail-video-section" v-if="props?.youtubeVideoId">
        <div class="section-title">
            <p>동영상</p>
        </div>
        <div class="youtube-video-wrap">
            <iframe :src="`https://www.youtube.com/embed/${props?.youtubeVideoId}`" 
            title="YouTube video player" 
            frameborder="0" 
            allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
            referrerpolicy="strict-origin-when-cross-origin" 
            allowfullscreen
            class="yotube-video"
            ></iframe>
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

    .youtube-video-wrap {
        text-align: center;

        .yotube-video {
            margin-top: 1rem;
            width: 30rem;
            height: 16.9rem;
            border-radius: 0.8rem;
        }
    }

}
</style>