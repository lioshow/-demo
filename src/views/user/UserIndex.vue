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
        <el-button type="text">首页</el-button>
      </div>
      <div class="subtitle2">
        <el-button
          style="color: #fff;"
          type="text"
          @click="goMyCredit"
        >我的信用</el-button>
      </div>
    </el-header>

    <el-container style="display: flex;">
      <el-descriptions
        class="personalInfo"
        title="个人信息（仅展示部分）"
        :column="3"
        border
      >
        <template slot="extra">
          <el-button
            type="primary"
            size="small"
            @click="dialogVisible = true"
          >编辑</el-button>
        </template>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-user"></i>
            姓名
          </template>
          {{ idcard.name }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-mobile-phone"></i>
            手机号
          </template>
          {{ user.phone }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-message"></i>
            邮箱
          </template>
          {{ user.email }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-male"></i>
            性别
          </template>
          {{ idcard.gender }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-user-solid"></i>
            职业
          </template>
          {{ employment.type }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-bank-card"></i>
            身份证号
          </template>
          {{ idcard.idNo }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-location-outline"></i>
            联系地址
          </template>
          {{ idcard.address }}
        </el-descriptions-item>
      </el-descriptions>
      <div
        ref="ringGaugeChart"
        class="ringGauge-container"
      ></div>
    </el-container>

    <!-- 主弹框 -->
    <el-dialog
      :visible.sync="dialogVisible"
      title="编辑信息"
      :append-to-body="true"
    >
      <!-- el-tabs 区域 -->
      <el-tabs v-model="activeTab">
        <el-tab-pane
          label="个人信息"
          name="user"
        >
          <!-- 个人信息表单 -->
          <el-form
            :model="user"
            label-width="80px"
            ref="personalForm"
            :rules="rules"
          >
            <el-form
              label-width="80px"
              disabled
            >
              <el-form-item
                label="手机号"
                prop="phone"
              >
                <el-input v-model="user.phone"></el-input>
              </el-form-item>
            </el-form>
            <el-form-item
              label="密码"
              prop="password"
            >
              <el-input
                type="password"
                v-model="user.password"
                show-password
              ></el-input>
            </el-form-item>
            <el-form-item
              label="邮箱"
              prop="email"
            >
              <el-input v-model="user.email"></el-input>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <el-tab-pane
          label="实名信息"
          name="idcard"
        >
          <!-- 实名信息表单 -->
          <div
            v-if="!idcardModify"
            style="display: flex; flex-direction: column; justify-content: center;"
          >
            <el-form
              :model="idcard"
              label-width="80px"
              disabled
            >
              <el-form-item label="姓名">
                <el-input v-model="idcard.name"></el-input>
              </el-form-item>
              <el-form-item label="性别">
                <el-input v-model="idcard.gender"></el-input>
              </el-form-item>
              <el-form-item label="民族">
                <el-input v-model="idcard.ethnicity"></el-input>
              </el-form-item>
              <el-form-item label="身份证号">
                <el-input v-model="idcard.idNo"></el-input>
              </el-form-item>
              <el-form-item label="出生日期">
                <el-input v-model="idcard.birthDate"></el-input>
              </el-form-item>
              <el-form-item label="家庭住址">
                <el-input v-model="idcard.address"></el-input>
              </el-form-item>
            </el-form>
            <div style="margin: 20px auto;">
              <el-button
                type="primary"
                @click="reUpload"
              >重新上传</el-button>
            </div>
          </div>
          <div
            v-else
            style="display: flex; flex-direction: column; justify-content: center;"
          >
            <!-- <el-dialog :visible.sync="uploadDialogVisible" title="上传信息" :append-to-body="true" @before-close="uploadDialogClose"> -->
            <el-upload
              class="upload-demo"
              action="http://localhost:8090/idCard/ocr"
              :before-upload="beforeUpload"
              :on-success="handleSuccess"
              :on-error="handleError"
            >
              <el-button
                size="medium"
                type="primary"
              >点击上传</el-button>
            </el-upload>

            <div class="image">
              <div
                v-for="(image, index) in imageArray"
                :key="index"
                class="uploaded-image-container"
              >
                <img
                  :src="image.url"
                  alt=""
                  style="width: 100px; height: 100px;"
                >
              </div>
            </div>

            <div
              slot="footer"
              class="dialog-footer"
            >
              <el-button
                v-if="imageArray.length > 0"
                @click="reUpload"
              >重新上传</el-button>
              <el-button
                v-if="imageArray.length > 0"
                @click="confirmUpload"
                type="success"
              >确定</el-button>
            </div>
            <!-- </el-dialog> -->
          </div>
        </el-tab-pane>

        <el-tab-pane
          label="就业情况"
          name="employment"
        >
          <el-form
            ref="employmentForm"
            label-width="100px"
            :model="employment"
            :rules="employmentRules"
          >
            <el-form-item
              label="就业状况"
              prop="status"
            >
              <el-select
                v-model="employment.status"
                placeholder="请选择就业状况"
              >
                <!-- 就业状况选项 -->
                <el-option
                  label="在职"
                  value="在职"
                ></el-option>
                <el-option
                  label="待业"
                  value="待业"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item
              label="职业类别"
              prop="type"
              v-if="employment.status === '在职'"
            >
              <el-select
                v-model="employment.type"
                placeholder="请选择职位"
              >
                <!-- 职业类别选项 -->
                <el-option
                  label="党的机关、国家机关、群众团体和社会组织、企事业单位负责人"
                  value="党的机关、国家机关、群众团体和社会组织、企事业单位负责人"
                ></el-option>
                <el-option
                  label="专业技术人员"
                  value="专业技术人员"
                ></el-option>
                <el-option
                  label="办事人员和有关人员"
                  value="办事人员和有关人员"
                ></el-option>
                <el-option
                  label="社会生产服务和生活服务人员"
                  value="社会生产服务和生活服务人员"
                ></el-option>
                <el-option
                  label="农、林、牧、渔业生产及辅助人员"
                  value="农、林、牧、渔业生产及辅助人员"
                ></el-option>
                <el-option
                  label="生产制造及有关人员"
                  value="生产制造及有关人员"
                ></el-option>
                <el-option
                  label="军人"
                  value="军人"
                ></el-option>
                <el-option
                  label="学生"
                  value="学生"
                ></el-option>
                <el-option
                  label="退休/无业"
                  value="退休/无业"
                ></el-option>
                <el-option
                  label="不便分类的其他从业人员"
                  value="不便分类的其他从业人"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane
          label="教育情况"
          name="education"
        >
          <el-form
            ref="educationForm"
            label-width="100px"
            :model="education"
            :rules="educationRules"
          >
            <el-form-item
              label="学历"
              prop="degree"
            >
              <el-select
                v-model="education.degree"
                placeholder="请选择学历"
              >
                <!-- 学历选项 -->
                <el-option
                  label="小学"
                  value="小学"
                ></el-option>
                <el-option
                  label="初中"
                  value="初中"
                ></el-option>
                <el-option
                  label="高中"
                  value="高中"
                ></el-option>
                <el-option
                  label="大专"
                  value="大专"
                ></el-option>
                <el-option
                  label="本科"
                  value="本科"
                ></el-option>
                <el-option
                  label="研究生"
                  value="研究生"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item
              label="学位"
              prop="title"
              v-if="education.degree === '本科' || education.degree === '研究生'"
            >
              <el-select
                v-model="education.title"
                placeholder="请选择学位"
              >
                <!-- 学位选项 -->
                <el-option
                  label="学士"
                  value="学士"
                ></el-option>
                <el-option
                  label="硕士"
                  value="硕士"
                ></el-option>
                <el-option
                  label="博士"
                  value="博士"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item
              label="毕业院校"
              prop="school"
            >
              <el-input
                v-model="education.school"
                placeholder="请输入毕业院校"
              ></el-input>
            </el-form-item>
          </el-form>
        </el-tab-pane>

      </el-tabs>

      <!-- 按钮区域 -->
      <span
        slot="footer"
        class="dialog-footer"
      >
        <el-button @click="cancel">取消</el-button>
        <el-button
          type="primary"
          @click="saveForm"
        >保存</el-button>
      </span>
    </el-dialog>

  </el-container>
</template>

<script>
import * as echarts from 'echarts'
import { mapState, mapMutations } from 'vuex'

import { getUser, updateEducation, updateEmployment, updateUser } from '@/api/index'

import { getInfo } from '@/utils/storage'

import { isEmail } from '@/api/email'

export default {
  computed: {
    ...mapState('level', {
      userLevel: state => state.level
    })
  },
  data () {
    const validateEmail = (rule, value, callback) => {
      if (!isEmail(value)) {
        callback(new Error('邮箱格式错误'))
      } else {
        callback()
      }
    }
    return {
      currentDate: '',
      info: '',
      user: '',
      originalUser: '', // 原始用户数据
      idcard: '',
      originalIdCard: '', // 原始身份证信息
      employment: '',
      originalEmployment: '', // 原始就业情况数据
      education: '',
      originalEducation: '', // 原始教育情况数据
      activeTab: 'user',
      dialogVisible: false,
      uploadDialogVisible: false,
      // 上传的身份证正反面
      uploadData: {
        face: null,
        back: null
      },
      faceCount: 0,
      backCount: 0,
      loading: false,
      // 正方面图片
      imageArray: [],
      rules: {
        phone: [
          { required: true, message: '手机号不可为空', trigger: 'blur' },
          { pattern: /^[1][3,4,5,7,8,9][0-9]{9}$/, message: '手机号格式错误', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '密码不可为空', trigger: 'blur' },
          { min: 6, max: 20, message: '新密码长度为6-20个字符', trigger: 'blur' },
          {
            pattern: /^([a-zA-Z]+[0-9]+[,._!@#$%^&*]+)|([a-zA-Z]+[,._!@#$%^&*]+[0-9]+)|([0-9]+[,._!@#$%^&*]+[a-zA-Z]+)|([0-9]+[a-zA-Z]+[,._!@#$%^&*]+)|([,._!@#$%^&*]+[a-zA-Z]+[0-9]+)|([,._!@#$%^&*]+[0-9]+[a-zA-Z]+)$/,
            message: '密码必须包含数字，字母和特殊符号',
            trigger: 'blur'
          }
        ],
        email: [
          { required: true, message: '邮箱地址不可为空', trigger: 'blur' },
          { validator: validateEmail, trigger: 'blur' }
        ]
      },
      educationRules: {
        degree: [{ required: true, message: '请选择学历', trigger: 'change' }],
        title: [{ required: true, message: '请选择学位', trigger: 'change' }],
        school: [{ required: true, message: '请输入毕业院校', trigger: 'blur' }]
      },
      employmentRules: {
        status: [{ required: true, message: '请选择就业状况', trigger: 'change' }],
        type: [{ required: true, message: '请选择职业类型', trigger: 'change' }]
      },
      // 判断相应的信息是否修改
      idcardModify: false,
      idModified: false
    }
  },
  async created () {
    this.getCurrentDate()
    this.info = JSON.parse(getInfo())
    // console.log(this.info)
    await this.getUserInfo()
    // console.log(this.user)
    // console.log(this.level)
  },
  mounted () {
    this.originalUser = { ...this.user }
    this.originalIdCard = { ...this.idcard }
    this.originalEducation = { ...this.education }
    this.originalEmployment = { ...this.employment }
  },
  watch: {
    // 监听用户信息的变化，修改 isModified 标记
    'user.password': function (newVal, oldVal) {
      this.isModified = newVal !== this.originalUser.password
    },
    'user.email': function (newVal, oldVal) {
      this.isModified = this.isModified || newVal !== this.originalUser.email
    }
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

      const userScore = this.user.score

      const option = {
        series: [
          {
            type: 'gauge',
            startAngle: 180,
            endAngle: 0,
            center: ['50%', '70%'],
            radius: '90%',
            min: 0,
            max: 1000,
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
                if (value === 875) {
                  return '优秀'
                } else if (value === 625) {
                  return '良好'
                } else if (value === 375) {
                  return '一般'
                } else if (value === 125) {
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
                } else if (value >= 250) {
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
    },

    async getUserInfo () {
      await getUser(
        this.info,
        (res) => {
          this.user = res.data.user
          this.idcard = res.data.idcard
          this.employment = res.data.employment
          this.education = res.data.education
          // console.log(this.user)
          // console.log(this.idcard)
          // console.log(this.employment)
          // console.log(this.education)

          this.initRingGaugeChart()
          localStorage.setItem('Score', JSON.stringify(this.user.score)) // 存储用户信息到浏览器
        },
        (res) => {
          this.$message.error(res.msg)
        }
      )
    },

    cancel () {
      this.idModified = false
      this.idcardModify = false
      this.dialogVisible = false
    },
    saveForm () {
      if (this.idcardModify) {
        this.$message.error('还未上传照片')
        return
      }
      if (this.isModified) {
        localStorage.setItem('Score', JSON.stringify(this.user.score)) // 存储用户信息到浏览器
        this.$confirm('确定修改吗？(多次反复修改可能影响信用分)', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.modifyUser()
          this.modifyEducation()
          this.modifyEmployment()
          if (!this.idModified) this.$message.success('修改成功')
        }).catch(() => {
          this.$message.info('操作取消')
          // 重置修改标记
          this.isModified = false
        })
      }

      this.dialogVisible = false
    },
    modifyUser () {
      updateUser(
        this.user,
        (res) => {
          console.log(res)
          this.user = res.data
          this.originalUser = { ...this.user }
          // 重置修改标记
          this.isModified = false
        },
        (res) => {
          this.$message.error(res.msg)
        }
      )
    },
    modifyEducation () {
      updateEducation(
        this.education,
        (res) => {
          console.log(res)
          this.education = res.data
          this.originalEducation = { ...this.education }
          // 重置修改标记
          this.isModified = false
        },
        (res) => {
          this.$message.error(res.msg)
        }
      )
    },
    modifyEmployment () {
      updateEmployment(
        this.employment,
        (res) => {
          console.log(res)
          this.employment = res.data
          this.originalEmployment = { ...this.employment }
          // 重置修改标记
          this.isModified = false
        },
        (res) => {
          this.$message.error(res.msg)
        }
      )
    },

    uploadDialogClose () {
      this.idcardModify = false
      this.idcard = this.originalIdCard
      this.loadFormData()
    },

    beforeUpload (file) {
      // 在上传之前的钩子，可以在这里进行一些验证操作
      // console.log('beforeUpload:', file)
      const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'
      if (this.faceCount && this.backCount) {
        this.$message.error('已完成上传，请勿重复操作')
        return false
      }
      if (!isJPG) {
        this.$message.error('上传失败，支持的格式为JPEG/PNG')
        return false
      }
      if (!file.name.includes('正面') && !file.name.includes('反面')) {
        this.$message.error('请先命名图片，例：正面.png')
        return false
      }
      if (this.imageArray.length >= 2) {
        this.$message.warning('最多可上传两张')
        return false
      }
      if (file.name.includes('正面')) {
        if (this.faceCount) {
          this.$message.error('正面已上传，请勿重复操作')
          return false
        } else {
          this.faceCount = 1 // 增加正面计数
        }
      } else if (file.name.includes('反面')) {
        if (this.backCount) {
          this.$message.error('反面已上传，请勿重复操作')
          return false
        } else {
          this.backCount = 1 // 增加反面计数
        }
      }
      return true // 返回 true 表示允许上传
    },

    // 上传成功
    handleSuccess (response) {
      this.loading = true
      setTimeout(() => {
        this.loading = false
      }, 2000)
      // console.log(response)
      if (response.code) {
        this.$message.success(response.msg)
        if (response.data.idCardRoot.data.face === null && response.data.idCardRoot.data.back !== null) {
          this.uploadData.back = response.data.idCardRoot.data.back
        } else if (response.data.idCardRoot.data.face !== null && response.data.idCardRoot.data.back === null) {
          this.uploadData.face = response.data.idCardRoot.data.face
        } else {
          this.uploadData.back = response.data.idCardRoot.data.back
          this.uploadData.face = response.data.idCardRoot.data.face
        }
        this.imageArray.push({ url: response.data.url })
        // console.log(this.uploadData.face)
      } else {
        this.$message.error(response.msg)
      }
    },

    handleError (error) {
      // 上传失败的回调
      this.$message.error(error)
      this.reUpload()
    },

    // 重新上传
    reUpload () {
      this.$confirm('多次重复上传可能会影响信用分，确定重新上传吗？', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.originalIdCard = this.idcard
        this.uploadData.face = null
        this.uploadData.back = null
        this.imageArray = []
        this.faceCount = 0
        this.backCount = 0
        this.idcardModify = !this.idcardModify

        this.uploadDialogVisible = false
        this.$nextTick(() => {
          // 在下一个视图更新周期更新对话框状态
          this.uploadDialogVisible = true
        })
      }).catch(() => {
        this.$message.info('操作取消')
      })
    },

    // 确认
    confirmUpload () {
      if (this.uploadData.face !== null && this.uploadData.back !== null) {
        this.idcard.name = this.uploadData.face.data.name
        this.idcard.gender = this.uploadData.face.data.sex
        this.idcard.idNo = this.uploadData.face.data.idNumber
        this.idcard.ethnicity = this.uploadData.face.data.ethnicity
        this.idcard.birthDate = this.uploadData.face.data.birthDate
        this.idcard.address = this.uploadData.face.data.address
        this.idcardModify = !this.idcardModify
        this.uploadDialogVisible = false
      } else {
        this.$message.error('信息上传不完整，请重新上传')
      }
    }
  }
}
</script>

<style lang="less" scoped>
@import "@/common/style.css";

.personalInfo {
  margin: 100px;
  width: 100%;
}

.ringGauge-container {
  width: 100%;
  height: 500px;
}

.image {
  display: flex;
  flex-direction: row;
}

.uploaded-image-container {
  display: flex;
  flex-direction: row;
  margin: 20px;
}

.re-upload {
  margin: 20px auto;
}
</style>
