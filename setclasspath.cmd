call :prepend_jar x:\source\main\jars\ojdbc6.jar
call :prepend_jar x:\source\main\jars\jcc_utils-1.1.0.jar
call :prepend_jar %~dp0\build\libs\simple_report-0.0.0-~master~.jar
call :append_jar  x:\source\main\jars\commons-cli-1.2.jar
call :append_jar  x:\source\main\jars\commons-logging-1.1.1.jar
call :append_jar  x:\source\main\jars\groovy-1.8.9.jar
goto :eof

:append_jar
set classpath=%classpath%;%~s1
goto :eof

:prepend_jar
set classpath=%~s1;%classpath%
goto :eof