<script setup>
import ImageUploader from '@/components/image/ImageUploader.vue';
import SelectDropDown from './SelectDropDown.vue';
import { commonInputHangle } from '@/utils/commonFunction';
import { reactive, ref } from 'vue';
import { commonValues } from '@/utils/commonValues';


const items = [
  { state: '한식', no: 1},
  { state: '일식', no: 2 },
  { state: '양식', no: 3 },
  { state: '중식', no: 4 },
  { state: '퓨전', no: 5 },
  { state: '디저트', no: 6 },
]

const formValues = reactive({
  title: '',
  description: '',
  videoId: null,
  serving: null,
  duration: null,
  level: null,
  categories: {
    cuisine: null,
    purpose: null,
  }
});


const imageFile = ref(null)
const url = ref('')

const validation = () => {
  if (!formValues.title) return '제목을 입력하세요'
  else if (!formValues.description) return '요리소개를 입력하세요'
  else if (formValues.description.length <= 10) return '요리소개는 10자 이상 작성해주세요.'
  else if (!imageFile.value) return '썸네일을 넣어주세요.'
  else if (url.value && !formValues.videoId) return '유효한 유튜브 동영상 URL을 입력하세요.'
  else if (!formValues.categories.cuisine) return '카테고리 종류를 선택하세요'
  else if (!formValues.categories.purpose) return '카테고리 목적을 선택하세요'
  else if (!formValues.serving) return '인원수를 선택하세요'
  else if (!formValues.duration) return '요리시간을 선택하세요'
  else if (!formValues.level) return '난이도를 선택하세요'
  else return true
}

const getData = () => ({
  title: formValues.title,
  description: formValues.description,
  thumbnail: imageFile.value,
  videoId: formValues.videoId,
  serving: formValues.serving?.no,
  duration: formValues.duration?.no,
  level: formValues.level?.no,
  categories: {
    cuisine: formValues.categories.cuisine?.no ?? null,
    purpose: formValues.categories.purpose?.no ?? null
  },
})

const getYoutubeVideoId = () => {
  const regex = commonValues.YOUTUBE_VIDEO_ID_REGEX;
  const match = url.value.match(regex)
  return match ? match[1] : null
}

const handleGetVideoId = () => {
  formValues.videoId = getYoutubeVideoId()
}


// 부모가 사용할 수 있게 expose
defineExpose({
  getData,
  validation
})



// 제목 50자 제한 (한글)
const handleTitleInput = (e) => commonInputHangle(e, 50, (value) => formValues.title = value)

// 레시피 소개 250자 제한 (한글)
const handleDescriptionInput = (e) => commonInputHangle(e, 250, (value) => formValues.description = value)

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
          v-model="formValues.title"
          @input="handleTitleInput"
          type="text"
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
          v-model="formValues.description"
          @input="handleDescriptionInput"
          placeholder="예) 된장찌개 끓이기"
          rows="7"
          no-resize
          variant="outlined"
          density="compact"
          hide-details=true
        />
      </div>

      <div class="input-filed-wrap">
        <div class="label-title">
          <p>레시피 동영상 URL</p>
          <p class="label-title-sub">ⓘ 레시피 동영상 등록은 Youtube만 가능합니다.</p>
        </div>

        <v-text-field
          v-model="url"
          type="text"
          placeholder="https://"
          class="input-filed"
          @input="handleGetVideoId"
          variant="outlined"
          density="compact"
          hide-details=true
        />
      </div>
    </div>


    <div class="section-group-second">
      <div class="image-preview-wrap">
        <div class="label-title">
          <p>썸네일</p>        
        </div>
        
        <ImageUploader v-model="imageFile" />
      </div>
      
      <div class="image-preview-wrap">
        <div class="label-title">
          <p>동영상 썸네일</p>
        </div>
        
        <div class="video-thumnail-wrap">
          <div v-if="formValues.videoId" class="video-thumnail-box">
            <img 
              class="video-thumnail"
              :src="`https://img.youtube.com/vi/${formValues.videoId}/0.jpg`" 
              alt="Youtube 동영상 썸네일"/>
          </div>

          <div v-else>
            <span>Youtube 동영상 썸네일</span>
          </div>
        </div>
      </div>
    </div>
  </div>

  <div>
    <div class="label-title">
      <p>카테고리</p>
    </div>

    <div class="category-wrap">
      <div class="category-item">
          <SelectDropDown
            v-model="formValues.categories.cuisine"
            :items="items"
            item-title="state"
            label="종류"
          />
      </div>
      
      <div class="category-item">

        <SelectDropDown
          v-model="formValues.categories.purpose"
          :items="items"
          item-title="state"
          label="목적"
        />
      </div>

      <div class="category-item">
        <SelectDropDown
          v-model="formValues.serving"
          :items="items"
          item-title="state"
          label="인원수"
        />
      </div>

      <div class="category-item">
        <SelectDropDown
          v-model="formValues.duration"
          :items="items"
          item-title="state"
          label="요리시간"
        />
      </div>

      <div class="category-item">

        <SelectDropDown
          v-model="formValues.level"
          :items="items"
          item-title="state"
          label="난이도"
        />
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

      .textarea-filed {
        width: 100%;
        border: 1px solid #e7e7e7;
        border-radius: 0.5rem;
        padding: 0.5rem;
      }

      .textarea-filed {
        resize: none;
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
}

.label-title {
  padding-bottom: 0.6rem;

  .label-title-sub{
    font-size: 0.8rem;
    color: #777;
  }
}

.category-wrap {
  display: flex;
  gap: 3rem;  
  padding-bottom: 2rem;

  .category-item{
    width: 8rem;
  }
  
}
</style>