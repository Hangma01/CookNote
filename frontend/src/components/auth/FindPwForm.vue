<script setup>
import { reactive, ref, watch } from 'vue';
import { debounce } from 'lodash';
import { required, defaultIdRule, emailRule, authCodeRule } from '@/utils/rules';
import { commonValues } from '@/utils/commonValues';
import { deleteMailAuthCode, sendMailAuthCode } from '@/services/mailService';
import { errorMessages } from '@/utils/messages/errorMessages';
import { successMessage } from '@/utils/messages/successMessage';
import { HttpStatusCode } from 'axios';
import { useRouter } from 'vue-router';
import { commonVerifyMailAuthCode } from '@/utils/commonFunction';
import { userFindPwAuth } from '@/services/authService';
import { usePwResetTokenStore } from '@/stores/pwResetToken';
import { useTimer } from '@/utils/useTimer';

// 화면 전환
const router = useRouter();

// 비밀번호 재설정 스토어
const pwResetToken = usePwResetTokenStore();

// 유효성 겁사
const formRef = ref(null);      						// Form 유효성 검사

// 에러 메시지
const errorMsgAuthCode = ref('')            // 메일 인증 코드 에러 메시지

// 성공 메시지
const isSuccessAuthCode = ref(false)        // 메일 인증 코드 성공 메시지

// etc...
const isAuthCodeRequest = ref(false)      	// 메일 인증 요청 토글
const isAtuhCodeimer = ref(false)						// 메일 인증 시간 제한

// 타이머를 2분으로 설정하고 타이머 종료시 동작
const { timer, startTimer, stopTimer, resetTimer, isTimerRunning } = useTimer(180, () => {
  isAtuhCodeimer.value = false
});

// input-field
const formValues = reactive({             	// Form input-field 
  id: '',
  email: '',
})

const authCodeValue = ref('')             	// 메일 인증 input-field


// 비밀번호 찾기 - 요청
const handleUserFindPwRequest = async () => {

  const isFormVal = await formRef.value.validate()

  // 유효성 검사 통과 시 메일 인증 코드 발송
  if (isFormVal.valid) { 
		try {
			resetTimer();   
  		startTimer();
			isAtuhCodeimer.value = true

			const res = await userFindPwAuth({ ...formValues })
			isAuthCodeRequest.value = true;
			pwResetToken.setPwResetToken(res.data.pwResetToken)
		} catch (e) {
			if(e.response &&
				(e.response.data.status === HttpStatusCode.NotFound && e.response.data.message)
			){
				alert(e.response.data.message);
			} else {
				alert(errorMessages.BADREQUEST);
			}
		}
	}  
}


// 메일 재전송
const handleSendMailAuthCodeRetry = async () => {

  try {
    const res = await sendMailAuthCode(formValues.email);

		resetTimer();   
  	startTimer();
		isAtuhCodeimer.value = true

    isSuccessAuthCode.value = false;
    authCodeValue.value = '';
    alert(successMessage.authMailRetry);
  } catch (e) {
    alert(errorMessages.BADREQUEST);
  }
}


// 비밀번호 찾기 - 변경 페이지로 이동
const handleFindPw = debounce(async () => {

	const isFormVal = await formRef.value.validate()
	if(!isAtuhCodeimer.value){
		alert("메일 인증 시간이 초과했습니다. 재전송을 해주세요.")
	} else if (isFormVal.valid) {
		await commonVerifyMailAuthCode(
			formValues.email,
			authCodeValue,
			isAuthCodeRequest,
			(result, message) => {
				isSuccessAuthCode.value = result;
				errorMsgAuthCode.value = message;
			},
		);

		if (isSuccessAuthCode.value) {
			try {
				const res = await deleteMailAuthCode(formValues.email)
				router.replace({ name: 'pwReset' });
			}catch (e) {
				alert(errorMessages.BADREQUEST);
			}
		}
	}

}, commonValues.defaultDebounce);



watch (
  () => ({ ...formValues }),
  (newVal, oldVal) => {
    if (
        isAuthCodeRequest.value &&
        Object.keys(newVal).some(key => newVal[key] !== oldVal[key])
    ) {
      isAuthCodeRequest.value = false;
      authCodeValue.value = '';
      errorMsgAuthCode.value = '';
      isSuccessAuthCode.value = false;
    }
  },
  { deep: true }  // formValues 내부 값들을 추적 가능하게 함
)
</script>

<template>
	<v-form ref="formRef" class="find-pw-form" @submit.prevent="handleFindPw">
		<div class="find-pw-content">
			<v-text-field
				v-model="formValues.id"
				type="text"
				label="아이디"
				variant="outlined"
				density="compact"
				hide-details="auto"
				:rules="[defaultIdRule]"
			/>

			<v-text-field
				v-model="formValues.email"
				type="text"
				label="이메일"
				variant="outlined"
				density="compact"
				hide-details="auto"
				:rules="[emailRule]"
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
					/> 

					<v-btn type="button" class="auth-mail-retry" @click="handleSendMailAuthCodeRetry" v-if="isAuthCodeRequest" >
						재전송
					</v-btn>
				</div>     
<!-- 
				<div v-if="isSuccessAuthCode" class="success-message">
					<span>
						인증에 성공했습니다.
					</span>
				</div> -->

				<div class="timer" v-if="isAuthCodeRequest">
					<span>{{ String(Math.floor(timer / 60)).padStart(1, '0') }}:{{ String(timer % 60).padStart(2, '0') }}</span>
				</div>
			</div>
		</div>

		<v-btn type="button" class="find-pw-btn" @click="handleUserFindPwRequest" v-show="!isAuthCodeRequest">
			인증요청
		</v-btn>
		
		<v-btn type="submit" class="find-pw-btn" v-if="isAuthCodeRequest" >
			비밀번호 변경 하기
		</v-btn>
	</v-form>
</template>


<style lang="scss" scoped>
.find-pw-form{
	display: flex;
	flex-direction: column;
	justify-content: space-between;
	gap: 1.5rem;
	height: 19rem;

	.find-pw-content {
		display: flex;
		flex-direction: column;
		gap: 1.8rem;

		.auth-code-wrap{
			display: flex;
			gap: 1.3rem;
		}

		.auth-mail-retry {
			display: inline;
			background-color: #888;
			color: white;
		}
	}

	.success-message {
		padding-left: 1rem;
		padding-top: 0.3rem;
		font-size: 0.75rem;
		color: green;
	}

	.find-pw-btn{
		background-color: #c09370;
		color: white;
		font-size: 1rem;
		height: 2.5rem;
	}

	.timer{
		font-size: 0.8rem;
		color: #FF3F3F;
		margin-top: 0.5rem;
		margin-left: 1rem;
	}
}
</style>