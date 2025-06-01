<script setup>
import { User, Lock, Search } from '@element-plus/icons-vue'
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router' 
import {useTokenStore} from '@/stores/token.js'
import {useUserTypeStore} from '@/stores/userType.js'



// 控制注册与登录表单的显示， 默认显示注册
const isRegister = ref(false)
// 控制身份，学生或者老师
const type = ref('')

const router = useRouter()

// 定义数据模型
const registerData = ref({
    username:'',
    password:'',
    rePassword:''
})

// 校验密码的函数
const checkRePassword = (rule, value, callback)=>{
    if (value === ''){
        callback(new Error('请再次确认密码'))
    } else if (value !== registerData.value.password){
        callback(new Error('两次密码输入不一致'))
    } else {
        callback()
    }
}

// 定义表单校验规则
const rules={
    username:[
        {required:true, message:"请输入用户名",trigger:'blur'},
        {min:4, max:16, message:"用户名长度需要介于4到16之间", trigger:'blur'},
    ],
    password:[
    {required:true, message:"请输入密码",trigger:'blur'},
    {min:4, max:16, message:"密码长度需要介于5到16之间", trigger:'blur'},
    ],
    rePassword:[
        {validator:checkRePassword, trigger:  'blur'},
    ]
}

import { teacherRegisterService, teacherLoginService } from '@/api/teacher'
import { studentRegisterService, studentLoginService } from '@/api/student'

// 提供register函数，调用api/user.js中的方法
const register = async()=>{

    let result;

    if(type.value === 'teacher'){
        result = await teacherRegisterService(registerData.value);
    } else if(type.value === 'student'){
        result = await studentRegisterService(registerData.value);
    }

    ElMessage.success(result.message?result.message:'注册成功')
}

// 绑定登录数据，可以复用注册时的数据
// 表单数据校验，同样复用注册的规则

const tokenStore = useTokenStore()
const userTypeStore = useUserTypeStore()

const login = async()=>{

    let result;

    if(type.value === 'teacher'){
        // 以家教老师身份登录
        result = await teacherLoginService(registerData.value);
        userTypeStore.setType('teacher');
        

    } else if(type.value === 'student'){
        // 以学生身份登录
        result = await studentLoginService(registerData.value);
        userTypeStore.setType('student');
    }

    // alert(result.msg?result.msg:'登录成功');
    ElMessage.success(result.message?result.message:'登录成功')
    // 把得到的token存储到pinia中
    tokenStore.setToken(result.data)
    // 跳转到首页，借助路由完成跳转
    router.push('/')
}


// 定义一个函数，用来清空数据模型的数据
const clearRegisterData = ()=>{
    type.value='';
    registerData.value={
        username : '',
        password : '',
        rePassword : ''
    }
}
</script>

<template>
    <el-row class="login-page">
        <el-col :span="12" class="bg"></el-col>
        <el-col :span="6" :offset="3" class="form">

            <!-- 注册表单 -->
            <el-form ref="form" size="large" autocomplete="off" v-if="isRegister" :model="registerData" :rules="rules">
                <el-form-item>
                    <h1>注册</h1>
                </el-form-item>

                <el-form-item>
                    <el-select v-model="type" placeholder="请选择您的角色">
                        <template #prefix>
                        <el-icon><Search /></el-icon> <!-- 使用 Element Plus 的图标组件 -->
                        </template>
                        <el-option label="家教老师" value="teacher" />
                        <el-option label="家长学生" value="student" />
                    </el-select>
                </el-form-item>

                <el-form-item prop="username">
                    <el-input :prefix-icon="User" placeholder="请输入用户名" v-model="registerData.username"></el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input :prefix-icon="Lock" type="password" placeholder="请输入密码" v-model="registerData.password"></el-input>
                </el-form-item>
                <el-form-item prop="rePassword">
                    <el-input :prefix-icon="Lock" type="password" placeholder="请输入再次密码" v-model="registerData.rePassword"></el-input>
                </el-form-item>
                <!-- 注册按钮 -->
                <el-form-item>
                    <el-button class="button" type="primary" auto-insert-space @click="register">
                        注册
                    </el-button>
                </el-form-item>
                <el-form-item class="flex">
                    <el-link type="info" :underline="false" @click="isRegister = false; clearRegisterData()">
                        ← 返回
                    </el-link>
                </el-form-item>
            </el-form>

            <!-- 登录表单 -->
            <el-form ref="form" size="large" autocomplete="off" v-else :model="registerData" :rules="rules">
                <el-form-item>
                    <h1>登录</h1>
                </el-form-item>

                <!-- 选择角色 -->
                <el-form-item>
                    <el-select v-model="type" placeholder="请选择您的角色">
                        <template #prefix>
                        <el-icon><Search /></el-icon> <!-- 使用 Element Plus 的图标组件 -->
                        </template>
                        <el-option label="家教老师" value="teacher" />
                        <el-option label="家长学生" value="student" />
                    </el-select>
                </el-form-item>

                <!-- 用户名 -->
                <el-form-item prop="username">
                    <el-input :prefix-icon="User" placeholder="请输入用户名" v-model="registerData.username"></el-input>
                </el-form-item>

                <!-- 密码 -->
                <el-form-item prop="password">
                    <el-input name="password" :prefix-icon="Lock" type="password" placeholder="请输入密码" v-model="registerData.password"></el-input>
                </el-form-item>

                <!-- 记住我和忘记密码 -->
                <el-form-item class="flex">
                    <div class="flex">
                        <el-checkbox>记住我</el-checkbox>
                        <el-link type="primary" :underline="false">忘记密码？</el-link>
                    </div>
                </el-form-item>

                <!-- 登录按钮 -->
                <el-form-item>
                    <el-button class="button" type="primary" auto-insert-space @click="login">登录</el-button>
                </el-form-item>
                <el-form-item class="flex">
                    <el-link type="info" :underline="false" @click="isRegister = true; clearRegisterData()">
                        注册 →
                    </el-link>
                </el-form-item>
            </el-form>
        </el-col>
    </el-row>
</template>

<style lang="scss" scoped>
/* 样式 */
.login-page {
    height: 100vh;
    background-color: #fff;

    .bg {
        background: url('@/assets/图片2.png') no-repeat 60% center / 480px auto,
            url('@/assets/login_bg.jpg') no-repeat center / cover;
        border-radius: 0 20px 20px 0;
    }

    .form {
        display: flex;
        flex-direction: column;
        justify-content: center;
        user-select: none;

        .title {
            margin: 0 auto;
        }

        .button {
            width: 100%;
        }

        .flex {
            width: 100%;
            display: flex;
            justify-content: space-between;
        }
    }
}
</style>