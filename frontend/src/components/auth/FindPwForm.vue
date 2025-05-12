<script setup>
import { reactive, ref, watch } from 'vue';
import { debounce } from 'lodash';
import { required, findUserIdRule, emailRule } from '@/utils/rules';
import { commonValues } from '@/utils/commonValues';
import { sendAuthCode } from '@/services/mailService';
import { errorMessages } from '@/utils/errorMessages';
import { successMessage } from '@/utils/successMessage';
import { HttpStatusCode } from 'axios';
import { useRouter } from 'vue-router';
import { commonVerifyAuthCode } from '@/utils/commonFunction';
import { userFindPwAuth } from '@/services/userService';

const router = useRouter();

// 유효성 겁사
const formRef = ref(null);      			// Form 유효성 검사

// 에러 메시지
const errorMsgAuthCode = ref('')            // 메일 인증 코드 에러 메시지

// 성공 메시지
const isSuccessAuthCode = ref(false)        // 메일 인증 코드 성공 메시지

// etc...
const isAuthCodeRequest = ref(false)      	// 메일 인증 요청 토글


// input-field
const formValues = reactive({             	// Form input-field 
  userId: '',
  email: '',
})

const authCodeValue = ref('')             	// 메일 인증 input-field
let pwResetToken = null;										// 비밀번호 변경 토큰


// 비밀번호 찾기 - 요청
const handleUserFindPwRequest = async () => {

  const isFormVal = await formRef.value.validate()

  // 유효성 검사 통과 시 메일 인증 코드 발송
  if (isFormVal.valid) { 
		try {
			const res = await userFindPwAuth({ ...formValues })
			pwResetToken = res.data.pwResetToken;
			isAuthCodeRequest.value = true;
		} catch (e) {
			if(e.response &&
					(e.response.data.status === HttpStatusCode.NotFound && e.response.data.message)
			){
				alert(e.response.data.message);
			} else {
				alert(errorMessages.badRequest);
			}
		}
	}  
}


// 메일 재전송
const handleSendAuthCodeRetry = async () => {

  try {
    const res = await sendAuthCode(formValues.email);
    isSuccessAuthCode.value = false;
    authCodeValue.value = '';
    alert(successMessage.authMailRetry);
  } catch (e) {
    alert(errorMessages.badRequest);
  }
}

// 인증 코드 검증하기
const handleVerifyAuthCode = debounce(async () => {
  await commonVerifyAuthCode(
    formValues.email,
    authCodeValue.value,
    (result, message) => {
      isSuccessAuthCode.value = result;
      errorMsgAuthCode.value = message;
    },
    router
  );
}, commonValues.defaultDebounce);



// 비밀번호 찾기 - 변경 페이지로 이동
const handleFindPw = debounce(async () => {
	
	const isFormVal = await formRef.value.validate()

	if (isFormVal.valid && isSuccessAuthCode) {
		router.replace({ name: 'pwReset', state: { pwResetToken: pwResetToken }});
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
				v-model="formValues.userId"
				type="text"
				label="아이디"
				variant="solo"
				density="comfortable"
				hide-details="auto"
				:rules="[findUserIdRule]"
			/>

			<v-text-field
				v-model="formValues.email"
				type="text"
				label="이메일"
				variant="solo"
				density="comfortable"
				hide-details="auto"
				:rules="[emailRule]"
			/>

			<div> 
				<div class="auth-code-wrap">
					<v-text-field
						v-model="authCodeValue"
						@input="handleVerifyAuthCode"
						type="text"
						label="인증번호"
						variant="solo"
						density="comfortable"
						hide-details="auto"
						maxlength="6"
						v-if="isAuthCodeRequest"
						:rules="[required]"
						:error-messages="errorMsgAuthCode"
						class="auth-code-field"
					/> 
					<v-btn type="button" class="auth-mail-retry" @click="handleSendAuthCodeRetry" v-if="isAuthCodeRequest">
						재전송
					</v-btn>
				</div>     

				<div v-if="isSuccessAuthCode" class="success-message">
					<span>
						인증에 성공했습니다.
					</span>
				</div>
			</div>
		</div>

		<v-btn type="button" class="find-pw-btn" @click="handleUserFindPwRequest" v-show="!isAuthCodeRequest">
			인증요청
		</v-btn>
		
		<v-btn type="submit" class="find-pw-btn" v-show="isAuthCodeRequest" :disabled="!isSuccessAuthCode">
			비밀번호 변경
		</v-btn>
	</v-form>
</template>


<style lang="scss" scoped>
.find-pw-form{
	display: flex;
	flex-direction: column;
	justify-content: space-between;
	gap: 1.5rem;
	height: 18rem;

	.find-pw-content {
		display: flex;
		flex-direction: column;
		gap: 1.8rem;

		.auth-code-wrap{
			display: flex;
			align-items: center;
			gap: 1.3rem;
		}
		.auth-code-field {
			height: 3rem;
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
}
</style>