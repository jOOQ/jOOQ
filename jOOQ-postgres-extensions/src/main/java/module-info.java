/**
 * The jOOQ postgres extensions module.
 */
module org.jooq.postgres.extensions {

    // Other jOOQ modules
    requires transitive org.jooq;

    // Nullability annotations for better Kotlin interop
    requires static org.jetbrains.annotations;

    // Vendor specific JDBC drivers
    requires static org.postgresql.jdbc;

    opens org.jooq.postgres.extensions.bindings;
    opens org.jooq.postgres.extensions.converters;
    opens org.jooq.postgres.extensions.types;
}
