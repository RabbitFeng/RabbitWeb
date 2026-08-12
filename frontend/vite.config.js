import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// 开发服务器默认跑在 5173 端口
// 所有 /api 开头的请求代理到后端 Spring Boot (application.yaml: server.port=8082)
export default defineConfig({
  plugins: [vue()],
  server: {
    port: 8091,
    proxy: {
      '/api': {
        target: 'http://127.0.0.1:8082',
        changeOrigin: true
      }
    }
  }
})
