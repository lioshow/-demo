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

export const getUser = async (info, successCallback, errorCallback) => {
  try {
    const response = await request.get('/user/getUser', { params: { phone: info.phone } })

    if (response.code === 200) {
      successCallback(response)
    } else {
      errorCallback(response)
    }
  } catch (error) {
    // 处理异常
    console.error('-_-!啊哦，出现意外:', error)
    errorCallback({ code: 500, msg: '-_-!啊哦，出现意外' })
  }
}

export const getMetrics = async (user, successCallback, errorCallback) => {
  try {
    const response = await request.get('/metrics/getMetrics', { params: { idNo: user.idcard.idNo } })

    if (response.code === 200) {
      successCallback(response)
    } else {
      errorCallback(response)
    }
  } catch (error) {
    // 处理异常
    console.error('-_-!啊哦，出现意外:', error)
    errorCallback({ code: 500, msg: '-_-!啊哦，出现意外' })
  }
}

export const updateUser = async (user, successCallback, errorCallback) => {
  try {
    const response = await request.put('/user/update', user)

    if (response.code === 200) {
      successCallback(response)
    } else {
      errorCallback(response)
    }
  } catch (error) {
    // 处理异常
    console.error('-_-!啊哦，出现意外:', error)
    errorCallback({ code: 500, msg: '-_-!啊哦，出现意外' })
  }
}

export const updateEducation = async (education, successCallback, errorCallback) => {
  try {
    const response = await request.put('/education/update', education)

    if (response.code === 200) {
      successCallback(response)
    } else {
      errorCallback(response)
    }
  } catch (error) {
    // 处理异常
    console.error('-_-!啊哦，出现意外:', error)
    errorCallback({ code: 500, msg: '-_-!啊哦，出现意外' })
  }
}

export const updateEmployment = async (employment, successCallback, errorCallback) => {
  try {
    const response = await request.put('/employment/update', employment)

    if (response.code === 200) {
      successCallback(response)
    } else {
      errorCallback(response)
    }
  } catch (error) {
    // 处理异常
    console.error('-_-!啊哦，出现意外:', error)
    errorCallback({ code: 500, msg: '-_-!啊哦，出现意外' })
  }
}

export const getRecentData = async (user, successCallback, errorCallback) => {
  try {
    const response = await request.get('/variation/getVariation', { params: { idNo: user.user.idNo } })

    if (response.code === 200) {
      successCallback(response)
    } else {
      errorCallback(response)
    }
  } catch (error) {
    // 处理异常
    console.error('-_-!啊哦，出现意外:', error)
    errorCallback({ code: 500, msg: '-_-!啊哦，出现意外' })
  }
}

export const getVariation = async (user, successCallback, errorCallback) => {
  try {
    const response = await request.get('/variation/getVariation', { params: { idNo: user.idNo } })

    if (response.code === 200) {
      successCallback(response)
    } else {
      errorCallback(response)
    }
  } catch (error) {
    // 处理异常
    console.error('-_-!啊哦，出现意外:', error)
    errorCallback({ code: 500, msg: '-_-!啊哦，出现意外' })
  }
}
