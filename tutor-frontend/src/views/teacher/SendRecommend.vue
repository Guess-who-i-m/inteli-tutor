<script setup>
import {
    Edit,
    Delete
} from '@element-plus/icons-vue'

import { ref } from 'vue'


//文章分类数据模型
const categorys = ref([
    {
        "id": 3,
        "categoryName": "美食",
        "categoryAlias": "my",
        "createTime": "2023-09-02 12:06:59",
        "updateTime": "2023-09-02 12:06:59"
    },
    {
        "id": 4,
        "categoryName": "娱乐",
        "categoryAlias": "yl",
        "createTime": "2023-09-02 12:08:16",
        "updateTime": "2023-09-02 12:08:16"
    },
    {
        "id": 5,
        "categoryName": "军事",
        "categoryAlias": "js",
        "createTime": "2023-09-02 12:08:33",
        "updateTime": "2023-09-02 12:08:33"
    }
])

//用户搜索时选中的分类id
const categoryId = ref('')

//用户搜索时选中的发布状态
const state = ref('')

//文章列表数据模型
const articles = ref([
    {
        "id": 5,
        "title": "陕西旅游攻略",
        "content": "兵马俑,华清池,法门寺,华山...爱去哪去哪...",
        "coverImg": "https://big-event-gwd.oss-cn-beijing.aliyuncs.com/9bf1cf5b-1420-4c1b-91ad-e0f4631cbed4.png",
        "state": "草稿",
        "categoryId": 2,
        "createTime": "2023-09-03 11:55:30",
        "updateTime": "2023-09-03 11:55:30"
    },
    {
        "id": 5,
        "title": "陕西旅游攻略",
        "content": "兵马俑,华清池,法门寺,华山...爱去哪去哪...",
        "coverImg": "https://big-event-gwd.oss-cn-beijing.aliyuncs.com/9bf1cf5b-1420-4c1b-91ad-e0f4631cbed4.png",
        "state": "草稿",
        "categoryId": 2,
        "createTime": "2023-09-03 11:55:30",
        "updateTime": "2023-09-03 11:55:30"
    },
    {
        "id": 5,
        "title": "陕西旅游攻略",
        "content": "兵马俑,华清池,法门寺,华山...爱去哪去哪...",
        "coverImg": "https://big-event-gwd.oss-cn-beijing.aliyuncs.com/9bf1cf5b-1420-4c1b-91ad-e0f4631cbed4.png",
        "state": "草稿",
        "categoryId": 2,
        "createTime": "2023-09-03 11:55:30",
        "updateTime": "2023-09-03 11:55:30"
    },
])

//分页条数据模型
const pageNum = ref(1)//当前页
const total = ref(20)//总条数
const pageSize = ref(3)//每页条数

//当每页条数发生了变化，调用此函数
const onSizeChange = (size) => {
    pageSize.value = size
    articleList()
}
//当前页码发生变化，调用此函数
const onCurrentChange = (num) => {
    pageNum.value = num
    articleList()
}

// 回显文章分类
import { articleCategoryListService, articleListService } from '@/api/article'

const articleCategoryList = async () => {
    let result = await articleCategoryListService();

    categorys.value = result.data
}

articleCategoryList()

// 获取文章列表函数
const articleList = async () => {
    let params = {
        pageNum: pageNum.value,
        pageSize: pageSize.value,
        categoryId: categoryId.value ? categoryId.value : null,
        state: state.value ? state.value : null,
    }
    let result = await articleListService(params);

    // 渲染视图
    total.value = result.data.total;
    articles.value = result.data.items;

    // 处理数据，给数据模型扩展一个属性categoryName，变量名称
    for (let i = 0; i < articles.value.length; i++) {
        let article = articles.value[i];
        for (let j = 0; j < categorys.value.length; j++) {
            if (article.categoryId == categorys.value[j].id) {
                article.categoryName = categorys.value[j].categoryName
            }
        }
    }
}

articleList()

//控制抽屉是否显示
const visibleDrawer = ref(false)

// 应聘信息数据模型
const recommendModel = ref({
    price:null,
    subject:'',
    online:null,
    detail:'',
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
// 确保online为布尔值
const handleOnlineChange = (value) => {
    recommendModel.value.online = Boolean(value);
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

import { recommendAddService } from '@/api/recommend'

const addRecommend = async() => {

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

    ElMessage.success(result.message? result.message : '添加成功')

    visibleDrawer.value = false;

    // 添加成功后清空表单
    recommendModel.value = {
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
    // articleList();
}

</script>



<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>应聘信息发布与管理</span>
                <div class="extra">
                    <el-button type="primary" @click="visibleDrawer = true">发布应聘</el-button>
                </div>
            </div>
        </template>
        <!-- 搜索表单 -->
        <el-form inline>
            <el-form-item label="辅导科目：">
                <el-select placeholder="请选择" v-model="categoryId">
                    <el-option v-for="c in categorys" :key="c.id" :label="c.categoryName" :value="c.id">
                    </el-option>
                </el-select>
            </el-form-item>

            <el-form-item label="上课形式：">
                <el-select placeholder="请选择" v-model="state">
                    <el-option label="已发布" value="已发布"></el-option>
                    <el-option label="草稿" value="草稿"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="articleList">搜索</el-button>
                <el-button @click="categoryId = ''; state = ''; articleList()">重置</el-button>
            </el-form-item>
        </el-form>
        <!-- 文章列表 -->
        <el-table :data="articles" style="width: 100%">

            <el-table-column label="文章标题" width="400" prop="title"></el-table-column>

            <el-table-column label="分类" prop="categoryName"></el-table-column>

            <el-table-column label="发表时间" prop="createTime"> </el-table-column>

            <el-table-column label="状态" prop="state"></el-table-column>

            <el-table-column label="操作" width="100">
                <template #default="{ row }">
                    <el-button :icon="Edit" circle plain type="primary"></el-button>
                    <el-button :icon="Delete" circle plain type="danger"></el-button>
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
    <el-drawer v-model="visibleDrawer" title="发布应聘信息" direction="rtl" size="50%">
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
                <el-select 
                    placeholder="请选择" 
                    v-model="recommendModel.online"
                    @change="handleOnlineChange">
                    <el-option label="线上" value="true"></el-option>
                    <el-option label="线下" value="false"></el-option>
                </el-select>
            </el-form-item>

            <el-form-item label="辅导价格">
                <el-input 
                    v-model="recommendModel.price" 
                    type="number"
                    placeholder="请输入价格(元/小时)"
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
                <el-input-number 
                    v-model="recommendModel.time_num" 
                    :min="1" 
                    :max="7"
                    @change="handleTimeNumChange"
                ></el-input-number>
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
                                <el-time-picker
                                    v-model="recommendModel.start_times[index]"
                                    format="HH:mm"          
                                    value-format="HH:mm:ss" 
                                    placeholder="开始时间"
                                    @change="handleTimeChange($event, 'start_times', index)"
                                ></el-time-picker>
                            </el-form-item>
                        </el-col>
                        
                        <!-- 结束时间 -->
                        <el-col :span="8">
                            <el-form-item :prop="`end_times[${index}]`">
                                <el-time-picker
                                v-model="recommendModel.end_times[index]"
                                format="HH:mm"          
                                value-format="HH:mm:ss" 
                                placeholder="结束时间"
                                @change="handleTimeChange($event, 'end_times', index)"
                            ></el-time-picker>
                            </el-form-item>
                        </el-col>
                    </el-row>
                </el-form-item>
            </div>

            <el-form-item>
                <el-button type="primary" @click="addRecommend()">发布</el-button>
            </el-form-item>

        </el-form>
    
    </el-drawer>

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

</style>