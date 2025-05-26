<script setup>
import { ref } from 'vue'
const userInfo = ref({
    username: 'zhangsan',
    gender: 'M',
    grade: 9,
    addr: '西大直街92号',
    phoneNum: '115211',
})

const rules = {
    nickname: [
        { required: true, message: '请输入用户昵称', trigger: 'blur' },
        {
            pattern: /^\S{2,10}$/,
            message: '昵称必须是2-10位的非空字符串',
            trigger: 'blur'
        }
    ],
    email: [
        { required: true, message: '请输入用户邮箱', trigger: 'blur' },
        { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }
    ]
}

import useUserInfoStore from '@/stores/userInfo'

const userInfoStore = useUserInfoStore()

const getUserInfo = ()=>{
    userInfo.value = userInfoStore.info
}

getUserInfo()

import { userInfoUpdateService } from '@/api/user'
import { ElMessage } from 'element-plus'
import { studentInfoUpdateService } from '@/api/student'

const modifyUserInfo = async()=>{
    // 虽然这个vue文件里只控制了username、gender、grade、addr和phoneNum等信息
    // 但由于pinia里存储了avatar，所以发送请求包含了原本avatar的数据，不用担心avatar丢失
    let result = await studentInfoUpdateService(userInfo.value)

    ElMessage.success(result.message ? result.message: "修改成功")
    
    // 修改pinia中的信息
    userInfoStore.setInfo(userInfo.value)
}

</script>


<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>基本资料</span>
            </div>
        </template>
        <el-row>
            <el-col :span="12">
                <el-form :model="userInfo" :rules="rules" label-width="100px" size="large">

                    <el-form-item label="登录名称">
                        <el-input v-model="userInfo.username" disabled></el-input>
                    </el-form-item>

                    <el-form-item label="学生性别" prop="gender">
                        <el-input v-model="userInfo.gender"></el-input>
                    </el-form-item>

                    <el-form-item label="学生年级" prop="grade">
                        <el-input v-model="userInfo.grade"></el-input>
                    </el-form-item>

                    <el-form-item label="家庭住址" prop="addr">
                        <el-input v-model="userInfo.addr"></el-input>
                    </el-form-item>

                    <el-form-item label="联系方式" prop="phone">
                        <el-input v-model="userInfo.phoneNum"></el-input>
                    </el-form-item>

                    <el-form-item>
                        <el-button type="primary" @click="modifyUserInfo">提交修改</el-button>
                    </el-form-item>

                </el-form>
            </el-col>
        </el-row>
    </el-card>
</template>