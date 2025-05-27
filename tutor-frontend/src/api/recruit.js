import request from '@/utils/request.js'

export const recruitAddService = (recommendData) => {
    return request.post("/recruit/publishRecruit", recommendData)
}

// queryString的调用方式
export const recruitListService = (params) =>{
    return request.get('/recruit/getMyRecruits', {params:params})
}

export const reqruitListAllService = (params) => {
    return request.get('/recruit/getAllRecruits', {params:params})
}