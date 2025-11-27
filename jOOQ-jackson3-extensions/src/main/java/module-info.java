/**
 * The jOOQ Jackson extensions module.
 */
module org.jooq.jackson3.extensions {

    // Other jOOQ modules
    requires transitive org.jooq;
    requires tools.jackson.databind;
    requires tools.jackson.module.kotlin;
    requires tools.jackson.core;

    // Nullability annotations for better Kotlin interop
    requires static org.jetbrains.annotations;

    exports org.jooq.jackson3.extensions.converters;
}
