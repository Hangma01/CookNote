<script setup>
import { useUserStore } from '@/stores/user';
import { onBeforeUnmount, onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';

// 화면 전환
const router = useRouter();

// 유저 스토어
const userStore = useUserStore();
const isLoggedIn = userStore.isLoggedIn;

// 마이 페이지 아이템 이벤트
const showMyPageItem = ref(false);
const myPageItemRef = ref(null);  

// 외부 클릭 시 마이 페이지 아이템 창 닫기
const closeMyPageItem = (event) => {
    if (showMyPageItem && !myPageItemRef.value.contains(event.target)) {
        showMyPageItem.value = false;
    }
}

// 마이 페이지 아이템 창 토글
const handleToggleMyPageItem = (event) => {
    showMyPageItem.value = !showMyPageItem.value
     event.stopPropagation();
}

// 전체 페이지 클릭 이벤트를 추가
onMounted(() => {
    document.addEventListener('click', closeMyPageItem);
});

// 컴포넌트가 언마운트 될 때 이벤트 리스너 제거
onBeforeUnmount(() => {
    document.removeEventListener('click', closeMyPageItem);
});
</script>

<template>
    <ul class="user-menu">
        <li class="user-menu-item">
            <router-link :to="{ name: 'myPage' }" v-if="!isLoggedIn">
                <font-awesome-icon :icon="['far', 'user']" style="color: #454F5B;" />
            </router-link>

            <div v-if="isLoggedIn" @click="handleToggleMyPageItem" class="mypage-icon">
                <font-awesome-icon :icon="['far', 'user']" style="color: #454F5B;" />
            </div>

            <div class="user-menu-mypage-item" v-show="showMyPageItem" ref="myPageItemRef">
                <ul>
                    <router-link :to="{ name: 'recipesSearch',  query: { categorytype: 1 } }">
                        <li class="mypage-item">
                            홈
                        </li>
                    </router-link>

                    <router-link :to="{ name: 'recipesSearch',  query: { categorytype: 2 } }">
                        <li class="mypage-item">
                            팔로워
                        </li>
                    </router-link>
                    
                    <li class="mypage-item">
                        로그아웃
                    </li>
                </ul>
            </div>
        </li>

        <li class="user-menu-item">
            <router-link to="/add-recipe">
                <font-awesome-icon :icon="['fas', 'pen-to-square']" style="color: #454F5B;" />
            </router-link>
        </li>
    </ul>
</template>


<style lang="scss" scoped>
.user-menu{
    display: flex;
    gap: 30px;

    .user-menu-item{
        width: 3rem;
        font-size: 1.7rem;
        border-radius: 100%;
        align-items: center;
        position: relative;

        .user-menu-mypage-item {
            position: absolute;
            font-size: 1rem;
            width: 8rem;
            border: 1px solid #eee;
            background-color: #fff;
            border-bottom-right-radius: 0.5rem;
            border-bottom-left-radius: 0.5rem;
            padding-bottom: 0.3rem;
            left: -3.1rem;
            margin-top: 1rem;

            .mypage-item {
                display: flex;
                justify-content: center;
                align-items: center;
                padding: 6px 0;
                
            }

            .mypage-item:hover{
                background-color:#c09370;
            }
        }
    }
    .mypage-icon {
        cursor: pointer;
    }

}
</style>