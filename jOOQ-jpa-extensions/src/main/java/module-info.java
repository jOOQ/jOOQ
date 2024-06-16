/**
 * The jOOQ beans extensions module.
 */
module org.jooq.beans.extensions {

    // Other jOOQ modules
    requires transitive org.jooq;

    // Nullability annotations for better Kotlin interop
    requires static org.jetbrains.annotations;

    // JPA annotations
    requires static jakarta.persistence;

    exports org.jooq.jpa.extensions;
}
