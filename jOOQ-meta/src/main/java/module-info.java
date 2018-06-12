/* [java-9] */
/**
 * The jOOQ code generation meta model module.
 */
module org.jooq.meta {
    requires org.jooq;

    // The XMLDatabase works with JAXB-annotated InformationSchema types
    requires java.xml.bind;
}
/* [/java-9] */