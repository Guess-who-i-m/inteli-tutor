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

// 新增：年级选项映射表（值->标签）
const gradeOptions = [
    { value: 1, label: '小学一年级' },
    { value: 2, label: '小学二年级' },
    { value: 3, label: '小学三年级' },
    { value: 4, label: '小学四年级' },
    { value: 5, label: '小学五年级' },
    { value: 6, label: '小学六年级' },
    { value: 7, label: '初中一年级' },
    { value: 8, label: '初中二年级' },
    { value: 9, label: '初中三年级' },
    { value: 10, label: '高中一年级' },
    { value: 11, label: '高中二年级' },
    { value: 12, label: '高中三年级' }
]

// 新增：将数字年级转换为汉字标签的方法
const gradeLabel = ref('')
const getGradeLabel = (gradeValue) => {
    const option = gradeOptions.find(opt => opt.value === gradeValue)
    return option ? option.label : '未知年级'
}

const getUserInfo = () => {
    console.log("转换前的存储",userInfoStore.info)
    userInfo.value = userInfoStore.info
    console.log("转换前的userInfo", userInfo)
    userInfo.value.grade = getGradeLabel(userInfo.value.grade)
}

getUserInfo()

import { ElMessage } from 'element-plus'
import { studentInfoUpdateService } from '@/api/student'

const modifyUserInfo = async () => {
    // 虽然这个vue文件里只控制了username、gender、grade、addr和phoneNum等信息
    // 但由于pinia里存储了avatar，所以发送请求包含了原本avatar的数据，不用担心avatar丢失
    let result = await studentInfoUpdateService(userInfo.value)

    ElMessage.success(result.message ? result.message : "修改成功")

    console.log("修改过后的userInfo", userInfo)

    // 修改pinia中的信息
    userInfoStore.setInfo(userInfo.value)

    console.log("修改过后的存储", userInfoStore.info)
    
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

                    <!-- <el-form-item label="登录名称">
                        <el-input v-model="userInfo.username" disabled></el-input>
                    </el-form-item> -->

                    <!-- <el-form-item label="学生性别" prop="gender">
                        <el-input v-model="userInfo.gender"></el-input>
                    </el-form-item> -->

                    <el-form-item label="学生性别" prop="gender">
                        <el-radio-group v-model="userInfo.gender">
                            <el-radio value="M">男生</el-radio>
                            <el-radio value="F">女生</el-radio>
                        </el-radio-group>
                    </el-form-item>

                    <!-- <el-form-item label="学生年级" prop="grade">
                        <el-input v-model="userInfo.grade"></el-input>
                    </el-form-item> -->

                    <el-form-item label="学生年级">
                        <el-select v-model="userInfo.grade" placeholder="请选择您孩子的年级">
                            <el-option label="小学一年级" :value="1" />
                            <el-option label="小学二年级" :value="2" />
                            <el-option label="小学三年级" :value="3" />
                            <el-option label="小学四年级" :value="4" />
                            <el-option label="小学五年级" :value="5" />
                            <el-option label="小学六年级" :value="6" />
                            <el-option label="初中一年级" :value="7" />
                            <el-option label="初中二年级" :value="8" />
                            <el-option label="初中三年级" :value="9" />
                            <el-option label="高中一年级" :value="10" />
                            <el-option label="高中二年级" :value="11" />
                            <el-option label="高中三年级" :value="12" />
                        </el-select>
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
