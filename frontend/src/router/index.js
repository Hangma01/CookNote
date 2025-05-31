import { createRouter, createWebHistory } from 'vue-router';
import { routes } from './routes';
import { useUserStore } from '@/stores/user';

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: routes,
    scrollBehavior(to, from, savedPosition) {
        // 1. 브라우저 뒤로가기/앞으로가기 시에는 이전 위치로 복원
        if (savedPosition) {
            // 데이터가 로드될 때까지 대기(예: 300ms) 후 복원
            return new Promise((resolve) => {
                setTimeout(() => {
                    resolve(savedPosition);
                }, 20);
            });
        }
        return { top: 0 };
    },
});

router.beforeEach((to, from, next) => {
    // 유저 스토어
    const userStore = useUserStore();
    const isLoggedIn = userStore.getIsLoggedIn;

    if (to.meta.requiresFlow && from.name !== 'userFindId' && to.name === 'userFindIdResult') {
        next({ name: 'userFindId' });
    } else if (to.meta.requiresFlow && from.name !== 'userFindPw' && to.name === 'pwReset') {
        next({ name: 'userFindPw' });
    } else if (to.matched.some((record) => record.meta.requiresAuth) && !isLoggedIn) {
        const proceed = confirm('로그인이 필요한 페이지입니다. 로그인하시겠습니까?');

        if (proceed) {
            next({ name: 'login' });
        } else {
            next(from.fullPath); // 이전페이지로
        }
    } else {
        next(); // 통과
    }
});

export default router;
