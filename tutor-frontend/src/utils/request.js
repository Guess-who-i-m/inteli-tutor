// 导入axios库（需先安装：npm install axios）
import axios from 'axios';

// 定义基础URL
// 开发环境可以使用代理前缀'/api'，生产环境替换为实际后端地址
// const baseURL = 'http://localhost:8080'; // 本地开发后端地址，这里的修改配置被挪到了vite.config.js中
const baseURL = '/api'; // 使用代理前缀（需配合vite或webpack代理配置）

// 创建axios实例
const instance = axios.create({ baseURL });

// 导入Element Plus的消息提示组件
import { ElMessage } from 'element-plus';

// 导入Pinia的token存储store
import { useTokenStore } from '@/stores/token.js';

// 添加请求拦截器
instance.interceptors.request.use(
    (config) => {
        // 请求发送前的处理逻辑
        
        // 从Pinia store中获取token
        const tokenStore = useTokenStore();
        
        // 如果token存在，将其添加到请求头Authorization中
        if (tokenStore.token) {
            config.headers.Authorization = tokenStore.token;
        }
        
        return config; // 返回处理后的配置
    },
    (err) => {
        // 请求错误处理（通常发生在请求发送前）
        return Promise.reject(err); // 将错误传递给catch处理
    }
);

// 导入路由实例（用于未登录跳转）
import router from '@/router';

// 添加响应拦截器
instance.interceptors.response.use(
    (result) => {
        // 成功响应处理（2xx状态码）
        
        // 检查业务状态码（假设0表示成功）
        if (result.data.code === 1) {
            // 业务成功，直接返回响应数据
            return result.data;
        }
        
        console.log(result.data.message)

        // 业务失败，显示错误消息
        ElMessage.error(result.data.msg ? result.data.msg : '服务异常');
        
        // 返回拒绝的Promise，触发catch处理
        return Promise.reject(result.data);
    },
    (err) => {
        // 响应错误处理（非2xx状态码）
        console.log(err);
        if (err.response.status === 401) {
            // 401未授权/未登录
            ElMessage.error('请先登录');
            // 跳转到登录页
            router.push('/login');
        } else {
            // 其他错误
            ElMessage.error("服务异常");
        }
        
        // 返回拒绝的Promise，触发catch处理
        return Promise.reject(err);
    }
);

// 导出配置好的axios实例
export default instance;
