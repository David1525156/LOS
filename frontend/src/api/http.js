import axios from 'axios'
import { ElMessage } from 'element-plus'

const http = axios.create({
  baseURL: '/api',
  timeout: 10000
})

// 请求拦截：自动携带管理员 token
http.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

// 响应拦截：统一处理业务错误，给出友好提示
http.interceptors.response.use(
  (response) => {
    const res = response.data
    if (res.code === 200) {
      return res.data
    }
    ElMessage.error(res.message || '请求失败')
    return Promise.reject(new Error(res.message || '请求失败'))
  },
  (error) => {
    if (error.response && error.response.status === 401) {
      ElMessage.error('未登录或登录已过期')
    } else {
      ElMessage.error('网络异常，请检查后端服务是否已启动')
    }
    return Promise.reject(error)
  }
)

export default http
