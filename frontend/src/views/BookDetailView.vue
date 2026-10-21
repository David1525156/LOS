<template>
  <div class="book-detail">
    <el-page-header @back="$router.back()" :content="book ? book.title : '图书详情'" style="margin-bottom: 24px" />

    <el-alert
      v-if="error"
      type="error"
      :title="error"
      show-icon
      :closable="false"
      style="margin-bottom: 16px"
    />

    <el-card v-else-if="book" v-loading="loading">
      <el-descriptions :column="1" border>
        <el-descriptions-item label="书名">{{ book.title }}</el-descriptions-item>
        <el-descriptions-item label="作者">{{ book.author }}</el-descriptions-item>
        <el-descriptions-item label="图书简介">{{ book.description || '暂无简介' }}</el-descriptions-item>
        <el-descriptions-item label="可借阅库存">{{ book.stock }} 本</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <div v-else v-loading="loading" style="min-height: 120px" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getBook } from '../api/book'

const props = defineProps({
  id: { type: [String, Number], required: true }
})

const book = ref(null)
const loading = ref(false)
const error = ref('')

async function loadDetail() {
  loading.value = true
  error.value = ''
  try {
    book.value = await getBook(props.id)
  } catch (e) {
    error.value = '图书详情加载失败，请稍后重试'
  } finally {
    loading.value = false
  }
}

onMounted(loadDetail)
</script>
