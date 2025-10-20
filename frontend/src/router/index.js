import { createRouter, createWebHistory } from 'vue-router';
import LoginPage from '../views/LoginPage.vue';
import RegisterPage from '../views/RegisterPage.vue';
import DashboardPage from '../views/DashboardPage.vue';
import DashboardHomePage from '../views/DashboardHomePage.vue';
import NovcaniciPage from '../views/NovcaniciPage.vue';
import NovcanikDetailPage from '../views/NovcanikDetailPage.vue';
import KategorijePage from '../views/KategorijePage.vue';
import CiljeviStednjePage from '../views/CiljeviStednjePage.vue';
import PonavljajucePage from '../views/PonavljajucePage.vue'; // DODATO

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: LoginPage
  },
  {
    path: '/register',
    name: 'Register',
    component: RegisterPage
  },
  {
    path: '/dashboard',
    component: DashboardPage,
    meta: { requiresAuth: true },
    children: [
      {
        path: '',
        name: 'DashboardHome',
        component: DashboardHomePage
      },
      {
        path: 'novcanici',
        name: 'Novcanici',
        component: NovcaniciPage
      },
      {
        path: 'novcanici/:id',
        name: 'NovcanikDetail',
        component: NovcanikDetailPage
      },
      {
        path: 'kategorije',
        name: 'Kategorije',
        component: KategorijePage
      },
      {
        path: 'ciljevi',
        name: 'Ciljevi',
        component: CiljeviStednjePage
      },
      // DODATA RUTA ZA PONAVLJAJUĆE
      {
        path: 'ponavljajuce',
        name: 'Ponavljajuce',
        component: PonavljajucePage
      }
    ]
  },
  {
    path: '/',
    redirect: '/login'
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

// Navigacioni stražar ostaje isti
router.beforeEach((to, from, next) => {
  const loggedIn = localStorage.getItem('token');

  if (to.meta.requiresAuth && !loggedIn) {
    next('/login');
  } else {
    next();
  }
});

export default router;
