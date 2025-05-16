import { createRouter, createWebHistory } from 'vue-router';
import { routes } from './routes';
import { useUserStore } from '@/stores/user';


const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: routes,
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
    } else if (to.name === 'recipeWrite' && !isLoggedIn) {
        const proceed = confirm("레시피 작성을 위해서 로그인이 필요합니다. 로그인 페이지로 이동할까요?");
        if (proceed) {
            next({ name: 'login'})
        }    
    } else {
        next()
    }

    
})

export default router
