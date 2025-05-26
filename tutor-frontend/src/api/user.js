// 导入request.js工具
import request from '@/utils/request.js'

// 提供注册接口调用函数
export const userRegisterService = (registerData)=>{
    // 借助urlSearchParams完成传递
    const params = new URLSearchParams()

    // 遍历regiserData，解析json格式的数据
    for (let key in registerData){
        params.append(key, registerData[key]);
    }

    return request.post('/user/register', params);
}

// 提供调用登录接口的函数
export const userLoginService = (loginData)=>{
    const params = new URLSearchParams();
    for (let key in loginData){
        params.append(key, loginData[key]);
    }
    return request.post('/user/login', params)
}

// 获取用户详细信息
export const userInfoService= ()=>{
    return request.get('/user/userInfo')
}

// 修改用户的个人信息
export const userInfoUpdateService = (userInfo)=>{
    return request.put('/user/update', userInfo)
}

// 修改用户的头像
export const userAvatarUpdateService = (avatar) =>{
    const params = new URLSearchParams;
    params.append('avatarUrl',avatar);
    return request.patch('user/updateAvatar', params)
}