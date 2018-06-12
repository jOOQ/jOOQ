/* [java-9] */
/**
 * The jOOQ runtime module.
 */
module org.jooq {

    // jOOQ heavily depends on JDBC and cannot work without it
    requires transitive java.sql;

    // JAXB is used optionally for loading a variety of XML content, including
    // - Settings (org.jooq.conf)
    // - InformationSchema (org.jooq.util.xml.jaxb)
    requires static java.xml.bind;

    // The DefaultRecordMapper makes use of JavaBeans utilities, including:
    // - Support for ConstructorProperties
    requires static java.desktop;

    // Various utilities can make use of JPA annotations, when present, including:
    // - The DefaultRecordMapper
    // - The JPADatabase in the code generator
    // - The EntityManagerConnectionProvider
    requires static java.persistence;

    // The runtime Java compiler is used to generate enum types on the fly.
    // This dependency may be removed in the future.
    requires static java.compiler;

    // Optional logging APIs
    requires static java.logging;
    requires static slf4j.api;
    requires static log4j;

    exports org.jooq;
    exports org.jooq.api.annotation;
    exports org.jooq.conf;
    exports org.jooq.exception;
    exports org.jooq.impl;
    exports org.jooq.tools;
    exports org.jooq.tools.csv;
    exports org.jooq.tools.jdbc;
    exports org.jooq.tools.json;
    exports org.jooq.tools.reflect;
    exports org.jooq.types;
    exports org.jooq.util.jaxb.tools;
    exports org.jooq.util.xml.jaxb;


















    exports org.jooq.util.cubrid;
    exports org.jooq.util.derby;
    exports org.jooq.util.firebird;
    exports org.jooq.util.h2;
    exports org.jooq.util.hsqldb;
    exports org.jooq.util.mariadb;
    exports org.jooq.util.mysql;
    exports org.jooq.util.postgres;
    exports org.jooq.util.sqlite;
}
/* [/java-9] */