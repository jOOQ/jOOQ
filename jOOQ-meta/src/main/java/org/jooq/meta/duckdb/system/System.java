/*
 * This file is generated by jOOQ.
 */
package org.jooq.meta.duckdb.system;


import java.util.Arrays;
import java.util.List;

import org.jooq.Constants;
import org.jooq.Schema;
import org.jooq.impl.CatalogImpl;
import org.jooq.meta.duckdb.system.information_schema.InformationSchema;
import org.jooq.meta.duckdb.system.main.Main;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class System extends CatalogImpl {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>system</code>
     */
    public static final System SYSTEM = new System();

    /**
     * The schema <code>system.information_schema</code>.
     */
    public final InformationSchema INFORMATION_SCHEMA = InformationSchema.INFORMATION_SCHEMA;

    /**
     * The schema <code>system.main</code>.
     */
    public final Main MAIN = Main.MAIN;

    /**
     * No further instances allowed
     */
    private System() {
        super("system");
    }

    @Override
    public final List<Schema> getSchemas() {
        return Arrays.asList(
            InformationSchema.INFORMATION_SCHEMA,
            Main.MAIN
        );
    }

    /**
     * A reference to the 3.20 minor release of the code generator. If this
     * doesn't compile, it's because the runtime library uses an older minor
     * release, namely: 3.20. You can turn off the generation of this reference
     * by specifying /configuration/generator/generate/jooqVersionReference
     */
    private static final String REQUIRE_RUNTIME_JOOQ_VERSION = Constants.VERSION_3_20;
}
