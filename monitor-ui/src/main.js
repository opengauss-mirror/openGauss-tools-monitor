 /* Copyright (c) 2020 Huawei Technologies Co.,Ltd.
 *
 * openGauss is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *
 *          http://license.coscl.org.cn/MulanPSL2
 *
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.*/
// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus';             //Import element globally
import 'element-plus/dist/index.css';    //Globally import the style of element
if(process.env.VUE_APP_BASE_API == '/'){
  require('./assets/css/reset.css'); 
} else {
  require('./assets/css/resetPlatform.css'); 
}
import axios from 'axios';
// The base url of the default request (if the address requested by axios does not have a domain name, the baseURL is automatically added) (default request domain name, / current domain name)
axios.defaults.baseURL = process.env.VUE_APP_BASE_API === '/' ? '/' : process.env.VUE_APP_BASE_API
// Set the content-Type value of the post request header
axios.defaults.headers.post['Content-Type'] = 'application/json;charset-utf-8';
// Request timeout 30000ms
axios.defaults.timeout = 30000;
axios.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('opengauss-token')
    if (token) {
      if (!config.headers) {
        config.headers = {}
      }
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    // do something
    return Promise.reject(error)
  }
)
import i18n from "@/i18n";

const app = createApp(App);
app.use(i18n);
app.config.globalProperties.$unFocus = () => {
    // 用于强制失焦 => elementUI bug, 点击按钮后不失焦
    const button = document.createElement('button');
    button.style.position = 'fixed';
    button.style.opacity = 0;
    document.body.appendChild(button);
    button.focus();
    button.remove();
  };
app.config.globalProperties.$http = axios;
app.use(ElementPlus);
app.use(router);
app.mount('#app')