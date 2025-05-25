import request from '@/utils/request.js'
import { useTokenStore } from '@/stores/token.js'

// 文章分类列表查询
export const articleCategoryListService = ()=>{
    // const tokenStore = useTokenStore()
    
    // // 在pinia中定义的响应式数据不需要.value
    // return request.get('/category',{headers:{Authorization:tokenStore.token}})

    // 上面的逻辑在请求拦截器里面已经实现了
    return request.get('/category')
}

// 文章分类添加接口
export const articleCategoryAddService = (categoryData)=>{
    return request.post('/category', categoryData)
}

// 文章分类修改函数
export const articleCategoryUpdateService = (categoryData)=>{
    return request.put('/category', categoryData)
}

// 文章分类删除函数
export const articleCategoryDeleteService = (id)=>{
    return request.delete('/category?id='+id)
}

// 文章列表查询
// queryString的调用方式
export const articleListService = (params) =>{
    return request.get('/article', {params:params})
}

// 文章添加
export const articleAddService = (articleData) => {
    return request.post("/article", articleData)
}