/**
 * The jOOQ beans extensions module.
 */
module org.jooq.beans.extensions {

    // Other jOOQ modules
    requires transitive org.jooq;

    // Nullability annotations for better Kotlin interop
    requires static org.jetbrains.annotations;

    // The DefaultRecordMapper makes use of JavaBeans utilities, including:
    // - Support for ConstructorProperties
    requires transitive java.desktop;
}
