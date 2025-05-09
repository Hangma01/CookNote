export const routes = [
    { 
        path: '/',
        // name으로 하면 path 경로 바뀌어도 괜찮음
        name: 'mainPage',
        component: () => import('../views/MainView.vue') 
    },
    { 
      path: '/recipes-search',
      // name으로 하면 path 경로 바뀌어도 괜찮음
      name: 'recipesSearch',
      component: () => import('../views/RecipesView.vue') ,
    },
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
          },
          {
            path: 'userfindpw',
            name: 'userFindPw',
            component: () => import('../components/auth/FindPwForm.vue'),
          },
          {
            path: 'userPwChange',
            name: 'userPwChange',
            component: () => import('../components/auth/userPwChange.vue'),
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