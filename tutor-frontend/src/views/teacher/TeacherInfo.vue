<!-- <script lang="ts" setup>
import { reactive } from 'vue' -->
<script setup>
import { ref } from 'vue'
const userInfo = ref({
    username: 'zhangsan',
    gender: 'M',
    school: '哈尔滨工业大学',
    schLevel: 'jbw',
    eduBg: 'bachelor',
    phoneNum: '24223414'
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

const getUserInfo = () => {
    // 直接赋值对象属性，不需要 .value
    // Object.assign(userInfo, userInfoStore.info)
    userInfo.value = userInfoStore.info
}

getUserInfo()

import { ElMessage } from 'element-plus'
import { teacherInfoUpdateService } from '@/api/teacher'

const modifyUserInfo = async () => {
    // 虽然这个vue文件里只控制了username、gender、grade、addr和phoneNum等信息
    // 但由于pinia里存储了avatar，所以发送请求包含了原本avatar的数据，不用担心avatar丢失
    let result = await teacherInfoUpdateService(userInfo.value)

    ElMessage.success(result.message ? result.message : "修改成功")

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

                    <!-- <el-form-item label="教师性别" prop="gender">
                        <el-input v-model="userInfo.gender"></el-input>
                    </el-form-item> -->

                    <el-form-item label="教师性别" prop="gender">
                        <el-radio-group v-model="userInfo.gender">
                            <el-radio value="M">男生</el-radio>
                            <el-radio value="F">女生</el-radio>
                        </el-radio-group>
                    </el-form-item>

                    <el-form-item label="就读学校" prop="school">
                        <el-input v-model="userInfo.school"></el-input>
                    </el-form-item>

                    <!-- <el-form-item label="学校层次" prop="schLevel">
                        <el-input v-model="userInfo.schLevel"></el-input>
                    </el-form-item> -->

                    <el-form-item label="学校层次" label-position="eb" prop="schLevel">
                        <el-radio-group v-model="userInfo.schLevel" aria-label="label position">
                            <el-radio-button value="jbw">985</el-radio-button>
                            <el-radio-button value="eyy">211</el-radio-button>
                            <el-radio-button value="syl">双一流</el-radio-button>
                            <el-radio-button value="yb">一本</el-radio-button>
                            <el-radio-button value="eb">二本</el-radio-button>
                        </el-radio-group>
                    </el-form-item>

                    <!-- <el-form-item label="学历层次" prop="eduBg">
                        <el-input v-model="userInfo.eduBg"></el-input>
                    </el-form-item> -->

                    <el-form-item label="学历层次" label-position="master" prop="eduBg">
                        <el-radio-group v-model="userInfo.eduBg" aria-label="label position">
                            <el-radio-button value="bachelor">本科</el-radio-button>
                            <el-radio-button value="master">硕士</el-radio-button>
                            <el-radio-button value="doctor">博士</el-radio-button>
                        </el-radio-group>
                    </el-form-item>

                    <el-form-item label="联系方式" prop="phoneNum">
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

<style lang="scss" scoped>
.avatar-uploader {
    :deep() {
        .avatar {
            width: 278px;
            height: 278px;
            display: block;
        }

        .el-upload {
            border: 1px dashed var(--el-border-color);
            border-radius: 6px;
            cursor: pointer;
            position: relative;
            overflow: hidden;
            transition: var(--el-transition-duration-fast);
        }

        .el-upload:hover {
            border-color: var(--el-color-primary);
        }

        .el-icon.avatar-uploader-icon {
            font-size: 28px;
            color: #8c939d;
            width: 278px;
            height: 278px;
            text-align: center;
        }
    }
}

.page-container {
    min-height: 100%;
    box-sizing: border-box;

    .header {
        display: flex;
        align-items: center;
        justify-content: space-between;
    }
}

</style>
