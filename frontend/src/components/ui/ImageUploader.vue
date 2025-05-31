<script setup>
import { ref, watch } from 'vue';
import { s3Delete, s3Upload } from '@/services/awsService';
import { errorMessages } from '@/utils/messages/errorMessages';
import { commonValues } from '@/utils/commonValues';

const props = defineProps({
    modelValue: String,
    sizeClass: {
        type: String,
        default: 'recipeInfo',
    },
});

const emit = defineEmits(['update:modelValue']);

const imageUrl = ref(props.modelValue || null);

const fileInput = ref(null);

// 이미지 변경
const onFileChange = async (files) => {
    const allowedExtensions = ['jpg', 'jpeg', 'png', 'webp'];

    if (files && files.length > 0) {
        const file = files[0];

        if (file.type.startsWith('image/')) {
            const fileExtension = file.name.split('.').pop().toLowerCase();

            if (!allowedExtensions.includes(fileExtension)) {
                alert(errorMessages.IMAGE_FILE_EXTENSION_ERROR_MESSAGE);

                if (fileInput.value) {
                    fileInput.value.value = null;
                }
                return;
            }

            if (file.size > commonValues.MAX_IMAGE_SIZE) {
                alert(errorMessages.MAX_IMAGE_SIZE_ERROR_MESSAGE);

                if (fileInput.value) {
                    fileInput.value.value = null;
                }

                return;
            }

            // formData에 담아서 전달
            const formData = new FormData();
            formData.append('image', file);

            // s3 업로드
            try {
                const res = await s3Upload(formData);

                // 이미지 url 저장
                imageUrl.value = res.data;
            } catch (e) {
                if (e.response && e.response?.data?.message) {
                    alert(e.response.data.message);
                } else if (e.code === commonValues.NETWORK_ERROR) {
                    alert(errorMessages.MAX_IMAGE_SIZE_OR_NETWORK_ERROR_MESSAGE);
                } else {
                    alert(errorMessages.BADREQUEST);
                }

                if (fileInput.value) {
                    fileInput.value.value = null;
                }

                return;
            }

            emit('update:modelValue', imageUrl.value);
        } else {
            alert(errorMessages.NOT_IMAGE_FILE_ERROR_MESSAGE);
            if (fileInput.value) {
                fileInput.value.value = null;
            }
            return;
        }
    }
};
// 부모의 modelValue가 변경될 때마다 imageUrl을 갱신
watch(
    () => props.modelValue,
    (newVal) => {
        imageUrl.value = newVal; // 부모에서 값이 변경되면 자식의 imageUrl도 갱신
    }
);
</script>

<template>
    <div
        :class="['image-uploader', props.sizeClass]"
        @dragover.prevent
        @drop.prevent="onFileChange($event.dataTransfer.files)"
        @click="$refs.fileInput.click()"
    >
        <input type="file" ref="fileInput" accept="image/*" hidden @change="onFileChange($event.target.files)" />

        <div v-if="imageUrl" class="preview-img-wrap">
            <img :src="imageUrl" alt="preview" class="preview-img" />
        </div>

        <div v-else>
            <div :class="['placeholder', props.sizeClass]">
                <font-awesome-icon :icon="['fas', 'plus']" />
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

.image-uploader.recipe-seq {
    width: 100%;
    height: 10.4rem;
    border: 1px solid #e7e7e7;
    border-radius: 1rem;
    display: flex;
    justify-content: center;
    align-items: center;
    cursor: pointer;
    overflow: hidden;
    background-color: #eee;
}

.preview-img-wrap,
.preview-img {
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

.placeholder.recipe-seq {
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
