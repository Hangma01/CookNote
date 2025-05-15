<script setup>
import { reactive, ref, watch } from 'vue';
import { idRule, pwRule, nameRule, nicknameRule, emailRule, required } from '@/utils/rules';
import { existsId, existsNickname, existsEmail, userJoin } from '@/services/authService';
import { sendMailAuthCode } from '@/services/mailService';
import { commonCheckDuplicate, commonInputHangle, commonVerifyMailAuthCode } from '@/utils/commonFunction';
import { commonValues } from '@/utils/commonValues';
import { errorMessages } from '@/utils/messages/errorMessages';
import { successMessage } from '@/utils/messages/successMessage';
import { HttpStatusCode } from 'axios';
import { useRouter } from 'vue-router';
import { debounce } from 'lodash'
import LogoMini from '../logo/LogoMini.vue';


// 화면 전환
const router = useRouter();

// 유효성 겁사
const formRef = ref(null)                     // Form 유효성 검사
const ruleIdRef = ref(null)                 // 아이디 유효성 검사
const ruleNicknameRef = ref(null)             // 닉네임 유효성 검사
const ruleEmailRef = ref(null)                // 이메일 유효성 검사

// 에러 메시지
const errorMsgIdDuplicate = ref('')       // 아이디 중복 시 에러 메시지
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
  id: '',
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
const handleExistsId = async () => {
  await commonCheckDuplicate({
    value: formValues.id,               
    validatorRef: ruleIdRef,            
    errorMsgRef: errorMsgIdDuplicate,   
    apiCall: existsId,
    router : router  
  });
};


// 닉네임 중복 체크
const handleExistsNickname = async () => {
  await commonCheckDuplicate({
    value: formValues.nickname,             
    validatorRef: ruleNicknameRef,          
    errorMsgRef: errorMsgNicknameDuplicate, 
    apiCall: existsNickname,
    router : router
  });
};


// 이메일 중복 체크
const handleExistsEmail = async () => {
  await commonCheckDuplicate({
    value: formValues.email,             
    validatorRef: ruleEmailRef,          
    errorMsgRef: errorMsgEmailDuplicate, 
    apiCall: existsEmail,
    router : router
  });
}

// 메일 인증 요청하기
const handleSendMailAuthCode = async () => {

  const isFormVal = await formRef.value.validate()

  // 유효성 검사 통과 시 메일 인증 코드 발송
  if (
      isFormVal.valid &&
      !errorMsgIdDuplicate.value &&
      !errorMsgNicknameDuplicate.value &&
      !errorMsgEmailDuplicate.value
  ) { 
      try {
        const res = await sendMailAuthCode(formValues.email);
        isAuthCodeRequest.value = true;
      } catch (e) {
        console.log(e)
        alert(errorMessages.BADREQUEST);
        router.replace({ name: 'login'});
      }
    }  
}

// 메일 재전송
const handleSendMailAuthCodeRetry = async () => {

  try {
    const res = await sendMailAuthCode(formValues.email);
    isSuccessAuthCode.value = false;
    authCodeValue.value = '';
    alert(successMessage.authMailRetry);
  } catch (e) {
    alert(errorMessages.BADREQUEST);
    router.replace({ name: 'login'});
  }
}


// 인증 코드 검증하기
const handleVerifyMailAuthCode = debounce(async () => {
  await commonVerifyMailAuthCode(
    formValues.email,
    authCodeValue,
		isAuthCodeRequest,
    (result, message) => {
      isSuccessAuthCode.value = result;
      errorMsgAuthCode.value = message;
    },
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

      alert(errorMessages.BADREQUEST);
    }
  } else {
    alert(errorMessages.BADREQUEST)
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
          v-model="formValues.id"
          type="text"
          label="아이디"
          variant="solo"
          density="comfortable"
          hide-details="auto"
          maxlength="20"
          ref="ruleIdRef"
          :rules="[idRule]"
          :error-messages="errorMsgIdDuplicate"
          @blur="handleExistsId()"
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
          @blur="handleExistsNickname()"
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
          @blur="handleExistsEmail()"
      />        
      <div> 
        <div class="auth-code-wrap">
          <v-text-field
            v-model="authCodeValue"
            @input="handleVerifyMailAuthCode"
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
          <v-btn type="button" class="auth-mail-retry" @click="handleSendMailAuthCodeRetry" v-if="isAuthCodeRequest">
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

    <v-btn type="button" class="join-btn" @click="handleSendMailAuthCode" v-show="!isAuthCodeRequest">
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