import { createRouter, createWebHistory } from 'vue-router';
import { routes } from './routes';
import { useUserStore } from '@/stores/user';


const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: routes,
    scrollBehavior(to, from, savedPosition) {
    // 1. 브라우저 뒤로가기/앞으로가기 시에는 이전 위치로 복원
    if (savedPosition) {
        return savedPosition;
    }

    // 2. 그 외에는 항상 최상단으로 스크롤
    return { top: 0 };
    }
})

router.beforeEach((to, from, next) => {
    // 유저 스토어
    const userStore = useUserStore()
    const isLoggedIn = userStore.getIsLoggedIn


    if (to.name === 'myPage' && !isLoggedIn) {
        next({ name: 'login' }) 
    } else if (to.meta.requiresFlow &&  from.name !== 'userFindId' && to.name === 'userFindIdResult') {
        next({ name: 'userFindId'})
    } else if (to.meta.requiresFlow &&  from.name !== 'userFindPw' && to.name === 'pwReset') {
        next({ name: 'userFindPw' }) 
    } else if ((to.name === 'recipeWrite' || to.name === 'recipeEdit') && !isLoggedIn) {
        const proceed = confirm("레시피 작성을 위해서 로그인이 필요합니다. 로그인 페이지로 이동할까요?");
        if (proceed) {
            next({ name: 'login'})
        }    
    } else {
        next()
    }

    
})

export default router
