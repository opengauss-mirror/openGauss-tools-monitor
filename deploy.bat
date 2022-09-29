set local_path=%cd%
echo %local_path%


if exist flaskProject (
rd flaskProject /s/q
)

call md flaskProject\templates && md flaskProject\static && md flaskProject\config

call pip install --target=.\flaskProject prometheus_client

call del .\flaskProject\prometheus_client\registry.py && call xcopy .\python\registry.py .\flaskProject\prometheus_client\ /s/y

if exist openGauss-3.0.0-JDBC.tar.gz (
	call tar -xvf openGauss-3.0.0-JDBC.tar.gz -C flaskProject
) else (
	echo "Please go to https://www.opengauss.org/zh/download.html to download openGauss-3.0.0-JDBC.tar.gz"
	pause
	exit
)

call cd js && npm install --no-fund --no-audit && npm run build && cd dist && pause && xcopy static %local_path%\flaskProject\static /s /e /d /y /c && xcopy index.html %local_path%\flaskProject\templates  /s/y && xcopy %local_path%\python\exporter.py  %local_path%\flaskProject && copy nul %local_path%\flaskProject\config\add_config.yml && copy nul %local_path%\flaskProject\config\check_config.yml && copy nul %local_path%\flaskProject\config\db_config.yml && copy nul %local_path%\flaskProject\config\re_config.yml && cd %local_path% && echo success && pause