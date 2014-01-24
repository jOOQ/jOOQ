#!/bin/sh
VERSION=3.3.0-SNAPSHOT

mvn install:install-file -Dfile=pom/jooq-parent/pom.xml             -DgroupId=org.jooq -DartifactId=jooq-parent        -Dversion=$VERSION -Dpackaging=pom
mvn install:install-file -Dfile=lib/jooq-$VERSION.jar               -DgroupId=org.jooq -DartifactId=jooq               -Dversion=$VERSION -Dpackaging=jar
mvn install:install-file -Dfile=lib/jooq-meta-$VERSION.jar          -DgroupId=org.jooq -DartifactId=jooq-meta          -Dversion=$VERSION -Dpackaging=jar
mvn install:install-file -Dfile=lib/jooq-codegen-$VERSION.jar       -DgroupId=org.jooq -DartifactId=jooq-codegen       -Dversion=$VERSION -Dpackaging=jar
mvn install:install-file -Dfile=lib/jooq-codegen-maven-$VERSION.jar -DgroupId=org.jooq -DartifactId=jooq-codegen-maven -Dversion=$VERSION -Dpackaging=jar
mvn install:install-file -Dfile=lib/jooq-scala-$VERSION.jar         -DgroupId=org.jooq -DartifactId=jooq-scala         -Dversion=$VERSION -Dpackaging=jar