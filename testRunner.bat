@echo off
REM Batch file to execute Cucumber tests with ApiTest tag filter
REM Usage: Just double-click this file or run from command prompt

setlocal

REM Set your Maven home path if not in system PATH
REM set MAVEN_HOME=C:\path\to\apache-maven-3.9.9

REM Set project directory (where pom.xml is located)
set PROJECT_DIR=%~dp0

echo.
echo [INFO] Running Cucumber tests with @ApiTest tag...
echo.

REM Main Maven command
call mvn test -Dcucumber.filter.tags="@ApiTest" -f "%PROJECT_DIR%pom.xml"

REM Check if the command succeeded
if %ERRORLEVEL% equ 0 (
    echo.
    echo [INFO] Test execution completed successfully
) else (
    echo.
    echo [ERROR] Test execution failed with error code %ERRORLEVEL%
)

echo.
pause
endlocal