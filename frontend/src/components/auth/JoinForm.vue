<script setup>
import {reactive, ref } from 'vue';
import { loginIdRule, userPwRule, userUsernameRule, userNicknameRule, userEmailRule } from '../../utils/rules';
import userService from '@/services/userService';
import { HttpStatusCode } from 'axios';


const formValues = reactive({         // input-field 목록들
  username: '',
  id: '',
  pw: '',
  email: '',
  nickname: ''
})


const formRef = ref(null)             // Form 유효성 검사
const ruleIdRef = ref(null)           // 아이디 유효성 검사
const errorMsgIdDuplicate = ref('')   // 아이디 중복 시 메시지

// 사용자 이름 길이 MAX 20 설정
const handleUsernameInput = (e) => {  
  const username = e.target.value;

  if (username.length > 20) {
    formValues.username = username.slice(0, 20);
  } else {
    formValues.username = username;
  }
}


// 사용자 닉네임 길이 MAX 15 설정
const handleNicknameInput = (e) => {
    const nickname = e.target.value;

  if (nickname.length > 15) {
    formValues.nickname = nickname.slice(0, 15);
  } else {
    formValues.nickname = nickname;
  }
}



// 아이디 중복 체크
const checkIdDuplicate = async () => {

  let isIdValid = await ruleIdRef.value.validate();

  // 아이디 유효성 검증
  if (!formValues.id || isIdValid[0]) {
    errorMsgIdDuplicate.value = ''
    return
  }

  // 아이디 중복 체크
  try {
    const res = await userService.existsLoginId(formValues.id)

    if (res.status === HttpStatusCode.Ok) {         // 아이디 중복 없음
        errorMsgIdDuplicate.value = ''
    } 
  } catch (e) {
    const error = e.response.data

    if (error.status === HttpStatusCode.Conflict) { // 아이디 중복일 경우
        errorMsgIdDuplicate.value = error.message
    } else {                                        // 예외 처리
      alert('서버와의 통신이 원할하지 않습니다.')
    }
  }
}

// 메일 인증하기
const handleSubmit = async () => {
  const isValid = await formRef.value.validate();

  if(isValid.vaild && errorMsgIdDuplicate){ // 유효성 검사 통과 시 메일 인증 코드 발송
    console.log('성공')
  } 
}
</script>

<template>
    <v-form ref="formRef" class="join-form" @submit.prevent="handleSubmit">
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
                type="text"
                label="비밀번호"
                variant="solo"
                density="comfortable"
                hide-details="auto"
                maxlength="16"
                :rules="[userPwRule]"
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
                :rules="[userNicknameRule]"
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
            />

            <v-btn type="submit" class="login-btn">
                인증요청
            </v-btn>
        </div>
    </v-form>
</template>


<style lang="scss" scoped>

.join-form{

    display: flex;
    flex-direction: column;
    gap: 1.5rem;

    .join-content {
        display: flex;
        flex-direction: column;
        gap: 1.8rem;
    }
}


</style>