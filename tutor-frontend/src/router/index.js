// 从 vue-router 库中导入创建路由和创建Web历史记录的函数
import { createRouter, createWebHistory } from 'vue-router'

// 导入各个路由组件
import LayoutVue from '@/views/Layout.vue'

import UserAvatarVue from '@/views/user/UserAvatar.vue'
import UserResetPasswordVue from '@/views/user/UserResetPassword.vue'

import RecommendListVue from '@/views/student/RecommendList.vue'
import SendRecruitVue from '@/views/student/SendRecruit.vue'
import StudentInfoVue from '@/views/student/StudentInfo.vue'

import RecruitListVue from '@/views/teacher/RecruitList.vue'
import SendRecommendVue from '@/views/teacher/SendRecommend.vue'
import TeacherInfoVue from '@/views/teacher/TeacherInfo.vue'

import LlmSearchTchVue from '@/views/llm/LlmSearchTchVue.vue'
import LlmStuInstructorVue from '@/views/llm/LlmStuInstructor.vue'
import LlmSearchStuVue from '@/views/llm/LlmSearchStu.vue'

// 定义路由配置数组
const routes = [

    // 1. 登录路由
    // 静态导入写法（已注释掉）:
    // { path: '/login', component: LoginVue },
    
    // 动态导入写法（推荐）:
    // 使用懒加载，只有在访问该路由时才会加载对应组件
    // 有助于优化应用初始加载性能
    { path: '/login', component: () => import("@/views/Login.vue") },
    
    // 2. 主布局路由
    {
        path: '/',          // 根路径
        component: LayoutVue, // 布局组件
        // redirect: '/article/manage', // 默认重定向到文章管理页
        children: [         // 嵌套路由（子路由）

            { path: '/student/recommendList', component: RecommendListVue},
            { path: '/student/sendRecruit', component: SendRecruitVue},
            { path: '/student/studentInfo', component: StudentInfoVue},
            { path: '/student/llmSearchTch', component: LlmSearchTchVue},
            { path: '/student/llmStuInstructor', component: LlmStuInstructorVue},

            { path: '/teacher/recruitList', component: RecruitListVue},
            { path: '/teacher/sendRecommend', component: SendRecommendVue},
            { path: '/teacher/teacherInfo', component: TeacherInfoVue},
            { path: '/teacher/llmSearchStu', component: LlmSearchStuVue},
            
            // 用户信息相关路由
            { path: '/user/avatar', component: UserAvatarVue },       // 用户头像
            { path: '/user/resetPassword', component: UserResetPasswordVue } // 重置密码
        ]
    }
]

// 创建路由实例
const router = createRouter({
    history: createWebHistory(), // 使用HTML5 History模式（去掉URL中的#）
    routes: routes               // 传入路由配置
})

// 获取用户类型的 Pinia Store
import { useUserTypeStore } from '@/stores/userType'

// 动态重定向逻辑
router.beforeEach((to, from, next) => {
  const userTypeStore = useUserTypeStore()
  
  if (to.path === '/') {
    if (userTypeStore.type === 'teacher') {
        // 老师就重定向到招聘信息列表
        next('/teacher/recruitList')
    } else if (userTypeStore.type === 'student') {
        // 学生重定向到应聘信息列表
        next('/student/recommendList')
    } else {
        // 其它情况就直接登录
        next('/login')
    }
  } else {
    next()
  }
})

// 导出路由实例，以便在main.js中挂载到Vue应用
export default router
