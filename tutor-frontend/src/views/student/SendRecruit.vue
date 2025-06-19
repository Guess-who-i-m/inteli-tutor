<script setup>
/*学生端-招聘信息发布与管理*/
// 目前没来得及做
// 删除按钮
// 条件筛选
// 修改按钮
// 显示detail

import {
    Edit,
    Delete,
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
        "schLevel": "九八五",
        "stuLevel": "A",
        "time": "星期一 19:00-21:00, 星期三 20:00-22:00",
        "detail": "这是一则有关描述"
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

const editorKey = ref(0) // 用于强制刷新编辑器

import { recruitListService, recruitAddService, recruitDeleteService, recruitUpdateService} from '@/api/recruit'

// 异步获取招聘列表数据并处理
const recruitList = async () => {
    // 准备请求参数：当前页码和每页数量（从Vue响应式变量获取）
    let params = {
        pageNum: pageNum.value,    // 当前页码
        pageSize: pageSize.value,  // 每页显示条数
        // 可选的科目过滤条件（示例中被注释）
        subject: subject.value ? subject.value : null,
        // 可选的线上/线下过滤条件（示例中被注释） 
        online: online.value ? online.value : null,
    }
    // 调用招聘列表服务获取数据
    let result = await recruitListService(params);
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
            yb: '一本',
            eb: '二本'
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
                recruitId: recruitData.recruitId,             // ID直接传递
                subject: recruitData.subject,                     // 科目直接传递
                online: recruitData.online,      // 布尔转中文
                price: String(recruitData.price),                 // 价格转字符串(显示需要)
                time: timeString || '暂无时间安排',                  // 空数据兜底显示
                schLevel: recruitData.schLevel,
                stuLevel: recruitData.stuLevel,
                detail: recruitData.detail,                          // 细节描述
                originalDates: dates // 将原始的 recommendDates 数组添加到 row 对象中  
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

//控制抽屉是否显示
const visibleDrawer = ref(false);

const recruitModel = ref({
    price: null,
    subject: '',
    online: null,
    schLevel: '',
    stuLevel: '',
    detail: '',
    time_num: 1,
    days: ['mon'],
    start_times: ['08:00:00'],
    end_times: ['17:00:00']
})

import { QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css'

import { ElMessage } from 'element-plus'



// 处理价格输入，确保为整数
const handlePriceInput = (value) => {
    recruitModel.value.price = value ? parseInt(value) || 0 : null;
}

// 处理时间组数量变化
const handleTimeNumChange = (newVal) => {
    // 处理数组长度变化
    if (newVal > recruitModel.value.days.length) {
        // 增加
        const addCount = newVal - recruitModel.value.days.length;
        for (let i = 0; i < addCount; i++) {
            recruitModel.value.days.push('mon');
            recruitModel.value.start_times.push('08:00:00');
            recruitModel.value.end_times.push('17:00:00');
        }
    } else {
        // 减少
        recruitModel.value.days = recruitModel.value.days.slice(0, newVal);
        recruitModel.value.start_times = recruitModel.value.start_times.slice(0, newVal);
        recruitModel.value.end_times = recruitModel.value.end_times.slice(0, newVal);
    }
}

const handleTimeChange = (timeValue, field, index) => {
    if (timeValue) {
        // 确保秒数始终为00
        const parts = timeValue.split(':');
        if (parts.length === 2) {
            // 如果只有HH:MM格式，补全SS
            recruitModel.value[field][index] = `${timeValue}:00`;
        } else if (parts.length === 3) {
            // 如果已经是HH:MM:SS格式，确保SS为00
            recruitModel.value[field][index] = `${parts[0]}:${parts[1]}:00`;
        }
    }
}


// 提交前验证时间有效性
const validateTimes = () => {
    for (let i = 0; i < recruitModel.value.time_num; i++) {
        const start = recruitModel.value.start_times[i];
        const end = recruitModel.value.end_times[i];

        if (start >= end) {
            return `时间组 ${i + 1} 的开始时间必须早于结束时间`;
        }
    }
    return true;
}

// import { recruitAddService, recruitUpdateService } from '@/api/recruit'


const addRecruit = async () => {

    // 验证时间有效性
    const timeValid = validateTimes();
    if (timeValid !== true) {
        ElMessage.error(timeValid);
        return;
    }

    // 准备提交数据
    const submitData = {
        ...recruitModel.value,
        // 确保数组长度与time_num一致
        days: recruitModel.value.days.slice(0, recruitModel.value.time_num),
        start_times: recruitModel.value.start_times.slice(0, recruitModel.value.time_num),
        end_times: recruitModel.value.end_times.slice(0, recruitModel.value.time_num)
    };

    // 调用接口
    console.log(submitData);

    let result = await recruitAddService(submitData);

    ElMessage.success(result.message ? result.message : '添加成功')

    visibleDrawer.value = false;

    // 添加成功后清空表单
    recruitModel.value = {
        price: null,
        subject: '',
        online: true,
        detail: '',
        time_num: 1,  // 重置为默认1组时间
        days: ['mon'], // 重置为默认周一
        start_times: ['08:00:00'], // 重置为默认开始时间
        end_times: ['17:00:00']   // 重置为默认结束时间
    };

    // 刷新当前列表
    recruitList();
}

//定义变量，控制变量的绑定,标题展示
const title = ref('')

// 展示修改抽屉（适配recruitModel）
const showDrawer = (row) => {
    visibleDrawer.value = true;
    title.value = '修改编辑信息';

    // 数据拷贝 - 适配recruitModel
    recruitModel.value = {
        price: row.price,
        subject: row.subject,
        online: row.online,
        schLevel: row.schLevel,       // 新增字段
        stuLevel: row.stuLevel,       // 新增字段
        detail: row.detail,
        time_num: 1, // 默认值，下面会覆盖
        days: ['mon'],
        start_times: ['08:00:00'],
        end_times: ['17:00:00']
    };
    // 处理时间数据
    if (row.originalDates && row.originalDates.length > 0) {
        recruitModel.value.time_num = row.originalDates.length;
        recruitModel.value.days = row.originalDates.map(date => date.day.toLowerCase());
        recruitModel.value.start_times = row.originalDates.map(date => date.startTime);
        recruitModel.value.end_times = row.originalDates.map(date => date.endTime);
    }

    // 保存ID用于后续更新
    recruitModel.value.recruit_id = row.recruitId; // 假设ID字段名称为recruitId

}
// 清空模型数据（适配recruitModel）
const clearData = () => {
    editorKey.value++ // 强制刷新编辑器
    recruitModel.value = {
        price: null,
        subject: '',
        online: null,
        schLevel: '',       // 新增字段
        stuLevel: '',       // 新增字段
        detail: '',
        time_num: 1,
        days: ['mon'],
        start_times: ['08:00:00'],
        end_times: ['17:00:00']
    };
}

const schMap = {
        jbw : '985',
        eyy : '211',
        syl : '双一流',
        yb : '一本',
        eb: '二本'
    };
    
// 修改发布信息（适配recruitModel）
const updateRecruit = async () => {
    // 验证时间有效性（保持原逻辑）
    const timeValid = validateTimes();
    if (timeValid !== true) {
        ElMessage.error(timeValid);
        return;
    }

    

    // 构建提交数据
    const submitData = {
        ...recruitModel.value,
        sch_level: recruitModel.value.schLevel,   // 转换字段
        stu_level: recruitModel.value.stuLevel,                  // 转换字段
        days: recruitModel.value.days.slice(0, recruitModel.value.time_num),
        start_times: recruitModel.value.start_times.slice(0, recruitModel.value.time_num),
        end_times: recruitModel.value.end_times.slice(0, recruitModel.value.time_num)
    };
    // 删除原有的schLevel和stuLevel字段
    delete submitData.schLevel;
    delete submitData.stuLevel;


    // 调用接口（假设有对应的recruitUpdateService）
    console.log(submitData);

    let result = await recruitUpdateService(submitData);
    ElMessage.success(result.msg || "修改成功");

    // 刷新列表
    recruitList();

    // 关闭抽屉
    visibleDrawer.value = false;
}



//删除分类
import { ElMessageBox } from "element-plus"
const deleteRecruit = (row) => {
    //提示用户 确认框
    ElMessageBox.confirm(
        '你确定要删除招聘信息吗？',
        '温馨提示',
        {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning',
        }
    )
        .then(async () => {
            //调用接口
            const json = { recruit_id: row.recruitId };
            console.log(json);
            let result = await recruitDeleteService(json);
            ElMessage.success(result.msg ? result.msg : "删除成功");
            //刷新列表
            recruitList();
        })
        .catch(() => {
            ElMessage({
                type: 'info',
                message: '用户取消了删除',
            })
        })
}
</script>



<template>
    <el-card class="page-container">

        <template #header>
            <div class="header">
                <span>招聘信息发布与管理</span>
                <div class="extra">
                    <el-button type="primary"
                        @click="visibleDrawer = true; title = '发布招聘信息'; clearData()">发布招聘</el-button>
                </div>
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
                <el-button type="primary" @click="recruitList()">搜索</el-button>
                <el-button @click="subject = ''; online = ''; recruitList()">重置</el-button>
            </el-form-item>
        </el-form>


        <!-- 应聘信息列表 -->
        <el-table :data="recruits" style="width: 100%">

            <el-table-column label="辅导科目" prop="subject"></el-table-column>

            <el-table-column label="辅导价格" prop="price"></el-table-column>

            <el-table-column label="辅导形式">
                <template #default="scope">
                {{ scope.row.online ? '线上' : '线下' }}
                </template>
            </el-table-column>

            <el-table-column label="教师要求"> 
                <template #default="scope">
                {{ schMap[scope.row.schLevel] }}
                </template>
            </el-table-column>

            <el-table-column label="学生水平" prop="stuLevel"> </el-table-column>

            <el-table-column label="辅导时间" width="400" prop="time"></el-table-column>

            <el-table-column label="操作" width="150">
                <template #default="{ row }">
                    <el-button :icon="Message" circle plain type="info" @click="showDetail(row.detail)">
                    </el-button>
                    <el-button :icon="Edit" circle plain type="primary" @click="showDrawer(row)"></el-button>
                    <el-button :icon="Delete" circle plain type="danger" @click="deleteRecruit(row)"></el-button>
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

    <!-- 抽屉 -->
    <el-drawer v-model="visibleDrawer" :title="title" direction="rtl" size="50%">
        <!-- 添加文章表单 -->
        <el-form :model="recruitModel" label-width="100px">
            <el-form-item label="辅导科目">
                <el-select placeholder="请选择" v-model="recruitModel.subject">
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

            <el-form-item label="辅导形式">
                <el-select placeholder="请选择" v-model="recruitModel.online">
                    <el-option label="线上" :value="true"></el-option>
                    <el-option label="线下" :value="false"></el-option>
                </el-select>
            </el-form-item>

            <el-form-item label="教师水平">
                <el-select placeholder="请选择" v-model="recruitModel.schLevel">
                    <el-option label="985" value="jbw"></el-option>
                    <el-option label="211" value="eyy"></el-option>
                    <el-option label="双一流" value="syl"></el-option>
                    <el-option label="一本" value="yb"></el-option>
                    <el-option label="二本" value="eb"></el-option>
                </el-select>
            </el-form-item>

            <el-form-item label="学生水平">
                <el-select placeholder="请选择" v-model="recruitModel.stuLevel">
                    <el-option label="A" value="A"></el-option>
                    <el-option label="B" value="B"></el-option>
                    <el-option label="C" value="C"></el-option>
                    <el-option label="D" value="D"></el-option>
                    <el-option label="E" value="E"></el-option>
                </el-select>
            </el-form-item>

            <el-form-item label="辅导价格">
                <el-input v-model="recruitModel.price" type="number" placeholder="请输入价格(元/小时)"
                    @input="handlePriceInput">
                </el-input>
            </el-form-item>

            <el-form-item label="详情描述">
                <div class="editor">
                    <quill-editor theme="snow" v-model:content="recruitModel.detail" contentType="html" :key="editorKey">
                    </quill-editor>
                </div>
            </el-form-item>


            <!-- 时间组数量 -->
            <el-form-item label="时间组数" prop="time_num">
                <el-input-number v-model="recruitModel.time_num" :min="1" :max="7"
                    @change="handleTimeNumChange"></el-input-number>
            </el-form-item>

            <!-- 动态时间组 -->
            <div v-for="(item, index) in recruitModel.time_num" :key="index">
                <el-form-item :label="`时间组 ${index + 1}`">
                    <el-row :gutter="20">
                        <!-- 星期选择 -->
                        <el-col :span="8">
                            <el-form-item :prop="`days[${index}]`">
                                <el-select v-model="recruitModel.days[index]" placeholder="选择星期">
                                    <el-option label="周一" value="mon"></el-option>
                                    <el-option label="周二" value="tue"></el-option>
                                    <el-option label="周三" value="wed"></el-option>
                                    <el-option label="周四" value="thu"></el-option>
                                    <el-option label="周五" value="fri"></el-option>
                                    <el-option label="周六" value="sat"></el-option>
                                    <el-option label="周日" value="sun"></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>

                        <!-- 开始时间 -->
                        <el-col :span="8">
                            <el-form-item :prop="`start_times[${index}]`">
                                <el-time-picker v-model="recruitModel.start_times[index]" format="HH:mm"
                                    value-format="HH:mm:ss" placeholder="开始时间"
                                    @change="handleTimeChange($event, 'start_times', index)"></el-time-picker>
                            </el-form-item>
                        </el-col>

                        <!-- 结束时间 -->
                        <el-col :span="8">
                            <el-form-item :prop="`end_times[${index}]`">
                                <el-time-picker v-model="recruitModel.end_times[index]" format="HH:mm"
                                    value-format="HH:mm:ss" placeholder="结束时间"
                                    @change="handleTimeChange($event, 'end_times', index)"></el-time-picker>
                            </el-form-item>
                        </el-col>
                    </el-row>
                </el-form-item>
            </div>

            <el-form-item>
                <el-button type="primary" @click="title == '发布招聘信息' ? addRecruit() : updateRecruit()">确认</el-button>
            </el-form-item>

        </el-form>

    </el-drawer>

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