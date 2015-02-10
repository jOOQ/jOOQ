#!/bin/sh
VERSION=3.5.2

if [ -f jOOQ-javadoc/jooq-$VERSION-javadoc.jar ]; then
  JAVADOC_JOOQ=-Djavadoc=jOOQ-javadoc/jooq-$VERSION-javadoc.jar
  JAVADOC_JOOQ_META=-Djavadoc=jOOQ-javadoc/jooq-meta-$VERSION-javadoc.jar
  JAVADOC_JOOQ_CODEGEN=-Djavadoc=jOOQ-javadoc/jooq-codegen-$VERSION-javadoc.jar
  JAVADOC_JOOQ_CODEGEN_MAVEN=-Djavadoc=jOOQ-javadoc/jooq-codegen-maven-$VERSION-javadoc.jar
  JAVADOC_JOOQ_SCALA=-Djavadoc=jOOQ-javadoc/jooq-scala-$VERSION-javadoc.jar
fi

if [ -f jOOQ-src/jooq-$VERSION-sources.jar ]; then
  SOURCES_JOOQ=-Dsources=jOOQ-src/jooq-$VERSION-sources.jar
  SOURCES_JOOQ_META=-Dsources=jOOQ-src/jooq-meta-$VERSION-sources.jar
  SOURCES_JOOQ_CODEGEN=-Dsources=jOOQ-src/jooq-codegen-$VERSION-sources.jar
  SOURCES_JOOQ_CODEGEN_MAVEN=-Dsources=jOOQ-src/jooq-codegen-maven-$VERSION-sources.jar
  SOURCES_JOOQ_SCALA=-Dsources=jOOQ-src/jooq-scala-$VERSION-sources.jar
fi

mvn install:install-file -Dfile=jOOQ-pom/pom.xml                         -DgroupId=org.jooq -DartifactId=jooq-parent        -Dversion=$VERSION -Dpackaging=pom
mvn install:install-file -Dfile=jOOQ-lib/jooq-$VERSION.jar               -DgroupId=org.jooq -DartifactId=jooq               -Dversion=$VERSION -Dpackaging=jar $JAVADOC_JOOQ               $SOURCES_JOOQ              -DpomFile=jOOQ-pom/jooq/pom.xml
mvn install:install-file -Dfile=jOOQ-lib/jooq-meta-$VERSION.jar          -DgroupId=org.jooq -DartifactId=jooq-meta          -Dversion=$VERSION -Dpackaging=jar $JAVADOC_JOOQ_META          $SOURCES_JOOQ_META         -DpomFile=jOOQ-pom/jooq-meta/pom.xml
mvn install:install-file -Dfile=jOOQ-lib/jooq-codegen-$VERSION.jar       -DgroupId=org.jooq -DartifactId=jooq-codegen       -Dversion=$VERSION -Dpackaging=jar $JAVADOC_JOOQ_CODEGEN       $SOURCES_JOOQ_CODEGEN      -DpomFile=jOOQ-pom/jooq-codegen/pom.xml
mvn install:install-file -Dfile=jOOQ-lib/jooq-codegen-maven-$VERSION.jar -DgroupId=org.jooq -DartifactId=jooq-codegen-maven -Dversion=$VERSION -Dpackaging=jar $JAVADOC_JOOQ_CODEGEN_MAVEN $SOURCES_JOOQ_CODEGEN_META -DpomFile=jOOQ-pom/jooq-codegen-maven/pom.xml
mvn install:install-file -Dfile=jOOQ-lib/jooq-scala-$VERSION.jar         -DgroupId=org.jooq -DartifactId=jooq-scala         -Dversion=$VERSION -Dpackaging=jar $JAVADOC_JOOQ_SCALA         $SOURCES_JOOQ_SCALA        -DpomFile=jOOQ-pom/jooq-scala/pom.xml