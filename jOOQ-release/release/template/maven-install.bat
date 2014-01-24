@echo off
set VERSION=3.3.0-SNAPSHOT

call mvn install:install-file -Dfile=pom\jooq-parent\pom.xml              -DgroupId=org.jooq -DartifactId=jooq-parent        -Dversion=%VERSION% -Dpackaging=pom
call mvn install:install-file -Dfile=lib\jooq-%VERSION%.jar               -DgroupId=org.jooq -DartifactId=jooq               -Dversion=%VERSION% -Dpackaging=jar
call mvn install:install-file -Dfile=lib\jooq-meta-%VERSION%.jar          -DgroupId=org.jooq -DartifactId=jooq-meta          -Dversion=%VERSION% -Dpackaging=jar
call mvn install:install-file -Dfile=lib\jooq-codegen-%VERSION%.jar       -DgroupId=org.jooq -DartifactId=jooq-codegen       -Dversion=%VERSION% -Dpackaging=jar
call mvn install:install-file -Dfile=lib\jooq-codegen-maven-%VERSION%.jar -DgroupId=org.jooq -DartifactId=jooq-codegen-maven -Dversion=%VERSION% -Dpackaging=jar
call mvn install:install-file -Dfile=lib\jooq-scala-%VERSION%.jar         -DgroupId=org.jooq -DartifactId=jooq-scala         -Dversion=%VERSION% -Dpackaging=jar