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
    <el-row>
        <el-col :span="16" style="padding: 0 24px 24px 24px;border-right: 2px solid #E4E7ED;">
            <header id="header">
                <span class="dataStyle">生成与发布</span>
                <el-card shadow="never">
                    <el-row style="color:#000000">
                        <el-col :span="1" style="text-align: center;">
                            <i class="el-icon-warning" style="font-size:20px"></i>
                        </el-col>
                        <el-col :span="23">
                            <p class="tipck">生成指标：在“SQL语句”窗口中输入想要生成的SQL指标，选择采集间隔时间，点击“生成指标”，提示成功后，会生成该SQL对应的插件配置，
                                同时会在“指标生成记录”中添加一条记录;</p>
                            <p class="tipck">指标发布：将“指标生成记录”对应最新的一条指标数据推送到监控平台;</p>
                            <p class="tipck">全部发布：将“指标生成记录”所有指标数据推送到监控平台;</p>
                            <p class="tipck">指标生成记录：可以对已生成的指标进行修改与删除，当修改完成后，需要重新点击“生成指标”并发布;</p>
                        </el-col>
                      </el-row>
                  </el-card>
            </header>
            <div id="layout-body">
                <main id="main">
                    <el-form :model="ruleForm" :label-position="'left'" :rules="rules" ref="ruleForm" label-width="120px" class="demo-ruleForm"
                    style="margin: 16px 0px 0px 0px;">
                        <el-row>
                            <el-col :span="24">
                                <el-form-item label="SQL语句" prop="sqlData" >
                                    <el-input type="textarea" :rows="18" v-model="ruleForm.sqlData" v-on:change="changeSql"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row style="">
                            <el-col :span="24">
                                <el-form-item label="采集间隔时间" prop="time">
                                    <el-input style="width:100px" v-model="ruleForm.time" v-on:change="change"></el-input>
                                    <el-select v-model="ruleForm.timeType" style="width:100px;" prop="timeType"  placeholder="">
                                        <el-option
                                          v-for="item in options"
                                          :key="item.value"
                                          :label="item.label"
                                          :value="item.value">
                                        </el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col>
                        </el-row>
                    </el-form>
                </main>
                <footer id="footer" style="margin-top: 16px;">
                    <el-button type="primary" :disabled="disabledFlg" @click="debounceMethods(submitForm,1000,'ruleForm')">生成指标</el-button>
                    <el-button  :disabled="disabledRelease" @click="debounceMethods(release,1000,'ruleForm')">指标发布</el-button>
                    <el-button  :disabled="disabledReleaseAll" @click="debounceMethods(releaseAll,1000,'ruleForm')">全部发布</el-button>
                </footer>
            </div>
        </el-col>
        <el-col :span="8" style="padding: 0 24px 24px 24px;">
            <header id="header">
                <span class="dataStyle">指标生成记录</span>
            </header>
            <div id="layout-body">
                <el-table
                :data="tableData"
                style="width: 100%"
                :row-class-name="tableRowClassName"
                :header-cell-style="{background:'#F4F4F4',color:'#2c3e50'}"
                >
                <el-table-column
                  fixed
                  prop="now_time"
                  label="生成时间"
                  width="150">
                </el-table-column>
                <el-table-column
                  label="SQL详情"
                  prop="sql"
                  show-overflow-tooltip
                  >
                </el-table-column>
                <el-table-column
                  label="操作"
                  width="120">
                  <template slot-scope="scope">
                    <div v-if="updateState != tableData[scope.$index].id">
                        <el-button
                        @click.native.prevent="updateRow(scope.$index, tableData)"
                        type="text"
                        size="small">
                        修改
                      </el-button>
                      <el-button
                      @click.native.prevent="deleteRow(scope.$index, tableData)"
                      type="text"
                      size="small">
                      删除
                    </el-button>
                    </div>
                  <div v-else>
                    <el-button
                    @click.native.prevent="cancelRow(scope.$index, tableData)"
                    type="text"
                    size="small">
                    取消
                  </el-button>
                  </div>
                  </template>
                </el-table-column>
              </el-table>
              <el-pagination
                background
                pager-count="5"
                layout="prev, pager, next"
                :total="total"
                @current-change="handleCurrentChange"
                current-page="current" style="text-align: end;padding: 16px 0px 16px 16px;">
              </el-pagination>
            </div>
        </el-col>
      </el-row>
</div>
</template>
<script>
    export default{
        components:{
        },
        data(){
            return{
                updateIndex:'',
                updateState:'',
                current:1,
                total:0,
                ruleForm:{
                    numIndex:'',
                    isFalse:false,
                    sqlData:'',
                    dataType:1,
                    time:5,
                    timeType:'second',
                },
                disabledFlg:true,
                disabledRelease:true,
                disabledReleaseAll:false,
                options: [{
                    value: 'year',
                    label: '年'
                    }, {
                    value: 'month',
                    label: '月'
                    }, {
                    value: 'week',
                    label: '周'
                    }, {
                    value: 'day',
                    label: '日'
                    }, {
                    value: 'hour',
                    label: '时'
                    }, {
                    value: 'minute',
                    label: '分'
                    }, {
                    value: 'second',
                    label: '秒'
                }],
                rules:{
                    sqlData:[
                        {required:true,message:'请输入SQL语句',trigger:'blur'}],
                    time:[
                        {required:true,message:'请输入采集间隔时间',trigger:'blur'},
                        {pattern: /^[0-9]{1,3}$/,message: "只能输入1~3位数字",trigger: "blur"}],
                },
                debounceTimer:null,
                tableData:[]
            }
        },
        mounted(){
            this.checkSql();
        },
        methods: {
        indexAdd(index) {
            return index + 1 + (this.current - 1) * 10
        },
        handleCurrentChange(val){
            this.current = val;
            this.checkSql();
        },
        tableRowClassName({row, rowIndex}) {
            if (rowIndex === this.updateIndex) {
            return 'success-row';
            }
            return '';
        },
        // Query all SQL
        checkSql() {
        this.$http.get('/api/checkSql/', { params:{"current":this.current}}).then(res=>{
            if(res.data.state == 1){
                this.tableData = res.data.data;
                this.total = res.data.sql_num;
                if(res.data.sql_num != 0){
                    this.disabledRelease = false;
                }
            } else {
                this.$message.warning("查询失败");
            }
            }).catch((mes)=>{
                this.$message.warning("查询失败");
        })
        },
        // Update a single sql
        updateRow(Index, data){
            this.updateIndex = Index;
            this.ruleForm.sqlData = data[Index].sql;
            this.updateState = data[Index].id;
            this.ruleForm.numIndex = this.indexAdd(Index);
        },
        // Delete a single sql
        deleteRow(Index, data){
            let num = this.indexAdd(Index);
            this.$confirm("确认删除该SQL, 是否继续?", "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                })
                .then(() => {
                    this.$http.post('/api/deleteSql/', JSON.stringify({deleteNum:num})).then(res=>{
                        if(res.data.state == 1){
                            this.$message.success("删除成功");
                            this.checkSql();
                        } else if(res.data.state == 2){
                            this.$message.warning("删除失败");
                        }
                    }).catch((mes)=>{
                        this.$message.warning("删除失败");
                    })
                })
                .catch(() => {
                    this.$message({
                        type: "info",
                        message: "已取消删除"
                    });
                });
        },
        cancelRow(Index, data){
            this.updateState = '';
            this.ruleForm.numIndex = '';
            this.ruleForm.sqlData = '';
        },
        // Monitoring interval time input box
        change(value) {
            if (value == 0) {
              this.disabledFlg = true;
            } else {
              this.disabledFlg = false;
            }
        },
        // Monitor sql input box
        changeSql(value) {
            if(this.ruleForm.time != 0){
                this.disabledFlg = false;
            } else{
                this.$message.warning("采集间隔时间不能为0");
            }
        },
        // button throttle
        debounceMethods(func,time,...args){
            let context = this;
            if (this.debounceTimer){
                return
            };
            let callNow = !this.debounceTimer;
            this.debounceTimer = setTimeout(() => {
                this.debounceTimer = null;
            },time)
            if(callNow){
                func.apply(context,args)
            }
        },
        // publish sql
        release() {
            this.$http.post('/api/release/', {}).then(res=>{
                    if(res.data.state == 1){
                        this.$message.success("发布成功");
                        this.disabledRelease = true;
                        setTimeout(() => {
                            this.disabledRelease = false;
                        }, 20000);
                    } else if(res.data.state == 2){
                        this.$message.success("发布失败");
                    } else if(res.data.state == 3){
                        this.$message.warning("请先配置数据库信息");
                    } 
                }).catch((mes)=>{
                    this.$message.warning("发布失败");
            })
        },
        // Publish all sql
        releaseAll() {
            this.$http.post('/api/releaseAll/', {}).then(res=>{
                    if(res.data.state == 1){
                        this.$message.success("全部发布成功");
                        this.disabledReleaseAll = true;
                        setTimeout(() => {
                            this.disabledReleaseAll = false;
                        }, 8000);
                    } else if(res.data.state == 2){
                        this.$message.success("全部发布失败");
                    } else if(res.data.state == 3){
                        this.$message.warning("请先配置数据库信息");
                    } 
                }).catch((mes)=>{
                    this.$message.warning("全部发布失败");
            })
        },
        // makefile
        submitForm(formName) {
            this.$refs[formName].validate((valid) => {
            if (valid) {
                this.$http.post('/api/getnewoption/', JSON.stringify(this.ruleForm)).then(res=>{
                    if(res.data.state == 1){
                        this.$message.success("生成成功");
                        this.disabledFlg = true;
                        this.disabledRelease = false;
                        this.disabledReleaseAll = false;
                        this.updateState = '';
                        this.ruleForm.numIndex = '';
                        this.checkSql();
                    } else if(res.data.state == 2){
                        if(res.data.err_message){
                            this.$message({
                                showClose: true,
                                duration:0,
                                message: res.data.err_message,
                                type: 'warning'
                            })
                        }
                    } else if(res.data.state == 3){
                        this.$message.warning("只能执行查询语句");
                    } else if(res.data.state == 4){
                        this.$message.warning("只能执行单条SQL语句");
                    } else if(res.data.state == 5){
                        this.$message.warning("SQL语句重复");
                    } else if(res.data.state == 6){
                        this.$confirm("该SQL可能无法生成指标，是否强制执行?", "提示", {
                            confirmButtonText: "确定",
                            cancelButtonText: "取消",
                            type: "warning"
                        })
                        .then(() => {
                            this.ruleForm.isFalse = true;
                            this.$http.post('/api/getnewoption/', JSON.stringify(this.ruleForm,)).then(res=>{
                                if(res.data.state == 1){
                                    this.$message.success("生成成功");
                                    this.ruleForm.isFalse = false;
                                    this.checkSql();
                                }
                            }).catch((mes)=>{
                                this.ruleForm.isFalse = false;
                                this.$message.warning("生成失败");
                            })
                        })
                        .catch(() => {
                            this.$message({
                                type: "info",
                                message: "已取消生成"
                            });
                        });
                    } else if(res.data.state == 7){
                        this.$message.warning("请先配置数据库信息");
                    } 
                }).catch((mes)=>{
                    this.$message.warning("生成失败");
                })
            } else {
                return false;
            }
            });
        },
        }
    }
</script>
<style>
    .el-card__body, .el-main {
        padding: 12px 18px 12px 8px;
        text-align: left;
        background-color: #F4F4F4;
    }
    .el-form-item {
        margin-bottom: 16px;
    }
    .el-table .success-row {
        background: #f0f9eb;
    }
    .el-table{
        font-size: 16px;
    }
</style>
<style  scoped>
    body,html{
        margin:0;
        padding:0;
    }
    #layout{
        display: flex;
        flex-direction: column;
        width: 100vw;
        height: 100%;
        padding: 20px;
    }
    #header{
        height: auto;
        flex-shrink: 0;
    }
    #layout-body{
        flex-grow: 2;
        overflow-y: hidden;
        display: flex;
        flex-direction: column;
    }
    #footer{
        flex-shrink: 0;
        text-align: center;
    }
    .dataStyle{
        font-weight: 600;
        font-size: 20px;
        display: block;
        margin-bottom: 16px;
    }
    #main{
        flex-grow: 2;
    }
    .tipck{
        margin:0px;
    }
</style>
