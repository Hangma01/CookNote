<script setup>
import { reactive, ref, watch } from 'vue';
import { loginIdRule, userPwRule, userUsernameRule, userNicknameRule, userEmailRule, required } from '@/utils/rules';
import { checkLoginId, checkNickname, checkEmail } from '@/services/userService';
import { checkDuplicate, handleInputHangle } from '@/utils/commonFunction';


// 유효성 겁사
const formRef = ref(null)                 // Form 유효성 검사
const ruleIdRef = ref(null)               // 아이디 유효성 검사
const ruleNicknameRef = ref(null)         // 닉네임 유효성 검사
const ruleEmailRef = ref(null)            // 이메일 유효성 검사

// 에러 메시지
const errorMsgIdDuplicate = ref('')       // 아이디 중복 시 에러 메시지
const errorMsgNicknameDuplicate = ref('') // 닉네임 중복 시 에러 메시지
const errorMsgEmailDuplicate = ref('')    // 이메일 중복 시 에러 메시지
const errorMsgAuthCode = ref('')          // 메일 인증 코드 에러 메시지

// 성공 메시지
const successMsgAuthCode = ref('fe')        // 메일 인증 코드 성공 메시지

// etc...
const isAuthCodeRequest = ref(false)      // 메일 인증 요청 토글
const pwVisible = ref(false)              // 비밀번호 필드 토글

// input-field
const formValues = reactive({             // Form input-field             
  username: '',
  id: '',
  pw: '',
  email: '',
  nickname: ''
})

const authCodeValue = ref('')             // 메일 인증 input-field


// 이름 20자 제한 (한글)
const handleUsernameInput = (e) => handleInputHangle(e, 20, (value) => formValues.username = value)

// 닉네임 15자 제한 (한글)
const handleNicknameInput = (e) => handleInputHangle(e, 15, (value) => formValues.nickname = value)


// 아이디 중복 체크
const checkIdDuplicate = async () => {
  await checkDuplicate({
    value: formValues.id,               
    validatorRef: ruleIdRef,            
    errorMsgRef: errorMsgIdDuplicate,   
    apiCall: checkLoginId              
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
const handleAuthMailRequest = async () => {

  const isFormValid = await formRef.value.validate()

  // 유효성 검사 통과 시 메일 인증 코드 발송
  if (
      isFormValid.valid &&
      !errorMsgIdDuplicate.value &&
      !errorMsgNicknameDuplicate.value &&
      !errorMsgEmailDuplicate.value
  ) { 
    // 인증 요청 True
    isAuthCodeRequest.value = true
  }  
}


// 인증 코드 검증하기


// 회원 가입 하기
const handleSubmitJoin = async () => {
  const isFormValid = await formRef.value.validate(); // 전체 유효성 검사 확인

  // 유효성 검사 통과 시 회원 가입
  if (
    isFormValid.valid &&
    !errorMsgAuthCode.value
  ) { 
    console.log('성공')
  } 
}

watch (
  () => ({ ...formValues }),
  (newVal, oldVal) => {
    if (
        isAuthCodeRequest.value &&
        Object.keys(newVal).some(key => newVal[key] !== oldVal[key])
    ) {
      isAuthCodeRequest.value = false;
      authCodeValue.value = ''
      errorMsgAuthCode.value = ''
      successMsgAuthCode.value = ''
    }
  },
  { deep: true }  // formValues 내부 값들을 추적 가능하게 함
)

</script>

<template>
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
                :rules="[loginIdRule]"
                :error-messages="errorMsgIdDuplicate"
                @blur="checkIdDuplicate()"
            />

            <v-text-field
                v-model="formValues.pw"
                :type="pwVisible ? 'text' : 'password'"
                label="비밀번호"
                variant="solo"
                density="comfortable"
                hide-details="auto"
                maxlength="16"
                :rules="[userPwRule]"
                :append-inner-icon="pwVisible ? 'mdi-eye-off' : 'mdi-eye'"
                @click:append-inner="pwVisible = !pwVisible"
            />

            <v-text-field
                v-model="formValues.username"
                @input="handleUsernameInput"
                type="text"
                label="이름"
                variant="solo"
                density="comfortable"
                hide-details="auto"
                :rules="[userUsernameRule]"
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
                :rules="[userNicknameRule]"
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
                :rules="[userEmailRule]"
                :error-messages="errorMsgEmailDuplicate"
                @blur="checkEmailDuplicate()"
            />        
            <v-text-field
                  v-model="authCodeValue"
                  type="text"
                  label="인증번호"
                  variant="solo"
                  density="comfortable"
                  hide-details="auto"
                  maxlength="6"
                  :rules="[required]"
                  :error-messages="errorMsgAuthCode"
                  :success-messages="successMsgAuthCode"
                  v-if="isAuthCodeRequest"
                  class="authCodeFiled"
          />       

        </div>
        <v-btn type="button" class="login-btn" @click="handleAuthMailRequest" v-show="!isAuthCodeRequest">
            인증요청
        </v-btn>

        <v-btn type="submit" class="login-btn" v-show="isAuthCodeRequest">
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

        .authCodeFiled {
          width: 10rem;
          height: 3rem;
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