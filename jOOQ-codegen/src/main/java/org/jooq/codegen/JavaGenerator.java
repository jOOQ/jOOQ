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
package org.jooq.codegen;


import static java.util.Arrays.asList;
// ...
// ...
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SortOrder.DESC;
import static org.jooq.codegen.AbstractGenerator.Language.JAVA;
import static org.jooq.codegen.AbstractGenerator.Language.SCALA;
import static org.jooq.codegen.GenerationUtil.convertToIdentifier;
import static org.jooq.impl.DSL.name;
import static org.jooq.meta.AbstractTypedElementDefinition.getDataType;
import static org.jooq.tools.StringUtils.defaultIfBlank;
import static org.jooq.tools.StringUtils.defaultString;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Modifier;
import java.lang.reflect.TypeVariable;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.DatatypeConverter;

import org.jooq.AggregateFunction;
import org.jooq.Catalog;
import org.jooq.Check;
import org.jooq.Configuration;
import org.jooq.Constants;
import org.jooq.DataType;
import org.jooq.EnumType;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Index;
// ...
import org.jooq.Name;
import org.jooq.OrderField;
import org.jooq.Package;
import org.jooq.Parameter;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Row;
import org.jooq.Schema;
import org.jooq.Sequence;
import org.jooq.SortOrder;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UDT;
import org.jooq.UDTField;
import org.jooq.UniqueKey;
import org.jooq.codegen.GeneratorStrategy.Mode;
import org.jooq.exception.SQLDialectNotSupportedException;
import org.jooq.impl.AbstractRoutine;
// ...
import org.jooq.impl.CatalogImpl;
import org.jooq.impl.DAOImpl;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultDataType;
import org.jooq.impl.EmbeddableRecordImpl;
import org.jooq.impl.Internal;
import org.jooq.impl.PackageImpl;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.SchemaImpl;
import org.jooq.impl.TableImpl;
import org.jooq.impl.TableRecordImpl;
import org.jooq.impl.UDTImpl;
import org.jooq.impl.UDTRecordImpl;
import org.jooq.impl.UpdatableRecordImpl;
import org.jooq.meta.AbstractTypedElementDefinition;
import org.jooq.meta.ArrayDefinition;
import org.jooq.meta.AttributeDefinition;
import org.jooq.meta.CatalogDefinition;
import org.jooq.meta.CheckConstraintDefinition;
import org.jooq.meta.ColumnDefinition;
import org.jooq.meta.DataTypeDefinition;
import org.jooq.meta.Database;
import org.jooq.meta.DefaultDataTypeDefinition;
import org.jooq.meta.Definition;
import org.jooq.meta.DomainDefinition;
import org.jooq.meta.EmbeddableColumnDefinition;
import org.jooq.meta.EmbeddableDefinition;
import org.jooq.meta.EnumDefinition;
import org.jooq.meta.ForeignKeyDefinition;
import org.jooq.meta.IdentityDefinition;
import org.jooq.meta.IndexColumnDefinition;
import org.jooq.meta.IndexDefinition;
import org.jooq.meta.JavaTypeResolver;
import org.jooq.meta.PackageDefinition;
import org.jooq.meta.ParameterDefinition;
import org.jooq.meta.RoutineDefinition;
import org.jooq.meta.SchemaDefinition;
import org.jooq.meta.SequenceDefinition;
import org.jooq.meta.TableDefinition;
import org.jooq.meta.TypedElementDefinition;
import org.jooq.meta.UDTDefinition;
import org.jooq.meta.UniqueKeyDefinition;
import org.jooq.meta.jaxb.GeneratedAnnotationType;
// ...
// ...
// ...
// ...
import org.jooq.meta.postgres.PostgresDatabase;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.StopWatch;
import org.jooq.tools.StringUtils;
import org.jooq.tools.reflect.Reflect;
import org.jooq.tools.reflect.ReflectException;
// ...
// ...


/**
 * A default implementation for code generation.
 * <p>
 * Replace this code with your own logic, if you need your database schema
 * represented in a different way.
 * <p>
 * Note that you can also extend this class to generate POJO's or other stuff
 * entirely independent of jOOQ.
 *
 * @author Lukas Eder
 */
public class JavaGenerator extends AbstractGenerator {

    private static final JooqLogger               log                          = JooqLogger.getLogger(JavaGenerator.class);

    /**
     * The Javadoc to be used for private constructors
     */
    private static final String                   NO_FURTHER_INSTANCES_ALLOWED = "No further instances allowed";

    /**
     * [#1459] Prevent large static initialisers by splitting nested classes
     */
    private static final int                      INITIALISER_SIZE             = 500;

    /**
     * [#4429] A map providing access to SQLDataType member literals
     */
    private static final Map<DataType<?>, String> SQLDATATYPE_LITERAL_LOOKUP;

    /**
     * [#6411] A set providing access to SQLDataTypes that can have length.
     */
    private static final Set<String>              SQLDATATYPE_WITH_LENGTH;

    /**
     * [#6411] A set providing access to SQLDataTypes that can have precision
     * (and scale).
     */
    private static final Set<String>              SQLDATATYPE_WITH_PRECISION;

    /**
     * An overall stop watch to measure the speed of source code generation
     */
    private final StopWatch                       watch                        = new StopWatch();

    /**
     * The underlying database of this generator
     */
    private Database                              database;

    /**
     * The code generation date, if needed.
     */
    private String                                isoDate;

    /**
     * The cached schema version numbers.
     */
    private Map<SchemaDefinition, String>         schemaVersions;

    /**
     * The cached catalog version numbers.
     */
    private Map<CatalogDefinition, String>        catalogVersions;

    /**
     * All files modified by this generator.
     */
    private Set<File>                             files                        = new LinkedHashSet<>();

    /**
     * These directories were not modified by this generator, but flagged as not
     * for removal (e.g. because of {@link #schemaVersions} or
     * {@link #catalogVersions}).
     */
    private Set<File>                             directoriesNotForRemoval     = new LinkedHashSet<>();

    private final boolean                         scala;
    private final String                          tokenVoid;
    private final Files                           fileCache;

    static {
        SQLDATATYPE_LITERAL_LOOKUP = new IdentityHashMap<>();
        SQLDATATYPE_WITH_LENGTH = new HashSet<>();
        SQLDATATYPE_WITH_PRECISION = new HashSet<>();

        try {
            for (java.lang.reflect.Field f : SQLDataType.class.getFields()) {
                if (Modifier.isPublic(f.getModifiers()) &&
                    Modifier.isStatic(f.getModifiers()) &&
                    Modifier.isFinal(f.getModifiers()))
                    SQLDATATYPE_LITERAL_LOOKUP.put((DataType<?>) f.get(SQLDataType.class), f.getName());
            }

            for (java.lang.reflect.Method m : SQLDataType.class.getMethods()) {
                if (Modifier.isPublic(m.getModifiers()) &&
                    Modifier.isStatic(m.getModifiers()) &&
                    m.getParameterTypes().length == 2)
                    SQLDATATYPE_WITH_PRECISION.add(m.getName());
            }

            for (java.lang.reflect.Method m : SQLDataType.class.getMethods()) {
                if (Modifier.isPublic(m.getModifiers()) &&
                    Modifier.isStatic(m.getModifiers()) &&
                    m.getParameterTypes().length == 1 &&
                    !SQLDATATYPE_WITH_PRECISION.contains(m.getName()))
                    SQLDATATYPE_WITH_LENGTH.add(m.getName());
            }
        }
        catch (Exception e) {
            log.warn(e);
        }
    }

    public JavaGenerator() {
        this(JAVA);
    }

    JavaGenerator(Language language) {
        super(language);

        this.scala = (language == SCALA);
        this.tokenVoid = (scala ? "Unit" : "void");
        this.fileCache = new Files();
    }

    @Override
    public final void generate(Database db) {
        this.isoDate = DatatypeConverter.printDateTime(Calendar.getInstance(TimeZone.getTimeZone("UTC")));
        this.schemaVersions = new LinkedHashMap<>();
        this.catalogVersions = new LinkedHashMap<>();

        this.database = db;
        this.database.addFilter(new AvoidAmbiguousClassesFilter());
        this.database.setIncludeRelations(generateRelations());
        this.database.setTableValuedFunctions(generateTableValuedFunctions());

        logDatabaseParameters(db);
        log.info("");
        log.info("JavaGenerator parameters");
        log.info("----------------------------------------------------------");
        log.info("  annotations (generated)", generateGeneratedAnnotation()
            + ((!generateGeneratedAnnotation && (useSchemaVersionProvider || useCatalogVersionProvider)) ?
                " (forced to true because of <schemaVersionProvider/> or <catalogVersionProvider/>)" : ""));
        log.info("  annotations (JPA: any)", generateJPAAnnotations());
        log.info("  annotations (JPA: version)", generateJPAVersion());
        log.info("  annotations (validation)", generateValidationAnnotations());
        log.info("  comments", generateComments());
        log.info("  comments on attributes", generateCommentsOnAttributes());
        log.info("  comments on catalogs", generateCommentsOnCatalogs());
        log.info("  comments on columns", generateCommentsOnColumns());
        log.info("  comments on keys", generateCommentsOnKeys());
        log.info("  comments on links", generateCommentsOnLinks());
        log.info("  comments on packages", generateCommentsOnPackages());
        log.info("  comments on parameters", generateCommentsOnParameters());
        log.info("  comments on queues", generateCommentsOnQueues());
        log.info("  comments on routines", generateCommentsOnRoutines());
        log.info("  comments on schemas", generateCommentsOnSchemas());
        log.info("  comments on sequences", generateCommentsOnSequences());
        log.info("  comments on tables", generateCommentsOnTables());
        log.info("  comments on udts", generateCommentsOnUDTs());
        log.info("  daos", generateDaos());
        log.info("  deprecated code", generateDeprecated());
        log.info("  global references (any)", generateGlobalObjectReferences());
        log.info("  global references (catalogs)", generateGlobalCatalogReferences());
        log.info("  global references (keys)", generateGlobalKeyReferences());
        log.info("  global references (links)", generateGlobalLinkReferences());
        log.info("  global references (queues)", generateGlobalQueueReferences());
        log.info("  global references (routines)", generateGlobalRoutineReferences());
        log.info("  global references (schemas)", generateGlobalSchemaReferences());
        log.info("  global references (sequences)", generateGlobalSequenceReferences());
        log.info("  global references (tables)", generateGlobalTableReferences());
        log.info("  global references (udts)", generateGlobalUDTReferences());
        log.info("  indexes", generateIndexes());
        log.info("  instance fields", generateInstanceFields());
        log.info("  interfaces", generateInterfaces()
              + ((!generateInterfaces && generateImmutableInterfaces) ? " (forced to true because of <immutableInterfaces/>)" : ""));
        log.info("  interfaces (immutable)", generateInterfaces());
        log.info("  javadoc", generateJavadoc());
        log.info("  keys", generateKeys());
        log.info("  links", generateLinks());
        log.info("  pojos", generatePojos()
              + ((!generatePojos && generateDaos) ? " (forced to true because of <daos/>)" :
                ((!generatePojos && generateImmutablePojos) ? " (forced to true because of <immutablePojos/>)" : "")));
        log.info("  pojos (immutable)", generateImmutablePojos());
        log.info("  queues", generateQueues());
        log.info("  records", generateRecords()
              + ((!generateRecords && generateDaos) ? " (forced to true because of <daos/>)" : ""));
        log.info("  routines", generateRoutines());
        log.info("  sequences", generateSequences());
        log.info("  sequenceFlags", generateSequenceFlags());
        log.info("  table-valued functions", generateTableValuedFunctions());
        log.info("  tables", generateTables()
              + ((!generateTables && generateRecords) ? " (forced to true because of <records/>)" :
                ((!generateTables && generateDaos) ? " (forced to true because of <daos/>)" : "")));
        log.info("  udts", generateUDTs());
        log.info("  relations", generateRelations()
            + ((!generateRelations && generateTables) ? " (forced to true because of <tables/>)" :
              ((!generateRelations && generateDaos) ? " (forced to true because of <daos/>)" : "")));
        log.info("----------------------------------------------------------");

        if (!generateInstanceFields()) {
            log.warn("");
            log.warn("Deprecation warnings");
            log.warn("----------------------------------------------------------");
            log.warn("  <generateInstanceFields/> = false is deprecated! Please adapt your configuration.");
        }

        log.info("");
        logGenerationRemarks(db);

        log.info("");
        log.info("----------------------------------------------------------");

        // ----------------------------------------------------------------------
        // XXX Generating catalogs
        // ----------------------------------------------------------------------
        log.info("Generating catalogs", "Total: " + database.getCatalogs().size());
        for (CatalogDefinition catalog : database.getCatalogs()) {
            try {
                if (generateCatalogIfEmpty(catalog))
                    generate(catalog);
                else
                    log.info("Excluding empty catalog", catalog);
            }
            catch (Exception e) {
                throw new GeneratorException("Error generating code for catalog " + catalog, e);
            }
        }

        // [#5556] Clean up common parent directory
        log.info("Removing excess files");
        empty(getStrategy().getFileRoot(), (scala ? ".scala" : ".java"), files, directoriesNotForRemoval);
        directoriesNotForRemoval.clear();
        files.clear();
    }

    private boolean generateCatalogIfEmpty(CatalogDefinition catalog) {
        if (generateEmptyCatalogs())
            return true;

        List<SchemaDefinition> schemas = catalog.getSchemata();
        if (schemas.isEmpty())
            return false;

        for (SchemaDefinition schema : schemas)
            if (generateSchemaIfEmpty(schema))
                return true;

        return false;
    }

    private final boolean generateSchemaIfEmpty(SchemaDefinition schema) {
        if (generateEmptySchemas())
            return true;

        if (database.getArrays(schema).isEmpty()
            && database.getEnums(schema).isEmpty()
            && database.getPackages(schema).isEmpty()
            && database.getRoutines(schema).isEmpty()
            && database.getSequences(schema).isEmpty()
            && database.getTables(schema).isEmpty()
            && database.getUDTs(schema).isEmpty())
            return false;

        return true;
    }

    private void generate(CatalogDefinition catalog) {
        String newVersion = catalog.getDatabase().getCatalogVersionProvider().version(catalog);

        if (StringUtils.isBlank(newVersion)) {
            log.info("No schema version is applied for catalog " + catalog.getInputName() + ". Regenerating.");
        }
        else {
            catalogVersions.put(catalog, newVersion);
            String oldVersion = readVersion(getFile(catalog), "catalog");

            if (StringUtils.isBlank(oldVersion)) {
                log.info("No previous version available for catalog " + catalog.getInputName() + ". Regenerating.");
            }
            else if (!oldVersion.equals(newVersion)) {
                log.info("Existing version " + oldVersion + " is not up to date with " + newVersion + " for catalog " + catalog.getInputName() + ". Regenerating.");
            }
            else {
                log.info("Existing version " + oldVersion + " is up to date with " + newVersion + " for catalog " + catalog.getInputName() + ". Ignoring catalog.");

                // [#5614] If a catalog is not regenerated, we must flag it as "not for removal", because its contents
                //         will not be listed in the files directory.
                directoriesNotForRemoval.add(getFile(catalog).getParentFile());
                return;
            }
        }

        generateCatalog(catalog);

        log.info("Generating schemata", "Total: " + catalog.getSchemata().size());
        for (SchemaDefinition schema : catalog.getSchemata()) {
            try {
                if (generateSchemaIfEmpty(schema))
                    generate(schema);
                else
                    log.info("Excluding empty schema", schema);
            }
            catch (Exception e) {
                throw new GeneratorException("Error generating code for schema " + schema, e);
            }
        }
    }

    private void generate(SchemaDefinition schema) {
        String newVersion = schema.getDatabase().getSchemaVersionProvider().version(schema);

        if (StringUtils.isBlank(newVersion)) {
            log.info("No schema version is applied for schema " + schema.getInputName() + ". Regenerating.");
        }
        else {
            schemaVersions.put(schema, newVersion);
            String oldVersion = readVersion(getFile(schema), "schema");

            if (StringUtils.isBlank(oldVersion)) {
                log.info("No previous version available for schema " + schema.getInputName() + ". Regenerating.");
            }
            else if (!oldVersion.equals(newVersion)) {
                log.info("Existing version " + oldVersion + " is not up to date with " + newVersion + " for schema " + schema.getInputName() + ". Regenerating.");
            }
            else {
                log.info("Existing version " + oldVersion + " is up to date with " + newVersion + " for schema " + schema.getInputName() + ". Ignoring schema.");

                // [#5614] If a schema is not regenerated, we must flag it as "not for removal", because its contents
                //         will not be listed in the files directory.
                directoriesNotForRemoval.add(getFile(schema).getParentFile());
                return;
            }
        }

        // ----------------------------------------------------------------------
        // XXX Initialising
        // ----------------------------------------------------------------------
        generateSchema(schema);

        if (generateGlobalSequenceReferences() && database.getSequences(schema).size() > 0) {
            generateSequences(schema);
        }

        if (generateTables() && database.getTables(schema).size() > 0) {
            generateTables(schema);
        }

        if (generateEmbeddables() && database.getEmbeddables(schema).size() > 0) {
            generateEmbeddables(schema);
        }

        if (generatePojos() && database.getTables(schema).size() > 0) {
            generatePojos(schema);
        }

        if (generateDaos() && database.getTables(schema).size() > 0) {
            generateDaos(schema);
        }

        if (generateGlobalTableReferences() && database.getTables(schema).size() > 0) {
            generateTableReferences(schema);
        }

        if (generateGlobalKeyReferences() && generateRelations() && database.getTables(schema).size() > 0) {
            generateRelations(schema);
        }

        if (generateIndexes() && database.getTables(schema).size() > 0) {
            generateIndexes(schema);
        }

        if (generateRecords() && database.getTables(schema).size() > 0) {
            generateRecords(schema);
        }

        if (generateInterfaces() && database.getTables(schema).size() > 0) {
            generateInterfaces(schema);
        }

        if (generateUDTs() && database.getUDTs(schema).size() > 0) {
            generateUDTs(schema);
        }

        if (generatePojos() && database.getUDTs(schema).size() > 0) {
            generateUDTPojos(schema);
        }

        if (generateUDTs() && generateRecords() && database.getUDTs(schema).size() > 0) {
            generateUDTRecords(schema);
        }

        if (generateInterfaces() && database.getUDTs(schema).size() > 0) {
            generateUDTInterfaces(schema);
        }

        if (generateUDTs() && generateRoutines() && database.getUDTs(schema).size() > 0) {
            generateUDTRoutines(schema);
        }

        if (generateGlobalUDTReferences() && database.getUDTs(schema).size() > 0) {
            generateUDTReferences(schema);
        }

        if (generateUDTs() && database.getArrays(schema).size() > 0) {
            generateArrays(schema);
        }

        if (generateUDTs() && database.getEnums(schema).size() > 0) {
            generateEnums(schema);
        }

        if (generateUDTs() && database.getDomains(schema).size() > 0) {
            generateDomains(schema);
        }

        if (generateRoutines() && (database.getRoutines(schema).size() > 0 || hasTableValuedFunctions(schema))) {
            generateRoutines(schema);
        }













        // XXX [#651] Refactoring-cursor
        watch.splitInfo("Generation finished: " + schema.getQualifiedName());
        log.info("");
    }

    private class AvoidAmbiguousClassesFilter implements Database.Filter {

        private Map<String, String> included = new HashMap<>();

        @Override
        public boolean exclude(Definition definition) {

            // These definitions don't generate types of their own.
            if (    definition instanceof ColumnDefinition
                 || definition instanceof AttributeDefinition
                 || definition instanceof ParameterDefinition)
                return false;

            // Check if we've previously encountered a Java type of the same case-insensitive, fully-qualified name.
            String name = getStrategy().getFullJavaClassName(definition);
            String nameLC = name.toLowerCase();
            String existing = included.put(nameLC, name);

            if (existing == null)
                return false;

            log.warn("Ambiguous type name", "The object " + definition.getQualifiedOutputName() + " generates a type " + name + " which conflicts with the existing type " + existing + " on some operating systems. Use a custom generator strategy to disambiguate the types.");
            return true;
        }
    }





















































































    private boolean hasTableValuedFunctions(SchemaDefinition schema) {
        for (TableDefinition table : database.getTables(schema)) {
            if (table.isTableValuedFunction()) {
                return true;
            }
        }

        return false;
    }

    protected void generateRelations(SchemaDefinition schema) {
        log.info("Generating Keys");

        boolean empty = true;
        JavaWriter out = newJavaWriter(new File(getFile(schema).getParentFile(), "Keys.java"));
        printPackage(out, schema);
        printClassJavadoc(out,
            "A class modelling foreign key relationships and constraints of tables of the <code>" + schema.getOutputName() + "</code> schema.");
        printClassAnnotations(out, schema);

        if (scala)
            out.println("object Keys {");
        else
            out.println("public class Keys {");

        List<IdentityDefinition> allIdentities = new ArrayList<>();
        List<UniqueKeyDefinition> allUniqueKeys = new ArrayList<>();
        List<ForeignKeyDefinition> allForeignKeys = new ArrayList<>();

        out.tab(1).header("IDENTITY definitions");
        out.println();

        for (TableDefinition table : database.getTables(schema)) {
            try {
                IdentityDefinition identity = table.getIdentity();

                if (identity != null) {
                    empty = false;

                    final String identityType = out.ref(getStrategy().getFullJavaClassName(identity.getColumn().getContainer(), Mode.RECORD));
                    final String columnTypeFull = getJavaType(identity.getColumn().getType(resolver()));
                    final String columnType = out.ref(columnTypeFull);
                    final String identityId = getStrategy().getJavaIdentifier(identity.getColumn().getContainer());
                    final int block = allIdentities.size() / INITIALISER_SIZE;

                    printDeprecationIfUnknownType(out, columnTypeFull);
                    if (scala)
                        out.tab(1).println("val IDENTITY_%s = Identities%s.IDENTITY_%s",
                                identityId, block, identityId);
                    else
                        out.tab(1).println("public static final %s<%s, %s> IDENTITY_%s = Identities%s.IDENTITY_%s;",
                            Identity.class, identityType, columnType, identityId, block, identityId);

                    allIdentities.add(identity);
                }
            }
            catch (Exception e) {
                log.error("Error while generating table " + table, e);
            }
        }

        // Unique keys
        out.tab(1).header("UNIQUE and PRIMARY KEY definitions");
        out.println();

        for (TableDefinition table : database.getTables(schema)) {
            try {
                List<UniqueKeyDefinition> uniqueKeys = table.getUniqueKeys();

                for (UniqueKeyDefinition uniqueKey : uniqueKeys) {
                    empty = false;

                    final String keyType = out.ref(getStrategy().getFullJavaClassName(uniqueKey.getTable(), Mode.RECORD));
                    final String keyId = getStrategy().getJavaIdentifier(uniqueKey);
                    final int block = allUniqueKeys.size() / INITIALISER_SIZE;

                    if (scala)
                        out.tab(1).println("val %s = UniqueKeys%s.%s", keyId, block, keyId);
                    else
                        out.tab(1).println("public static final %s<%s> %s = UniqueKeys%s.%s;", UniqueKey.class, keyType, keyId, block, keyId);

                    allUniqueKeys.add(uniqueKey);
                }
            }
            catch (Exception e) {
                log.error("Error while generating table " + table, e);
            }
        }

        // Foreign keys
        out.tab(1).header("FOREIGN KEY definitions");
        out.println();

        for (TableDefinition table : database.getTables(schema)) {
            try {
                List<ForeignKeyDefinition> foreignKeys = table.getForeignKeys();

                for (ForeignKeyDefinition foreignKey : foreignKeys) {
                    empty = false;

                    final String keyType = out.ref(getStrategy().getFullJavaClassName(foreignKey.getKeyTable(), Mode.RECORD));
                    final String referencedType = out.ref(getStrategy().getFullJavaClassName(foreignKey.getReferencedTable(), Mode.RECORD));
                    final String keyId = getStrategy().getJavaIdentifier(foreignKey);
                    final int block = allForeignKeys.size() / INITIALISER_SIZE;

                    if (scala)
                        out.tab(1).println("val %s = ForeignKeys%s.%s", keyId, block, keyId);
                    else
                        out.tab(1).println("public static final %s<%s, %s> %s = ForeignKeys%s.%s;", ForeignKey.class, keyType, referencedType, keyId, block, keyId);

                    allForeignKeys.add(foreignKey);
                }
            }
            catch (Exception e) {
                log.error("Error while generating reference " + table, e);
            }
        }

        // [#1459] Print nested classes for actual static field initialisations
        // keeping top-level initialiser small
        int identityCounter = 0;
        int uniqueKeyCounter = 0;
        int foreignKeyCounter = 0;

        out.tab(1).header("[#1459] distribute members to avoid static initialisers > 64kb");

        // Identities
        // ----------

        for (IdentityDefinition identity : allIdentities) {
            printIdentity(out, identityCounter, identity);
            identityCounter++;
        }

        if (identityCounter > 0) {
            out.tab(1).println("}");
        }

        // UniqueKeys
        // ----------

        for (UniqueKeyDefinition uniqueKey : allUniqueKeys) {
            printUniqueKey(out, uniqueKeyCounter, uniqueKey);
            uniqueKeyCounter++;
        }

        if (uniqueKeyCounter > 0) {
            out.tab(1).println("}");
        }

        // ForeignKeys
        // -----------

        for (ForeignKeyDefinition foreignKey : allForeignKeys) {
            printForeignKey(out, foreignKeyCounter, foreignKey);
            foreignKeyCounter++;
        }

        if (foreignKeyCounter > 0) {
            out.tab(1).println("}");
        }

        out.println("}");

        if (empty) {
            log.info("Skipping empty keys");
        }
        else {
            closeJavaWriter(out);
            watch.splitInfo("Keys generated");
        }
    }

    protected void generateIndexes(SchemaDefinition schema) {
        log.info("Generating Indexes");

        if (database.getIndexes(schema).isEmpty()) {
            log.info("Skipping empty indexes");
            return;
        }

        JavaWriter out = newJavaWriter(new File(getFile(schema).getParentFile(), "Indexes.java"));
        printPackage(out, schema);
        printClassJavadoc(out,
            "A class modelling indexes of tables of the <code>" + schema.getOutputName() + "</code> schema.");
        printClassAnnotations(out, schema);

        if (scala)
            out.println("object Indexes {");
        else
            out.println("public class Indexes {");

        List<IndexDefinition> allIndexes = new ArrayList<>();

        out.tab(1).header("INDEX definitions");
        out.println();

        for (TableDefinition table : database.getTables(schema)) {
            try {
                List<IndexDefinition> indexes = table.getIndexes();

                for (IndexDefinition index : indexes) {
                    final String keyId = getStrategy().getJavaIdentifier(index);
                    final int block = allIndexes.size() / INITIALISER_SIZE;

                    if (scala)
                        out.tab(1).println("val %s = Indexes%s.%s", keyId, block, keyId);
                    else
                        out.tab(1).println("public static final %s %s = Indexes%s.%s;", Index.class, keyId, block, keyId);

                    allIndexes.add(index);
                }
            }
            catch (Exception e) {
                log.error("Error while generating table " + table, e);
            }
        }

        // [#1459] Print nested classes for actual static field initialisations
        // keeping top-level initialiser small
        int indexCounter = 0;

        out.tab(1).header("[#1459] distribute members to avoid static initialisers > 64kb");

        // Indexes
        // -------

        for (IndexDefinition index : allIndexes) {
            printIndex(out, indexCounter, index);
            indexCounter++;
        }

        if (indexCounter > 0) {
            out.tab(1).println("}");
        }

        out.println("}");
        closeJavaWriter(out);

        watch.splitInfo("Indexes generated");
    }

    protected void printIndex(JavaWriter out, int indexCounter, IndexDefinition index) {
        final int block = indexCounter / INITIALISER_SIZE;

        // Print new nested class
        if (indexCounter % INITIALISER_SIZE == 0) {
            if (indexCounter > 0) {
                out.tab(1).println("}");
            }

            out.println();

            if (scala)
                out.tab(1).println("private object Indexes%s {", block);
            else
                out.tab(1).println("private static class Indexes%s {", block);
        }

        // (Name name, Table<?> table, SortField<?>[] sortFields, boolean unique)
        String sortFieldSeparator = "";
        StringBuilder orderFields = new StringBuilder();

        for (IndexColumnDefinition column : index.getIndexColumns()) {
            orderFields.append(sortFieldSeparator);
            orderFields.append(out.ref(getStrategy().getFullJavaIdentifier(column.getColumn()), colRefSegments(null)));
            orderFields.append(column.getSortOrder() == DESC ? ".desc()" : "");

            sortFieldSeparator = ", ";
        }

        if (scala)
            out.tab(2).println("val %s : %s = %s.createIndex(\"%s\", %s, Array[%s [_] ](%s), %s)",
                getStrategy().getJavaIdentifier(index),
                Index.class,
                Internal.class,
                escapeString(index.getOutputName()),
                out.ref(getStrategy().getFullJavaIdentifier(index.getTable()), 2),
                OrderField.class,
                orderFields,
                index.isUnique()
            );
        else
            out.tab(2).println("public static %s %s = %s.createIndex(\"%s\", %s, new %s[] { %s }, %s);",
                Index.class,
                getStrategy().getJavaIdentifier(index),
                Internal.class,
                escapeString(index.getOutputName()),
                out.ref(getStrategy().getFullJavaIdentifier(index.getTable()), 2),
                OrderField.class,
                orderFields,
                index.isUnique()
            );
    }

    protected void printIdentity(JavaWriter out, int identityCounter, IdentityDefinition identity) {
        final int block = identityCounter / INITIALISER_SIZE;
        final String identityTypeFull = getJavaType(identity.getColumn().getType(resolver()));
        final String identityType = out.ref(identityTypeFull);

        // Print new nested class
        if (identityCounter % INITIALISER_SIZE == 0) {
            if (identityCounter > 0) {
                out.tab(1).println("}");
            }

            out.println();

            if (scala)
                out.tab(1).println("private object Identities%s {", block);
            else
                out.tab(1).println("private static class Identities%s {", block);
        }

        printDeprecationIfUnknownType(out, identityTypeFull);
        if (scala)
            out.tab(2).print("val %s : %s[%s, %s] = ",
                getStrategy().getJavaIdentifier(identity),
                Identity.class,
                out.ref(getStrategy().getFullJavaClassName(identity.getTable(), Mode.RECORD)),
                identityType);
        else
            out.tab(2).print("public static %s<%s, %s> %s = ",
                Identity.class,
                out.ref(getStrategy().getFullJavaClassName(identity.getTable(), Mode.RECORD)),
                identityType,
                getStrategy().getJavaIdentifier(identity));

        printCreateIdentity(out, identity);

        if (scala)
            out.println();
        else
            out.println(";");
    }

    private void printCreateIdentity(JavaWriter out, IdentityDefinition identity) {
        if (scala)
            out.print("%s.createIdentity(%s, %s)",
                Internal.class,
                out.ref(getStrategy().getFullJavaIdentifier(identity.getColumn().getContainer()), 2),
                out.ref(getStrategy().getFullJavaIdentifier(identity.getColumn()), colRefSegments(identity.getColumn())));
        else
            out.print("%s.createIdentity(%s, %s)",
                Internal.class,
                out.ref(getStrategy().getFullJavaIdentifier(identity.getColumn().getContainer()), 2),
                out.ref(getStrategy().getFullJavaIdentifier(identity.getColumn()), colRefSegments(identity.getColumn())));
    }

    protected void printUniqueKey(JavaWriter out, int uniqueKeyCounter, UniqueKeyDefinition uniqueKey) {
        final int block = uniqueKeyCounter / INITIALISER_SIZE;

        // Print new nested class
        if (uniqueKeyCounter % INITIALISER_SIZE == 0) {
            if (uniqueKeyCounter > 0) {
                out.tab(1).println("}");
            }

            out.println();

            if (scala)
                out.tab(1).println("private object UniqueKeys%s {", block);
            else
                out.tab(1).println("private static class UniqueKeys%s {", block);
        }

        if (scala)
            out.tab(2).print("val %s : %s[%s] = ",
                getStrategy().getJavaIdentifier(uniqueKey),
                UniqueKey.class,
                out.ref(getStrategy().getFullJavaClassName(uniqueKey.getTable(), Mode.RECORD)));
        else
            out.tab(2).print("public static final %s<%s> %s = ",
                UniqueKey.class,
                out.ref(getStrategy().getFullJavaClassName(uniqueKey.getTable(), Mode.RECORD)),
                getStrategy().getJavaIdentifier(uniqueKey));

        printCreateUniqueKey(out, uniqueKey);

        if (scala)
            out.println();
        else
            out.println(";");
    }

    private void printCreateUniqueKey(JavaWriter out, UniqueKeyDefinition uniqueKey) {
        if (scala)
            out.print("%s.createUniqueKey(%s, \"%s\", [[%s]])",
                Internal.class,
                out.ref(getStrategy().getFullJavaIdentifier(uniqueKey.getTable()), 2),
                escapeString(uniqueKey.getOutputName()),
                out.ref(getStrategy().getFullJavaIdentifiers(uniqueKey.getKeyColumns()), colRefSegments(null)));
        else
            out.print("%s.createUniqueKey(%s, \"%s\", [[%s]])",
                Internal.class,
                out.ref(getStrategy().getFullJavaIdentifier(uniqueKey.getTable()), 2),
                escapeString(uniqueKey.getOutputName()),
                out.ref(getStrategy().getFullJavaIdentifiers(uniqueKey.getKeyColumns()), colRefSegments(null)));
    }

    protected void printForeignKey(JavaWriter out, int foreignKeyCounter, ForeignKeyDefinition foreignKey) {
        final int block = foreignKeyCounter / INITIALISER_SIZE;

        // Print new nested class
        if (foreignKeyCounter % INITIALISER_SIZE == 0) {
            if (foreignKeyCounter > 0) {
                out.tab(1).println("}");
            }

            out.println();

            if (scala)
                out.tab(1).println("private object ForeignKeys%s {", block);
            else
                out.tab(1).println("private static class ForeignKeys%s {", block);
        }

        if (scala)
            out.tab(2).println("val %s : %s[%s, %s] = %s.createForeignKey(%s, %s, \"%s\", [[%s]])",
                getStrategy().getJavaIdentifier(foreignKey),
                ForeignKey.class,
                out.ref(getStrategy().getFullJavaClassName(foreignKey.getKeyTable(), Mode.RECORD)),
                out.ref(getStrategy().getFullJavaClassName(foreignKey.getReferencedTable(), Mode.RECORD)),
                Internal.class,
                out.ref(getStrategy().getFullJavaIdentifier(foreignKey.getReferencedKey()), 2),
                out.ref(getStrategy().getFullJavaIdentifier(foreignKey.getKeyTable()), 2),
                escapeString(foreignKey.getOutputName()),
                out.ref(getStrategy().getFullJavaIdentifiers(foreignKey.getKeyColumns()), colRefSegments(null)));
        else
            out.tab(2).println("public static final %s<%s, %s> %s = %s.createForeignKey(%s, %s, \"%s\", [[%s]]);",
                ForeignKey.class,
                out.ref(getStrategy().getFullJavaClassName(foreignKey.getKeyTable(), Mode.RECORD)),
                out.ref(getStrategy().getFullJavaClassName(foreignKey.getReferencedTable(), Mode.RECORD)),
                getStrategy().getJavaIdentifier(foreignKey),
                Internal.class,
                out.ref(getStrategy().getFullJavaIdentifier(foreignKey.getReferencedKey()), 2),
                out.ref(getStrategy().getFullJavaIdentifier(foreignKey.getKeyTable()), 2),
                escapeString(foreignKey.getOutputName()),
                out.ref(getStrategy().getFullJavaIdentifiers(foreignKey.getKeyColumns()), colRefSegments(null)));
    }

    protected void generateRecords(SchemaDefinition schema) {
        log.info("Generating table records");

        for (TableDefinition table : database.getTables(schema)) {
            try {
                generateRecord(table);
            } catch (Exception e) {
                log.error("Error while generating table record " + table, e);
            }
        }

        watch.splitInfo("Table records generated");
    }


    protected void generateRecord(TableDefinition table) {
        JavaWriter out = newJavaWriter(getFile(table, Mode.RECORD));
        log.info("Generating record", out.file().getName());
        generateRecord(table, out);
        closeJavaWriter(out);
    }

    protected void generateUDTRecord(UDTDefinition udt) {
        JavaWriter out = newJavaWriter(getFile(udt, Mode.RECORD));
        log.info("Generating record", out.file().getName());
        generateRecord0(udt, out);
        closeJavaWriter(out);
    }

    protected void generateRecord(TableDefinition table, JavaWriter out) {
        generateRecord0(table, out);
    }

    protected void generateUDTRecord(UDTDefinition udt, JavaWriter out) {
        generateRecord0(udt, out);
    }

    private final void generateRecord0(Definition tableUdtOrEmbeddable, JavaWriter out) {
        final UniqueKeyDefinition key = (tableUdtOrEmbeddable instanceof TableDefinition)
            ? ((TableDefinition) tableUdtOrEmbeddable).getPrimaryKey()
            : null;
        final String className = getStrategy().getJavaClassName(tableUdtOrEmbeddable, Mode.RECORD);
        final String tableIdentifier = !(tableUdtOrEmbeddable instanceof EmbeddableDefinition)
            ? out.ref(getStrategy().getFullJavaIdentifier(tableUdtOrEmbeddable), 2)
            : null;
        final List<String> interfaces = out.ref(getStrategy().getJavaClassImplements(tableUdtOrEmbeddable, Mode.RECORD));
        final List<? extends TypedElementDefinition<?>> columns = getTypedElements(tableUdtOrEmbeddable);

        printPackage(out, tableUdtOrEmbeddable, Mode.RECORD);

        if (tableUdtOrEmbeddable instanceof TableDefinition)
            generateRecordClassJavadoc((TableDefinition) tableUdtOrEmbeddable, out);
        else if (tableUdtOrEmbeddable instanceof EmbeddableDefinition)
            generateEmbeddableClassJavadoc((EmbeddableDefinition) tableUdtOrEmbeddable, out);
        else
            generateUDTRecordClassJavadoc((UDTDefinition) tableUdtOrEmbeddable, out);

        printClassAnnotations(out, tableUdtOrEmbeddable.getSchema());
        if (tableUdtOrEmbeddable instanceof TableDefinition)
            printTableJPAAnnotation(out, (TableDefinition) tableUdtOrEmbeddable);

        Class<?> baseClass;
        if (tableUdtOrEmbeddable instanceof UDTDefinition)
            baseClass = UDTRecordImpl.class;
        else if (tableUdtOrEmbeddable instanceof EmbeddableDefinition)
            baseClass = EmbeddableRecordImpl.class;
        else if (generateRelations() && key != null)
            baseClass = UpdatableRecordImpl.class;
        else
            baseClass = TableRecordImpl.class;

        int degree = columns.size();
        String rowType = null;
        String rowTypeRecord = null;

        // [#3130] Invalid UDTs may have a degree of 0
        // [#6072] Generate these super types only if configured to do so
        if (generateRecordsImplementingRecordN() && degree > 0 && degree <= Constants.MAX_ROW_DEGREE) {
            rowType = refRowType(out, columns);

            if (scala)
                rowTypeRecord = out.ref(Record.class.getName() + degree) + "[" + rowType + "]";
            else
                rowTypeRecord = out.ref(Record.class.getName() + degree) + "<" + rowType + ">";

            interfaces.add(rowTypeRecord);
        }

        if (generateInterfaces())
            interfaces.add(out.ref(getStrategy().getFullJavaClassName(tableUdtOrEmbeddable, Mode.INTERFACE)));

        if (scala)
            if (tableUdtOrEmbeddable instanceof EmbeddableDefinition)
                out.println("class %s extends %s[%s]()[[before= with ][separator= with ][%s]] {", className, baseClass, className, interfaces);
            else
                out.println("class %s extends %s[%s](%s)[[before= with ][separator= with ][%s]] {", className, baseClass, className, tableIdentifier, interfaces);
        else
            out.println("public class %s extends %s<%s>[[before= implements ][%s]] {", className, baseClass, className, interfaces);

        out.printSerial();

        for (int i = 0; i < degree; i++) {
            TypedElementDefinition<?> column = columns.get(i);

            if (tableUdtOrEmbeddable instanceof TableDefinition) {
                generateRecordSetter(column, i, out);
                generateRecordGetter(column, i, out);
            }
            else if (tableUdtOrEmbeddable instanceof EmbeddableDefinition) {
                generateEmbeddableSetter(column, i, out);
                generateEmbeddableGetter(column, i, out);
            }
            else {
                generateUDTRecordSetter(column, i, out);
                generateUDTRecordGetter(column, i, out);
            }
        }

        if (tableUdtOrEmbeddable instanceof TableDefinition) {
            List<EmbeddableDefinition> embeddables = ((TableDefinition) tableUdtOrEmbeddable).getEmbeddables();

            for (int i = 0; i < embeddables.size(); i++) {
                EmbeddableDefinition embeddable = embeddables.get(i);

                // [#2530] TODO: Implement setters and getters for embeddables here
            }
        }

        if (generateRelations() && key != null) {
            int keyDegree = key.getKeyColumns().size();

            if (keyDegree <= Constants.MAX_ROW_DEGREE) {
                String keyType = refRowType(out, key.getKeyColumns());
                out.tab(1).header("Primary key information");

                if (scala) {
                    out.tab(1).println("override def key : %s[%s] = {", out.ref(Record.class.getName() + keyDegree), keyType);
                    out.tab(2).println("return super.key.asInstanceOf[ %s[%s] ]", out.ref(Record.class.getName() + keyDegree), keyType);
                    out.tab(1).println("}");
                }
                else {
                    out.tab(1).overrideInherit();
                    out.tab(1).println("public %s<%s> key() {", out.ref(Record.class.getName() + keyDegree), keyType);
                    out.tab(2).println("return (%s) super.key();", out.ref(Record.class.getName() + keyDegree));
                    out.tab(1).println("}");
                }
            }
        }

        if (tableUdtOrEmbeddable instanceof UDTDefinition) {

            // [#799] Oracle UDT's can have member procedures
            for (RoutineDefinition routine : ((UDTDefinition) tableUdtOrEmbeddable).getRoutines()) {

                // Instance methods ship with a SELF parameter at the first position
                // [#1584] Static methods don't have that
                boolean instance = routine.getInParameters().size() > 0
                                && routine.getInParameters().get(0).getInputName().toUpperCase().equals("SELF");

                try {
                    if (!routine.isSQLUsable()) {
                        // Instance execute() convenience method
                        printConvenienceMethodProcedure(out, routine, instance);
                    }
                    else {
                        // Instance execute() convenience method
                        if (!routine.isAggregate()) {
                            printConvenienceMethodFunction(out, routine, instance);
                        }
                    }

                } catch (Exception e) {
                    log.error("Error while generating routine " + routine, e);
                }
            }
        }

        // [#3130] Invalid UDTs may have a degree of 0
        if (generateRecordsImplementingRecordN() && degree > 0 && degree <= Constants.MAX_ROW_DEGREE) {
            out.tab(1).header("Record%s type implementation", degree);

            // fieldsRow()
            if (scala) {
                out.println();
                out.tab(1).println("override def fieldsRow : %s[%s] = {", out.ref(Row.class.getName() + degree), rowType);
                out.tab(2).println("super.fieldsRow.asInstanceOf[ %s[%s] ]", out.ref(Row.class.getName() + degree), rowType);
                out.tab(1).println("}");
            }
            else {
                out.tab(1).overrideInherit();
                out.tab(1).println("public %s<%s> fieldsRow() {", out.ref(Row.class.getName() + degree), rowType);
                out.tab(2).println("return (%s) super.fieldsRow();", out.ref(Row.class.getName() + degree));
                out.tab(1).println("}");
            }

            // valuesRow()
            if (scala) {
                out.println();
                out.tab(1).println("override def valuesRow : %s[%s] = {", out.ref(Row.class.getName() + degree), rowType);
                out.tab(2).println("super.valuesRow.asInstanceOf[ %s[%s] ]", out.ref(Row.class.getName() + degree), rowType);
                out.tab(1).println("}");
            }
            else {
                out.tab(1).overrideInherit();
                out.tab(1).println("public %s<%s> valuesRow() {", out.ref(Row.class.getName() + degree), rowType);
                out.tab(2).println("return (%s) super.valuesRow();", out.ref(Row.class.getName() + degree));
                out.tab(1).println("}");
            }

            // field[N]()
            for (int i = 1; i <= degree; i++) {
                TypedElementDefinition<?> column = columns.get(i - 1);

                if (column instanceof EmbeddableColumnDefinition)
                    column = ((EmbeddableColumnDefinition) column).getColumn();

                final String colTypeFull = getJavaType(column.getType(resolver()));
                final String colType = out.ref(colTypeFull);
                final String colIdentifier = out.ref(getStrategy().getFullJavaIdentifier(column), colRefSegments(column));

                if (scala) {
                    printDeprecationIfUnknownType(out, colTypeFull);
                    out.tab(1).println("override def field%s : %s[%s] = %s", i, Field.class, colType, colIdentifier);
                }
                else {
                    if (printDeprecationIfUnknownType(out, colTypeFull))
                        out.tab(1).override();
                    else
                        out.tab(1).overrideInherit();

                    out.tab(1).println("public %s<%s> field%s() {", Field.class, colType, i);

                    if (tableUdtOrEmbeddable instanceof EmbeddableDefinition)
                        out.tab(2).println("return (%s<%s>) FIELDS[%s];", Field.class, colType, i - 1);
                    else
                        out.tab(2).println("return %s;", colIdentifier);

                    out.tab(1).println("}");
                }
            }

            // component[N]()
            for (int i = 1; i <= degree; i++) {
                TypedElementDefinition<?> column = columns.get(i - 1);

                final String colTypeFull = getJavaType(column.getType(resolver()));
                final String colType = out.ref(colTypeFull);
                final String colGetter = getStrategy().getJavaGetterName(column, Mode.RECORD);

                if (scala) {
                    printDeprecationIfUnknownType(out, colTypeFull);
                    out.tab(1).println("override def component%s : %s = %s", i, colType, colGetter);
                }
                else {
                    if (printDeprecationIfUnknownType(out, colTypeFull))
                        out.tab(1).override();
                    else
                        out.tab(1).overrideInherit();

                    out.tab(1).println("public %s component%s() {", colType, i);
                    out.tab(2).println("return %s();", colGetter);
                    out.tab(1).println("}");
                }
            }

            // value[N]()
            for (int i = 1; i <= degree; i++) {
                TypedElementDefinition<?> column = columns.get(i - 1);

                final String colTypeFull = getJavaType(column.getType(resolver()));
                final String colType = out.ref(colTypeFull);
                final String colGetter = getStrategy().getJavaGetterName(column, Mode.RECORD);

                if (scala) {
                    printDeprecationIfUnknownType(out, colTypeFull);
                    out.tab(1).println("override def value%s : %s = %s", i, colType, colGetter);
                }
                else {
                    if (printDeprecationIfUnknownType(out, colTypeFull))
                        out.tab(1).override();
                    else
                        out.tab(1).overrideInherit();

                    out.tab(1).println("public %s value%s() {", colType, i);
                    out.tab(2).println("return %s();", colGetter);
                    out.tab(1).println("}");
                }
            }

            // value[N](T[N])
            for (int i = 1; i <= degree; i++) {
                TypedElementDefinition<?> column = columns.get(i - 1);

                final String colTypeFull = getJavaType(column.getType(resolver()));
                final String colType = out.ref(colTypeFull);
                final String colSetter = getStrategy().getJavaSetterName(column, Mode.RECORD);

                if (scala) {
                    out.println();
                    printDeprecationIfUnknownType(out, colTypeFull);
                    out.tab(1).println("override def value%s(value : %s) : %s = {", i, colType, className);
                    out.tab(2).println("%s(value)", colSetter);
                    out.tab(2).println("this");
                    out.tab(1).println("}");
                }
                else {

                    if (printDeprecationIfUnknownType(out, colTypeFull))
                        out.tab(1).override();
                    else
                        out.tab(1).overrideInherit();

                    out.tab(1).println("public %s value%s(%s value) {", className, i, varargsIfArray(colType));
                    out.tab(2).println("%s(value);", colSetter);
                    out.tab(2).println("return this;");
                    out.tab(1).println("}");
                }
            }

            List<String> arguments = new ArrayList<>(degree);
            List<String> calls = new ArrayList<>(degree);
            for (int i = 1; i <= degree; i++) {
                TypedElementDefinition<?> column = columns.get(i - 1);

                final String colType = out.ref(getJavaType(column.getType(resolver())));

                if (scala) {
                    arguments.add("value" + i + " : " + colType);
                    calls.add("this.value" + i + "(value" + i + ")");
                }
                else {
                    arguments.add(colType + " value" + i);
                    calls.add("value" + i + "(value" + i + ");");
                }
            }

            if (scala) {
                out.println();
                out.tab(1).println("override def values([[%s]]) : %s = {", arguments, className);

                for (String call : calls)
                    out.tab(2).println(call);

                out.tab(2).println("this");
                out.tab(1).println("}");
            }
            else {
                out.tab(1).overrideInherit();
                out.tab(1).println("public %s values([[%s]]) {", className, arguments);

                for (String call : calls)
                    out.tab(2).println(call);

                out.tab(2).println("return this;");
                out.tab(1).println("}");
            }
        }

        if (generateInterfaces())
            printFromAndInto(out, tableUdtOrEmbeddable);

        if (scala) {
        }
        else {
            out.tab(1).header("Constructors");

            if (tableUdtOrEmbeddable instanceof EmbeddableDefinition) {
                out.println();
                out.tab(1).println("private static final %s<?>[] FIELDS = {", Field.class);

                for (EmbeddableColumnDefinition column : ((EmbeddableDefinition) tableUdtOrEmbeddable).getColumns()) {
                    final String colIdentifier = out.ref(getStrategy().getFullJavaIdentifier(column.getColumn()), colRefSegments(column));

                    out.tab(2).println("%s.field(%s.name(\"%s\"), %s.getDataType()),", DSL.class, DSL.class, column.getOutputName(), colIdentifier);
                }

                out.tab(1).println("};");
                out.println();
            }

            out.tab(1).javadoc("Create a detached %s", className);

            out.tab(1).println("public %s() {", className);
            if (tableUdtOrEmbeddable instanceof EmbeddableDefinition)
                out.tab(2).println("super(FIELDS);");
            else
                out.tab(2).println("super(%s);", tableIdentifier);
            out.tab(1).println("}");
        }

        // [#3130] Invalid UDTs may have a degree of 0
        // [#3176] Avoid generating constructors for tables with more than 255 columns (Java's method argument limit)
        if (degree > 0 && degree < 256) {
            List<String> arguments = new ArrayList<>(degree);

            for (int i = 0; i < degree; i++) {
                final TypedElementDefinition<?> column = columns.get(i);
                final String columnMember = getStrategy().getJavaMemberName(column, Mode.DEFAULT);
                final String type = out.ref(getJavaType(column.getType(resolver())));

                if (scala)
                    arguments.add(columnMember + " : " + type);
                else
                    arguments.add(type + " " + columnMember);
            }

            out.tab(1).javadoc("Create a detached, initialised %s", className);

            if (scala) {
                out.tab(1).println("def this([[%s]]) = {", arguments);
                out.tab(2).println("this()", tableIdentifier);
            }
            else {
                out.tab(1).println("public %s([[%s]]) {", className, arguments);

                if (tableUdtOrEmbeddable instanceof EmbeddableDefinition)
                    out.tab(2).println("this();", tableIdentifier);
                else
                    out.tab(2).println("super(%s);", tableIdentifier);
            }

            out.println();

            for (int i = 0; i < degree; i++) {
                final TypedElementDefinition<?> column = columns.get(i);
                final String columnMember = getStrategy().getJavaMemberName(column, Mode.DEFAULT);

                if (scala)
                    out.tab(2).println("set(%s, %s)", i, columnMember);
                else
                    out.tab(2).println("set(%s, %s);", i, columnMember);
            }

            out.tab(1).println("}");
        }

        if (tableUdtOrEmbeddable instanceof TableDefinition)
            generateRecordClassFooter((TableDefinition) tableUdtOrEmbeddable, out);
        else if (tableUdtOrEmbeddable instanceof EmbeddableDefinition)
            generateEmbeddableClassFooter((EmbeddableDefinition) tableUdtOrEmbeddable, out);
        else
            generateUDTRecordClassFooter((UDTDefinition) tableUdtOrEmbeddable, out);

        out.println("}");
    }

    /**
     * Subclasses may override this method to provide their own record setters.
     */
    protected void generateRecordSetter(TypedElementDefinition<?> column, int index, JavaWriter out) {
        generateRecordSetter0(column, index, out);
    }

    /**
     * Subclasses may override this method to provide their own embeddable setters.
     */
    protected void generateEmbeddableSetter(TypedElementDefinition<?> column, int index, JavaWriter out) {
        generateRecordSetter0(column, index, out);
    }

    /**
     * Subclasses may override this method to provide their own record setters.
     */
    protected void generateUDTRecordSetter(TypedElementDefinition<?> column, int index, JavaWriter out) {
        generateRecordSetter0(column, index, out);
    }

    private final void generateRecordSetter0(TypedElementDefinition<?> column, int index, JavaWriter out) {
        final String comment = StringUtils.defaultString(column.getComment());
        final String className = getStrategy().getJavaClassName(column.getContainer(), Mode.RECORD);
        final String setterReturnType = generateFluentSetters() ? className : tokenVoid;
        final String setter = getStrategy().getJavaSetterName(column, Mode.RECORD);
        final String typeFull = getJavaType(column.getType(resolver()));
        final String type = out.ref(typeFull);
        final String name = column.getQualifiedOutputName();
        final boolean isUDT = column.getType(resolver()).isUDT();
        final boolean isArray = column.getType(resolver()).isArray();
        final boolean isUDTArray = column.getType(resolver()).isArray() && database.getArray(column.getType(resolver()).getSchema(), column.getType(resolver()).getQualifiedUserType()).getElementType(resolver()).isUDT();


        // We cannot have covariant setters for arrays because of type erasure
        if (!(generateInterfaces() && isArray)) {
            if (!printDeprecationIfUnknownType(out, typeFull))
                out.tab(1).javadoc("Setter for <code>%s</code>.%s", name, columnComment(column, comment));

            if (scala) {
                out.tab(1).println("def %s(value : %s) : %s = {", setter, type, setterReturnType);
                out.tab(2).println("set(%s, value)", index);
                if (generateFluentSetters())
                    out.tab(2).println("this");
                out.tab(1).println("}");
            }
            else {
                out.tab(1).overrideIf(generateInterfaces() && !generateImmutableInterfaces() && !isUDT);
                out.tab(1).println("public %s %s(%s value) {", setterReturnType, setter, varargsIfArray(type));
                out.tab(2).println("set(%s, value);", index);
                if (generateFluentSetters())
                    out.tab(2).println("return this;");
                out.tab(1).println("}");
            }
        }

        // [#3117] Avoid covariant setters for UDTs when generating interfaces
        if (generateInterfaces() && !generateImmutableInterfaces() && (isUDT || isArray)) {
            final String columnTypeFull = getJavaType(column.getType(resolver(Mode.RECORD)), Mode.RECORD);
            final String columnType = out.ref(columnTypeFull);
            final String columnTypeInterface = out.ref(getJavaType(column.getType(resolver(Mode.INTERFACE)), Mode.INTERFACE));

            if (!printDeprecationIfUnknownType(out, columnTypeFull))
                out.tab(1).javadoc("Setter for <code>%s</code>.%s", name, defaultIfBlank(" " + comment, ""));

            out.tab(1).override();

            if (scala) {
                // [#3082] TODO Handle <interfaces/> + ARRAY also for Scala

                out.tab(1).println("def %s(value : %s) : %s = {", setter, columnTypeInterface, setterReturnType);
                out.tab(2).println("if (value == null)");
                out.tab(3).println("set(%s, null)", index);
                out.tab(2).println("else");
                out.tab(3).println("set(%s, value.into(new %s()))", index, type);
                if (generateFluentSetters())
                    out.tab(2).println("this");
                out.tab(1).println("}");
            }
            else {
                out.tab(1).println("public %s %s(%s value) {", setterReturnType, setter, varargsIfArray(columnTypeInterface));
                out.tab(2).println("if (value == null)");
                out.tab(3).println("set(%s, null);", index);

                if (isUDT) {
                    out.tab(2).println("else");
                    out.tab(3).println("set(%s, value.into(new %s()));", index, type);
                }
                else if (isArray) {
                    final ArrayDefinition array = database.getArray(column.getType(resolver()).getSchema(), column.getType(resolver()).getQualifiedUserType());
                    final String componentType = out.ref(getJavaType(array.getElementType(resolver(Mode.RECORD)), Mode.RECORD));
                    final String componentTypeInterface = out.ref(getJavaType(array.getElementType(resolver(Mode.INTERFACE)), Mode.INTERFACE));

                    out.tab(2).println("else {");
                    out.tab(3).println("%s a = new %s();", columnType, columnType);
                    out.println();
                    out.tab(3).println("for (%s i : value)", componentTypeInterface);

                    if (isUDTArray)
                        out.tab(4).println("a.add(i.into(new %s()));", componentType);
                    else
                        out.tab(4).println("a.add(i);", componentType);

                    out.println();
                    out.tab(3).println("set(1, a);");
                    out.tab(2).println("}");
                }

                if (generateFluentSetters())
                    out.tab(2).println("return this;");

                out.tab(1).println("}");
            }
        }
    }

    /**
     * Subclasses may override this method to provide their own record getters.
     */
    protected void generateRecordGetter(TypedElementDefinition<?> column, int index, JavaWriter out) {
        generateRecordGetter0(column, index, out);
    }

    /**
     * Subclasses may override this method to provide their own embeddable getters.
     */
    protected void generateEmbeddableGetter(TypedElementDefinition<?> column, int index, JavaWriter out) {
        generateRecordGetter0(column, index, out);
    }

    /**
     * Subclasses may override this method to provide their own record getters.
     */
    protected void generateUDTRecordGetter(TypedElementDefinition<?> column, int index, JavaWriter out) {
        generateRecordGetter0(column, index, out);
    }

    private final void generateRecordGetter0(TypedElementDefinition<?> column, int index, JavaWriter out) {
        final String comment = StringUtils.defaultString(column.getComment());
        final String getter = getStrategy().getJavaGetterName(column, Mode.RECORD);
        final String typeFull = getJavaType(column.getType(resolver()));
        final String type = out.ref(typeFull);
        final String name = column.getQualifiedOutputName();

        if (!printDeprecationIfUnknownType(out, typeFull))
            out.tab(1).javadoc("Getter for <code>%s</code>.%s", name, columnComment(column, comment));

        if (column instanceof ColumnDefinition)
            printColumnJPAAnnotation(out, (ColumnDefinition) column);
        printValidationAnnotation(out, column);

        if (scala) {
            out.tab(1).println("def %s : %s = {", getter, type);
            out.tab(2).println("val r = get(%s)", index);
            out.tab(2).println("if (r == null) null else r.asInstanceOf[%s]", type);
            out.tab(1).println("}");
        }
        else {
            out.tab(1).overrideIf(generateInterfaces());
            out.tab(1).println("public %s %s() {", type, getter);

            // [#6705] Avoid generating code with a redundant (Object) cast
            if ("java.lang.Object".equals(typeFull))
                out.tab(2).println("return get(%s);", index);
            else
                out.tab(2).println("return (%s) get(%s);", type, index);

            out.tab(1).println("}");
        }
    }

    private int colRefSegments(TypedElementDefinition<?> column) {
        if (column != null && column.getContainer() instanceof UDTDefinition)
            return 2;

        if (!getStrategy().getInstanceFields())
            return 2;

        return 3;
    }

    /**
     * Subclasses may override this method to provide record class footer code.
     */
    @SuppressWarnings("unused")
    protected void generateRecordClassFooter(TableDefinition table, JavaWriter out) {}

    /**
     * Subclasses may override this method to provide their own Javadoc.
     */
    protected void generateRecordClassJavadoc(TableDefinition table, JavaWriter out) {
        if (generateCommentsOnTables())
            printClassJavadoc(out, table);
        else
            printClassJavadoc(out, "The table <code>" + table.getQualifiedInputName() + "</code>.");
    }

    /**
     * Subclasses may override this method to provide record class footer code.
     */
    @SuppressWarnings("unused")
    protected void generateEmbeddableClassFooter(EmbeddableDefinition embeddable, JavaWriter out) {}

    /**
     * Subclasses may override this method to provide their own Javadoc.
     */
    protected void generateEmbeddableClassJavadoc(EmbeddableDefinition embeddable, JavaWriter out) {
        printClassJavadoc(out, "The embeddable <code>" + embeddable.getQualifiedInputName() + "</code>.");
    }

    private String refRowType(JavaWriter out, Collection<? extends TypedElementDefinition<?>> columns) {
        StringBuilder result = new StringBuilder();
        String separator = "";

        for (TypedElementDefinition<?> column : columns) {
            result.append(separator);
            result.append(out.ref(getJavaType(column.getType(resolver()))));

            separator = ", ";
        }

        return result.toString();
    }

    protected void generateInterfaces(SchemaDefinition schema) {
        log.info("Generating table interfaces");

        for (TableDefinition table : database.getTables(schema)) {
            try {
                generateInterface(table);
            } catch (Exception e) {
                log.error("Error while generating table interface " + table, e);
            }
        }

        watch.splitInfo("Table interfaces generated");
    }

    protected void generateInterface(TableDefinition table) {
        JavaWriter out = newJavaWriter(getFile(table, Mode.INTERFACE));
        log.info("Generating interface", out.file().getName());
        generateInterface(table, out);
        closeJavaWriter(out);
    }

    protected void generateUDTInterface(UDTDefinition udt) {
        JavaWriter out = newJavaWriter(getFile(udt, Mode.INTERFACE));
        log.info("Generating interface", out.file().getName());
        generateInterface0(udt, out);
        closeJavaWriter(out);
    }

    protected void generateInterface(TableDefinition table, JavaWriter out) {
        generateInterface0(table, out);
    }

    protected void generateUDTInterface(UDTDefinition udt, JavaWriter out) {
        generateInterface0(udt, out);
    }

    private final void generateInterface0(Definition tableOrUDT, JavaWriter out) {
        final String className = getStrategy().getJavaClassName(tableOrUDT, Mode.INTERFACE);
        final List<String> interfaces = out.ref(getStrategy().getJavaClassImplements(tableOrUDT, Mode.INTERFACE));

        printPackage(out, tableOrUDT, Mode.INTERFACE);
        if (tableOrUDT instanceof TableDefinition)
            generateInterfaceClassJavadoc((TableDefinition) tableOrUDT, out);
        else
            generateUDTInterfaceClassJavadoc((UDTDefinition) tableOrUDT, out);

        printClassAnnotations(out, tableOrUDT.getSchema());

        if (tableOrUDT instanceof TableDefinition)
            printTableJPAAnnotation(out, (TableDefinition) tableOrUDT);

        if (scala)
            out.println("trait %s[[before= extends ][%s]] {", className, interfaces);
        else
            out.println("public interface %s[[before= extends ][%s]] {", className, interfaces);

        List<? extends TypedElementDefinition<?>> typedElements = getTypedElements(tableOrUDT);
        for (int i = 0; i < typedElements.size(); i++) {
            TypedElementDefinition<?> column = typedElements.get(i);

            if (!generateImmutableInterfaces())
                if (tableOrUDT instanceof TableDefinition)
                    generateInterfaceSetter(column, i, out);
                else
                    generateUDTInterfaceSetter(column, i, out);

            if (tableOrUDT instanceof TableDefinition)
                generateInterfaceGetter(column, i, out);
            else
                generateUDTInterfaceGetter(column, i, out);
        }

        if (!generateImmutableInterfaces()) {
            String local = getStrategy().getJavaClassName(tableOrUDT, Mode.INTERFACE);
            String qualified = out.ref(getStrategy().getFullJavaClassName(tableOrUDT, Mode.INTERFACE));

            out.tab(1).header("FROM and INTO");

            out.tab(1).javadoc("Load data from another generated Record/POJO implementing the common interface %s", local);

            if (scala)
                out.tab(1).println("def from(from : %s)", qualified);
            else
                out.tab(1).println("public void from(%s from);", qualified);

            out.tab(1).javadoc("Copy data into another generated Record/POJO implementing the common interface %s", local);

            if (scala)
                out.tab(1).println("def into [E <: %s](into : E) : E", qualified);
            else
                out.tab(1).println("public <E extends %s> E into(E into);", qualified);
        }


        if (tableOrUDT instanceof TableDefinition)
            generateInterfaceClassFooter((TableDefinition) tableOrUDT, out);
        else
            generateUDTInterfaceClassFooter((UDTDefinition) tableOrUDT, out);

        out.println("}");
    }

    /**
     * Subclasses may override this method to provide their own interface setters.
     */
    protected void generateInterfaceSetter(TypedElementDefinition<?> column, int index, JavaWriter out) {
        generateInterfaceSetter0(column, index, out);
    }

    /**
     * Subclasses may override this method to provide their own interface setters.
     */
    protected void generateUDTInterfaceSetter(TypedElementDefinition<?> column, int index, JavaWriter out) {
        generateInterfaceSetter0(column, index, out);
    }

    private final void generateInterfaceSetter0(TypedElementDefinition<?> column, @SuppressWarnings("unused") int index, JavaWriter out) {
        final String className = getStrategy().getJavaClassName(column.getContainer(), Mode.INTERFACE);
        final String comment = StringUtils.defaultString(column.getComment());
        final String setterReturnType = generateFluentSetters() ? className : "void";
        final String setter = getStrategy().getJavaSetterName(column, Mode.INTERFACE);
        final String typeFull = getJavaType(column.getType(resolver(Mode.INTERFACE)), Mode.INTERFACE);
        final String type = out.ref(typeFull);
        final String name = column.getQualifiedOutputName();

        if (!printDeprecationIfUnknownType(out, typeFull))
            out.tab(1).javadoc("Setter for <code>%s</code>.%s", name, columnComment(column, comment));

        if (scala)
            out.tab(1).println("def %s(value : %s) : %s", setter, type, setterReturnType);
        else
            out.tab(1).println("public %s %s(%s value);", setterReturnType, setter, varargsIfArray(type));
    }

    /**
     * Subclasses may override this method to provide their own interface getters.
     */
    protected void generateInterfaceGetter(TypedElementDefinition<?> column, int index, JavaWriter out) {
        generateInterfaceGetter0(column, index, out);
    }

    /**
     * Subclasses may override this method to provide their own interface getters.
     */
    protected void generateUDTInterfaceGetter(TypedElementDefinition<?> column, int index, JavaWriter out) {
        generateInterfaceGetter0(column, index, out);
    }

    private final void generateInterfaceGetter0(TypedElementDefinition<?> column, @SuppressWarnings("unused") int index, JavaWriter out) {
        final String comment = StringUtils.defaultString(column.getComment());
        final String getter = getStrategy().getJavaGetterName(column, Mode.INTERFACE);
        final String typeFull = getJavaType(column.getType(resolver(Mode.INTERFACE)), Mode.INTERFACE);
        final String type = out.ref(typeFull);
        final String name = column.getQualifiedOutputName();

        if (!printDeprecationIfUnknownType(out, typeFull))
            out.tab(1).javadoc("Getter for <code>%s</code>.%s", name, columnComment(column, comment));

        if (column instanceof ColumnDefinition)
            printColumnJPAAnnotation(out, (ColumnDefinition) column);

        printValidationAnnotation(out, column);

        if (scala)
            out.tab(1).println("def %s : %s", getter, type);
        else
            out.tab(1).println("public %s %s();", type, getter);
    }

    private String columnComment(TypedElementDefinition<?> column, String comment) {
        return column instanceof ColumnDefinition && generateCommentsOnColumns()
            || column instanceof AttributeDefinition && generateCommentsOnAttributes()
            ?  defaultIfBlank(" " + escapeEntities(comment), "")
            :  "";
    }

    private String parameterComment(String comment) {
        return generateCommentsOnParameters()
            ?  defaultIfBlank(" " + escapeEntities(comment), "")
            :  "";
    }

    /**
     * Subclasses may override this method to provide interface class footer
     * code.
     */
    @SuppressWarnings("unused")
    protected void generateInterfaceClassFooter(TableDefinition table, JavaWriter out) {}

    /**
     * Subclasses may override this method to provide their own Javadoc.
     */
    protected void generateInterfaceClassJavadoc(TableDefinition table, JavaWriter out) {
        if (generateCommentsOnTables())
            printClassJavadoc(out, table);
        else
            printClassJavadoc(out, "The table <code>" + table.getQualifiedInputName() + "</code>.");
    }

    protected void generateUDTs(SchemaDefinition schema) {
        log.info("Generating UDTs");

        for (UDTDefinition udt : database.getUDTs(schema)) {
            try {
                generateUDT(schema, udt);
            } catch (Exception e) {
                log.error("Error while generating udt " + udt, e);
            }
        }

        watch.splitInfo("UDTs generated");
    }

    @SuppressWarnings("unused")
    protected void generateUDT(SchemaDefinition schema, UDTDefinition udt) {
        JavaWriter out = newJavaWriter(getFile(udt));
        log.info("Generating UDT ", out.file().getName());
        generateUDT(udt, out);
        closeJavaWriter(out);
    }

    protected void generateUDT(UDTDefinition udt, JavaWriter out) {
        final SchemaDefinition schema = udt.getSchema();
        final PackageDefinition pkg = udt.getPackage();
        final boolean synthetic = udt.isSynthetic();
        final String className = getStrategy().getJavaClassName(udt);
        final String recordType = out.ref(getStrategy().getFullJavaClassName(udt, Mode.RECORD));
        final List<String> interfaces = out.ref(getStrategy().getJavaClassImplements(udt, Mode.DEFAULT));
        final String schemaId = out.ref(getStrategy().getFullJavaIdentifier(schema), 2);
        final String packageId = pkg == null ? null : out.ref(getStrategy().getFullJavaIdentifier(pkg), 2);
        final String udtId = out.ref(getStrategy().getJavaIdentifier(udt), 2);

        printPackage(out, udt);

        if (scala) {
            out.println("object %s {", className);
            printSingletonInstance(out, udt);

            for (AttributeDefinition attribute : udt.getAttributes()) {
                final String attrId = out.ref(getStrategy().getJavaIdentifier(attribute), 2);
                final String attrComment = StringUtils.defaultString(attribute.getComment());

                out.tab(1).javadoc("The attribute <code>%s</code>.%s", attribute.getQualifiedOutputName(), columnComment(attribute, attrComment));
                out.tab(1).println("val %s = %s.%s", attrId, udtId, attrId);
            }

            out.println("}");
            out.println();
        }

        generateUDTClassJavadoc(udt, out);
        printClassAnnotations(out, schema);








        if (scala) {
            out.println("class %s extends %s[%s](\"%s\", null, %s, %s)[[before= with ][separator= with ][%s]] {", className, UDTImpl.class, recordType, udt.getOutputName(), packageId, synthetic, interfaces);
        }
        else {
            out.println("public class %s extends %s<%s>[[before= implements ][%s]] {", className, UDTImpl.class, recordType, interfaces);
            out.printSerial();
            printSingletonInstance(out, udt);
        }

        printRecordTypeMethod(out, udt);

        for (AttributeDefinition attribute : udt.getAttributes()) {
            final String attrTypeFull = getJavaType(attribute.getType(resolver()));
            final String attrType = out.ref(attrTypeFull);
            final String attrTypeRef = getJavaTypeReference(attribute.getDatabase(), attribute.getType(resolver()));
            final String attrId = out.ref(getStrategy().getJavaIdentifier(attribute), 2);
            final String attrName = attribute.getName();
            final String attrComment = StringUtils.defaultString(attribute.getComment());
            final List<String> converter = out.ref(list(attribute.getType(resolver()).getConverter()));
            final List<String> binding = out.ref(list(attribute.getType(resolver()).getBinding()));

            if (scala) {
                printDeprecationIfUnknownType(out, attrTypeFull);
                out.tab(1).println("private val %s : %s[%s, %s] = %s.createField(%s.name(\"%s\"), %s, this, \"%s\"" + converterTemplate(converter) + converterTemplate(binding) + ")",
                    attrId, UDTField.class, recordType, attrType, UDTImpl.class, DSL.class, attrName, attrTypeRef, escapeString(""), converter, binding);
            }
            else {
                if (!printDeprecationIfUnknownType(out, attrTypeFull))
                    out.tab(1).javadoc("The attribute <code>%s</code>.%s", attribute.getQualifiedOutputName(), columnComment(attribute, attrComment));

                out.tab(1).println("public static final %s<%s, %s> %s = createField(%s.name(\"%s\"), %s, %s, \"%s\"" + converterTemplate(converter) + converterTemplate(binding) + ");",
                    UDTField.class, recordType, attrType, attrId, DSL.class, attrName, attrTypeRef, udtId, escapeString(""), converter, binding);
            }
        }

        // [#799] Oracle UDT's can have member procedures
        for (RoutineDefinition routine : udt.getRoutines()) {
            try {
                if (!routine.isSQLUsable()) {

                    // Static execute() convenience method
                    printConvenienceMethodProcedure(out, routine, false);
                }
                else {

                    // Static execute() convenience method
                    if (!routine.isAggregate()) {
                        printConvenienceMethodFunction(out, routine, false);
                    }

                    // Static asField() convenience method
                    printConvenienceMethodFunctionAsField(out, routine, false);
                    printConvenienceMethodFunctionAsField(out, routine, true);
                }

            } catch (Exception e) {
                log.error("Error while generating routine " + routine, e);
            }
        }

        if (scala) {
        }
        else {
            out.tab(1).javadoc(NO_FURTHER_INSTANCES_ALLOWED);
            out.tab(1).println("private %s() {", className);
            out.tab(2).println("super(\"%s\", null, %s, %s);", udt.getOutputName(), packageId, synthetic);
            out.tab(1).println("}");
        }

        if (scala) {
            out.println();
            out.tab(1).println("override def getSchema : %s = %s", Schema.class, schemaId);
        }
        else {
            out.tab(1).overrideInherit();
            out.tab(1).println("public %s getSchema() {", Schema.class);
            out.tab(2).println("return %s != null ? %s : new %s(%s.name(\"%s\"));", schemaId, schemaId, SchemaImpl.class, DSL.class, schema.getOutputName());
            out.tab(1).println("}");
        }

        generateUDTClassFooter(udt, out);
        out.println("}");
        closeJavaWriter(out);
    }

    /**
     * Subclasses may override this method to provide udt class footer code.
     */
    @SuppressWarnings("unused")
    protected void generateUDTClassFooter(UDTDefinition udt, JavaWriter out) {}

    /**
     * Subclasses may override this method to provide their own Javadoc.
     */
    protected void generateUDTClassJavadoc(UDTDefinition udt, JavaWriter out) {
        if (generateCommentsOnUDTs())
            printClassJavadoc(out, udt);
        else
            printClassJavadoc(out, "The udt <code>" + udt.getQualifiedInputName() + "</code>.");
    }

    protected void generateUDTPojos(SchemaDefinition schema) {
        log.info("Generating UDT POJOs");

        for (UDTDefinition udt : database.getUDTs(schema)) {
            try {
                generateUDTPojo(udt);
            }
            catch (Exception e) {
                log.error("Error while generating UDT POJO " + udt, e);
            }
        }

        watch.splitInfo("UDT POJOs generated");
    }

    /**
     * Subclasses may override this method to provide UDT POJO class footer code.
     */
    @SuppressWarnings("unused")
    protected void generateUDTPojoClassFooter(UDTDefinition udt, JavaWriter out) {}

    /**
     * Subclasses may override this method to provide their own Javadoc.
     */
    protected void generateUDTPojoClassJavadoc(UDTDefinition udt, JavaWriter out) {
        if (generateCommentsOnUDTs())
            printClassJavadoc(out, udt);
        else
            printClassJavadoc(out, "The udt <code>" + udt.getQualifiedInputName() + "</code>.");
    }

    protected void generateUDTInterfaces(SchemaDefinition schema) {
        log.info("Generating UDT interfaces");

        for (UDTDefinition udt : database.getUDTs(schema)) {
            try {
                generateUDTInterface(udt);
            } catch (Exception e) {
                log.error("Error while generating UDT interface " + udt, e);
            }
        }

        watch.splitInfo("UDT interfaces generated");
    }

    /**
     * Subclasses may override this method to provide UDT interface class footer code.
     */
    @SuppressWarnings("unused")
    protected void generateUDTInterfaceClassFooter(UDTDefinition udt, JavaWriter out) {}

    /**
     * Subclasses may override this method to provide their own Javadoc.
     */
    protected void generateUDTInterfaceClassJavadoc(UDTDefinition udt, JavaWriter out) {
        if (generateCommentsOnUDTs())
            printClassJavadoc(out, udt);
        else
            printClassJavadoc(out, "The udt <code>" + udt.getQualifiedInputName() + "</code>.");
    }

    /**
     * Generating UDT record classes
     */
    protected void generateUDTRecords(SchemaDefinition schema) {
        log.info("Generating UDT records");

        for (UDTDefinition udt : database.getUDTs(schema)) {
            try {
                generateUDTRecord(udt);
            } catch (Exception e) {
                log.error("Error while generating UDT record " + udt, e);
            }
        }

        watch.splitInfo("UDT records generated");
    }

    /**
     * Subclasses may override this method to provide udt record class footer code.
     */
    @SuppressWarnings("unused")
    protected void generateUDTRecordClassFooter(UDTDefinition udt, JavaWriter out) {}

    /**
     * Subclasses may override this method to provide their own Javadoc.
     */
    protected void generateUDTRecordClassJavadoc(UDTDefinition udt, JavaWriter out) {
        if (generateCommentsOnUDTs())
            printClassJavadoc(out, udt);
        else
            printClassJavadoc(out, "The udt <code>" + udt.getQualifiedInputName() + "</code>.");
    }

    protected void generateUDTRoutines(SchemaDefinition schema) {
        for (UDTDefinition udt : database.getUDTs(schema)) {
            if (udt.getRoutines().size() > 0) {
                try {
                    log.info("Generating member routines");

                    for (RoutineDefinition routine : udt.getRoutines()) {
                        try {
                            generateRoutine(schema, routine);
                        } catch (Exception e) {
                            log.error("Error while generating member routines " + routine, e);
                        }
                    }
                } catch (Exception e) {
                    log.error("Error while generating UDT " + udt, e);
                }

                watch.splitInfo("Member procedures routines");
            }
        }
    }

    /**
     * Generating central static udt access
     */
    protected void generateUDTReferences(SchemaDefinition schema) {
        log.info("Generating UDT references");
        JavaWriter out = newJavaWriter(new File(getFile(schema).getParentFile(), "UDTs.java"));

        printPackage(out, schema);
        printClassJavadoc(out, "Convenience access to all UDTs in " + schema.getOutputName());
        printClassAnnotations(out, schema);

        if (scala)
            out.println("object UDTs {");
        else
            out.println("public class UDTs {");

        for (UDTDefinition udt : database.getUDTs(schema)) {
            final String className = out.ref(getStrategy().getFullJavaClassName(udt));
            final String id = getStrategy().getJavaIdentifier(udt);
            final String fullId = getStrategy().getFullJavaIdentifier(udt);

            out.tab(1).javadoc("The type <code>%s</code>", udt.getQualifiedOutputName());

            if (scala)
                out.tab(1).println("val %s = %s", id, fullId);
            else
                out.tab(1).println("public static %s %s = %s;", className, id, fullId);
        }

        out.println("}");
        closeJavaWriter(out);

        watch.splitInfo("UDT references generated");
    }

    protected void generateArrays(SchemaDefinition schema) {
        log.info("Generating ARRAYs");

        for (ArrayDefinition array : database.getArrays(schema)) {
            try {
                generateArray(schema, array);
            } catch (Exception e) {
                log.error("Error while generating ARRAY record " + array, e);
            }
        }

        watch.splitInfo("ARRAYs generated");
    }

    @SuppressWarnings("unused")
    protected void generateArray(SchemaDefinition schema, ArrayDefinition array) {
        JavaWriter out = newJavaWriter(getFile(array, Mode.RECORD));
        log.info("Generating ARRAY", out.file().getName());
        generateArray(array, out);
        closeJavaWriter(out);
    }


    protected void generateArray(ArrayDefinition array, JavaWriter out) {


































































































    }

    /**
     * Subclasses may override this method to provide array class footer code.
     */
    @SuppressWarnings("unused")
    protected void generateArrayClassFooter(ArrayDefinition array, JavaWriter out) {}

    /**
     * Subclasses may override this method to provide their own Javadoc.
     */
    protected void generateArrayClassJavadoc(ArrayDefinition array, JavaWriter out) {
        if (generateCommentsOnUDTs())
            printClassJavadoc(out, array);
        else
            printClassJavadoc(out, "The type <code>" + array.getQualifiedInputName() + "</code>.");
    }

    protected void generateEnums(SchemaDefinition schema) {
        log.info("Generating ENUMs");

        for (EnumDefinition e : database.getEnums(schema)) {
            try {
                generateEnum(e);
            } catch (Exception ex) {
                log.error("Error while generating enum " + e, ex);
            }
        }

        watch.splitInfo("Enums generated");
    }

    protected void generateDomains(SchemaDefinition schema) {
        log.info("Generating DOMAINs");

        for (DomainDefinition d : database.getDomains(schema)) {
            try {
                generateDomain(d);
            } catch (Exception ex) {
                log.error("Error while generating domain " + d, ex);
            }
        }

        watch.splitInfo("Domains generated");
    }

    protected void generateEnum(EnumDefinition e) {
        JavaWriter out = newJavaWriter(getFile(e, Mode.ENUM));
        log.info("Generating ENUM", out.file().getName());
        generateEnum(e, out);
        closeJavaWriter(out);
    }

    protected void generateEnum(EnumDefinition e, JavaWriter out) {
        final String className = getStrategy().getJavaClassName(e, Mode.ENUM);
        final List<String> interfaces = out.ref(getStrategy().getJavaClassImplements(e, Mode.ENUM));
        final List<String> literals = e.getLiterals();
        final List<String> identifiers = new ArrayList<>(literals.size());

        for (int i = 0; i < literals.size(); i++) {
            String identifier = convertToIdentifier(literals.get(i), language);

            // [#2781] Disambiguate collisions with the leading package name
            if (identifier.equals(getStrategy().getJavaPackageName(e).replaceAll("\\..*", "")))
                identifier += "_";

            identifiers.add(identifier);
        }

        printPackage(out, e);
        generateEnumClassJavadoc(e, out);
        printClassAnnotations(out, e.getSchema());


        boolean enumHasNoSchema = e.isSynthetic() || !(e.getDatabase() instanceof PostgresDatabase);
        if (scala) {
            out.println("object %s {", className);
            out.println();

            for (int i = 0; i < identifiers.size(); i++) {
                out.tab(1).println("val %s : %s = %s.%s", identifiers.get(i), className, getStrategy().getJavaPackageName(e), identifiers.get(i));
            }

            out.println();
            out.tab(1).println("def values : %s[%s] = %s(",
                out.ref("scala.Array"),
                className,
                out.ref("scala.Array"));

            for (int i = 0; i < identifiers.size(); i++) {
                out.tab(2).print((i > 0 ? ", " : "  "));
                out.println(identifiers.get(i));
            }

            out.tab(1).println(")");
            out.println();

            out.tab(1).println("def valueOf(s : %s) : %s = s match {", String.class, className);
            for (int i = 0; i < identifiers.size(); i++) {
                out.tab(2).println("case \"%s\" => %s", literals.get(i), identifiers.get(i));
            }
            out.tab(2).println("case _ => throw new %s()", IllegalArgumentException.class);
            out.tab(1).println("}");
            out.println("}");

            out.println();
            out.println("sealed trait %s extends %s[[before= with ][%s]] {", className, EnumType.class, interfaces);

            if (enumHasNoSchema)
                out.tab(1).println("override def getCatalog : %s = null", Catalog.class);
            else
                out.tab(1).println("override def getCatalog : %s = if (getSchema == null) null else getSchema().getCatalog()", Catalog.class);

            // [#2135] Only the PostgreSQL database supports schema-scoped enum types
            out.tab(1).println("override def getSchema : %s = %s",
                Schema.class,
                enumHasNoSchema
                    ? "null"
                    : out.ref(getStrategy().getFullJavaIdentifier(e.getSchema()), 2));
            out.tab(1).println("override def getName : %s = %s",
                String.class,
                e.isSynthetic() ? "null" : "\"" + e.getName().replace("\"", "\\\"") + "\"");

            generateEnumClassFooter(e, out);
            out.println("}");

            for (int i = 0; i < literals.size(); i++) {
                out.println();
                out.println("case object %s extends %s {", identifiers.get(i), className);
                out.tab(1).println("override def getLiteral : %s = \"%s\"",
                    String.class,
                    literals.get(i));
                out.println("}");
            }
        }
        else {
            interfaces.add(out.ref(EnumType.class));
            out.println("public enum %s[[before= implements ][%s]] {", className, interfaces);

            for (int i = 0; i < literals.size(); i++) {
                out.println();
                out.tab(1).println("%s(\"%s\")%s", identifiers.get(i), literals.get(i), (i == literals.size() - 1) ? ";" : ",");
            }

            out.println();
            out.tab(1).println("private final %s literal;", String.class);
            out.println();
            out.tab(1).println("private %s(%s literal) {", className, String.class);
            out.tab(2).println("this.literal = literal;");
            out.tab(1).println("}");

            out.tab(1).overrideInherit();
            out.tab(1).println("public %s getCatalog() {", Catalog.class);

            if (enumHasNoSchema)
                out.tab(2).println("return null;");
            else
                out.tab(2).println("return getSchema() == null ? null : getSchema().getCatalog();");

            out.tab(1).println("}");

            // [#2135] Only the PostgreSQL database supports schema-scoped enum types
            out.tab(1).overrideInherit();
            out.tab(1).println("public %s getSchema() {", Schema.class);
            out.tab(2).println("return %s;",
                enumHasNoSchema
                    ? "null"
                    : out.ref(getStrategy().getFullJavaIdentifier(e.getSchema()), 2));
            out.tab(1).println("}");

            out.tab(1).overrideInherit();
            out.tab(1).println("public %s getName() {", String.class);
            out.tab(2).println("return %s;", e.isSynthetic() ? "null" : "\"" + e.getName().replace("\"", "\\\"") + "\"");
            out.tab(1).println("}");

            out.tab(1).overrideInherit();
            out.tab(1).println("public %s getLiteral() {", String.class);
            out.tab(2).println("return literal;");
            out.tab(1).println("}");

            generateEnumClassFooter(e, out);
            out.println("}");
        }
    }

    /**
     * Subclasses may override this method to provide enum class footer code.
     */
    @SuppressWarnings("unused")
    protected void generateEnumClassFooter(EnumDefinition e, JavaWriter out) {}

    /**
     * Subclasses may override this method to provide their own Javadoc.
     */
    protected void generateEnumClassJavadoc(EnumDefinition e, JavaWriter out) {
        if (generateCommentsOnUDTs())
            printClassJavadoc(out, e);
        else
            printClassJavadoc(out, "The enum <code>" + e.getQualifiedInputName() + "</code>.");
    }

    protected void generateDomain(DomainDefinition d) {
        JavaWriter out = newJavaWriter(getFile(d, Mode.DOMAIN));
        log.info("Generating DOMAIN", out.file().getName());
        generateDomain(d, out);
        closeJavaWriter(out);
    }

    protected void generateDomain(DomainDefinition d, JavaWriter out) {
        final String className = getStrategy().getJavaClassName(d, Mode.DOMAIN);
        final String superName = out.ref(getStrategy().getJavaClassExtends(d, Mode.DOMAIN));
        final List<String> interfaces = out.ref(getStrategy().getJavaClassImplements(d, Mode.DOMAIN));
        final List<String> superTypes = list(superName, interfaces);

        printPackage(out, d);
        generateDomainClassJavadoc(d, out);
        printClassAnnotations(out, d.getSchema());

        for (String clause : d.getCheckClauses())
            out.println("// " + clause);

        if (scala)
            out.println("class %s[[before= extends ][%s]][[before= with ][separator= with ][%s]] {", className, first(superTypes), remaining(superTypes));
        else
            out.println("public class %s[[before= extends ][%s]][[before= implements ][%s]] {", className, list(superName), interfaces);

        generateDomainClassFooter(d, out);
        out.println("}");
    }

    /**
     * Subclasses may override this method to provide enum class footer code.
     */
    @SuppressWarnings("unused")
    protected void generateDomainClassFooter(DomainDefinition d, JavaWriter out) {}

    /**
     * Subclasses may override this method to provide their own Javadoc.
     */
    protected void generateDomainClassJavadoc(DomainDefinition e, JavaWriter out) {
        if (generateCommentsOnUDTs())
            printClassJavadoc(out, e);
        else
            printClassJavadoc(out, "The domain <code>" + e.getQualifiedInputName() + "</code>.");
    }

    protected void generateRoutines(SchemaDefinition schema) {
        log.info("Generating routines and table-valued functions");

        if (generateGlobalRoutineReferences()) {
            JavaWriter out = newJavaWriter(new File(getFile(schema).getParentFile(), "Routines.java"));
            printPackage(out, schema);
            printClassJavadoc(out, "Convenience access to all stored procedures and functions in " + schema.getOutputName());
            printClassAnnotations(out, schema);

            if (scala)
                out.println("object Routines {");
            else
                out.println("public class Routines {");

            for (RoutineDefinition routine : database.getRoutines(schema))
                printRoutine(out, routine);

            for (TableDefinition table : database.getTables(schema)) {
                if (table.isTableValuedFunction()) {
                    printTableValuedFunction(out, table, getStrategy().getJavaMethodName(table, Mode.DEFAULT));
                }
            }

            out.println("}");
            closeJavaWriter(out);
        }

        for (RoutineDefinition routine : database.getRoutines(schema)) {
            try {
                generateRoutine(schema, routine);
            }
            catch (Exception e) {
                log.error("Error while generating routine " + routine, e);
            }
        }

        watch.splitInfo("Routines generated");
    }

    protected void printConstant(JavaWriter out, AttributeDefinition constant) {


























    }

    protected void printRoutine(JavaWriter out, RoutineDefinition routine) {
        if (!routine.isSQLUsable()) {

            // Static execute() convenience method
            printConvenienceMethodProcedure(out, routine, false);
        }
        else {

            // Static execute() convenience method
            // [#457] This doesn't make any sense for user-defined aggregates
            if (!routine.isAggregate()) {
                printConvenienceMethodFunction(out, routine, false);
            }

            // Static asField() convenience method
            printConvenienceMethodFunctionAsField(out, routine, false);
            printConvenienceMethodFunctionAsField(out, routine, true);
        }
    }

    protected void printTableValuedFunction(JavaWriter out, TableDefinition table, String javaMethodName) {
        printConvenienceMethodTableValuedFunction(out, table, javaMethodName);
        printConvenienceMethodTableValuedFunctionAsField(out, table, false, javaMethodName);
        printConvenienceMethodTableValuedFunctionAsField(out, table, true, javaMethodName);
    }

    protected void generatePackages(SchemaDefinition schema) {













    }

    @SuppressWarnings("unused")
    protected void generatePackage(SchemaDefinition schema, PackageDefinition pkg) {






    }

    protected void generatePackage(PackageDefinition pkg, JavaWriter out) {



















































    }

    /**
     * Subclasses may override this method to provide package class footer code.
     */
    @SuppressWarnings("unused")
    protected void generatePackageClassFooter(PackageDefinition pkg, JavaWriter out) {}

    /**
     * Subclasses may override this method to provide their own Javadoc.
     */
    protected void generatePackageClassJavadoc(PackageDefinition pkg, JavaWriter out) {
        printClassJavadoc(out, "Convenience access to all stored procedures and functions in " + pkg.getName());
    }

    /**
     * Generating central static table access
     */
    protected void generateTableReferences(SchemaDefinition schema) {
        log.info("Generating table references");
        JavaWriter out = newJavaWriter(new File(getFile(schema).getParentFile(), "Tables.java"));

        printPackage(out, schema);
        printClassJavadoc(out, "Convenience access to all tables in " + schema.getOutputName());
        printClassAnnotations(out, schema);

        if (scala)
            out.println("object Tables {");
        else
            out.println("public class Tables {");

        for (TableDefinition table : database.getTables(schema)) {
            final String className = getStrategy().getJavaClassName(table);
            final String fullClassName = scala
                ? ""
                : out.ref(getStrategy().getFullJavaClassName(table));
            final String id = getStrategy().getJavaIdentifier(table);

            // [#8863] Use the imported table class to dereference the singleton
            //         table instance, *only* if the class name is not equal to
            //         the instance name. Otherwise, we would get a
            //         "error: self-reference in initializer" compilation error
            final String referencedId = className.equals(id)
                ? getStrategy().getFullJavaIdentifier(table)
                : out.ref(getStrategy().getFullJavaIdentifier(table), 2);
            final String comment = !StringUtils.isBlank(table.getComment()) && generateCommentsOnTables()
                ? escapeEntities(table.getComment())
                : "The table <code>" + table.getQualifiedOutputName() + "</code>.";

            // [#4883] Scala doesn't have separate namespaces for val and def
            if (scala && table.isTableValuedFunction() && table.getParameters().isEmpty())
                ;
            else {
                out.tab(1).javadoc(comment);

                if (scala)
                    out.tab(1).println("val %s = %s", id, referencedId);
                else
                    out.tab(1).println("public static final %s %s = %s;", fullClassName, id, referencedId);
            }

            // [#3797] Table-valued functions generate two different literals in
            // globalObjectReferences
            if (table.isTableValuedFunction())
                printTableValuedFunction(out, table, getStrategy().getJavaIdentifier(table));
        }

        out.println("}");
        closeJavaWriter(out);

        watch.splitInfo("Table refs generated");
    }

    protected void generateDaos(SchemaDefinition schema) {
        log.info("Generating DAOs");

        for (TableDefinition table : database.getTables(schema)) {
            try {
                generateDao(table);
            }
            catch (Exception e) {
                log.error("Error while generating table DAO " + table, e);
            }
        }

        watch.splitInfo("Table DAOs generated");
    }

    protected void generateDao(TableDefinition table) {
        JavaWriter out = newJavaWriter(getFile(table, Mode.DAO));
        log.info("Generating DAO", out.file().getName());
        generateDao(table, out);
        closeJavaWriter(out);
    }

    protected void generateDao(TableDefinition table, JavaWriter out) {
        UniqueKeyDefinition key = table.getPrimaryKey();
        if (key == null) {
            log.info("Skipping DAO generation", out.file().getName());
            return;
        }

        final String className = getStrategy().getJavaClassName(table, Mode.DAO);
        final List<String> interfaces = out.ref(getStrategy().getJavaClassImplements(table, Mode.DAO));
        final String tableRecord = out.ref(getStrategy().getFullJavaClassName(table, Mode.RECORD));
        final String daoImpl = out.ref(DAOImpl.class);
        final String tableIdentifier = out.ref(getStrategy().getFullJavaIdentifier(table), 2);

        String tType = (scala ? "Unit" : "Void");
        String pType = out.ref(getStrategy().getFullJavaClassName(table, Mode.POJO));

        List<ColumnDefinition> keyColumns = key.getKeyColumns();

        if (keyColumns.size() == 1) {
            tType = getJavaType(keyColumns.get(0).getType(resolver()), Mode.POJO);
        }
        else if (keyColumns.size() <= Constants.MAX_ROW_DEGREE) {
            String generics = "";
            String separator = "";

            for (ColumnDefinition column : keyColumns) {
                generics += separator + out.ref(getJavaType(column.getType(resolver())));
                separator = ", ";
            }

            if (scala)
                tType = Record.class.getName() + keyColumns.size() + "[" + generics + "]";
            else
                tType = Record.class.getName() + keyColumns.size() + "<" + generics + ">";
        }
        else {
            tType = Record.class.getName();
        }

        tType = out.ref(tType);

        printPackage(out, table, Mode.DAO);
        generateDaoClassJavadoc(table, out);
        printClassAnnotations(out, table.getSchema());

        if (generateSpringAnnotations())
            out.println("@%s", out.ref("org.springframework.stereotype.Repository"));

        if (scala)
            out.println("class %s(configuration : %s) extends %s[%s, %s, %s](%s, classOf[%s], configuration)[[before= with ][separator= with ][%s]] {",
                    className, Configuration.class, daoImpl, tableRecord, pType, tType, tableIdentifier, pType, interfaces);
        else
            out.println("public class %s extends %s<%s, %s, %s>[[before= implements ][%s]] {", className, daoImpl, tableRecord, pType, tType, interfaces);

        // Default constructor
        // -------------------
        out.tab(1).javadoc("Create a new %s without any configuration", className);

        if (scala) {
            out.tab(1).println("def this() = {");
            out.tab(2).println("this(null)");
            out.tab(1).println("}");
        }
        else {
            out.tab(1).println("public %s() {", className);
            out.tab(2).println("super(%s, %s.class);", tableIdentifier, pType);
            out.tab(1).println("}");
        }

        // Initialising constructor
        // ------------------------

        if (scala) {
        }
        else {
            out.tab(1).javadoc("Create a new %s with an attached configuration", className);

            if (generateSpringAnnotations())
                out.tab(1).println("@%s", out.ref("org.springframework.beans.factory.annotation.Autowired"));

            out.tab(1).println("public %s(%s configuration) {", className, Configuration.class);
            out.tab(2).println("super(%s, %s.class, configuration);", tableIdentifier, pType);
            out.tab(1).println("}");
        }

        // Template method implementations
        // -------------------------------
        if (scala) {
            out.println();
            out.tab(1).println("override def getId(o : %s) : %s = {", pType, tType);
        }
        else {
            out.tab(1).overrideInherit();
            out.tab(1).println("public %s getId(%s object) {", tType, pType);
        }

        if (keyColumns.size() == 1) {
            if (scala)
                out.tab(2).println("o.%s", getStrategy().getJavaGetterName(keyColumns.get(0), Mode.POJO));
            else
                out.tab(2).println("return object.%s();", getStrategy().getJavaGetterName(keyColumns.get(0), Mode.POJO));
        }

        // [#2574] This should be replaced by a call to a method on the target table's Key type
        else {
            String params = "";
            String separator = "";

            for (ColumnDefinition column : keyColumns) {
                if (scala)
                    params += separator + "o." + getStrategy().getJavaGetterName(column, Mode.POJO);
                else
                    params += separator + "object." + getStrategy().getJavaGetterName(column, Mode.POJO) + "()";

                separator = ", ";
            }

            if (scala)
                out.tab(2).println("compositeKeyRecord(%s)", params);
            else
                out.tab(2).println("return compositeKeyRecord(%s);", params);
        }

        out.tab(1).println("}");

        for (ColumnDefinition column : table.getColumns()) {
            final String colName = column.getOutputName();
            final String colClass = getStrategy().getJavaClassName(column);
            final String colTypeFull = getJavaType(column.getType(resolver()));
            final String colType = out.ref(colTypeFull);
            final String colIdentifier = out.ref(getStrategy().getFullJavaIdentifier(column), colRefSegments(column));

            // fetchRangeOf[Column]([T]...)
            // -----------------------
            if (!printDeprecationIfUnknownType(out, colTypeFull))
                out.tab(1).javadoc("Fetch records that have <code>%s BETWEEN lowerInclusive AND upperInclusive</code>", colName);

            if (scala) {
                out.tab(1).println("def fetchRangeOf%s(lowerInclusive : %s, upperInclusive : %s) : %s[%s] = {", colClass, colType, colType, List.class, pType);
                out.tab(2).println("fetchRange(%s, lowerInclusive, upperInclusive)", colIdentifier);
                out.tab(1).println("}");
            }
            else {
                out.tab(1).println("public %s<%s> fetchRangeOf%s(%s lowerInclusive, %s upperInclusive) {", List.class, pType, colClass, colType, colType);
                out.tab(2).println("return fetchRange(%s, lowerInclusive, upperInclusive);", colIdentifier);
                out.tab(1).println("}");
            }

            // fetchBy[Column]([T]...)
            // -----------------------
            if (!printDeprecationIfUnknownType(out, colTypeFull))
                out.tab(1).javadoc("Fetch records that have <code>%s IN (values)</code>", colName);

            if (scala) {
                out.tab(1).println("def fetchBy%s(values : %s*) : %s[%s] = {", colClass, colType, List.class, pType);
                out.tab(2).println("fetch(%s, values:_*)", colIdentifier);
                out.tab(1).println("}");
            }
            else {
                out.tab(1).println("public %s<%s> fetchBy%s(%s... values) {", List.class, pType, colClass, colType);
                out.tab(2).println("return fetch(%s, values);", colIdentifier);
                out.tab(1).println("}");
            }

            // fetchOneBy[Column]([T])
            // -----------------------
            ukLoop:
            for (UniqueKeyDefinition uk : column.getUniqueKeys()) {

                // If column is part of a single-column unique key...
                if (uk.getKeyColumns().size() == 1 && uk.getKeyColumns().get(0).equals(column)) {
                    if (!printDeprecationIfUnknownType(out, colTypeFull))
                        out.tab(1).javadoc("Fetch a unique record that has <code>%s = value</code>", colName);

                    if (scala) {
                        out.tab(1).println("def fetchOneBy%s(value : %s) : %s = {", colClass, colType, pType);
                        out.tab(2).println("fetchOne(%s, value)", colIdentifier);
                        out.tab(1).println("}");
                    }
                    else {
                        out.tab(1).println("public %s fetchOneBy%s(%s value) {", pType, colClass, colType);
                        out.tab(2).println("return fetchOne(%s, value);", colIdentifier);
                        out.tab(1).println("}");
                    }

                    break ukLoop;
                }
            }
        }

        generateDaoClassFooter(table, out);
        out.println("}");
    }

    /**
     * Subclasses may override this method to provide dao class footer code.
     */
    @SuppressWarnings("unused")
    protected void generateDaoClassFooter(TableDefinition table, JavaWriter out) {}

    /**
     * Subclasses may override this method to provide their own Javadoc.
     */
    protected void generateDaoClassJavadoc(TableDefinition table, JavaWriter out) {
        if (generateCommentsOnTables())
            printClassJavadoc(out, table);
        else
            printClassJavadoc(out, "The table <code>" + table.getQualifiedInputName() + "</code>.");
    }

    protected void generatePojos(SchemaDefinition schema) {
        log.info("Generating table POJOs");

        for (TableDefinition table : database.getTables(schema)) {
            try {
                generatePojo(table);
            }
            catch (Exception e) {
                log.error("Error while generating table POJO " + table, e);
            }
        }

        watch.splitInfo("Table POJOs generated");
    }

    protected void generatePojo(TableDefinition table) {
        JavaWriter out = newJavaWriter(getFile(table, Mode.POJO));
        log.info("Generating POJO", out.file().getName());
        generatePojo(table, out);
        closeJavaWriter(out);
    }

    protected void generateUDTPojo(UDTDefinition udt) {
        JavaWriter out = newJavaWriter(getFile(udt, Mode.POJO));
        log.info("Generating POJO", out.file().getName());
        generatePojo0(udt, out);
        closeJavaWriter(out);
    }

    protected void generatePojo(TableDefinition table, JavaWriter out) {
        generatePojo0(table, out);
    }

    protected void generateUDTPojo(UDTDefinition udt, JavaWriter out) {
        generatePojo0(udt, out);
    }

    private final void generatePojo0(Definition tableOrUDT, JavaWriter out) {
        final String className = getStrategy().getJavaClassName(tableOrUDT, Mode.POJO);
        final String interfaceName = generateInterfaces()
            ? out.ref(getStrategy().getFullJavaClassName(tableOrUDT, Mode.INTERFACE))
            : "";
        final String superName = out.ref(getStrategy().getJavaClassExtends(tableOrUDT, Mode.POJO));
        final List<String> interfaces = out.ref(getStrategy().getJavaClassImplements(tableOrUDT, Mode.POJO));
        final List<String> superTypes = list(superName, interfaces);

        if (generateInterfaces())
            interfaces.add(interfaceName);

        printPackage(out, tableOrUDT, Mode.POJO);

        if (tableOrUDT instanceof TableDefinition)
            generatePojoClassJavadoc((TableDefinition) tableOrUDT, out);
        else
            generateUDTPojoClassJavadoc((UDTDefinition) tableOrUDT, out);

        printClassAnnotations(out, tableOrUDT.getSchema());

        if (tableOrUDT instanceof TableDefinition)
            printTableJPAAnnotation(out, (TableDefinition) tableOrUDT);

        int maxLength = 0;
        for (TypedElementDefinition<?> column : getTypedElements(tableOrUDT))
            maxLength = Math.max(maxLength, out.ref(getJavaType(column.getType(resolver(Mode.POJO)), Mode.POJO)).length());

        if (scala) {
            out.println("%sclass %s(", (generateImmutablePojos() ? "case " : ""), className);

            String separator = "  ";
            for (TypedElementDefinition<?> column : getTypedElements(tableOrUDT)) {
                out.tab(1).println("%s%s%s : %s",
                    separator,
                    generateImmutablePojos() ? "" : "private var ",
                    getStrategy().getJavaMemberName(column, Mode.POJO),
                    out.ref(getJavaType(column.getType(resolver(Mode.POJO)), Mode.POJO)));

                separator = ", ";
            }

            out.println(")[[before= extends ][%s]][[before= with ][separator= with ][%s]] {", first(superTypes), remaining(superTypes));
        }
        else {
            out.println("public class %s[[before= extends ][%s]][[before= implements ][%s]] {", className, list(superName), interfaces);

            if (generateSerializablePojos() || generateSerializableInterfaces())
                out.printSerial();

            out.println();

            for (TypedElementDefinition<?> column : getTypedElements(tableOrUDT)) {
                out.tab(1).println("private %s%s %s;",
                    generateImmutablePojos() ? "final " : "",
                    StringUtils.rightPad(out.ref(getJavaType(column.getType(resolver(Mode.POJO)), Mode.POJO)), maxLength),
                    getStrategy().getJavaMemberName(column, Mode.POJO));
            }
        }

        // Constructors
        // ---------------------------------------------------------------------

        // Default constructor
        if (!generateImmutablePojos())
            generatePojoDefaultConstructor(tableOrUDT, out);

        // [#1363] [#7055] copy constructor
        generatePojoCopyConstructor(tableOrUDT, out);

        // Multi-constructor
        generatePojoMultiConstructor(tableOrUDT, out);

        List<? extends TypedElementDefinition<?>> elements = getTypedElements(tableOrUDT);
        for (int i = 0; i < elements.size(); i++) {
            TypedElementDefinition<?> column = elements.get(i);

            if (tableOrUDT instanceof TableDefinition)
                generatePojoGetter(column, i, out);
            else
                generateUDTPojoGetter(column, i, out);

            // Setter
            if (!generateImmutablePojos())
                if (tableOrUDT instanceof TableDefinition)
                    generatePojoSetter(column, i, out);
                else
                    generateUDTPojoSetter(column, i, out);
        }

        if (generatePojosEqualsAndHashCode())
            generatePojoEqualsAndHashCode(tableOrUDT, out);

        if (generatePojosToString())
            generatePojoToString(tableOrUDT, out);

        if (generateInterfaces() && !generateImmutablePojos())
            printFromAndInto(out, tableOrUDT);

        if (tableOrUDT instanceof TableDefinition)
            generatePojoClassFooter((TableDefinition) tableOrUDT, out);
        else
            generateUDTPojoClassFooter((UDTDefinition) tableOrUDT, out);

        out.println("}");
        closeJavaWriter(out);
    }
    /**
     * Subclasses may override this method to provide their own pojo copy constructors.
     */
    protected void generatePojoMultiConstructor(Definition tableOrUDT, JavaWriter out) {
        final String className = getStrategy().getJavaClassName(tableOrUDT, Mode.POJO);

        int maxLength = 0;
        for (TypedElementDefinition<?> column : getTypedElements(tableOrUDT))
            maxLength = Math.max(maxLength, out.ref(getJavaType(column.getType(resolver(Mode.POJO)), Mode.POJO)).length());

        if (scala) {
        }

        // [#3010] Invalid UDTs may have no attributes. Avoid generating this constructor in that case
        // [#3176] Avoid generating constructors for tables with more than 255 columns (Java's method argument limit)
        else if (getTypedElements(tableOrUDT).size() > 0 &&
                 getTypedElements(tableOrUDT).size() < 256) {
            out.println();
            out.tab(1).print("public %s(", className);

            String separator1 = "";
            for (TypedElementDefinition<?> column : getTypedElements(tableOrUDT)) {
                out.println(separator1);

                out.tab(2).print("%s %s",
                    StringUtils.rightPad(out.ref(getJavaType(column.getType(resolver(Mode.POJO)), Mode.POJO)), maxLength),
                    getStrategy().getJavaMemberName(column, Mode.POJO));
                separator1 = ",";
            }

            out.println();
            out.tab(1).println(") {");

            for (TypedElementDefinition<?> column : getTypedElements(tableOrUDT)) {
                final String columnMember = getStrategy().getJavaMemberName(column, Mode.POJO);

                out.tab(2).println("this.%s = %s;", columnMember, columnMember);
            }

            out.tab(1).println("}");
        }
    }

    /**
     * Subclasses may override this method to provide their own pojo copy constructors.
     */
    protected void generatePojoCopyConstructor(Definition tableOrUDT, JavaWriter out) {
        final String className = getStrategy().getJavaClassName(tableOrUDT, Mode.POJO);
        final String interfaceName = generateInterfaces()
            ? out.ref(getStrategy().getFullJavaClassName(tableOrUDT, Mode.INTERFACE))
            : "";

        out.println();

        if (scala) {
            out.tab(1).println("def this (value : %s) = {", generateInterfaces() ? interfaceName : className);
            out.tab(2).println("this(");

            String separator = "  ";
            for (TypedElementDefinition<?> column : getTypedElements(tableOrUDT)) {
                out.tab(3).println("%svalue.%s",
                    separator,
                    getStrategy().getJavaMemberName(column, Mode.POJO),
                    generateInterfaces()
                        ? getStrategy().getJavaMemberName(column, Mode.INTERFACE)
                        : getStrategy().getJavaMemberName(column, Mode.POJO));

                separator = ", ";
            }

            out.tab(2).println(")");
            out.tab(1).println("}");
        }
        else {
            out.tab(1).println("public %s(%s value) {", className, generateInterfaces() ? interfaceName : className);

            for (TypedElementDefinition<?> column : getTypedElements(tableOrUDT)) {
                out.tab(2).println("this.%s = value.%s%s;",
                    getStrategy().getJavaMemberName(column, Mode.POJO),
                    generateInterfaces()
                        ? getStrategy().getJavaGetterName(column, Mode.INTERFACE)
                        : getStrategy().getJavaMemberName(column, Mode.POJO),
                    generateInterfaces()
                        ? "()"
                        : "");
            }

            out.tab(1).println("}");
        }
    }

    /**
     * Subclasses may override this method to provide their own pojo default constructors.
     */
    protected void generatePojoDefaultConstructor(Definition tableOrUDT, JavaWriter out) {
        final String className = getStrategy().getJavaClassName(tableOrUDT, Mode.POJO);

        out.println();

        if (scala) {

            // [#3010] Invalid UDTs may have no attributes. Avoid generating this constructor in that case
            int size = getTypedElements(tableOrUDT).size();
            if (size > 0) {
                List<String> nulls = new ArrayList<>(size);
                for (TypedElementDefinition<?> column : getTypedElements(tableOrUDT))

                    // Avoid ambiguities between a single-T-value constructor
                    // and the copy constructor
                    if (size == 1)
                        nulls.add("null : " + out.ref(getJavaType(column.getType(resolver(Mode.POJO)), Mode.POJO)));
                    else
                        nulls.add("null");

                out.tab(1).println("def this() = {", className);
                out.tab(2).println("this([[%s]])", nulls);
                out.tab(1).println("}");
            }
        }
        else {
            out.tab(1).println("public %s() {}", className);
        }
    }

    /**
     * Subclasses may override this method to provide their own pojo getters.
     */
    protected void generatePojoGetter(TypedElementDefinition<?> column, int index, JavaWriter out) {
        generatePojoGetter0(column, index, out);
    }

    /**
     * Subclasses may override this method to provide their own pojo getters.
     */
    protected void generateUDTPojoGetter(TypedElementDefinition<?> column, int index, JavaWriter out) {
        generatePojoGetter0(column, index, out);
    }

    private final void generatePojoGetter0(TypedElementDefinition<?> column, @SuppressWarnings("unused") int index, JavaWriter out) {
        final String columnTypeFull = getJavaType(column.getType(resolver(Mode.POJO)), Mode.POJO);
        final String columnType = out.ref(columnTypeFull);
        final String columnGetter = getStrategy().getJavaGetterName(column, Mode.POJO);
        final String columnMember = getStrategy().getJavaMemberName(column, Mode.POJO);

        // Getter
        out.println();

        printDeprecationIfUnknownType(out, columnTypeFull);

        if (column instanceof ColumnDefinition)
            printColumnJPAAnnotation(out, (ColumnDefinition) column);

        printValidationAnnotation(out, column);

        if (scala) {
            out.tab(1).println("def %s : %s = {", columnGetter, columnType);
            out.tab(2).println("this.%s", columnMember);
            out.tab(1).println("}");
        }
        else {
            out.tab(1).overrideIf(generateInterfaces());
            out.tab(1).println("public %s %s() {", columnType, columnGetter);
            out.tab(2).println("return this.%s;", columnMember);
            out.tab(1).println("}");
        }
    }


    /**
     * Subclasses may override this method to provide their own pojo setters.
     */
    protected void generatePojoSetter(TypedElementDefinition<?> column, int index, JavaWriter out) {
        generatePojoSetter0(column, index, out);
    }

    /**
     * Subclasses may override this method to provide their own pojo setters.
     */
    protected void generateUDTPojoSetter(TypedElementDefinition<?> column, int index, JavaWriter out) {
        generatePojoSetter0(column, index, out);
    }

    private final void generatePojoSetter0(TypedElementDefinition<?> column, @SuppressWarnings("unused") int index, JavaWriter out) {
        final String className = getStrategy().getJavaClassName(column.getContainer(), Mode.POJO);
        final String columnTypeFull = getJavaType(column.getType(resolver(Mode.POJO)), Mode.POJO);
        final String columnType = out.ref(columnTypeFull);
        final String columnSetterReturnType = generateFluentSetters() ? className : (scala ? "Unit" : "void");
        final String columnSetter = getStrategy().getJavaSetterName(column, Mode.POJO);
        final String columnMember = getStrategy().getJavaMemberName(column, Mode.POJO);
        final boolean isUDT = column.getType(resolver()).isUDT();
        final boolean isUDTArray = column.getType(resolver()).isArray() && database.getArray(column.getType(resolver()).getSchema(), column.getType(resolver()).getQualifiedUserType()).getElementType(resolver()).isUDT();

        // We cannot have covariant setters for arrays because of type erasure
        if (!(generateInterfaces() && isUDTArray)) {
            out.println();

            printDeprecationIfUnknownType(out, columnTypeFull);
            if (scala) {
                out.tab(1).println("def %s(%s : %s) : %s = {", columnSetter, columnMember, columnType, columnSetterReturnType);
                out.tab(2).println("this.%s = %s", columnMember, columnMember);
                if (generateFluentSetters())
                    out.tab(2).println("this");
                out.tab(1).println("}");
            }
            else {
                out.tab(1).overrideIf(generateInterfaces() && !generateImmutableInterfaces() && !isUDT);
                out.tab(1).println("public %s %s(%s %s) {", columnSetterReturnType, columnSetter, varargsIfArray(columnType), columnMember);
                out.tab(2).println("this.%s = %s;", columnMember, columnMember);
                if (generateFluentSetters())
                    out.tab(2).println("return this;");
                out.tab(1).println("}");
            }
        }

        // [#3117] To avoid covariant setters on POJOs, we need to generate two setter overloads
        if (generateInterfaces() && (isUDT || isUDTArray)) {
            final String columnTypeInterface = out.ref(getJavaType(column.getType(resolver(Mode.INTERFACE)), Mode.INTERFACE));

            out.println();

            if (scala) {
                // [#3082] TODO Handle <interfaces/> + ARRAY also for Scala

                out.tab(1).println("def %s(%s : %s) : %s = {", columnSetter, columnMember, columnTypeInterface, columnSetterReturnType);
                out.tab(2).println("if (%s == null)", columnMember);
                out.tab(3).println("this.%s = null", columnMember);
                out.tab(2).println("else");
                out.tab(3).println("this.%s = %s.into(new %s)", columnMember, columnMember, columnType);

                if (generateFluentSetters())
                    out.tab(2).println("this");

                out.tab(1).println("}");
            }
            else {
                out.tab(1).override();
                out.tab(1).println("public %s %s(%s %s) {", columnSetterReturnType, columnSetter, varargsIfArray(columnTypeInterface), columnMember);
                out.tab(2).println("if (%s == null)", columnMember);
                out.tab(3).println("this.%s = null;", columnMember);

                if (isUDT) {
                    out.tab(2).println("else");
                    out.tab(3).println("this.%s = %s.into(new %s());", columnMember, columnMember, columnType);
                }
                else if (isUDTArray) {
                    final ArrayDefinition array = database.getArray(column.getType(resolver()).getSchema(), column.getType(resolver()).getQualifiedUserType());
                    final String componentType = out.ref(getJavaType(array.getElementType(resolver(Mode.POJO)), Mode.POJO));
                    final String componentTypeInterface = out.ref(getJavaType(array.getElementType(resolver(Mode.INTERFACE)), Mode.INTERFACE));

                    out.tab(2).println("else {");
                    out.tab(3).println("this.%s = new %s();", columnMember, ArrayList.class);
                    out.println();
                    out.tab(3).println("for (%s i : %s)", componentTypeInterface, columnMember);
                    out.tab(4).println("this.%s.add(i.into(new %s()));", columnMember, componentType);
                    out.tab(2).println("}");
                }

                if (generateFluentSetters())
                    out.tab(2).println("return this;");

                out.tab(1).println("}");
            }
        }
    }

    protected void generatePojoEqualsAndHashCode(Definition tableOrUDT, JavaWriter out) {
        final String className = getStrategy().getJavaClassName(tableOrUDT, Mode.POJO);

        out.println();

        if (scala) {
            out.tab(1).println("override def equals(obj : Any) : scala.Boolean = {");
            out.tab(2).println("if (this == obj)");
            out.tab(3).println("return true");
            out.tab(2).println("if (obj == null)");
            out.tab(3).println("return false");
            out.tab(2).println("if (getClass() != obj.getClass())");
            out.tab(3).println("return false");

            out.tab(2).println("val other = obj.asInstanceOf[%s]", className);

            for (TypedElementDefinition<?> column : getTypedElements(tableOrUDT)) {
                final String columnMember = getStrategy().getJavaMemberName(column, Mode.POJO);

                out.tab(2).println("if (%s == null) {", columnMember);
                out.tab(3).println("if (other.%s != null)", columnMember);
                out.tab(4).println("return false");
                out.tab(2).println("}");

                if (getJavaType(column.getType(resolver())).endsWith("[]")) {
                    out.tab(2).println("else if (!%s.equals(%s, other.%s))", Arrays.class, columnMember, columnMember);
                }
                else {
                    out.tab(2).println("else if (!%s.equals(other.%s))", columnMember, columnMember);
                }

                out.tab(3).println("return false");
            }

            out.tab(2).println("return true");
            out.tab(1).println("}");
        }
        else {
            out.tab(1).println("@Override");
            out.tab(1).println("public boolean equals(%s obj) {", Object.class);
            out.tab(2).println("if (this == obj)");
            out.tab(3).println("return true;");
            out.tab(2).println("if (obj == null)");
            out.tab(3).println("return false;");
            out.tab(2).println("if (getClass() != obj.getClass())");
            out.tab(3).println("return false;");

            out.tab(2).println("final %s other = (%s) obj;", className, className);

            for (TypedElementDefinition<?> column : getTypedElements(tableOrUDT)) {
                final String columnMember = getStrategy().getJavaMemberName(column, Mode.POJO);

                out.tab(2).println("if (%s == null) {", columnMember);
                out.tab(3).println("if (other.%s != null)", columnMember);
                out.tab(4).println("return false;");
                out.tab(2).println("}");

                if (getJavaType(column.getType(resolver())).endsWith("[]")) {
                    out.tab(2).println("else if (!%s.equals(%s, other.%s))", Arrays.class, columnMember, columnMember);
                }
                else {
                    out.tab(2).println("else if (!%s.equals(other.%s))", columnMember, columnMember);
                }

                out.tab(3).println("return false;");
            }

            out.tab(2).println("return true;");
            out.tab(1).println("}");
        }

        out.println();

        if (scala) {
            out.tab(1).println("override def hashCode : Int = {");
            out.tab(2).println("val prime = 31");
            out.tab(2).println("var result = 1");

            for (TypedElementDefinition<?> column : getTypedElements(tableOrUDT)) {
                final String columnMember = getStrategy().getJavaMemberName(column, Mode.POJO);

                if (getJavaType(column.getType(resolver())).endsWith("[]")) {
                    out.tab(2).println("result = prime * result + (if (this.%s == null) 0 else %s.hashCode(this.%s))", columnMember, Arrays.class, columnMember);
                }
                else {
                    out.tab(2).println("result = prime * result + (if (this.%s == null) 0 else this.%s.hashCode())", columnMember, columnMember);
                }
            }

            out.tab(2).println("return result");
            out.tab(1).println("}");
        }
        else {
            out.tab(1).println("@Override");
            out.tab(1).println("public int hashCode() {");
            out.tab(2).println("final int prime = 31;");
            out.tab(2).println("int result = 1;");

            for (TypedElementDefinition<?> column : getTypedElements(tableOrUDT)) {
                final String columnMember = getStrategy().getJavaMemberName(column, Mode.POJO);

                if (getJavaType(column.getType(resolver())).endsWith("[]")) {
                    out.tab(2).println("result = prime * result + ((this.%s == null) ? 0 : %s.hashCode(this.%s));", columnMember, Arrays.class, columnMember);
                }
                else {
                    out.tab(2).println("result = prime * result + ((this.%s == null) ? 0 : this.%s.hashCode());", columnMember, columnMember);
                }
            }

            out.tab(2).println("return result;");
            out.tab(1).println("}");
        }
    }

    protected void generatePojoToString(Definition tableOrUDT, JavaWriter out) {
        final String className = getStrategy().getJavaClassName(tableOrUDT, Mode.POJO);

        out.println();

        if (scala) {
            out.tab(1).println("override def toString : String = {");

            out.tab(2).println("val sb = new %s(\"%s (\")", StringBuilder.class, className);
            out.tab(2).println();

            String separator = "";
            for (TypedElementDefinition<?> column : getTypedElements(tableOrUDT)) {
                final String columnMember = getStrategy().getJavaMemberName(column, Mode.POJO);
                final String columnType = getJavaType(column.getType(resolver()));

                if (columnType.equals("scala.Array[scala.Byte]")) {
                    out.tab(2).println("sb%s.append(\"[binary...]\")", separator);
                }
                else {
                    out.tab(2).println("sb%s.append(%s)", separator, columnMember);
                }

                separator = ".append(\", \")";
            }

            out.tab(2).println();
            out.tab(2).println("sb.append(\")\")");

            out.tab(2).println("return sb.toString");
            out.tab(1).println("}");
        }
        else {
            out.tab(1).println("@Override");
            out.tab(1).println("public String toString() {");
            out.tab(2).println("%s sb = new %s(\"%s (\");", StringBuilder.class, StringBuilder.class, className);
            out.tab(2).println();

            String separator = "";
            for (TypedElementDefinition<?> column : getTypedElements(tableOrUDT)) {
                final String columnMember = getStrategy().getJavaMemberName(column, Mode.POJO);
                final String columnType = getJavaType(column.getType(resolver()));
                final boolean array = columnType.endsWith("[]");

                if (array && columnType.equals("byte[]")) {
                    out.tab(2).println("sb%s.append(\"[binary...]\");", separator);
                }
                else if (array) {
                    out.tab(2).println("sb%s.append(%s.toString(%s));", separator, Arrays.class, columnMember);
                }
                else {
                    out.tab(2).println("sb%s.append(%s);", separator, columnMember);
                }

                separator = ".append(\", \")";
            }

            out.tab(2).println();
            out.tab(2).println("sb.append(\")\");");
            out.tab(2).println("return sb.toString();");
            out.tab(1).println("}");
        }
    }

    private List<? extends TypedElementDefinition<? extends Definition>> getTypedElements(Definition definition) {
        if (definition instanceof TableDefinition)
            return ((TableDefinition) definition).getColumns();
        else if (definition instanceof EmbeddableDefinition)
            return ((EmbeddableDefinition) definition).getColumns();
        else if (definition instanceof UDTDefinition)
            return ((UDTDefinition) definition).getAttributes();
        else if (definition instanceof RoutineDefinition)
            return ((RoutineDefinition) definition).getAllParameters();
        else
            throw new IllegalArgumentException("Unsupported type : " + definition);
    }

    /**
     * Subclasses may override this method to provide POJO class footer code.
     */
    @SuppressWarnings("unused")
    protected void generatePojoClassFooter(TableDefinition table, JavaWriter out) {}

    /**
     * Subclasses may override this method to provide their own Javadoc.
     */
    protected void generatePojoClassJavadoc(TableDefinition table, JavaWriter out) {
        if (generateCommentsOnTables())
            printClassJavadoc(out, table);
        else
            printClassJavadoc(out, "The table <code>" + table.getQualifiedInputName() + "</code>.");
    }

    protected void generateTables(SchemaDefinition schema) {
        log.info("Generating tables");

        for (TableDefinition table : database.getTables(schema)) {
            try {
                generateTable(schema, table);
            }
            catch (Exception e) {
                log.error("Error while generating table " + table, e);
            }
        }

        watch.splitInfo("Tables generated");
    }

    @SuppressWarnings("unused")
    protected void generateTable(SchemaDefinition schema, TableDefinition table) {
        JavaWriter out = newJavaWriter(getFile(table));
        generateTable(table, out);
        closeJavaWriter(out);
    }

    protected void generateTable(TableDefinition table, JavaWriter out) {
        final SchemaDefinition schema = table.getSchema();
        final UniqueKeyDefinition primaryKey = table.getPrimaryKey();

        final String className = getStrategy().getJavaClassName(table);
        final String tableId = scala
            ? out.ref(getStrategy().getFullJavaIdentifier(table), 2)
            : getStrategy().getJavaIdentifier(table);
        final String recordType = out.ref(getStrategy().getFullJavaClassName(table, Mode.RECORD));
        final List<String> interfaces = out.ref(getStrategy().getJavaClassImplements(table, Mode.DEFAULT));
        final String schemaId = out.ref(getStrategy().getFullJavaIdentifier(schema), 2);
        final String comment = defaultString(table.getComment());

        log.info("Generating table", out.file().getName() +
            " [input=" + table.getInputName() +
            ", output=" + table.getOutputName() +
            ", pk=" + (primaryKey != null ? primaryKey.getName() : "N/A") +
            "]");

        printPackage(out, table);

        if (scala) {
            out.println("object %s {", className);
            printSingletonInstance(out, table);
            out.println("}");
            out.println();
        }

        generateTableClassJavadoc(table, out);
        printClassAnnotations(out, schema);

        if (scala) {
            out.println("class %s(", className);
            out.tab(1).println("alias : %s,", Name.class);
            out.tab(1).println("child : %s[_ <: %s],", Table.class, Record.class);
            out.tab(1).println("path : %s[_ <: %s, %s],", ForeignKey.class, Record.class, recordType);
            out.tab(1).println("aliased : %s[%s],", Table.class, recordType);
            out.tab(1).println("parameters : %s[ %s[_] ]", out.ref("scala.Array"), Field.class);
            out.println(")");
            out.println("extends %s[%s](", TableImpl.class, recordType);
            out.tab(1).println("alias,");
            out.tab(1).println("%s,", schemaId);
            out.tab(1).println("child,");
            out.tab(1).println("path,");
            out.tab(1).println("aliased,");
            out.tab(1).println("parameters,");
            out.tab(1).println("%s.comment(\"%s\")", DSL.class, escapeString(comment));
            out.println(")");

            if (!interfaces.isEmpty())
                out.println("[[before= with ][separator= with ][%s]]", interfaces);

            out.println("{");
        }
        else {
            out.println("public class %s extends %s<%s>[[before= implements ][%s]] {",
                    className, TableImpl.class, recordType, interfaces);
            out.printSerial();
            printSingletonInstance(out, table);
        }

        printRecordTypeMethod(out, table);

        for (ColumnDefinition column : table.getColumns()) {
            final String columnTypeFull = getJavaType(column.getType(resolver()));
            final String columnType = out.ref(columnTypeFull);
            final String columnTypeRef = getJavaTypeReference(column.getDatabase(), column.getType(resolver()));
            final String columnId = out.ref(getStrategy().getJavaIdentifier(column), colRefSegments(column));
            final String columnName = column.getName();
            final String columnComment = StringUtils.defaultString(column.getComment());
            final List<String> converter = out.ref(list(column.getType(resolver()).getConverter()));
            final List<String> binding = out.ref(list(column.getType(resolver()).getBinding()));

            if (!printDeprecationIfUnknownType(out, columnTypeFull))
                out.tab(1).javadoc("The column <code>%s</code>.%s", column.getQualifiedOutputName(), columnComment(column, columnComment));

            if (scala) {
                out.tab(1).println("val %s : %s[%s, %s] = createField(%s.name(\"%s\"), %s, \"%s\"" + converterTemplate(converter) + converterTemplate(binding) + ")",
                        columnId, TableField.class, recordType, columnType, DSL.class, columnName, columnTypeRef, escapeString(columnComment), converter, binding);
            }
            else {
                String isStatic = generateInstanceFields() ? "" : "static ";
                String tableRef = generateInstanceFields() ? "this" : out.ref(getStrategy().getJavaIdentifier(table), 2);

                out.tab(1).println("public %sfinal %s<%s, %s> %s = createField(%s.name(\"%s\"), %s, %s, \"%s\"" + converterTemplate(converter) + converterTemplate(binding) + ");",
                    isStatic, TableField.class, recordType, columnType, columnId, DSL.class, columnName, columnTypeRef, tableRef, escapeString(columnComment), converter, binding);
            }
        }

        // [#2530] Embeddable types
        for (EmbeddableDefinition embeddable : table.getEmbeddables()) {
            final String columnId = out.ref(getStrategy().getJavaIdentifier(embeddable), colRefSegments(null));
            final String columnType = out.ref(getStrategy().getFullJavaClassName(embeddable, Mode.RECORD));

            final List<String> columnIds = new ArrayList<>();
            for (EmbeddableColumnDefinition column : embeddable.getColumns())
                columnIds.add(out.ref(getStrategy().getJavaIdentifier(column), colRefSegments(column)));

            out.tab(1).javadoc("The embeddable type <code>%s</code>.", embeddable.getOutputName());

            if (scala) {
                out.tab(1).println("val %s : %s[%s, %s] = %s.createEmbeddable(%s.name(\"%s\"), classOf[%s], this, [[%s]])",
                        columnId, TableField.class, recordType, columnType, Internal.class, DSL.class, embeddable.getName(), columnType, columnIds);
            }
            else {
                out.tab(1).println("public final %s<%s, %s> %s = %s.createEmbeddable(%s.name(\"%s\"), %s.class, this, [[%s]]);",
                    TableField.class, recordType, columnType, columnId, Internal.class, DSL.class, embeddable.getName(), columnType, columnIds);
            }
        }

        if (scala) {
            out.tab(1).javadoc("Create a <code>%s</code> table reference", table.getQualifiedOutputName());
            out.tab(1).println("def this() = {");
            out.tab(2).println("this(%s.name(\"%s\"), null, null, null, null)", DSL.class, table.getOutputName());
            out.tab(1).println("}");
        }
        else {
            // [#1255] With instance fields, the table constructor may
            // be public, as tables are no longer singletons
            if (generateInstanceFields()) {
                out.tab(1).javadoc("Create a <code>%s</code> table reference", table.getQualifiedOutputName());
                out.tab(1).println("public %s() {", className);
            }
            else {
                out.tab(1).javadoc(NO_FURTHER_INSTANCES_ALLOWED);
                out.tab(1).println("private %s() {", className);
            }

            out.tab(2).println("this(%s.name(\"%s\"), null);", DSL.class, table.getOutputName());
            out.tab(1).println("}");
        }


        if (scala) {
            out.tab(1).javadoc("Create an aliased <code>%s</code> table reference", table.getQualifiedOutputName());
            out.tab(1).println("def this(alias : %s) = {", String.class);
            out.tab(2).println("this(%s.name(alias), null, null, %s, null)", DSL.class, tableId);
            out.tab(1).println("}");

            out.tab(1).javadoc("Create an aliased <code>%s</code> table reference", table.getQualifiedOutputName());
            out.tab(1).println("def this(alias : %s) = {", Name.class);
            out.tab(2).println("this(alias, null, null, %s, null)", tableId);
            out.tab(1).println("}");
        }

        // [#117] With instance fields, it makes sense to create a
        // type-safe table alias
        // [#1255] With instance fields, the table constructor may
        // be public, as tables are no longer singletons
        else if (generateInstanceFields()) {
            out.tab(1).javadoc("Create an aliased <code>%s</code> table reference", table.getQualifiedOutputName());
            out.tab(1).println("public %s(%s alias) {", className, String.class);
            out.tab(2).println("this(%s.name(alias), %s);", DSL.class, tableId);
            out.tab(1).println("}");

            out.tab(1).javadoc("Create an aliased <code>%s</code> table reference", table.getQualifiedOutputName());
            out.tab(1).println("public %s(%s alias) {", className, Name.class);
            out.tab(2).println("this(alias, %s);", tableId);
            out.tab(1).println("}");
        }

        out.println();

        if (scala) {
            out.tab(1).println("private def this(alias : %s, aliased : %s[%s]) = {", Name.class, Table.class, recordType);
            if (table.isTableValuedFunction())
                out.tab(2).println("this(alias, null, null, aliased, new %s[ %s[_] ](%s))", out.ref("scala.Array"), Field.class, table.getParameters().size());
            else
                out.tab(2).println("this(alias, null, null, aliased, null)");

            out.tab(1).println("}");
        }
        else {
            out.tab(1).println("private %s(%s alias, %s<%s> aliased) {", className, Name.class, Table.class, recordType);
            if (table.isTableValuedFunction())
                out.tab(2).println("this(alias, aliased, new %s[%s]);", Field.class, table.getParameters().size());
            else
                out.tab(2).println("this(alias, aliased, null);");

            out.tab(1).println("}");

            out.println();
            out.tab(1).println("private %s(%s alias, %s<%s> aliased, %s<?>[] parameters) {", className, Name.class, Table.class, recordType, Field.class);
            out.tab(2).println("super(alias, null, aliased, parameters, %s.comment(\"%s\"));", DSL.class, escapeString(comment));
            out.tab(1).println("}");
        }

        if (generateImplicitJoinPathsToOne() && generateGlobalKeyReferences()) {
            out.println();

            if (scala) {
                out.tab(1).println("def this(child : %s[_ <: %s], key : %s[_ <: %s, %s]) = {", Table.class, Record.class, ForeignKey.class, Record.class, recordType);
                out.tab(2).println("this(%s.createPathAlias(child, key), child, key, %s, null)", Internal.class, tableId);
                out.tab(1).println("}");
            }
            else {
                out.tab(1).println("public <O extends %s> %s(%s<O> child, %s<O, %s> key) {", Record.class, className, Table.class, ForeignKey.class, recordType);
                out.tab(2).println("super(child, key, %s);", tableId);
                out.tab(1).println("}");
            }
        }

        if (scala) {
            out.println();
            out.tab(1).println("override def getSchema : %s = %s", Schema.class, schemaId);
        }
        else {
            out.tab(1).overrideInherit();
            out.tab(1).println("public %s getSchema() {", Schema.class);
            out.tab(2).println("return %s;", schemaId);
            out.tab(1).println("}");
        }

        // Add index information
        if (generateIndexes()) {
            List<IndexDefinition> indexes = table.getIndexes();

            if (!indexes.isEmpty()) {
                final List<String> indexFullIds = out.ref(getStrategy().getFullJavaIdentifiers(indexes), 2);

                if (scala) {
                    out.println();
                    out.tab(1).println("override def getIndexes : %s[ %s ] = {", List.class, Index.class);
                    out.tab(2).println("return %s.asList[ %s ]([[%s]])", Arrays.class, Index.class, indexFullIds);
                    out.tab(1).println("}");
                }
                else {
                    out.tab(1).overrideInherit();
                    out.tab(1).println("public %s<%s> getIndexes() {", List.class, Index.class);
                    out.tab(2).println("return %s.<%s>asList([[%s]]);", Arrays.class, Index.class, indexFullIds);
                    out.tab(1).println("}");
                }
            }
        }

        // Add primary / unique / foreign key information
        if (generateRelations()) {
            IdentityDefinition identity = table.getIdentity();

            // The identity column
            if (identity != null) {
                final String identityTypeFull = getJavaType(identity.getColumn().getType(resolver()));
                final String identityType = out.ref(identityTypeFull);
                final String identityFullId = generateGlobalKeyReferences()
                    ? out.ref(getStrategy().getFullJavaIdentifier(identity), 2)
                    : null;

                if (scala) {
                    out.println();

                    printDeprecationIfUnknownType(out, identityTypeFull);
                    out.tab(1).println("override def getIdentity : %s[%s, %s] = {", Identity.class, recordType, identityType);

                    if (identityFullId != null)
                        out.tab(2).println("%s", identityFullId);
                    else
                        printCreateIdentity(out, identity);

                    out.tab(1).println("}");
                }
                else {
                    if (printDeprecationIfUnknownType(out, identityTypeFull))
                        out.tab(1).override();
                    else
                        out.tab(1).overrideInherit();

                    out.tab(1).println("public %s<%s, %s> getIdentity() {", Identity.class, recordType, identityType);
                    out.tab(2).print("return ");

                    if (identityFullId != null)
                        out.tab(2).print("%s", identityFullId);
                    else
                        printCreateIdentity(out, identity);

                    out.println(";");
                    out.tab(1).println("}");
                }
            }

            // The primary / main unique key
            if (primaryKey != null) {
                final String keyFullId = generateGlobalKeyReferences()
                    ? out.ref(getStrategy().getFullJavaIdentifier(primaryKey), 2)
                    : null;

                if (scala) {
                    out.println();
                    out.tab(1).println("override def getPrimaryKey : %s[%s] = {", UniqueKey.class, recordType);

                    if (keyFullId != null)
                        out.tab(2).println("%s", keyFullId);
                    else
                        printCreateUniqueKey(out, primaryKey);

                    out.tab(1).println("}");
                }
                else {
                    out.tab(1).overrideInherit();
                    out.tab(1).println("public %s<%s> getPrimaryKey() {", UniqueKey.class, recordType);
                    out.tab(2).print("return ");

                    if (keyFullId != null)
                        out.tab(2).print("%s", keyFullId);
                    else
                        printCreateUniqueKey(out, primaryKey);

                    out.println(";");
                    out.tab(1).println("}");
                }
            }

            // The remaining unique keys
            List<UniqueKeyDefinition> uniqueKeys = table.getUniqueKeys();
            if (uniqueKeys.size() > 0) {
                if (generateGlobalKeyReferences()) {
                    final List<String> keyFullIds = out.ref(getStrategy().getFullJavaIdentifiers(uniqueKeys), 2);

                    if (scala) {
                        out.println();
                        out.tab(1).println("override def getKeys : %s[ %s[%s] ] = {", List.class, UniqueKey.class, recordType);
                        out.tab(2).println("return %s.asList[ %s[%s] ]([[%s]])", Arrays.class, UniqueKey.class, recordType, keyFullIds);
                        out.tab(1).println("}");
                    }
                    else {
                        out.tab(1).overrideInherit();
                        out.tab(1).println("public %s<%s<%s>> getKeys() {", List.class, UniqueKey.class, recordType);
                        out.tab(2).println("return %s.<%s<%s>>asList([[%s]]);", Arrays.class, UniqueKey.class, recordType, keyFullIds);
                        out.tab(1).println("}");
                    }
                }
                else {
                    String separator = "  ";

                    if (scala) {
                        out.println();
                        out.tab(1).println("override def getKeys : %s[ %s[%s] ] = {", List.class, UniqueKey.class, recordType);
                        out.tab(2).println("return %s.asList[ %s[%s] ](", Arrays.class, UniqueKey.class, recordType);

                        for (UniqueKeyDefinition uniqueKey : uniqueKeys) {
                            out.tab(3).print("%s", separator);
                            printCreateUniqueKey(out, uniqueKey);
                            out.println();
                            separator = ", ";
                        }

                        out.tab(2).println(")");
                        out.tab(1).println("}");
                    }
                    else {
                        out.tab(1).overrideInherit();
                        out.tab(1).println("public %s<%s<%s>> getKeys() {", List.class, UniqueKey.class, recordType);
                        out.tab(2).println("return %s.<%s<%s>>asList(", Arrays.class, UniqueKey.class, recordType);

                        for (UniqueKeyDefinition uniqueKey : uniqueKeys) {
                            out.tab(3).print("%s", separator);
                            printCreateUniqueKey(out, uniqueKey);
                            out.println();
                            separator = ", ";
                        }

                        out.tab(2).println(");");
                        out.tab(1).println("}");

                    }
                }
            }

            // Foreign keys
            List<ForeignKeyDefinition> foreignKeys = table.getForeignKeys();

            // [#7554] Not yet supported with global key references turned off
            if (foreignKeys.size() > 0 && generateGlobalKeyReferences()) {
                final List<String> keyFullIds = out.ref(getStrategy().getFullJavaIdentifiers(foreignKeys), 2);

                if (scala) {
                    out.println();
                    out.tab(1).println("override def getReferences : %s[ %s[%s, _] ] = {", List.class, ForeignKey.class, recordType);
                    out.tab(2).println("return %s.asList[ %s[%s, _] ]([[%s]])", Arrays.class, ForeignKey.class, recordType, keyFullIds);
                    out.tab(1).println("}");
                }
                else {
                    out.tab(1).overrideInherit();
                    out.tab(1).println("public %s<%s<%s, ?>> getReferences() {", List.class, ForeignKey.class, recordType);
                    out.tab(2).println("return %s.<%s<%s, ?>>asList([[%s]]);", Arrays.class, ForeignKey.class, recordType, keyFullIds);
                    out.tab(1).println("}");
                }

                // Outbound (to-one) implicit join paths
                if (generateImplicitJoinPathsToOne()) {
                    for (ForeignKeyDefinition foreignKey : foreignKeys) {
                        final String keyFullId = out.ref(getStrategy().getFullJavaIdentifier(foreignKey), 2);
                        final String referencedTableClassName = out.ref(getStrategy().getFullJavaClassName(foreignKey.getReferencedTable()));
                        final String keyMethodName = out.ref(getStrategy().getJavaMethodName(foreignKey));

                        if (scala) {
                            out.tab(1).println();
                            out.tab(1).println("def %s : %s = {", keyMethodName, referencedTableClassName);
                            out.tab(2).println("return new %s(this, %s)", referencedTableClassName, keyFullId);
                            out.tab(1).println("}");
                        }
                        else {
                            out.tab(1).println();
                            out.tab(1).println("public %s %s() {", referencedTableClassName, keyMethodName);
                            out.tab(2).println("return new %s(this, %s);", referencedTableClassName, keyFullId);
                            out.tab(1).println("}");
                        }
                    }
                }
            }
        }

        List<CheckConstraintDefinition> cc = table.getCheckConstraints();

        if (!cc.isEmpty()) {
            if (scala) {
                out.println();
                out.tab(1).println("override def getChecks : %s[ %s[%s] ] = {", List.class, Check.class, recordType);
                out.tab(2).println("return %s.asList[ %s[%s] ](", Arrays.class, Check.class, recordType);
            }
            else {
                out.tab(1).overrideInherit();
                out.tab(1).println("public %s<%s<%s>> getChecks() {", List.class, Check.class, recordType);
                out.tab(2).println("return %s.<%s<%s>>asList(", Arrays.class, Check.class, recordType);
            }

            String separator = "  ";
            for (CheckConstraintDefinition c : cc) {
                out.tab(3).println("%s%s.createCheck(this, %s.name(\"%s\"), \"%s\")", separator, Internal.class, DSL.class, c.getName().replace("\"", "\\\""), c.getCheckClause().replace("\"", "\\\""));
                separator = ", ";
            }

            out.tab(2).println(");");
            out.tab(1).println("}");
        }

        // [#1596] Updatable tables can provide fields for optimistic locking if properly configured.
        // [#7904] Records being updatable isn't a strict requirement. Version and timestamp values
        //         can still be generated
        versionLoop: for (String pattern : database.getRecordVersionFields()) {
            Pattern p = Pattern.compile(pattern, Pattern.COMMENTS);

            for (ColumnDefinition column : table.getColumns()) {
                if ((p.matcher(column.getName()).matches() ||
                     p.matcher(column.getQualifiedName()).matches())) {

                    final String columnTypeFull = getJavaType(column.getType(resolver()));
                    final String columnType = out.ref(columnTypeFull);
                    final String columnId = getStrategy().getJavaIdentifier(column);

                    if (scala) {
                        out.println();

                        printDeprecationIfUnknownType(out, columnTypeFull);
                        out.tab(1).println("override def getRecordVersion : %s[%s, %s] = {", TableField.class, recordType, columnType);
                        out.tab(2).println("%s", columnId);
                        out.tab(1).println("}");
                    }
                    else {
                        if (printDeprecationIfUnknownType(out, columnTypeFull))
                            out.tab(1).override();
                        else
                            out.tab(1).overrideInherit();

                        out.tab(1).println("public %s<%s, %s> getRecordVersion() {", TableField.class, recordType, columnType);
                        out.tab(2).println("return %s;", columnId);
                        out.tab(1).println("}");
                    }

                    // Avoid generating this method twice
                    break versionLoop;
                }
            }
        }

        timestampLoop: for (String pattern : database.getRecordTimestampFields()) {
            Pattern p = Pattern.compile(pattern, Pattern.COMMENTS);

            for (ColumnDefinition column : table.getColumns()) {
                if ((p.matcher(column.getName()).matches() ||
                     p.matcher(column.getQualifiedName()).matches())) {

                    final String columnTypeFull = getJavaType(column.getType(resolver()));
                    final String columnType = out.ref(columnTypeFull);
                    final String columnId = getStrategy().getJavaIdentifier(column);

                    if (scala) {
                        out.println();

                        printDeprecationIfUnknownType(out, columnTypeFull);
                        out.tab(1).println("override def getRecordTimestamp : %s[%s, %s] = {", TableField.class, recordType, columnType);
                        out.tab(2).println("%s", columnId);
                        out.tab(1).println("}");
                    }
                    else {
                        if (printDeprecationIfUnknownType(out, columnTypeFull))
                            out.tab(1).override();
                        else
                            out.tab(1).overrideInherit();

                        out.tab(1).println("public %s<%s, %s> getRecordTimestamp() {", TableField.class, recordType, columnType);
                        out.tab(2).println("return %s;", columnId);
                        out.tab(1).println("}");
                    }

                    // Avoid generating this method twice
                    break timestampLoop;
                }
            }
        }

        if (scala) {
            out.println();
            out.tab(1).println("override def as(alias : %s) : %s = {", String.class, className);

            if (table.isTableValuedFunction())
                out.tab(2).println("new %s(%s.name(alias), this, parameters)", className, DSL.class);
            else
                out.tab(2).println("new %s(%s.name(alias), this)", className, DSL.class);

            out.tab(1).println("}");


            out.println();
            out.tab(1).println("override def as(alias : %s) : %s = {", Name.class, className);

            if (table.isTableValuedFunction())
                out.tab(2).println("new %s(alias, this, parameters)", className);
            else
                out.tab(2).println("new %s(alias, this)", className);

            out.tab(1).println("}");
        }

        // [#117] With instance fields, it makes sense to create a
        // type-safe table alias
        else if (generateInstanceFields()) {
            out.tab(1).overrideInherit();
            out.tab(1).println("public %s as(%s alias) {", className, String.class);

            if (table.isTableValuedFunction())
                out.tab(2).println("return new %s(%s.name(alias), this, parameters);", className, DSL.class);
            else
                out.tab(2).println("return new %s(%s.name(alias), this);", className, DSL.class);

            out.tab(1).println("}");


            out.tab(1).overrideInherit();
            out.tab(1).println("public %s as(%s alias) {", className, Name.class);

            if (table.isTableValuedFunction())
                out.tab(2).println("return new %s(alias, this, parameters);", className);
            else
                out.tab(2).println("return new %s(alias, this);", className);

            out.tab(1).println("}");
        }

        if (scala) {
            out.tab(1).javadoc("Rename this table");
            out.tab(1).println("override def rename(name : %s) : %s = {", String.class, className);

            if (table.isTableValuedFunction())
                out.tab(2).println("new %s(%s.name(name), null, parameters)", className, DSL.class);
            else
                out.tab(2).println("new %s(%s.name(name), null)", className, DSL.class);

            out.tab(1).println("}");

            out.tab(1).javadoc("Rename this table");
            out.tab(1).println("override def rename(name : %s) : %s = {", Name.class, className);

            if (table.isTableValuedFunction())
                out.tab(2).println("new %s(name, null, parameters)", className);
            else
                out.tab(2).println("new %s(name, null)", className);

            out.tab(1).println("}");
        }

        // [#2921] With instance fields, tables can be renamed.
        else if (generateInstanceFields()) {
            out.tab(1).javadoc("Rename this table");
            out.tab(1).override();
            out.tab(1).println("public %s rename(%s name) {", className, String.class);

            if (table.isTableValuedFunction())
                out.tab(2).println("return new %s(%s.name(name), null, parameters);", className, DSL.class);
            else
                out.tab(2).println("return new %s(%s.name(name), null);", className, DSL.class);

            out.tab(1).println("}");

            out.tab(1).javadoc("Rename this table");
            out.tab(1).override();
            out.tab(1).println("public %s rename(%s name) {", className, Name.class);

            if (table.isTableValuedFunction())
                out.tab(2).println("return new %s(name, null, parameters);", className);
            else
                out.tab(2).println("return new %s(name, null);", className);

            out.tab(1).println("}");
        }

        // [#7809] fieldsRow()
        int degree = table.getColumns().size();
        String rowType = refRowType(out, table.getColumns());

        if (generateRecordsImplementingRecordN() && degree > 0 && degree <= Constants.MAX_ROW_DEGREE) {
            out.tab(1).header("Row%s type methods", degree);

            if (scala) {
                out.println();
                out.tab(1).println("override def fieldsRow : %s[%s] = {", out.ref(Row.class.getName() + degree), rowType);
                out.tab(2).println("super.fieldsRow.asInstanceOf[ %s[%s] ]", out.ref(Row.class.getName() + degree), rowType);
                out.tab(1).println("}");
            }
            else {
                out.tab(1).overrideInherit();
                out.tab(1).println("public %s<%s> fieldsRow() {", out.ref(Row.class.getName() + degree), rowType);
                out.tab(2).println("return (%s) super.fieldsRow();", out.ref(Row.class.getName() + degree));
                out.tab(1).println("}");
            }
        }

        // [#1070] Table-valued functions should generate an additional set of call() methods
        if (table.isTableValuedFunction()) {
            for (boolean parametersAsField : new boolean[] { false, true }) {

                // Don't overload no-args call() methods
                if (parametersAsField && table.getParameters().size() == 0)
                    break;

                out.tab(1).javadoc("Call this table-valued function");

                if (scala) {
                    out.tab(1).print("def call(");
                    printParameterDeclarations(out, table, parametersAsField);
                    out.println(") : %s = {", className);

                    out.tab(2).println("return new %s(%s.name(getName()), null, %s(", className, DSL.class, out.ref("scala.Array"));
                    String separator = "  ";
                    for (ParameterDefinition parameter : table.getParameters()) {
                        final String paramArgName = getStrategy().getJavaMemberName(parameter);
                        final String paramTypeRef = getJavaTypeReference(parameter.getDatabase(), parameter.getType(resolver()));
                        final List<String> converter = out.ref(list(parameter.getType(resolver()).getConverter()));
                        final List<String> binding = out.ref(list(parameter.getType(resolver()).getBinding()));

                        out.tab(3).print(separator);

                        if (parametersAsField)
                            out.println("%s", paramArgName);
                        else
                            out.println("%s.value(%s, %s" + converterTemplateForTableValuedFunction(converter) + converterTemplateForTableValuedFunction(binding) + ")", DSL.class, paramArgName, paramTypeRef, converter, binding);

                        separator = ", ";
                    }

                    out.tab(2).println("));");
                    out.tab(1).println("}");
                }
                else {
                    out.tab(1).print("public %s call(", className);
                    printParameterDeclarations(out, table, parametersAsField);
                    out.println(") {");

                    out.tab(2).println("return new %s(%s.name(getName()), null, new %s[] { ", className, DSL.class, Field.class);
                    String separator = "  ";
                    for (ParameterDefinition parameter : table.getParameters()) {
                        final String paramArgName = getStrategy().getJavaMemberName(parameter);
                        final String paramTypeRef = getJavaTypeReference(parameter.getDatabase(), parameter.getType(resolver()));
                        final List<String> converter = out.ref(list(parameter.getType(resolver()).getConverter()));
                        final List<String> binding = out.ref(list(parameter.getType(resolver()).getBinding()));

                        out.tab(3).print(separator);

                        if (parametersAsField)
                            out.println("%s", paramArgName);
                        else
                            out.println("%s.val(%s, %s" + converterTemplateForTableValuedFunction(converter) + converterTemplateForTableValuedFunction(binding) + ")", DSL.class, paramArgName, paramTypeRef, converter, binding);

                        separator = ", ";
                    }

                    out.tab(2).println("});");
                    out.tab(1).println("}");
                }
            }
        }

        generateTableClassFooter(table, out);
        out.println("}");
        closeJavaWriter(out);
    }

    protected void generateEmbeddables(SchemaDefinition schema) {
        log.info("Generating embeddables");

        for (EmbeddableDefinition embeddable : database.getEmbeddables(schema)) {
            try {
                generateEmbeddable(schema, embeddable);
            }
            catch (Exception e) {
                log.error("Error while generating embeddable " + embeddable, e);
            }
        }

        watch.splitInfo("Tables generated");
    }

    @SuppressWarnings("unused")
    protected void generateEmbeddable(SchemaDefinition schema, EmbeddableDefinition embeddable) {
        JavaWriter out = newJavaWriter(getFile(embeddable, Mode.RECORD));
        generateRecord0(embeddable, out);
        closeJavaWriter(out);
    }

    private String converterTemplate(List<String> converter) {
        if (converter == null || converter.isEmpty())
            return "[[]]";
        if (converter.size() > 1)
            throw new IllegalArgumentException();
        switch (GenerationUtil.expressionType(converter.get(0))) {
            case CONSTRUCTOR_REFERENCE:
                return "[[before=, ][new %s()]]";
            case EXPRESSION:
                return "[[before=, ][%s]]";
            default:
                throw new IllegalArgumentException();
        }
    }

    private String converterTemplateForTableValuedFunction(List<String> converter) {
        if (converter == null || converter.isEmpty())
            return "[[]]";
        if (converter.size() > 1)
            throw new IllegalArgumentException();
        switch (GenerationUtil.expressionType(converter.get(0))) {
            case CONSTRUCTOR_REFERENCE:
                return "[[before=.asConvertedDataType(][after=)][new %s()]]";
            case EXPRESSION:
                return "[[before=.asConvertedDataType(][after=)][%s]]";
            default:
                throw new IllegalArgumentException();
        }
    }

    private String escapeString(String comment) {

        if (comment == null)
            return null;

        // [#3450] Escape also the escape sequence, among other things that break Java strings.
        return comment.replace("\\", "\\\\")
                      .replace("\"", "\\\"")
                      .replace("\n", "\\n")
                      .replace("\r", "\\r");
    }

    /**
     * Subclasses may override this method to provide table class footer code.
     */
    @SuppressWarnings("unused")
    protected void generateTableClassFooter(TableDefinition table, JavaWriter out) {}

    /**
     * Subclasses may override this method to provide their own Javadoc.
     */
    protected void generateTableClassJavadoc(TableDefinition table, JavaWriter out) {
        if (generateCommentsOnTables())
            printClassJavadoc(out, table);
        else
            printClassJavadoc(out, "The table <code>" + table.getQualifiedInputName() + "</code>.");
    }

    protected void generateSequences(SchemaDefinition schema) {
        log.info("Generating sequences");
        JavaWriter out = newJavaWriter(new File(getFile(schema).getParentFile(), "Sequences.java"));

        printPackage(out, schema);
        printClassJavadoc(out, "Convenience access to all sequences in " + schema.getOutputName());
        printClassAnnotations(out, schema);

        if (scala)
            out.println("object Sequences {");
        else
            out.println("public class Sequences {");

        boolean qualifySequenceClassReferences = containsConflictingDefinition(schema, database.getSequences(schema));

        for (SequenceDefinition sequence : database.getSequences(schema)) {
            final String seqTypeFull = getJavaType(sequence.getType(resolver()));
            final String seqType = out.ref(seqTypeFull);
            final String seqId = getStrategy().getJavaIdentifier(sequence);
            final String seqName = sequence.getOutputName();
            final String schemaId = qualifySequenceClassReferences ? getStrategy().getFullJavaIdentifier(schema)
                : out.ref(getStrategy().getFullJavaIdentifier(schema), 2);
            final String typeRef = getJavaTypeReference(sequence.getDatabase(), sequence.getType(resolver()));

            if (!printDeprecationIfUnknownType(out, seqTypeFull))
                out.tab(1).javadoc("The sequence <code>%s</code>", sequence.getQualifiedOutputName());

            boolean flags = generateSequenceFlags();

            if (scala)
                out.tab(1).println("val %s : %s[%s] = %s.createSequence(\"%s\", %s, %s, %s, %s, %s, %s, %s, %s)",
                    seqId,
                    Sequence.class,
                    seqType,
                    Internal.class,
                    seqName,
                    schemaId,
                    typeRef,
                    flags && sequence.getStartWith() != null ? sequence.getStartWith() + "L" : "null",
                    flags && sequence.getIncrementBy() != null ? sequence.getIncrementBy() + "L" : "null",
                    flags && sequence.getMinvalue() != null ? sequence.getMinvalue() + "L" : "null",
                    flags && sequence.getMaxvalue() != null ? sequence.getMaxvalue() + "L" : "null",
                    flags && sequence.getCycle(),
                    flags && sequence.getCache() != null ? sequence.getCache() + "L" : "null");
            else
                out.tab(1).println("public static final %s<%s> %s = %s.<%s> createSequence(\"%s\", %s, %s, %s, %s, %s, %s, %s, %s);",
                    Sequence.class,
                    seqType,
                    seqId,
                    Internal.class,
                    seqType,
                    seqName,
                    schemaId,
                    typeRef,
                    flags && sequence.getStartWith() != null ? sequence.getStartWith() + "L" : "null",
                    flags && sequence.getIncrementBy() != null ? sequence.getIncrementBy() + "L" : "null",
                    flags && sequence.getMinvalue() != null ? sequence.getMinvalue() + "L" : "null",
                    flags && sequence.getMaxvalue() != null ? sequence.getMaxvalue() + "L" : "null",
                    flags && sequence.getCycle(),
                    flags && sequence.getCache() != null ? sequence.getCache() + "L" : "null"
                );
        }

        out.println("}");
        closeJavaWriter(out);

        watch.splitInfo("Sequences generated");
    }

    private boolean containsConflictingDefinition(SchemaDefinition schema, List<? extends Definition> definitions) {
        final String unqualifiedSchemaId = getStrategy().getJavaIdentifier(schema);
        for (Definition def : definitions)
            if (unqualifiedSchemaId.equals(getStrategy().getJavaIdentifier(def)))
                return true;
        return false;
    }

    protected void generateCatalog(CatalogDefinition catalog) {
        JavaWriter out = newJavaWriter(getFile(catalog));
        log.info("");
        log.info("Generating catalog", out.file().getName());
        log.info("==========================================================");
        generateCatalog(catalog, out);
        closeJavaWriter(out);
    }

    protected void generateCatalog(CatalogDefinition catalog, JavaWriter out) {
        final String catalogName = catalog.getQualifiedOutputName();
        final String catalogId = getStrategy().getJavaIdentifier(catalog);
        final String className = getStrategy().getJavaClassName(catalog);
        final List<String> interfaces = out.ref(getStrategy().getJavaClassImplements(catalog, Mode.DEFAULT));

        printPackage(out, catalog);

        if (scala) {
            out.println("object %s {", className);
            out.tab(1).javadoc("The reference instance of <code>%s</code>", catalogName);
            out.tab(1).println("val %s = new %s", catalogId, className);
            out.println("}");
            out.println();
        }

        generateCatalogClassJavadoc(catalog, out);
        printClassAnnotations(out, null, catalog);

        if (scala) {
            out.println("class %s extends %s(\"%s\")[[before= with ][separator= with ][%s]] {", className, CatalogImpl.class, catalog.getOutputName(), interfaces);
        }
        else {
            out.println("public class %s extends %s[[before= implements ][%s]] {", className, CatalogImpl.class, interfaces);
            out.printSerial();
            out.tab(1).javadoc("The reference instance of <code>%s</code>", catalogName);
            out.tab(1).println("public static final %s %s = new %s();", className, catalogId, className);
        }

        List<SchemaDefinition> schemas = new ArrayList<>();
        if (generateGlobalSchemaReferences()) {
            for (SchemaDefinition schema : catalog.getSchemata()) {
                if (generateSchemaIfEmpty(schema)) {
                    schemas.add(schema);

                    final String schemaClassName = out.ref(getStrategy().getFullJavaClassName(schema));
                    final String schemaId = getStrategy().getJavaIdentifier(schema);
                    final String schemaFullId = getStrategy().getFullJavaIdentifier(schema);
                    final String schemaComment = !StringUtils.isBlank(schema.getComment()) && generateCommentsOnSchemas()
                        ? escapeEntities(schema.getComment())
                        : "The schema <code>" + schema.getQualifiedOutputName() + "</code>.";

                    out.tab(1).javadoc(schemaComment);

                    if (scala)
                        out.tab(1).println("val %s = %s", schemaId, schemaFullId);
                    else
                        out.tab(1).println("public final %s %s = %s;", schemaClassName, schemaId, schemaFullId);
                }
            }
        }

        if (scala)
            ;
        else {
            out.tab(1).javadoc(NO_FURTHER_INSTANCES_ALLOWED);
            out.tab(1).println("private %s() {", className);
            out.tab(2).println("super(\"%s\");", catalog.getOutputName());
            out.tab(1).println("}");
        }

        printReferences(out, schemas, Schema.class, false);

        generateCatalogClassFooter(catalog, out);
        out.println("}");
    }

    /**
     * Subclasses may override this method to provide catalog class footer code.
     */
    @SuppressWarnings("unused")
    protected void generateCatalogClassFooter(CatalogDefinition schema, JavaWriter out) {}

    /**
     * Subclasses may override this method to provide their own Javadoc.
     */
    protected void generateCatalogClassJavadoc(CatalogDefinition catalog, JavaWriter out) {
        if (generateCommentsOnCatalogs())
            printClassJavadoc(out, catalog);
        else
            printClassJavadoc(out, "The catalog <code>" + catalog.getQualifiedInputName() + "</code>.");
    }

    protected void generateSchema(SchemaDefinition schema) {
        JavaWriter out = newJavaWriter(getFile(schema));
        log.info("Generating schema", out.file().getName());
        log.info("----------------------------------------------------------");
        generateSchema(schema, out);
        closeJavaWriter(out);
    }

    protected void generateSchema(SchemaDefinition schema, JavaWriter out) {
        final String catalogId = out.ref(getStrategy().getFullJavaIdentifier(schema.getCatalog()), 2);
        final String schemaName = schema.getQualifiedOutputName();
        final String schemaId = getStrategy().getJavaIdentifier(schema);
        final String className = getStrategy().getJavaClassName(schema);
        final List<String> interfaces = out.ref(getStrategy().getJavaClassImplements(schema, Mode.DEFAULT));

        printPackage(out, schema);

        if (scala) {
            out.println("object %s {", className);
            out.tab(1).javadoc("The reference instance of <code>%s</code>", schemaName);
            out.tab(1).println("val %s = new %s", schemaId, className);
            out.println("}");
            out.println();
        }

        generateSchemaClassJavadoc(schema, out);
        printClassAnnotations(out, schema);

        if (scala) {
            out.println("class %s extends %s(\"%s\", %s)[[before= with ][separator= with ][%s]] {", className, SchemaImpl.class, schema.getOutputName(), catalogId, interfaces);
        }
        else {
            out.println("public class %s extends %s[[before= implements ][%s]] {", className, SchemaImpl.class, interfaces);
            out.printSerial();
            out.tab(1).javadoc("The reference instance of <code>%s</code>", schemaName);
            out.tab(1).println("public static final %s %s = new %s();", className, schemaId, className);

            if (generateGlobalTableReferences()) {
                for (TableDefinition table : schema.getTables()) {
                    final String tableClassName = out.ref(getStrategy().getFullJavaClassName(table));
                    final String tableId = getStrategy().getJavaIdentifier(table);
                    final String tableFullId = getStrategy().getFullJavaIdentifier(table);
                    final String tableComment = !StringUtils.isBlank(table.getComment()) && generateCommentsOnTables()
                        ? escapeEntities(table.getComment())
                        : "The table <code>" + table.getQualifiedOutputName() + "</code>.";

                    out.tab(1).javadoc(tableComment);

                    if (scala)
                        out.tab(1).println("val %s = %s", tableId, tableFullId);
                    else
                        out.tab(1).println("public final %s %s = %s;", tableClassName, tableId, tableFullId);

                    // [#3797] Table-valued functions generate two different literals in
                    // globalObjectReferences
                    if (table.isTableValuedFunction())
                        printTableValuedFunction(out, table, getStrategy().getJavaIdentifier(table));
                }
            }

            out.tab(1).javadoc(NO_FURTHER_INSTANCES_ALLOWED);
            out.tab(1).println("private %s() {", className);
            out.tab(2).println("super(\"%s\", null);", schema.getOutputName());
            out.tab(1).println("}");
        }

        out.println();
        if (scala) {
            out.tab(1).println("override def getCatalog : %s = %s", Catalog.class, catalogId);
        }
        else {
            out.tab(1).overrideInherit();
            out.tab(1).println("public %s getCatalog() {", Catalog.class);
            out.tab(2).println("return %s;", catalogId);
            out.tab(1).println("}");
        }

        // [#2255] Avoid referencing sequence literals, if they're not generated
        if (generateGlobalSequenceReferences())
            printReferences(out, database.getSequences(schema), Sequence.class, true);

        if (generateGlobalTableReferences())
            printReferences(out, database.getTables(schema), Table.class, true);

        if (generateGlobalUDTReferences())
            printReferences(out, database.getUDTs(schema), UDT.class, true);

        generateSchemaClassFooter(schema, out);
        out.println("}");
    }

    /**
     * Subclasses may override this method to provide schema class footer code.
     */
    @SuppressWarnings("unused")
    protected void generateSchemaClassFooter(SchemaDefinition schema, JavaWriter out) {}

    /**
     * Subclasses may override this method to provide their own Javadoc.
     */
    protected void generateSchemaClassJavadoc(SchemaDefinition schema, JavaWriter out) {
        if (generateCommentsOnSchemas())
            printClassJavadoc(out, schema);
        else
            printClassJavadoc(out, "The schema <code>" + schema.getQualifiedInputName() + "</code>.");
    }

    protected void printFromAndInto(JavaWriter out, TableDefinition table) {
        printFromAndInto(out, (Definition) table);
    }

    private void printFromAndInto(JavaWriter out, Definition tableOrUDT) {
        String qualified = out.ref(getStrategy().getFullJavaClassName(tableOrUDT, Mode.INTERFACE));

        out.tab(1).header("FROM and INTO");
        out.tab(1).overrideInheritIf(generateInterfaces() && !generateImmutableInterfaces());
        out.tab(1).println("public void from(%s from) {", qualified);

        for (TypedElementDefinition<?> column : getTypedElements(tableOrUDT)) {
            String setter = getStrategy().getJavaSetterName(column, Mode.INTERFACE);
            String getter = getStrategy().getJavaGetterName(column, Mode.INTERFACE);

            if (scala)
                out.tab(2).println("%s(from.%s)", setter, getter);
            else
                out.tab(2).println("%s(from.%s());", setter, getter);
        }

        out.tab(1).println("}");

        if (generateInterfaces() && !generateImmutableInterfaces()) {
            if (scala) {
                out.tab(1).println("public <E extends %s> E into(E into) {", qualified);
                out.tab(2).println("into.from(this)");
                out.tab(2).println("return into");
                out.tab(1).println("}");
            }
            else {
                out.tab(1).overrideInherit();
                out.tab(1).println("public <E extends %s> E into(E into) {", qualified);
                out.tab(2).println("into.from(this);");
                out.tab(2).println("return into;");
                out.tab(1).println("}");
            }
        }
    }

    protected void printReferences(JavaWriter out, List<? extends Definition> definitions, Class<?> type, boolean isGeneric) {
        if (out != null && !definitions.isEmpty()) {
            final String generic = isGeneric ? (scala ? "[_]" : "<?>") : "";
            final List<String> references = out.ref(getStrategy().getFullJavaIdentifiers(definitions), 2);

            out.println();

            if (scala) {
                out.tab(1).println("override def get%ss : %s[%s%s] = {", type.getSimpleName(), List.class, type, generic);
                out.tab(2).println("val result = new %s[%s%s]", ArrayList.class, type, generic);
                for (int i = 0; i < definitions.size(); i += INITIALISER_SIZE) {
                    out.tab(2).println("result.addAll(get%ss%s)", type.getSimpleName(), i / INITIALISER_SIZE);
                }
                out.tab(2).println("result");
                out.tab(1).println("}");
            }
            else {
                out.tab(1).override();
                out.tab(1).println("public final %s<%s%s> get%ss() {", List.class, type, generic, type.getSimpleName());
                out.tab(2).println("%s result = new %s();", List.class, ArrayList.class);
                for (int i = 0; i < definitions.size(); i += INITIALISER_SIZE) {
                    out.tab(2).println("result.addAll(get%ss%s());", type.getSimpleName(), i / INITIALISER_SIZE);
                }
                out.tab(2).println("return result;");
                out.tab(1).println("}");
            }

            for (int i = 0; i < definitions.size(); i += INITIALISER_SIZE) {
                out.println();

                if (scala) {
                    out.tab(1).println("private def get%ss%s(): %s[%s%s] = {", type.getSimpleName(), i / INITIALISER_SIZE, List.class, type, generic);
                    out.tab(2).println("return %s.asList[%s%s]([[before=\n\t\t\t][separator=,\n\t\t\t][%s]])", Arrays.class, type, generic, references.subList(i, Math.min(i + INITIALISER_SIZE, references.size())));
                    out.tab(1).println("}");
                }
                else {
                    out.tab(1).println("private final %s<%s%s> get%ss%s() {", List.class, type, generic, type.getSimpleName(), i / INITIALISER_SIZE);
                    out.tab(2).println("return %s.<%s%s>asList([[before=\n\t\t\t][separator=,\n\t\t\t][%s]]);", Arrays.class, type, generic, references.subList(i, Math.min(i + INITIALISER_SIZE, references.size())));
                    out.tab(1).println("}");
                }
            }
        }
    }

    protected void printTableJPAAnnotation(JavaWriter out, TableDefinition table) {
        SchemaDefinition schema = table.getSchema();

        if (generateJPAAnnotations()) {
            // Since JPA 1.0
            out.println("@%s", out.ref("javax.persistence.Entity"));

            // Since JPA 1.0
            out.print("@%s(name = \"", out.ref("javax.persistence.Table"));
            out.print(table.getName().replace("\"", "\\\""));
            out.print("\"");

            if (!schema.isDefaultSchema()) {
                out.print(", schema = \"");
                out.print(schema.getOutputName().replace("\"", "\\\""));
                out.print("\"");
            }

            StringBuilder sb1 = new StringBuilder();
            String glue1 = generateNewline();

            for (UniqueKeyDefinition uk : table.getUniqueKeys()) {
                sb1.append(glue1);
                sb1.append("\t")
                   .append(scala ? "new " : "@")

                   // Since JPA 1.0
                   .append(out.ref("javax.persistence.UniqueConstraint"))
                   .append("(");

                if (!StringUtils.isBlank(uk.getOutputName()))
                    sb1.append("name = \"" + uk.getOutputName().replace("\"", "\\\"") + "\", ");

                sb1.append("columnNames = ")
                   .append(scala ? "Array(" : "{");

                String glue1Inner = "";
                for (ColumnDefinition column : uk.getKeyColumns()) {
                    sb1.append(glue1Inner);
                    sb1.append("\"");
                    sb1.append(column.getName().replace("\"", "\\\""));
                    sb1.append("\"");

                    glue1Inner = ", ";
                }

                sb1.append(scala ? ")" : "}").append(")");

                glue1 = "," + generateNewline();
            }

            if (sb1.length() > 0) {
                out.print(", uniqueConstraints = ")
                   .print(scala ? "Array(" : "{");
                out.println(sb1.toString());
                out.print(scala ? ")" : "}");
            }

            if (StringUtils.isBlank(generateJPAVersion()) || "2.1".compareTo(generateJPAVersion()) <= 0) {
                StringBuilder sb2 = new StringBuilder();
                String glue2 = generateNewline();

                for (IndexDefinition index : table.getIndexes()) {
                    sb2.append(glue2);
                    sb2.append("\t")
                       .append(scala ? "new " : "@")
                       .append(out.ref("javax.persistence.Index"))
                       .append("(name = \"").append(index.getOutputName().replace("\"", "\\\"")).append("\"");

                    if (index.isUnique())
                        sb2.append(", unique = true");

                    sb2.append(", columnList = \"");

                    String glue2Inner = "";
                    for (IndexColumnDefinition column : index.getIndexColumns()) {
                        sb2.append(glue2Inner)
                           .append(column.getOutputName().replace("\"", "\\\""));

                        if (column.getSortOrder() == SortOrder.ASC)
                            sb2.append(" ASC");
                        else if (column.getSortOrder() == SortOrder.DESC)
                            sb2.append(" DESC");

                        glue2Inner = ", ";
                    }

                    sb2.append("\")");
                    glue2 = "," + generateNewline();
                }

                if (sb2.length() > 0) {
                    out.print(", indexes = ")
                       .print(scala ? "Array(" : "{");
                    out.println(sb2.toString());
                    out.print(scala ? ")" : "}");
                }
            }

            out.println(")");
        }
    }

    protected void printColumnJPAAnnotation(JavaWriter out, ColumnDefinition column) {
        if (generateJPAAnnotations()) {
            UniqueKeyDefinition pk = column.getPrimaryKey();

            if (pk != null) {
                if (pk.getKeyColumns().size() == 1) {

                    // Since JPA 1.0
                    out.tab(1).println("@%s", out.ref("javax.persistence.Id"));

                    // Since JPA 1.0
                    if (pk.getKeyColumns().get(0).isIdentity())
                        out.tab(1).println("@%s(strategy = %s.IDENTITY)",
                            out.ref("javax.persistence.GeneratedValue"),
                            out.ref("javax.persistence.GenerationType"));
                }
            }

            String nullable = "";
            if (!column.getType(resolver()).isNullable()) {
                nullable = ", nullable = false";
            }

            String length = "";
            String precision = "";
            String scale = "";

            if (column.getType(resolver()).getLength() > 0) {
                length = ", length = " + column.getType(resolver()).getLength();
            }
            else if (column.getType(resolver()).getPrecision() > 0) {
                precision = ", precision = " + column.getType(resolver()).getPrecision();

                if (column.getType(resolver()).getScale() > 0) {
                    scale = ", scale = " + column.getType(resolver()).getScale();
                }
            }

            // [#8535] The unique flag is not set on the column, but only on
            //         the table's @UniqueConstraint section.

            // Since JPA 1.0
            out.print("\t@%s(name = \"", out.ref("javax.persistence.Column"));
            out.print(column.getName().replace("\"", "\\\""));
            out.print("\"");
            out.print(nullable);
            out.print(length);
            out.print(precision);
            out.print(scale);
            out.println(")");
        }
    }

    /**
     * @deprecated - This method is no longer used by the generator.
     */
    @Deprecated
    protected void printColumnValidationAnnotation(JavaWriter out, ColumnDefinition column) {
        printValidationAnnotation(out, column);
    }

    private void printValidationAnnotation(JavaWriter out, TypedElementDefinition<?> column) {
        if (generateValidationAnnotations()) {
            DataTypeDefinition type = column.getType(resolver());

            // [#5128] defaulted columns are nullable in Java
            if (!column.getType(resolver()).isNullable() &&
                !column.getType(resolver()).isDefaulted() &&
                !column.getType(resolver()).isIdentity())
                out.tab(1).println("@%s", out.ref("javax.validation.constraints.NotNull"));

            String javaType = getJavaType(type);
            if ("java.lang.String".equals(javaType) || "byte[]".equals(javaType)) {
                int length = type.getLength();

                if (length > 0)
                    out.tab(1).println("@%s(max = %s)", out.ref("javax.validation.constraints.Size"), length);
            }
        }
    }

    private boolean printDeprecationIfUnknownTypes(JavaWriter out, Collection<? extends ParameterDefinition> params) {
        for (ParameterDefinition param : params)
            if (printDeprecationIfUnknownType(out, getJavaType(param.getType(resolver()))))
                return true;

        return false;
    }

    private boolean printDeprecationIfUnknownType(JavaWriter out, String type) {
        return printDeprecationIfUnknownType(out, type, 1);
    }

    private boolean printDeprecationIfUnknownType(JavaWriter out, String type, int indentation) {
        if (generateDeprecationOnUnknownTypes() && "java.lang.Object".equals(type)) {
            out.tab(indentation).javadoc("@deprecated Unknown data type. "
                + "Please define an explicit {@link org.jooq.Binding} to specify how this "
                + "type should be handled. Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} "
                + "in your code generator configuration.");
            out.tab(indentation).println("@java.lang.Deprecated");
            return true;
        }
        else {
            return false;
        }
    }

    @SuppressWarnings("unused")
    protected void generateRoutine(SchemaDefinition schema, RoutineDefinition routine) {
        JavaWriter out = newJavaWriter(getFile(routine));
        log.info("Generating routine", out.file().getName());
        generateRoutine(routine, out);
        closeJavaWriter(out);
    }

    protected void generateRoutine(RoutineDefinition routine, JavaWriter out) {
        final SchemaDefinition schema = routine.getSchema();
        final String className = getStrategy().getJavaClassName(routine);
        final String returnTypeFull = (routine.getReturnValue() == null)
            ? Void.class.getName()
            : getJavaType(routine.getReturnType(resolver()));
        final String returnType = (routine.getReturnValue() == null)
            ? Void.class.getName()
            : out.ref(returnTypeFull);
        final List<String> returnTypeRef = list((routine.getReturnValue() != null)
            ? getJavaTypeReference(database, routine.getReturnType(resolver()))
            : null);
        final List<String> returnConverter = out.ref(list(
             (routine.getReturnValue() != null)
            ? routine.getReturnType(resolver()).getConverter()
            : null));
        final List<String> returnBinding = out.ref(list(
             (routine.getReturnValue() != null)
            ? routine.getReturnType(resolver()).getBinding()
            : null));

        final List<String> interfaces = out.ref(getStrategy().getJavaClassImplements(routine, Mode.DEFAULT));
        final String schemaId = out.ref(getStrategy().getFullJavaIdentifier(schema), 2);
        final List<String> packageId = out.ref(getStrategy().getFullJavaIdentifiers(routine.getPackage()), 2);

        printPackage(out, routine);

        if (scala) {
            out.println("object %s {", className);
            for (ParameterDefinition parameter : routine.getAllParameters()) {
                final String paramTypeFull = getJavaType(parameter.getType(resolver()));
                final String paramType = out.ref(paramTypeFull);
                final String paramTypeRef = getJavaTypeReference(parameter.getDatabase(), parameter.getType(resolver()));
                final String paramId = out.ref(getStrategy().getJavaIdentifier(parameter), 2);
                final String paramName = parameter.getName();
                final String paramComment = StringUtils.defaultString(parameter.getComment());
                final String isDefaulted = parameter.isDefaulted() ? "true" : "false";
                final String isUnnamed = parameter.isUnnamed() ? "true" : "false";
                final List<String> converter = out.ref(list(parameter.getType(resolver()).getConverter()));
                final List<String> binding = out.ref(list(parameter.getType(resolver()).getBinding()));

                if (!printDeprecationIfUnknownType(out, paramTypeFull))
                    out.tab(1).javadoc("The parameter <code>%s</code>.%s", parameter.getQualifiedOutputName(), parameterComment(paramComment));

                out.tab(1).println("val %s : %s[%s] = %s.createParameter(\"%s\", %s, %s, %s" + converterTemplate(converter) + converterTemplate(binding) + ")",
                        paramId, Parameter.class, paramType, Internal.class, paramName, paramTypeRef, isDefaulted, isUnnamed, converter, binding);
            }

            out.println("}");
            out.println();
        }

        if (!printDeprecationIfUnknownType(out, returnTypeFull, 0))
            generateRoutineClassJavadoc(routine, out);

        printClassAnnotations(out, schema);

        if (scala) {
            out.println("class %s extends %s[%s](\"%s\", %s[[before=, ][%s]][[before=, ][%s]]" + converterTemplate(returnConverter) + converterTemplate(returnBinding) + ")[[before= with ][separator= with ][%s]] {",
                    className, AbstractRoutine.class, returnType, routine.getName(), schemaId, packageId, returnTypeRef, returnConverter, returnBinding, interfaces);
        }
        else {
            out.println("public class %s extends %s<%s>[[before= implements ][%s]] {",
                    className, AbstractRoutine.class, returnType, interfaces);
            out.printSerial();

            for (ParameterDefinition parameter : routine.getAllParameters()) {
                final String paramTypeFull = getJavaType(parameter.getType(resolver()));
                final String paramType = out.ref(paramTypeFull);
                final String paramTypeRef = getJavaTypeReference(parameter.getDatabase(), parameter.getType(resolver()));
                final String paramId = out.ref(getStrategy().getJavaIdentifier(parameter), 2);
                final String paramName = parameter.getName();
                final String paramComment = StringUtils.defaultString(parameter.getComment());
                final String isDefaulted = parameter.isDefaulted() ? "true" : "false";
                final String isUnnamed = parameter.isUnnamed() ? "true" : "false";
                final List<String> converter = out.ref(list(parameter.getType(resolver()).getConverter()));
                final List<String> binding = out.ref(list(parameter.getType(resolver()).getBinding()));

                if (!printDeprecationIfUnknownType(out, paramTypeFull))
                    out.tab(1).javadoc("The parameter <code>%s</code>.%s", parameter.getQualifiedOutputName(), parameterComment(paramComment));

                out.tab(1).println("public static final %s<%s> %s = %s.createParameter(\"%s\", %s, %s, %s" + converterTemplate(converter) + converterTemplate(binding) + ");",
                        Parameter.class, paramType, paramId, Internal.class, paramName, paramTypeRef, isDefaulted, isUnnamed, converter, binding);
            }
        }


        if (scala) {
            out.tab(1).println("{");
        }
        else {
            out.tab(1).javadoc("Create a new routine call instance");
            out.tab(1).println("public %s() {", className);
            out.tab(2).println("super(\"%s\", %s[[before=, ][%s]][[before=, ][%s]]" + converterTemplate(returnConverter) + converterTemplate(returnBinding) + ");", routine.getName(), schemaId, packageId, returnTypeRef, returnConverter, returnBinding);


            if (routine.getAllParameters().size() > 0) {
                out.println();
            }
        }

        for (ParameterDefinition parameter : routine.getAllParameters()) {
            final String paramId = getStrategy().getJavaIdentifier(parameter);

            if (parameter.equals(routine.getReturnValue())) {
                if (scala)
                    out.tab(2).println("setReturnParameter(%s.%s)", className, paramId);
                else
                    out.tab(2).println("setReturnParameter(%s);", paramId);
            }
            else if (routine.getInParameters().contains(parameter)) {
                if (routine.getOutParameters().contains(parameter)) {
                    if (scala)
                        out.tab(2).println("addInOutParameter(%s.%s)", className, paramId);
                    else
                        out.tab(2).println("addInOutParameter(%s);", paramId);
                }
                else {
                    if (scala)
                        out.tab(2).println("addInParameter(%s.%s)", className, paramId);
                    else
                        out.tab(2).println("addInParameter(%s);", paramId);
                }
            }
            else {
                if (scala)
                    out.tab(2).println("addOutParameter(%s.%s)", className, paramId);
                else
                    out.tab(2).println("addOutParameter(%s);", paramId);
            }











        }

        if (routine.getOverload() != null) {
            if (scala)
                out.tab(2).println("setOverloaded(true)");
            else
                out.tab(2).println("setOverloaded(true);");
        }










        out.tab(1).println("}");

        for (ParameterDefinition parameter : routine.getInParameters()) {
            final String setterReturnType = generateFluentSetters() ? className : (scala ? "Unit" : "void");
            final String setter = getStrategy().getJavaSetterName(parameter, Mode.DEFAULT);
            final String numberValue = parameter.getType(resolver()).isGenericNumberType() ? "Number" : "Value";
            final String numberField = parameter.getType(resolver()).isGenericNumberType() ? "Number" : "Field";
            final String paramId = getStrategy().getJavaIdentifier(parameter);
            final String paramName = "value".equals(paramId) ? "value_" : "value";

            out.tab(1).javadoc("Set the <code>%s</code> parameter IN value to the routine", parameter.getOutputName());

            if (scala) {
                out.tab(1).println("def %s(%s : %s) : Unit = {", setter, paramName, refNumberType(out, parameter.getType(resolver())));
                out.tab(2).println("set%s(%s.%s, %s)", numberValue, className, paramId, paramName);
                out.tab(1).println("}");
            }
            else {
                out.tab(1).println("public void %s(%s %s) {", setter, varargsIfArray(refNumberType(out, parameter.getType(resolver()))), paramName);
                out.tab(2).println("set%s(%s, %s);", numberValue, paramId, paramName);
                out.tab(1).println("}");
            }

            if (routine.isSQLUsable()) {
                out.tab(1).javadoc("Set the <code>%s</code> parameter to the function to be used with a {@link org.jooq.Select} statement", parameter.getOutputName());

                if (scala) {
                    out.tab(1).println("def %s(field : %s[%s]) : %s = {", setter, Field.class, refExtendsNumberType(out, parameter.getType(resolver())), setterReturnType);
                    out.tab(2).println("set%s(%s.%s, field)", numberField, className, paramId);
                    if (generateFluentSetters())
                        out.tab(2).println("this");
                    out.tab(1).println("}");
                }
                else {
                    out.tab(1).println("public %s %s(%s<%s> field) {", setterReturnType, setter, Field.class, refExtendsNumberType(out, parameter.getType(resolver())));
                    out.tab(2).println("set%s(%s, field);", numberField, paramId);
                    if (generateFluentSetters())
                        out.tab(2).println("return this;");
                    out.tab(1).println("}");
                }
            }
        }

        for (ParameterDefinition parameter : routine.getAllParameters()) {
            boolean isReturnValue = parameter.equals(routine.getReturnValue());
            boolean isOutParameter = routine.getOutParameters().contains(parameter);

            if (isOutParameter && !isReturnValue) {
                final String paramName = parameter.getOutputName();
                final String paramTypeFull = getJavaType(parameter.getType(resolver()));
                final String paramType = out.ref(paramTypeFull);
                final String paramGetter = getStrategy().getJavaGetterName(parameter, Mode.DEFAULT);
                final String paramId = getStrategy().getJavaIdentifier(parameter);

                if (!printDeprecationIfUnknownType(out, paramTypeFull))
                    out.tab(1).javadoc("Get the <code>%s</code> parameter OUT value from the routine", paramName);

                if (scala) {
                    out.tab(1).println("def %s : %s = {", paramGetter, paramType);
                    out.tab(2).println("get(%s.%s)", className, paramId);
                    out.tab(1).println("}");
                }
                else {
                    out.tab(1).println("public %s %s() {", paramType, paramGetter);
                    out.tab(2).println("return get(%s);", paramId);
                    out.tab(1).println("}");
                }
            }
        }

        generateRoutineClassFooter(routine, out);
        out.println("}");
    }

    /**
     * Subclasses may override this method to provide routine class footer code.
     */
    @SuppressWarnings("unused")
    protected void generateRoutineClassFooter(RoutineDefinition routine, JavaWriter out) {}

    /**
     * Subclasses may override this method to provide their own Javadoc.
     */
    protected void generateRoutineClassJavadoc(RoutineDefinition routine, JavaWriter out) {
        if (generateCommentsOnRoutines())
            printClassJavadoc(out, routine);
        else
            printClassJavadoc(out, "The routine <code>" + routine.getQualifiedInputName() + "</code>.");
    }

    protected void printConvenienceMethodFunctionAsField(JavaWriter out, RoutineDefinition function, boolean parametersAsField) {
        // [#281] - Java can't handle more than 255 method parameters
        if (function.getInParameters().size() > 254) {
            log.warn("Too many parameters", "Function " + function + " has more than 254 in parameters. Skipping generation of convenience method.");
            return;
        }

        // Do not generate separate convenience methods, if there are no IN
        // parameters. They would have the same signature and no additional
        // meaning
        if (parametersAsField && function.getInParameters().isEmpty()) {
            return;
        }

        final String functionTypeFull = getJavaType(function.getReturnType(resolver()));
        final String functionType = out.ref(functionTypeFull);
        final String className = out.ref(getStrategy().getFullJavaClassName(function));
        final String localVar = disambiguateJavaMemberName(function.getInParameters(), "f");

        if (!printDeprecationIfUnknownType(out, functionTypeFull) &&
            !printDeprecationIfUnknownTypes(out, function.getInParameters()))
            out.tab(1).javadoc("Get <code>%s</code> as a field.", function.getQualifiedOutputName());

        if (scala)
            out.tab(1).print("def %s(",
                getStrategy().getJavaMethodName(function, Mode.DEFAULT));
        else
            out.tab(1).print("public static %s<%s> %s(",
                function.isAggregate() ? AggregateFunction.class : Field.class,
                functionType,
                getStrategy().getJavaMethodName(function, Mode.DEFAULT));

        String separator = "";
        for (ParameterDefinition parameter : function.getInParameters()) {
            out.print(separator);

            if (scala) {
                out.print("%s : ", getStrategy().getJavaMemberName(parameter));

                if (parametersAsField) {
                    out.print("%s[%s]", Field.class, refExtendsNumberType(out, parameter.getType(resolver())));
                } else {
                    out.print(refNumberType(out, parameter.getType(resolver())));
                }
            }
            else {
                if (parametersAsField) {
                    out.print("%s<%s>", Field.class, refExtendsNumberType(out, parameter.getType(resolver())));
                } else {
                    out.print(refNumberType(out, parameter.getType(resolver())));
                }

                out.print(" %s", getStrategy().getJavaMemberName(parameter));
            }

            separator = ", ";
        }

        if (scala) {
            out.println(") : %s[%s] = {",
                function.isAggregate() ? AggregateFunction.class : Field.class,
                functionType);
            out.tab(2).println("val %s = new %s", localVar, className);
        }
        else {
            out.println(") {");
            out.tab(2).println("%s %s = new %s();", className, localVar, className);
        }

        for (ParameterDefinition parameter : function.getInParameters()) {
            final String paramSetter = getStrategy().getJavaSetterName(parameter, Mode.DEFAULT);
            final String paramMember = getStrategy().getJavaMemberName(parameter);

            if (scala)
                out.tab(2).println("%s.%s(%s)", localVar, paramSetter, paramMember);
            else
                out.tab(2).println("%s.%s(%s);", localVar, paramSetter, paramMember);
        }

        out.println();

        if (scala)
            out.tab(2).println("return %s.as%s", localVar, function.isAggregate() ? "AggregateFunction" : "Field");
        else
            out.tab(2).println("return %s.as%s();", localVar, function.isAggregate() ? "AggregateFunction" : "Field");

        out.tab(1).println("}");
    }

    protected void printConvenienceMethodTableValuedFunctionAsField(JavaWriter out, TableDefinition function, boolean parametersAsField, String javaMethodName) {
        // [#281] - Java can't handle more than 255 method parameters
        if (function.getParameters().size() > 254) {
            log.warn("Too many parameters", "Function " + function + " has more than 254 in parameters. Skipping generation of convenience method.");
            return;
        }

        // Do not generate separate convenience methods, if there are no IN
        // parameters. They would have the same signature and no additional
        // meaning
        if (parametersAsField && function.getParameters().isEmpty()) {
            return;
        }

        final String className = out.ref(getStrategy().getFullJavaClassName(function));

        if (!printDeprecationIfUnknownTypes(out, function.getParameters()))
            out.tab(1).javadoc("Get <code>%s</code> as a table.", function.getQualifiedOutputName());

        if (scala)
            out.tab(1).print("def %s(", javaMethodName);
        else
            out.tab(1).print("public static %s %s(", className, javaMethodName);

        printParameterDeclarations(out, function, parametersAsField);

        if (scala) {
            out.println(") : %s = {", className);
            // [#5765] To prevent name clashes, this identifier is not imported
            out.tab(2).print("%s.call(", getStrategy().getFullJavaIdentifier(function));
        }
        else {
            out.println(") {");
            // [#5765] To prevent name clashes, this identifier is not imported
            out.tab(2).print("return %s.call(", getStrategy().getFullJavaIdentifier(function));
        }

        String separator = "";
        for (ParameterDefinition parameter : function.getParameters()) {
            out.print(separator);
            out.print("%s", getStrategy().getJavaMemberName(parameter));

            separator = ", ";
        }

        if (scala)
            out.println(")");
        else
            out.println(");");

        out.tab(1).println("}");
    }

    private void printParameterDeclarations(JavaWriter out, TableDefinition function, boolean parametersAsField) {
        String sep1 = "";
        for (ParameterDefinition parameter : function.getParameters()) {
            out.print(sep1);

            if (scala) {
                out.print("%s : ", getStrategy().getJavaMemberName(parameter));

                if (parametersAsField) {
                    out.print("%s[%s]", Field.class, refExtendsNumberType(out, parameter.getType(resolver())));
                }
                else {
                    out.print(refNumberType(out, parameter.getType(resolver())));
                }
            }
            else {
                if (parametersAsField) {
                    out.print("%s<%s>", Field.class, refExtendsNumberType(out, parameter.getType(resolver())));
                }
                else {
                    out.print(refNumberType(out, parameter.getType(resolver())));
                }

                out.print(" %s", getStrategy().getJavaMemberName(parameter));
            }

            sep1 = ", ";
        }
    }

    private String disambiguateJavaMemberName(Collection<? extends Definition> definitions, String defaultName) {

        // [#2502] - Some name mangling in the event of procedure arguments
        // called "configuration"
        Set<String> names = new HashSet<>();

        for (Definition definition : definitions)
            names.add(getStrategy().getJavaMemberName(definition));

        String name = defaultName;

        while (names.contains(name))
            name += "_";

        return name;
    }

    protected void printConvenienceMethodFunction(JavaWriter out, RoutineDefinition function, boolean instance) {
        // [#281] - Java can't handle more than 255 method parameters
        if (function.getInParameters().size() > 254) {
            log.warn("Too many parameters", "Function " + function + " has more than 254 in parameters. Skipping generation of convenience method.");
            return;
        }

        final String className = out.ref(getStrategy().getFullJavaClassName(function));
        final String functionName = function.getQualifiedOutputName();
        final String functionTypeFull = getJavaType(function.getReturnType(resolver()));
        final String functionType = out.ref(functionTypeFull);
        final String methodName = getStrategy().getJavaMethodName(function, Mode.DEFAULT);

        // [#3456] Local variables should not collide with actual function arguments
        final String configurationArgument = disambiguateJavaMemberName(function.getInParameters(), "configuration");
        final String localVar = disambiguateJavaMemberName(function.getInParameters(), "f");

        if (!printDeprecationIfUnknownType(out, functionTypeFull) &&
            !printDeprecationIfUnknownTypes(out, function.getInParameters()))
            out.tab(1).javadoc("Call <code>%s</code>", functionName);

        if (scala)
            out.tab(1).print("def %s(", methodName);
        else
            out.tab(1).print("public %s%s %s(", !instance ? "static " : "", functionType, methodName);

        String glue = "";
        if (!instance) {
            if (scala)
                out.print("%s : %s", configurationArgument, Configuration.class);
            else
                out.print("%s %s", Configuration.class, configurationArgument);

            glue = ", ";
        }

        for (ParameterDefinition parameter : function.getInParameters()) {
            // Skip SELF parameter
            if (instance && parameter.equals(function.getInParameters().get(0))) {
                continue;
            }

            final String paramType = refNumberType(out, parameter.getType(resolver()));
            final String paramMember = getStrategy().getJavaMemberName(parameter);

            if (scala)
                out.print("%s%s : %s", glue, paramMember, paramType);
            else
                out.print("%s%s %s", glue, paramType, paramMember);

            glue = ", ";
        }

        if (scala) {
            out.println(") : %s = {", functionType);
            out.tab(2).println("val %s = new %s()", localVar, className);
        }
        else {
            out.println(") {");
            out.tab(2).println("%s %s = new %s();", className, localVar, className);
        }

        for (ParameterDefinition parameter : function.getInParameters()) {
            final String paramSetter = getStrategy().getJavaSetterName(parameter, Mode.DEFAULT);
            final String paramMember = (instance && parameter.equals(function.getInParameters().get(0)))
                ? "this"
                : getStrategy().getJavaMemberName(parameter);

            if (scala)
                out.tab(2).println("%s.%s(%s)", localVar, paramSetter, paramMember);
            else
                out.tab(2).println("%s.%s(%s);", localVar, paramSetter, paramMember);
        }

        out.println();

        if (scala)
            out.tab(2).println("%s.execute(%s)", localVar, instance ? "configuration()" : configurationArgument);
        else
            out.tab(2).println("%s.execute(%s);", localVar, instance ? "configuration()" : configurationArgument);

        // TODO [#956] Find a way to register "SELF" as OUT parameter
        // in case this is a UDT instance (member) function

        if (scala)
            out.tab(2).println("%s.getReturnValue", localVar);
        else
            out.tab(2).println("return %s.getReturnValue();", localVar);

        out.tab(1).println("}");
    }

    protected void printConvenienceMethodProcedure(JavaWriter out, RoutineDefinition procedure, boolean instance) {
        // [#281] - Java can't handle more than 255 method parameters
        if (procedure.getInParameters().size() > 254) {
            log.warn("Too many parameters", "Procedure " + procedure + " has more than 254 in parameters. Skipping generation of convenience method.");
            return;
        }

        final String className = out.ref(getStrategy().getFullJavaClassName(procedure));
        final String configurationArgument = disambiguateJavaMemberName(procedure.getInParameters(), "configuration");
        final String localVar = disambiguateJavaMemberName(procedure.getInParameters(), "p");
        final List<ParameterDefinition> outParams = list(procedure.getReturnValue(), procedure.getOutParameters());

        if (!printDeprecationIfUnknownTypes(out, procedure.getAllParameters()))
            out.tab(1).javadoc("Call <code>%s</code>", procedure.getQualifiedOutputName());

        if (scala) {
            out.tab(1).print("def ");
        }
        else {
            out.tab(1).print("public ");

            if (!instance) {
                out.print("static ");
            }

            if (outParams.size() == 0) {
                out.print("void ");
            }
            else if (outParams.size() == 1) {
                out.print(out.ref(getJavaType(outParams.get(0).getType(resolver()))));
                out.print(" ");
            }
            else {
                out.print(className + " ");
            }
        }

        out.print(getStrategy().getJavaMethodName(procedure, Mode.DEFAULT));
        out.print("(");

        String glue = "";
        if (!instance) {
            if (scala)
                out.print("%s : %s", configurationArgument, Configuration.class);
            else
                out.print("%s %s", Configuration.class, configurationArgument);

            glue = ", ";
        }

        for (ParameterDefinition parameter : procedure.getInParameters()) {
            // Skip SELF parameter
            if (instance && parameter.equals(procedure.getInParameters().get(0))) {
                continue;
            }

            out.print(glue);

            if (scala)
                out.print("%s : %s", getStrategy().getJavaMemberName(parameter), refNumberType(out, parameter.getType(resolver())));
            else
                out.print("%s %s", refNumberType(out, parameter.getType(resolver())), getStrategy().getJavaMemberName(parameter));

            glue = ", ";
        }

        if (scala) {
            out.print(") : ");

            if (outParams.size() == 0) {
                out.print("Unit");
            }
            else if (outParams.size() == 1) {
                out.print(out.ref(getJavaType(outParams.get(0).getType(resolver()))));
            }
            else {
                out.print(className);
            }


            out.println(" = {");
            out.tab(2).println("val %s = new %s", localVar, className);
        }
        else {
            out.println(") {");
            out.tab(2).println("%s %s = new %s();", className, localVar, className);
        }

        for (ParameterDefinition parameter : procedure.getInParameters()) {
            final String setter = getStrategy().getJavaSetterName(parameter, Mode.DEFAULT);
            final String arg = (instance && parameter.equals(procedure.getInParameters().get(0)))
                ? "this"
                : getStrategy().getJavaMemberName(parameter);

            if (scala)
                out.tab(2).println("%s.%s(%s)", localVar, setter, arg);
            else
                out.tab(2).println("%s.%s(%s);", localVar, setter, arg);
        }

        out.println();

        if (scala)
            out.tab(2).println("%s.execute(%s)", localVar, instance ? "configuration()" : configurationArgument);
        else
            out.tab(2).println("%s.execute(%s);", localVar, instance ? "configuration()" : configurationArgument);

        if (outParams.size() > 0) {
            final ParameterDefinition parameter = outParams.get(0);

            // Avoid disambiguation for RETURN_VALUE getter
            final String getter = parameter == procedure.getReturnValue()
                ? "getReturnValue"
                : getStrategy().getJavaGetterName(parameter, Mode.DEFAULT);
            final boolean isUDT = parameter.getType(resolver()).isUDT();

            if (instance) {

                // [#3117] Avoid funny call-site ambiguity if this is a UDT that is implemented by an interface
                if (generateInterfaces() && isUDT) {
                    final String columnTypeInterface = out.ref(getJavaType(parameter.getType(resolver(Mode.INTERFACE)), Mode.INTERFACE));

                    if (scala)
                        out.tab(2).println("from(%s.%s.asInstanceOf[%s])", localVar, getter, columnTypeInterface);
                    else
                        out.tab(2).println("from((%s) %s.%s());", columnTypeInterface, localVar, getter);
                }
                else {
                    if (scala)
                        out.tab(2).println("from(%s.%s)", localVar, getter);
                    else
                        out.tab(2).println("from(%s.%s());", localVar, getter);
                }
            }

            if (outParams.size() == 1) {
                if (scala)
                    out.tab(2).println("return %s.%s", localVar, getter);
                else
                    out.tab(2).println("return %s.%s();", localVar, getter);
            }
            else if (outParams.size() > 1) {
                if (scala)
                    out.tab(2).println("return %s", localVar);
                else
                    out.tab(2).println("return %s;", localVar);
            }
        }

        out.tab(1).println("}");
    }

    protected void printConvenienceMethodTableValuedFunction(JavaWriter out, TableDefinition function, String javaMethodName) {
        // [#281] - Java can't handle more than 255 method parameters
        if (function.getParameters().size() > 254) {
            log.warn("Too many parameters", "Function " + function + " has more than 254 in parameters. Skipping generation of convenience method.");
            return;
        }

        final String recordClassName = out.ref(getStrategy().getFullJavaClassName(function, Mode.RECORD));

        // [#3456] Local variables should not collide with actual function arguments
        final String configurationArgument = disambiguateJavaMemberName(function.getParameters(), "configuration");

        if (!printDeprecationIfUnknownTypes(out, function.getParameters()))
            out.tab(1).javadoc("Call <code>%s</code>.", function.getQualifiedOutputName());

        if (scala)
            out.tab(1).print("def %s(%s : %s", javaMethodName, configurationArgument, Configuration.class);
        else
            out.tab(1).print("public static %s<%s> %s(%s %s", Result.class, recordClassName, javaMethodName, Configuration.class, configurationArgument);

        if (!function.getParameters().isEmpty())
            out.print(", ");

        printParameterDeclarations(out, function, false);

        if (scala) {
            out.println(") : %s[%s] = {", Result.class, recordClassName);
            out.tab(2).print("%s.dsl().selectFrom(%s.call(",
                configurationArgument,
                // [#5765] To prevent name clashes, this identifier is not imported
                getStrategy().getFullJavaIdentifier(function));
        }
        else {
            out.println(") {");
            out.tab(2).print("return %s.dsl().selectFrom(%s.call(",
                configurationArgument,
                // [#5765] To prevent name clashes, this identifier is not imported
                getStrategy().getFullJavaIdentifier(function));
        }

        String separator = "";
        for (ParameterDefinition parameter : function.getParameters()) {
            out.print(separator);
            out.print("%s", getStrategy().getJavaMemberName(parameter));

            separator = ", ";
        }

        out.print(")).fetch()");

        if (scala)
            out.println();
        else
            out.println(";");

        out.tab(1).println("}");
    }

    protected void printRecordTypeMethod(JavaWriter out, Definition definition) {
        final String className = out.ref(getStrategy().getFullJavaClassName(definition, Mode.RECORD));

        out.tab(1).javadoc("The class holding records for this type");

        if (scala) {
            out.tab(1).println("override def getRecordType : %s[%s] = {", Class.class, className);
            out.tab(2).println("classOf[%s]", className);
            out.tab(1).println("}");
        }
        else {
            out.tab(1).override();
            out.tab(1).println("public %s<%s> getRecordType() {", Class.class, className);
            out.tab(2).println("return %s.class;", className);
            out.tab(1).println("}");
        }
    }

    protected void printSingletonInstance(JavaWriter out, Definition definition) {
        final String className = getStrategy().getJavaClassName(definition);
        final String identifier = getStrategy().getJavaIdentifier(definition);

        out.tab(1).javadoc("The reference instance of <code>%s</code>", definition.getQualifiedOutputName());

        if (scala)
            out.tab(1).println("val %s = new %s", identifier, className);
        else
            out.tab(1).println("public static final %s %s = new %s();", className, identifier, className);
    }

    protected final String escapeEntities(String comment) {

        if (comment == null)
            return null;

        // [#5704] Do not allow certain HTML entities
        return comment
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;");
    }

    protected void printClassJavadoc(JavaWriter out, Definition definition) {
        printClassJavadoc(out, escapeEntities(definition.getComment()));
    }

    protected void printClassJavadoc(JavaWriter out, String comment) {
        if (generateJavadoc()) {
            out.println("/**");

            if (comment != null && comment.length() > 0) {
                printJavadocParagraph(out, comment, "");
            }
            else {
                out.println(" * This class is generated by jOOQ.");
            }

            out.println(" */");
        }
    }

    protected void printClassAnnotations(JavaWriter out, SchemaDefinition schema) {
        printClassAnnotations(out, schema, schema.getCatalog());
    }

    protected void printClassAnnotations(JavaWriter out, SchemaDefinition schema, CatalogDefinition catalog) {
        if (generateGeneratedAnnotation()) {

            // [#7581] The concrete annotation type depends on the JDK, with
            //         javax.annotation.Generated being deprecated in JDK 9
            GeneratedAnnotationType type = generateGeneratedAnnotationType();
            if (type == null)
                type = GeneratedAnnotationType.DETECT_FROM_JDK;

            String generated;
            switch (type) {
                case DETECT_FROM_JDK:
                    try {

                        // Seems more reliable than tampering with java.version
                        Reflect.on("java.util.Optional").call("of", new Object()).call("stream");
                        generated = "javax.annotation.processing.Generated";
                    }
                    catch (ReflectException e) {
                        generated = "javax.annotation.Generated";
                    }

                    break;
                case JAVAX_ANNOTATION_GENERATED:
                    generated = "javax.annotation.Generated";
                    break;
                case JAVAX_ANNOTATION_PROCESSING_GENERATED:
                    generated = "javax.annotation.processing.Generated";
                    break;
                default:
                    throw new IllegalStateException("Unsupported type: " + type);
            }

            out.println("@%s(", out.ref(generated));

            if (useSchemaVersionProvider() || useCatalogVersionProvider()) {
                boolean hasCatalogVersion = !StringUtils.isBlank(catalogVersions.get(catalog));
                boolean hasSchemaVersion = !StringUtils.isBlank(schemaVersions.get(schema));

                if (scala)
                    out.tab(1).println("value = %s(", out.ref("scala.Array"));
                else
                    out.tab(1).println("value = {");

                out.tab(2).println("\"http://www.jooq.org\",");
                out.tab(2).println("\"jOOQ version:%s\"%s", Constants.VERSION, (hasCatalogVersion || hasSchemaVersion ? "," : ""));

                if (hasCatalogVersion)
                    out.tab(2).println("\"catalog version:%s\"%s", catalogVersions.get(catalog).replace("\"", "\\\""), (hasSchemaVersion ? "," : ""));
                if (hasSchemaVersion)
                    out.tab(2).println("\"schema version:%s\"", schemaVersions.get(schema).replace("\"", "\\\""));

                if (scala)
                    out.tab(1).println("),");
                else
                    out.tab(1).println("},");

                out.tab(1).println("date = \"" + isoDate + "\",");
                out.tab(1).println("comments = \"This class is generated by jOOQ\"");
            }
            else {
                if (scala)
                    out.tab(1).println("value = %s(", out.ref("scala.Array"));
                else
                    out.tab(1).println("value = {");

                out.tab(2).println("\"http://www.jooq.org\",");
                out.tab(2).println("\"jOOQ version:%s\"", Constants.VERSION);

                if (scala)
                    out.tab(1).println("),");
                else
                    out.tab(1).println("},");

                out.tab(1).println("comments = \"This class is generated by jOOQ\"");
            }

            out.println(")");
        }

        if (!scala)
            out.println("@%s({ \"all\", \"unchecked\", \"rawtypes\" })", out.ref("java.lang.SuppressWarnings"));
    }

    private String readVersion(File file, String type) {
        String result = null;

        try {
            RandomAccessFile f = new RandomAccessFile(file, "r");

            try {
                byte[] bytes = new byte[(int) f.length()];
                f.readFully(bytes);
                String string = new String(bytes);

                Matcher matcher = Pattern.compile("@(?:javax\\.annotation\\.)?Generated\\(\\s*?value\\s*?=\\s*?" + (scala ? "Array\\([^)]*?" : "\\{[^}]*?") + "\"" + type + " version:([^\"]*?)\"").matcher(string);
                if (matcher.find()) {
                    result = matcher.group(1);
                }
            }
            finally {
                f.close();
            }
        }
        catch (IOException ignore) {}

        return result;
    }

    /**
     * This method is used to add line breaks in lengthy javadocs
     */
    protected void printJavadocParagraph(JavaWriter out, String comment, String indent) {

        // [#3450] [#4880] [#7693] Must not print */ inside Javadoc
        String escaped = JavaWriter.escapeJavadoc(comment);
        printParagraph(out, escaped, indent + " * ");
    }

    protected void printParagraph(GeneratorWriter<?> out, String comment, String indent) {
        boolean newLine = true;
        int lineLength = 0;

        for (int i = 0; i < comment.length(); i++) {
            if (newLine) {
                out.print(indent);

                newLine = false;
            }

            out.print(comment.charAt(i));
            lineLength++;

            if (comment.charAt(i) == '\n') {
                lineLength = 0;
                newLine = true;
            }
            else if (lineLength > 70 && Character.isWhitespace(comment.charAt(i))) {
                out.println();
                lineLength = 0;
                newLine = true;
            }
        }

        if (!newLine) {
            out.println();
        }
    }

    protected void printPackage(JavaWriter out, Definition definition) {
        printPackage(out, definition, Mode.DEFAULT);
    }

    protected void printPackage(JavaWriter out, Definition definition, Mode mode) {
        printPackageComment(out, definition, mode);

        if (scala)
            out.println("package %s", getStrategy().getJavaPackageName(definition, mode));
        else
            out.println("package %s;", getStrategy().getJavaPackageName(definition, mode));

        out.println();
        out.printImports();
        out.println();
    }

    protected void printPackageComment(JavaWriter out, Definition definition, Mode mode) {
        String header = getStrategy().getFileHeader(definition, mode);

        if (!StringUtils.isBlank(header)) {
            out.println("/*");
            printJavadocParagraph(out, header, "");
            out.println(" */");
        }
    }

    @Deprecated
    protected String getExtendsNumberType(DataTypeDefinition type) {
        return getNumberType(type, scala ? "_ <: " : "? extends ");
    }

    protected String refExtendsNumberType(JavaWriter out, DataTypeDefinition type) {
        if (type.isGenericNumberType()) {
            return (scala ? "_ <: " : "? extends ") + out.ref(Number.class);
        }
        else {
            return out.ref(getJavaType(type));
        }
    }

    @Deprecated
    protected String getNumberType(DataTypeDefinition type) {
        if (type.isGenericNumberType()) {
            return Number.class.getName();
        }
        else {
            return getJavaType(type);
        }
    }

    protected String refNumberType(JavaWriter out, DataTypeDefinition type) {
        if (type.isGenericNumberType()) {
            return out.ref(Number.class);
        }
        else {
            return out.ref(getJavaType(type));
        }
    }

    @Deprecated
    protected String getNumberType(DataTypeDefinition type, String prefix) {
        if (type.isGenericNumberType()) {
            return prefix + Number.class.getName();
        }
        else {
            return getJavaType(type);
        }
    }

    @Deprecated
    protected String getSimpleJavaType(DataTypeDefinition type) {
        return GenerationUtil.getSimpleJavaType(getJavaType(type));
    }

    protected String getJavaTypeReference(Database db, DataTypeDefinition type) {

        // [#4388] TODO: Improve array handling
        if (database.isArrayType(type.getType())) {
            Name baseType = GenerationUtil.getArrayBaseType(db.getDialect(), type.getType(), type.getQualifiedUserType());
            return getTypeReference(
                db,
                type.getSchema(),
                baseType.last(),
                type.getPrecision(),
                type.getScale(),
                type.getLength(),
                true,
                false,
                null,
                baseType
            ) + ".getArrayDataType()";
        }
        else {
            return getTypeReference(
                db,
                type.getSchema(),
                type.getType(),
                type.getPrecision(),
                type.getScale(),
                type.getLength(),
                type.isNullable(),
                type.isIdentity(),
                type.getDefaultValue(),
                type.getQualifiedUserType()
            );
        }
    }

    protected JavaTypeResolver resolver() {
        return new JavaTypeResolver() {
            @Override
            public String resolve(DataTypeDefinition type) {
                return getJavaType(type);
            }
        };
    }

    protected JavaTypeResolver resolver(final Mode udtMode) {
        return new JavaTypeResolver() {
            @Override
            public String resolve(DataTypeDefinition type) {
                return getJavaType(type, udtMode);
            }
        };
    }

    protected String getJavaType(DataTypeDefinition type) {
        return getJavaType(type, Mode.RECORD);
    }

    protected String getJavaType(DataTypeDefinition type, Mode udtMode) {
        return getType(
            type.getDatabase(),
            type.getSchema(),
            type.getType(),
            type.getPrecision(),
            type.getScale(),
            type.getQualifiedUserType(),
            type.getJavaType(),
            Object.class.getName(),
            udtMode);
    }

    /**
     * @deprecated - 3.9.0 - [#330]  - Use {@link #getType(Database, SchemaDefinition, String, int, int, Name, String, String)} instead.
     */
    @Deprecated
    protected String getType(Database db, SchemaDefinition schema, String t, int p, int s, String u, String javaType, String defaultType) {
        return getType(db, schema, t, p, s, name(u), javaType, defaultType);
    }

    protected String getType(Database db, SchemaDefinition schema, String t, int p, int s, Name u, String javaType, String defaultType) {
        return getType(db, schema, t, p, s, u, javaType, defaultType, Mode.RECORD);
    }

    /**
     * @deprecated - 3.9.0 - [#330]  - Use {@link #getType(Database, SchemaDefinition, String, int, int, Name, String, String, Mode)} instead.
     */
    @Deprecated
    protected String getType(Database db, SchemaDefinition schema, String t, int p, int s, String u, String javaType, String defaultType, Mode udtMode) {
        return getType(db, schema, t, p, s, name(u), javaType, defaultType, udtMode);
    }

    protected String getType(Database db, SchemaDefinition schema, String t, int p, int s, Name u, String javaType, String defaultType, Mode udtMode) {
        String type = defaultType;

        // Custom types
        if (javaType != null) {
            type = javaType;
        }

        // Array types
        else if (db.isArrayType(t)) {

            // [#4388] TODO: Improve array handling
            Name baseType = GenerationUtil.getArrayBaseType(db.getDialect(), t, u);

            if (scala)
                type = "scala.Array[" + getType(db, schema, baseType.last(), p, s, baseType, javaType, defaultType, udtMode) + "]";
            else
                type = getType(db, schema, baseType.last(), p, s, baseType, javaType, defaultType, udtMode) + "[]";
        }

        // Check for Oracle-style VARRAY types
        else if (db.getArray(schema, u) != null) {
            boolean udtArray = db.getArray(schema, u).getElementType(resolver()).isUDT();

            if (udtMode == Mode.POJO || (udtMode == Mode.INTERFACE && !udtArray)) {
                if (scala)
                    type = "java.util.List[" + getJavaType(db.getArray(schema, u).getElementType(resolver(udtMode)), udtMode) + "]";
                else
                    type = "java.util.List<" + getJavaType(db.getArray(schema, u).getElementType(resolver(udtMode)), udtMode) + ">";
            }
            else if (udtMode == Mode.INTERFACE) {
                if (scala)
                    type = "java.util.List[_ <:" + getJavaType(db.getArray(schema, u).getElementType(resolver(udtMode)), udtMode) + "]";
                else
                    type = "java.util.List<? extends " + getJavaType(db.getArray(schema, u).getElementType(resolver(udtMode)), udtMode) + ">";
            }
            else {
                type = getStrategy().getFullJavaClassName(db.getArray(schema, u), Mode.RECORD);
            }
        }

        // Check for ENUM types
        else if (db.getEnum(schema, u) != null) {
            type = getStrategy().getFullJavaClassName(db.getEnum(schema, u));
        }

        // Check for UDTs
        else if (db.getUDT(schema, u) != null) {
            type = getStrategy().getFullJavaClassName(db.getUDT(schema, u), udtMode);
        }

        // [#3942] PostgreSQL treats UDTs and table types in similar ways
        // [#5334] In MySQL, the user type is (ab)used for synthetic enum types. This can lead to accidental matches here
        else if (db.getDialect().family() == POSTGRES && db.getTable(schema, u) != null) {
            type = getStrategy().getFullJavaClassName(db.getTable(schema, u), udtMode);
        }

        // Check for custom types
        else if (u != null && db.getConfiguredCustomType(u.last()) != null) {
            type = u.last();
        }

        // Try finding a basic standard SQL type according to the current dialect
        else {
            try {
                Class<?> clazz = mapJavaTimeTypes(getDataType(db, t, p, s)).getType();
                if (scala && clazz == byte[].class)
                    type = "scala.Array[scala.Byte]";
                else
                    type = clazz.getCanonicalName();

                if (clazz.getTypeParameters().length > 0) {
                    type += (scala ? "[" : "<");

                    String separator = "";
                    for (TypeVariable<?> var : clazz.getTypeParameters()) {
                        type += separator;
                        type += ((Class<?>) var.getBounds()[0]).getCanonicalName();

                        separator = ", ";
                    }

                    type += (scala ? "]" : ">");
                }
            }
            catch (SQLDialectNotSupportedException e) {
                if (defaultType == null) {
                    throw e;
                }
            }
        }

        return type;
    }

    protected String getTypeReference(Database db, SchemaDefinition schema, String t, int p, int s, int l, boolean n, boolean i, String d, Name u) {
        StringBuilder sb = new StringBuilder();
        if (db.getArray(schema, u) != null) {
            ArrayDefinition array = database.getArray(schema, u);

            sb.append(getJavaTypeReference(db, array.getElementType(resolver())));
            sb.append(".asArrayDataType(");
            sb.append(classOf(getStrategy().getFullJavaClassName(array, Mode.RECORD)));
            sb.append(")");
        }
        else if (db.getUDT(schema, u) != null) {
            sb.append(getStrategy().getFullJavaIdentifier(db.getUDT(schema, u)));
            sb.append(".getDataType()");
        }
        // [#3942] PostgreSQL treats UDTs and table types in similar ways
        // [#5334] In MySQL, the user type is (ab)used for synthetic enum types. This can lead to accidental matches here
        else if (db.getDialect().family() == POSTGRES && db.getTable(schema, u) != null) {
            sb.append(getStrategy().getFullJavaIdentifier(db.getTable(schema, u)));
            sb.append(".getDataType()");
        }
        else if (db.getEnum(schema, u) != null) {
            sb.append(getJavaTypeReference(db, new DefaultDataTypeDefinition(
                db, schema, DefaultDataType.getDataType(db.getDialect(), String.class).getTypeName(), l, p, s, n, d, (Name) null
            )));
            sb.append(".asEnumDataType(");
            sb.append(classOf(getStrategy().getFullJavaClassName(db.getEnum(schema, u))));
            sb.append(")");
        }
        else {
            DataType<?> dataType;
            String sqlDataTypeRef;

            try {
                dataType = mapJavaTimeTypes(getDataType(db, t, p, s));
            }

            // Mostly because of unsupported data types.
            catch (SQLDialectNotSupportedException ignore) {
                dataType = SQLDataType.OTHER.nullable(n).identity(i);

                sb = new StringBuilder();

                sb.append(DefaultDataType.class.getName());
                sb.append(".getDefaultDataType(\"");
                sb.append((u != null ? u.toString() : t).replace("\"", "\\\""));
                sb.append("\")");
            }

            dataType = dataType
                .nullable(n)
                .identity(i);

            if (d != null)
                dataType = dataType.defaultValue((Field) DSL.field(d, dataType));

            // If there is a standard SQLDataType available for the dialect-
            // specific DataType t, then reference that one.
            if (dataType.getSQLDataType() != null && sb.length() == 0) {
                DataType<?> sqlDataType = dataType.getSQLDataType();
                String literal = SQLDATATYPE_LITERAL_LOOKUP.get(sqlDataType);
                sqlDataTypeRef =
                    SQLDataType.class.getCanonicalName()
                  + '.'
                  + literal;

                sb.append(sqlDataTypeRef);

                if (dataType.hasPrecision() && p > 0) {

                    // [#6411] Call static method if available, rather than instance method
                    if (SQLDATATYPE_WITH_PRECISION.contains(literal))
                        sb.append('(').append(p);
                    else
                        sb.append(".precision(").append(p);

                    if (dataType.hasScale() && s > 0)
                        sb.append(", ").append(s);

                    sb.append(')');
                }

                if (dataType.hasLength() && l > 0)

                    // [#6411] Call static method if available, rather than instance method
                    if (SQLDATATYPE_WITH_LENGTH.contains(literal))
                        sb.append("(").append(l).append(")");
                    else
                        sb.append(".length(").append(l).append(")");
            }
            else {
                sqlDataTypeRef = SQLDataType.class.getCanonicalName() + ".OTHER";

                if (sb.length() == 0)
                    sb.append(sqlDataTypeRef);
            }

            if (!dataType.nullable())
                sb.append(".nullable(false)");

            if (dataType.identity())
                sb.append(".identity(true)");

            // [#5291] Some dialects report valid SQL expresions (e.g. PostgreSQL), others
            //         report actual values (e.g. MySQL).
            if (dataType.defaulted()) {
                sb.append(".defaultValue(");

                if (asList(MYSQL).contains(db.getDialect().family()))

                    // [#5574] While MySQL usually reports actual values, it does report
                    //         a CURRENT_TIMESTAMP expression, inconsistently
                    if (d != null && d.toLowerCase().startsWith("current_timestamp"))
                        sb.append("org.jooq.impl.DSL.field(\"")
                          .append(escapeString(d))
                          .append("\"");
                    else
                        sb.append("org.jooq.impl.DSL.inline(\"")
                          .append(escapeString(d))
                          .append("\"");
                else
                    sb.append("org.jooq.impl.DSL.field(\"")
                      .append(escapeString(d))
                      .append("\"");

                sb.append(", ")
                  .append(sqlDataTypeRef)
                  .append("))");
            }
        }

        return sb.toString();
    }

    private DataType<?> mapJavaTimeTypes(DataType<?> dataType) {
        DataType<?> result = dataType;


        // [#4429] [#5713] This logic should be implemented in Configuration
        if (dataType.isDateTime() && generateJavaTimeTypes) {
            if (dataType.getType() == Date.class)
                result = SQLDataType.LOCALDATE;
            else if (dataType.getType() == Time.class)
                result = SQLDataType.LOCALTIME;
            else if (dataType.getType() == Timestamp.class)
                result = SQLDataType.LOCALDATETIME;
        }


        return result;
    }

    @Deprecated
    protected boolean match(DataTypeDefinition type1, DataTypeDefinition type2) {
        return getJavaType(type1).equals(getJavaType(type2));
    }


    @SafeVarargs

    private static final <T> List<T> list(T... objects) {
        List<T> result = new ArrayList<>();

        if (objects != null)
            for (T object : objects)
                if (object != null && !"".equals(object))
                    result.add(object);

        return result;
    }

    private static final <T> List<T> list(T first, List<T> remaining) {
        List<T> result = new ArrayList<>();

        result.addAll(list(first));
        result.addAll(remaining);

        return result;
    }

    private static final <T> List<T> first(Collection<T> objects) {
        List<T> result = new ArrayList<>();

        if (objects != null) {
            for (T object : objects) {
                result.add(object);
                break;
            }
        }

        return result;
    }

    private static final <T> List<T> remaining(Collection<T> objects) {
        List<T> result = new ArrayList<>();

        if (objects != null) {
            result.addAll(objects);

            if (result.size() > 0)
                result.remove(0);
        }

        return result;
    }

    private String classOf(String string) {
        if (scala)
            return "classOf[" + string + "]";
        else
            return string + ".class";
    }

    private static final Pattern SQUARE_BRACKETS = Pattern.compile("\\[\\]$");

    private String varargsIfArray(String type) {
        if (!generateVarargsSetters())
            return type;
        else if (scala)
            return type;
        else
            return SQUARE_BRACKETS.matcher(type).replaceFirst("...");
    }

    // [#3880] Users may need to call this method
    protected JavaWriter newJavaWriter(File file) {
        file = fixSuffix(file);
        JavaWriter result = new JavaWriter(file, generateFullyQualifiedTypes(), targetEncoding, generateJavadoc(), fileCache);

        if (generateIndentation != null)
            result.tabString(generateIndentation);
        if (generateNewline != null)
            result.newlineString(generateNewline);

        return result;
    }

    protected File getFile(Definition definition) {
        return fixSuffix(getStrategy().getFile(definition));
    }

    protected File getFile(Definition definition, Mode mode) {
        return fixSuffix(getStrategy().getFile(definition, mode));
    }

    private File fixSuffix(File file) {
        if (scala)
            file = new File(file.getParentFile(), file.getName().replace(".java", ".scala"));

        return file;
    }

    // [#4626] Users may need to call this method
    protected void closeJavaWriter(JavaWriter out) {
        if (out.close())
            files.add(out.file());
    }
}
