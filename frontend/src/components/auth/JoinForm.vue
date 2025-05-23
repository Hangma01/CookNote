<script setup>
import { reactive, ref, watch } from 'vue';
import { idRule, pwRule, nameRule, pwConfirmRule, nicknameRule, emailRule, required, authCodeRule } from '@/utils/rules';
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
import { useTimer } from '@/utils/useTimer';


// 화면 전환
const router = useRouter();

// 유효성 겁사
const formRef = ref(null)                     // Form 유효성 검사
const ruleIdRef = ref(null)                 // 아이디 유효성 검사
const ruleNicknameRef = ref(null)             // 닉네임 유효성 검사
const pwConfirmRef = ref(null);

// 에러 메시지
const errorMsgIdDuplicate = ref('')           // 아이디 중복 시 에러 메시지
const errorMsgNicknameDuplicate = ref('')     // 닉네임 중복 시 에러 메시지
const errorMsgAuthCode = ref('')              // 메일 인증 코드 에러 메시지

// 성공 메시지
const isSuccessAuthCode = ref(false)          // 메일 인증 코드 성공 메시지
const isSuccessIdCheck = ref(false)           // 아이디 인증 성공 메시지
const isSuccessNicknameCheck = ref(false)     // 닉네임 인증 성공 메시지

// etc...
const isAuthCodeRequest = ref(false)          // 메일 인증 요청 토글
const pwVisible = ref(false)                  // 비밀번호 필드 토글
const pwConfirmVisible = ref(false)           // 비밀번호 확인
const isAtuhCodeimer = ref(false)						// 메일 인증 시간 제한


// input-field
const formValues = reactive({                 // Form input-field             
  id: '',
  pw: '',
  pwConfirm: '',
  name: '',
  nickname: '',
  email: ''
})

const authCodeValue = ref('')                 // 메일 인증 input-field



// 타이머를 2분으로 설정하고 타이머 종료시 동작
const { timer, startTimer, stopTimer, resetTimer, isTimerRunning } = useTimer(15, () => {
  isAtuhCodeimer.value = false
});

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
    successMsgRef: isSuccessIdCheck,
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
    successMsgRef: isSuccessNicknameCheck,
    apiCall: existsNickname,
    router : router
  });
};


// 메일 인증 요청하기
const handleSendMailAuthCode = async () => {

  const isFormVal = await formRef.value.validate()

  if (
      isFormVal.valid &&
      !errorMsgIdDuplicate.value &&
      !errorMsgNicknameDuplicate.value
  ) { 

      if(isSuccessIdCheck.value === false) {
        alert('아이디 중복 검사를 해주세요.')
      } else if(isSuccessNicknameCheck.value === false) {
        alert('닉네임 중복 검사를 해주세요.')
      } else {
        try {
          const res = await sendMailAuthCode(formValues.email);

          resetTimer();   
          startTimer();
          isAtuhCodeimer.value = true

          isAuthCodeRequest.value = true;
        } catch (e) {
          console.log(e)
          alert(errorMessages.BADREQUEST);
          router.replace({ name: 'login'});
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
    router.replace({ name: 'login'});
  }
}


// 회원 가입 하기
const handleSubmitJoin = debounce(async () => {

  const isFormVal = await formRef.value.validate(); // 전체 유효성 검사 확인

  if(!isAtuhCodeimer.value){
		alert("메일 인증 시간이 초과했습니다. 재전송을 해주세요.")
	} else if(isFormVal.valid) {
    // 인증 코드 검증하기
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
        const res = await userJoin({ ...formValues })
        
        alert(successMessage.userJoin);
        router.push({ name: 'mainPage'});
      } catch (e) {
        if(e.response && e.response.data.status === HttpStatusCode.BadRequest) {
          alert(e.response.data.message)
        }else {
          alert(errorMessages.BADREQUEST);
        }
        window.location.reload();
      }
    }
  }
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
    
    if(newVal.id !== oldVal.id) {
      isSuccessIdCheck.value = false;
    } else if(newVal.nickname !== oldVal.nickname) {
      isSuccessNicknameCheck.value = false;
    }
  },
  { deep: true }  // formValues 내부 값들을 추적 가능하게 함
)

watch(() => formValues.pw, () => {
  if (formValues.pwConfirm !== '' && formRef.value)  {
    pwConfirmRef.value.validate();
  }
});
</script>

<template>
  <div class="title-wrap">
    <LogoMini />
    <h1 class="title">회원가입</h1>
  </div>

  <v-form ref="formRef" class="join-form" @submit.prevent="handleSubmitJoin">
    <div class="join-content">
      <div>
        <div class="id-form">
          <v-text-field
            v-model="formValues.id"
            type="text"
            label="아이디"
            variant="outlined"
            density="compact"
            hide-details="auto"
            maxlength="20"
            ref="ruleIdRef"
            :rules="[idRule]"
            :error-messages="errorMsgIdDuplicate"
          />
          <v-btn class="id-check-btn" @click="handleExistsId()">
            중복확인
          </v-btn>
        </div>
              
        <div v-if="isSuccessIdCheck" class="success-message">
          <span>
            사용 가능한 아이디입니다.
          </span>
        </div>
      </div>


      <v-text-field
          v-model="formValues.pw"
          :type="pwVisible ? 'text' : 'password'"
          label="비밀번호"
          variant="outlined"
          density="compact"
          hide-details="auto"
          maxlength="16"
          :rules="[pwRule]"
          :append-inner-icon="pwVisible ? 'mdi-eye-off' : 'mdi-eye'"
          @click:append-inner="pwVisible = !pwVisible"
      />

			<v-text-field
        ref="pwConfirmRef"
				v-model="formValues.pwConfirm"
				:type="pwConfirmVisible ? 'text' : 'password'"
				label="비밀번호 확인"
				variant="outlined"
				density="compact"
				hide-details="auto"
        maxlength="16"
				:rules="[pwConfirmRule(() => formValues.pw)]"
				:append-inner-icon="pwConfirmVisible ? 'mdi-eye-off' : 'mdi-eye'"
        @click:append-inner="pwConfirmVisible = !pwConfirmVisible"
			/>

      <v-text-field
          v-model="formValues.name"
          @input="handleNameInput"
          type="text"
          label="이름"
          variant="outlined"
          density="compact"
          hide-details="auto"
          :rules="[nameRule]"
      />

      <div>
        <div class="nickname-form">
          <v-text-field
            v-model="formValues.nickname"
            @input="handleNicknameInput"
            type="text"
            label="닉네임"
            variant="outlined"
            density="compact"
            hide-details="auto"
            ref="ruleNicknameRef"
            :rules="[nicknameRule]"
            :error-messages="errorMsgNicknameDuplicate"
          />

          <v-btn class="nickname-check-btn" @click="handleExistsNickname()">
            중복확인
          </v-btn>
        </div>
        
        <div v-if="isSuccessNicknameCheck" class="success-message">
          <span>
            사용 가능한 닉네임입니다.
          </span>
        </div>
      </div>

      <v-text-field
          v-model="formValues.email"
          type="email"
          label="이메일"
          variant="outlined"
          density="compact"
          hide-details="auto"
          maxlength="100"
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
          <v-btn type="button" class="auth-mail-retry" @click="handleSendMailAuthCodeRetry" v-if="isAuthCodeRequest">
            재전송
          </v-btn>
        </div>     

        <!-- <div v-if="isSuccessAuthCode" class="success-message">
          <span>
            인증에 성공했습니다.
          </span>
        </div> -->

        <div class="timer" v-if="isAuthCodeRequest">
					<span>{{ String(Math.floor(timer / 60)).padStart(1, '0') }}:{{ String(timer % 60).padStart(2, '0') }}</span>
				</div>
      </div>
    </div>

    <v-btn type="button" class="join-btn" @click="handleSendMailAuthCode" v-show="!isAuthCodeRequest">
        인증요청
    </v-btn>

    <v-btn type="submit" class="join-btn" v-if="isAuthCodeRequest">
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
        gap: 1.5rem;

        .id-form {
          display: flex;
          gap: 1rem;
          
          .id-check-btn {
            margin-top: 0.1rem;
            font-size: 0.8rem;
          }
        }

        .nickname-form {
          display: flex;
          gap: 1rem;

          .nickname-check-btn {
            margin-top: 0.1rem;
            font-size: 0.8rem;
          }
        }


        .auth-code-wrap{
          display: flex;
          align-items: center;
          gap: 1.3rem;
          

          .auth-mail-retry {
            display: inline;
            background-color: #888;
            color: white;
          }
        }


    }

    .join-btn{
      background-color: #c09370;
      color: white;
      font-size: 1rem;
      height: 2.5rem;
    }

    .success-message {
      padding-left: 1rem;
      padding-top: 0.3rem;
      font-size: 0.75rem;
      color: green;
    }
  .timer{
		font-size: 0.8rem;
		color: #FF3F3F;
		margin-top: 0.5rem;
		margin-left: 1rem;
	}
}
</style>