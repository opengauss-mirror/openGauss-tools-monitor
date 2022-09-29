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
    <el-tabs v-model='activeName' @tab-click="handleClick">
        <el-tab-pane label='配置中心' name='first' style="font-size:16px;">
            <configurationCenter v-if="isFirst"></configurationCenter>
        </el-tab-pane>
        <el-tab-pane label='插件生成' name='second' style="font-size:16px;">
            <fileGeneration v-if="isSecond"></fileGeneration>
        </el-tab-pane>
    </el-tabs>
</template>
<script>
    import configurationCenter from '../views/configurationCenter'
    import fileGeneration from '../views/fileGeneration' 
    export default{
        name: 'exporter',
        components:{
            configurationCenter,
            fileGeneration
        },
        data(){
            return{
                activeName:'first',
                isFirst:true,
                isSecond:false,
            }
        },
        methods: {
        // tabs click event
        handleClick(tab, event) {
            if(tab.name == 'first'){
                this.isFirst = true;
                this.isSecond = false;
            } else if (tab.name == 'second'){
                this.isFirst = false;
                this.isSecond = true;
            }
        }
    }
    }
</script>
<style>
    body,html {
        margin:0;
        padding:0;
    }
    .el-tabs__item {
        font-size: 16px;
        padding: 0 16px;
    }
    .el-tabs__header {
        padding: 0 24px;
        position: relative;
        margin: 0 0px 24px;
        border-bottom: 2px solid #E4E7ED;
    }
    .el-tabs__nav-wrap::after {
        background-color: #ffffff;
    }
</style>