@echo off
title TripMate - Smart Travel Planning System
echo =============================================================
echo               Starting TripMate Web Application              
echo =============================================================
echo.

set MAVEN_CMD=""

where mvn >nul 2>nul
if %ERRORLEVEL% EQU 0 (
    set MAVEN_CMD=mvn
) else (
    if exist "C:\Program Files\JetBrains\IntelliJ IDEA 2026.2.4\plugins\maven-plugin\lib\maven3\bin\mvn.cmd" (
        set MAVEN_CMD="C:\Program Files\JetBrains\IntelliJ IDEA 2026.2.4\plugins\maven-plugin\lib\maven3\bin\mvn.cmd"
    )
)

if %MAVEN_CMD%=="" (
    echo [ERROR] Maven not found. Please run Main.java inside IntelliJ IDEA!
    pause
    exit /b 1
)

echo [1/3] Compiling application...
call %MAVEN_CMD% compile
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Compilation failed.
    pause
    exit /b %ERRORLEVEL%
)

echo [2/3] Syncing classes to WEB-INF/classes...
if not exist "src\main\webapp\WEB-INF\classes" mkdir "src\main\webapp\WEB-INF\classes"
xcopy /s /e /y "target\classes\*" "src\main\webapp\WEB-INF\classes\" >nul 2>nul

echo [3/3] Booting Embedded Apache Tomcat 10.1 on port 8080...
echo.
call %MAVEN_CMD% exec:java

pause
