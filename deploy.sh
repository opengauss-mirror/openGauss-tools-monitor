#!/bin/sh
set -ex
local_path=$(cd `dirname $0`;pwd)

cd ${local_path}
if [ -d "flaskProject" ];then
  rm -rf flaskProject
fi


mkdir flaskProject
mkdir -p flaskProject/templates
mkdir -p flaskProject/static
mkdir -p flaskProject/config

pip install --target=./flaskProject prometheus_client
rm -f ./flaskProject/prometheus_client/registry.py
\cp -rf ./python/registry.py ./flaskProject/prometheus_client/

if [ -f "openGauss-3.0.0-JDBC.tar.gz" ]; then
  tar -xvf openGauss-3.0.0-JDBC.tar.gz -C flaskProject
else
  echo "Please go to https://www.opengauss.org/zh/download.html to download openGauss-3.0.0-JDBC.tar.gz"
  exit 1
fi

cd js
npm install --no-fund --no-audit &&
npm run build &&
cd dist
\cp -rf static/* ${local_path}/flaskProject/static
\cp -rf index.html ${local_path}/flaskProject/templates
\cp -rf ${local_path}/python/exporter.py  ${local_path}/flaskProject
touch ${local_path}/flaskProject/config/add_config.yml
touch ${local_path}/flaskProject/config/check_config.yml
touch ${local_path}/flaskProject/config/db_config.yml
touch ${local_path}/flaskProject/config/re_config.yml
cd ${local_path}
