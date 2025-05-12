<script setup>
import { reactive, ref, watch } from 'vue';
import { userIdRule, pwRule, nameRule, nicknameRule, emailRule, required } from '@/utils/rules';
import { checkUserId, checkNickname, checkEmail, userJoin } from '@/services/userService';
import { sendAuthCode } from '@/services/mailService';
import { commonCheckDuplicate, commonInputHangle, commonVerifyAuthCode } from '@/utils/commonFunction';
import { commonValues } from '@/utils/commonValues';
import { errorMessages } from '@/utils/errorMessages';
import { successMessage } from '@/utils/successMessage';
import { HttpStatusCode } from 'axios';
import { useRouter } from 'vue-router';
import { debounce } from 'lodash'
import LogoMini from '../header/LogoMini.vue';


// 화면 전환
const router = useRouter();

// 유효성 겁사
const formRef = ref(null)                     // Form 유효성 검사
const ruleUserIdRef = ref(null)               // 아이디 유효성 검사
const ruleNicknameRef = ref(null)             // 닉네임 유효성 검사
const ruleEmailRef = ref(null)                // 이메일 유효성 검사

// 에러 메시지
const errorMsgUserIdDuplicate = ref('')       // 아이디 중복 시 에러 메시지
const errorMsgNicknameDuplicate = ref('')     // 닉네임 중복 시 에러 메시지
const errorMsgEmailDuplicate = ref('')        // 이메일 중복 시 에러 메시지
const errorMsgAuthCode = ref('')              // 메일 인증 코드 에러 메시지

// 성공 메시지
const isSuccessAuthCode = ref(false)          // 메일 인증 코드 성공 메시지

// etc...
const isAuthCodeRequest = ref(false)          // 메일 인증 요청 토글
const pwVisible = ref(false)            // 비밀번호 필드 토글


// input-field
const formValues = reactive({                 // Form input-field             
  userId: '',
  pw: '',
  name: '',
  nickname: '',
  email: ''
})

const authCodeValue = ref('')                 // 메일 인증 input-field


// 이름 20자 제한 (한글)
const handleNameInput = (e) => commonInputHangle(e, 20, (value) => formValues.name = value)

// 닉네임 15자 제한 (한글)
const handleNicknameInput = (e) => commonInputHangle(e, 15, (value) => formValues.nickname = value)


// 아이디 중복 체크
const checkUserIdDuplicate = async () => {
  await commonCheckDuplicate({
    value: formValues.userId,               
    validatorRef: ruleUserIdRef,            
    errorMsgRef: errorMsgUserIdDuplicate,   
    apiCall: checkUserId,
    router : router  
  });
};


// 닉네임 중복 체크
const checkNicknameDuplicate = async () => {
  await commonCheckDuplicate({
    value: formValues.nickname,             
    validatorRef: ruleNicknameRef,          
    errorMsgRef: errorMsgNicknameDuplicate, 
    apiCall: checkNickname,
    router : router
  });
};


// 이메일 중복 체크
const checkEmailDuplicate = async () => {
  await commonCheckDuplicate({
    value: formValues.email,             
    validatorRef: ruleEmailRef,          
    errorMsgRef: errorMsgEmailDuplicate, 
    apiCall: checkEmail,
    router : router
  });
}

// 메일 인증 요청하기
const handleSendAuthCode = async () => {

  const isFormVal = await formRef.value.validate()

  // 유효성 검사 통과 시 메일 인증 코드 발송
  if (
      isFormVal.valid &&
      !errorMsgUserIdDuplicate.value &&
      !errorMsgNicknameDuplicate.value &&
      !errorMsgEmailDuplicate.value
  ) { 
      try {
        const res = await sendAuthCode(formValues.email);
        isAuthCodeRequest.value = true;
      } catch (e) {
        alert(errorMessages.badRequest);
        router.replace({ name: 'login'});
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
    router.replace({ name: 'login'});
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


// 회원 가입 하기
const handleSubmitJoin = debounce(async () => {
  const isFormVal = await formRef.value.validate(); // 전체 유효성 검사 확인

  // 유효성 검사 통과 및 인증 검증 성공 시 회원 가입
  if (isFormVal.valid && isSuccessAuthCode) {
    try {
      const res = await userJoin({ ...formValues })
      
      alert(successMessage.userJoin);
    } catch (e) {
      
      if(e.response && e.response.data.status === HttpStatusCode.BadRequest) {
        alert(e.response.data.message)
      }

      alert(errorMessages.badRequest);
    }
  } else {
    alert(errorMessages.badRequest)
  }

  router.replace({ name: 'login'});
}, commonValues.defaultDebounce)

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
    <h1 class="title">회원가입</h1>
  </div>

  <v-form ref="formRef" class="join-form" @submit.prevent="handleSubmitJoin">
    <div class="join-content">
      <v-text-field
          v-model="formValues.userId"
          type="text"
          label="아이디"
          variant="solo"
          density="comfortable"
          hide-details="auto"
          maxlength="20"
          ref="ruleUserIdRef"
          :rules="[userIdRule]"
          :error-messages="errorMsgUserIdDuplicate"
          @blur="checkUserIdDuplicate()"
      />

      <v-text-field
          v-model="formValues.pw"
          :type="pwVisible ? 'text' : 'password'"
          label="비밀번호"
          variant="solo"
          density="comfortable"
          hide-details="auto"
          maxlength="16"
          :rules="[pwRule]"
          :append-inner-icon="pwVisible ? 'mdi-eye-off' : 'mdi-eye'"
          @click:append-inner="pwVisible = !pwVisible"
      />

      <v-text-field
          v-model="formValues.name"
          @input="handleNameInput"
          type="text"
          label="이름"
          variant="solo"
          density="comfortable"
          hide-details="auto"
          :rules="[nameRule]"
      />

      <v-text-field
          v-model="formValues.nickname"
          @input="handleNicknameInput"
          type="text"
          label="닉네임"
          variant="solo"
          density="comfortable"
          hide-details="auto"
          ref="ruleNicknameRef"
          :rules="[nicknameRule]"
          :error-messages="errorMsgNicknameDuplicate"
          @blur="checkNicknameDuplicate()"
      />

      <v-text-field
          v-model="formValues.email"
          type="email"
          label="이메일"
          variant="solo"
          density="comfortable"
          hide-details="auto"
          maxlength="100"
          :rules="[emailRule]"
          :error-messages="errorMsgEmailDuplicate"
          @blur="checkEmailDuplicate()"
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

    <v-btn type="button" class="join-btn" @click="handleSendAuthCode" v-show="!isAuthCodeRequest">
        인증요청
    </v-btn>

    <v-btn type="submit" class="join-btn" v-show="isAuthCodeRequest" :disabled="!isSuccessAuthCode">
        인증 후 회원가입
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

.join-form {
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    gap: 1.5rem;
    height: 36rem;

    .join-content {
        display: flex;
        flex-direction: column;
        gap: 1.8rem;


        .auth-code-wrap{
          display: flex;
          align-items: center;
          gap: 1.3rem;
          
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
    }

    .join-btn{
      background-color: #c09370;
      color: white;
      font-size: 1rem;
      height: 2.5rem;
    }
}
</style>