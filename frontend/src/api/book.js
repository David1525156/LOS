import http from './http'

/** 分页获取图书列表（US-01） */
export function listBooks(params) {
  return http.get('/books', { params })
}

/** 获取图书详情（US-02） */
export function getBook(id) {
  return http.get(`/books/${id}`)
}

/** 新增图书（US-03，需管理员登录） */
export function createBook(data) {
  return http.post('/books', data)
}

/** 修改图书（需管理员登录） */
export function updateBook(id, data) {
  return http.put(`/books/${id}`, data)
}

/** 删除图书（需管理员登录） */
export function deleteBook(id) {
  return http.delete(`/books/${id}`)
}
