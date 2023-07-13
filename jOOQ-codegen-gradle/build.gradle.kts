plugins {
    id("java")
    id("com.gradle.plugin-publish") version "1.2.0"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenLocal()
    mavenCentral()
}

group = "org.jooq"
version = "3.19.0-SNAPSHOT"

dependencies {
    implementation(gradleApi())
    implementation("$group:jooq-codegen:$version")
    implementation("$group:jooq-meta-kotlin:$version")
}

tasks.withType<Javadoc> {
    (options as StandardJavadocDocletOptions).addStringOption("Xdoclint:none", "-quiet")
}

gradlePlugin {
    website = "https://jooq.org"
    vcsUrl = "https://github.com/jOOQ/jOOQ"
    plugins {
        create("simplePlugin") {
            id = "${group}.jooq-codegen-gradle"
            displayName = "jooq-codegen-gradle"
            description = "jOOQ code generation plugin for Gradle"
            tags.set(listOf("jooq"))
            implementationClass = "org.jooq.codegen.gradle.CodegenPlugin"
        }
    }
}