// 存放所有接口请求
// 封装api模块与页面逻辑分离
// 相同请求可直接复用
// 请求进行了统一管理
import request from '@/utils/request'

export const loginUser = async (loginForm, successCallback, errorCallback) => {
  try {
    // 处理登录逻辑
    const { validCode, ...formData } = loginForm
    const response = await request.post('/user/login', formData)

    if (response.code === 200) {
      localStorage.setItem('user', JSON.stringify(response.data))
      successCallback(response)
    } else {
      errorCallback(response)
    }
  } catch (error) {
    // 处理异常
    console.error('-_-!啊哦，登录时出现意外:', error)
    errorCallback({ code: 500, msg: '-_-!啊哦，登录时出现意外' })
  }
}

export const registerUser = async (registerData, successCallback, errorCallback) => {
  try {
    // 处理注册逻辑
    const response = await request.post('/user/register', registerData)

    if (response.code === 200) {
      successCallback(response)
    } else {
      errorCallback(response)
    }
  } catch (error) {
    // 处理异常
    console.error('-_-!啊哦，注册时出现意外:', error)
    errorCallback({ code: 500, msg: '-_-!啊哦，注册时出现意外' })
  }
}

export const sendEmailCode = async (email, successCallback, errorCallback) => {
  try {
    const response = await request.get('/user/sendEmailCode', email)

    if (response.code === 200) {
      successCallback(response)
    } else {
      errorCallback(response)
    }
  } catch (error) {
    // 处理异常
    console.error('-_-!啊哦，发送邮件时出现意外:', error)
    errorCallback({ code: 500, msg: '-_-!啊哦，发送邮件时出现意外' })
  }
}
