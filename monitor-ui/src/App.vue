 <!-- * Copyright (c) 2020 Huawei Technologies Co.,Ltd.
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
 * See the Mulan PSL v2 for more details. -->
 <template>
  <el-config-provider :locale="locale">
    <router-view />
  </el-config-provider>
</template>

<script setup>
import {onMounted,getCurrentInstance,ref} from "vue";
import zhCn from "element-plus/lib/locale/lang/zh-cn";
import en from "element-plus/lib/locale/lang/en";
const { proxy } = getCurrentInstance();
const lang = {
  zhCn,
  en,
};
let locale = ref(lang["zhCn"]);
onMounted(() => {
  if(localStorage.getItem('opengauss-theme') == 'dark'){
     document.documentElement.classList.add('dark')
    } else {
     document.documentElement.classList.remove('dark')
  }
});

// 监听 opengauss-locale-change 语言切换事件
window.$wujie?.bus.$on('opengauss-locale-change', val => {
  proxy.$i18n.locale = (val == "en-US" ? "en" : "zhCn");
  locale.value = lang[(val == "en-US" ? "en" : "zhCn") || "zhCn"];
})
// 监听 opengauss-theme-change 主题切换事件
window.$wujie?.bus.$on('opengauss-theme-change', val => {
  if(val == 'dark'){
    document.documentElement.classList.add('dark')
  } else if(val == 'light') {
    document.documentElement.classList.remove('dark')
  }
})
</script>
<style>
  .dark{
    --el-menu-active-color: #E41D1D !important;
    --el-menu-hover-bg-color: #ffffff14 !important;
    --el-color-primary: #E41D1D !important;
    --el-color-primary-light-3: #E94842 !important;
    --el-color-primary-dark-2: #BF1118 !important;
    --el-input-bg-color: #ffece8 !important;
    --el-color-primary-light-5: #f53f3f !important;
    --el-table-header-bg-color: #f2f3f5 !important;
    --el-row-background: #232324 !important;
    --el-menu-bg-color: #232324 !important;
    --el-menu-text-color: #ffffffb3 !important;
    --el-text-color-regular: #ffffffe6 !important;
    --el-text-color-primary-light: #ffffffe6 !important;
    --el-fill-color-light: #232324 !important;
    --el-bg-color-overlay: #373739 !important;
    --el-border-color-light: #373739 !important;
    --el-button-color-bg-light: #dfdfdf14 !important;
    --el-button-color-light: #ffffffb3 !important;
    --el-button-bord-color-light: #dfdfdf14 !important;
    --el-input-bg-color-light: #ffffff14 !important;
    --el-input-border-color-light: #ffffff14 !important;
    --el-card-body-bg-color-light: #373739 !important;
    --el-card-body-color-light: #ffffffb3 !important;
    --el-span-title-light: #ffffffE6 !important;
    --el-radio-button-light: #373739 !important;
    --el-table-expanded-cell-bg-color-light:#373739 !important;
    --el-table-tr-cell-light:#373739 !important;
    --el-dialog-bg-color-light:#2a2a2b !important;
    --el-datepicker-inrange-bg-color-light:#2a2a2b !important;
    --el-datepicker-inrange-hover-bg-color-light:#2a2a2b !important;
    --el-button-bg-color-light:#e41d1d !important;
    --el-message-box-el-button-hover:#ffffff !important;
    --el-disabled-bg-color:#232324 !important;
    --el-table-head-bg-color: #2e2e30 !important;
    --el-table-head-text-color: #f6f6f6 !important;
}
body{
  margin: 0;
  padding: 0;
  height: 100%;
  font-size: 14px;
  position: relative;
}
</style>