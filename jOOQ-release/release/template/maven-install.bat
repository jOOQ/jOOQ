@echo off
set VERSION=3.5.2

if exist jOOQ-javadoc\jooq-%VERSION%-javadoc.jar (
  set JAVADOC_JOOQ=-Djavadoc=jOOQ-javadoc\jooq-%VERSION%-javadoc.jar
  set JAVADOC_JOOQ_META=-Djavadoc=jOOQ-javadoc\jooq-meta-%VERSION%-javadoc.jar
  set JAVADOC_JOOQ_CODEGEN=-Djavadoc=jOOQ-javadoc\jooq-codegen-%VERSION%-javadoc.jar
  set JAVADOC_JOOQ_CODEGEN_MAVEN=-Djavadoc=jOOQ-javadoc\jooq-codegen-maven-%VERSION%-javadoc.jar
  set JAVADOC_JOOQ_SCALA=-Djavadoc=jOOQ-javadoc\jooq-scala-%VERSION%-javadoc.jar
)

if exist jOOQ-src\jooq-%VERSION%-sources.jar (
  set SOURCES_JOOQ=-Dsources=jOOQ-src\jooq-%VERSION%-sources.jar
  set SOURCES_JOOQ_META=-Dsources=jOOQ-src\jooq-meta-%VERSION%-sources.jar
  set SOURCES_JOOQ_CODEGEN=-Dsources=jOOQ-src\jooq-codegen-%VERSION%-sources.jar
  set SOURCES_JOOQ_CODEGEN_MAVEN=-Dsources=jOOQ-src\jooq-codegen-maven-%VERSION%-sources.jar
  set SOURCES_JOOQ_SCALA=-Dsources=jOOQ-src\jooq-scala-%VERSION%-sources.jar
)

call mvn install:install-file -Dfile=jOOQ-pom\pom.xml                          -DgroupId=org.jooq -DartifactId=jooq-parent        -Dversion=%VERSION% -Dpackaging=pom
call mvn install:install-file -Dfile=jOOQ-lib\jooq-%VERSION%.jar               -DgroupId=org.jooq -DartifactId=jooq               -Dversion=%VERSION% -Dpackaging=jar %JAVADOC_JOOQ%               %SOURCES_JOOQ%              -DpomFile=jOOQ-pom\jooq\pom.xml
call mvn install:install-file -Dfile=jOOQ-lib\jooq-meta-%VERSION%.jar          -DgroupId=org.jooq -DartifactId=jooq-meta          -Dversion=%VERSION% -Dpackaging=jar %JAVADOC_JOOQ_META%          %SOURCES_JOOQ_META%         -DpomFile=jOOQ-pom\jooq-meta\pom.xml
call mvn install:install-file -Dfile=jOOQ-lib\jooq-codegen-%VERSION%.jar       -DgroupId=org.jooq -DartifactId=jooq-codegen       -Dversion=%VERSION% -Dpackaging=jar %JAVADOC_JOOQ_CODEGEN%       %SOURCES_JOOQ_CODEGEN%      -DpomFile=jOOQ-pom\jooq-codegen\pom.xml
call mvn install:install-file -Dfile=jOOQ-lib\jooq-codegen-maven-%VERSION%.jar -DgroupId=org.jooq -DartifactId=jooq-codegen-maven -Dversion=%VERSION% -Dpackaging=jar %JAVADOC_JOOQ_CODEGEN_MAVEN% %SOURCES_JOOQ_CODEGEN_META% -DpomFile=jOOQ-pom\jooq-codegen-maven\pom.xml
call mvn install:install-file -Dfile=jOOQ-lib\jooq-scala-%VERSION%.jar         -DgroupId=org.jooq -DartifactId=jooq-scala         -Dversion=%VERSION% -Dpackaging=jar %JAVADOC_JOOQ_SCALA%         %SOURCES_JOOQ_SCALA%        -DpomFile=jOOQ-pom\jooq-scala\pom.xml