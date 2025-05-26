// 定义store
import { defineStore } from "pinia";
import { ref } from "vue"
/**
 * 第一个参数：名字，唯一性
 * 第二个参数：函数，函数内部可以定义状态所有内容
 */
export const useUserTypeStore = defineStore('type', ()=>{
    // 定义状态内的所有内容

    // 1. 响应式变量
    const type = ref('')

    // 2. 定义一个函数，修改token的值
    const setType = (newType)=>{
        type.value = newType
    }

    // 3. 函数，移除token的值
    const removeType = ()=>{
        type.value=''
    }

    return {
        type,setType, removeType
    }
}, {
    persist:true // 设定持久化存储
});