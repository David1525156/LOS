import { createRouter, createWebHistory } from 'vue-router'
import BookListView from '../views/BookListView.vue'
import BookDetailView from '../views/BookDetailView.vue'
import BookCreateView from '../views/BookCreateView.vue'
import BookEditView from '../views/BookEditView.vue'
import LoginView from '../views/LoginView.vue'

const routes = [
  { path: '/', name: 'BookList', component: BookListView },
  { path: '/books/:id', name: 'BookDetail', component: BookDetailView, props: true },
  { path: '/login', name: 'Login', component: LoginView },
  // 新增 / 编辑需管理员登录（路由守卫校验）
  { path: '/books/create', name: 'BookCreate', component: BookCreateView, meta: { requiresAuth: true } },
  { path: '/books/:id/edit', name: 'BookEdit', component: BookEditView, props: true, meta: { requiresAuth: true } }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫：未登录访问受保护页面时跳转登录页，并记录回跳地址
router.beforeEach((to) => {
  if (to.meta.requiresAuth && !localStorage.getItem('token')) {
    return { name: 'Login', query: { redirect: to.fullPath } }
  }
  return true
})

export default router
