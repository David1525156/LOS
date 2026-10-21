<template>
  <div class="book-edit">
    <el-page-header @back="$router.back()" content="编辑图书" style="margin-bottom: 24px" />

    <el-alert
      v-if="error"
      type="error"
      :title="error"
      show-icon
      :closable="false"
      style="margin-bottom: 16px"
    />

    <el-card v-else v-loading="loading">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px" style="max-width: 480px">
        <el-form-item label="书名" prop="title">
          <el-input v-model="form.title" placeholder="请输入书名" />
        </el-form-item>
        <el-form-item label="作者" prop="author">
          <el-input v-model="form.author" placeholder="请输入作者" />
        </el-form-item>
        <el-form-item label="简介" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入图书简介（可选）" />
        </el-form-item>
        <el-form-item label="库存" prop="stock">
          <el-input-number v-model="form.stock" :min="0" :precision="0" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="saving" @click="handleSave">保存</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getBook, updateBook } from '../api/book'

const props = defineProps({
  id: { type: [String, Number], required: true }
})

const router = useRouter()
const formRef = ref(null)
const loading = ref(false)
const saving = ref(false)
const error = ref('')
const form = ref({ title: '', author: '', description: '', stock: 0 })

const rules = {
  title: [{ required: true, message: '请输入书名', trigger: 'blur' }],
  author: [{ required: true, message: '请输入作者', trigger: 'blur' }],
  stock: [{ required: true, message: '请输入库存', trigger: 'blur' }]
}

async function loadBook() {
  loading.value = true
  try {
    const book = await getBook(props.id)
    form.value = {
      title: book.title,
      author: book.author,
      description: book.description || '',
      stock: book.stock
    }
  } catch (e) {
    error.value = '图书信息加载失败，请稍后重试'
  } finally {
    loading.value = false
  }
}

async function handleSave() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  saving.value = true
  try {
    await updateBook(props.id, form.value)
    ElMessage.success('保存成功')
    router.push('/')
  } catch (e) {
    // 错误提示由 axios 拦截器统一弹出
  } finally {
    saving.value = false
  }
}

onMounted(loadBook)
</script>
