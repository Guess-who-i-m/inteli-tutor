<script setup>
import { Plus, Upload } from '@element-plus/icons-vue'
import {ref} from 'vue'
import avatar from '@/assets/default.png'
const uploadRef = ref()

import { useTokenStore } from '@/stores/token'
const tokenStore = useTokenStore()

import useUserInfoStore from '@/stores/userInfo'
const userInfoStore = useUserInfoStore()

//用户头像地址
const imgUrl= ref(userInfoStore.info.avatar)

// 图片上传成功回调函数
const uploadSuccess = (result)=>{
    imgUrl.value = result.data;
}

import { studentAvatarUpdateService } from '@/api/student'
import { teacherAvatarUpdateService } from '@/api/teacher'
import { ElMessage } from 'element-plus'
import { useUserTypeStore } from '@/stores/userType'

const userTypeStore = useUserTypeStore();
const updateAvatar = async()=>{
    // 构造参数
    const requestData = {
        avatar: imgUrl.value
    }

    // 调用接口
    let result
    if (userTypeStore.type === "student"){
        result = await studentAvatarUpdateService(requestData);
    } else if (userTypeStore.type === "teacher"){
        result = await teacherAvatarUpdateService(requestData);
    }

    ElMessage.success(result.message? result.message : "修改成功")

    // 修改pinia中的数据
    userInfoStore.info.avatar = imgUrl.value
}

</script>

<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>更换头像</span>
            </div>
        </template>
        <el-row>
            <el-col :span="12">
                <el-upload 
                    ref="uploadRef"
                    class="avatar-uploader" 
                    :show-file-list="false"
                    :auto-upload="true"
                    action="/api/file/upload"
                    name="file"
                    :headers="{'Authorization':tokenStore.token}"
                    :on-success="uploadSuccess"
                    >
                    <img v-if="imgUrl" :src="imgUrl" class="avatar" />
                    <img v-else :src="avatar" width="278" />
                </el-upload>
                <br />
                <el-button type="primary" :icon="Plus" size="large"  @click="uploadRef.$el.querySelector('input').click()">
                    选择图片
                </el-button>
                <el-button type="success" :icon="Upload" size="large" @click="updateAvatar">
                    上传头像
                </el-button>
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
