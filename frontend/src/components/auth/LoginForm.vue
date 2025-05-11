<script setup>
import { computed, reactive } from 'vue';
import Logo from '../header/Logo.vue';
import AuthHeader from '../header/authHeader/AuthHeader.vue';


const formValues = reactive({
  id: '',
  pw: '',
})

const isFormValid = computed(() => {
  return formValues.id.trim() !== '' && formValues.pw.trim() !== '';
});
</script>

<template>
    <AuthHeader />
    <v-form class="login-form">
        <div class="login-content">
            <v-text-field
                v-model="formValues.id"
                type="text"
                label="아이디"
                variant="solo"
                density="comfortable"
                hide-details="auto"
            />
 
            <v-text-field
                v-model="formValues.pw"
                type="password"
                label="비밀번호"
                variant="solo"
                density="comfortable"
                hide-details="auto"
            />

            <v-btn :disabled="!isFormValid" class="login-btn">
                로그인
            </v-btn>
        </div>
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
.login-form, .find-wrap{

    display: flex;
    flex-direction: column;
    gap: 1.5rem;

    .login-content {
        display: flex;
        flex-direction: column;
        gap: 1.8rem;

        .login-btn {
            margin-top: 2rem;
            font-size: 1rem;
            height: 2.5rem;
            font-weight: 700;
        }

        .login-btn:not(:disabled) {
            background-color: #c09370;
        }
    }

    .find-box {
        margin-top: 3rem;
        display: flex;
        gap: 2rem;
        justify-content: space-around;
    }

    li {
        position: relative;
        font-size: 0.75rem;
  
        .find-text{
            color: #888;
        }
    }

    li + li::before {
        content: '|';
        position: absolute;
        left: -65%;
        top: 50%;
        transform: translateY(-50%);
        color: #dadada;
        font-size: 0.7rem;
    }
}



</style>