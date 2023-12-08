import Vue from 'vue'
import Vuex from 'vuex'

import { getLevel, setLevel } from '@/utils/storage'

Vue.use(Vuex)

export default {
  namespaced: true,
  // state: 所有组件共享的数据，类似data
  state: {
    level: getLevel()
  },
  // getters：计算属性，类似get
  getters: {
  },
  // 更改 Vuex 的 store 中的状态，类似set
  mutations: {
    setUserLevel (state, obj) {
      state.level = obj
      setLevel(obj)
    }
  },
  actions: {
  },
  modules: {
  }
}
