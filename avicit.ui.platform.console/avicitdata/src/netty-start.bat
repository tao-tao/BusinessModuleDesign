@echo off

setlocal EnableDelayedExpansion 
set jarpth=..\classes
for /F %%a in ('dir ..\lib\*.jar/b') do set jarpth=!jarpth!;..\lib\%%a

java -Xms512m -Xmx1024m -XX:PermSize=256M -XX:MaxPermSize=512m -cp %jarpth%  avicit.platform6.core.rest.NettyStart 10001 2000