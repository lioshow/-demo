<template>
  <div>
    <div class="register-container">
      <el-upload class="upload-demo" action="http://localhost:8090/api"
        :before-upload="beforeUpload"
        :on-success="handleSuccess"
        :on-error="handleError">
        <el-button size="medium" type="primary">点击上传</el-button>
      </el-upload>
    </div>
    <el-button class="prev-button" @click="prevStep">上一步</el-button>
    <el-button class="next-button" type="primary" @click="nextStep">下一步</el-button>
  </div>
</template>

<script>
// import UploadComponent from '@/components/UploadComponent'
// import axios from 'axios'
export default {
  data () {
    return {
      uploadData: ''
    }
  },
  methods: {
    prevStep () {
      // 返回上一步
      this.$emit('prev', 0)
    },
    nextStep () {
      // 在这里可以进行文件上传成功的验证等操作
      // 如果通过验证，跳转到下一步
      // if (this.uploadData === '') {
      //   this.$message.warning('还未上传图片')
      // } else {

      // }
      this.$emit('next', 2)
    },

    beforeUpload (file) {
      // 在上传之前的钩子，可以在这里进行一些验证操作
      console.log('beforeUpload:', file)
      return true // 返回 true 表示允许上传
    },
    handleSuccess (response, file, fileList) {
      // 上传成功的回调
      if (response.code) {
        if (response.data.data.back === null) {
          this.$message.info('身份证反面还未上传')
        } else if (response.data.data.face === null) {
          this.$message.info('身份证正面还未上传')
        } else {
          this.$message.success('上传成功')
          this.uploadData = response.data.data
        }
      } else {
        this.$message.error(response.msg)
      }
      console.log('handleSuccess:', response, file, fileList)
    },
    handleError (error, file, fileList) {
      // 上传失败的回调
      console.error('handleError:', error, file, fileList)
    }
  }
}
</script>

<style lang="less" scoped>
@import '@/common/style.css';
</style>
