/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq.util;


import static org.jooq.tools.StringUtils.defaultIfBlank;
import static org.jooq.tools.StringUtils.defaultString;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.TypeVariable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
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
import org.jooq.Configuration;
import org.jooq.Constants;
import org.jooq.DataType;
import org.jooq.EnumType;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Parameter;
import org.jooq.Record;
import org.jooq.Row;
import org.jooq.Schema;
import org.jooq.Sequence;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UDT;
import org.jooq.UDTField;
import org.jooq.UniqueKey;
import org.jooq.exception.SQLDialectNotSupportedException;
import org.jooq.impl.AbstractKeys;
import org.jooq.impl.AbstractRoutine;
// ...
import org.jooq.impl.DAOImpl;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultDataType;
import org.jooq.impl.PackageImpl;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.SchemaImpl;
import org.jooq.impl.SequenceImpl;
import org.jooq.impl.TableImpl;
import org.jooq.impl.TableRecordImpl;
import org.jooq.impl.UDTImpl;
import org.jooq.impl.UDTRecordImpl;
import org.jooq.impl.UpdatableRecordImpl;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.StopWatch;
import org.jooq.tools.StringUtils;
import org.jooq.tools.reflect.Reflect;
import org.jooq.tools.reflect.ReflectException;
import org.jooq.util.GeneratorStrategy.Mode;
// ...
// ...
// ...
// ...
import org.jooq.util.postgres.PostgresDatabase;


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

    private static final JooqLogger       log                          = JooqLogger.getLogger(JavaGenerator.class);

    /**
     * The Javadoc to be used for private constructors
     */
    private static final String           NO_FURTHER_INSTANCES_ALLOWED = "No further instances allowed";

    /**
     * [#1459] Prevent large static initialisers by splitting nested classes
     */
    private static final int              INITIALISER_SIZE             = 500;

    /**
     * An overall stop watch to measure the speed of source code generation
     */
    private final StopWatch               watch                        = new StopWatch();

    /**
     * The underlying database of this generator
     */
    private Database                      database;

    /**
     * The code generation date, if needed.
     */
    private String                        isoDate;

    /**
     * The cached schema version numbers
     */
    private Map<SchemaDefinition, String> schemaVersions;

    /**
     * All files modified by this generator
     */
    private Set<File>                     files                        = new LinkedHashSet<File>();

    @Override
    public final void generate(Database db) {
        this.isoDate = DatatypeConverter.printDateTime(Calendar.getInstance(TimeZone.getTimeZone("UTC")));
        this.schemaVersions = new LinkedHashMap<SchemaDefinition, String>();

        this.database = db;
        this.database.addFilter(new AvoidAmbiguousClassesFilter());
        this.database.setIncludeRelations(generateRelations());

        String url = "";
        try {
            Connection connection = database.getConnection();

            if (connection != null)
                url = connection.getMetaData().getURL();
        }
        catch (SQLException ignore) {}

        log.info("License parameters");
        log.info("----------------------------------------------------------");
        log.info("  Thank you for using jOOQ and jOOQ's code generator");
        log.info("");
        log.info("Database parameters");
        log.info("----------------------------------------------------------");
        log.info("  dialect", database.getDialect());
        log.info("  URL", url);
        log.info("  target dir", getTargetDirectory());
        log.info("  target package", getTargetPackage());
        log.info("  includes", Arrays.asList(database.getIncludes()));
        log.info("  excludes", Arrays.asList(database.getExcludes()));
        log.info("  includeExcludeColumns", database.getIncludeExcludeColumns());
        log.info("----------------------------------------------------------");
        log.info("");
        log.info("DefaultGenerator parameters");
        log.info("----------------------------------------------------------");
        log.info("  strategy", strategy.delegate.getClass());
        log.info("  deprecated", generateDeprecated());
        log.info("  generated annotation", generateGeneratedAnnotation()
            + ((!generateGeneratedAnnotation && useSchemaVersionProvider) ? " (forced to true because of <schemaVersionProvider/>)" : ""));
        log.info("  JPA annotations", generateJPAAnnotations());
        log.info("  validation annotations", generateValidationAnnotations());
        log.info("  instance fields", generateInstanceFields());
        log.info("  records", generateRecords()
            + ((!generateRecords && generateDaos) ? " (forced to true because of <daos/>)" : ""));
        log.info("  pojos", generatePojos()
            + ((!generatePojos && generateDaos) ? " (forced to true because of <daos/>)" :
              ((!generatePojos && generateImmutablePojos) ? " (forced to true because of <immutablePojos/>)" : "")));
        log.info("  immutable pojos", generateImmutablePojos());
        log.info("  interfaces", generateInterfaces());
        log.info("  daos", generateDaos());
        log.info("  relations", generateRelations()
            + ((!generateRelations && generateDaos) ? " (forced to true because of <daos/>)" : ""));
        log.info("  global references", generateGlobalObjectReferences());
        log.info("----------------------------------------------------------");

        if (!generateInstanceFields()) {
            log.warn("");
            log.warn("Deprecation warnings");
            log.warn("----------------------------------------------------------");
            log.warn("  <generateInstanceFields/> = false is deprecated! Please adapt your configuration.");
        }

        log.info("");
        log.info("Generation remarks");
        log.info("----------------------------------------------------------");

        if (generateImmutablePojos && generateInterfaces)
            log.info("  immutable pojos", "Immutable POJOs do not have any setters. Hence, setters are also missing from interfaces");
        else
            log.info("  none");

        log.info("");
        log.info("----------------------------------------------------------");

        // ----------------------------------------------------------------------
        // XXX Generating schemas
        // ----------------------------------------------------------------------
        log.info("Generating schemata", "Total: " + database.getSchemata().size());
        for (SchemaDefinition schema : database.getSchemata()) {
            try {
                generate(schema);
            }
            catch (Exception e) {
                throw new GeneratorException("Error generating code for schema " + schema, e);
            }
        }
    }

    private final void generate(SchemaDefinition schema) {
        String newVersion = schema.getDatabase().getSchemaVersionProvider().version(schema);

        if (StringUtils.isBlank(newVersion)) {
            log.info("No schema version is applied for schema " + schema.getInputName() + ". Regenerating.");
        }
        else {
            schemaVersions.put(schema, newVersion);
            String oldVersion = readVersion(getStrategy().getFile(schema));

            if (StringUtils.isBlank(oldVersion)) {
                log.info("No previous version available for schema " + schema.getInputName() + ". Regenerating.");
            }
            else if (!oldVersion.equals(newVersion)) {
                log.info("Existing version " + oldVersion + " is not up to date with " + newVersion + " for schema " + schema.getInputName() + ". Regenerating.");
            }
            else {
                log.info("Existing version " + oldVersion + " is up to date with " + newVersion + " for schema " + schema.getInputName() + ". Ignoring schema.");
                return;
            }
        }

        File targetPackage = getStrategy().getFile(schema).getParentFile();

        // ----------------------------------------------------------------------
        // XXX Initialising
        // ----------------------------------------------------------------------
        generateSchema(schema);

        if (generateGlobalObjectReferences() && database.getSequences(schema).size() > 0) {
            generateSequences(schema);
        }

        if (database.getTables(schema).size() > 0) {
            generateTables(schema);
        }

        if (generatePojos() && database.getTables(schema).size() > 0) {
            generatePojos(schema);
        }

        if (generateDaos() && database.getTables(schema).size() > 0) {
            generateDaos(schema);
        }

        if (generateGlobalObjectReferences() && database.getTables(schema).size() > 0) {
            generateTableReferences(schema);
        }

        if (generateRelations() && database.getTables(schema).size() > 0) {
            generateRelations(schema);
        }

        if (generateRecords() && database.getTables(schema).size() > 0) {
            generateRecords(schema);
        }

        if (generateInterfaces() && database.getTables(schema).size() > 0) {
            generateInterfaces(schema);
        }

        if (database.getUDTs(schema).size() > 0) {
            generateUDTs(schema);
        }

        if (generatePojos() && database.getUDTs(schema).size() > 0) {
            generateUDTPojos(schema);
        }

        if (database.getUDTs(schema).size() > 0) {
            generateUDTRecords(schema);
        }

        if (generateInterfaces() && database.getUDTs(schema).size() > 0) {
            generateUDTInterfaces(schema);
        }

        if (database.getUDTs(schema).size() > 0) {
            generateUDTRoutines(schema);
        }

        if (generateGlobalObjectReferences() && database.getUDTs(schema).size() > 0) {
            generateUDTReferences(schema);
        }

        if (database.getArrays(schema).size() > 0) {
            generateArrays(schema);
        }

        if (database.getEnums(schema).size() > 0) {
            generateEnums(schema);
        }

        if (database.getRoutines(schema).size() > 0 || hasTableValuedFunctions(schema)) {
            generateRoutines(schema);
        }

        if (database.getPackages(schema).size() > 0) {
            generatePackages(schema);
        }

        /* [pro] xx
        xx xxxxxxxxx xxxxxxxxxx xxxxxxxxxxxxxxx x
            xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        x
        xx [/pro] */

        log.info("Removing excess files");
        empty(getStrategy().getFile(schema).getParentFile(), ".java", files);
        files.clear();

        // XXX [#651] Refactoring-cursor
        watch.splitInfo("GENERATION FINISHED: " + schema.getQualifiedName());
    }

    private class AvoidAmbiguousClassesFilter implements Database.Filter {

        private Map<String, String> included = new HashMap<String, String>();

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

    /* [pro] xx
    xxxxxxxxx xxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxx x
        xxxxxxxxxxxxxx xxxxxxxxxxxxxx x xxxxxxxxxxxxxxxx xxxxxxxxx

        xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx x xx x
            xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        x
    x

    xxxxxxxxx xxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxx x
        xxxxxxxxxxxxxxxxxxxx xxxxxxxxx
        xxxxxxxxxxxxxx xxxxxxxxxxxxxx x xxxxxxxxxxxxxxxx xxxxxxxxx

        xxxxxxxxxx xxx x xxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxx xxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxx xxxxxx xx xxx xxxxxx xx x x xxxxxxxxxxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxx
        xxxxxxxxxxxxxxxxxxx xxxxx xxxxxx xxxx

        xxx xxxxxxxxxxxxxxxxxxxxxx xxxxx x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx x
            xxxxx xxxxxxxxxxxxx xxx x xxxxxxxxxxxxxxx

            xxxxx xxxxxx xxxxxxx x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
            xxxxx xxxxxx xxxxxxxxx x xxxxxxxxxxxxxxxxxxxxxx
            xxxxx xxxxxx xxxxxxxxx x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxx
            xxxxx xxxxxx xxxxx x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
            xxxxx xxxxxx xxxxxxxx x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

            xxxxxxxxxxxxxxxxxxxxxxx xxxxx xxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
            xxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxx xxxxx xxxxxx xx x xxx xxxxxxxxxxxxxx xxx xxxxxx xxxxxxxxxxxx xxxxxxxxxx xxxxxxxx xxxxxxxxxxxxxxxx xxxxxxxxxx xxxxxxxxxx xxxxxxxxx xxxxxxx
        x

        xxxxxxxxxxxxxxxxx
        xxxxxxxxxxxx

        xxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxx
    x
    xx [/pro] */

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

        JavaWriter out = newJavaWriter(new File(getStrategy().getFile(schema).getParentFile(), "Keys.java"));
        printPackage(out, schema);
        printClassJavadoc(out,
            "A class modelling foreign key relationships between tables of the <code>" + schema.getOutputName() + "</code> schema");
        printClassAnnotations(out, schema);

        out.println("public class Keys {");
        out.tab(1).header("IDENTITY definitions");
        out.println();

        List<IdentityDefinition> allIdentities = new ArrayList<IdentityDefinition>();
        List<UniqueKeyDefinition> allUniqueKeys = new ArrayList<UniqueKeyDefinition>();
        List<ForeignKeyDefinition> allForeignKeys = new ArrayList<ForeignKeyDefinition>();

        for (TableDefinition table : database.getTables(schema)) {
            try {
                IdentityDefinition identity = table.getIdentity();

                if (identity != null) {
                    final String identityType = getStrategy().getFullJavaClassName(identity.getColumn().getContainer(), Mode.RECORD);
                    final String columnType = getJavaType(identity.getColumn().getType());
                    final String identityId = getStrategy().getJavaIdentifier(identity.getColumn().getContainer());
                    final int block = allIdentities.size() / INITIALISER_SIZE;

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
                    final String keyType = getStrategy().getFullJavaClassName(uniqueKey.getTable(), Mode.RECORD);
                    final String keyId = getStrategy().getJavaIdentifier(uniqueKey);
                    final int block = allUniqueKeys.size() / INITIALISER_SIZE;

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
                    final String keyType = getStrategy().getFullJavaClassName(foreignKey.getKeyTable(), Mode.RECORD);
                    final String referencedType = getStrategy().getFullJavaClassName(foreignKey.getReferencedTable(), Mode.RECORD);
                    final String keyId = getStrategy().getJavaIdentifier(foreignKey);
                    final int block = allForeignKeys.size() / INITIALISER_SIZE;

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
            out.println("\t}");
        }

        // UniqueKeys
        // ----------

        for (UniqueKeyDefinition uniqueKey : allUniqueKeys) {
            printUniqueKey(out, uniqueKeyCounter, uniqueKey);
            uniqueKeyCounter++;
        }

        if (uniqueKeyCounter > 0) {
            out.println("\t}");
        }

        // ForeignKeys
        // -----------

        for (ForeignKeyDefinition foreignKey : allForeignKeys) {
            printForeignKey(out, foreignKeyCounter, foreignKey);
            foreignKeyCounter++;
        }

        if (foreignKeyCounter > 0) {
            out.println("\t}");
        }

        out.println("}");
        out.close();

        watch.splitInfo("Keys generated");
    }

    protected void printIdentity(JavaWriter out, int identityCounter, IdentityDefinition identity) {
        final int block = identityCounter / INITIALISER_SIZE;

        // Print new nested class
        if (identityCounter % INITIALISER_SIZE == 0) {
            if (identityCounter > 0) {
                out.tab(1).println("}");
            }

            out.println();
            out.tab(1).println("private static class Identities%s extends %s {", block, AbstractKeys.class);
        }

        out.tab(2).println("public static %s<%s, %s> %s = createIdentity(%s, %s);",
            Identity.class,
            getStrategy().getFullJavaClassName(identity.getTable(), Mode.RECORD),
            getJavaType(identity.getColumn().getType()),
            getStrategy().getJavaIdentifier(identity),
            getStrategy().getFullJavaIdentifier(identity.getColumn().getContainer()),
            getStrategy().getFullJavaIdentifier(identity.getColumn()));
    }

    protected void printUniqueKey(JavaWriter out, int uniqueKeyCounter, UniqueKeyDefinition uniqueKey) {
        final int block = uniqueKeyCounter / INITIALISER_SIZE;

        // Print new nested class
        if (uniqueKeyCounter % INITIALISER_SIZE == 0) {
            if (uniqueKeyCounter > 0) {
                out.tab(1).println("}");
            }

            out.println();
            out.tab(1).println("private static class UniqueKeys%s extends %s {", block, AbstractKeys.class);
        }

        out.tab(2).println("public static final %s<%s> %s = createUniqueKey(%s, [[%s]]);",
            UniqueKey.class,
            getStrategy().getFullJavaClassName(uniqueKey.getTable(), Mode.RECORD),
            getStrategy().getJavaIdentifier(uniqueKey),
            getStrategy().getFullJavaIdentifier(uniqueKey.getTable()),
            getStrategy().getFullJavaIdentifiers(uniqueKey.getKeyColumns()));
    }

    protected void printForeignKey(JavaWriter out, int foreignKeyCounter, ForeignKeyDefinition foreignKey) {
        final int block = foreignKeyCounter / INITIALISER_SIZE;

        // Print new nested class
        if (foreignKeyCounter % INITIALISER_SIZE == 0) {
            if (foreignKeyCounter > 0) {
                out.tab(1).println("}");
            }

            out.println();
            out.tab(1).println("private static class ForeignKeys%s extends %s {", block, AbstractKeys.class);
        }

        out.tab(2).println("public static final %s<%s, %s> %s = createForeignKey(%s, %s, [[%s]]);",
            ForeignKey.class,
            getStrategy().getFullJavaClassName(foreignKey.getKeyTable(), Mode.RECORD),
            getStrategy().getFullJavaClassName(foreignKey.getReferencedTable(), Mode.RECORD),
            getStrategy().getJavaIdentifier(foreignKey),
            getStrategy().getFullJavaIdentifier(foreignKey.getReferencedKey()),
            getStrategy().getFullJavaIdentifier(foreignKey.getKeyTable()),
            getStrategy().getFullJavaIdentifiers(foreignKey.getKeyColumns()));
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
        generateRecord((Definition) table);
    }

    private void generateRecord(Definition tableOrUdt) {
        log.info("Generating record", getStrategy().getFileName(tableOrUdt, Mode.RECORD));

        final UniqueKeyDefinition key = (tableOrUdt instanceof TableDefinition)
            ? ((TableDefinition) tableOrUdt).getPrimaryKey()
            : null;
        final String className = getStrategy().getJavaClassName(tableOrUdt, Mode.RECORD);
        final String tableIdentifier = getStrategy().getFullJavaIdentifier(tableOrUdt);
        final String recordType = getStrategy().getFullJavaClassName(tableOrUdt, Mode.RECORD);
        final List<String> interfaces = getStrategy().getJavaClassImplements(tableOrUdt, Mode.RECORD);
        final List<? extends TypedElementDefinition<?>> columns = getTypedElements(tableOrUdt);

        JavaWriter out = newJavaWriter(getStrategy().getFile(tableOrUdt, Mode.RECORD));
        printPackage(out, tableOrUdt, Mode.RECORD);

        if (tableOrUdt instanceof TableDefinition)
            generateRecordClassJavadoc((TableDefinition) tableOrUdt, out);
        else
            generateUDTRecordClassJavadoc((UDTDefinition) tableOrUdt, out);

        printClassAnnotations(out, tableOrUdt.getSchema());
        if (tableOrUdt instanceof TableDefinition)
            printTableJPAAnnotation(out, (TableDefinition) tableOrUdt);

        Class<?> baseClass;
        if (tableOrUdt instanceof UDTDefinition)
            baseClass = UDTRecordImpl.class;
        else if (generateRelations() && key != null)
            baseClass = UpdatableRecordImpl.class;
        else
            baseClass = TableRecordImpl.class;

        int degree = columns.size();
        String rowType = null;
        String rowTypeRecord = null;

        // [#3130] Invalid UDTs may have a degree of 0
        if (degree > 0 && degree <= Constants.MAX_ROW_DEGREE) {
            rowType = getRowType(columns);
            rowTypeRecord = Record.class.getName() + degree + "<" + rowType + ">";
            interfaces.add(rowTypeRecord);
        }

        if (generateInterfaces()) {
            interfaces.add(getStrategy().getFullJavaClassName(tableOrUdt, Mode.INTERFACE));
        }

        out.println("public class %s extends %s<%s>[[before= implements ][%s]] {", className, baseClass, recordType, interfaces);
        out.printSerial();

        for (int i = 0; i < degree; i++) {
            TypedElementDefinition<?> column = columns.get(i);

            final String comment = StringUtils.defaultString(column.getComment());
            final String setterReturnType = fluentSetters() ? className : "void";
            final String setter = getStrategy().getJavaSetterName(column, Mode.DEFAULT);
            final String getter = getStrategy().getJavaGetterName(column, Mode.DEFAULT);
            final String type = getJavaType(column.getType());
            final String typeInterface = getJavaType(column.getType(), Mode.INTERFACE);
            final String name = column.getQualifiedOutputName();
            final boolean isUDT = column.getType().isUDT();

            out.tab(1).javadoc("Setter for <code>%s</code>.%s", name, defaultIfBlank(" " + comment, ""));
            out.tab(1).overrideIf(generateInterfaces() && !generateImmutablePojos() && !isUDT);
            out.tab(1).println("public %s %s(%s value) {", setterReturnType, setter, type);
            out.tab(2).println("setValue(%s, value);", i);
            if (fluentSetters())
                out.tab(2).println("return this;");
            out.tab(1).println("}");

            // [#3117] Avoid covariant setters for UDTs when generating interfaces
            if (generateInterfaces() && !generateImmutablePojos() && isUDT) {
                out.tab(1).javadoc("Setter for <code>%s</code>.%s", name, defaultIfBlank(" " + comment, ""));
                out.tab(1).override();
                out.tab(1).println("public %s %s(%s value) {", setterReturnType, setter, typeInterface);
                out.tab(2).println("if (value == null)");
                out.tab(3).println("setValue(%s, null);", i);
                out.tab(2).println("else");
                out.tab(3).println("setValue(%s, value.into(new %s()));", i, type);
                if (fluentSetters())
                    out.tab(2).println("return this;");
                out.tab(1).println("}");
            }

            out.tab(1).javadoc("Getter for <code>%s</code>.%s", name, defaultIfBlank(" " + comment, ""));
            if (tableOrUdt instanceof TableDefinition)
                printColumnJPAAnnotation(out, (ColumnDefinition) column);
            printValidationAnnotation(out, column);
            out.tab(1).overrideIf(generateInterfaces());
            out.tab(1).println("public %s %s() {", type, getter);
            out.tab(2).println("return (%s) getValue(%s);", type, i);
            out.tab(1).println("}");
        }

        if (generateRelations() && key != null) {
            int keyDegree = key.getKeyColumns().size();

            if (keyDegree <= Constants.MAX_ROW_DEGREE) {
                String keyType = getRowType(key.getKeyColumns());
                out.tab(1).header("Primary key information");

                out.tab(1).overrideInherit();
                out.tab(1).println("public %s%s<%s> key() {", Record.class, keyDegree, keyType);
                out.tab(2).println("return (%s%s) super.key();", Record.class, keyDegree);
                out.tab(1).println("}");
            }
        }

        if (tableOrUdt instanceof UDTDefinition) {

            // [#799] Oracle UDT's can have member procedures
            for (RoutineDefinition routine : ((UDTDefinition) tableOrUdt).getRoutines()) {

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
        if (degree > 0 && degree <= Constants.MAX_ROW_DEGREE) {
            out.tab(1).header("Record%s type implementation", degree);

            // fieldsRow()
            out.tab(1).overrideInherit();
            out.tab(1).println("public %s%s<%s> fieldsRow() {", Row.class, degree, rowType);
            out.tab(2).println("return (%s%s) super.fieldsRow();", Row.class, degree);
            out.tab(1).println("}");

            // valuesRow()
            out.tab(1).overrideInherit();
            out.tab(1).println("public %s%s<%s> valuesRow() {", Row.class, degree, rowType);
            out.tab(2).println("return (%s%s) super.valuesRow();", Row.class, degree);
            out.tab(1).println("}");

            // field[N]()
            for (int i = 1; i <= degree; i++) {
                TypedElementDefinition<?> column = columns.get(i - 1);

                final String colType = getJavaType(column.getType());
                final String colIdentifier = getStrategy().getFullJavaIdentifier(column);

                out.tab(1).overrideInherit();
                out.tab(1).println("public %s<%s> field%s() {", Field.class, colType, i);
                out.tab(2).println("return %s;", colIdentifier);
                out.tab(1).println("}");
            }

            // value[N]()
            for (int i = 1; i <= degree; i++) {
                TypedElementDefinition<?> column = columns.get(i - 1);

                final String colType = getJavaType(column.getType());
                final String colGetter = getStrategy().getJavaGetterName(column, Mode.RECORD);

                out.tab(1).overrideInherit();
                out.tab(1).println("public %s value%s() {", colType, i);
                out.tab(2).println("return %s();", colGetter);
                out.tab(1).println("}");
            }

            // value[N](T[N])
            for (int i = 1; i <= degree; i++) {
                TypedElementDefinition<?> column = columns.get(i - 1);

                final String colType = getJavaType(column.getType());
                final String colSetter = getStrategy().getJavaSetterName(column, Mode.RECORD);

                out.tab(1).overrideInherit();
                out.tab(1).println("public %s value%s(%s value) {", className, i, colType);
                out.tab(2).println("%s(value);", colSetter);
                out.tab(2).println("return this;");
                out.tab(1).println("}");
            }

            List<String> arguments = new ArrayList<String>();
            for (int i = 1; i <= degree; i++) {
                TypedElementDefinition<?> column = columns.get(i - 1);

                final String colType = getJavaType(column.getType());

                arguments.add(colType + " value" + i);
            }

            out.tab(1).overrideInherit();
            out.tab(1).println("public %s values([[%s]]) {", className, arguments);
            out.tab(2).println("return this;");
            out.tab(1).println("}");
        }

        if (generateInterfaces() && !generateImmutablePojos()) {
            printFromAndInto(out, tableOrUdt);
        }

        out.tab(1).header("Constructors");
        out.tab(1).javadoc("Create a detached %s", className);
        out.tab(1).println("public %s() {", className);
        out.tab(2).println("super(%s);", tableIdentifier);
        out.tab(1).println("}");

        // [#3130] Invalid UDTs may have a degree of 0
        // [#3176] Avoid generating constructors for tables with more than 255 columns (Java's method argument limit)
        if (degree > 0 && degree < 256) {
            List<String> arguments = new ArrayList<String>();

            for (int i = 0; i < degree; i++) {
                final TypedElementDefinition<?> column = columns.get(i);
                final String columnMember = getStrategy().getJavaMemberName(column, Mode.DEFAULT);
                final String type = getJavaType(column.getType());

                arguments.add(type + " " + columnMember);
            }

            out.tab(1).javadoc("Create a detached, initialised %s", className);
            out.tab(1).println("public %s([[%s]]) {", className, arguments);
            out.tab(2).println("super(%s);", tableIdentifier);
            out.println();

            for (int i = 0; i < degree; i++) {
                final TypedElementDefinition<?> column = columns.get(i);
                final String columnMember = getStrategy().getJavaMemberName(column, Mode.DEFAULT);

                out.tab(2).println("setValue(%s, %s);", i, columnMember);
            }

            out.tab(1).println("}");
        }

        if (tableOrUdt instanceof TableDefinition)
            generateRecordClassFooter((TableDefinition) tableOrUdt, out);
        else
            generateUDTRecordClassFooter((UDTDefinition) tableOrUdt, out);

        out.println("}");
        out.close();
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
        printClassJavadoc(out, table);
    }

    private final String getRowType(Collection<? extends TypedElementDefinition<?>> columns) {
        StringBuilder result = new StringBuilder();
        String separator = "";

        for (TypedElementDefinition<?> column : columns) {
            result.append(separator);
            result.append(getJavaType(column.getType()));

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
        generateInterface((Definition) table);
    }

    private void generateInterface(Definition tableOrUDT) {
        log.info("Generating interface", getStrategy().getFileName(tableOrUDT, Mode.INTERFACE));

        final String className = getStrategy().getJavaClassName(tableOrUDT, Mode.INTERFACE);
        final List<String> interfaces = getStrategy().getJavaClassImplements(tableOrUDT, Mode.INTERFACE);

        JavaWriter out = newJavaWriter(getStrategy().getFile(tableOrUDT, Mode.INTERFACE));
        printPackage(out, tableOrUDT, Mode.INTERFACE);
        if (tableOrUDT instanceof TableDefinition)
            generateInterfaceClassJavadoc((TableDefinition) tableOrUDT, out);
        else
            generateUDTInterfaceClassJavadoc((UDTDefinition) tableOrUDT, out);

        printClassAnnotations(out, tableOrUDT.getSchema());

        if (tableOrUDT instanceof TableDefinition)
            printTableJPAAnnotation(out, (TableDefinition) tableOrUDT);

        out.println("public interface %s [[before=extends ][%s]] {", className, interfaces);

        for (TypedElementDefinition<?> column : getTypedElements(tableOrUDT)) {
            final String comment = StringUtils.defaultString(column.getComment());
            final String setterReturnType = fluentSetters() ? className : "void";
            final String setter = getStrategy().getJavaSetterName(column, Mode.DEFAULT);
            final String getter = getStrategy().getJavaGetterName(column, Mode.DEFAULT);
            final String type = getJavaType((column).getType(), Mode.INTERFACE);
            final String name = column.getQualifiedOutputName();

            if (!generateImmutablePojos()) {
                out.tab(1).javadoc("Setter for <code>%s</code>.%s", name, defaultIfBlank(" " + comment, ""));
                out.tab(1).println("public %s %s(%s value);", setterReturnType, setter, type);
            }

            out.tab(1).javadoc("Getter for <code>%s</code>.%s", name, defaultIfBlank(" " + comment, ""));

            if (column instanceof ColumnDefinition)
                printColumnJPAAnnotation(out, (ColumnDefinition) column);

            printValidationAnnotation(out, column);
            out.tab(1).println("public %s %s();", type, getter);
        }

        if (!generateImmutablePojos()) {
            String local = getStrategy().getJavaClassName(tableOrUDT, Mode.INTERFACE);
            String qualified = getStrategy().getFullJavaClassName(tableOrUDT, Mode.INTERFACE);

            out.tab(1).header("FROM and INTO");

            out.tab(1).javadoc("Load data from another generated Record/POJO implementing the common interface %s", local);
            out.tab(1).println("public void from(%s from);", qualified);

            out.tab(1).javadoc("Copy data into another generated Record/POJO implementing the common interface %s", local);
            out.tab(1).println("public <E extends %s> E into(E into);", qualified);
        }


        if (tableOrUDT instanceof TableDefinition)
            generateInterfaceClassFooter((TableDefinition) tableOrUDT, out);
        else
            generateUDTInterfaceClassFooter((UDTDefinition) tableOrUDT, out);

        out.println("}");
        out.close();
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
        printClassJavadoc(out, table);
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

    protected void generateUDT(SchemaDefinition schema, UDTDefinition udt) {
        log.info("Generating UDT ", getStrategy().getFileName(udt));

        final String className = getStrategy().getJavaClassName(udt);
        final String recordType = getStrategy().getFullJavaClassName(udt, Mode.RECORD);
        final List<String> interfaces = getStrategy().getJavaClassImplements(udt, Mode.DEFAULT);
        final String schemaId = getStrategy().getFullJavaIdentifier(schema);
        final String udtId = getStrategy().getJavaIdentifier(udt);

        JavaWriter out = newJavaWriter(getStrategy().getFile(udt));
        printPackage(out, udt);
        generateUDTClassJavadoc(udt, out);
        printClassAnnotations(out, schema);

        // [#799] Oracle UDTs with member procedures have similarities with packages
        if (udt.getRoutines().size() > 0) {
            interfaces.add(org.jooq.Package.class.getName());
        }

        out.println("public class %s extends %s<%s>[[before= implements ][%s]] {", className, UDTImpl.class, recordType, interfaces);
        out.printSerial();

        printSingletonInstance(out, udt);
        printRecordTypeMethod(out, udt);

        for (AttributeDefinition attribute : udt.getAttributes()) {
            final String attrType = getJavaType(attribute.getType());
            final String attrTypeRef = getJavaTypeReference(attribute.getDatabase(), attribute.getType());
            final String attrId = getStrategy().getJavaIdentifier(attribute);
            final String attrName = attribute.getName();
            final String attrComment = StringUtils.defaultString(attribute.getComment());
            final List<String> converters = list(
                attribute.getType().getConverter(),
                attribute.getType().getBinding()
            );

            out.tab(1).javadoc("The attribute <code>%s</code>.%s", attribute.getQualifiedOutputName(), defaultIfBlank(" " + attrComment, ""));
            out.tab(1).println("public static final %s<%s, %s> %s = createField(\"%s\", %s, %s, \"%s\"[[before=, ][new %s()]]);",
                UDTField.class, recordType, attrType, attrId, attrName, attrTypeRef, udtId, escapeString(""), converters);
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

        out.tab(1).javadoc(NO_FURTHER_INSTANCES_ALLOWED);
        out.tab(1).println("private %s() {", className);
        out.tab(2).println("super(\"%s\", %s);", udt.getOutputName(), schemaId);

        out.println();
        out.tab(2).println("// Initialise data type");
        out.tab(2).println("getDataType();");
        out.tab(1).println("}");

        generateUDTClassFooter(udt, out);
        out.println("}");
        out.close();
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
        printClassJavadoc(out, udt);
    }

    protected void generateUDTPojos(SchemaDefinition schema) {
        log.info("Generating UDT POJOs");

        for (UDTDefinition udt : database.getUDTs(schema)) {
            try {
                generatePojo(udt);
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
        printClassJavadoc(out, udt);
    }

    protected void generateUDTInterfaces(SchemaDefinition schema) {
        log.info("Generating UDT interfaces");

        for (UDTDefinition udt : database.getUDTs(schema)) {
            try {
                generateInterface(udt);
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
        printClassJavadoc(out, udt);
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

    protected void generateUDTRecord(UDTDefinition udt) {
        generateRecord(udt);
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
        printClassJavadoc(out, udt);
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

        JavaWriter out = newJavaWriter(new File(getStrategy().getFile(schema).getParentFile(), "UDTs.java"));
        printPackage(out, schema);
        printClassJavadoc(out, "Convenience access to all UDTs in " + schema.getOutputName());
        printClassAnnotations(out, schema);
        out.println("public class UDTs {");

        for (UDTDefinition udt : database.getUDTs(schema)) {
            final String className = getStrategy().getFullJavaClassName(udt);
            final String id = getStrategy().getJavaIdentifier(udt);
            final String fullId = getStrategy().getFullJavaIdentifier(udt);

            out.tab(1).javadoc("The type <code>%s</code>", udt.getQualifiedOutputName());
            out.tab(1).println("public static %s %s = %s;", className, id, fullId);
        }

        out.println("}");
        out.close();

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

    protected void generateArray(SchemaDefinition schema, ArrayDefinition array) {
        log.info("Generating ARRAY", getStrategy().getFileName(array, Mode.RECORD));

        /* [pro] xx
        xxxxx xxxxxx xxxxxxxxx x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxx
        xxxxx xxxxxx xxxxxxxxxxx x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        xxxxx xxxxxx xxxxxxxxxxxxxx x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxx
        xxxxx xxxxxxxxxxxx xxxxxxxxxx x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxx
        xxxxx xxxxxx xxxxxxxxx x xxxxxxxxxxxxxxxxxxxxxx
        xxxxx xxxxxx xxxxxxxx x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        xxxxx xxxxxxxxxxxx xxxxxxxxxx x xxxxx
            xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
            xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        xx

        xxxxxxxxxx xxx x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxx xxxxxx xxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxx
        xxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxx

        xxxxxxxxxxxxxxxxxxx xxxxx xx xxxxxxx xxxxxxxxxxxxxxx xxxxxxxxxx xxxxxx xxx xxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxx xxxxxxxxxxxx
        xxxxxxxxxxxxxxxxxx

        xx xxxxxxxxxxxxxxxxxxxxxx x
            xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx x xxxxx x xxxxxxx x xxx xxx xxxxxx xxxxxx xxxxxxxxxxx xxxxxxxxx xxxxxxxxxxx
            xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
            xxxxxxxxxxxxxxxxxxxxxxxxxx xxxxx xxxxxxxxxxxxxx xxx xxxxxxxxxx xxxxxxxxxxxxxxxxxxxxx

            xxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxx xxx xxxxxxxxxxxxxxxxxxxxxxx xxxxx xxxxxxxxxx xxxxxxxxx xxxxxxxxxx xxxxxxxxxxxxxxx xxxxxxxxxxxx
            xxxxxxxxxxxxxxxxxxxxxxxx

            xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx x xxxxx x xxxxxxx x xxx xxx xxxxxx xxxxxx xxxxxxxxxxx xxxxxxxxx xxxxxxxxxxx
            xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
            xxxxxxxxxxxxxxxxxxxxxxxxxx xxxxx xxxxxxxxxxxxxx xxxxx xxxxxx xxx xxxxxxxxxx xxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxx
            xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
            xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
            xxxxxxxxxxxxxxxxxxxxxxxx

            xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx x xxxxx x xxxxxxx x xxx xxx xxxxxx xxxxxx xxxxxxxxxxx xxxxxxxxx xxxxxxxxxxx
            xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
            xxxxxxxxxxxxxxxxxxxxxxxxxx xxxxx xxxxxxxxxxxxxx xxxx xxxxxxx xxx xxxxx xxx xxxxxxxxxx xxxxxxxxxxxxxxxxxxxx xxxxxxxxxxx xxxxxxxxxxxxx
            xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
            xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
            xxxxxxxxxxxxxxxxxxxxxxxx
        x

        xxxxxxxxxxxxxxxxxxxxxxxxxx x xxx xxxxxxxxxxxxxxx xxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxxxxxx xxxx xxx xxxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxx xxxxxxxxxxxx xxxxx xxxxxxxxxx xxxxxxxxx xxxxxxxxxx xxxxxxxxxxxxxxx xxxxxxxxxxxx

        xxxxxxxxxxxxxxxxxxxxxxxx

        xxxxxxxxxxxxxxxxxxxxxxxxxx x xxx xxxxxxxxxxxxxxx xxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxx xxxxxx xxx xxxxxxxxxx xxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxxxx

        xxxxxxxxxxxxxxxxxxxxxxxxxx x xxx xxxxxxxxxxxxxxx xxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxx xxxxxxx xxx xxxxxxxxxxx xxx xxxxxxxxxx xxxxxxxxxxxxxxxxx xxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxxxx

        xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxx
        xxxxxxxxxxxxxxxxx
        xxxxxxxxxxxx
        xx [/pro] */
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
        printClassJavadoc(out, array);
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

    protected void generateEnum(EnumDefinition e) {
        log.info("Generating ENUM", getStrategy().getFileName(e, Mode.ENUM));

        final String className = getStrategy().getJavaClassName(e, Mode.ENUM);
        final List<String> interfaces = getStrategy().getJavaClassImplements(e, Mode.ENUM);

        JavaWriter out = newJavaWriter(getStrategy().getFile(e, Mode.ENUM));
        printPackage(out, e);
        generateEnumClassJavadoc(e, out);
        printClassAnnotations(out, e.getSchema());

        interfaces.add(EnumType.class.getName());

        out.println("public enum %s[[before= implements ][%s]] {", className, interfaces);

        List<String> literals = e.getLiterals();
        for (int i = 0; i < literals.size(); i++) {
            String literal = literals.get(i);
            String terminator = (i == literals.size() - 1) ? ";" : ",";

            String identifier = GenerationUtil.convertToJavaIdentifier(literal);

            // [#2781] Disambiguate collisions with the leading package name
            if (identifier.equals(getStrategy().getJavaPackageName(e).replaceAll("\\..*", ""))) {
                identifier += "_";
            }

            out.println();
            out.tab(1).println("%s(\"%s\")%s", identifier, literal, terminator);
        }

        out.println();
        out.tab(1).println("private final java.lang.String literal;");
        out.println();
        out.tab(1).println("private %s(java.lang.String literal) {", className);
        out.tab(2).println("this.literal = literal;");
        out.tab(1).println("}");

        // [#2135] Only the PostgreSQL database supports schema-scoped enum types
        out.tab(1).overrideInherit();
        out.tab(1).println("public %s getSchema() {", Schema.class);
        out.tab(2).println("return %s;",
            (e.isSynthetic() || !(e.getDatabase() instanceof PostgresDatabase))
                ? "null"
                : getStrategy().getFullJavaIdentifier(e.getSchema()));
        out.tab(1).println("}");

        out.tab(1).overrideInherit();
        out.tab(1).println("public java.lang.String getName() {");
        out.tab(2).println("return %s;", e.isSynthetic() ? "null" : "\"" + e.getName().replace("\"", "\\\"") + "\"");
        out.tab(1).println("}");

        out.tab(1).overrideInherit();
        out.tab(1).println("public java.lang.String getLiteral() {");
        out.tab(2).println("return literal;");
        out.tab(1).println("}");

        generateEnumClassFooter(e, out);
        out.println("}");
        out.close();
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
        printClassJavadoc(out, e);
    }

    protected void generateRoutines(SchemaDefinition schema) {
        log.info("Generating routines and table-valued functions");

        JavaWriter out = newJavaWriter(new File(getStrategy().getFile(schema).getParentFile(), "Routines.java"));
        printPackage(out, schema);
        printClassJavadoc(out, "Convenience access to all stored procedures and functions in " + schema.getOutputName());
        printClassAnnotations(out, schema);

        out.println("public class Routines {");
        for (RoutineDefinition routine : database.getRoutines(schema)) {
            printRoutine(out, routine);

            try {
                generateRoutine(schema, routine);
            } catch (Exception e) {
                log.error("Error while generating routine " + routine, e);
            }
        }

        for (TableDefinition table : database.getTables(schema)) {
            if (table.isTableValuedFunction()) {
                printTableValuedFunction(out, table);
            }
        }

        out.println("}");
        out.close();

        watch.splitInfo("Routines generated");
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

    protected void printTableValuedFunction(JavaWriter out, TableDefinition table) {
        printConvenienceMethodTableValuedFunctionAsField(out, table, false);
        printConvenienceMethodTableValuedFunctionAsField(out, table, true);
    }

    protected void generatePackages(SchemaDefinition schema) {
        log.info("Generating packages");

        for (PackageDefinition pkg : database.getPackages(schema)) {
            try {
                generatePackage(schema, pkg);
            } catch (Exception e) {
                log.error("Error while generating package " + pkg, e);
            }
        }

        watch.splitInfo("Packages generated");
    }

    protected void generatePackage(SchemaDefinition schema, PackageDefinition pkg) {
        log.info("Generating package", pkg);

        final String className = getStrategy().getJavaClassName(pkg);
        final String schemaIdentifier = getStrategy().getFullJavaIdentifier(schema);
        final List<String> interfaces = getStrategy().getJavaClassImplements(pkg, Mode.DEFAULT);

        // Static convenience methods
        JavaWriter out = newJavaWriter(getStrategy().getFile(pkg));
        printPackage(out, pkg);
        generatePackageClassJavadoc(pkg, out);
        printClassAnnotations(out, schema);

        out.println("public class %s extends %s[[before= implements ][%s]] {", className, PackageImpl.class, interfaces);
        out.printSerial();
        printSingletonInstance(out, pkg);

        for (RoutineDefinition routine : pkg.getRoutines()) {
            printRoutine(out, routine);

            try {
                generateRoutine(schema, routine);
            } catch (Exception e) {
                log.error("Error while generating routine " + routine, e);
            }
        }

        out.tab(1).javadoc(NO_FURTHER_INSTANCES_ALLOWED);
        out.tab(1).println("private %s() {", className);
        out.tab(2).println("super(\"%s\", %s);", pkg.getOutputName(), schemaIdentifier);
        out.tab(1).println("}");

        generatePackageClassFooter(pkg, out);
        out.println("}");
        out.close();
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

        JavaWriter out = newJavaWriter(new File(getStrategy().getFile(schema).getParentFile(), "Tables.java"));
        printPackage(out, schema);
        printClassJavadoc(out, "Convenience access to all tables in " + schema.getOutputName());
        printClassAnnotations(out, schema);
        out.println("public class Tables {");

        for (TableDefinition table : database.getTables(schema)) {
            final String className = getStrategy().getFullJavaClassName(table);
            final String id = getStrategy().getJavaIdentifier(table);
            final String fullId = getStrategy().getFullJavaIdentifier(table);
            final String comment = !StringUtils.isBlank(table.getComment())
                ? table.getComment()
                : "The table " + table.getQualifiedOutputName();

            out.tab(1).javadoc(comment);
            out.tab(1).println("public static final %s %s = %s;", className, id, fullId);
        }

        out.println("}");
        out.close();

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
        final String className = getStrategy().getJavaClassName(table, Mode.DAO);
        final String tableRecord = getStrategy().getFullJavaClassName(table, Mode.RECORD);
        final String daoImpl = DAOImpl.class.getName();
        final String tableIdentifier = getStrategy().getFullJavaIdentifier(table);

        String tType = "Void";
        String pType = getStrategy().getFullJavaClassName(table, Mode.POJO);

        UniqueKeyDefinition key = table.getPrimaryKey();
        if (key == null) {
            log.info("Skipping DAO generation", getStrategy().getFileName(table, Mode.DAO));
            return;
        }

        List<ColumnDefinition> keyColumns = key.getKeyColumns();

        if (keyColumns.size() == 1) {
            tType = getJavaType(keyColumns.get(0).getType());
        }
        else if (keyColumns.size() <= Constants.MAX_ROW_DEGREE) {
            String generics = "";
            String separator = "";

            for (ColumnDefinition column : keyColumns) {
                generics += separator + getJavaType(column.getType());
                separator = ", ";
            }

            tType = Record.class.getName() + keyColumns.size() + "<" + generics + ">";
        }
        else {
            tType = Record.class.getName();
        }

        log.info("Generating DAO", getStrategy().getFileName(table, Mode.DAO));

        JavaWriter out = newJavaWriter(getStrategy().getFile(table, Mode.DAO));
        printPackage(out, table, Mode.DAO);
        generateDaoClassJavadoc(table, out);
        printClassAnnotations(out, table.getSchema());

        out.println("public class %s extends %s<%s, %s, %s> {", className, daoImpl, tableRecord, pType, tType);

        // Default constructor
        // -------------------
        out.tab(1).javadoc("Create a new %s without any configuration", className);
        out.tab(1).println("public %s() {", className);
        out.tab(2).println("super(%s, %s.class);", tableIdentifier, pType);
        out.tab(1).println("}");

        // Initialising constructor
        // ------------------------
        out.tab(1).javadoc("Create a new %s with an attached configuration", className);
        out.tab(1).println("public %s(%s configuration) {", className, Configuration.class);
        out.tab(2).println("super(%s, %s.class, configuration);", tableIdentifier, pType);
        out.tab(1).println("}");

        // Template method implementations
        // -------------------------------
        out.tab(1).overrideInherit();
        out.tab(1).println("protected %s getId(%s object) {", tType, pType);

        if (keyColumns.size() == 1) {
            out.tab(2).println("return object.%s();", getStrategy().getJavaGetterName(keyColumns.get(0), Mode.POJO));
        }

        // [#2574] This should be replaced by a call to a method on the target table's Key type
        else {
            String params = "";
            String separator = "";

            for (ColumnDefinition column : keyColumns) {
                params += separator + "object." + getStrategy().getJavaGetterName(column, Mode.POJO) + "()";
                separator = ", ";
            }

            out.tab(2).println("return compositeKeyRecord(%s);", params);
        }

        out.tab(1).println("}");

        for (ColumnDefinition column : table.getColumns()) {
            final String colName = column.getOutputName();
            final String colClass = getStrategy().getJavaClassName(column, Mode.POJO);
            final String colType = getJavaType(column.getType());
            final String colIdentifier = getStrategy().getFullJavaIdentifier(column);

            // fetchBy[Column]([T]...)
            // -----------------------
            out.tab(1).javadoc("Fetch records that have <code>%s IN (values)</code>", colName);
            out.tab(1).println("public %s<%s> fetchBy%s(%s... values) {", List.class, pType, colClass, colType);
            out.tab(2).println("return fetch(%s, values);", colIdentifier);
            out.tab(1).println("}");

            // fetchOneBy[Column]([T])
            // -----------------------
            ukLoop:
            for (UniqueKeyDefinition uk : column.getUniqueKeys()) {

                // If column is part of a single-column unique key...
                if (uk.getKeyColumns().size() == 1 && uk.getKeyColumns().get(0).equals(column)) {
                    out.tab(1).javadoc("Fetch a unique record that has <code>%s = value</code>", colName);
                    out.tab(1).println("public %s fetchOneBy%s(%s value) {", pType, colClass, colType);
                    out.tab(2).println("return fetchOne(%s, value);", colIdentifier);
                    out.tab(1).println("}");

                    break ukLoop;
                }
            }
        }

        generateDaoClassFooter(table, out);
        out.println("}");
        out.close();
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
        printClassJavadoc(out, table);
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
        generatePojo((Definition) table);
    }

    private void generatePojo(Definition tableOrUDT) {
        log.info("Generating POJO", getStrategy().getFileName(tableOrUDT, Mode.POJO));

        final String className = getStrategy().getJavaClassName(tableOrUDT, Mode.POJO);
        final String superName = getStrategy().getJavaClassExtends(tableOrUDT, Mode.POJO);
        final List<String> interfaces = getStrategy().getJavaClassImplements(tableOrUDT, Mode.POJO);

        if (generateInterfaces()) {
            interfaces.add(getStrategy().getFullJavaClassName(tableOrUDT, Mode.INTERFACE));
        }

        JavaWriter out = newJavaWriter(getStrategy().getFile(tableOrUDT, Mode.POJO));
        printPackage(out, tableOrUDT, Mode.POJO);

        if (tableOrUDT instanceof TableDefinition)
            generatePojoClassJavadoc((TableDefinition) tableOrUDT, out);
        else
            generateUDTPojoClassJavadoc((UDTDefinition) tableOrUDT, out);

        printClassAnnotations(out, tableOrUDT.getSchema());

        if (tableOrUDT instanceof TableDefinition)
            printTableJPAAnnotation(out, (TableDefinition) tableOrUDT);

        out.println("public class %s[[before= extends ][%s]][[before= implements ][%s]] {", className, list(superName), interfaces);
        out.printSerial();

        out.println();

        int maxLength = 0;
        for (TypedElementDefinition<?> column : getTypedElements(tableOrUDT)) {
            maxLength = Math.max(maxLength, getJavaType(column.getType(), Mode.POJO).length());
        }

        for (TypedElementDefinition<?> column : getTypedElements(tableOrUDT)) {
            out.tab(1).println("private %s%s %s;",
                generateImmutablePojos() ? "final " : "",
                StringUtils.rightPad(getJavaType(column.getType(), Mode.POJO), maxLength),
                getStrategy().getJavaMemberName(column, Mode.POJO));
        }

        // Constructors
        // ---------------------------------------------------------------------

        // Default constructor
        if (!generateImmutablePojos()) {
            out.println();
            out.tab(1).print("public %s() {}", className);
            out.println();
        }

        // Multi-constructor

        // [#3010] Invalid UDTs may have no attributes. Avoid generating this constructor in that case
        // [#3176] Avoid generating constructors for tables with more than 255 columns (Java's method argument limit)
        if (getTypedElements(tableOrUDT).size() > 0 &&
            getTypedElements(tableOrUDT).size() < 256) {
            out.println();
            out.tab(1).print("public %s(", className);

            String separator1 = "";
            for (TypedElementDefinition<?> column : getTypedElements(tableOrUDT)) {
                out.println(separator1);

                out.tab(2).print("%s %s",
                    StringUtils.rightPad(getJavaType(column.getType(), Mode.POJO), maxLength),
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

        for (TypedElementDefinition<?> column : getTypedElements(tableOrUDT)) {
            final String columnType = getJavaType(column.getType(), Mode.POJO);
            final String columnTypeInterface = getJavaType(column.getType(), Mode.INTERFACE);
            final String columnSetterReturnType = fluentSetters() ? className : "void";
            final String columnSetter = getStrategy().getJavaSetterName(column, Mode.POJO);
            final String columnGetter = getStrategy().getJavaGetterName(column, Mode.POJO);
            final String columnMember = getStrategy().getJavaMemberName(column, Mode.POJO);
            final boolean isUDT = column.getType().isUDT();

            // Getter
            out.println();

            if (column instanceof ColumnDefinition)
                printColumnJPAAnnotation(out, (ColumnDefinition) column);

            printValidationAnnotation(out, column);
            out.tab(1).overrideIf(generateInterfaces());
            out.tab(1).println("public %s %s() {", columnType, columnGetter);
            out.tab(2).println("return this.%s;", columnMember);
            out.tab(1).println("}");

            // Setter
            if (!generateImmutablePojos()) {
                out.println();
                out.tab(1).overrideIf(generateInterfaces() && !isUDT);
                out.tab(1).println("public %s %s(%s %s) {", columnSetterReturnType, columnSetter, columnType, columnMember);
                out.tab(2).println("this.%s = %s;", columnMember, columnMember);
                if (fluentSetters())
                    out.tab(2).println("return this;");
                out.tab(1).println("}");

                // [#3117] To avoid covariant setters on POJOs, we need to generate two setter overloads
                if (generateInterfaces() && isUDT) {
                    out.println();
                    out.tab(1).override();
                    out.tab(1).println("public %s %s(%s %s) {", columnSetterReturnType, columnSetter, columnTypeInterface, columnMember);
                    out.tab(2).println("if (%s == null)", columnMember);
                    out.tab(3).println("this.%s = null;", columnMember);
                    out.tab(2).println("else");
                    out.tab(3).println("this.%s = %s.into(new %s());", columnMember, columnMember, columnType);
                    if (fluentSetters())
                        out.tab(2).println("return this;");
                    out.tab(1).println("}");
                }
            }
        }

        if (generatePojosEqualsAndHashCode()) {
            generatePojoEqualsAndHashCode(tableOrUDT, out);
        }


        if (generateInterfaces() && !generateImmutablePojos()) {
            printFromAndInto(out, tableOrUDT);
        }

        if (tableOrUDT instanceof TableDefinition)
            generatePojoClassFooter((TableDefinition) tableOrUDT, out);
        else
            generateUDTPojoClassFooter((UDTDefinition) tableOrUDT, out);

        out.println("}");
        out.close();
    }

    protected void generatePojoEqualsAndHashCode(Definition tableOrUDT, JavaWriter out) {
        final String className = getStrategy().getJavaClassName(tableOrUDT, Mode.POJO);

        out.println();
        out.tab(1).println("@Override");
        out.tab(1).println("public boolean equals(Object obj) {");
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

            if (getJavaType(column.getType()).endsWith("[]")) {
                out.tab(2).println("else if (!java.util.Arrays.equals(%s, other.%s))", columnMember, columnMember);
            }
            else {
                out.tab(2).println("else if (!%s.equals(other.%s))", columnMember, columnMember);
            }

            out.tab(3).println("return false;");
        }

        out.tab(2).println("return true;");
        out.tab(1).println("}");

        out.println();
        out.tab(1).println("@Override");
        out.tab(1).println("public int hashCode() {");
        out.tab(2).println("final int prime = 31;");
        out.tab(2).println("int result = 1;");

        for (TypedElementDefinition<?> column : getTypedElements(tableOrUDT)) {
            final String columnMember = getStrategy().getJavaMemberName(column, Mode.POJO);

            if (getJavaType(column.getType()).endsWith("[]")) {
                out.tab(2).println("result = prime * result + ((%s == null) ? 0 : java.util.Arrays.hashCode(%s));", columnMember, columnMember);
            }
            else {
                out.tab(2).println("result = prime * result + ((%s == null) ? 0 : %s.hashCode());", columnMember, columnMember);
            }
        }

        out.tab(2).println("return result;");
        out.tab(1).println("}");
    }

    private List<? extends TypedElementDefinition<? extends Definition>> getTypedElements(Definition definition) {
        if (definition instanceof TableDefinition) {
            return ((TableDefinition) definition).getColumns();
        }
        else if (definition instanceof UDTDefinition) {
            return ((UDTDefinition) definition).getAttributes();
        }
        else if (definition instanceof RoutineDefinition) {
            return ((RoutineDefinition) definition).getAllParameters();
        }
        else {
            throw new IllegalArgumentException("Unsupported type : " + definition);
        }
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
        printClassJavadoc(out, table);
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

    protected void generateTable(SchemaDefinition schema, TableDefinition table) {
        UniqueKeyDefinition primaryKey = table.getPrimaryKey();

        final boolean updatable = generateRelations() && primaryKey != null;
        final String className = getStrategy().getJavaClassName(table);
        final String fullClassName = getStrategy().getFullJavaClassName(table);
        final String fullTableId = getStrategy().getFullJavaIdentifier(table);
        final String recordType = getStrategy().getFullJavaClassName(table, Mode.RECORD);
        final List<String> interfaces = getStrategy().getJavaClassImplements(table, Mode.DEFAULT);
        final String comment = defaultString(table.getComment());

        log.info("Generating table", getStrategy().getFileName(table) +
            " [input=" + table.getInputName() +
            ", output=" + table.getOutputName() +
            ", pk=" + (primaryKey != null ? primaryKey.getName() : "N/A") +
            "]");

        JavaWriter out = newJavaWriter(getStrategy().getFile(table));
        printPackage(out, table);
        generateTableClassJavadoc(table, out);
        printClassAnnotations(out, schema);

        out.println("public class %s extends %s<%s>[[before= implements ][%s]] {", className, TableImpl.class, recordType, interfaces);
        out.printSerial();
        printSingletonInstance(out, table);
        printRecordTypeMethod(out, table);

        for (ColumnDefinition column : table.getColumns()) {
            final String columnType = getJavaType(column.getType());
            final String columnTypeRef = getJavaTypeReference(column.getDatabase(), column.getType());
            final String columnId = getStrategy().getJavaIdentifier(column);
            final String columnName = column.getName();
            final String columnComment = StringUtils.defaultString(column.getComment());
            final List<String> converters = list(
                column.getType().getConverter(),
                column.getType().getBinding()
            );

            String isStatic = generateInstanceFields() ? "" : "static ";
            String tableRef = generateInstanceFields() ? "this" : getStrategy().getJavaIdentifier(table);

            out.tab(1).javadoc("The column <code>%s</code>.%s", column.getQualifiedOutputName(), defaultIfBlank(" " + columnComment, ""));
            out.tab(1).println("public %sfinal %s<%s, %s> %s = createField(\"%s\", %s, %s, \"%s\"[[before=, ][new %s()]]);",
                isStatic, TableField.class, recordType, columnType, columnId, columnName, columnTypeRef, tableRef, escapeString(columnComment), converters);
        }

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

        out.tab(2).println("this(\"%s\", null);", table.getOutputName());
        out.tab(1).println("}");

        // [#117] With instance fields, it makes sense to create a
        // type-safe table alias
        // [#1255] With instance fields, the table constructor may
        // be public, as tables are no longer singletons
        final String schemaId = getStrategy().getFullJavaIdentifier(schema);

        if (generateInstanceFields()) {
            out.tab(1).javadoc("Create an aliased <code>%s</code> table reference", table.getQualifiedOutputName());
            out.tab(1).println("public %s(%s alias) {", className, String.class);
            out.tab(2).println("this(alias, %s);", fullTableId);
            out.tab(1).println("}");
        }

        out.println();
        out.tab(1).println("private %s(%s alias, %s<%s> aliased) {", className, String.class, Table.class, recordType);
        out.tab(2).println("this(alias, aliased, null);");
        out.tab(1).println("}");

        out.println();
        out.tab(1).println("private %s(%s alias, %s<%s> aliased, %s<?>[] parameters) {", className, String.class, Table.class, recordType, Field.class);
        out.tab(2).println("super(alias, %s, aliased, parameters, \"%s\");", schemaId, escapeString(comment));
        out.tab(1).println("}");

        // Add primary / unique / foreign key information
        if (generateRelations()) {
            IdentityDefinition identity = table.getIdentity();

            // The identity column
            if (identity != null) {
                final String identityType = getJavaType(identity.getColumn().getType());
                final String identityFullId = getStrategy().getFullJavaIdentifier(identity);

                out.tab(1).overrideInherit();
                out.tab(1).println("public %s<%s, %s> getIdentity() {", Identity.class, recordType, identityType);
                out.tab(2).println("return %s;", identityFullId);
                out.tab(1).println("}");
            }

            // The primary / main unique key
            if (primaryKey != null) {
                final String keyFullId = getStrategy().getFullJavaIdentifier(primaryKey);

                out.tab(1).overrideInherit();
                out.tab(1).println("public %s<%s> getPrimaryKey() {", UniqueKey.class, recordType);
                out.tab(2).println("return %s;", keyFullId);
                out.tab(1).println("}");
            }

            // The remaining unique keys
            List<UniqueKeyDefinition> uniqueKeys = table.getUniqueKeys();
            if (uniqueKeys.size() > 0) {
                final List<String> keyFullIds = getStrategy().getFullJavaIdentifiers(uniqueKeys);

                out.tab(1).overrideInherit();
                out.tab(1).println("public %s<%s<%s>> getKeys() {", List.class, UniqueKey.class, recordType);
                out.tab(2).println("return %s.<%s<%s>>asList([[%s]]);", Arrays.class, UniqueKey.class, recordType, keyFullIds);
                out.tab(1).println("}");
            }

            // Foreign keys
            List<ForeignKeyDefinition> foreignKeys = table.getForeignKeys();
            if (foreignKeys.size() > 0) {
                final List<String> keyFullIds = getStrategy().getFullJavaIdentifiers(foreignKeys);

                out.tab(1).overrideInherit();
                out.tab(1).println("public %s<%s<%s, ?>> getReferences() {", List.class, ForeignKey.class, recordType);
                out.tab(2).println("return %s.<%s<%s, ?>>asList([[%s]]);", Arrays.class, ForeignKey.class, recordType, keyFullIds);
                out.tab(1).println("}");
            }
        }

        // [#1596] Updatable tables can provide fields for optimistic locking
        // if properly configured
        if (updatable) {
            patternLoop: for (String pattern : database.getRecordVersionFields()) {
                Pattern p = Pattern.compile(pattern, Pattern.COMMENTS);

                for (ColumnDefinition column : table.getColumns()) {
                    if ((p.matcher(column.getName()).matches() ||
                         p.matcher(column.getQualifiedName()).matches())) {

                        final String columnType = getJavaType(column.getType());
                        final String columnId = getStrategy().getFullJavaIdentifier(column);

                        out.tab(1).overrideInherit();
                        out.tab(1).println("public %s<%s, %s> getRecordVersion() {", TableField.class, recordType, columnType);
                        out.tab(2).println("return %s;", columnId);
                        out.tab(1).println("}");

                        // Avoid generating this method twice
                        break patternLoop;
                    }
                }
            }

            timestampLoop: for (String pattern : database.getRecordTimestampFields()) {
                Pattern p = Pattern.compile(pattern, Pattern.COMMENTS);

                for (ColumnDefinition column : table.getColumns()) {
                    if ((p.matcher(column.getName()).matches() ||
                         p.matcher(column.getQualifiedName()).matches())) {

                        final String columnType = getJavaType(column.getType());
                        final String columnId = getStrategy().getFullJavaIdentifier(column);

                        out.tab(1).overrideInherit();
                        out.tab(1).println("public %s<%s, %s> getRecordTimestamp() {", TableField.class, recordType, columnType);
                        out.tab(2).println("return %s;", columnId);
                        out.tab(1).println("}");

                        // Avoid generating this method twice
                        break timestampLoop;
                    }
                }
            }
        }

        // [#117] With instance fields, it makes sense to create a
        // type-safe table alias
        if (generateInstanceFields()) {
            out.tab(1).overrideInherit();
            out.tab(1).println("public %s as(%s alias) {", fullClassName, String.class);

            if (table.isTableValuedFunction())
                out.tab(2).println("return new %s(alias, this, parameters);", fullClassName);
            else
                out.tab(2).println("return new %s(alias, this);", fullClassName);

            out.tab(1).println("}");
        }

        // [#2921] With instance fields, tables can be renamed.
        if (generateInstanceFields()) {
            out.tab(1).javadoc("Rename this table");
            out.tab(1).println("public %s rename(%s name) {", fullClassName, String.class);

            if (table.isTableValuedFunction())
                out.tab(2).println("return new %s(name, null, parameters);", fullClassName);
            else
                out.tab(2).println("return new %s(name, null);", fullClassName);

            out.tab(1).println("}");
        }

        // [#1070] Table-valued functions should generate an additional set of call() methods
        if (table.isTableValuedFunction()) {
            for (boolean parametersAsField : new boolean[] { false, true }) {

                // Don't overload no-args call() methods
                if (parametersAsField && table.getParameters().size() == 0)
                    break;

                out.tab(1).javadoc("Call this table-valued function");
                out.tab(1).print("public %s call(", fullClassName);
                printParameterDeclarations(out, table, parametersAsField);
                out.println(") {");

                out.tab(2).print("return new %s(getName(), null, new %s[] { ", fullClassName, Field.class);
                String separator = "";
                for (ParameterDefinition parameter : table.getParameters()) {
                    out.print(separator);

                    if (parametersAsField) {
                        out.print("%s", getStrategy().getJavaMemberName(parameter));
                    }
                    else {
                        out.print("%s.val(%s)", DSL.class, getStrategy().getJavaMemberName(parameter));
                    }

                    separator = ", ";
                }
                out.println(" });");

                out.tab(1).println("}");
            }
        }

        generateTableClassFooter(table, out);
        out.println("}");
        out.close();
    }

    private String escapeString(String comment) {

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
        printClassJavadoc(out, table);
    }

    protected void generateSequences(SchemaDefinition schema) {
        log.info("Generating sequences");

        JavaWriter out = newJavaWriter(new File(getStrategy().getFile(schema).getParentFile(), "Sequences.java"));
        printPackage(out, schema);
        printClassJavadoc(out, "Convenience access to all sequences in " + schema.getOutputName());
        printClassAnnotations(out, schema);
        out.println("public class Sequences {");

        for (SequenceDefinition sequence : database.getSequences(schema)) {
            final String seqType = getJavaType(sequence.getType());
            final String seqId = getStrategy().getJavaIdentifier(sequence);
            final String seqName = sequence.getOutputName();
            final String schemaId = getStrategy().getFullJavaIdentifier(schema);
            final String typeRef = getJavaTypeReference(sequence.getDatabase(), sequence.getType());

            out.tab(1).javadoc("The sequence <code>%s</code>", sequence.getQualifiedOutputName());
            out.tab(1).println("public static final %s<%s> %s = new %s<%s>(\"%s\", %s, %s);", Sequence.class, seqType, seqId, SequenceImpl.class, seqType, seqName, schemaId, typeRef);
        }

        out.println("}");
        out.close();

        watch.splitInfo("Sequences generated");
    }

    protected void generateSchema(SchemaDefinition schema) {
        log.info("Generating schema", getStrategy().getFileName(schema));
        log.info("----------------------------------------------------------");

        final String schemaName = schema.getQualifiedOutputName();
        final String schemaId = getStrategy().getJavaIdentifier(schema);
        final String className = getStrategy().getJavaClassName(schema);
        final List<String> interfaces = getStrategy().getJavaClassImplements(schema, Mode.DEFAULT);

        JavaWriter out = newJavaWriter(getStrategy().getFile(schema));
        printPackage(out, schema);
        generateSchemaClassJavadoc(schema, out);
        printClassAnnotations(out, schema);

        out.println("public class %s extends %s[[before= implements ][%s]] {", className, SchemaImpl.class, interfaces);
        out.printSerial();
        out.tab(1).javadoc("The singleton instance of <code>%s</code>", schemaName);
        out.tab(1).println("public static final %s %s = new %s();", className, schemaId, className);

        out.tab(1).javadoc(NO_FURTHER_INSTANCES_ALLOWED);
        out.tab(1).println("private %s() {", className);
        out.tab(2).println("super(\"%s\");", schema.getOutputName());
        out.tab(1).println("}");

        // [#2255] Avoid referencing sequence literals, if they're not generated
        if (generateGlobalObjectReferences()) {
            printSchemaReferences(out, database.getSequences(schema), Sequence.class, true);
        }

        printSchemaReferences(out, database.getTables(schema), Table.class, true);
        printSchemaReferences(out, database.getUDTs(schema), UDT.class, true);

        generateSchemaClassFooter(schema, out);
        out.println("}");
        out.close();
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
        printClassJavadoc(out, schema);
    }

    protected void printFromAndInto(JavaWriter out, TableDefinition table) {
        printFromAndInto(out, (Definition) table);
    }

    private void printFromAndInto(JavaWriter out, Definition tableOrUDT) {
        String qualified = getStrategy().getFullJavaClassName(tableOrUDT, Mode.INTERFACE);

        out.tab(1).header("FROM and INTO");
        out.tab(1).overrideInherit();
        out.tab(1).println("public void from(%s from) {", qualified);

        for (TypedElementDefinition<?> column : getTypedElements(tableOrUDT)) {
            String setter = getStrategy().getJavaSetterName(column, Mode.INTERFACE);
            String getter = getStrategy().getJavaGetterName(column, Mode.INTERFACE);

            out.tab(2).println("%s(from.%s());", setter, getter);
        }

        out.tab(1).println("}");

        out.tab(1).overrideInherit();
        out.tab(1).println("public <E extends %s> E into(E into) {", qualified);
        out.tab(2).println("into.from(this);");
        out.tab(2).println("return into;");
        out.tab(1).println("}");
    }

    protected void printSchemaReferences(JavaWriter out, List<? extends Definition> definitions, Class<?> type, boolean isGeneric) {
        if (out != null && !definitions.isEmpty()) {
            final String generic = isGeneric ? "<?>" : "";
            final List<String> references = getStrategy().getFullJavaIdentifiers(definitions);

            out.println();
            out.tab(1).override();
            out.tab(1).println("public final %s<%s%s> get%ss() {", List.class, type, generic, type.getSimpleName());
            out.tab(2).println("%s result = new %s();", List.class, ArrayList.class);
            for (int i = 0; i < definitions.size(); i += INITIALISER_SIZE) {
                out.tab(2).println("result.addAll(get%ss%s());", type.getSimpleName(), i / INITIALISER_SIZE);
            }
            out.tab(2).println("return result;");
            out.tab(1).println("}");

            for (int i = 0; i < definitions.size(); i += INITIALISER_SIZE) {
                out.println();
                out.tab(1).println("private final %s<%s%s> get%ss%s() {", List.class, type, generic, type.getSimpleName(), i / INITIALISER_SIZE);
                out.tab(2).println("return %s.<%s%s>asList([[before=\n\t\t\t][separator=,\n\t\t\t][%s]]);", Arrays.class, type, generic, references.subList(i, Math.min(i + INITIALISER_SIZE, references.size())));
                out.tab(1).println("}");
            }
        }
    }

    protected void printTableJPAAnnotation(JavaWriter out, TableDefinition table) {
        SchemaDefinition schema = table.getSchema();

        if (generateJPAAnnotations()) {
            out.println("@javax.persistence.Entity");
            out.print("@javax.persistence.Table(name = \"");
            out.print(table.getName().replace("\"", "\\\""));
            out.print("\"");

            if (!schema.isDefaultSchema()) {
                out.print(", schema = \"");
                out.print(schema.getOutputName().replace("\"", "\\\""));
                out.print("\"");
            }

            StringBuilder sb = new StringBuilder();
            String glue1 = "";

            for (UniqueKeyDefinition uk : table.getUniqueKeys()) {

                // Single-column keys are annotated on the column itself
                if (uk.getKeyColumns().size() > 1) {
                    sb.append(glue1);
                    sb.append("\t@javax.persistence.UniqueConstraint(columnNames = {");

                    String glue2 = "";
                    for (ColumnDefinition column : uk.getKeyColumns()) {
                        sb.append(glue2);
                        sb.append("\"");
                        sb.append(column.getName().replace("\"", "\\\""));
                        sb.append("\"");

                        glue2 = ", ";
                    }

                    sb.append("})");

                    glue1 = ",\n";
                }
            }

            if (sb.length() > 0) {
                out.println(", uniqueConstraints = {");
                out.println(sb.toString());
                out.print("}");
            }

            out.println(")");
        }
    }

    protected void printColumnJPAAnnotation(JavaWriter out, ColumnDefinition column) {
        if (generateJPAAnnotations()) {
            UniqueKeyDefinition pk = column.getPrimaryKey();
            List<UniqueKeyDefinition> uks = column.getUniqueKeys();

            if (pk != null) {
                if (pk.getKeyColumns().size() == 1) {
                    out.println("\t@javax.persistence.Id");
                }
            }

            String unique = "";
            for (UniqueKeyDefinition uk : uks) {
                if (uk.getKeyColumns().size() == 1) {
                    unique = ", unique = true";
                    break;
                }
            }

            String nullable = "";
            if (!column.getType().isNullable()) {
                nullable = ", nullable = false";
            }

            String length = "";
            String precision = "";
            String scale = "";

            if (column.getType().getLength() > 0) {
                length = ", length = " + column.getType().getLength();
            }
            else if (column.getType().getPrecision() > 0) {
                precision = ", precision = " + column.getType().getPrecision();

                if (column.getType().getScale() > 0) {
                    scale = ", scale = " + column.getType().getScale();
                }
            }

            out.print("\t@javax.persistence.Column(name = \"");
            out.print(column.getName().replace("\"", "\\\""));
            out.print("\"");
            out.print(unique);
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
            DataTypeDefinition type = column.getType();

            if (!column.getType().isNullable()) {
                out.tab(1).println("@javax.validation.constraints.NotNull");
            }

            if ("java.lang.String".equals(getJavaType(type))) {
                int length = type.getLength();

                if (length > 0) {
                    out.tab(1).println("@javax.validation.constraints.Size(max = %s)", length);
                }
            }
        }
    }

    protected void generateRoutine(SchemaDefinition schema, RoutineDefinition routine) {
        log.info("Generating routine", getStrategy().getFileName(routine));

        final String className = getStrategy().getJavaClassName(routine);
        final String returnType = (routine.getReturnValue() == null)
            ? Void.class.getName()
            : getJavaType(routine.getReturnType());
        final List<?> returnTypeRef = list((routine.getReturnValue() != null)
            ? getJavaTypeReference(database, routine.getReturnType())
            : null);
        final List<?> returnConverterType = list(
             (routine.getReturnValue() != null)
            ? routine.getReturnType().getConverter()
            : null,
             (routine.getReturnValue() != null)
            ? routine.getReturnType().getBinding()
            : null
        );
        final List<String> interfaces = getStrategy().getJavaClassImplements(routine, Mode.DEFAULT);
        final String schemaId = getStrategy().getFullJavaIdentifier(schema);
        final List<String> packageId = getStrategy().getFullJavaIdentifiers(routine.getPackage());

        JavaWriter out = newJavaWriter(getStrategy().getFile(routine));
        printPackage(out, routine);
        generateRoutineClassJavadoc(routine, out);
        printClassAnnotations(out, schema);

        out.println("public class %s extends %s<%s>[[before= implements ][%s]] {", className, AbstractRoutine.class, returnType, interfaces);
        out.printSerial();

        for (ParameterDefinition parameter : routine.getAllParameters()) {
            final String paramType = getJavaType(parameter.getType());
            final String paramTypeRef = getJavaTypeReference(parameter.getDatabase(), parameter.getType());
            final String paramId = getStrategy().getJavaIdentifier(parameter);
            final String paramName = parameter.getName();
            final String paramComment = StringUtils.defaultString(parameter.getComment());
            final String isDefaulted = parameter.isDefaulted() ? "true" : "false";
            final String paramConverterType = parameter.getType().getConverter();

            out.tab(1).javadoc("The parameter <code>%s</code>.%s", parameter.getQualifiedOutputName(), defaultIfBlank(" " + paramComment, ""));

            if (paramConverterType != null)
                out.tab(1).println("public static final %s<%s> %s = createParameter(\"%s\", %s, %s, new %s());",
                    Parameter.class, paramType, paramId, paramName, paramTypeRef, isDefaulted, paramConverterType);
            else
                out.tab(1).println("public static final %s<%s> %s = createParameter(\"%s\", %s, %s);",
                    Parameter.class, paramType, paramId, paramName, paramTypeRef, isDefaulted);
        }

        out.tab(1).javadoc("Create a new routine call instance");
        out.tab(1).println("public %s() {", className);
        out.tab(2).println("super(\"%s\", %s[[before=, ][%s]][[before=, ][%s]][[before=, ][new %s()]]);", routine.getName(), schemaId, packageId, returnTypeRef, returnConverterType);

        if (routine.getAllParameters().size() > 0) {
            out.println();
        }

        for (ParameterDefinition parameter : routine.getAllParameters()) {
            final String paramId = getStrategy().getJavaIdentifier(parameter);

            if (parameter.equals(routine.getReturnValue())) {
                out.tab(2).println("setReturnParameter(%s);", paramId);
            }
            else if (routine.getInParameters().contains(parameter)) {
                if (routine.getOutParameters().contains(parameter)) {
                    out.tab(2).println("addInOutParameter(%s);", paramId);
                }
                else {
                    out.tab(2).println("addInParameter(%s);", paramId);
                }
            }
            else {
                out.tab(2).println("addOutParameter(%s);", paramId);
            }
        }

        if (routine.getOverload() != null) {
            out.tab(2).println("setOverloaded(true);");
        }

        out.tab(1).println("}");

        for (ParameterDefinition parameter : routine.getInParameters()) {
            final String setterReturnType = fluentSetters() ? className : "void";
            final String setter = getStrategy().getJavaSetterName(parameter, Mode.DEFAULT);
            final String numberValue = parameter.getType().isGenericNumberType() ? "Number" : "Value";
            final String numberField = parameter.getType().isGenericNumberType() ? "Number" : "Field";
            final String paramId = getStrategy().getJavaIdentifier(parameter);
            final String paramFullId = getStrategy().getFullJavaIdentifier(parameter);

            out.tab(1).javadoc("Set the <code>%s</code> parameter IN value to the routine", parameter.getOutputName());
            out.tab(1).println("public void %s(%s value) {", setter, getNumberType(parameter.getType()));
            out.tab(2).println("set%s(%s, value);", numberValue, paramFullId);
            out.tab(1).println("}");

            if (routine.isSQLUsable()) {
                out.tab(1).javadoc("Set the <code>%s</code> parameter to the function to be used with a {@link org.jooq.Select} statement", parameter.getOutputName());
                out.tab(1).println("public %s %s(%s<%s> field) {", setterReturnType, setter, Field.class, getExtendsNumberType(parameter.getType()));
                out.tab(2).println("set%s(%s, field);", numberField, paramId);
                if (fluentSetters())
                    out.tab(2).println("return this;");
                out.tab(1).println("}");
            }
        }

        for (ParameterDefinition parameter : routine.getAllParameters()) {
            boolean isReturnValue = parameter.equals(routine.getReturnValue());
            boolean isOutParameter = routine.getOutParameters().contains(parameter);

            if (isOutParameter && !isReturnValue) {
                final String paramName = parameter.getOutputName();
                final String paramType = getJavaType(parameter.getType());
                final String paramGetter = getStrategy().getJavaGetterName(parameter, Mode.DEFAULT);
                final String paramId = getStrategy().getJavaIdentifier(parameter);

                out.tab(1).javadoc("Get the <code>%s</code> parameter OUT value from the routine", paramName);
                out.tab(1).println("public %s %s() {", paramType, paramGetter);
                out.tab(2).println("return getValue(%s);", paramId);
                out.tab(1).println("}");
            }
        }

        generateRoutineClassFooter(routine, out);
        out.println("}");
        out.close();
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
        printClassJavadoc(out, routine);
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

        final String className = getStrategy().getFullJavaClassName(function);
        final String localVar = disambiguateJavaMemberName(function.getInParameters(), "f");

        out.tab(1).javadoc("Get <code>%s</code> as a field", function.getQualifiedOutputName());
        out.tab(1).print("public static %s<%s> %s(",
            function.isAggregate() ? AggregateFunction.class : Field.class,
            getJavaType(function.getReturnType()),
            getStrategy().getJavaMethodName(function, Mode.DEFAULT));

        String separator = "";
        for (ParameterDefinition parameter : function.getInParameters()) {
            out.print(separator);

            if (parametersAsField) {
                out.print("%s<%s>", Field.class, getExtendsNumberType(parameter.getType()));
            } else {
                out.print(getNumberType(parameter.getType()));
            }

            out.print(" %s", getStrategy().getJavaMemberName(parameter));
            separator = ", ";
        }

        out.println(") {");
        out.tab(2).println("%s %s = new %s();", className, localVar, className);

        for (ParameterDefinition parameter : function.getInParameters()) {
            final String paramSetter = getStrategy().getJavaSetterName(parameter, Mode.DEFAULT);
            final String paramMember = getStrategy().getJavaMemberName(parameter);

            out.tab(2).println("%s.%s(%s);", localVar, paramSetter, paramMember);
        }

        out.println();
        out.tab(2).println("return %s.as%s();", localVar, function.isAggregate() ? "AggregateFunction" : "Field");
        out.tab(1).println("}");
    }

    protected void printConvenienceMethodTableValuedFunctionAsField(JavaWriter out, TableDefinition function, boolean parametersAsField) {
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

        final String className = getStrategy().getFullJavaClassName(function);

        out.tab(1).javadoc("Get <code>%s</code> as a field", function.getQualifiedOutputName());
        out.tab(1).print("public static %s %s(",
            className,
            getStrategy().getJavaMethodName(function, Mode.DEFAULT));

        printParameterDeclarations(out, function, parametersAsField);

        out.println(") {");
        out.tab(2).print("return %s.call(", getStrategy().getFullJavaIdentifier(function));

        String separator = "";
        for (ParameterDefinition parameter : function.getParameters()) {
            out.print(separator);
            out.print("%s", getStrategy().getJavaMemberName(parameter));

            separator = ", ";
        }

        out.println(");");
        out.tab(1).println("}");
    }

    private void printParameterDeclarations(JavaWriter out, TableDefinition function, boolean parametersAsField) {
        String sep1 = "";
        for (ParameterDefinition parameter : function.getParameters()) {
            out.print(sep1);

            if (parametersAsField) {
                out.print("%s<%s>", Field.class, getExtendsNumberType(parameter.getType()));
            } else {
                out.print(getNumberType(parameter.getType()));
            }

            out.print(" %s", getStrategy().getJavaMemberName(parameter));
            sep1 = ", ";
        }
    }

    private String disambiguateJavaMemberName(Collection<? extends Definition> definitions, String defaultName) {

        // [#2502] - Some name mangling in the event of procedure arguments
        // called "configuration"
        Set<String> names = new HashSet<String>();

        for (Definition definition : definitions) {
            names.add(getStrategy().getJavaMemberName(definition));
        }

        String name = defaultName;

        while (names.contains(name)) {
            name += "_";
        }

        return name;
    }

    protected void printConvenienceMethodFunction(JavaWriter out, RoutineDefinition function, boolean instance) {
        // [#281] - Java can't handle more than 255 method parameters
        if (function.getInParameters().size() > 254) {
            log.warn("Too many parameters", "Function " + function + " has more than 254 in parameters. Skipping generation of convenience method.");
            return;
        }

        final String className = getStrategy().getFullJavaClassName(function);
        final String functionName = function.getQualifiedOutputName();
        final String functionType = getJavaType(function.getReturnType());
        final String methodName = getStrategy().getJavaMethodName(function, Mode.DEFAULT);

        // [#3456] Local variables should not collide with actual function arguments
        final String configurationArgument = disambiguateJavaMemberName(function.getInParameters(), "configuration");
        final String localVar = disambiguateJavaMemberName(function.getInParameters(), "f");

        out.tab(1).javadoc("Call <code>%s</code>", functionName);
        out.tab(1).print("public %s%s %s(",
            !instance ? "static " : "", functionType, methodName);

        String glue = "";
        if (!instance) {
            out.print("%s %s", Configuration.class, configurationArgument);
            glue = ", ";
        }

        for (ParameterDefinition parameter : function.getInParameters()) {
            // Skip SELF parameter
            if (instance && parameter.equals(function.getInParameters().get(0))) {
                continue;
            }

            final String paramType = getNumberType(parameter.getType());
            final String paramMember = getStrategy().getJavaMemberName(parameter);

            out.print("%s%s %s", glue, paramType, paramMember);
            glue = ", ";
        }

        out.println(") {");
        out.tab(2).println("%s %s = new %s();", className, localVar, className);

        for (ParameterDefinition parameter : function.getInParameters()) {
            final String paramSetter = getStrategy().getJavaSetterName(parameter, Mode.DEFAULT);
            final String paramMember = (instance && parameter.equals(function.getInParameters().get(0)))
                ? "this"
                : getStrategy().getJavaMemberName(parameter);

            out.tab(2).println("%s.%s(%s);", localVar, paramSetter, paramMember);
        }

        out.println();
        out.tab(2).println("%s.execute(%s);", localVar, instance ? "configuration()" : configurationArgument);

        // TODO [#956] Find a way to register "SELF" as OUT parameter
        // in case this is a UDT instance (member) function
        out.tab(2).println("return %s.getReturnValue();", localVar);
        out.tab(1).println("}");
    }

    protected void printConvenienceMethodProcedure(JavaWriter out, RoutineDefinition procedure, boolean instance) {
        // [#281] - Java can't handle more than 255 method parameters
        if (procedure.getInParameters().size() > 254) {
            log.warn("Too many parameters", "Procedure " + procedure + " has more than 254 in parameters. Skipping generation of convenience method.");
            return;
        }

        final String className = getStrategy().getFullJavaClassName(procedure);
        final String configurationArgument = disambiguateJavaMemberName(procedure.getInParameters(), "configuration");
        final String localVar = disambiguateJavaMemberName(procedure.getInParameters(), "p");

        out.tab(1).javadoc("Call <code>%s</code>", procedure.getQualifiedOutputName());
        out.print("\tpublic ");

        if (!instance) {
            out.print("static ");
        }

        if (procedure.getOutParameters().size() == 0) {
            out.print("void ");
        }
        else if (procedure.getOutParameters().size() == 1) {
            out.print(getJavaType(procedure.getOutParameters().get(0).getType()));
            out.print(" ");
        }
        else {
            out.print(className + " ");
        }

        out.print(getStrategy().getJavaMethodName(procedure, Mode.DEFAULT));
        out.print("(");

        String glue = "";
        if (!instance) {
            out.print(Configuration.class);
            out.print(" ");
            out.print(configurationArgument);
            glue = ", ";
        }

        for (ParameterDefinition parameter : procedure.getInParameters()) {
            // Skip SELF parameter
            if (instance && parameter.equals(procedure.getInParameters().get(0))) {
                continue;
            }

            out.print(glue);
            out.print(getNumberType(parameter.getType()));
            out.print(" ");
            out.print(getStrategy().getJavaMemberName(parameter));

            glue = ", ";
        }

        out.println(") {");
        out.tab(2).println("%s %s = new %s();", className, localVar, className);

        for (ParameterDefinition parameter : procedure.getInParameters()) {
            final String setter = getStrategy().getJavaSetterName(parameter, Mode.DEFAULT);
            final String arg = (instance && parameter.equals(procedure.getInParameters().get(0)))
                ? "this"
                : getStrategy().getJavaMemberName(parameter);

            out.tab(2).println("%s.%s(%s);", localVar, setter, arg);
        }

        out.println();
        out.tab(2).println("%s.execute(%s);", localVar, instance ? "configuration()" : configurationArgument);

        if (procedure.getOutParameters().size() > 0) {
            final ParameterDefinition parameter = procedure.getOutParameters().get(0);

            final String getter = getStrategy().getJavaGetterName(parameter, Mode.DEFAULT);
            final String columnTypeInterface = getJavaType(parameter.getType(), Mode.INTERFACE);
            final boolean isUDT = parameter.getType().isUDT();

            if (instance) {

                // [#3117] Avoid funny call-site ambiguity if this is a UDT that is implemented by an interface
                if (generateInterfaces() && isUDT) {
                    out.tab(2).println("from((%s) %s.%s());", columnTypeInterface, localVar, getter);
                }
                else {
                    out.tab(2).println("from(%s.%s());", localVar, getter);
                }
            }

            if (procedure.getOutParameters().size() == 1) {
                out.tab(2).println("return %s.%s();", localVar, getter);
            }
            else if (procedure.getOutParameters().size() > 1) {
                out.tab(2).println("return %s;", localVar);
            }
        }

        out.tab(1).println("}");
    }

    protected void printRecordTypeMethod(JavaWriter out, Definition definition) {
        final String className = getStrategy().getFullJavaClassName(definition, Mode.RECORD);

        out.tab(1).javadoc("The class holding records for this type");
        out.tab(1).override();
        out.tab(1).println("public %s<%s> getRecordType() {", Class.class, className);
        out.tab(2).println("return %s.class;", className);
        out.tab(1).println("}");
    }

    protected void printSingletonInstance(JavaWriter out, Definition definition) {
        final String className = getStrategy().getFullJavaClassName(definition);
        final String identifier = getStrategy().getJavaIdentifier(definition);

        out.tab(1).javadoc("The singleton instance of <code>%s</code>", definition.getQualifiedOutputName());
        out.tab(1).println("public static final %s %s = new %s();", className, identifier, className);
    }

    protected void printClassJavadoc(JavaWriter out, Definition definition) {
        printClassJavadoc(out, definition.getComment());
    }

    protected void printClassJavadoc(JavaWriter out, String comment) {
        out.println("/**");

        if (comment != null && comment.length() > 0) {
            printJavadocParagraph(out, comment, "");
        }
        else {
            out.println(" * This class is generated by jOOQ.");
        }

        out.println(" */");
    }

    protected void printClassAnnotations(JavaWriter out, SchemaDefinition schema) {
        if (generateGeneratedAnnotation()) {
            out.println("@javax.annotation.Generated(");

            if (useSchemaVersionProvider()) {
                out.tab(1).println("value = {");
                out.tab(2).println("\"http://www.jooq.org\",");
                out.tab(2).println("\"jOOQ version:%s\",", Constants.VERSION);
                out.tab(2).println("\"schema version:%s\"", schemaVersions.get(schema).replace("\"", "\\\""));
                out.tab(1).println("},");
                out.tab(1).println("date = \"" + isoDate + "\",");
                out.tab(1).println("comments = \"This class is generated by jOOQ\"");
            }
            else {
                out.tab(1).println("value = {");
                out.tab(2).println("\"http://www.jooq.org\",");
                out.tab(2).println("\"jOOQ version:%s\"", Constants.VERSION);
                out.tab(1).println("},");
                out.tab(1).println("comments = \"This class is generated by jOOQ\"");
            }

            out.println(")");
        }

        out.println("@java.lang.SuppressWarnings({ \"all\", \"unchecked\", \"rawtypes\" })");
    }

    private final String readVersion(File file) {
        String result = null;

        try {
            RandomAccessFile f = new RandomAccessFile(file, "r");

            try {
                byte[] bytes = new byte[(int) f.length()];
                f.readFully(bytes);
                String string = new String(bytes);

                Matcher matcher = Pattern.compile("@javax.annotation.Generated\\(value\\s+= \\{.*?\"schema version:(.*?)\" \\},").matcher(string);
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

        // [#3450] Must not print */ inside Javadoc
        String escaped = comment.replace("*/", "* /");
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
        out.println("/**");
        out.println(" * This class is generated by jOOQ");
        out.println(" */");
        out.println("package %s;", getStrategy().getJavaPackageName(definition, mode));
        out.println();
    }

    protected String getExtendsNumberType(DataTypeDefinition type) {
        return getNumberType(type, "? extends ");
    }

    protected String getNumberType(DataTypeDefinition type) {
        return getNumberType(type, "");
    }

    protected String getNumberType(DataTypeDefinition type, String prefix) {
        if (type.isGenericNumberType()) {
            return prefix + Number.class.getName();
        }
        else {
            return getJavaType(type);
        }
    }

    protected String getSimpleJavaType(DataTypeDefinition type) {
        return GenerationUtil.getSimpleJavaType(getJavaType(type));
    }

    protected String getJavaTypeReference(Database db, DataTypeDefinition type) {
        if (database.isArrayType(type.getType())) {
            String baseType = GenerationUtil.getArrayBaseType(db.getDialect(), type.getType(), type.getUserType());
            return getTypeReference(
                db,
                type.getSchema(),
                baseType,
                0,
                0,
                0,
                true,
                false,
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
                type.isDefaulted(),
                type.getUserType()
            );
        }
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
            type.getUserType(),
            Object.class.getName(),
            udtMode);
    }

    protected String getType(Database db, SchemaDefinition schema, String t, int p, int s, String u, String defaultType) {
        return getType(db, schema, t, p, s, u, defaultType, Mode.RECORD);
    }

    protected String getType(Database db, SchemaDefinition schema, String t, int p, int s, String u, String defaultType, Mode udtMode) {
        String type = defaultType;

        // Array types
        if (db.isArrayType(t)) {
            String baseType = GenerationUtil.getArrayBaseType(db.getDialect(), t, u);
            type = getType(db, schema, baseType, p, s, baseType, defaultType) + "[]";
        }

        // Check for Oracle-style VARRAY types
        else if (db.getArray(schema, u) != null) {
            type = getStrategy().getFullJavaClassName(db.getArray(schema, u), Mode.RECORD);
        }

        // Check for ENUM types
        else if (db.getEnum(schema, u) != null) {
            type = getStrategy().getFullJavaClassName(db.getEnum(schema, u));
        }

        // Check for UDTs
        else if (db.getUDT(schema, u) != null) {
            type = getStrategy().getFullJavaClassName(db.getUDT(schema, u), udtMode);
        }

        // Check for custom types
        else if (db.getConfiguredCustomType(u) != null) {
            type = u;
        }

        // Try finding a basic standard SQL type according to the current dialect
        else {
            try {
                Class<?> clazz = DefaultDataType.getType(db.getDialect(), t, p, s);
                type = clazz.getCanonicalName();

                if (clazz.getTypeParameters().length > 0) {
                    type += "<";

                    String separator = "";
                    for (TypeVariable<?> var : clazz.getTypeParameters()) {
                        type += separator;
                        type += ((Class<?>) var.getBounds()[0]).getCanonicalName();

                        separator = ", ";
                    }

                    type += ">";
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

    protected String getTypeReference(Database db, SchemaDefinition schema, String t, int p, int s, int l, boolean n, boolean d, String u) {
        StringBuilder sb = new StringBuilder();
        if (db.getArray(schema, u) != null) {
            ArrayDefinition array = database.getArray(schema, u);

            sb.append(getJavaTypeReference(db, array.getElementType()));
            sb.append(".asArrayDataType(");
            sb.append(getStrategy().getFullJavaClassName(array, Mode.RECORD));
            sb.append(".class)");
        }
        else if (db.getUDT(schema, u) != null) {
            UDTDefinition udt = db.getUDT(schema, u);

            sb.append(getStrategy().getFullJavaIdentifier(udt));
            sb.append(".getDataType()");
        }
        else if (db.getEnum(schema, u) != null) {
            sb.append("org.jooq.util.");
            sb.append(db.getDialect().getName().toLowerCase());
            sb.append(".");
            sb.append(db.getDialect().getName());
            sb.append("DataType.");
            sb.append(DefaultDataType.normalise(DefaultDataType.getDataType(db.getDialect(), String.class).getTypeName()));
            sb.append(".asEnumDataType(");
            sb.append(getStrategy().getFullJavaClassName(db.getEnum(schema, u)));
            sb.append(".class)");
        }
        else {
            DataType<?> dataType = null;

            try {
                dataType = DefaultDataType.getDataType(db.getDialect(), t, p, s).nullable(n).defaulted(d);
            }

            // Mostly because of unsupported data types. Will be handled later.
            catch (SQLDialectNotSupportedException ignore) {
            }

            // If there is a standard SQLDataType available for the dialect-
            // specific DataType t, then reference that one.
            if (dataType != null && dataType.getSQLDataType() != null) {
                DataType<?> sqlDataType = dataType.getSQLDataType();

                sb.append(SQLDataType.class.getCanonicalName());
                sb.append(".");
                sb.append(DefaultDataType.normalise(sqlDataType.getTypeName()));

                if (dataType.hasPrecision() && p > 0) {
                    sb.append(".precision(").append(p);

                    if (dataType.hasScale() && s > 0) {
                        sb.append(", ").append(s);
                    }

                    sb.append(")");
                }

                if (dataType.hasLength() && l > 0) {
                    sb.append(".length(").append(l).append(")");
                }

                if (!dataType.nullable()) {
                    sb.append(".nullable(false)");
                }

                if (dataType.defaulted()) {
                    sb.append(".defaulted(true)");
                }
            }

            // Otherwise, reference the dialect-specific DataType itself.
            else {
                String typeClass =
                    "org.jooq.util." +
                    db.getDialect().getName().toLowerCase() +
                    "." +
                    db.getDialect().getName() +
                    "DataType";

                sb.append(typeClass);
                sb.append(".");

                try {
                    String type1 = getType(db, schema, t, p, s, u, null);
                    String type2 = getType(db, schema, t, 0, 0, u, null);
                    String typeName = DefaultDataType.normalise(t);

                    // [#1298] Prevent compilation errors for missing types
                    Reflect.on(typeClass).field(typeName);

                    sb.append(typeName);
                    if (!type1.equals(type2)) {
                        Class<?> clazz = DefaultDataType.getType(db.getDialect(), t, p, s);

                        sb.append(".asNumberDataType(");
                        sb.append(clazz.getCanonicalName());
                        sb.append(".class)");
                    }
                }

                // Mostly because of unsupported data types
                catch (SQLDialectNotSupportedException e) {
                    sb = new StringBuilder();

                    sb.append(DefaultDataType.class.getName());
                    sb.append(".getDefaultDataType(\"");
                    sb.append(t.replace("\"", "\\\""));
                    sb.append("\")");
                }

                // More unsupported data types
                catch (ReflectException e) {
                    sb = new StringBuilder();

                    sb.append(DefaultDataType.class.getName());
                    sb.append(".getDefaultDataType(\"");
                    sb.append(t.replace("\"", "\\\""));
                    sb.append("\")");
                }
            }
        }

        return sb.toString();
    }

    protected boolean match(DataTypeDefinition type1, DataTypeDefinition type2) {
        return getJavaType(type1).equals(getJavaType(type2));
    }

    private static final <T> List<T> list(T... objects) {
        List<T> result = new ArrayList<T>();

        if (objects != null)
            for (T object : objects)
                if (object != null && !"".equals(object))
                    result.add(object);

        return result;
    }

    private final JavaWriter newJavaWriter(File file) {
        files.add(file);
        return new JavaWriter(file);
    }
}
