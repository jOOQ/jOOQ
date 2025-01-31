/**
 * The jOOQ reactor extensions module.
 */
module org.jooq.reactor.extensions {

    // Other jOOQ modules
    requires transitive org.jooq;

    // Nullability annotations for better Kotlin interop
    requires static org.jetbrains.annotations;

    requires transitive reactor.core;

    exports org.jooq.reactor.extensions;
}
