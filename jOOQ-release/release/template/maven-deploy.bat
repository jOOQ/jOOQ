@echo off

:parse
IF "%~1"=="" GOTO endparse
IF "%~1"=="-i" GOTO parserepository
IF "%~1"=="--repository" GOTO parserepository
IF "%~1"=="-u" GOTO parseurl
IF "%~1"=="--url" GOTO parseurl
IF "%~1"=="-h" GOTO parsehelp
IF "%~1"=="--help" GOTO parsehelp
GOTO endparse
:parserepository
SHIFT
SET REPOSITORY=%~1
GOTO repeat
:parseurl
SHIFT
SET URL=%~1
GOTO repeat
:parsehelp
ECHO Usage: maven-deploy.bat ^<options^>
ECHO.
ECHO Options:
ECHO  -h, --help                      Display this help and exit
ECHO  -i, --repository   (Optional)   The repository id as specified in your settings.xml
ECHO  -u, --url          (Mandatory)  The repository URL
GOTO end
:repeat
SHIFT
GOTO parse

:usage
ECHO Wrong usage. Run with -h or --help argument for details.
GOTO end
:endparse
IF NOT "%~1"=="" GOTO usage
IF "%URL%"=="" GOTO usage

set VERSION=3.11.0-SNAPSHOT

if exist jOOQ-javadoc\jooq-%VERSION%-javadoc.jar (
  set JAVADOC_JOOQ=-Djavadoc=jOOQ-javadoc\jooq-%VERSION%-javadoc.jar
  set JAVADOC_JOOQ_META=-Djavadoc=jOOQ-javadoc\jooq-meta-%VERSION%-javadoc.jar
  set JAVADOC_JOOQ_META_EXTENSIONS=-Djavadoc=jOOQ-javadoc\jooq-meta-extensions-%VERSION%-javadoc.jar
  set JAVADOC_JOOQ_CODEGEN=-Djavadoc=jOOQ-javadoc\jooq-codegen-%VERSION%-javadoc.jar
  set JAVADOC_JOOQ_CODEGEN_MAVEN=-Djavadoc=jOOQ-javadoc\jooq-codegen-maven-%VERSION%-javadoc.jar

  set JAVADOC_JOOQ_CHECKER=-Djavadoc=jOOQ-javadoc\jooq-checker-%VERSION%-javadoc.jar




  set JAVADOC_JOOQ_SCALA_2_11=-Djavadoc=jOOQ-javadoc\jooq-scala_2.11-%VERSION%-javadoc.jar
  set JAVADOC_JOOQ_SCALA_2_12=-Djavadoc=jOOQ-javadoc\jooq-scala_2.12-%VERSION%-javadoc.jar
)

if exist jOOQ-src\jooq-%VERSION%-sources.jar (
  set SOURCES_JOOQ=-Dsources=jOOQ-src\jooq-%VERSION%-sources.jar
  set SOURCES_JOOQ_META=-Dsources=jOOQ-src\jooq-meta-%VERSION%-sources.jar
  set SOURCES_JOOQ_META_EXTENSIONS=-Dsources=jOOQ-src\jooq-meta-extensions-%VERSION%-sources.jar
  set SOURCES_JOOQ_CODEGEN=-Dsources=jOOQ-src\jooq-codegen-%VERSION%-sources.jar
  set SOURCES_JOOQ_CODEGEN_MAVEN=-Dsources=jOOQ-src\jooq-codegen-maven-%VERSION%-sources.jar

  set SOURCES_JOOQ_CHECKER=-Dsources=jOOQ-src\jooq-checker-%VERSION%-sources.jar




  set SOURCES_JOOQ_SCALA_2_11=-Djavadoc=jOOQ-src\jooq-scala_2.11-%VERSION%-sources.jar
  set SOURCES_JOOQ_SCALA_2_12=-Djavadoc=jOOQ-src\jooq-scala_2.12-%VERSION%-sources.jar
)

call mvn deploy:deploy-file -Dfile=jOOQ-pom\pom.xml                            -DgroupId=org.jooq -DartifactId=jooq-parent          -DrepositoryId=%REPOSITORY% -Durl=%URL% -Dversion=%VERSION% -Dpackaging=pom
call mvn deploy:deploy-file -Dfile=jOOQ-lib\jooq-%VERSION%.jar                 -DgroupId=org.jooq -DartifactId=jooq                 -DrepositoryId=%REPOSITORY% -Durl=%URL% -Dversion=%VERSION% -Dpackaging=jar %JAVADOC_JOOQ%                 %SOURCES_JOOQ%                 -DpomFile=jOOQ-pom\jooq\pom.xml
call mvn deploy:deploy-file -Dfile=jOOQ-lib\jooq-meta-%VERSION%.jar            -DgroupId=org.jooq -DartifactId=jooq-meta            -DrepositoryId=%REPOSITORY% -Durl=%URL% -Dversion=%VERSION% -Dpackaging=jar %JAVADOC_JOOQ_META%            %SOURCES_JOOQ_META%            -DpomFile=jOOQ-pom\jooq-meta\pom.xml
call mvn deploy:deploy-file -Dfile=jOOQ-lib\jooq-meta-extensions-%VERSION%.jar -DgroupId=org.jooq -DartifactId=jooq-meta-extensions -DrepositoryId=%REPOSITORY% -Durl=%URL% -Dversion=%VERSION% -Dpackaging=jar %JAVADOC_JOOQ_META_EXTENSIONS% %SOURCES_JOOQ_META_EXTENSIONS% -DpomFile=jOOQ-pom\jooq-meta-extensions\pom.xml
call mvn deploy:deploy-file -Dfile=jOOQ-lib\jooq-codegen-%VERSION%.jar         -DgroupId=org.jooq -DartifactId=jooq-codegen         -DrepositoryId=%REPOSITORY% -Durl=%URL% -Dversion=%VERSION% -Dpackaging=jar %JAVADOC_JOOQ_CODEGEN%         %SOURCES_JOOQ_CODEGEN%         -DpomFile=jOOQ-pom\jooq-codegen\pom.xml
call mvn deploy:deploy-file -Dfile=jOOQ-lib\jooq-codegen-maven-%VERSION%.jar   -DgroupId=org.jooq -DartifactId=jooq-codegen-maven   -DrepositoryId=%REPOSITORY% -Durl=%URL% -Dversion=%VERSION% -Dpackaging=jar %JAVADOC_JOOQ_CODEGEN_MAVEN%   %SOURCES_JOOQ_CODEGEN_META%    -DpomFile=jOOQ-pom\jooq-codegen-maven\pom.xml

call mvn deploy:deploy-file -Dfile=jOOQ-lib\jooq-checker-%VERSION%.jar         -DgroupId=org.jooq -DartifactId=jooq-checker         -DrepositoryId=%REPOSITORY% -Durl=%URL% -Dversion=%VERSION% -Dpackaging=jar %JAVADOC_JOOQ_CHECKER%         %SOURCES_JOOQ_CHECKER%         -DpomFile=jOOQ-pom\jooq-checker\pom.xml





call mvn deploy:deploy-file -Dfile=jOOQ-lib\jooq-scala_2.11-%VERSION%.jar      -DgroupId=org.jooq -DartifactId=jooq-scala_2.11      -DrepositoryId=%REPOSITORY% -Durl=%URL% -Dversion=%VERSION% -Dpackaging=jar %JAVADOC_JOOQ_SCALA_2_11%      %SOURCES_JOOQ_SCALA_2_11%      -DpomFile=jOOQ-pom\jooq-scala_2.11\pom.xml
call mvn deploy:deploy-file -Dfile=jOOQ-lib\jooq-scala_2.12-%VERSION%.jar      -DgroupId=org.jooq -DartifactId=jooq-scala_2.12      -DrepositoryId=%REPOSITORY% -Durl=%URL% -Dversion=%VERSION% -Dpackaging=jar %JAVADOC_JOOQ_SCALA_2_12%      %SOURCES_JOOQ_SCALA_2_12%      -DpomFile=jOOQ-pom\jooq-scala_2.12\pom.xml


echo
echo
echo The different jOOQ editions are released under different Maven groupIds!
echo ------------------------------------------------------------------------
echo - org.jooq.pro        : The jOOQ Express, Professional, and Enterprise Editions
echo - org.jooq.pro-java-6 : The jOOQ Express, Professional, and Enterprise Editions with support for Java 6
echo - org.jooq.trial      : The jOOQ Trial Edition
echo - org.jooq            : The jOOQ Open Source Edition

:end