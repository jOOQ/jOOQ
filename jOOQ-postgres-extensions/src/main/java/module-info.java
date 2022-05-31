/**
 * The jOOQ postgres extensions module.
 */
module org.jooq.postgres.extensions {

    // Other jOOQ modules
    requires transitive org.jooq;

    // Nullability annotations for better Kotlin interop
    requires static transitive org.jetbrains.annotations;

    // Vendor specific JDBC drivers
    requires static org.postgresql.jdbc;

    exports org.jooq.postgres.extensions.bindings;
    exports org.jooq.postgres.extensions.converters;
    exports org.jooq.postgres.extensions.types;
}
