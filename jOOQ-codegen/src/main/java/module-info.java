/* [java-9] */
/**
 * The jOOQ code generation module.
 */
module org.jooq.codegen {
    requires org.jooq;
    requires org.jooq.meta;

    // Some marshalling and unmarshalling of JAXB-annotated XML configuration is needed
    requires java.xml.bind;

    exports org.jooq.codegen;
    exports org.jooq.codegen.example;
}
/* [/java-9] */