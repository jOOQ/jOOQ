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

package org.jooq.util;


import java.io.File;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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
import org.jooq.Sequence;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UDT;
import org.jooq.UDTField;
import org.jooq.UniqueKey;
import org.jooq.exception.SQLDialectNotSupportedException;
import org.jooq.impl.AbstractKeys;
import org.jooq.impl.AbstractRoutine;
import org.jooq.impl.ArrayRecordImpl;
import org.jooq.impl.DAOImpl;
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

    private static final JooqLogger log                          = JooqLogger.getLogger(JavaGenerator.class);

    /**
     * The Javadoc to be used for private constructors
     */
    private static final String     NO_FURTHER_INSTANCES_ALLOWED = "No further instances allowed";

    /**
     * [#1459] Prevent large static initialisers by splitting nested classes
     */
    private static final int        INITIALISER_SIZE             = 500;

    /**
     * An overall stop watch to measure the speed of source code generation
     */
    private final StopWatch         watch                        = new StopWatch();

    /**
     * The underlying database of this generator
     */
    private Database                database;

    @Override
    public final void generate(Database db) {

        this.database = db;

        log.info("Database parameters");
        log.info("----------------------------------------------------------");
        log.info("  dialect", database.getDialect());
        log.info("  target dir", getTargetDirectory());
        log.info("  target package", getTargetPackage());
        log.info("----------------------------------------------------------");
        log.info("");
        log.info("DefaultGenerator parameters");
        log.info("----------------------------------------------------------");
        log.info("  strategy", strategy.delegate.getClass());
        log.info("  deprecated", generateDeprecated());
        log.info("  generated annotation", generateGeneratedAnnotation());
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

        String targetPackage = getTargetPackage();
        File targetPackageDir = new File(getTargetDirectory() + File.separator + targetPackage.replace('.', File.separatorChar));

        // ----------------------------------------------------------------------
        // XXX Initialising
        // ----------------------------------------------------------------------
        log.info("Emptying", targetPackageDir.getAbsolutePath());
        empty(targetPackageDir, ".java");

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

        if (database.getUDTs(schema).size() > 0) {
            generateUDTRecords(schema);
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

        if (database.getRoutines(schema).size() > 0) {
            generateRoutines(schema);
        }

        if (database.getPackages(schema).size() > 0) {
            generatePackages(schema);
        }

        // XXX [#651] Refactoring-cursor
        watch.splitInfo("GENERATION FINISHED!");
    }

    protected void generateRelations(SchemaDefinition schema) {
        log.info("Generating Keys");

        JavaWriter out = new JavaWriter(new File(getStrategy().getFile(schema).getParentFile(), "Keys.java"));
        printPackage(out, schema);
        printClassJavadoc(out,
            "A class modelling foreign key relationships between tables of the <code>" + schema.getOutputName() + "</code> schema");

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
        log.info("Generating records");

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
        log.info("Generating record", getStrategy().getFileName(table, Mode.RECORD));

        final UniqueKeyDefinition key = table.getPrimaryKey();
        final String className = getStrategy().getJavaClassName(table, Mode.RECORD);
        final String tableIdentifier = getStrategy().getFullJavaIdentifier(table);
        final String recordType = getStrategy().getFullJavaClassName(table, Mode.RECORD);
        final List<String> interfaces = getStrategy().getJavaClassImplements(table, Mode.RECORD);

        JavaWriter out = new JavaWriter(getStrategy().getFile(table, Mode.RECORD));
        printPackage(out, table, Mode.RECORD);
        printClassJavadoc(out, table);
        printTableJPAAnnotation(out, table);

        Class<?> baseClass;

        if (generateRelations() && key != null) {
            baseClass = UpdatableRecordImpl.class;
        } else {
            baseClass = TableRecordImpl.class;
        }

        int degree = table.getColumns().size();
        String rowType = null;
        String rowTypeRecord = null;

        if (degree <= Constants.MAX_ROW_DEGREE) {
            rowType = getRowType(table.getColumns());
            rowTypeRecord = Record.class.getName() + degree + "<" + rowType + ">";
            interfaces.add(rowTypeRecord);
        }

        if (generateInterfaces()) {
            interfaces.add(getStrategy().getFullJavaClassName(table, Mode.INTERFACE));
        }

        out.println("public class %s extends %s<%s>[[before= implements ][%s]] {", className, baseClass, recordType, interfaces);
        out.printSerial();

        for (int i = 0; i < degree; i++) {
            ColumnDefinition column = table.getColumn(i);

            final String comment = StringUtils.defaultString(column.getComment());
            final String setter = getStrategy().getJavaSetterName(column, Mode.DEFAULT);
            final String getter = getStrategy().getJavaGetterName(column, Mode.DEFAULT);
            final String type = getJavaType(column.getType());
            final String name = column.getQualifiedOutputName();

            out.tab(1).javadoc("Setter for <code>%s</code>. %s", name, comment);
            out.tab(1).overrideIf(generateInterfaces());
            out.tab(1).println("public void %s(%s value) {", setter, type);
            out.tab(2).println("setValue(%s, value);", i);
            out.tab(1).println("}");

            out.tab(1).javadoc("Getter for <code>%s</code>. %s", name, comment);
            printColumnJPAAnnotation(out, column);
            printColumnValidationAnnotation(out, column);
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

        if (degree <= Constants.MAX_ROW_DEGREE) {
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
                ColumnDefinition column = table.getColumn(i - 1);

                final String colType = getJavaType(column.getType());
                final String colIdentifier = getStrategy().getFullJavaIdentifier(column);

                out.tab(1).overrideInherit();
                out.tab(1).println("public %s<%s> field%s() {", Field.class, colType, i);
                out.tab(2).println("return %s;", colIdentifier);
                out.tab(1).println("}");
            }

            // value[N]()
            for (int i = 1; i <= degree; i++) {
                ColumnDefinition column = table.getColumn(i - 1);

                final String colType = getJavaType(column.getType());
                final String colGetter = getStrategy().getJavaGetterName(column, Mode.RECORD);

                out.tab(1).overrideInherit();
                out.tab(1).println("public %s value%s() {", colType, i);
                out.tab(2).println("return %s();", colGetter);
                out.tab(1).println("}");
            }
        }

        if (generateInterfaces()) {
            printFromAndInto(out, table);
        }

        out.tab(1).header("Constructors");
        out.tab(1).javadoc("Create a detached %s", className);
        out.tab(1).println("public %s() {", className);
        out.tab(2).println("super(%s);", tableIdentifier);
        out.tab(1).println("}");

        out.println("}");
        out.close();
    }

    private final String getRowType(Collection<? extends ColumnDefinition> columns) {
        StringBuilder result = new StringBuilder();
        String separator = "";

        for (ColumnDefinition column : columns) {
            result.append(separator);
            result.append(getJavaType(column.getType()));

            separator = ", ";
        }

        return result.toString();
    }

    protected void generateInterfaces(SchemaDefinition schema) {
        log.info("Generating interfaces");

        for (TableDefinition table : database.getTables(schema)) {
            try {
                generateInterface(table);
            } catch (Exception e) {
                log.error("Error while generating table record " + table, e);
            }
        }

        watch.splitInfo("Table records generated");
    }

    protected void generateInterface(TableDefinition table) {
        log.info("Generating interface", getStrategy().getFileName(table, Mode.INTERFACE));

        final String className = getStrategy().getJavaClassName(table, Mode.INTERFACE);
        final List<String> interfaces = getStrategy().getJavaClassImplements(table, Mode.INTERFACE);

        JavaWriter out = new JavaWriter(getStrategy().getFile(table, Mode.INTERFACE));
        printPackage(out, table, Mode.INTERFACE);
        printClassJavadoc(out, table);
        printTableJPAAnnotation(out, table);

        out.println("public interface %s [[before=extends ][%s]] {", className, interfaces);

        for (ColumnDefinition column : table.getColumns()) {
            final String comment = StringUtils.defaultString(column.getComment());
            final String setter = getStrategy().getJavaSetterName(column, Mode.DEFAULT);
            final String getter = getStrategy().getJavaGetterName(column, Mode.DEFAULT);
            final String type = getJavaType((column).getType());
            final String name = column.getQualifiedOutputName();

            out.tab(1).javadoc("Setter for <code>%s</code>. %s", name, comment);
            out.tab(1).println("public void %s(%s value);", setter, type);

            out.tab(1).javadoc("Getter for <code>%s</code>. %s", name, comment);
            printColumnJPAAnnotation(out, column);
            printColumnValidationAnnotation(out, column);
            out.tab(1).println("public %s %s();", type, getter);
        }

        String local = getStrategy().getJavaClassName(table, Mode.INTERFACE);
        String qualified = getStrategy().getFullJavaClassName(table, Mode.INTERFACE);

        out.tab(1).header("FROM and INTO");
        out.tab(1).javadoc("Load data from another generated Record/POJO implementing the common interface %s", local);
        out.tab(1).println("public void from(%s from);", qualified);

        out.tab(1).javadoc("Copy data into another generated Record/POJO implementing the common interface %s", local);
        out.tab(1).println("public <E extends %s> E into(E into);", qualified);
        out.println("}");
        out.close();
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

        JavaWriter out = new JavaWriter(getStrategy().getFile(udt));
        printPackage(out, udt);
        printClassJavadoc(out, udt);

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

            out.tab(1).javadoc("The attribute <code>%s</code>. %s", attribute.getQualifiedOutputName(), attrComment);
            out.tab(1).println("public static final %s<%s, %s> %s = createField(\"%s\", %s, %s);",
                UDTField.class, recordType, attrType, attrId, attrName, attrTypeRef, udtId);
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

        out.println("}");
        out.close();
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
        log.info("Generating UDT record", getStrategy().getFileName(udt, Mode.RECORD));

        final String className = getStrategy().getJavaClassName(udt, Mode.RECORD);
        final String recordType = getStrategy().getFullJavaClassName(udt, Mode.RECORD);
        final List<String> interfaces = getStrategy().getJavaClassImplements(udt, Mode.RECORD);
        final String udtId = getStrategy().getFullJavaIdentifier(udt);

        JavaWriter out = new JavaWriter(getStrategy().getFile(udt, Mode.RECORD));
        printPackage(out, udt, Mode.RECORD);
        printClassJavadoc(out, udt);

        out.println("public class %s extends %s<%s>[[before= implements ][%s]] {", className, UDTRecordImpl.class, recordType, interfaces);
        out.printSerial();
        out.println();

        for (AttributeDefinition attribute : udt.getAttributes()) {
            final String comment = StringUtils.defaultString(attribute.getComment());
            final String setter = getStrategy().getJavaSetterName(attribute, Mode.DEFAULT);
            final String getter = getStrategy().getJavaGetterName(attribute, Mode.DEFAULT);
            final String type = getJavaType((attribute).getType());
            final String id = getStrategy().getFullJavaIdentifier(attribute);
            final String name = attribute.getQualifiedOutputName();

            out.tab(1).javadoc("Setter for <code>%s</code>. %s", name, comment);
            out.tab(1).println("public void %s(%s value) {", setter, type);
            out.tab(2).println("setValue(%s, value);", id);
            out.tab(1).println("}");

            out.tab(1).javadoc("Getter for <code>%s</code>. %s", name, comment);
            out.tab(1).println("public %s %s() {", type, getter);
            out.tab(2).println("return getValue(%s);", id);
            out.tab(1).println("}");
        }

        // [#799] Oracle UDT's can have member procedures
        for (RoutineDefinition routine : udt.getRoutines()) {

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

        out.tab(1).javadoc("Create a new <code>%s</code> record", udt.getQualifiedOutputName());
        out.tab(1).println("public %s() {", className);
        out.tab(2).println("super(%s);", udtId);
        out.tab(1).println("}");

        out.println("}");
        out.close();
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

        JavaWriter out = new JavaWriter(new File(getStrategy().getFile(schema).getParentFile(), "UDTs.java"));
        printPackage(out, schema);
        printClassJavadoc(out, "Convenience access to all UDTs in " + schema.getOutputName());
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

        final String className = getStrategy().getJavaClassName(array, Mode.RECORD);
        final String elementType = getJavaType(array.getElementType());
        final String elementTypeRef = getJavaTypeReference(database, array.getElementType());
        final List<String> interfaces = getStrategy().getJavaClassImplements(array, Mode.RECORD);
        final String arrayName = array.getOutputName();
        final String schemaId = getStrategy().getFullJavaIdentifier(schema);

        JavaWriter out = new JavaWriter(getStrategy().getFile(array, Mode.RECORD));
        printPackage(out, array, Mode.RECORD);
        printClassJavadoc(out, array);

        out.println("public class %s extends %s<%s>[[before= implements ][%s]] {", className, ArrayRecordImpl.class, elementType, interfaces);
        out.printSerial();

        out.tab(1).javadoc("Create a new <code>%s</code> record", array.getQualifiedOutputName());
        out.tab(1).println("public %s(%s configuration) {", className, Configuration.class);
        out.tab(2).println("super(%s, \"%s\", %s, configuration);", schemaId, arrayName, elementTypeRef);
        out.tab(1).println("}");

        out.tab(1).javadoc("Create a new <code>%s</code> record", array.getQualifiedOutputName());
        out.tab(1).println("public %s(%s configuration, %s... array) {", className, Configuration.class, elementType);
        out.tab(2).println("this(configuration);");
        out.tab(2).println("set(array);");
        out.tab(1).println("}");

        out.tab(1).javadoc("Create a new <code>%s</code> record", array.getQualifiedOutputName());
        out.tab(1).println("public %s(%s configuration, %s<? extends %s> list) {", className, Configuration.class, List.class, elementType);
        out.tab(2).println("this(configuration);");
        out.tab(2).println("setList(list);");
        out.tab(1).println("}");

        out.println("}");
        out.close();
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

        JavaWriter out = new JavaWriter(getStrategy().getFile(e, Mode.ENUM));
        printPackage(out, e);
        printClassJavadoc(out, e);

        interfaces.add(EnumType.class.getName());

        out.println("public enum %s[[before= implements ][%s]] {", className, interfaces);

        for (String literal : e.getLiterals()) {
            final String identifier = GenerationUtil.convertToJavaIdentifier(literal);

            out.println();
            out.tab(1).println("%s(\"%s\"),", identifier, literal);
        }

        out.println();
        out.tab(1).println(";");
        out.println();
        out.tab(1).println("private final java.lang.String literal;");
        out.println();
        out.tab(1).println("private %s(java.lang.String literal) {", className);
        out.tab(2).println("this.literal = literal;");
        out.tab(1).println("}");

        out.tab(1).overrideInherit();
        out.tab(1).println("public java.lang.String getName() {");
        out.tab(2).println("return %s;", e.isSynthetic() ? "null" : "\"" + e.getName() + "\"");
        out.tab(1).println("}");

        out.tab(1).overrideInherit();
        out.tab(1).println("public java.lang.String getLiteral() {");
        out.tab(2).println("return literal;");
        out.tab(1).println("}");

        out.println("}");
        out.close();
    }

    protected void generateRoutines(SchemaDefinition schema) {
        log.info("Generating routines");

        JavaWriter outR = new JavaWriter(new File(getStrategy().getFile(schema).getParentFile(), "Routines.java"));
        printPackage(outR, schema);
        printClassJavadoc(outR, "Convenience access to all stored procedures and functions in " + schema.getOutputName());

        outR.println("public class Routines {");
        for (RoutineDefinition routine : database.getRoutines(schema)) {
            printRoutine(outR, routine);

            try {
                generateRoutine(schema, routine);
            } catch (Exception e) {
                log.error("Error while generating routine " + routine, e);
            }
        }

        outR.println("}");
        outR.close();

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
        JavaWriter out = new JavaWriter(getStrategy().getFile(pkg));
        printPackage(out, pkg);
        printClassJavadoc(out, "Convenience access to all stored procedures and functions in " + pkg.getName());

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
        out.println("}");

        out.close();
    }

    /**
     * Generating central static table access
     */
    protected void generateTableReferences(SchemaDefinition schema) {
        log.info("Generating table references");

        JavaWriter out = new JavaWriter(new File(getStrategy().getFile(schema).getParentFile(), "Tables.java"));
        printPackage(out, schema);
        printClassJavadoc(out, "Convenience access to all tables in " + schema.getOutputName());
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
        ColumnDefinition keyColumn = null;

        if (key != null) {
            List<ColumnDefinition> columns = key.getKeyColumns();

            if (columns.size() == 1) {
                keyColumn = columns.get(0);
                tType = getJavaType(keyColumn.getType());
            }
        }

        // Skip DAOs for tables that don't have 1-column-PKs (for now)
        if (keyColumn == null) {
            log.info("Skipping DAO generation", getStrategy().getFileName(table, Mode.DAO));
            return;
        }
        else {
            log.info("Generating DAO", getStrategy().getFileName(table, Mode.DAO));
        }

        JavaWriter out = new JavaWriter(getStrategy().getFile(table, Mode.DAO));
        printPackage(out, table, Mode.DAO);
        printClassJavadoc(out, table);

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
        out.tab(2).println("return object.%s();", getStrategy().getJavaGetterName(keyColumn, Mode.POJO));
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

        out.println("}");
        out.close();
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
        log.info("Generating table POJO", getStrategy().getFileName(table, Mode.POJO));

        final String className = getStrategy().getJavaClassName(table, Mode.POJO);
        final String superName = getStrategy().getJavaClassExtends(table, Mode.POJO);
        final List<String> interfaces = getStrategy().getJavaClassImplements(table, Mode.POJO);

        if (generateInterfaces()) {
            interfaces.add(getStrategy().getFullJavaClassName(table, Mode.INTERFACE));
        }

        JavaWriter out = new JavaWriter(getStrategy().getFile(table, Mode.POJO));
        printPackage(out, table, Mode.POJO);
        printClassJavadoc(out, table);
        printTableJPAAnnotation(out, table);

        out.println("public class %s[[before= extends ][%s]][[before= implements ][%s]] {", className, list(superName), interfaces);
        out.printSerial();

        out.println();

        int maxLength = 0;
        for (ColumnDefinition column : table.getColumns()) {
            maxLength = Math.max(maxLength, getJavaType(column.getType()).length());
        }

        for (ColumnDefinition column : table.getColumns()) {
            out.tab(1).println("private %s%s %s;",
                generateImmutablePojos() ? "final " : "",
                StringUtils.rightPad(getJavaType(column.getType()), maxLength),
                getStrategy().getJavaMemberName(column, Mode.POJO));
        }

        // Constructor
        if (generateImmutablePojos()) {
            out.println();
            out.tab(1).print("public %s(", className);

            String separator1 = "";
            for (ColumnDefinition column : table.getColumns()) {
                out.println(separator1);

                out.tab(2).print("%s %s",
                    StringUtils.rightPad(getJavaType(column.getType()), maxLength),
                    getStrategy().getJavaMemberName(column, Mode.POJO));
                separator1 = ",";
            }

            out.println();
            out.tab(1).println(") {");

            for (ColumnDefinition column : table.getColumns()) {
                final String columnMember = getStrategy().getJavaMemberName(column, Mode.POJO);

                out.tab(2).println("this.%s = %s;", columnMember, columnMember);
            }

            out.tab(1).println("}");
        }

        for (ColumnDefinition column : table.getColumns()) {
            final String columnType = getJavaType(column.getType());
            final String columnGetter = getStrategy().getJavaGetterName(column, Mode.POJO);
            final String columnSetter = getStrategy().getJavaSetterName(column, Mode.POJO);
            final String columnMember = getStrategy().getJavaMemberName(column, Mode.POJO);

            // Getter
            out.println();
            printColumnJPAAnnotation(out, column);
            printColumnValidationAnnotation(out, column);
            out.tab(1).overrideIf(generateInterfaces());
            out.tab(1).println("public %s %s() {", columnType, columnGetter);
            out.tab(2).println("return this.%s;", columnMember);
            out.tab(1).println("}");

            // Setter
            if (!generateImmutablePojos()) {
                out.println();
                out.tab(1).overrideIf(generateInterfaces());
                out.tab(1).println("public void %s(%s %s) {", columnSetter, columnType, columnMember);
                out.tab(2).println("this.%s = %s;", columnMember, columnMember);
                out.tab(1).println("}");
            }
        }

        if (generateInterfaces()) {
            printFromAndInto(out, table);
        }

        out.println("}");
        out.close();
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

        log.info("Generating table", getStrategy().getFileName(table) +
            " [input=" + table.getInputName() +
            ", output=" + table.getOutputName() +
            ", pk=" + (primaryKey != null ? primaryKey.getName() : "N/A") +
            "]");

        JavaWriter out = new JavaWriter(getStrategy().getFile(table));
        printPackage(out, table);
        printClassJavadoc(out, table);

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

            String isStatic = generateInstanceFields() ? "" : "static ";
            String tableRef = generateInstanceFields() ? "this" : getStrategy().getJavaIdentifier(table);

            out.tab(1).javadoc("The column <code>%s</code>. %s", column.getQualifiedOutputName(), columnComment);
            out.tab(1).println("public %sfinal %s<%s, %s> %s = createField(\"%s\", %s, %s);",
                isStatic, TableField.class, recordType, columnType, columnId, columnName, columnTypeRef, tableRef);
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

        out.tab(2).println("super(\"%s\", %s);", table.getOutputName(), getStrategy().getFullJavaIdentifier(schema));
        out.tab(1).println("}");

        // [#117] With instance fields, it makes sense to create a
        // type-safe table alias
        // [#1255] With instance fields, the table constructor may
        // be public, as tables are no longer singletons
        if (generateInstanceFields()) {
            final String schemaId = getStrategy().getFullJavaIdentifier(schema);

            out.tab(1).javadoc("Create an aliased <code>%s</code> table reference", table.getQualifiedOutputName());
            out.tab(1).println("public %s(%s alias) {", className, String.class);
            out.tab(2).println("super(alias, %s, %s);", schemaId, fullTableId);
            out.tab(1).println("}");
        }

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
                for (ColumnDefinition column : table.getColumns()) {
                    if ((column.getName().matches(pattern.trim()) ||
                         column.getQualifiedName().matches(pattern.trim()))) {

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
                for (ColumnDefinition column : table.getColumns()) {
                    if ((column.getName().matches(pattern.trim()) ||
                         column.getQualifiedName().matches(pattern.trim()))) {

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
            out.tab(2).println("return new %s(alias);", fullClassName);
            out.tab(1).println("}");
        }

        out.println("}");
        out.close();
    }

    protected void generateSequences(SchemaDefinition schema) {
        log.info("Generating sequences");

        JavaWriter out = new JavaWriter(new File(getStrategy().getFile(schema).getParentFile(), "Sequences.java"));
        printPackage(out, schema);
        printClassJavadoc(out, "Convenience access to all sequences in " + schema.getOutputName());
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

        JavaWriter out = new JavaWriter(getStrategy().getFile(schema));
        printPackage(out, schema);
        printClassJavadoc(out, schema);

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

        out.println("}");
        out.close();
    }

    protected void printFromAndInto(JavaWriter out, TableDefinition table) {
        String qualified = getStrategy().getFullJavaClassName(table, Mode.INTERFACE);

        out.tab(1).header("FROM and INTO");
        out.tab(1).overrideInherit();
        out.tab(1).println("public void from(%s from) {", qualified);

        for (ColumnDefinition column : table.getColumns()) {
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
            if (!column.isNullable()) {
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

    protected void printColumnValidationAnnotation(JavaWriter out, ColumnDefinition column) {
        if (generateValidationAnnotations()) {
            DataTypeDefinition type = column.getType();

            if (!column.isNullable()) {
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
        final List<String> interfaces = getStrategy().getJavaClassImplements(routine, Mode.DEFAULT);
        final String schemaId = getStrategy().getFullJavaIdentifier(schema);
        final List<String> packageId = getStrategy().getFullJavaIdentifiers(routine.getPackage());

        JavaWriter out = new JavaWriter(getStrategy().getFile(routine));
        printPackage(out, routine);
        printClassJavadoc(out, routine);

        out.println("public class %s extends %s<%s>[[before= implements ][%s]] {", className, AbstractRoutine.class, returnType, interfaces);
        out.printSerial();

        for (ParameterDefinition parameter : routine.getAllParameters()) {
            final String paramType = getJavaType(parameter.getType());
            final String paramTypeRef = getJavaTypeReference(parameter.getDatabase(), parameter.getType());
            final String paramId = getStrategy().getJavaIdentifier(parameter);
            final String paramName = parameter.getName();
            final String paramComment = StringUtils.defaultString(parameter.getComment());
            final List<String> isDefaulted = list(parameter.isDefaulted() ? "true" : null);

            out.tab(1).javadoc("The parameter <code>%s</code>. %s", parameter.getQualifiedOutputName(), paramComment);
            out.tab(1).println("public static final %s<%s> %s = createParameter(\"%s\", %s[[before=, ][%s]]);",
                Parameter.class, paramType, paramId, paramName, paramTypeRef, isDefaulted);
        }

        out.tab(1).javadoc("Create a new routine call instance");
        out.tab(1).println("public %s() {", className);
        out.tab(2).println("super(\"%s\", %s[[before=, ][%s]][[before=, ][%s]]);", routine.getName(), schemaId, packageId, returnTypeRef);

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
                out.tab(1).println("public void %s(%s<%s> field) {", setter, Field.class, getExtendsNumberType(parameter.getType()));
                out.tab(2).println("set%s(%s, field);", numberField, paramId);
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

        out.println("}");
        out.close();
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
        out.tab(2).println("%s f = new %s();", className, className);

        for (ParameterDefinition parameter : function.getInParameters()) {
            final String paramSetter = getStrategy().getJavaSetterName(parameter, Mode.DEFAULT);
            final String paramMember = getStrategy().getJavaMemberName(parameter);

            out.tab(2).println("f.%s(%s);", paramSetter, paramMember);
        }

        out.println();
        out.tab(2).println("return f.as%s();", function.isAggregate() ? "AggregateFunction" : "Field");
        out.tab(1).println("}");
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

        out.tab(1).javadoc("Call <code>%s</code>", functionName);
        out.tab(1).print("public %s%s %s(",
            !instance ? "static " : "", functionType, methodName);

        String glue = "";
        if (!instance) {
            out.print("%s configuration", Configuration.class);
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
        out.tab(2).println("%s f = new %s();", className, className);

        for (ParameterDefinition parameter : function.getInParameters()) {
            final String paramSetter = getStrategy().getJavaSetterName(parameter, Mode.DEFAULT);
            final String paramMember = (instance && parameter.equals(function.getInParameters().get(0)))
                ? "this"
                : getStrategy().getJavaMemberName(parameter);

            out.tab(2).println("f.%s(%s);", paramSetter, paramMember);
        }

        out.println();
        out.tab(2).println("f.execute(%s);", instance ? "configuration()" : "configuration");

        // TODO [#956] Find a way to register "SELF" as OUT parameter
        // in case this is a UDT instance (member) function
        out.tab(2).println("return f.getReturnValue();");
        out.tab(1).println("}");
    }

    protected void printConvenienceMethodProcedure(JavaWriter out, RoutineDefinition procedure, boolean instance) {
        // [#281] - Java can't handle more than 255 method parameters
        if (procedure.getInParameters().size() > 254) {
            log.warn("Too many parameters", "Procedure " + procedure + " has more than 254 in parameters. Skipping generation of convenience method.");
            return;
        }

        final String className = getStrategy().getFullJavaClassName(procedure);

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
            out.print(" configuration");
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
        out.tab(2).println("%s p = new %s();", className, className);

        for (ParameterDefinition parameter : procedure.getInParameters()) {
            final String setter = getStrategy().getJavaSetterName(parameter, Mode.DEFAULT);
            final String arg = (instance && parameter.equals(procedure.getInParameters().get(0)))
                ? "this"
                : getStrategy().getJavaMemberName(parameter);

            out.tab(2).println("p.%s(%s);", setter, arg);
        }

        out.println();
        out.tab(2).println("p.execute(%s);", instance ? "configuration()" : "configuration");

        if (procedure.getOutParameters().size() > 0) {
            final String getter = getStrategy().getJavaGetterName(procedure.getOutParameters().get(0), Mode.DEFAULT);

            if (instance) {
                out.tab(2).println("from(p.%s());", getter);
            }

            if (procedure.getOutParameters().size() == 1) {
                out.tab(2).println("return p.%s();", getter);
            }
            else if (procedure.getOutParameters().size() > 1) {
                out.tab(2).println("return p;");
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
        out.println(" * This class is generated by jOOQ.");

        if (comment != null && comment.length() > 0) {
            out.println(" *");
            printJavadocParagraph(out, comment, "");
        }

        out.println(" */");

        if (generateGeneratedAnnotation()) {
            out.println("@javax.annotation.Generated(value    = {\"http://www.jooq.org\", \"%s\"},", Constants.VERSION);
            out.println("                            comments = \"This class is generated by jOOQ\")");
        }

        out.println("@java.lang.SuppressWarnings({ \"all\", \"unchecked\", \"rawtypes\" })");
    }

    /**
     * This method is used to add line breaks in lengthy javadocs
     */
    protected void printJavadocParagraph(JavaWriter out, String comment, String indent) {
        boolean newLine = true;
        int lineLength = 0;

        for (int i = 0; i < comment.length(); i++) {
            if (newLine) {
                out.print(indent);
                out.print(" * ");

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
        out.javadoc("This class is generated by jOOQ");
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
            return getTypeReference(db, type.getSchema(), baseType, 0, 0, 0, baseType) + ".getArrayDataType()";
        }
        else {
            return getTypeReference(db, type.getSchema(), type.getType(), type.getPrecision(), type.getScale(), type.getLength(), type.getUserType());
        }
    }

    protected String getJavaType(DataTypeDefinition type) {
        return getType(
            type.getDatabase(),
            type.getSchema(),
            type.getType(),
            type.getPrecision(),
            type.getScale(),
            type.getUserType(),
            Object.class.getName());
    }

    protected String getType(Database db, SchemaDefinition schema, String t, int p, int s, String u, String defaultType) {
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
            type = getStrategy().getFullJavaClassName(db.getUDT(schema, u), Mode.RECORD);
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

    protected String getTypeReference(Database db, SchemaDefinition schema, String t, int p, int s, int l, String u) {
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
                dataType = DefaultDataType.getDataType(db.getDialect(), t, p, s);
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

                if (db.getConfiguredCustomType(u) != null) {
                    sb.append(".asConvertedDataType(new ");
                    sb.append(db.getConfiguredCustomType(u).getConverter());
                    sb.append("())");
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

    private static final <T> List<T> list(T object) {
        List<T> result = new ArrayList<T>();

        if (object != null && !"".equals(object)) {
            result.add(object);
        }

        return result;
    }
}
