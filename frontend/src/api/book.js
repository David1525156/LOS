import http from './http'

/** 分页获取图书列表（US-01） */
export function listBooks(params) {
  return http.get('/books', { params })
}

/** 获取图书详情（US-02） */
export function getBook(id) {
  return http.get(`/books/${id}`)
}
