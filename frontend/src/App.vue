<template>
  <div class="app">
    <header class="app-header">
      <h2 @click="$router.push('/')" style="cursor: pointer">在线图书借阅系统</h2>
      <nav class="app-nav">
        <span v-if="isLoggedIn" @click="$router.push('/books/create')" style="cursor: pointer">新增图书</span>
        <span v-if="isLoggedIn" @click="logout" style="cursor: pointer">退出</span>
        <span v-else @click="$router.push('/login')" style="cursor: pointer">管理员登录</span>
      </nav>
    </header>
    <main class="app-main">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const isLoggedIn = ref(!!localStorage.getItem('token'))

function logout() {
  localStorage.removeItem('token')
  localStorage.removeItem('username')
  window.location.href = '/'
}
</script>

<style>
body {
  margin: 0;
  font-family: 'Helvetica Neue', Helvetica, Arial, 'Microsoft YaHei', sans-serif;
  background: #f5f7fa;
}

.app-header {
  background: #409eff;
  color: #fff;
  padding: 12px 24px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.app-header h2 {
  margin: 0;
}

.app-nav {
  display: flex;
  gap: 16px;
  font-size: 14px;
}

.app-main {
  max-width: 960px;
  margin: 24px auto;
  padding: 0 16px;
}
</style>
