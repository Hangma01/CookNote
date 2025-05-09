<script setup>
import { reactive, ref } from 'vue';
import { debounce } from 'lodash';
import { findIdRule, emailRule } from '@/utils/rules';
import { commonValues } from '@/utils/commonValues';
import { userFindId } from '@/services/userService';
import { errorMessages } from '@/utils/errorMessages';
import { HttpStatusCode } from 'axios';


// 유효성 겁사
const formRef = ref(null);                     // Form 유효성 검사


// input-field
const formValues = reactive({
  name: '',
  email: '',
})

const handleSubmitFindId = debounce(async () => {

	const isFormVal = await formRef.value.validate()
	if (isFormVal.valid) {
		try {
				const res = await userFindId(formValues.name, formValues.email)

				if(res.status === HttpStatusCode.Ok) {
						console.log(res.data.userId)
				}
		} catch (e) {
			if(e.response &&
				(e.response.data.status === HttpStatusCode.BadRequest || e.response.data.status === HttpStatusCode.NotFound)
					&& e.response.data.message
			){
				alert(e.response.data.message);
			} else {
				alert(errorMessages.badRequest);
			}
		}
	}
}, commonValues.defaultDebounce);
</script>

<template>
	<v-form ref="formRef" class="find-id-form" @submit.prevent="handleSubmitFindId">
		<div class="find-id-content">
			<v-text-field
				v-model="formValues.name"
				type="text"
				label="이름"
				variant="solo"
				density="comfortable"
				hide-details="auto"
				:rules="[findIdRule]"
			/>

			<v-text-field
				v-model="formValues.email"
				type="text"
				label="이메일"
				variant="solo"
				density="comfortable"
				hide-details="auto"
				:rules="[emailRule]"
			/>
		</div>

			<v-btn type="submit" class="find-id-btn">
				아이디 찾기
			</v-btn>
	</v-form>


    
</template>


<style lang="scss" scoped>
.find-id-form{
	display: flex;
	flex-direction: column;
	justify-content: space-between;
	gap: 1.5rem;
	height: 15rem;

	.find-id-content {
		display: flex;
		flex-direction: column;
		gap: 1.8rem;

		.find-id-btn {
				margin-top: 2rem;
				font-size: 1rem;
				height: 2.5rem;
				font-weight: 700;
		}
	}

	.find-id-btn{
		background-color: #c09370;
		color: white;
		font-size: 1rem;
		height: 2.5rem;
	}
}
</style>