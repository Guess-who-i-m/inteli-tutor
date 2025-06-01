import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  server: {
    proxy: {
      '/api':{    // 获取路径包含了/api的请求
        target:'http://localhost:9999',     // 后台服务所在的源
        // target:'http://39.105.122.87:9999',
        changeOrigin: true,                 // 修改源
        // rewrite:(path)=>path.replace(/^\/api/,'') // 将/api替换为空字符串
      }
    }
  },
})


