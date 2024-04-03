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
        <el-button
          style="color: #fff;"
          type="text"
          @click="goMyCredit"
        >我的信用</el-button>
      </div>
    </el-header>
    <div style="font-size: 24px;">
      <b>我的信用分为：</b>
      <span
        style="font-size: 30px; "
        :style="{ color: getColor(score) }"
      >{{ score }}</span>
    </div>
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
            <b style="font-size: 20px;">分数变动&nbsp;&nbsp; {{ count }}
              &nbsp;&nbsp;分</b>
          </el-col>
          <el-col :span="24">
            <div
              ref="myChart"
              class="radar-container"
            ></div>
          </el-col>
        </el-row>
      </el-aside>

      <!-- 右侧布局 -->
      <el-main class="right">
        <el-row style="height: 100%;">
          <el-col :span="24">
            <div style="display: flex;">
              <div style="height: 300px;">
                <b style="font-size: 20px;">信用服务记录</b>
                <p
                  v-for="text in texts"
                  :key="text"
                >{{ text }}</p>
              </div>
            </div>
          </el-col>

        </el-row>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import * as echarts from 'echarts'

import { getUser, getVariation } from '@/api/index'

import { getInfo } from '@/utils/storage'

export default {
  data () {
    return {
      info: '',
      user: '',
      idcard: '',
      employment: '',
      education: '',
      score: 0,
      // count: 0,
      count: localStorage.getItem('varScore') > 0 ? localStorage.getItem('varScore') : -localStorage.getItem('varScore'),
      texts: [],
      intervalTime: 1000,
      currentIndex: 0
    }
  },
  created () {
    this.info = JSON.parse(getInfo())
    this.getUserInfo()
  },
  mounted () {
    // 在组件挂载后，初始化得分环
    // console.log(this.isUserLoggedIn)
    if (this.currentIndex === 0 || this.texts[this.currentIndex - 1] != null) {
      setInterval(() => {
        this.updateText()
      }, this.intervalTime)
    }
  },
  methods: {
    difference () {
      const scoreCount = this.score - this.score_pre
      this.count = scoreCount > 0 ? scoreCount : (-1 * scoreCount)
    },
    updateText () {
      //
      const modify = '您累计修改'
      const modifyUser = '个人信息 ' + this.user.modifyTimes + ' 次'
      const modifyIdcard = '实名信息 ' + this.idcard.modifyTimes + ' 次'
      const modifyEducation = '教育情况 ' + this.education.modifyTimes + ' 次'
      const modifyEmployment = '就业情况 ' + this.employment.modifyTimes + ' 次'
      const texts = [modify, modifyUser, modifyIdcard, modifyEducation, modifyEmployment, '请注意减少信息的修改次数']
      if (texts[this.currentIndex] != null) {
        this.texts.push(texts[this.currentIndex++])
      }
    },
    goMyCredit () {
      this.$router.push({ path: '/myCredit' })
    },

    goHome () {
      this.$router.push({ path: '/user' })
    },

    getUserInfo () {
      getUser(
        this.info,
        (res) => {
          this.user = res.data.user
          this.idcard = res.data.idcard
          this.employment = res.data.employment
          this.education = res.data.education
          this.score = this.user.score
          // this.difference()
          this.initRing()
          this.getVariation()
        },
        (res) => {
          this.$message.error(res.msg)
        }
      )
    },

    initRing () {
      const chartDom = this.$refs.myChart
      const myChart = echarts.init(chartDom)
      const gaugeData = [
        {
          value: this.score,
          name: '',
          title: {
            offsetCenter: ['0%', '0%']
          },
          detail: {
            valueAnimation: true,
            offsetCenter: ['0%', '0%'],
            color: this.getColor(this.score) // 设置分数颜色
          }
        }
      ]
      const option = {
        series: [
          {
            max: 1000,
            type: 'gauge',
            startAngle: 90,
            endAngle: -270,
            pointer: {
              show: false
            },
            progress: {
              show: true,
              overlap: false,
              roundCap: true,
              clip: false,
              itemStyle: {
                borderWidth: 0,
                borderColor: '#464646',
                color: this.getColor(this.score) // 设置环的颜色
              }
            },
            axisLine: {
              lineStyle: {
                width: 40
              }
            },
            splitLine: {
              show: false,
              distance: 0,
              length: 10
            },
            axisTick: {
              show: false
            },
            axisLabel: {
              show: false,
              distance: 50
            },
            data: gaugeData,
            title: {
              fontSize: 14
            },
            detail: {
              // width: 100,
              // height: 40,
              fontSize: 40,
              color: 'green',
              // borderRadius: 40,
              borderWidth: 0,
              borderColor: this.getColor(this.score),
              formatter: '{value}'
            }
          }
        ]

      }
      myChart.setOption(option)
    },

    getColor (score) {
      if (score < 250) {
        return '#F56C6C'
      } else if (score < 500) {
        return '#E6A23C'
      } else if (score < 750) {
        return '#409EFF'
      } else {
        return '#67C23A'
      }
    },

    getVariation () {
      // console.log(this.user)
      getVariation(
        this.user,
        (res) => {
          console.log(res)
        },
        (res) => {
          this.$message.error(res.msg)
        }
      )
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
</style>
