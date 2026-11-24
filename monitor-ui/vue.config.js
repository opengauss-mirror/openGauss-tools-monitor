/*
 * Copyright (c) 2026 Huawei Technologies Co., Ltd.
 * openGauss-tools-monitor is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

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
