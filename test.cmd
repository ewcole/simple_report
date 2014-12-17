@echo off
pushd %~dp0
cmd /c gradlew test
for %%f in (.\build\reports\tests\index.html) do (
    echo Starting %%~ff
    start %%f
)

:: if "%userid%" equ "" (
::    set /p userid=userid: 
:: )
:: setlocal
:: set classpath=.\build\classes\main;%classpath%
:: testQueryEngine.groovy %userid%
:: endlocal
popd
