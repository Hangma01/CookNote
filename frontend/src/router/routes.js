export const routes = [
    { 
        path: '/',
        // name으로 하면 path 경로 바뀌어도 괜찮음
        name: 'mainPage',
        component: () => import('../views/MainView.vue') 
    },
    {
        path: '/login',
        component: () => import('../views/Login.vue'),
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
            component: () => import('../components/auth/JoinForm.vue'),
          },
          {
            path: 'userfindpw',
            name: 'userFindPw',
            component: () => import('../components/auth/JoinForm.vue'),
          }
        ]
    },
    {
        path: '/mypage',
        name: 'myPage',
        component: () => import('../views/MyPage.vue'),
        meta: { requiresAuth: true },
    }
  ]