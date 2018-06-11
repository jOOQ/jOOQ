/* [java-9] */
module org.jooq {

    // jOOQ heavily depends on JDBC and cannot work without it
    requires java.sql;

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
}
/* [/java-9] */