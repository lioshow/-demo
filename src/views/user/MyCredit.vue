<template>
  <el-container class="container">
    <el-header class="header">
      <img
        src="@/assets/logo.jpg"
        alt=""
        class="logo"
      >
      <div class="title">缅北征信中心</div>
      <div class="subtitle1">
        <el-button
          style="color: #fff;"
          type="text"
          @click="goHome"
        >首页</el-button>
      </div>
      <div class="subtitle2">
        <el-button type="text">我的信用</el-button>
      </div>
    </el-header>

    <el-container style="background-color: #f0f0f0;">
      <el-aside
        width="500px"
        class="left"
      >
        <el-row>
          <el-col
            :span="24"
            class="left-col"
          >
            <el-avatar
              v-if="!isUserLoggedIn"
              :size="80"
              style="font-size: 60px;"
              icon="el-icon-user-solid"
            ></el-avatar>
            <p v-else>欢迎您，{{ user.idcard.name }}</p>
            <el-button
              v-if="!isUserLoggedIn"
              class="left-button"
              type="primary"
              @click="login"
            >登录</el-button>
            <el-button
              v-else
              class="left-button"
              type="primary"
              @click="logout"
            >退出</el-button>
          </el-col>
          <el-col :span="24">
            <div
              ref="radarChart"
              class="radar-container"
            ></div>
          </el-col>
        </el-row>
      </el-aside>

      <!-- 右侧布局 -->
      <el-main class="right">
        <el-row style="height: 100%;">
          <el-col :span="24">
            <el-button
              v-if="!isUserLoggedIn"
              type="primary"
              @click="login"
            ><i class="el-icon-chat-dot-square"></i>请登录查看信用详情</el-button>
            <div
              v-else
              style="display: flex;"
            >
              <div>
                <i class="el-icon-chat-dot-square"></i>
                当前信用分为：{{ user.user.score }}，信用水平：
                <span
                  :style="{ color: getFontColor(getUserLevel) }">{{ getUserLevel }}</span>
              </div>
              <el-button
                style="margin-left: 50px;"
                type="success"
                @click="gotoReport"
              >生成信用报告</el-button>
            </div>
          </el-col>
          <el-col :span="24">
            <!-- 折线图容器 -->
            <div
              ref="lineChart"
              class="line-container"
            ></div>
          </el-col>
        </el-row>
      </el-main>
    </el-container>

    <!-- 登录弹框 -->
    <el-dialog
      style="text-align: center;"
      title="欢迎使用缅北征信平台"
      :visible.sync="dialogVisible"
      width="400px"
    >
      <!-- 表单 -->
      <el-form
        :model="loginForm"
        ref="loginForm"
        :rules="loginRules"
      >
        <el-form-item
          label="手机号"
          prop="phone"
        >
          <el-input
            v-model="loginForm.phone"
            prefix-icon="el-icon-mobile"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item
          label="密码"
          prop="password"
        >
          <el-input
            type="password"
            v-model="loginForm.password"
            prefix-icon="el-icon-lock"
            autocomplete="off"
            show-password
          ></el-input>
        </el-form-item>
        <!-- 验证码 -->
        <el-form-item prop="validCode">
          <div style="display: flex;align-items: center;">
            <el-input
              v-model="loginForm.validCode"
              prefix-icon="el-icon-circle-check"
              style="flex: 1"
              placeholder="请输入验证码"
            ></el-input>
            <div style="flex: 1;margin-left: 20px;">
              <valid-code
                @input="getCode"
                ref="validCode"
              />
            </div>
          </div>
        </el-form-item>
      </el-form>

      <!-- 忘记密码和没有账号 -->
      <div align="left">
        <el-button
          style="font-size: 10px;"
          type="text"
          @click="forgetPassword"
        >忘记密码?</el-button>
        <el-button
          style="font-size: 10px;"
          type="text"
          @click="noAccount"
        >没有账号?</el-button>
      </div>

      <!-- 登录按钮 -->
      <el-button
        :loading="loading"
        type="primary"
        @click="login"
      >登录</el-button>

      <!-- 登录协议 -->
      <div class="login-agreement">
        注意：登录即代表您同意使用我们的服务，且自愿服从相关条例。详细说明请看
        <el-button
          style="font-size: 12px;"
          type="text"
          @click="goLoginAgreement"
        >《缅北征信服务平台协议》</el-button>
      </div>
    </el-dialog>

    <!-- <div v-if="isAssessing" class="load-style"> -->
    <!-- <el-dialog :visible.sync="assessmentVisible" title="正在为您进行信用评估，请勿离开..." :show-close="false" :close-on-click-modal="false" :modal="true">
        <el-progress :percentage="progress"></el-progress>
        <div class="access-button" align="center">
          <el-button type="success" :disabled="progress==100 ? false : true" @click="goUser">评估完成，进入主页</el-button>
        </div>
      </el-dialog> -->
    <!-- </div> -->
  </el-container>
</template>

<script>
import * as echarts from 'echarts'
import { getMetrics, getUser, getRecentData } from '@/api/index.js'
import ValidCode from '@/components/ValidCode.vue'

import { mapState, mapMutations } from 'vuex'
import { getInfo, getLevel, removeInfo, removeLevel, removeState } from '@/utils/storage'

export default {
  components: {
    ValidCode
  },
  computed: {
    ...mapState('userLoggedState', {
      isUserLoggedIn: state => state.isUserLoggedIn
    }),

    getUserLevel () {
      return JSON.parse(getLevel()).trim()
    }
  },
  data () {
    // 验证码校验
    const validateCode = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入验证码'))
      } else if (value.toLowerCase() !== this.code.toLowerCase()) {
        callback(new Error('验证码错误'))
      } else {
        callback()
      }
    }
    return {
      info: '',
      user: {
        user: {
          idNo: '',
          score: ''
        },
        idcard: {
          name: ''
        }
      },
      radarData: [],
      lineData: [],
      recentDates: [],
      dialogVisible: false,
      code: '', // 组件传过来的验证码
      loginForm: {
        validCode: '', // 验证码
        phone: '',
        password: ''
      },
      loginRules: {
        phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
        validCode: [{ validator: validateCode, trigger: 'blur' }]
      },
      loading: false,
      assessmentVisible: false,
      progress: 0,
      isAssessing: true
    }
  },
  created () {
    // this.creditAssessment()
    this.getRecentDates()
    if (this.isUserLoggedIn) {
      this.info = getInfo()
      this.getUserInfo()
    }
  },
  mounted () {
    // 在组件挂载后，初始化雷达图和折线图
    // console.log(this.isUserLoggedIn)
    if (!this.isUserLoggedIn) {
      this.initRadarChart()
      this.initLineChart()
    }
  },
  methods: {
    ...mapMutations('userLoggedState', ['setUserLoggedState']),

    getUserInfo () {
      const info = JSON.parse(this.info)

      getUser(
        info,
        (res) => {
          this.user = res.data
          console.log(this.user)
          getMetrics(
            this.user,
            (res) => {
              // console.log(res)
              const { id, idNo, score, ...radarData } = res.data
              // const radarArray = Object.keys(radarData).map(key => radarData[key])
              this.radarData = radarData
              // 定义键的中英文映射关系
              const keyMapping = {
                ability: '履约能力',
                account: '账号信息',
                activity: '平台活跃',
                behavior: '行为特质',
                user: '用户特质'
              }

              // 将英文键映射为中文
              for (const englishKey in this.radarData) {
                if (Object.prototype.hasOwnProperty.call(this.radarData, englishKey) && keyMapping[englishKey]) {
                  this.radarData[keyMapping[englishKey]] = this.radarData[englishKey]
                  delete this.radarData[englishKey]
                }
              }
              // console.log(this.radarData)
              this.initRadarChart()
              this.getRecentData()
            },
            (res) => {
              this.$message.error(res.msg)
            }
          )
        },
        (res) => {
          this.$message.error(res.msg)
        }
      )
    },

    initRadarChart () {
      // 获取图表容器
      const chartContainer = this.$refs.radarChart
      // 初始化图表
      const radarChart = echarts.init(chartContainer)
      // 图表配置
      const option = {
        // title: {
        //   text: '雷达图示例'
        // },
        tooltip: {},
        // legend: {
        //   data: ['某人']
        // },
        radar: {
          // 雷达图的指示器，根据需要设置
          // indicator: [
          //   { name: '平台活跃', max: 200 },
          //   { name: '账号信息', max: 200 },
          //   { name: '履约能力', max: 200 },
          //   { name: '用户特质', max: 200 },
          //   { name: '行为特质', max: 200 }
          // ]
          indicator: this.isUserLoggedIn
            ? Object.keys(this.radarData).map(key => ({ name: key, max: 200 }))
            : [
              { name: '平台活跃', max: 200 },
              { name: '账号信息', max: 200 },
              { name: '履约能力', max: 200 },
              { name: '用户特质', max: 200 },
              { name: '行为特质', max: 200 }
            ]
        },
        series: [
          {
            name: '雷达图',
            type: 'radar',
            areaStyle: {
              color: '#55e195',
              opacity: 0.8 // 将透明度设置为0，隐藏实心区域
            },
            // 不显示雷达线
            lineStyle: {
              // color: 'green',
              opacity: 0 // 将透明度设置为0，隐藏雷达线
            },
            axisPointer: {
              type: 'cross', // 设置为'cross'类型，显示在雷达图中心
              label: {
                show: true
              }
            },
            // // 不显示数据点
            symbol: 'none', // 设置数据点不显示
            // 数据
            data: [
              {
                value: Object.values(this.radarData)
              }
            ]
          }
        ]
      }
      // 使用刚指定的配置项和数据显示图表。
      radarChart.setOption(option)
    },

    initLineChart () {
      const lineChartContainer = this.$refs.lineChart
      // 初始化ECharts实例
      const lineChart = echarts.init(lineChartContainer)
      // 定义折线图的配置项
      const options = {
        title: {
          text: '近十五天信用变化'
        },
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          data: this.recentDates,
          axisLabel: {
            interval: 0 // 强制显示所有标签
            // rotate: -45 // 旋转标签，确保它们不重叠
          }
        },
        yAxis: {
          type: 'value',
          min: 0,
          max: 1000,
          interval: 200
        },
        series: [{
          name: '示例数据',
          type: 'line',
          data: this.lineData.reverse()
        }]
      }
      // 使用配置项绘制折线图
      lineChart.setOption(options)
    },

    getCode (code) {
      this.code = code
      // console.log(code)
    },

    goHome () {
      if (this.isUserLoggedIn) this.$router.push({ path: '/user' })
      else this.$router.push({ path: '/' })
    },

    getRecentData () {
      getRecentData(
        this.user,
        (res) => {
          console.log(res)
          for (let i = 0; i < res.data.length; i++) {
            this.lineData.push(res.data[i].score)
          }
          if (this.lineData.length < 15) {
            let i = this.lineData.length
            this.lineData.reverse()
            while (i < 15) {
              this.lineData.push(this.lineData[this.lineData.length - 1])
              i++
            }
          }
          this.initLineChart()
          const varScore = this.lineData[this.lineData.length - 1] - this.lineData[this.lineData.length - 2]
          window.localStorage.setItem('varScore', JSON.stringify(varScore))
          console.log(this.lineData)
        },
        (res) => {
          this.$message.error(res.msg)
        }
      )
    },

    getRecentDates () {
      const currentDate = new Date()
      for (let i = 0; i < 15; i++) {
        const date = new Date()
        date.setDate(currentDate.getDate() - i)
        // const month = (date.getMonth() + 1).toString().padStart(2, '0')
        const day = date.getDate().toString().padStart(2, '0')
        this.recentDates.push(`${day}`)
      }
      this.recentDates.reverse()
    },

    goLoginAgreement () {
      this.$router.push({ path: '/agreement' })
    },

    gotoReport () {
      this.$router.push({ path: '/report' })
    },

    // 忘记密码
    forgetPassword () {
      this.$router.push({ path: '/reset' })
    },

    // 没有账号
    noAccount () {
      this.$router.push({ path: '/register' })
    },

    // 登录
    login () {
      // this.$refs.loginForm.validate(async (valid) => {
      //   if (valid) {
      //     this.loading = true

      //     loginUser(
      //       this.loginForm,
      //       (res) => {
      //         this.loading = false
      //         this.$message.success(res.msg)
      //         this.$store.commit('user/setUserInfo', res.data)
      //         this.setUserLoggedState(true)
      //         if (res.data.days === 1) {
      //           this.dialogVisible = false
      //           this.creditAssessment()
      //         }
      //         location.reload()
      //       },
      //       (res) => {
      //         this.loading = false
      //         this.$message.error(res.msg)
      //         // 刷新验证码
      //         this.$refs.validCode.refreshCode()
      //       }
      //     )
      //   }
      // })
      this.goHome()
    },

    creditAssessment () {
      this.assessmentVisible = true
      // 模拟加载过程，每100毫秒增加1%的进度
      const interval = setInterval(() => {
        this.progress += 1
        if (this.progress >= 100) {
          clearInterval(interval)
        }
      }, 100)
    },

    // 登出
    logout () {
      this.$confirm('确定退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('退出成功')
        this.setUserLoggedState(false)
        removeInfo()
        removeLevel()
        removeState()
        this.$router.replace({ path: '/' })
      }).catch(() => {
        this.$message.info('操作取消')
      })
    },

    getFontColor (level) {
      if (level === '优秀') {
        return '#67C23A'
      } else if (level === '良好') {
        return '#409EFF'
      } else if (level === '一般') {
        return '#E6A23C'
      } else {
        return '#F56C6C'
      }
    }
  }
}
</script>

<style lang="less" scoped>
@import "@/common/style.css";

.left {
  background-color: #fff;
  border-radius: 30px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
  margin: 30px;
  padding: 50px; /* 左侧内边距 */
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.left-col {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-left: 30px;
}

.left-button {
  position: relative;
  right: 190px;
}

.right {
  background-color: #fff;
  border-radius: 30px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
  margin: 30px;
  padding: 50px; /* 右侧内边距 */
}

.radar-container {
  width: 100%;
  height: 300px;
  margin-top: 50px;
}

.line-container {
  width: 100%;
  height: 400px;
  margin-top: 50px;
}

.login-agreement {
  text-align: left;
  margin-top: 10px;
  font-size: 12px;
  color: #999;
}

.load-style {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: #fff;
  backdrop-filter: blur(4px);
  z-index: 99;
}

.access-button {
  margin: 50px auto;
  align-items: center;
}
</style>
