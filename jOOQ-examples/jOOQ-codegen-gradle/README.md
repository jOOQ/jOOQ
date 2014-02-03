# Gradle jOOQ Plugin

[jOOQ](http://www.jooq.org) generates a simple Java representation of your database schema.
Every table, view, stored procedure, enum, UDT is a class. This plugin performs code generation
as part of the Gradle build.

## Usage

This plugin is hosted on the Maven Central Repository. All actions are logged at the `info` level.

The configuration is defined as an XML DSL based on jOOQ's 
[codegen schema](http://www.jooq.org/xsd/jooq-codegen-3.0.0.xsd). The default target directory is
updated to reflect Gradle's build directory (`build/generated-sources/jooq`).

```groovy
apply plugin: 'jooq'

buildscript {
  repositories {
    mavenCentral()
  }
  
  dependencies {
    classpath "org.jooq:jooq-codegen-gradle:${versions.jOOQ}"
  }
}

jooq {
  jdbc {
    url 'jdbc:mysql://localhost:3306'
    driver 'com.mysql.jdbc.Driver'
    user 'root'
  }
  generator {
    database {
      name 'org.jooq.util.mysql.MySQLDatabase'
      inputSchema 'example'
      includes '.*'
    }
  }
}
```

## Tasks

### `generateJooq`

Executes the jOOQ [code generator](http://www.jooq.org/doc/3.0/manual/code-generation/).
