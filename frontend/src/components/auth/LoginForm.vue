<script setup>
import { reactive, ref } from 'vue';
import AuthHeader from '../header/authHeader/AuthHeader.vue';
import { debounce } from 'lodash';
import { commonValues } from '@/utils/commonValues';
import { errorMessages } from '@/utils/messages/errorMessages';
import { useRouter } from 'vue-router';
import { login } from '@/services/authService';
import { useUserStore } from '@/stores/user';
import { defaultIdRule, defaultPwRule } from '@/utils/rules';

// 화면 전환
const router = useRouter();

// 유저 스토어
const userStore = useUserStore();

// 유효성 겁사
const formRef = ref(null);      						// Form 유효성 검사

// input-field
const formValues = reactive({             	// Form input-field 
  id: '',
  password: '',
})

// etc...
const pwVisible = ref(false)            		// 새 비밀번호 필드 토글


// 로그인 요청
const handleLogin = debounce(async () => {
	
	const isFormVal = await formRef.value.validate()

	if (isFormVal.valid) {
    try {
		  const res = await login({...formValues})
      const accessToken = res.headers[commonValues.AUTHORIZATION_HEADER]
  
      userStore.login(accessToken)
  
      router.replace({name : "mainPage"})
    } catch (e) {
      alert(errorMessages.LOGIN_ERROR)
    }
	}
}, commonValues.defaultDebounce);
</script>

<template>
  <AuthHeader />
  <v-form ref="formRef" class="login-form" @submit.prevent="handleLogin">
    <div class="login-content">
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
        v-model="formValues.password"
        :type="pwVisible ? 'text' : 'password'"
        label="비밀번호"
        variant="outlined"
        density="compact"
        hide-details="auto"
        :rules="[defaultPwRule]"
        :append-inner-icon="pwVisible ? 'mdi-eye-off' : 'mdi-eye'"
        @click:append-inner="pwVisible = !pwVisible"
      />


    </div>
      <v-btn type="submit" class="login-btn">
        로그인
      </v-btn>
  </v-form>

  <div class="find-wrap">
    <ul class="find-box">
      <li>
        <router-link :to="{ name: 'userFindPw' }" class="find-text">
            비밀번호 찾기
        </router-link>
      </li>

      <li>
        <router-link :to="{ name: 'userFindId' }" class="find-text">
            아이디 찾기
        </router-link>
      </li>

      <li> 
        <router-link :to="{ name: 'userjoin' }" class="find-text">
            회원가입
        </router-link>
      </li>
    </ul>
  </div>
</template>


<style lang="scss" scoped>
.login-form {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  height: 16rem;

  .login-content {
    display: flex;
    flex-direction: column;
    gap: 1.8rem;
  }    
  .login-btn {
    margin-top: 2rem;
    font-size: 1rem;
    height: 2.5rem;
    background-color: #c09370;
    color: white;
  }
}

.find-wrap{
  display: flex;
  flex-direction: column;
  gap: 1.5rem;


  .find-box {
    margin-top: 3rem;
    display: flex;
    gap: 2rem;
    justify-content: space-around;

    li {
      position: relative;
      font-size: 0.85rem;

      .find-text{
          color: #888;
      }
    }

    li + li::before {
      content: '|';
      position: absolute;
      left: -58%;
      top: 50%;
      transform: translateY(-50%);
      color: #dadada;
      font-size: 0.8rem;
    }
  }

  
}

</style>