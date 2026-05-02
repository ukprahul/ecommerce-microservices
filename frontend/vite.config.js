import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/api/products': {
        target: 'http://localhost:8283',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api\/products/, '/products'),
      },
      '/api/auth': {
        target: 'http://localhost:8282',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ''),
      },
      '/api/users': {
        target: 'http://localhost:8282',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ''),
      },
      '/api/roles': {
        target: 'http://localhost:8282',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ''),
      },
    },
  },
})
