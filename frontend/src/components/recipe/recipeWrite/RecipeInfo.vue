<script setup>
import ImageUploader from '@/components/image/ImageUploader.vue';
import SelectDropDown from './SelectDropDown.vue';
import { recipeInfoForm } from '@/composables/recipes/recipeInfoForm';


const items = [
  { state: '한식', no: 1},
  { state: '일식', no: 2 },
  { state: '양식', no: 3 },
  { state: '중식', no: 4 },
  { state: '퓨전', no: 5 },
  { state: '디저트', no: 6 },
]

// 요리 정보 form 가져오기
const { 
  title, 
  description, 
  videoId, 
  serving, 
  duration, 
  level, 
  categories, 
  imageFile, 
  url, 
  getData, 
  handleGetVideoId, 
  validation 
} = recipeInfoForm();


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
          v-model="description"
          placeholder="예) 된장찌개 끓이기"
          rows="4"
          class="textarea-filed"
        ></textarea>
      </div>

      <div class="input-filed-wrap">
        <div class="label-title">
          <p>레시피 동영상 URL</p>
          <p class="label-title-sub">ⓘ 레시피 동영상 등록은 Youtube만 가능합니다.</p>
        </div>

        <input
          v-model="url"
          type="text"
          placeholder="https://"
          class="input-filed"
          @input="handleGetVideoId"
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

  <div>
    <div class="label-title">
      <p>카테고리</p>
    </div>

    <div class="category-wrap">
      <div class="category-item">
          <SelectDropDown
            v-model="categories.cuisine"
            :items="items"
            item-title="state"
            label="종류"
          />
      </div>
      
      <div class="category-item">

        <SelectDropDown
          v-model="categories.purpose"
          :items="items"
          item-title="state"
          label="목적"
        />
      </div>

      <div class="category-item">
        <SelectDropDown
          v-model="serving"
          :items="items"
          item-title="state"
          label="인원수"
        />
      </div>

      <div class="category-item">
        <SelectDropDown
          v-model="duration"
          :items="items"
          item-title="state"
          label="요리시간"
        />
      </div>

      <div class="category-item">

        <SelectDropDown
          v-model="level"
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