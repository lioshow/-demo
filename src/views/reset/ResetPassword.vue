<template>
  <el-container class="container">
    <el-header class="header">
      <img src="@/assets/logo.jpg" alt="" class="logo">
      <div class="title">缅北征信中心</div>
      <div class="subtitle1">
        <el-button style="color: #fff;" type="text" @click="goHome">首页</el-button>
      </div>
      <div class="subtitle2">
        <el-button style="color: #fff;" type="text" @click="goMyCredit">我的信用</el-button>
      </div>
    </el-header>

    <el-steps :active="currentStep - 1" finish-status="success" space="400px" align-center>
      <el-step title="步骤 1" description="验证账户"></el-step>
      <el-step title="步骤 2" description="重置密码"></el-step>
      <el-step title="步骤 3" description="重置结果结果" :status="currentStep === 3 ? 'success' : (currentStep === 4 ? 'error' : '')"></el-step>
    </el-steps>

    <div class="button-container">
      <el-button v-if="currentStep === 2" class="prev-button" @click="prevStep">上一步</el-button>
      <el-button v-if="currentStep === 1" :loading="loading" class="next-button" type="primary" @click="nextStep">下一步</el-button>
      <el-button v-if="currentStep === 2" :loading="loading" class="next-button" type="primary" @click="nextStep">完成重置</el-button>
    </div>

    <div class="register-container" v-if="currentStep === 1">
      <el-form :model="resetForm" ref="resetForm" :rules="resetFormRules" label-width="80px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="resetForm.name"></el-input>
        </el-form-item>
        <el-form-item label="证件号码" prop="idNo">
          <el-input v-model="resetForm.idNo"></el-input>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="resetForm.phone"></el-input>
        </el-form-item>
      </el-form>
    </div>

    <div class="register-container" v-if="currentStep === 2">
      <el-form :model="passwordForm" ref="passwordForm" :rules="passwordFormRules" label-width="80px">
        <el-form-item label="原密码" prop="oldPassword">
          <el-input v-model="passwordForm.oldPassword"></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="passwordForm.newPassword"></el-input>
        </el-form-item>
      </el-form>
    </div>

    <div class="register-container" v-if="currentStep === 3">
      <el-result icon="success" title="重置成功" subTitle="点击返回首页">
        <template slot="extra">
          <el-button type="primary" size="medium" @click="goHome">返回</el-button>
        </template>
      </el-result>
    </div>

    <div class="register-container" v-if="currentStep === 4">
      <el-result icon="error" title="重置失败" subTitle="点击返回第一步">
        <template slot="extra">
          <el-button type="primary" size="medium" @click="currentStep = 1">返回</el-button>
        </template>
      </el-result>
    </div>
  </el-container>
</template>

<script>
import axios from 'axios'
export default {
  data () {
    return {
      currentStep: 1,
      resetForm: {
        name: '',
        idNo: '',
        phone: ''
      },
      passwordForm: {
        oldPassword: '',
        newPassword: ''
      },
      loading: false,
      resetFormRules: {
        phone: [
          { required: true, message: '手机号不可为空', trigger: 'blur' },
          { pattern: /^[1][3,4,5,7,8,9][0-9]{9}$/, message: '手机号格式错误', trigger: 'blur' }
        ],
        name: [
          { required: true, message: '姓名不可为空', trigger: 'blur' }
        ],
        idNo: [
          { required: true, message: '身份证号不可为空', trigger: 'blur' },
          { pattern: /^\d{17}[\dXx]$/, message: '身份证格式错误', trigger: 'blur' }
        ]
      },
      passwordFormRules: {
        oldPassword: [
          { required: true, message: '原密码不可为空', trigger: 'blur' }
        ],
        newPassword: [
          { required: true, message: '新密码不可为空', trigger: 'blur' },
          { min: 6, max: 20, message: '新密码长度为6-20个字符', trigger: 'blur' },
          {
            pattern: /^([a-zA-Z]+[0-9]+[,._!@#$%^&*]+)|([a-zA-Z]+[,._!@#$%^&*]+[0-9]+)|([0-9]+[,._!@#$%^&*]+[a-zA-Z]+)|([0-9]+[a-zA-Z]+[,._!@#$%^&*]+)|([,._!@#$%^&*]+[a-zA-Z]+[0-9]+)|([,._!@#$%^&*]+[0-9]+[a-zA-Z]+)$/,
            message: '密码必须包含数字，字母和特殊符号',
            trigger: 'blur'
          },
          { validator: this.validatePassword, trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    goMyCredit () {
      this.$router.push({ path: '/myCredit' })
    },

    goHome () {
      this.$router.push({ path: '/' })
    },

    validatePassword (rule, value, callback) {
      // 自定义校验逻辑，确保新密码和原密码不一致
      if (value === this.passwordForm.oldPassword) {
        callback(new Error('新密码不能与原密码一致'))
      } else {
        callback()
      }
    },

    prevStep () {
      this.currentStep -= 1
    },

    nextStep () {
      this.loading = true
      setTimeout(() => {
        this.loading = false
      }, 2000)
      if (this.currentStep === 1) {
        this.$refs.resetForm.validate(async (resetValid) => {
          if (resetValid) {
            this.loading = false
            this.currentStep += 1
          }
        })
      } else if (this.currentStep === 2) {
        this.$refs.passwordForm.validate(async (passwordValid) => {
          if (passwordValid) {
            this.loading = false
            this.finishReset()
          }
        })
      }
    },

    // 完成重置
    finishReset () {
      this.$confirm('确认重置吗', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.loading = true
        setTimeout(() => {
          this.loading = false
        }, 2000)
        this.resetForm.newPassword = this.passwordForm.newPassword
        axios.put('http://localhost:8090/user/reset', this.resetForm).then((response) => {
          this.loading = false
          // console.log(response)
          if (response.data.code) {
            this.currentStep += 1
          } else {
            this.$message.error(response.data.msg)
            this.currentStep += 2
          }
        })
      }).catch(() => {
        this.$message.info('操作取消')
      })
    }

  }
}
</script>

<style lang="less" scoped>
@import '@/common/style.css';

  .el-steps {
    width: 80%;
    margin: 50px auto;
  }

  .el-form {
    width: 300px;
    margin: 30px;
  }

  .button-container {
    position: fixed;
    bottom: 20px;
    left: 50%;
    transform: translateX(-50%);
    text-align: center;
  }
</style>
