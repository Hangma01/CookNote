<script setup>
import { reactive, ref, watch } from 'vue';
import { debounce } from 'lodash';
import { required, findNameRule, emailRule } from '@/utils/rules';
import { commonValues } from '@/utils/commonValues';
import { userFindIdAuth, userFindId } from '@/services/userService';
import { sendAuthCode } from '@/services/mailService';
import { errorMessages } from '@/utils/errorMessages';
import { successMessage } from '@/utils/successMessage';
import { HttpStatusCode } from 'axios';
import { useRouter } from 'vue-router';
import { commonVerifyAuthCode } from '@/utils/commonFunction';
import LogoMini from '../header/LogoMini.vue';

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
  name: '',
  email: '',
})

const authCodeValue = ref('')             	// 메일 인증 input-field



// 아이디 찾기 - 요청
const handleUserFindIdRequest = async () => {

  const isFormVal = await formRef.value.validate()

  // 유효성 검사 통과 시 메일 인증 코드 발송
  if (isFormVal.valid) { 
		try {
			const res = await userFindIdAuth({ ...formValues })
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



// 아이디 찾기
const handleFindId = debounce(async () => {
	
	const isFormVal = await formRef.value.validate()

	if (isFormVal.valid && isSuccessAuthCode) {
		try {
				const res = await userFindId(formValues.name, formValues.email)
				
				if(res.status === HttpStatusCode.Ok) {
						router.replace({ name: 'userFindIdResult', state: { userId: res.data.userId }});
				}
		} catch (e) {
			if(e.response &&
				(e.response.data.status === HttpStatusCode.BadRequest || e.response.data.status === HttpStatusCode.NotFound)
					&& e.response.data.message
			){
				alert(e.response.data.message);
			} else {
				alert(errorMessages.badRequest);
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
	<div class="title-wrap">
		<LogoMini />
		<h1 class="title">비밀번호 찾기</h1>
	</div>
	
	<v-form ref="formRef" class="find-id-form" @submit.prevent="handleFindId">
		<div class="find-id-content">
			<v-text-field
				v-model="formValues.name"
				type="text"
				label="이름"
				variant="solo"
				density="comfortable"
				hide-details="auto"
				:rules="[findNameRule]"
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

		<v-btn type="button" class="find-id-btn" @click="handleUserFindIdRequest" v-show="!isAuthCodeRequest">
			인증요청
		</v-btn>
		
		<v-btn type="submit" class="find-id-btn" v-show="isAuthCodeRequest" :disabled="!isSuccessAuthCode">
			인증 후 아이디 찾기
		</v-btn>
	</v-form>


    
</template>


<style lang="scss" scoped>
.title-wrap{
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

.find-id-form{
	display: flex;
	flex-direction: column;
	justify-content: space-between;
	gap: 1.5rem;
	height: 18rem;

	.find-id-content {
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

	.find-id-btn{
		background-color: #c09370;
		color: white;
		font-size: 1rem;
		height: 2.5rem;
	}
}
</style>