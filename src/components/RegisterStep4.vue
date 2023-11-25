<template>
  <div class="register-container">
    <el-result icon="success" title="注册成功" subTitle="点击返回首页">
      <template slot="extra">
        <el-button type="primary" size="medium" @click="goHome">返回</el-button>
      </template>
    </el-result>
  </div>
</template>

<script>
export default {
  data () {
    return {

    }
  },
  methods: {
    goHome () {
      this.$router.push({ path: '/' })
    }
  }
}
</script>

<style lang="less" scoped>
@import '@/common/style.css';
</style>
