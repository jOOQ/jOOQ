/* [java-9] */
/**
 * The jOOQ code generation meta model extensions module.
 */
module org.jooq.meta.extensions {
    requires org.jooq;
    requires org.jooq.meta;

    requires hibernate.jpa;
    requires hibernate.core;
    requires spring.beans;
    requires spring.context;
    requires spring.core;
    requires h2;

    exports org.jooq.meta.extensions.ddl;
    exports org.jooq.meta.extensions.jpa;
}
/* [/java-9] */