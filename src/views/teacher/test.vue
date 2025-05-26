 <template>
    <el-form :model="form" label-width="auto" style="max-width: 600px">
      
        <el-form-item label="Activity name">
        <el-input v-model="form.name" />
      </el-form-item>

      <el-form-item label="Activity zone">
        <el-select v-model="form.region" placeholder="please select your zone">
          <el-option label="Zone one" value="shanghai" />
          <el-option label="Zone two" value="beijing" />
        </el-select>
      </el-form-item>
      
      <el-form-item label="Activity time">
        <el-col :span="11">
          <el-date-picker
            v-model="form.date1"
            type="date"
            placeholder="Pick a date"
            style="width: 100%"
          />
        </el-col>
        <el-col :span="2" class="text-center">
          <span class="text-gray-500">-</span>
        </el-col>
        <el-col :span="11">
          <el-time-picker
            v-model="form.date2"
            placeholder="Pick a time"
            style="width: 100%"
          />
        </el-col>
      </el-form-item>
      
      <el-form-item label="Instant delivery">
        <el-switch v-model="form.delivery" />
      </el-form-item>
      
      <el-form-item label="Activity type">
        <el-checkbox-group v-model="form.type">
          <el-checkbox value="Online activities" name="type">
            Online activities
          </el-checkbox>
          <el-checkbox value="Promotion activities" name="type">
            Promotion activities
          </el-checkbox>
          <el-checkbox value="Offline activities" name="type">
            Offline activities
          </el-checkbox>
          <el-checkbox value="Simple brand exposure" name="type">
            Simple brand exposure
          </el-checkbox>
        </el-checkbox-group>
      </el-form-item>
      
      <el-form-item label="Resources">
        <el-radio-group v-model="form.resource">
          <el-radio value="Sponsor">Sponsor</el-radio>
          <el-radio value="Venue">Venue</el-radio>
        </el-radio-group>
      </el-form-item>
      
      <el-form-item label="Activity form">
        <el-input v-model="form.desc" type="textarea" />
      </el-form-item>
      
      <el-form-item>
        <el-button type="primary" @click="onSubmit">Create</el-button>
        <el-button>Cancel</el-button>
      </el-form-item>
    
    </el-form>
  </template>
  
  <script lang="ts" setup>
  import { reactive } from 'vue'
  
  // do not use same name with ref
  const form = reactive({
    name: '',
    region: '',
    date1: '',
    date2: '',
    delivery: false,
    type: [],
    resource: '',
    desc: '',
  })
  
  const onSubmit = () => {
    console.log('submit!')
  }
  </script>


<!-- <script setup>
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
    userInfo.value = userInfoStore.info
}

getUserInfo()

import { userInfoUpdateService } from '@/api/user'
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

                    <el-form-item label="教师性别" prop="gender">
                        <el-input v-model="userInfo.gender"></el-input>
                    </el-form-item>

                    <el-form-item label="就读学校" prop="school">
                        <el-input v-model="userInfo.school"></el-input>
                    </el-form-item>

                    <el-form-item label="学校层次" prop="schLevel">
                        <el-input v-model="userInfo.schLevel"></el-input>
                    </el-form-item>

                    <el-form-item label="学历层次" prop="eduBg">
                        <el-input v-model="userInfo.eduBg"></el-input>
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
</template> -->




 