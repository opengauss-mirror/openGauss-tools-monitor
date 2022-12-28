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
        <el-row class="rowBackgroundColor" :style="{'height':isPlatform == '/'?'100vh':'calc(100vh - 170px)'}">
            <el-col :span="3" class="elMenu" style="border-right: 2px solid #E4E7ED;height: 100%;box-shadow: 2px 0px 5px rgb(0 0 0 / 10%);border: none;">
                <el-menu
                :default-active="activePathFn"
                class="el-menu-vertical-demo"
                router>
              <el-menu-item index="/configurationCenter">
                <template v-slot:title>
                    <el-icon><Menu /></el-icon>
                    <span>{{ $t("menus.configurationCenter") }}</span>
                </template>
              </el-menu-item>
              <el-menu-item index="/fileGeneration">
                <template v-slot:title>
                    <el-icon><RefreshRight /></el-icon>
                    <span>{{ $t("menus.fileGeneration") }}</span>
                </template>
              </el-menu-item>
              <el-menu-item index="/IndexRelease">
                <template v-slot:title>
                    <el-icon><Promotion /></el-icon>
                    <span>{{ $t("menus.IndexRelease") }}</span>
                </template>
              </el-menu-item>
                </el-menu>
            </el-col>
            <el-col :span="21" style="padding: 15px 30px;height: 100%;">
                <router-view/>
            </el-col>
        </el-row>
</template>
<script setup>
    import { Promotion,RefreshRight,Menu } from '@element-plus/icons-vue';
    import { watch,reactive} from 'vue';
    import { useRouter } from 'vue-router';
            const router = useRouter();
            const isPlatform = process.env.VUE_APP_BASE_API;
            let activePathFn = reactive(router.currentRoute.value.path);
            watch(() => router.currentRoute.value.path,(toPath) => {
                activePathFn = toPath;
            })
</script>
<style  scoped>
    .el-menu {
        border-right: none;
    }
    .el-col-3 {
        max-width: 14.5%;
        flex: 0 0 14.5%;
    }
    .el-col-21 {
        max-width: 85.5%;
        flex: 0 0 85.5%;
    }
    .elMenu .el-menu-item [class=el-icon]{
        margin-right: 8px;
    }
</style>