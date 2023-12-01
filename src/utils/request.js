import router from '@/router'
import axios from 'axios'
// 将axios封装成模块

// 创建axios实例，对创建出来的实例进行自定义配置
const request = axios.create({
  baseURL: 'http://localhost:8090', // 后端接口
  timeout: 3000
})

// request拦截器
// 可以自请求发送前对请求做一些处理，比如统一加token，对请求参数统一加密
request.interceptors.request.use(config => {
  config.headers['Content-Type'] = 'application/json;charset=utf-8'
  const user = JSON.parse(localStorage.getItem('user') || '{}')
  config.headers.token = user.token // 设置请求头

  return config
}, error => {
  console.log('request error: ' + error)
  return Promise.reject(error)
})

// response拦截器
// 可以在接口响应后统一处理结果
request.interceptors.response.use(response => {
  let res = response.data

  // 兼容服务器端返回的字符串数据
  if (typeof res === 'string') {
    res = res ? JSON.parse(res) : res
  }

  if (res.code === 401) {
    this.$message.warning(res.msg)
    router.push('/login')
  }

  return res
}, error => {
  console.log('response error: ' + error)
  return Promise.reject(error)
})

// 导出配置好的实例
export default request
