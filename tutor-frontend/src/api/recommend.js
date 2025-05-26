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