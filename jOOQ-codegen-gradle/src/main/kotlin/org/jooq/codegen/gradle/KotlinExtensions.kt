package org.jooq.codegen.gradle

import org.jooq.meta.jaxb.Configuration

fun CodegenPluginExtension.configuration(block: Configuration.() -> Unit) {
    val c = Configuration()
    block(c)
    this.configuration0(c)
}

fun NamedConfiguration.configuration(block: Configuration.() -> Unit) {
    val c = Configuration()
    block(c)
    this.configuration0(c)
}