<script setup>
import { onMounted, reactive, ref, watch } from 'vue';
import { debounce } from 'lodash';
import { required, defaultNameRule, emailRule, authCodeRule } from '@/utils/rules';
import { commonValues } from '@/utils/commonValues';
import { userFindIdAuth, userFindId } from '@/services/authService';
import { deleteMailAuthCode, sendMailAuthCode } from '@/services/mailService';
import { errorMessages } from '@/utils/messages/errorMessages';
import { successMessage } from '@/utils/messages/successMessage';
import { HttpStatusCode } from 'axios';
import { useRouter } from 'vue-router';
import { commonVerifyMailAuthCode } from '@/utils/commonFunction';
import LogoMini from '../logo/LogoMini.vue';
import { useTimer } from '@/utils/useTimer';

// 화면 전환
const router = useRouter();

// 유효성 겁사
const formRef = ref(null); // Form 유효성 검사

// 에러 메시지
const errorMsgAuthCode = ref(''); // 메일 인증 코드 에러 메시지

// 성공 메시지
const isSuccessAuthCode = ref(false); // 메일 인증 코드 성공 메시지

// etc...
const isAuthCodeRequest = ref(false); // 메일 인증 요청 토글
const authCodeValue = ref(''); // 메일 인증 input-field
const isAtuhCodetimer = ref(false); // 메일 인증 시간 제한

// input-field
const formValues = reactive({
    // Form input-field
    name: '',
    email: '',
});

// 타이머를 3분으로 설정하고 타이머 종료시 동작
const { timer, startTimer, stopTimer, resetTimer, isTimerRunning } = useTimer(commonValues.MAIL_AUTH_TIMER, () => {
    isAtuhCodetimer.value = false;
    alert(errorMessages.MAIL_AUTH_TIME_OVER_MESSAGE);
});

// Snackbar
const snackbar = reactive({
    show: false,
    message: '',
    color: 'error', // or 'success'
});

const showSnackbar = (msg, type = 'error') => {
    snackbar.message = msg;
    snackbar.color = type;
    snackbar.show = true;
};

// 아이디 찾기 - 요청
const handleUserFindIdRequest = async () => {
    const isFormVal = await formRef.value.validate();

    // 이름 유효성 검사
    const nameValid = required(formValues.name) === true;
    if (!nameValid) {
        alert('이름을 입력해주세요.');
        return;
    }

    // 이메일 유효성 검사
    const emailValid = emailRule(formValues.email) === true;
    if (!emailValid) {
        alert('이메일을 올바르게 입력해주세요.');
        return;
    }

    // 유효성 검사 통과 시 메일 인증 코드 발송
    if (isFormVal.valid) {
        try {
            const res = await userFindIdAuth({ ...formValues });
            isAuthCodeRequest.value = true;
            isAtuhCodetimer.value = true;

            stopTimer();
            resetTimer();
            startTimer();
        } catch (e) {
            if (e.response && e.response.data.status === HttpStatusCode.NotFound && e.response.data.message) {
                alert(e.response.data.message);
            } else {
                alert(errorMessages.BADREQUEST);
            }
        }
    }
};

// 메일 재전송
const handleSendMailAuthCodeRetry = async () => {
    try {
        const res = await sendMailAuthCode(formValues.email);
        stopTimer();
        resetTimer();
        startTimer();
        isAtuhCodetimer.value = true;

        isSuccessAuthCode.value = false;
        authCodeValue.value = '';
        alert(successMessage.AUTH_MAIL_RETRY_SUCCESS_MESSAGE);
    } catch (e) {
        alert(errorMessages.BADREQUEST);
    }
};

// 아이디 찾기
const handleFindId = debounce(async () => {
    const isFormVal = await formRef.value.validate();

    // 인증번호 유효성 검사
    if (authCodeValue.value.length !== 6) {
        showSnackbar('인증번호를 올바르게 입력해주세요.');
        return;
    }

    if (isFormVal.valid) {
        await commonVerifyMailAuthCode(formValues.email, authCodeValue, isAuthCodeRequest, (result, message) => {
            isSuccessAuthCode.value = result;
            errorMsgAuthCode.value = message;
            if (message) {
                showSnackbar(message);
            }
        });

        if (isSuccessAuthCode.value) {
            try {
                const userFindIdres = await userFindId(formValues.name, formValues.email);

                if (userFindIdres.status === HttpStatusCode.Ok) {
                    try {
                        const deleteRes = await deleteMailAuthCode(formValues.email);
                        router.replace({ name: 'userFindIdResult', state: { id: userFindIdres.data.id } });
                    } catch (e) {
                        alert(errorMessages.BADREQUEST);
                    }
                }
            } catch (e) {
                if (
                    e.response &&
                    (e.response.data.status === HttpStatusCode.BadRequest || e.response.data.status === HttpStatusCode.NotFound) &&
                    e.response.data.message
                ) {
                    alert(e.response.data.message);
                } else {
                    alert(errorMessages.BADREQUEST);
                }

                isAuthCodeRequest.value = false;
                authCodeValue.value = '';
                errorMsgAuthCode.value = '';
                isSuccessAuthCode.value = false;
            }
        }
    }
}, commonValues.DEFALUT_DEBOUNCE);

watch(
    () => ({ ...formValues }),
    (newVal, oldVal) => {
        if (isAuthCodeRequest.value && Object.keys(newVal).some((key) => newVal[key] !== oldVal[key])) {
            isAuthCodeRequest.value = false;
            authCodeValue.value = '';
            errorMsgAuthCode.value = '';
            isSuccessAuthCode.value = false;
        }
    },
    { deep: true } // formValues 내부 값들을 추적 가능하게 함
);

onMounted(() => {
    resetTimer();
    stopTimer();
});
</script>

<template>
    <div class="title-wrap">
        <LogoMini />
        <h1 class="title">아이디 찾기</h1>
    </div>

    <v-form ref="formRef" class="find-id-form" @submit.prevent="handleFindId">
        <div class="find-id-content">
            <v-text-field
                v-model="formValues.name"
                type="text"
                label="이름"
                variant="outlined"
                density="compact"
                hide-details="auto"
                :rules="[defaultNameRule]"
                autocomplete="off"
            />

            <v-text-field
                v-model="formValues.email"
                type="text"
                label="이메일"
                variant="outlined"
                density="compact"
                hide-details="auto"
                :rules="[emailRule]"
                autocomplete="off"
            />

            <div>
                <div class="auth-code-wrap">
                    <v-text-field
                        v-model="authCodeValue"
                        type="text"
                        label="인증번호"
                        variant="outlined"
                        density="compact"
                        hide-details="auto"
                        maxlength="6"
                        v-if="isAuthCodeRequest"
                        :rules="[authCodeRule]"
                        :error-messages="errorMsgAuthCode"
                        autocomplete="off"
                    />

                    <v-btn type="button" class="auth-mail-retry" @click="handleSendMailAuthCodeRetry" v-if="isAuthCodeRequest"> 재전송 </v-btn>
                </div>

                <div class="timer" v-if="isAuthCodeRequest">
                    <span>인증시간 : {{ String(Math.floor(timer / 60)).padStart(1, '0') }}:{{ String(timer % 60).padStart(2, '0') }}</span>
                </div>
            </div>
        </div>

        <v-btn type="button" class="find-id-btn" @click="handleUserFindIdRequest" v-show="!isAuthCodeRequest"> 인증요청 </v-btn>

        <v-btn type="submit" class="find-id-btn" v-if="isAuthCodeRequest" :disabled="!isAtuhCodetimer"> 인증 후 아이디 찾기 </v-btn>
    </v-form>

    <v-snackbar v-model="snackbar.show" :color="snackbar.color" timeout="3000" location="bottom">
        {{ snackbar.message }}
    </v-snackbar>
</template>


<style lang="scss" scoped>
.title-wrap {
    display: flex;
    align-items: end;
    margin-top: 6rem;
    margin-bottom: 3rem;
    border-bottom: 1px solid #eee;
    padding-bottom: 1rem;
    gap: 4rem;

    .title {
        font-size: 1.5rem;
        color: #2c2c30;
        font-weight: 600;
    }
}

.find-id-form {
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    gap: 1.5rem;
    height: 20rem;

    .find-id-content {
        display: flex;
        flex-direction: column;
        gap: 1.8rem;

        .auth-code-wrap {
            display: flex;
            gap: 1.3rem;
        }

        .auth-mail-retry {
            display: inline;
            background-color: #888;
            color: white;
        }
    }

    .find-id-btn {
        background-color: #c09370;
        color: white;
        font-size: 1rem;
        height: 2.5rem;
    }

    .timer {
        font-size: 0.8rem;
        color: #ff3f3f;
        margin-top: 0.5rem;
        margin-left: 1rem;
    }
}
</style>