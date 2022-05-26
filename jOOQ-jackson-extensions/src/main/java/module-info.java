/**
 * The jOOQ Jackson extensions module.
 */
module org.jooq.jackson.extensions {

    // Other jOOQ modules
    requires transitive org.jooq;
    requires com.fasterxml.jackson.databind;

    // Nullability annotations for better Kotlin interop
    requires static org.jetbrains.annotations;

    exports org.jooq.jackson.extensions.converters;
}
