<template>
  <div class="app">
    <header class="app-header">
      <h2 @click="$router.push('/')" style="cursor: pointer">在线图书借阅系统</h2>
    </header>
    <main class="app-main">
      <router-view />
    </main>
  </div>
</template>

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
}

.app-main {
  max-width: 960px;
  margin: 24px auto;
  padding: 0 16px;
}
</style>
