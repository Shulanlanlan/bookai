import {createRouter, createWebHistory} from 'vue-router';
import StartScreen from '../components/WelcomeScreen.vue';
import LoginPage from '../views/LoginPage.vue';
import RegisterPage from '../views/RegisterPage.vue';
import BookChatPage from '../views/BookChatPage.vue';

const routes = [
    {path: '/', component: StartScreen},
    {path: '/login', component: LoginPage},
    {path: '/register', component: RegisterPage},
    {path: '/bookai', component: BookChatPage},
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

// 全局导航守卫
router.beforeEach((to, from, next) => {
  const publicPages = ['/', '/login', '/register'];
  const authRequired = !publicPages.includes(to.path);
  const token = localStorage.getItem('token');

  if (authRequired && !token) {
    next('/'); // 没有 token 的话跳转到欢迎界面
  } else {
    next(); // 允许访问
  }
});

export default router;
