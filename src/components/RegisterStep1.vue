<template>
  <div>
    <div class="register-container">
      <el-form :model="registerForm" ref="registerForm" :rules="rules" label-width="80px">
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="registerForm.phone"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="registerForm.password"></el-input>
        </el-form-item>
      </el-form>
    </div>
    <el-button class="next-button" type="primary" @click="nextStep">下一步</el-button>
  </div>
</template>

<script>
export default {
  data () {
    return {
      registerForm: {
        phone: '',
        password: ''
      },
      rules: {
        phone: [
          { required: true, message: '手机号不可为空', trigger: 'blur' },
          { pattern: /^[1][3,4,5,7,8,9][0-9]{9}$/, message: '手机号格式错误', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '密码不可为空', trigger: 'blur' },
          { min: 6, message: '密码不少于6位', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    nextStep () {
      this.$refs.registerForm.validate(async (valid) => {
        if (valid) {
          this.$emit('next', 1)
        }
      })
    }
  }
}
</script>

<style lang="less" scoped>
  @import '@/common/style.css';
</style>
