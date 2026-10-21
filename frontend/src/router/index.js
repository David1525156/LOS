import { createRouter, createWebHistory } from 'vue-router'
import BookListView from '../views/BookListView.vue'
import BookDetailView from '../views/BookDetailView.vue'

const routes = [
  { path: '/', name: 'BookList', component: BookListView },
  { path: '/books/:id', name: 'BookDetail', component: BookDetailView, props: true }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
