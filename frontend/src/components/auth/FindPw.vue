<script setup>
import LogoMini from '../header/LogoMini.vue';

</script>

<template>
	<div class="title-wrap">
		<LogoMini />
		<h1 class="title">비밀번호 찾기</h1>
	</div>
	
  <router-view />
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
</style>