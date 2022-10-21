const path = require('path')
const {VueLoaderPlugin} = require('vue-loader')
const CopyPlugin = require('copy-webpack-plugin')
const HtmlWebpackPlugin = require('html-webpack-plugin')
const {CleanWebpackPlugin} = require('clean-webpack-plugin')
module.exports = {
  entry: ["./src/main.js", "./src/modules.js"],
  output: {
    path: path.resolve(__dirname, 'dist/'),
    filename: 'build.js',
  },
  mode: "production",
  module: {
    rules: [
      {test: /\.vue$/, loader: 'vue-loader'},
      {test: /\.js$/, loader: 'babel-loader'},
      {test: /\.css$/, use: ['vue-style-loader', 'css-loader']},
      {
        test: /\.jpg|png|gif$/, loader: 'url-loader',
        options: {
          limit: 8192,
          esModule: false
        }
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
      templateParameters: {
        BASE_URL: '/',
      },
    }),
    new CopyPlugin({
      patterns: [
        {from: 'public/favicon.ico', to: './'},
        {from: 'src/assets', to: './assets'}
      ],
    }),
  ]
}