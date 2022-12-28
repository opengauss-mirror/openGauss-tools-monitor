const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  publicPath: process.env.VUE_APP_REQUEST_URL ? process.env.VUE_APP_REQUEST_URL : '/',
  outputDir:'dist',
  transpileDependencies: true,
  devServer: {
    // Paths
    proxy: {
      '/': {  //使用"/api"来代替"http://f.apiplus.c"
        target: 'http://localhost:8085', //源地址
        ws: false,
        changeOrigin: true, //改变源
        pathRewrite: {
          '^/': '' //路径重写
        }
      }
    },
    client: {
      webSocketURL: 'ws://localhost:8080/ws',
    },
    headers: {
      'Access-Control-Allow-Origin': '*',
    },
    // Various Dev Server settings
    host: 'localhost', // can be overwritten by process.env.HOST
    port: 8080, // can be overwritten by process.env.PORT, if port is in use, a free one will be determined
  }
})
