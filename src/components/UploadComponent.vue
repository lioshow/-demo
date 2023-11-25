<template>
  <el-upload
    class="upload-demo"
    action="/api/upload"
    :before-upload="beforeUpload"
    :on-success="handleSuccess"
    :limit="1"
    :show-file-list="false"
  >
    <el-button size="medium" type="primary">点击上传</el-button>
  </el-upload>
</template>

<script>
export default {
  methods: {
    beforeUpload (file) {
      const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'
      if (!isJPG) {
        this.$message.error('上传图片只能是 JPG 或 PNG 格式!')
      }
      return isJPG
    },
    handleSuccess (response, file) {
      this.$emit('upload-success', response) // 将上传成功的结果传递给父组件
    }
  }
}
</script>

<style scoped>
.upload-demo {
  display: inline-block;
  margin-bottom: 20px;
}
</style>
