<script setup>
import ImageUploader from '@/components/image/ImageUploader.vue';
import SelectDropDown from './SelectDropDown.vue';
import { commonInputHangle } from '@/utils/commonFunction';
import { reactive, ref, watch } from 'vue';
import { commonValues } from '@/utils/commonValues';


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
        formValues.serving = newVal.recipeCategories.recipeServingList?.find(s => s.name === newVal.serving) || null
        formValues.duration = newVal.recipeCategories.recipeDurationList?.find(d => d.name === newVal.duration) || null
        formValues.level = newVal.recipeCategories.recipeLevelList?.find(l => l.name === newVal.level) || null
        formValues.categories.cuisine = newVal.recipeCategories.categoryCuisineList?.find(
            c => c.id === newVal.categoryCuisineId
        ) || null

        formValues.categories.purpose = newVal.recipeCategories.categoryPurposeList?.find(
            p => p.id === newVal.categoryPurposeId
        ) || null

        imageFile.value = newVal.thumbnail || null
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
    title: formValues.title,
    description: formValues.description,
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
const handleGetVideoId = () => {
    formValues.videoId = getYoutubeVideoId()
}

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
            :items="categories?.categoryCuisineList || props.recipeCategories?.categoryCuisineList"
            item-title="type"
            label="종류"
          />
      </div>
      
      <div class="category-item">

        <SelectDropDown
          v-model="formValues.categories.purpose"
          :items="categories?.categoryPurposeList || props.recipeCategories?.categoryPurposeList"
          item-title="type"
          label="목적"
        />
      </div>

      <div class="category-item">
        <SelectDropDown
          v-model="formValues.serving"
          :items="categories?.recipeServingList || props.recipeCategories?.recipeServingList"
          item-title="label"
          label="인원수"
        />
      </div>

      <div class="category-item">
        <SelectDropDown
          v-model="formValues.duration"
          :items="categories?.recipeDurationList || props.recipeCategories?.recipeDurationList"
          item-title="label"
          label="요리시간"
        />
      </div>

      <div class="category-item">

        <SelectDropDown
          v-model="formValues.level"
          :items="categories?.recipeLevelList || props.recipeCategories?.recipeLevelList"
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