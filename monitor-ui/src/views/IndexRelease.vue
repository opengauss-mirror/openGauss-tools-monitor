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
            <header id="header">
                <span class="dataStyle">指标发布</span>
                <div style="padding-bottom: 16px;">
                    <el-button size="large" type="primary" @click="updateRow('')">批量发布</el-button>
                    <el-button size="large" class="buttonColor" @click="deleteRow('','','all')">批量删除</el-button>
                </div>
            </header>
            <div id="layoutCss">
                <el-card shadow="never" style="margin-bottom: 16px;">
                    <el-row class="rowColor">
                        <el-col :span="1" style="text-align: center;padding-top: 16px;">
                            <el-icon style="font-size:20px"><WarningFilled /></el-icon>
                        </el-col>
                        <el-col :span="23" style="font-size: 14px;">
                            <p class="tipck">发布指标：对已生成的SQL指标进行发布，支持按指标分组、监控平台进行指标筛选，同时回显已发布过的SQL指标，选中指标即可发布到主机</p>
                            <p class="tipck">修改主机：对主机信息进行修改</p>
                            <p class="tipck">删除主机：对主机进行删除，同时该主机上所有已发布指标也一同删除</p>
                            <p class="tipck">批量发布：可选中多台主机进行批量发布，支持按指标分组、监控平台进行指标筛选，选中指标即可批量发布到主机</p>
                            <p class="tipck">批量删除：删除选中的主机，同时对应主机所有已发布的指标也一同删除</p>
                        </el-col>
                    </el-row>
                </el-card>
                    <el-table
                    :data="tableData"
                    style="width: 100%"
                    ref="table"
                    :header-cell-style="{fontWeight:'normal',textAlign: 'center'}"
                    :cell-style="{padding: '4px 0px',textAlign: 'center'}"
                    row-key="dataSourceId"
                    >
                    <el-table-column
                    type="selection"
                    width="55"
                    align="center"
                    :reserve-selection="true">
                    </el-table-column>
                    <el-table-column type="expand">
                        <template v-slot="props">
                          <el-table 
                          :data="props.row.targets"
                          :cell-style="{padding: '4px 0px',textAlign: 'center'}"
                          :header-cell-style="{fontWeight:'normal',textAlign: 'center'}"
                          style="width: calc(100% - 103px);margin-left: 103px;">
                            <el-table-column
                            prop="createTime"
                            label="生成时间"
                            width="150"
                            >
                            </el-table-column>
                            <el-table-column
                            label="SQL详情"
                            prop="target"
                            show-overflow-tooltip
                            >
                            </el-table-column>
                            <el-table-column
                            label="指标分组"
                            prop="targetGroup"
                            show-overflow-tooltip
                            width="100"
                            >
                            </el-table-column>
                            <el-table-column
                            label="监控平台"
                            prop="platform"
                            show-overflow-tooltip
                            width="150"
                            >
                            </el-table-column>
                            <el-table-column label="操作" width="220">
                              <template v-slot="scope">
                                <el-button
                                @click.prevent="deleteRowIndex(scope.$index, props.row,'')"
                                link type="primary"
                                size="small">
                                删除指标
                            </el-button>
                              </template>
                            </el-table-column>
                          </el-table>
                        </template>
                      </el-table-column>
                    <el-table-column
                    prop="connectName"
                    label="实例名称"
                    show-overflow-tooltip
                    width="150"
                    >
                    </el-table-column>
                    <el-table-column
                    label="主机地址"
                    prop="ip"
                    show-overflow-tooltip
                    >
                    </el-table-column>
                    <el-table-column
                    label="主机端口"
                    prop="port"
                    show-overflow-tooltip
                    width="100"
                    >
                    </el-table-column>
                    <el-table-column
                    label="最后发布时间"
                    prop="lastReleaseTime"
                    show-overflow-tooltip
                    width="150"
                    >
                    </el-table-column>
                    <el-table-column
                    label="操作"
                    width="220">
                    <template v-slot="scope">
                        <el-button
                            @click.prevent="updateRow(scope.row)"
                            link type="primary"
                            size="small">
                            发布指标
                        </el-button>
                        <el-button
                            @click.prevent="detail(scope.row)"
                            link type="primary"
                            size="small">
                            修改主机
                        </el-button>
                        <el-button
                            @click.prevent="deleteRow(scope.$index, tableData,'')"
                            link type="primary"
                            size="small">
                            删除主机
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
        <el-dialog
            title="主机配置修改"
            v-model ="dialogVisible"
            width="33%"
            top="25vh"
            class="indexClass"
            destroy-on-close
            :close-on-click-modal = false>
            <div>
                        <el-form :model="ruleForm" :label-position="'left'" :rules="rulesHost" ref="updateForm" label-width="82px" class="demo-ruleForm "
                            style="margin-top: 14px;">
                            <el-form-item label="实例名称" prop="connectName">
                                <el-input v-model="ruleForm.connectName" placeholder="示例：业务名_master" :readonly="isReadOnly"></el-input>
                            </el-form-item>
                            <el-form-item label="主机地址" prop="ip">
                                <el-input v-model="ruleForm.ip" placeholder="示例：192.168.0.10" :readonly="true"></el-input>
                            </el-form-item>
                            <el-form-item label="主机端口" prop="port">
                                <el-input v-model="ruleForm.port" placeholder="示例：5432"></el-input>
                            </el-form-item>
                            <el-form-item label="只读用户" prop="userName">
                                <el-input v-model="ruleForm.userName" placeholder="示例：zabbix_readonly"></el-input>
                            </el-form-item>
                            <el-form-item label="登录密码" prop="password">
                                <el-input type="password" v-model="ruleForm.password" placeholder="示例：d(*dS1"></el-input>
                            </el-form-item>
                        </el-form>
            </div>
            <template v-slot:footer>
                <span class="dialog-footer">
                    <el-button class="buttonColor" @click="dialogVisible = false">取 消</el-button>
                    <el-button type="primary" @click="updateHost('updateForm')">保 存</el-button>
                </span>
            </template>
        </el-dialog>
        <el-dialog
            title="指标发布"
            v-model ="indexDialogVisible"
            width="60%"
            class="indexClass"
            destroy-on-close
            :close-on-click-modal = false>
            <div style="height:56vh;">
                    <el-table
                    :data="tableDataIndex.slice((currentIndex - 1) * pageSizeIndex, (currentIndex - 1) * pageSizeIndex + pageSizeIndex)"
                    style="width: 100%"
                    class="indexClass"
                    ref="indexTable"
                    :header-cell-style="{fontWeight:'normal'}"
                    :cell-style="{padding: '8px 0px'}"
                    @filter-change="filterChange"
                    :row-key="getRowKey"
                    @selection-change="handleChangeSelection"
                    >
                    <el-table-column
                    type="selection"
                    width="55"
                    align="center"
                    :reserve-selection="true">
                    </el-table-column>
                    <el-table-column
                    prop="createTime"
                    label="生成时间"
                    width="150"
                    >
                    <template #header>
                        <span :style="{'color':params.timeInterval ?'#e41d1d':''}">生成时间</span>
                        <el-popover  ref="setRemovePop" placement="bottom-start" width="auto" trigger="click" :popper-options="{ boundariesElement: 'viewport', removeOnDestroy: true }">
                            <div class="el-table-filter__checkbox-group">
                                <span class="demonstration">时间区间：</span>
                                <el-date-picker
                                  v-model="params.timeInterval"
                                  type="daterange"
                                  :teleported="false"
                                  :editable="false"
                                  :clearable="false"
                                  start-placeholder="开始时间"
                                  end-placeholder="结束时间"
                                  value-format="YYYY-MM-DD HH:mm:ss"
                                />
                            </div>
                            <div class="el-table-filter__bottom"><button :class="params.timeInterval ? '' : 'is-disabled'"
                                type="button" @click="searchDate()">确定</button><button type="button" @click="resetDate()">重置</button></div>
                            <template #reference>
                                <span class="el-table__column-filter-trigger">
                                    <el-icon>
                                        <Search />
                                    </el-icon>
                                </span>
                            </template>
                          </el-popover>
                    </template>
                    </el-table-column>
                    <el-table-column
                    label="SQL详情"
                    prop="target"
                    show-overflow-tooltip
                    >
                    </el-table-column>
                    <el-table-column
                    label="指标分组"
                    prop="targetGroup"
                    show-overflow-tooltip
                    width="200"
                    column-key="targetGroup"
                    :filters="targetGroup"
                    >
                    </el-table-column>
                    <el-table-column
                    label="监控平台"
                    prop="platform"
                    width="200"
                    show-overflow-tooltip
                    column-key="platform"
                    :filters="platform"
                    >
                    </el-table-column>
              </el-table>
              <el-pagination
                background
                :pager-count="5"
                :total="totalIndex"
                @current-change="handleCurrentIndexChange"
                :current-page="currentIndex" style="text-align: end;padding: 16px 0px 16px 16px;"
                @size-change="handleIndexSizeChange"
                :page-sizes="[20, 50, 100]"
                :page-size="pageSizeIndex"
                layout="->,total, sizes, prev, pager, next, jumper"
                >
              </el-pagination>
            </div>
            <template v-slot:footer>
                <span class="dialog-footer">
                    <el-button class="buttonColor" @click="indexDialogVisible = false">取 消</el-button>
                    <el-button type="primary" @click="submitForm()">发 布</el-button>
                </span>
            </template>
        </el-dialog>
</div>
</template>
<script setup>
    import { WarningFilled,Search } from '@element-plus/icons-vue'
    import { watch,ref,reactive,onMounted,getCurrentInstance,nextTick } from 'vue';
    import { ElMessage,ElMessageBox } from "element-plus";
    const { proxy } = getCurrentInstance();
                const updateForm = ref(null);
                const themeButton = ref(localStorage.getItem('opengauss-theme'));
                let isReadOnly = ref(false);
                let isOneDelete = ref(0);
                let isAllDelete = ref(true);
                let dialogVisible = ref(false);
                let indexDialogVisible = ref(false);
                let setRemovePop = ref(null);
                let current = ref(1);
                let total = ref(0);
                let pageSize = ref(20);
                let currentIndex = ref(1);
                let totalIndex = ref(0);
                let pageSizeIndex = ref(20);
                let platform = ref([]);
                let targetGroup = ref([]);
                let ruleForm = reactive({
                    connectName:'',
                    userName:'',
                    password:'',
                    port:'',
                    ip:'',
                    platform:'Prometheus',
                    dataSourceId:''
                });
                let dataSourceId = reactive([]);
                let jobIds = ref([]);
                let disabledRelease = ref(true);
                let tableData = ref([]);
                let tableDataIndex = ref([]);
                let params = reactive({
                    timeInterval:null,
                    platform:'',
                    targetGroup:'',
                    dataSourceId:'',
                    isManagement:true
                });
                const rulesHost = reactive({
                    connectName:[
                        { required: true, message: '请输入实例名称', trigger: 'blur' },
                        { pattern: /^[0-9A-Za-z_]{2,16}$/g, message: '连接名称只支持英文、下划线、数字组合2-16个字符' }],
                    userName:[
                        { required: true, message: '请输入只读用户', trigger: 'blur' },
                        { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur' }],
                    password:[
                        { required: true, message: '请输入登录密码', trigger: 'blur' },
                        { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur' }],
                        ip:[
                        { required: true, message: '请输入主机地址', trigger: 'blur' },
                        { pattern: /^(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])$/, message: '只允许输入IP地址' }],
                    port:[
                        { required: true, message: '请输入主机端口', trigger: 'blur' },
                        { pattern: /^((6[0-4]\d{3}|65[0-4]\d{2}|655[0-2]\d|6553[0-5])|[0-5]?\d{0,4})$/g, message: '只允许输入端口号' }]
                });
        onMounted(() => {
            getFormData();
            window.$wujie?.bus.$on('opengauss-theme-change', () => {
                themeButton.value = localStorage.getItem('opengauss-theme');
            })
        });
        watch(indexDialogVisible, (newName) => {
            if(newName == false){
                params.platform = '';
                params.targetGroup = '';
                params.timeInterval = null;
                dataSourceId = [];
                totalIndex.value = 0;
                tableDataIndex.value = [];
            }
        });
        watch(dialogVisible, (newName) => {
            if(newName == false){
                isReadOnly.value = false;
            }
        });
         // List Filter Status
         const filterChange = (filter) => {
            if(filter['platform']){
            // Modify the parameters passed to the back-end interface and call the interface again
                params.platform = filter.platform.join(',')
                checkSql();
            }
            else{
                params.targetGroup = filter.targetGroup.join(',')
                checkSql();
            }
        };
        const searchDate = () => {
            if(params.timeInterval){
                checkSql();
                setRemovePop.value.hide();
            }
        }
        const resetDate = () => {
            params.timeInterval = null;
            checkSql();
        }
        const unique = (arr) => {
            const res = new Map();
            return arr.filter((arr) => !res.has(arr.jobId) && res.set(arr.jobId, 1))
        }
        // Query host list
        const getFormData = () => {
        proxy.$http.get('/data/list/source/'+current.value+'/'+pageSize.value).then(res=>{
            if(res.data.code == 200){
                tableData.value = res.data.data;
                total.value = res.data.total;
                if(res.data.sql_num != 0){
                    disabledRelease.value = false;
                }
            } else {
                ElMessage.warning("查询失败");
            }
            }).catch(()=>{
                ElMessage.warning("查询失败");
        })
        };
        // Modify host information
        const detail = (row) => {
            if(row.targets.length > 0){
                isReadOnly.value = true;
            }
            dialogVisible.value = true;
            ruleForm.connectName = row.connectName;
            ruleForm.userName = row.userName;
            ruleForm.port = row.port;
            ruleForm.ip = row.ip;
            ruleForm.dataSourceId = row.dataSourceId;
        };
        const handleCurrentChange = (val) => {
            current.value = val;
            getFormData();
        };
        const handleSizeChange = (val) => {
            pageSize.value = val;
            getFormData();
        };
        const handleIndexSizeChange = (val) => {
            pageSizeIndex.value = val;
        };
        const handleCurrentIndexChange = (val) => {
            currentIndex.value = val;
        };
        const handleChangeSelection = () => {
        };
        const getRowKey = (row) => {
            return row.jobId;
        };
        // Query Indicator List
        const checkSql = () => {
        proxy.$http.post('/monitor/job/list/'+currentIndex.value+'/'+pageSizeIndex.value,JSON.stringify(params)).then(res=>{
            if(res.data.code == 200){
                tableDataIndex.value = res.data.data.tableData;
                totalIndex.value = res.data.total;
                targetGroup.value = res.data.data.targetGroup;
                platform.value = res.data.data.platForm;
                res.data.data.tableData.filter(item => {
                    if(item.isPbulish && !params.platform && !params.targetGroup && !params.timeInterval){
                        nextTick(()=>{
                            proxy.$refs["indexTable"].toggleRowSelection(item,true);
                        })
                    }            
                });
            } else {
                ElMessage.warning("查询失败");
            }
            }).catch(()=>{
                ElMessage.warning("查询失败");
        })
        };
        // Publish indicators individually or in batch
        const updateRow = (rows) => {
            proxy.$unFocus();
            if(proxy.$refs["table"].getSelectionRows().length == 0 && rows == ''){
                ElMessage.warning("请至少选中一台主机信息"); 
                return false;
            }
            indexDialogVisible.value = true;
            if(proxy.$refs["table"].getSelectionRows().length > 0 && rows == ''){
                proxy.$refs["table"].getSelectionRows().filter(item => {            
                    dataSourceId.push(item.dataSourceId)
                });
                if(proxy.$refs["table"].getSelectionRows().length == 1){
                    params.dataSourceId = proxy.$refs["table"].getSelectionRows().dataSourceId;
                } else {
                    params.dataSourceId = '';
                }
            } else {
                dataSourceId.push(rows.dataSourceId);
                params.dataSourceId = rows.dataSourceId;
            }
            checkSql();
        };
        // Save host information
        const updateHost = (formName) => {
            proxy.$refs[formName].validate((valid) => {
            if (valid) {
                proxy.$http.post('/data/update/config', JSON.stringify(ruleForm)).then(res=>{
                    if(res.data.code == 200){
                        dialogVisible.value = false;
                        getFormData();
                        ElMessage.success("保存成功")
                    } else if(res.data.code == 400){
                        ElMessage.warning(res.data.message);
                    }
                }).catch(()=>{
                    ElMessage.warning("保存失败")
                })
            } else {
                return false;
            }
            });
        };
        // Delete hosts individually or in bulk
        const deleteRow = (Index, data,rows) => {
            proxy.$unFocus();
            let tips,arrayData = [];
            if(rows){
                if(proxy.$refs["table"].getSelectionRows().length == 0){
                    ElMessage.warning("请至少选中一台主机信息"); 
                    return false;
                }
                proxy.$refs["table"].getSelectionRows().filter(item => {            
                    arrayData.push(item.dataSourceId)
                    if(item.targets.length > 0){
                        isAllDelete.value = false
                    }    
                });
                if(!isAllDelete.value){
                    tips = "该主机下存在已发布指标, 是否继续删除?"
                } else{
                    tips = "确认批量删除多台主机, 是否继续?"
                }
            } else {
                isOneDelete.value = data[Index].targets.length;
                arrayData = [data[Index].dataSourceId];
                if(isOneDelete.value > 0){
                    tips = "该主机下存在已发布指标, 是否继续删除?"
                } else{
                    tips = "确认删除该主机, 是否继续?"
                }
            }
            ElMessageBox.confirm(tips, "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                })
                .then(() => {
                    proxy.$http.post('/data/delete', JSON.stringify(arrayData)).then(res=>{
                                if(res.data.code == 200){
                                    ElMessage.success("删除成功");
                                    getFormData();
                                } else if(res.data.code == 400){
                                    ElMessage.warning(res.data.message);
                                }
                                isAllDelete.value = true;
                                isOneDelete.value = 0;
                            }).catch(()=>{
                                ElMessage.warning("删除失败");
                                isAllDelete.value = true;
                                isOneDelete.value = 0;
                            })
                })
                .catch(() => {
                    ElMessage({
                        type: "info",
                        message: "已取消删除"
                    });
                });
        };
        // Indicator information for unbinding with host
        const deleteRowIndex = (Index, data) => {
            ElMessageBox.confirm("确认删除该指标, 是否继续?", "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                })
                .then(() => {
                    let param = {
                        dataSourceId:data.dataSourceId,
                        jobIds:[data.targets[Index].jobId]
                    }
                    proxy.$http.post('/monitor/job/single/publish/pause', JSON.stringify(param)).then(res=>{
                        if(res.data.code == 200){
                            ElMessage.success("删除成功");
                            getFormData();
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
                proxy.$unFocus();
        };
        // Save Publication
        const submitForm = () => {
            if(unique(proxy.$refs["indexTable"].getSelectionRows()).length == 0){
                ElMessage.warning("请至少选中一条指标"); 
                return false;
            }
            unique(proxy.$refs["indexTable"].getSelectionRows()).filter(item => {            
                jobIds.value.push(item.jobId)
            });
            let param = {
                dataSourceId:dataSourceId,
                jobIds:jobIds.value
            }
            proxy.$http.post('/monitor/job/batch/publish', JSON.stringify(param)).then(res=>{
                if(res.data.code == 200){
                    ElMessage.success("发布成功");
                    indexDialogVisible.value = false;
                    jobIds.value = [],
                    dataSourceId = [];
                    getFormData();
                } else if(res.data.code == 400){
                    jobIds.value = [];
                    ElMessage.warning(res.data.message);
                } 
            }).catch(()=>{
                ElMessage.warning("发布失败");
            })
        };
</script>
