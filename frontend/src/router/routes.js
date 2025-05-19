export const routes = [
  {
    path: '/',
    // name으로 하면 path 경로 바뀌어도 괜찮음
    name: 'mainPage',
    component: () => import('../views/MainView.vue')
  },
  // 로그인 관련련
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
  // 레시피 관련
  {  
    path: '/recipe',
    component: () => import('../views/RecipeView.vue'),
    children: [
      {
        path: '',
        name: 'recipeDetail',
        component: () => import('../components/recipe/recipeDetail/RecipeDetail.vue'),
      },
      {
        path: 'write',
        name: 'recipeWrite',
        component: () => import('../components/recipe/recipeWrite/RecipeWrite.vue'),
        meta: { requiresAuth: true },
      },
      {
        path: 'edit/:recipeId',
        name: 'recipeEdit',
        component: () => import('../components/recipe/recipeWrite/RecipeWrite.vue'),
        meta: { requiresAuth: true },
      },
    ]
  },
  {
    path: '/mypage',
    name: 'myPage',
    component: () => import('../views/MyPageView.vue'),
    meta: { requiresAuth: true },
  }
]