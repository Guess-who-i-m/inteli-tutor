<script setup>
/*教师端-应聘信息发布与管理*/
// 目前没来得及做
// 删除按钮
// 条件筛选
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

//recommends->categorys
const recommends = ref([
    {
        "recommendId": 1,
        "subject": "语文",
        "online": "线上",
        "price": "100",
        "time": "星期一 19:00-21:00, 星期三 20:00-22:00",
        "detail": "这是一则有关描述"
    },
    {
        "recommendId": 1,
        "subject": "语文",
        "online": "线上",
        "price": "100",
        "time": "星期一 19:00-21:00, 星期三 20:00-22:00",
        "detail": "这是一则有关描述"
    },
    {
        "recommendId": 1,
        "subject": "语文",
        "online": "线上",
        "price": "100",
        "time": "星期一 19:00-21:00, 星期三 20:00-22:00",
        "detail": "这是一则有关描述"
    },
    {
        "recommendId": 1,
        "subject": "语文",
        "online": "线上",
        "price": "100",
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
    recommendList();
}
//当前页码发生变化，调用此函数
const onCurrentChange = (num) => {
    pageNum.value = num
    recommendList();
}



import { recommendListService, recommendAddService, recommendUpdateService, recommendDeleteService } from '@/api/recommend'

// 异步获取推荐列表数据并处理
const recommendList = async () => {
    // 准备请求参数：当前页码和每页数量（从Vue响应式变量获取）
    let params = {
        pageNum: pageNum.value,    // 当前页码
        pageSize: pageSize.value,  // 每页显示条数
        // 可选的科目过滤条件（示例中被注释）
        // subject: subject.value ? subject.value : null,
        // 可选的线上/线下过滤条件（示例中被注释） 
        // online: online.value ? online.value : null,
    }
    // 调用推荐列表服务获取数据
    let result = await recommendListService(params);
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
        // 转换数据结构：将后端数据转换为前端需要的格式
        recommends.value = result.data.items.map(item => {
            // 基础数据：从推荐主体数据中提取
            const recommendData = item.recommend;

            // 处理时间数据 ------------------------------------------------------
            // 安全处理：确保dates变量始终是数组（防止undefined/null导致.map报错）
            // 注意：如果recommendDates是字符串需要先转换，此处假设已修复后端返回格式
            const dates = item.recommendDates || [];  // 空数组兜底

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
                recommendId: recommendData.recommendId,             // ID直接传递
                subject: recommendData.subject,                     // 科目直接传递
                online: recommendData.online ? '线上' : '线下',      // 布尔转中文
                price: String(recommendData.price),                 // 价格转字符串(显示需要)
                time: timeString || '暂无时间安排',                  // 空数据兜底显示
                detail: recommendData.detail,
                // **新增：保留原始的时间数据数组**
                originalDates: dates // 将原始的 recommendDates 数组添加到 row 对象中               
            };
        });

        // 调试输出：在控制台显示转换后的数据结构
        console.log("转换后的推荐数据:", recommends.value);
    } else {
        // 异常处理：数据缺失时清空列表
        total.value = 0;            // 重置总条数
        recommends.value = [];      // 清空推荐列表
        console.error("数据获取或处理失败:", result);  // 输出错误日志
    }
};

recommendList();

//控制抽屉是否显示
const visibleDrawer = ref(false);

// 应聘信息数据模型
const recommendModel = ref({
    price: null,
    subject: '',
    online: null,
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
    recommendModel.value.price = value ? parseInt(value) || 0 : null;
}

// 处理时间组数量变化
const handleTimeNumChange = (newVal) => {
    // 处理数组长度变化
    if (newVal > recommendModel.value.days.length) {
        // 增加
        const addCount = newVal - recommendModel.value.days.length;
        for (let i = 0; i < addCount; i++) {
            recommendModel.value.days.push('mon');
            recommendModel.value.start_times.push('08:00:00');
            recommendModel.value.end_times.push('17:00:00');
        }
    } else {
        // 减少
        recommendModel.value.days = recommendModel.value.days.slice(0, newVal);
        recommendModel.value.start_times = recommendModel.value.start_times.slice(0, newVal);
        recommendModel.value.end_times = recommendModel.value.end_times.slice(0, newVal);
    }
}

const handleTimeChange = (timeValue, field, index) => {
    if (timeValue) {
        // 确保秒数始终为00
        const parts = timeValue.split(':');
        if (parts.length === 2) {
            // 如果只有HH:MM格式，补全SS
            recommendModel.value[field][index] = `${timeValue}:00`;
        } else if (parts.length === 3) {
            // 如果已经是HH:MM:SS格式，确保SS为00
            recommendModel.value[field][index] = `${parts[0]}:${parts[1]}:00`;
        }
    }
}


// 提交前验证时间有效性
const validateTimes = () => {
    for (let i = 0; i < recommendModel.value.time_num; i++) {
        const start = recommendModel.value.start_times[i];
        const end = recommendModel.value.end_times[i];

        if (start >= end) {
            return `时间组 ${i + 1} 的开始时间必须早于结束时间`;
        }
    }
    return true;
}

// import { recommendAddService } from '@/api/recommend'

const addRecommend = async () => {

    // 验证时间有效性
    const timeValid = validateTimes();
    if (timeValid !== true) {
        ElMessage.error(timeValid);
        return;
    }

    // 准备提交数据
    const submitData = {
        ...recommendModel.value,
        // 确保数组长度与time_num一致
        days: recommendModel.value.days.slice(0, recommendModel.value.time_num),
        start_times: recommendModel.value.start_times.slice(0, recommendModel.value.time_num),
        end_times: recommendModel.value.end_times.slice(0, recommendModel.value.time_num)
    };

    // 调用接口
    console.log(submitData);

    let result = await recommendAddService(submitData);

    ElMessage.success(result.message ? result.message : '添加成功')

    // 刷新当前列表
    recommendList();

    visibleDrawer.value = false;
}

//定义变量，控制变量的绑定,标题展示
const title = ref('')

//展示修改抽屉
const showDrawer = (row) => {
    visibleDrawer.value = true; title.value = '修改编辑信息'
    //数据拷贝
    recommendModel.value.price = row.price;
    recommendModel.value.subject = row.subject;
    recommendModel.value.online = row.online === '线上' ? true : false; // 根据显示文本判断
    recommendModel.value.detail = row.detail;
    // **修改：根据原始时间数据填充时间相关字段**
    if (row.originalDates && row.originalDates.length > 0) {
        recommendModel.value.time_num = row.originalDates.length;
        recommendModel.value.days = row.originalDates.map(date => date.day.toLowerCase()); // 转换为小写，与 option value 匹配
        recommendModel.value.start_times = row.originalDates.map(date => date.startTime);
        recommendModel.value.end_times = row.originalDates.map(date => date.endTime);
    } else {
        // 如果没有原始时间数据，则使用默认值
        recommendModel.value.time_num = 1;
        recommendModel.value.days = ['mon'];
        recommendModel.value.start_times = ['08:00:00'];
        recommendModel.value.end_times = ['17:00:00'];
    }
    //扩展 recommendModel的id属性，将来需要传递给后台，完成分类的修改
    recommendModel.value.recommend_id = row.recommendId
}

//修改发布信息
const updateRecommend = async () => {
    // 验证时间有效性
    const timeValid = validateTimes();
    if (timeValid !== true) {
        ElMessage.error(timeValid);
        return;
    }

    //调用接口
    let result = await recommendUpdateService(recommendModel.value);

    ElMessage.success(result.msg ? result.msg : "修改成功")

    //调用获取所有发布信息的函数
    recommendList();

    //隐藏弹窗
    visibleDrawer.value = false;
}

//清空模型的数据防止回显
const clearData = () => {
    // recommendModel.value.price = null;
    // recommendModel.value.subject = '';
    // recommendModel.value.online = true;
    // recommendModel.value.detail = '';
    // recommendModel.value.time_num = '';
    // recommendModel.value.days = ['mon'];
    // recommendModel.value.start_times = ['08:00:00'];
    // recommendModel.value.end_times = ['17:00:00'];
    recommendModel.value = {
        price: null,
        subject: '',
        online: null,
        detail: ' ',
        time_num: 1,  // 重置为默认1组时间
        days: ['mon'], // 重置为默认周一
        start_times: ['08:00:00'], // 重置为默认开始时间
        end_times: ['17:00:00']   // 重置为默认结束时间
    };
}

//删除分类
import { ElMessageBox } from "element-plus"
const deleteRecommend = (row) => {
    //提示用户 确认框
    ElMessageBox.confirm(
        '你确定要删除应聘信息吗？',
        '温馨提示',
        {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning',
        }
    )
        .then(async () => {
            //调用接口
            const json = { recommend_id: row.recommendId };
            console.log(json);
            let result = await recommendDeleteService(json);
            ElMessage.success(result.msg ? result.msg : "删除成功");
            //刷新列表
            recommendList();
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
                <span>应聘信息发布与管理</span>
                <div class="extra">
                    <el-button type="primary"
                        @click="visibleDrawer = true; title = '发布应聘'; clearData()">发布应聘</el-button>
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
                <el-button type="primary" @click="recommendList">搜索</el-button>
                <el-button @click="subject = ''; online = ''; recommendList()">重置</el-button>
            </el-form-item>
        </el-form>


        <!-- 应聘信息列表 -->
        <el-table :data="recommends" style="width: 100%">

            <el-table-column label="辅导科目" prop="subject"></el-table-column>

            <el-table-column label="辅导价格" prop="price"></el-table-column>

            <el-table-column label="辅导形式" prop="online"> </el-table-column>

            <el-table-column label="辅导时间" width="400" prop="time"></el-table-column>

            <el-table-column label="操作" width="150">
                <template #default="{ row }">
                    <el-button :icon="Message" circle plain type="info" @click="showDetail(row.detail)"></el-button>
                    <el-button :icon="Edit" circle plain type="primary" @click="showDrawer(row)"></el-button>
                    <el-button :icon="Delete" circle plain type="danger" @click="deleteRecommend(row)"></el-button>
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

    <!-- 抽屉  添加发布信息弹窗 判断title 复用同一弹窗有回显-->
    <el-drawer v-model="visibleDrawer" :title="title" direction="rtl" size="50%">
        <!-- 添加文章表单 -->
        <el-form :model="recommendModel" label-width="100px">
            <el-form-item label="辅导科目">
                <el-select placeholder="请选择" v-model="recommendModel.subject">
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
                <el-select placeholder="请选择" v-model="recommendModel.online">
                    <el-option label="线上" value="true"></el-option>
                    <el-option label="线下" value="false"></el-option>
                </el-select>
            </el-form-item>

            <el-form-item label="辅导价格">
                <el-input v-model="recommendModel.price" type="number" placeholder="请输入价格(元/小时)"
                    @input="handlePriceInput">
                </el-input>
            </el-form-item>

            <el-form-item label="详情描述">
                <div class="editor">
                    <quill-editor theme="snow" v-model:content="recommendModel.detail" contentType="html">
                    </quill-editor>
                </div>
            </el-form-item>


            <!-- 时间组数量 -->
            <el-form-item label="时间组数" prop="time_num">
                <el-input-number v-model="recommendModel.time_num" :min="1" :max="7"
                    @change="handleTimeNumChange"></el-input-number>
            </el-form-item>

            <!-- 动态时间组 -->
            <div v-for="(item, index) in recommendModel.time_num" :key="index">
                <el-form-item :label="`时间组 ${index + 1}`">
                    <el-row :gutter="20">
                        <!-- 星期选择 -->
                        <el-col :span="8">
                            <el-form-item :prop="`days[${index}]`">
                                <el-select v-model="recommendModel.days[index]" placeholder="选择星期">
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
                                <el-time-picker v-model="recommendModel.start_times[index]" format="HH:mm"
                                    value-format="HH:mm:ss" placeholder="开始时间"
                                    @change="handleTimeChange($event, 'start_times', index)"></el-time-picker>
                            </el-form-item>
                        </el-col>

                        <!-- 结束时间 -->
                        <el-col :span="8">
                            <el-form-item :prop="`end_times[${index}]`">
                                <el-time-picker v-model="recommendModel.end_times[index]" format="HH:mm"
                                    value-format="HH:mm:ss" placeholder="结束时间"
                                    @change="handleTimeChange($event, 'end_times', index)"></el-time-picker>
                            </el-form-item>
                        </el-col>
                    </el-row>
                </el-form-item>
            </div>

            <el-form-item>
                <el-button type="primary" @click="title == '发布应聘' ? addRecommend() : updateRecommend()">确认</el-button>
            </el-form-item>

        </el-form>

        <!-- <template></template> -->

    </el-drawer>

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