/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package org.jooq;

import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.schema;
import static org.jooq.tools.StringUtils.isBlank;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jooq.conf.MappedCatalog;
import org.jooq.conf.MappedSchema;
import org.jooq.conf.MappedTable;
import org.jooq.conf.RenderMapping;
import org.jooq.conf.Settings;
import org.jooq.conf.SettingsTools;
import org.jooq.impl.DSL;
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
    private volatile transient Map<String, Catalog>  catalogs;
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

    private final boolean renderCatalog() {
        return Boolean.TRUE.equals(configuration.settings().isRenderCatalog());
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

    public Catalog map(Catalog catalog) {

        // [#1774] [#4795] The default Settings render schema flag takes
        // precedence over the DefaultConfiguration's ignoreMapping flag!
        if (!renderCatalog()) return null;
        // Don't map an already mapped catalog again
        else if (catalog instanceof RenamedCatalog) return catalog;

        Catalog result = catalog;
        if (result == null)
            result = DSL.catalog(name(""));

        // [#2089] DefaultCatalog has an empty schema name
        // But we're mapping those names as well
        String catalogName = result.getName();

        // [#4642] Don't initialise catalog mapping if not necessary
        if (!mapping().getCatalogs().isEmpty()) {

            // Lazy initialise catalog mapping
            if (!getCatalogs().containsKey(catalogName)) {

                // [#1857] thread-safe lazy initialisation for those users who
                // want to use a Configuration and dependent objects in a "thread-safe" manner
                synchronized (this) {
                    if (!getCatalogs().containsKey(catalogName)) {
                        for (MappedCatalog c : mapping().getCatalogs()) {

                            // A configured mapping was found, add a renamed catalog
                            if (matches(c, catalogName)) {

                                // Ignore self-mappings and void-mappings
                                if (!isBlank(c.getOutput()))
                                    if (c.getInput() != null && !c.getOutput().equals(catalogName))
                                        result = new RenamedCatalog(result, c.getOutput());
                                    else if (c.getInputExpression() != null)
                                        result = new RenamedCatalog(result, c.getInputExpression().matcher(catalogName).replaceAll(c.getOutput()));

                                break;
                            }
                        }

                        // Add mapped catalog or self if no mapping was found
                        getCatalogs().put(catalogName, result);
                    }
                }
            }

            result = getCatalogs().get(catalogName);
        }

        // The configured default catalog is mapped to "null". This prevents
        // it from being rendered to SQL
        if ("".equals(result.getName()) || result.getName().equals(mapping().getDefaultCatalog()))
            result = null;

        return result;
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
        // [#9708] Don't map an already mapped schema again
        else if (schema instanceof RenamedSchema) return schema;

        Schema result = schema;
        if (result == null)
            result = schema(name(""));

        Catalog catalog = result.getCatalog();
        if (catalog == null)
            catalog = DSL.catalog(name(""));

        // [#2089] DefaultSchema has an empty schema name
        // [#7498] But we're mapping those names as well
        String catalogName = catalog.getName();
        String schemaName = result.getName();
        String key = StringUtils.isEmpty(catalogName) ? schemaName : catalogName + '.' + schemaName;

        // [#4642] Don't initialise schema mapping if not necessary
        if (!mapping().getSchemata().isEmpty() || !mapping().getCatalogs().isEmpty()) {

            // Lazy initialise schema mapping
            if (!getSchemata().containsKey(key)) {

                // [#1857] thread-safe lazy initialisation for those users who
                // want to use a Configuration and dependent objects in a "thread-safe" manner
                synchronized (this) {
                    if (!getSchemata().containsKey(key)) {

                        catalogLoop:
                        for (MappedCatalog c : mapping().getCatalogs()) {
                            if (matches(c, catalogName)) {
                                for (MappedSchema s : c.getSchemata()) {
                                    if (matches(s, schemaName)) {

                                        // Ignore self-mappings and void-mappings
                                        if (!isBlank(s.getOutput()))
                                            if (s.getInput() != null && !s.getOutput().equals(schemaName))
                                                result = new RenamedSchema(map(catalog), result, s.getOutput());
                                            else if (s.getInputExpression() != null)
                                                result = new RenamedSchema(map(catalog), result, s.getInputExpression().matcher(schemaName).replaceAll(s.getOutput()));

                                        break catalogLoop;
                                    }
                                }

                                // [#7498] Even without schema mapping configuration, we may still need to map the catalog
                                result = new RenamedSchema(map(catalog), result, schemaName);
                                break catalogLoop;
                            }
                        }

                        if (!(result instanceof RenamedSchema))
                            for (MappedSchema s : mapping().getSchemata()) {

                                // A configured mapping was found, add a renamed schema
                                if (matches(s, schemaName)) {

                                    // Ignore self-mappings and void-mappings
                                    if (!isBlank(s.getOutput()))
                                        if (s.getInput() != null && !s.getOutput().equals(schemaName))
                                            result = new RenamedSchema(catalog, result, s.getOutput());
                                        else if (s.getInputExpression() != null)
                                            result = new RenamedSchema(catalog, result, s.getInputExpression().matcher(schemaName).replaceAll(s.getOutput()));

                                    break;
                                }
                            }

                        // Add mapped schema or self if no mapping was found
                        getSchemata().put(key, result);
                    }
                }
            }

            result = getSchemata().get(key);
        }

        // The configured default schema is mapped to "null". This prevents
        // it from being rendered to SQL
        if ("".equals(result.getName())
            || result.getName().equals(mapping().getDefaultSchema())
                && (result.getCatalog() == null
                    || "".equals(result.getCatalog().getName())
                    || result.getCatalog().getName().equals(mapping().getDefaultCatalog())))
            result = null;

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
        Table<R> result = table;

        // [#4652] Don't initialise table mapping if not necessary
        if (result != null && (!mapping().getSchemata().isEmpty() || !mapping().getCatalogs().isEmpty())) {
            Catalog catalog = result.getCatalog();
            Schema schema = result.getSchema();

            // [#1189] Schema can be null in SQLite
            // [#2089] DefaultSchema have empty schema names
            // [#1186] TODO: replace this by calling table.getQualifiedName()
            if (catalog == null)
                catalog = DSL.catalog(name(""));
            if (schema == null)
                schema = schema(name(""));

            String catalogName = catalog.getName();
            String schemaName = schema.getName();
            String tableName = result.getName();
            String key = StringUtils.isEmpty(catalogName) ?
                (StringUtils.isEmpty(schemaName) ? tableName : (schemaName + "." + tableName))
                : (catalogName + '.' + schemaName + '.' + tableName);

            // Lazy initialise table mapping
            if (!getTables().containsKey(key)) {

                // [#1857] thread-safe lazy initialisation for those users who
                // want to use Configuration and dependent objects in a "thread-safe" manner
                synchronized (this) {
                    if (!getTables().containsKey(key)) {

                        catalogLoop:
                        for (MappedCatalog c : mapping().getCatalogs()) {
                            if (matches(c, catalogName)) {
                                for (MappedSchema s : c.getSchemata()) {
                                    if (matches(s, schemaName)) {
                                        for (MappedTable t : s.getTables()) {

                                            // A configured mapping was found, add a renamed table
                                            if (matches(t, tableName)) {

                                                // Ignore self-mappings and void-mappings
                                                if (!isBlank(t.getOutput()))
                                                    if (t.getInput() != null && !t.getOutput().equals(tableName))
                                                        result = new RenamedTable<>(map(schema), result, t.getOutput());
                                                    else if (t.getInputExpression() != null)
                                                        result = new RenamedTable<>(map(schema), result, t.getInputExpression().matcher(tableName).replaceAll(t.getOutput()));

                                                break catalogLoop;
                                            }
                                        }
                                    }
                                }

                                // [#7498] Even without table mapping configuration, we may still need to map the schema
                                result = new RenamedTable<>(map(schema), result, tableName);
                                break catalogLoop;
                            }
                        }

                        if (!(result instanceof RenamedTable))
                            schemaLoop:
                            for (MappedSchema s : mapping().getSchemata()) {
                                if (matches(s, schemaName)) {
                                    for (MappedTable t : s.getTables()) {

                                        // A configured mapping was found, add a renamed table
                                        if (matches(t, tableName)) {

                                            // Ignore self-mappings and void-mappings
                                            if (!isBlank(t.getOutput()))
                                                if (t.getInput() != null && !t.getOutput().equals(tableName))
                                                    result = new RenamedTable<>(map(schema), result, t.getOutput());
                                                else if (t.getInputExpression() != null)
                                                    result = new RenamedTable<>(map(schema), result, t.getInputExpression().matcher(tableName).replaceAll(t.getOutput()));

                                            break schemaLoop;
                                        }
                                    }

                                    // [#7498] Even without table mapping configuration, we may still need to map the schema
                                    result = new RenamedTable<>(map(schema), result, tableName);
                                    break schemaLoop;
                                }
                            }

                        // Add mapped table or self if no mapping was found
                        getTables().put(key, result);
                    }
                }
            }

            result = (Table<R>) getTables().get(key);
        }

        return result;
    }

    private final boolean matches(MappedCatalog c, String catalogName) {
        return (c.getInput() != null && catalogName.equals(c.getInput()))
            || (c.getInputExpression() != null && c.getInputExpression().matcher(catalogName).matches());
    }

    private final boolean matches(MappedSchema s, String schemaName) {
        return (s.getInput() != null && schemaName.equals(s.getInput()))
            || (s.getInputExpression() != null && s.getInputExpression().matcher(schemaName).matches());
    }

    private final boolean matches(MappedTable t, String tableName) {
        return (t.getInput() != null && tableName.equals(t.getInput()))
            || (t.getInputExpression() != null && t.getInputExpression().matcher(tableName).matches());
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

    private final Map<String, Catalog> getCatalogs() {
        if (catalogs == null) {

            // [#1857] thread-safe lazy initialisation for those users who
            // want to use Configuration and dependent objects in a "thread-safe" manner
            synchronized (this) {
                if (catalogs == null) {
                    catalogs = new HashMap<>();
                }
            }
        }
        return catalogs;
    }

    private final Map<String, Schema> getSchemata() {
        if (schemata == null) {

            // [#1857] thread-safe lazy initialisation for those users who
            // want to use Configuration and dependent objects in a "thread-safe" manner
            synchronized (this) {
                if (schemata == null) {
                    schemata = new HashMap<>();
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
                    tables = new HashMap<>();
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
        return "" + mapping();
    }
}
