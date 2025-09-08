@echo off
REM Compilation des fichiers Java en Java 17
setlocal
set SRC_DIR=%~dp0src
set CLASS_DIR=%~dp0class
if not exist "%CLASS_DIR%" mkdir "%CLASS_DIR%"

REM Compilation
javac -encoding UTF-8 -d "%CLASS_DIR%" --release 17 "%SRC_DIR%\metier\*.java" "%SRC_DIR%\ihm\*.java"
if %errorlevel% neq 0 (
    echo Erreur de compilation.
    exit /b %errorlevel%
)

REM Exécution avec support ANSI
cd /d "%CLASS_DIR%"
set JAVA_ANSI_OPTS=-Djansi.passthrough=true
java -Dfile.encoding=UTF-8 metier.Demineur
endlocal
pause
