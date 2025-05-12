<script setup>
import { reactive, ref } from 'vue';
import { debounce } from 'lodash';
import { pwRule, newPwConfirmRule } from '@/utils/rules';
import { commonValues } from '@/utils/commonValues';
import { userFindPwRest } from '@/services/userService';
import { HttpStatusCode } from 'axios';
import { errorMessages } from '@/utils/errorMessages';
import { useRouter } from 'vue-router';

// 화면 전환
const router = useRouter();


// 유효성 겁사
const formRef = ref(null);                     // Form 유효성 검사


// input-field
const formValues = reactive({
  newPw: '',
  newPwConfirm: '',
})

// history
const pwResetToken = history.state.pwResetToken;

// etc...
const newPwVisible = ref(false)            		// 새 비밀번호 필드 토글
const newPwConfirmVisible = ref(false)     		// 새 비밀번호 확인 필드 토글


const handleFindPwReset = debounce(async () => {
	
	const isFormVal = await formRef.value.validate()

	if (isFormVal.valid) {
		try {
			const res = await userFindPwRest({
				...formValues,
				pwResetToken
			})
			
			alert('성공적으로 비밀번호가 변경되었습니다.')
			router.replace({ name: 'login' });
		} catch (e) {
			if (e.response 
					&& (e.response.data.status === HttpStatusCode.Gone || e.response.data.status === HttpStatusCode.BadRequest) 
					&& e.response.data.message){
				alert(e.response.data.message);
			} else {
				alert(errorMessages.badRequest);
			}

			router.replace({ name: 'userFindPw' });
		}
	}
}, commonValues.defaultDebounce);
</script>

<template>
	<v-form ref="formRef" class="find-pw-change-form" @submit.prevent="handleFindPwReset">
		<div class="find-pw-change-content">
			<v-text-field
				v-model="formValues.newPw"
				:type="newPwVisible ? 'text' : 'password'"
				label="새 비밀번호"
				variant="solo"
				density="comfortable"
				hide-details="auto"
				maxlength="16"
				:rules="[pwRule]"
				:append-inner-icon="newPwVisible ? 'mdi-eye-off' : 'mdi-eye'"
        @click:append-inner="newPwVisible = !newPwVisible"
			/>

			<v-text-field
				v-model="formValues.newPwConfirm"
				:type="newPwConfirmVisible ? 'text' : 'password'"
				label="새 비밀번호 확인"
				variant="solo"
				density="comfortable"
				hide-details="auto"
				:rules="[newPwConfirmRule(() => formValues.newPw)]"
				:append-inner-icon="newPwConfirmVisible ? 'mdi-eye-off' : 'mdi-eye'"
        @click:append-inner="newPwConfirmVisible = !newPwConfirmVisible"
			/>
		</div>
		
		<v-btn type="submit" class="find-pw-change-btn">
			비밀번호 변경
		</v-btn>
	</v-form>
</template>


<style lang="scss" scoped>
.find-pw-change-form{
	display: flex;
	flex-direction: column;
	justify-content: space-between;
	gap: 1.5rem;
	height: 17rem;

	.find-pw-change-content {
		display: flex;
		flex-direction: column;
		gap: 1.8rem;

	}

	.find-pw-change-btn{
		margin-top: 2rem;
		background-color: #c09370;
		color: white;
		font-size: 1rem;				
		height: 2.5rem;
		font-weight: 700;
	}
}
</style>