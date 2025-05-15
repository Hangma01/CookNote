<script setup>
import ImageUploader from '@/components/image/ImageUploader.vue';
import { commonValues } from '@/utils/commonValues';
import { ref, shallowRef, watch } from 'vue';


const title = ref('')
const desciption = ref('')
const youtubeUrl = ref('')
const videoId = ref(null)

// 부모 컴포넌트에서 접근 가능하도록 허용하는 함수
defineExpose({
  title,
  desciption,
  videoId,
})

  const select = shallowRef()

  const items = [
    { state: '한식', no: 1},
    { state: '일식', no: 2 },
    { state: '양식', no: 3 },
    { state: '중식', no: 4 },
    { state: '퓨전', no: 5 },
    { state: '디저트', no: 6 },
  ]

const selectedFile = ref(null);


const getYoutubeVideoId = () => {
  const regex = commonValues.YOUTUBE_VIDEO_ID_REGEX;
  const match = youtubeUrl.value.match(regex)
  return match ? match[1] : null
}

const handleYotubeURL = () => {
  videoId.value = getYoutubeVideoId()
}
</script>

<template>
  <h2 class="sub-title">기본정보</h2>

  <div class="section-group">
    <div class="section-group-first">
      <div class="label-title">
        <p>레시피 제목</p>
      </div>

      <div class="input-filed-wrap">
        <v-text-field
          v-model="title"
          type="text"
          label=""
          placeholder="예) 된장찌개 끓이기"
          variant="outlined"
          density="compact"
          hide-details=true
        />
      </div>

      <div class="label-title">
        <p>레시피 소개</p>
      </div>

      <div class="input-filed-wrap">
        <v-textarea
          v-model="desciption"
          type="text"
          label=""
          placeholder="예) 된장찌개 끓이기"
          variant="outlined"
          density="compact"
          hide-details="auto"
          rows="4"
          no-resize
        />
      </div>

      <div class="input-filed-wrap">
        <div class="label-title">
          <p>레시피 동영상 URL</p>
          <p class="label-title-sub">ⓘ 레시피 동영상 등록은 Youtube만 가능합니다.</p>
        </div>

        <v-text-field
          v-model="youtubeUrl "
          type="text"
          label=""
          placeholder="https://"
          variant="outlined"
          density="compact"
          hide-details=true
          @input="handleYotubeURL"
        />
      </div>

      <div>
        <div class="label-title">
          <p>카테고리</p>
        </div>


        <div class="input-filed-wrap category-dropdown">
          <div class="category-item">
              <v-select
                v-model="select"
                :items="items"
                item-title="state"
                return-object
                label="종류"
                variant="outlined"
                hide-details=true
                density="compact"
                single-line
              />
          </div>
          
          <div class="category-item">

            <v-select
              v-model="select"
              :items="items"
              item-title="state"
              return-object
              label="목적"
              variant="outlined"
              hide-details=true
              density="compact"
              single-line
            />
          </div>

          <div class="category-item">
            <v-select
              v-model="select"
              :items="items"
              item-title="state"
              return-object
              label="요리시간"
              variant="outlined"
              hide-details=true
              density="compact"
              single-line
            />
          </div>

          <div class="category-item">

            <v-select
              v-model="select"
              :items="items"
              item-title="state"
              return-object
              label="난이도"
              variant="outlined"
              hide-details=true
              density="compact"
              single-line
            />
          </div>
        </div>

      </div>
    </div>


    <div class="section-group-second">
      <div class="image-preview-wrap">
        <div class="label-title">
          <p>썸네일</p>        
        </div>
        
        <ImageUploader v-model="selectedFile" />
      </div>
      
      <div class="image-preview-wrap">
        <div class="label-title">
          <p>동영상 썸네일</p>
        </div>
        
        <div class="video-thumnail-wrap">
          <div v-if="videoId" class="video-thumnail-box">
            <img 
              class="video-thumnail"
              :src="`https://img.youtube.com/vi/${videoId}/0.jpg`" 
              alt="Youtube 동영상 썸네일"/>
          </div>

          <div v-else>
            <span>Youtube 동영상 썸네일</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.sub-title {
  font-size: 1.2rem;
  color: #c09370;
  padding-bottom: 1.5rem;
}

.section-group {
  display: flex;
  justify-content: space-between;

  .section-group-first {
    width: 40rem;

    .input-filed-wrap{
      padding-bottom: 2rem;
    }

    .category-dropdown{
      display: flex;
      justify-content: space-between;

      .category-item{
        width: 8rem;
      }
    }
  }

  .section-group-second {
    width: 20rem;

    
    .image-preview-wrap{
      padding-bottom: 2rem;
    }

    .video-thumnail-wrap{
      width: 100%;
      height: 12rem;
      border: 1px solid #e7e7e7;
      border-radius: 1rem;
      background-color: #eee;
      display: flex;
      align-items: center;
      justify-content: center;
      color: #aaa;
      font-size: 1.2rem;

      .video-thumnail-box, .video-thumnail{
        width: 100%;
        height: 100%;
        border-radius: 1rem;
      }
    }
  }

  .label-title {
    padding-bottom: 0.6rem;

    .label-title-sub{
      font-size: 0.8rem;
      color: #777;
    }
  }
}
</style>