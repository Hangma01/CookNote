export const routes = [
    {
        path: '/',
        // 메인 레이아웃
        component: () => import('@/views/MainLayout.vue'),
        children: [
            // 메인 페이지 라우터
            {
                path: '',
                name: 'mainPage',
                component: () => import('@/views/MainView.vue')
            },
            // 레시피 라우터
            {  
                path: '/recipe',
                component: () => import('@/views/RecipeView.vue'),
                children: [
                    {
                        path: 'write',
                        name: 'recipeWrite',
                        component: () => import('@/components/recipe/recipeWrite/RecipeWrite.vue'),
                        meta: { requiresAuth: true, forceRemount: true },
                    },
                    {
                        path: 'edit/:recipeId',
                        name: 'recipeEdit',
                        component: () => import('../components/recipe/recipeWrite/RecipeWrite.vue'),
                        meta: { requiresAuth: true, forceRemount: true },
                    },
                    {
                        path: ':recipeId',
                        name: 'recipeDetail',
                        component: () => import('../components/recipe/recipeDetail/RecipeDetail.vue'),
                    },
                ]
            },
            // 마이페이지 라우터
            {
                path: '/mypage',
                component: () => import('../views/MyPageView.vue'),
                meta: { requiresAuth: true },
                children: [
                    {
                        path: '',
                        name: 'myPage',
                        component: () => import('@/components/recipe/recipeWrite/RecipeWrite.vue'),
                        meta: { requiresAuth: true, forceRemount: true },
                    },
                    // {
                    //     path: ','
                    //     name: 'recipeEdit',
                    //     component: () => import('../components/recipe/recipeWrite/RecipeWrite.vue'),
                    //     meta: { requiresAuth: true, forceRemount: true },
                    // },
                    // {
                    //     path: '',
                    //     name: 'recipeDetail',
                    //     component: () => import('../components/recipe/recipeDetail/RecipeDetail.vue'),
                    // },
                ]
            }
        ]
    },
    // 로그인 라우터
    {
        path: '/login',
        component: () => import('../views/LoginView.vue'),
        children: [
            {
                path: '',
                name: 'login',
                component: () => import('../components/auth/LoginForm.vue'),
            },
            {
                path: 'userjoin',
                name: 'userjoin',
                component: () => import('../components/auth/JoinForm.vue'),
            },
            {
                path: 'userfindid',
                name: 'userFindId',
                component: () => import('../components/auth/FindIdForm.vue'),
            },
            {
                path: 'userfindidresult',
                name: 'userFindIdResult',
                component: () => import('../components/auth/FindIdResult.vue'),
                meta: { requiresFlow: true },
            },
            {
                path: 'userfindpw',
                component: () => import('../components/auth/FindPw.vue'),
                children: [
                {
                    path: '',
                    name: 'userFindPw',
                    component: () => import('../components/auth/FindPwForm.vue'),
                },
                {
                    path: 'pwreset',
                    name: 'pwReset',
                    component: () => import('../components/auth/FindPwResetForm.vue'),
                    meta: { requiresFlow: true },
                },
                ]
            },
        ]
    },
    // 404 페이지 라우터 추가
    {
        path: '/:pathMatch(.*)*',
        name: 'notFound',
        component: () => import('../views/NotFound.vue'),
    }
]