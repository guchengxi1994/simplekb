@echo off
REM 进入打包文件的目录

REM 打包Flutter web项目
flutter build web --web-renderer html --base-href "/kb/"

REM 进入打包后的web文件夹
cd build\web

REM 删除目标目录中的旧文件
rd /s /q ..\..\..\src\main\resources\static

REM 创建目标目录
mkdir ..\..\..\src\main\resources\static

REM 复制打包后的文件到目标目录
xcopy * ..\..\..\src\main\resources\static /e /i /y

echo "Web files have been successfully copied to static folder!"
pause


