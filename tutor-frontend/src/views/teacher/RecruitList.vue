<script setup>

// 目前没来得及做
// 条件筛选
// 显示detail
// 获取个人信息按钮

import {
    Star,
    Message
} from '@element-plus/icons-vue'

import { ref } from 'vue'

// 用户设置列表筛选条件时的科目
const subject = ref('')

// 用户搜索时选中的线上或线下
const online = ref('')

// 展示细节弹窗
const detailVisible = ref(false)

const selectedDetail = ref('') // 新增：存储当前选中的详情内容
// 新增：显示详情的方法
const showDetail = (detail) => {
  selectedDetail.value = detail
  detailVisible.value = true
}

const recruits = ref([
    {
        "recruitId": 1,
        "subject": "语文",
        "online": "线上",
        "price": "100",
        "schLevel":"九八五",
        "stuLevel":"A",
        "time": "星期一 19:00-21:00, 星期三 20:00-22:00",
        "detail":"这是一则有关描述"
    }
])

//分页条数据模型
const pageNum = ref(1)//当前页
const total = ref(20)//总条数
const pageSize = ref(3)//每页条数

//当每页条数发生了变化，调用此函数
const onSizeChange = (size) => {
    pageSize.value = size
    recruitList();
}
//当前页码发生变化，调用此函数
const onCurrentChange = (num) => {
    pageNum.value = num
    recruitList();
}

import { reqruitListAllService } from '@/api/recruit'

// 异步获取招聘列表数据并处理
const recruitList = async () => {
    // 准备请求参数：当前页码和每页数量（从Vue响应式变量获取）
    let params = {
        pageNum: pageNum.value,    // 当前页码
        pageSize: pageSize.value,  // 每页显示条数
        // 可选的科目过滤条件（示例中被注释）
        // subject: subject.value ? subject.value : null,
        // 可选的线上/线下过滤条件（示例中被注释） 
        // online: online.value ? online.value : null,
    }
    // 调用招聘列表服务获取数据
    let result = await reqruitListAllService(params);
    // 数据处理流程
    if (result.data && result.data.items) {
        // 更新总数据条数（用于分页显示）
        total.value = result.data.total;
        // 星期映射表：将英文缩写转换为中文星期显示
        const dayMap = {
            mon: '星期一',  // Monday
            tue: '星期二',  // Tuesday
            wed: '星期三',  // Wednesday
            thu: '星期四',  // Thursday
            fri: '星期五',  // Friday
            sat: '星期六',  // Saturday
            sun: '星期日'   // Sunday
        };

        const schMap = {
            jbw: '985',
            eyy: '211',
            syl: '双一流',
            yb:  '一本',
            eb:  '二本'
        }

        // 转换数据结构：将后端数据转换为前端需要的格式
        recruits.value = result.data.items.map(item => {
            // 基础数据：从招聘主体数据中提取
            const recruitData = item.recruit;
            
            // 处理时间数据 ------------------------------------------------------
            // 安全处理：确保dates变量始终是数组（防止undefined/null导致.map报错）
            // 注意：如果recommendDates是字符串需要先转换，此处假设已修复后端返回格式
            const dates = item.recruitDates || [];  // 空数组兜底
            
            // 构建时间显示字符串
            const timeString = dates
                .map(dateObj => {
                    // 转换星期显示：将英文缩写转为中文
                    // 安全处理：转换小写 + 默认显示原始值（防止未知值导致显示异常）
                    const dayName = dayMap[dateObj.day.toLowerCase()] || dateObj.day;
                    
                    // 时间格式处理：截取HH:MM部分（假设后端返回格式为HH:mm:ss）
                    const startTime = dateObj.startTime 
                        ? dateObj.startTime.substring(0, 5)  // 截取前5位(08:00)
                        : 'N/A';  // 异常兜底
                    const endTime = dateObj.endTime 
                        ? dateObj.endTime.substring(0, 5) 
                        : 'N/A';
                        
                    // 返回单条时间信息格式：e.g. "星期一 08:00-17:00"
                    return `${dayName} ${startTime}-${endTime}`;
                })
                .join(', ');  // 多条用逗号分隔
            
            // 返回最终结构 ------------------------------------------------------
            return {
                recommendId: recruitData.recruitId,             // ID直接传递
                subject: recruitData.subject,                     // 科目直接传递
                online: recruitData.online ? '线上' : '线下',      // 布尔转中文
                price: String(recruitData.price),                 // 价格转字符串(显示需要)
                time: timeString || '暂无时间安排',                  // 空数据兜底显示
                schLevel: schMap[recruitData.schLevel],
                stuLevel: recruitData.stuLevel,
                detail: recruitData.detail                          // 细节描述
            };
        });
        
        // 调试输出：在控制台显示转换后的数据结构
        console.log("转换后的招聘数据:", recruits.value);
    } else {
        // 异常处理：数据缺失时清空列表
        total.value = 0;            // 重置总条数
        recruits.value = [];      // 清空招聘列表
        console.error("数据获取或处理失败:", result);  // 输出错误日志
    }
};

recruitList();

</script>



<template>
    <el-card class="page-container">
        
        <template #header>
            <div class="header">
                <span>家教招聘信息一览</span>
                
            </div>
        </template>

        <!-- 搜索表单 -->
        <el-form inline>
            <el-form-item label="辅导科目：">
                <el-select placeholder="请选择" v-model="subject">
                    <el-option label="语文" value="语文"></el-option>
                    <el-option label="数学" value="数学"></el-option>
                    <el-option label="英语" value="英语"></el-option>
                    <el-option label="物理" value="物理"></el-option>
                    <el-option label="化学" value="化学"></el-option>
                    <el-option label="生物" value="生物"></el-option>
                    <el-option label="历史" value="历史"></el-option>
                    <el-option label="地理" value="地理"></el-option>
                    <el-option label="政治" value="政治"></el-option>
                </el-select>
            </el-form-item>

            <el-form-item label="上课形式：">
                <el-select placeholder="请选择" v-model="online">
                    <el-option label="线上" value="true"></el-option>
                    <el-option label="线下" value="false"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="recruitList">搜索</el-button>
                <el-button @click="subject = ''; online = ''; recruitList()">重置</el-button>
            </el-form-item>
        </el-form>


        <!-- 应聘信息列表 -->
        <el-table :data="recruits" style="width: 100%">

            <el-table-column label="辅导科目" prop="subject"></el-table-column>

            <el-table-column label="辅导价格" prop="price"></el-table-column>

            <el-table-column label="辅导形式" prop="online"> </el-table-column>

            <el-table-column label="教师要求" prop="schLevel"> </el-table-column>

            <el-table-column label="学生水平" prop="stuLevel"> </el-table-column>

            <el-table-column label="辅导时间" width="400" prop="time"></el-table-column>

            <el-table-column label="操作" width="150">
                <template #default="{ row }">
                    <el-button 
                    :icon="Message" 
                    circle 
                    plain 
                    type="info" 
                    @click="showDetail(row.detail)"></el-button>
                    <el-button :icon="Star" circle plain type="primary"></el-button>
                </template>
            </el-table-column>

            <template #empty>
                <el-empty description="没有数据" />
            </template>

        </el-table>

        <!-- 分页条 -->
        <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :page-sizes="[3, 5, 10, 15]"
            layout="jumper, total, sizes, prev, pager, next" background :total="total" @size-change="onSizeChange"
            @current-change="onCurrentChange" style="margin-top: 20px; justify-content: flex-end" />
    
    </el-card>

    <el-dialog v-model="detailVisible" title="详细描述" width="800">
        <div 
            class="rich-content" 
            v-html="selectedDetail"
            style="padding: 0 20px; line-height: 1.6"></div>
        
        <template #footer>
            <span class="dialog-footer">
            <el-button @click="detailVisible = false">关闭</el-button>
            </span>
        </template>
    </el-dialog>

</template>


<style lang="scss" scoped>
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