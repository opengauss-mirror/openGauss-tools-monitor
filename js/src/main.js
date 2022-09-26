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
import Vue from 'vue'
import ElementUI from 'element-ui';             //Import element globally
import 'element-ui/lib/theme-chalk/index.css';    //Globally import the style of element
import App from './App'
import router from './router'

// import axios
import axios from 'axios'
// The base url of the default request (if the address requested by axios does not have a domain name, the baseURL is automatically added) (default request domain name, / current domain name)
axios.defaults.baseURL = "/"
// Set the content-Type value of the post request header
axios.defaults.headers.post['Content-Type'] = 'application/json;charset-utf-8';
// Request timeout 5000ms
axios.defaults.timeout = 30000;
Vue.prototype.$http = axios;
Vue.config.delimiters = ['${', '}'];
Vue.config.productionTip = false
Vue.use(ElementUI); 
/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
