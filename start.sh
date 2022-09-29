#!/bin/sh
set -ex
local_path=$(cd `dirname $0`;pwd)
echo ${local_path}
cd ${local_path}/flaskProject
python exporter.py

