import request from '@/utils/request.js'

// 文章添加
export const recommendAddService = (recommendData) => {
    return request.post("/recommend/publishRecommend", recommendData)
}