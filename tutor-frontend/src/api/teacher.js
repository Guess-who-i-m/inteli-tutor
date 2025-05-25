// 导入request.js工具
import request from '@/utils/request.js'

// 提供注册接口调用函数
export const teacherRegisterService = (registerData)=>{

    return request.post('/teacher/register', registerData);
}


export const teacherLoginService = (loginData) =>{
    
    return request.post('/teacher/login', loginData);
}

export const teacherInfoService = () =>{
    
    return request.get('/tchInfo/getTchMsg');
}

export const teacherInfoUpdateService = (stuInfo) =>{

    return request.post('/tchInfo/uploadMsg', stuInfo);
}

export const teacherAvatarUpdateService = (imgUrl) =>{
    
    return request.post('/tchInfo/updateAvatar', imgUrl)
}
