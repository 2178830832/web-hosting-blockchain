const {defineConfig} = require('@vue/cli-service')
module.exports = defineConfig({
  publicPath: './',
  transpileDependencies: true,
  devServer: {
    port: 8090,
    // proxy: {
    //   'api': {
    //     target: 'http://localhost:8000', // change to the port of springboot
    //     changeOrigin: true,
    //     pathRewrite: {
    //       '^/api': ""
    //     }
    //   }
    // }
  },
  lintOnSave: false
})
