const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true
})

//打包配置文件
module.exports = {
  assetsDir: 'static',
  parallel: false,
  publicPath: './',
};
