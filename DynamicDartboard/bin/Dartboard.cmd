@echo OFF
@setlocal
cd C:\dev\DynamicDartboard\bin
set ENVIRONMENT_HOME=\dev\DynamicDartboard
set LIBDIR=%ENVIRONMENT_HOME%\lib

@set JDK=%ENVIRONMENT_HOME%\jdk
@set CLASSPATH=%JDK%/jre/lib/rt.jar;%JDK%/lib/tools.jar;%LIBDIR%/javax.servlet/servlet.jar;%ENVIRONMENT_HOME%/classes
%JDK%\jre\bin\java -server -Xms256m -Xmx256m -XX:NewRatio=3 -XX:SurvivorRatio=5 com.dynamicdartboard.DartBoard %*

if not errorlevel 1 goto :end
@echo Error running program
goto end

:end
@endlocal
