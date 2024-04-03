<template>
  <el-container>
    <el-container class="home-container">
      <img src="@/assets/logo.jpg" alt="" class="home-logo">
      <div class="title">缅北征信中心</div>
      <div class="subtitle1">
        <el-button type="text">首页</el-button>
      </div>
      <div class="subtitle2">
        <el-button style="color: #fff;" type="text" @click="goMyCredit">我的信用</el-button>
      </div>
      <div class="center">
        <p>为了腰子而守护，共同的信誉世界</p>
        <el-button class="button" @click="dialogVisible = true">登录</el-button>
      </div>
    </el-container>

    <!-- 登录弹框 -->
    <el-dialog style="text-align: center;" title="欢迎使用缅北征信平台" :visible.sync="dialogVisible" :close-on-click-modal="false" width="400px">
      <!-- 表单 -->
      <el-form :model="loginForm" ref="loginForm" :rules="loginRules">
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="loginForm.phone" prefix-icon="el-icon-mobile" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="loginForm.password" prefix-icon="el-icon-lock" autocomplete="off" show-password></el-input>
        </el-form-item>
        <!-- 验证码 -->
        <el-form-item prop="validCode">
          <div style="display: flex;align-items: center;">
            <el-input v-model="loginForm.validCode" prefix-icon="el-icon-circle-check" style="flex: 1" placeholder="请输入验证码"></el-input>
            <div style="flex: 1;margin-left: 20px;">
              <valid-code @input="getCode" ref="validCode"/>
            </div>
          </div>
        </el-form-item>
      </el-form>

      <!-- 忘记密码和没有账号 -->
      <div align="left">
        <el-button style="font-size: 10px;" type="text" @click="forgetPassword">忘记密码?</el-button>
        <el-button style="font-size: 10px;" type="text" @click="noAccount">没有账号?</el-button>
      </div>

        <!-- 登录按钮 -->
      <el-button :loading="loading" type="primary" @click="login">登录</el-button>

      <!-- 登录协议 -->
      <div class="login-agreement">
        注意：登录即代表您同意使用我们的服务，且自愿服从相关条例。详细说明请看
        <el-button style="font-size: 12px;" type="text" @click="goLoginAgreement">《缅北征信服务平台协议》</el-button>
      </div>
    </el-dialog>

    <el-dialog :visible.sync="assessmentVisible" title="正在为您进行信用评估，请勿离开..." :show-close="false" :close-on-click-modal="false">
      <el-progress :percentage="progress"></el-progress>
      <div class="access-button" align="center">
        <el-button type="success" :disabled="progress==100 ? false : true" @click="goUser">评估完成，进入主页</el-button>
      </div>
    </el-dialog>
  </el-container>
</template>

<script>
import { loginUser } from '@/api/index.js'
import ValidCode from '@/components/ValidCode.vue'
import { mapState, mapMutations } from 'vuex'

export default {
  components: {
    ValidCode
  },
  computed: {
    ...mapState('userLoggedState', {
      isUserLoggedIn: state => state.isUserLoggedIn
    })
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
      progress: 0
    }
  },
  created () {
    // this.creditAssessment()
  },
  methods: {
    ...mapMutations('userLoggedState', ['setUserLoggedState']),

    getCode (code) {
      this.code = code
      // console.log(code)
    },

    goMyCredit () {
      this.$router.push({ path: '/myCredit' })
    },

    goLoginAgreement () {
      this.$router.push({ path: '/agreement' })
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
      this.$refs.loginForm.validate(async (valid) => {
        if (valid) {
          this.loading = true

          loginUser(
            this.loginForm,
            (res) => {
              this.loading = false
              this.$store.commit('user/setUserInfo', res.data)
              this.setUserLoggedState(true)
              // console.log(res.data)

              if (res.data.days === 1) {
                this.dialogVisible = false
                this.creditAssessment()
              } else {
                this.goUser()
              }
            },
            (res) => {
              this.loading = false
              this.$message.error(res.msg)
              // 刷新验证码
              this.$refs.validCode.refreshCode()
            }
          )
        }
      })
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

    goUser () {
      this.$message.success('登录成功')
      this.$router.push({ path: '/user' })
    }

    // login () {
    //   // 处理登录逻辑，可以在这里提交表单等
    //   // 如果表单验证通过，可以关闭弹框：this.dialogVisible = false;
    //   this.$refs.loginForm.validate(async (valid) => {
    //     if (valid) {
    //       this.loading = true
    //       setTimeout(() => {
    //         this.loading = false
    //       }, 3000)
    //       // 使用解构语法创建新对象，不包含 validCode 属性
    //       const { validCode, ...formData } = this.loginForm
    //       request.post('/user/login', formData).then((res) => {
    //         this.loading = false
    //         console.log(res)
    //         if (res.code === 200) {
    //           this.$message.success(res.msg)
    //           localStorage.setItem('user', JSON.stringify(res.data))
    //           this.$router.push({ path: '/user' })
    //         } else {
    //           this.$message.error(res.msg)
    //           // 刷新验证码
    //           this.$refs.validCode.refreshCode()
    //         }
    //       })
    //     }
    //   })
    // }
  }
}
</script>

<style lang="less" scoped>
@import '@/common/style.css';
  .home-container {
    position: absolute;
    background: #1D1B20;
    color: white;
    height: 100%;
    width: 100%;
    top: 0px;
    left: 0px;
  }

  .home-logo {
    height: 122px;
    width: 213px;
    top: 11px;
    left: 22px;
  }

  .center {
    position: relative;
    text-align: center;
    top: 180px;
    font-size: 72px;
    font-family: '华文隶书';
  }

  .button {
    position: relative;
    top: 100px;
    width: 300px;
    height: 80px;
    font-size: 48px;
  }

  .login-agreement {
    text-align: left;
    margin-top: 10px;
    font-size: 12px;
    color: #999;
  }

  .access-button {
    margin: 50px auto;
    align-items: center;
  }
</style>
