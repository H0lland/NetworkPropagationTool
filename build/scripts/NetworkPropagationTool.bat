@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  NetworkPropagationTool startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Add default JVM options here. You can also use JAVA_OPTS and NETWORK_PROPAGATION_TOOL_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windows variants

if not "%OS%" == "Windows_NT" goto win9xME_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\gs-gradle-0.1.0.jar;%APP_HOME%\lib\ejml-all-0.33.jar;%APP_HOME%\lib\jblas-1.2.4.jar;%APP_HOME%\lib\gs-algo-1.3.jar;%APP_HOME%\lib\gs-core-1.3.jar;%APP_HOME%\lib\ejml-simple-0.33.jar;%APP_HOME%\lib\ejml-fdense-0.33.jar;%APP_HOME%\lib\ejml-dsparse-0.33.jar;%APP_HOME%\lib\ejml-ddense-0.33.jar;%APP_HOME%\lib\ejml-cdense-0.33.jar;%APP_HOME%\lib\ejml-zdense-0.33.jar;%APP_HOME%\lib\ejml-core-0.33.jar;%APP_HOME%\lib\junit-4.12.jar;%APP_HOME%\lib\pherd-1.0.jar;%APP_HOME%\lib\mbox2-1.0.jar;%APP_HOME%\lib\commons-math-2.1.jar;%APP_HOME%\lib\commons-math3-3.4.1.jar;%APP_HOME%\lib\jfreechart-1.0.14.jar;%APP_HOME%\lib\hamcrest-core-1.3.jar;%APP_HOME%\lib\jcommon-1.0.17.jar;%APP_HOME%\lib\xml-apis-1.3.04.jar;%APP_HOME%\lib\itext-2.1.5.jar;%APP_HOME%\lib\bcmail-jdk14-138.jar;%APP_HOME%\lib\bcprov-jdk14-138.jar

@rem Execute NetworkPropagationTool
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %NETWORK_PROPAGATION_TOOL_OPTS%  -classpath "%CLASSPATH%" catworks.Main %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable NETWORK_PROPAGATION_TOOL_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%NETWORK_PROPAGATION_TOOL_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
