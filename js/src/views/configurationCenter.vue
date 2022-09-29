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
    <aside id="left">
        <el-row style="padding: 0 24px;">
            <el-col :span="24">
              <el-card shadow="never">
                openGuass
              </el-card>
            </el-col>
          </el-row>
    </aside>
    <section id="right">
        <span class="dataStyle">数据库信息
            <el-popover
            placement="top-start"
            title="配置信息示例"
            width="200"
            trigger="hover">
            <p class="tipck">1.实例名称：openGauss</p>
            <p class="tipck">2.主机地址：jdbc:postgaussgresql://xx.xx.xxx.xxx</p>
            <p class="tipck">3.主机端口：80808</p>
            <p class="tipck">4.只读用户：opengaussabc</p>
            <p class="tipck">5.登录密码：opengaussabc123</p>
            <i class="el-icon-question" slot="reference"></i>
          </el-popover></span>
        <el-form :model="ruleForm" :label-position="'left'" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm "
        style="margin: 0px 50% 0px 0px;">
            <el-form-item label="实例名称" prop="instanceName">
                <el-input v-model="ruleForm.instanceName" placeholder="请输入实例名称"></el-input>
            </el-form-item>
            <el-form-item label="主机地址" prop="hostAddress">
                <el-input v-model="ruleForm.hostAddress" placeholder="请输入主机地址"></el-input>
            </el-form-item>
            <el-form-item label="主机端口" prop="hostPort">
                <el-input v-model="ruleForm.hostPort" placeholder="请输入主机端口"></el-input>
            </el-form-item>
            <el-form-item label="只读用户" prop="user">
                <el-input v-model="ruleForm.user" placeholder="请输入数据库只读用户名"></el-input>
            </el-form-item>
            <el-form-item label="登录密码" prop="password">
                <el-input type="password" v-model="ruleForm.password" placeholder="请输入数据库登录密码"></el-input>
            </el-form-item>
            <el-form-item style="text-align: center;padding-top: 16px;">
                <el-button type="primary" @click="submitForm('ruleForm')">保存配置</el-button>
                <el-button @click="resetForm('ruleForm')">重置数据</el-button>
            </el-form-item>
        </el-form>
    </section>
</div>
</template>
<script>
    export default{
        components:{

        },
        data(){
            return{
                ruleForm:{
                    instanceName:'',
                    user:'',
                    password:'',
                    hostPort:'',
                    hostAddress:''
                },
                radio1:'openGuass',
                rules:{
                    instanceName:[
                        { required: true, message: '请输入实例名称', trigger: 'blur' },
                        { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur' }],
                    user:[
                        { required: true, message: '请输入只读用户', trigger: 'blur' },
                        { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur' }],
                    password:[
                        { required: true, message: '请输入登录密码', trigger: 'blur' },
                        { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur' }],
                    hostAddress:[
                        { required: true, message: '请输入主机地址', trigger: 'blur' },
                        { pattern: /^(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])$/, message: '只允许输入IP地址' }],
                    hostPort:[
                        { required: true, message: '请输入主机端口', trigger: 'blur' },
                        { pattern: /^((6[0-4]\d{3}|65[0-4]\d{2}|655[0-2]\d|6553[0-5])|[0-5]?\d{0,4})$/g, message: '只允许输入端口号' }]
                }
            }
        },
        watch:{
        },
        mounted() {
            this.getFormData();
        },
        methods: {
        // query configuration data
        getFormData(){
            this.$http.get('api/getData/').then(res=>{
                    if(res.data.state == 1){
                        this.ruleForm = res.data;
                    } else if(res.data.state == 0){
                        return false;
                    } else {
                        this.$message.warning("请求失败")
                    }
                }).catch((mes)=>{
                    this.$message.warning("请求失败")
                })
        },
        // save configuration
        submitForm(formName) {
            this.$refs[formName].validate((valid) => {
            if (valid) {
                this.$http.post('/api/addData/', JSON.stringify(this.ruleForm)).then(res=>{
                    if(res.data.state == 1){
                        this.getFormData();
                        this.$message.success("保存成功")
                    } else {
                        this.$message.warning("数据库连接失败")
                    }
                }).catch((mes)=>{
                    this.$message.warning("保存失败")
                })
            } else {
                return false;
            }
            });
        },
        resetForm(formName) {
            this.$refs[formName].resetFields();
            }
        }
    }
</script>
<style>
    #left .el-radio-button__inner{
        min-width: 150px;
    }
    .el-form-item__label {
        font-weight: 600;
        font-size: 16px;
    }
    .el-card__body, .el-main {
        padding: 12px 18px;
        text-align: left;
        color: #409EFF;
        background-color: #F4F4F4;
    }
</style>
<style  scoped>
    .wrap{
        margin:0 auto;
        display: flex;
        height: calc(100vh - 89px);
    }
    #left{
        width: 300px;
        height: 100%;
        padding: 0px;
        border-right: 2px solid #E4E7ED;
        text-align: center;
    }
    #right{
        flex: 1;
        height: 100%;
        padding: 0px 24px;
    }
    .dataStyle{
        font-weight: 600;
        font-size: 20px;
        display: block;
        margin-bottom: 24px;
    }
    .el-row{
        margin-bottom: 20px;
    }
    .el-form-item {
        margin-bottom: 16px;
    }
</style>
