<script setup>
import { userPwEdit } from '@/services/userService';
import { commonValues } from '@/utils/commonValues';
import { errorMessages } from '@/utils/messages/errorMessages';
import { successMessage } from '@/utils/messages/successMessage';
import { newPwConfirmRule, pwRule, required } from '@/utils/rules';
import { debounce } from 'lodash';
import { reactive, ref, watch } from 'vue';

// 유효성 겁사
const formRef = ref(null); // Form 유효성 검사

// etc...
const newPwVisible = ref(false); // 새 비밀번호 필드 토글
const newPwConfirmVisible = ref(false); // 새 비밀번호 확인 필드 토글

// input-field
const formValues = reactive({
    currentPw: '',
    newPw: '',
    newPwConfirm: '',
});

// 비밀번호 변경 요청
const handleEditPw = debounce(async () => {
    if (formValues.currentPw === formValues.newPw) {
        alert('현재 비밀번호와 새로운 비밀번호를 똑같이 입력했습니다. 다시 입력해주세요.');
        return;
    }
    const isFormVal = await formRef.value.validate();

    if (isFormVal.valid) {
        try {
            const res = await userPwEdit({
                ...formValues,
            });

            alert(successMessage.USER_EDIT_PW_SUCCESS_MESSAGE);
            window.location.reload();
        } catch (e) {
            if (e.response && e.response?.data?.message) {
                alert(e.response.data.message);
            } else {
                alert(errorMessages.BADREQUEST);
                window.location.reload();
            }
        }
    }
}, commonValues.DEFALUT_DEBOUNCE);

watch(
    () => formValues.newPw,
    () => {
        if (formValues.newPwConfirm !== '' && formRef.value) {
            formRef.value.validate();
        }
    }
);
</script>

<template>
    <div class="profile-pw-edit-container">
        <v-form ref="formRef">
            <div class="pw-edit-form">
                <div class="form-group">
                    <label>현재 비밀번호</label>
                    <v-text-field
                        v-model="formValues.currentPw"
                        :type="newPwVisible ? 'text' : 'password'"
                        placeholder="현재 비밀번호"
                        variant="outlined"
                        density="compact"
                        hide-details="auto"
                        maxlength="16"
                        :rules="[required]"
                        :append-inner-icon="newPwVisible ? 'mdi-eye-off' : 'mdi-eye'"
                        @click:append-inner="newPwVisible = !newPwVisible"
                        autocomplete="off"
                    />
                </div>

                <div class="form-group">
                    <label>새 비밀번호</label>
                    <v-text-field
                        v-model="formValues.newPw"
                        :type="newPwVisible ? 'text' : 'password'"
                        placeholder="새 비밀번호"
                        variant="outlined"
                        density="compact"
                        hide-details="auto"
                        maxlength="16"
                        :rules="[pwRule]"
                        :append-inner-icon="newPwVisible ? 'mdi-eye-off' : 'mdi-eye'"
                        @click:append-inner="newPwVisible = !newPwVisible"
                        autocomplete="off"
                    />
                </div>

                <div class="form-group">
                    <label>새 비밀번호 확인</label>
                    <v-text-field
                        v-model="formValues.newPwConfirm"
                        :type="newPwConfirmVisible ? 'text' : 'password'"
                        placeholder="새 비밀번호 확인"
                        variant="outlined"
                        density="compact"
                        hide-details="auto"
                        :rules="[newPwConfirmRule(() => formValues.newPw)]"
                        :append-inner-icon="newPwConfirmVisible ? 'mdi-eye-off' : 'mdi-eye'"
                        @click:append-inner="newPwConfirmVisible = !newPwConfirmVisible"
                        autocomplete="off"
                    />
                </div>
            </div>
        </v-form>

        <div class="button-wrap">
            <button class="save-btn" @click="handleEditPw">수정</button>
        </div>
    </div>
</template>



<style lang="scss" scoped>
.profile-pw-edit-container {
    margin-bottom: 8rem;

    .pw-edit-form {
        margin-top: 2rem;
        height: 24rem;
        border: 1px solid rgb(200, 200, 200);
        border-radius: 0.5rem;
        display: flex;
        padding: 1rem 2rem;
        flex-direction: column;
        gap: 1.5rem;

        .form-group {
            display: flex;
            flex-direction: column;

            label {
                font-weight: bold;
                margin-bottom: 0.4rem;
            }

            .nickname {
                display: flex;
                gap: 1rem;
            }

            .editable-field {
                padding: 0.6rem 1rem;
                border-radius: 0.3rem;
                border: 1px solid #ccc;
                font-size: 1rem;
            }
        }
    }

    .button-wrap {
        display: flex;
        justify-content: flex-end;
        gap: 1rem;
        margin-top: 1rem;

        .save-btn {
            padding: 0.6rem 1.2rem;
            font-size: 1rem;
            border-radius: 0.3rem;
            cursor: pointer;
            border: none;
            background-color: #007bff;
            color: white;
        }
    }
}
</style>