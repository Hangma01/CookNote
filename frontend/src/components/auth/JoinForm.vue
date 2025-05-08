<script setup>
import { reactive, ref, watch } from 'vue';
import { userIdRule, passwordRule, nameRule, nicknameRule, emailRule, required } from '@/utils/rules';
import { checkUserId, checkNickname, checkEmail, userJoin } from '@/services/userService';
import { sendAuthCode, verifyAuthCode } from '@/services/mailService';
import { checkDuplicate, handleInputHangle } from '@/utils/commonFunction';
import { commonValues } from '@/utils/commonValues';
import { errorMessages } from '@/utils/errorMessages';
import { successMessage } from '@/utils/successMessage';
import { HttpStatusCode } from 'axios';
import { useRouter } from 'vue-router';
import { debounce } from 'lodash'

const router = useRouter();

// 유효성 겁사
const formRef = ref(null)                 // Form 유효성 검사
const ruleUserIdRef = ref(null)          // 아이디 유효성 검사
const ruleNicknameRef = ref(null)         // 닉네임 유효성 검사
const ruleEmailRef = ref(null)            // 이메일 유효성 검사

// 에러 메시지
const errorMsgUserIdDuplicate = ref('')       // 아이디 중복 시 에러 메시지
const errorMsgNicknameDuplicate = ref('')     // 닉네임 중복 시 에러 메시지
const errorMsgEmailDuplicate = ref('')        // 이메일 중복 시 에러 메시지
const errorMsgAuthCode = ref('')              // 메일 인증 코드 에러 메시지

// 성공 메시지
const isSuccessAuthCode = ref(false)        // 메일 인증 코드 성공 메시지

// etc...
const isAuthCodeRequest = ref(false)      // 메일 인증 요청 토글
const passwordVisible = ref(false)        // 비밀번호 필드 토글

// input-field
const formValues = reactive({             // Form input-field             
  userId: '',
  password: '',
  name: '',
  nickname: '',
  email: ''
})

const authCodeValue = ref('')             // 메일 인증 input-field


// 이름 20자 제한 (한글)
const handleNameInput = (e) => handleInputHangle(e, 20, (value) => formValues.name = value)

// 닉네임 15자 제한 (한글)
const handleNicknameInput = (e) => handleInputHangle(e, 15, (value) => formValues.nickname = value)


// 아이디 중복 체크
const checkUserIdDuplicate = async () => {
  await checkDuplicate({
    value: formValues.userId,               
    validatorRef: ruleUserIdRef,            
    errorMsgRef: errorMsgUserIdDuplicate,   
    apiCall: checkUserId              
  });
};


// 닉네임 중복 체크
const checkNicknameDuplicate = async () => {
  await checkDuplicate({
    value: formValues.nickname,             
    validatorRef: ruleNicknameRef,          
    errorMsgRef: errorMsgNicknameDuplicate, 
    apiCall: checkNickname
  });
};


// 이메일 중복 체크
const checkEmailDuplicate = async () => {
  await checkDuplicate({
    value: formValues.email,             
    validatorRef: ruleEmailRef,          
    errorMsgRef: errorMsgEmailDuplicate, 
    apiCall: checkEmail
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
      } catch (error) {
        alert(errorMessages.badRequest);
        router.replace('/login');
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
  } catch (error) {
    alert(errorMessages.badRequest);
    router.replace('/login');
  }
}


// 인증 코드 검증하기
const handleVerifyAuthCode = debounce(async () => {
  try {
    const res = await verifyAuthCode(formValues.email, authCodeValue.value);

    if (res.data.result) {  // 인증 성공
      isSuccessAuthCode.value = res.data.result;
      errorMsgAuthCode.value = '';
    } else {                // 인증 실패
      isSuccessAuthCode.value = res.data.result;
      errorMsgAuthCode.value = res.data.message;
    }
  } catch (error) {
    if (error.response.data.status === HttpStatusCode.Gone) { // 인증 번호 만료
      alert(res.response.data.message);
    } else {
      alert(errorMessages.badRequest);
      router.replace('/login');
    }
  }
}, commonValues.defaultDebounce)


// 회원 가입 하기
const handleSubmitJoin = debounce(async () => {
  const isFormVal = await formRef.value.validate(); // 전체 유효성 검사 확인

  // 유효성 검사 통과 및 인증 검증 성공 시 회원 가입
  if (isFormVal.valid && isSuccessAuthCode) {
    try {
      const res = await userJoin({ ...formValues })
      console.log(res)
      alert(successMessage.userJoin);
    } catch (error) {
      console.log(error)
      if(error.response.data.status === HttpStatusCode.BadRequest) {
        alert(error.response.data.message)
      }

      alert(errorMessages.badRequest);
    }
  } else {
    alert(errorMessages.badRequest)
  }

  router.replace('/login');
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
                v-model="formValues.password"
                :type="passwordVisible ? 'text' : 'password'"
                label="비밀번호"
                variant="solo"
                density="comfortable"
                hide-details="auto"
                maxlength="16"
                :rules="[passwordRule]"
                :append-inner-icon="passwordVisible ? 'mdi-eye-off' : 'mdi-eye'"
                @click:append-inner="passwordVisible = !passwordVisible"
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
                  class="auth-code-filed"
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

        <v-btn type="button" class="login-btn" @click="handleSendAuthCode" v-show="!isAuthCodeRequest">
            인증요청
        </v-btn>

        <v-btn type="submit" class="login-btn" v-show="isAuthCodeRequest" :disabled="!isSuccessAuthCode">
            인증 후 회원가입
        </v-btn>
    </v-form>
</template>


<style lang="scss" scoped>
.join-form {

    display: flex;
    flex-direction: column;
    gap: 1.5rem;
    height: 36rem;
    justify-content: space-between;

    .join-content {
        display: flex;
        flex-direction: column;
        gap: 1.8rem;

        .auth-code-wrap{
          display: flex;
          align-items: center;
          gap: 1.3rem;
          
          .auth-code-filed {
            height: 3rem;
          }

          .auth-mail-retry {
            display: inline;
            background-color: #888;
            color: white;
          }
        }
        


        .success-message {
          padding-left: 16px;
          padding-top: 6px;
          font-size: 12px;
          color: green;
        }
    }

    .login-btn {
      background-color: #c09370;
      color: white;
      font-size: 1.3rem;
      height: 3rem;
      
    }
}
</style>