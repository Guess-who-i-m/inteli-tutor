<script setup>
import {
    Management,
    Promotion,
    UserFilled,
    User,
    Crop,
    EditPen,
    SwitchButton,
    CaretBottom
} from '@element-plus/icons-vue'

import avatar from '@/assets/default.png'

import { userInfoService } from '@/api/user.js';
import { teacherInfoService } from '@/api/teacher.js'
import { studentInfoService } from '@/api/student.js'
import { ElMessage } from 'element-plus';
import useUserInfoStore from '@/stores/userInfo.js';

const userInfoStore = useUserInfoStore();

import { ElMessageBox } from 'element-plus';
import { useTokenStore } from '@/stores/token';
import { useUserTypeStore } from '@/stores/userType';
import { computed } from 'vue';

const tokenStore = useTokenStore()
const userTypeStore = useUserTypeStore()

// 判断当前用户是学生还是老师
const isTeacher = computed(() => userTypeStore.type === 'teacher');
const isStudent = computed(() => userTypeStore.type === 'student');


const getUserInfo = async()=>{
    // 从后端获取对应的信息
    let result;

    if (isStudent.value){
        console.log("学生模式")
        result = await studentInfoService();
    } else if (isTeacher.value){
        console.log("教师模式")
        result = await teacherInfoService();
    }
    
    // 将数据存储到pinia中
    console.log(result.data);
    userInfoStore.setInfo(result.data);
}

getUserInfo()

import { useRouter } from 'vue-router';
const router = useRouter()

// 条目被点击后所调用的函数
const handleCommand = (command)=>{
    // 判断指令
    if (command === 'logout'){
        // 退出登录
        deleteCategory()
    } else {
        // 路由
        if (command === 'avatar'){

            router.push('/user/'+ command);

        } else if (command === 'info') {

            if(userTypeStore.type === "student"){

                router.push('/student/studentInfo');

            } else if(userTypeStore.type === "teacher"){

                router.push('/teacher/teacherInfo');

            }
        } else {
            // 看后续要不要改进
            if(userTypeStore.type === "student"){

                router.push('/user/' + command);

            } else if(userTypeStore.type === "teacher"){

                router.push('/user/' + command);

            }
        }
    }
}




// 删除分类
const deleteCategory = () => {
    // 提示用户 展示确认框
    ElMessageBox.confirm(
        '确认要退出登录？',
        '温馨提示',
        {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning',
        }
    )
    .then(async() => {
        // 退出登录
        // 1. 删除token、type和个人信息
        tokenStore.removeToken()
        userInfoStore.removeInfo()
        userTypeStore.removeType()
        

        // 2. 路由跳转到登录界面
        router.push('/login')

        ElMessage({
            type: 'success',
            message: '退出登录成功',
        })
    })
    .catch(() => {
        ElMessage({
            type: 'info',
            message: '退出登录取消',
        })
    })
}

</script>

<template>
    <!-- element-plus里面的容器 -->
    <el-container class="layout-container">
        <!-- 左侧菜单 -->
        <el-aside width="200px">
            <div class="el-aside__logo"></div>
            <!-- element-plus的菜单标签 -->
            <el-menu active-text-color="#ffd04b" background-color="#232323"  text-color="#fff" router>
                <!-- 只有家长/学生才能看到的界面 -->
                <template v-if="isStudent">
                    <!-- 家教应聘信息列表 -->
                    <el-menu-item index="/student/recommendList">
                        <el-icon>
                            <Management />
                        </el-icon>
                        <span>家教应聘信息</span>
                    </el-menu-item>
                    <!-- 家教招聘信息发布 -->
                    <el-menu-item index="/student/sendRecruit">
                        <el-icon>
                            <Promotion />
                        </el-icon>
                        <span>招聘信息发布</span>
                    </el-menu-item>
                    <!-- 个人中心 -->
                    <el-sub-menu >

                        <template #title>
                            <el-icon>
                                <UserFilled />
                            </el-icon>
                            <span>个人中心</span>
                        </template>

                        <el-menu-item index="/student/studentInfo">
                            <el-icon>
                                <User />
                            </el-icon>
                            <span>学生信息</span>
                        </el-menu-item>

                        <el-menu-item index="/user/avatar">
                            <el-icon>
                                <Crop />
                            </el-icon>
                            <span>更换头像</span>
                        </el-menu-item>

                        <el-menu-item index="/user/resetPassword">
                            <el-icon>
                                <EditPen />
                            </el-icon>
                            <span>重置密码</span>
                        </el-menu-item>

                    </el-sub-menu>
                </template>

                <!-- 只有家教老师才能看得到的界面 -->
                <template v-if="isTeacher">

                    <!-- 家教招聘信息列表 -->
                    <el-menu-item index="/teacher/recruitList">
                        <el-icon>
                            <Management />
                        </el-icon>
                        <span>家教招聘信息</span>
                    </el-menu-item>

                    <!-- 家教应聘信息发布 -->
                    <el-menu-item index="/teacher/sendRecommend">
                        <el-icon>
                            <Promotion />
                        </el-icon>
                        <span>应聘信息发布</span>
                    </el-menu-item>

                    <!-- 个人中心 -->
                    <el-sub-menu >

                        <template #title>
                            <el-icon>
                                <UserFilled />
                            </el-icon>
                            <span>个人中心</span>
                        </template>

                        <el-menu-item index="/teacher/teacherInfo">
                            <el-icon>
                                <User />
                            </el-icon>
                            <span>教师信息</span>
                        </el-menu-item>

                        <el-menu-item index="/user/avatar">
                            <el-icon>
                                <Crop />
                            </el-icon>
                            <span>更换头像</span>
                        </el-menu-item>

                        <el-menu-item index="/user/resetPassword">
                            <el-icon>
                                <EditPen />
                            </el-icon>
                            <span>重置密码</span>
                        </el-menu-item>

                    </el-sub-menu>
                </template>
            </el-menu>
        </el-aside>
        <!-- 右侧主区域 -->
        <el-container>
            <!-- 头部区域 -->
            <el-header>
                <template v-if="isTeacher">
                    <div style="font-size: 28px; font-weight: bold;">
                        <strong>慧辅导 - AI智能家教匹配与规划平台 - 教师端</strong>
                    </div>
                </template>

                <template v-if="isStudent">
                    <div style="font-size: 28px; font-weight: bold;">
                        <strong>慧辅导 - AI智能家教匹配与规划平台 - 学生端</strong>
                    </div>
                </template>

                <!-- 下拉菜单 -->
                <!-- command:条目点击后被触发，在事件函数上可以声明一个参数，
                 这个参数就是下拉裁断里的每一个item的command属性。接收条目对应的指令 -->
                <el-dropdown placement="bottom-end" @command="handleCommand">
                    <span class="el-dropdown__box">
                        <el-avatar :src="userInfoStore.info.avatar ? userInfoStore.info.avatar :avatar" />
                        <el-icon>
                            <CaretBottom />
                        </el-icon>
                    </span>
                    <template #dropdown>
                        <el-dropdown-menu>
                            <el-dropdown-item command="info" :icon="User">基本资料</el-dropdown-item>
                            <el-dropdown-item command="avatar" :icon="Crop">更换头像</el-dropdown-item>
                            <el-dropdown-item command="resetPassword" :icon="EditPen">重置密码</el-dropdown-item>
                            <el-dropdown-item command="logout" :icon="SwitchButton">退出登录</el-dropdown-item>
                        </el-dropdown-menu>
                    </template>
                </el-dropdown>
            </el-header>
            <!-- 中间区域 -->
            <el-main>
                <!-- <div style="width: 1290px; height: 570px;border: 1px solid red;">
                    内容展示区
                </div> -->
                <router-view></router-view>
            </el-main>
            <!-- 底部区域 -->
            <!-- <el-footer>大事件 ©2023 Created by 黑马程序员</el-footer> -->
        </el-container>
    </el-container>
</template>

<style lang="scss" scoped>
.layout-container {
    height: 100vh;

    .el-aside {
        background-color: #232323;

        &__logo {
            height: 120px;
            background: url('@/assets/logo.png') no-repeat center / 120px auto;
        }

        .el-menu {
            border-right: none;
        }
    }

    .el-header {
        background-color: #fff;
        display: flex;
        align-items: center;
        justify-content: space-between;

        .el-dropdown__box {
            display: flex;
            align-items: center;

            .el-icon {
                color: #999;
                margin-left: 10px;
            }

            &:active,
            &:focus {
                outline: none;
            }
        }
    }

    /*.el-footer {
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 14px;
        color: #666;
    }*/
}
</style>