<script setup>
import ImageUploader from '@/components/ui/ImageUploader.vue';
import SelectDropDown from '../../ui/SelectDropDown.vue';
import { commonInputHangle } from '@/utils/commonFunction';
import { reactive, ref, watch } from 'vue';
import { commonValues } from '@/utils/commonValues';
import { youtubeApi } from '@/services/googleService';
import { debounce } from 'lodash';


// RecipeWrite.vue로부터 받는 데이터
const props = defineProps({ 
    categories: {   // 레시피 작성 시 오는 데이터
        type: Object
    },
    originalRecipeData: { // 레시피 수정 시 오는 데이터
        type: Object
    },
    isEditMode: {
        type: Boolean
    }
})

// 입력 값 선언
const formValues = reactive({
    title: '',
    description: '',
    videoId: null,
    serving: null,
    duration: null,
    level: null,
    categories: {
        cuisine: null,
        purpose: null
    }
})

// 이미지 파일 선언
const imageFile = ref(null)

// url 선언
const url = ref('')


// 레시피 수정 시 데이터가 바뀐것을 감지해야햠
watch(() => props.originalRecipeData, (newVal) => {
    console.log(newVal)
    if (newVal) {
        formValues.title = newVal.title || ''
        formValues.description = newVal.description || ''
        formValues.videoId = newVal.videoId || null
        formValues.serving = props.categories.recipeServingList?.find(s => s.name === newVal.serving) || null
        formValues.duration = props.categories.recipeDurationList?.find(d => d.name === newVal.duration) || null
        formValues.level = props.categories.recipeLevelList?.find(l => l.name === newVal.level) || null
        formValues.categories.cuisine = props.categories.categoryCuisineList?.find(
            c => c.id === newVal.categoryCuisineId
        ) || null

        formValues.categories.purpose = props.categories.categoryPurposeList?.find(
            p => p.id === newVal.categoryPurposeId
        ) || null

        imageFile.value = newVal.thumbnail || null
        url.value = newVal.videoId ? `https://www.youtube.com/watch?v=${newVal.videoId}` : '';
    }
})

// 유효성 검사사
const validation = () => {
    if (!formValues.title) return '제목을 입력하세요'
    else if (!formValues.description) return '레시피 소개를 입력하세요'
    else if (formValues.description.length <= 10) return '레시피 소개는 10자 이상 작성해주세요.'
    else if (!imageFile.value) return '썸네일을 넣어주세요.'
    else if (url.value && !formValues.videoId) return '유효한 유튜브 동영상 URL을 입력하세요.'
    else if (!formValues.categories.cuisine) return '카테고리 종류를 선택하세요'
    else if (!formValues.categories.purpose) return '카테고리 목적을 선택하세요'
    else if (!formValues.serving) return '인원수를 선택하세요'
    else if (!formValues.duration) return '요리시간을 선택하세요'
    else if (!formValues.level) return '난이도를 선택하세요'
    else return true
}

// RecipeWrite.vue에게 보낼 데이터
const getData = () => ({
    title: formValues.title.trim(),
    description: formValues.description.trim(),
    thumbnail: imageFile.value,
    videoId: formValues.videoId,
    serving: formValues.serving?.name,
    duration: formValues.duration?.name,
    level: formValues.level?.name,
    categories: {
        cuisine: formValues.categories.cuisine?.id || null,
        purpose: formValues.categories.purpose?.id || null
    },
})

// Youtube VideoId 추출
const handleGetVideoId = debounce( async () => {
  const videoId = getYoutubeVideoId()

  const googleApiKey = import.meta.env.VITE_API_GOOGLE_KEY
  const url = `https://www.googleapis.com/youtube/v3/videos?part=id&id=${videoId}&key=${googleApiKey}`;
  try {
    const res = await youtubeApi(url);
    
    if(res.data.items.length > 0) {
      formValues.videoId = videoId
    }else {
      formValues.videoId = null;
    }
  } catch (error) {
    formValues.videoId = null;
  }
}, commonValues.defaultDebounce)

const getYoutubeVideoId = () => {
    const regex = commonValues.YOUTUBE_VIDEO_ID_REGEX;
    const match = url.value.match(regex)
    return match ? match[1] : null
}


// 제목 50자 제한 (한글)
const handleTitleInput = (e) => commonInputHangle(e, 50, (value) => formValues.title = value)

// 레시피 소개 400자 제한 (한글)
const handleDescriptionInput = (e) => commonInputHangle(e, 400, (value) => formValues.description = value)


// 부모가 사용할 수 있게 expose
defineExpose({
    getData,
    validation
})
</script>

<template>
  <h2 class="sub-title">
    <span>기본정보</span>
    
    <p class="required">ⓘ 제목, 레시피 소개, 썸네일, 카테고리는 필수 입력 항목입니다.</p>
  </h2>

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
          placeholder="제목은 50자 이내로 작성해주세요."
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
          placeholder="레시피 소개는 최소 10자 이상 400자 이내로 작성해주세요."
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
        
        <ImageUploader v-model="imageFile"/>
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
            :items="categories?.categoryCuisineList"
            item-title="type"
            label="종류"
          />
      </div>
      
      <div class="category-item">

        <SelectDropDown
          v-model="formValues.categories.purpose"
          :items="categories?.categoryPurposeList"
          item-title="type"
          label="목적"
        />
      </div>

      <div class="category-item">
        <SelectDropDown
          v-model="formValues.serving"
          :items="categories?.recipeServingList"
          item-title="label"
          label="인원수"
        />
      </div>

      <div class="category-item">
        <SelectDropDown
          v-model="formValues.duration"
          :items="categories?.recipeDurationList"
          item-title="label"
          label="요리시간"
        />
      </div>

      <div class="category-item">

        <SelectDropDown
          v-model="formValues.level"
          :items="categories?.recipeLevelList"
          item-title="label"
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

  .required {
    font-size: 0.8rem;
    color: #777;
    font-weight: 400;
  }
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

.label-title.required::before {
  position: absolute;
  content: '*';
  color: red;
  margin-right: 0.25rem;
  font-weight: bold;
  left: -0.7rem;
}

.label-title {
  position: relative;
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