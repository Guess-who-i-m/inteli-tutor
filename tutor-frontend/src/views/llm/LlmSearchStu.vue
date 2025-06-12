<script setup>
import { Message, Star } from '@element-plus/icons-vue'
/* 教师端-智能学生推荐 */
import { ElMessage } from 'element-plus'
import { ref } from 'vue'

// 添加加载状态变量
const loading = ref(false) // 控制加载状态

// --- 弹窗与详情展示 ---
// 详细描述弹窗
const detailVisible = ref(false)
const selectedDetail = ref('')

const showDetail = (detail) => {
    selectedDetail.value = detail
    detailVisible.value = true
}

// 学生信息弹窗
const stuInfoVisible = ref(false)
const stuInfoModel = ref({
    username: '',
    gender: '',
    grade: '',
    addr: '',
    phoneNum: '',
})

// --- LLM 智能推荐相关 ---
const llmPrompt = ref('') // LLM提问输入
const llmReason = ref('LLM将会根据您的个人情况和教学期望，为您匹配合适的学生岗位') // LLM回答的原因
const recruits = ref([]) // 推荐的学生岗位列表

// 导入新的LLM服务
import { llmRecommendStudentService } from '@/api/llm'

// 重置列表和推荐原因
const resetList = () => {
    recruits.value = [];
    llmReason.value = 'LLM将会根据您的个人情况和教学期望，为您匹配合适的学生岗位';
};

// 调用LLM推荐学生岗位
const callLLMRecommend = async () => {
    if (!llmPrompt.value.trim()) {
        ElMessage.warning('请描述您的个人优势和教学期望');
        return;
    }

    // 设置加载状态
    loading.value = true;
    
    try {
        const result = await llmRecommendStudentService({ prompt: llmPrompt.value });
        
        if (result.code === 1) {
            // 处理返回的原因
            llmReason.value = result.map?.reason || '已为您找到最匹配的岗位！';
            
            // 定义映射表
            const schMap = { jbw: '985', eyy: '211', syl: '双一流', yb: '一本', eb: '二本' };

            // 处理返回的推荐数据
            recruits.value = result.data.map(item => {
                return {
                    recruitId: item.recruitId,
                    subject: item.subject,
                    online: item.online ? '线上' : '线下',
                    price: String(item.price),
                    schLevel: schMap[item.schLevel] || '不限',
                    stuLevel: item.stuLevel,
                    time: '具体时间请与学生/家长沟通', // LLM返回的数据不包含具体时间
                    detail: item.detail,
                    stuId: item.stuId
                };
            });
            if (recruits.value.length === 0) {
                 ElMessage.info('没有找到完全匹配的岗位，请尝试优化您的描述');
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

// --- 获取学生详细信息 ---
import { studentInfoFromIdService } from '@/api/student'

const getStuInfo = async (row) => {
    try {
        let result = await studentInfoFromIdService(row.stuId);
        
        // 定义映射表
        const genderMap = { F: '女', M: '男' };
        const gradeMap = {
            '1': '小学一年级', '2': '小学二年级', '3': '小学三年级',
            '4': '小学四年级', '5': '小学五年级', '6': '小学六年级',
            '7': '初中一年级', '8': '初中二年级', '9': '初中三年级',
            '10': '高中一年级', '11': '高中二年级', '12': '高中三年级',
        };
        
        // 填充数据模型
        stuInfoModel.value.gender = genderMap[result.data.gender] || '未知';
        stuInfoModel.value.grade = gradeMap[String(result.data.grade)] || '未知';
        stuInfoModel.value.addr = result.data.addr;
        stuInfoModel.value.phoneNum = result.data.phoneNum;
        
        stuInfoVisible.value = true;
    } catch (error) {
        ElMessage.error('获取学生信息失败');
        console.error(error);
    }
}
</script>

<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>DeepSeek为您推荐合适岗位</span>
            </div>
        </template>
        <!-- LLM智能推荐区域 -->
        <div class="llm-recommend">
            <el-input
                v-model="llmPrompt"
                type="textarea"
                :rows="3"
                placeholder="请描述您的个人优势和教学期望，例如：我是985院校数学系在读，有两年家教经验，擅长辅导初中生，希望找一份线上数学家教工作。"
                clearable
            ></el-input>
            <div style="margin-top: 10px; text-align: right">
                <el-button type="primary" @click="callLLMRecommend" :loading="loading">智能推荐</el-button>
                <el-button @click="llmPrompt = ''; resetList();">重置</el-button>
            </div>
            
            <!-- 显示LLM推荐原因 -->
            <div v-if="llmReason" class="llm-reason">
                <h4>推荐原因：</h4>
                <p>{{ llmReason }}</p>
            </div>
        </div>
      
        <!-- 招聘信息列表 -->
        <el-table 
            :data="recruits" 
            style="width: 100%"
            v-loading="loading"  
            element-loading-text="DeepSeek正在为您智能匹配..."
            element-loading-background="rgba(255, 255, 255, 0.8)"
        >
            <el-table-column label="辅导科目" prop="subject"></el-table-column>
            <el-table-column label="辅导价格(元/小时)" prop="price"></el-table-column>
            <el-table-column label="辅导形式" prop="online"></el-table-column>
            <el-table-column label="教师要求" prop="schLevel"></el-table-column>
            <el-table-column label="学生水平" prop="stuLevel"></el-table-column>
            <el-table-column label="辅导时间" width="250" prop="time"></el-table-column>
            <el-table-column label="操作" width="150">
                <template #default="{ row }">
                    <el-button :icon="Message" circle plain type="info" @click="showDetail(row.detail)" title="查看岗位详情"></el-button>
                    <el-button :icon="Star" circle plain type="primary" @click="getStuInfo(row)" title="查看学生信息"></el-button>
                </template>
            </el-table-column>
            <template #empty>
                <el-empty description="暂无推荐，请输入您的需求以获取智能推荐" />
            </template>
        </el-table>

    </el-card>

    <!-- 学生信息弹窗 -->
    <el-dialog v-model="stuInfoVisible" title="学生详细信息" width="500">
        <el-form :model="stuInfoModel">
            <el-form-item label="性别" label-width="80px">
                <el-input v-model="stuInfoModel.gender" readonly class="readonly-input"/>
            </el-form-item>
            <el-form-item label="年级" label-width="80px">
                <el-input v-model="stuInfoModel.grade" readonly class="readonly-input"/>
            </el-form-item>
            <el-form-item label="家庭住址" label-width="80px">
                <el-input v-model="stuInfoModel.addr" readonly class="readonly-input"/>
            </el-form-item>
            <el-form-item label="联系方式" label-width="80px">
                <el-input v-model="stuInfoModel.phoneNum" readonly class="readonly-input"/>
            </el-form-item>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="stuInfoVisible = false">关闭</el-button>
            </span>
        </template>
    </el-dialog>
    
    <!-- 岗位详细描述弹窗 -->
    <el-dialog v-model="detailVisible" title="岗位详细描述" width="800">
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