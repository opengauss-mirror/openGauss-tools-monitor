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
<div class="wrap">
    <section id="right">
        <el-tabs v-model='activeName'>
            <el-tab-pane label='openGauss' name='first' style="font-size:16px;">
                <el-card shadow="never">
                    <el-row class="rowColor">
                        <el-col :span="1" style="text-align: center;padding-top: 16px;">
                            <el-icon style="font-size:20px"><WarningFilled /></el-icon>
                        </el-col>
                        <el-col :span="23" style="font-size: 14px;">
                            <p class="tipck">实例名称：被监控源端数据库的实例别名，建议按业务规则命名</p>
                            <p class="tipck">主机地址：被监控源端数据库的主机IP地址</p>
                            <p class="tipck">主机端口：被监控源端数据库的主机端口</p>
                            <p class="tipck">只读用户：被监控源端数据库的用户，对监控对象具有只读权限</p>
                            <p class="tipck">登录密码：被监控源端数据库的密码</p>
                            <p class="tipck" style="font-style:italic;">注：需要提前将本服务部署地址加入到openGauss的pg_hba.conf中进行客户端接入认证</p>
                        </el-col>
                    </el-row>
                </el-card>
                <el-row style="margin-top: 24px;">
                    <el-col :span="10" style="height: 100%;">
                        <el-form :model="ruleForm" :label-position="'left'" :rules="rules" ref="openForm" label-width="90px" class="demo-ruleForm "
                        style="margin: 0px 24px 0px 0px;">
                            <el-form-item label="实例名称" prop="connectName">
                                <el-input v-model="ruleForm.connectName" placeholder="示例：business_master"></el-input>
                            </el-form-item>
                            <el-form-item label="主机地址" prop="ip">
                                <el-input v-model="ruleForm.ip" placeholder="示例：192.168.0.10"></el-input>
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
                    </el-col>
                    <el-col :span="24">
                        <div style="text-align: center;padding: 5vh 0px 32px 0px;">
                            <el-button size="large" type="primary" @click="submitForm('openForm')">保存配置</el-button>
                            <el-button size="large" class="buttonColor" @click="resetForm('openForm')">重置数据</el-button>
                        </div>
                    </el-col>
                </el-row>
            </el-tab-pane>
            <el-tab-pane label='Zabbix' name='second' style="font-size:16px;">
                <el-card shadow="never">
                    <el-row class="rowColor">
                        <el-col :span="1" style="text-align: center;padding-top: 16px;">
                            <el-icon style="font-size:20px"><WarningFilled /></el-icon>
                        </el-col>
                        <el-col :span="23" style="font-size: 14px;">
                            <p class="tipck">实例名称：zabbix监控平台元数据库实例名</p>
                            <p class="tipck">主机地址：zabbix监控平台元数据库主机IP地址</p>
                            <p class="tipck">主机端口：zabbix监控平台元数据库主机端口</p>
                            <p class="tipck">数据库名：zabbix监控平台元数据库数据库库名</p>
                            <p class="tipck">读写用户：zabbix监控平台元数据库用户，具有读写权限</p>
                            <p class="tipck">登录密码：zabbix监控平台元数据库密码</p>
                        </el-col>
                    </el-row>
                </el-card>
                <el-row style="margin-top: 24px;">
                    <el-col :span="10" style="height: 100%;">
                        <el-form :model="zabbixRuleForm" :label-position="'left'" :rules="rules" ref="zabbixForm" label-width="90px" class="demo-ruleForm "
                        style="margin: 0px 24px 0px 0px;">
                            <el-form-item label="实例名称" prop="connectName">
                                <el-input v-model="zabbixRuleForm.connectName" placeholder="示例：business_master"></el-input>
                            </el-form-item>
                            <el-form-item label="主机地址" prop="ip">
                                <el-input v-model="zabbixRuleForm.ip" placeholder="示例：192.168.0.10"></el-input>
                            </el-form-item>
                            <el-form-item label="主机端口" prop="port">
                                <el-input v-model="zabbixRuleForm.port" placeholder="示例：5432"></el-input>
                            </el-form-item>
                            <el-form-item label="数据库名" prop="dataBaseName">
                                <el-input v-model="zabbixRuleForm.dataBaseName" placeholder="示例：zabbix"></el-input>
                            </el-form-item>
                            <el-form-item label="读写用户" prop="userName">
                                <el-input v-model="zabbixRuleForm.userName" placeholder="示例：zabbix_readonly"></el-input>
                            </el-form-item>
                            <el-form-item label="登录密码" prop="password">
                                <el-input type="password" v-model="zabbixRuleForm.password" placeholder="示例：d(*dS1"></el-input>
                            </el-form-item>
                        </el-form>
                    </el-col>
                    <el-col :span="24">
                        <div style="text-align: center;padding: 5vh 0px 32px 0px;">
                            <el-button size="large" type="primary" @click="submitForm('zabbixForm')">保存配置</el-button>
                            <el-button size="large" class="buttonColor" @click="resetForm('zabbixForm')">重置数据</el-button>
                        </div>
                    </el-col>
                </el-row>
            </el-tab-pane>
            <el-tab-pane label='Nagios' name='three' style="font-size:16px;">
                <el-card shadow="never">
                    <el-row class="rowColor">
                        <el-col :span="1" style="text-align: center;padding-top: 16px;">
                            <el-icon style="font-size:20px"><WarningFilled /></el-icon>
                        </el-col>
                        <el-col :span="23" style="font-size: 14px;">
                            <el-row class="rowColor">
                                <el-col :span="12">
                                    <p class="tipck">服务端地址：nagios监控平台服务端数据库主机IP地址</p>
                                    <p class="tipck">服务端用户名：nagios监控平台服务端数据库用户，具有读写权限</p>
                                    <p class="tipck">服务端密码：nagios监控平台服务端数据库密码</p>
                                    <p class="tipck">服务端路径：nagios监控平台服务端安装路径</p>
                                </el-col>
                                <el-col :span="12">
                                    <p class="tipck">客户端地址：nagios监控平台客户端数据库主机IP地址</p>
                                    <p class="tipck">客户端用户名：nagios监控平台客户端数据库用户，具有读写权限</p>
                                    <p class="tipck">客户端密码：nagios监控平台客户端数据库密码</p>
                                    <p class="tipck">客户端路径：nagios监控平台客户端安装路径</p>
                                </el-col>
                            </el-row>
                        </el-col>
                    </el-row>
                </el-card>
                <el-row style="margin-top: 24px;">
                    <el-col :span="24" style="height: 100%;">
                        <el-form :model="nagiosRuleForm" :label-position="'left'" :rules="rules" ref="nagiosForm" label-width="110px" class="demo-ruleForm ">
                            <div class="parent">
                                <div class="left">
                                    <el-form-item label="服务端地址" prop="serverIp">
                                        <el-input v-model="nagiosRuleForm.serverIp" placeholder="示例：192.168.0.10"></el-input>
                                    </el-form-item>
                                    <el-form-item label="服务端用户名" prop="serverName">
                                        <el-input v-model="nagiosRuleForm.serverName" placeholder="示例：nagios_readonly"></el-input>
                                    </el-form-item>
                                    <el-form-item label="服务端密码" prop="serverPassword">
                                        <el-input type="password" v-model="nagiosRuleForm.serverPassword" placeholder="示例：d(*dS1"></el-input>
                                    </el-form-item>
                                    <el-form-item label="服务端路径" prop="serverPath">
                                        <el-input v-model="nagiosRuleForm.serverPath" placeholder="示例：/usr/local/nagios"></el-input>
                                    </el-form-item>
                                </div>
                                <div class="right">
                                    <el-form-item label="客户端地址" prop="clientIp">
                                        <el-input v-model="nagiosRuleForm.clientIp" placeholder="示例：192.168.0.10"></el-input>
                                    </el-form-item>
                                    <el-form-item label="客户端用户名" prop="clientName">
                                        <el-input v-model="nagiosRuleForm.clientName" placeholder="示例：nagios_readonly"></el-input>
                                    </el-form-item>
                                    <el-form-item label="客户端密码" prop="clientPassword">
                                        <el-input type="password" v-model="nagiosRuleForm.clientPassword" placeholder="示例：d(*dS1"></el-input>
                                    </el-form-item>
                                    <el-form-item label="客户端路径" prop="clientPath">
                                        <el-input v-model="nagiosRuleForm.clientPath" placeholder="示例：/usr/local/nagios"></el-input>
                                    </el-form-item>
                                </div>
                            </div>
                        </el-form>
                    </el-col>
                    <el-col :span="24">
                        <div style="text-align: center;padding: 10vh 0px 32px 0px;">
                            <el-button size="large" type="primary" @click="submitForm('nagiosForm')">保存配置</el-button>
                            <el-button size="large" class="buttonColor" @click="resetForm('nagiosForm')">重置数据</el-button>
                        </div>
                    </el-col>
                </el-row>
            </el-tab-pane>
        </el-tabs>
    </section>
</div>
</template>
<script setup>
    import { WarningFilled } from '@element-plus/icons-vue';
    import { watch,ref,reactive,onMounted,getCurrentInstance} from 'vue';
    import { ElMessage } from "element-plus";
    import { useRouter } from 'vue-router';
            const router = useRouter();
            const { proxy } = getCurrentInstance();
            let activeName = ref(localStorage.getItem("activeName")?localStorage.getItem("activeName"):"first");
            const openForm = ref(null);
            const zabbixForm = ref(null);
            const nagiosForm = ref(null);
            let ruleForm = reactive({
                    connectName:'',
                    userName:'',
                    password:'',
                    port:'',
                    ip:'',
                    platform:'Prometheus'
            });
            let zabbixRuleForm = reactive({
                    containerIp:'',
                    containerPort:'',
                    connectName:'',
                    userName:'',
                    password:'',
                    port:'',
                    ip:'',
                    dataBaseName:'',
                    platform:'Zabbix'
            });
            let nagiosRuleForm = reactive({
                    serverIp:'',
                    serverName:'',
                    serverPassword:'',
                    serverPath:'',
                    clientIp:'',
                    clientName:'',
                    clientPassword:'',
                    clientPath:'',
                    platform:'Nagios'
            });
            const rules = reactive({
                    connectName:[
                        { required: true, message: '请输入实例名称', trigger: 'blur' },
                        { pattern: /^[0-9A-Za-z_]{2,16}$/g, message: '连接名称只支持英文、下划线、数字组合2-16个字符' }],
                    userName:[
                        { required: true, message: '请输入用户', trigger: 'blur' },
                        { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur' }],
                    dataBaseName:[
                        { required: true, message: '请输入数据库库名', trigger: 'blur' },
                        { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur' }],
                    password:[
                        { required: true, message: '请输入登录密码', trigger: 'blur' },
                        { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur' }],
                    ip:[
                        { required: true, message: '请输入主机地址', trigger: 'blur' },
                        { pattern: /^(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])$/, message: '只允许输入IP地址' }],
                    port:[
                        { required: true, message: '请输入主机端口', trigger: 'blur' },
                        { pattern: /^((6[0-4]\d{3}|65[0-4]\d{2}|655[0-2]\d|6553[0-5])|[0-5]?\d{0,4})$/g, message: '只允许输入端口号' }],
                    serverIp:[
                        { required: true, message: '请输入主机地址', trigger: 'blur' },
                        { pattern: /^(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])$/, message: '只允许输入IP地址' }],
                    clientIp:[
                        { required: true, message: '请输入主机地址', trigger: 'blur' },
                        { pattern: /^(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])$/, message: '只允许输入IP地址' }],
                    serverName:[
                        { required: true, message: '请输入用户', trigger: 'blur' },
                        { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur' }],
                    clientName:[
                        { required: true, message: '请输入用户', trigger: 'blur' },
                        { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur' }],
                    serverPassword:[
                        { required: true, message: '请输入登录密码', trigger: 'blur' },
                        { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur' }],
                    clientPassword:[
                        { required: true, message: '请输入登录密码', trigger: 'blur' },
                        { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur' }],
                    serverPath:[
                        { required: true, message: '请输入服务端路径', trigger: 'blur' },
                        { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur' }],
                    clientPath:[
                        { required: true, message: '请输入客户端路径', trigger: 'blur' },
                        { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur' }],
                });
        onMounted(() => {
            getFormData();
        });
        watch(activeName, (newName) => {
            localStorage.setItem("activeName",newName)
        });
        watch(() => router.currentRoute.value.path,() => {
            localStorage.removeItem("activeName")
        })
        // Query the configuration information of zabbix and nagios
        const getFormData = ()=>{ // eslint-disable-line no-unused-vars
            proxy.$http.get('/data/zabbix/config').then(res=>{
                    if(res.data.code == 200){
                        if(res.data.data){
                            zabbixRuleForm.connectName = res.data.data.connectName;
                            zabbixRuleForm.port = res.data.data.port;
                            zabbixRuleForm.ip = res.data.data.ip;
                            zabbixRuleForm.userName = res.data.data.userName;
                            zabbixRuleForm.dataBaseName = res.data.data.dataBaseName;
                        }
                    } else if(res.data.code == 400){
                        return false;
                    }
                }).catch(()=>{
                    ElMessage.warning("请求失败")
                })
                proxy.$http.get('/data/nagios/config').then(res=>{
                    if(res.data.code == 200){
                        if(res.data.data){
                            nagiosRuleForm.serverIp = res.data.data.serverIp;
                            nagiosRuleForm.serverName = res.data.data.serverName;
                            nagiosRuleForm.serverPath = res.data.data.serverPath;
                            nagiosRuleForm.clientIp = res.data.data.clientIp;
                            nagiosRuleForm.clientName = res.data.data.clientName;
                            nagiosRuleForm.clientPath = res.data.data.clientPath;
                        }
                    } else if(res.data.code == 400){
                        return false;
                    }
                }).catch(()=>{
                    ElMessage.warning("请求失败")
                })
        }
        // Get address bar information
        const getPath = ()=> {
            zabbixRuleForm.containerIp = window.location.host.split(':')[0];
            zabbixRuleForm.containerPort = window.location.host.split(':')[1];
        }
        // save configuration
        const submitForm = (formName)=> {
            proxy.$refs[formName].validate((valid) => {
            if (valid) {
                let formData;
                if(activeName.value == 'first'){
                   formData = ruleForm;
                } else if(activeName.value == 'second'){
                   getPath();
                   formData = zabbixRuleForm;
                } else {
                   formData = nagiosRuleForm;
                }
                proxy.$http.post('/data/create/config', JSON.stringify(formData)).then(res=>{
                    if(res.data.code == 200){
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
            proxy.$unFocus();
        }
        const resetForm = (formName)=> {
            proxy.$unFocus();
            proxy.$refs[formName].resetFields();
        }
</script>
<style  scoped>
    .el-form-item {
        margin-bottom: 16px;
    }
</style>
