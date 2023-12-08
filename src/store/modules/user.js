import Vue from 'vue'
import Vuex from 'vuex'

import { getInfo, setInfo } from '@/utils/storage'

Vue.use(Vuex)

export default {
  namespaced: true,
  // state: 所有组件共享的数据，类似data
  state: {
    userInfo: getInfo()
  },
  // getters：计算属性，类似get
  getters: {
  },
  // 更改 Vuex 的 store 中的状态，类似set
  mutations: {
    setUserInfo (state, obj) {
      state.userInfo = obj
      setInfo(obj)
    }
  },
  actions: {
  },
  modules: {
  }
}
