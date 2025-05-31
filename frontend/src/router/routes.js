import { nameRule } from '@/utils/rules';

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
                component: () => import('@/views/MainView.vue'),
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
                        meta: { requiresAuth: true },
                    },
                    {
                        path: 'edit/:recipeId',
                        name: 'recipeEdit',
                        component: () => import('../components/recipe/recipeWrite/RecipeWrite.vue'),
                        meta: { requiresAuth: true },
                    },
                    {
                        path: ':recipeId',
                        name: 'recipeDetail',
                        component: () => import('../components/recipe/recipeDetail/RecipeDetail.vue'),
                    },
                ],
            },
            // 프로필 라우터
            {
                path: '/profile',
                component: () => import('../views/ProfileView.vue'),
                children: [
                    {
                        path: '',
                        component: () => import('@/components/profile/ProfileHome.vue'),
                        meta: { requiresAuth: true },
                        children: [
                            {
                                path: 'recipe',
                                name: 'profileRecipe',
                                component: () => import('@/components/profile/ProfileRecipe.vue'),
                            },
                            {
                                path: 'bookmark',
                                name: 'profileBookmark',
                                component: () => import('@/components/profile/ProfileBookmark.vue'),
                            },
                            {
                                path: 'comment',
                                name: 'profileComment',
                                component: () => import('@/components/profile/ProfileComment.vue'),
                            },
                            {
                                path: 'like',
                                name: 'profileLike',
                                component: () => import('@/components/profile/ProfileLike.vue'),
                            },
                            {
                                path: 'follow',
                                name: 'profileFollow',
                                component: () => import('@/components/profile/ProfileFollow.vue'),
                            },
                        ],
                    },
                    {
                        path: 'edit',
                        component: () => import('@/components/profile/edit/ProfileEditHome.vue'),
                        meta: { requiresAuth: true },
                        children: [
                            {
                                path: '',
                                name: 'profileEdit',
                                component: () => import('@/components/profile/edit/ProfileEdit.vue'),
                            },
                            {
                                path: 'pw-rest',
                                name: 'profilePwEdit',
                                component: () => import('@/components/profile/edit/ProfilePwEdit.vue'),
                            },
                        ],
                    },
                    {
                        path: 'report',
                        component: () => import('@/components/profile/report/ProfileReportHome.vue'),
                        meta: { requiresAuth: true },
                        children: [
                            {
                                path: '',
                                name: 'profileReport',
                                component: () => import('@/components/profile/report/ProfileReport.vue'),
                            },
                            {
                                path: 'pw-rest',
                                name: 'profileSanction',
                                component: () => import('@/components/profile/report/ProfileSanction.vue'),
                            },
                        ],
                    },
                    {
                        path: ':hostId',
                        name: 'profileHost',
                        component: () => import('@/components/profile/ProfileHost.vue'),
                    },
                ],
            },
            // 검색 라우터
            {
                path: '/search',
                name: 'search',
                component: () => import('../views/SearchView.vue'),
            },
            {
                path: '/search-ingredient',
                name: 'searchIngredient',
                component: () => import('../views/SearchIngredient.vue'),
            },
            {
                path: '/search-chef',
                name: 'searchChef',
                component: () => import('../views/SearchChef.vue'),
            },
            {
                path: '/feed',
                name: 'followFeed',
                component: () => import('../views/FeedView.vue'),
                meta: { requiresAuth: true },
            },
        ],
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
                ],
            },
        ],
    },
    {
        path: '/policies/terms',
        name: 'terms',
        component: () => import('../views/TermsOfPolicyView.vue'),
    },
    {
        path: '/policies/privacy',
        name: 'policy',
        component: () => import('../views/PrivacyOfPolicyView.vue'),
    },
    // 404 페이지 라우터 추가
    {
        path: '/:pathMatch(.*)*',
        name: 'notFound',
        component: () => import('../views/NotFound.vue'),
    },
];
