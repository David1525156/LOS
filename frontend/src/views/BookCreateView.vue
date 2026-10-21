<template>
  <div class="book-create">
    <el-page-header @back="$router.back()" content="新增图书" style="margin-bottom: 24px" />

    <el-card>
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
          <el-button type="primary" :loading="loading" @click="handleCreate">新增图书</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { createBook } from '../api/book'

const router = useRouter()
const formRef = ref(null)
const loading = ref(false)
const form = ref({ title: '', author: '', description: '', stock: 1 })

const rules = {
  title: [{ required: true, message: '请输入书名', trigger: 'blur' }],
  author: [{ required: true, message: '请输入作者', trigger: 'blur' }],
  stock: [{ required: true, message: '请输入库存', trigger: 'blur' }]
}

async function handleCreate() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  loading.value = true
  try {
    await createBook(form.value)
    ElMessage.success('新增成功')
    router.push('/')
  } catch (e) {
    // 错误提示（含后端校验信息）由 axios 拦截器统一弹出
  } finally {
    loading.value = false
  }
}
</script>
