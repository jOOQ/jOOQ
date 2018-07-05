/* [java-9] */
/**
 * The jOOQ example JPA entities module.
 */
module org.jooq.example.jpa.entities {
    requires java.persistence;
    requires org.hibernate.orm.core;

    exports org.jooq.example.jpa.converters;
    exports org.jooq.example.jpa.embeddables;
    exports org.jooq.example.jpa.entity;
}
/* [/java-9] */