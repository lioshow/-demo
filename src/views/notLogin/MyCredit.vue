<template>
  <el-container class="container">
    <el-header class="header">
      <img src="@/assets/logo.jpg" alt="" class="logo">
      <div class="title">缅北征信中心</div>
      <div class="subtitle1">
        <el-button style="color: #fff;" type="text" @click="goHome">首页</el-button>
      </div>
      <div class="subtitle2">
        <el-button type="text">我的信用</el-button>
      </div>
    </el-header>

    <el-container style="background-color: #f0f0f0;">
      <el-aside width="500px" class="left">
        <el-row>
          <el-col :span="24" class="left-col">
            <el-avatar :size="80" style="font-size: 60px;" icon="el-icon-user-solid"></el-avatar>
            <el-button class="left-button" type="primary" @click="dialogVisible = true">登录</el-button>
          </el-col>
          <el-col :span="24">
            <div ref="radarChart" class="radar-container"></div>
          </el-col>
        </el-row>
      </el-aside>

      <!-- 右侧布局 -->
      <el-main class="right">
        <el-row style="height: 100%;">
          <el-col :span="12">
            <el-button type="primary" @click="dialogVisible = true"><i class="el-icon-chat-dot-square"></i>请登录查看信用详情</el-button>
          </el-col>
          <el-col :span="24">
            <!-- 折线图容器 -->
            <div ref="lineChart" class="line-container"></div>
          </el-col>
        </el-row>
      </el-main>
    </el-container>

    <!-- 登录弹框 -->
    <el-dialog style="text-align: center;" title="欢迎使用缅北征信平台" :visible.sync="dialogVisible" width="400px">
      <!-- 表单 -->
      <el-form :model="loginForm" ref="loginForm" :rules="loginRules">
        <el-form-item label="账号" prop="phone">
          <el-input v-model="loginForm.phone" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="loginForm.password" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>

      <!-- 忘记密码和没有账号 -->
      <div align="left">
        <el-button style="font-size: 10px;" type="text" @click="forgetPassword">忘记密码?</el-button>
        <el-button style="font-size: 10px;" type="text" @click="noAccount">没有账号?</el-button>
      </div>

        <!-- 登录按钮 -->
      <el-button type="primary" @click="login">登录</el-button>

      <!-- 登录协议 -->
      <div class="login-agreement">
        注意：登录即代表您同意使用我们的服务，且自愿服从相关条例。详细说明请看
        <el-button style="font-size: 12px;" type="text" @click="goLoginAgreement">《缅北征信服务平台协议》</el-button>
      </div>
    </el-dialog>
  </el-container>
</template>

<script>
import * as echarts from 'echarts'
export default {
  data () {
    return {
      radarData: [],
      recentDates: [],
      dialogVisible: false,
      loginForm: {
        phone: '',
        password: ''
      },
      loginRules: {
        phone: [{ required: true, message: '请输入账号', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
      }
    }
  },
  created () {
    this.getRecentDates()
  },
  mounted () {
    // 在组件挂载后，初始化雷达图和折线图
    this.initRadarChart()
    this.initLineChart()
  },
  methods: {
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
          indicator: [
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
            // areaStyle: {
            //   opacity: 0 // 将透明度设置为0，隐藏实心区域

            // },
            // // 不显示雷达线
            // lineStyle: {
            //   opacity: 0 // 将透明度设置为0，隐藏雷达线

            // },
            // // 不显示数据点
            symbol: 'none', // 设置数据点不显示
            // 数据
            data: this.radarData
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
          data: []
        }]
      }
      // 使用配置项绘制折线图
      lineChart.setOption(options)
    },

    goHome () {
      this.$router.push({ path: '/' })
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

    forgetPassword () {
      // 处理忘记密码逻辑
    },
    noAccount () {
      // 处理没有账号逻辑
    },
    login () {
      // 处理登录逻辑，可以在这里提交表单等
      // 如果表单验证通过，可以关闭弹框：this.dialogVisible = false;
    }
  }
}
</script>

<style lang="less" scoped>
@import '@/common/style.css';

  .left {
    background-color: #fff;
    border-radius: 30px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
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
    box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
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
</style>
