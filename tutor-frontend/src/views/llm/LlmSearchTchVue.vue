<script setup>

import {Message, Star} from '@element-plus/icons-vue'

/*学生端-家教应聘信息一览*/
import { ElMessage } from 'element-plus'
import { ref } from 'vue'

// 添加加载状态变量
const loading = ref(false) // 控制加载状态

// 展示细节弹窗
const detailVisible = ref(false)
const selectedDetail = ref('') 

// 新增：显示详情的方法
const showDetail = (detail) => {
    selectedDetail.value = detail
    detailVisible.value = true
}

// 新增：LLM相关变量
const llmPrompt = ref('') // LLM提问输入
const llmReason = ref('LLM将会根据您的需求进行推荐') // LLM回答的原因

// 初始推荐列表为空
const recommends = ref([])

// 普通推荐列表
const recommendList = async () => {
    // 重置LLM状态
    recommends.value = [];
};

import { llmRecommendTeacherService } from '@/api/llm'

// 调用LLM推荐
const callLLMRecommend = async () => {
    if (!llmPrompt.value.trim()) {
        ElMessage.warning('请输入您的需求描述');
        return;
    }

    // 设置加载状态
    loading.value = true;
    
    try {
        const result = await llmRecommendTeacherService({ prompt: llmPrompt.value });

        console.log(result)
        
        if (result.code === 1) {
            // 处理返回的原因
            llmReason.value = result.map?.reason || '已为您找到最匹配的老师！';

            console.log(result.data)
            
            // 处理返回的推荐数据
            recommends.value = result.data.map(item => {
                return {
                    recommendId: item.recommendId,
                    subject: item.subject,
                    online: item.online ? '线上' : '线下',
                    price: String(item.price),
                    time: '请在列表界面查询明细或者直接与老师沟通', // LLM返回的数据可能没有时间
                    detail: item.detail,
                    tchId: item.tchId,
                    originalDates: [] // LLM返回的数据可能没有时间安排
                };
            });
            if (recommends.value.length === 0) {
                 ElMessage.info('没有找到完全匹配的老师，请尝试优化您的描述');
            }
        } else {
            ElMessage.error(result.msg || '智能推荐失败');
        }
    } catch (error) {
        ElMessage.error('智能推荐服务请求失败');
        console.error(error);
    } finally {
        // 无论成功失败都关闭加载状态
        loading.value = false;
    }
};

const tchInfoVisible = ref(false)
const tchInfoModel = ref({
    username: 'zhangsan',
    gender: 'M',
    school: '哈尔滨工业大学',
    schLevel: 'jbw',
    eduBg: 'bachelor',
    phoneNum: '24223414'
})
import { teacherInfoFromIdService } from '@/api/teacher'

const getTchInfo = async (row) => {
    try {
        let result = await teacherInfoFromIdService(row.tchId);
        const schLevelMap = { jbw: '985', eyy: '211', syl: '双一流', yb: '一本', eb: '二本' };
        const genderMap = { F: '女', M: '男' };
        const eduBgMap = { bachelor: '本科', master: '硕士', doctor: '博士' };
        
        tchInfoModel.value.gender = genderMap[result.data.gender]
        tchInfoModel.value.school = result.data.school
        tchInfoModel.value.schLevel = schLevelMap[result.data.schLevel]
        tchInfoModel.value.eduBg = eduBgMap[result.data.eduBg]
        tchInfoModel.value.phoneNum = result.data.phoneNum
        
        tchInfoVisible.value = true;
    } catch (error) {
        ElMessage.error('获取教师信息失败');
        console.error(error);
    }
}
</script>

<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>DeepSeek为您推荐家教老师</span>
            </div>
        </template>
        <!-- LLM智能推荐区域 -->
        <div class="llm-recommend">
            <el-input
                v-model="llmPrompt"
                type="textarea"
                :rows="3"
                placeholder="请描述您的需求，例如：孩子目前的学习情况、薄弱科目、期望的教师特点等"
                clearable
            ></el-input>
            <div style="margin-top: 10px; text-align: right">
                <el-button type="primary" @click="callLLMRecommend" :loading="loading">智能推荐</el-button>
                <el-button @click="llmPrompt = ''; recommendList();">重置</el-button>
            </div>
            
            <!-- 显示LLM推荐原因 -->
            <div v-if="llmReason" class="llm-reason">
                <h4>推荐原因：</h4>
                <p>{{ llmReason }}</p>
            </div>
        </div>
      
        <!-- 应聘信息列表 -->
        <el-table 
            :data="recommends" 
            style="width: 100%"
            v-loading="loading"  
            element-loading-text="DeepSeek正在为您智能匹配..."
            element-loading-background="rgba(255, 255, 255, 0.8)"
        >
            <el-table-column label="辅导科目" prop="subject"></el-table-column>
            <el-table-column label="辅导价格" prop="price"></el-table-column>
            <el-table-column label="辅导形式" prop="online"> </el-table-column>
            <el-table-column label="辅导时间" width="400" prop="time"></el-table-column>
            <el-table-column label="操作" width="150">
                <template #default="{ row }">
                    <el-button :icon="Message" circle plain type="info" @click="showDetail(row.detail)"></el-button>
                    <el-button :icon="Star" circle plain type="primary" @click="getTchInfo(row)"></el-button>
                </template>
            </el-table-column>
            <template #empty>
                <el-empty description="没有数据" />
            </template>
        </el-table>

    </el-card>
    <el-dialog v-model="tchInfoVisible" title="教师详细信息" width="500">
        <el-form :model="tchInfoModel">
            <el-form-item label="性别" label-width="80px">
                <el-input v-model="tchInfoModel.gender" readonly class="readonly-input"/>
            </el-form-item>
            <el-form-item label="学校" label-width="80px">
                <el-input v-model="tchInfoModel.school" readonly class="readonly-input"/>
            </el-form-item>
            <el-form-item label="学校层次" label-width="80px">
                <el-input v-model="tchInfoModel.schLevel" readonly class="readonly-input"/>
            </el-form-item>
            <el-form-item label="学历层次" label-width="80px">
                <el-input v-model="tchInfoModel.eduBg" readonly class="readonly-input"/>
            </el-form-item>
            <el-form-item label="联系方式" label-width="80px">
                <el-input v-model="tchInfoModel.phoneNum" readonly class="readonly-input"/>
            </el-form-item>
        </el-form>
    </el-dialog>
    
    <el-dialog v-model="detailVisible" title="详细描述" width="800">
        <div class="rich-content" v-html="selectedDetail" style="padding: 0 20px; line-height: 1.6"></div>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="detailVisible = false">关闭</el-button>
            </span>
        </template>
    </el-dialog>
</template>
<style lang="scss" scoped>

/* 添加加载指示器自定义样式 */
:deep(.el-loading-spinner .path) {
  stroke: #409EFF; /* 加载动画颜色 */
}
:deep(.el-loading-spinner) {
  margin-top: -30px; /* 调整位置 */
}

.el-select {
    --el-select-width: 220px;
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
.llm-recommend {
    margin-bottom: 15px;
    padding: 0px;
    // background-color: #f8f9fa;
    border-radius: 4px;
    
    .llm-reason {
        margin-top: 15px;
        padding: 10px;
        background-color: #e8f4ff;
        border-radius: 4px;
        
        h4 {
            margin: 5px 0;
            color: #409EFF;
        }
        
        p {
            margin: 0;
            line-height: 1.6;
        }
    }
}
/* 抽屉样式 */
.avatar-uploader {
    :deep() {
        .avatar {
            width: 178px;
            height: 178px;
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
            width: 178px;
            height: 178px;
            text-align: center;
        }
    }
}
.editor {
    width: 100%;
    :deep(.ql-editor) {
        min-height: 200px;
    }
}
.readonly-input {
    :deep(.el-input__inner) {
        // background-color: #f5f7fa;
        cursor: default;
    }
}
/* 富文本内容样式 */
.rich-content :deep(p) {
  margin: 1em 0;
}
.rich-content :deep(ul) {
  padding-left: 2em;
}
.rich-content :deep(pre) {
  background: #f5f7fa;
  padding: 1em;
  border-radius: 4px;
  overflow: auto;
}
</style>