/* [java-9] */
/**
 * The jOOQ example JPA entities module.
 */
module org.jooq.example.jpa {
    requires org.jooq;
    requires org.jooq.example.jpa.entities;

    requires java.persistence;
    requires org.hibernate.orm.core;

    requires spring.jdbc;
    requires spring.orm;
}
/* [/java-9] */