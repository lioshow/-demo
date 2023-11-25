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
      <el-step title="步骤 1" description="请填写您的手机号和密码"></el-step>
      <el-step title="步骤 2" description="上传身份证照片，最多两张"></el-step>
      <el-step title="步骤 3" description="完善信息"></el-step>
      <el-step title="步骤 4" description="注册成功" :status="currentStep === 4 ? 'success' : ''"></el-step>
    </el-steps>

    <div class="button-container">
      <el-button v-if="currentStep > 1 && currentStep < 4" class="prev-button" @click="prevStep">上一步</el-button>
      <el-button v-if="currentStep < 3" :loading="loading" class="next-button" type="primary" @click="nextStep">下一步</el-button>
      <el-button v-if="currentStep === 3" :loading="loading" class="next-button" type="primary" @click="finishRegister">完成注册</el-button>
    </div>
    <!-- <component :is="currentStepComponent" @next="nextStep" @prev="prevStep"></component>-->
    <div class="register-container" v-if="currentStep === 1">
      <el-form :model="registerForm" ref="registerForm" :rules="rules" label-width="80px">
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="registerForm.phone"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="registerForm.password"></el-input>
        </el-form-item>
      </el-form>
    </div>

    <div class="register-container" v-if="currentStep === 2" v-loading="loading">
      <el-upload class="upload-demo" action="http://localhost:8090/api"
        :before-upload="beforeUpload"
        :on-success="handleSuccess"
        :on-error="handleError">
        <el-button size="medium" type="primary">点击上传</el-button>
      </el-upload>

      <div class="image">
        <div v-for="(image, index) in imageArray" :key="index" class="uploaded-image-container">
          <img :src="image.url" alt="" style="width: 100px; height: 100px;">
        </div>
      </div>

      <el-button class="re-upload" v-if="imageArray.length > 0"  @click="reUpload">重新上传</el-button>
    </div>

    <div class="register-container" v-if="currentStep === 3">
      <div class="select-container">
        <el-form ref="educationForm" label-width="80px" :model="education" :rules="educationRules">
          <el-form-item label="学历" prop="degree">
            <el-select v-model="education.degree" placeholder="请选择学历">
              <!-- 学历选项 -->
              <el-option label="小学" value="小学"></el-option>
              <el-option label="初中" value="初中"></el-option>
              <el-option label="高中" value="高中"></el-option>
              <el-option label="大专" value="大专"></el-option>
              <el-option label="本科" value="本科"></el-option>
              <el-option label="研究生" value="研究生"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="学位" prop="title" v-if="education.degree === '本科' || education.degree === '研究生'">
            <el-select v-model="education.title" placeholder="请选择学位">
              <!-- 学位选项 -->
              <el-option label="学士" value="学士"></el-option>
              <el-option label="硕士" value="硕士"></el-option>
              <el-option label="博士" value="博士"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="毕业院校" prop="school">
            <el-input v-model="education.school" placeholder="请输入毕业院校"></el-input>
          </el-form-item>
        </el-form>

      <!-- 就业情况部分 -->
        <el-form ref="employmentForm" label-width="80px" :model="employment" :rules="employmentRules">
          <el-form-item label="就业状况" prop="status">
            <el-select v-model="employment.status" placeholder="请选择就业状况">
              <!-- 就业状况选项 -->
              <el-option label="在职" value="在职"></el-option>
              <el-option label="待业" value="待业"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="职业类别" prop="type" v-if="employment.status === '在职'">
            <el-select v-model="employment.type" placeholder="请选择职位">
              <!-- 职业类别选项 -->
              <el-option label="党的机关、国家机关、群众团体和社会组织、企事业单位负责人"
                          value="党的机关、国家机关、群众团体和社会组织、企事业单位负责人"></el-option>
              <el-option label="专业技术人员" value="专业技术人员"></el-option>
              <el-option label="办事人员和有关人员" value="办事人员和有关人员"></el-option>
              <el-option label="社会生产服务和生活服务人员" value="社会生产服务和生活服务人员"></el-option>
              <el-option label="农、林、牧、渔业生产及辅助人员" value="农、林、牧、渔业生产及辅助人员"></el-option>
              <el-option label="生产制造及有关人员" value="生产制造及有关人员"></el-option>
              <el-option label="军人" value="军人"></el-option>
              <el-option label="学生" value="学生"></el-option>
              <el-option label="退休/无业" value="退休/无业"></el-option>
              <el-option label="不便分类的其他从业人员" value="不便分类的其他从业人"></el-option>
            </el-select>
          </el-form-item>
        </el-form>
      </div>

      <el-button type="text" style="align-items: center; color:#000" @click="showTip">暂时不填，跳过</el-button>

    </div>

    <div class="register-container" v-if="currentStep === 4">
      <el-result icon="success" title="注册成功" subTitle="点击返回首页">
        <template slot="extra">
          <el-button type="primary" size="medium" @click="goHome">返回</el-button>
        </template>
      </el-result>
    </div>
  </el-container>
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
          {
            pattern: /^([a-zA-Z]+[0-9]+[,._!@#$%^&*]+)|([a-zA-Z]+[,._!@#$%^&*]+[0-9]+)|([0-9]+[,._!@#$%^&*]+[a-zA-Z]+)|([0-9]+[a-zA-Z]+[,._!@#$%^&*]+)|([,._!@#$%^&*]+[a-zA-Z]+[0-9]+)|([,._!@#$%^&*]+[0-9]+[a-zA-Z]+)$/,
            message: '密码必须包含数字，字母和特殊符号',
            trigger: 'blur'
          }
        ]
      },
      currentStep: 1,
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
      education: {
        degree: '',
        title: '',
        school: ''
      },
      employment: {
        status: '',
        type: ''
      },
      educationRules: {
        degree: [{ required: true, message: '请选择学历', trigger: 'change' }],
        title: [{ required: true, message: '请选择学位', trigger: 'change' }],
        school: [{ required: true, message: '请输入毕业院校', trigger: 'blur' }]
      },
      employmentRules: {
        status: [{ required: true, message: '请选择就业状况', trigger: 'change' }],
        type: [{ required: true, message: '请选择职业类型', trigger: 'change' }]
      }
      // imageArray: ['http://localhost:8090/images/d14e5431b2bd48b294ca1fc0db2df780',
      //   'http://localhost:8090/images/9d860b2350814f37954a9fb4da1ff68f']
      // stepComponents: [Step1, Step2, Step3, Step4]
    }
  },
  methods: {
    goMyCredit () {
      this.$router.push({ path: '/myCredit' })
    },

    goHome () {
      this.$router.push({ path: '/' })
    },

    nextStep () {
      this.loading = true
      setTimeout(() => {
        this.loading = false
      }, 2000)
      if (this.currentStep === 1) {
        this.$refs.registerForm.validate(async (valid) => {
          if (valid) {
            this.loading = false
            this.currentStep += 1
          }
        })
      } else if (this.currentStep === 2) {
        if (this.uploadData.back === null && this.uploadData.face === null) {
          this.$message.warning('还未上传图片')
        } else if (this.uploadData.back === null) {
          this.$message.warning('身份证反面还未上传')
        } else if (this.uploadData.face === null) {
          this.$message.warning('身份证正面还未上传')
        } else {
          this.loading = false
          this.currentStep += 1
        }
      } else if (this.currentStep === 3) {
        this.$refs.educationForm.validate(async (educationValid) => {
          if (educationValid) {
            this.$refs.employmentForm.validate(async (employmentValid) => {
              if (employmentValid) {
                this.loading = false
                this.currentStep += 1
              }
            })
          }
        })
      }
    },

    prevStep () {
      this.currentStep -= 1
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
        // console.log(this.uploadData)
      } else {
        this.$message.error(response.msg)
      }
    },

    handleError (error) {
      // 上传失败的回调
      this.$message.error(error)
    },

    // 重新上传
    reUpload () {
      this.uploadData.face = null
      this.uploadData.back = null
      this.imageArray = []
    },

    // 完成注册
    finishRegister () {
      this.$confirm('确认注册吗', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        this.currentStep += 1
      }).catch(() => {
        this.$message.info('操作取消')
      })
    },

    showTip () {
      this.$confirm('跳过此步骤将可能影响到信用分评估，确定要跳过吗', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.finishRegister()
        this.currentStep += 1
      }).catch(() => {
        this.$message.info('操作取消')
      })
    }
  }
}
</script>

<style lang="less" scoped>
  .el-steps {
    margin: 50px;
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
    margin: 0px;
  }

  .select-container {
    display: flex;
    justify-content: center;
  }
</style>
