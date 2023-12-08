import Vue from 'vue'
import Vuex from 'vuex'

import { getState, setState } from '@/utils/storage'

Vue.use(Vuex)

export default {
  namespaced: true,
  // state: 所有组件共享的数据，类似data
  state: {
    isUserLoggedIn: getState() // 初始值为未登录
  },
  // getters：计算属性，类似get
  getters: {
    // isUserLoggedIn: (state) => state.isUserLoggedIn
  },
  // 更改 Vuex 的 store 中的状态，类似set
  mutations: {
    setUserLoggedState (state, status) {
      state.isUserLoggedIn = status
      setState(status)
    }
  },
  actions: {
  },
  modules: {
  }
}
