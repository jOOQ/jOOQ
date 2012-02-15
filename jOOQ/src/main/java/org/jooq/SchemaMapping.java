/**
 * Copyright (c) 2009-2011, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jooq;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jooq.conf.MappedSchema;
import org.jooq.conf.MappedTable;
import org.jooq.conf.RenderMapping;
import org.jooq.conf.Rendering;
import org.jooq.conf.Settings;
import org.jooq.impl.SchemaImpl;
import org.jooq.impl.TableImpl;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.StringUtils;

/**
 * General mapping of generated artefacts onto run-time substitutes.
 * <p>
 * There are several use cases, when the run-time schema configuration may be
 * different from the compile-time (or code-generation-time) schema
 * configuration. Say, you develop a schema called <code>DEV</code>. It contains
 * a table <code>DEV.T</code>. When you install your database on a productive
 * system, you might have two schemata:
 * <ul>
 * <li><code>PROD</code>: The productive schema. It contains the table
 * <code>PROD.T</code></li>
 * <li><code>BACKUP</code>: The productive backup schema. This schema might be
 * shared with other applications, so you might have table name collisions.
 * Therefore, you'd want to map your table <code>DEV.T</code> onto
 * <code>BACKUP.MY_T</code>
 * </ul>
 * <p>
 * This can be achieved with the <code>SchemaMapping</code>, where you can map
 * schemata and tables, for them to render different names at run-time, than at
 * compile-time.
 *
 * @author Lukas Eder
 * @see <a
 *      href="https://sourceforge.net/apps/trac/jooq/wiki/Manual/ADVANCED/SchemaMapping">https://sourceforge.net/apps/trac/jooq/wiki/Manual/ADVANCED/SchemaMapping</a>
 * @since 1.5.2, 1.6.0
 * @deprecated - 2.0.5 - Use runtime configuration {@link Settings} instead
 */
@Deprecated
public class SchemaMapping implements Serializable {

    /**
     * Generated UID
     */
    private static final long           serialVersionUID  = 8269660159338710470L;
    private static final JooqLogger     log               = JooqLogger.getLogger(SchemaMapping.class);
    private static boolean              loggedDeprecation = false;

    /**
     * The default, unmodifiable mapping that just takes generated schemata
     */
    public static final SchemaMapping   NO_MAPPING        = new SchemaMapping(false);

    /**
     * The underlying mapping for schemata
     */
    private final Map<String, Schema>   schemata          = new HashMap<String, Schema>();

    /**
     * The underlying mapping for tables
     */
    private final Map<String, Table<?>> tables            = new HashMap<String, Table<?>>();

    /**
     * The default schema
     */
    private Schema                      defaultSchema;

    /**
     * Construct an empty mapping
     */
    public SchemaMapping() {
        this(true);
    }

    private SchemaMapping(boolean logDeprecation) {
        if (logDeprecation && !loggedDeprecation) {

            // Log only once
            loggedDeprecation = true;
            log.warn("DEPRECATION", "org.jooq.SchemaMapping is deprecated as of jOOQ 2.0.5. Consider using jOOQ's runtime configuration org.jooq.conf.Settings instead");
        }
    }

    /**
     * Set a schema as the default schema. This results in the supplied schema
     * being omitted in generated SQL.
     * <p>
     * If the supplied mapping has already been added using
     * {@link #add(Schema, Schema)}, then <code>use()</code> has no effect.
     *
     * @param schema the default schema
     */
    public void use(Schema schema) {
        defaultSchema = schema;
    }

    /**
     * Set a schema as the default schema. This results in the supplied schema
     * being omitted in generated SQL.
     * <p>
     * If the supplied mapping has already been added using
     * {@link #add(Schema, Schema)}, then <code>use()</code> has no effect.
     *
     * @param schemaName the default schema
     */
    public void use(String schemaName) {
        defaultSchema = new SchemaImpl(schemaName);
    }

    /**
     * Add schemata to this mapping
     *
     * @param inputSchema The schema known at codegen time to be mapped
     * @param outputSchema The schema configured at run time to be mapped
     */
    public void add(String inputSchema, String outputSchema) {
        add(inputSchema, new SchemaImpl(outputSchema));
    }

    /**
     * Add schemata to this mapping
     *
     * @param inputSchema The schema known at codegen time to be mapped
     * @param outputSchema The schema configured at run time to be mapped
     */
    public void add(String inputSchema, Schema outputSchema) {
        schemata.put(inputSchema, outputSchema);
    }

    /**
     * Add schemata to this mapping
     *
     * @param inputSchema The schema known at codegen time to be mapped
     * @param outputSchema The schema configured at run time to be mapped
     */
    public void add(Schema inputSchema, Schema outputSchema) {
        add(inputSchema.getName(), outputSchema);
    }

    /**
     * Add schemata to this mapping
     *
     * @param inputSchema The schema known at codegen time to be mapped
     * @param outputSchema The schema configured at run time to be mapped
     */
    public void add(Schema inputSchema, String outputSchema) {
        add(inputSchema.getName(), new SchemaImpl(outputSchema));
    }

    /**
     * Add tables to this mapping
     *
     * @param generatedTable The table known at codegen time to be mapped
     * @param configuredTable The table configured at run time to be mapped
     */
    public void add(Table<?> generatedTable, Table<?> configuredTable) {
        tables.put(generatedTable.getName(), configuredTable);
    }

    /**
     * Add tables to this mapping
     *
     * @param generatedTable The table known at codegen time to be mapped
     * @param configuredTableName The table configured at run time to be mapped
     */
    public void add(final Table<?> generatedTable, final String configuredTableName) {

        @SuppressWarnings("serial")
        Table<Record> configuredTable = new TableImpl<Record>(configuredTableName, generatedTable.getSchema()) {{
            for (Field<?> field : generatedTable.getFields()) {
                createField(field.getName(), field.getDataType(), this);
            }
        }};

        add(generatedTable, configuredTable);
    }

    /**
     * Apply mapping to a given schema
     *
     * @param generatedSchema The generated schema to be mapped
     * @return The configured schema
     */
    public Schema map(Schema generatedSchema) {
        Schema result = null;

        if (generatedSchema != null) {
            result = schemata.get(generatedSchema.getName());

            if (result != null) {
                return result;
            }
            else if (generatedSchema.equals(defaultSchema)) {
                return null;
            }
        }

        return generatedSchema;
    }

    /**
     * Apply mapping to a given table
     *
     * @param generatedTable The generated table to be mapped
     * @return The configured table
     */
    public Table<?> map(Table<?> generatedTable) {
        Table<?> result = null;

        if (generatedTable != null) {
            result = tables.get(generatedTable.getName());

            if (result != null) {
                return result;
            }
        }

        return generatedTable;
    }

    /**
     * Synonym for {@link #use(String)}. Added for better interoperability with
     * Spring
     */
    public void setDefaultSchema(String schema) {
        use(schema);
    }

    /**
     * Initialise SchemaMapping. Added for better interoperability with Spring
     */
    public void setSchemaMapping(Map<String, String> schemaMap) {
        for (Entry<String, String> entry : schemaMap.entrySet()) {
            add(new SchemaImpl(entry.getKey()), new SchemaImpl(entry.getValue()));
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("SchemaMapping[");
        String separator = "";

        if (!schemata.isEmpty()) {
            sb.append(separator);
            sb.append("schemata=");
            sb.append(schemata);
            separator = ", ";
        }

        if (defaultSchema != null) {
            sb.append(separator);
            sb.append("use=");
            sb.append(defaultSchema);
            separator = ", ";
        }

        if (!tables.isEmpty()) {
            sb.append(separator);
            sb.append("tables=");
            sb.append(tables);
            separator = ", ";
        }

        sb.append("]");
        return sb.toString();
    }

    /**
     * Convert jOOQ runtime {@link Settings} into the deprecated
     * <code>SchemaMapping</code>
     * <p>
     * This method is for JOOQ INTERNAL USE only. Do not reference directly
     */
    public static SchemaMapping fromSettings(Settings settings) {
        SchemaMapping result = new SchemaMapping(false);

        if (settings != null) {
            Rendering rendering = settings.getRendering();

            if (rendering != null) {
                RenderMapping r = rendering.getRenderMapping();

                if (r != null) {
                    if (!StringUtils.isEmpty(r.getDefaultSchema())) {
                        result.use(r.getDefaultSchema());
                    }

                    for (MappedSchema schema : r.getSchemata()) {
                        result.add(schema.getInput(), schema.getOutput());

                        for (MappedTable table : schema.getTables()) {
                            log.warn("TODO", "Re-implement table mapping for table " + table);
                        }
                    }
                }
            }
        }

        return result;
    }
}
