/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
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

import static org.jooq.tools.StringUtils.isBlank;

import java.io.Serializable;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.JAXB;

import org.jooq.conf.MappedSchema;
import org.jooq.conf.MappedTable;
import org.jooq.conf.RenderMapping;
import org.jooq.conf.Settings;
import org.jooq.conf.SettingsTools;
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
    private static final long                        serialVersionUID  = 8269660159338710470L;
    private static final JooqLogger                  log               = JooqLogger.getLogger(SchemaMapping.class);
    private static volatile boolean                  loggedDeprecation = false;

    private final Configuration                      configuration;
    private volatile transient Map<String, Schema>   schemata;
    private volatile transient Map<String, Table<?>> tables;

    /**
     * Construct a mapping from a {@link Configuration} object
     */
    public SchemaMapping(Configuration configuration) {
        this.configuration = configuration;
    }

    private final RenderMapping mapping() {
        return SettingsTools.getRenderMapping(configuration.settings());
    }

    private final boolean renderSchema() {
        return Boolean.TRUE.equals(configuration.settings().isRenderSchema());
    }

    private static void logDeprecation() {
        if (!loggedDeprecation) {

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
        use(schema.getName());
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
        logDeprecation();

        mapping().setDefaultSchema(schemaName);
    }

    /**
     * Add schemata to this mapping
     *
     * @param inputSchema The schema known at codegen time to be mapped
     * @param outputSchema The schema configured at run time to be mapped
     */
    public void add(String inputSchema, String outputSchema) {
        logDeprecation();

        // Find existing mapped schema
        MappedSchema schema = null;
        for (MappedSchema s : mapping().getSchemata()) {
            if (inputSchema.equals(s.getInput())) {
                schema = s;
                break;
            }
        }

        if (schema == null) {
            schema = new MappedSchema().withInput(inputSchema);
            mapping().getSchemata().add(schema);
        }

        // Add new mapping
        schema.setOutput(outputSchema);
    }

    /**
     * Add schemata to this mapping
     *
     * @param inputSchema The schema known at codegen time to be mapped
     * @param outputSchema The schema configured at run time to be mapped
     */
    public void add(String inputSchema, Schema outputSchema) {
        add(inputSchema, outputSchema.getName());
    }

    /**
     * Add schemata to this mapping
     *
     * @param inputSchema The schema known at codegen time to be mapped
     * @param outputSchema The schema configured at run time to be mapped
     */
    public void add(Schema inputSchema, Schema outputSchema) {
        add(inputSchema.getName(), outputSchema.getName());
    }

    /**
     * Add schemata to this mapping
     *
     * @param inputSchema The schema known at codegen time to be mapped
     * @param outputSchema The schema configured at run time to be mapped
     */
    public void add(Schema inputSchema, String outputSchema) {
        add(inputSchema.getName(), outputSchema);
    }

    /**
     * Add tables to this mapping
     *
     * @param inputTable The table known at codegen time to be mapped
     * @param outputTable The table configured at run time to be mapped
     */
    public void add(Table<?> inputTable, Table<?> outputTable) {
        add(inputTable, outputTable.getName());
    }

    /**
     * Add tables to this mapping
     *
     * @param inputTable The table known at codegen time to be mapped
     * @param outputTable The table configured at run time to be mapped
     */
    public void add(final Table<?> inputTable, final String outputTable) {
        logDeprecation();

        // Try to find a pre-existing schema mapping in the settings
        MappedSchema schema = null;
        MappedTable table = null;

        for (MappedSchema s : mapping().getSchemata()) {
            if (inputTable.getSchema().getName().equals(s.getInput())) {

                // Find existing mapped table
                tableLoop:
                for (MappedTable t : s.getTables()) {
                    if (inputTable.getName().equals(t.getInput())) {
                        table = t;
                        break tableLoop;
                    }
                }

                schema = s;
                break;
            }
        }

        if (schema == null) {
            schema = new MappedSchema().withInput(inputTable.getSchema().getName());
            mapping().getSchemata().add(schema);
        }

        if (table == null) {
            table = new MappedTable().withInput(inputTable.getName());
            schema.getTables().add(table);
        }

        // Add new mapping
        table.setOutput(outputTable);
    }

    /**
     * Apply mapping to a given schema
     *
     * @param schema The schema to be mapped
     * @return The configured schema
     */
    public Schema map(Schema schema) {

        // [#1774] The default Settings render schema flag takes precedence over
        // The DefaultConfiguration's ignoreMapping flag!
        if (!renderSchema()) return null;

        Schema result = null;
        if (schema != null) {
            String schemaName = schema.getName();

            // [#2089] DefaultSchema has an empty schema name
            if (!StringUtils.isEmpty(schemaName)) {

                // Lazy initialise schema mapping
                if (!getSchemata().containsKey(schemaName)) {

                    // [#1857] thread-safe lazy initialisation for those users who
                    // want to use a Configuration and dependent objects in a "thread-safe" manner
                    synchronized (this) {
                        if (!getSchemata().containsKey(schemaName)) {
                            Schema mapped = schema;

                            for (MappedSchema s : mapping().getSchemata()) {

                                // A configured mapping was found, add a renamed schema
                                if (schemaName.equals(s.getInput())) {

                                    // Ignore self-mappings and void-mappings
                                    if (!isBlank(s.getOutput()) && !s.getOutput().equals(s.getInput())) {
                                        mapped = new RenamedSchema(schema, s.getOutput());
                                    }

                                    break;
                                }
                            }

                            // Add mapped schema or self if no mapping was found
                            getSchemata().put(schemaName, mapped);
                        }
                    }
                }

                result = getSchemata().get(schemaName);

                // The configured default schema is mapped to "null". This prevents
                // it from being rendered to SQL
                if (result.getName().equals(mapping().getDefaultSchema())) {
                    result = null;
                }
            }
        }

        return result;
    }

    /**
     * Apply mapping to a given table
     *
     * @param table The generated table to be mapped
     * @return The configured table
     */
    @SuppressWarnings("unchecked")
    public <R extends Record> Table<R> map(Table<R> table) {
        Table<R> result = null;

        if (table != null) {
            Schema schema = table.getSchema();

            // [#1189] Schema can be null in SQLite
            // [#2089] DefaultSchema have empty schema names
            // [#1186] TODO: replace this by calling table.getQualifiedName()
            String schemaName = (schema == null) ? "" : schema.getName();
            String tableName = table.getName();
            String key = (schema == null || StringUtils.isEmpty(schemaName)) ? tableName : (schemaName + "." + tableName);

            // Lazy initialise table mapping
            if (!getTables().containsKey(key)) {

                // [#1857] thread-safe lazy initialisation for those users who
                // want to use Configuration and dependent objects in a "thread-safe" manner
                synchronized (this) {
                    if (!getTables().containsKey(key)) {
                        Table<R> mapped = table;

                        schemaLoop:
                        for (MappedSchema s : mapping().getSchemata()) {
                            if (schemaName.equals(s.getInput())) {
                                for (MappedTable t : s.getTables()) {

                                    // A configured mapping was found, add a renamed table
                                    if (tableName.equals(t.getInput())) {

                                        // Ignore self-mappings and void-mappings
                                        if (!isBlank(t.getOutput()) && !t.getOutput().equals(t.getInput())) {
                                            mapped = new RenamedTable<R>(table, t.getOutput());
                                        }

                                        break schemaLoop;
                                    }
                                }
                            }
                        }

                        // Add mapped table or self if no mapping was found
                        getTables().put(key, mapped);
                    }
                }
            }

            result = (Table<R>) getTables().get(key);
        }

        return result;
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
            add(entry.getKey(), entry.getValue());
        }
    }

    private final Map<String, Schema> getSchemata() {
        if (schemata == null) {

            // [#1857] thread-safe lazy initialisation for those users who
            // want to use Configuration and dependent objects in a "thread-safe" manner
            synchronized (this) {
                if (schemata == null) {
                    schemata = new HashMap<String, Schema>();
                }
            }
        }
        return schemata;
    }

    private final Map<String, Table<?>> getTables() {
        if (tables == null) {

            // [#1857] thread-safe lazy initialisation for those users who
            // want to use Configuration and dependent objects in a "thread-safe" manner
            synchronized (this) {
                if (tables == null) {
                    tables = new HashMap<String, Table<?>>();
                }
            }
        }
        return tables;
    }

    // ------------------------------------------------------------------------
    // Object API
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        StringWriter writer = new StringWriter();
        JAXB.marshal(mapping(), writer);
        return writer.toString();
    }
}
