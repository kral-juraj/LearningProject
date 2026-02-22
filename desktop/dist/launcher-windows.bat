@echo off
REM Beekeeper Desktop Launcher Script for Windows

REM Get application home directory
set APP_HOME=%~dp0..

REM Build Windows-specific classpath (all JARs for classpath)
set APP_CLASSPATH=%APP_HOME%\lib\*

REM Build Windows-specific module path (only win.jar files for modules)
set JAVAFX_MODULES=
for %%f in (%APP_HOME%\lib\javafx-base-*-win.jar) do set JAVAFX_MODULES=%%f
for %%f in (%APP_HOME%\lib\javafx-controls-*-win.jar) do set JAVAFX_MODULES=%JAVAFX_MODULES%;%%f
for %%f in (%APP_HOME%\lib\javafx-fxml-*-win.jar) do set JAVAFX_MODULES=%JAVAFX_MODULES%;%%f
for %%f in (%APP_HOME%\lib\javafx-graphics-*-win.jar) do set JAVAFX_MODULES=%JAVAFX_MODULES%;%%f

REM Debug: Print module path (comment out in production)
REM echo JavaFX Modules: %JAVAFX_MODULES%

REM Execute application with Windows-specific JavaFX module-path
REM Note: Uses only javafx-*-win.jar to avoid module conflicts
java ^
  -Djavafx.animation.fullspeed=false ^
  --enable-native-access=ALL-UNNAMED ^
  --module-path "%JAVAFX_MODULES%" ^
  --add-modules javafx.controls,javafx.fxml ^
  -cp "%APP_CLASSPATH%" ^
  com.beekeeper.desktop.Main
