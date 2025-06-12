import request from '@/utils/request.js'

// queryString的调用方式
export const llmChatStreamService = (params) =>{
    return request.post('/llm/stream', params)
}