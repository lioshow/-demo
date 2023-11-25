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

    <el-container>
      <div class="agreement-box">
        <h3>缅北征信平台服务协议</h3>
        <ul class="agreement-list">
          <li>《缅北征信服务平台协议》（以下简称“本协议”）由您与缅北征信集团（以下简称“缅北集团”或“我们”）共同缔结，
        本协议具有合同效力。请您务必审慎阅读、充分理解各条款内容。
          </li>
          <li>除非您已阅读并接受本协议所有条款，否则您无权使用缅北征信服务。
        如果您对本协议或缅北征信服务有意见或建议，可与缅北征信集团的服务部门联系，我们会给予您必要的帮助。
        您点击同意、接受 、下一步或以其他形式确认本协议的 ，均视为您已阅读并同意签署本协议。
          </li>
          <li>如果您未满18周岁，请在法定监护人的陪同下阅读本协议。</li>
          <li>一、【定义】</li>
          <li>1.1 本协议：指本协议正文。</li>
          <li>1.2 缅北信用评分：指平台针对用户在使用缅北征信平台过程中产生的用户信息，通过计算综合得出的对用户信用水平的评分。</li>
          <li>1.3 您：又称“用户”，指被授权使用缅北信用及其服务的自然人。</li>
          <li>1.4 处理：指对您的信息进行收集、整理、保存、分析、比对、核实、校验、加工、传输、公开、删除等相应活动。</li>
          <li>1.5 书面：指能够通过载体记载内容的形式，包括纸质及短信、站内信、电子邮件、公告等电子形式。</li>
          <li>二、【服务规则】</li>
          <li>2.1 为了守护良好的信誉世界，您充分理解并同意，我们会以账号为单位，
            收集、使用您在使用缅北征信服务过程中产生的个人信息，用于计算和生成用户的平台信用分。</li>
          <li>三、【用户信息保护】</li>
          <li>3.1 您应对通过缅北信用及相关服务了解、接收或可接触到的包括但不限于其他用户在内的任何人的个人信息予以充分尊重，您不应以搜集、复制、存储、传播或以其他任何方式使用其他用户的个人信息，否则，由此产生的后果由您自行承担。</li>
          <li>四、【遵守当地法律监管】</li>
          <li>4.1 您在使用缅北信用过程中应当遵守当地相关的法律法规，并尊重当地的道德和风俗习惯。如果您的行为违反了当地法律法规或道德风俗，您应当为此独立承担责任。</li>
          <li>4.2 您应避免因使用缅北信用而使缅北集团卷入政治和公共事件，否则我们有权暂停或终止对您的服务，并没收您的腰子。</li>
        </ul>
      </div>
    </el-container>
  </el-container>
</template>

<script>
export default {
  data () {
    return {
    }
  },
  methods: {
    goMyCredit () {
      this.$router.push({ path: '/myCredit' })
    },

    goHome () {
      this.$router.push({ path: '/' })
    }
  }
}
</script>

<style lang="less" scoped>
  @import '@/common/style.css';

  .agreement-box {
    width: 80%;
    margin: 0 auto;
  }

  .agreement-box h3 {
    text-align: center;
    font-size: 50px;
    margin: 20px;
  }

  .agreement-list {
    list-style-type: none; /* 去除默认的列表标记 */
    padding: 0;
  }

  .agreement-list li {
    margin-bottom: 10px;
    font-family: 'Arial', sans-serif;
    line-height: 1.6;
  }

</style>
