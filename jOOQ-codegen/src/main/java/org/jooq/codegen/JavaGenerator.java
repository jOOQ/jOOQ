/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * For more information, please visit: https://www.jooq.org/legal/licensing
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
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.rangeClosed;
// ...
// ...
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.YUGABYTEDB;
import static org.jooq.SortOrder.DESC;
import static org.jooq.codegen.Language.JAVA;
import static org.jooq.codegen.Language.KOTLIN;
import static org.jooq.codegen.Language.SCALA;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.QOM.GenerationOption.STORED;
import static org.jooq.impl.QOM.GenerationOption.VIRTUAL;
import static org.jooq.meta.AbstractTypedElementDefinition.getDataType;
import static org.jooq.tools.StringUtils.isBlank;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Modifier;
import java.lang.reflect.TypeVariable;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jooq.AggregateFunction;
import org.jooq.Catalog;
import org.jooq.Check;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Constants;
import org.jooq.DSLContext;
import org.jooq.DataType;
import org.jooq.Domain;
import org.jooq.EnumType;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Generated;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.InverseForeignKey;
// ...
import org.jooq.Name;
import org.jooq.OrderField;
import org.jooq.Param;
import org.jooq.Parameter;
import org.jooq.Parser;
import org.jooq.Path;
import org.jooq.PlainSQL;
// ...
import org.jooq.Query;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Result;
import org.jooq.Row;
import org.jooq.RowId;
import org.jooq.SQL;
import org.jooq.SQLDialect;
import org.jooq.Schema;
import org.jooq.Select;
import org.jooq.SelectField;
import org.jooq.Sequence;
import org.jooq.SortOrder;
import org.jooq.Stringly;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
// ...
// ...
// ...
// ...
import org.jooq.UDT;
import org.jooq.UDTField;
import org.jooq.UniqueKey;
import org.jooq.UpdatableRecord;
import org.jooq.codegen.GenerationUtil.BaseType;
import org.jooq.codegen.GeneratorStrategy.Mode;
import org.jooq.codegen.GeneratorWriter.CloseResult;
import org.jooq.conf.ParseSearchSchema;
import org.jooq.conf.ParseWithMetaLookups;
import org.jooq.exception.SQLDialectNotSupportedException;
// ...
// ...
import org.jooq.impl.DAOImpl;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultDataType;
import org.jooq.impl.Internal;
import org.jooq.impl.LazySchema;
import org.jooq.impl.LazySupplier;
import org.jooq.impl.PackageImpl;
import org.jooq.impl.QOM.GenerationOption;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.SchemaImpl;
import org.jooq.impl.TableImpl;
import org.jooq.impl.TableRecordImpl;
import org.jooq.impl.UDTImpl;
import org.jooq.impl.UpdatableRecordImpl;
import org.jooq.meta.AbstractTypedElementDefinition;
import org.jooq.meta.ArrayDefinition;
import org.jooq.meta.AttributeDefinition;
import org.jooq.meta.CatalogDefinition;
import org.jooq.meta.CheckConstraintDefinition;
import org.jooq.meta.ColumnDefinition;
import org.jooq.meta.ConstraintDefinition;
import org.jooq.meta.DataTypeDefinition;
import org.jooq.meta.Database;
import org.jooq.meta.DefaultDataTypeDefinition;
import org.jooq.meta.DefaultMetaTableDefinition;
import org.jooq.meta.DefaultSyntheticDaoDefinition;
import org.jooq.meta.Definition;
import org.jooq.meta.DomainDefinition;
import org.jooq.meta.EmbeddableColumnDefinition;
import org.jooq.meta.EmbeddableDefinition;
import org.jooq.meta.EnumDefinition;
import org.jooq.meta.ForeignKeyDefinition;
import org.jooq.meta.IdentityDefinition;
import org.jooq.meta.IndexColumnDefinition;
import org.jooq.meta.IndexDefinition;
import org.jooq.meta.InverseForeignKeyDefinition;
import org.jooq.meta.JavaTypeResolver;
import org.jooq.meta.ManyToManyKeyDefinition;
import org.jooq.meta.PackageDefinition;
import org.jooq.meta.ParameterDefinition;
import org.jooq.meta.RoutineDefinition;
import org.jooq.meta.SchemaDefinition;
import org.jooq.meta.SequenceDefinition;
import org.jooq.meta.SyntheticDaoDefinition;
import org.jooq.meta.TableDefinition;
// ...
import org.jooq.meta.TypedElementDefinition;
import org.jooq.meta.UDTDefinition;
import org.jooq.meta.UniqueKeyDefinition;
import org.jooq.meta.XMLTypeDefinition;
import org.jooq.meta.jaxb.ForcedType;
import org.jooq.meta.jaxb.GeneratedAnnotationType;
import org.jooq.meta.jaxb.GeneratedTextBlocks;
import org.jooq.meta.jaxb.SyntheticDaoMethodType;
import org.jooq.meta.jaxb.SyntheticDaoType;
import org.jooq.meta.jaxb.VisibilityModifier;
// ...
// ...
// ...
// ...
import org.jooq.meta.postgres.PostgresRoutineDefinition;
// ...
import org.jooq.tools.JooqLogger;
import org.jooq.tools.StopWatch;
import org.jooq.tools.StringUtils;
import org.jooq.tools.reflect.Reflect;
import org.jooq.tools.reflect.ReflectException;
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
     * Dialects that can reference tables in contexts where UDTs are expected.
     */
    private static final Set<SQLDialect>          SUPPORT_TABLE_AS_UDT         = SQLDialect.supportedBy(POSTGRES, YUGABYTEDB);

    /**
     * The Javadoc to be used for private constructors
     */
    private static final String                   NO_FURTHER_INSTANCES_ALLOWED = "No further instances allowed";

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
     * Some reusable type references.
     */
    private static final String                   KLIST                        = "kotlin.collections.List";
    private static final String                   KMUTABLELIST                 = "kotlin.collections.MutableList";

    private static final Set<String>              PRIMITIVE_WRAPPERS           = new HashSet<>(Arrays.asList(
        Byte.class.getName(),
        Short.class.getName(),
        Integer.class.getName(),
        Long.class.getName(),
        Float.class.getName(),
        Double.class.getName(),
        Boolean.class.getName(),
        Character.class.getName(),
        "kotlin.Byte",
        "kotlin.Short",
        "kotlin.Int",
        "kotlin.Long",
        "kotlin.Float",
        "kotlin.Double",
        "kotlin.Boolean",
        "kotlin.Char"
    ));

    /**
     * An overall stop watch to measure the speed of source code generation
     */
    private final StopWatch                       watch                        = new StopWatch();

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
     * All files affected by this generator run.
     */
    private Set<File>                             affectedFiles                = new LinkedHashSet<>();

    /**
     * All files modified by this generator run.
     */
    private Set<File>                             modifiedFiles                = new LinkedHashSet<>();

    /**
     * These directories were not modified by this generator, but flagged as not
     * for removal (e.g. because of {@link #schemaVersions} or
     * {@link #catalogVersions}).
     */
    private Set<File>                             directoriesNotForRemoval     = new LinkedHashSet<>();

    private boolean                               scala;
    private final boolean                         scalaConfigured;
    private boolean                               kotlin;
    private final boolean                         kotlinConfigured;
    private final String                          semicolon;
    private final String                          emptyparens;
    private final String                          tokenVoid;
    private final Files                           fileCache;
    private String                                visibility;

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
                    ((DataType<?>) SQLDataType.class.getField(m.getName()).get(SQLDataType.class)).hasPrecision())
                    SQLDATATYPE_WITH_PRECISION.add(m.getName());
            }

            for (java.lang.reflect.Method m : SQLDataType.class.getMethods()) {
                if (Modifier.isPublic(m.getModifiers()) &&
                    Modifier.isStatic(m.getModifiers()) &&
                    ((DataType<?>) SQLDataType.class.getField(m.getName()).get(SQLDataType.class)).hasLength() &&
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

        this.scalaConfigured = this.scala = (language == SCALA);
        this.kotlinConfigured = this.kotlin = (language == KOTLIN);
        this.tokenVoid = (scala || kotlin ? "Unit" : "void");
        this.semicolon = (scala || kotlin ? "" : ";");
        this.emptyparens = (scala ? "" : "()");
        this.fileCache = new Files();
    }

    private String visibilityPublic() {
        if (scala)
            return "";

        // [#14855] Make sure visibility isn't reduced to anything less than "public"
        // [#14883] Don't generate explicit "public" unless explicitly requested
        else if (kotlin)
            return visibility(
                generateVisibilityModifier() == VisibilityModifier.PUBLIC
                ? VisibilityModifier.PUBLIC
                : VisibilityModifier.DEFAULT
            );
        else
            return "public ";
    }

    private String visibility(boolean effectivelyPublic) {
        return effectivelyPublic ? visibilityPublic() : visibility();
    }

    private String visibility() {
        if (visibility == null)
            visibility = visibility(generateVisibilityModifier());

        return visibility;
    }

    private String visibility(VisibilityModifier modifier) {
        switch (modifier) {
            case NONE:
                return "";
            case PUBLIC:
                return scala ? "" : "public ";
            case INTERNAL:
                return scala ? "" : kotlin ? "internal " : "public ";
            case PRIVATE:
                return "private ";
            case DEFAULT:
            default:
                return scala || kotlin ? "" : "public ";
        }
    }

    private boolean visible(VisibilityModifier modifier) {
        switch (modifier) {
            case NONE:
            case PRIVATE:
                return false;
            case PUBLIC:
            case INTERNAL:
            case DEFAULT:
            default:
                return true;
        }
    }

    @Override
    public final void generate0(Database db) {
        this.isoDate = Instant.now().toString();
        this.schemaVersions = new LinkedHashMap<>();
        this.catalogVersions = new LinkedHashMap<>();
        this.database.addFilter(new AvoidAmbiguousClassesFilter());

        logDatabaseParameters(db);
        log.info("");
        log.info("JavaGenerator parameters");
        log.info("----------------------------------------------------------");
        log.info("annotations");
        log.info("  generated", generateGeneratedAnnotation()
            + ((!generateGeneratedAnnotation && (useSchemaVersionProvider || useCatalogVersionProvider)) ?
                " (forced to true because of <schemaVersionProvider/> or <catalogVersionProvider/>)" : ""));
        log.info("  JPA", generateJPAAnnotations());
        log.info("  JPA version", generateJPAVersion());
        log.info("  validation", generateValidationAnnotations());
        log.info("  spring", generateSpringAnnotations());
        log.info("comments");
        log.info("  comments", generateComments());
        log.info("  on attributes", generateCommentsOnAttributes());
        log.info("  on catalogs", generateCommentsOnCatalogs());
        log.info("  on columns", generateCommentsOnColumns());
        log.info("  on embeddables", generateCommentsOnEmbeddables());
        log.info("  on keys", generateCommentsOnKeys());
        log.info("  on links", generateCommentsOnLinks());
        log.info("  on packages", generateCommentsOnPackages());
        log.info("  on parameters", generateCommentsOnParameters());
        log.info("  on queues", generateCommentsOnQueues());
        log.info("  on routines", generateCommentsOnRoutines());
        log.info("  on schemas", generateCommentsOnSchemas());
        log.info("  on sequences", generateCommentsOnSequences());
        log.info("  on tables", generateCommentsOnTables());
        log.info("  on udts", generateCommentsOnUDTs());
        log.info("sources");
        log.info("  sources", generateSources());
        log.info("  sources on views", generateSourcesOnViews());
        log.info("global references");
        log.info("  global references", generateGlobalObjectReferences());
        log.info("  catalogs", generateGlobalCatalogReferences());
        log.info("  domains", generateGlobalDomainReferences());
        log.info("  indexes", generateGlobalIndexReferences());
        log.info("  keys", generateGlobalKeyReferences());
        log.info("  links", generateGlobalLinkReferences());
        log.info("  queues", generateGlobalQueueReferences());
        log.info("  routines", generateGlobalRoutineReferences());
        log.info("  schemas", generateGlobalSchemaReferences());
        log.info("  sequences", generateGlobalSequenceReferences());
        log.info("  tables", generateGlobalTableReferences());
        log.info("  udts", generateGlobalUDTReferences());
        log.info("object types");
        log.info("  daos", generateDaos());
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
                ((!generateTables && generateDaos) ? " (forced to true because of <daos/>)" :
                ((!generateTables && generateIndexes) ? " (forced to true because of <indexes/>)" : ""))));



        log.info("  udts", generateUDTs());
        log.info("  relations", generateRelations()
            + ((!generateRelations && generateTables) ? " (forced to true because of <tables/>)" :
              ((!generateRelations && generateDaos) ? " (forced to true because of <daos/>)" : "")));
        log.info("other");
        log.info("  deprecated code", generateDeprecated());
        log.info("----------------------------------------------------------");

        if (!generateInstanceFields()) {
            log.warn("");
            log.warn("Deprecation warnings");
            log.warn("----------------------------------------------------------");
            log.warn("  <generateInstanceFields/> = false is deprecated! This feature is no longer maintained and will be removed in jOOQ 4.0. Please adapt your configuration.");
        }

        log.info("");
        logGenerationRemarks(db);

        log.info("");
        log.info("----------------------------------------------------------");

        // ----------------------------------------------------------------------
        // XXX Generating catalogs
        // ----------------------------------------------------------------------
        log.info("Generating catalogs", "Total: " + database.getCatalogs().size());

        StopWatch w = new StopWatch();
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

        long time = w.split();

        // [#10648] Log modified files
        log.info("Affected files: " + affectedFiles.size());
        log.info("Modified files: " + modifiedFiles.size());

        // [#10648] Don't log this info if we're already using schema version providers
        if (modifiedFiles.isEmpty() && catalogVersions.isEmpty() && schemaVersions.isEmpty()) {
            log.info(
                "No modified files",
                "This code generation run has not produced any file modifications.\n"
              + "This means, the schema has not changed, and no other parameters (jOOQ version, driver version, database version,\n"
              + "and any configuration elements) have changed either.\n\n"
              + "In automated builds, it is recommended to prevent unnecessary code generation runs. This run took: " + StopWatch.format(time) + "\n"
              + "Possible means to prevent this:\n"
              + "- Use manual code generation and check in generated sources: https://www.jooq.org/doc/latest/manual/code-generation/codegen-version-control/\n"
              + "- Use schema version providers: https://www.jooq.org/doc/latest/manual/code-generation/codegen-advanced/codegen-config-database/codegen-database-version-providers/\n"
              + "- Use gradle tasks and inputs: https://github.com/etiennestuder/gradle-jooq-plugin/blob/master/README.md");
        }

        // [#5556] Clean up common parent directory
        log.info("Removing excess files");
        empty(getStrategy().getFileRoot(), (scala ? ".scala" : kotlin ? ".kt" : ".java"), affectedFiles, directoriesNotForRemoval);
        directoriesNotForRemoval.clear();
        affectedFiles.clear();
    }

    private boolean generateCatalogIfEmpty(CatalogDefinition catalog) {
        return generateEmptyCatalogs() || catalog.getSchemata().stream().anyMatch(this::generateSchemaIfEmpty);
    }

    private final boolean generateSchemaIfEmpty(SchemaDefinition schema) {
        return generateEmptySchemas()
               || !database.getArrays(schema).isEmpty()
               || !database.getDomains(schema).isEmpty()



               || !database.getEmbeddables(schema).isEmpty()
               || !database.getEnums(schema).isEmpty()
               || !database.getPackages(schema).isEmpty()
               || !database.getRoutines(schema).isEmpty()
               || !database.getSequences(schema).isEmpty()
               || !database.getTables(schema).isEmpty()
               || !database.getUDTs(schema).isEmpty();
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

        if (generateDefaultCatalog(catalog))
            generateCatalog(catalog);

        if (generateSpringDao() && catalog.getSchemata().stream().anyMatch(s -> !s.getTables().isEmpty()))
            generateSpringDao(catalog);

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
        if (generateDefaultSchema(schema))
            generateSchema(schema);

        if (generateGlobalSequenceReferences() && database.getSequences(schema).size() > 0)
            generateSequences(schema);

        if (generateTables() && database.getTables(schema).size() > 0)
            generateTables(schema);

        if (generateEmbeddables() && database.getEmbeddables(schema).size() > 0)
            generateEmbeddables(schema);

        if (generateEmbeddables() && generateInterfaces() && database.getEmbeddables(schema).size() > 0)
            generateEmbeddableInterfaces(schema);

        if (generateEmbeddables() && generatePojos() && database.getEmbeddables(schema).size() > 0)
            generateEmbeddablePojos(schema);

        if (generatePojos() && database.getTables(schema).size() > 0)
            generatePojos(schema);

        if (database.getConfiguredSyntheticDaos().size() > 0)
            generateSyntheticDaos(schema);

        if (generateDaos() && database.getTables(schema).size() > 0)
            generateDaos(schema);

        if (generateGlobalTableReferences() && database.getTables(schema).size() > 0)
            generateTableReferences(schema);

        if (generateGlobalKeyReferences() && generateRelations() && database.getTables(schema).size() > 0)
            generateRelations(schema);

        if (generateGlobalIndexReferences() && database.getTables(schema).size() > 0)
            generateIndexes(schema);

        if (generateRecords() && database.getTables(schema).size() > 0)
            generateRecords(schema);

        if (generateInterfaces() && database.getTables(schema).size() > 0)
            generateInterfaces(schema);

        if (generateUDTs() && database.getUDTs(schema).size() > 0)
            generateUDTs(schema);

        if (generateUDTs() && generatePojos() && database.getUDTs(schema).size() > 0)
            generateUDTPojos(schema);

        if (generateUDTs() && database.getUDTs(schema).size() > 0)
            generateUDTRecords(schema);

        if (generateUDTs() && generateInterfaces() && database.getUDTs(schema).size() > 0)
            generateUDTInterfaces(schema);

        if (generateUDTs() && generateRoutines() && database.getUDTs(schema).size() > 0)
            generateUDTRoutines(schema);

        if (generateUDTs() && generateGlobalUDTReferences() && database.getUDTs(schema).size() > 0)
            generateUDTReferences(schema);

        if (generateUDTs() && database.getArrays(schema).size() > 0)
            generateArrays(schema);

        if (generateUDTs() && database.getEnums(schema).size() > 0)
            generateEnums(schema);

        if (generateUDTs() && database.getDomains(schema).size() > 0)
            generateDomainReferences(schema);






        if (generateRoutines() && (database.getRoutines(schema).size() > 0 || hasTableValuedFunctions(schema)))
            generateRoutines(schema);











        // XXX [#651] Refactoring-cursor
        watch.splitInfo("Generation finished: " + schema.getQualifiedName());
        log.info("");
    }

    private static final record Included(String name, Definition definition) {}

    private class AvoidAmbiguousClassesFilter implements Database.Filter {

        private Map<String, Included> included = new HashMap<>();

        @Override
        public boolean exclude(Definition definition) {

            // These definitions don't generate types of their own.
            if (    definition instanceof ColumnDefinition
                 || definition instanceof AttributeDefinition
                 || definition instanceof ParameterDefinition)
                return false;

            // Check if we've previously encountered a Java type of the same case-insensitive, fully-qualified name.
            String name = getStrategy().getFullJavaClassName(definition);
            String nameLC = name.toLowerCase(getStrategy().getTargetLocale());
            Included existing = included.put(nameLC, new Included(name, definition));

            if (existing == null)
                return false;

            log.warn("Ambiguous type name",
                "The database object " + definition.getQualifiedOutputName()
              + " generates a class " + name + " (" + definition.getClass() + ")"
              + " which conflicts with the previously generated class " + existing.name() + " (" + existing.definition().getClass() + ")."
              + " Use a custom generator strategy to disambiguate the types. More information here:\n"
              + " - https://www.jooq.org/doc/latest/manual/code-generation/codegen-generatorstrategy/\n"
              + " - https://www.jooq.org/doc/latest/manual/code-generation/codegen-matcherstrategy/"
            );

            return true;
        }
    }
































































































































    private boolean hasTableValuedFunctions(SchemaDefinition schema) {
        return database.getTables(schema).stream().anyMatch(TableDefinition::isTableValuedFunction);
    }

    protected void generateRelations(SchemaDefinition schema) {
        log.info("Generating Keys");

        boolean empty = true;
        JavaWriter out = newJavaWriter(getStrategy().getGlobalReferencesFile(schema, ConstraintDefinition.class));
        out.refConflicts(getStrategy().getJavaIdentifiers(database.getKeys(schema)));
        out.refConflicts(getStrategy().getJavaIdentifiers(database.getForeignKeys(schema)));

        printGlobalReferencesPackage(out, schema, ConstraintDefinition.class);

        if (!kotlin) {
            printClassJavadoc(out, "A class modelling foreign key relationships and constraints of tables in " + schemaNameOrDefault(schema) + ".");
            printClassAnnotations(out, schema, Mode.DEFAULT);
        }

        final String referencesClassName = getStrategy().getGlobalReferencesJavaClassName(schema, ConstraintDefinition.class);

        if (scala)
            out.println("%sobject %s {", visibility(), referencesClassName);
        else if (kotlin) {}
        else
            out.println("%sclass %s {", visibility(), referencesClassName);

        // [#1459] [#10554] [#10653] Distribute keys to nested classes only if necessary
        boolean distributeUniqueKeys = database.getKeys(schema).size() > maxMembersPerInitialiser();
        boolean distributeForeignKeys = database.getForeignKeys(schema).size() > maxMembersPerInitialiser();

        List<UniqueKeyDefinition> allUniqueKeys = new ArrayList<>();
        List<ForeignKeyDefinition> allForeignKeys = new ArrayList<>();

        // Unique keys
        try {
            for (UniqueKeyDefinition uniqueKey : database.getKeys(schema)) {
                empty = false;

                final String keyType = out.ref(getStrategy().getFullJavaClassName(uniqueKey.getTable(), Mode.RECORD));
                final String keyId = getStrategy().getJavaIdentifier(uniqueKey);
                final int block = allUniqueKeys.size() / maxMembersPerInitialiser();

                // [#10480] Print header before first key
                if (allUniqueKeys.isEmpty()) {
                    out.header("UNIQUE and PRIMARY KEY definitions");
                    out.println();
                }

                if (distributeUniqueKeys)
                    if (scala)
                        out.println("%sval %s = UniqueKeys%s.%s", visibility(), keyId, block, keyId);
                    else if (kotlin)
                        out.println("%sval %s: %s<%s> = UniqueKeys%s.%s", visibility(), keyId, UniqueKey.class, keyType, block, keyId);
                    else
                        out.println("%sstatic final %s<%s> %s = UniqueKeys%s.%s;", visibility(), UniqueKey.class, keyType, keyId, block, keyId);
                else
                    printUniqueKey(out, -1, uniqueKey, distributeUniqueKeys);

                allUniqueKeys.add(uniqueKey);
            }
        }
        catch (Exception e) {
            log.error("Error while generating unique keys for schema " + schema, e);
        }

        // Foreign keys
        try {
            for (ForeignKeyDefinition foreignKey : database.getForeignKeys(schema)) {
                empty = false;

                final String keyType = out.ref(getStrategy().getFullJavaClassName(foreignKey.getKeyTable(), Mode.RECORD));
                final String referencedType = out.ref(getStrategy().getFullJavaClassName(foreignKey.getReferencedTable(), Mode.RECORD));
                final String keyId = getStrategy().getJavaIdentifier(foreignKey);
                final int block = allForeignKeys.size() / maxMembersPerInitialiser();

                // [#10480] Print header before first key
                if (allForeignKeys.isEmpty()) {
                    out.header("FOREIGN KEY definitions");
                    out.println();
                }

                if (distributeForeignKeys)
                    if (scala)
                        out.println("%sval %s = ForeignKeys%s.%s", visibility(), keyId, block, keyId);
                    else if (kotlin)
                        out.println("%sval %s: %s<%s, %s> = ForeignKeys%s.%s", visibility(), keyId, ForeignKey.class, keyType, referencedType, block, keyId);
                    else
                        out.println("%sstatic final %s<%s, %s> %s = ForeignKeys%s.%s;", visibility(), ForeignKey.class, keyType, referencedType, keyId, block, keyId);
                else
                    printForeignKey(out, -1, foreignKey, distributeForeignKeys);

                allForeignKeys.add(foreignKey);
            }
        }
        catch (Exception e) {
            log.error("Error while generating foreign keys for schema " + schema, e);
        }

        // [#1459] [#10554] [#10653] Print nested classes for actual static field initialisations
        // keeping top-level initialiser small
        int uniqueKeyCounter = 0;
        int foreignKeyCounter = 0;

        if (distributeUniqueKeys || distributeForeignKeys) {
            out.header("[#1459] [#10554] [#10653] distribute members to avoid static initialisers > 64kb");

            // UniqueKeys
            // ----------

            if (distributeUniqueKeys) {
                for (UniqueKeyDefinition uniqueKey : allUniqueKeys)
                    printUniqueKey(out, uniqueKeyCounter++, uniqueKey, distributeUniqueKeys);

                if (uniqueKeyCounter > 0)
                    out.println("}");
            }

            // ForeignKeys
            // -----------

            if (distributeForeignKeys) {
                for (ForeignKeyDefinition foreignKey : allForeignKeys)
                    printForeignKey(out, foreignKeyCounter++, foreignKey, distributeForeignKeys);

                if (foreignKeyCounter > 0)
                    out.println("}");
            }
        }

        generateRelationsClassFooter(schema, out);

        if (!kotlin)
            out.println("}");

        if (empty) {
            log.info("Skipping empty keys");
        }
        else {
            closeJavaWriter(out);
            watch.splitInfo("Keys generated");
        }
    }

    /**
     * Subclasses may override this method to provide relations references class footer code.
     */
    @SuppressWarnings("unused")
    protected void generateRelationsClassFooter(SchemaDefinition schema, JavaWriter out) {}

    protected void generateIndexes(SchemaDefinition schema) {
        log.info("Generating Indexes");

        if (database.getIndexes(schema).isEmpty()) {
            log.info("Skipping empty indexes");
            return;
        }

        JavaWriter out = newJavaWriter(getStrategy().getGlobalReferencesFile(schema, IndexDefinition.class));
        out.refConflicts(getStrategy().getJavaIdentifiers(database.getIndexes(schema)));

        printGlobalReferencesPackage(out, schema, IndexDefinition.class);

        if (!kotlin) {
            printClassJavadoc(out, "A class modelling indexes of tables in "  + schemaNameOrDefault(schema) + ".");
            printClassAnnotations(out, schema, Mode.DEFAULT);
        }

        final String referencesClassName = getStrategy().getGlobalReferencesJavaClassName(schema, IndexDefinition.class);

        if (scala)
            out.println("%sobject %s {", visibility(), referencesClassName);
        else if (kotlin) {}
        else
            out.println("%sclass %s {", visibility(), referencesClassName);

        // [#1459] [#10554] [#10653] Distribute keys to nested classes only if necessary
        boolean distributeIndexes = database.getIndexes(schema).size() > maxMembersPerInitialiser();
        List<IndexDefinition> allIndexes = new ArrayList<>();

        out.header("INDEX definitions");
        out.println();

        for (IndexDefinition index : database.getIndexes(schema)) {
            try {
                final String keyId = getStrategy().getJavaIdentifier(index);
                final int block = allIndexes.size() / maxMembersPerInitialiser();

                if (distributeIndexes)
                    if (scala)
                        out.println("%sval %s = Indexes%s.%s", visibility(), keyId, block, keyId);
                    else if (kotlin)
                        out.println("%sval %s: %s = Indexes%s.%s", visibility(), keyId, Index.class, block, keyId);
                    else
                        out.println("%sstatic final %s %s = Indexes%s.%s;", visibility(), Index.class, keyId, block, keyId);
                else
                    printIndex(out, -1, index, distributeIndexes);

                allIndexes.add(index);
            }
            catch (Exception e) {
                log.error("Error while generating index " + index, e);
            }
        }

        // [#1459] [#10554] [#10653] Print nested classes for actual static field initialisations
        // keeping top-level initialiser small
        int indexCounter = 0;
        if (distributeIndexes) {
            out.header("[#1459] [#10554] [#10653] distribute members to avoid static initialisers > 64kb");

            // Indexes
            // -------

            for (IndexDefinition index : allIndexes) {
                printIndex(out, indexCounter, index, distributeIndexes);
                indexCounter++;
            }

            if (indexCounter > 0)
                out.println("}");
        }

        generateIndexesClassFooter(schema, out);

        if (!kotlin)
            out.println("}");
        closeJavaWriter(out);

        watch.splitInfo("Indexes generated");
    }

    /**
     * Subclasses may override this method to provide index references class footer code.
     */
    @SuppressWarnings("unused")
    protected void generateIndexesClassFooter(SchemaDefinition schema, JavaWriter out) {}

    protected void printIndex(JavaWriter out, int indexCounter, IndexDefinition index, boolean distributeIndexes) {
        final int block = indexCounter / maxMembersPerInitialiser();

        // Print new nested class
        if (indexCounter % maxMembersPerInitialiser() == 0) {
            if (indexCounter > 0)
                out.println("}");

            out.println();

            if (scala || kotlin)
                out.println("private object Indexes%s {", block);
            else
                out.println("private static class Indexes%s {", block);
        }

        if (scala)
            out.print("%sval %s: %s = ",
                visibility(),
                scalaWhitespaceSuffix(getStrategy().getJavaIdentifier(index)),
                Index.class
            );
        else if (kotlin)
            out.print("%sval %s: %s = ",
                visibility(),
                getStrategy().getJavaIdentifier(index),
                Index.class
            );
        else
            out.print("%sstatic final %s %s = ",
                visibility(),
                Index.class,
                getStrategy().getJavaIdentifier(index)
            );

        printCreateIndex(out, index);

        out.println("%s", semicolon);
    }



    private void printCreateIndex(JavaWriter out, IndexDefinition index) {
        String sortFieldSeparator = "";
        StringBuilder orderFields = new StringBuilder();

        for (IndexColumnDefinition column : index.getIndexColumns()) {
            orderFields.append(sortFieldSeparator);
            orderFields.append(out.ref(getStrategy().getFullJavaIdentifier(column.getColumn()), colRefSegments(null)));
            orderFields.append(column.getSortOrder() == DESC ? ".desc()" : "");

            sortFieldSeparator = ", ";
        }

        if (scala)
            out.print("%s.createIndex(%s.name(\"%s\"), %s, Array[%s [_] ](%s), %s)",
                Internal.class,
                DSL.class,
                escapeString(index.getOutputName()),
                out.ref(getStrategy().getFullJavaIdentifier(index.getTable()), 2),
                OrderField.class,
                orderFields,
                index.isUnique()
            );
        else if (kotlin)
            out.print("%s.createIndex(%s.name(\"%s\"), %s, arrayOf(%s), %s)",
                Internal.class,
                DSL.class,
                escapeString(index.getOutputName()),
                out.ref(getStrategy().getFullJavaIdentifier(index.getTable()), 2),
                orderFields,
                index.isUnique()
            );
        else
            out.print("%s.createIndex(%s.name(\"%s\"), %s, new %s[] { %s }, %s)",
                Internal.class,
                DSL.class,
                escapeString(index.getOutputName()),
                out.ref(getStrategy().getFullJavaIdentifier(index.getTable()), 2),
                OrderField.class,
                orderFields,
                index.isUnique()
            );
    }

    protected void printUniqueKey(JavaWriter out, int uniqueKeyCounter, UniqueKeyDefinition uniqueKey, boolean distributeUniqueKeys) {
        final int block = uniqueKeyCounter / maxMembersPerInitialiser();

        // Print new nested class
        if (distributeUniqueKeys && uniqueKeyCounter % maxMembersPerInitialiser() == 0) {
            if (uniqueKeyCounter > 0)
                out.println("}");

            out.println();

            if (scala || kotlin)
                out.println("private object UniqueKeys%s {", block);
            else
                out.println("private static class UniqueKeys%s {", block);
        }

        if (scala)
            out.print("%sval %s: %s[%s] = ",
                visibility(),
                scalaWhitespaceSuffix(getStrategy().getJavaIdentifier(uniqueKey)),
                UniqueKey.class,
                out.ref(getStrategy().getFullJavaClassName(uniqueKey.getTable(), Mode.RECORD)));
        else if (kotlin)
            out.print("%sval %s: %s<%s> = ",
                visibility(),
                getStrategy().getJavaIdentifier(uniqueKey),
                UniqueKey.class,
                out.ref(getStrategy().getFullJavaClassName(uniqueKey.getTable(), Mode.RECORD)));
        else
            out.print("%sstatic final %s<%s> %s = ",
                visibility(),
                UniqueKey.class,
                out.ref(getStrategy().getFullJavaClassName(uniqueKey.getTable(), Mode.RECORD)),
                getStrategy().getJavaIdentifier(uniqueKey));

        printCreateUniqueKey(out, uniqueKey);

        out.println("%s", semicolon);
    }
























    private void printCreateUniqueKey(JavaWriter out, UniqueKeyDefinition uniqueKey) {








        printCreateNonEmbeddableUniqueKey(out, uniqueKey);
    }

    private void printCreateNonEmbeddableUniqueKey(JavaWriter out, UniqueKeyDefinition uniqueKey) {
        if (scala)
            out.print("%s.createUniqueKey(%s, %s.name(\"%s\"), Array([[%s]]).asInstanceOf[Array[%s[%s, _] ] ], %s)",
                Internal.class,
                out.ref(getStrategy().getFullJavaIdentifier(uniqueKey.getTable()), 2),
                DSL.class,
                escapeString(uniqueKey.getOutputName()),
                out.ref(getStrategy().getFullJavaIdentifiers(uniqueKey.getKeyColumns()), colRefSegments(null)),
                TableField.class,
                out.ref(getStrategy().getJavaClassName(uniqueKey.getTable(), Mode.RECORD)),
                uniqueKey.enforced());
        else if (kotlin)
            out.print("%s.createUniqueKey(%s, %s.name(\"%s\"), arrayOf([[%s]]), %s)",
                Internal.class,
                out.ref(getStrategy().getFullJavaIdentifier(uniqueKey.getTable()), 2),
                DSL.class,
                escapeString(uniqueKey.getOutputName()),
                out.ref(getStrategy().getFullJavaIdentifiers(uniqueKey.getKeyColumns()), colRefSegments(null)),
                uniqueKey.enforced());
        else
            out.print("%s.createUniqueKey(%s, %s.name(\"%s\"), new %s[] { [[%s]] }, %s)",
                Internal.class,
                out.ref(getStrategy().getFullJavaIdentifier(uniqueKey.getTable()), 2),
                DSL.class,
                escapeString(uniqueKey.getOutputName()),
                TableField.class,
                out.ref(getStrategy().getFullJavaIdentifiers(uniqueKey.getKeyColumns()), colRefSegments(null)),
                uniqueKey.enforced());
    }
















    protected void printForeignKey(JavaWriter out, int foreignKeyCounter, ForeignKeyDefinition foreignKey, boolean distributeForeignKey) {
        final int block = foreignKeyCounter / maxMembersPerInitialiser();

        // Print new nested class
        if (distributeForeignKey && foreignKeyCounter % maxMembersPerInitialiser() == 0) {
            if (foreignKeyCounter > 0)
                out.println("}");

            out.println();

            if (scala || kotlin)
                out.println("private object ForeignKeys%s {", block);
            else
                out.println("private static class ForeignKeys%s {", block);
        }

        if (scala)
            out.print("%sval %s: %s[%s, %s] = ",
                visibility(),
                scalaWhitespaceSuffix(getStrategy().getJavaIdentifier(foreignKey)),
                ForeignKey.class,
                out.ref(getStrategy().getFullJavaClassName(foreignKey.getKeyTable(), Mode.RECORD)),
                out.ref(getStrategy().getFullJavaClassName(foreignKey.getReferencedTable(), Mode.RECORD)));
        else if (kotlin)
            out.print("%sval %s: %s<%s, %s> = ",
                visibility(),
                getStrategy().getJavaIdentifier(foreignKey),
                ForeignKey.class,
                out.ref(getStrategy().getFullJavaClassName(foreignKey.getKeyTable(), Mode.RECORD)),
                out.ref(getStrategy().getFullJavaClassName(foreignKey.getReferencedTable(), Mode.RECORD)));
        else
            out.print("%sstatic final %s<%s, %s> %s = ",
                visibility(),
                ForeignKey.class,
                out.ref(getStrategy().getFullJavaClassName(foreignKey.getKeyTable(), Mode.RECORD)),
                out.ref(getStrategy().getFullJavaClassName(foreignKey.getReferencedTable(), Mode.RECORD)),
                getStrategy().getJavaIdentifier(foreignKey));













        printCreateNonEmbeddableForeignKey(out, foreignKey);

        out.println("%s", semicolon);
    }

    private void printCreateNonEmbeddableForeignKey(JavaWriter out, ForeignKeyDefinition foreignKey) {
        if (scala)
            out.print("%s.createForeignKey(%s, %s.name(\"%s\"), Array([[%s]]).asInstanceOf[Array[%s[%s, _] ] ], %s, Array([[%s]]).asInstanceOf[Array[%s[%s, _] ] ], %s)",
                Internal.class,
                out.ref(getStrategy().getFullJavaIdentifier(foreignKey.getKeyTable()), 2),
                DSL.class,
                escapeString(foreignKey.getOutputName()),
                out.ref(getStrategy().getFullJavaIdentifiers(foreignKey.getKeyColumns()), colRefSegments(null)),
                TableField.class,
                out.ref(getStrategy().getJavaClassName(foreignKey.getTable(), Mode.RECORD)),
                out.ref(getStrategy().getFullJavaIdentifier(foreignKey.getReferencedKey()), 2),
                out.ref(getStrategy().getFullJavaIdentifiers(foreignKey.getReferencedColumns()), colRefSegments(null)),
                TableField.class,
                out.ref(getStrategy().getJavaClassName(foreignKey.getReferencedTable(), Mode.RECORD)),
                foreignKey.enforced()
            );
        else if (kotlin)
            out.print("%s.createForeignKey(%s, %s.name(\"%s\"), arrayOf([[%s]]), %s, arrayOf([[%s]]), %s)",
                Internal.class,
                out.ref(getStrategy().getFullJavaIdentifier(foreignKey.getKeyTable()), 2),
                DSL.class,
                escapeString(foreignKey.getOutputName()),
                out.ref(getStrategy().getFullJavaIdentifiers(foreignKey.getKeyColumns()), colRefSegments(null)),
                out.ref(getStrategy().getFullJavaIdentifier(foreignKey.getReferencedKey())),
                out.ref(getStrategy().getFullJavaIdentifiers(foreignKey.getReferencedColumns()), colRefSegments(null)),
                foreignKey.enforced()
            );
        else
            out.print("%s.createForeignKey(%s, %s.name(\"%s\"), new %s[] { [[%s]] }, %s, new %s[] { [[%s]] }, %s)",
                Internal.class,
                out.ref(getStrategy().getFullJavaIdentifier(foreignKey.getKeyTable()), 2),
                DSL.class,
                escapeString(foreignKey.getOutputName()),
                TableField.class,
                out.ref(getStrategy().getFullJavaIdentifiers(foreignKey.getKeyColumns()), colRefSegments(null)),
                out.ref(getStrategy().getFullJavaIdentifier(foreignKey.getReferencedKey()), 2),
                TableField.class,
                out.ref(getStrategy().getFullJavaIdentifiers(foreignKey.getReferencedColumns()), colRefSegments(null)),
                foreignKey.enforced()
            );
    }




























    protected void generateRecords(SchemaDefinition schema) {
        log.info("Generating table records");

        for (TableDefinition table : database.getTables(schema)) {
            try {
                if (table.isTableValuedFunction() && table.getReferencedTable() != table)
                    continue;

                generateRecord(table);
            }
            catch (Exception e) {
                log.error("Error while generating table record " + table, e);
            }
        }

        watch.splitInfo("Table records generated");
    }


    protected void generateRecord(TableDefinition table) {
        JavaWriter out = newJavaWriter(getFile(table, Mode.RECORD));
        log.info("Generating record", out.file().getName());
        generateRecord0(table, out);
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

        printPackage(out, tableUdtOrEmbeddable, Mode.RECORD);

        if (tableUdtOrEmbeddable instanceof TableDefinition)
            generateRecordClassJavadoc((TableDefinition) tableUdtOrEmbeddable, out);
        else if (tableUdtOrEmbeddable instanceof EmbeddableDefinition)
            generateEmbeddableClassJavadoc((EmbeddableDefinition) tableUdtOrEmbeddable, out);
        else
            generateUDTRecordClassJavadoc((UDTDefinition) tableUdtOrEmbeddable, out);

        printClassAnnotations(out, tableUdtOrEmbeddable, Mode.RECORD);
        if (tableUdtOrEmbeddable instanceof TableDefinition)
            printTableJPAAnnotation(out, (TableDefinition) tableUdtOrEmbeddable);

        String baseClass = out.ref(getStrategy().getJavaClassExtends(tableUdtOrEmbeddable, Mode.RECORD));

        // [#9844] The GeneratorStrategy doesn't have access to the generateRelations flag,
        //         so restore the behaviour here in case users don't override the defaults.
        if (UpdatableRecordImpl.class.getName().equals(baseClass) && !generateRelations())
            baseClass = TableRecordImpl.class.getName();

        // [#10481] Use the types from replaced embeddables if applicable
        List<Definition> embeddablesAndColumns = embeddablesAndColumns(tableUdtOrEmbeddable);
        List<Definition> embeddablesAndUnreplacedColumns = embeddablesAndUnreplacedColumns(tableUdtOrEmbeddable);
        List<Definition> replacingEmbeddablesAndUnreplacedColumns = replacingEmbeddablesAndUnreplacedColumns(tableUdtOrEmbeddable);
        List<Definition> embeddablesOrColumns = embeddablesOrColumns(tableUdtOrEmbeddable);
        int degree = replacingEmbeddablesAndUnreplacedColumns.size();

        String rowType = null;
        String rowTypeRecord = null;

        // [#3130] Invalid UDTs may have a degree of 0
        // [#6072] Generate these super types only if configured to do so
        if (generateRecordsImplementingRecordN() && degree > 0 && degree <= Constants.MAX_ROW_DEGREE) {
            rowType = refRowType(out, replacingEmbeddablesAndUnreplacedColumns);

            if (scala)
                rowTypeRecord = out.ref(Record.class.getName() + degree) + "[" + rowType + "]";
            else
                rowTypeRecord = out.ref(Record.class.getName() + degree) + "<" + rowType + ">";

            interfaces.add(rowTypeRecord);
        }

        if (generateInterfaces())
            interfaces.add(out.ref(getStrategy().getFullJavaClassName(tableUdtOrEmbeddable, Mode.INTERFACE)));

        if (scala) {
            if (tableUdtOrEmbeddable instanceof EmbeddableDefinition)
                out.println("%sclass %s extends %s[%s](%s.%s.getDataType.getRow)[[before= with ][separator= with ][%s]] {",
                    visibility(),
                    className,
                    baseClass,
                    className,
                    out.ref(getStrategy().getFullJavaIdentifier(((EmbeddableDefinition) tableUdtOrEmbeddable).getTable()), 2),
                    getStrategy().getJavaIdentifier(tableUdtOrEmbeddable),
                    interfaces
                );
            else
                out.println("%sclass %s extends %s[%s](%s)[[before= with ][separator= with ][%s]] {",
                    visibility(),
                    className,
                    baseClass,
                    className,
                    tableIdentifier,
                    interfaces
                );
        }
        else if (kotlin) {
            String constructorVisibility = generateKotlinNotNullRecordAttributes() ? " private constructor" : "";
            if (tableUdtOrEmbeddable instanceof EmbeddableDefinition)
                out.println("%sopen class %s%s() : %s<%s>(%s.%s.dataType.row)[[before=, ][%s]] {",
                    visibility(),
                    className,
                    constructorVisibility,
                    baseClass,
                    className,
                    out.ref(getStrategy().getFullJavaIdentifier(((EmbeddableDefinition) tableUdtOrEmbeddable).getTable()), 2),
                    getStrategy().getJavaIdentifier(tableUdtOrEmbeddable),
                    interfaces
                );
            else
                out.println("%sopen class %s%s() : %s<%s>(%s)[[before=, ][%s]] {",
                    visibility(),
                    className,
                    constructorVisibility,
                    baseClass,
                    className,
                    tableIdentifier,
                    interfaces
                );
        }
        else
            out.println("%sclass %s extends %s<%s>[[before= implements ][%s]] {", visibility(), className, baseClass, className, interfaces);

        out.printSerial();

        for (Definition column : embeddablesAndUnreplacedColumns) {
            final int index = replacingEmbeddablesAndUnreplacedColumns.indexOf(column);

            if (column instanceof EmbeddableDefinition) {
                final EmbeddableDefinition embeddable = (EmbeddableDefinition) column;

                generateEmbeddableRecordSetter(embeddable, index, out);
                generateEmbeddableRecordGetter(embeddable, index, out);
            }
            else {
                final TypedElementDefinition<?> c = (TypedElementDefinition<?>) column;

                if (tableUdtOrEmbeddable instanceof TableDefinition) {
                    generateRecordSetter(c, index, out);
                    generateRecordGetter(c, index, out);
                }
                else if (tableUdtOrEmbeddable instanceof EmbeddableDefinition) {
                    generateEmbeddableSetter(c, index, out);
                    generateEmbeddableGetter(c, index, out);
                }
                else {
                    generateUDTRecordSetter(c, index, out);
                    generateUDTRecordGetter(c, index, out);
                }
            }
        }




        if (generateRelations() && key != null) {
            int keyDegree = key.getKeyColumns().size();

            if (keyDegree <= Constants.MAX_ROW_DEGREE) {
                final String recordNType = out.ref(Record.class.getName() + keyDegree);
                final String keyType = refRowType(out, key.getKeyColumns());
                out.header("Primary key information");

                if (scala) {
                    out.println();
                    out.println("%soverride def key: %s[%s] = super.key.asInstanceOf[ %s[%s] ]", visibilityPublic(), recordNType, keyType, recordNType, keyType);
                }
                else if (kotlin) {
                    out.println();
                    out.println("%soverride fun key(): %s<%s> = super.key() as %s<%s>", visibilityPublic(), recordNType, keyType, recordNType, keyType);
                }
                else {
                    out.overrideInherit();
                    printNonnullAnnotation(out);
                    out.println("%s%s<%s> key() {", visibilityPublic(), recordNType, keyType);
                    out.println("return (%s) super.key();", recordNType);
                    out.println("}");
                }
            }
        }

        if (tableUdtOrEmbeddable instanceof UDTDefinition) {

            // [#799] Oracle UDT's can have member procedures
            for (RoutineDefinition routine : ((UDTDefinition) tableUdtOrEmbeddable).getRoutines()) {

                // Instance methods ship with a SELF parameter at the first position
                // [#1584] Static methods don't have that
                boolean instance = routine.getInParameters().size() > 0
                                && routine.getInParameters().get(0).getInputName().toUpperCase(getStrategy().getTargetLocale()).equals("SELF");

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

                }
                catch (Exception e) {
                    log.error("Error while generating routine " + routine, e);
                }
            }
        }

        // [#3130] Invalid UDTs may have a degree of 0
        if (generateRecordsImplementingRecordN() && degree > 0 && degree <= Constants.MAX_ROW_DEGREE) {
            final String recordNType = out.ref(Row.class.getName() + degree);

            out.header("Record%s type implementation", degree);

            // fieldsRow()
            if (scala) {
                out.println();
                out.println("%soverride def fieldsRow: %s[%s] = super.fieldsRow.asInstanceOf[ %s[%s] ]", visibilityPublic(), recordNType, rowType, recordNType, rowType);
            }
            else if (kotlin) {
                out.println();
                out.println("%soverride fun fieldsRow(): %s<%s> = super.fieldsRow() as %s<%s>", visibilityPublic(), recordNType, rowType, recordNType, rowType);
            }
            else {
                out.overrideInherit();
                printNonnullAnnotation(out);
                out.println("%s%s<%s> fieldsRow() {", visibilityPublic(), recordNType, rowType);
                out.println("return (%s) super.fieldsRow();", recordNType);
                out.println("}");
            }

            // valuesRow()
            if (scala) {
                out.println();
                out.println("%soverride def valuesRow: %s[%s] = super.valuesRow.asInstanceOf[ %s[%s] ]", visibilityPublic(), recordNType, rowType, recordNType, rowType);
            }
            else if (kotlin) {
                out.println("%soverride fun valuesRow(): %s<%s> = super.valuesRow() as %s<%s>", visibilityPublic(), recordNType, rowType, recordNType, rowType);
            }
            else {
                out.overrideInherit();
                printNonnullAnnotation(out);
                out.println("%s%s<%s> valuesRow() {", visibilityPublic(), recordNType, rowType);
                out.println("return (%s) super.valuesRow();", recordNType);
                out.println("}");
            }

            // field[N]()
            for (int i = 1; i <= degree; i++) {
                Definition column = replacingEmbeddablesAndUnreplacedColumns.get(i - 1);

                if (column instanceof EmbeddableColumnDefinition)
                    column = ((EmbeddableColumnDefinition) column).getReferencingColumn();

                final String colTypeFull = getJavaType(column, out);
                final String colType = out.ref(colTypeFull);
                final String colIdentifierFull = out.ref(getStrategy().getFullJavaIdentifier(column), colRefSegments(column));
                final String colIdentifier = getStrategy().getJavaIdentifier(column);

                if (scala) {
                    printDeprecationIfUnknownType(out, colTypeFull);

                    if (tableUdtOrEmbeddable instanceof EmbeddableDefinition)
                        out.println("%soverride def field%s: %s[%s] = field(%s).asInstanceOf[%s [%s] ]",
                            visibilityPublic(),
                            i,
                            Field.class,
                            colType,
                            i - 1,
                            Field.class,
                            colType
                        );
                    else
                        out.println("%soverride def field%s: %s[%s] = %s", visibilityPublic(), i, Field.class, colType, colIdentifierFull);
                }
                else if (kotlin) {
                    printDeprecationIfUnknownType(out, colTypeFull);

                    if (tableUdtOrEmbeddable instanceof EmbeddableDefinition)
                        out.println("%soverride fun field%s(): %s<%s?> = field(%s) as %s<%s?>",
                            visibilityPublic(),
                            i,
                            Field.class,
                            colType,
                            i - 1,
                            Field.class,
                            colType
                        );
                    else if (tableUdtOrEmbeddable instanceof UDTDefinition)
                        out.println("%soverride fun field%s(): %s<%s%s> = %s.%s", visibilityPublic(), i, Field.class, colType, column instanceof EmbeddableDefinition ? "" : "?", out.ref(getStrategy().getFullJavaIdentifier(((AttributeDefinition) column).getContainer()), 2), colIdentifier);
                    else
                        out.println("%soverride fun field%s(): %s<%s%s> = %s", visibilityPublic(), i, Field.class, colType, column instanceof EmbeddableDefinition ? "" : "?", colIdentifierFull);
                }
                else {
                    if (printDeprecationIfUnknownType(out, colTypeFull))
                        out.override();
                    else
                        out.overrideInherit();

                    printNonnullAnnotation(out);
                    out.println("%s%s<%s> field%s() {", visibilityPublic(), Field.class, colType, i);

                    if (tableUdtOrEmbeddable instanceof EmbeddableDefinition)
                        out.println("return (%s<%s>) field(%s);", Field.class, colType, i - 1);
                    else
                        out.println("return %s;", colIdentifierFull);

                    out.println("}");
                }
            }

            // component[N]()
            for (int i = 1; i <= degree; i++) {
                Definition column = replacingEmbeddablesAndUnreplacedColumns.get(i - 1);

                final String colTypeFull = getJavaType(column, out);
                final String colType = out.ref(colTypeFull);
                final String colGetter = getStrategy().getJavaGetterName(column, Mode.RECORD);
                final String colMember = getStrategy().getJavaMemberName(column, Mode.POJO);

                if (scala) {
                    printDeprecationIfUnknownType(out, colTypeFull);
                    out.println("%soverride def component%s: %s = %s", visibilityPublic(), i, colType, colGetter);
                }
                else if (kotlin) {
                    final String nullability;

                    if (column instanceof TypedElementDefinition<?> ted)
                        nullability = kotlinNullability(out, ted, Mode.RECORD);
                    else
                        nullability = "?";

                    printDeprecationIfUnknownType(out, colTypeFull);
                    out.println("%soverride fun component%s(): %s%s = %s", visibilityPublic(), i, colType, nullability, colMember);
                }
                else {
                    if (printDeprecationIfUnknownType(out, colTypeFull))
                        out.override();
                    else
                        out.overrideInherit();

                    printNullableOrNonnullAnnotation(out, column);
                    out.println("%s%s component%s() {", visibilityPublic(), colType, i);
                    out.println("return %s();", colGetter);
                    out.println("}");
                }
            }

            // value[N]()
            for (int i = 1; i <= degree; i++) {
                Definition column = replacingEmbeddablesAndUnreplacedColumns.get(i - 1);

                final String colTypeFull = getJavaType(column, out);
                final String colType = out.ref(colTypeFull);
                final String colGetter = getStrategy().getJavaGetterName(column, Mode.RECORD);
                final String colMember = getStrategy().getJavaMemberName(column, Mode.POJO);

                if (scala) {
                    printDeprecationIfUnknownType(out, colTypeFull);
                    out.println("%soverride def value%s: %s = %s", visibilityPublic(), i, colType, colGetter);
                }
                else if (kotlin) {
                    final String nullability;

                    if (column instanceof TypedElementDefinition<?> ted)
                        nullability = kotlinNullability(out, ted, Mode.RECORD);
                    else
                        nullability = "?";

                    printDeprecationIfUnknownType(out, colTypeFull);
                    out.println("%soverride fun value%s(): %s%s = %s", visibilityPublic(), i, colType, nullability, colMember);
                }
                else {
                    if (printDeprecationIfUnknownType(out, colTypeFull))
                        out.override();
                    else
                        out.overrideInherit();

                    printNullableOrNonnullAnnotation(out, column);
                    out.println("%s%s value%s() {", visibilityPublic(), colType, i);
                    out.println("return %s();", colGetter);
                    out.println("}");
                }
            }

            // value[N](T[N])
            for (int i = 1; i <= degree; i++) {
                Definition column = replacingEmbeddablesAndUnreplacedColumns.get(i - 1);

                final String colTypeFull = getJavaType(column, out);
                final String colType = out.ref(colTypeFull);
                final String colSetter = getStrategy().getJavaSetterName(column, Mode.RECORD);
                final String colMember = getStrategy().getJavaMemberName(column, Mode.POJO);

                if (scala) {
                    out.println();
                    printDeprecationIfUnknownType(out, colTypeFull);
                    out.println("%soverride def value%s(value: %s): %s = {", visibilityPublic(), i, colType, className);
                    out.println("%s(value)", colSetter);
                    out.println("this");
                    out.println("}");
                }
                else if (kotlin) {
                    out.println();
                    printDeprecationIfUnknownType(out, colTypeFull);
                    out.println("%soverride fun value%s(value: %s%s): %s {", visibilityPublic(), i, colType, column instanceof EmbeddableDefinition ? "" : "?", className);

                    // [#14785] Can't use setter, in case <kotlinNotNullRecordAttributes/> is active
                    out.println("set(%s, value)", (i - 1));
                    out.println("return this");
                    out.println("}");
                }
                else {
                    final String nullableAnnotation = nullableOrNonnullAnnotation(out, column);

                    if (printDeprecationIfUnknownType(out, colTypeFull))
                        out.override();
                    else
                        out.overrideInherit();

                    printNonnullAnnotation(out);
                    out.println("%s%s value%s([[before=@][after= ][%s]]%s value) {", visibilityPublic(), className, i, list(nullableAnnotation), varargsIfArray(colType));
                    out.println("%s(value);", colSetter);
                    out.println("return this;");
                    out.println("}");
                }
            }

            List<String> arguments = new ArrayList<>(degree);
            List<String> calls = new ArrayList<>(degree);
            for (int i = 1; i <= degree; i++) {
                final Definition column = replacingEmbeddablesAndUnreplacedColumns.get(i - 1);
                final String colType = getJavaTypeRef(column, out);

                if (scala) {
                    arguments.add("value" + i + " : " + colType);
                    calls.add("this.value" + i + "(value" + i + ")");
                }
                else if (kotlin) {
                    arguments.add("value" + i + ": " + colType + (column instanceof EmbeddableDefinition ? "" : "?"));
                    calls.add("this.value" + i + "(value" + i + ")");
                }
                else {
                    final String nullableAnnotation = nullableOrNonnullAnnotation(out, column);

                    arguments.add((nullableAnnotation == null ? "" : "@" + nullableAnnotation + " ") + colType + " value" + i);
                    calls.add("value" + i + "(value" + i + ");");
                }
            }

            if (scala) {
                out.println();
                out.println("%soverride def values([[%s]]): %s = {", visibilityPublic(), arguments, className);

                for (String call : calls)
                    out.println(call);

                out.println("this");
                out.println("}");
            }
            else if (kotlin) {
                out.println();
                out.println("%soverride fun values([[%s]]): %s {", visibilityPublic(), arguments, className);

                for (String call : calls)
                    out.println(call);

                out.println("return this");
                out.println("}");
            }
            else {
                out.overrideInherit();
                printNonnullAnnotation(out);
                out.println("%s%s values([[%s]]) {", visibilityPublic(), className, arguments);

                for (String call : calls)
                    out.println(call);

                out.println("return this;");
                out.println("}");
            }
        }

        if (generateInterfaces())
            printFromAndInto(out, tableUdtOrEmbeddable, Mode.RECORD);

        if (scala || kotlin) {}
        else {
            out.header("Constructors");
            out.javadoc("Create a detached %s", className);
            out.println("%s%s() {", visibility(), className);

            if (tableUdtOrEmbeddable instanceof EmbeddableDefinition)
                out.println("super(%s.%s.getDataType().getRow());",
                    out.ref(getStrategy().getFullJavaIdentifier(((EmbeddableDefinition) tableUdtOrEmbeddable).getTable()), 2),
                    getStrategy().getJavaIdentifier(tableUdtOrEmbeddable));
            else
                out.println("super(%s);", tableIdentifier);

            out.println("}");
        }

        // [#3130] Invalid UDTs may have a degree of 0
        // [#3176] Avoid generating constructors for tables with more than 255 columns (Java's method argument limit)
        generateRecordConstructor(tableUdtOrEmbeddable, out, replacingEmbeddablesAndUnreplacedColumns, false);










        if (!replacingEmbeddablesAndUnreplacedColumns.equals(embeddablesOrColumns))
            generateRecordConstructor(tableUdtOrEmbeddable, out, embeddablesOrColumns, false);

        if (generatePojos())
            generateRecordConstructor(tableUdtOrEmbeddable, out, replacingEmbeddablesAndUnreplacedColumns, true);

        if (tableUdtOrEmbeddable instanceof TableDefinition)
            generateRecordClassFooter((TableDefinition) tableUdtOrEmbeddable, out);
        else if (tableUdtOrEmbeddable instanceof EmbeddableDefinition)
            generateEmbeddableClassFooter((EmbeddableDefinition) tableUdtOrEmbeddable, out);
        else
            generateUDTRecordClassFooter((UDTDefinition) tableUdtOrEmbeddable, out);

        out.println("}");
    }

    @FunctionalInterface
    private interface EmbeddableFilter {
        void accept(List<Definition> result, Set<EmbeddableDefinition> duplicates, int index, EmbeddableDefinition embeddable);
    }

    private static final EmbeddableFilter EMBEDDABLES_OR_COLUMNS = (result, duplicates, index, embeddable) -> {
        if (duplicates.add(embeddable))
            result.set(index, embeddable);
        else
            result.remove(index);
    };

    private static final EmbeddableFilter EMBEDDABLES_AND_COLUMNS = (result, duplicates, index, embeddable) -> {
        if (duplicates.add(embeddable))
            result.add(index, embeddable);
    };

    /**
     * Get embeddables and all columns that are not part of embeddables.
     */
    private List<Definition> embeddablesOrColumns(Definition tableUdtOrEmbeddable) {
        return embeddablesAndColumns(tableUdtOrEmbeddable, EMBEDDABLES_OR_COLUMNS);
    }

    /**
     * Get embeddables if they replace columns and all columns that are not replaced by embeddables.
     */
    private List<Definition> embeddablesAndUnreplacedColumns(Definition tableUdtOrEmbeddable) {
        return embeddablesAndColumns(tableUdtOrEmbeddable, (result, duplicates, index, embeddable) -> {






            EMBEDDABLES_AND_COLUMNS.accept(result, duplicates, index, embeddable);
        });
    }

    /**
     * Get embeddables if they replace columns and all columns that are not replaced by embeddables.
     */
    private List<Definition> replacingEmbeddablesAndUnreplacedColumns(Definition tableUdtOrEmbeddable) {
        return embeddablesAndColumns(tableUdtOrEmbeddable, (result, duplicates, index, embeddable) -> {




        });
    }

    /**
     * Get embeddables and all columns.
     */
    private List<Definition> embeddablesAndColumns(Definition tableUdtOrEmbeddable) {
        return embeddablesAndColumns(tableUdtOrEmbeddable, EMBEDDABLES_AND_COLUMNS);
    }

    private List<Definition> embeddablesAndColumns(Definition tableUdtOrEmbeddable, EmbeddableFilter filter) {
        List<Definition> result = new ArrayList<>(getTypedElements(tableUdtOrEmbeddable));

        if (tableUdtOrEmbeddable instanceof TableDefinition) {
            Set<EmbeddableDefinition> duplicates = new HashSet<>();

            for (EmbeddableDefinition embeddable : ((TableDefinition) tableUdtOrEmbeddable).getReferencedEmbeddables()) {
                for (EmbeddableColumnDefinition embeddableColumn : embeddable.getColumns()) {
                    int index = result.indexOf(embeddableColumn.getReferencingColumn());

                    if (index >= 0)
                        filter.accept(result, duplicates, index, embeddable);
                }
            }
        }

        return result;
    }

    private void generateRecordConstructor(
        Definition tableUdtOrEmbeddable,
        JavaWriter out,
        Collection<? extends Definition> columns,
        boolean pojoArgument
    ) {
        if (pojoArgument && !generatePojos())
            return;







        final String className = getStrategy().getJavaClassName(tableUdtOrEmbeddable, Mode.RECORD);
        final String pojoNameFull = getStrategy().getFullJavaClassName(tableUdtOrEmbeddable, Mode.POJO);
        final String tableIdentifier = !(tableUdtOrEmbeddable instanceof EmbeddableDefinition)
            ? out.ref(getStrategy().getFullJavaIdentifier(tableUdtOrEmbeddable), 2)
            : null;
        final int degree = columns.size();

        // There are some edge cases for 0-degree record types, such as Oracle's XMLTYPE
        // While these types shouldn't have any auxiliary constructors accepting individual attributes
        // they can still have a pojoArgument constructor
        if (pojoArgument || degree > 0 && degree < 255) {
            List<String> arguments = new ArrayList<>(degree);
            List<String> properties = new ArrayList<>(degree);

            for (Definition column : columns) {
                final String columnMember = getStrategy().getJavaMemberName(column, Mode.DEFAULT);
                final String type = getJavaTypeRef(column, out);

                if (scala) {
                    arguments.add(columnMember + " : " + type);
                }
                else if (kotlin) {
                    final boolean nn;

                    if (column instanceof EmbeddableDefinition)
                        nn = kotlinEffectivelyNotNull(out, (EmbeddableDefinition) column, Mode.RECORD);
                    else
                        nn = kotlinEffectivelyNotNull(out, (TypedElementDefinition<?>) column, Mode.RECORD);

                    if (nn)
                        arguments.add(columnMember + ": " + type);
                    else
                        arguments.add(columnMember + ": " + type + "? = null");
                }
                else {
                    final String nullableAnnotation = column instanceof EmbeddableDefinition
                        ? null
                        : nullableOrNonnullAnnotation(out, column);

                    arguments.add((nullableAnnotation == null ? "" : "@" + nullableAnnotation + " ") + type + " " + columnMember);
                }

                properties.add("\"" + escapeString(columnMember) + "\"");
            }

            out.javadoc("Create a detached, initialised %s", className);

            if (scala) {
                if (pojoArgument)
                    out.println("%sdef this(value: %s) = {", visibility(), out.ref(pojoNameFull));
                else
                    out.println("%sdef this([[%s]]) = {", visibility(), arguments);

                out.println("this()", tableIdentifier);
                out.println();
            }
            else if (kotlin) {
                if (pojoArgument)
                    out.println("%sconstructor(value: %s?): this() {", visibility(), out.ref(pojoNameFull));
                else
                    out.println("%sconstructor([[%s]]): this() {", visibility(), arguments);
            }
            else {
                if (pojoArgument) {
                    out.println("%s%s(%s value) {", visibility(), className, out.ref(pojoNameFull));
                }
                else {
                    if (generateConstructorPropertiesAnnotationOnRecords())
                        out.println("@%s({ [[%s]] })", out.ref("java.beans.ConstructorProperties"), properties);

                    out.println("%s%s([[%s]]) {", visibility(), className, arguments);
                }

                if (tableUdtOrEmbeddable instanceof EmbeddableDefinition)
                    out.println("this();", tableIdentifier);
                else
                    out.println("super(%s);", tableIdentifier);

                out.println();
            }

            if (pojoArgument && degree > 0)
                out.println("if (value != null) {");

            for (Definition column : columns) {
                if (column instanceof EmbeddableDefinition e) {

                    // TODO: Setters of X properties cannot accept X? in Kotlin: https://twitter.com/lukaseder/status/1296371561214234624
                    if (kotlin)
                        if (pojoArgument)
                            if (kotlinEffectivelyNotNull(out, e, Mode.RECORD))
                                out.println("this.%s = %s(%s)",
                                    getStrategy().getJavaMemberName(column, Mode.POJO),
                                    out.ref(getStrategy().getFullJavaClassName(column, Mode.RECORD)),
                                    getStrategy().getJavaMemberName(column, Mode.POJO));
                            else
                                out.println("this.%s = %s(%s) ?: %s([[%s]])",
                                    getStrategy().getJavaMemberName(column, Mode.POJO),
                                    out.ref(getStrategy().getFullJavaClassName(column, Mode.RECORD)),
                                    getStrategy().getJavaMemberName(column, Mode.POJO),
                                    out.ref(getStrategy().getFullJavaClassName(column, Mode.RECORD)),
                                    Collections.nCopies(e.getColumns().size(), "null"));
                        else if (kotlinEffectivelyNotNull(out, e, Mode.RECORD))
                            out.println("this.%s = %s",
                                getStrategy().getJavaMemberName(column, Mode.POJO),
                                getStrategy().getJavaMemberName(column, Mode.POJO));
                        else
                            out.println("this.%s = %s ?: %s([[%s]])",
                                getStrategy().getJavaMemberName(column, Mode.POJO),
                                getStrategy().getJavaMemberName(column, Mode.POJO),
                                out.ref(getStrategy().getFullJavaClassName(column, Mode.RECORD)),
                                Collections.nCopies(e.getColumns().size(), "null"));

                    // In Scala, the setter call can be ambiguous, e.g. when using KeepNamesGeneratorStrategy
                    else if (scala)
                        if (pojoArgument)
                            out.println("this.%s(new %s(value.%s))",
                                getStrategy().getJavaSetterName(column, Mode.RECORD),
                                out.ref(getStrategy().getFullJavaClassName(column, Mode.RECORD)),
                                getStrategy().getJavaGetterName(column, Mode.DEFAULT));
                        else
                            out.println("this.%s(%s)",
                                getStrategy().getJavaSetterName(column, Mode.RECORD),
                                getStrategy().getJavaMemberName(column, Mode.DEFAULT));
                    else
                        if (pojoArgument)
                            out.println("%s(new %s(value.%s()));",
                                getStrategy().getJavaSetterName(column, Mode.RECORD),
                                out.ref(getStrategy().getFullJavaClassName(column, Mode.RECORD)),
                                generatePojosAsJavaRecordClasses()
                                    ? getStrategy().getJavaMemberName(column, Mode.POJO)
                                    : getStrategy().getJavaGetterName(column, Mode.DEFAULT));
                        else
                            out.println("%s(%s);",
                                getStrategy().getJavaSetterName(column, Mode.RECORD),
                                getStrategy().getJavaMemberName(column, Mode.DEFAULT));
                }
                else {
                    final TypedElementDefinition<?> t = (TypedElementDefinition<?>) column;
                    final JavaTypeResolver r = resolver(out, Mode.RECORD);

                    final boolean isUDT = t.getType(r).isUDT();
                    final boolean isArray = t.getType(r).isArray();
                    final boolean isUDTArray = t.getType(r).isUDTArray();
                    final ArrayDefinition array = database.getArray(t.getType(r).getSchema(), t.getType(r).getQualifiedUserType());
                    final String indexTypeFull = array == null || array.getIndexType() == null ? null : getJavaType(array.getIndexType(resolver(out)), out);
                    final boolean isArrayOfUDTs = isArrayOfUDTs(t, r);

                    final String udtType = (isUDT || isArray)
                        ? out.ref(getJavaType(t.getType(r), out, Mode.RECORD))
                        : "";
                    final String udtArrayElementType = isUDTArray
                        ? out.ref(database.getArray(t.getType(r).getSchema(), t.getType(r).getQualifiedUserType()).getElementType(r).getJavaType(r))
                        : isArrayOfUDTs
                        ? out.ref(getArrayBaseType(t.getType(r).getJavaType(r)))
                        : "";

                    if (kotlin) {
                        if (pojoArgument)
                            if (isUDTArray)
                                out.println("this.%s = value.%s?.let { %s(it.map { it?.let { %s(it) } }) }",
                                    getStrategy().getJavaMemberName(column, Mode.POJO),
                                    getStrategy().getJavaMemberName(column, Mode.POJO),
                                    udtType,
                                    udtArrayElementType);
                            else if (isArrayOfUDTs)
                                out.println("this.%s = value.%s?.let { it.map { it?.let { %s(it) } }.toTypedArray() }",
                                    getStrategy().getJavaMemberName(column, Mode.POJO),
                                    getStrategy().getJavaMemberName(column, Mode.POJO),
                                    udtArrayElementType);
                            else if (isUDT || isArray)
                                out.println("this.%s = value.%s?.let { %s(it) }",
                                    getStrategy().getJavaMemberName(column, Mode.POJO),
                                    getStrategy().getJavaMemberName(column, Mode.POJO),
                                    udtType);
                            else if (kotlinEffectivelyNotNull(out, t, Mode.RECORD) && !kotlinEffectivelyNotNull(out, t, Mode.POJO))
                                out.println("value.%s?.let { this.%s = it }",
                                    getStrategy().getJavaMemberName(column, Mode.POJO),
                                    getStrategy().getJavaMemberName(column, Mode.POJO));
                            else
                                out.println("this.%s = value.%s",
                                    getStrategy().getJavaMemberName(column, Mode.POJO),
                                    getStrategy().getJavaMemberName(column, Mode.POJO));
                        else
                            out.println("this.%s = %s",
                                getStrategy().getJavaMemberName(column, Mode.POJO),
                                getStrategy().getJavaMemberName(column, Mode.POJO));
                    }

                    // In Scala, the setter call can be ambiguous, e.g. when using KeepNamesGeneratorStrategy
                    else if (scala) {
                        if (pojoArgument)
                            if (isUDTArray)
                                out.println("this.%s(if (value.%s == null) null else new %s(value.%s.stream().map { it => new %s(it) }.collect(%s.toList())))",
                                    getStrategy().getJavaSetterName(column, Mode.POJO),
                                    getStrategy().getJavaGetterName(column, Mode.POJO),
                                    udtType,
                                    getStrategy().getJavaGetterName(column, Mode.POJO),
                                    udtArrayElementType,
                                    Collectors.class);
                            else if (isArrayOfUDTs)
                                out.println("this.%s(if (value.%s == null) null else value.%s.map { it => new %s(it) })",
                                    getStrategy().getJavaSetterName(column, Mode.POJO),
                                    getStrategy().getJavaGetterName(column, Mode.POJO),
                                    getStrategy().getJavaGetterName(column, Mode.POJO),
                                    udtArrayElementType);
                            else if (isUDT || isArray)
                                out.println("this.%s(if (value.%s == null) null else new %s(value.%s))",
                                    getStrategy().getJavaSetterName(column, Mode.RECORD),
                                    getStrategy().getJavaGetterName(column, Mode.POJO),
                                    udtType,
                                    getStrategy().getJavaGetterName(column, Mode.POJO));
                            else
                                out.println("this.%s(value.%s)",
                                    getStrategy().getJavaSetterName(column, Mode.RECORD),
                                    getStrategy().getJavaGetterName(column, Mode.POJO));
                        else
                            out.println("this.%s(%s)",
                                getStrategy().getJavaSetterName(column, Mode.RECORD),
                                getStrategy().getJavaMemberName(column, Mode.POJO));
                    }
                    else {
                        if (pojoArgument) {
                            if (isUDTArray) {
                                if (indexTypeFull == null) {
                                    out.println("%s(value.%s() == null ? null : new %s(value.%s().stream().map(%s::new).collect(%s.toList())));",
                                        getStrategy().getJavaSetterName(column, Mode.RECORD),
                                        getStrategy().getJavaGetterName(column, Mode.POJO),
                                        udtType,
                                        generatePojosAsJavaRecordClasses()
                                            ? getStrategy().getJavaMemberName(column, Mode.POJO)
                                            : getStrategy().getJavaGetterName(column, Mode.POJO),
                                        udtArrayElementType,
                                        Collectors.class);
                                }
                                else {
                                    out.println("if (true)");
                                    out.println("throw new %s(\"Cannot use POJO constructor for POJO that references Oracle associative array yet. See https://github.com/jOOQ/jOOQ/issues/15108 for details.\");", UnsupportedOperationException.class);
                                }
                            }
                            else if (isArrayOfUDTs) {
                                final String columnTypeFull = getJavaType(t.getType(resolver(out, Mode.POJO)), out, Mode.POJO);
                                final String brackets = columnTypeFull.substring(columnTypeFull.indexOf("[]"));

                                String mapping = udtArrayElementType + "::new";
                                String arrayType = udtArrayElementType + "[]";

                                for (int dimensions = brackets.length() / 2 - 1; dimensions > 0; dimensions--) {
                                    String a = "a" + dimensions;
                                    mapping = a + " -> Stream.of(" + a + ").map(" + mapping + ").toArray(" + arrayType + "::new)";
                                    arrayType += "[]";
                                }

                                out.println("%s(value.%s() == null ? null : %s.of(value.%s()).map(%s).toArray(%s%s::new));",
                                    getStrategy().getJavaSetterName(column, Mode.RECORD),
                                    getStrategy().getJavaGetterName(column, Mode.POJO),
                                    Stream.class,
                                    generatePojosAsJavaRecordClasses()
                                        ? getStrategy().getJavaMemberName(column, Mode.POJO)
                                        : getStrategy().getJavaGetterName(column, Mode.POJO),
                                    mapping,
                                    udtArrayElementType,
                                    brackets);
                            }
                            else if (isUDT || isArray) {
                                out.println("%s(value.%s() == null ? null : new %s(value.%s()));",
                                    getStrategy().getJavaSetterName(column, Mode.RECORD),
                                    getStrategy().getJavaGetterName(column, Mode.POJO),
                                    udtType,
                                    generatePojosAsJavaRecordClasses()
                                        ? getStrategy().getJavaMemberName(column, Mode.POJO)
                                        : getStrategy().getJavaGetterName(column, Mode.POJO));
                            }
                            else {
                                out.println("%s(value.%s());",
                                    getStrategy().getJavaSetterName(column, Mode.RECORD),
                                    generatePojosAsJavaRecordClasses()
                                        ? getStrategy().getJavaMemberName(column, Mode.POJO)
                                        : getStrategy().getJavaGetterName(column, Mode.POJO));
                            }
                        }
                        else
                            out.println("%s(%s);",
                                getStrategy().getJavaSetterName(column, Mode.RECORD),
                                getStrategy().getJavaMemberName(column, Mode.POJO));
                    }
                }
            }

            // [#14817] [#14727] Make sure the same behaviour as from(Object) is implemented
            out.println("resetChangedOnNotNull()%s", semicolon);

            if (pojoArgument && degree > 0)
                out.println("}");

            out.println("}");
        }
    }

    private boolean isArrayOfUDTs(final TypedElementDefinition<?> t, final JavaTypeResolver r) {

        // [#11183] TODO: Move this to DataTypeDefinition?
        String javaType = t.getType(r).getJavaType(r);
        if (!isArrayType(javaType))
            return false;

        String baseType = getArrayBaseType(javaType);
        for (UDTDefinition udt : t.getDatabase().getUDTs())
            if (baseType.equals(getStrategy().getFullJavaClassName(udt, Mode.RECORD)))
                return true;

        for (TableDefinition table : t.getDatabase().getTables())
            if (baseType.equals(getStrategy().getFullJavaClassName(table, Mode.RECORD)))
                return true;

        return false;
    }

    private String getJavaType(Definition column, JavaWriter out) {
        return getJavaType(column, out, Mode.RECORD);
    }

    private String getJavaType(Definition column, JavaWriter out, Mode mode) {
        return column instanceof EmbeddableDefinition
            ? getStrategy().getFullJavaClassName(column, mode)
            : getJavaType(((TypedElementDefinition<?>) column).getType(resolver(out)), out);
    }

    private String getJavaTypeRef(Definition column, JavaWriter out) {
        return out.ref(getJavaType(column, out));
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
        final String className = getStrategy().getJavaClassName(column.getContainer(), Mode.RECORD);

        // [#12459] Kotlin setters must return Unit
        final String setterReturnType = generateFluentSetters() && !kotlin ? className : tokenVoid;
        final String setter = getStrategy().getJavaSetterName(column, Mode.RECORD);
        final String member = getStrategy().getJavaMemberName(column, Mode.POJO);
        final String typeFull = getJavaType(column.getType(resolver(out)), out);
        final String type = out.ref(typeFull);
        final String name = column.getQualifiedOutputName();
        final boolean isUDT = column.getType(resolver(out)).isUDT();
        final boolean isArray = column.getType(resolver(out)).isArray();
        final boolean isUDTArray = column.getType(resolver(out)).isUDTArray();
        final boolean override = generateInterfaces() && !generateImmutableInterfaces() && !isUDT;

        // We cannot have covariant setters for arrays because of type erasure
        if (!(generateInterfaces() && isArray)) {
            if (!kotlin && !printDeprecationIfUnknownType(out, typeFull))
                out.javadoc("Setter for <code>%s</code>.[[before= ][%s]]", name, list(escapeEntities(comment(column))));

            if (scala) {
                out.println("%sdef %s(value: %s): %s = {", visibility(override), setter, type, setterReturnType);
                out.println("set(%s, value)", index);

                if (generateFluentSetters())
                    out.println("this");

                out.println("}");
            }
            else if (kotlin) {
                out.println();

                if (column instanceof ColumnDefinition)
                    printColumnJPAAnnotation(out, (ColumnDefinition) column);

                printValidationAnnotation(out, column);
                printKotlinSetterAnnotation(out, column, Mode.RECORD);

                out.println("%sopen %svar %s: %s%s", visibility(generateInterfaces()), (generateInterfaces() ? "override " : ""), member, type, kotlinNullability(out, column, Mode.RECORD));
                out.tab(1).println("set(value): %s = set(%s, value)", setterReturnType, index);
            }
            else {
                final String nullableAnnotation = nullableOrNonnullAnnotation(out, column);

                out.overrideIf(override);
                out.println("%s%s %s([[before=@][after= ][%s]]%s value) {", visibility(override), setterReturnType, setter, list(nullableAnnotation), varargsIfArray(type));
                out.println("set(%s, value);", index);

                if (generateFluentSetters())
                    out.println("return this;");

                out.println("}");
            }
        }

        // [#3117] Avoid covariant setters for UDTs when generating interfaces
        if (generateInterfaces() && !generateImmutableInterfaces() && (isUDT || isArray)) {
            final String columnTypeFull = getJavaType(column.getType(resolver(out, Mode.RECORD)), out, Mode.RECORD);
            final String columnType = out.ref(columnTypeFull);
            final String columnTypeInterface = out.ref(getJavaType(column.getType(resolver(out, Mode.INTERFACE)), out, Mode.INTERFACE));

            if (!printDeprecationIfUnknownType(out, columnTypeFull))
                out.javadoc("Setter for <code>%s</code>.[[before= ][%s]]", name, list(escapeEntities(comment(column))));

            out.override();

            if (scala) {
                // [#3082] TODO Handle <interfaces/> + ARRAY also for Scala

                out.println("%sdef %s(value: %s): %s = {", visibility(), setter, columnTypeInterface, setterReturnType);
                out.println("if (value == null)");
                out.println("set(%s, null)", index);
                out.println("else");
                out.println("set(%s, value.into(new %s()))", index, type);

                if (generateFluentSetters())
                    out.println("this");

                out.println("}");
            }

            // TODO
            else if (kotlin) {}
            else {
                final String nullableAnnotation = nullableOrNonnullAnnotation(out, column);

                out.println("%s%s %s([[before=@][after= ][%s]]%s value) {", visibility(), setterReturnType, setter, list(nullableAnnotation), varargsIfArray(columnTypeInterface));
                out.println("if (value == null)");
                out.println("set(%s, null);", index);

                if (isUDT) {
                    out.println("else");
                    out.println("set(%s, value.into(new %s()));", index, type);
                }
                else if (isArray) {
                    final ArrayDefinition array = database.getArray(column.getType(resolver(out)).getSchema(), column.getType(resolver(out)).getQualifiedUserType());
                    final String componentType = out.ref(getJavaType(array.getElementType(resolver(out, Mode.RECORD)), out, Mode.RECORD));
                    final String componentTypeInterface = out.ref(getJavaType(array.getElementType(resolver(out, Mode.INTERFACE)), out, Mode.INTERFACE));

                    out.println("else {");
                    out.println("%s a = new %s();", columnType, columnType);
                    out.println();
                    out.println("for (%s i : value)", componentTypeInterface);

                    if (isUDTArray)
                        out.println("a.add(i.into(new %s()));", componentType);
                    else
                        out.println("a.add(i);", componentType);

                    out.println();
                    out.println("set(1, a);");
                    out.println("}");
                }

                if (generateFluentSetters())
                    out.println("return this;");

                out.println("}");
            }
        }
    }

    /**
     * Subclasses may override this method to provide their own record getters for embeddables.
     */
    protected void generateEmbeddableRecordSetter(EmbeddableDefinition embeddable, int index, JavaWriter out) {
        final String className = getStrategy().getJavaClassName(embeddable.getReferencingTable(), Mode.RECORD);
        final String setterReturnType = generateFluentSetters() ? className : tokenVoid;
        final String member = getStrategy().getJavaMemberName(embeddable, Mode.POJO);
        final String setter = getStrategy().getJavaSetterName(embeddable, Mode.RECORD);
        final String typeFull = getStrategy().getFullJavaClassName(embeddable, generateInterfaces() ? Mode.INTERFACE : Mode.RECORD);
        final String type = out.ref(typeFull);
        final String name = embeddable.getQualifiedOutputName();
        final boolean override = generateInterfaces() && !generateImmutableInterfaces();

        if (!kotlin && !printDeprecationIfUnknownType(out, typeFull))
            out.javadoc("Setter for the embeddable <code>%s</code>.", name);

        if (scala) {
            out.println("%sdef %s(value: %s): %s = {", visibility(override), setter, type, setterReturnType);
        }
        else if (kotlin) {
            out.println();
            out.println("%sopen %svar %s: %s", visibility(generateInterfaces()), (generateInterfaces() ? "override " : ""), member, type);
            out.tab(1).println("set(value): %s {", setterReturnType);
        }
        else {
            out.overrideIf(override);
            out.println("%s%s %s([[before=@][after= ][%s]]%s value) {", visibility(override), setterReturnType, setter, list(nonnullAnnotation(out)), type);
        }

        if (index > -1) {
            if (kotlin)
                out.tab(1).println("set(%s, value)", index);
            else
                out.println("set(%s, value)%s", index, semicolon);
        }
        else {
            for (EmbeddableColumnDefinition column : embeddable.getColumns()) {
                final int position = column.getReferencingColumnPosition() - 1;

                if (kotlin)
                    out.tab(1).println("set(%s, value.%s)",
                        position,
                        getStrategy().getJavaMemberName(column, Mode.POJO)
                    );
                else
                    out.println("set(%s, value.%s%s)%s",
                        position,
                        getStrategy().getJavaGetterName(column, Mode.RECORD),
                        emptyparens,
                        semicolon
                    );
            }
        }

        if (generateFluentSetters())
            if (scala)
                out.println("this");
            else
                out.println("return this%s", semicolon);

        if (kotlin)
            out.tab(1).println("}");
        else
            out.println("}");
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
        final String getter = getStrategy().getJavaGetterName(column, Mode.RECORD);
        final String typeFull = getJavaType(column.getType(resolver(out)), out);
        final String type = out.ref(typeFull);
        final String name = column.getQualifiedOutputName();

        if (!kotlin) {
            if (!printDeprecationIfUnknownType(out, typeFull))
                out.javadoc("Getter for <code>%s</code>.[[before= ][%s]]", name, list(escapeEntities(comment(column))));

            if (column instanceof ColumnDefinition)
                printColumnJPAAnnotation(out, (ColumnDefinition) column);

            printValidationAnnotation(out, column);
        }

        printNullableOrNonnullAnnotation(out, column);
        boolean override = generateInterfaces();

        if (scala) {
            out.println("%sdef %s: %s = get(%s).asInstanceOf[%s]", visibility(override), scalaWhitespaceSuffix(getter), type, index, type);
        }
        else if (kotlin) {
            String nullable = column instanceof EmbeddableDefinition ? "" : kotlinNullability(out, column, Mode.RECORD);
            out.tab(1).println("get(): %s%s = get(%s) as %s%s", type, nullable, index, type, nullable);
        }
        else {
            out.overrideIf(override);
            out.println("%s%s %s() {", visibility(override), type, getter);

            // [#6705] Avoid generating code with a redundant (Object) cast
            if (Object.class.getName().equals(typeFull))
                out.println("return get(%s);", index);
            else
                out.println("return (%s) get(%s);", type, index);

            out.println("}");
        }
    }

    /**
     * Subclasses may override this method to provide their own record getters for embeddables.
     */
    protected void generateEmbeddableRecordGetter(EmbeddableDefinition embeddable, int index, JavaWriter out) {
        final String getter = getStrategy().getJavaGetterName(embeddable, Mode.RECORD);
        final String typeFull = getStrategy().getFullJavaClassName(embeddable, Mode.RECORD);
        final String type = out.ref(typeFull);
        final String declaredType = out.ref(getStrategy().getFullJavaClassName(embeddable, generateInterfaces() ? Mode.INTERFACE : Mode.RECORD));
        final String name = embeddable.getQualifiedOutputName();

        if (!kotlin && !printDeprecationIfUnknownType(out, typeFull))
            out.javadoc("Getter for the embeddable <code>%s</code>.", name);

        boolean override = generateInterfaces();

        if (scala) {
            out.print("%sdef %s: %s = ", visibility(override), scalaWhitespaceSuffix(getter), type);
        }
        else if (kotlin) {
            out.tab(1).print("get(): %s = ", declaredType);
        }
        else {
            out.overrideIf(override);
            out.println("%s%s %s() {", visibility(override), type, getter);
        }

        if (index > -1) {
            if (scala)
                out.println("get(%s).asInstanceOf[%s]", index, type);
            else if (kotlin)
                out.tab(1).println("get(%s) as %s", index, type);
            else {

                // [#6705] Avoid generating code with a redundant (Object) cast
                if (Object.class.getName().equals(typeFull))
                    out.println("return get(%s);", index);
                else
                    out.println("return (%s) get(%s);", type, index);
            }
        }
        else {
            if (scala)
                out.println("new %s(", type);
            else if (kotlin)
                out.tab(1).println("%s(", type);
            else
                out.println("return new %s(", type);

            forEach(embeddable.getColumns(), (column, separator) -> {
                final String columnType = out.ref(getJavaType(column.getReferencingColumn().getType(resolver(out)), out));
                final int position = column.getReferencingColumnPosition() - 1;

                if (scala)
                    out.println("get(%s).asInstanceOf[%s]%s", position, columnType, separator);
                else if (kotlin)
                    out.tab(1).println("get(%s) as %s%s%s", position, columnType, kotlinNullability(out, column, Mode.RECORD), separator);
                else {

                    // [#6705] Avoid generating code with a redundant (Object) cast
                    if (Object.class.getName().equals(typeFull))
                        out.println("get(%s)%s", position, separator);
                    else
                        out.println("(%s) get(%s)%s", columnType, position, separator);
                }
            });

            if (scala)
                out.println(")");
            else if (kotlin)
                out.tab(1).println(")");
            else
                out.println(");");
        }

        if (scala || kotlin) {}
        else
            out.println("}");
    }

    private int colRefSegments(Definition column) {
        if (column instanceof TypedElementDefinition && ((TypedElementDefinition<?>) column).getContainer() instanceof UDTDefinition)
            return 2;

        if (!getStrategy().getInstanceFields())
            return 2;

        return 3;
    }

    private int domainRefSegments() {
        return kotlin ? 1 : 2;
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

    private String refRowType(JavaWriter out, Collection<? extends Definition> columns) {
        return refRowType(out, columns, identity());
    }

    private String refRowType(JavaWriter out, Collection<? extends Definition> columns, Function<String, String> map) {
        StringBuilder result = new StringBuilder();

        forEach(columns, "", ", ", (column, separator) -> {
            result.append(map.apply(getJavaTypeRef(column, out)));

            if (kotlin && !(column instanceof EmbeddableDefinition))
                result.append("?");

            result.append(separator);
        });

        return result.toString();
    }

    protected void generateInterfaces(SchemaDefinition schema) {
        log.info("Generating table interfaces");

        for (TableDefinition table : database.getTables(schema)) {
            try {
                generateInterface(table);
            }
            catch (Exception e) {
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

    protected void generateEmbeddableInterface(EmbeddableDefinition embeddable) {
        JavaWriter out = newJavaWriter(getFile(embeddable, Mode.INTERFACE));
        log.info("Generating interface", out.file().getName());
        generateInterface0(embeddable, out);
        closeJavaWriter(out);
    }

    protected void generateInterface(TableDefinition table, JavaWriter out) {
        generateInterface0(table, out);
    }

    protected void generateUDTInterface(UDTDefinition udt, JavaWriter out) {
        generateInterface0(udt, out);
    }

    private final void generateInterface0(Definition tableUdtOrEmbeddable, JavaWriter out) {
        final String className = getStrategy().getJavaClassName(tableUdtOrEmbeddable, Mode.INTERFACE);
        final List<String> interfaces = out.ref(getStrategy().getJavaClassImplements(tableUdtOrEmbeddable, Mode.INTERFACE));

        printPackage(out, tableUdtOrEmbeddable, Mode.INTERFACE);
        if (tableUdtOrEmbeddable instanceof TableDefinition)
            generateInterfaceClassJavadoc((TableDefinition) tableUdtOrEmbeddable, out);
        else if (tableUdtOrEmbeddable instanceof EmbeddableDefinition)
            generateEmbeddableClassJavadoc((EmbeddableDefinition) tableUdtOrEmbeddable, out);
        else
            generateUDTInterfaceClassJavadoc((UDTDefinition) tableUdtOrEmbeddable, out);

        printClassAnnotations(out, tableUdtOrEmbeddable, Mode.INTERFACE);

        if (tableUdtOrEmbeddable instanceof TableDefinition)
            printTableJPAAnnotation(out, (TableDefinition) tableUdtOrEmbeddable);

        if (scala)
            out.println("%strait %s[[before= extends ][%s]] {", visibility(), className, interfaces);
        else if (kotlin)
            out.println("%sinterface %s[[before= : ][%s]] {", visibility(), className, interfaces);
        else
            out.println("%sinterface %s[[before= extends ][%s]] {", visibility(), className, interfaces);

        List<? extends TypedElementDefinition<?>> typedElements = getTypedElements(tableUdtOrEmbeddable);
        for (int i = 0; i < typedElements.size(); i++) {
            TypedElementDefinition<?> column = typedElements.get(i);

            if (!generateImmutableInterfaces())
                if (tableUdtOrEmbeddable instanceof TableDefinition)
                    generateInterfaceSetter(column, i, out);
                else
                    generateUDTInterfaceSetter(column, i, out);

            if (tableUdtOrEmbeddable instanceof TableDefinition)
                generateInterfaceGetter(column, i, out);
            else
                generateUDTInterfaceGetter(column, i, out);
        }

        if (tableUdtOrEmbeddable instanceof TableDefinition) {
            List<EmbeddableDefinition> embeddables = ((TableDefinition) tableUdtOrEmbeddable).getReferencedEmbeddables();

            for (int i = 0; i < embeddables.size(); i++) {
                EmbeddableDefinition embeddable = embeddables.get(i);

                generateEmbeddableInterfaceSetter(embeddable, i, out);
                generateEmbeddableInterfaceGetter(embeddable, i, out);
            }
        }

        if (!generateImmutableInterfaces()) {
            String local = getStrategy().getJavaClassName(tableUdtOrEmbeddable, Mode.INTERFACE);
            String qualified = out.ref(getStrategy().getFullJavaClassName(tableUdtOrEmbeddable, Mode.INTERFACE));

            out.header("FROM and INTO");

            out.javadoc("Load data from another generated Record/POJO implementing the common interface %s", local);

            if (scala)
                out.println("%sdef from(from: %s): Unit", visibilityPublic(), qualified);
            else if (kotlin)
                out.println("%sfun from(from: %s)", visibilityPublic(), qualified);
            else
                out.println("%svoid from(%s from);", visibilityPublic(), qualified);

            // [#10191] Java and Kotlin can produce overloads for this method despite
            // generic type erasure, but Scala cannot, see
            // https://twitter.com/lukaseder/status/1262652304773259264
            if (scala) {}
            else {
                out.javadoc("Copy data into another generated Record/POJO implementing the common interface %s", local);

                if (kotlin)
                    out.println("%sfun <E : %s> into(into: E): E", visibilityPublic(), qualified);
                else
                    out.println("%s<E extends %s> E into(E into);", visibilityPublic(), qualified);
            }
        }


        if (tableUdtOrEmbeddable instanceof TableDefinition)
            generateInterfaceClassFooter((TableDefinition) tableUdtOrEmbeddable, out);
        else if (tableUdtOrEmbeddable instanceof EmbeddableDefinition)
            generateEmbeddableClassFooter((EmbeddableDefinition) tableUdtOrEmbeddable, out);
        else
            generateUDTInterfaceClassFooter((UDTDefinition) tableUdtOrEmbeddable, out);

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
    protected void generateEmbeddableInterfaceSetter(EmbeddableDefinition embeddable, @SuppressWarnings("unused") int index, JavaWriter out) {
        final String className = getStrategy().getJavaClassName(embeddable.getReferencingTable(), Mode.INTERFACE);
        final String setterReturnType = generateFluentSetters() ? className : tokenVoid;
        final String setter = getStrategy().getJavaSetterName(embeddable, Mode.INTERFACE);
        final String typeFull = getStrategy().getFullJavaClassName(embeddable, Mode.INTERFACE);
        final String type = out.ref(typeFull);

        if (!kotlin && !printDeprecationIfUnknownType(out, typeFull))
            out.javadoc("Setter for <code>%s</code>.", embeddable.getQualifiedOutputName());

        if (scala)
            out.println("%sdef %s(value: %s): %s", visibilityPublic(), setter, type, setterReturnType);
        // The property is already defined in the getter
        else if (kotlin) {}
        else
            out.println("%s%s %s([[before=@][after= ][%s]]%s value);", visibilityPublic(), setterReturnType, setter, list(nonnullAnnotation(out)), type);
    }

    /**
     * Subclasses may override this method to provide their own interface setters.
     */
    protected void generateUDTInterfaceSetter(TypedElementDefinition<?> column, int index, JavaWriter out) {
        generateInterfaceSetter0(column, index, out);
    }

    private final void generateInterfaceSetter0(TypedElementDefinition<?> column, @SuppressWarnings("unused") int index, JavaWriter out) {
        final String className = getStrategy().getJavaClassName(column.getContainer(), Mode.INTERFACE);
        final String setterReturnType = generateFluentSetters() ? className : tokenVoid;
        final String setter = getStrategy().getJavaSetterName(column, Mode.INTERFACE);
        final String typeFull = getJavaType(column.getType(resolver(out, Mode.INTERFACE)), out, Mode.INTERFACE);
        final String type = out.ref(typeFull);
        final String name = column.getQualifiedOutputName();

        if (!kotlin && !printDeprecationIfUnknownType(out, typeFull))
            out.javadoc("Setter for <code>%s</code>.[[before= ][%s]]", name, list(escapeEntities(comment(column))));

        if (scala)
            out.println("%sdef %s(value: %s): %s", visibilityPublic(), setter, type, setterReturnType);
        // The property is already defined in the getter
        else if (kotlin) {}
        else
            out.println("%s%s %s([[before=@][after= ][%s]]%s value);", visibilityPublic(), setterReturnType, setter, list(nullableOrNonnullAnnotation(out, column)), varargsIfArray(type));
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
    protected void generateEmbeddableInterfaceGetter(EmbeddableDefinition embeddable, @SuppressWarnings("unused") int index, JavaWriter out) {

        // TODO: The Mode should be INTERFACE
        final String member = getStrategy().getJavaMemberName(embeddable, Mode.POJO);
        final String getter = getStrategy().getJavaGetterName(embeddable, Mode.INTERFACE);
        final String typeFull = getStrategy().getFullJavaClassName(embeddable, Mode.INTERFACE);
        final String type = out.ref(typeFull);
        final String name = embeddable.getQualifiedOutputName();

        if (!kotlin && !printDeprecationIfUnknownType(out, typeFull))
            out.javadoc("Getter for <code>%s</code>.", name);

        printNonnullAnnotation(out);

        if (scala)
            out.println("%sdef %s: %s", visibilityPublic(), scalaWhitespaceSuffix(getter), type);
        else if (kotlin)
            out.println("%s%s %s: %s", visibilityPublic(), (generateImmutableInterfaces() ? "val" : "var"), member, type);
        else
            out.println("%s%s %s();", visibilityPublic(), type, getter);
    }

    /**
     * Subclasses may override this method to provide their own interface getters.
     */
    protected void generateUDTInterfaceGetter(TypedElementDefinition<?> column, int index, JavaWriter out) {
        generateInterfaceGetter0(column, index, out);
    }

    private final void generateInterfaceGetter0(TypedElementDefinition<?> column, @SuppressWarnings("unused") int index, JavaWriter out) {

        // TODO: The Mode should be INTERFACE
        final String member = getStrategy().getJavaMemberName(column, Mode.POJO);
        final String getter = getStrategy().getJavaGetterName(column, Mode.INTERFACE);
        final String typeFull = getJavaType(column.getType(resolver(out, Mode.INTERFACE)), out, Mode.INTERFACE);
        final String type = out.ref(typeFull);
        final String name = column.getQualifiedOutputName();

        if (!kotlin && !printDeprecationIfUnknownType(out, typeFull))
            out.javadoc("Getter for <code>%s</code>.[[before= ][%s]]", name, list(escapeEntities(comment(column))));

        if (column instanceof ColumnDefinition)
            printColumnJPAAnnotation(out, (ColumnDefinition) column);

        printValidationAnnotation(out, column);
        printNullableOrNonnullAnnotation(out, column);
        if (kotlin)
            printKotlinSetterAnnotation(out, column, Mode.INTERFACE);

        if (scala)
            out.println("%sdef %s: %s", visibilityPublic(), scalaWhitespaceSuffix(getter), type);
        else if (kotlin)
            out.println("%s%s %s: %s%s", visibilityPublic(), (generateImmutableInterfaces() ? "val" : "var"), member, type, kotlinNullability(out, column, Mode.INTERFACE));
        else
            out.println("%s%s %s();", visibilityPublic(), type, getter);
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
            }
            catch (Exception e) {
                log.error("Error while generating udt " + udt, e);
            }
        }

        watch.splitInfo("UDTs generated");
    }

    @SuppressWarnings("unused")
    protected void generateUDT(SchemaDefinition schema, UDTDefinition udt) {
        JavaWriter out = newJavaWriter(getFile(udt));
        out.refConflicts(getStrategy().getJavaIdentifiers(udt.getAttributes()));

        log.info("Generating UDT ", out.file().getName());

        if (log.isDebugEnabled())
            for (AttributeDefinition attribute : udt.getAttributes())
                log.debug("With attribute", "name=" + attribute.getOutputName() + ", matching type names=" + attribute.getDefinedType().getMatchNames());

        generateUDT(udt, out);
        closeJavaWriter(out);
    }

    protected void generateUDT(UDTDefinition udt, JavaWriter out) {
        final SchemaDefinition schema = udt.getSchema();
        final PackageDefinition pkg = udt.getPackage();
        final boolean synthetic = udt.isSynthetic();
        final String className = getStrategy().getJavaClassName(udt);
        final String recordType = out.ref(getStrategy().getFullJavaClassName(udt, Mode.RECORD));
        final String classExtends = out.ref(getStrategy().getJavaClassExtends(udt, Mode.DEFAULT));
        final List<String> interfaces = out.ref(getStrategy().getJavaClassImplements(udt, Mode.DEFAULT));
        final String schemaId = generateDefaultSchema(schema)
            ? out.ref(getStrategy().getFullJavaIdentifier(schema), 2)
            : null;
        final String packageId = pkg == null ? null : out.ref(getStrategy().getFullJavaIdentifier(pkg), 2);
        final String udtId = out.ref(getStrategy().getJavaIdentifier(udt), 2);

        printPackage(out, udt);

        if (scala) {
            out.println("object %s {", className);
            printSingletonInstance(out, udt);

            for (AttributeDefinition attribute : udt.getAttributes()) {
                final String attrId = out.ref(getStrategy().getJavaIdentifier(attribute), 2);

                out.javadoc("The attribute <code>%s</code>.[[before= ][%s]]", attribute.getQualifiedOutputName(), list(escapeEntities(comment(attribute))));
                out.println("val %s = %s.%s", attrId, udtId, attrId);
            }

            out.println("}");
            out.println();
        }

        generateUDTClassJavadoc(udt, out);
        printClassAnnotations(out, udt, Mode.DEFAULT);







        if (scala) {
            out.println("%sclass %s extends %s[%s](\"%s\", null, %s, %s)[[before= with ][separator= with ][%s]] {", visibility(), className, classExtends, recordType, escapeString(udt.getOutputName()), packageId, synthetic, interfaces);
        }
        else if (kotlin) {
            out.println("%sopen class %s : %s<%s>(\"%s\", null, %s, %s)[[before=, ][%s]] {", visibility(), className, classExtends, recordType, escapeString(udt.getOutputName()), packageId, synthetic, interfaces);

            out.println();
            out.println("public companion object {");
            out.javadoc("The reference instance of <code>%s</code>", udt.getQualifiedOutputName());
            out.println("public val %s: %s = %s()", getStrategy().getJavaIdentifier(udt), className, className);
            out.println("}");
        }
        else {
            out.println("%sclass %s extends %s<%s>[[before= implements ][%s]] {", visibility(), className, classExtends, recordType, interfaces);
            out.printSerial();
            printSingletonInstance(out, udt);
        }

        printRecordTypeMethod(out, udt);

        for (AttributeDefinition attribute : udt.getAttributes()) {
            final String attrTypeFull = getJavaType(attribute.getType(resolver(out)), out);
            final String attrType = out.ref(attrTypeFull);
            final String attrTypeRef = getJavaTypeReference(attribute.getDatabase(), attribute.getType(resolver(out)), out);
            final String attrId = out.ref(getStrategy().getJavaIdentifier(attribute), 2);
            final String attrName = attribute.getName();
            final List<String> converter = out.ref(list(attribute.getType(resolver(out)).getConverter()));
            final List<String> binding = out.ref(list(attribute.getType(resolver(out)).getBinding()));

            if (!printDeprecationIfUnknownType(out, attrTypeFull))
                out.javadoc("The attribute <code>%s</code>.[[before= ][%s]]", attribute.getQualifiedOutputName(), list(escapeEntities(comment(attribute))));

            if (scala)
                out.println("private val %s: %s[%s, %s] = %s.createField(%s.name(\"%s\"), %s, this, \"%s\"" + converterTemplate(converter) + converterTemplate(binding) + ")",
                    scalaWhitespaceSuffix(attrId), UDTField.class, recordType, attrType, UDTImpl.class, DSL.class, escapeString(attrName), attrTypeRef, escapeString(""), converter, binding);
            else if (kotlin)
                out.println("%sval %s: %s<%s, %s> = %s.createField(%s.name(\"%s\"), %s, this, \"%s\"" + converterTemplate(converter) + converterTemplate(binding) + ")",
                    visibility(), attrId, UDTField.class, recordType, attrType, UDTImpl.class, DSL.class, escapeString(attrName), attrTypeRef, escapeString(""), converter, binding);
            else
                out.println("%sstatic final %s<%s, %s> %s = createField(%s.name(\"%s\"), %s, %s, \"%s\"" + converterTemplate(converter) + converterTemplate(binding) + ");",
                    visibility(), UDTField.class, recordType, attrType, attrId, DSL.class, escapeString(attrName), attrTypeRef, udtId, escapeString(""), converter, binding);
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
                    if (!routine.isAggregate())
                        printConvenienceMethodFunction(out, routine, false);

                    // Static asField() convenience method
                    printConvenienceMethodFunctionAsField(out, routine, false);
                    printConvenienceMethodFunctionAsField(out, routine, true);
                }
            }
            catch (Exception e) {
                log.error("Error while generating routine " + routine, e);
            }
        }

        if (scala || kotlin) {
        }
        else {
            out.javadoc(NO_FURTHER_INSTANCES_ALLOWED);
            out.println("private %s() {", className);
            out.println("super(\"%s\", null, %s, %s);", udt.getOutputName(), packageId, synthetic);
            out.println("}");
        }

        if (schemaId != null) {
            if (scala) {
                out.println();
                out.println("%soverride def getSchema: %s = %s", visibilityPublic(), Schema.class, schemaId);
            }
            else if (kotlin) {
                out.println();
                out.println("%s override fun getSchema(): %s = %s", visibilityPublic(), Schema.class, schemaId);
            }
            else {
                out.overrideInherit();
                out.println("%s%s getSchema() {", visibilityPublic(), Schema.class);
                out.println("return %s != null ? %s : new %s(%s.name(\"%s\"));", schemaId, schemaId, SchemaImpl.class, DSL.class, schema.getOutputName());
                out.println("}");
            }
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
            }
            catch (Exception e) {
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
            }
            catch (Exception e) {
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
                        }
                        catch (Exception e) {
                            log.error("Error while generating member routines " + routine, e);
                        }
                    }
                }
                catch (Exception e) {
                    log.error("Error while generating UDT " + udt, e);
                }

                watch.splitInfo("Member procedures routines");
            }
        }
    }

    /**
     * Generating central static udt access
     */
    protected void generateUDTReferences(Definition schemaOrPackage) {
        String logSuffix = schemaOrPackage instanceof SchemaDefinition ? "" : (" for package " + schemaOrPackage.getOutputName());
        log.info("Generating UDT references" + logSuffix);
        JavaWriter out = newJavaWriter(getStrategy().getGlobalReferencesFile(schemaOrPackage, UDTDefinition.class));
        printGlobalReferencesPackage(out, schemaOrPackage, UDTDefinition.class);

        if (!kotlin) {
            printClassJavadoc(out, "Convenience access to all UDTs in " + schemaNameOrDefault(schemaOrPackage) + ".");
            printClassAnnotations(out, schemaOrPackage, Mode.DEFAULT);
        }

        final String referencesClassName = getStrategy().getGlobalReferencesJavaClassName(schemaOrPackage, UDTDefinition.class);

        if (scala)
            out.println("%sobject %s {", visibility(), referencesClassName);
        else if (kotlin) {}
        else
            out.println("%sclass %s {", visibility(), referencesClassName);

        List<UDTDefinition> udts = new ArrayList<>();

        if (schemaOrPackage instanceof SchemaDefinition) {
            for (UDTDefinition udt : database.getUDTs((SchemaDefinition) schemaOrPackage))
                if (udt.getPackage() == null)
                    udts.add(udt);
        }
        else
            udts.addAll(database.getUDTs((PackageDefinition) schemaOrPackage));

        for (UDTDefinition udt : udts) {
            final String className = out.ref(getStrategy().getFullJavaClassName(udt));
            final String id = getStrategy().getJavaIdentifier(udt);
            final String fullId = getStrategy().getFullJavaIdentifier(udt);

            out.javadoc("The type <code>%s</code>", udt.getQualifiedOutputName());

            if (scala)
                out.println("%sdef %s = %s", visibility(), id, fullId);
            else if (kotlin)
                out.println("%sval %s: %s = %s", visibility(), id, className, fullId);
            else
                out.println("%sstatic final %s %s = %s;", visibility(), className, id, fullId);
        }

        generateUDTReferencesClassFooter(schemaOrPackage, out);

        if (!kotlin)
            out.println("}");
        closeJavaWriter(out);

        watch.splitInfo("UDT references generated" + logSuffix);

        if (schemaOrPackage instanceof SchemaDefinition)
            for (PackageDefinition pkg : database.getPackages((SchemaDefinition) schemaOrPackage))
                if (!pkg.getUDTs().isEmpty())
                    generateUDTReferences(pkg);
    }

    /**
     * Subclasses may override this method to provide UDT references class footer code.
     */
    @SuppressWarnings("unused")
    protected void generateUDTReferencesClassFooter(Definition schemaOrPackage, JavaWriter out) {}

    /**
     * Subclasses may override this method to provide UDT references class footer code.
     */
    @SuppressWarnings("unused")
    protected void generateUDTReferencesClassFooter(PackageDefinition pkg, JavaWriter out) {}

    /**
     * Generating central static domain access
     */
    protected void generateDomainReferences(SchemaDefinition schema) {
        log.info("Generating DOMAIN references");
        JavaWriter out = newJavaWriter(getStrategy().getGlobalReferencesFile(schema, DomainDefinition.class));
        out.refConflicts(getStrategy().getJavaIdentifiers(database.getDomains(schema)));

        printGlobalReferencesPackage(out, schema, DomainDefinition.class);

        final String schemaId = generateDefaultSchema(schema)
            ? out.ref(getStrategy().getFullJavaIdentifier(schema), 2)
            : null;

        if (!kotlin) {
            printClassJavadoc(out, "Convenience access to all Domains in " + schemaNameOrDefault(schema) + ".");
            printClassAnnotations(out, schema, Mode.DOMAIN);
        }

        final String referencesClassName = getStrategy().getGlobalReferencesJavaClassName(schema, DomainDefinition.class);

        if (scala)
            out.println("%sobject %s {", visibility(), referencesClassName);
        else if (kotlin) {}
        else
            out.println("%sclass %s {", visibility(), referencesClassName);

        for (DomainDefinition domain : database.getDomains(schema)) {
            final String id = getStrategy().getJavaIdentifier(domain);
            final String domainTypeFull = getJavaType(domain.getType(resolver(out)), out);
            final String domainType = out.ref(domainTypeFull);
            final String domainTypeRef = getJavaTypeReference(domain.getDatabase(), domain.getType(resolver(out)), out);
            final List<String> converter = out.ref(list(domain.getType(resolver(out)).getConverter()));
            final List<String> binding = out.ref(list(domain.getType(resolver(out)).getBinding()));

            out.javadoc("The domain <code>%s</code>.", domain.getQualifiedOutputName());

            if (scala) {
                out.println("%sval %s: %s[%s] = %s.createDomain(", visibility(), scalaWhitespaceSuffix(id), Domain.class, domainType, Internal.class);
                out.println("  %s", schemaId != null ? "schema" : null);
                out.println(", %s.name(\"%s\")", DSL.class, escapeString(domain.getOutputName()));
                out.println(", %s", domainTypeRef);
                if (!converter.isEmpty() || !binding.isEmpty())
                    out.println(converterTemplate(converter) + converterTemplate(binding), converter, binding);

                for (String check : domain.getCheckClauses())
                    out.println(", %s.createCheck(null, null, \"%s\")", Internal.class, escapeString(check));

                out.println(")");
            }
            else if (kotlin) {
                out.println("%sval %s: %s<%s> = %s.createDomain(", visibility(), id, Domain.class, domainType, Internal.class);
                out.println("  %s", schemaId != null ? "schema()" : null);
                out.println(", %s.name(\"%s\")", DSL.class, escapeString(domain.getOutputName()));
                out.println(", %s", domainTypeRef);
                if (!converter.isEmpty() || !binding.isEmpty())
                    out.println(converterTemplate(converter) + converterTemplate(binding), converter, binding);

                for (String check : domain.getCheckClauses())
                    out.println(", %s.createCheck<%s>(null, null, \"%s\")", Internal.class, Record.class, escapeString(check));

                out.println(")");
            }
            else {
                //
                out.println("%sstatic final %s<%s> %s = %s.createDomain(", visibility(), Domain.class, domainType, id, Internal.class);
                out.println("  %s", schemaId != null ? "schema()" : null);
                out.println(", %s.name(\"%s\")", DSL.class, escapeString(domain.getOutputName()));
                out.println(", %s", domainTypeRef);
                if (!converter.isEmpty() || !binding.isEmpty())
                    out.println(converterTemplate(converter) + converterTemplate(binding), converter, binding);

                for (String check : domain.getCheckClauses())
                    out.println(", %s.createCheck(null, null, \"%s\")", Internal.class, escapeString(check));

                out.println(");");
            }
        }

        if (schemaId != null) {
            if (scala) {
                out.println();
                out.println("private def schema: %s = new %s(%s.name(\"%s\"), %s.comment(\"\"), () => %s)", Schema.class, LazySchema.class, DSL.class, escapeString(schema.getOutputName()), DSL.class, schemaId);
            }
            else if (kotlin) {
                out.println();
                out.println("private fun schema(): %s = %s(%s.name(\"%s\"), %s.comment(\"\"), %s { %s })", Schema.class, LazySchema.class, DSL.class, escapeString(schema.getOutputName()), DSL.class, LazySupplier.class, schemaId);
            }
            else {
                out.println();
                out.println("private static final %s schema() {", Schema.class);
                out.println("return new %s(%s.name(\"%s\"), %s.comment(\"\"), () -> %s);", LazySchema.class, DSL.class, escapeString(schema.getOutputName()), DSL.class, schemaId);
                out.println("}");
            }
        }

        generateDomainReferencesClassFooter(schema, out);

        if (!kotlin)
            out.println("}");
        closeJavaWriter(out);

        watch.splitInfo("DOMAIN references generated");
    }

    /**
     * Subclasses may override this method to provide domain references class footer code.
     */
    @SuppressWarnings("unused")
    protected void generateDomainReferencesClassFooter(SchemaDefinition schema, JavaWriter out) {}

    protected void generateArrays(SchemaDefinition schema) {
        log.info("Generating ARRAYs");

        for (ArrayDefinition array : database.getArrays(schema)) {
            try {
                generateArray(schema, array);
            }
            catch (Exception e) {
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
            }
            catch (Exception ex) {
                log.error("Error while generating enum " + e, ex);
            }
        }

        watch.splitInfo("Enums generated");
    }

    /**
     * @deprecated - [#681] - 3.14.0 - This method is no longer being called
     */
    @Deprecated
    protected void generateDomains(SchemaDefinition schema) {}

    protected void generateEnum(EnumDefinition e) {
        boolean s = scala;
        Language l = language;

        try {
            if (!generateEnumsAsScalaSealedTraits()) {
                scala = false;
                language = l == SCALA ? JAVA : l;
                getStrategy().setTargetLanguage(language);
            }

            JavaWriter out = newJavaWriter(getFile(e, Mode.ENUM));
            log.info("Generating ENUM", out.file().getName());
            generateEnum(e, out);
            closeJavaWriter(out);
        }
        finally {
            scala = s;
            language = l;
            getStrategy().setTargetLanguage(language);
        }
    }

    protected void generateEnum(EnumDefinition e, JavaWriter out) {
        final String className = getStrategy().getJavaClassName(e, Mode.ENUM);
        final List<String> interfaces = out.ref(getStrategy().getJavaClassImplements(e, Mode.ENUM));
        final List<String> literals = e.getLiterals();
        final List<String> identifiers = getStrategy().getJavaEnumLiterals(e, literals);

        printPackage(out, e);
        generateEnumClassJavadoc(e, out);
        printClassAnnotations(out, e, Mode.ENUM);

        boolean enumHasNoSchema = e.isSynthetic();
        boolean noSchema = enumHasNoSchema || !generateDefaultSchema(e.getSchema());
        boolean noCatalog = enumHasNoSchema || noSchema || !generateDefaultCatalog(e.getCatalog());

        if (scala) {
            out.println("object %s {", className);
            out.println();

            for (String identifier : identifiers)
                out.println("val %s: %s = %s.%s", scalaWhitespaceSuffix(identifier), className, getStrategy().getJavaPackageName(e), identifier);

            out.println();
            out.println("def values: %s[%s] = %s(",
                out.ref("scala.Array"),
                className,
                out.ref("scala.Array"));

            for (int i = 0; i < identifiers.size(); i++) {
                out.print((i > 0 ? ", " : "  "));
                out.println(identifiers.get(i));
            }

            out.println(")");
            out.println();

            out.println("def valueOf(s: %s): %s = s match {", String.class, className);
            for (int i = 0; i < identifiers.size(); i++) {
                out.println("case \"%s\" => %s", literals.get(i), identifiers.get(i));
            }
            out.println("case _ => throw new %s()", IllegalArgumentException.class);
            out.println("}");
            out.println("}");

            out.println();
            out.println("sealed trait %s extends %s[[before= with ][separator= with ][%s]] {", className, EnumType.class, interfaces);

            if (noCatalog)
                out.println("override def getCatalog: %s = null", Catalog.class);
            else
                out.println("override def getCatalog: %s = if (getSchema == null) null else getSchema().getCatalog()", Catalog.class);

            // [#2135] Only the PostgreSQL database supports schema-scoped enum types
            out.println("override def getSchema: %s = %s",
                Schema.class,
                noSchema
                    ? "null"
                    : out.ref(getStrategy().getFullJavaIdentifier(e.getSchema()), 2));
            out.println("override def getName: %s = %s",
                String.class,
                e.isSynthetic() ? "null" : "\"" + escapeString(e.getName()) + "\"");

            generateEnumClassFooter(e, out);
            out.println("}");

            for (int i = 0; i < literals.size(); i++) {
                out.println();
                out.println("case object %s extends %s {", identifiers.get(i), className);
                out.println("override def getLiteral: %s = \"%s\"",
                    String.class,
                    literals.get(i));
                out.println("}");
            }
        }
        else if (kotlin) {
            interfaces.add(out.ref(EnumType.class));
            out.println("%senum class %s(@get:JvmName(\"literal\") public val literal: String)[[before= : ][%s]] {", visibility(), className, interfaces);

            for (int i = 0; i < literals.size(); i++)
                out.println("%s(\"%s\")%s", identifiers.get(i), literals.get(i), (i == literals.size() - 1) ? ";" : ",");

            out.println("%soverride fun getCatalog(): %s? = %s",
                visibilityPublic(), Catalog.class, noCatalog ? "null" : "schema.catalog");

            // [#2135] Only the PostgreSQL database supports schema-scoped enum types
            out.println("%soverride fun getSchema(): %s%s = %s",
                visibilityPublic(), Schema.class, noSchema ? "?" : "", noSchema ? "null" : out.ref(getStrategy().getFullJavaIdentifier(e.getSchema()), 2));

            out.println("%soverride fun getName(): %s%s = %s",
                visibilityPublic(), String.class, e.isSynthetic() ? "?" : "", e.isSynthetic() ? "null" : "\"" + escapeString(e.getName()) + "\"");

            out.println("%soverride fun getLiteral(): String = literal", visibilityPublic());

            generateEnumClassFooter(e, out);
            out.println("}");
        }
        else {
            interfaces.add(out.ref(EnumType.class));
            out.println("%senum %s[[before= implements ][%s]] {", visibilityPublic(), className, interfaces);

            for (int i = 0; i < literals.size(); i++) {
                out.println();
                out.println("%s(\"%s\")%s", identifiers.get(i), literals.get(i), (i == literals.size() - 1) ? ";" : ",");
            }

            out.println();
            out.println("private final %s literal;", String.class);
            out.println();
            out.println("private %s(%s literal) {", className, String.class);
            out.println("this.literal = literal;");
            out.println("}");

            out.overrideInherit();
            out.println("%s%s getCatalog() {", visibilityPublic(), Catalog.class);

            if (noCatalog)
                out.println("return null;");
            else
                out.println("return getSchema().getCatalog();");

            out.println("}");

            // [#2135] Only the PostgreSQL database supports schema-scoped enum types
            out.overrideInherit();
            out.println("%s%s getSchema() {", visibilityPublic(), Schema.class);

            // [#10998] The ScalaGenerator's schema reference is a method
            if (scalaConfigured) {
                out.println("return %s%s;",
                    noSchema ? "null" : getStrategy().getFullJavaIdentifier(e.getSchema()).replaceFirst("^(.*)\\.(.*?)$", "$1\\$.MODULE\\$.$2"),
                    noSchema ? "" : "()"
                );
            }
            else {
                out.println("return %s;",
                    noSchema ? "null" : out.ref(getStrategy().getFullJavaIdentifier(e.getSchema()), 2));
            }
            out.println("}");

            out.overrideInherit();
            out.println("%s%s getName() {", visibilityPublic(), String.class);
            out.println("return %s;", e.isSynthetic() ? "null" : "\"" + escapeString(e.getName()) + "\"");
            out.println("}");

            out.overrideInherit();
            out.println("%s%s getLiteral() {", visibilityPublic(), String.class);
            out.println("return literal;");
            out.println("}");

            out.javadoc("Lookup a value of this EnumType by its literal. Returns <code>null</code>, if no such value could be found, see {@link %s#lookupLiteral(Class, String)}.", EnumType.class);
            printNullableAnnotation(out);
            out.println("%sstatic %s lookupLiteral(%s literal) {", visibilityPublic(), className, String.class);
            out.println("return %s.lookupLiteral(%s.class, literal);", EnumType.class, className);
            out.println("}");

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

    /**
     * @deprecated - [#681] - 3.14.0 - This method is no longer being called
     */
    @Deprecated
    protected void generateDomain(DomainDefinition d) {}

    /**
     * @deprecated - [#681] - 3.14.0 - This method is no longer being called
     */
    @Deprecated
    protected void generateDomain(DomainDefinition d, JavaWriter out) {}

    /**
     * @deprecated - [#681] - 3.14.0 - This method is no longer being called
     */
    @Deprecated
    @SuppressWarnings("unused")
    protected void generateDomainClassFooter(DomainDefinition d, JavaWriter out) {}

    /**
     * @deprecated - [#681] - 3.14.0 - This method is no longer being called
     */
    @Deprecated
    protected void generateDomainClassJavadoc(DomainDefinition e, JavaWriter out) {}

    protected void generateRoutines(SchemaDefinition schema) {
        log.info("Generating routines and table-valued functions");

        for (RoutineDefinition routine : database.getRoutines(schema)) {
            try {
                generateRoutine(schema, routine);
            }
            catch (Exception e) {
                log.error("Error while generating routine " + routine, e);
            }
        }

        if (generateGlobalRoutineReferences()) {
            JavaWriter out = newJavaWriter(getStrategy().getGlobalReferencesFile(schema, RoutineDefinition.class));
            printGlobalReferencesPackage(out, schema, RoutineDefinition.class);

            if (!kotlin) {
                printClassJavadoc(out, "Convenience access to all stored procedures and functions in " + schemaNameOrDefault(schema) + ".");
                printClassAnnotations(out, schema, Mode.DEFAULT);
            }

            final String referencesClassName = getStrategy().getGlobalReferencesJavaClassName(schema, RoutineDefinition.class);

            if (scala)
                out.println("%sobject %s {", visibility(), referencesClassName);
            else if (kotlin) {}
            else
                out.println("%sclass %s {", visibility(), referencesClassName);

            for (RoutineDefinition routine : database.getRoutines(schema))
                printRoutine(out, routine);

            for (TableDefinition table : database.getTables(schema))
                if (table.isTableValuedFunction())
                    printTableValuedFunction(out, table, getStrategy().getJavaMethodName(table, Mode.DEFAULT));

            generateRoutinesClassFooter(schema, out);

            if (!kotlin)
                out.println("}");
            closeJavaWriter(out);
        }

        watch.splitInfo("Routines generated");
    }

    /**
     * Subclasses may override this method to provide routine references class footer code.
     */
    @SuppressWarnings("unused")
    protected void generateRoutinesClassFooter(SchemaDefinition schema, JavaWriter out) {}

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
            if (!routine.isAggregate())
                printConvenienceMethodFunction(out, routine, false);

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
        JavaWriter out = newJavaWriter(getStrategy().getGlobalReferencesFile(schema, TableDefinition.class));
        printGlobalReferencesPackage(out, schema, TableDefinition.class);

        if (!kotlin) {
            printClassJavadoc(out, "Convenience access to all tables in " + schemaNameOrDefault(schema) + ".");
            printClassAnnotations(out, schema, Mode.DEFAULT);
        }

        final String referencesClassName = getStrategy().getGlobalReferencesJavaClassName(schema, TableDefinition.class);

        if (scala)
            out.println("%sobject %s {", visibility(), referencesClassName);
        else if (kotlin) {}
        else
            out.println("%sclass %s {", visibility(), referencesClassName);

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
            final String comment = escapeEntities(comment(table));

            out.javadoc(isBlank(comment) ? "The table <code>" + table.getQualifiedOutputName() + "</code>." : comment);

            if (scala)
                out.println("%sdef %s = %s", visibility(), id, referencedId);
            else if (kotlin)
                out.println("%sval %s: %s = %s", visibility(), id, fullClassName, referencedId);
            else
                out.println("%sstatic final %s %s = %s;", visibility(), fullClassName, id, referencedId);

            // [#3797] Table-valued functions generate two different literals in
            // globalObjectReferences
            if (table.isTableValuedFunction())
                printTableValuedFunction(out, table, getStrategy().getJavaIdentifier(table));
        }

        generateTableReferencesClassFooter(schema, out);

        if (!kotlin)
            out.println("}");

        closeJavaWriter(out);

        watch.splitInfo("Table refs generated");
    }

    /**
     * Subclasses may override this method to provide table references class footer code.
     */
    @SuppressWarnings("unused")
    protected void generateTableReferencesClassFooter(SchemaDefinition schema, JavaWriter out) {}

    private String schemaNameOrDefault(Definition schema) {
        return StringUtils.isEmpty(schema.getOutputName()) ? "the default schema" : schema.getOutputName();
    }

    protected void generateSyntheticDaos(SchemaDefinition schema) {
        log.info("Generating synthetic DAOs");
        log.warn("Experimental", "Synthetic daos are experimental functionality and subject to change.");

        for (SyntheticDaoType dao : database.getConfiguredSyntheticDaos()) {
            if (schema.getQualifiedInputNamePart().equals(name(dao.getCatalog(), dao.getSchema()))) {
                try {
                    generateSyntheticDao(new DefaultSyntheticDaoDefinition(database, schema, dao));
                }
                catch (Exception e) {
                    log.error("Error while generating synthetic DAO " + dao, e);
                }
            }
        }

        watch.splitInfo("Synthetic DAOs generated");
    }

    protected void generateSyntheticDao(DefaultSyntheticDaoDefinition dao) {
        JavaWriter out = newJavaWriter(getFile(dao, Mode.SYNTHETIC_DAO));
        log.info("Generating Synthetic DAO", out.file().getName());
        generateSyntheticDao(dao, out);
        closeJavaWriter(out);
    }

    protected void generateSyntheticDao(SyntheticDaoDefinition dao, JavaWriter out) {
        final String className = getStrategy().getJavaClassName(dao, Mode.SYNTHETIC_DAO);
        final List<String> interfaces = out.ref(getStrategy().getJavaClassImplements(dao, Mode.SYNTHETIC_DAO));
        final Parser parser = database
            .create()
            .configuration()
            .derive(() -> database.create().meta())
            .deriveSettings(s -> s
                .withParseSearchPath(new ParseSearchSchema().withCatalog(dao.getDao().getCatalog()).withSchema(dao.getDao().getSchema()))
                .withParseWithMetaLookups(ParseWithMetaLookups.THROW_ON_FAILURE))
            .dsl()
            .parser();

        printPackage(out, dao, Mode.SYNTHETIC_DAO);
        generateSyntheticDaoClassJavadoc(dao, out);
        printClassAnnotations(out, dao, Mode.SYNTHETIC_DAO);

        if (generateSpringAnnotations())
            out.println("@%s", out.ref("org.springframework.stereotype.Repository"));

        if (scala) {}
        else if (kotlin) {}
        else
            out.println("%sclass %s[[before= implements ][%s]] {",
                visibility(), className, interfaces);

        // Initialising constructor
        // ------------------------

        out.println();
        out.println("final %s dsl;", DSLContext.class);

        if (!scala && !kotlin) {
            out.javadoc("Create a new %s with an attached configuration", className);

            printDaoConstructorAnnotations(dao, out);
            out.println("%s%s(%s configuration) {", visibility(), className, Configuration.class);
            out.println("this.dsl = configuration.dsl();");
            out.println("}");
        }

        for (SyntheticDaoMethodType method : dao.getDao().getMethods()) {
            String returnType = out.ref(method.getReturnType());
            String recordType = "?";

            // TODO: Parser error handling (don't fail all after only one failure)
            Query query = parser.parseQuery(method.getSql());

            // TODO: ResultQuery (e.g. RETURNING clause)
            if (query instanceof Select<?> select) {
                final String namespace = StringUtils.toUC(method.getName(), getTargetLocale());
                final List<Field<?>> fields = select.getSelect();
                final TableDefinition table = new DefaultMetaTableDefinition(dao.getSchema(), new TableImpl<Record>("t") {{
                    for (Field<?> field : fields)
                        createField(field.getUnqualifiedName(), field.getDataType());
                }});
                // TODO: Other data types
                // TODO: Single results
                // TODO: Reactive execution
                if (returnType.startsWith("List<")) {
                    out.ref(List.class);
                    recordType = returnType.replaceFirst("^List<(.*)>$", "$1");
                }
                else if (returnType.startsWith("Set<")) {
                    out.ref(Set.class);
                    recordType = returnType.replaceFirst("^Set<(.*)>$", "$1");
                }

                // TODO: Error
                else
                    ;

                if (!"?".equals(recordType)) {
                    out.javadoc("Result type for {@link #%s()}", method.getName());
                    out.println("public static final class %s {", namespace);

                    // TODO: Reuse these POJOs?
                    // TODO: Support also non-records
                    out.println("public record %s(", recordType);

                    forEach(fields, (field, separator) -> {
                        out.println("%s %s%s",
                            out.ref(field.getType()),
                            getStrategy().getJavaMemberName(table.getColumn(field.getName(), true)),
                            separator
                        );
                    });

                    out.println(") {}");
                    out.println("}");

                    returnType = returnType.replace(recordType, namespace + "." + recordType);
                    recordType = namespace + "." + recordType;
                }
            }
            else {
                // TODO: Other acceptable types?
                if (!returnType.matches("void|int|long|Integer|Long")) {

                    // TODO: Error
                    returnType = "void";
                }
            }

            if (method.getComment() != null)
                out.javadoc(method.getComment());
            else
                out.println();

            List<String> bindDeclarations = new ArrayList<>();
            List<String> bindNames = new ArrayList<>();

            // TODO: Multiple occurrences of the same bind value
            for (Entry<String, Param<?>> param : query.getParams().entrySet()) {
                String key = param.getKey();

                if (Character.isDigit(key.charAt(0)))
                    key = "_" + key;

                bindNames.add(key);
                bindDeclarations.add(out.ref(param.getValue().getType()) + " " + key);
            }

            out.println("public %s %s([[%s]]) {", returnType, method.getName(), bindDeclarations);
            out.println("return dsl");
            // TODO: Parse the query and cache it
            out.tab(1).println(".resultQuery(%s[[before=, ][%s]])", textBlock(method.getSql()), bindNames);
            // TODO: Type safe mapping
            out.tab(1).println(".fetchInto(%s.class);", recordType);
            out.println("}");
        }

        generateSyntheticDaoClassFooter(dao, out);
        out.println("}");
    }

    /**
     * Subclasses may override this method to provide alternative DAO
     * constructor annotations, such as DI annotations. [#10801]
     */
    protected void printDaoConstructorAnnotations(SyntheticDaoDefinition dao, JavaWriter out) {
        if (generateSpringAnnotations())
            out.println("@%s", out.ref("org.springframework.beans.factory.annotation.Autowired"));
    }

    /**
     * Subclasses may override this method to provide dao class footer code.
     */
    @SuppressWarnings("unused")
    protected void generateSyntheticDaoClassFooter(SyntheticDaoDefinition dao, JavaWriter out) {}

    /**
     * Subclasses may override this method to provide their own Javadoc.
     */
    protected void generateSyntheticDaoClassJavadoc(SyntheticDaoDefinition dao, JavaWriter out) {
        if (generateCommentsOnTables())
            printClassJavadoc(out, dao);
        else
            printClassJavadoc(out, "The synthetic DAO <code>" + dao.getQualifiedInputName() + "</code>.");
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

    protected void generateSpringDao(CatalogDefinition catalog) {
        // [#10756] Optionally, this class could be implemented in a new jooq-spring-extensions module
        JavaWriter out = newJavaWriter(new File(getStrategy().getFile(catalog).getParentFile(), "AbstractSpringDAOImpl.java"));
        log.info("Generating AbstractSpringDAOImpl", out.file().getName());
        generateSpringDao(catalog, out);
        closeJavaWriter(out);
    }

    protected void generateSpringDao(CatalogDefinition catalog, JavaWriter out) {
        printPackage(out, catalog);

        printClassJavadoc(out, "Spring specific {@link " + out.ref(DAOImpl.class) + "} override.");
        printClassAnnotations(out, catalog, Mode.DEFAULT);

        String transactional = generateSpringAnnotations()
            ? out.ref("org.springframework.transaction.annotation.Transactional")
            : null;
        String className = "AbstractSpringDAOImpl";

        if (scala) {
            if (generateSpringAnnotations())
                out.println("@%s(readOnly = true)", transactional);

            out.println("%sabstract class %s[R <: %s[R], P, T](table: %s[R], klass: java.lang.Class[P], configuration: %s) extends %s[R, P, T](table, klass, configuration) {",
                visibility(), className, UpdatableRecord.class, Table.class, Configuration.class, DAOImpl.class);

            out.println();
            out.println("%sdef this(table: %s[R], klass: java.lang.Class[P]) = this(table, klass, null)", visibility(), Table.class);
        }
        else if (kotlin) {
            if (generateSpringAnnotations())
                out.println("@%s(readOnly = true)", transactional);

            out.println("%sabstract class %s<R : %s<R>, P, T>(table: %s<R>, type: %s<P>, configuration: %s?) : %s<R, P, T>(table, type, configuration) {",
                visibility(), className, UpdatableRecord.class, Table.class, Class.class, Configuration.class, DAOImpl.class);

            out.println();
            out.println("%sconstructor(table: %s<R>, type: %s<P>) : this(table, type, null)", visibility(), Table.class, Class.class);
        }
        else {
            if (generateSpringAnnotations())
                out.println("@%s(readOnly = true)", transactional);

            out.println("%sabstract class %s<R extends %s<R>, P, T> extends %s<R, P, T> {", visibility(), className, UpdatableRecord.class, DAOImpl.class);
            out.println();
            out.println("protected %s(%s<R> table, %s<P> type) {", className, Table.class, Class.class);
            out.println("super(table, type);");
            out.println("}");
            out.println();
            out.println("protected %s(%s<R> table, %s<P> type, %s configuration) {", className, Table.class, Class.class, Configuration.class);
            out.println("super(table, type, configuration);");
            out.println("}");
        }

        Consumer<Boolean> printTransactionalHeader = readOnly -> {
            out.println();

            if (readOnly)
                out.println("@%s(readOnly = true)", transactional);
            else
                out.println("@%s", transactional);
        };

        printTransactionalHeader.accept(true);
        if (scala) {
            out.println("override def count(): Long = super.count()");
        }
        else if (kotlin) {
            out.println("public override fun count(): Long = super.count()");
        }
        else {
            out.override();
            out.println("public long count() {");
            out.println("return super.count();");
            out.println("}");
        }

        printTransactionalHeader.accept(true);
        if (scala) {
            out.println("override def exists(obj: P): Boolean = super.exists(obj)");
        }
        else if (kotlin) {
            out.println("public override fun exists(obj: P): Boolean = super.exists(obj)");
        }
        else {
            out.override();
            out.println("public boolean exists(P object) {");
            out.println("return super.exists(object);");
            out.println("}");
        }

        printTransactionalHeader.accept(true);
        if (scala) {
            out.println("override def existsById(id: T): Boolean = super.existsById(id)");
        }
        else if (kotlin) {
            out.println("public override fun existsById(id: T): Boolean = super.existsById(id)");
        }
        else {
            out.override();
            out.println("public boolean existsById(T id) {");
            out.println("return super.existsById(id);");
            out.println("}");
        }

        printTransactionalHeader.accept(true);
        if (scala) {
            out.println("override def fetch[Z](field: %s[Z], values: %s[_ <: Z]): %s[P] = super.fetch(field, values)", Field.class, Collection.class, List.class);
        }
        else if (kotlin) {
            out.println("public override fun <Z> fetch(field: %s<Z>, values: %s<Z>): %s<P> = super.fetch(field, values)", Field.class, out.ref("kotlin.collections.Collection"), out.ref("kotlin.collections.List"));
        }
        else {
            out.override();
            out.println("public <Z> %s<P> fetch(%s<Z> field, %s<? extends Z> values) {", List.class, Field.class, Collection.class);
            out.println("return super.fetch(field, values);");
            out.println("}");
        }

        printTransactionalHeader.accept(true);
        if (scala) {
            out.println("override def fetch[Z](field: %s[Z], values: Z*): %s[P] = super.fetch(field, values:_*)", Field.class, List.class);
        }
        else if (kotlin) {
            out.println("public override fun <Z> fetch(field: %s<Z>, vararg values: Z): %s<P> = super.fetch(field, *values)", Field.class, out.ref("kotlin.collections.List"));
        }
        else {
            out.override();
            out.println("public <Z> %s<P> fetch(%s<Z> field, Z... values) {", List.class, Field.class);
            out.println("return super.fetch(field, values);");
            out.println("}");
        }

        printTransactionalHeader.accept(true);
        if (scala) {
            out.println("override def fetchOne[Z](field: %s[Z], value: Z): P = super.fetchOne(field, value)", Field.class);
        }
        else if (kotlin) {
            out.println("public override fun <Z> fetchOne(field: %s<Z>, value: Z): P? = super.fetchOne(field, value)", Field.class);
        }
        else {
            out.override();
            out.println("public <Z> P fetchOne(%s<Z> field, Z value) {", Field.class);
            out.println("return super.fetchOne(field, value);");
            out.println("}");
        }

        printTransactionalHeader.accept(true);
        if (scala) {
            out.println("override def fetchOptional[Z](field: %s[Z], value: Z): %s[P] = super.fetchOptional(field, value)", Field.class, Optional.class);
        }
        else if (kotlin) {
            out.println("public override fun <Z> fetchOptional(field: %s<Z>, value: Z): %s<P> = super.fetchOptional(field, value)", Field.class, Optional.class);
        }
        else {
            out.override();
            out.println("public <Z> %s<P> fetchOptional(%s<Z> field, Z value) {", Optional.class, Field.class);
            out.println("return super.fetchOptional(field, value);");
            out.println("}");
        }

        printTransactionalHeader.accept(true);
        if (scala) {
            out.println("override def fetchRange[Z](field: %s[Z], lowerInclusive: Z, upperInclusive: Z): %s[P] = super.fetchRange(field, lowerInclusive, upperInclusive)", Field.class, List.class);
        }
        else if (kotlin) {
            out.println("public override fun <Z> fetchRange(field: %s<Z>, lowerInclusive: Z, upperInclusive: Z): %s<P> = super.fetchRange(field, lowerInclusive, upperInclusive)", Field.class, out.ref("kotlin.collections.List"));
        }
        else {
            out.override();
            out.println("public <Z> %s<P> fetchRange(%s<Z> field, Z lowerInclusive, Z upperInclusive) {", List.class, Field.class);
            out.println("return super.fetchRange(field, lowerInclusive, upperInclusive);");
            out.println("}");
        }

        printTransactionalHeader.accept(true);
        if (scala) {
            out.println("override def findAll(): %s[P] = super.findAll()", List.class);
        }
        else if (kotlin) {
            out.println("public override fun findAll(): %s<P> = super.findAll()", out.ref("kotlin.collections.List"));
        }
        else {
            out.override();
            out.println("public %s<P> findAll() {", List.class);
            out.println("return super.findAll();");
            out.println("}");
        }

        printTransactionalHeader.accept(true);
        if (scala) {
            out.println("override def findById(id: T): P = super.findById(id)");
        }
        else if (kotlin) {
            out.println("public override fun findById(id: T): P? = super.findById(id)");
        }
        else {
            out.override();
            out.println("public P findById(T id) {");
            out.println("return super.findById(id);");
            out.println("}");
        }

        printTransactionalHeader.accept(true);
        if (scala) {
            out.println("override def findOptionalById(id: T): %s[P] = super.findOptionalById(id)", Optional.class);
        }
        else if (kotlin) {
            out.println("public override fun findOptionalById(id: T): %s<P> = super.findOptionalById(id)", Optional.class);
        }
        else {
            out.override();
            out.println("public %s<P> findOptionalById(T id) {", Optional.class);
            out.println("return super.findOptionalById(id);");
            out.println("}");
        }

        for (String name : asList("insert", "update", "merge", "delete", "deleteById")) {
            String argType = name.endsWith("ById") ? "T" : "P";
            String argName = name.endsWith("ById") ? "id" : "obj";

            printTransactionalHeader.accept(false);
            if (scala) {
                out.println("override def %s(%s: %s): Unit = super.%s(%s)", name, argName, argType, name, argName);
            }
            else if (kotlin) {
                out.println("public override fun %s(%s: %s): Unit = super.%s(%s)", name, argName, argType, name, argName);
            }
            else {
                out.override();
                out.println("public void %s(%s<%s> %ss) {", name, Collection.class, argType, argName);
                out.println("super.%s(%ss);", name, argName);
                out.println("}");
            }

            printTransactionalHeader.accept(false);
            if (scala) {
                out.println("override def %s(%ss: %s*): Unit = super.%s(%ss:_*)", name, argName, argType, name, argName);
            }
            else if (kotlin) {
                out.println("public override fun %s(vararg %ss: %s): Unit = super.%s(*%ss)", name, argName, argType, name, argName);
            }
            else {
                out.override();
                out.println("public void %s(%s %s) {", name, argType, argName);
                out.println("super.%s(%s);", name, argName);
                out.println("}");
            }

            printTransactionalHeader.accept(false);
            if (scala) {
                out.println("override def %s(%ss: %s[%s]): Unit = super.%s(%ss)", name, argName, Collection.class, argType, name, argName);
            }
            else if (kotlin) {
                out.println("public override fun %s(%ss: %s<%s>): Unit = super.%s(%ss)", name, argName, out.ref("kotlin.collections.Collection"), argType, name, argName);
            }
            else {
                out.override();
                out.println("public void %s(%s... %ss) {", name, argType, argName);
                out.println("super.%s(%ss);", name, argName);
                out.println("}");
            }
        }

        generateSpringDaoClassFooter(catalog, out);
        out.println("}");
    }

    /**
     * Subclasses may override this method to provide table references class footer code.
     */
    @SuppressWarnings("unused")
    protected void generateSpringDaoClassFooter(CatalogDefinition catalog, JavaWriter out) {}

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
        final String daoImpl = generateSpringDao()
            ? out.ref(getStrategy().getJavaPackageName(table.getCatalog(), Mode.DAO) + ".AbstractSpringDAOImpl")
            : out.ref(DAOImpl.class);
        final String tableIdentifier = out.ref(getStrategy().getFullJavaIdentifier(table), 2);

        String tType = (scala || kotlin ? "Unit" : "Void");
        String tNullability = "";
        String pType = out.ref(getStrategy().getFullJavaClassName(table, Mode.POJO));

        List<ColumnDefinition> keyColumns = key.getKeyColumns();

        if (keyColumns.size() == 1) {
            tType = getJavaType(keyColumns.get(0).getType(resolver(out)), out, Mode.POJO);
            tNullability = kotlinNullability(out, keyColumns.get(0), Mode.POJO);
        }
        else if (keyColumns.size() <= Constants.MAX_ROW_DEGREE) {
            StringBuilder generics = new StringBuilder();

            forEach(keyColumns, "", ", ", (column, separator) -> {
                generics.append(out.ref(getJavaType(column.getType(resolver(out)), out)));

                if (kotlin)
                    generics.append("?");

                generics.append(separator);
            });

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
        printClassAnnotations(out, table, Mode.DAO);

        if (generateSpringAnnotations())
            out.println("@%s", out.ref("org.springframework.stereotype.Repository"));

        if (scala)
            out.println("%sclass %s(configuration: %s) extends %s[%s, %s, %s](%s, classOf[%s], configuration)[[before= with ][separator= with ][%s]] {",
                visibility(), className, Configuration.class, daoImpl, tableRecord, pType, tType, tableIdentifier, pType, interfaces);
        else if (kotlin)
            out.println("%sopen class %s(configuration: %s?) : %s<%s, %s, %s>(%s, %s::class.java, configuration)[[before=, ][%s]] {",
                visibility(), className, Configuration.class, daoImpl, tableRecord, pType, tType, tableIdentifier, pType, interfaces);
        else
            out.println("%sclass %s extends %s<%s, %s, %s>[[before= implements ][%s]] {",
                visibility(), className, daoImpl, tableRecord, pType, tType, interfaces);

        // Default constructor
        // -------------------
        out.javadoc("Create a new %s without any configuration", className);

        if (scala) {
            out.println("%sdef this() = this(null)", visibility());
        }
        else if (kotlin) {
            out.println("%sconstructor(): this(null)", visibility());
        }
        else {
            out.println("%s%s() {", visibility(), className);
            out.println("super(%s, %s.class);", tableIdentifier, pType);
            out.println("}");
        }

        // Initialising constructor
        // ------------------------

        if (!scala && !kotlin) {
            out.javadoc("Create a new %s with an attached configuration", className);

            printDaoConstructorAnnotations(table, out);
            out.println("%s%s(%s configuration) {", visibility(), className, Configuration.class);
            out.println("super(%s, %s.class, configuration);", tableIdentifier, pType);
            out.println("}");
        }

        // Template method implementations
        // -------------------------------
        if (scala) {
            out.println();
            out.print("%soverride def getId(o: %s): %s = ", visibilityPublic(), pType, tType);
        }
        else if (kotlin) {
            out.println();
            out.print("%soverride fun getId(o: %s): %s%s = ", visibilityPublic(), pType, tType, tNullability);
        }
        else {
            out.overrideInherit();
            printNonnullAnnotation(out);
            out.println("%s%s getId(%s object) {", visibilityPublic(), tType, pType);
        }

        if (keyColumns.size() == 1) {
            if (scala)
                out.println("o.%s", getStrategy().getJavaGetterName(keyColumns.get(0), Mode.POJO));
            else if (kotlin)
                out.println("o.%s", getStrategy().getJavaMemberName(keyColumns.get(0), Mode.POJO));
            else
                out.println("return object.%s();",
                    generatePojosAsJavaRecordClasses()
                    ? getStrategy().getJavaMemberName(keyColumns.get(0), Mode.POJO)
                    : getStrategy().getJavaGetterName(keyColumns.get(0), Mode.POJO)
                );
        }

        // [#2574] This should be replaced by a call to a method on the target table's Key type
        else {
            StringBuilder params = new StringBuilder();

            forEach(keyColumns, "", ", ", (column, separator) -> {
                if (scala)
                    params.append("o.").append(getStrategy().getJavaGetterName(column, Mode.POJO));
                else if (kotlin)
                    params.append("o.").append(getStrategy().getJavaMemberName(column, Mode.POJO));
                else
                    params.append("object.").append(
                        generatePojosAsJavaRecordClasses()
                            ? getStrategy().getJavaMemberName(column, Mode.POJO)
                            : getStrategy().getJavaGetterName(column, Mode.POJO)
                    ).append("()");

                params.append(separator);
            });

            if (scala || kotlin)
                out.println("compositeKeyRecord(%s)", params.toString());
            else
                out.println("return compositeKeyRecord(%s);", params.toString());
        }

        if (scala || kotlin) {}
        else
            out.println("}");

        List<Definition> embeddablesAndUnreplacedColumns = embeddablesAndUnreplacedColumns(table);

        columnLoop:
        for (Definition column : embeddablesAndUnreplacedColumns) {
            final String colName = column.getOutputName();
            final String colClass = getStrategy().getJavaClassName(column);
            final String colMemberUC = StringUtils.toUC(getStrategy().getJavaMemberName(column), getStrategy().getTargetLocale());
            final String colTypeFull = getJavaType(column, out, Mode.POJO);
            final String colTypeRecord = out.ref(getJavaType(column, out, Mode.RECORD));
            final String colType = out.ref(colTypeFull);
            final String colIdentifier = out.ref(getStrategy().getFullJavaIdentifier(column), colRefSegments(column));











            // fetchRangeOf[Column]([T]...)
            // -----------------------
            if (!printDeprecationIfUnknownType(out, colTypeFull))
                out.javadoc("Fetch records that have <code>%s BETWEEN lowerInclusive AND upperInclusive</code>", colName);

            if (scala) {
                if (column instanceof EmbeddableDefinition)
                    out.println("%sdef fetchRangeOf%s(lowerInclusive: %s, upperInclusive: %s): %s[%s] = fetchRange(%s, new %s(lowerInclusive), new %s(upperInclusive))",
                        visibility(), colMemberUC, colType, colType, List.class, pType, colIdentifier, colTypeRecord, colTypeRecord);
                else
                    out.println("%sdef fetchRangeOf%s(lowerInclusive: %s, upperInclusive: %s): %s[%s] = fetchRange(%s, lowerInclusive, upperInclusive)",
                        visibility(), colClass, colType, colType, List.class, pType, colIdentifier);
            }
            else if (kotlin) {
                if (column instanceof EmbeddableDefinition) {
                    out.println("%sfun fetchRangeOf%s(lowerInclusive: %s?, upperInclusive: %s?): %s<%s> = fetchRange(%s, if (lowerInclusive != null) %s(lowerInclusive) else null, if (upperInclusive != null) %s(upperInclusive) else null)",
                        visibility(), colMemberUC, colType, colType, out.ref(KLIST), pType, colIdentifier, colTypeRecord, colTypeRecord);
                }
                else {
                    final String nullability = kotlinNullability(out, (TypedElementDefinition<?>) column, Mode.POJO);

                    out.println("%sfun fetchRangeOf%s(lowerInclusive: %s%s, upperInclusive: %s%s): %s<%s> = fetchRange(%s, lowerInclusive, upperInclusive)",
                        visibility(), colClass, colType, nullability, colType, nullability, out.ref(KLIST), pType, colIdentifier);
                }
            }
            else {
                printNonnullAnnotation(out);

                if (column instanceof EmbeddableDefinition) {
                    out.println("%s%s<%s> fetchRangeOf%s(%s lowerInclusive, %s upperInclusive) {", visibility(), List.class, pType, colMemberUC, colType, colType);
                    out.println("return fetchRange(%s, new %s(lowerInclusive), new %s(upperInclusive));", colIdentifier, colTypeRecord, colTypeRecord);
                }
                else {
                    out.println("%s%s<%s> fetchRangeOf%s(%s lowerInclusive, %s upperInclusive) {", visibility(), List.class, pType, colClass, colType, colType);
                    out.println("return fetchRange(%s, lowerInclusive, upperInclusive);", colIdentifier);
                }

                out.println("}");
            }

            // fetchBy[Column]([T]...)
            // -----------------------
            if (!printDeprecationIfUnknownType(out, colTypeFull))
                out.javadoc("Fetch records that have <code>%s IN (values)</code>", colName);

            if (scala) {
                if (column instanceof EmbeddableDefinition)
                    out.println("%sdef fetchBy%s(values: %s*): %s[%s] = fetch(%s, values.map(v => new %s(v)).toArray:_*)",
                        visibility(), colMemberUC, colType, List.class, pType, colIdentifier, colTypeRecord);
                else
                    out.println("%sdef fetchBy%s(values: %s*): %s[%s] = fetch(%s, values:_*)",
                        visibility(), colClass, colType, List.class, pType, colIdentifier);
            }
            else if (kotlin) {
                String toTypedArray = PRIMITIVE_WRAPPERS.contains(colTypeFull) ? ".toTypedArray()" : "";

                if (column instanceof EmbeddableDefinition)
                    out.println("%sfun fetchBy%s(vararg values: %s): %s<%s> = fetch(%s, values.map { %s(it) })",
                        visibility(), colMemberUC, colType, out.ref(KLIST), pType, colIdentifier, colTypeRecord);
                else
                    out.println("%sfun fetchBy%s(vararg values: %s): %s<%s> = fetch(%s, *values%s)",
                        visibility(), colClass, colType, out.ref(KLIST), pType, colIdentifier, toTypedArray);
            }
            else {
                printNonnullAnnotation(out);

                if (column instanceof EmbeddableDefinition) {
                    out.println("%s%s<%s> fetchBy%s(%s... values) {", visibility(), List.class, pType, colMemberUC, colType);
                    out.println("%s[] records = new %s[values.length];", colTypeRecord, colTypeRecord);
                    out.println();
                    out.println("for (int i = 0; i < values.length; i++)");
                    out.tab(1).println("records[i] = new %s(values[i]);", colTypeRecord);
                    out.println();
                    out.println("return fetch(%s, records);", colIdentifier);
                }
                else {
                    out.println("%s%s<%s> fetchBy%s(%s... values) {", visibility(), List.class, pType, colClass, colType);
                    out.println("return fetch(%s, values);", colIdentifier);
                }

                out.println("}");
            }

            // fetchOneBy[Column]([T])
            // -----------------------
            ukLoop:
            if (column instanceof ColumnDefinition) {
                for (UniqueKeyDefinition uk : ((ColumnDefinition) column).getKeys()) {

                    // If column is part of a single-column unique key...
                    if (uk.getKeyColumns().size() == 1 && uk.getKeyColumns().get(0).equals(column)) {
                        if (!printDeprecationIfUnknownType(out, colTypeFull))
                            out.javadoc("Fetch a unique record that has <code>%s = value</code>", colName);

                        if (scala) {
                            out.println("%sdef fetchOneBy%s(value: %s): %s = fetchOne(%s, value)", visibility(), colClass, colType, pType, colIdentifier);
                        }
                        else if (kotlin) {
                            out.println("%sfun fetchOneBy%s(value: %s): %s? = fetchOne(%s, value)", visibility(), colClass, colType, pType, colIdentifier);
                        }
                        else {
                            printNullableAnnotation(out);
                            out.println("%s%s fetchOneBy%s(%s value) {", visibility(), pType, colClass, colType);
                            out.println("return fetchOne(%s, value);", colIdentifier);
                            out.println("}");

                            if (!printDeprecationIfUnknownType(out, colTypeFull))
                                out.javadoc("Fetch a unique record that has <code>%s = value</code>", colName);

                            printNonnullAnnotation(out);
                            out.println("%s%s<%s> fetchOptionalBy%s(%s value) {", visibility(), Optional.class, pType, colClass, colType);
                            out.println("return fetchOptional(%s, value);", colIdentifier);
                            out.println("}");
                        }

                        break ukLoop;
                    }
                }
            }
        }

        generateDaoClassFooter(table, out);
        out.println("}");
    }

    /**
     * Subclasses may override this method to provide alternative DAO
     * constructor annotations, such as DI annotations. [#10801]
     */
    protected void printDaoConstructorAnnotations(TableDefinition table, JavaWriter out) {
        if (generateSpringAnnotations())
            out.println("@%s", out.ref("org.springframework.beans.factory.annotation.Autowired"));
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

    protected void generateEmbeddablePojo(EmbeddableDefinition embeddable) {
        JavaWriter out = newJavaWriter(getFile(embeddable, Mode.POJO));
        log.info("Generating POJO", out.file().getName());
        generatePojo0(embeddable, out);
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

    private final void generatePojo0(Definition tableUdtOrEmbeddable, JavaWriter out) {
        final String className = getStrategy().getJavaClassName(tableUdtOrEmbeddable, Mode.POJO);
        final String interfaceName = generateInterfaces()
            ? out.ref(getStrategy().getFullJavaClassName(tableUdtOrEmbeddable, Mode.INTERFACE))
            : "";
        final String superName = out.ref(getStrategy().getJavaClassExtends(tableUdtOrEmbeddable, Mode.POJO));
        final List<String> interfaces = out.ref(getStrategy().getJavaClassImplements(tableUdtOrEmbeddable, Mode.POJO));

        if (generateInterfaces())
            interfaces.add(interfaceName);

        final List<String> superTypes = list(superName, interfaces);
        printPackage(out, tableUdtOrEmbeddable, Mode.POJO);

        if (tableUdtOrEmbeddable instanceof TableDefinition)
            generatePojoClassJavadoc((TableDefinition) tableUdtOrEmbeddable, out);
        else if (tableUdtOrEmbeddable instanceof EmbeddableDefinition)
            generateEmbeddableClassJavadoc((EmbeddableDefinition) tableUdtOrEmbeddable, out);
        else
            generateUDTPojoClassJavadoc((UDTDefinition) tableUdtOrEmbeddable, out);

        printClassAnnotations(out, tableUdtOrEmbeddable, Mode.POJO);

        if (tableUdtOrEmbeddable instanceof TableDefinition)
            printTableJPAAnnotation(out, (TableDefinition) tableUdtOrEmbeddable);

        if (scala) {
            out.println("%s%sclass %s(", visibility(), (generatePojosAsScalaCaseClasses() ? "case " : ""), className);

            forEach(getTypedElements(tableUdtOrEmbeddable), (column, separator) -> {
                out.println("%s%s %s: %s%s",
                    visibility(generateInterfaces()),
                    generateImmutablePojos() ? "val" : "var",
                    scalaWhitespaceSuffix(getStrategy().getJavaMemberName(column, Mode.POJO)),
                    out.ref(getJavaType(column.getType(resolver(out, Mode.POJO)), out, Mode.POJO)),
                    separator
                );
            });

            out.println(")[[before= extends ][%s]][[before= with ][separator= with ][%s]] {", first(superTypes), remaining(superTypes));
        }
        else if (kotlin) {
            out.println("%s%sclass %s(", visibility(), (generatePojosAsKotlinDataClasses() ? "data " : ""), className);

            forEach(getTypedElements(tableUdtOrEmbeddable), (column, separator) -> {
                final String member = getStrategy().getJavaMemberName(column, Mode.POJO);
                final String nullability = kotlinNullability(out, column, Mode.POJO);

                if (column instanceof ColumnDefinition)
                    printColumnJPAAnnotation(out, (ColumnDefinition) column);

                printValidationAnnotation(out, column);
                if (!generateImmutablePojos())
                    printKotlinSetterAnnotation(out, column, Mode.POJO);

                out.println("%s%s%s %s: %s%s%s%s",
                    visibility(generateInterfaces()),
                    generateInterfaces() ? "override " : "",
                    generateImmutablePojos() ? "val" : "var",
                    member,
                    out.ref(getJavaType(column.getType(resolver(out, Mode.POJO)), out, Mode.POJO)),
                    nullability,
                    nullability.isEmpty() ? "" : " = null",
                    separator
                );
            });

            out.println(")[[before=: ][%s]] {", superTypes);
        }
        else {
            if (generatePojosAsJavaRecordClasses()) {
                out.println("%srecord %s(", visibility(), className);

                forEach(getTypedElements(tableUdtOrEmbeddable), (column, separator) -> {
                    out.println("[[before=@][after= ][%s]]%s %s%s",
                        list(nullableOrNonnullAnnotation(out, column)),
                        out.ref(getJavaType(column.getType(resolver(out, Mode.POJO)), out, Mode.POJO)),
                        getStrategy().getJavaMemberName(column, Mode.POJO),
                        separator);
                });

                out.println(")[[before= implements ][%s]] {", interfaces);
            }
            else {
                out.println("%sclass %s[[before= extends ][%s]][[before= implements ][%s]] {", visibility(), className, list(superName), interfaces);
            }

            if (generateSerializablePojos() || generateSerializableInterfaces())
                out.printSerial();

            out.println();

            if (!generatePojosAsJavaRecordClasses())
                for (TypedElementDefinition<?> column : getTypedElements(tableUdtOrEmbeddable))
                    out.println("private %s%s %s;",
                        generateImmutablePojos() ? "final " : "",
                        out.ref(getJavaType(column.getType(resolver(out, Mode.POJO)), out, Mode.POJO)),
                        getStrategy().getJavaMemberName(column, Mode.POJO));
        }

        // Constructors
        // ---------------------------------------------------------------------

        // Default constructor
        if (!generateImmutablePojos() && !generatePojosAsJavaRecordClasses())
            generatePojoDefaultConstructor(tableUdtOrEmbeddable, out);

        if (!kotlin) {
            if (!generatePojosAsJavaRecordClasses()) {

                // [#1363] [#7055] copy constructor
                generatePojoCopyConstructor(tableUdtOrEmbeddable, out);

                // Multi-constructor
                generatePojoMultiConstructor(tableUdtOrEmbeddable, out);
            }

            List<? extends TypedElementDefinition<?>> elements = getTypedElements(tableUdtOrEmbeddable);
            for (int i = 0; i < elements.size(); i++) {
                TypedElementDefinition<?> column = elements.get(i);

                if (!generatePojosAsJavaRecordClasses() || generateInterfaces())
                    if (tableUdtOrEmbeddable instanceof TableDefinition)
                        generatePojoGetter(column, i, out);
                    else
                        generateUDTPojoGetter(column, i, out);

                // Setter
                if (!generateImmutablePojos())
                    if (tableUdtOrEmbeddable instanceof TableDefinition)
                        generatePojoSetter(column, i, out);
                    else
                        generateUDTPojoSetter(column, i, out);
            }
        }

        if (tableUdtOrEmbeddable instanceof TableDefinition) {
            List<EmbeddableDefinition> embeddables = ((TableDefinition) tableUdtOrEmbeddable).getReferencedEmbeddables();

            for (int i = 0; i < embeddables.size(); i++) {
                EmbeddableDefinition embeddable = embeddables.get(i);

                if (!generateImmutablePojos())
                    generateEmbeddablePojoSetter(embeddable, i, out);
                generateEmbeddablePojoGetter(embeddable, i, out);
            }
        }

        if (generatePojosEqualsAndHashCode())
            generatePojoEqualsAndHashCode(tableUdtOrEmbeddable, out);

        if (generatePojosToString())
            generatePojoToString(tableUdtOrEmbeddable, out);

        if (generateInterfaces() && !generateImmutablePojos())
            printFromAndInto(out, tableUdtOrEmbeddable, Mode.POJO);

        if (tableUdtOrEmbeddable instanceof TableDefinition)
            generatePojoClassFooter((TableDefinition) tableUdtOrEmbeddable, out);
        else if (tableUdtOrEmbeddable instanceof EmbeddableDefinition)
            generateEmbeddableClassFooter((EmbeddableDefinition) tableUdtOrEmbeddable, out);
        else
            generateUDTPojoClassFooter((UDTDefinition) tableUdtOrEmbeddable, out);

        out.println("}");
        closeJavaWriter(out);
    }
    /**
     * Subclasses may override this method to provide their own pojo copy constructors.
     */
    protected void generatePojoMultiConstructor(Definition tableOrUDT, JavaWriter out) {
        final String className = getStrategy().getJavaClassName(tableOrUDT, Mode.POJO);
        final List<String> properties = new ArrayList<>();

        for (TypedElementDefinition<?> column : getTypedElements(tableOrUDT))
            properties.add("\"" + escapeString(getStrategy().getJavaMemberName(column, Mode.POJO)) + "\"");

        if (scala) {
        }

        // [#3010] Invalid UDTs may have no attributes. Avoid generating this constructor in that case
        // [#3176] Avoid generating constructors for tables with more than 255 columns (Java's method argument limit)
        else if (getTypedElements(tableOrUDT).size() > 0 &&
                 getTypedElements(tableOrUDT).size() < 255) {
            out.println();

            if (generateConstructorPropertiesAnnotationOnPojos())
                out.println("@%s({ [[%s]] })", out.ref("java.beans.ConstructorProperties"), properties);

            out.print("%s%s(", visibility(), className);

            String separator1 = "";
            for (TypedElementDefinition<?> column : getTypedElements(tableOrUDT)) {
                final String nullableAnnotation = nullableOrNonnullAnnotation(out, column);

                out.println(separator1);
                out.print("[[before=@][after= ][%s]]%s %s",
                    list(nullableAnnotation),
                    out.ref(getJavaType(column.getType(resolver(out, Mode.POJO)), out, Mode.POJO)),
                    getStrategy().getJavaMemberName(column, Mode.POJO));
                separator1 = ",";
            }

            out.println();
            out.println(") {");

            for (TypedElementDefinition<?> column : getTypedElements(tableOrUDT)) {
                final String columnMember = getStrategy().getJavaMemberName(column, Mode.POJO);

                out.println("this.%s = %s;", columnMember, columnMember);
            }

            out.println("}");
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
            out.println("%sdef this(value: %s) = this(", visibility(), generateInterfaces() ? interfaceName : className);

            forEach(getTypedElements(tableOrUDT), (column, separator) -> {
                out.println("value.%s%s",
                    generateInterfaces()
                        ? getStrategy().getJavaGetterName(column, Mode.INTERFACE)
                        : getStrategy().getJavaMemberName(column, Mode.POJO),
                    separator
                );
            });

            out.println(")");
        }

        // Should never be called
        else if (kotlin) {}
        else {
            out.println("%s%s(%s value) {", visibility(), className, generateInterfaces() ? interfaceName : className);

            if (generatePojosAsJavaRecordClasses()) {
                out.println("this(");

                forEach(getTypedElements(tableOrUDT), (column, separator) -> {
                    out.println("value.%s%s%s",
                        generateInterfaces()
                            ? getStrategy().getJavaGetterName(column, Mode.INTERFACE)
                            : getStrategy().getJavaMemberName(column, Mode.POJO),
                        generateInterfaces()
                            ? "()"
                            : "",
                        separator);
                });

                out.println(");");
            }
            else {
                for (TypedElementDefinition<?> column : getTypedElements(tableOrUDT)) {
                    out.println("this.%s = value.%s%s;",
                        getStrategy().getJavaMemberName(column, Mode.POJO),
                        generateInterfaces()
                            ? getStrategy().getJavaGetterName(column, Mode.INTERFACE)
                            : getStrategy().getJavaMemberName(column, Mode.POJO),
                        generateInterfaces()
                            ? "()"
                            : "");
                }
            }

            out.println("}");
        }
    }

    /**
     * Subclasses may override this method to provide their own pojo default constructors.
     */
    protected void generatePojoDefaultConstructor(Definition tableOrUDT, JavaWriter out) {
        final String className = getStrategy().getJavaClassName(tableOrUDT, Mode.POJO);

        out.println();
        int size = getTypedElements(tableOrUDT).size();

        if (scala) {

            // [#3010] Invalid UDTs may have no attributes. Avoid generating this constructor in that case
            if (size > 0) {
                List<String> nulls = new ArrayList<>(size);
                for (TypedElementDefinition<?> column : getTypedElements(tableOrUDT))

                    // Avoid ambiguities between a single-T-value constructor
                    // and the copy constructor
                    if (size == 1)
                        nulls.add("null: " + out.ref(getJavaType(column.getType(resolver(out, Mode.POJO)), out, Mode.POJO)));
                    else
                        nulls.add("null");

                out.println("def this() = this([[%s]])", nulls);
            }
        }

        // [#6248] [#10288] The no-args constructor isn't needed because we have named, defaulted parameters
        else if (kotlin) {}
        else {
            out.println("%s%s() {}", visibility(), className);
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
    protected void generateEmbeddablePojoGetter(EmbeddableDefinition embeddable, @SuppressWarnings("unused") int index, JavaWriter out) {
        final String columnTypeFull = getStrategy().getFullJavaClassName(embeddable, Mode.POJO);
        final String columnType = out.ref(columnTypeFull);
        final String columnTypeDeclaredFull = getStrategy().getFullJavaClassName(embeddable, generateInterfaces() ? Mode.INTERFACE : Mode.POJO);
        final String columnTypeDeclared = out.ref(columnTypeDeclaredFull);
        final String columnGetter = getStrategy().getJavaGetterName(embeddable, Mode.POJO);
        final String columnMember = getStrategy().getJavaMemberName(embeddable, Mode.POJO);
        final String name = embeddable.getQualifiedOutputName();

        // Getter
        if (!kotlin && !printDeprecationIfUnknownType(out, columnTypeFull))
            out.javadoc("Getter for <code>%s</code>.", name);

        printNonnullAnnotation(out);

        if (scala) {
            out.println("%sdef %s: %s = new %s(", visibility(generateInterfaces()), scalaWhitespaceSuffix(columnGetter), columnType, columnType);
        }
        else if (kotlin) {

            // [#14853] The POJO property hasn't been generated in the setter, if the POJO
            //          is immutable, as there are no setters.
            if (generateImmutablePojos())
                generateEmbeddablePojoProperty(out, generateImmutableInterfaces(), generateInterfaces(), columnTypeDeclared, columnMember);

            out.tab(1).println("get(): %s = %s(", columnTypeDeclared, columnType);
        }
        else {
            out.overrideIf(generateInterfaces());
            out.println("%s%s %s() {", visibility(generateInterfaces()), columnType, columnGetter);
            out.println("return new %s(", columnType);
        }

        forEach(embeddable.getColumns(), (column, separator) -> {
            if (kotlin)
                out.tab(1).println("%s%s", getStrategy().getJavaMemberName(column.getReferencingColumn(), Mode.POJO), separator);
            else
                out.println("%s%s%s", generatePojosAsJavaRecordClasses()
                    ? getStrategy().getJavaMemberName(column.getReferencingColumn(), Mode.POJO)
                    : getStrategy().getJavaGetterName(column.getReferencingColumn(), Mode.POJO), emptyparens, separator
                );
        });

        if (scala)
            out.println(")");
        else if (kotlin)
            out.tab(1).println(")");
        else {
            out.println(");");
            out.println("}");
        }
    }

    /**
     * Subclasses may override this method to provide their own pojo getters.
     */
    protected void generateUDTPojoGetter(TypedElementDefinition<?> column, int index, JavaWriter out) {
        generatePojoGetter0(column, index, out);
    }

    private final void generatePojoGetter0(TypedElementDefinition<?> column, @SuppressWarnings("unused") int index, JavaWriter out) {
        final String columnTypeFull = getJavaType(column.getType(resolver(out, Mode.POJO)), out, Mode.POJO);
        final String columnType = out.ref(columnTypeFull);
        final String columnGetter = getStrategy().getJavaGetterName(column, Mode.POJO);
        final String columnMember = getStrategy().getJavaMemberName(column, Mode.POJO);
        final String name = column.getQualifiedOutputName();

        // Getter
        if (!printDeprecationIfUnknownType(out, columnTypeFull))
            out.javadoc("Getter for <code>%s</code>.[[before= ][%s]]", name, list(escapeEntities(comment(column))));

        if (column instanceof ColumnDefinition)
            printColumnJPAAnnotation(out, (ColumnDefinition) column);

        printValidationAnnotation(out, column);
        printNullableOrNonnullAnnotation(out, column);

        if (scala) {
            out.println("%sdef %s: %s = this.%s", visibility(generateInterfaces()), scalaWhitespaceSuffix(columnGetter), columnType, columnMember);
        }
        else {
            out.overrideIf(generateInterfaces());
            out.println("%s%s %s() {", visibility(generateInterfaces()), columnType, columnGetter);
            out.println("return this.%s;", columnMember);
            out.println("}");
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
    protected void generateEmbeddablePojoSetter(EmbeddableDefinition embeddable, @SuppressWarnings("unused") int index, JavaWriter out) {
        final boolean override = generateInterfaces() && !generateImmutableInterfaces();
        final String className = getStrategy().getJavaClassName(embeddable.getReferencingTable(), Mode.POJO);
        final String columnTypeFull = getStrategy().getFullJavaClassName(embeddable, override ? Mode.INTERFACE : Mode.POJO);
        final String columnType = out.ref(columnTypeFull);
        final String columnSetterReturnType = generateFluentSetters() ? className : tokenVoid;
        final String columnSetter = getStrategy().getJavaSetterName(embeddable, Mode.POJO);
        final String columnMember = getStrategy().getJavaMemberName(embeddable, Mode.POJO);
        final String name = embeddable.getQualifiedOutputName();

        if (!kotlin && !printDeprecationIfUnknownType(out, columnTypeFull))
            out.javadoc("Setter for <code>%s</code>.", name);

        if (scala) {
            out.println("%sdef %s(value: %s): %s = {", visibility(override), columnSetter, columnType, columnSetterReturnType);
        }
        else if (kotlin) {
            generateEmbeddablePojoProperty(out, false, override, columnType, columnMember);
            out.tab(1).println("set(value): %s {", columnSetterReturnType);
        }
        else {
            out.overrideIf(override);
            out.println("%s%s %s([[before=@][after= ][%s]]%s value) {", visibility(), columnSetterReturnType, columnSetter, list(nonnullAnnotation(out)), columnType);
        }

        if (kotlin) {
            for (EmbeddableColumnDefinition column : embeddable.getColumns()) {
                final String s = getStrategy().getJavaMemberName(column.getReferencingColumn(), Mode.POJO);
                final String g = getStrategy().getJavaMemberName(column, Mode.POJO);

                out.tab(1).println("%s = value.%s[[before= ?: throw NullPointerException(\"Shared embeddable allows NULL value for column ][after=, but this table does not\")][%s]]",
                    s, g,
                    list(kotlinEffectivelyNotNull(out, column.getReferencingColumn(), Mode.POJO) &&
                        !kotlinEffectivelyNotNull(out, column, Mode.POJO)
                        ? column.getReferencingColumn().getName()
                        : null
                    )
                );
            }
        }
        else {
            for (EmbeddableColumnDefinition column : embeddable.getColumns()) {
                final String s = getStrategy().getJavaSetterName(column.getReferencingColumn(), Mode.POJO);
                final String g = getStrategy().getJavaGetterName(column, Mode.POJO);

                out.println("%s(value.%s%s)%s", s, g, emptyparens, semicolon);
            }
        }

        if (generateFluentSetters())
            out.println("return this;");

        if (kotlin)
            out.tab(1).println("}");
        else
            out.println("}");
    }

    private void generateEmbeddablePojoProperty(JavaWriter out, boolean immutable, boolean override, String columnType, String columnMember) {
        out.println("%s%s%s %s: %s", visibility(override), override ? "override " : "", immutable ? "val" : "var", columnMember, columnType);
    }

    /**
     * Subclasses may override this method to provide their own pojo setters.
     */
    protected void generateUDTPojoSetter(TypedElementDefinition<?> column, int index, JavaWriter out) {
        generatePojoSetter0(column, index, out);
    }

    private final void generatePojoSetter0(TypedElementDefinition<?> column, @SuppressWarnings("unused") int index, JavaWriter out) {
        final String className = getStrategy().getJavaClassName(column.getContainer(), Mode.POJO);
        final String columnTypeFull = getJavaType(column.getType(resolver(out, Mode.POJO)), out, Mode.POJO);
        final String columnType = out.ref(columnTypeFull);
        final String columnSetterReturnType = generateFluentSetters() ? className : tokenVoid;
        final String columnSetter = getStrategy().getJavaSetterName(column, Mode.POJO);
        final String columnMember = getStrategy().getJavaMemberName(column, Mode.POJO);
        final boolean isUDT = column.getType(resolver(out)).isUDT();
        final boolean isUDTArray = column.getType(resolver(out)).isUDTArray();
        final String name = column.getQualifiedOutputName();

        // We cannot have covariant setters for arrays because of type erasure
        if (!(generateInterfaces() && isUDTArray)) {
            if (!printDeprecationIfUnknownType(out, columnTypeFull))
                out.javadoc("Setter for <code>%s</code>.[[before= ][%s]]", name, list(escapeEntities(comment(column))));

            if (scala) {
                out.println("%sdef %s(%s: %s): %s = {", visibility(), columnSetter, scalaWhitespaceSuffix(columnMember), columnType, columnSetterReturnType);
                out.println("this.%s = %s", columnMember, columnMember);

                if (generateFluentSetters())
                    out.println("this");

                out.println("}");
            }
            else {
                final String nullableAnnotation = nullableOrNonnullAnnotation(out, column);

                out.overrideIf(generateInterfaces() && !generateImmutableInterfaces() && !isUDT);
                out.println("%s%s %s([[before=@][after= ][%s]]%s %s) {", visibility(), columnSetterReturnType, columnSetter, list(nullableAnnotation), varargsIfArray(columnType), columnMember);
                out.println("this.%s = %s;", columnMember, columnMember);

                if (generateFluentSetters())
                    out.println("return this;");

                out.println("}");
            }
        }

        // [#3117] To avoid covariant setters on POJOs, we need to generate two setter overloads
        if (generateInterfaces() && (isUDT || isUDTArray)) {
            final String columnTypeInterface = out.ref(getJavaType(column.getType(resolver(out, Mode.INTERFACE)), out, Mode.INTERFACE));

            out.println();

            if (scala) {
                // [#3082] TODO Handle <interfaces/> + ARRAY also for Scala

                out.println("%sdef %s(%s: %s): %s = {", visibility(), columnSetter, scalaWhitespaceSuffix(columnMember), columnTypeInterface, columnSetterReturnType);
                out.println("if (%s == null)", columnMember);
                out.println("this.%s = null", columnMember);
                out.println("else");
                out.println("this.%s = %s.into(new %s)", columnMember, columnMember, columnType);

                if (generateFluentSetters())
                    out.println("this");

                out.println("}");
            }
            else {
                out.override();
                out.println("%s%s %s(%s %s) {", visibility(), columnSetterReturnType, columnSetter, varargsIfArray(columnTypeInterface), columnMember);
                out.println("if (%s == null)", columnMember);
                out.println("this.%s = null;", columnMember);

                if (isUDT) {
                    out.println("else");
                    out.println("this.%s = %s.into(new %s());", columnMember, columnMember, columnType);
                }
                else if (isUDTArray) {
                    final ArrayDefinition array = database.getArray(column.getType(resolver(out)).getSchema(), column.getType(resolver(out)).getQualifiedUserType());
                    final String componentType = out.ref(getJavaType(array.getElementType(resolver(out, Mode.POJO)), out, Mode.POJO));
                    final String componentTypeInterface = out.ref(getJavaType(array.getElementType(resolver(out, Mode.INTERFACE)), out, Mode.INTERFACE));

                    out.println("else {");
                    out.println("this.%s = new %s();", columnMember, ArrayList.class);
                    out.println();
                    out.println("for (%s i : %s)", componentTypeInterface, columnMember);
                    out.println("this.%s.add(i.into(new %s()));", columnMember, componentType);
                    out.println("}");
                }

                if (generateFluentSetters())
                    out.println("return this;");

                out.println("}");
            }
        }
    }

    protected void generatePojoEqualsAndHashCode(Definition tableOrUDT, JavaWriter out) {
        // [#10805] We used to prevent equals and hash code when generating case classes, data classes
        //          or record classes. There isn't really a good reason for this. Users can define
        //          the flags themselves to their liking, and in some cases, overriding is still required
        //          e.g. in the presence of arrays.
        final String className = getStrategy().getJavaClassName(tableOrUDT, Mode.POJO);

        out.println();

        if (scala) {
            out.println("%soverride def equals(obj: Any): scala.Boolean = {", visibilityPublic());
            out.println("if (this eq obj.asInstanceOf[AnyRef])");
            out.println("return true");
            out.println("if (obj == null)");
            out.println("return false");
            out.println("if (getClass() != obj.getClass())");
            out.println("return false");

            out.println("val other = obj.asInstanceOf[%s]", className);

            for (TypedElementDefinition<?> column : getTypedElements(tableOrUDT)) {
                final String columnMember = getStrategy().getJavaMemberName(column, Mode.POJO);

                out.println("if (this.%s == null) {", columnMember);
                out.println("if (other.%s != null)", columnMember);
                out.println("return false");
                out.println("}");

                if (isArrayType(getJavaType(column.getType(resolver(out)), out)))
                    out.println("else if (!(this.%s sameElements other.%s))", columnMember, columnMember);
                else
                    out.println("else if (!this.%s.equals(other.%s))", columnMember, columnMember);

                out.println("return false");
            }

            out.println("true");
            out.println("}");
        }
        else if (kotlin) {
            out.println("%soverride fun equals(other: Any?): Boolean {", visibilityPublic());
            out.println("if (this === other)");
            out.println("return true");
            out.println("if (other == null)");
            out.println("return false");
            out.println("if (this::class != other::class)");
            out.println("return false");

            out.println("val o: %s = other as %s", className, className);

            for (TypedElementDefinition<?> column : getTypedElements(tableOrUDT)) {
                final String columnMember = getStrategy().getJavaMemberName(column, Mode.POJO);
                final boolean nn = kotlinEffectivelyNotNull(out, column, Mode.POJO);

                if (nn) {
                    if (isObjectArrayType(getJavaType(column.getType(resolver(out)), out)))
                        out.println("if (!%s.deepEquals(this.%s, o.%s))", Arrays.class, columnMember, columnMember);
                    else if (isArrayType(getJavaType(column.getType(resolver(out)), out)))
                        out.println("if (!%s.equals(this.%s, o.%s))", Arrays.class, columnMember, columnMember);
                    else
                        out.println("if (this.%s != o.%s)", columnMember, columnMember);
                }
                else {
                    out.println("if (this.%s == null) {", columnMember);
                    out.println("if (o.%s != null)", columnMember);
                    out.println("return false");
                    out.println("}");

                    if (isObjectArrayType(getJavaType(column.getType(resolver(out)), out)))
                        out.println("else if (!%s.deepEquals(this.%s, o.%s))", Arrays.class, columnMember, columnMember);
                    else if (isArrayType(getJavaType(column.getType(resolver(out)), out)))
                        out.println("else if (!%s.equals(this.%s, o.%s))", Arrays.class, columnMember, columnMember);
                    else
                        out.println("else if (this.%s != o.%s)", columnMember, columnMember);
                }

                out.println("return false");
            }

            out.println("return true");
            out.println("}");
        }
        else {
            out.println("@Override");
            out.println("%sboolean equals(%s obj) {", visibilityPublic(), Object.class);
            out.println("if (this == obj)");
            out.println("return true;");
            out.println("if (obj == null)");
            out.println("return false;");
            out.println("if (getClass() != obj.getClass())");
            out.println("return false;");

            out.println("final %s other = (%s) obj;", className, className);

            for (TypedElementDefinition<?> column : getTypedElements(tableOrUDT)) {
                final String columnMember = getStrategy().getJavaMemberName(column, Mode.POJO);

                out.println("if (this.%s == null) {", columnMember);
                out.println("if (other.%s != null)", columnMember);
                out.println("return false;");
                out.println("}");

                if (isObjectArrayType(getJavaType(column.getType(resolver(out)), out)))
                    out.println("else if (!%s.deepEquals(this.%s, other.%s))", Arrays.class, columnMember, columnMember);
                else if (isArrayType(getJavaType(column.getType(resolver(out)), out)))
                    out.println("else if (!%s.equals(this.%s, other.%s))", Arrays.class, columnMember, columnMember);
                else
                    out.println("else if (!this.%s.equals(other.%s))", columnMember, columnMember);

                out.println("return false;");
            }

            out.println("return true;");
            out.println("}");
        }

        out.println();

        if (scala) {
            out.println("%soverride def hashCode: Int = {", visibilityPublic());
            out.println("val prime = 31");
            out.println("var result = 1");

            for (TypedElementDefinition<?> column : getTypedElements(tableOrUDT)) {
                final String columnMember = getStrategy().getJavaMemberName(column, Mode.POJO);

                if (isArrayType(getJavaType(column.getType(resolver(out)), out)))
                    out.println("result = prime * result + (if (this.%s == null) 0 else %s.toSeq.hashCode)", columnMember, columnMember);
                else
                    out.println("result = prime * result + (if (this.%s == null) 0 else this.%s.hashCode)", columnMember, columnMember);
            }

            out.println("return result");
            out.println("}");
        }
        else if (kotlin) {
            out.println("%soverride fun hashCode(): Int {", visibilityPublic());
            out.println("val prime = 31");
            out.println("var result = 1");

            for (TypedElementDefinition<?> column : getTypedElements(tableOrUDT)) {
                final String columnMember = getStrategy().getJavaMemberName(column, Mode.POJO);
                final boolean nn = kotlinEffectivelyNotNull(out, column, Mode.POJO);

                if (nn) {
                    if (isObjectArrayType(getJavaType(column.getType(resolver(out)), out)))
                        out.println("result = prime * result + %s.deepHashCode(this.%s)", Arrays.class, columnMember);
                    else if (isArrayType(getJavaType(column.getType(resolver(out)), out)))
                        out.println("result = prime * result + %s.hashCode(this.%s)", Arrays.class, columnMember);
                    else
                        out.println("result = prime * result + this.%s.hashCode()", columnMember);
                }
                else {
                    if (isObjectArrayType(getJavaType(column.getType(resolver(out)), out)))
                        out.println("result = prime * result + (if (this.%s == null) 0 else %s.deepHashCode(this.%s))", columnMember, Arrays.class, columnMember);
                    else if (isArrayType(getJavaType(column.getType(resolver(out)), out)))
                        out.println("result = prime * result + (if (this.%s == null) 0 else %s.hashCode(this.%s))", columnMember, Arrays.class, columnMember);
                    else
                        out.println("result = prime * result + (if (this.%s == null) 0 else this.%s.hashCode())", columnMember, columnMember);
                }
            }

            out.println("return result");
            out.println("}");
        }
        else {
            out.println("@Override");
            out.println("%sint hashCode() {", visibilityPublic());
            out.println("final int prime = 31;");
            out.println("int result = 1;");

            for (TypedElementDefinition<?> column : getTypedElements(tableOrUDT)) {
                final String columnMember = getStrategy().getJavaMemberName(column, Mode.POJO);

                if (isObjectArrayType(getJavaType(column.getType(resolver(out)), out)))
                    out.println("result = prime * result + ((this.%s == null) ? 0 : %s.deepHashCode(this.%s));", columnMember, Arrays.class, columnMember);
                else if (isArrayType(getJavaType(column.getType(resolver(out)), out)))
                    out.println("result = prime * result + ((this.%s == null) ? 0 : %s.hashCode(this.%s));", columnMember, Arrays.class, columnMember);
                else
                    out.println("result = prime * result + ((this.%s == null) ? 0 : this.%s.hashCode());", columnMember, columnMember);
            }

            out.println("return result;");
            out.println("}");
        }
    }

    protected void generatePojoToString(Definition tableOrUDT, JavaWriter out) {
        final String className = getStrategy().getJavaClassName(tableOrUDT, Mode.POJO);

        out.println();

        if (scala) {
            out.println("%soverride def toString: String = {", visibilityPublic());

            out.println("val sb = new %s(\"%s (\")", StringBuilder.class, className);
            out.println();

            String separator = "";
            for (TypedElementDefinition<?> column : getTypedElements(tableOrUDT)) {
                final String columnMember = getStrategy().getJavaMemberName(column, Mode.POJO);
                final String columnType = getJavaType(column.getType(resolver(out)), out);
                final boolean array = isArrayType(columnType);

                if (columnType.equals("scala.Array[scala.Byte]"))
                    out.println("sb%s.append(\"[binary...]\")", separator);
                else if (array)
                    out.println("sb%s.append(if (this.%s == null) \"\" else %s.deepToString(%s.asInstanceOf[ Array[Object] ]))", separator, columnMember, Arrays.class, columnMember);
                else
                    out.println("sb%s.append(%s)", separator, columnMember);

                separator = ".append(\", \")";
            }

            out.println();
            out.println("sb.append(\")\")");

            out.println("sb.toString");
            out.println("}");
        }
        else if (kotlin) {
            out.println("%soverride fun toString(): String {", visibilityPublic());
            out.println("val sb = %s(\"%s (\")", StringBuilder.class, className);
            out.println();

            String separator = "";
            for (TypedElementDefinition<?> column : getTypedElements(tableOrUDT)) {
                final String columnMember = getStrategy().getJavaMemberName(column, Mode.POJO);
                final String columnType = getJavaType(column.getType(resolver(out)), out);
                final boolean array = isArrayType(columnType);

                if (array && columnType.equals("kotlin.ByteArray"))
                    out.println("sb%s.append(\"[binary...]\")", separator);
                else if (array)
                    out.println("sb%s.append(%s.deepToString(%s))", separator, Arrays.class, columnMember);
                else
                    out.println("sb%s.append(%s)", separator, columnMember);

                separator = ".append(\", \")";
            }

            out.println();
            out.println("sb.append(\")\")");
            out.println("return sb.toString()");
            out.println("}");
        }
        else {
            out.println("@Override");
            out.println("%sString toString() {", visibilityPublic());
            out.println("%s sb = new %s(\"%s (\");", StringBuilder.class, StringBuilder.class, className);
            out.println();

            String separator = "";
            for (TypedElementDefinition<?> column : getTypedElements(tableOrUDT)) {
                final String columnMember = getStrategy().getJavaMemberName(column, Mode.POJO);
                final String columnType = getJavaType(column.getType(resolver(out)), out);
                final boolean array = isArrayType(columnType);

                if (array && columnType.equals("byte[]"))
                    out.println("sb%s.append(\"[binary...]\");", separator);
                else if (array)
                    out.println("sb%s.append(%s.deepToString(%s));", separator, Arrays.class, columnMember);
                else
                    out.println("sb%s.append(%s);", separator, columnMember);

                separator = ".append(\", \")";
            }

            out.println();
            out.println("sb.append(\")\");");
            out.println("return sb.toString();");
            out.println("}");
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
        out.refConflicts(getStrategy().getJavaIdentifiers(table.getColumns()));
        out.refConflicts(getStrategy().getJavaIdentifiers(table.getReferencedEmbeddables()));

        log.info("Generating table", out.file().getName() +
            " [input=" + table.getInputName() +
            (!table.getInputName().equals(table.getOutputName())
          ? ", output=" + table.getOutputName()
          : "") +
            (table.getPrimaryKey() != null
          ? ", pk=" + table.getPrimaryKey().getName()
          : "") +
            "]");

        if (log.isDebugEnabled())
            for (ColumnDefinition column : table.getColumns())
                log.debug("With column", "name=" + column.getOutputName() + ", matching type names=" + column.getDefinedType().getMatchNames());

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
        final String recordType = out.ref(getStrategy().getFullJavaClassName(table.getReferencedTable(), Mode.RECORD));
        final String classExtends = out.ref(getStrategy().getJavaClassExtends(table, Mode.DEFAULT));
        final List<String> interfaces = out.ref(getStrategy().getJavaClassImplements(table, Mode.DEFAULT));
        final String schemaId = generateDefaultSchema(schema)
            ? out.ref(getStrategy().getFullJavaIdentifier(schema), 2)
            : null;
        final String tableType = table.isTemporary()
            ? "temporaryTable"
            : table.isView()
            ? "view"
            : table.isMaterializedView()
            ? "materializedView"
            : table.isTableValuedFunction()
            ? "function"
            : "table";
        final List<ParameterDefinition> parameters = table.getParameters();
        final boolean supportsPathsToOne = generateGlobalKeyReferences()
            && !table.isTableValuedFunction()
            && generateImplicitJoinPathsToOne()
            && (generateImplicitJoinPathUnusedConstructors() || !table.getInverseForeignKeys().isEmpty());

        final boolean supportsPathsToMany = generateGlobalKeyReferences()
            && !table.isTableValuedFunction()
            && generateImplicitJoinPathsToMany()
            && (generateImplicitJoinPathUnusedConstructors() || !table.getForeignKeys().isEmpty());

        printPackage(out, table);

        if (scala) {
            out.println("object %s {", className);
            printSingletonInstance(out, table);

            // [#14985] Scala nested classes have to be located in the object
            if (generateImplicitJoinPathTableSubtypes() && (supportsPathsToOne || supportsPathsToMany)) {
                out.javadoc("A subtype implementing {@link %s} for simplified path-based joins.", Path.class);
                out.println("%sclass %sPath(path: %s[_ <: %s], childPath: %s[_ <: %s, %s], parentPath: %s[_ <: %s, %s]) extends %s(path, childPath, parentPath) with %s[%s]",
                    visibility(), className, Table.class, Record.class, ForeignKey.class, Record.class, recordType, InverseForeignKey.class, Record.class, recordType, className, Path.class, recordType);
            }

            out.println("}");
            out.println();
        }

        generateTableClassJavadoc(table, out);
        printClassAnnotations(out, table, Mode.DEFAULT);

        if (scala) {
            out.println("%sclass %s(", visibility(), className);
            out.println("alias: %s,", Name.class);
            out.println("path: %s[_ <: %s],", Table.class, Record.class);
            out.println("childPath: %s[_ <: %s, %s],", ForeignKey.class, Record.class, recordType);
            out.println("parentPath: %s[_ <: %s, %s],", InverseForeignKey.class, Record.class, recordType);
            out.println("aliased: %s[%s],", Table.class, recordType);
            out.println("parameters: %s[ %s[_] ],", out.ref("scala.Array"), Field.class);
            out.println("where: %s", Condition.class);
            out.println(")");
            out.println("extends %s[%s](", classExtends, recordType);
            out.println("alias,");
            out.println("%s,", schemaId);
            out.println("path,");
            out.println("childPath,");
            out.println("parentPath,");
            out.println("aliased,");
            out.println("parameters,");
            out.println("%s.comment(\"%s\"),", DSL.class, escapeString(comment(table)));

            if ((generateSourcesOnViews() || table.isSynthetic()) && table.isView() && table.getSource() != null)
                out.println("%s.%s(%s),", TableOptions.class, tableType, textBlock(table.getSource()));
            else if (table.isSynthetic() && table.isTableValuedFunction() && table.getSource() != null)
                out.println("%s.%s(%s),", TableOptions.class, tableType, textBlock(table.getSource()));
            else
                out.println("%s.%s,", TableOptions.class, tableType);

            out.println("where");
            out.println(")[[before= with ][separator= with ][%s]] {", interfaces);
        }
        else if (kotlin) {
            out.println("%sopen class %s(", visibility(), className);
            out.println("alias: %s,", Name.class);
            out.println("path: %s<out %s>?,", Table.class, Record.class);
            out.println("childPath: %s<out %s, %s>?,", ForeignKey.class, Record.class, recordType);
            out.println("parentPath: %s<out %s, %s>?,", InverseForeignKey.class, Record.class, recordType);
            out.println("aliased: %s<%s>?,", Table.class, recordType);
            out.println("parameters: Array<%s<*>?>?,", Field.class);
            out.println("where: %s?", Condition.class);
            out.println("): %s<%s>(", classExtends, recordType);
            out.println("alias,");
            out.println("%s,", schemaId);
            out.println("path,");
            out.println("childPath,");
            out.println("parentPath,");
            out.println("aliased,");
            out.println("parameters,");
            out.println("%s.comment(\"%s\"),", DSL.class, escapeString(comment(table)));

            if ((generateSourcesOnViews() || table.isSynthetic()) && table.isView() && table.getSource() != null)
                out.println("%s.%s(%s),", TableOptions.class, tableType, textBlock(table.getSource()));
            else if (table.isSynthetic() && table.isTableValuedFunction() && table.getSource() != null)
                out.println("%s.%s(%s),", TableOptions.class, tableType, textBlock(table.getSource()));
            else
                out.println("%s.%s(),", TableOptions.class, tableType);

            out.println("where,");
            out.println(")[[before=, ][%s]] {", interfaces);

            out.println("%scompanion object {", visibility());
            printSingletonInstance(out, table);
            out.println("}");
        }
        else {
            out.println("%sclass %s extends %s<%s>[[before= implements ][%s]] {", visibility(), className, classExtends, recordType, interfaces);
            out.printSerial();
            printSingletonInstance(out, table);
        }

        printRecordTypeMethod(out, table);

        if (table.isSynthetic()) {
            if (scala) {
                out.println();
                out.println("protected override def isSynthetic(): %s = true", out.ref("scala.Boolean"));
            }
            else if (kotlin) {
                out.println();
                out.println("protected override fun isSynthetic(): Boolean = true");
            }
            else {
                out.println();
                out.override();
                out.println("protected boolean isSynthetic() {");
                out.println("return true;");
                out.println("}");
            }
        }

        for (ColumnDefinition column : table.getColumns()) {
            final DataTypeDefinition columnTypeDef = column.getType(resolver(out));
            final DomainDefinition domain = schema.getDatabase().getDomain(schema, columnTypeDef.getQualifiedUserType());
            final String columnTypeFull = getJavaType(columnTypeDef, out);
            final String columnType = out.ref(columnTypeFull);
            final String columnTypeRef = getJavaTypeReference(column.getDatabase(), columnTypeDef, out);
            final String columnId = out.ref(getStrategy().getJavaIdentifier(column), colRefSegments(column));
            final String columnName = column.getName();
            final List<String> converter = new ArrayList<>();
            final List<String> binding = new ArrayList<>();
            final List<String> generator = new ArrayList<>();
            // [#14916] Domain types may already have bindings/converters. Don't re-apply them.
            if (domain == null || !StringUtils.equals(domain.getType(resolver(out)).getConverter(), columnTypeDef.getConverter()))
                converter.addAll(out.ref(list(columnTypeDef.getConverter())));
            if (domain == null || !StringUtils.equals(domain.getType(resolver(out)).getBinding(), columnTypeDef.getBinding()))
                binding.addAll(out.ref(list(columnTypeDef.getBinding())));






            final String columnVisibility;











            columnVisibility = visibility();

            if (!printDeprecationIfUnknownType(out, columnTypeFull))
                out.javadoc("The column <code>%s</code>.[[before= ][%s]]", column.getQualifiedOutputName(), list(escapeEntities(comment(column))));

            if (scala) {
                // [#9879] In scala, subclasses cannot call a superclass protected static method
                //         only a superclass protected instance method. But to capture the Generator<?, TR, ?>
                //         type variable, we need to pass it explicitly. The relevant createField0() method
                //         can't be overloaded, so it has that "0" suffix...
                if (generator.isEmpty())
                    out.println("%sval %s: %s[%s, %s] = createField(%s.name(\"%s\"), %s, \"%s\"" + converterTemplate(converter) + converterTemplate(binding) + converterTemplate(generator) + ")",
                        columnVisibility, scalaWhitespaceSuffix(columnId), TableField.class, recordType, columnType, DSL.class, columnName, columnTypeRef, escapeString(comment(column)), converter, binding, generator);
                else
                    out.println("%sval %s: %s[%s, %s] = createField0(%s.name(\"%s\"), %s, this, \"%s\"" + converterTemplate(converter) + converterTemplate(binding) + converterTemplate(generator) + ")",
                        columnVisibility, scalaWhitespaceSuffix(columnId), TableField.class, recordType, columnType, DSL.class, columnName, columnTypeRef, escapeString(comment(column)), converter, binding, generator);
            }
            else if (kotlin) {
                out.println("%sval %s: %s<%s, %s?> = createField(%s.name(\"%s\"), %s, this, \"%s\"" + converterTemplate(converter) + converterTemplate(binding) + converterTemplate(generator) + ")",
                    columnVisibility, columnId, TableField.class, recordType, columnType, DSL.class, columnName, columnTypeRef, escapeString(comment(column)), converter, binding, generator);
            }
            else {
                String isStatic = generateInstanceFields() ? "" : "static ";
                String tableRef = generateInstanceFields() ? "this" : out.ref(getStrategy().getJavaIdentifier(table), 2);

                out.println("%s%sfinal %s<%s, %s> %s = createField(%s.name(\"%s\"), %s, %s, \"%s\"" + converterTemplate(converter) + converterTemplate(binding) + converterTemplate(generator) + ");",
                    columnVisibility, isStatic, TableField.class, recordType, columnType, columnId, DSL.class, columnName, columnTypeRef, tableRef, escapeString(comment(column)), converter, binding, generator);
            }
        }

        // [#2530] Embeddable types
        for (EmbeddableDefinition embeddable : table.getReferencedEmbeddables()) {
            final String columnId = out.ref(getStrategy().getJavaIdentifier(embeddable), colRefSegments(null));
            final String columnType = out.ref(getStrategy().getFullJavaClassName(embeddable, Mode.RECORD));

            final List<String> columnIds = new ArrayList<>();
            for (EmbeddableColumnDefinition column : embeddable.getColumns())
                columnIds.add(out.ref(getStrategy().getJavaIdentifier(column.getReferencingColumn()), colRefSegments(column.getReferencingColumn())));

            out.javadoc("The embeddable type <code>%s</code>.[[before= ][%s]]", embeddable.getOutputName(), list(escapeEntities(referencingComment(embeddable))));

            if (scala)
                out.println("%sval %s: %s[%s, %s] = %s.createEmbeddable(%s.name(\"%s\"), classOf[%s], %s, this, [[%s]])",
                    visibility(), scalaWhitespaceSuffix(columnId), TableField.class, recordType, columnType, Internal.class, DSL.class, escapeString(embeddable.getName()), columnType, embeddable.replacesFields(), columnIds);
            else if (kotlin)
                out.println("%sval %s: %s<%s, %s> = %s.createEmbeddable(%s.name(\"%s\"), %s::class.java, %s, this, [[%s]])",
                    visibility(), columnId, TableField.class, recordType, columnType, Internal.class, DSL.class, escapeString(embeddable.getName()), columnType, embeddable.replacesFields(), columnIds);
            else
                out.println("%sfinal %s<%s, %s> %s = %s.createEmbeddable(%s.name(\"%s\"), %s.class, %s, this, [[%s]]);",
                    visibility(), TableField.class, recordType, columnType, columnId, Internal.class, DSL.class, escapeString(embeddable.getName()), columnType, embeddable.replacesFields(), columnIds);
        }

        out.println();

        // [#10191] This constructor must be generated first in Scala to prevent
        // "called constructor's definition must precede calling constructor's definition"
        if (scala) {
            if (table.isTableValuedFunction()) {
                out.println("private def this(alias: %s, aliased: %s[%s]) = this(alias, null, null, null, aliased, %s(",
                    Name.class, Table.class, recordType, out.ref("scala.Array"));

                forEach(parameters, (parameter, separator) -> {
                    final String paramTypeRef = getJavaTypeReference(parameter.getDatabase(), parameter.getType(resolver(out)), out);
                    final List<String> converter = out.ref(list(parameter.getType(resolver(out)).getConverter()));
                    final List<String> binding = out.ref(list(parameter.getType(resolver(out)).getBinding()));

                    out.println("%s.value(null, %s" + converterTemplateForTableValuedFunction(converter) + converterTemplateForTableValuedFunction(binding) + ")%s", DSL.class, paramTypeRef, converter, binding, separator);
                });

                out.println("), null)");
            }
            else
                out.println("private def this(alias: %s, aliased: %s[%s]) = this(alias, null, null, null, aliased, null, null)",
                    Name.class, Table.class, recordType);

            if (generateWhereMethodOverrides() && !table.isTableValuedFunction())
                out.println("private def this(alias: %s, aliased: %s[%s], where: %s) = this(alias, null, null, null, aliased, null, where)",
                    Name.class, Table.class, recordType, Condition.class);
        }
        else if (kotlin) {
            if (table.isTableValuedFunction()) {
                out.println("private constructor(alias: %s, aliased: %s<%s>?): this(alias, null, null, null, aliased, arrayOf(",
                    Name.class, Table.class, recordType, Field.class, parameters.size());

                forEach(parameters, (parameter, separator) -> {
                    final String paramTypeRef = getJavaTypeReference(parameter.getDatabase(), parameter.getType(resolver(out)), out);
                    final List<String> converter = out.ref(list(parameter.getType(resolver(out)).getConverter()));
                    final List<String> binding = out.ref(list(parameter.getType(resolver(out)).getBinding()));

                    out.println("%s.value(null, %s" + converterTemplateForTableValuedFunction(converter) + converterTemplateForTableValuedFunction(binding) + ")%s", DSL.class, paramTypeRef, converter, binding, separator);
                });

                out.println("), null)");
            }
            else
                out.println("private constructor(alias: %s, aliased: %s<%s>?): this(alias, null, null, null, aliased, null, null)",
                    Name.class, Table.class, recordType);

            out.println("private constructor(alias: %s, aliased: %s<%s>?, parameters: Array<%s<*>?>?): this(alias, null, null, null, aliased, parameters, null)",
                Name.class, Table.class, recordType, Field.class);

            if (generateWhereMethodOverrides() && !table.isTableValuedFunction())
                out.println("private constructor(alias: %s, aliased: %s<%s>?, where: %s): this(alias, null, null, null, aliased, null, where)",
                    Name.class, Table.class, recordType, Condition.class);
        }
        else {
            out.println("private %s(%s alias, %s<%s> aliased) {", className, Name.class, Table.class, recordType);
            if (table.isTableValuedFunction()) {
                out.println("this(alias, aliased, new %s[] {", Field.class);

                forEach(parameters, (parameter, separator) -> {
                    final String paramTypeRef = getJavaTypeReference(parameter.getDatabase(), parameter.getType(resolver(out)), out);
                    final List<String> converter = out.ref(list(parameter.getType(resolver(out)).getConverter()));
                    final List<String> binding = out.ref(list(parameter.getType(resolver(out)).getBinding()));

                    out.println("%s.val(null, %s" + converterTemplateForTableValuedFunction(converter) + converterTemplateForTableValuedFunction(binding) + ")%s", DSL.class, paramTypeRef, converter, binding, separator);
                });

                out.println("});");
            }
            else
                out.println("this(alias, aliased, (%s<?>[]) null, null);", Field.class);

            out.println("}");

            if (table.isTableValuedFunction()) {
                out.println();
                out.println("private %s(%s alias, %s<%s> aliased, %s<?>[] parameters) {", className, Name.class, Table.class, recordType, Field.class);
                out.println("this(alias, aliased, parameters, null);");
                out.println("}");
            }

            out.println();
            out.println("private %s(%s alias, %s<%s> aliased, %s<?>[] parameters, %s where) {", className, Name.class, Table.class, recordType, Field.class, Condition.class);


            if ((generateSourcesOnViews() || table.isSynthetic()) && table.isView() && table.getSource() != null)
                out.println("super(alias, null, aliased, parameters, %s.comment(\"%s\"), %s.%s(%s), where);", DSL.class, escapeString(comment(table)), TableOptions.class, tableType, textBlock(table.getSource()));
            else if (table.isSynthetic() && table.isTableValuedFunction() && table.getSource() != null)
                out.println("super(alias, null, aliased, parameters, %s.comment(\"%s\"), %s.%s(%s), where);", DSL.class, escapeString(comment(table)), TableOptions.class, tableType, textBlock(table.getSource()));
            else
                out.println("super(alias, null, aliased, parameters, %s.comment(\"%s\"), %s.%s(), where);", DSL.class, escapeString(comment(table)), TableOptions.class, tableType);

            out.println("}");
        }

        if (scala) {
            out.javadoc("Create an aliased <code>%s</code> table reference", table.getQualifiedOutputName());
            out.println("%sdef this(alias: %s) = this(%s.name(alias), %s)", visibility(), String.class, DSL.class, tableId);

            out.javadoc("Create an aliased <code>%s</code> table reference", table.getQualifiedOutputName());
            out.println("%sdef this(alias: %s) = this(alias, %s)", visibility(), Name.class, tableId);
        }
        else if (kotlin) {
            out.javadoc("Create an aliased <code>%s</code> table reference", table.getQualifiedOutputName());
            out.println("%sconstructor(alias: %s): this(%s.name(alias))", visibility(), String.class, DSL.class);

            out.javadoc("Create an aliased <code>%s</code> table reference", table.getQualifiedOutputName());
            out.println("%sconstructor(alias: %s): this(alias, null)", visibility(), Name.class, tableId);
        }

        // [#117] With instance fields, it makes sense to create a
        // type-safe table alias
        // [#1255] With instance fields, the table constructor may
        // be public, as tables are no longer singletons
        else if (generateInstanceFields()) {
            out.javadoc("Create an aliased <code>%s</code> table reference", table.getQualifiedOutputName());
            out.println("%s%s(%s alias) {", visibility(), className, String.class);
            out.println("this(%s.name(alias), %s);", DSL.class, tableId);
            out.println("}");

            out.javadoc("Create an aliased <code>%s</code> table reference", table.getQualifiedOutputName());
            out.println("%s%s(%s alias) {", visibility(), className, Name.class);
            out.println("this(alias, %s);", tableId);
            out.println("}");
        }

        if (scala) {
            out.javadoc("Create a <code>%s</code> table reference", table.getQualifiedOutputName());
            out.println("%sdef this() = this(%s.name(\"%s\"), null)", visibility(), DSL.class, escapeString(table.getOutputName()));
        }
        else if (kotlin) {
            out.javadoc("Create a <code>%s</code> table reference", table.getQualifiedOutputName());
            out.println("%sconstructor(): this(%s.name(\"%s\"), null)", visibility(), DSL.class, escapeString(table.getOutputName()));
        }
        else {
            // [#1255] With instance fields, the table constructor may
            // be public, as tables are no longer singletons
            if (generateInstanceFields()) {
                out.javadoc("Create a <code>%s</code> table reference", table.getQualifiedOutputName());
                out.println("%s%s() {", visibility(), className);
            }
            else {
                out.javadoc(NO_FURTHER_INSTANCES_ALLOWED);
                out.println("private %s() {", className);
            }

            out.println("this(%s.name(\"%s\"), null);", DSL.class, escapeString(table.getOutputName()));
            out.println("}");
        }

        if (supportsPathsToOne || supportsPathsToMany) {
            out.println();

            if (scala) {
                out.println("%sdef this(path: %s[_ <: %s], childPath: %s[_ <: %s, %s], parentPath: %s[_ <: %s, %s]) = this(%s.createPathAlias(path, childPath, parentPath), path, childPath, parentPath, %s, null, null)",
                    visibility(), Table.class, Record.class, ForeignKey.class, Record.class, recordType, InverseForeignKey.class, Record.class, recordType, Internal.class, tableId);
            }
            else if (kotlin) {
                out.println("%sconstructor(path: %s<out %s>, childPath: %s<out %s, %s>?, parentPath: %s<out %s, %s>?): this(%s.createPathAlias(path, childPath, parentPath), path, childPath, parentPath, %s, null, null)",
                    visibility(), Table.class, Record.class, ForeignKey.class, Record.class, recordType, InverseForeignKey.class, Record.class, recordType, Internal.class, tableId);
            }
            else {
                out.println("%s<O extends %s> %s(%s<O> path, %s<O, %s> childPath, %s<O, %s> parentPath) {", visibility(), Record.class, className, Table.class, ForeignKey.class, recordType, InverseForeignKey.class, recordType);
                out.println("super(path, childPath, parentPath, %s);", tableId);
                out.println("}");
            }

            if (generateImplicitJoinPathTableSubtypes()) {

                // [#14985] Scala nested classes have to be located in the companion object
                if (scala) {}
                else if (kotlin) {
                    out.javadoc("A subtype implementing {@link %s} for simplified path-based joins.", Path.class);
                    out.println("%sopen class %sPath : %s, %s<%s> {",
                        visibility(), className, className, Path.class, recordType);
                    out.println("%sconstructor(path: %s<out %s>, childPath: %s<out %s, %s>?, parentPath: %s<out %s, %s>?): super(path, childPath, parentPath)", visibility(), Table.class, Record.class, ForeignKey.class, Record.class, recordType, InverseForeignKey.class, Record.class, recordType);
                    out.println("private constructor(alias: %s, aliased: %s<%s>): super(alias, aliased)", Name.class, Table.class, recordType);
                    out.println("%soverride fun `as`(alias: %s): %sPath = %sPath(%s.name(alias), this)", visibilityPublic(), String.class, className, className, DSL.class);
                    out.println("%soverride fun `as`(alias: %s): %sPath = %sPath(alias, this)", visibilityPublic(), Name.class, className, className);
                    out.println("%soverride fun `as`(alias: %s<*>): %sPath = %sPath(alias.qualifiedName, this)", visibilityPublic(), Table.class, className, className);
                    out.println("}");
                }
                else {
                    out.javadoc("A subtype implementing {@link %s} for simplified path-based joins.", Path.class);
                    out.println("%sstatic class %sPath extends %s implements %s<%s> {", visibility(), className, className, Path.class, recordType);
                    out.println("%s<O extends %s> %sPath(%s<O> path, %s<O, %s> childPath, %s<O, %s> parentPath) {", visibility(), Record.class, className, Table.class, ForeignKey.class, recordType, InverseForeignKey.class, recordType);
                    out.println("super(path, childPath, parentPath);");
                    out.println("}");
                    out.println("private %sPath(%s alias, %s<%s> aliased) {", className, Name.class, Table.class, recordType);
                    out.println("super(alias, aliased);");
                    out.println("}");
                    out.overrideInherit();
                    out.println("%s%sPath as(%s alias) {", visibilityPublic(), className, String.class);
                    out.println("return new %sPath(%s.name(alias), this);", className, DSL.class);
                    out.println("}");
                    out.overrideInherit();
                    out.println("%s%sPath as(%s alias) {", visibilityPublic(), className, Name.class);
                    out.println("return new %sPath(alias, this);", className);
                    out.println("}");
                    out.overrideInherit();
                    out.println("%s%sPath as(%s<?> alias) {", visibilityPublic(), className, Table.class);
                    out.println("return new %sPath(alias.getQualifiedName(), this);", className);
                    out.println("}");
                    out.println("}");
                }
            }
        }

        if (schemaId != null) {
            if (scala) {
                out.println();
                out.println("%soverride def getSchema: %s = if (aliased()) null else %s", visibilityPublic(), Schema.class, schemaId);
            }
            else if (kotlin) {
                out.println("%soverride fun getSchema(): %s? = if (aliased()) null else %s", visibilityPublic(), Schema.class, schemaId);
            }
            else {
                out.overrideInherit();
                printNullableAnnotation(out);
                out.println("%s%s getSchema() {", visibilityPublic(), Schema.class);
                out.println("return aliased() ? null : %s;", schemaId);
                out.println("}");
            }
        }

        // Add index information
        if (generateIndexes()) {
            List<IndexDefinition> indexes = table.getIndexes();

            if (!indexes.isEmpty()) {
                if (generateGlobalIndexReferences()) {
                    final List<String> indexFullIds = kotlin
                        ? out.ref(getStrategy().getFullJavaIdentifiers(indexes))
                        : out.ref(getStrategy().getFullJavaIdentifiers(indexes), 2);

                    if (scala) {
                        out.println();
                        out.println("%soverride def getIndexes: %s[%s] = %s.asList[ %s ]([[%s]])",
                            visibilityPublic(), List.class, Index.class, Arrays.class, Index.class, indexFullIds);
                    }
                    else if (kotlin) {
                        out.println("%soverride fun getIndexes(): %s<%s> = listOf([[%s]])", visibilityPublic(), out.ref(KLIST), Index.class, indexFullIds);
                    }
                    else {
                        out.overrideInherit();
                        printNonnullAnnotation(out);
                        out.println("%s%s<%s> getIndexes() {", visibilityPublic(), List.class, Index.class);
                        out.println("return %s.asList([[%s]]);", Arrays.class, indexFullIds);
                        out.println("}");
                    }
                }
                else {
                    if (scala) {
                        out.println();
                        out.println("%soverride def getIndexes: %s[%s] = %s.asList[%s](", visibilityPublic(), List.class, Index.class, Arrays.class, Index.class);

                        forEach(indexes, "", ", ", (index, separator) -> {
                            printCreateIndex(out, index);
                            out.println("%s", separator);
                        });

                        out.println(")");
                    }
                    else if (kotlin) {
                        out.println("%soverride fun getIndexes(): %s<%s> = listOf(", visibilityPublic(), out.ref(KLIST), Index.class);

                        forEach(indexes, "", ", ", (index, separator) -> {
                            printCreateIndex(out, index);
                            out.println("%s", separator);
                        });

                        out.println(")");
                    }
                    else {
                        out.overrideInherit();
                        printNonnullAnnotation(out);
                        out.println("%s%s<%s> getIndexes() {", visibilityPublic(), List.class, Index.class);
                        out.println("return %s.asList(", Arrays.class);

                        forEach(indexes, "", ", ", (index, separator) -> {
                            printCreateIndex(out, index);
                            out.println("%s", separator);
                        });

                        out.println(");");
                        out.println("}");
                    }
                }
            }
        }

        // Add primary / unique / foreign key information
        if (generateRelations()) {
            IdentityDefinition identity = table.getIdentity();

            // The identity column
            if (identity != null) {
                final String identityTypeFull = getJavaType(identity.getColumn().getType(resolver(out)), out);
                final String identityType = out.ref(identityTypeFull);

                if (scala) {
                    out.println();

                    printDeprecationIfUnknownType(out, identityTypeFull);
                    out.println("%soverride def getIdentity: %s[%s, %s] = super.getIdentity.asInstanceOf[ %s[%s, %s] ]",
                        visibilityPublic(), Identity.class, recordType, identityType, Identity.class, recordType, identityType);
                }
                else if (kotlin) {
                    printDeprecationIfUnknownType(out, identityTypeFull);
                    out.println("%soverride fun getIdentity(): %s<%s, %s?> = super.getIdentity() as %s<%s, %s?>",
                        visibilityPublic(), Identity.class, recordType, identityType, Identity.class, recordType, identityType);
                }
                else {
                    if (printDeprecationIfUnknownType(out, identityTypeFull))
                        out.override();
                    else
                        out.overrideInherit();

                    printNonnullAnnotation(out);
                    out.println("%s%s<%s, %s> getIdentity() {", visibilityPublic(), Identity.class, recordType, identityType);
                    out.println("return (%s<%s, %s>) super.getIdentity();", Identity.class, recordType, identityType);
                    out.println("}");
                }
            }

            // The primary / main unique key
            if (primaryKey != null) {
                final String keyFullId = generateGlobalKeyReferences()
                    ? kotlin
                        ? out.ref(getStrategy().getFullJavaIdentifier(primaryKey))
                        : out.ref(getStrategy().getFullJavaIdentifier(primaryKey), 2)
                    : null;

                if (scala) {
                    out.println();
                    out.print("%soverride def getPrimaryKey: %s[%s] = ", visibilityPublic(), UniqueKey.class, recordType);

                    if (keyFullId != null)
                        out.print("%s", keyFullId);
                    else
                        printCreateUniqueKey(out, primaryKey);

                    out.println();
                }
                else if (kotlin) {
                    out.print("%soverride fun getPrimaryKey(): %s<%s> = ", visibilityPublic(), UniqueKey.class, recordType);

                    if (keyFullId != null)
                        out.print("%s", keyFullId);
                    else
                        printCreateUniqueKey(out, primaryKey);

                    out.println();
                }
                else {
                    out.overrideInherit();
                    printNonnullAnnotation(out);
                    out.println("%s%s<%s> getPrimaryKey() {", visibilityPublic(), UniqueKey.class, recordType);
                    out.print("return ");

                    if (keyFullId != null)
                        out.print("%s", keyFullId);
                    else
                        printCreateUniqueKey(out, primaryKey);

                    out.println(";");
                    out.println("}");
                }
            }

            // The remaining unique keys
            List<UniqueKeyDefinition> uniqueKeys = table.getUniqueKeys();
            if (uniqueKeys.size() > 0) {
                if (generateGlobalKeyReferences()) {
                    final List<String> keyFullIds = kotlin
                        ? out.ref(getStrategy().getFullJavaIdentifiers(uniqueKeys))
                        : out.ref(getStrategy().getFullJavaIdentifiers(uniqueKeys), 2);

                    if (scala) {
                        out.println();
                        out.println("%soverride def getUniqueKeys: %s[ %s[%s] ] = %s.asList[ %s[%s] ]([[%s]])",
                            visibilityPublic(), List.class, UniqueKey.class, recordType, Arrays.class, UniqueKey.class, recordType, keyFullIds);
                    }
                    else if (kotlin) {
                        out.println("%soverride fun getUniqueKeys(): %s<%s<%s>> = listOf([[%s]])",
                            visibilityPublic(), out.ref(KLIST), UniqueKey.class, recordType, keyFullIds);
                    }
                    else {
                        out.overrideInherit();
                        printNonnullAnnotation(out);
                        out.println("%s%s<%s<%s>> getUniqueKeys() {", visibilityPublic(), List.class, UniqueKey.class, recordType);
                        out.println("return %s.asList([[%s]]);", Arrays.class, keyFullIds);
                        out.println("}");
                    }
                }
                else {
                    if (scala) {
                        out.println();
                        out.println("%soverride def getUniqueKeys: %s[ %s[%s] ] = %s.asList[ %s[%s] ](",
                            visibilityPublic(), List.class, UniqueKey.class, recordType, Arrays.class, UniqueKey.class, recordType);

                        forEach(uniqueKeys, "", ", ", (uniqueKey, separator) -> {
                            printCreateUniqueKey(out, uniqueKey);
                            out.println("%s", separator);
                        });

                        out.println(")");
                    }
                    else if (kotlin) {
                        out.println("%soverride fun getUniqueKeys(): %s<%s<%s>> = listOf(",
                            visibilityPublic(), out.ref(KLIST), UniqueKey.class, recordType);

                        forEach(uniqueKeys, "", ", ", (uniqueKey, separator) -> {
                            printCreateUniqueKey(out, uniqueKey);
                            out.println("%s", separator);
                        });

                        out.println(")");
                    }
                    else {
                        out.overrideInherit();
                        printNonnullAnnotation(out);
                        out.println("%s%s<%s<%s>> getUniqueKeys() {", visibilityPublic(), List.class, UniqueKey.class, recordType);
                        out.println("return %s.asList(", Arrays.class);

                        forEach(uniqueKeys, "", ", ", (uniqueKey, separator) -> {
                            printCreateUniqueKey(out, uniqueKey);
                            out.println("%s", separator);
                        });

                        out.println(");");
                        out.println("}");
                    }
                }
            }

            // Foreign keys
            List<ForeignKeyDefinition> outboundFKs = table.getForeignKeys();
            Set<String> outboundKeyMethodNames = new HashSet<>();

            // [#7554] [#8028] Not yet supported with global key references turned off
            if (outboundFKs.size() > 0 && generateGlobalKeyReferences()) {
                final List<String> keyFullIds = kotlin
                    ? out.ref(getStrategy().getFullJavaIdentifiers(outboundFKs))
                    : out.ref(getStrategy().getFullJavaIdentifiers(outboundFKs), 2);

                if (scala) {
                    out.println();
                    out.println("%soverride def getReferences: %s[ %s[%s, _] ] = %s.asList[ %s[%s, _] ]([[%s]])",
                        visibilityPublic(), List.class, ForeignKey.class, recordType, Arrays.class, ForeignKey.class, recordType, keyFullIds);
                }
                else if (kotlin) {
                    out.println("%soverride fun getReferences(): %s<%s<%s, *>> = listOf([[%s]])",
                        visibilityPublic(), out.ref(KLIST), ForeignKey.class, recordType, keyFullIds);
                }
                else {
                    out.overrideInherit();
                    printNonnullAnnotation(out);
                    out.println("%s%s<%s<%s, ?>> getReferences() {", visibilityPublic(), List.class, ForeignKey.class, recordType);
                    out.println("return %s.asList([[%s]]);", Arrays.class, keyFullIds);
                    out.println("}");
                }

                // Outbound (to-one) implicit join paths
                if (generateImplicitJoinPathsToOne()) {
                    Map<TableDefinition, Long> pathCounts = outboundFKs.stream().collect(groupingBy(ForeignKeyDefinition::getReferencedTable, counting()));

                    // [#8762] Cache these calls for much improved runtime performance!
                    for (ForeignKeyDefinition foreignKey : outboundFKs) {
                        final String keyMethodName = out.ref(getStrategy().getJavaMethodName(foreignKey));
                        final String referencedTableClassName = out.ref(
                              getStrategy().getFullJavaClassName(foreignKey.getReferencedTable())
                            + (generateImplicitJoinPathTableSubtypes() ? ("." + getStrategy().getJavaClassName(foreignKey.getReferencedTable()) + "Path") : "")
                        );
                        final String keyFullId = kotlin
                            ? out.ref(getStrategy().getFullJavaIdentifier(foreignKey))
                            : out.ref(getStrategy().getFullJavaIdentifier(foreignKey), 2);

                        // [#13008] Prevent conflicts with the below leading underscore
                        final String unquotedKeyMethodName = keyMethodName.replace("`", "");

                        if (scala) {}
                        else {
                            out.println();

                            outboundKeyMethodNames.add(keyMethodName);

                            if (kotlin)
                                out.println("private lateinit var _%s: %s", unquotedKeyMethodName, referencedTableClassName);
                            else
                                out.println("private transient %s _%s;", referencedTableClassName, keyMethodName);
                        }

                        out.javadoc(
                            "Get the implicit join path to the <code>" + foreignKey.getReferencedTable().getQualifiedName() + "</code> table"
                          + (pathCounts.get(foreignKey.getReferencedTable()) > 1 ? ", via the <code>" + foreignKey.getInputName() + "</code> key" : "")
                          + "."
                        );

                        if (scala) {
                            out.println("%slazy val %s: %s = { new %s(this, %s, null) }", visibility(), scalaWhitespaceSuffix(keyMethodName), referencedTableClassName, referencedTableClassName, keyFullId);
                        }
                        else if (kotlin) {
                            out.println("%sfun %s(): %s {", visibility(), keyMethodName, referencedTableClassName);
                            out.println("if (!this::_%s.isInitialized)", unquotedKeyMethodName);
                            out.println("_%s = %s(this, %s, null)", unquotedKeyMethodName, referencedTableClassName, keyFullId);
                            out.println();
                            out.println("return _%s;", unquotedKeyMethodName);
                            out.println("}");

                            if (generateImplicitJoinPathsAsKotlinProperties()) {
                                out.println();
                                out.println("%sval %s: %s", visibility(), keyMethodName, referencedTableClassName);
                                out.tab(1).println("get(): %s = %s()", referencedTableClassName, keyMethodName);
                            }
                        }
                        else {
                            out.println("%s%s %s() {", visibility(), referencedTableClassName, keyMethodName);
                            out.println("if (_%s == null)", keyMethodName);
                            out.println("_%s = new %s(this, %s, null);", keyMethodName, referencedTableClassName, keyFullId);
                            out.println();
                            out.println("return _%s;", keyMethodName);
                            out.println("}");
                        }
                    }
                }
            }

            if (generateImplicitJoinPathsToMany() && generateGlobalKeyReferences()) {
                List<InverseForeignKeyDefinition> inboundFKs = table.getInverseForeignKeys();

                if (inboundFKs.size() > 0) {
                    Map<TableDefinition, Long> pathCounts = inboundFKs.stream().collect(groupingBy(InverseForeignKeyDefinition::getReferencingTable, counting()));

                    inboundFKLoop:
                    for (InverseForeignKeyDefinition foreignKey : inboundFKs) {
                        final String keyMethodName = out.ref(getStrategy().getJavaMethodName(foreignKey));

                        if (!outboundKeyMethodNames.add(keyMethodName)) {
                            log.warn("Ambiguous key name",
                                "The database object " + foreignKey.getQualifiedOutputName()
                              + " generates an inbound key method name " + keyMethodName
                              + " which conflicts with the previously generated outbound key method name."
                              + " Use a custom generator strategy to disambiguate the types. More information here:\n"
                              + " - https://www.jooq.org/doc/latest/manual/code-generation/codegen-generatorstrategy/\n"
                              + " - https://www.jooq.org/doc/latest/manual/code-generation/codegen-matcherstrategy/"
                            );
                            continue inboundFKLoop;
                        }

                        final String keyFullId = kotlin
                            ? out.ref(getStrategy().getFullJavaIdentifier(foreignKey))
                            : out.ref(getStrategy().getFullJavaIdentifier(foreignKey), 2);
                        final String referencingTableClassName = out.ref(
                            getStrategy().getFullJavaClassName(foreignKey.getReferencingTable())
                            + (generateImplicitJoinPathTableSubtypes() ? ("." + getStrategy().getJavaClassName(foreignKey.getReferencingTable()) + "Path") : "")
                        );

                        // [#13008] Prevent conflicts with the below leading underscore
                        final String unquotedKeyMethodName = keyMethodName.replace("`", "");

                        if (scala) {}
                        else {
                            out.println();

                            if (kotlin)
                                out.println("private lateinit var _%s: %s", unquotedKeyMethodName, referencingTableClassName);
                            else
                                out.println("private transient %s _%s;", referencingTableClassName, keyMethodName);
                        }

                        out.javadoc(
                            "Get the implicit to-many join path to the <code>" + foreignKey.getReferencingTable().getQualifiedName() + "</code> table"
                          + (pathCounts.get(foreignKey.getReferencingTable()) > 1 ? ", via the <code>" + foreignKey.getInputName() + "</code> key" : "")
                        );

                        if (scala) {
                            out.println("%slazy val %s: %s = { new %s(this, null, %s.getInverseKey()) }", visibility(), scalaWhitespaceSuffix(keyMethodName), referencingTableClassName, referencingTableClassName, keyFullId);
                        }
                        else if (kotlin) {
                            out.println("%sfun %s(): %s {", visibility(), keyMethodName, referencingTableClassName);
                            out.println("if (!this::_%s.isInitialized)", unquotedKeyMethodName);
                            out.println("_%s = %s(this, null, %s.inverseKey)", unquotedKeyMethodName, referencingTableClassName, keyFullId);
                            out.println();
                            out.println("return _%s;", unquotedKeyMethodName);
                            out.println("}");

                            if (generateImplicitJoinPathsAsKotlinProperties()) {
                                out.println();
                                out.println("%sval %s: %s", visibility(), keyMethodName, referencingTableClassName);
                                out.tab(1).println("get(): %s = %s()", referencingTableClassName, keyMethodName);
                            }
                        }
                        else {
                            out.println("%s%s %s() {", visibility(), referencingTableClassName, keyMethodName);
                            out.println("if (_%s == null)", keyMethodName);
                            out.println("_%s = new %s(this, null, %s.getInverseKey());", keyMethodName, referencingTableClassName, keyFullId);
                            out.println();
                            out.println("return _%s;", keyMethodName);
                            out.println("}");
                        }
                    }

                    List<ManyToManyKeyDefinition> manyToManyKeys = table.getManyToManyKeys();
                    Map<TableDefinition, Long> pathCountsManytoMany = manyToManyKeys.stream().collect(groupingBy(d -> d.getForeignKey2().getReferencedTable(), counting()));

                    manyToManyKeyLoop:
                    for (ManyToManyKeyDefinition manyToManyKey : manyToManyKeys) {
                        final String keyMethodName = out.ref(getStrategy().getJavaMethodName(manyToManyKey));

                        if (!outboundKeyMethodNames.add(keyMethodName)) {
                            log.warn("Ambiguous key name",
                                "The database object " + manyToManyKey.getQualifiedOutputName()
                              + " generates an inbound key method name " + keyMethodName
                              + " which conflicts with the previously generated outbound key method name."
                              + " Use a custom generator strategy to disambiguate the types. More information here:\n"
                              + " - https://www.jooq.org/doc/latest/manual/code-generation/codegen-generatorstrategy/\n"
                              + " - https://www.jooq.org/doc/latest/manual/code-generation/codegen-matcherstrategy/"
                            );
                            continue manyToManyKeyLoop;
                        }

                        final String key1MethodName = out.ref(getStrategy().getJavaMethodName(manyToManyKey.getForeignKey1().getInverse()));
                        final String key2MethodName = out.ref(getStrategy().getJavaMethodName(manyToManyKey.getForeignKey2()));
                        final TableDefinition referencedTable = manyToManyKey.getForeignKey2().getReferencedTable();
                        final String referencedTableClassName = out.ref(
                            getStrategy().getFullJavaClassName(referencedTable)
                            + (generateImplicitJoinPathTableSubtypes() ? ("." + getStrategy().getJavaClassName(referencedTable) + "Path") : "")
                        );

                        out.javadoc(
                            "Get the implicit many-to-many join path to the <code>" + referencedTable.getQualifiedName() + "</code> table"
                          + (pathCountsManytoMany.get(referencedTable) > 1 ? ", via the <code>" + manyToManyKey.getInputName() + "</code> key" : "")
                        );

                        if (scala) {
                            out.println("%sdef %s: %s = %s.%s", visibility(), scalaWhitespaceSuffix(keyMethodName), referencedTableClassName, key1MethodName, key2MethodName);
                        }
                        else if (kotlin) {
                            if (generateImplicitJoinPathsAsKotlinProperties()) {
                                out.println("%sval %s: %s", visibility(), keyMethodName, referencedTableClassName);
                                out.tab(1).println("get(): %s = %s().%s()", referencedTableClassName, key1MethodName, key2MethodName);
                            }
                            else {
                                out.println();
                                out.println("%sfun %s(): %s = %s().%s()", visibility(), keyMethodName, referencedTableClassName, key1MethodName, key2MethodName);
                            }
                        }
                        else {
                            out.println("%s%s %s() {", visibility(), referencedTableClassName, keyMethodName);
                            out.println("return %s().%s();", key1MethodName, key2MethodName);
                            out.println("}");
                        }
                    }
                }
            }
        }

        List<CheckConstraintDefinition> cc = table.getCheckConstraints();

        if (!cc.isEmpty()) {
            if (scala) {
                out.println("%soverride def getChecks: %s[ %s[%s] ] = %s.asList[ %s[%s] ](",
                    visibilityPublic(), List.class, Check.class, recordType, Arrays.class, Check.class, recordType);
            }
            else if (kotlin) {
                out.println("%soverride fun getChecks(): %s<%s<%s>> = listOf(",
                    visibilityPublic(), out.ref(KLIST), Check.class, recordType);
            }
            else {
                out.overrideInherit();
                printNonnullAnnotation(out);
                out.println("%s%s<%s<%s>> getChecks() {", visibilityPublic(), List.class, Check.class, recordType);
                out.println("return %s.asList(", Arrays.class);
            }

            forEach(cc, (c, separator) -> {
                out.println("%s.createCheck(this, %s.name(\"%s\"), \"%s\", %s)%s", Internal.class, DSL.class, escapeString(c.getName()), escapeString(c.getCheckClause()), c.enforced(), separator);
            });

            if (scala || kotlin) {
                out.println(")");
            }
            else {
                out.println(");");
                out.println("}");
            }
        }

        // [#1596] Updatable tables can provide fields for optimistic locking if properly configured.
        // [#7904] Records being updatable isn't a strict requirement. Version and timestamp values
        //         can still be generated
        versionLoop: for (String pattern : database.getRecordVersionFields()) {
            Pattern p = Pattern.compile(pattern, Pattern.COMMENTS);

            for (ColumnDefinition column : table.getColumns()) {
                if ((p.matcher(column.getName()).matches() ||
                     p.matcher(column.getQualifiedName()).matches())) {

                    final String columnTypeFull = getJavaType(column.getType(resolver(out)), out);
                    final String columnType = out.ref(columnTypeFull);
                    final String columnId = getStrategy().getJavaIdentifier(column);

                    if (scala) {
                        printDeprecationIfUnknownType(out, columnTypeFull);
                        out.println("%soverride def getRecordVersion: %s[%s, %s] = %s", visibilityPublic(), TableField.class, recordType, columnType, columnId);
                    }
                    else if (kotlin) {
                        printDeprecationIfUnknownType(out, columnTypeFull);
                        out.println("%soverride fun getRecordVersion(): %s<%s, %s?> = %s", visibilityPublic(), TableField.class, recordType, columnType, columnId);
                    }
                    else {
                        if (printDeprecationIfUnknownType(out, columnTypeFull))
                            out.override();
                        else
                            out.overrideInherit();

                        printNonnullAnnotation(out);
                        out.println("%s%s<%s, %s> getRecordVersion() {", visibilityPublic(), TableField.class, recordType, columnType);
                        out.println("return %s;", columnId);
                        out.println("}");
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

                    final String columnTypeFull = getJavaType(column.getType(resolver(out)), out);
                    final String columnType = out.ref(columnTypeFull);
                    final String columnId = getStrategy().getJavaIdentifier(column);

                    if (scala) {
                        printDeprecationIfUnknownType(out, columnTypeFull);
                        out.println("%soverride def getRecordTimestamp: %s[%s, %s] = %s", visibilityPublic(), TableField.class, recordType, columnType, columnId);
                    }
                    else if (kotlin) {
                        printDeprecationIfUnknownType(out, columnTypeFull);
                        out.println("%soverride fun getRecordTimestamp(): %s<%s, %s?> = %s", visibilityPublic(), TableField.class, recordType, columnType, columnId);
                    }
                    else {
                        if (printDeprecationIfUnknownType(out, columnTypeFull))
                            out.override();
                        else
                            out.overrideInherit();

                        printNonnullAnnotation(out);
                        out.println("%s%s<%s, %s> getRecordTimestamp() {", visibilityPublic(), TableField.class, recordType, columnType);
                        out.println("return %s;", columnId);
                        out.println("}");
                    }

                    // Avoid generating this method twice
                    break timestampLoop;
                }
            }
        }

        if (generateAsMethodOverrides()) {
            if (scala) {
                out.print("%soverride def as(alias: %s): %s = ", visibilityPublic(), String.class, className);

                if (table.isTableValuedFunction())
                    out.println("new %s(%s.name(alias), null, null, null, this, parameters, null)", className, DSL.class);
                else
                    out.println("new %s(%s.name(alias), this)", className, DSL.class);

                out.print("%soverride def as(alias: %s): %s = ", visibilityPublic(), Name.class, className);

                if (table.isTableValuedFunction())
                    out.println("new %s(alias, null, null, null, this, parameters, null)", className);
                else
                    out.println("new %s(alias, this)", className);

                out.print("%soverride def as(alias: %s[_]): %s = ", visibilityPublic(), Table.class, className);

                if (table.isTableValuedFunction())
                    out.println("new %s(alias.getQualifiedName(), null, null, null, this, parameters, null)", className);
                else
                    out.println("new %s(alias.getQualifiedName(), this)", className);
            }
            else if (kotlin) {
                out.print("%soverride fun `as`(alias: %s): %s = ", visibilityPublic(), String.class, className);

                if (table.isTableValuedFunction())
                    out.println("%s(%s.name(alias), this, parameters)", className, DSL.class);
                else
                    out.println("%s(%s.name(alias), this)", className, DSL.class);

                out.print("%soverride fun `as`(alias: %s): %s = ", visibilityPublic(), Name.class, className);

                if (table.isTableValuedFunction())
                    out.println("%s(alias, this, parameters)", className);
                else
                    out.println("%s(alias, this)", className);

                out.print("%soverride fun `as`(alias: %s<*>): %s = ", visibilityPublic(), Table.class, className);

                if (table.isTableValuedFunction())
                    out.println("%s(alias.qualifiedName, this, parameters)", className);
                else
                    out.println("%s(alias.qualifiedName, this)", className);
            }

            // [#117] With instance fields, it makes sense to create a
            // type-safe table alias
            else if (generateInstanceFields()) {
                out.overrideInherit();
                printNonnullAnnotation(out);
                out.println("%s%s as(%s alias) {", visibilityPublic(), className, String.class);

                if (table.isTableValuedFunction())
                    out.println("return new %s(%s.name(alias), this, parameters);", className, DSL.class);
                else
                    out.println("return new %s(%s.name(alias), this);", className, DSL.class);

                out.println("}");


                out.overrideInherit();
                printNonnullAnnotation(out);
                out.println("%s%s as(%s alias) {", visibilityPublic(), className, Name.class);

                if (table.isTableValuedFunction())
                    out.println("return new %s(alias, this, parameters);", className);
                else
                    out.println("return new %s(alias, this);", className);

                out.println("}");


                out.overrideInherit();
                printNonnullAnnotation(out);
                out.println("%s%s as(%s<?> alias) {", visibilityPublic(), className, Table.class);

                if (table.isTableValuedFunction())
                    out.println("return new %s(alias.getQualifiedName(), this, parameters);", className);
                else
                    out.println("return new %s(alias.getQualifiedName(), this);", className);

                out.println("}");
            }
        }

        if (scala) {
            if (generateRenameMethodOverrides()) {
                out.javadoc("Rename this table");
                out.print("%soverride def rename(name: %s): %s = ", visibilityPublic(), String.class, className);

                if (table.isTableValuedFunction())
                    out.println("new %s(%s.name(name), null, null, null, null, parameters, null)", className, DSL.class);
                else
                    out.println("new %s(%s.name(name), null)", className, DSL.class);

                out.javadoc("Rename this table");
                out.print("%soverride def rename(name: %s): %s = ", visibilityPublic(), Name.class, className);

                if (table.isTableValuedFunction())
                    out.println("new %s(name, null, null, null, null, parameters, null)", className);
                else
                    out.println("new %s(name, null)", className);

                out.javadoc("Rename this table");
                out.print("%soverride def rename(name: %s[_]): %s = ", visibilityPublic(), Table.class, className);

                if (table.isTableValuedFunction())
                    out.println("new %s(name.getQualifiedName(), null, null, null, null, parameters, null)", className);
                else
                    out.println("new %s(name.getQualifiedName(), null)", className);
            }

            if (generateWhereMethodOverrides() && !table.isTableValuedFunction()) {
                Consumer<Runnable> idt = r -> {
                    out.javadoc("Create an inline derived table from this table");
                    r.run();
                };

                idt.accept(() -> out.println("%soverride def where(condition: %s): %s = new %s(getQualifiedName(), if (aliased()) this else null, condition)", visibilityPublic(), Condition.class, className, className));
                idt.accept(() -> out.println("%soverride def where(conditions: %s[_ <: %s]): %s = where(%s.and(conditions))", visibilityPublic(), Collection.class, Condition.class, className, DSL.class));
                idt.accept(() -> out.println("%soverride def where(conditions: %s*): %s = where(%s.and(conditions:_*))", visibilityPublic(), Condition.class, className, DSL.class));
                idt.accept(() -> out.println("%soverride def where(condition: %s[%s]): %s = where(%s.condition(condition))", visibilityPublic(), Field.class, Boolean.class, className, DSL.class));
                idt.accept(() -> out.println("@%s %soverride def where(condition: %s): %s = where(%s.condition(condition))", PlainSQL.class, visibilityPublic(), SQL.class, className, DSL.class));
                idt.accept(() -> out.println("@%s %soverride def where(@%s.SQL condition: %s): %s = where(%s.condition(condition))", PlainSQL.class, visibilityPublic(), Stringly.class, String.class, className, DSL.class));
                idt.accept(() -> out.println("@%s %soverride def where(@%s.SQL condition: %s, binds: AnyRef*): %s = where(%s.condition(condition, binds:_*))", PlainSQL.class, visibilityPublic(), Stringly.class, String.class, className, DSL.class));
                // This produces the same erasure as the previous, in scala:
                // (condition: String, binds: Seq)
                // idt.accept(() -> out.println("@%s %soverride def where(@%s.SQL condition: %s, parts: %s*): %s = where(%s.condition(condition, parts:_*))", PlainSQL.class, visibilityPublic(), Stringly.class, String.class, QueryPart.class, className, DSL.class));
                idt.accept(() -> out.println("%soverride def whereExists(select: %s[_]): %s = where(%s.exists(select))", visibilityPublic(), Select.class, className, DSL.class));
                idt.accept(() -> out.println("%soverride def whereNotExists(select: %s[_]): %s = where(%s.notExists(select))", visibilityPublic(), Select.class, className, DSL.class));
            }
        }

        else if (kotlin) {
            if (generateRenameMethodOverrides()) {
                out.javadoc("Rename this table");
                out.print("%soverride fun rename(name: %s): %s = ", visibilityPublic(), String.class, className);

                if (table.isTableValuedFunction())
                    out.println("%s(%s.name(name), null, parameters)", className, DSL.class);
                else
                    out.println("%s(%s.name(name), null)", className, DSL.class);

                out.javadoc("Rename this table");
                out.print("%soverride fun rename(name: %s): %s = ", visibilityPublic(), Name.class, className);

                if (table.isTableValuedFunction())
                    out.println("%s(name, null, parameters)", className);
                else
                    out.println("%s(name, null)", className);

                out.javadoc("Rename this table");
                out.print("%soverride fun rename(name: %s<*>): %s = ", visibilityPublic(), Table.class, className);

                if (table.isTableValuedFunction())
                    out.println("%s(name.qualifiedName, null, parameters)", className);
                else
                    out.println("%s(name.qualifiedName, null)", className);
            }

            if (generateWhereMethodOverrides() && !table.isTableValuedFunction()) {
                Consumer<Runnable> idt = r -> {
                    out.javadoc("Create an inline derived table from this table");
                    r.run();
                };

                idt.accept(() -> out.println("%soverride fun where(condition: %s): %s = %s(qualifiedName, if (aliased()) this else null, condition)", visibilityPublic(), Condition.class, className, className));
                idt.accept(() -> out.println("%soverride fun where(conditions: %s<%s>): %s = where(%s.and(conditions))", visibilityPublic(), out.ref("kotlin.collections.Collection"), Condition.class, className, DSL.class));
                idt.accept(() -> out.println("%soverride fun where(vararg conditions: %s): %s = where(%s.and(*conditions))", visibilityPublic(), Condition.class, className, DSL.class));
                idt.accept(() -> out.println("%soverride fun where(condition: %s<%s?>): %s = where(%s.condition(condition))", visibilityPublic(), Field.class, Boolean.class, className, DSL.class));
                idt.accept(() -> out.println("@%s %soverride fun where(condition: %s): %s = where(%s.condition(condition))", PlainSQL.class, visibilityPublic(), SQL.class, className, DSL.class));
                idt.accept(() -> out.println("@%s %soverride fun where(@%s.SQL condition: %s): %s = where(%s.condition(condition))", PlainSQL.class, visibilityPublic(), Stringly.class, String.class, className, DSL.class));
                idt.accept(() -> out.println("@%s %soverride fun where(@%s.SQL condition: %s, vararg binds: Any?): %s = where(%s.condition(condition, *binds))", PlainSQL.class, visibilityPublic(), Stringly.class, String.class, className, DSL.class));
                idt.accept(() -> out.println("@%s %soverride fun where(@%s.SQL condition: %s, vararg parts: %s): %s = where(%s.condition(condition, *parts))", PlainSQL.class, visibilityPublic(), Stringly.class, String.class, QueryPart.class, className, DSL.class));
                idt.accept(() -> out.println("%soverride fun whereExists(select: %s<*>): %s = where(%s.exists(select))", visibilityPublic(), Select.class, className, DSL.class));
                idt.accept(() -> out.println("%soverride fun whereNotExists(select: %s<*>): %s = where(%s.notExists(select))", visibilityPublic(), Select.class, className, DSL.class));
            }
        }

        // [#2921] With instance fields, tables can be renamed.
        else if (generateInstanceFields()) {
            if (generateRenameMethodOverrides()) {
                out.javadoc("Rename this table");
                out.override();
                printNonnullAnnotation(out);
                out.println("%s%s rename(%s name) {", visibilityPublic(), className, String.class);

                if (table.isTableValuedFunction())
                    out.println("return new %s(%s.name(name), null, parameters);", className, DSL.class);
                else
                    out.println("return new %s(%s.name(name), null);", className, DSL.class);

                out.println("}");

                out.javadoc("Rename this table");
                out.override();
                printNonnullAnnotation(out);
                out.println("%s%s rename(%s name) {", visibilityPublic(), className, Name.class);

                if (table.isTableValuedFunction())
                    out.println("return new %s(name, null, parameters);", className);
                else
                    out.println("return new %s(name, null);", className);

                out.println("}");

                out.javadoc("Rename this table");
                out.override();
                printNonnullAnnotation(out);
                out.println("%s%s rename(%s<?> name) {", visibilityPublic(), className, Table.class);

                if (table.isTableValuedFunction())
                    out.println("return new %s(name.getQualifiedName(), null, parameters);", className);
                else
                    out.println("return new %s(name.getQualifiedName(), null);", className);

                out.println("}");
            }

            if (generateWhereMethodOverrides() && !table.isTableValuedFunction()) {
                Consumer<Runnable> idt = r -> {
                    out.javadoc("Create an inline derived table from this table");
                    out.override();
                    printNonnullAnnotation(out);
                    r.run();
                };

                idt.accept(() -> {
                    out.println("%s%s where(%s condition) {", visibilityPublic(), className, Condition.class);
                    out.println("return new %s(getQualifiedName(), aliased() ? this : null, null, condition);", className);
                    out.println("}");
                });
                idt.accept(() -> {
                    out.println("%s%s where(%s<? extends %s> conditions) {", visibilityPublic(), className, Collection.class, Condition.class);
                    out.println("return where(%s.and(conditions));", DSL.class);
                    out.println("}");
                });
                idt.accept(() -> {
                    out.println("%s%s where(%s... conditions) {", visibilityPublic(), className, Condition.class);
                    out.println("return where(%s.and(conditions));", DSL.class);
                    out.println("}");
                });
                idt.accept(() -> {
                    out.println("%s%s where(%s<%s> condition) {", visibilityPublic(), className, Field.class, Boolean.class);
                    out.println("return where(%s.condition(condition));", DSL.class);
                    out.println("}");
                });
                idt.accept(() -> {
                    out.println("@%s", PlainSQL.class);
                    out.println("%s%s where(%s condition) {", visibilityPublic(), className, SQL.class);
                    out.println("return where(%s.condition(condition));", DSL.class);
                    out.println("}");
                });
                idt.accept(() -> {
                    out.println("@%s", PlainSQL.class);
                    out.println("%s%s where(@%s.SQL %s condition) {", visibilityPublic(), className, Stringly.class, String.class);
                    out.println("return where(%s.condition(condition));", DSL.class);
                    out.println("}");
                });
                idt.accept(() -> {
                    out.println("@%s", PlainSQL.class);
                    out.println("%s%s where(@%s.SQL %s condition, %s... binds) {", visibilityPublic(), className, Stringly.class, String.class, Object.class);
                    out.println("return where(%s.condition(condition, binds));", DSL.class);
                    out.println("}");
                });
                idt.accept(() -> {
                    out.println("@%s", PlainSQL.class);
                    out.println("%s%s where(@%s.SQL %s condition, %s... parts) {", visibilityPublic(), className, Stringly.class, String.class, QueryPart.class);
                    out.println("return where(%s.condition(condition, parts));", DSL.class);
                    out.println("}");
                });
                idt.accept(() -> {
                    out.println("%s%s whereExists(%s<?> select) {", visibilityPublic(), className, Select.class);
                    out.println("return where(%s.exists(select));", DSL.class);
                    out.println("}");
                });
                idt.accept(() -> {
                    out.println("%s%s whereNotExists(%s<?> select) {", visibilityPublic(), className, Select.class);
                    out.println("return where(%s.notExists(select));", DSL.class);
                    out.println("}");
                });
            }
        }


























        // [#7809] fieldsRow()
        // [#10481] Use the types from replaced embeddables if applicable
        List<Definition> replacingEmbeddablesAndUnreplacedColumns = replacingEmbeddablesAndUnreplacedColumns(table);
        int degree = replacingEmbeddablesAndUnreplacedColumns.size();
        int referencedDegree = replacingEmbeddablesAndUnreplacedColumns(table.getReferencedTable()).size();

        String rowType = refRowType(out, replacingEmbeddablesAndUnreplacedColumns);
        String rowTypeContravariantJava = refRowType(out, replacingEmbeddablesAndUnreplacedColumns, s -> "? super " + s);

        if (generateRecordsImplementingRecordN() && degree > 0 && degree <= Constants.MAX_ROW_DEGREE) {
            final String rowNType = out.ref(Row.class.getName() + degree);

            out.header("Row%s type methods", degree);
            if (scala) {
                out.println("%soverride def fieldsRow: %s[%s] = super.fieldsRow.asInstanceOf[ %s[%s] ]",
                    visibilityPublic(), rowNType, rowType, rowNType, rowType);
            }
            else if (kotlin) {
                out.println("%soverride fun fieldsRow(): %s<%s> = super.fieldsRow() as %s<%s>",
                    visibilityPublic(), rowNType, rowType, rowNType, rowType);
            }
            else {
                out.overrideInherit();
                printNonnullAnnotation(out);
                out.println("%s%s<%s> fieldsRow() {", visibilityPublic(), rowNType, rowType);
                out.println("return (%s) super.fieldsRow();", rowNType);
                out.println("}");
            }
        }

        // [#1070] Table-valued functions should generate an additional set of call() methods
        if (table.isTableValuedFunction()) {
            for (boolean parametersAsField : new boolean[] { false, true }) {

                // Don't overload no-args call() methods
                if (parametersAsField && parameters.size() == 0)
                    break;

                out.javadoc("Call this table-valued function");

                if (scala) {
                    out.print("%sdef call(", visibility()).printlnIf(!parameters.isEmpty());
                    printParameterDeclarations(out, parameters, parametersAsField, "  ");
                    out.print("): %s = ", className);

                    out.print("Option(new %s(%s.name(\"%s\"), null, null, null, null, %s(", className, DSL.class, escapeString(table.getOutputName()), out.ref("scala.Array")).printlnIf(!parameters.isEmpty());

                    forEach(parameters, (parameter, separator) -> {
                        final String paramArgName = getStrategy().getJavaMemberName(parameter);
                        final String paramTypeRef = getJavaTypeReference(parameter.getDatabase(), parameter.getType(resolver(out)), out);
                        final List<String> converter = out.ref(list(parameter.getType(resolver(out)).getConverter()));
                        final List<String> binding = out.ref(list(parameter.getType(resolver(out)).getBinding()));

                        if (parametersAsField)
                            out.println("%s%s", paramArgName, separator);
                        else
                            out.println("%s.value(%s, %s" + converterTemplateForTableValuedFunction(converter) + converterTemplateForTableValuedFunction(binding) + ")%s", DSL.class, paramArgName, paramTypeRef, converter, binding, separator);
                    });

                    out.println("), null)).map(r => if (aliased()) r.as(getUnqualifiedName) else r).get");
                }
                else if (kotlin) {
                    out.print("%sfun call(", visibility()).printlnIf(!parameters.isEmpty());
                    printParameterDeclarations(out, parameters, parametersAsField, "  ");
                    out.print("): %s = %s(%s.name(\"%s\"), null, arrayOf(", className, className, DSL.class, escapeString(table.getOutputName()), Field.class).printlnIf(!parameters.isEmpty());

                    forEach(parameters, (parameter, separator) -> {
                        final String paramArgName = getStrategy().getJavaMemberName(parameter);
                        final String paramTypeRef = getJavaTypeReference(parameter.getDatabase(), parameter.getType(resolver(out)), out);
                        final List<String> converter = out.ref(list(parameter.getType(resolver(out)).getConverter()));
                        final List<String> binding = out.ref(list(parameter.getType(resolver(out)).getBinding()));

                        if (parametersAsField)
                            out.println("%s%s", paramArgName, separator);
                        else
                            out.println("%s.value(%s, %s" + converterTemplateForTableValuedFunction(converter) + converterTemplateForTableValuedFunction(binding) + ")%s", DSL.class, paramArgName, paramTypeRef, converter, binding, separator);
                    });

                    out.println(")).let { if (aliased()) it.`as`(unqualifiedName) else it }");
                }
                else {
                    out.print("%s%s call(", visibility(), className).printlnIf(!parameters.isEmpty());
                    printParameterDeclarations(out, parameters, parametersAsField, "  ");
                    out.println(") {");

                    out.print("%s result = new %s(%s.name(\"%s\"), null, new %s[] {", className, className, DSL.class, escapeString(table.getOutputName()), Field.class).printlnIf(!parameters.isEmpty());

                    forEach(parameters, (parameter, separator) -> {
                        final String paramArgName = getStrategy().getJavaMemberName(parameter);
                        final String paramTypeRef = getJavaTypeReference(parameter.getDatabase(), parameter.getType(resolver(out)), out);
                        final List<String> converter = out.ref(list(parameter.getType(resolver(out)).getConverter()));
                        final List<String> binding = out.ref(list(parameter.getType(resolver(out)).getBinding()));

                        if (parametersAsField)
                            out.println("%s%s", paramArgName, separator);
                        else
                            out.println("%s.val(%s, %s" + converterTemplateForTableValuedFunction(converter) + converterTemplateForTableValuedFunction(binding) + ")%s", DSL.class, paramArgName, paramTypeRef, converter, binding, separator);
                    });

                    out.println("});");
                    out.println();
                    out.println("return aliased() ? result.as(getUnqualifiedName()) : result;");
                    out.println("}");
                }
            }
        }

        if (generateRecordsImplementingRecordN()
            && degree > 0
            && degree <= Constants.MAX_ROW_DEGREE

            // [#5405] [#14830] A table valued function might have a degree that
            //                  is different from the base table's Record type!
            && degree == referencedDegree
        ) {
            out.javadoc("Convenience mapping calling {@link %s#convertFrom(%s)}.", SelectField.class, Function.class);

            if (scala) {
                out.println("%sdef mapping[U](from: (" + rowType + ") => U): %s[U] = convertFrom(r => from.apply(" + rangeClosed(1, degree).mapToObj(i -> "r.value" + i + "()").collect(joining(", ")) + "))",
                    visibility(), SelectField.class);
            }
            else if (kotlin) {
                out.println("%sfun <U> mapping(from: (" + rowType + ") -> U): %s<U> = convertFrom(%s.mapping(from))",
                    visibility(), SelectField.class, Records.class);
            }
            else {
                out.println("%s<U> %s<U> mapping(%s<" + rowTypeContravariantJava + ", ? extends U> from) {",
                    visibility(), SelectField.class, out.ref("org.jooq.Function" + degree));
                out.println("return convertFrom(%s.mapping(from));", Records.class);
                out.println("}");
            }

            out.javadoc("Convenience mapping calling {@link %s#convertFrom(%s, %s)}.", SelectField.class, Class.class, Function.class);

            if (scala) {
                out.println("%sdef mapping[U](toType: %s[U], from: (" + rowType + ") => U): %s[U] = convertFrom(toType,r => from.apply(" + rangeClosed(1, degree).mapToObj(i -> "r.value" + i + "()").collect(joining(", ")) + "))",
                    visibility(), Class.class, SelectField.class);
            }
            else if (kotlin) {
                out.println("%sfun <U> mapping(toType: %s<U>, from: (" + rowType + ") -> U): %s<U> = convertFrom(toType, %s.mapping(from))",
                    visibility(), Class.class, SelectField.class, Records.class);
            }
            else {
                out.println("%s<U> %s<U> mapping(%s<U> toType, %s<" + rowTypeContravariantJava + ", ? extends U> from) {",
                    visibility(), SelectField.class, Class.class, out.ref("org.jooq.Function" + degree));
                out.println("return convertFrom(toType, %s.mapping(from));", Records.class);
                out.println("}");
            }
        }

        generateTableClassFooter(table, out);
        out.println("}");
        closeJavaWriter(out);
    }

    private Iterable<EmbeddableDefinition> embeddables(SchemaDefinition schema) {

        // [#6124] Prevent FKs from overriding PK embeddable
        LinkedHashSet<EmbeddableDefinition> embeddables = new LinkedHashSet<>(database.getEmbeddables(schema));
        for (EmbeddableDefinition embeddable : embeddables)

            // [#6124] [#10481] Don't generate embeddable types for FKs
            if (embeddable.getTable().equals(embeddable.getReferencingTable()))
                embeddables.add(embeddable);

        return embeddables;
    }

    protected void generateEmbeddables(SchemaDefinition schema) {
        log.info("Generating embeddables");

        Set<File> duplicates = new HashSet<>();
        for (EmbeddableDefinition embeddable : embeddables(schema)) {
            try {

                // [#6124] [#10481] <embeddableKeys/> map to the same embeddable for PKs/FKs.
                //                  The FKs are always listed after the PKs, so we can simply skip
                //                  unnecessary re-generations
                if (duplicates.add(getFile(embeddable, Mode.RECORD)))
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
        log.info("Generating embeddable", out.file().getName());
        generateRecord0(embeddable, out);
        closeJavaWriter(out);
    }

    protected void generateEmbeddablePojos(SchemaDefinition schema) {
        log.info("Generating embeddable POJOs");

        for (EmbeddableDefinition embeddable : embeddables(schema)) {
            try {
                generateEmbeddablePojo(embeddable);
            }
            catch (Exception e) {
                log.error("Error while generating embeddable POJO " + embeddable, e);
            }
        }

        watch.splitInfo("Embeddable POJOs generated");
    }

    /**
     * Subclasses may override this method to provide embeddable POJO class footer code.
     */
    @SuppressWarnings("unused")
    protected void generateEmbeddablePojoClassFooter(EmbeddableDefinition embeddable, JavaWriter out) {}

    /**
     * Subclasses may override this method to provide their own Javadoc.
     */
    protected void generateEmbeddablePojoClassJavadoc(EmbeddableDefinition embeddable, JavaWriter out) {
        if (generateCommentsOnEmbeddables())
            printClassJavadoc(out, embeddable);
        else
            printClassJavadoc(out, "The embeddable <code>" + embeddable.getQualifiedInputName() + "</code>.");
    }

    protected void generateEmbeddableInterfaces(SchemaDefinition schema) {
        log.info("Generating embeddable interfaces");

        for (EmbeddableDefinition embeddable : embeddables(schema)) {
            try {
                generateEmbeddableInterface(embeddable);
            }
            catch (Exception e) {
                log.error("Error while generating embeddable interface " + embeddable, e);
            }
        }

        watch.splitInfo("embeddable interfaces generated");
    }

    /**
     * Subclasses may override this method to provide embeddable interface class footer code.
     */
    @SuppressWarnings("unused")
    protected void generateEmbeddableInterfaceClassFooter(EmbeddableDefinition embeddable, JavaWriter out) {}

    /**
     * Subclasses may override this method to provide their own Javadoc.
     */
    protected void generateEmbeddableInterfaceClassJavadoc(EmbeddableDefinition embeddable, JavaWriter out) {
        if (generateCommentsOnEmbeddables())
            printClassJavadoc(out, embeddable);
        else
            printClassJavadoc(out, "The embeddable <code>" + embeddable.getQualifiedInputName() + "</code>.");
    }

    private String converterTemplate(List<String> converter) {
        if (converter == null || converter.isEmpty())
            return "[[]]";
        if (converter.size() > 1)
            throw new IllegalArgumentException();
        switch (GenerationUtil.expressionType(converter.get(0))) {
            case CONSTRUCTOR_REFERENCE:
                switch (language) {
                    case KOTLIN:
                        return "[[before=, ][%s()]]";

                    case JAVA:
                    case SCALA:
                    default:
                        return "[[before=, ][new %s()]]";
                }
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
                switch (language) {
                    case KOTLIN:
                        return "[[before=.asConvertedDataType(][after=)][%s()]]";

                    case JAVA:
                    case SCALA:
                    default:
                        return "[[before=.asConvertedDataType(][after=)][new %s()]]";
                }
            case EXPRESSION:
                return "[[before=.asConvertedDataType(][after=)][%s]]";
            default:
                throw new IllegalArgumentException();
        }
    }

    private boolean generateTextBlocks0() {
        switch (StringUtils.defaultIfNull(generateTextBlocks(), GeneratedTextBlocks.DETECT_FROM_JDK)) {
            case ON:
                return true;
            case OFF:
                return false;
            default:

                if (true)
                    return true;

                return false;
        }
    }

    private static final Pattern P_LEADING_WHITESPACE = Pattern.compile("\\r?\\n( +)");
    private static final int     MAX_STRING_LENGTH    = 16384;

    private String textBlock(String string) {
        if (string == null)
            return null;

        // [#10007] [#10318] Very long strings cannot be handled by the javac compiler.
        if (string.length() > MAX_STRING_LENGTH) {
            return "\"" + escapeString(string) + "\"";
        }

        // [#9817] Generate text blocks only if there are newlines or quotes
        else if (generateTextBlocks0() && (
                   string.contains("\\")
                || string.contains("\n")
                || string.contains("\r")
                || string.contains("\"")
        )) {
            // TODO [#3450] Escape sequences?
            // TODO [#10007] [#10318] Long textblocks?
            // TODO [#10869] String interpolation in kotlin?
            String result = string
                .replace("\\", "\\\\")
                .replace("\"\"\"", "\\\"\\\"\\\"");


            // Only Java has incidental whitespace support (?)
            // The first line may be ill-indented, because we read it from XML.
            // Depending on the reader, the content may have been trimmed.
            if (!result.startsWith(" ")) {
                Matcher matcher = P_LEADING_WHITESPACE.matcher(result);
                int indent = Integer.MAX_VALUE;

                while (matcher.find())
                    indent = Math.min(indent, matcher.group(1).length());

                result = (indent != Integer.MAX_VALUE ? " ".repeat(indent) : "") + result;
            }

            result = result.stripIndent();


            return "\"\"\"\n" + result + "\n\"\"\"";
        }
        else
            return "\"" + escapeString(string) + "\"";
    }

    private String escapeString(String string) {
        if (string == null)
            return null;

        // [#3450] Escape also the escape sequence, among other things that break Java strings.
        String result = string.replace("\\", "\\\\")
                              .replace("\"", "\\\"")
                              .replace("\n", "\\n")
                              .replace("\r", "\\r");

        // [#10869] Prevent string interpolation in Kotlin
        if (kotlin)
            result = result.replace("$", "\\$");

        // [#10007] [#10318] Very long strings cannot be handled by the javac compiler.
        int max = 16384;
        if (result.length() <= max)
            return result;

        StringBuilder sb = new StringBuilder("\" + \"");
        for (int i = 0; i < result.length(); i += max) {
            if (i > 0)
                sb.append("\".toString() + \"");

            sb.append(result, i, Math.min(i + max, result.length()));
        }

        return sb.append("\".toString() + \"").toString();
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
        JavaWriter out = newJavaWriter(getStrategy().getGlobalReferencesFile(schema, SequenceDefinition.class));
        out.refConflicts(getStrategy().getJavaIdentifiers(database.getSequences(schema)));

        printGlobalReferencesPackage(out, schema, SequenceDefinition.class);

        if (!kotlin) {
            printClassJavadoc(out, "Convenience access to all sequences in " + schemaNameOrDefault(schema) + ".");
            printClassAnnotations(out, schema, Mode.DEFAULT);
        }

        final String referencesClassName = getStrategy().getGlobalReferencesJavaClassName(schema, SequenceDefinition.class);

        if (scala)
            out.println("%sobject %s {", visibility(), referencesClassName);
        else if (kotlin) {}
        else
            out.println("%sclass %s {", visibility(), referencesClassName);

        for (SequenceDefinition sequence : database.getSequences(schema)) {
            final String seqTypeFull = getJavaType(sequence.getType(resolver(out)), out);
            final String seqType = out.ref(seqTypeFull);
            final String seqId = getStrategy().getJavaIdentifier(sequence);
            final String seqName = sequence.getOutputName();
            final String schemaId = generateDefaultSchema(schema)
                ? out.ref(getStrategy().getFullJavaIdentifier(schema), 2)
                : null;
            final String typeRef = getJavaTypeReference(sequence.getDatabase(), sequence.getType(resolver(out)), out);

            if (!printDeprecationIfUnknownType(out, seqTypeFull))
                out.javadoc("The sequence <code>%s</code>", sequence.getQualifiedOutputName());

            boolean flags = generateSequenceFlags();

            if (scala)
                out.println("%sval %s: %s[%s] = %s.createSequence(\"%s\", %s, %s, %s, %s, %s, %s, %s, %s)",
                    visibility(),
                    scalaWhitespaceSuffix(seqId),
                    Sequence.class,
                    seqType,
                    Internal.class,
                    seqName,
                    schemaId,
                    typeRef,
                    flags ? numberLiteral(sequence.getStartWith()) : "null",
                    flags ? numberLiteral(sequence.getIncrementBy()) : "null",
                    flags ? numberLiteral(sequence.getMinvalue()) : "null",
                    flags ? numberLiteral(sequence.getMaxvalue()) : "null",
                    flags && sequence.getCycle(),
                    flags ? numberLiteral(sequence.getCache()) : "null"
                );
            else if (kotlin)
                out.println("%sval %s: %s<%s> = %s.createSequence(\"%s\", %s, %s, %s, %s, %s, %s, %s, %s)",
                    visibility(),
                    seqId,
                    Sequence.class,
                    seqType,
                    Internal.class,
                    seqName,
                    schemaId,
                    typeRef,
                    flags ? numberLiteral(sequence.getStartWith()) : "null",
                    flags ? numberLiteral(sequence.getIncrementBy()) : "null",
                    flags ? numberLiteral(sequence.getMinvalue()) : "null",
                    flags ? numberLiteral(sequence.getMaxvalue()) : "null",
                    flags && sequence.getCycle(),
                    flags ? numberLiteral(sequence.getCache()) : "null"
                );
            else
                out.println("%sstatic final %s<%s> %s = %s.createSequence(\"%s\", %s, %s, %s, %s, %s, %s, %s, %s);",
                    visibility(),
                    Sequence.class,
                    seqType,
                    seqId,
                    Internal.class,
                    seqName,
                    schemaId,
                    typeRef,
                    flags ? numberLiteral(sequence.getStartWith()) : "null",
                    flags ? numberLiteral(sequence.getIncrementBy()) : "null",
                    flags ? numberLiteral(sequence.getMinvalue()) : "null",
                    flags ? numberLiteral(sequence.getMaxvalue()) : "null",
                    flags && sequence.getCycle(),
                    flags ? numberLiteral(sequence.getCache()) : "null"
                );
        }

        generateSequencesClassFooter(schema, out);

        if (!kotlin)
            out.println("}");
        closeJavaWriter(out);

        watch.splitInfo("Sequences generated");
    }

    /**
     * Subclasses may override this method to provide sequence references class footer code.
     */
    @SuppressWarnings("unused")
    protected void generateSequencesClassFooter(SchemaDefinition schema, JavaWriter out) {}









































































































    private String numberLiteral(Number n) {
        if (n instanceof BigInteger) {
            BigInteger bi = (BigInteger) n;
            int bitLength = ((BigInteger) n).bitLength();
            if (bitLength > Long.SIZE - 1)
                return "new java.math.BigInteger(\"" + bi.toString() + "\")";
            else if (bitLength > Integer.SIZE - 1)
                return Long.toString(n.longValue()) + 'L';
            else
                return Integer.toString(n.intValue());
        }
        else if (n instanceof Integer || n instanceof Short || n instanceof Byte)
            return Integer.toString(n.intValue());
        else if (n != null)
            return Long.toString(n.longValue()) + 'L';
        return "null";
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
        final String catalogId = getStrategy().getJavaIdentifier(catalog);
        final String catalogName = !catalog.getQualifiedOutputName().isEmpty() ? catalog.getQualifiedOutputName() : catalogId;
        final String className = getStrategy().getJavaClassName(catalog);
        final String classExtends = out.ref(getStrategy().getJavaClassExtends(catalog));
        final List<String> interfaces = out.ref(getStrategy().getJavaClassImplements(catalog, Mode.DEFAULT));

        printPackage(out, catalog);

        if (scala) {
            out.println("object %s {", className);
            out.javadoc("The reference instance of <code>%s</code>", catalogName);
            out.println("val %s = new %s", catalogId, className);
            out.println("}");
            out.println();
        }

        generateCatalogClassJavadoc(catalog, out);
        printClassAnnotations(out, catalog, Mode.DEFAULT);

        if (scala) {
            out.println("%sclass %s extends %s(\"%s\")[[before= with ][separator= with ][%s]] {",
                visibility(), className, classExtends, catalog.getOutputName(), interfaces);
        }
        else if (kotlin) {
            out.println("%sopen class %s : %s(\"%s\")[[before=, ][%s]] {",
                visibility(), className, classExtends, catalog.getOutputName(), interfaces);

            out.println("%scompanion object {", visibility());
            out.javadoc("The reference instance of <code>%s</code>", catalogName);
            out.println("public val %s: %s = %s()", catalogId, className, className);
            out.println("}");
        }
        else {
            out.println("%sclass %s extends %s[[before= implements ][%s]] {", visibility(), className, classExtends, interfaces);
            out.printSerial();
            out.javadoc("The reference instance of <code>%s</code>", catalogName);
            out.println("%sstatic final %s %s = new %s();", visibility(), className, catalogId, className);
        }

        List<SchemaDefinition> schemas = new ArrayList<>();
        if (generateGlobalSchemaReferences()) {
            Set<String> fieldNames = new HashSet<>();
            fieldNames.add(catalogId);
            for (SchemaDefinition schema : catalog.getSchemata())
                if (generateSchemaIfEmpty(schema))
                    fieldNames.add(getStrategy().getJavaIdentifier(schema));

            for (SchemaDefinition schema : catalog.getSchemata()) {
                if (generateSchemaIfEmpty(schema) && generateDefaultSchema(schema)) {
                    schemas.add(schema);

                    final String schemaClassName = out.ref(getStrategy().getFullJavaClassName(schema));
                    final String schemaId = getStrategy().getJavaIdentifier(schema);
                    final String schemaFullId = getStrategy().getFullJavaIdentifier(schema);
                    String schemaShortId = out.ref(getStrategy().getFullJavaIdentifier(schema), 2);
                    if (fieldNames.contains(schemaShortId.substring(0, schemaShortId.indexOf('.'))))
                        schemaShortId = schemaFullId;
                    final String schemaComment = escapeEntities(comment(schema));

                    out.javadoc(isBlank(schemaComment) ? ("The schema <code>" + (!schema.getQualifiedOutputName().isEmpty() ? schema.getQualifiedOutputName() : schemaId) + "</code>.") : schemaComment);

                    if (scala)
                        out.println("%sdef %s = %s", visibility(), schemaId, schemaShortId);
                    else if (kotlin)
                        out.println("%sval %s: %s get(): %s = %s", visibility(), schemaId, schemaClassName, schemaClassName, schemaShortId);
                    else
                        out.println("%sfinal %s %s = %s;", visibility(), schemaClassName, schemaId, schemaShortId);
                }
            }
        }

        if (scala || kotlin)
            ;
        else {
            out.javadoc(NO_FURTHER_INSTANCES_ALLOWED);
            out.println("private %s() {", className);
            out.println("super(\"%s\");", catalog.getOutputName());
            out.println("}");
        }

        printReferences(out, schemas, Schema.class);

        if (generateJooqVersionReference()) {
            String version = org.jooq.codegen.Constants.MINOR_VERSION.replace(".", "_");

            out.javadoc("A reference to the " + org.jooq.codegen.Constants.MINOR_VERSION + " minor release of the code generator. "
                + "If this doesn't compile, it's because the runtime library uses an older minor release, namely: " + org.jooq.Constants.MINOR_VERSION + ". "
                + "You can turn off the generation of this reference by specifying /configuration/generator/generate/jooqVersionReference");

            if (scala)
                out.println("private val REQUIRE_RUNTIME_JOOQ_VERSION = %s.VERSION_%s", org.jooq.Constants.class, version);
            else if (kotlin)
                out.println("private val REQUIRE_RUNTIME_JOOQ_VERSION = %s.VERSION_%s", org.jooq.Constants.class, version);
            else
                out.println("private static final String REQUIRE_RUNTIME_JOOQ_VERSION = %s.VERSION_%s;", org.jooq.Constants.class, version);
        }

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
        final String catalogId = generateDefaultCatalog(schema.getCatalog())
            ? out.ref(getStrategy().getFullJavaIdentifier(schema.getCatalog()), 2)
            : null;
        final String schemaId = getStrategy().getJavaIdentifier(schema);
        final String schemaName = !schema.getQualifiedOutputName().isEmpty() ? schema.getQualifiedOutputName() : schemaId;
        final String className = getStrategy().getJavaClassName(schema);
        final String classExtends = out.ref(getStrategy().getJavaClassExtends(schema));
        final List<String> interfaces = out.ref(getStrategy().getJavaClassImplements(schema, Mode.DEFAULT));

        printPackage(out, schema);

        if (scala) {
            out.println("object %s {", className);
            out.javadoc("The reference instance of <code>%s</code>", schemaName);
            out.println("val %s = new %s", schemaId, className);
            out.println("}");
            out.println();
        }

        generateSchemaClassJavadoc(schema, out);
        printClassAnnotations(out, schema, Mode.DEFAULT);

        if (scala) {
            out.println("%sclass %s extends %s(\"%s\", %s)[[before= with ][separator= with ][%s]] {",
                visibility(), className, classExtends, escapeString(schema.getOutputName()), catalogId, interfaces);
        }
        else if (kotlin) {
            out.println("%sopen class %s : %s(\"%s\", %s)[[before=, ][%s]] {",
                visibility(), className, classExtends, escapeString(schema.getOutputName()), catalogId, interfaces);

            out.println("public companion object {");
            out.javadoc("The reference instance of <code>%s</code>", schemaName);
            out.println("%sval %s: %s = %s()", visibility(), scalaWhitespaceSuffix(schemaId), className, className);
            out.println("}");
        }
        else {
            out.println("%sclass %s extends %s[[before= implements ][%s]] {", visibility(), className, classExtends, interfaces);
            out.printSerial();
            out.javadoc("The reference instance of <code>%s</code>", schemaName);
            out.println("%sstatic final %s %s = new %s();", visibility(), className, schemaId, className);
        }

        if (generateGlobalTableReferences()) {
            Set<String> memberNames = getMemberNames(schema);

            for (TableDefinition table : schema.getTables()) {

                // [#10191] In Scala, methods and attributes are in the same
                //          namespace because parentheses can be omitted. This
                //          means that parameter-less table valued functions
                //          produce a clash if we generate both the global table
                //          reference in the schema, and the function call
                if (scala && table.isTableValuedFunction() && table.getParameters().isEmpty())
                    continue;

                final String tableClassName = out.ref(getStrategy().getFullJavaClassName(table));
                final String tableId = getStrategy().getJavaIdentifier(table);
                final String tableShortId = getShortId(out, memberNames, table);
                final String tableComment = escapeEntities(comment(table));

                out.javadoc(isBlank(tableComment) ? "The table <code>" + table.getQualifiedOutputName() + "</code>." : tableComment);

                if (scala)
                    out.println("%sdef %s = %s", visibility(), tableId, tableShortId);
                else if (kotlin)
                    out.println("%sval %s: %s get() = %s", visibility(), scalaWhitespaceSuffix(tableId), tableClassName, tableShortId);
                else
                    out.println("%sfinal %s %s = %s;", visibility(), tableClassName, tableId, tableShortId);

                // [#3797] Table-valued functions generate two different literals in
                // globalObjectReferences
                if (table.isTableValuedFunction())
                    printTableValuedFunction(out, table, getStrategy().getJavaIdentifier(table));
            }
        }

        if (!scala && !kotlin) {
            out.javadoc(NO_FURTHER_INSTANCES_ALLOWED);
            out.println("private %s() {", className);
            out.println("super(\"%s\", null);", escapeString(schema.getOutputName()));
            out.println("}");
        }

        if (catalogId != null) {
            out.println();
            if (scala) {
                out.println("%soverride def getCatalog: %s = %s", visibilityPublic(), Catalog.class, catalogId);
            }
            else if (kotlin) {
                out.println("%soverride fun getCatalog(): %s = %s", visibilityPublic(),Catalog.class, catalogId);
            }
            else {
                out.overrideInherit();
                printNonnullAnnotation(out);
                out.println("%s%s getCatalog() {", visibilityPublic(), Catalog.class);
                out.println("return %s;", catalogId);
                out.println("}");
            }
        }

        // [#2255] Avoid referencing sequence literals, if they're not generated
        if (generateGlobalSequenceReferences())
            printReferences(out, database.getSequences(schema), Sequence.class);

        // [#681] Avoid referencing domain literals, if they're not generated
        if (generateGlobalDomainReferences())
            printReferences(out, database.getDomains(schema), Domain.class);









        // [#9685] Avoid referencing table literals if they're not generated
        if (generateTables())
            printReferences(out, database.getTables(schema), Table.class);

        // [#9685] Avoid referencing UDT literals if they're not generated
        if (generateUDTs())
            printReferences(out, database.getUDTs(schema), UDT.class);

        generateSchemaClassFooter(schema, out);
        out.println("}");
    }

    private Set<String> getMemberNames(CatalogDefinition catalog) {
        Set<String> members = new HashSet<>();
        members.add(getStrategy().getJavaIdentifier(catalog));

        for (SchemaDefinition table : catalog.getSchemata())
            members.add(getStrategy().getJavaIdentifier(table));

        return members;
    }

    private Set<String> getMemberNames(SchemaDefinition schema) {
        Set<String> members = new HashSet<>();
        members.add(getStrategy().getJavaIdentifier(schema));

        for (TableDefinition table : schema.getTables())
            members.add(getStrategy().getJavaIdentifier(table));

        return members;
    }

    private String getShortId(JavaWriter out, Set<String> memberNames, Definition table) {
        String shortId = out.ref(getStrategy().getFullJavaIdentifier(table), 2);

        if (memberNames.contains(shortId.substring(0, shortId.indexOf('.'))))
            shortId = getStrategy().getFullJavaIdentifier(table);

        return shortId;
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
        printFromAndInto(out, table, Mode.DEFAULT);
    }

    private void printFromAndInto(JavaWriter out, Definition tableOrUDT, Mode mode) {
        String qualified = out.ref(getStrategy().getFullJavaClassName(tableOrUDT, Mode.INTERFACE));

        out.header("FROM and INTO");
        boolean override = generateInterfaces() && !generateImmutableInterfaces();

        if (scala) {
            out.println();
            out.println("%s%sdef from(from: %s): Unit = {", visibilityPublic(), (override ? "override " : ""), qualified);
        }
        else if (kotlin) {
            out.println();
            out.println("%s%sfun from(from: %s) {", visibilityPublic(), (override ? "override " : ""), qualified);
        }
        else {
            out.overrideInheritIf(override);
            out.println("%svoid from(%s from) {", visibilityPublic(), qualified);
        }

        for (TypedElementDefinition<?> column : getTypedElements(tableOrUDT)) {
            final String setter = getStrategy().getJavaSetterName(column, Mode.INTERFACE);
            final String getter = getStrategy().getJavaGetterName(column, Mode.INTERFACE);

            // TODO: Use appropriate Mode here
            final String member = getStrategy().getJavaMemberName(column, Mode.POJO);

            if (scala)
                out.println("%s(from.%s)", setter, getter);
            else if (kotlin)
                out.println("%s = from.%s", member, member);
            else
                out.println("%s(from.%s());", setter, getter);
        }

        // [#14727] Make sure the same behaviour as from(Object) is implemented
        if (mode == Mode.RECORD)
            out.println("resetChangedOnNotNull()%s", semicolon);

        out.println("}");

        if (override) {
            // [#10191] Java and Kotlin can produce overloads for this method despite
            // generic type erasure, but Scala cannot, see
            // https://twitter.com/lukaseder/status/1262652304773259264

            if (scala) {
                if (mode != Mode.POJO) {
                    out.println();
                    out.println("%soverride def into [E](into: E): E = {", visibilityPublic(), qualified);
                    out.println("if (into.isInstanceOf[%s])", qualified);
                    out.println("into.asInstanceOf[%s].from(this)", qualified);
                    out.println("else");
                    out.println("super.into(into)");
                    out.println("into");
                    out.println("}");
                }
            }
            else if (kotlin) {
                out.println();
                out.println("%s%sfun <E : %s> into(into: E): E {", visibilityPublic(), (override ? "override " : ""), qualified);
                out.println("into.from(this)");
                out.println("return into");
                out.println("}");
            }
            else {
                out.overrideInherit();
                out.println("%s<E extends %s> E into(E into) {", visibilityPublic(), qualified);
                out.println("into.from(this);");
                out.println("return into;");
                out.println("}");
            }
        }
    }

    protected void printReferences(JavaWriter out, List<? extends Definition> definitions, Class<?> type) {
        if (out != null && !definitions.isEmpty()) {
            final String generic = type.getTypeParameters().length > 0
                ? Stream.of(type.getTypeParameters())
                        .map(x -> scala ? "_" : kotlin ? "*" : "?")
                        .collect(joining(", ", scala ? "[" : "<", scala ? "]" : ">"))
                : "";
            final List<String> references = new ArrayList<>();
            final Definition first = definitions.get(0);

            // [#6248] We cannot use the members in this class because:
            //         - They are not always available (global object references)
            //         - They may not have been initialised yet!
            //         Referencing the singleton identifier with 2 identifier segments
            //         doesn't work in Kotlin if the identifier conflicts with the
            //         members in this class. Java can resolve the ambiguity.
            if ((scala || kotlin) && (first instanceof TableDefinition || first instanceof UDTDefinition)) {
                final Set<String> memberNames = getMemberNames(first.getSchema());

                for (Definition table : definitions)
                    references.add(getShortId(out, memberNames, table));
            }
            else if ((scala || kotlin) && first instanceof SchemaDefinition) {
                final Set<String> memberNames = getMemberNames(first.getCatalog());

                for (Definition schema : definitions)
                    references.add(getShortId(out, memberNames, schema));
            }
            else {
                references.addAll(kotlin
                    ? out.ref(getStrategy().getFullJavaIdentifiers(definitions))
                    : out.ref(getStrategy().getFullJavaIdentifiers(definitions), 2));
            }

            out.println();

            if (scala) {
                if (definitions.size() > maxMembersPerInitialiser()) {
                    out.println("%soverride def get%ss: %s[%s%s] = {", visibilityPublic(), type.getSimpleName(), List.class, type, generic);
                    out.println("val result = new %s[%s%s]", ArrayList.class, type, generic);

                    for (int i = 0; i < definitions.size(); i += maxMembersPerInitialiser())
                        out.println("result.addAll(get%ss%s)", type.getSimpleName(), i / maxMembersPerInitialiser());

                    out.println("result");
                    out.println("}");
                }
                else {
                    out.println("%soverride def get%ss: %s[%s%s] = %s.asList[%s%s](", visibilityPublic(), type.getSimpleName(), List.class, type, generic, Arrays.class, type, generic);
                    out.println("[[separator=,\n][%s]]", references);
                    out.println(")");
                }
            }
            else if (kotlin) {
                if (definitions.size() > maxMembersPerInitialiser()) {
                    out.println("%soverride fun get%ss(): %s<%s%s> {", visibilityPublic(), type.getSimpleName(), out.ref(KLIST), type, generic);
                    out.println("val result = mutableListOf<%s%s>()", type, generic);

                    for (int i = 0; i < definitions.size(); i += maxMembersPerInitialiser())
                        out.println("result.addAll(get%ss%s())", type.getSimpleName(), i / maxMembersPerInitialiser());

                    out.println("return result");
                    out.println("}");
                }
                else {
                    out.println("%soverride fun get%ss(): %s<%s%s> = listOf(", visibilityPublic(), type.getSimpleName(), out.ref(KLIST), type, generic);
                    out.println("[[separator=,\n][%s]]", references);
                    out.println(")");
                }
            }
            else {
                out.override();
                printNonnullAnnotation(out);
                out.println("%sfinal %s<%s%s> get%ss() {", visibilityPublic(), List.class, type, generic, type.getSimpleName());

                if (definitions.size() > maxMembersPerInitialiser()) {
                    out.println("%s result = new %s();", List.class, ArrayList.class);

                    for (int i = 0; i < definitions.size(); i += maxMembersPerInitialiser())
                        out.println("result.addAll(get%ss%s());", type.getSimpleName(), i / maxMembersPerInitialiser());

                    out.println("return result;");
                }
                else {
                    out.println("return %s.asList(", Arrays.class);
                    out.println("[[separator=,\n][%s]]", references);
                    out.println(");");
                }

                out.println("}");
            }

            if (definitions.size() > maxMembersPerInitialiser()) {
                for (int i = 0; i < definitions.size(); i += maxMembersPerInitialiser()) {
                    out.println();

                    if (scala) {
                        out.println("private def get%ss%s(): %s[%s%s] = %s.asList[%s%s](", type.getSimpleName(), i / maxMembersPerInitialiser(), List.class, type, generic, Arrays.class, type, generic);
                        out.println("[[separator=,\n][%s]]", references.subList(i, Math.min(i + maxMembersPerInitialiser(), references.size())));
                        out.println(")");
                    }
                    else if (kotlin) {
                        out.println("private fun get%ss%s(): %s<%s%s> = listOf(", type.getSimpleName(), i / maxMembersPerInitialiser(), out.ref(KLIST), type, generic);
                        out.println("[[separator=,\n][%s]]", references.subList(i, Math.min(i + maxMembersPerInitialiser(), references.size())));
                        out.println(")");
                    }
                    else {
                        out.println("private final %s<%s%s> get%ss%s() {", List.class, type, generic, type.getSimpleName(), i / maxMembersPerInitialiser());
                        out.println("return %s.asList(", Arrays.class);
                        out.println("[[separator=,\n][%s]]", references.subList(i, Math.min(i + maxMembersPerInitialiser(), references.size())));
                        out.println(");");
                        out.println("}");
                    }
                }
            }
        }
    }

    protected void printTableJPAAnnotation(JavaWriter out, TableDefinition table) {
        SchemaDefinition schema = table.getSchema();
        int indent = out.indent();

        if (generateJPAAnnotations()) {
            // Since JPA 1.0
            out.println("@%s", out.ref("jakarta.persistence.Entity"));

            // Since JPA 1.0
            out.println("@%s(", out.ref("jakarta.persistence.Table"));
            out.print("name = \"", out.ref("jakarta.persistence.Table"));
            out.print(escapeString(table.getName()));
            out.print("\"");

            if (!schema.isDefaultSchema()) {
                out.println(",");
                out.print("schema = \"");
                out.print(escapeString(schema.getOutputName()));
                out.print("\"");
            }

            List<UniqueKeyDefinition> keys = table.getUniqueKeys();

            if (!keys.isEmpty()) {
                out.println(",");
                out.print("uniqueConstraints = ");
                out.println(scala ? "Array(" : kotlin ? "[" : "{");

                for (int i = 0; i < keys.size(); i++) {
                    UniqueKeyDefinition uk = keys.get(i);
                    out.print(scala ? "new " : kotlin ? "" : "@")

                       // Since JPA 1.0
                       .print(out.ref("jakarta.persistence.UniqueConstraint"))
                       .print("(");

                    if (!StringUtils.isBlank(uk.getOutputName()))
                        out.print("name = \"" + escapeString(uk.getOutputName()) + "\", ");

                    out.print("columnNames = ")
                       .print(scala ? "Array(" : kotlin ? "[ " : "{ ");

                    List<ColumnDefinition> columns = uk.getKeyColumns();
                    for (int j = 0; j < columns.size(); j++) {
                        out.print(j > 0 ? ", " : "");
                        out.print("\"");
                        out.print(escapeString(columns.get(j).getName()));
                        out.print("\"");
                    }

                    out.print(scala ? ")" : kotlin ? " ]" : " }").print(")").println(i < keys.size() - 1 ? "," : "");
                }

                out.print(scala ? ")" : kotlin ? "]" : "}");
            }

            if (StringUtils.isBlank(generateJPAVersion()) || "2.1".compareTo(generateJPAVersion()) <= 0) {
                List<IndexDefinition> indexes = table.getIndexes();

                if (!indexes.isEmpty()) {
                    out.println(",");
                    out.print("indexes = ").println(scala ? "Array(" : kotlin ? "[" : "{");

                    for (int i = 0; i < indexes.size(); i++) {
                        IndexDefinition index = indexes.get(i);

                        out.print(scala ? "new " : kotlin ? "" : "@");
                        out.print(out.ref("jakarta.persistence.Index"));
                        out.print("(name = \"").print(escapeString(index.getOutputName())).print("\"");

                        if (index.isUnique())
                            out.print(", unique = true");

                        out.print(", columnList = \"");

                        List<IndexColumnDefinition> columns = index.getIndexColumns();

                        for (int j = 0; j < columns.size(); j++) {
                            IndexColumnDefinition column = columns.get(j);

                            if (j > 0)
                                out.print(", ");

                            out.print(escapeString(column.getOutputName()));

                            if (column.getSortOrder() == SortOrder.ASC)
                                out.print(" ASC");
                            else if (column.getSortOrder() == SortOrder.DESC)
                                out.print(" DESC");
                        }

                        out.print("\")").println(i < indexes.size() - 1 ? "," : "");
                    }

                    out.print(scala ? ")" : kotlin ? "]" : "}");
                }
            }

            out.println();
            out.println(")");
        }

        // [#10196] The above logic triggers an indent level of -1, incorrectly
        out.indent(indent);
    }

    protected void printColumnJPAAnnotation(JavaWriter out, ColumnDefinition column) {
        int indent = out.indent();

        if (generateJPAAnnotations()) {
            String prefix = kotlin ? "get:" : "";
            UniqueKeyDefinition pk = column.getPrimaryKey();

            if (pk != null) {
                if (pk.getKeyColumns().size() == 1) {

                    // Since JPA 1.0
                    out.println("@%s%s", prefix, out.ref("jakarta.persistence.Id"));

                    // Since JPA 1.0
                    if (pk.getKeyColumns().get(0).isIdentity())
                        out.println("@%s%s(strategy = %s.IDENTITY)",
                            prefix,
                            out.ref("jakarta.persistence.GeneratedValue"),
                            out.ref("jakarta.persistence.GenerationType")
                        );
                }
            }

            String nullable = "";
            if (effectivelyNotNull(out, column))
                nullable = ", nullable = false";

            String length = "";
            String precision = "";
            String scale = "";

            DataTypeDefinition type = column.getType(resolver(out));
            DataType<?> t = getRuntimeDataType(type);
            if (t.hasLength() && type.getLength() > 0) {
                length = ", length = " + type.getLength();
            }
            else if (t.hasPrecision() && type.getPrecision() > 0) {
                precision = ", precision = " + type.getPrecision();

                if (t.hasScale() && type.getScale() > 0) {
                    scale = ", scale = " + type.getScale();
                }
            }

            // [#8535] The unique flag is not set on the column, but only on
            //         the table's @UniqueConstraint section.

            // Since JPA 1.0
            out.print("@%s%s(name = \"", prefix, out.ref("jakarta.persistence.Column"));
            out.print(escapeString(column.getName()));
            out.print("\"");
            out.print(nullable);
            out.print(length);
            out.print(precision);
            out.print(scale);
            out.println(")");
        }

        // [#10196] The above logic triggers an indent level of -1, incorrectly
        out.indent(indent);
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
            String prefix = kotlin ? "get:" : "";
            DataTypeDefinition type = column.getType(resolver(out));

            // [#5128] defaulted columns are nullable in Java
            if (effectivelyNotNull(out, column))
                out.println("@%s%s", prefix, out.ref("jakarta.validation.constraints.NotNull"));

            String javaType = getJavaType(type, out);
            if ("java.lang.String".equals(javaType) || "byte[]".equals(javaType)) {
                int length = type.getLength();

                if (length > 0)
                    out.println("@%s%s(max = %s)", prefix, out.ref("jakarta.validation.constraints.Size"), length);
            }
        }
    }

    private String kotlinNullability(JavaWriter out, TypedElementDefinition<?> typed) {
        return kotlinNullability(out, typed, Mode.DEFAULT);
    }

    private String kotlinNullability(JavaWriter out, TypedElementDefinition<?> typed, Mode mode) {
        return kotlinEffectivelyNotNull(out, typed, mode) ? "" : "?";
    }

    private boolean kotlinEffectivelyNotNull(JavaWriter out, EmbeddableDefinition e, Mode mode) {
        for (EmbeddableColumnDefinition c : e.getColumns())
            if (kotlinEffectivelyNotNull(out, c, mode))
                return true;

        return false;
    }

    private boolean kotlinEffectivelyNotNull(JavaWriter out, TypedElementDefinition<?> typed, Mode mode) {
        return (
            mode == Mode.POJO && generateKotlinNotNullPojoAttributes() ||
            mode == Mode.RECORD && generateKotlinNotNullRecordAttributes() ||
            mode == Mode.INTERFACE && generateKotlinNotNullInterfaceAttributes() ||
            mode == Mode.DEFAULT
        ) && effectivelyNotNull(out, typed);
    }

    private boolean effectivelyNotNull(JavaWriter out, TypedElementDefinition<?> column) {
        return effectivelyNotNull(column.getType(resolver(out)));
    }

    private boolean effectivelyNotNull(DataTypeDefinition type) {
        return !type.isNullable()
            && !type.isDefaulted()
            && !type.isIdentity();
    }

    private static final Pattern P_IS = Pattern.compile("^is[A-Z].*$");

    protected void printKotlinSetterAnnotation(JavaWriter out, TypedElementDefinition<?> column, Mode mode) {

        // [#11912] When X and IS_X create conflicts, we need to resolve
        //          them by specifying an explicit setter name
        if (kotlin
                && generateKotlinSetterJvmNameAnnotationsOnIsPrefix()
                && column instanceof ColumnDefinition
                && P_IS.matcher(getStrategy().getJavaMemberName(column, mode)).matches()) {

            // [#12440] And if we have interfaces, we'll run into https://youtrack.jetbrains.com/issue/KT-31420
            // [#13467] Since jOOQ 3.17, all these properties are open, so this applies everywhere
            out.println("@Suppress(\"INAPPLICABLE_JVM_NAME\")");
            out.println("@set:JvmName(\"%s\")", getStrategy().getJavaSetterName(column, mode));
        }
    }

    private String nullableAnnotation(JavaWriter out) {
        return generateNullableAnnotation() ? out.ref(generatedNullableAnnotationType()) : null;
    }

    private String nonnullAnnotation(JavaWriter out) {
        return generateNonnullAnnotation() ? out.ref(generatedNonnullAnnotationType()) : null;
    }

    private String nullableOrNonnullAnnotation(JavaWriter out, Definition column) {
        return (column instanceof TypedElementDefinition && !effectivelyNotNull(out, (TypedElementDefinition<?>) column))
             ? nullableAnnotation(out)
             : nonnullAnnotation(out);
    }

    private void printNullableOrNonnullAnnotation(JavaWriter out, Definition column) {
        if (column instanceof TypedElementDefinition && !effectivelyNotNull(out, (TypedElementDefinition<?>) column))
            printNullableAnnotation(out);
        else
            printNonnullAnnotation(out);
    }

    protected void printNullableAnnotation(JavaWriter out) {
        if (generateNullableAnnotation())
            out.println("@%s", out.ref(generatedNullableAnnotationType()));
    }

    protected void printNonnullAnnotation(JavaWriter out) {
        if (generateNonnullAnnotation())
            out.println("@%s", out.ref(generatedNonnullAnnotationType()));
    }

    private boolean printDeprecationIfUnknownTypes(JavaWriter out, Collection<? extends ParameterDefinition> params) {
        for (ParameterDefinition param : params)
            if (printDeprecationIfUnknownType(out, getJavaType(param.getType(resolver(out)), out), "Parameter type or return type is unknown. "))
                return true;

        return false;
    }

    private boolean printDeprecationIfUnknownType(JavaWriter out, String type) {
        return printDeprecationIfUnknownType(out, type, "");
    }

    private boolean printDeprecationIfUnknownType(JavaWriter out, String type, String precision) {
        if (generateDeprecationOnUnknownTypes() && (Object.class.getName().equals(type) || kotlin && "Any".equals(type))) {
            if (kotlin) {
                out.println("@%s(message = \"%s\")", out.ref("kotlin.Deprecated"), escapeString(
                      "Unknown data type. " + precision
                    + "If this is a qualified, user-defined type, it may have been excluded from code generation. "
                    + "If this is a built-in type, you can define an explicit org.jooq.Binding to specify how this type should be handled. "
                    + "Deprecation can be turned off using <deprecationOnUnknownTypes/> in your code generator configuration."
                ));
            }
            else {
                out.javadoc("@deprecated "
                    + "Unknown data type. " + precision
                    + "If this is a qualified, user-defined type, it may have been excluded from code generation. "
                    + "If this is a built-in type, you can define an explicit {@link org.jooq.Binding} to specify how this type should be handled. "
                    + "Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration."
                );

                out.println("@%s", out.ref(Deprecated.class));
            }

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

        if (log.isDebugEnabled())
            for (ParameterDefinition parameter : routine.getAllParameters())
                log.debug("With parameter", "name=" + parameter.getOutputName() + ", matching type names=" + parameter.getDefinedType().getMatchNames());

        generateRoutine(routine, out);
        closeJavaWriter(out);
    }

    protected void generateRoutine(RoutineDefinition routine, JavaWriter out) {
        final SchemaDefinition schema = routine.getSchema();
        final String className = getStrategy().getJavaClassName(routine);
        final String returnTypeFull = (routine.getReturnValue() == null)
            ? Void.class.getName()
            : getJavaType(routine.getReturnType(resolver(out)), out);
        final String returnType = (routine.getReturnValue() == null)
            ? Void.class.getName()
            : out.ref(returnTypeFull);
        final List<String> returnTypeRef = list((routine.getReturnValue() != null)
            ? getJavaTypeReference(database, routine.getReturnType(resolver(out)), out)
            : null);
        final List<String> returnConverter = out.ref(list(
             (routine.getReturnValue() != null)
            ? routine.getReturnType(resolver(out)).getConverter()
            : null));
        final List<String> returnBinding = out.ref(list(
             (routine.getReturnValue() != null)
            ? routine.getReturnType(resolver(out)).getBinding()
            : null));

        final String classExtends = out.ref(getStrategy().getJavaClassExtends(routine));
        final List<String> interfaces = out.ref(getStrategy().getJavaClassImplements(routine, Mode.DEFAULT));
        final String schemaId = generateDefaultSchema(schema)
            ? out.ref(getStrategy().getFullJavaIdentifier(schema), 2)
            : null;
        final List<String> packageId = out.ref(getStrategy().getFullJavaIdentifiers(routine.getPackage()), 2);

        printPackage(out, routine);

        if (scala) {
            out.println("object %s {", className);
            for (ParameterDefinition parameter : routine.getAllParameters()) {
                final String paramTypeFull = getJavaType(parameter.getType(resolver(out)), out);
                final String paramType = out.ref(paramTypeFull);
                final String paramTypeRef = getJavaTypeReference(parameter.getDatabase(), parameter.getType(resolver(out)), out);
                final String paramId = out.ref(getStrategy().getJavaIdentifier(parameter), 2);
                final String paramName = parameter.getName();
                final String isDefaulted = parameter.isDefaulted() ? "true" : "false";
                final String isUnnamed = parameter.isUnnamed() ? "true" : "false";
                final List<String> converter = out.ref(list(parameter.getType(resolver(out)).getConverter()));
                final List<String> binding = out.ref(list(parameter.getType(resolver(out)).getBinding()));

                if (!printDeprecationIfUnknownType(out, paramTypeFull))
                    out.javadoc("The parameter <code>%s</code>.[[before= ][%s]]", parameter.getQualifiedOutputName(), list(escapeEntities(comment(parameter))));

                out.println("val %s: %s[%s] = %s.createParameter(\"%s\", %s, %s, %s" + converterTemplate(converter) + converterTemplate(binding) + ")",
                    scalaWhitespaceSuffix(paramId), Parameter.class, paramType, Internal.class, escapeString(paramName), paramTypeRef, isDefaulted, isUnnamed, converter, binding);
            }

            out.println("}");
            out.println();
        }

        if (!printDeprecationIfUnknownType(out, returnTypeFull))
            generateRoutineClassJavadoc(routine, out);

        printClassAnnotations(out, routine, Mode.DEFAULT);

        if (scala) {
            out.println("%sclass %s extends %s[%s](\"%s\", %s[[before=, ][%s]][[before=, ][%s]]" + converterTemplate(returnConverter) + converterTemplate(returnBinding) + ")[[before= with ][separator= with ][%s]] {",
                visibility(), className, classExtends, returnType, escapeString(routine.getName()), schemaId, packageId, returnTypeRef, returnConverter, returnBinding, interfaces);
        }
        else {
            if (kotlin) {
                out.println("%sopen class %s : %s<%s>(\"%s\", %s[[before=, ][%s]][[before=, ][%s]]" + converterTemplate(returnConverter) + converterTemplate(returnBinding) + ")[[before=, ][%s]] {",
                    visibility(), className, classExtends, returnType, escapeString(routine.getName()), schemaId, packageId, returnTypeRef, returnConverter, returnBinding, interfaces);
            }
            else {
                out.println("%sclass %s extends %s<%s>[[before= implements ][%s]] {",
                    visibility(), className, classExtends, returnType, interfaces);
                out.printSerial();
            }

            if (kotlin)
                out.println("%scompanion object {", visibility());

            for (ParameterDefinition parameter : routine.getAllParameters()) {
                final String paramTypeFull = getJavaType(parameter.getType(resolver(out)), out);
                final String paramType = out.ref(paramTypeFull);
                final String paramTypeRef = getJavaTypeReference(parameter.getDatabase(), parameter.getType(resolver(out)), out);
                final String paramId = getStrategy().getJavaIdentifier(parameter);
                final String paramName = parameter.getName();
                final String isDefaulted = parameter.isDefaulted() ? "true" : "false";
                final String isUnnamed = parameter.isUnnamed() ? "true" : "false";
                final List<String> converter = out.ref(list(parameter.getType(resolver(out)).getConverter()));
                final List<String> binding = out.ref(list(parameter.getType(resolver(out)).getBinding()));

                if (!printDeprecationIfUnknownType(out, paramTypeFull))
                    out.javadoc("The parameter <code>%s</code>.[[before= ][%s]]", parameter.getQualifiedOutputName(), list(escapeEntities(comment(parameter))));

                if (kotlin)
                    out.println("%sval %s: %s<%s?> = %s.createParameter(\"%s\", %s, %s, %s" + converterTemplate(converter) + converterTemplate(binding) + ")",
                        visibility(), scalaWhitespaceSuffix(paramId), Parameter.class, paramType, Internal.class, escapeString(paramName), paramTypeRef, isDefaulted, isUnnamed, converter, binding);
                else
                    out.println("%sstatic final %s<%s> %s = %s.createParameter(\"%s\", %s, %s, %s" + converterTemplate(converter) + converterTemplate(binding) + ");",
                        visibility(), Parameter.class, paramType, paramId, Internal.class, escapeString(paramName), paramTypeRef, isDefaulted, isUnnamed, converter, binding);
            }

            if (kotlin)
                out.println("}").println();
        }

        if (scala) {
            out.println("{");
        }
        else if (kotlin) {
            out.println("init {");
        }
        else {
            out.javadoc("Create a new routine call instance");
            out.println("%s%s() {", visibility(), className);
            out.println("super(\"%s\", %s[[before=, ][%s]][[before=, ][%s]]" + converterTemplate(returnConverter) + converterTemplate(returnBinding) + ");", routine.getName(), schemaId, packageId, returnTypeRef, returnConverter, returnBinding);


            if (routine.getAllParameters().size() > 0)
                out.println();
        }

        for (ParameterDefinition parameter : routine.getAllParameters()) {
            final String paramId = getStrategy().getJavaIdentifier(parameter);

            if (parameter.equals(routine.getReturnValue())) {
                if (parameter.isSynthetic()) {
                    if (scala)
                        out.println("setSyntheticReturnParameter(%s.%s)", className, paramId);
                    else if (kotlin)
                        out.println("setSyntheticReturnParameter(%s)", paramId);
                    else
                        out.println("setSyntheticReturnParameter(%s);", paramId);
                }
                else {
                    if (scala)
                        out.println("setReturnParameter(%s.%s)", className, paramId);
                    else if (kotlin)
                        out.println("returnParameter = %s", paramId);
                    else
                        out.println("setReturnParameter(%s);", paramId);
                }
            }
            else if (routine.getInParameters().contains(parameter)) {
                if (routine.getOutParameters().contains(parameter)) {
                    if (scala)
                        out.println("addInOutParameter(%s.%s)", className, paramId);
                    else if (kotlin)
                        out.println("addInOutParameter(%s)", paramId);
                    else
                        out.println("addInOutParameter(%s);", paramId);
                }
                else {
                    if (scala)
                        out.println("addInParameter(%s.%s)", className, paramId);
                    else if (kotlin)
                        out.println("addInParameter(%s)", paramId);
                    else
                        out.println("addInParameter(%s);", paramId);
                }
            }
            else {
                if (scala)
                    out.println("addOutParameter(%s.%s)", className, paramId);
                else if (kotlin)
                    out.println("addOutParameter(%s)", paramId);
                else
                    out.println("addOutParameter(%s);", paramId);
            }













        }

        if (routine.getOverload() != null)
            out.println("setOverloaded(true)%s", semicolon);

        if (routine instanceof PostgresRoutineDefinition p)
            if (p.isProcedure())
                out.println("setSQLUsable(false)%s", semicolon);










        out.println("}");

        for (ParameterDefinition parameter : routine.getInParameters()) {
            final String setterReturnType = generateFluentSetters() ? className : tokenVoid;
            final String setter = getStrategy().getJavaSetterName(parameter, Mode.DEFAULT);
            final String numberValue = parameter.getType(resolver(out)).isGenericNumberType() ? "Number" : "Value";
            final String numberField = parameter.getType(resolver(out)).isGenericNumberType() ? "Number" : "Field";
            final String paramId = getStrategy().getJavaIdentifier(parameter);
            final String paramName = "value".equals(paramId) ? "value_" : "value";

            out.javadoc("Set the <code>%s</code> parameter IN value to the routine", parameter.getOutputName());

            if (scala) {
                out.println("%sdef %s(%s: %s) : Unit = set%s(%s.%s, %s)",
                    visibility(), setter, scalaWhitespaceSuffix(paramName), refNumberType(out, parameter.getType(resolver(out))), numberValue, className, paramId, paramName);
            }
            else if (kotlin) {
                out.println("%sfun %s(%s: %s?): Unit = set%s(%s, %s)",
                    visibility(), setter, paramName, refNumberType(out, parameter.getType(resolver(out))), numberValue, paramId, paramName);
            }
            else {
                out.println("%svoid %s(%s %s) {", visibility(), setter, varargsIfArray(refNumberType(out, parameter.getType(resolver(out)))), paramName);
                out.println("set%s(%s, %s);", numberValue, paramId, paramName);
                out.println("}");
            }

            if (routine.isSQLUsable()) {
                out.javadoc("Set the <code>%s</code> parameter to the function to be used with a {@link org.jooq.Select} statement", parameter.getOutputName());

                if (scala) {
                    out.println("%sdef %s(field: %s[%s]): %s = {", visibility(), setter, Field.class, refExtendsNumberType(out, parameter.getType(resolver(out))), setterReturnType);
                    out.println("set%s(%s.%s, field)", numberField, className, paramId);

                    if (generateFluentSetters())
                        out.println("this");

                    out.println("}");
                }
                else if (kotlin) {
                    out.println("%sfun %s(field: %s<%s?>): %s {", visibility(), setter, Field.class, refExtendsNumberType(out, parameter.getType(resolver(out))), setterReturnType);
                    out.println("set%s(%s, field)", numberField, paramId);

                    if (generateFluentSetters())
                        out.println("return this");

                    out.println("}");
                }
                else {
                    out.println("%s%s %s(%s<%s> field) {", visibility(), setterReturnType, setter, Field.class, refExtendsNumberType(out, parameter.getType(resolver(out))));
                    out.println("set%s(%s, field);", numberField, paramId);

                    if (generateFluentSetters())
                        out.println("return this;");

                    out.println("}");
                }
            }
        }

        for (ParameterDefinition parameter : routine.getAllParameters()) {
            boolean isReturnValue = parameter.equals(routine.getReturnValue());
            boolean isOutParameter = routine.getOutParameters().contains(parameter);

            if (isOutParameter && !isReturnValue) {
                final String paramName = parameter.getOutputName();
                final String paramTypeFull = getJavaType(parameter.getType(resolver(out)), out);
                final String paramType = out.ref(paramTypeFull);
                final String paramGetter = getStrategy().getJavaGetterName(parameter, Mode.DEFAULT);
                final String paramId = getStrategy().getJavaIdentifier(parameter);

                if (!printDeprecationIfUnknownType(out, paramTypeFull))
                    out.javadoc("Get the <code>%s</code> parameter OUT value from the routine", paramName);

                if (scala) {
                    out.println("%sdef %s: %s = get(%s.%s)", visibility(), scalaWhitespaceSuffix(paramGetter), paramType, className, paramId);
                }
                else if (kotlin) {
                    out.println("%sfun %s(): %s? = get(%s)", visibility(), paramGetter, paramType, paramId);
                }
                else {
                    out.println("%s%s %s() {", visibility(), paramType, paramGetter);
                    out.println("return get(%s);", paramId);
                    out.println("}");
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
            log.info("Too many parameters", "Function " + function + " has more than 254 in parameters. Skipping generation of convenience method.");
            return;
        }

        // Do not generate separate convenience methods, if there are no IN
        // parameters. They would have the same signature and no additional
        // meaning
        if (parametersAsField && function.getInParameters().isEmpty())
            return;

        final String functionTypeFull = getJavaType(function.getReturnType(resolver(out)), out);
        final String functionType = out.ref(functionTypeFull);
        final String className = out.ref(getStrategy().getFullJavaClassName(function));
        final String localVar = disambiguateJavaMemberName(function.getInParameters(), "f");
        final String methodName = getStrategy().getJavaMethodName(function, Mode.DEFAULT);

        if (!printDeprecationIfUnknownType(out, functionTypeFull) &&
            !printDeprecationIfUnknownTypes(out, function.getInParameters()))
            out.javadoc("Get <code>%s</code> as a field.", function.getQualifiedOutputName());

        if (scala)
            out.print("%sdef %s(", visibility(), methodName);
        else if (kotlin)
            out.print("%sfun %s(", visibility(), methodName);
        else
            out.print("%sstatic %s<%s> %s(",
                visibility(),
                function.isAggregate() ? AggregateFunction.class : Field.class,
                functionType,
                methodName);

        if (!function.getInParameters().isEmpty())
            out.println();

        printParameterDeclarations(out, function.getInParameters(), parametersAsField, "  ");

        if (scala) {
            out.println("): %s[%s] = {",
                function.isAggregate() ? AggregateFunction.class : Field.class,
                functionType);
            out.println("val %s = new %s", localVar, className);
        }
        else if (kotlin) {
            out.println("): %s<%s?> {",
                function.isAggregate() ? AggregateFunction.class : Field.class,
                functionType);
            out.println("val %s = %s()", localVar, className);
        }
        else {
            out.println(") {");
            out.println("%s %s = new %s();", className, localVar, className);
        }

        for (ParameterDefinition parameter : function.getInParameters()) {
            final String paramSetter = getStrategy().getJavaSetterName(parameter, Mode.DEFAULT);
            final String paramMember = getStrategy().getJavaMemberName(parameter);

            out.println("%s.%s(%s)%s", localVar, paramSetter, paramMember, semicolon);
        }

        out.println();
        out.println("return %s.as%s%s%s", localVar, function.isAggregate() ? "AggregateFunction" : "Field", emptyparens, semicolon);

        out.println("}");
    }

    protected void printConvenienceMethodTableValuedFunctionAsField(JavaWriter out, TableDefinition function, boolean parametersAsField, String methodName) {
        // [#281] - Java can't handle more than 255 method parameters
        if (function.getParameters().size() > 254) {
            log.info("Too many parameters", "Function " + function + " has more than 254 in parameters. Skipping generation of convenience method.");
            return;
        }

        if (function.getParameters().isEmpty())

            // Do not generate separate convenience methods, if there are no IN
            // parameters. They would have the same signature and no additional
            // meaning
            if (parametersAsField)
                return;

            // [#4883] Scala doesn't have separate namespaces for val and def
            else if (scala)
                return;

        final String className = out.ref(getStrategy().getFullJavaClassName(function));

        // [#5765] To prevent name clashes, this identifier is not imported
        final String functionIdentifier = getStrategy().getFullJavaIdentifier(function);

        if (!printDeprecationIfUnknownTypes(out, function.getParameters()))
            out.javadoc("Get <code>%s</code> as a table.", function.getQualifiedOutputName());

        if (scala)
            out.print("%sdef %s(", visibility(), methodName);
        else if (kotlin)
            out.print("%sfun %s(", visibility(), methodName);
        else
            out.print("%sstatic %s %s(", visibility(), className, methodName);

        if (!function.getParameters().isEmpty())
            out.println();

        printParameterDeclarations(out, function.getParameters(), parametersAsField, "  ");

        if (scala || kotlin) {
            out.println("): %s = %s.call(", className, functionIdentifier);
        }
        else {
            out.println(") {");
            out.println("return %s.call(", functionIdentifier);
        }

        forEach(function.getParameters(), (parameter, separator) -> {
            out.println("%s%s", getStrategy().getJavaMemberName(parameter), separator);
        });

        if (scala || kotlin)
            out.println(")");
        else
            out.println(");")
               .println("}");
    }

    private void printParameterDeclarations(JavaWriter out, List<ParameterDefinition> parameters, boolean parametersAsField, String separator) {
        for (ParameterDefinition parameter : parameters) {
            final String memberName = getStrategy().getJavaMemberName(parameter);

            if (scala) {
                if (parametersAsField)
                    out.println("%s%s: %s[%s]", separator, scalaWhitespaceSuffix(memberName), Field.class, refExtendsNumberType(out, parameter.getType(resolver(out))));
                else
                    out.println("%s%s: %s", separator, scalaWhitespaceSuffix(memberName), refNumberType(out, parameter.getType(resolver(out))));
            }
            else if (kotlin) {
                if (parametersAsField)
                    out.println("%s%s: %s<%s?>", separator, memberName, Field.class, refExtendsNumberType(out, parameter.getType(resolver(out))));
                else
                    out.println("%s%s: %s%s", separator, memberName, refNumberType(out, parameter.getType(resolver(out))), kotlinNullability(out, parameter));
            }
            else {
                if (parametersAsField)
                    out.println("%s%s<%s> %s", separator, Field.class, refExtendsNumberType(out, parameter.getType(resolver(out))), memberName);
                else
                    out.println("%s%s %s", separator, refNumberType(out, parameter.getType(resolver(out))), memberName);
            }

            separator = ", ";
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
            log.info("Too many parameters", "Function " + function + " has more than 254 in parameters. Skipping generation of convenience method.");
            return;
        }

        final String className = out.ref(getStrategy().getFullJavaClassName(function));
        final String functionName = function.getQualifiedOutputName();
        final String functionTypeFull = getJavaType(function.getReturnType(resolver(out)), out);
        final String functionType = out.ref(functionTypeFull);
        final String methodName = getStrategy().getJavaMethodName(function, Mode.DEFAULT);

        // [#3456] Local variables should not collide with actual function arguments
        final String configurationArgument = disambiguateJavaMemberName(function.getInParameters(), "configuration");
        final String localVar = disambiguateJavaMemberName(function.getInParameters(), "f");

        if (!printDeprecationIfUnknownType(out, functionTypeFull) &&
            !printDeprecationIfUnknownTypes(out, function.getInParameters()))
            out.javadoc("Call <code>%s</code>", functionName);

        if (scala)
            out.println("%sdef %s(", visibility(), methodName);
        else if (kotlin)
            out.println("%sfun %s(", visibility(), methodName);
        else
            out.println("%s%s%s %s(", visibility(), !instance ? "static " : "", functionType, methodName);

        String separator = "  ";
        if (!instance) {
            if (scala)
                out.println("%s%s: %s", separator, scalaWhitespaceSuffix(configurationArgument), Configuration.class);
            else if (kotlin)
                out.println("%s%s: %s", separator, configurationArgument, Configuration.class);
            else
                out.println("%s%s %s", separator, Configuration.class, configurationArgument);

            separator = ", ";
        }

        for (ParameterDefinition parameter : function.getInParameters()) {

            // Skip SELF parameter
            if (instance && parameter.equals(function.getInParameters().get(0)))
                continue;

            final String paramType = refNumberType(out, parameter.getType(resolver(out)));
            final String paramMember = getStrategy().getJavaMemberName(parameter);

            if (scala)
                out.println("%s%s: %s", separator, scalaWhitespaceSuffix(paramMember), paramType);
            else if (kotlin)
                out.println("%s%s: %s%s", separator, paramMember, paramType, kotlinNullability(out, parameter));
            else
                out.println("%s%s %s", separator, paramType, paramMember);

            separator = ", ";
        }

        if (scala) {
            out.println("): %s = {", functionType);
            out.println("val %s = new %s()", localVar, className);
        }
        else if (kotlin) {
            out.println("): %s? {", functionType);
            out.println("val %s = %s()", localVar, className);
        }
        else {
            out.println(") {");
            out.println("%s %s = new %s();", className, localVar, className);
        }

        for (ParameterDefinition parameter : function.getInParameters()) {
            final String paramSetter = getStrategy().getJavaSetterName(parameter, Mode.DEFAULT);
            final String paramMember = (instance && parameter.equals(function.getInParameters().get(0)))
                ? "this"
                : getStrategy().getJavaMemberName(parameter);

            out.println("%s.%s(%s)%s", localVar, paramSetter, paramMember, semicolon);
        }

        out.println();
        out.println("%s.execute(%s)%s", localVar, instance ? "configuration()" : configurationArgument, semicolon);

        // TODO [#956] Find a way to register "SELF" as OUT parameter
        // in case this is a UDT instance (member) function

        if (scala)
            out.println("%s.getReturnValue", localVar);
        else if (kotlin)
            out.println("return %s.returnValue", localVar);
        else
            out.println("return %s.getReturnValue();", localVar);

        out.println("}");
    }

    protected void printConvenienceMethodProcedure(JavaWriter out, RoutineDefinition procedure, boolean instance) {
        // [#281] - Java can't handle more than 255 method parameters
        if (procedure.getInParameters().size() > 254) {
            log.info("Too many parameters", "Procedure " + procedure + " has more than 254 in parameters. Skipping generation of convenience method.");
            return;
        }

        final String className = out.ref(getStrategy().getFullJavaClassName(procedure));
        final String configurationArgument = disambiguateJavaMemberName(procedure.getInParameters(), "configuration");
        final String localVar = disambiguateJavaMemberName(procedure.getInParameters(), "p");
        final List<ParameterDefinition> outParams = list(procedure.getReturnValue(), procedure.getOutParameters());
        final String methodName = getStrategy().getJavaMethodName(procedure, Mode.DEFAULT);
        final String firstOutParamType = outParams.size() == 1 ? out.ref(getJavaType(outParams.get(0).getType(resolver(out)), out)) : "";

        if (!printDeprecationIfUnknownTypes(out, procedure.getAllParameters()))
            out.javadoc("Call <code>%s</code>", procedure.getQualifiedOutputName());

        if (scala)
            out.println("%sdef %s(", visibility(), methodName);
        else if (kotlin)
            out.println("%sfun %s(", visibility(), methodName);
        else {
            out.println("%s%s%s %s(",
                visibility(),
                !instance ? "static " : "",
                outParams.size() == 0 ? "void" : outParams.size() == 1 ? firstOutParamType : className,
                methodName
            );
        }

        String separator = "  ";
        if (!instance) {
            if (scala)
                out.println("%s%s: %s", separator, scalaWhitespaceSuffix(configurationArgument), Configuration.class);
            else if (kotlin)
                out.println("%s%s: %s", separator, configurationArgument, Configuration.class);
            else
                out.println("%s%s %s", separator, Configuration.class, configurationArgument);

            separator = ", ";
        }

        for (ParameterDefinition parameter : procedure.getInParameters()) {

            // Skip SELF parameter
            if (instance && parameter.equals(procedure.getInParameters().get(0)))
                continue;

            final String memberName = getStrategy().getJavaMemberName(parameter);
            final String typeName = refNumberType(out, parameter.getType(resolver(out)));

            if (scala)
                out.println("%s%s: %s", separator, scalaWhitespaceSuffix(memberName), typeName);
            else if (kotlin)
                out.println("%s%s: %s?", separator, memberName, typeName);
            else
                out.println("%s%s %s", separator, typeName, memberName);

            separator = ", ";
        }

        if (scala) {
            out.println("): %s = {", outParams.size() == 0 ? "Unit" : outParams.size() == 1 ? firstOutParamType : className);
            out.println("val %s = new %s", localVar, className);
        }
        else if (kotlin) {
            out.println("): %s%s {", outParams.size() == 0 ? "Unit" : outParams.size() == 1 ? firstOutParamType : className, outParams.size() == 1 ? "?" : "");
            out.println("val %s = %s()", localVar, className);
        }
        else {
            out.println(") {");
            out.println("%s %s = new %s();", className, localVar, className);
        }

        for (ParameterDefinition parameter : procedure.getInParameters()) {
            final String setter = getStrategy().getJavaSetterName(parameter, Mode.DEFAULT);
            final String arg = (instance && parameter.equals(procedure.getInParameters().get(0)))
                ? "this"
                : getStrategy().getJavaMemberName(parameter);

            out.println("%s.%s(%s)%s", localVar, setter, arg, semicolon);
        }

        out.println();
        out.println("%s.execute(%s)%s", localVar, instance ? "configuration()" : configurationArgument, semicolon);

        if (outParams.size() > 0) {
            final ParameterDefinition parameter = outParams.get(0);

            // Avoid disambiguation for RETURN_VALUE getter
            final String getter = parameter == procedure.getReturnValue()
                ? "getReturnValue"
                : getStrategy().getJavaGetterName(parameter, Mode.DEFAULT);
            final boolean isUDT = parameter.getType(resolver(out)).isUDT();

            if (instance) {

                // [#3117] Avoid funny call-site ambiguity if this is a UDT that is implemented by an interface
                if (generateInterfaces() && isUDT) {
                    final String columnTypeInterface = out.ref(getJavaType(parameter.getType(resolver(out, Mode.INTERFACE)), out, Mode.INTERFACE));

                    if (scala)
                        out.println("from(%s.%s.asInstanceOf[%s])", localVar, getter, columnTypeInterface);
                    else if (kotlin)
                        out.println("from(%s.%s() as %s);", localVar, getter, columnTypeInterface);
                    else
                        out.println("from((%s) %s.%s());", columnTypeInterface, localVar, getter);
                }
                else {
                    out.println("from(%s.%s%s)%s", localVar, getter, emptyparens, semicolon);
                }
            }

            if (outParams.size() == 1)
                out.println("return %s.%s%s%s", localVar, getter, emptyparens, semicolon);
            else if (outParams.size() > 1)
                out.println("return %s%s", localVar, semicolon);
        }

        out.println("}");
    }

    protected void printConvenienceMethodTableValuedFunction(JavaWriter out, TableDefinition function, String methodName) {
        // [#281] - Java can't handle more than 255 method parameters
        if (function.getParameters().size() > 254) {
            log.info("Too many parameters", "Function " + function + " has more than 254 in parameters. Skipping generation of convenience method.");
            return;
        }

        final String recordClassName = out.ref(getStrategy().getFullJavaClassName(function.getReferencedTable(), Mode.RECORD));

        // [#3456] Local variables should not collide with actual function arguments
        final String configurationArgument = disambiguateJavaMemberName(function.getParameters(), "configuration");

        // [#5765] To prevent name clashes, this identifier is not imported
        final String functionName = getStrategy().getFullJavaIdentifier(function);

        if (!printDeprecationIfUnknownTypes(out, function.getParameters()))
            out.javadoc("Call <code>%s</code>.", function.getQualifiedOutputName());

        if (scala)
            out.println("%sdef %s(", visibility(), methodName);
        else if (kotlin)
            out.println("%sfun %s(", visibility(), methodName);
        else
            out.println("%sstatic %s<%s> %s(", visibility(), Result.class, recordClassName, methodName);

        String separator = "  ";
        if (scala)
            out.println("%s%s: %s", separator, scalaWhitespaceSuffix(configurationArgument), Configuration.class);
        else if (kotlin)
            out.println("%s%s: %s", separator, configurationArgument, Configuration.class);
        else
            out.println("%s%s %s", separator, Configuration.class, configurationArgument);

        printParameterDeclarations(out, function.getParameters(), false, ", ");

        if (scala) {
            out.println("): %s[%s] = %s.dsl().selectFrom(%s.call(", Result.class, recordClassName, configurationArgument, functionName);
        }
        else if (kotlin) {
            out.println("): %s<%s> = %s.dsl().selectFrom(%s.call(", Result.class, recordClassName, configurationArgument, functionName);
        }
        else {
            out.println(") {");
            out.println("return %s.dsl().selectFrom(%s.call(", configurationArgument, functionName);
        }

        separator = "  ";
        for (ParameterDefinition parameter : function.getParameters()) {
            out.println("%s%s", separator, getStrategy().getJavaMemberName(parameter));

            separator = ", ";
        }

        if (scala || kotlin)
            out.println(")).fetch()");
        else
            out.println(")).fetch();")
               .println("}");
    }

    protected void printRecordTypeMethod(JavaWriter out, Definition tableOrUDT) {
        final String className = out.ref(getStrategy().getFullJavaClassName(tableOrUDT instanceof TableDefinition ? ((TableDefinition) tableOrUDT).getReferencedTable() : tableOrUDT, Mode.RECORD));

        out.javadoc("The class holding records for this type");

        if (scala) {
            out.println("%soverride def getRecordType: %s[%s] = classOf[%s]", visibilityPublic(), Class.class, className, className);
        }
        else if (kotlin) {
            out.println("%soverride fun getRecordType(): %s<%s> = %s::class.java", visibilityPublic(), Class.class, className, className);
        }
        else {
            out.override();
            printNonnullAnnotation(out);
            out.println("%s%s<%s> getRecordType() {", visibilityPublic(), Class.class, className);
            out.println("return %s.class;", className);
            out.println("}");
        }
    }

    protected void printSingletonInstance(JavaWriter out, Definition definition) {
        final String className = getStrategy().getJavaClassName(definition);
        final String identifier = getStrategy().getJavaIdentifier(definition);

        out.javadoc("The reference instance of <code>%s</code>", definition.getQualifiedOutputName());

        if (scala)
            out.println("%sval %s = new %s", visibility(), identifier, className);
        else if (kotlin)
            out.println("%sval %s: %s = %s()", visibility(), identifier, className, className);
        else
            out.println("%sstatic final %s %s = new %s();", visibility(), className, identifier, className);
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

    private String comment(Definition definition) {
        return definition instanceof CatalogDefinition && generateCommentsOnCatalogs()
            || definition instanceof SchemaDefinition && generateCommentsOnSchemas()
            || definition instanceof TableDefinition && generateCommentsOnTables()
            || definition instanceof ColumnDefinition && generateCommentsOnColumns()
            || definition instanceof EmbeddableDefinition && generateCommentsOnEmbeddables()
            || definition instanceof UDTDefinition && generateCommentsOnUDTs()
            || definition instanceof AttributeDefinition && generateCommentsOnAttributes()
            || definition instanceof PackageDefinition && generateCommentsOnPackages()
            || definition instanceof RoutineDefinition && generateCommentsOnRoutines()
            || definition instanceof ParameterDefinition && generateCommentsOnParameters()
            || definition instanceof SequenceDefinition && generateCommentsOnSequences()
             ? StringUtils.defaultIfBlank(definition.getComment(), "")
             : "";
    }

    private String referencingComment(EmbeddableDefinition definition) {
        return generateCommentsOnEmbeddables()
             ? StringUtils.defaultIfBlank(definition.getReferencingComment(), "")
             : "";
    }

    protected void printClassJavadoc(JavaWriter out, String comment) {
        if (generateJavadoc()) {
            out.println("/**");

            if (comment != null && comment.length() > 0)
                out.println(JavaWriter.escapeJavadoc(comment));
            else
                out.println("This class is generated by jOOQ.");

            out.println(" */");
        }
    }

    /**
     * @deprecated - [#10355] - 3.14.0 - This method is no longer used by the
     *             code generator. Use a
     *             {@link #printClassAnnotations(JavaWriter, Definition, Mode)}
     *             instead.
     */
    @SuppressWarnings("unused")
    @Deprecated
    protected final void printClassAnnotations(JavaWriter out, SchemaDefinition schema) {}

    /**
     * @deprecated - [#10355] - 3.14.0 - This method is no longer used by the
     *             code generator. Use a
     *             {@link #printClassAnnotations(JavaWriter, Definition, Mode)}
     *             instead.
     */
    @SuppressWarnings("unused")
    @Deprecated
    protected final void printClassAnnotations(JavaWriter out, SchemaDefinition schema, CatalogDefinition catalog) {}

    protected void printClassAnnotations(JavaWriter out, Definition definition, Mode mode) {
        if (generateGeneratedAnnotation()) {
            SchemaDefinition schema = definition.getSchema();
            CatalogDefinition catalog = definition.getCatalog();

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
                        Reflect.onClass("java.util.Optional").call("of", new Object()).call("stream");
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
                case ORG_JOOQ_GENERATED:
                    generated = Generated.class.getName();
                    break;
                default:
                    throw new IllegalStateException("Unsupported type: " + type);
            }

            out.println("@%s(", out.ref(generated));

            if (useSchemaVersionProvider() || useCatalogVersionProvider()) {
                boolean hasCatalogVersion = !StringUtils.isBlank(catalogVersions.get(catalog));
                boolean hasSchemaVersion = !StringUtils.isBlank(schemaVersions.get(schema));

                if (scala)
                    out.println("value = %s(", out.ref("scala.Array"));
                else if (kotlin)
                    out.println("value = [");
                else
                    out.println("value = {");

                out.println("\"https://www.jooq.org\"%s", (generateGeneratedAnnotationJooqVersion() || hasCatalogVersion || hasSchemaVersion ? "," : ""));

                if (generateGeneratedAnnotationJooqVersion())
                    out.println("\"jOOQ version:%s\"%s", Constants.VERSION, (hasCatalogVersion || hasSchemaVersion ? "," : ""));
                if (hasCatalogVersion)
                    out.println("\"catalog version:%s\"%s", escapeString(catalogVersions.get(catalog)), (hasSchemaVersion ? "," : ""));
                if (hasSchemaVersion)
                    out.println("\"schema version:%s\"", escapeString(schemaVersions.get(schema)));

                if (scala)
                    out.println("),");
                else if (kotlin)
                    out.println("],");
                else
                    out.println("},");

                if (generateGeneratedAnnotationDate())
                    out.println("date = \"" + isoDate + "\",");
                out.println("comments = \"This class is generated by jOOQ\"");
            }
            else {
                if (scala)
                    out.println("value = %s(", out.ref("scala.Array"));
                else if (kotlin)
                    out.println("value = [");
                else
                    out.println("value = {");

                out.println("\"https://www.jooq.org\"%s", (generateGeneratedAnnotationJooqVersion() ? "," : ""));

                if (generateGeneratedAnnotationJooqVersion())
                    out.println("\"jOOQ version:%s\"", Constants.VERSION);

                if (scala)
                    out.println("),");
                else if (kotlin)
                    out.println("],");
                else
                    out.println("},");

                out.println("comments = \"This class is generated by jOOQ\"");
            }

            out.println(")");
        }

        if (scala) {}
        else if (kotlin)
            out.println("@Suppress(\"UNCHECKED_CAST\")");
        else
            out.println("@%s({ \"all\", \"unchecked\", \"rawtypes\" })", out.ref("java.lang.SuppressWarnings"));
    }

    private String readVersion(File file, String type) {
        try (RandomAccessFile f = new RandomAccessFile(file, "r")) {
            byte[] bytes = new byte[(int) f.length()];
            f.readFully(bytes);
            String string = new String(bytes);

            Matcher matcher = Pattern.compile("@(?:javax\\.annotation\\.)?Generated\\(\\s*?value\\s*?=\\s*?" + (kotlin ? "\\[[^]]*?" : scala ? "Array\\([^)]*?" : "\\{[^}]*?") + "\"" + type + " version:([^\"]*?)\"").matcher(string);
            if (matcher.find())
                return matcher.group(1);
        }
        catch (IOException ignore) {}

        return null;
    }

    protected void printPackage(JavaWriter out, Definition definition) {
        printPackage(out, definition, Mode.DEFAULT);
    }

    protected void printPackage(JavaWriter out, Definition definition, Mode mode) {
        printPackageComment(out, definition, mode);

        out.printPackageSpecification(getStrategy().getJavaPackageName(definition, mode));
        out.println();
        out.printImports();
        out.println();
    }

    protected void printGlobalReferencesPackage(JavaWriter out, Definition container, Class<? extends Definition> objectType) {
        printGlobalReferencesPackageComment(out, container, objectType);

        out.printPackageSpecification(getStrategy().getGlobalReferencesJavaPackageName(container, objectType));
        out.println();
        out.printImports();
        out.println();
    }

    protected void printPackageComment(JavaWriter out, Definition definition, Mode mode) {
        String header = getStrategy().getFileHeader(definition, mode);

        if (!StringUtils.isBlank(header)) {
            out.println("/*");
            out.println(JavaWriter.escapeJavadoc(header));
            out.println(" */");
        }
    }

    protected void printGlobalReferencesPackageComment(JavaWriter out, Definition container, Class<? extends Definition> objectType) {
        String header = getStrategy().getGlobalReferencesFileHeader(container, objectType);

        if (!StringUtils.isBlank(header)) {
            out.println("/*");
            out.println(JavaWriter.escapeJavadoc(header));
            out.println(" */");
        }
    }

    private final <T> void forEach(Collection<T> list, BiConsumer<? super T, ? super String> definitionAndSeparator) {
        forEach(list, "", ",", definitionAndSeparator);
    }

    private final <T> void forEach(Collection<T> list, String nonSeparator, String separator, BiConsumer<? super T, ? super String> definitionAndSeparator) {
        int size = list.size();
        int i = 0;

        for (T d : list)
            definitionAndSeparator.accept(d, i++ < size - 1 ? separator : nonSeparator);
    }

    protected String refExtendsNumberType(JavaWriter out, DataTypeDefinition type) {
        if (type.isGenericNumberType())
            return (scala ? "_ <: " : kotlin ? "out ": "? extends ") + out.ref(Number.class);
        else
            return out.ref(getJavaType(type, out));
    }

    protected String refNumberType(JavaWriter out, DataTypeDefinition type) {
        if (type.isGenericNumberType())
            return out.ref(Number.class);
        else
            return out.ref(getJavaType(type, out));
    }

    protected String getJavaTypeReference(Database db, DataTypeDefinition type, JavaWriter out) {

        // [#4388] TODO: Improve array handling
        if (database.isArrayType(type.getType())) {
            DataTypeDefinition base = GenerationUtil.getArrayBaseType(db.getDialect(), type);

            // [#252] This check prevents StackOverflowError in case of e.g. PostgreSQL ANYARRAY types
            if (base != type)
                return getJavaTypeReference(db, base, out) + ".array()";
            else
                return getJavaTypeReference0(db, type, out) + ".array()";
        }

        else
            return getJavaTypeReference0(db, type, out);
    }

    private String getJavaTypeReference0(Database db, DataTypeDefinition type, JavaWriter out) {
        return getTypeReference(
            db,
            type.getSchema(),
            out,
            type.getType(),
            type.getPrecision(),
            type.getScale(),
            type.getLength(),
            type.isNullable(),
            type.isIdentity(),
            type.isReadonly(),
            type.getGeneratedAlwaysAs(),
            type.getGenerationOption(),
            type.getGenerator(),
            type.getDefaultValue(),
            type.getQualifiedUserType()
        );
    }

    private class Resolver implements JavaTypeResolver {
        private final JavaWriter out;
        private final Mode       mode;

        Resolver(JavaWriter out, Mode mode) {
            this.out = out;
            this.mode = mode;
        }

        @Override
        public String resolve(DataTypeDefinition type) {
            return mode == null ? getJavaType(type, out) : getJavaType(type, out, mode);
        }

        @Override
        public String resolve(Name name) {
            return getType(
                database,

                // [#15276] TODO: Handle SQL Server catalogs
                database.getSchema(name.qualifier().last()),
                out,
                name.last(),
                0,
                0,
                name,
                null,
                Object.class.getName(),
                mode == null ? Mode.RECORD : mode,
                null
            );
        }

        @Override
        public String classLiteral(String type) {
            String rawtype;

            switch (language) {
                case SCALA:
                    rawtype = type.replaceAll("\\[.*\\]", "");
                    break;

                case KOTLIN:
                case JAVA:
                default:
                    rawtype = type.replaceAll("<.*>", "");
                    break;
            }

            boolean generic = !rawtype.equals(type);

            switch (language) {
                case SCALA:
                    return "classOf[" + out.ref(type) + "]";

                case KOTLIN:
                    return out.ref("kotlin.Array".equals(rawtype) ? type : rawtype) + "::class.java" + (generic ? (" as " + out.ref(Class.class) + "<" + out.ref(type) + ">") : "");

                // The double cast is required only in Java 8 and less, not in Java 11
                case JAVA:
                default:
                    return (generic ? "(" + out.ref(Class.class) + "<" + out.ref(type) + ">) (" + out.ref(Class.class) + ") " : "") + out.ref(rawtype) + ".class";
            }
        }

        @Override
        public String constructorCall(String type) {
            String rawtype = type.replaceAll("<.*>", "").replaceAll("\\[.*\\]", "");
            String typeParams = type.replace(rawtype, "");

            switch (language) {
                case SCALA:
                    return "new " + out.ref(rawtype) + typeParams.replace("<", "[").replace(">", "]");

                case KOTLIN:
                    return out.ref(rawtype) + typeParams;

                case JAVA:
                default:
                    return "new " + out.ref(rawtype) + typeParams;
            }
        }

        @Override
        public String ref(String type) {
            return out.ref(type);
        }

        @Override
        public String ref(Class<?> type) {
            return out.ref(type);
        }
    }

    protected JavaTypeResolver resolver(JavaWriter out) {
        return new Resolver(out, null);
    }

    protected JavaTypeResolver resolver(JavaWriter out, Mode mode) {
        return new Resolver(out, mode);
    }

    protected boolean isObjectArrayType(String javaType) {
        if (scala)
            return javaType.startsWith("scala.Array");
        else if (kotlin)
            return javaType.startsWith("kotlin.Array");
        else
            return javaType.endsWith("[]") && !javaType.equals("byte[]");
    }

    protected boolean isArrayType(String javaType) {
        if (scala)
            return javaType.startsWith("scala.Array");
        else if (kotlin)
            return javaType.startsWith("kotlin.Array") || javaType.equals("kotlin.ByteArray");
        else
            return javaType.endsWith("[]");
    }

    protected String getArrayBaseType(String javaType) {
        String result = javaType
            .replace("[]", "")
            .replaceAll("^scala.Array\\[(.*?)\\]$", "$1")
            .replaceAll("^kotlin.Array<(.*?)\\??>$", "$1");

        if (result.equals(javaType))
            return result;
        else
            return getArrayBaseType(result);
    }

    protected String getJavaType(DataTypeDefinition type, JavaWriter out) {
        return getJavaType(type, out, Mode.RECORD);
    }

    protected String getJavaType(DataTypeDefinition type, JavaWriter out, Mode udtMode) {
        return getType(
            type.getDatabase(),
            type.getSchema(),
            out,
            type.getType(),
            type.getPrecision(),
            type.getScale(),
            type.getQualifiedUserType(),
            type.getJavaType(),
            Object.class.getName(),
            udtMode,
            type.getXMLTypeDefinition()
        );
    }

    private DataType<?> getRuntimeDataType(DataTypeDefinition type) {
        try {
            return mapTypes(getDataType(type.getDatabase(), type.getType(), type.getPrecision(), type.getScale()));
        }
        catch (SQLDialectNotSupportedException ignore) {
            return SQLDataType.OTHER;
        }
    }

    /**
     * @deprecated - 3.9.0 - [#330]  - Use {@link #getType(Database, SchemaDefinition, String, int, int, Name, String, String)} instead.
     */
    @Deprecated
    protected String getType(Database db, SchemaDefinition schema, JavaWriter out, String t, int p, int s, String u, String javaType, String defaultType) {
        return getType(db, schema, out, t, p, s, name(u), javaType, defaultType);
    }

    protected String getType(Database db, SchemaDefinition schema, JavaWriter out, String t, int p, int s, Name u, String javaType, String defaultType) {
        return getType(db, schema, out, t, p, s, u, javaType, defaultType, Mode.RECORD);
    }

    /**
     * @deprecated - 3.9.0 - [#330]  - Use {@link #getType(Database, SchemaDefinition, String, int, int, Name, String, String, Mode)} instead.
     */
    @Deprecated
    protected String getType(Database db, SchemaDefinition schema, JavaWriter out, String t, int p, int s, String u, String javaType, String defaultType, Mode udtMode) {
        return getType(db, schema, out, t, p, s, name(u), javaType, defaultType, udtMode);
    }

    protected String getType(Database db, SchemaDefinition schema, JavaWriter out, String t, int p, int s, Name u, String javaType, String defaultType, Mode udtMode) {
        return getType(db, schema, out, t, p, s, u, javaType, defaultType, udtMode, null);
    }

    protected String getType(Database db, SchemaDefinition schema, JavaWriter out, String t, int p, int s, Name u, String javaType, String defaultType, Mode udtMode, XMLTypeDefinition xmlType) {
        String type = defaultType;

        // XML types
        if (xmlType != null) {
            type = out.ref(getStrategy().getFullJavaClassName(xmlType));
        }

        // Custom types
        else if (javaType != null) {
            type = javaType;
        }

        // Array types
        else if (db.isArrayType(t)) {

            // [#4388] TODO: Improve array handling
            BaseType bt = GenerationUtil.getArrayBaseType(db.getDialect(), t, u);

            // [#9067] Prevent StackOverflowErrors
            String newT = t.equals(bt.t()) ? "OTHER" : bt.t();

            // [#10309] TODO: The schema should be taken from baseType, if available. Might be different than the argument schema.
            //          When can this happen?
            String baseType = getType(db, schema, out, newT, p, s, bt.u(), javaType, defaultType, udtMode);

            if (scala)
                type = "scala.Array[" + baseType + "]";
            else if (kotlin)
                type = "kotlin.Array<" + baseType + "?>";
            else
                type = baseType + "[]";
        }

        // Check for Oracle-style VARRAY types
        else if (db.getArray(schema, u) != null) {
            boolean udtArray = db.getArray(schema, u).getElementType(resolver(out)).isUDT();

            if (udtMode == Mode.POJO || (udtMode == Mode.INTERFACE && !udtArray)) {
                if (scala)
                    type = "java.util.List[" + getJavaType(db.getArray(schema, u).getElementType(resolver(out, udtMode)), out, udtMode) + "]";
                else
                    type = "java.util.List<" + getJavaType(db.getArray(schema, u).getElementType(resolver(out, udtMode)), out, udtMode) + ">";
            }
            else if (udtMode == Mode.INTERFACE) {
                if (scala)
                    type = "java.util.List[_ <:" + getJavaType(db.getArray(schema, u).getElementType(resolver(out, udtMode)), out, udtMode) + "]";
                else
                    type = "java.util.List<? extends " + getJavaType(db.getArray(schema, u).getElementType(resolver(out, udtMode)), out, udtMode) + ">";
            }
            else {
                type = getStrategy().getFullJavaClassName(db.getArray(schema, u), Mode.RECORD);
            }
        }

        // Check for DOMAIN types
        else if (db.getDomain(schema, u) != null) {
            type = getJavaType(db.getDomain(schema, u).getType(resolver(out)), out);
        }

        // Check for ENUM types
        else if (db.getEnum(schema, u) != null) {
            type = getStrategy().getFullJavaClassName(db.getEnum(schema, u), Mode.ENUM);
        }

        // Check for UDTs
        else if (db.getUDT(schema, u) != null) {
            type = getStrategy().getFullJavaClassName(db.getUDT(schema, u), udtMode);
        }

        // [#3942] [#7863] Dialects that support tables as UDTs
        // [#5334] In MySQL, the user type is (ab)used for synthetic enum types. This can lead to accidental matches here
        else if (SUPPORT_TABLE_AS_UDT.contains(db.getDialect()) && db.getTable(schema, u) != null) {
            type = getStrategy().getFullJavaClassName(db.getTable(schema, u), udtMode);
        }

        // Check for custom types
        else if (u != null && db.getConfiguredCustomType(u.last()) != null) {
            type = u.last();
        }

        // Try finding a basic standard SQL type according to the current dialect
        else {
            try {
                Class<?> clazz = mapTypes(getDataType(db, t, p, s)).getType();
                if (scala && clazz == byte[].class)
                    type = "scala.Array[scala.Byte]";
                else if (kotlin && clazz == byte[].class)
                    type = "kotlin.ByteArray";
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

        if (kotlin && Object.class.getName().equals(type))
            type = "Any";

        return type;
    }

    protected String getTypeReference(
        Database db,
        SchemaDefinition schema,
        JavaWriter out,
        String t,
        int p,
        int s,
        int l,
        boolean n,
        boolean i,
        boolean r,
        String g,
        GenerationOption go,
        String ge,
        String d,
        Name u
    ) {
        StringBuilder sb = new StringBuilder();

        if (db.getArray(schema, u) != null) {
            ArrayDefinition array = database.getArray(schema, u);

            sb.append(getJavaTypeReference(db, array.getElementType(resolver(out)), out));
            sb.append(array.getIndexType() != null ? ".asAssociativeArrayDataType(" : ".asArrayDataType(");
            sb.append(classOf(out.ref(getStrategy().getFullJavaClassName(array, Mode.RECORD))));
            sb.append(")");
        }
        else if (db.getDomain(schema, u) != null) {
            final String sqlDataTypeRef = out.ref(getStrategy().getFullJavaIdentifier(db.getDomain(schema, u)), domainRefSegments()) + ".getDataType()";
            sb.append(sqlDataTypeRef);

            appendTypeReferenceNullability(db, out, sb, n);
            appendTypeReferenceDefault(db, out, sb, d, sqlDataTypeRef);
        }
        else if (db.getUDT(schema, u) != null) {
            sb.append(out.ref(getStrategy().getFullJavaIdentifier(db.getUDT(schema, u)), 2));
            sb.append(".getDataType()");
        }
        // [#3942] [#7863] Dialects that support tables as UDTs
        // [#5334] In MySQL, the user type is (ab)used for synthetic enum types. This can lead to accidental matches here
        else if (SUPPORT_TABLE_AS_UDT.contains(db.getDialect()) && db.getTable(schema, u) != null) {
            sb.append(out.ref(getStrategy().getFullJavaIdentifier(db.getTable(schema, u)), 2));
            sb.append(".getDataType()");
        }
        else if (db.getEnum(schema, u) != null) {
            sb.append(getJavaTypeReference(db, new DefaultDataTypeDefinition(
                db,
                schema,
                DefaultDataType.getDataType(db.getDialect(), String.class).getTypeName(),
                l, p, s, n, r, g, d, i, (Name) null, ge, null, null, null
            ), out));
            sb.append(".asEnumDataType(");
            sb.append(classOf(out.ref(getStrategy().getFullJavaClassName(db.getEnum(schema, u), Mode.ENUM))));
            sb.append(")");
        }
        else {
            DataType<?> dataType;
            String sqlDataTypeRef;

            try {
                dataType = mapTypes(getDataType(db, t, p, s));
            }

            // Mostly because of unsupported data types.
            catch (SQLDialectNotSupportedException ignore) {
                dataType = SQLDataType.OTHER.nullable(n).identity(i);

                sb = new StringBuilder();

                sb.append(out.ref(DefaultDataType.class));
                sb.append(".getDefaultDataType(\"");
                sb.append(escapeString(u != null ? u.toString() : t));
                sb.append("\")");
            }

            dataType = dataType.nullable(n).identity(i);







            if (d != null)
                dataType = dataType.defaultValue((Field) DSL.field(d, dataType));

            // If there is a standard SQLDataType available for the dialect-
            // specific DataType t, then reference that one.
            if (dataType.getSQLDataType() != null && sb.length() == 0) {
                DataType<?> sqlDataType = dataType.getSQLDataType();
                String literal = SQLDATATYPE_LITERAL_LOOKUP.get(sqlDataType);
                sqlDataTypeRef =
                    out.ref(SQLDataType.class)
                  + '.'
                  + (literal == null ? "OTHER" : literal);

                sb.append(sqlDataTypeRef);

                if (dataType.hasPrecision() && (dataType.isTimestamp() || p > 0)) {

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

            appendTypeReferenceNullability(db, out, sb, n);

            if (dataType.identity())
                sb.append(".identity(true)");























            // [#5291] Some dialects report valid SQL expresions (e.g. PostgreSQL), others
            //         report actual values (e.g. MySQL).
            if (dataType.defaulted())
                appendTypeReferenceDefault(db, out, sb, d, sqlDataTypeRef);
        }

        return sb.toString();
    }

    private final void appendTypeReferenceNullability(Database db, JavaWriter out, StringBuilder sb, boolean n) {
        if (!n)
            sb.append(".nullable(false)");
    }

    private final void appendTypeReferenceDefault(Database db, JavaWriter out, StringBuilder sb, String d, String sqlDataTypeRef) {
        if (d != null) {
            sb.append(".defaultValue(");

            if (asList(MYSQL).contains(db.getDialect().family()))

                // [#5574] While MySQL usually reports actual values, it does report
                //         a CURRENT_TIMESTAMP expression, inconsistently
                if (d != null && d.toLowerCase(getStrategy().getTargetLocale()).startsWith("current_timestamp"))
                    sb.append(out.ref(DSL.class))
                      .append(".field(")
                      .append(out.ref(DSL.class))
                      .append(".raw(\"")
                      .append(escapeString(d))
                      .append("\")");
                else
                    sb.append(out.ref(DSL.class))
                      .append(".inline(\"")
                      .append(escapeString(d))
                      .append("\"");
            else
                sb.append(out.ref(DSL.class))
                  .append(".field(")
                  .append(out.ref(DSL.class))
                  .append(".raw(\"")
                  .append(escapeString(d))
                  .append("\")");

            sb.append(", ")
              .append(sqlDataTypeRef)
              .append(")")
              .append(kotlin && sqlDataTypeRef.contains(".OTHER") ? " as Any?" : "")
              .append(")");
        }
    }

    private DataType<?> mapTypes(DataType<?> dataType) {
        DataType<?> result = dataType;

        // [#4429] [#5713] This logic should be implemented in Configuration
        if (dataType.isDateTime() && generateJavaTimeTypes()) {
            if (dataType.getType() == Date.class)
                result = SQLDataType.LOCALDATE;
            else if (dataType.getType() == Time.class)
                result = SQLDataType.LOCALTIME;
            else if (dataType.getType() == Timestamp.class)
                result = SQLDataType.LOCALDATETIME;
        }

        // [#13143] Turn off support for some types
        else if (
            dataType.isSpatial() && !generateSpatialTypes() ||
            dataType.isJSON() && !generateJsonTypes() ||
            dataType.isXML() && !generateXmlTypes() ||
            dataType.isInterval() && !generateIntervalTypes()
        )
            result = SQLDataType.OTHER;

        return result;
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
        else if (kotlin)
            return string + "::class.java";
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
        JavaWriter result = new JavaWriter(file, generateFullyQualifiedTypes(), targetEncoding, generateJavadoc(), fileCache, generatedSerialVersionUID());

        if (generateIndentation != null)
            result.tabString(generateIndentation);
        if (generateNewline != null)
            result.newlineString(generateNewline);

        result.printMarginForBlockComment(generatePrintMarginForBlockComment);
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
        else if (kotlin)
            file = new File(file.getParentFile(), file.getName().replace(".java", ".kt"));

        return file;
    }

    private static final Pattern P_SCALA_WHITESPACE_SUFFIX = Pattern.compile("^.*[^a-zA-Z0-9]$");

    private String scalaWhitespaceSuffix(String string) {
        return string == null
             ? null
             : P_SCALA_WHITESPACE_SUFFIX.matcher(string).matches()
             ? (string + " ")
             : string;
    }

    // [#4626] Users may need to call this method
    protected void closeJavaWriter(JavaWriter out) {
        CloseResult result = out.close();

        if (result.affected)
            affectedFiles.add(out.file());

        if (result.modified)
            modifiedFiles.add(out.file());
    }
}
