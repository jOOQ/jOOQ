/**
 * The jOOQ runtime module.
 */
module org.jooq {

    // jOOQ heavily depends on JDBC and cannot work without it
    requires transitive java.sql;

    // We're using SAX to parse XML
    requires static java.xml;

    // JAXB is used optionally for loading a variety of XML content, including
    // - Settings (org.jooq.conf)
    // - InformationSchema (org.jooq.util.xml.jaxb)
    requires static jakarta.xml.bind;

    // The DefaultRecordMapper makes use of JavaBeans utilities, including:
    // - Support for ConstructorProperties
    requires static java.desktop;

    // Various utilities can make use of JPA annotations, when present, including:
    // - The DefaultRecordMapper
    // - The JPADatabase in the code generator
    // - The EntityManagerConnectionProvider
    requires static jakarta.persistence;

    // The runtime Java compiler is used to generate enum types on the fly.
    // This dependency may be removed in the future.
    requires static java.compiler;

    // Optional logging APIs - slf4j will be preferred if found
    requires static org.slf4j;
    requires static java.logging;

    // Nullability annotations for better Kotlin interop
    requires static org.jetbrains.annotations;

    // Needed for Checker Framework to work with modules.
    requires org.checkerframework.checker.qual;

    requires transitive org.reactivestreams;
    requires transitive r2dbc.spi;

    // [#11738] optional vendor specific JDBC drivers





    exports org.jooq;
    exports org.jooq.conf;
    exports org.jooq.exception;
    exports org.jooq.impl;
    exports org.jooq.migrations.xml.jaxb;
    exports org.jooq.tools;
    exports org.jooq.tools.csv;
    exports org.jooq.tools.jdbc;
    exports org.jooq.tools.json;
    exports org.jooq.tools.r2dbc;
    exports org.jooq.tools.reflect;
    exports org.jooq.types;
    exports org.jooq.util.jaxb.tools;
    exports org.jooq.util.xml.jaxb;
























    exports org.jooq.util.cubrid;
    exports org.jooq.util.derby;
    exports org.jooq.util.firebird;
    exports org.jooq.util.h2;
    exports org.jooq.util.hsqldb;
    exports org.jooq.util.ignite;
    exports org.jooq.util.mariadb;
    exports org.jooq.util.mysql;
    exports org.jooq.util.postgres;
    exports org.jooq.util.sqlite;
    exports org.jooq.util.yugabytedb;
}
