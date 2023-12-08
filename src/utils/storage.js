// 用户的key
const INFO_KEY = 'user_key'
// 登录状态的key
const USERLOGGEDINSTATE_KEY = 'userLoggedIn_key'
// 信用等级的key
const LEVEL_KEY = 'userLevel_key'

// 用户信息的存取删
export const getInfo = () => {
  const result = localStorage.getItem(INFO_KEY) || ''
  return result
}

export const setInfo = (obj) => {
  localStorage.setItem(INFO_KEY, JSON.stringify(obj))
}

export const removeInfo = () => {
  localStorage.removeItem(INFO_KEY)
}

// 登录状态的存取
export const getState = () => {
  const result = localStorage.getItem(USERLOGGEDINSTATE_KEY) || false // 初始为未登录
  return result
}

export const setState = (obj) => {
  localStorage.setItem(USERLOGGEDINSTATE_KEY, JSON.stringify(obj))
}

export const removeState = () => {
  localStorage.removeItem(USERLOGGEDINSTATE_KEY)
}

export const getLevel = () => {
  const result = localStorage.getItem(LEVEL_KEY) || '未评级'
  return result
}

export const setLevel = (obj) => {
  localStorage.setItem(LEVEL_KEY, JSON.stringify(obj))
}

export const removeLevel = () => {
  localStorage.removeItem(LEVEL_KEY)
}
