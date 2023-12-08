<template>
  <el-container class="container">
    <el-header class="header">
      <img src="@/assets/logo.jpg" alt="" class="logo">
      <div class="title">缅北征信中心</div>
      <div class="subtitle1">
        <el-button type="text">首页</el-button>
      </div>
      <div class="subtitle2">
        <el-button style="color: #fff;" type="text" @click="goMyCredit">我的信用</el-button>
      </div>
    </el-header>

    <el-container style="display: flex;">
      <el-descriptions class="personalInfo" title="个人信息" :column="3" border>
        <template slot="extra">
          <el-button type="primary" size="small">编辑</el-button>
        </template>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-user"></i>
            姓名
          </template>
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-mobile-phone"></i>
            手机号
          </template>
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-message"></i>
            邮箱
          </template>
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-male"></i>
            性别
          </template>
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-user-solid"></i>
            民族
          </template>
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-bank-card"></i>
            身份证号
          </template>
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-location-outline"></i>
            联系地址
          </template>
        </el-descriptions-item>
      </el-descriptions>
      <div ref="ringGaugeChart" class="ringGauge-container"></div>
    </el-container>
  </el-container>
</template>

<script>
import * as echarts from 'echarts'
import { mapState, mapMutations } from 'vuex'

import { getInfo } from '@/utils/storage'

export default {
  computed: {
    ...mapState('level', {
      userLevel: state => state.level
    })
  },
  data () {
    return {
      currentDate: '',
      user: ''
    }
  },
  created () {
    this.getCurrentDate()
    this.user = getInfo()
    // console.log(this.level)
  },
  mounted () {
    this.initRingGaugeChart()
  },
  methods: {
    ...mapMutations('level', ['setUserLevel']),

    goMyCredit () {
      this.$router.push({ path: '/myCredit' })
    },

    getCurrentDate () {
      const today = new Date()
      this.currentDate = today.toLocaleDateString()
    },

    initRingGaugeChart () {
      const ringGaugeContainer = this.$refs.ringGaugeChart

      const ringGaugeChart = echarts.init(ringGaugeContainer)

      const userScore = 910

      const option = {
        series: [
          {
            type: 'gauge',
            startAngle: 180,
            endAngle: 0,
            center: ['50%', '70%'],
            radius: '90%',
            min: 0,
            max: 1,
            splitNumber: 8,
            axisLine: {
              lineStyle: {
                width: 6,
                color: [
                  [0.25, '#FF6E76'],
                  [0.5, '#FDDD60'],
                  [0.75, '#58D9F9'],
                  [1, '#7CFFB2']
                ]
              }
            },
            pointer: {
              icon: 'path://M12.8,0.7l12,40.1H0.7L12.8,0.7z',
              length: '12%',
              width: 20,
              offsetCenter: [0, '-60%'],
              itemStyle: {
                color: 'auto'
              }
            },
            axisTick: {
              length: 12,
              lineStyle: {
                color: 'auto',
                width: 2
              }
            },
            splitLine: {
              length: 20,
              lineStyle: {
                color: 'auto',
                width: 5
              }
            },
            axisLabel: {
              color: '#464646',
              fontSize: 20,
              distance: -60,
              rotate: 'tangential',
              formatter: (value) => {
                if (value === 0.875) {
                  return '优秀'
                } else if (value === 0.625) {
                  return '良好'
                } else if (value === 0.375) {
                  return '一般'
                } else if (value === 0.125) {
                  return '很差'
                }
                return ''
              }
            },
            title: {
              offsetCenter: [0, '-10%'],
              fontSize: 20
            },
            detail: {
              fontSize: 30,
              offsetCenter: [0, '-35%'],
              valueAnimation: true,
              formatter: (value) => {
                if (value >= 750) {
                  this.setUserLevel('优秀')
                } else if (value >= 500) {
                  this.setUserLevel('良好')
                } else if (value >= 2500) {
                  this.setUserLevel('一般')
                } else {
                  this.setUserLevel('很差')
                }
                return Math.round(value) + ''
              },
              color: 'inherit'
            },
            data: [
              {
                value: userScore,
                name: this.currentDate
              }
            ]
          }
        ],
        graphic: [
          {
            type: 'group',
            left: 'center',
            top: '80%',
            children: [
              {
                type: 'text',
                style: {
                  text: '信用分每天更新一次',
                  font: '14px Arial',
                  fill: '#333',
                  width: 100,
                  height: 30,
                  textAlign: 'center',
                  textVerticalAlign: 'middle'
                }
              }]
          }
        ]
      }

      // const gaugeData = [
      //   {
      //     value: 910,
      //     name: 'Perfect',
      //     title: {
      //       offsetCenter: ['0%', '-30%']
      //     },
      //     detail: {
      //       valueAnimation: true,
      //       offsetCenter: ['0%', '-20%']
      //     }
      //   }
      // ]
      // const option = {
      //   series: [
      //     {
      //       type: 'gauge',
      //       radius: '100%',
      //       center: ['50%', '50%'],
      //       startAngle: 90,
      //       endAngle: -270,
      //       pointer: {
      //         show: false
      //       },
      //       progress: {
      //         show: true,
      //         overlap: false,
      //         roundCap: true,
      //         clip: false,
      //         itemStyle: {
      //           borderWidth: 1,
      //           borderColor: '#10DB4D',
      //           color: '#10DB4D'
      //         }
      //       },
      //       axisLine: {
      //         lineStyle: {
      //           width: 30
      //         }
      //       },
      //       splitLine: {
      //         show: false,
      //         distance: 0,
      //         length: 10
      //       },
      //       axisTick: {
      //         show: false
      //       },
      //       axisLabel: {
      //         show: false,
      //         distance: 50
      //       },
      //       data: gaugeData,
      //       title: {
      //         fontSize: 14
      //       },
      //       detail: {
      //         width: 50,
      //         height: 14,
      //         fontSize: 14,
      //         color: 'inherit',
      //         borderColor: 'inherit',
      //         borderRadius: 20,
      //         borderWidth: 1,
      //         formatter: '{value}分'
      //       },
      //       max: 1000
      //     }
      //   ],
      //   graphic: [
      //     {
      //       type: 'group',
      //       left: 'center',
      //       top: '10%',
      //       children: [
      //         {
      //           type: 'text',
      //           style: {
      //             text: '刘肖',
      //             font: '14px Arial',
      //             fill: '#333',
      //             textAlign: 'center',
      //             textVerticalAlign: 'middle'
      //           }
      //         }
      //       ]
      //     },
      //     {
      //       type: 'group',
      //       left: 'center',
      //       top: '20%',
      //       children: [
      //         {
      //           type: 'rect',
      //           shape: {
      //             width: 80,
      //             height: 30
      //           },
      //           style: {
      //             fill: '#fff',
      //             text: '切换账号',
      //             font: '14px Arial',
      //             width: 80,
      //             height: 30,
      //             textAlign: 'center',
      //             textVerticalAlign: 'middle'
      //           },
      //           onclick: function () {
      //             // 切换账号按钮点击事件
      //           }
      //         }
      //       ]
      //     },
      //     {
      //       type: 'group',
      //       left: 'center',
      //       top: '20%',
      //       children: [
      //         {
      //           type: 'rect',
      //           shape: {
      //             width: 60,
      //             height: 30
      //           },
      //           style: {
      //             fill: '#fff',
      //             text: '退出',
      //             font: '14px Arial',
      //             width: 60,
      //             height: 30,
      //             textAlign: 'center',
      //             textVerticalAlign: 'middle'
      //           },
      //           onclick: function () {
      //             // 退出按钮点击事件
      //           }
      //         }
      //       ]
      //     },
      //     {
      //       type: 'group',
      //       left: 'center',
      //       top: '70%',
      //       children: [
      //         {
      //           type: 'text',
      //           style: {
      //             text: this.currentDate,
      //             font: '14px Arial',
      //             fill: '#333',
      //             textAlign: 'center',
      //             textVerticalAlign: 'middle'
      //           }
      //         }
      //       ]
      //     },
      //     {
      //       type: 'group',
      //       left: 'center',
      //       top: '80%',
      //       children: [
      //         {
      //           type: 'rect',
      //           shape: {
      //             width: 100,
      //             height: 30
      //           },
      //           style: {
      //             fill: '#409EFF',
      //             text: '查看详情',
      //             font: '14px Arial',
      //             width: 100,
      //             height: 30,
      //             textAlign: 'center',
      //             textVerticalAlign: 'middle'
      //           },
      //           onclick: function () {
      //             // 查看详情按钮点击事件
      //           }
      //         }
      //       ]
      //     }
      //   ]
      // }
      ringGaugeChart.setOption(option)
    }
  }
}
</script>

<style lang="less" scoped>
@import '@/common/style.css';

  .personalInfo {
    margin: 100px;
    width: 100%;
  }

 .ringGauge-container {
  width: 100%;
  height: 500px;
 }
</style>
