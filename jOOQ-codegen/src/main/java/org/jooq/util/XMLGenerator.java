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
 */
package org.jooq.util;

import static org.jooq.tools.StringUtils.isBlank;
import static org.jooq.util.xml.jaxb.TableConstraintType.CHECK;
import static org.jooq.util.xml.jaxb.TableConstraintType.FOREIGN_KEY;
import static org.jooq.util.xml.jaxb.TableConstraintType.PRIMARY_KEY;
import static org.jooq.util.xml.jaxb.TableConstraintType.UNIQUE;

import java.io.StringWriter;
import java.util.List;

import javax.xml.bind.JAXB;

import org.jooq.tools.JooqLogger;
import org.jooq.util.xml.jaxb.Column;
import org.jooq.util.xml.jaxb.InformationSchema;
import org.jooq.util.xml.jaxb.KeyColumnUsage;
import org.jooq.util.xml.jaxb.Parameter;
import org.jooq.util.xml.jaxb.ParameterMode;
import org.jooq.util.xml.jaxb.ReferentialConstraint;
import org.jooq.util.xml.jaxb.Routine;
import org.jooq.util.xml.jaxb.RoutineType;
import org.jooq.util.xml.jaxb.Schema;
import org.jooq.util.xml.jaxb.Sequence;
import org.jooq.util.xml.jaxb.Table;
import org.jooq.util.xml.jaxb.TableConstraint;

/**
 * @author Lukas Eder
 */
public class XMLGenerator extends AbstractGenerator {

    private static final JooqLogger log = JooqLogger.getLogger(XMLGenerator.class);

    public XMLGenerator() {
        super(Language.XML);
    }

    @Override
    public void generate(Database db) {
        logDatabaseParameters(db);
        log.info("");
        logGenerationRemarks(db);

        log.info("");
        log.info("----------------------------------------------------------");

        TextWriter out = new TextWriter(getStrategy().getFile("information_schema.xml"), targetEncoding);
        log.info("");
        log.info("Generating XML", out.file().getName());
        log.info("==========================================================");

        InformationSchema is = new InformationSchema();

        for (CatalogDefinition c : db.getCatalogs()) {
            String catalogName = c.getOutputName();

            for (SchemaDefinition s : c.getSchemata()) {
                String schemaName = s.getOutputName();

                Schema schema = new Schema();
                schema.setCatalogName(catalogName);
                schema.setSchemaName(schemaName);

                is.getSchemata().add(schema);

                for (TableDefinition t : s.getTables()) {
                    String tableName = t.getOutputName();

                    Table table = new Table();
                    table.setTableCatalog(catalogName);
                    table.setTableSchema(schemaName);
                    table.setTableName(tableName);

                    is.getTables().add(table);

                    for (ColumnDefinition co : t.getColumns()) {
                        String columnName = co.getOutputName();
                        DataTypeDefinition type = co.getType();

                        Column column = new Column();
                        column.setTableCatalog(catalogName);
                        column.setTableSchema(schemaName);
                        column.setTableName(tableName);
                        column.setColumnName(columnName);
                        column.setCharacterMaximumLength(type.getLength());
                        column.setColumnDefault(type.getDefaultValue());
                        column.setDataType(type.getType());
//                      TODO This is not yet supported
//                      column.setIdentityGeneration(co.isIdentity());
                        column.setIsNullable(column.isIsNullable());
                        column.setNumericPrecision(type.getPrecision());
                        column.setNumericScale(type.getScale());
                        column.setOrdinalPosition(co.getPosition());

                        is.getColumns().add(column);
                    }
                }

                for (UniqueKeyDefinition u : db.getUniqueKeys(s)) {
                    String constraintName = u.getOutputName();
                    TableDefinition table = u.getTable();
                    List<ColumnDefinition> columns = u.getKeyColumns();

                    TableConstraint constraint = new TableConstraint();
                    constraint.setConstraintCatalog(catalogName);
                    constraint.setConstraintSchema(schemaName);
                    constraint.setConstraintName(constraintName);
                    constraint.setConstraintType(u.isPrimaryKey() ? PRIMARY_KEY : UNIQUE);
                    constraint.setTableCatalog(table.getCatalog().getOutputName());
                    constraint.setTableSchema(table.getSchema().getOutputName());
                    constraint.setTableName(table.getOutputName());

                    is.getTableConstraints().add(constraint);

                    for (int i = 0; i < columns.size(); i++) {
                        ColumnDefinition column = columns.get(i);

                        KeyColumnUsage kc = new KeyColumnUsage();

                        kc.setConstraintCatalog(catalogName);
                        kc.setConstraintSchema(schemaName);
                        kc.setConstraintName(constraintName);
                        kc.setColumnName(column.getOutputName());
                        kc.setOrdinalPosition(i);
                        kc.setTableCatalog(table.getCatalog().getOutputName());
                        kc.setTableSchema(table.getSchema().getOutputName());
                        kc.setTableName(table.getOutputName());

                        is.getKeyColumnUsages().add(kc);
                    }
                }

                for (ForeignKeyDefinition f : db.getForeignKeys(s)) {
                    String constraintName = f.getOutputName();
                    UniqueKeyDefinition referenced = f.getReferencedKey();
                    TableDefinition table = f.getKeyTable();
                    List<ColumnDefinition> columns = f.getKeyColumns();

                    TableConstraint tc = new TableConstraint();
                    tc.setConstraintCatalog(catalogName);
                    tc.setConstraintSchema(schemaName);
                    tc.setConstraintName(constraintName);
                    tc.setConstraintType(FOREIGN_KEY);
                    tc.setTableCatalog(table.getCatalog().getOutputName());
                    tc.setTableSchema(table.getSchema().getOutputName());
                    tc.setTableName(table.getOutputName());

                    ReferentialConstraint rc = new ReferentialConstraint();
                    rc.setConstraintCatalog(catalogName);
                    rc.setConstraintSchema(schemaName);
                    rc.setConstraintName(constraintName);
                    rc.setUniqueConstraintCatalog(referenced.getOutputName());
                    rc.setUniqueConstraintSchema(referenced.getSchema().getOutputName());
                    rc.setUniqueConstraintName(referenced.getOutputName());

                    is.getTableConstraints().add(tc);
                    is.getReferentialConstraints().add(rc);

                    for (int i = 0; i < columns.size(); i++) {
                        ColumnDefinition column = columns.get(i);

                        KeyColumnUsage kc = new KeyColumnUsage();

                        kc.setConstraintCatalog(catalogName);
                        kc.setConstraintSchema(schemaName);
                        kc.setConstraintName(constraintName);
                        kc.setColumnName(column.getOutputName());
                        kc.setOrdinalPosition(i);
                        kc.setTableCatalog(table.getCatalog().getOutputName());
                        kc.setTableSchema(table.getSchema().getOutputName());
                        kc.setTableName(table.getOutputName());

                        is.getKeyColumnUsages().add(kc);
                    }
                }

                for (CheckConstraintDefinition ch : db.getCheckConstraints(s)) {
                    String constraintName = ch.getOutputName();
                    TableDefinition table = ch.getTable();

                    TableConstraint constraint = new TableConstraint();
                    constraint.setConstraintCatalog(catalogName);
                    constraint.setConstraintSchema(schemaName);
                    constraint.setConstraintName(constraintName);
                    constraint.setConstraintType(CHECK);
                    constraint.setTableCatalog(table.getCatalog().getOutputName());
                    constraint.setTableSchema(table.getSchema().getOutputName());
                    constraint.setTableName(table.getOutputName());

                    is.getTableConstraints().add(constraint);
                }

                for (SequenceDefinition se : db.getSequences(s)) {
                    String sequenceName = se.getOutputName();
                    DataTypeDefinition type = se.getType();

                    Sequence sequence = new Sequence();
                    sequence.setSequenceCatalog(catalogName);
                    sequence.setSequenceSchema(schemaName);
                    sequence.setSequenceName(sequenceName);
                    sequence.setCharacterMaximumLength(type.getLength());
                    sequence.setDataType(type.getType());
                    sequence.setNumericPrecision(type.getPrecision());
                    sequence.setNumericScale(type.getScale());

                    is.getSequences().add(sequence);
                }

                for (PackageDefinition pkg : db.getPackages(s))
                    for (RoutineDefinition r : pkg.getRoutines())
                        exportRoutine(is, r, catalogName, schemaName);

                for (RoutineDefinition r : db.getRoutines(s))
                    exportRoutine(is, r, catalogName, schemaName);
            }
        }

        StringWriter writer = new StringWriter();
        JAXB.marshal(is, writer);
        out.print(writer.toString());
        out.close();
    }

    private void exportRoutine(InformationSchema is, RoutineDefinition r, String catalogName, String schemaName) {
        String specificName = r.getName() + (isBlank(r.getOverload()) ? "" : "_" + r.getOverload());

        Routine routine = new Routine();
        routine.setRoutineCatalog(catalogName);
        routine.setSpecificCatalog(catalogName);
        routine.setRoutineSchema(schemaName);
        routine.setSpecificSchema(schemaName);

        if (r.getPackage() != null) {
            routine.setRoutinePackage(r.getPackage().getName());
            routine.setSpecificPackage(r.getPackage().getName());
        }

        routine.setRoutineName(r.getName());
        routine.setSpecificName(specificName);

        if (r.getReturnValue() == null) {
            routine.setRoutineType(RoutineType.PROCEDURE);
        }
        else {
            routine.setRoutineType(RoutineType.FUNCTION);
            routine.setDataType(r.getReturnType().getType());
            routine.setCharacterMaximumLength(r.getReturnType().getLength());
            routine.setNumericPrecision(r.getReturnType().getPrecision());
            routine.setNumericScale(r.getReturnType().getScale());
        }

        is.getRoutines().add(routine);

        int i = 1;
        for (ParameterDefinition p : r.getAllParameters()) {
            if (p != r.getReturnValue()) {
                Parameter parameter = new Parameter();

                parameter.setSpecificCatalog(catalogName);
                parameter.setSpecificSchema(schemaName);

                if (r.getPackage() != null)
                    parameter.setSpecificPackage(r.getPackage().getName());

                parameter.setSpecificName(specificName);
                parameter.setOrdinalPosition(i++);
                parameter.setParameterName(p.getName());

                boolean in = r.getInParameters().contains(p);
                boolean out = r.getOutParameters().contains(p);

                if (in && out)
                    parameter.setParameterMode(ParameterMode.INOUT);
                else if (in)
                    parameter.setParameterMode(ParameterMode.IN);
                else if (out)
                    parameter.setParameterMode(ParameterMode.OUT);

                parameter.setDataType(p.getType().getType());
                parameter.setCharacterMaximumLength(p.getType().getLength());
                parameter.setNumericPrecision(p.getType().getPrecision());
                parameter.setNumericScale(p.getType().getScale());
                parameter.setParameterDefault(p.getType().getDefaultValue());

                is.getParameters().add(parameter);
            }
        }
    }
}
