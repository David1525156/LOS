import http from './http'

/** 管理员登录（US-03） */
export function login(data) {
  return http.post('/auth/login', data)
}
