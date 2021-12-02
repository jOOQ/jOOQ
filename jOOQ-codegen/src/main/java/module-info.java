/**
 * The jOOQ codegen module.
 */
module org.jooq.codegen {

    // Other jOOQ modules
    requires transitive org.jooq.meta;

    // JAXB is used optionally for loading a variety of XML content, including
    // - Settings (org.jooq.conf)
    // - InformationSchema (org.jooq.util.xml.jaxb)
    requires static jakarta.xml.bind;

    exports org.jooq.codegen;
    exports org.jooq.codegen.example;
}
