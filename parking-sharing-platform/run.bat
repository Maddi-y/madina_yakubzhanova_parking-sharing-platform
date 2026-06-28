@echo off

echo Killing old Java processes...
taskkill /F /IM java.exe >nul 2>&1

set JAVA_HOME=C:\Program Files\Java\jdk-21
set PATH=%JAVA_HOME%\bin;%PATH%

set TOMCAT_HOME=C:\Users\mmm19\OneDrive\Рабочий стол\Tomcat\apache-tomcat-10.1.55
set CATALINA_HOME=%TOMCAT_HOME%

echo Cleaning old deployment...

if exist "%TOMCAT_HOME%\webapps\parking-sharing-platform" (
    rmdir /S /Q "%TOMCAT_HOME%\webapps\parking-sharing-platform"
)

if exist "%TOMCAT_HOME%\webapps\parking-sharing-platform.war" (
    del /Q "%TOMCAT_HOME%\webapps\parking-sharing-platform.war"
)

echo Building project...
call mvn clean package

echo Copying WAR...
copy target\parking-sharing-platform.war "%TOMCAT_HOME%\webapps\parking-sharing-platform.war" /Y

echo Starting Tomcat...
call "%TOMCAT_HOME%\bin\startup.bat"

timeout /t 8

start http://localhost:9090/parking-sharing-platform/

pause