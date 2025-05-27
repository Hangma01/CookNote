<script setup>
import { commonValues } from '@/utils/commonValues';
import { ref, watch } from 'vue';




const selectedStatus = ref(commonValues.PRIVATE_TEXT)

const props = defineProps({ 
    originalRecipeData: { // 레시피 수정 시 오는 데이터
        type: Object    
    },
})

const validation = () => {
    if (!selectedStatus.value) return '설정을 선택해주세요.'

    return true;
}

const getData = () => {
    return {
        data: selectedStatus.value,
        selectedStatus: selectedStatus.value,
    }
}

// 부모가 사용할 수 있게 expose
defineExpose({
    validation,
    getData
})

watch(() => props.originalRecipeData, (newVal) => {
    selectedStatus.value = newVal.status
})
</script>

<template>
<h2 class="sub-title">설정</h2>
    <v-radio-group v-model="selectedStatus" inline >
        <v-radio
            class="me-10"
            color="primary"
            label="비공개"
            :value="commonValues.PRIVATE_TEXT"
        />
        <v-radio
            color="primary"
            label="공개"
            :value="commonValues.PUBLIC_TEXT"
        />
    </v-radio-group>
</template>

<style lang="scss" scoped>
.sub-title {
    font-size: 1.2rem;
    color: #c09370;
    padding-bottom: 1.5rem;
}

.recipe-set-wrap {
    display: flex;
  
    .seq-item-wrap {
        display: flex;
        gap: 2rem;
        align-items: center;
        margin-bottom: 2rem;

        .seq-item-box {
            display: flex;
            gap: 2em;

            .seq-item-title {
                margin-right: 1rem;
            }

            .seq-item-filed {
                display: flex;
                width: 33.6rem;

                .textarea-filed {
                    width: 100%;
                    border: 1px solid #e7e7e7;
                    border-radius: 0.5rem;
                    padding: 0.5rem;
                    resize: none;
                }

            }
        }

        .image-preview-wrap{
            width: 13rem;
            margin-left: 2rem;
        }

        .seq-item-remove {
            font-size: 1.4rem;
        }
    }

    .remove-button {
        background-color: transparent;
        border: none;
        font-weight: bold;
        color: #8c8c8c;
        cursor: pointer;
        font-size: 1.7rem;
    }
}

.add-btn-wrap {
    text-align: center;
    border: none;
    margin-bottom: 1rem;
    color: #3c3c3c;
    font-size: 1.1rem;

    .add-btn {
        padding: 1rem;
    }

    .add-icon {
        margin-right: 1rem;
    }
}
</style>