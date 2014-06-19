@echo off
pushd %~dp0
cmd /c gradlew test
for %%f in (.\build\reports\tests\index.html) do (
    echo Starting %%~ff
    start %%f
)
popd