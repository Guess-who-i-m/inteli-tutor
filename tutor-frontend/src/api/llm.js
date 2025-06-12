import request from '@/utils/request.js'

// queryString的调用方式
export const llmChatStreamService = (params) =>{
    return request.post('/llm/stream', params)
}

// queryString的调用方式
export const llmRecommendTeacherService = (params) =>{
    return request.post('/teacher/llmRecommendTeacher', params)
}

export const llmRecommendStudentService = (params) =>{
    return request.post('/student/llmRecommendStudent', params)
}
