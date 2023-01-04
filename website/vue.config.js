const {defineConfig} = require('@vue/cli-service')
module.exports = defineConfig({
  publicPath: './',
  transpileDependencies: true,
  devServer: {
    port: 8090,
  },
  lintOnSave: false,
  chainWebpack: config => {
    config.module
    .rule('raw')
    .test(/\.txt$/)
    .use('raw-loader')
    .loader('raw-loader')
    .end()
  },
  pages: {
    text: {
      entry: 'src/view/text/text.js',
      template: '/src/view/text/text.html',
      filename: 'text.html'
    },
    image: {
      entry: 'src/view/image/image.js',
      template: '/src/view/image/image.html',
      filename: 'image.html'
    },
    audio: {
      entry: 'src/view/audio/audio.js',
      template: '/src/view/audio/audio.html',
      filename: 'audio.html'
    },
    video: {
      entry: 'src/view/video/video.js',
      template: '/src/view/video/video.html',
      filename: 'video.html'
    },
  },
})
