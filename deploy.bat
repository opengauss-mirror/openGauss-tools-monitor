set local_path=%cd%
echo %local_path%


if exist flaskProject (
rd flaskProject /s/q
)

call md flaskProject\templates && md flaskProject\static && md flaskProject\config && md flaskProject\prometheus_client

if exist openGauss-3.0.0-JDBC.tar.gz (
	call tar -xvf openGauss-3.0.0-JDBC.tar.gz -C flaskProject
) else (
	echo "Please go to https://www.opengauss.org/zh/download.html to download openGauss-3.0.0-JDBC.tar.gz"
	pause
	exit
)

call cd js && npm install --no-fund --no-audit && npm run build && cd dist && xcopy static %local_path%\flaskProject\static /s /e /d /y /c && xcopy index.html %local_path%\flaskProject\templates  /s/y && xcopy %local_path%\python\exporter.py  %local_path%\flaskProject && copy nul %local_path%\flaskProject\config\add_config.yml && copy nul %local_path%\flaskProject\config\check_config.yml && copy nul %local_path%\flaskProject\config\db_config.yml && copy nul %local_path%\flaskProject\config\re_config.yml && cd %local_path% && xcopy thirdFile\prometheus_client %local_path%\flaskProject\prometheus_client /s /e /d /y /c && echo success && pause