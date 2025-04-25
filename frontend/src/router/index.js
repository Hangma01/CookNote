import { createRouter, createWebHistory } from 'vue-router'
import { routes } from './routes'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: routes,
})

router.beforeEach((to, from, next) => {
    let isAuthenticated = false

    if (to.meta.requiresAuth && to.name === 'myPage' && !isAuthenticated) next({ name: 'login' })  
    else next()
})

export default router
