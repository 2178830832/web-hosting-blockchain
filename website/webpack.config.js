const path = require('path')
const {VueLoaderPlugin} = require('vue-loader')
const CopyPlugin = require('copy-webpack-plugin')
const HtmlWebpackPlugin = require('html-webpack-plugin')
const {CleanWebpackPlugin} = require('clean-webpack-plugin')
module.exports = {
  entry: {
    'index': './src/main.js',
    'text': './src/view/text/text.js',
    'image': './src/view/image/image.js',
    'video': './src/view/video/video.js',
    'audio': './src/view/audio/audio.js',
  },
  output: {
    path: path.resolve(__dirname, 'dist/'),
    filename: 'js/[name]-build.js',
  },
  mode: "production",
  module: {
    rules: [
      {test: /\.vue$/, loader: 'vue-loader'},
      {test: /\.js$/, loader: 'babel-loader'},
      {test: /\.css$/, use: ['vue-style-loader', 'css-loader']},
      // {test: /\.txt$/, use: ['raw-loader']},
      {
        test: /\.txt$/, type: "asset/source",
        generator: {filename: "assets/text/[name][ext]"}
      },
      {
        test: /\.jpg|png|gif$/, type: "asset/resource",
        generator: {filename: "assets/image/[name][ext]"}
      },
      {
        test: /\.mp3$/, type: "asset/resource",
        generator: {filename: "assets/audio/[name][ext]"}
      },
      {
        test: /\.mp4$/, type: "asset/resource",
        generator: {filename: "assets/video/[name][ext]"}
      }
    ],
  },
  resolve: {
    extensions: [
      '.js', '.json', '.vue'
    ],
    alias: {
      '@': path.resolve(__dirname, './src'), // point @ to the src path
    },
  },
  plugins: [
    new CleanWebpackPlugin(),
    new VueLoaderPlugin(),
    new HtmlWebpackPlugin({
      template: 'public/index.html',
      filename: "index.html",
      chunks: ['index']
    }),
    new HtmlWebpackPlugin({
      template: 'src/view/text/text.html',
      filename: "text.html",
      chunks: ['text']
    }),
    new HtmlWebpackPlugin({
      template: 'src/view/image/image.html',
      filename: "image.html",
      chunks: ['image']
    }),
    new HtmlWebpackPlugin({
      template: 'src/view/video/video.html',
      filename: "video.html",
      chunks: ['video']
    }),
    new HtmlWebpackPlugin({
      template: 'src/view/audio/audio.html',
      filename: "audio.html",
      chunks: ['audio']
    }),
    new CopyPlugin({
      patterns: [
        {from: 'public/favicon.ico', to: './'},
        // {from: 'src/assets', to: './assets'}
      ],
    }),
  ],
}