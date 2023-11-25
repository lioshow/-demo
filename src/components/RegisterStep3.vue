<template>
  <div>
    <div class="register-container">
      这是第三步
    </div>
    <el-button class="prev-button" @click="prevStep">上一步</el-button>
    <el-button class="next-button" type="primary" @click="nextStep">完成注册</el-button>
  </div>
</template>

<script>
export default {
  data () {
    return {

    }
  },
  methods: {
    prevStep () {
      // 返回上一步
      this.$emit('prev', 1) // 1 表示上一步的索引
    },
    nextStep () {
      // 在这里可以进行表单验证等操作
      // 如果通过验证，跳转到下一步
      this.$emit('next', 3)
    }
  }
}
</script>

<style lang="less" scoped>
@import '@/common/style.css';
</style>
