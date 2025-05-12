<script setup>

const userId = history.state.userId;

</script>

<template>
  <div class="find-id-result-info">
    <p>고객님의 아이디 찾기가 완료되었습니다.</p>
    <p class="find-id-value">{{ userId }}</p>
  </div>

  <div class="page-router-btn-wrap">
    
    <router-link :to="{ name: 'login' }">
      <v-btn class="page-router-btn login-page-router-btn">로그인하기</v-btn>
    </router-link>
    
    <router-link :to="{ name: 'userFindPw' }">
      <v-btn class="page-router-btn find-pw-page-router-btn">비밀번호 찾기</v-btn>
    </router-link>
  </div>
</template>


<style lang="scss" scoped>
.find-id-result-info {
  margin-top: 5rem;
  display: flex;
  flex-direction: column;
  gap: 2rem;
  font-size: 1.3rem;
  text-align: center;

  .find-id-value{
    color: #c09370;
    font-weight: 700;
    font-size: 2rem;
  }
}

.page-router-btn-wrap {
  margin-top: 4rem;
  display: flex;
  gap: 2rem;
  justify-content: center;

  .page-router-btn{
    height: 3rem;
    width: 8rem;
    font-size: 1rem;
  }

  .login-page-router-btn{
    background-color: #c09370;
    color: white;
    font-weight: 700;
  }

  .find-pw-page-router-btn {
    border: 1px solid #eee;
    color: #888;
  }
}
</style>