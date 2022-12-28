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
<div class="layout">
            <el-tabs v-model='activeName' @tab-click="handleClick">
            <el-tab-pane label='生成指标' name='first' style="font-size:16px;">
            <header id="header">
                <el-card shadow="never">
                    <el-row class="rowColor">
                        <el-col :span="1" style="text-align: center;padding-top: 16px;">
                            <el-icon style="font-size:20px"><WarningFilled /></el-icon>
                        </el-col>
                        <el-col :span="23" style="font-size: 14px;">
                            <p class="tipck">监控平台：选择对应的监控平台，当前支持三种：Prometheus、Zabbix、Nagios</p>
                            <p class="tipck">指标分组：可以手动输入或者选择历史分组，分组可方便管理与发布</p>
                            <p class="tipck">采集间隔：对指标数据采集间隔时间设置</p>
                            <p class="tipck">实例名称：在生成指标时，会在所选实例上进行权限与可执行性校验</p>
                            <p class="tipck">SQL语句：输入需要监控的SQL语句，会进行权限校验与监控分析</p>
                            <p class="tipck">生成指标：根据不同监控平台，将已通过检验的SQL语句生成对应监控平台的指标文件</p>
                        </el-col>
                    </el-row>
                </el-card>
            </header>
            <div id="layout-body">
                <main id="main">
                    <el-form :model="ruleForm" :label-position="'left'" :rules="rules" ref="openForm" label-width="" class="demo-ruleForm"
                    style="margin: 16px 0px 0px 0px;">
                        <el-row>
                            <el-col :span="7">
                                  <el-form-item label="监控平台" prop="platform">
                                    <el-radio-group v-model="ruleForm.platform">
                                    <el-radio-button label="Prometheus"></el-radio-button>
                                    <el-radio-button label="Zabbix"></el-radio-button>
                                    <el-radio-button label="Nagios"></el-radio-button>
                                    </el-radio-group>
                                </el-form-item>
                            </el-col>
                            <el-col :span="6">
                                  <el-form-item label="指标分组" prop="targetGroup">
                                    <el-select
                                        style="width:230px"
                                        v-model="ruleForm.targetGroup"
                                        filterable
                                        allow-create
                                        default-first-option
                                        @blur="productSelect"
                                        :teleported="false"
                                        placeholder="请输入或选择指标分组">
                                        <el-option
                                        v-for="item in jobOptions"
                                        :key="item"
                                        :label="item"
                                        :value="item">
                                        <span>{{item}}</span>
                                        <span style="float: right;" @click.stop="handleDeleteTag(item)" ><div style="padding-left: 5px;padding-right: 5px"><el-icon><Close /></el-icon></div></span>
                                        </el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col>
                            <el-col :span="6">
                                <el-form-item label="采集间隔" prop="num">
                                    <el-input style="width:120px" v-model="ruleForm.num" v-on:change="change"></el-input>
                                    <el-select v-model="ruleForm.timeType" :teleported="false" style="width:120px;" prop="timeType"  placeholder="">
                                        <el-option
                                          v-for="item in options"
                                          :key="item.value"
                                          :label="item.label"
                                          :value="item.value">
                                        </el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col>
                            <el-col :span="5">
                                <el-form-item label="实例名称" prop="dataSourceId" >
                                    <el-select v-model="ruleForm.dataSourceId" :teleported="false" style="width:180px;" prop="dataSourceId"  placeholder="">
                                        <el-option
                                          v-for="item in hostOptions"
                                          :key="item.dataSourceId"
                                          :label="item.connectName"
                                          :value="item.dataSourceId">
                                        </el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row>
                            <el-col :span="24">
                                <el-form-item label="SQL语句" prop="target" >
                                    <el-input type="textarea" :rows="21" v-model="ruleForm.target" v-on:change="changeSql"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                    </el-form>
                </main>
                <footer id="footer">
                    <el-button size="large" type="primary" :disabled="disabledFlg" @click="debounceMethods(submitForm,1000,'openForm')">生成指标</el-button>
                </footer>
            </div>
            </el-tab-pane>
            <el-tab-pane label='指标管理' name='second' style="font-size:16px;">
            <div id="layout-body">
                <el-card shadow="never" style="margin-bottom: 16px;">
                    <el-row class="rowColor">
                        <el-col :span="1" style="text-align: center;padding-top: 16px;">
                            <el-icon style="font-size:20px"><WarningFilled /></el-icon>
                        </el-col>
                        <el-col :span="23" style="font-size: 14px;">
                            <p class="tipck">修改：对已生成指标文件数据进行修改</p>
                            <p class="tipck">删除：删除指标文件数据及相应的配置文件</p>
                            <p class="tipck" style="font-style:italic;">注：指标管理中包含150条默认指标，每个监控平台（Prometheus\Zabbix\Nagios）各50条，不支持对默认指标的删除操作</p>
                        </el-col>
                    </el-row>
                </el-card>
                    <el-table
                    :data="tableData"
                    style="width: 100%"
                    id="indexId"
                    :header-cell-style="{fontWeight:'normal',textAlign: 'center'}"
                    :cell-style="{padding: '4px 0px'}"
                    >
                    <el-table-column
                    prop="jobName"
                    label="SQLID"
                    width="80"
                    align="center">
                    </el-table-column>
                    <el-table-column
                    label="SQL详情"
                    prop="target"
                    show-overflow-tooltip
                    align="left"
                    >
                    <template v-slot="scope">
                        <a @click="detail(scope.row)" style="cursor:pointer;font-size: 14px;">{{
                        scope.row.target
                        }}</a>
                    </template>
                    </el-table-column>
                    <el-table-column
                    label="指标分组"
                    prop="targetGroup"
                    show-overflow-tooltip
                    width="120"
                    align="center"
                    >
                    </el-table-column>
                    <el-table-column
                    label="监控平台"
                    prop="platform"

                    width="100"
                    align="center"
                    >
                    </el-table-column>
                    <el-table-column
                    prop="createTime"
                    label="生成时间"
                    width="150"
                    align="center">
                    </el-table-column>
                    <el-table-column
                    label="操作"
                    width="120"
                    align="center">
                    <template v-slot="scope">
                        <el-button
                            @click.prevent="updateRow(scope.row)"
                            link type="primary"
                            size="small">
                            修改
                        </el-button>
                        <el-button
                            @click.prevent="deleteRow(scope.$index, tableData)"
                            link type="primary"
                            size="small" :disabled="scope.row.targetGroup == 'system_default'">
                            删除
                        </el-button>
                    </template>
                    </el-table-column>
              </el-table>
              <el-pagination
                background
                :pager-count="5"
                :total="total"
                @current-change="handleCurrentChange"
                :current-page="current" style="text-align: end;padding: 16px 0px 16px 16px;"
                @size-change="handleSizeChange"
                :page-sizes="[20, 50, 100]"
                :page-size="pageSize"
                layout="->,total, sizes, prev, pager, next, jumper"
                >
              </el-pagination>
            </div>
            </el-tab-pane>
        </el-tabs>
        <el-dialog
            title="SQL详情"
            v-model ="dialogVisible"
            width="50%"
            top="25vh"
            destroy-on-close>
            <div style="height:340px;overflow-y: auto;">
                <span>{{sqlContent}}</span>
            </div>
            <template v-slot:footer>
                <span class="dialog-footer">
                </span>
            </template>
        </el-dialog>
        <el-dialog
            :title="updateRuleForm.targetGroup != 'system_default'?'指标修改':'默认指标修改'"
            v-model ="indexDialogVisible"
            width="80%"
            top="8vh"
            class="indexClass"
            destroy-on-close
            :close-on-click-modal = false>
            <div style="height:100%;overflow-y: auto;">
                <el-form :model="updateRuleForm" :label-position="'left'" :rules="rules" ref="updateForm" label-width="auto" class="demo-ruleForm"
                        style="margin: 16px 0px 0px 0px;">
                            <el-row>
                                <el-col :span="7">
                                    <el-form-item label="监控平台" prop="platform">
                                        <el-popover
                                        placement="top-start"
                                        title=""
                                        :width="200"
                                        trigger="hover"
                                        :teleported="false"
                                        :content="updateRuleForm.targetGroup == 'system_default' ? '默认指标不允许修改监控平台' : '已发布的指标不允许修改监控平台'"
                                        :disabled="updateRuleForm.isCanUpdate && updateRuleForm.targetGroup != 'system_default'"
                                      >
                                        <template #reference>
                                            <el-radio-group v-model="updateRuleForm.platform" :disabled="!updateRuleForm.isCanUpdate || updateRuleForm.targetGroup == 'system_default'">
                                                <el-radio-button label="Prometheus"></el-radio-button>
                                                <el-radio-button label="Zabbix"></el-radio-button>
                                                <el-radio-button label="Nagios"></el-radio-button>
                                            </el-radio-group>
                                        </template>
                                      </el-popover>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="6">
                                    <el-form-item label="指标分组" prop="targetGroup">
                                        <el-select
                                            style="width:250px"
                                            v-model="updateRuleForm.targetGroup"
                                            :disabled="updateRuleForm.targetGroup == 'system_default'"
                                            filterable
                                            allow-create
                                            default-first-option
                                            @blur="productSelectDialog"
                                            :teleported="false"
                                            placeholder="请输入或选择指标分组">
                                            <el-option
                                            v-for="(item,Index) in jobOptions"
                                            :key="Index"
                                            :label="item"
                                            :value="item">
                                            <span>{{item}}</span>
                                            <span style="float: right;" @click.stop="handleDeleteTag(jobOptions[Index])" ><div style="padding-left: 5px;padding-right: 5px"><el-icon><Close /></el-icon></div></span>
                                            </el-option>
                                        </el-select>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="6">
                                    <el-form-item label="采集间隔" prop="num">
                                        <el-input style="width:120px" v-model="updateRuleForm.num" v-on:change="change"></el-input>
                                        <el-select v-model="updateRuleForm.timeType" :teleported="false" style="width:120px;" prop="timeType"  placeholder="">
                                            <el-option
                                            v-for="item in options"
                                            :key="item.value"
                                            :label="item.label"
                                            :value="item.value">
                                            </el-option>
                                        </el-select>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="5" v-if="updateRuleForm.targetGroup != 'system_default'">
                                    <el-form-item label="实例名称" prop="dataSourceId" >
                                        <el-select v-model="updateRuleForm.dataSourceId" :teleported="false" style="width:180px;" prop="dataSourceId"   placeholder="">
                                            <el-option
                                              v-for="item in hostOptions"
                                              :key="item.dataSourceId"
                                              :label="item.connectName"
                                              :value="item.dataSourceId">
                                            </el-option>
                                        </el-select>
                                    </el-form-item>
                                </el-col>
                            </el-row>
                            <el-row>
                                <el-col :span="24">
                                    <el-form-item label="SQL语句" prop="target" >
                                        <el-input type="textarea" :rows="26" v-model="updateRuleForm.target" v-on:change="changeSql" :disabled="updateRuleForm.targetGroup == 'system_default'"></el-input>
                                    </el-form-item>
                                </el-col>
                            </el-row>
                </el-form>
            </div>
            <template v-slot:footer>
                <span class="dialog-footer">
                    <el-button class="buttonColor" @click="indexDialogVisible = false">取 消</el-button>
                    <el-button type="primary" @click="debounceMethods(submitForm,1000,'updateForm')">保 存</el-button>
                </span>
            </template>
        </el-dialog>
</div>
</template>
<script setup>
    import { WarningFilled,Close } from '@element-plus/icons-vue';
    import { watch,ref,reactive,onMounted,getCurrentInstance} from 'vue';
    import { ElMessage,ElMessageBox } from "element-plus";
    import { useRouter } from 'vue-router';
                const router = useRouter();
                const { proxy } = getCurrentInstance();
                const openForm = ref(null);
                const updateForm = ref(null);
                let themeTableClass = ref(localStorage.getItem('opengauss-theme'));
                let dialogVisible = ref(false);
                let indexDialogVisible = ref(false);
                let sqlContent = ref('');
                let activeName = ref(localStorage.getItem("activeNameFile")?localStorage.getItem("activeNameFile"):"first");
                let current = ref(1);
                let total = ref(0);
                let pageSize = ref(20);
                let disabledFlg = ref(false);
                let debounceTimer = ref(null);
                let tableData = ref([]);
                let jobOptions = ref([]);
                let ruleForm = reactive({
                    jobId:null,
                    isFalse:false,
                    target:'',
                    num:5,
                    timeType:'second',
                    targetGroup:'',
                    platform:'Prometheus',
                    dataSourceId:''
                });
                const updateRuleForm = reactive({data:{
                    jobId:null,
                    isFalse:false,
                    target:'',
                    num:5,
                    timeType:'second',
                    targetGroup:'',
                    platform:'Prometheus',
                    dataSourceId:'',
                    isCanUpdate:true
                }});
                const hostOptions= ref([]);
                const options = reactive([{
                    value: 'second',
                    label: '秒'
                    },{
                    value: 'minute',
                    label: '分'
                    },{
                    value: 'hour',
                    label: '时'
                    },{
                    value: 'day',
                    label: '日'
                    },{
                    value: 'week',
                    label: '周'
                    },{
                    value: 'month',
                    label: '月'
                    },{
                    value: 'year',
                    label: '年'
                    }]);
                const rules = reactive({
                    platform:[
                        {required:true,message:'请选择监控平台',trigger:'blur'}],
                    targetGroup:[
                        {required:true,message:'请输入或选择指标分组',trigger:'blur'}],
                    target:[
                        {required:true,message:'请输入SQL语句',trigger:'blur'}],
                    num:[
                        {required:true,message:'请输入采集间隔时间',trigger:'blur'},
                        {pattern: /^[0-9]{1,3}$/,message: "只能输入1~3位数字",trigger: "blur"}],
                    dataSourceId:[
                        {required:true,message:'请选择实例名称',trigger:'blur'}],
                });
        onMounted(() => {
                getJobs();
                isZabbixData();
                hostData();
            if(activeName.value == 'second'){
                checkSql();
            }
            window.$wujie?.bus.$on('opengauss-theme-change', () => {
                console.log('fileGeneration')
                themeTableClass.value = localStorage.getItem('opengauss-theme');
            })
        });
        watch(activeName, (newName) => {
            localStorage.setItem("activeNameFile",newName);
        });
        watch(() => router.currentRoute.value.path,() => {
            localStorage.removeItem("activeNameFile")
        })
        const hostData = () => {
            proxy.$http.get('/data/source/name').then(res=>{
                    if(res.data.code == 200){
                        if(res.data.data){
                            hostOptions.value = res.data.data;
                        }
                    }
                }).catch(()=>{
                    ElMessage.warning("请求失败")
                })
        };
        const productSelect = (e) => {
        let value = e.target.value; 
            if(value) { 
                ruleForm.targetGroup = value;
            }
        };
        const productSelectDialog = (e) => {
        let value = e.target.value; 
            if(value) { 
                updateRuleForm.targetGroup = value
            }
        };
        // Determine whether the configuration information already exists for zabbix
        const isZabbixData = () => {
            proxy.$http.get('/data/zabbix/config').then(res=>{
                    if(res.data.code == 200){
                        if(res.data.data && res.data.data.ip){
                            ruleForm.platform = 'Zabbix';
                        }
                    } else if(res.data.code == 400){
                        return false;
                    }
                }).catch(()=>{
                    ElMessage.warning("请求失败")
                })
        };
        // tabs click event
        const handleClick = (tab) => {
            if(tab.props.name == 'second'){
                checkSql();
            }
        };
        // button throttle
        const debounceMethods = (func,time,...args) =>{
            let context = this;
            if (debounceTimer.value){
                return
            }
            let callNow = !debounceTimer.value;
            debounceTimer.value = setTimeout(() => {
                debounceTimer.value = null;
            },time)
            if(callNow){
                func.apply(context,args)
            }
        };
        // Get indicator group list information
        const getJobs = () => {
            proxy.$http.get('/monitor/job/group').then(res=>{
                    if(res.data.code == 200){
                        if(window.localStorage.getItem('jobOptions') !== null){
                            jobOptions.value = JSON.parse(window.localStorage.getItem('jobOptions'));
                        } else {
                            jobOptions.value = res.data.data;
                            window.localStorage.setItem('jobOptions',JSON.stringify(jobOptions.value));
                        }
                    } else if(res.data.code == 400){
                        return false;
                    }
                }).catch(()=>{
                    ElMessage.warning("指标分组查询失败")
                })
        };
        // SQL Details
        const detail = (row) => {
            dialogVisible.value = true;
            sqlContent.value = row.target;
        };
        // Delete the group information of front-end cache
        const handleDeleteTag = (tag) => {
            jobOptions.value = JSON.parse(window.localStorage.getItem('jobOptions'));
            jobOptions.value.splice((jobOptions.value).indexOf(tag), 1);
            window.localStorage.setItem('jobOptions',JSON.stringify(jobOptions.value));
        };
        const handleCurrentChange = (val) => {
            current.value = val;
            checkSql();
        };
        const handleSizeChange = (val) => {
            pageSize.value = val;
            checkSql();
        };
        // Query Indicator List
        const checkSql = () => {
        proxy.$http.post('/monitor/job/list/'+current.value+'/'+pageSize.value,{}).then(res=>{
            if(res.data.code == 200){
                tableData.value = res.data.data.tableData;
                total.value = res.data.total;
            } else {
                ElMessage.warning("查询失败");
            }
            }).catch(()=>{
                ElMessage.warning("查询失败");
        })
        };
        // Modify indicator information
        const updateRow = (rows) => {
            indexDialogVisible.value = true;
            Object.assign(updateRuleForm, rows)
        };
        // Delete indicator information
        const deleteRow = (Index, data) => {
            let tips;
            proxy.$http.post('/monitor/job/check', JSON.stringify([data[Index].jobId])).then(res=>{
                        if(res.data.code == 200 && res.data.message){
                            tips = res.data.message;
                        } else {
                            tips = "确认删除该指标, 是否继续?";
                        }
                        ElMessageBox.confirm(tips, "提示", {
                            confirmButtonText: "确定",
                            cancelButtonText: "取消",
                            type: "warning"
                            })
                            .then(() => {
                                proxy.$http.post('/monitor/job/delete', JSON.stringify([data[Index].jobId])).then(res=>{
                                    if(res.data.code == 200){
                                        ElMessage.success("删除成功");
                                        checkSql();
                                    } else if(res.data.code == 400){
                                        ElMessage.warning(res.data.message);
                                    }
                                }).catch(()=>{
                                    ElMessage.warning("删除失败");
                                })
                            })
                            .catch(() => {
                                ElMessage({
                                    type: "info",
                                    message: "已取消删除"
                                });
                            });
                    }).catch(()=>{
                        ElMessage.warning("查询失败");
                    })
                proxy.$unFocus();
        };
        // Monitoring interval time input box
        const change = (value) => {
            if (value == 0) {
              disabledFlg.value = true;
            } else {
              disabledFlg.value = false;
            }
        };
        // Monitor sql input box
        const changeSql = () => {
            if(ruleForm.num != 0){
                disabledFlg.value = false;
            } else{
                ElMessage.warning("采集间隔时间不能为0");
            }
        };
        // makefile
        const submitForm = (formName) => {
            proxy.$refs[formName].validate((valid) => {
            if (valid) {
                let formData,url;
                if(activeName.value == 'first'){
                   formData =  ruleForm;
                   url = '/monitor/job/create'
                } else {
                   formData = updateRuleForm;
                    url = '/monitor/job/update'
                }
                proxy.$http.post(url, JSON.stringify(formData)).then(res=>{
                    if(res.data.code == 200){
                        disabledFlg.value = true;
                        ruleForm.jobId = '';
                        indexDialogVisible.value = false;
                        if(activeName.value == 'first'){
                            ElMessage.success("生成成功");
                        } else {
                            ElMessage.success("修改成功");
                        }
                        jobOptions.value = JSON.parse(window.localStorage.getItem('jobOptions'));
                        if((jobOptions.value).indexOf(formData.targetGroup) == -1){
                            (jobOptions.value).unshift(formData.targetGroup);
                        }
                        window.localStorage.setItem('jobOptions',JSON.stringify(jobOptions.value));
                        checkSql();
                    } else if(res.data.code == 300){
                        if(res.data.err_message){
                            ElMessage({
                                showClose: true,
                                duration:0,
                                message: res.data.err_message,
                                type: 'warning'
                            })
                        }
                    } else if(res.data.code == 400){
                        ElMessage.warning(res.data.message);
                    } else if(res.data.code == 600){
                        ElMessageBox.confirm("该SQL可能无法生成指标，是否强制执行?", "提示", {
                            confirmButtonText: "确定",
                            cancelButtonText: "取消",
                            type: "warning"
                        })
                        .then(() => {
                            ruleForm.isFalse = true;
                            proxy.$http.post('/monitor/job/create', JSON.stringify(ruleForm,)).then(res=>{
                                if(res.data.code == 200){
                                    ruleForm.isFalse = false;
                                    indexDialogVisible.value = false;
                                    ElMessage.success("生成成功");
                                    jobOptions.value = JSON.parse(window.localStorage.getItem('jobOptions'));
                                    if((jobOptions.value).indexOf(formData.targetGroup) == -1){
                                        (jobOptions.value).unshift(formData.targetGroup);
                                    }
                                    window.localStorage.setItem('jobOptions',JSON.stringify(jobOptions.value));
                                    checkSql();
                                }
                            }).catch(()=>{
                                ruleForm.isFalse = false;
                                ElMessage.warning("生成失败");
                            })
                        })
                        .catch(() => {
                            ElMessage({
                                type: "info",
                                message: "已取消生成"
                            });
                        });
                    }
                }).catch(()=>{
                    ElMessage.warning("生成失败");
                })
            } else {
                return false;
            }
            });
        }
</script>
