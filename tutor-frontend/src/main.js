// 导入全局样式文件（SCSS格式）
// 这里通常包含应用的全局样式、变量定义和第三方样式重置等
import './assets/main.scss'

// 导入Vue核心功能
import { createApp } from 'vue'

// 导入Element Plus UI库及其样式
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

// 导入根组件
import App from './App.vue'

// 导入路由配置
import router from '@/router'

// 导入Pinia状态管理库及其插件
import { createPinia } from 'pinia'
import { createPersistedState } from 'pinia-persistedstate-plugin'

// 导入Element Plus的中文语言包
import locale from 'element-plus/dist/locale/zh-cn.js'


const app = createApp(App)                      // 创建Vue应用实例

const pinia = createPinia();                    // 创建Pinia状态管理实例
const persist = createPersistedState();         // 创建Pinia持久化存储插件实例
pinia.use(persist)                              // 使用Pinia持久化插件，这会让Pinia的状态在页面刷新后仍然保持
app.use(pinia)                                  // 在Vue应用中使用Pinia

app.use(ElementPlus, { locale })                // 在Vue应用中使用Element Plus，并配置中文语言包

app.use(router)                                 // 在Vue应用中使用路由

app.mount('#app')                               // 将Vue应用挂载到DOM中的#app元素上
