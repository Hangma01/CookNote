<script setup>
import ImageUploader from '@/components/image/ImageUploader.vue';
import { commonValues } from '@/utils/commonValues';
import { ref, shallowRef, watch } from 'vue';


const title = ref('')
const desciption = ref('')
const youtubeUrl = ref('')
const videoId = ref(null)
const serving = ref(null)
const level = ref(null)

const categories = ref({
  type: null,
  purpose: null
})

const getData = () => {
  return {
    title: title.value,
    description: desciption.value,
    videoId: videoId.value,
    serving: serving.value,
    level: level.value,
    categories: {
      type: categories.value.type?.no ?? null,
      purpose: categories.value.purpose?.no ?? null
    },
    thumbnail: selectedFile.value
  }
}

// 부모가 사용할 수 있게 expose
defineExpose({
  getData
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
        <input
          v-model="title"
          type="text"
          placeholder="예) 된장찌개 끓이기"
          class="input-filed"
        />
      </div>

      <div class="label-title">
        <p>레시피 소개</p>
      </div>

      <div class="input-filed-wrap">
        <textarea
          v-model="desciption"
          placeholder="예) 된장찌개 끓이기"
          rows="4"
          no-resize
          class="textarea-filed"
        ></textarea>
      </div>

      <div class="input-filed-wrap">
        <div class="label-title">
          <p>레시피 동영상 URL</p>
          <p class="label-title-sub">ⓘ 레시피 동영상 등록은 Youtube만 가능합니다.</p>
        </div>

        <input
          v-model="youtubeUrl "
          type="text"
          placeholder="https://"
          class="input-filed"
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
                v-model="categories.type"
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
              v-model="categories.purpose"
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
              v-model="serving"
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
              v-model="level"
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

      .input-filed, .textarea-filed {
        width: 100%;
        border: 1px solid #e7e7e7;
        border-radius: 0.5rem;
        padding: 0.5rem;
      }

      .textarea-filed {
        resize: none;
      }
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