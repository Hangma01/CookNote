<script setup>
import { ref, onBeforeUnmount } from 'vue';

const props = defineProps({
  modelValue: File,
  sizeClass: {
    type: String,
    default: 'recipeInfo'
  }
});

const emit = defineEmits(['update:modelValue']);

const imagePreview = ref(null);
const imageFile = ref(null);

const onFileChange = (files) => {
  if (files && files.length > 0) {
    const file = files[0]
    if (file.type.startsWith('image/')) {
      imageFile.value = file
      
      if (imagePreview.value) {
        URL.revokeObjectURL(imagePreview.value);
      }

      // 미리보기 임시 주소 생성
      imagePreview.value = URL.createObjectURL(file)
    }
  }
}

// 컴포넌트 사라질 때 메모리 정리
onBeforeUnmount(() => {
  if (imagePreview.value) {
    URL.revokeObjectURL(imagePreview.value);
  }
});
</script>

<template>
  <div
    :class="['image-uploader', props.sizeClass]"
    @dragover.prevent
    @drop.prevent="onFileChange($event.dataTransfer.files)"
    @click="$refs.fileInput.click()"
  >
    <input
      type="file"
      ref="fileInput"
      accept="image/*"
      hidden
      @change="onFileChange($event.target.files)"
    />
    <div v-if="imagePreview" class="preview-img-wrap">
      <img :src="imagePreview" alt="preview" class="preview-img"/>
    </div>
    <div v-else>
      <div :class="['placeholder', props.sizeClass]">
        <font-awesome-icon :icon="['fas', 'plus']"/>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped >
.image-uploader.recipeInfo {
  width: 100%;
  height: 12rem;
  border: 1px solid #e7e7e7;
  border-radius: 1rem;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  overflow: hidden;
  background-color: #eee;
}


.image-uploader.recipe-seq{
  width: 100%;
  height: 8rem;
  border: 1px solid #e7e7e7;
  border-radius: 1rem;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  overflow: hidden;
  background-color: #eee;
}

.preview-img-wrap, .preview-img {
  width: 100%;
  height: 100%;
}

.placeholder.recipeInfo {
  color: #aaa;
  text-align: center;
  font-size: 3rem;
  border: 4px solid #aaa;
  border-radius: 100%;
  width: 5rem;
  height: 5rem;
  display: flex;
  justify-content: center;
  align-items: center;
}

.placeholder.recipe-seq{
  color: #aaa;
  text-align: center;
  font-size: 1.5rem;
  border: 4px solid #aaa;
  border-radius: 100%;
  width: 3rem;
  height: 3rem;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
