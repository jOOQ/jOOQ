/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
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


import static java.util.Arrays.asList;
import static org.jooq.util.GenerationUtil.convertToJavaIdentifier;

import java.io.File;
import java.lang.reflect.TypeVariable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.jooq.Configuration;
import org.jooq.Constants;
import org.jooq.DataType;
import org.jooq.EnumType;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.MasterDataType;
import org.jooq.Parameter;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Select;
import org.jooq.Sequence;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UDT;
import org.jooq.UDTField;
import org.jooq.UniqueKey;
import org.jooq.conf.Settings;
import org.jooq.conf.SettingsTools;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.SQLDialectNotSupportedException;
import org.jooq.impl.AbstractKeys;
import org.jooq.impl.AbstractRoutine;
import org.jooq.impl.ArrayRecordImpl;
import org.jooq.impl.DAOImpl;
import org.jooq.impl.Factory;
import org.jooq.impl.FieldTypeHelper;
import org.jooq.impl.PackageImpl;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.SchemaImpl;
import org.jooq.impl.SequenceImpl;
import org.jooq.impl.TableImpl;
import org.jooq.impl.TableRecordImpl;
import org.jooq.impl.UDTImpl;
import org.jooq.impl.UDTRecordImpl;
import org.jooq.impl.UpdatableRecordImpl;
import org.jooq.impl.UpdatableTableImpl;
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
public class DefaultGenerator extends AbstractGenerator {

    private static final JooqLogger log = JooqLogger.getLogger(DefaultGenerator.class);

    StopWatch watch = new StopWatch();

    Database database;

    @Override
    public void generate(Database db) {

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
        log.info("  instance fields", generateInstanceFields());
        log.info("  JPA annotations", generateJPAAnnotations());
        log.info("  validation annotations", generateValidationAnnotations());
        log.info("  navigation methods", generateNavigationMethods());
        log.info("  records", generateRecords()
            + ((!generateRecords && generateDaos) ? " (forced to true because of <daos/>)" : ""));
        log.info("  pojos", generatePojos()
            + ((!generatePojos && generateDaos) ? " (forced to true because of <daos/>)" : ""));
        log.info("  interfaces", generateInterfaces());
        log.info("  daos", generateDaos());
        log.info("  relations", generateRelations());
        log.info("----------------------------------------------------------");

        String targetPackage = getTargetPackage();
        File targetPackageDir = new File(getTargetDirectory() + File.separator + targetPackage.replace('.', File.separatorChar));

        // ----------------------------------------------------------------------
        // XXX Initialising
        // ----------------------------------------------------------------------
        log.info("Emptying", targetPackageDir.getAbsolutePath());
        empty(targetPackageDir);

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

    GenerationWriter outS = null;
    GenerationWriter outF = null;

    protected void generate(SchemaDefinition schema) {

        File targetSchemaDir = strategy.getFile(schema).getParentFile();

        if (!schema.isDefaultSchema()) {
            generateSchema(schema);
        }

        if (database.getSequences(schema).size() > 0) {
            generateSequences(schema, targetSchemaDir);
        }

        if (database.getMasterDataTables(schema).size() > 0) {
            generateMasterTables(schema);
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

        if (database.getTables(schema).size() > 0) {
            generateTableReferences(schema, targetSchemaDir);
        }

        if (generateRelations() && database.getTables(schema).size() > 0) {
            generateRelations(schema, targetSchemaDir);
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
            generateUDTDefinitions(schema);
        }

        if (database.getUDTs(schema).size() > 0) {
            generateUDTRoutines(schema);
        }

        if (database.getUDTs(schema).size() > 0) {
            generateUDTReferences(schema, targetSchemaDir);
        }

        if (database.getArrays(schema).size() > 0) {
            generateArrays(schema);
        }

        if (database.getEnums(schema).size() > 0) {
            generateEnums(schema);
        }

        if (database.getRoutines(schema).size() > 0) {
            generateRoutines(schema, targetSchemaDir);
        }

        if (database.getPackages(schema).size() > 0) {
            generatePackages(schema);
        }

        close();

        // XXX [#651] Refactoring-cursor
        watch.splitInfo("GENERATION FINISHED!");
    }

    protected void generateRelations(SchemaDefinition schema, File targetSchemaDir) {
        log.info("Generating Keys");

        GenerationWriter out = new GenerationWriter(new File(targetSchemaDir, "Keys.java"));
        printHeader(out, schema);
        printClassJavadoc(out,
            "A class modelling foreign key relationships between tables of the <code>" + schema.getOutputName() + "</code> schema");

        out.println("public class Keys {");
        out.println();
        out.println("\t// IDENTITY definitions");

        // [#1459] Prevent large static initialisers by splitting nested classes
        final int INITIALISER_SIZE = 500;
        List<IdentityDefinition> allIdentities = new ArrayList<IdentityDefinition>();
        List<UniqueKeyDefinition> allUniqueKeys = new ArrayList<UniqueKeyDefinition>();
        List<ForeignKeyDefinition> allForeignKeys = new ArrayList<ForeignKeyDefinition>();

        for (TableDefinition table : database.getTables(schema)) {
            try {
                IdentityDefinition identity = table.getIdentity();

                if (identity != null) {
                    out.print("\tpublic static final ");
                    out.print(Identity.class);
                    out.print("<");
                    out.print(strategy.getFullJavaClassName(identity.getColumn().getContainer(), Mode.RECORD));
                    out.print(", ");
                    out.print(getJavaType(identity.getColumn().getType()));
                    out.print("> IDENTITY_");
                    out.print(strategy.getJavaIdentifier(identity.getColumn().getContainer()));
                    out.print(" = Identities");
                    out.print(allIdentities.size() / INITIALISER_SIZE);
                    out.print(".IDENTITY_");
                    out.print(strategy.getJavaIdentifier(identity.getColumn().getContainer()));
                    out.println(";");

                    allIdentities.add(identity);
                }
            }
            catch (Exception e) {
                log.error("Error while generating table " + table, e);
            }
        }

        // Unique keys
        out.println();
        out.println("\t// UNIQUE and PRIMARY KEY definitions");

        for (TableDefinition table : database.getTables(schema)) {
            try {
                List<UniqueKeyDefinition> uniqueKeys = table.getUniqueKeys();

                for (UniqueKeyDefinition uniqueKey : uniqueKeys) {
                    out.print("\tpublic static final ");
                    out.print(UniqueKey.class);
                    out.print("<");
                    out.print(strategy.getFullJavaClassName(uniqueKey.getTable(), Mode.RECORD));
                    out.print("> ");
                    out.print(strategy.getJavaIdentifier(uniqueKey));
                    out.print(" = UniqueKeys");
                    out.print(allUniqueKeys.size() / INITIALISER_SIZE);
                    out.print(".");
                    out.print(strategy.getJavaIdentifier(uniqueKey));
                    out.println(";");

                    allUniqueKeys.add(uniqueKey);
                }
            }
            catch (Exception e) {
                log.error("Error while generating table " + table, e);
            }
        }

        // Foreign keys
        out.println();
        out.println("\t// FOREIGN KEY definitions");

        for (TableDefinition table : database.getTables(schema)) {
            try {
                List<ForeignKeyDefinition> foreignKeys = table.getForeignKeys();

                for (ForeignKeyDefinition foreignKey : foreignKeys) {

                    // Skip master data foreign keys
                    if (foreignKey.getReferencedTable() instanceof MasterDataTableDefinition) {
                        continue;
                    }

                    out.print("\tpublic static final ");
                    out.print(ForeignKey.class);
                    out.print("<");
                    out.print(strategy.getFullJavaClassName(foreignKey.getKeyTable(), Mode.RECORD));
                    out.print(", ");
                    out.print(strategy.getFullJavaClassName(foreignKey.getReferencedTable(), Mode.RECORD));
                    out.print("> ");
                    out.print(strategy.getJavaIdentifier(foreignKey));
                    out.print(" = ForeignKeys");
                    out.print(allForeignKeys.size() / INITIALISER_SIZE);
                    out.print(".");
                    out.print(strategy.getJavaIdentifier(foreignKey));
                    out.println(";");

                    allForeignKeys.add(foreignKey);
                }
            }
            catch (Exception e) {
                log.error("Error while generating reference " + table, e);
            }
        }

        printPrivateConstructor(out, "Keys");

        // [#1459] Print nested classes for actual static field initialisations
        // keeping top-level initialiser small
        int identityCounter = 0;
        int uniqueKeyCounter = 0;
        int foreignKeyCounter = 0;

        // Identities
        // ----------

        for (IdentityDefinition identity : allIdentities) {
            generateIdentity(out, INITIALISER_SIZE, identityCounter, identity);
            identityCounter++;
        }

        if (identityCounter > 0) {
            out.println("\t}");
        }

        // UniqueKeys
        // ----------

        for (UniqueKeyDefinition uniqueKey : allUniqueKeys) {
            generateUniqueKey(out, INITIALISER_SIZE, uniqueKeyCounter, uniqueKey);
            uniqueKeyCounter++;
        }

        if (uniqueKeyCounter > 0) {
            out.println("\t}");
        }

        // ForeignKeys
        // -----------

        for (ForeignKeyDefinition foreignKey : allForeignKeys) {
            generateForeignKey(out, INITIALISER_SIZE, foreignKeyCounter, foreignKey);
            foreignKeyCounter++;
        }

        if (foreignKeyCounter > 0) {
            out.println("\t}");
        }

        out.println("}");
        out.close();

        watch.splitInfo("Keys generated");
    }

    protected void generateIdentity(GenerationWriter out, final int INITIALISER_SIZE, int identityCounter,
        IdentityDefinition identity) {
        // Print new nested class
        if (identityCounter % INITIALISER_SIZE == 0) {
            if (identityCounter > 0) {
                out.println("\t}");
            }

            out.println();
            out.println("\t@SuppressWarnings(\"hiding\")");
            out.print("\tprivate static class Identities");
            out.print(identityCounter / INITIALISER_SIZE);
            out.print(" extends ");
            out.print(AbstractKeys.class);
            out.println(" {");
        }

        out.print("\t\tpublic static ");
        out.print(Identity.class);
        out.print("<");
        out.print(strategy.getFullJavaClassName(identity.getTable(), Mode.RECORD));
        out.print(", ");
        out.print(getJavaType(identity.getColumn().getType()));
        out.print("> ");
        out.print(strategy.getJavaIdentifier(identity));
        out.print(" = createIdentity(");
        out.print(strategy.getFullJavaIdentifier(identity.getColumn().getContainer()));
        out.print(", ");
        out.print(strategy.getFullJavaIdentifier(identity.getColumn()));
        out.println(");");
    }

    protected void generateUniqueKey(GenerationWriter out, final int INITIALISER_SIZE, int uniqueKeyCounter,
        UniqueKeyDefinition uniqueKey) {
        // Print new nested class
        if (uniqueKeyCounter % INITIALISER_SIZE == 0) {
            if (uniqueKeyCounter > 0) {
                out.println("\t}");
            }

            out.println();
            out.print("\t@SuppressWarnings({");
            generateUniqueKeySuppressHidingWarning( out, uniqueKey );
            out.println("\"unchecked\"})");
            out.print("\tprivate static class UniqueKeys");
            out.print(uniqueKeyCounter / INITIALISER_SIZE);
            out.print(" extends ");
            out.print(AbstractKeys.class);
            out.println(" {");
        }

        out.print("\t\tpublic static final ");
        out.print(UniqueKey.class);
        out.print("<");
        out.print(strategy.getFullJavaClassName(uniqueKey.getTable(), Mode.RECORD));
        out.print("> ");
        out.print(strategy.getJavaIdentifier(uniqueKey));
        out.print(" = createUniqueKey(");
        out.print(strategy.getFullJavaIdentifier(uniqueKey.getTable()));
        out.print(", ");

        String separator = "";
        for (ColumnDefinition column : uniqueKey.getKeyColumns()) {
            out.print(separator);
            out.print(strategy.getFullJavaIdentifier(column));
            separator = ", ";
        }

        out.println(");");
    }

    protected void generateUniqueKeySuppressHidingWarning(GenerationWriter out, UniqueKeyDefinition uniqueKey) {
        out.print("\"hiding\", ");
    }

    protected void generateForeignKey(GenerationWriter out, final int INITIALISER_SIZE, int foreignKeyCounter,
        ForeignKeyDefinition foreignKey) {
        // Print new nested class
        if (foreignKeyCounter % INITIALISER_SIZE == 0) {
            if (foreignKeyCounter > 0) {
                out.println("\t}");
            }

            out.println();
            out.print("\t@SuppressWarnings({");
            generateForeignKeySuppressHidingWarning(out, foreignKey);
            out.println("\"unchecked\"})");
            out.print("\tprivate static class ForeignKeys");
            out.print(foreignKeyCounter / INITIALISER_SIZE);
            out.print(" extends ");
            out.print(AbstractKeys.class);
            out.println(" {");
        }

        out.print("\t\tpublic static final ");
        out.print(ForeignKey.class);
        out.print("<");
        out.print(strategy.getFullJavaClassName(foreignKey.getKeyTable(), Mode.RECORD));
        out.print(", ");
        out.print(strategy.getFullJavaClassName(foreignKey.getReferencedTable(), Mode.RECORD));
        out.print("> ");
        out.print(strategy.getJavaIdentifier(foreignKey));
        out.print(" = createForeignKey(");
        out.print(strategy.getFullJavaIdentifier(foreignKey.getReferencedKey()));
        out.print(", ");
        out.print(strategy.getFullJavaIdentifier(foreignKey.getKeyTable()));
        out.print(", ");

        String separator = "";
        for (ColumnDefinition column : foreignKey.getKeyColumns()) {
            out.print(separator);
            out.print(strategy.getFullJavaIdentifier(column));
            separator = ", ";
        }

        out.println(");");
    }

    protected void generateForeignKeySuppressHidingWarning(GenerationWriter out, ForeignKeyDefinition foreignKey) {
        out.print("\"hiding\", ");
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
        log.info("Generating record", strategy.getFileName(table, Mode.RECORD));

        GenerationWriter out = new GenerationWriter(strategy.getFile(table, Mode.RECORD));
        printHeader(out, table, Mode.RECORD);
        printClassJavadoc(out, table);
        printTableJPAAnnotation(out, table);

        Class<?> baseClass;

        if (generateRelations() && table.getMainUniqueKey() != null) {
            baseClass = UpdatableRecordImpl.class;
        } else {
            baseClass = TableRecordImpl.class;
        }

        out.print("public class ");
        out.print(strategy.getJavaClassName(table, Mode.RECORD));
        out.print(" extends ");
        out.print(baseClass);
        out.print("<");
        out.print(strategy.getFullJavaClassName(table, Mode.RECORD));
        out.print(">");
        printImplements(out, table, Mode.RECORD);
        out.println(" {");
        out.printSerial();

        for (ColumnDefinition column : table.getColumns()) {
            printGetterAndSetter(out, column);
        }

        out.println();
        out.println("\t/**");
        out.println("\t * Create a detached " + strategy.getJavaClassName(table, Mode.RECORD));
        out.println("\t */");
        out.println("\tpublic " + strategy.getJavaClassName(table, Mode.RECORD) + "() {");
        out.print("\t\tsuper(");
        out.print(strategy.getFullJavaIdentifier(table));
        out.println(");");
        out.println("\t}");
        out.println("}");
        out.close();
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
        log.info("Generating interface", strategy.getFileName(table, Mode.INTERFACE));

        GenerationWriter out = new GenerationWriter(strategy.getFile(table, Mode.INTERFACE));
        printHeader(out, table, Mode.INTERFACE);
        printClassJavadoc(out, table);
        printTableJPAAnnotation(out, table);

        out.print("public interface ");
        out.print(strategy.getJavaClassName(table, Mode.INTERFACE));
        printImplements(out, table, Mode.INTERFACE);
        out.println(" {");

        for (ColumnDefinition column : table.getColumns()) {
            printGetterAndSetter(out, column, false);
        }

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

        registerInSchema(database.getUDTs(schema), UDT.class, true);
        watch.splitInfo("UDTs generated");
    }

    protected void generateUDT(SchemaDefinition schema, UDTDefinition udt) {
        log.info("Generating UDT ", strategy.getFileName(udt));

        GenerationWriter out = new GenerationWriter(strategy.getFile(udt));
        printHeader(out, udt);
        printClassJavadoc(out, udt);

        out.print("public class ");
        out.print(strategy.getJavaClassName(udt));
        out.print(" extends ");
        out.print(UDTImpl.class);
        out.print("<");
        out.print(strategy.getFullJavaClassName(udt, Mode.RECORD));
        out.print(">");

        // [#799] Oracle UDTs with member procedures have similarities
        // with packages
        if (udt.getRoutines().size() > 0) {
            printImplements(out, udt, Mode.DEFAULT, org.jooq.Package.class.getName());
        }
        else {
            printImplements(out, udt, Mode.DEFAULT);
        }

        out.println(" {");
        out.printSerial();

        printSingletonInstance(udt, out);
        printRecordTypeMethod(udt, out);

        for (AttributeDefinition attribute : udt.getAttributes()) {
            printUDTColumn(out, attribute, udt);
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
                    printConvenienceMethodFunction(out, routine, false);

                    // Static asField() convenience method
                    printConvenienceMethodFunctionAsField(out, routine, false);
                    printConvenienceMethodFunctionAsField(out, routine, true);
                }

            } catch (Exception e) {
                log.error("Error while generating routine " + routine, e);
            }
        }

        out.println();
        printNoFurtherInstancesAllowedJavadoc(out);
        out.println("\tprivate " + strategy.getJavaClassName(udt) + "() {");

        if (!schema.isDefaultSchema()) {
            out.println("\t\tsuper(\"" + udt.getOutputName() + "\", " + strategy.getFullJavaIdentifier(schema) + ");");
        } else {
            out.println("\t\tsuper(\"" + udt.getOutputName() + "\");");
        }

        out.println();
        out.println("\t\t// Initialise data type");
        out.println("\t\tgetDataType();");
        out.println("\t}");

        out.println("}");
        out.close();
    }

    /**
     * Generating UDT record classes
     */
    protected void generateUDTDefinitions(SchemaDefinition schema) {
        log.info("Generating UDT records");

        for (UDTDefinition udt : database.getUDTs(schema)) {
            try {
                generateUDTDefinition(udt);
            } catch (Exception e) {
                log.error("Error while generating UDT record " + udt, e);
            }
        }

        watch.splitInfo("UDT records generated");
    }

    protected void generateUDTDefinition(UDTDefinition udt) {
        log.info("Generating UDT record", strategy.getFileName(udt, Mode.RECORD));

        GenerationWriter out = new GenerationWriter(strategy.getFile(udt, Mode.RECORD));
        printHeader(out, udt, Mode.RECORD);
        printClassJavadoc(out, udt);

        out.print("public class ");
        out.print(strategy.getJavaClassName(udt, Mode.RECORD));
        out.print(" extends ");
        out.print(UDTRecordImpl.class);
        out.print("<");
        out.print(strategy.getFullJavaClassName(udt, Mode.RECORD));
        out.print(">");
        printImplements(out, udt, Mode.RECORD);
        out.println(" {");

        out.printSerial();
        out.println();

        for (AttributeDefinition attribute : udt.getAttributes()) {
            printGetterAndSetter(out, attribute);
        }

        // [#799] Oracle UDT's can have member procedures
        for (RoutineDefinition routine : udt.getRoutines()) {
            try {
                if (!routine.isSQLUsable()) {
                    // Instance execute() convenience method
                    printConvenienceMethodProcedure(out, routine, true);
                }
                else {
                    // Instance execute() convenience method
                    printConvenienceMethodFunction(out, routine, true);
                }

            } catch (Exception e) {
                log.error("Error while generating routine " + routine, e);
            }
        }

        out.println();
        out.println("\tpublic " + strategy.getJavaClassName(udt, Mode.RECORD) + "() {");

        out.print("\t\tsuper(");
        out.print(strategy.getFullJavaIdentifier(udt));
        out.println(");");

        out.println("\t}");
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
                            printRoutine(schema, routine);
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
    protected void generateUDTReferences(SchemaDefinition schema, File targetSchemaDir) {
        log.info("Generating UDT references");

        GenerationWriter out = new GenerationWriter(new File(targetSchemaDir, "UDTs.java"));
        printHeader(out, schema);
        printClassJavadoc(out, "Convenience access to all UDTs in " + schema.getOutputName());
        out.println("public final class UDTs {");

        for (UDTDefinition udt : database.getUDTs(schema)) {
            generateUDTReference(out, udt);
        }

        printPrivateConstructor(out, "UDTs");
        out.println("}");
        out.close();

        watch.splitInfo("UDT references generated");
    }

    protected void generateUDTReference(GenerationWriter out, UDTDefinition udt) {
        out.println();
        out.println("\t/**");
        out.println("\t * The type " + udt.getQualifiedOutputName());
        out.println("\t */");

        out.print("\tpublic static ");
        out.print(strategy.getFullJavaClassName(udt));
        out.print(" ");
        out.print(strategy.getJavaIdentifier(udt));
        out.print(" = ");
        out.print(strategy.getFullJavaIdentifier(udt));
        out.println(";");
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
        log.info("Generating ARRAY", strategy.getFileName(array, Mode.RECORD));

        GenerationWriter out = new GenerationWriter(strategy.getFile(array, Mode.RECORD));
        printHeader(out, array, Mode.RECORD);
        printClassJavadoc(out, array);

        out.print("public class ");
        out.print(strategy.getJavaClassName(array, Mode.RECORD));
        out.print(" extends ");
        out.print(ArrayRecordImpl.class);
        out.print("<");
        out.print(getJavaType(array.getElementType()));
        out.print(">");
        printImplements(out, array, Mode.RECORD);
        out.println(" {");
        out.printSerial();

        out.println();
        out.print("\tpublic ");
        out.print(strategy.getJavaClassName(array, Mode.RECORD));
        out.print("(");
        out.print(Configuration.class);
        out.println(" configuration) {");
        out.print("\t\tsuper(");
        out.print(strategy.getFullJavaIdentifier(schema));
        out.print(", \"");
        out.print(array.getOutputName());
        out.print("\", ");
        out.print(getJavaTypeReference(database, array.getElementType()));
        out.println(", configuration);");
        out.println("\t}");

        out.println();
        out.print("\tpublic ");
        out.print(strategy.getJavaClassName(array, Mode.RECORD));
        out.print("(");
        out.print(Configuration.class);
        out.print(" configuration, ");
        out.print(getJavaType(array.getElementType()));
        out.print("... array");
        out.println(") {");
        out.println("\t\tthis(configuration);");
        out.println("\t\tset(array);");
        out.println("\t}");

        out.println();
        out.print("\tpublic ");
        out.print(strategy.getJavaClassName(array, Mode.RECORD));
        out.print("(");
        out.print(Configuration.class);
        out.print(" configuration, ");
        out.print(List.class);
        out.print("<? extends ");
        out.print(getJavaType(array.getElementType()));
        out.print("> list");
        out.println(") {");
        out.println("\t\tthis(configuration);");
        out.println("\t\tsetList(list);");
        out.println("\t}");

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
        log.info("Generating ENUM", strategy.getFileName(e, Mode.ENUM));

        GenerationWriter out = new GenerationWriter(strategy.getFile(e, Mode.ENUM));
        printHeader(out, e);
        printClassJavadoc(out, e);

        out.print("public enum ");
        out.print(strategy.getJavaClassName(e, Mode.ENUM));
        printImplements(out, e, Mode.ENUM, EnumType.class.getName());
        out.print(" {");
        out.println();

        for (String literal : e.getLiterals()) {
            out.println("\t" + GenerationUtil.convertToJavaIdentifier(literal) + "(\"" + literal + "\"),");
            out.println();
        }

        out.println("\t;");
        out.println();
        out.println("\tprivate final java.lang.String literal;");
        out.println();
        out.println("\tprivate " + strategy.getJavaClassName(e, Mode.ENUM) + "(java.lang.String literal) {");
        out.println("\t\tthis.literal = literal;");
        out.println("\t}");
        out.println();
        out.println("\t@Override");
        out.println("\tpublic java.lang.String getName() {");

        if (e.isSynthetic()) {
            out.println("\t\treturn null;");
        }
        else {
            out.println("\t\treturn \"" + e.getName() + "\";");
        }

        out.println("\t}");
        out.println();
        out.println("\t@Override");
        out.println("\tpublic java.lang.String getLiteral() {");
        out.println("\t\treturn literal;");
        out.println("\t}");

        out.println("}");

        out.close();
    }

    protected void generateRoutines(SchemaDefinition schema, File targetSchemaDir) {
        log.info("Generating routines");

        GenerationWriter outR = new GenerationWriter(new File(targetSchemaDir, "Routines.java"));
        printHeader(outR, schema);
        printClassJavadoc(outR, "Convenience access to all stored procedures and functions in " + schema.getOutputName());

        outR.println("public final class Routines {");
        for (RoutineDefinition routine : database.getRoutines(schema)) {
            try {
                generateRoutine(schema, outR, routine);
            } catch (Exception e) {
                log.error("Error while generating routine " + routine, e);
            }
        }

        printPrivateConstructor(outR, "Routines");
        outR.println("}");
        outR.close();

        watch.splitInfo("Routines generated");
    }

    protected void generateRoutine(SchemaDefinition schema, GenerationWriter outR,
        RoutineDefinition routine) {
        printRoutine(schema, routine);

        if (!routine.isSQLUsable()) {

            // Static execute() convenience method
            printConvenienceMethodProcedure(outR, routine, false);
        }
        else {

            // Static execute() convenience method
            printConvenienceMethodFunction(outR, routine, false);

            // Static asField() convenience method
            printConvenienceMethodFunctionAsField(outR, routine, false);
            printConvenienceMethodFunctionAsField(outR, routine, true);
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

        for (RoutineDefinition routine : pkg.getRoutines()) {
            try {
                printRoutine(schema, routine);
            } catch (Exception e) {
                log.error("Error while generating routine " + routine, e);
            }
        }

        // Static convenience methods
        GenerationWriter outPkg = new GenerationWriter(strategy.getFile(pkg));
        printHeader(outPkg, pkg);
        printClassJavadoc(outPkg, "Convenience access to all stored procedures and functions in " + pkg.getName());
        outPkg.print("public final class ");
        outPkg.print(strategy.getJavaClassName(pkg));
        outPkg.print(" extends ");
        outPkg.print(PackageImpl.class);
        printImplements(outPkg, pkg, Mode.DEFAULT);
        outPkg.println(" {");
        outPkg.printSerial();
        printSingletonInstance(pkg, outPkg);

        for (RoutineDefinition routine : pkg.getRoutines()) {
            try {
                if (!routine.isSQLUsable()) {
                    // Static execute() convenience method
                    printConvenienceMethodProcedure(outPkg, routine, false);
                }
                else {
                    // Static execute() convenience method
                    printConvenienceMethodFunction(outPkg, routine, false);

                    // Static asField() convenience method
                    printConvenienceMethodFunctionAsField(outPkg, routine, false);
                    printConvenienceMethodFunctionAsField(outPkg, routine, true);
                }

            } catch (Exception e) {
                log.error("Error while generating routine " + routine, e);
            }
        }

        printNoFurtherInstancesAllowedJavadoc(outPkg);
        outPkg.println("\tprivate " + strategy.getJavaClassName(pkg) + "() {");
        outPkg.print("\t\tsuper(\"");
        outPkg.print(pkg.getOutputName());
        outPkg.print("\", ");
        outPkg.print(strategy.getFullJavaIdentifier(schema));
        outPkg.println(");");
        outPkg.println("\t}");
        outPkg.println("}");

        outPkg.close();
    }

    protected void close() {
        // Finalise schema
        if (outS != null) {
            outS.println("}");
            outS.close();
        }

        // Finalise factory
        if (outF != null) {
            outF.println("}");
            outF.close();
        }
    }

    /**
     * Generating central static table access
     */
    protected void generateTableReferences(SchemaDefinition schema, File targetSchemaDir) {
        log.info("Generating table references");

        File file = new File(targetSchemaDir, "Tables.java");
        GenerationWriter out;
        try {
            out = new GenerationWriter(file);
        } catch (Exception e) {
            log.error("Error while generating " + file, e);
            return;
        }
        printHeader(out, schema);
        printClassJavadoc(out, "Convenience access to all tables in " + schema.getOutputName());
        out.println("public final class Tables {");

        for (TableDefinition table : database.getTables(schema)) {
            generateTableReference(out, table);
        }

        printPrivateConstructor(out, "Tables");
        out.println("}");
        out.close();

        watch.splitInfo("Table references generated");
    }

    protected void generateTableReference(GenerationWriter out, TableDefinition table) {
        out.println();
        out.println("\t/**");

        if (!StringUtils.isBlank(table.getComment())) {
            out.println("\t * " + table.getComment());
        }
        else {
            out.println("\t * The table " + table.getQualifiedOutputName());
        }

        out.println("\t */");

        out.print("\tpublic static ");
        out.print(strategy.getFullJavaClassName(table));
        out.print(" ");
        out.print(strategy.getJavaIdentifier(table));
        out.print(" = ");
        out.print(strategy.getFullJavaIdentifier(table));
        out.println(";");
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
        String tType = "Void";
        String pType = strategy.getFullJavaClassName(table, Mode.POJO);

        UniqueKeyDefinition key = table.getMainUniqueKey();
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
            log.info("Skipping DAO generation", strategy.getFileName(table, Mode.DAO));
            return;
        }
        else {
            log.info("Generating DAO", strategy.getFileName(table, Mode.DAO));
        }

        GenerationWriter out = new GenerationWriter(strategy.getFile(table, Mode.DAO));
        printHeader(out, table, Mode.DAO);
        printClassJavadoc(out, table);

        out.print("public class ");
        out.print(strategy.getJavaClassName(table, Mode.DAO));
        // printExtends(out, table, Mode.DAO);
        // printImplements(out, table, Mode.DAO);
        out.print(" extends ");
        out.print(DAOImpl.class);
        out.print("<");
        out.print(strategy.getFullJavaClassName(table, Mode.RECORD));
        out.print(", ");
        out.print(pType);
        out.print(", ");
        out.print(tType);
        out.println("> {");
        out.println();

        // Default constructor
        // -------------------
        printJavadoc(out, "Create a new "
            + strategy.getJavaClassName(table, Mode.DAO)
            + " without any factory");
        out.print("\tpublic ");
        out.print(strategy.getJavaClassName(table, Mode.DAO));
        out.println("() {");
        out.print("\t\tsuper(");
        out.print(strategy.getFullJavaIdentifier(table));
        out.print(", ");
        out.print(strategy.getFullJavaClassName(table, Mode.POJO));
        out.println(".class);");
        out.println("\t}");
        out.println();

        // Initialising constructor
        // ------------------------
        printJavadoc(out, "Create a new "
            + strategy.getJavaClassName(table, Mode.DAO)
            + " with an attached factory");
        out.print("\tpublic ");
        out.print(strategy.getJavaClassName(table, Mode.DAO));
        out.print("(");
        out.print(Factory.class);
        out.println(" factory) {");
        out.print("\t\tsuper(");
        out.print(strategy.getFullJavaIdentifier(table));
        out.print(", ");
        out.print(strategy.getFullJavaClassName(table, Mode.POJO));
        out.println(".class, factory);");
        out.println("\t}");
        out.println();

        // Template method implementations
        // -------------------------------
        printOverride(out);
        out.print("\tprotected ");
        out.print(tType);
        out.print(" getId(");
        out.print(strategy.getFullJavaClassName(table, Mode.POJO));
        out.println(" object) {");
        out.print("\t\treturn object.");
        out.print(strategy.getJavaGetterName(keyColumn, Mode.POJO));
        out.println("();");
        out.println("\t}");

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
        log.info("Generating table POJO", strategy.getFileName(table, Mode.POJO));

        GenerationWriter out = new GenerationWriter(strategy.getFile(table, Mode.POJO));
        printHeader(out, table, Mode.POJO);
        printClassJavadoc(out, table);
        printTableJPAAnnotation(out, table);

        out.print("public class ");
        out.print(strategy.getJavaClassName(table, Mode.POJO));
        printExtends(out, table, Mode.POJO);
        printImplements(out, table, Mode.POJO);
        out.print(" {");
        out.println();
        out.printSerial();

        out.println();

        int maxLength = 0;
        for (ColumnDefinition column : table.getColumns()) {
            maxLength = Math.max(maxLength, getJavaType(column.getType()).length());
        }

        for (ColumnDefinition column : table.getColumns()) {
            printColumnValidationAnnotation(out, column);

            out.print("\tprivate ");
            out.print(StringUtils.rightPad(getJavaType(column.getType()), maxLength));
            out.print(" ");
            out.print(convertToJavaIdentifier(strategy.getJavaMemberName(column, Mode.POJO)));
            out.println(";");
        }

        for (ColumnDefinition column : table.getColumns()) {

            // Getter
            out.println();
            printColumnJPAAnnotation(out, column);

            if (generateInterfaces()) {
                printOverride(out);
            }

            out.print("\tpublic ");
            out.print(getJavaType(column.getType()));
            out.print(" ");
            out.print(strategy.getJavaGetterName(column, Mode.POJO));
            out.println("() {");

            out.print("\t\treturn this.");
            out.print(convertToJavaIdentifier(strategy.getJavaMemberName(column, Mode.POJO)));
            out.println(";");
            out.println("\t}");

            // Setter
            out.println();

            if (generateInterfaces()) {
                printOverride(out);
            }

            out.print("\tpublic void ");
            out.print(strategy.getJavaSetterName(column, Mode.POJO));
            out.print("(");
            out.print(getJavaType(column.getType()));
            out.print(" ");
            out.print(convertToJavaIdentifier(strategy.getJavaMemberName(column, Mode.POJO)));
            out.println(") {");

            out.print("\t\tthis.");
            out.print(convertToJavaIdentifier(strategy.getJavaMemberName(column, Mode.POJO)));
            out.print(" = ");
            out.print(convertToJavaIdentifier(strategy.getJavaMemberName(column, Mode.POJO)));
            out.println(";");
            out.println("\t}");
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

        registerInSchema(database.getTables(schema), Table.class, true);
        watch.splitInfo("Tables generated");
    }

    protected void generateTable(SchemaDefinition schema, TableDefinition table) {
        log.info("Generating table", strategy.getFileName(table));

        GenerationWriter out = new GenerationWriter(strategy.getFile(table));
        printHeader(out, table);
        printClassJavadoc(out, table);

        Class<?> baseClass;
        if (generateRelations() && table.getMainUniqueKey() != null) {
            baseClass = UpdatableTableImpl.class;
        } else {
            baseClass = TableImpl.class;
        }

        out.print("public class ");
        out.print(strategy.getJavaClassName(table));
        out.print(" extends ");
        out.print(baseClass);
        out.print("<");
        out.print(strategy.getFullJavaClassName(table, Mode.RECORD));
        out.print(">");
        printImplements(out, table, Mode.DEFAULT);
        out.println(" {");
        out.printSerial();
        printSingletonInstance(table, out);
        printRecordTypeMethod(table, out);

        for (ColumnDefinition column : table.getColumns()) {
            printTableColumn(out, column, table);
        }

        // [#1255] With instance fields, the table constructor may
        // be public, as tables are no longer singletons
        out.println();
        if (generateInstanceFields()) {
            out.print("\tpublic ");
        }
        else {
            printNoFurtherInstancesAllowedJavadoc(out);
            out.print("\tprivate ");
        }
        out.println(strategy.getJavaClassName(table) + "() {");

        if (!schema.isDefaultSchema()) {
            out.println("\t\tsuper(\"" + table.getOutputName() + "\", " + strategy.getFullJavaIdentifier(schema) + ");");
        } else {
            out.println("\t\tsuper(\"" + table.getOutputName() + "\");");
        }

        out.println("\t}");

        // [#117] With instance fields, it makes sense to create a
        // type-safe table alias
        // [#1255] With instance fields, the table constructor may
        // be public, as tables are no longer singletons
        if (generateInstanceFields()) {
            out.println();
            out.print("\tpublic ");
            out.print(strategy.getJavaClassName(table));
            out.print("(");
            out.print(String.class);
            out.println(" alias) {");

            out.print("\t\tsuper(alias, ");
            out.print(strategy.getFullJavaIdentifier(schema));
            out.print(", ");
            out.print(strategy.getFullJavaIdentifier(table));
            out.println(");");
            out.println("\t}");
        }

        // Add primary / unique / foreign key information
        if (generateRelations()) {
            IdentityDefinition identity = table.getIdentity();

            // The identity column
            if (identity != null) {
                out.println();

                out.println("\t@Override");
                out.print("\tpublic ");
                out.print(Identity.class);
                out.print("<");
                out.print(strategy.getFullJavaClassName(table, Mode.RECORD));
                out.print(", ");
                out.print(getJavaType(identity.getColumn().getType()));
                out.println("> getIdentity() {");

                out.print("\t\treturn ");
                out.print(strategy.getFullJavaIdentifier(identity));
                out.println(";");

                out.println("\t}");
            }

            UniqueKeyDefinition mainKey = table.getMainUniqueKey();

            // The primary / main unique key
            if (mainKey != null) {
                out.println();

                out.println("\t@Override");
                out.print("\tpublic ");
                out.print(UniqueKey.class);
                out.print("<");
                out.print(strategy.getFullJavaClassName(table, Mode.RECORD));
                out.println("> getMainKey() {");

                out.print("\t\treturn ");
                out.print(strategy.getFullJavaIdentifier(mainKey));
                out.println(";");

                out.println("\t}");
            }

            // The remaining unique keys
            List<UniqueKeyDefinition> uniqueKeys = table.getUniqueKeys();
            if (uniqueKeys.size() > 0) {
                out.println();
                out.println("\t@Override");
                out.println("\t@SuppressWarnings(\"unchecked\")");

                out.print("\tpublic ");
                out.print(List.class);
                out.print("<");
                out.print(UniqueKey.class);
                out.print("<");
                out.print(strategy.getFullJavaClassName(table, Mode.RECORD));
                out.println(">> getKeys() {");

                out.print("\t\treturn ");
                out.print(Arrays.class);
                out.print(".<");
                out.print(UniqueKey.class);
                out.print("<");
                out.print(strategy.getFullJavaClassName(table, Mode.RECORD));
                out.print(">>asList(");

                String separator = "";
                for (UniqueKeyDefinition uniqueKey : uniqueKeys) {
                    out.print(separator);
                    out.print(strategy.getFullJavaIdentifier(uniqueKey));

                    separator = ", ";
                }

                out.println(");");
                out.println("\t}");
            }

            // Foreign keys
            List<ForeignKeyDefinition> foreignKeys = table.getForeignKeys();
            if (foreignKeys.size() > 0) {
                out.println();
                out.println("\t@Override");
                out.println("\t@SuppressWarnings(\"unchecked\")");

                out.print("\tpublic ");
                out.print(List.class);
                out.print("<");
                out.print(ForeignKey.class);
                out.print("<");
                out.print(strategy.getFullJavaClassName(table, Mode.RECORD));
                out.println(", ?>> getReferences() {");

                out.print("\t\treturn ");
                out.print(Arrays.class);
                out.print(".<");
                out.print(ForeignKey.class);
                out.print("<");
                out.print(strategy.getFullJavaClassName(table, Mode.RECORD));
                out.print(", ?>>asList(");

                String separator = "";
                for (ForeignKeyDefinition foreignKey : foreignKeys) {
                    TableDefinition referencedTable = foreignKey.getReferencedTable();

                    // Skip master data foreign keys
                    if (referencedTable instanceof MasterDataTableDefinition) {
                        continue;
                    }

                    out.print(separator);
                    out.print(strategy.getFullJavaIdentifier(foreignKey));

                    separator = ", ";
                }

                out.println(");");
                out.println("\t}");
            }
        }

        // [#117] With instance fields, it makes sense to create a
        // type-safe table alias
        if (generateInstanceFields()) {
            out.println();
            out.println("\t@Override");
            out.print("\tpublic ");
            out.print(strategy.getFullJavaClassName(table));
            out.print(" as(");
            out.print(String.class);
            out.println(" alias) {");

            out.print("\t\treturn new ");
            out.print(strategy.getFullJavaClassName(table));
            out.println("(alias);");
            out.println("\t}");
        }

        out.printStaticInitialisationStatementsPlaceholder();
        out.println("}");
        out.close();
    }

    protected void generateMasterTables(SchemaDefinition schema) {
        log.info("Generating master data");

        for (MasterDataTableDefinition table : database.getMasterDataTables(schema)) {
            try {
                generateMasterTable(table);
            } catch (Exception e) {
                log.error("Exception while generating master data table " + table, e);
            }
        }

        watch.splitInfo("Master data generated");
    }

    protected void generateMasterTable(MasterDataTableDefinition table) {
        log.info("Generating table", strategy.getFileName(table));

        GenerationWriter out = new GenerationWriter(strategy.getFile(table));
        printHeader(out, table);
        printClassJavadoc(out, table);

        ColumnDefinition pk = table.getPrimaryKeyColumn();
        ColumnDefinition l = table.getLiteralColumn();
        ColumnDefinition d = table.getDescriptionColumn();

        Result<Record> data = table.getData();

        out.print("public enum ");
        out.print(strategy.getJavaClassName(table));
        printImplements(out, table, Mode.ENUM,
            MasterDataType.class.getName() + "<" + getJavaType(pk.getType()) + ">");
        out.println(" {");

        Set<ColumnDefinition> columns =
            new LinkedHashSet<ColumnDefinition>(Arrays.asList(pk, l, d));


        for (Record record : data) {
            String literal = record.getValueAsString(l.getName());
            String description = record.getValueAsString(d.getName());

            if (!StringUtils.isEmpty(description)) {
                out.println();
                out.println("\t/**");
                out.println("\t * " + description);
                out.println("\t */");
            }

            out.print("\t");
            out.print(GenerationUtil.convertToJavaIdentifier(literal));
            out.print("(");

            String separator = "";
            for (ColumnDefinition column : columns) {
                out.print(separator);
                out.printNewJavaObject(getJavaType(column.getType()), record.getValue(column.getName()));

                separator = ", ";
            }

            out.println("),");
        }

        out.println("\t;");
        out.println();

        // Fields
        for (ColumnDefinition column : columns) {
            out.print("\tprivate final ");
            out.print(getJavaType(column.getType()));
            out.print(" ");
            out.println(strategy.getJavaMemberName(column) + ";");
        }

        // Constructor
        out.println();
        out.print("\tprivate " + strategy.getJavaClassName(table) + "(");

        String separator = "";
        for (ColumnDefinition column : columns) {
            out.print(separator);
            out.print(getJavaType(column.getType()));
            out.print(" ");
            out.print(strategy.getJavaMemberName(column));

            separator = ", ";
        }

        out.println(") {");
        for (ColumnDefinition column : columns) {
            out.print("\t\tthis.");
            out.print(strategy.getJavaMemberName(column));
            out.print(" = ");
            out.print(strategy.getJavaMemberName(column));
            out.println(";");
        }
        out.println("\t}");

        // Implementation methods
        out.println();
        printOverride(out);
        out.print("\tpublic ");
        out.print(getJavaType(pk.getType()));
        out.println(" getPrimaryKey() {");
        out.println("\t\treturn " + strategy.getJavaMemberName(pk) + ";");
        out.println("\t}");

        // Getters
        for (ColumnDefinition column : columns) {
            printFieldJavaDoc(out, column);
            out.print("\tpublic final ");
            out.print(getJavaType(column.getType()));
            out.print(" ");
            out.print(strategy.getJavaGetterName(column, Mode.DEFAULT));
            out.println("() {");
            out.print("\t\treturn ");
            out.print(strategy.getJavaMemberName(column));
            out.println(";");
            out.println("\t}");
        }

        out.println("}");
        out.close();
    }

    protected void generateSequences(SchemaDefinition schema, File targetSchemaDir) {
        log.info("Generating sequences");

        GenerationWriter out = new GenerationWriter(new File(targetSchemaDir, "Sequences.java"));
        printHeader(out, schema);
        printClassJavadoc(out, "Convenience access to all sequences in " + schema.getOutputName());
        out.println("public final class Sequences {");

        for (SequenceDefinition sequence : database.getSequences(schema)) {
            out.println();
            out.println("\t/**");
            out.println("\t * The sequence " + sequence.getQualifiedOutputName());
            out.println("\t */");

            out.print("\tpublic static final ");
            out.print(Sequence.class);
            out.print("<");
            out.print(getJavaType(sequence.getType()));
            out.print(">");
            out.print(" ");
            out.print(strategy.getJavaIdentifier(sequence));
            out.print(" = new ");
            out.print(SequenceImpl.class);
            out.print("<");
            out.print(getJavaType(sequence.getType()));
            out.print(">");
            out.print("(\"");
            out.print(sequence.getOutputName());
            out.print("\"");

            if (!schema.isDefaultSchema()) {
                out.print(", ");
                out.print(strategy.getFullJavaIdentifier(schema));
            } else {
                out.print(", null");
            }

            out.print(", ");
            out.print(getJavaTypeReference(sequence.getDatabase(), sequence.getType()));

            out.println(");");
        }

        printPrivateConstructor(out, "Sequences");
        out.println("}");
        out.close();

        registerInSchema(database.getSequences(schema), Sequence.class, true);
        watch.splitInfo("Sequences generated");
    }

    protected void generateSchema(SchemaDefinition schema) {
        log.info("Generating schema", strategy.getFileName(schema));
        log.info("----------------------------------------------------------");

        outS = new GenerationWriter(strategy.getFile(schema));
        printHeader(outS, schema);
        printClassJavadoc(outS, schema);

        outS.print("public class ");
        outS.print(strategy.getJavaClassName(schema));
        outS.print(" extends ");
        outS.print(SchemaImpl.class);
        printImplements(outS, schema, Mode.DEFAULT);
        outS.println(" {");
        outS.printSerial();
        outS.println();
        outS.println("\t/**");
        outS.println("\t * The singleton instance of " + schema.getQualifiedOutputName());
        outS.println("\t */");
        outS.println("\tpublic static final " + strategy.getJavaClassName(schema) + " " + strategy.getJavaIdentifier(schema) + " = new " + strategy.getJavaClassName(schema) + "();");

        outS.println();
        printNoFurtherInstancesAllowedJavadoc(outS);
        outS.println("\tprivate " + strategy.getJavaClassName(schema) + "() {");
        outS.println("\t\tsuper(\"" + schema.getOutputName() + "\");");
        outS.println("\t}");

        outS.printInitialisationStatementsPlaceholder();

        // Generating the factory
        // -----------------------------------------------------------------
        log.info("Generating factory", strategy.getFileName(schema, Mode.FACTORY));

        outF = new GenerationWriter(strategy.getFile(schema, Mode.FACTORY));
        printHeader(outF, schema);
        printClassJavadoc(outF,
            "A Factory for specific use with the <code>" + schema.getOutputName() +
            "</code> schema.\n<p>\nThis Factory will not render the <code>" + schema.getOutputName() +
            "</code> schema's schema name in rendered SQL (assuming that you use it as the default schema on your connection!). Use the more generic {@link " +
            database.getDialect().getFactory().getName() +
            "} or the {@link " + Factory.class.getName() + "} instead, if you want to fully qualify tables, routines, etc.");

        outF.print("public class ");
        outF.print(strategy.getJavaClassName(schema, Mode.FACTORY));
        outF.print(" extends ");
        outF.print(database.getDialect().getFactory());
        printImplements(outF, schema, Mode.FACTORY);
        outF.println(" {");
        outF.printSerial();

        outF.println();
        outF.println("\t/**");
        outF.println("\t * Create a factory with a connection");
        outF.println("\t *");
        outF.println("\t * @param connection The connection to use with objects created from this factory");
        outF.println("\t */");
        outF.print("\tpublic ");
        outF.print(strategy.getJavaClassName(schema, Mode.FACTORY));
        outF.print("(");
        outF.print(Connection.class);
        outF.println(" connection) {");
        outF.println("\t\tsuper(connection);");
        outF.println();
        outF.println("\t\tinitDefaultSchema();");
        outF.println("\t}");

        if (generateDeprecated()) {
            outF.println();
            outF.println("\t/**");
            outF.println("\t * Create a factory with a connection and a schema mapping");
            outF.println("\t * ");
            outF.print("\t * @deprecated - 2.0.5 - Use {@link #");
            outF.print(strategy.getJavaClassName(schema, Mode.FACTORY));
            outF.print("(");
            outF.print(Connection.class);
            outF.print(", ");
            outF.print(Settings.class);
            outF.println(")} instead");
            outF.println("\t */");
            outF.println("\t@Deprecated");
            outF.print("\tpublic ");
            outF.print(strategy.getJavaClassName(schema, Mode.FACTORY));
            outF.print("(");
            outF.print(Connection.class);
            outF.println(" connection, org.jooq.SchemaMapping mapping) {");
            outF.println("\t\tsuper(connection, mapping);");
            outF.println();
            outF.println("\t\tinitDefaultSchema();");
            outF.println("\t}");
        }

        outF.println();
        outF.println("\t/**");
        outF.println("\t * Create a factory with a connection and some settings");
        outF.println("\t *");
        outF.println("\t * @param connection The connection to use with objects created from this factory");
        outF.println("\t * @param settings The settings to apply to objects created from this factory");
        outF.println("\t */");
        outF.print("\tpublic ");
        outF.print(strategy.getJavaClassName(schema, Mode.FACTORY));
        outF.print("(");
        outF.print(Connection.class);
        outF.print(" connection, ");
        outF.print(Settings.class);
        outF.println(" settings) {");
        outF.println("\t\tsuper(connection, settings);");
        outF.println();
        outF.println("\t\tinitDefaultSchema();");
        outF.println("\t}");

        // [#1315] schema-specific factories override the default schema
        outF.println();
        outF.println("\t/**");
        outF.println("\t * Initialise the render mapping's default schema.");
        outF.println("\t * <p>");
        outF.println("\t * For convenience, this schema-specific factory should override any pre-existing setting");
        outF.println("\t */");
        outF.println("\tprivate final void initDefaultSchema() {");
        outF.print("\t\t");
        outF.print(SettingsTools.class);
        outF.print(".getRenderMapping(getSettings()).setDefaultSchema(");
        outF.print(strategy.getFullJavaIdentifier(schema));
        outF.println(".getName());");
        outF.println("\t}");

        watch.splitInfo("Schema generated");
    }

    protected void printExtends(GenerationWriter out, Definition definition, Mode mode) {
        String superclass = strategy.getJavaClassExtends(definition, mode);

        if (!StringUtils.isBlank(superclass)) {
            out.print(" extends ");
            out.print(superclass);
        }
    }

    protected void printImplements(GenerationWriter out, Definition definition, Mode mode, String... forcedInterfaces) {
        List<String> interfaces = strategy.getJavaClassImplements(definition, mode);

        interfaces = new ArrayList<String>(interfaces == null ? Collections.<String>emptyList() : interfaces);
        interfaces.addAll(Arrays.asList(forcedInterfaces));

        if (generateInterfaces() &&
            asList(Mode.POJO, Mode.RECORD).contains(mode) &&
            definition instanceof TableDefinition) {

            interfaces.add(strategy.getFullJavaClassName(definition, Mode.INTERFACE));
        }

        if (!interfaces.isEmpty()) {
            String glue;

            if (mode == Mode.INTERFACE) {
                glue = " extends ";
            }
            else {
                glue = " implements ";
            }

            // Avoid duplicates
            for (String i : new LinkedHashSet<String>(interfaces)) {
                if (!StringUtils.isBlank(i)) {
                    out.print(glue);
                    out.print(i);

                    glue = ", ";
                }
            }
        }
    }

    protected void printTableJPAAnnotation(GenerationWriter out, TableDefinition table) {
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
                out.println(sb);
                out.print("}");
            }

            out.println(")");
        }
    }

    protected void printColumnJPAAnnotation(GenerationWriter out, ColumnDefinition column) {
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

    protected void printColumnValidationAnnotation(GenerationWriter out, ColumnDefinition column) {
        if (generateValidationAnnotations()) {
            DataTypeDefinition type = column.getType();

            boolean newline = true;
            if (!column.isNullable()) {
                newline = out.println(newline);
                out.println("\t@javax.validation.constraints.NotNull");
            }

            if ("java.lang.String".equals(getJavaType(type))) {
                int length = type.getLength();

                if (length > 0) {
                    newline = out.println(newline);
                    out.print("\t@javax.validation.constraints.Size(max = ");
                    out.print(length);
                    out.println(")");
                }
            }
        }
    }

    protected void registerInSchema(List<? extends Definition> definitions, Class<?> type, boolean isGeneric) {
        if (outS != null) {
            outS.println();
            printOverride(outS);
            outS.print("\tpublic final ");
            outS.print(List.class);
            outS.print("<");
            outS.print(type);

            if (isGeneric) {
                outS.print("<?>");
            }

            outS.print("> get");
            outS.print(type.getSimpleName());
            outS.println("s() {");

            outS.print("\t\treturn ");
            outS.print(Arrays.class);
            outS.print(".<");
            outS.print(type);

            if (isGeneric) {
                outS.print("<?>");
            }

            outS.print(">asList(");

            if (definitions.size() > 1) {
                outS.print("\n\t\t\t");
            }

            for (int i = 0; i < definitions.size(); i++) {
                Definition def = definitions.get(i);

                if (i > 0) {
                    outS.print(",\n\t\t\t");
                }

                printSingletonReference(outS, def);
            }

            outS.println(");");
            outS.println("\t}");
        }
    }

    protected void printRoutine(SchemaDefinition schema, RoutineDefinition routine) {
        log.info("Generating routine", strategy.getFileName(routine));

        GenerationWriter out = new GenerationWriter(strategy.getFile(routine));
        printHeader(out, routine);
        printClassJavadoc(out, routine);

        out.print("public class ");
        out.print(strategy.getJavaClassName(routine));
        out.print(" extends ");
        out.print(AbstractRoutine.class);
        out.print("<");

        if (routine.getReturnValue() == null) {
            out.print(Void.class);
        }
        else {
            out.print(getJavaType(routine.getReturnType()));
        }

        out.print(">");
        printImplements(out, routine, Mode.DEFAULT);
        out.println(" {");
        out.printSerial();
        out.println();

        for (ParameterDefinition parameter : routine.getAllParameters()) {
            printParameter(out, parameter, routine);
        }

        out.println();
        printJavadoc(out, "Create a new routine call instance");
        out.println("\tpublic " + strategy.getJavaClassName(routine) + "() {");
        out.print("\t\tsuper(");
        out.print("\"");
        out.print(routine.getName());
        out.print("\", ");
        out.print(strategy.getFullJavaIdentifier(schema));

        if (routine.getPackage() != null) {
            out.print(", ");
            out.print(strategy.getFullJavaIdentifier(routine.getPackage()));
        }

        if (routine.getReturnValue() != null) {
            out.print(", ");
            out.print(getJavaTypeReference(database, routine.getReturnType()));
        }

        out.println(");");

        if (routine.getAllParameters().size() > 0) {
            out.println();
        }

        for (ParameterDefinition parameter : routine.getAllParameters()) {
            out.print("\t\t");

            if (parameter.equals(routine.getReturnValue())) {
                out.println("setReturnParameter(" + strategy.getJavaIdentifier(parameter) + ");");
            }
            else if (routine.getInParameters().contains(parameter)) {
                if (routine.getOutParameters().contains(parameter)) {
                    out.println("addInOutParameter(" + strategy.getJavaIdentifier(parameter) + ");");
                }
                else {
                    out.println("addInParameter(" + strategy.getJavaIdentifier(parameter) + ");");
                }
            }
            else {
                out.println("addOutParameter(" + strategy.getJavaIdentifier(parameter) + ");");
            }
        }

        if (routine.getOverload() != null) {
            out.println("\t\tsetOverloaded(true);");
        }

        out.println("\t}");

        for (ParameterDefinition parameter : routine.getInParameters()) {
            out.println();
            out.println("\t/**");
            out.println("\t * Set the <code>" + parameter.getOutputName() + "</code> parameter to the routine");
            out.println("\t */");
            out.print("\tpublic void ");
            out.print(strategy.getJavaSetterName(parameter, Mode.DEFAULT));
            out.print("(");
            printNumberType(out, parameter.getType());
            out.println(" value) {");

            out.print("\t\tset");
            if (parameter.getType().isGenericNumberType()) {
                out.print("Number");
            }
            else {
                out.print("Value");
            }
            out.print("(");
            out.print(strategy.getJavaIdentifier(parameter));
            out.println(", value);");
            out.println("\t}");

            if (routine.isSQLUsable()) {
                out.println();
                out.println("\t/**");
                out.println("\t * Set the <code>" + parameter.getOutputName() + "</code> parameter to the function");
                out.println("\t * <p>");
                out.print("\t * Use this method only, if the function is called as a {@link ");
                out.print(Field.class);
                out.print("} in a {@link ");
                out.print(Select.class);
                out.println("} statement!");
                out.println("\t */");
                out.print("\tpublic void ");
                out.print(strategy.getJavaSetterName(parameter, Mode.DEFAULT));
                out.print("(");
                out.print(Field.class);
                out.print("<");
                printExtendsNumberType(out, parameter.getType());
                out.println("> field) {");

                out.print("\t\tset");
                if (parameter.getType().isGenericNumberType()) {
                    out.print("Number");
                }
                else {
                    out.print("Field");
                }
                out.print("(");
                out.print(strategy.getJavaIdentifier(parameter));
                out.println(", field);");
                out.println("\t}");
            }
        }

        for (ParameterDefinition parameter : routine.getAllParameters()) {
            boolean isReturnValue = parameter.equals(routine.getReturnValue());
            boolean isOutParameter = routine.getOutParameters().contains(parameter);

            if (isOutParameter && !isReturnValue) {
                out.println();
                out.print("\tpublic ");
                out.print(getJavaType(parameter.getType()));
                out.print(" ");
                out.print(strategy.getJavaGetterName(parameter, Mode.DEFAULT));
                out.println("() {");

                out.print("\t\treturn getValue(");
                out.print(strategy.getJavaIdentifier(parameter));
                out.println(");");
                out.println("\t}");
            }
        }

        out.println("}");
        out.close();
    }

    protected void printConvenienceMethodFunctionAsField(GenerationWriter out, RoutineDefinition function, boolean parametersAsField) {
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

        out.println();
        out.println("\t/**");
        out.println("\t * Get " + function.getQualifiedOutputName() + " as a field");
        out.println("\t *");

        for (ParameterDefinition parameter : function.getInParameters()) {
            out.println("\t * @param " + strategy.getJavaMemberName(parameter));
        }

        out.println("\t */");
        out.print("\tpublic static ");
        out.print(Field.class);
        out.print("<");
        out.print(getJavaType(function.getReturnType()));
        out.print("> ");
        out.print(strategy.getJavaMethodName(function, Mode.DEFAULT));
        out.print("(");

        String separator = "";
        for (ParameterDefinition parameter : function.getInParameters()) {
            out.print(separator);

            if (parametersAsField) {
                out.print(Field.class);
                out.print("<");
                printExtendsNumberType(out, parameter.getType());
                out.print(">");
            } else {
                printNumberType(out, parameter.getType());
            }

            out.print(" ");
            out.print(strategy.getJavaMemberName(parameter));

            separator = ", ";
        }

        out.println(") {");
        out.print("\t\t");
        out.print(strategy.getFullJavaClassName(function));
        out.print(" f = new ");
        out.print(strategy.getFullJavaClassName(function));
        out.println("();");

        for (ParameterDefinition parameter : function.getInParameters()) {
            out.print("\t\tf.");
            out.print(strategy.getJavaSetterName(parameter, Mode.DEFAULT));
            out.print("(");
            out.print(strategy.getJavaMemberName(parameter));
            out.println(");");
        }

        out.println();
        out.println("\t\treturn f.asField();");
        out.println("\t}");
    }

    protected void printConvenienceMethodFunction(GenerationWriter out, RoutineDefinition function, boolean instance) {
        // [#281] - Java can't handle more than 255 method parameters
        if (function.getInParameters().size() > 254) {
            log.warn("Too many parameters", "Function " + function + " has more than 254 in parameters. Skipping generation of convenience method.");
            return;
        }

        out.println();
        out.println("\t/**");
        out.println("\t * Call " + function.getQualifiedOutputName());
        out.println("\t *");

        for (ParameterDefinition parameter : function.getInParameters()) {
            out.println("\t * @param " + strategy.getJavaMemberName(parameter));
        }

        printThrowsDataAccessException(out);
        out.println("\t */");
        out.print("\tpublic ");

        if (!instance) {
            out.print("static ");
        }

        out.print(getJavaType(function.getReturnType()));
        out.print(" ");
        out.print(strategy.getJavaMethodName(function, Mode.DEFAULT));
        out.print("(");

        String glue = "";
        if (!instance) {
            out.print(Configuration.class);
            out.print(" configuration");
            glue = ", ";
        }

        for (ParameterDefinition parameter : function.getInParameters()) {
            // Skip SELF parameter
            if (instance && parameter.equals(function.getInParameters().get(0))) {
                continue;
            }

            out.print(glue);
            printNumberType(out, parameter.getType());
            out.print(" ");
            out.print(strategy.getJavaMemberName(parameter));

            glue = ", ";
        }

        out.println(") {");
        out.print("\t\t");
        out.print(strategy.getFullJavaClassName(function));
        out.print(" f = new ");
        out.print(strategy.getFullJavaClassName(function));
        out.println("();");

        for (ParameterDefinition parameter : function.getInParameters()) {
            out.print("\t\tf.");
            out.print(strategy.getJavaSetterName(parameter, Mode.DEFAULT));
            out.print("(");

            if (instance && parameter.equals(function.getInParameters().get(0))) {
                out.print("this");
            }
            else {
                out.print(strategy.getJavaMemberName(parameter));
            }

            out.println(");");
        }

        out.println();
        out.print("\t\tf.execute(");

        if (instance) {
            out.print("getConfiguration()");
        }
        else {
            out.print("configuration");
        }

        out.println(");");

        // TODO [#956] Find a way to register "SELF" as OUT parameter
        // in case this is a UDT instance (member) function

        out.println("\t\treturn f.getReturnValue();");
        out.println("\t}");
    }

    protected void printThrowsDataAccessException(GenerationWriter out) {
        out.print("\t * @throws ");
        out.print(DataAccessException.class);
        out.print(" if something went wrong executing the query");
        out.println();
    }

    protected void printPrivateConstructor(GenerationWriter out, String javaClassName) {
        out.println();
        out.println("\t/**");
        out.println("\t * No instances");
        out.println("\t */");
        out.println("\tprivate " + javaClassName + "() {}");
    }

    protected void printConvenienceMethodProcedure(GenerationWriter out, RoutineDefinition procedure, boolean instance) {
        // [#281] - Java can't handle more than 255 method parameters
        if (procedure.getInParameters().size() > 254) {
            log.warn("Too many parameters", "Procedure " + procedure + " has more than 254 in parameters. Skipping generation of convenience method.");
            return;
        }

        out.println();
        out.println("\t/**");
        out.println("\t * Call " + procedure.getQualifiedOutputName());
        out.println("\t *");

        for (ParameterDefinition parameter : procedure.getAllParameters()) {
            out.print("\t * @param " + strategy.getJavaMemberName(parameter) + " ");

            if (procedure.getInParameters().contains(parameter)) {
                if (procedure.getOutParameters().contains(parameter)) {
                    out.println("IN OUT parameter");
                } else {
                    out.println("IN parameter");
                }
            } else {
                out.println("OUT parameter");
            }
        }

        printThrowsDataAccessException(out);
        out.println("\t */");
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
            out.print(strategy.getFullJavaClassName(procedure) + " ");
        }

        out.print(strategy.getJavaMethodName(procedure, Mode.DEFAULT));
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
            printNumberType(out, parameter.getType());
            out.print(" ");
            out.print(strategy.getJavaMemberName(parameter));

            glue = ", ";
        }

        out.println(") {");
        out.print("\t\t");
        out.print(strategy.getFullJavaClassName(procedure));
        out.print(" p = new ");
        out.print(strategy.getFullJavaClassName(procedure));
        out.println("();");

        for (ParameterDefinition parameter : procedure.getInParameters()) {
            out.print("\t\tp.");
            out.print(strategy.getJavaSetterName(parameter, Mode.DEFAULT));
            out.print("(");

            if (instance && parameter.equals(procedure.getInParameters().get(0))) {
                out.print("this");
            }
            else {
                out.print(strategy.getJavaMemberName(parameter));
            }

            out.println(");");
        }

        out.println();
        out.print("\t\tp.execute(");

        if (instance) {
            out.print("getConfiguration()");
        }
        else {
            out.print("configuration");
        }

        out.println(");");

        if (procedure.getOutParameters().size() > 0) {
            if (instance) {
                out.print("\t\tfrom(p.");
                out.print(strategy.getJavaGetterName(procedure.getOutParameters().get(0), Mode.DEFAULT));
                out.println("());");
            }

            if (procedure.getOutParameters().size() == 1) {
                out.print("\t\treturn p.");
                out.print(strategy.getJavaGetterName(procedure.getOutParameters().get(0), Mode.DEFAULT));
                out.println("();");
            }
            else if (procedure.getOutParameters().size() > 1) {
                out.println("\t\treturn p;");
            }
        }

        out.println("\t}");
    }

    protected void printRecordTypeMethod(Definition definition, GenerationWriter out) {
        out.println();
        out.println("\t/**");
        out.println("\t * The class holding records for this type");
        out.println("\t */");
        printOverride(out);
        out.print("\tpublic ");
        out.print(Class.class);
        out.print("<");
        out.print(strategy.getFullJavaClassName(definition, Mode.RECORD));
        out.println("> getRecordType() {");
        out.print("\t\treturn ");
        out.print(strategy.getFullJavaClassName(definition, Mode.RECORD));
        out.println(".class;");
        out.println("\t}");
    }

    protected void printSingletonInstance(Definition definition, GenerationWriter out) {
        out.println();
        out.println("\t/**");
        out.println("\t * The singleton instance of " + definition.getQualifiedOutputName());
        out.println("\t */");
        out.print("\tpublic static final ");
        out.print(strategy.getFullJavaClassName(definition));
        out.print(" ");
        out.print(strategy.getJavaIdentifier(definition));
        out.print(" = new ");
        out.print(strategy.getFullJavaClassName(definition));
        out.println("();");
    }

    protected void printSingletonReference(GenerationWriter out, Definition definition) {
        if (definition instanceof SequenceDefinition) {
            out.print(strategy.getJavaPackageName(definition.getSchema()));
            out.print(".");
            out.print("Sequences");
            out.print(".");
            out.print(strategy.getJavaIdentifier(definition));
        }
        else {
            out.print(strategy.getFullJavaIdentifier(definition));
        }
    }

    protected void printOverride(GenerationWriter out) {
        out.println("\t@Override");
    }

    /**
     * If file is a directory, recursively empty its children.
     * If file is a file, delete it
     */
    protected void empty(File file) {
        if (file != null) {
            if (file.isDirectory()) {
                File[] children = file.listFiles();

                if (children != null) {
                    for (File child : children) {
                        empty(child);
                    }
                }
            } else {
                if (file.getName().endsWith(".java")) {
                    file.delete();
                }
            }
        }
    }

    protected void printGetterAndSetter(GenerationWriter out, TypedElementDefinition<?> element) {
        printGetterAndSetter(out, element, true);
    }

    protected void printGetterAndSetter(GenerationWriter out, TypedElementDefinition<?> element, boolean printBody) {
        printFieldJavaDoc(out, element);

        if (printBody && generateInterfaces() && element instanceof ColumnDefinition) {
            printOverride(out);
        }

        printSetter(out, element, printBody);

        printFieldJavaDoc(out, element);
        if (element instanceof ColumnDefinition) {
            printColumnJPAAnnotation(out, (ColumnDefinition) element);
        }

        if (printBody && generateInterfaces() && element instanceof ColumnDefinition) {
            printOverride(out);
        }

        printGetter(out, element, printBody);

        if (printBody &&
            generateRelations() &&
            generateNavigationMethods() &&
            element instanceof ColumnDefinition) {

            ColumnDefinition column = (ColumnDefinition) element;

            generateNavigateMethods(out, column);
        }
    }

    protected void printSetter(GenerationWriter out, TypedElementDefinition<?> element, boolean printBody) {
        out.print("\tpublic void ");
        out.print(strategy.getJavaSetterName(element, Mode.DEFAULT));
        out.print("(");
        out.print(getJavaType(element.getType()));
        out.print(" value)");

        if (printBody) {
            out.println(" {");
            out.println("\t\tsetValue(" + strategy.getFullJavaIdentifier(element) + ", value);");
            out.println("\t}");
        }
        else {
            out.println(";");
        }
    }

    protected void printGetter(GenerationWriter out, TypedElementDefinition<?> element, boolean printBody) {
        out.print("\tpublic ");
        out.print(getJavaType(element.getType()));
        out.print(" ");
        out.print(strategy.getJavaGetterName(element, Mode.DEFAULT));
        out.print("()");

        if (printBody) {
            out.println(" {");
            out.println("\t\treturn getValue(" + strategy.getFullJavaIdentifier(element) + ");");
            out.println("\t}");
        }
        else {
            out.println(";");
        }
    }

    protected void generateNavigateMethods(GenerationWriter out, ColumnDefinition column) {

        List<UniqueKeyDefinition> uniqueKeys = column.getUniqueKeys();

        // Print references from this column's unique keys to all
        // corresponding foreign keys.

        // e.g. in TAuthorRecord, print getTBooks()
        // -----------------------------------------------------------------
        Set<String> fetchMethodNames = new HashSet<String>();
        for (UniqueKeyDefinition uniqueKey : uniqueKeys) {
            if (out.printOnlyOnce(uniqueKey)) {
                for (ForeignKeyDefinition foreignKey : uniqueKey.getForeignKeys()) {
                    generateFetchFKList(out, uniqueKey, foreignKey, column, fetchMethodNames);
                }
            }
        }

        // Print references from this foreign key to its related primary key
        // E.g. in TBookRecord print getTAuthor()
        // -----------------------------------------------------------------
        ForeignKeyDefinition foreignKey = column.getForeignKey();
        if (foreignKey != null && out.printOnlyOnce(foreignKey)) {
            generateFetchFK(out, column, foreignKey);
        }
    }

    protected void generateFetchFK(GenerationWriter out, ColumnDefinition column, ForeignKeyDefinition foreignKey) {

        // #64 - If the foreign key does not match the referenced key, it
        // is most likely because it references a non-primary unique key
        // Skip code generation for this foreign key

        // #69 - Should resolve this issue more thoroughly.
        if (foreignKey.getReferencedColumns().size() != foreignKey.getKeyColumns().size()) {
            log.warn("Foreign key mismatch", foreignKey.getName() + " does not match its primary key! No code is generated for this key. See trac tickets #64 and #69");
            return;
        }

        // Do not generate referential code for master data tables
        TableDefinition referenced = foreignKey.getReferencedTable();
        if (referenced instanceof MasterDataTableDefinition) {
            return;
        }

        printFetchMethod(out, column, foreignKey, referenced);
    }

    protected void printFetchMethod(GenerationWriter out, ColumnDefinition column, ForeignKeyDefinition foreignKey,
        TableDefinition referenced) {

        printFieldJavaDoc(out, column);

        out.print("\tpublic ");
        out.print(strategy.getFullJavaClassName(referenced, Mode.RECORD));
        out.print(" fetch");
        out.print(strategy.getJavaClassName(referenced));

        // #350 - Disambiguate multiple foreign keys referencing
        // the same table
        if (foreignKey.countSimilarReferences() > 1) {
            out.print("By");
            out.print(strategy.getJavaClassName(column));
        }

        out.println("() {");
        out.println("\t\treturn create()");
        out.print("\t\t\t.selectFrom(");
        out.print(strategy.getFullJavaIdentifier(referenced));
        out.println(")");

        String connector = "\t\t\t.where(";

        for (int i = 0; i < foreignKey.getReferencedColumns().size(); i++) {
            out.print(connector);
            out.print(strategy.getFullJavaIdentifier(foreignKey.getReferencedColumns().get(i)));
            out.print(".equal(getValue");

            DataTypeDefinition foreignType = foreignKey.getKeyColumns().get(i).getType();
            DataTypeDefinition primaryType = foreignKey.getReferencedColumns().get(i).getType();

            // Convert foreign key value, if there is a type mismatch
            if (!match(foreignType, primaryType)) {
                out.print("As");
                out.print(getSimpleJavaType(foreignKey.getReferencedColumns().get(i).getType()));
            }

            out.print("(");
            out.print(strategy.getFullJavaIdentifier(foreignKey.getKeyColumns().get(i)));
            out.println(")))");

            connector = "\t\t\t.and(";
        }

        out.println("\t\t\t.fetchOne();");
        out.println("\t}");
    }

    private void generateFetchFKList(GenerationWriter out, UniqueKeyDefinition uniqueKey, ForeignKeyDefinition foreignKey, ColumnDefinition column, Set<String> fetchMethodNames) {
        // #64 - If the foreign key does not match the referenced key, it
        // is most likely because it references a non-primary unique key
        // Skip code generation for this foreign key

        // #69 - Should resolve this issue more thoroughly.
        if (foreignKey.getReferencedColumns().size() != foreignKey.getKeyColumns().size()) {
            log.warn("Foreign key mismatch", foreignKey.getName() + " does not match its primary key! No code is generated for this key. See trac tickets #64 and #69");
            return;
        }

        TableDefinition referencing = foreignKey.getKeyTable();

        StringBuilder fetchMethodName = new StringBuilder();
        fetchMethodName.append("fetch");
        fetchMethodName.append(strategy.getJavaClassName(referencing));

        // #352 - Disambiguate foreign key navigation directions
        fetchMethodName.append("List");

        // #350 - Disambiguate multiple foreign keys referencing
        // the same table
        if (foreignKey.countSimilarReferences() > 1) {
            fetchMethodName.append("By");
            fetchMethodName.append(strategy.getJavaClassName(foreignKey.getKeyColumns().get(0)));
        }

        // #1270 - Disambiguate identical foreign keys
        if (fetchMethodNames.contains(fetchMethodName.toString())) {
            log.warn("Duplicate foreign key", foreignKey.getName() + " has the same properties as another foreign key! No code is generated for this key. See trac ticket #1270");
            return;
        }
        else {
            fetchMethodNames.add(fetchMethodName.toString());
        }

        printFieldJavaDoc(out, column);
        out.print("\tpublic ");
        out.print(List.class);
        out.print("<");
        out.print(strategy.getFullJavaClassName(referencing, Mode.RECORD));
        out.print("> ");
        out.print(fetchMethodName);

        out.println("() {");
        out.println("\t\treturn create()");
        out.print("\t\t\t.selectFrom(");
        out.print(strategy.getFullJavaIdentifier(referencing));
        out.println(")");

        String connector = "\t\t\t.where(";

        for (int i = 0; i < foreignKey.getReferencedColumns().size(); i++) {
            out.print(connector);
            out.print(strategy.getFullJavaIdentifier(foreignKey.getKeyColumns().get(i)));
            out.print(".equal(getValue");

            DataTypeDefinition foreignType = foreignKey.getKeyColumns().get(i).getType();
            DataTypeDefinition primaryType = uniqueKey.getKeyColumns().get(i).getType();

            // Convert foreign key value, if there is a type mismatch
            if (!match(foreignType, primaryType)) {
                out.print("As");
                out.print(getSimpleJavaType(foreignKey.getKeyColumns().get(i).getType()));
            }

            out.print("(");
            out.print(strategy.getFullJavaIdentifier(uniqueKey.getKeyColumns().get(i)));
            out.println(")))");

            connector = "\t\t\t.and(";
        }

        out.println("\t\t\t.fetch();");
        out.println("\t}");
    }

    protected void printUDTColumn(GenerationWriter out, AttributeDefinition attribute, Definition table) {
        Class<?> declaredMemberClass = UDTField.class;
        printColumnDefinition(out, attribute, table, declaredMemberClass);
    }

    protected void printTableColumn(GenerationWriter out, ColumnDefinition column, Definition table) {
        Class<?> declaredMemberClass = TableField.class;
        printColumnDefinition(out, column, table, declaredMemberClass);
    }

    protected void printParameter(GenerationWriter out, ParameterDefinition parameter, Definition proc) {
        printColumnDefinition(out, parameter, proc, Parameter.class);
    }

    protected void printColumnDefinition(GenerationWriter out, TypedElementDefinition<?> column, Definition type, Class<?> declaredMemberClass) {
        printFieldJavaDoc(out, column);

        boolean hasType =
            type instanceof TableDefinition ||
            type instanceof UDTDefinition;

        boolean isDefaulted =
            column instanceof ParameterDefinition &&
            ((ParameterDefinition) column).isDefaulted();

        if (type instanceof TableDefinition) {
            if (generateInstanceFields()) {
                out.print("\tpublic final ");
            }
            else {
                out.print("\tpublic static final ");
            }
        }
        else {
            out.print("\tpublic static final ");
        }
        out.print(declaredMemberClass);
        out.print("<");

        if (hasType) {
            out.print(strategy.getFullJavaClassName(type, Mode.RECORD));
            out.print(", ");
        }

        out.print(getJavaType(column.getType()));
        out.print("> ");
        out.print(strategy.getJavaIdentifier(column));

        if (declaredMemberClass == TableField.class) {
            out.print(" = createField");
        }
        else if (declaredMemberClass == UDTField.class) {
            out.print(" = createField");
        }
        else {
            out.print(" = createParameter");
        }

        out.print("(\"");
        out.print(column.getName());
        out.print("\", ");
        out.print(getJavaTypeReference(column.getDatabase(), column.getType()));

        if (hasType) {
            if (type instanceof TableDefinition) {
                if (generateInstanceFields()) {
                    out.print(", this");
                }
                else {
                    out.print(", " + strategy.getJavaIdentifier(type));
                }
            }
            else {
                out.print(", " + strategy.getJavaIdentifier(type));
            }
        }

        if (isDefaulted) {
            out.print(", true");
        }

        out.println(");");
    }

    protected void printFieldJavaDoc(GenerationWriter out, TypedElementDefinition<?> element) {
        printFieldJavaDoc(out, element, null);
    }

    protected void printFieldJavaDoc(GenerationWriter out, TypedElementDefinition<?> element, String deprecation) {
        out.println();
        out.println("\t/**");

        String comment = element.getComment();

        if (comment != null && comment.length() > 0) {
            out.println("\t * " + comment);
        } else {
            out.println("\t * An uncommented item");
        }

        if (getJavaType(element.getType()).startsWith("java.lang.Object")) {
            String t = element.getType().getType();
            String u = element.getType().getUserType();
            String combined = t.equalsIgnoreCase(u) ? t : t + ", " + u;

            out.println("\t * ");
            out.print("\t * The SQL type of this item (");
            out.print(combined);
            out.println(") could not be mapped.<br/>");
            out.println("\t * Deserialising this field might not work!");

            log.warn("Unknown type", element.getQualifiedName() + " (" + combined + ")");
        }

        if (element instanceof ColumnDefinition) {
            ColumnDefinition column = (ColumnDefinition) element;

            UniqueKeyDefinition primaryKey = column.getPrimaryKey();
            ForeignKeyDefinition foreignKey = column.getForeignKey();

            if (primaryKey != null) {
                out.println("\t * ");
                out.print("\t * PRIMARY KEY");
                out.println();
            }

            if (foreignKey != null) {
                out.println("\t * <p>");
                out.println("\t * <code><pre>");

                out.print("\t * CONSTRAINT ");
                out.println(foreignKey.getOutputName());
                out.print("\t * FOREIGN KEY (");

                String separator = "";
                for (ColumnDefinition fkColumn : foreignKey.getKeyColumns()) {
                    out.print(separator);
                    out.print(fkColumn.getOutputName());
                    separator = ", ";
                }

                out.println(")");
                out.print("\t * REFERENCES ");
                out.print(foreignKey.getReferencedTable().getQualifiedOutputName());
                out.print(" (");

                separator = "";
                for (ColumnDefinition fkColumn : foreignKey.getReferencedColumns()) {
                    out.print(separator);
                    out.print(fkColumn.getOutputName());
                    separator = ", ";
                }

                out.println(")");
                out.println("\t * </pre></code>");
            }
        }

        printDeprecation(out, deprecation);

        out.println("\t */");

        if (deprecation != null) {
            out.println("\t@Deprecated");
        }
    }

    protected void printDeprecation(GenerationWriter out, String deprecation) {
        if (deprecation != null) {
            out.println("\t *");

            String[] strings = deprecation.split("[\n\r]+");
            for (int i = 0; i < strings.length; i++) {
                if (i == 0) {
                    out.println("\t * @deprecated " + strings[i]);
                }
                else {
                    out.println("\t *             " + strings[i]);
                }
            }
        }
    }

    protected void printNoFurtherInstancesAllowedJavadoc(GenerationWriter out) {
        printJavadoc(out, "No further instances allowed");
    }

    protected void printJavadoc(GenerationWriter out, String doc) {
        out.println("\t/**");
        out.println("\t * " + doc);
        out.println("\t */");
    }

    protected void printClassJavadoc(GenerationWriter out, Definition definition) {
        printClassJavadoc(out, definition.getComment());
    }

    protected void printClassJavadoc(GenerationWriter out, String comment) {
        printClassJavadoc(out, comment, null);
    }

    protected void printClassJavadoc(GenerationWriter out, String comment, String deprecation) {
        out.println("/**");
        out.println(" * This class is generated by jOOQ.");

        if (comment != null && comment.length() > 0) {
            out.println(" *");
            printJavadocParagraph(out, comment, "");
        }

        if (deprecation != null && deprecation.length() > 0) {
            out.println(" *");
            printJavadocParagraph(out, "@deprecated : " + deprecation, "");
        }

        out.println(" */");

        if (generateGeneratedAnnotation()) {
            out.println(
                "@javax.annotation.Generated(value    = {\"http://www.jooq.org\", \"" + Constants.VERSION + "\"},\n" +
                "                            comments = \"This class is generated by jOOQ\")");
        }

        if (deprecation != null && deprecation.length() > 0) {
            out.println("@Deprecated");
        }

        out.printSuppressWarningsPlaceholder();
    }

    /**
     * This method is used to add line breaks in lengthy javadocs
     */
    protected void printJavadocParagraph(GenerationWriter out, String comment, String indent) {
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

    protected void printHeader(GenerationWriter out, Definition definition) {
        printHeader(out, definition, Mode.DEFAULT);
    }

    protected void printHeader(GenerationWriter out, Definition definition, Mode mode) {
        out.println("/**");
        out.println(" * This class is generated by jOOQ");
        out.println(" */");
        out.println("package " + strategy.getJavaPackageName(definition, mode) + ";");
        out.println();
    }

    protected void printExtendsNumberType(GenerationWriter out, DataTypeDefinition type) {
        printNumberType(out, type, "? extends ");
    }

    protected void printNumberType(GenerationWriter out, DataTypeDefinition type) {
        printNumberType(out, type, "");
    }

    protected void printNumberType(GenerationWriter out, DataTypeDefinition type, String prefix) {
        if (type.isGenericNumberType()) {
            out.print(prefix);
            out.print(Number.class);
        }
        else {
            out.print(getJavaType(type));
        }
    }

    protected String getSimpleJavaType(DataTypeDefinition type) {
        return GenerationUtil.getSimpleJavaType(getJavaType(type));
    }

    protected String getJavaTypeReference(Database db, DataTypeDefinition type) {
        if (type instanceof MasterDataTypeDefinition) {
            StringBuilder sb = new StringBuilder();

            sb.append(getJavaTypeReference(db, ((MasterDataTypeDefinition) type).underlying));
            sb.append(".asMasterDataType(");
            sb.append(getJavaType(type));
            sb.append(".class)");

            return sb.toString();
        }

        else {
            if (database.isArrayType(type.getType())) {
                String baseType = GenerationUtil.getArrayBaseType(db.getDialect(), type.getType(), type.getUserType());
                return getTypeReference(db, type.getSchema(), baseType, 0, 0, baseType) + ".getArrayDataType()";
            }
            else {
                return getTypeReference(db, type.getSchema(), type.getType(), type.getPrecision(), type.getScale(), type.getUserType());
            }
        }
    }

    protected String getJavaType(DataTypeDefinition type) {
        if (type instanceof MasterDataTypeDefinition) {
            return strategy.getFullJavaClassName(((MasterDataTypeDefinition) type).table);
        }
        else {
            return getType(
                type.getDatabase(),
                type.getSchema(),
                type.getType(),
                type.getPrecision(),
                type.getScale(),
                type.getUserType(),
                Object.class.getName());
        }
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
            type = strategy.getFullJavaClassName(db.getArray(schema, u), Mode.RECORD);
        }

        // Check for ENUM types
        else if (db.getEnum(schema, u) != null) {
            type = strategy.getFullJavaClassName(db.getEnum(schema, u));
        }

        // Check for UDTs
        else if (db.getUDT(schema, u) != null) {
            type = strategy.getFullJavaClassName(db.getUDT(schema, u), Mode.RECORD);
        }

        // Check for custom types
        else if (db.getConfiguredCustomType(u) != null) {
            type = u;
        }

        // Try finding a basic standard SQL type according to the current dialect
        else {
            try {
                Class<?> clazz = FieldTypeHelper.getDialectJavaType(db.getDialect(), t, p, s);
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

    protected String getTypeReference(Database db, SchemaDefinition schema, String t, int p, int s, String u) {
        StringBuilder sb = new StringBuilder();
        if (db.getArray(schema, u) != null) {
            ArrayDefinition array = database.getArray(schema, u);

            sb.append(getJavaTypeReference(db, array.getElementType()));
            sb.append(".asArrayDataType(");
            sb.append(strategy.getFullJavaClassName(array, Mode.RECORD));
            sb.append(".class)");
        }
        else if (db.getUDT(schema, u) != null) {
            UDTDefinition udt = db.getUDT(schema, u);

            sb.append(strategy.getFullJavaIdentifier(udt));
            sb.append(".getDataType()");
        }
        else if (db.getEnum(schema, u) != null) {
            sb.append("org.jooq.util.");
            sb.append(db.getDialect().getName().toLowerCase());
            sb.append(".");
            sb.append(db.getDialect().getName());
            sb.append("DataType.");
            sb.append(FieldTypeHelper.normalise(FieldTypeHelper.getDataType(db.getDialect(), String.class).getTypeName()));
            sb.append(".asEnumDataType(");
            sb.append(strategy.getFullJavaClassName(db.getEnum(schema, u)));
            sb.append(".class)");
        }
        else {
            DataType<?> dataType = null;

            try {
                dataType = FieldTypeHelper.getDialectDataType(db.getDialect(), t, p, s);
            }

            // Mostly because of unsupported data types. Will be handled later.
            catch (SQLDialectNotSupportedException ignore) {
            }

            // If there is a standard SQLDataType available for the dialect-
            // specific DataType t, then reference that one.
            if (dataType != null && dataType.getSQLDataType() != null) {
                SQLDataType<?> sqlDataType = dataType.getSQLDataType();

                sb.append(SQLDataType.class.getCanonicalName());
                sb.append(".");
                sb.append(FieldTypeHelper.normalise(sqlDataType.getTypeName()));

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
                    String typeName = FieldTypeHelper.normalise(t);

                    // [#1298] Prevent compilation errors for missing types
                    Reflect.on(typeClass).field(typeName);

                    sb.append(typeName);
                    if (!type1.equals(type2)) {
                        Class<?> clazz = FieldTypeHelper.getDialectJavaType(db.getDialect(), t, p, s);

                        sb.append(".asNumberDataType(");
                        sb.append(clazz.getCanonicalName());
                        sb.append(".class)");
                    }
                }

                // Mostly because of unsupported data types
                catch (SQLDialectNotSupportedException e) {
                    sb.append("getDefaultDataType(\"");
                    sb.append(t.replace("\"", "\\\""));
                    sb.append("\")");
                }

                // More unsupported data types
                catch (ReflectException e) {
                    sb.append("getDefaultDataType(\"");
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
}
