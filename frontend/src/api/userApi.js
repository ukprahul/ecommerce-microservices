import axios from 'axios'

const userApi = axios.create({ baseURL: '/api' })

export const signup = (email, password) =>
  userApi.post('/auth/signup', { email, password })

export const login = (email, password) =>
  userApi.post('/auth/login', { email, password })

export const logout = (token, userId) =>
  userApi.post('/auth/logout', { token, userId })

export const getRoles = () =>
  userApi.get('/roles')

export const createRole = (name) =>
  userApi.post('/roles', { name })

export const setUserRoles = (userId, roleIds, token) =>
  userApi.post(`/users/${userId}/roles`, { roleIds }, {
    headers: { Authorization: `Bearer ${token}` }
  })

export const getUserDetails = (userId, token) =>
  userApi.get(`/users/${userId}`, {
    headers: { Authorization: `Bearer ${token}` }
  })
