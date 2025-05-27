import request from '@/utils/request.js'

// 文章添加
export const recommendAddService = (recommendData) => {
    return request.post("/recommend/publishRecommend", recommendData)
}

// 文章列表查询
// queryString的调用方式
export const recommendListService = (params) =>{
    return request.get('/recommend/getMyRecommends', {params:params})
}

//文章分类修改接口
export const recommendUpdateService = (recommendData)=>{
    return request.post('/recommend/updateRecommend',recommendData)
}


//文章分类删除接口
// export const recommendDeleteService = (id)=>{
//     return request.delete('/category?id='+id)
// }



