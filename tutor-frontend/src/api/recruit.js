import request from '@/utils/request.js'

export const recruitAddService = (recruitData) => {
    return request.post("/recruit/publishRecruit", recruitData)
}

// queryString的调用方式
export const recruitListService = (params) =>{
    return request.get('/recruit/getMyRecruits', {params:params})
}

export const reqruitListAllService = (params) => {
    return request.get('/recruit/getAllRecruits', {params:params})
}

//文章分类修改接口
export const recruitUpdateService = (recruitData)=>{
    return request.post('/recruit/updateRecruit',recruitData)
}

//文章分类删除接口
export const recruitDeleteService = (json)=>{
    return request.post('/recruit/deleteRecruit',json)
}