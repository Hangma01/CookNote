<script setup>
import { ReportType } from '@/constans/reportType';
import { userReport, userReportDuplicationCheck } from '@/services/userService';
import { commonValues } from '@/utils/commonValues';
import { errorMessages } from '@/utils/messages/errorMessages';
import { successMessage } from '@/utils/messages/successMessage';
import { debounce } from 'lodash';
import { onMounted, ref, watch } from 'vue';

const props = defineProps({
    categoryReportReason: Array,
    reportType: String,
    targetId: String,
    reportedId: String,
});

// 부모 이벤트
const emit = defineEmits(['closeReportModal']);

const dialogVisible = ref(true);
const selectedReasonId = ref(null);

const recipeId = ref(null);
const commentId = ref(null);

// 닫기
const close = () => {
    emit('closeReportModal');
    dialogVisible.value = false;
    selectedReasonId.value = null;
};

// 신고하기
const submitReport = debounce(async () => {
    try {
        await userReport(props.reportType, recipeId.value, commentId.value, selectedReasonId.value, props.reportedId);

        alert(successMessage.REPORT_SUCCESS_MESSAGE);
    } catch (e) {
        if (e.response && e.response?.data?.message) {
            alert(e.response.data.message);
        } else {
            alert(errorMessages.BADREQUEST);
            window.location.reload();
        }
    }
    close();
}, commonValues.FIVE_MS_DEBOUNCE);

watch(dialogVisible, (newVal) => {
    if (newVal) {
        selectedReasonId.value = null;
    }
});

onMounted(async () => {
    try {
        if (ReportType.RECIPE === props.reportType) {
            recipeId.value = props.targetId;
        } else if (ReportType.COMMENT === props.reportType) {
            commentId.value = props.targetId;
        }
        await userReportDuplicationCheck(props.reportType, recipeId.value, commentId.value);
    } catch (e) {
        close();
        if (e.response && e.response?.data?.message) {
            alert(e.response.data.message);
        } else {
            alert(errorMessages.BADREQUEST);
        }
    }
});
</script>


<template>
    <div class="pa-4 text-center">
        <v-dialog width="auto" scrollable v-model="dialogVisible" min-width="30rem" min-height="35rem" persistent>
            <v-card w-20 title="신고하기">
                <v-divider></v-divider>

                <v-card-text class="px-4" style="height: 300px">
                    <v-radio-group v-model="selectedReasonId">
                        <ul>
                            <li v-for="item in props?.categoryReportReason" :key="item.id" class="radio-box">
                                <v-radio :label="item.type" :value="item.id" class="custom-radio" />
                            </li>
                        </ul>
                    </v-radio-group>
                </v-card-text>

                <v-divider></v-divider>

                <v-card-actions>
                    <v-btn text="취소" @click="close" variant="outlined"></v-btn>

                    <v-btn color="surface-variant" text="신고" variant="flat" @click="submitReport"></v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>
    </div>
</template>


<style lang="scss" scoped>
.radio-box {
    padding-bottom: 0.5rem;
}
</style>