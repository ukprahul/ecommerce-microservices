import axios from 'axios'

const productApi = axios.create({ baseURL: '/api/products' })

const authHeader = (token) => ({ headers: { Authorization: `Bearer ${token}` } })

export const getAllProducts = () =>
  productApi.get('/')

export const getProduct = (id) =>
  productApi.get(`/${id}`)

export const getCategories = () =>
  productApi.get('/categories')

export const getProductsByCategory = (category) =>
  productApi.get(`/category/${category}`)

export const createProduct = (data, token) =>
  productApi.post('/', data, authHeader(token))

export const updateProduct = (id, data, token) =>
  productApi.put(`/${id}`, data, authHeader(token))

export const deleteProduct = (id, token) =>
  productApi.delete(`/${id}`, authHeader(token))
