
<h2 align="center" style="margin: 30px 0 30px; font-weight: bold;">openGauss数据库监控插件生成工具 v1.0.0</h2>
<h4 align="center">基于 Vue/Element UI 和 Python 前后端不分离的快速开发框架</h4>

### openGauss数据库监控插件工具简介
不同的客户在使用openGauss数据库过程中，可能会使用不同的监控平台来实现对数据库的监控，为帮助行业客户更放心、更快速使openGauss数据库，因此开发一款通用的监控插件生成工具。
* 可以根据客户自定义的监控SQL来生成Prometheus、Zabbix以及Nagios平台的监控插件。
* 内置50个通用的监控插件指标，可直接部署在已有平台上来实现对openGauss的全链路监控，这50个通用指标覆盖了openGauss的连接数、动态内存、共享内存、TPS、QPS、长连接等常用指标监控
* 为减少不同平台的兼容性，工具所有插件均使用Python语言进行适配。支持在Linux、Windows上运行
* 采用前后端不分离的模式,前端(基于 Vue/Element UI)，通过前端页面实现数据库配置、监控平台配置、SQL语句的校验、指标文件生成、插件发布等功能。

### openGauss数据库监控插件工具第一期对Prometheus特性支持如下
* 配置openGauss数据库连接信息
* 支持sql指标生成
* 插件生成,支持自定义采集时间,支持单条sql指标发布,多条sql指标全部发布。
* sql预览,支持查看历史sql详情,修改和删除历史sql
* 发送上报Prometheus平台
* 在Prometheus平台可以查看监控指标。

### openGauss数据库监控插件工具的使用限制
* 用户需要具有数据库的操作权限,权限配置为只读权限,配置正确的数据源信息,数据库支持openGauss。
* sql语句仅限于DQL语句,只支持select查询语句。
* 在Prometheus服务端需要配置exporter的信息。
* 系统支持windows、linux。

<img src="https://gitee.com/jun-peng-liu/gauss/raw/master/img/peizhi.png"/>

### 系统模块
~~~
flaskProject(编译后)    
├── static                         // 前端静态资源 
        └── css                   // css文件
│       └── font                 // 字体文件
│       └── js                  // javaScript脚本
├── templates                  // 访问页面 index.html[8000]
├── prometheus_client         // Prometheus服务依赖

├── config                           // 配置文件,生成yml文件
        └── add_config.yml                    
        └── check_config.yml                    
        └── db_config.yml                     
        └── re_config.yml                       
js                                // 前端资源  
python 
    └── exporter.py             // 运行脚本              
    └── registry.py 
thirdFile                     // Prometheus相关依赖
deploy.bat                  // windows编译脚本
tart.bat                   // windows启动脚本
deploy.sh                 // linux编译脚本
start.sh                 // linux启动脚本

~~~

### 架构图
<img src="https://gitee.com/jun-peng-liu/gauss/raw/master/img/2.png"/>

### 工具的安装windows
#### 1.安装python
* 在python官网下载python3.0以上版本https://www.python.org/
* 双击运行python-3.9.1-amd64.exe
* 配置环境变量,点级高级系统设置,在系统变量下,点击path,添加python所在的目录如:D:\python\Scripts\
<table>
    <tr>
        <td><img src="https://gitee.com/jun-peng-liu/gauss/raw/master/img/python01.png"/></td>
        <td><img src="https://gitee.com/jun-peng-liu/gauss/raw/master/img/python02.png"/></td>
    </tr>
    <tr>
        <td><img src="https://gitee.com/jun-peng-liu/gauss/raw/master/img/python03.png"/></td>
        <td><img src="https://gitee.com/jun-peng-liu/gauss/raw/master/img/python04.png"/></td>
    </tr>
    <tr>
        <td><img src="https://gitee.com/jun-peng-liu/gauss/raw/master/img/python05.png"/></td>
        <td><img src="https://gitee.com/jun-peng-liu/gauss/raw/master/img/python06.png"/></td>
    </tr>
</table>

#### 2.安装nodejs V16.16.0
* 官网下载windows版本node安装包:Node.js。下载地址：https://nodejs.org/en/
* 下载node-v16.16.0-x64.msi，安装过程中除更换安装路径，其他均点下一步即可。
* 打开cmd命令执行node -v,结果为v16.16.0。
* 打开cmd命令执行npm -v,结果为8.11.0。
* 更换镜像:打开cmd命令:npm config set registry https://registry.npm.taobao.org
* 执行npm config get registry,结果为https://registry.npm.taobao.org/,更换镜像成功

#### 3.安装jdk
* Oracle官方下载:https://www.oracle.com/java/technologies/downloads/
* 双击打开下载好的jdk,点击下一步,安装完成后进行java环境变量的配置
* 右键我的电脑,点击属性,点击高级系统设置,点击环境变量。
* 在系统变量中新建JAVA_HONE,变量值为java的安装路径
* 在path写入bin所在的路径,如图所示;
* 打开cmd命令执行java -version,结果为java version "java版本号",安装成功。
<table>
    <tr>
        <td><img src="https://gitee.com/jun-peng-liu/gauss/raw/master/img/java01.jpg"/></td>
        <td><img src="https://gitee.com/jun-peng-liu/gauss/raw/master/img/java02.jpg"/></td>
    </tr>
    <tr>
        <td><img src="https://gitee.com/jun-peng-liu/gauss/raw/master/img/java03.jpg"/></td>
        <td><img src="https://gitee.com/jun-peng-liu/gauss/raw/master/img/java04.jpg"/></td>
    </tr>
    <tr>
        <td><img src="https://gitee.com/jun-peng-liu/gauss/raw/master/img/java05.jpg"/></td>
        <td><img src="https://gitee.com/jun-peng-liu/gauss/raw/master/img/java06.jpg"/></td>
    </tr>
</table>

### 工具的安装linux-Centos7
#### 1.安装python
* python下载地址：https://www.python.org/downloads/source/
<table>
    <tr>
        <td><img src="https://gitee.com/jun-peng-liu/gauss/raw/master/img/linux_python01.jpg"/></td>
        <td><img src="https://gitee.com/jun-peng-liu/gauss/raw/master/img/linux_python02.jpg"/></td>
    </tr>
</table>

#####1.1 linux执行
* yum install -y gcc zlib-devel bzip2-devel openssl-devel ncurses-devel sqlite-devel readline-devel tk-devel gdbm-devel xz-devel libffi-devel 
* [root@192 ~]# cd /opt/
* [root@192 opt]# wget https://www.python.org/ftp/python/3.9.1/Python-3.9.1.tgz
* [root@192 opt]# tar xf Python-3.9.1.tgz
* [root@192 opt]# cd Python-3.9.1/
* [root@192 Python-3.10.0]# ./configure --prefix=/usr/local/python --enable-optimizations
* [root@192 Python-3.10.0]# make && make install
* [root@192 Python-3.10.0]# ln -s /usr/local/bin/python3.9 /usr/bin/pyhton
* [root@192 Python-3.10.0]# ln -s /usr/local/bin/pip3.9 /usr/bin/pip
#####1.2 linux配置python全局环境
* [root@192 ~]# echo 'export PATH=$PATH:/usr/local/python/bin/' >> /etc/profile
* [root@192 ~]# source /etc/profile
* [root@192 ~]# python -V

#### 2.安装node.js
#####2.1下载node-v 16.16.0
* mkdir -p /usr/local/soft/package
* cd /usr/local/soft/package
* wget https://repo.huaweicloud.com/nodejs/latest-v16.x/node-v16.16.0-linux-x64.tar.xz
* tar -xvf node-v16.16.0-linux-x64.tar.xz
* mv node-v16.16.0-linux-x64 ../node
#####2.2创建软连接
* ln -s /usr/local/soft/node/bin/npm /usr/local/bin/npm
* ln -s /usr/local/soft/node/bin/node /usr/local/bin/node

#### 3.安装jdk
* 进入oracle官网下载linux版本jdk8,地址:https://www.oracle.com/java/technologies/downloads/#java8
* cd /usr/local (注:目录自定义);
* mkdir jdk 
* 上传jdk到目录/usr/local/jdk
* 执行tar -zxcv jdk-8u341-linux-x64.tar.gz
* 配置成功后,进行环境变量的配置
* vim /etc/profile
* 添加如下命令
* export JAVA_HOME=/usr/local/jdk/jdk1.8.0_341
* export CLASSPATH=$:CLASSPATH:$JAVA_HOME/lib/
* export PATH=$PATH:$JAVA_HOME/bin
* 刷新环境变量:执行source /etc/profile
* 查看java版本:执行java -version
<table>
    <tr>
        <td><img src="https://gitee.com/jun-peng-liu/gauss/raw/master/img/linuxjdk1.png"/></td>
        <td><img src="https://gitee.com/jun-peng-liu/gauss/raw/master/img/linuxjdk2.png"/></td>
    </tr>
</table>

### openGauss数据库监控插件工具使用说明-windows
* 环境的准备,windows系统安装python3.9.1,安装jdk1.8,安装nodejs V16.16.0(参考工具的安装windows)。
* 下载openGauss3.0.0驱动jar包,下载地址:https://www.opengauss.org/zh/download.html。
* 安装python环境之后,导入python第三方件
* 打开cmd命令分别执行以下命令
* pip  install jaydebeapi
* pip  install flask
* pip  install pyyaml
* pip  install flask
* pip  install datetime
* 将驱动tar.gz包移动至项目所在的目录
* 下载代码,cmd进入到文件目录,执行以下命令
* deploy.bat(注:编译)
* start.bat(注:启动)
* 浏览器访问127.0.0.1:8000
<table>
    <tr>
        <td><img src="https://gitee.com/jun-peng-liu/gauss/raw/master/img/jar01.jpg"/></td>
        <td><img src="https://gitee.com/jun-peng-liu/gauss/raw/master/img/jar02.jpg"/></td>
    </tr>
</table>

### openGauss数据库监控插件工具使用说明-linux
* 环境的准备,linux安装python3.9.1,安装jdk1.8,安装nodejs V16.16.0(参考工具的安装linux)。
* 下载openGauss3.0.0驱动jar包,下载地址:https://www.opengauss.org/zh/download.html。
* 安装python环境之后,导入python第三方件
* 进入项目所在的路径分别执行以下命令
* pip  install jaydebeapi
* pip  install flask
* pip  install pyyaml
* pip  install flask
* pip  install datetime
* 下载代码
* 将驱动tar.gz包移动至项目所在的目录
* cd进入到文件目录,执行以下命令
* sh deploy.sh(注:编译)
* sh start.sh(注:启动)
* 浏览器访问:容器Ip:8000

### 演示图

<table>
    <tr>
        <td><img src="https://gitee.com/jun-peng-liu/gauss/raw/master/img/yanshi03.png"/></td>
        <td><img src="https://gitee.com/jun-peng-liu/gauss/raw/master/img/yanshi2.jpg"/></td>
    </tr>
</table>

