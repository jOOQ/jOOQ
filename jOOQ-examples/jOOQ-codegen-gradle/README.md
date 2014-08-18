# Using the standalone jOOQ code generator with Gradle

[jOOQ](http://www.jooq.org) generates a simple Java representation of your database schema.
Every table, view, stored procedure, enum, UDT is a class. This plugin performs code generation
as part of the Gradle build.

## Usage

A very simple way of using jOOQ's standalone code generator is by writing the following
build.gradle script:

```groovy

// These imports are needed further down
// -------------------------------------
import javax.xml.bind.JAXB
import org.jooq.util.GenerationTool

// Configure the Java plugin and the dependencies
// ----------------------------------------------
apply plugin: 'java'

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    compile 'org.jooq:jooq:3.5.0-SNAPSHOT'

    runtime 'com.h2database:h2:1.4.177'
    testCompile 'junit:junit:4.11'
}

buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
    }

    dependencies {
        classpath 'org.jooq:jooq-codegen:3.5.0-SNAPSHOT'
        classpath 'com.h2database:h2:1.4.177'
    }
}

// Use your favourite XML builder to construct the code generation configuration file
// ----------------------------------------------------------------------------------
def writer = new StringWriter()
def xml = new groovy.xml.MarkupBuilder(writer)
.configuration('xmlns': 'http://www.jooq.org/xsd/jooq-codegen-3.5.0.xsd') {
    jdbc() {
        driver('org.h2.Driver')
        url('jdbc:h2:~/test-gradle')
        user('sa')
        password('')
    }
    generator() {
        database() {
        }
        generate() {
        }
        target() {
            packageName('org.jooq.example.gradle.db')
            directory('src/main/java')
        }
    }
}

// Run the code generator
// ----------------------
GenerationTool.main(
    JAXB.unmarshal(new StringReader(writer.toString()), org.jooq.util.jaxb.Configuration.class)
)
```

## Using a third-party plugin

There are also two third-party plugins maintained by the respective owners.

- https://github.com/etiennestuder/gradle-jooq-plugin
- https://github.com/ben-manes/gradle-jooq-plugin