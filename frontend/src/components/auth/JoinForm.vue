<script setup>
import {reactive, ref, watch } from 'vue';
import { userIdRule, userPwRule, userUsernameRule, userNicknameRule, userEmailRule } from '../../utils/rules';

// input-field 목록들
const formValues = reactive({
  username: '',
  id: '',
  pw: '',
  email: '',
  nickname: ''
})

const handleUsernameInput = (e) => {
  const username = e.target.value;

  if (username.length > 20) {
    formValues.username = username.slice(0, 20);
  } else {
    formValues.username = username;
  }
}

const handleNicknameInput = (e) => {
    const nickname = e.target.value;

  if (nickname.length > 15) {
    formValues.nickname = nickname.slice(0, 15);
  } else {
    formValues.nickname = nickname;
  }
}

const idError = ref('')

const checkUserIdDuplicate = async () => {
  if (!formValues.id) return

  try {
    const res = await axios.get(`/users/check-id?value=${formValues.id}`)
    if (!res.data.available) {
      idError.value = '이미 사용 중인 아이디입니다'
    } else {
      idError.value = ''
    }
  } catch (e) {
    idError.value = '중복 체크 실패 (서버 오류)'
  }
}
</script>

<template>
    <div>

    </div>

    <v-form class="join-form">
        <div class="join-content">
            <v-text-field
                v-model="formValues.id"
                type="text"
                label="아이디"
                variant="solo"
                density="comfortable"
                hide-details="auto"
                maxlength="20"
                :rules="[userIdRule]"
                :error-messages="idError"
                @blur="checkUserIdDuplicate()"
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