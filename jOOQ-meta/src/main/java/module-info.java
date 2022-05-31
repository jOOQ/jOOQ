/**
 * The jOOQ meta module.
 */
module org.jooq.meta {

    // Other jOOQ modules
    requires transitive org.jooq;

    // Nullability annotations for better Kotlin interop
    requires static org.jetbrains.annotations;

    // JAXB is used optionally for loading a variety of XML content, including
    // - Settings (org.jooq.conf)
    // - InformationSchema (org.jooq.util.xml.jaxb)
    requires static jakarta.xml.bind;

    exports org.jooq.meta;
    exports org.jooq.meta.cubrid;
    exports org.jooq.meta.derby;
    exports org.jooq.meta.firebird;
    exports org.jooq.meta.h2;
    exports org.jooq.meta.hsqldb;
    exports org.jooq.meta.ignite;
    exports org.jooq.meta.jaxb;
    exports org.jooq.meta.jdbc;
    exports org.jooq.meta.mariadb;
    exports org.jooq.meta.mysql;
    exports org.jooq.meta.postgres;
    exports org.jooq.meta.sqlite;
    exports org.jooq.meta.xml;
    exports org.jooq.meta.yugabytedb;























}
