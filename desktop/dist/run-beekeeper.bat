@echo off
REM ========================================
REM Beekeeper Desktop Application Launcher
REM Windows Batch Script
REM ========================================

echo ====================================
echo Beekeeper Desktop - Starting...
echo ====================================
echo.

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Java not found!
    echo Please install Java 17 or higher from: https://adoptium.net/
    echo.
    pause
    exit /b 1
)

REM Set database path to user home directory
set DB_PATH=%USERPROFILE%\beekeeper-desktop.db
echo Database location: %DB_PATH%
echo.

REM Check if database exists (first run)
if not exist "%DB_PATH%" (
    echo First run detected - database will be initialized with test data
    echo Test apiary "Testovacia vcelnica" will be created
    echo.
)

REM Build classpath from all JARs in lib directory
set CLASSPATH=lib\*

REM Run the application
echo Starting Beekeeper Desktop...
echo.
java -Dprism.order=sw -Djavafx.animation.fullspeed=false -cp "%CLASSPATH%" com.beekeeper.desktop.Main

REM Check exit code
if %errorlevel% neq 0 (
    echo.
    echo ====================================
    echo Application exited with error!
    echo ====================================
    pause
    exit /b %errorlevel%
)

echo.
echo ====================================
echo Application closed successfully
echo ====================================
