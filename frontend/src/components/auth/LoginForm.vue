<script setup>
import { reactive } from 'vue';


const formValues = reactive({
  id: '',
  pw: '',
})
</script>

<template>
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
        </div>

        <div>
            버튼들
        </div>
        <div>
            <router-link :to="{ name: 'userjoin' }">
                gd
            </router-link>
        </div>


    </v-form>
</template>


<style lang="scss" scoped>
.login-form{

    display: flex;
    flex-direction: column;
    gap: 1.5rem;

    .login-content {
        display: flex;
        flex-direction: column;
        gap: 1.8rem;

    }
}



</style>