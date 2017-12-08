@echo off

setlocal EnableDelayedExpansion 
set jarpth=..\classes
for /F %%a in ('dir ..\lib\*.jar/b') do set jarpth=!jarpth!;..\lib\%%a

java -Xms256m -Xmx512m -XX:PermSize=128M -XX:MaxPermSize=256m -cp %jarpth%  avicit.platform6.core.rest.NettyStart 10001 2000