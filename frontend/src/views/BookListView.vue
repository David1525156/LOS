<template>
  <div class="book-list">
    <h2>图书列表</h2>

    <!-- 加载失败友好提示 -->
    <el-alert
      v-if="loadError"
      type="error"
      :title="loadError"
      show-icon
      :closable="false"
      style="margin-bottom: 16px"
    />

    <el-table
      v-loading="loading"
      :data="books"
      style="width: 100%"
      empty-text="暂无图书"
      @row-click="goDetail"
    >
      <el-table-column prop="title" label="书名" min-width="220" />
      <el-table-column prop="author" label="作者" min-width="160" />
      <el-table-column prop="stock" label="库存" width="100" align="center" />
    </el-table>

    <div class="pagination">
      <el-pagination
        v-model:current-page="page"
        v-model:page-size="size"
        layout="total, prev, pager, next"
        :total="total"
        @current-change="loadBooks"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { listBooks } from '../api/book'

const router = useRouter()
const books = ref([])
const total = ref(0)
const page = ref(1)
const size = ref(10)
const loading = ref(false)
const loadError = ref('')

async function loadBooks() {
  loading.value = true
  loadError.value = ''
  try {
    const data = await listBooks({ page: page.value, size: size.value })
    books.value = data.records
    total.value = data.total
  } catch (e) {
    loadError.value = '图书列表加载失败，请稍后重试'
  } finally {
    loading.value = false
  }
}

function goDetail(row) {
  router.push(`/books/${row.id}`)
}

onMounted(loadBooks)
</script>

<style scoped>
.pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}
</style>
