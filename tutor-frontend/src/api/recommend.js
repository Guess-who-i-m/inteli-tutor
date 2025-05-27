import request from '@/utils/request.js'

export const recommendAddService = (recommendData) => {
    return request.post("/recommend/publishRecommend", recommendData)
}

// queryString的调用方式
export const recommendListService = (params) =>{
    return request.get('/recommend/getMyRecommends', {params:params})
}

//文章分类修改接口
export const recommendUpdateService = (recommendData)=>{
    return request.post('/recommend/updateRecommend',recommendData)
}

// queryString的调用方式
export const recommendListAllService = (params) =>{
    return request.get('/recommend/getAllRecommends', {params:params})
}

//文章分类删除接口
export const recommendDeleteService = (recommendId) => {
    // 这里是关键：将 recommendId 封装到 JSON 对象中
    return request.post('/recommend/deleteRecommend', recommendId);
};



