@echo off
cd build
cd web
echo "Copying web files to static folder..."
rd /s /q ..\..\..\src\main\resources\static
mkdir ..\..\..\src\main\resources\static
xcopy * ..\..\..\src\main\resources\static /e /i /y
echo "Web files have been successfully copied to static folder!"
pause



