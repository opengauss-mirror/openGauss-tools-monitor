# -*-coding:utf-8 -*-
"""
 * Copyright (c) 2020 Huawei Technologies Co.,Ltd.
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
 * See the Mulan PSL v2 for more details.
"""

from prometheus_client import Gauge, start_http_server
import time
import datetime
import jaydebeapi as jaydebeapi
from flask import Flask, render_template, request
import json
import yaml
import base64
import threading
import inspect
import ctypes
import re
import os
from sys import platform

app = Flask(__name__)

t_thread = []
g_env = []


def opengauss_Connection(db_params):
    url = "jdbc:postgresql://" + db_params["hostAddress"] + ":" + db_params["hostPort"] + "/postgres"
    user = db_params["user"]
    password = db_params["password"]
    drive = db_params["drive"]
    jarFile = db_params["jarFile"]
    conn = jaydebeapi.connect(drive, url, [user, base64.b64decode(password).decode(encoding='utf-8')], jarFile)
    curs = conn.cursor()
    return curs


def sql_read(url):
    db_file = open(url, 'r', encoding="utf-8")
    db_cfg = db_file.read()
    db_params = yaml.load(db_cfg, Loader=yaml.SafeLoader)
    return db_params


def sql_insert(db_params, sql_select, url):
    db_params.append(sql_select)
    with open(url, "w", encoding="utf-8") as f:
        yaml.dump(db_params, f)


def sec_opengauss_Connection(db_params):
    # This function is used to validate database information
    url = db_params["url"]
    user = db_params["user"]
    password = db_params["password"]
    drive = db_params["drive"]
    jarFile = db_params["jarFile"]
    conn = jaydebeapi.connect(drive, url, [user, password], jarFile)
    conn.close()


def select_Sql(curs, sqlStr):
    curs.execute(sqlStr)
    result1 = curs.fetchall()
    result2 = curs.description

    key = []
    for i in result2:
        key.append(i[0])
    result = []
    for i in result1:
        result_dict = {}
        for j in range(len(key)):
            result_dict[key[j]] = i[j]
        result.append(result_dict)
    return result


def get_metrics_info(curs, sqlStr, lab_dict, time_sleep):
    global g_env
    for before_guage in g_env:
        before_guage.clear()
    while True:
        label_list = []
        gauge_list = []
        sql_value = select_Sql(curs, sqlStr)
        for i in sql_value[0]:
            if str(type(sql_value[0][i])) == "<java class 'java.lang.Long'>" or str(
                    type(sql_value[0][i])) == "<java class 'java.lang.Double'>" or str(
                type(sql_value[0][i])) == "<java class 'java.lang.Byte'>" or str(
                type(sql_value[0][i])) == "<java class 'java.lang.Short'>" or str(
                type(sql_value[0][i])) == "<java class 'java.lang.Integer'>" or str(
                type(sql_value[0][i])) == "<java class 'java.lang.Float'>" or str(
                type(sql_value[0][i])) == "<java class 'JDouble'>" or sql_value[0][i].isdecimal() == True:
                gauge_list.append(i)
            else:
                label_list.append(i)
        if sql_value:
            for v in gauge_list:
                if lab_dict.__contains__(v):
                    metrics_data = lab_dict[v]
                else:
                    try:
                        if label_list:
                            metrics_data = Gauge(v, v, label_list)
                        else:
                            metrics_data = Gauge(v, v, ["instance"])
                        lab_dict[v] = metrics_data
                    except:
                        break
                g_env.append(metrics_data)
                for w in sql_value:
                    label_str_list = []
                    if label_list:
                        for label in label_list:
                            label_str_list.append(w[label])
                        label_str_tuple = tuple(label_str_list)
                    else:
                        label_str_tuple = ("node",)
                    metrics_data.labels(*label_str_tuple).set(float(w[v]))
            time.sleep(time_sleep)
            print("数据采集成功")


def guage_check(curs, sqlStr):
    check_list = []
    sql_value = select_Sql(curs, sqlStr)
    for i in sql_value[0]:
        if str(type(sql_value[0][i])) == "<java class 'java.lang.Long'>" or str(
                type(sql_value[0][i])) == "<java class 'java.lang.Double'>" or str(
            type(sql_value[0][i])) == "<java class 'java.lang.Byte'>" or str(
            type(sql_value[0][i])) == "<java class 'java.lang.Short'>" or str(
            type(sql_value[0][i])) == "<java class 'java.lang.Integer'>" or str(
            type(sql_value[0][i])) == "<java class 'java.lang.Float'>" or str(
            type(sql_value[0][i])) == "<java class 'JDouble'>":
            check_list.append(sql_value[0][i])
    if check_list:
        pass
    else:
        a = 1 / 0

    for j in check_list:
        if j == None:
            a = 1 / 0


def check_repetition(curs, sqlDate, is_False):
    # Open method reads db_config file
    check_result = {}
    # 验证sql语句是否重复
    check_config = sql_read('config/check_config.yml')
    check_sqlDate = sqlDate.replace(' ', '').replace('\n', '')
    if check_config:
        if check_sqlDate in check_config:
            return json.dumps({"state": "5"})
    # Write the SQL statement and interval information to the configuration file re_config.yml
    str_sql_strip = sqlDate.strip()
    str_sql_type = str_sql_strip[0:6].lower()
    str_sql_with = str_sql_strip[0:4].lower()
    into_sql = re.search(r'\binto\b', sqlDate.lower())
    update_sql = re.search(r'\bupdate\b', sqlDate.lower())
    delete_sql = re.search(r'\bdelete\b', sqlDate.lower())
    insert_sql = re.search(r'\binsert\b', sqlDate.lower())
    truncate_sql = re.search(r'\btruncate\b', sqlDate.lower())
    alter_sql = re.search(r'\balter\b', sqlDate.lower())
    drop_sql = re.search(r'\bdrop\b', sqlDate.lower())
    single_sql = str_sql_strip.endswith(";")
    if str_sql_type == 'select' or str_sql_with == 'with':
        pass
    else:
        return json.dumps({"state": "3"})
    # Validate SQL statement
    if single_sql and sqlDate.count(";") == 1:
        if not into_sql and not update_sql and not delete_sql and not insert_sql and not truncate_sql and not alter_sql and not drop_sql:
            pass
        else:
            return json.dumps({"state": "3"})
    else:
        return json.dumps({"state": "4"})
    try:
        # Connect to database

        select_Sql(curs, "EXPLAIN " + sqlDate)

        if is_False != True:
            try:
                guage_check(curs, sqlDate)
            except:
                return json.dumps({"state": "6"})
        curs.close()
    except Exception as err:
        return json.dumps({"state": "2", "err_message": str(err)})
    check_result["check_config"] = check_config
    check_result["check_sqlDate"] = check_sqlDate
    return check_result


def time_disposal(interval_time, interval_time_type):
    # Processing interval information, in seconds
    if interval_time_type == "year":
        sleep_time = interval_time * 60 * 60 * 24 * 365
    elif interval_time_type == "mouth":
        sleep_time = interval_time * 60 * 60 * 24 * 30
    elif interval_time_type == "week":
        sleep_time = interval_time * 60 * 60 * 24 * 7
    elif interval_time_type == "day":
        sleep_time = interval_time * 60 * 60 * 24
    elif interval_time_type == "hour":
        sleep_time = interval_time * 60 * 60
    elif interval_time_type == "minute":
        sleep_time = interval_time * 60
    else:
        sleep_time = interval_time
    return sleep_time


@app.route('/')
def hello_world():  # put application's code here
    return render_template("index.html")


@app.route('/index')
def hello_index():  # put application's code here
    return render_template("index.html")


@app.route('/api/getData/', methods=["GET"])
def api_getdata():
    if request.method == "GET":
        with open("config/db_config.yml", "r", encoding="utf-8") as f:
            res = yaml.safe_load(f)
            if res:
                res['password'] = base64.b64decode(res['password']).decode(encoding='utf-8')
                res["state"] = 1
                return json.dumps(res)
            else:
                return json.dumps({"state": "0"})


@app.route('/api/addData/', methods=["GET", "POST"])
def add_database():
    if request.method == "POST":
        try:
            # Get the incoming database information from the front end
            instanceName = json.loads(request.get_data(as_text=True))["instanceName"]
            hostAddress = json.loads(request.get_data(as_text=True))["hostAddress"]
            hostPort = json.loads(request.get_data(as_text=True))["hostPort"]
            user = json.loads(request.get_data(as_text=True))["user"]
            password = json.loads(request.get_data(as_text=True))["password"]
            # drive = json.loads(list(request.form)[0]).get("drive")
            drive = "org.postgresql.Driver"
            if ("linux" == platform) or ("linux2" == platform):
                jarFile = str(
                    os.path.abspath(os.path.dirname(os.path.dirname(__file__)))) + "/flaskProect/postgresql.jar"
            elif ("win32" == platform):
                jarFile = str(
                    os.path.abspath(os.path.dirname(os.path.dirname(__file__)))) + "\\flaskProject\\postgresql.jar"
            # Verify whether the database information is correct
            check_params = {}
            check_params["url"] = "jdbc:postgresql://" + hostAddress + ":" + hostPort + "/postgres"
            check_params["user"] = user
            check_params["password"] = password
            check_params["drive"] = drive
            check_params["jarFile"] = jarFile
            try:
                sec_opengauss_Connection(check_params)
            except:
                return json.dumps({"state": "2"})
            # Store the correct database information into the configuration file DB_ config.yml
            res = {'instanceName': instanceName, "hostAddress": hostAddress, "hostPort": hostPort, 'user': user,
                   'password': base64.b64encode(password.encode(encoding='utf-8')),
                   'drive': drive, 'jarFile': jarFile}
            with open("config/db_config.yml", "w", encoding="utf-8") as f:
                yaml.dump(res, f)
            # The database information is correct, and the returned status code is 1
            return json.dumps({"state": "1"})
        except:
            # Error in database information, return status code 2
            return json.dumps({"state": "2"})


@app.route('/api/getnewoption/', methods=["GET", "POST"])
def generate_data():
    if request.method == "POST":
        # Get SQL statement and interval information from the front end
        # result = response.read()
        # result.decode("utf-8")
        # numIndex = json.loads(result)
        numIndex = json.loads(request.get_data(as_text=True))["numIndex"]
        sqlDate = json.loads(request.get_data(as_text=True))["sqlData"].strip()
        sql_time = json.loads(request.get_data(as_text=True))["time"]
        time_type = json.loads(request.get_data(as_text=True))["timeType"]
        now_time = datetime.datetime.now().strftime("%Y-%m-%d %H:%M")
        is_False = json.loads(request.get_data(as_text=True))["isFalse"]
        # re_ Config.yml stores single SQL
        db_file = open('config/db_config.yml', 'r', encoding="utf-8")
        db_cfg = db_file.read()
        db_params = yaml.load(db_cfg, Loader=yaml.SafeLoader)
        if db_params:
            curs = opengauss_Connection(db_params)
        else:
            return json.dumps({"state": "7"})
        if sqlDate.endswith(";"):
            sqlDate = sqlDate
        else:
            sqlDate = sqlDate + ";"
        check_result = check_repetition(curs, sqlDate, is_False)

        if isinstance(check_result, dict) == False:
            return check_result
        sql_select = {'id': datetime.datetime.now().strftime('%Y%m%d%H%M%S'), 'sql': sqlDate.strip(), 'time': sql_time,
                      'type': time_type, "now_time": now_time}
        # Empty the re_config.yml file before validation
        f = open('config/re_config.yml', "r+")
        f.truncate()
        with open("config/re_config.yml", "w", encoding="utf-8") as f:
            yaml.dump(sql_select, f)
        # add_ Config.yml stores multiple SQL
        add_config = sql_read('config/add_config.yml')
        if numIndex:
            numIndex = int(numIndex)
            add_config[-numIndex] = sql_select
            with open("config/add_config.yml", "w", encoding="utf-8") as f:
                yaml.dump(add_config, f)
            check_result["check_config"][-numIndex] = sql_select
            with open("config/check_config.yml", "w", encoding="utf-8") as f:
                yaml.dump(check_result["check_config"], f)
        else:
            if add_config:
                sql_insert(add_config, sql_select, "config/add_config.yml")
            else:
                add_config = []
                sql_insert(add_config, sql_select, "config/add_config.yml")
            # check_config = sql_read('config/check_config.yml')
            # check_sqlDate = sqlDate.replace(' ', '').replace('\n', '')
            check_config = check_result["check_config"]
            check_sqlDate = check_result["check_sqlDate"]
            if check_config:
                sql_insert(check_config, check_sqlDate, "config/check_config.yml")
            else:
                check_config = []
                sql_insert(check_config, check_sqlDate, "config/check_config.yml")
    return json.dumps({"state": "1"})


@app.route('/api/release/', methods=["GET", "POST"])
def publish_data():
    if request.method == "POST":
        # Start exporter
        # Open method reads DB_ Config file
        db_file = open('config/db_config.yml', 'r', encoding="utf-8")
        db_cfg = db_file.read()
        db_params = yaml.load(db_cfg, Loader=yaml.SafeLoader)
        # Open method reads add_config.yml file
        file = open('config/re_config.yml', 'r', encoding="utf-8")
        cfg = file.read()
        sql_Str = yaml.load(cfg, Loader=yaml.SafeLoader)
        sqlStr = sql_Str["sql"]
        # The time interval information is processed into data in seconds
        interval_time = float(sql_Str["time"])
        interval_time_type = str(sql_Str["type"])
        time_sleep = time_disposal(interval_time, interval_time_type)
        # Exposed port
        try:
            start_http_server(9000)
        except:
            pass
        global t_thread
        for item in t_thread:
            stop_thread(item[0])
            item[1].close()
            item[0].join()
        # Connect to database
        if db_params:
            cursi = opengauss_Connection(db_params)
        else:
            return json.dumps({"state": "3"})
        # select_Sql(curs, "EXPLAIN " + sqlStr)
        print("数据库连接成功1")
        # Return Prometheus data
        lab_dict = {}
        t = threading.Thread(target=get_metrics_info, args=(cursi, sqlStr, lab_dict, time_sleep))
        t_thread.append((t, cursi))
        t.start()

        return json.dumps({"state": "1"})
    return json.dumps({"state": "2"})


@app.route('/api/releaseAll/', methods=["GET", "POST"])
def publish_all_data():
    if request.method == "POST":
        try:
            # Exposed port
            start_http_server(9000)
        except:
            pass
        try:
            # Start exporter
            # Open method reads DB_ Config file
            db_file = open('config/db_config.yml', 'r', encoding="utf-8")
            db_cfg = db_file.read()
            db_params = yaml.load(db_cfg, Loader=yaml.SafeLoader)
            # The open method reads add_ Config.yml file
            file = open('config/add_config.yml', 'r', encoding="utf-8")
            cfg = file.read()
            sql_Str = yaml.load(cfg, Loader=yaml.SafeLoader)
            # Connect to database
            if db_params:
                pass
            else:
                return json.dumps({"state": "3"})
            print("数据库连接成功")
            global t_thread
            for item in t_thread:
                stop_thread(item[0])
                item[1].close()
                item[0].join()
            for j in sql_Str:
                curs = opengauss_Connection(db_params)
                sqlStr = j["sql"]
                # The time interval information is processed into data in seconds
                interval_time = float(j["time"])
                interval_time_type = str(j["type"])
                time_sleep = time_disposal(interval_time, interval_time_type)
                # Return Prometheus data
                lab_dict = {}

                t = threading.Thread(target=get_metrics_info, args=(curs, sqlStr, lab_dict, time_sleep))
                t_thread.append((t, curs))
                t.start()
            return json.dumps({"state": "1"})
        except:
            return json.dumps({"state": "2"})


@app.route('/api/checkSql/', methods=["GET", "POST"])
def check_sql():
    if request.method == "GET":
        try:
            add_config = sql_read('config/add_config.yml')
            if add_config:
                sql_num = len(add_config)
                current = request.args.get("current")
                current = int(current)
                add_config_page = add_config[-current * 10: -(current - 1) * 10] if current > 1 else add_config[-10:]
                add_config_page.reverse()
                return json.dumps({"state": "1", "data": add_config_page, "sql_num": sql_num})
            else:
                return json.dumps({"state": "1", "data": []})
        except:
            return json.dumps({"state": "2"})


@app.route('/api/deleteSql/', methods=["GET", "POST"])
def delete_sql():
    if request.method == "POST":
        try:
            delete_num = json.loads(request.get_data(as_text=True))["deleteNum"]
            add_config = sql_read('config/add_config.yml')
            add_config.pop(-int(delete_num))
            with open("config/add_config.yml", "w", encoding="utf-8") as f:
                yaml.dump(add_config, f)
            check_sql = sql_read('config/check_config.yml')
            check_sql.pop(-int(delete_num))
            with open("config/check_config.yml", "w", encoding="utf-8") as f:
                yaml.dump(check_sql, f)
            if len(add_config) == 0:
                open('config/re_config.yml', "r+").truncate()
            if add_config and f != add_config[-1]:
                with open("config/re_config.yml", "w", encoding="utf-8") as f:
                    yaml.dump(add_config[-1], f)
            return json.dumps({"state": "1"})
        except Exception as e:
            return json.dumps({"state": "2"})


@app.route('/api/deleteAllSql/', methods=["GET", "POST"])
def delete_all_sql():
    if request.method == "POST":
        try:
            open('config/add_config.yml', "r+").truncate()

            open('config/check_config.yml', "r+").truncate()

            open('config/check_config.yml', "r+").truncate()

            return json.dumps({"state": "1"})
        except:
            return json.dumps({"state": "2"})


def _async_raise(tid, exctype):
    """raises the exception, performs cleanup if needed"""

    tid = ctypes.c_long(tid)

    if not inspect.isclass(exctype):
        exctype = type(exctype)

    res = ctypes.pythonapi.PyThreadState_SetAsyncExc(tid, ctypes.py_object(exctype))

    if res == 0:

        # raise ValueError("invalid thread id")

        pass
    elif res != 1:

        # """if it returns a number greater than one, you're in trouble,

        # and you should call it again with exc=NULL to revert the effect"""

        ctypes.pythonapi.PyThreadState_SetAsyncExc(tid, None)

        raise SystemError("PyThreadState_SetAsyncExc failed")


def stop_thread(thread):
    _async_raise(thread.ident, SystemExit)


if __name__ == '__main__':
    # app.run(port=8000, debug=True)
    app.run(host="0.0.0.0", port=8000, debug=True)
