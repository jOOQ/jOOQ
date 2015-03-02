@ECHO OFF

IF "%1" == "off" (
    SET MAVEN_OPTS=-Djava.library.path=C:\sqljdbc_4.0\enu\auth\x64
) ELSE (
    SET MAVEN_OPTS=-Djava.library.path=C:\sqljdbc_4.0\enu\auth\x64 -Xdebug -Xnoagent -Djava.compile=NONE -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005
)

SET MAVEN_OPTS