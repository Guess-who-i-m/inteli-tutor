// 导入request.js工具
import request from '@/utils/request.js'

// 提供注册接口调用函数
export const studentRegisterService = (registerData)=>{

    return request.post('/student/register', registerData);
}

// 提供登录接口调用函数
export const studentLoginService = (loginData) =>{

    return request.post('/student/login', loginData);
}

export const studentInfoService = () =>{

    return request.get('/stuInfo/getStuMsg');
}

export const studentInfoUpdateService = (stuInfo) =>{

    return request.post('/stuInfo/uploadMsg', stuInfo);
}

export const studentAvatarUpdateService = (imgUrl) =>{
    
    return request.post('/stuInfo/updateAvatar', imgUrl)
}