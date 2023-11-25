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
      <el-button :loading="loading" type="primary" @click="login">登录</el-button>

      <!-- 登录协议 -->
      <div class="login-agreement">
        注意：登录即代表您同意使用我们的服务，且自愿服从相关条例。详细说明请看
        <el-button style="font-size: 12px;" type="text" @click="goLoginAgreement">《缅北征信服务平台协议》</el-button>
      </div>
    </el-dialog>
  </el-container>
</template>

<script>
export default {
  data () {
    return {
      dialogVisible: false,
      loginForm: {
        phone: '',
        password: ''
      },
      loginRules: {
        phone: [{ required: true, message: '请输入账号', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
      },
      loading: false
    }
  },
  methods: {
    goMyCredit () {
      this.$router.push({ path: '/myCredit' })
    },

    goLoginAgreement () {
      this.$router.push({ path: '/agreement' })
    },

    forgetPassword () {
      // 处理忘记密码逻辑
    },
    noAccount () {
      this.$router.push({ path: '/register' })
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
</style>
