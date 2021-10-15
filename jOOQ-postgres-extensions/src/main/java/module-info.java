/**
 * The jOOQ postgres extensions module.
 */
module org.jooq.codegen {

    // Other jOOQ modules
    requires transitive org.jooq;

    // Nullability annotations for better Kotlin interop
    requires static org.jetbrains.annotations;

    // Vendor specific JDBC drivers
    requires static org.postgresql.jdbc;

}
