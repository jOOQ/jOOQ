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

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXB;

import org.jooq.tools.JooqLogger;
import org.jooq.tools.StopWatch;
import org.jooq.util.information_schema.ColumnsRecordType;
import org.jooq.util.information_schema.ConstraintType;
import org.jooq.util.information_schema.InformationSchemaType;
import org.jooq.util.information_schema.KeyColumnUsageRecordType;
import org.jooq.util.information_schema.ParameterModeType;
import org.jooq.util.information_schema.ParametersRecordType;
import org.jooq.util.information_schema.ReferentialConstraintsRecordType;
import org.jooq.util.information_schema.RoutinesRecordType;
import org.jooq.util.information_schema.SchemataRecordType;
import org.jooq.util.information_schema.SequencesRecordType;
import org.jooq.util.information_schema.TableConstraintsRecordType;
import org.jooq.util.information_schema.TablesRecordType;
import org.jooq.util.information_schema.YesOrNoType;

/**
 * A generator for the INFORMATION_SCHEMA.xml file
 * <p>
 * Use this generator to transform your schema meta information into an XML file
 * that describes your schema according to the SQL standard INFORMATION SCHEMA.
 *
 * @author Lukas Eder
 */
public class XMLGenerator extends AbstractGenerator {

    private static final JooqLogger log = JooqLogger.getLogger(XMLGenerator.class);

    @Override
    public void generate(Database database) throws IOException {
        StopWatch watch = new StopWatch();

        log.info("Database parameters");
        log.info("----------------------------------------------------------");
        log.info("  dialect", database.getDialect());
        log.info("  target dir", getTargetDirectory());
        log.info("----------------------------------------------------------");
        log.info("");
        log.info("XMLGenerator parameters");
        log.info("----------------------------------------------------------");
        log.info("  N/A");
        log.info("----------------------------------------------------------");

        InformationSchemaType is = new InformationSchemaType();

        // ---------------------------------------------------------------------
        // XXX: INFORMATION_SCHEMA.SCHEMATA
        // ---------------------------------------------------------------------
        for (SchemaDefinition schema : database.getSchemata()) {
            SchemataRecordType record = new SchemataRecordType();

            record.setSCHEMANAME(schema.getOutputName());
            is.getSCHEMATA().add(record);
        }

        // ---------------------------------------------------------------------
        // XXX: INFORMATION_SCHEMA.TABLES
        // ---------------------------------------------------------------------
        for (TableDefinition table : database.getTables(null)) {
            TablesRecordType rec = new TablesRecordType();
            SchemaDefinition schema = table.getSchema();

            if (schema != null) {
                rec.setTABLESCHEMA(schema.getOutputName());
            }

            rec.setTABLENAME(table.getOutputName());
            is.getTABLES().add(rec);

            // -----------------------------------------------------------------
            // XXX: INFORMATION_SCHEMA.COLUMNS
            // -----------------------------------------------------------------
            for (ColumnDefinition column : table.getColumns()) {
                ColumnsRecordType record = new ColumnsRecordType();

                if (schema != null) {
                    record.setTABLESCHEMA(schema.getOutputName());
                }

                record.setTABLENAME(table.getOutputName());
                record.setCOLUMNNAME(column.getOutputName());
                record.setORDINALPOSITION(column.getPosition());
                record.setISIDENTITY(yn(column.isIdentity()));
                record.setISNULLABLE(yn(column.isNullable()));

                // TODO: add data type information

                is.getCOLUMNS().add(record);
            }

            // -----------------------------------------------------------------
            // XXX: INFORMATION_SCHEMA.TABLE_CONSTRAINTS
            // -----------------------------------------------------------------
            for (UniqueKeyDefinition uniqueKey : table.getUniqueKeys()) {
                TableConstraintsRecordType record = new TableConstraintsRecordType();

                if (schema != null) {
                    record.setCONSTRAINTSCHEMA(schema.getOutputName());
                    record.setTABLESCHEMA(schema.getOutputName());
                }

                record.setCONSTRAINTNAME(uniqueKey.getName());
                record.setCONSTRAINTTYPE(uniqueKey.isPrimaryKey() ? ConstraintType.PRIMARY_KEY : ConstraintType.UNIQUE);
                record.setTABLENAME(table.getOutputName());

                is.getTABLECONSTRAINTS().add(record);
            }

            for (ForeignKeyDefinition foreignKey : table.getForeignKeys()) {
                TableConstraintsRecordType record = new TableConstraintsRecordType();

                if (schema != null) {
                    record.setCONSTRAINTSCHEMA(schema.getOutputName());
                    record.setTABLESCHEMA(schema.getOutputName());
                }

                record.setCONSTRAINTNAME(foreignKey.getName());
                record.setCONSTRAINTTYPE(ConstraintType.FOREIGN_KEY);
                record.setTABLENAME(table.getOutputName());

                is.getTABLECONSTRAINTS().add(record);
            }

            // -----------------------------------------------------------------
            // XXX: INFORMATION_SCHEMA.KEY_COLUMN_USAGE
            // -----------------------------------------------------------------
            for (UniqueKeyDefinition uniqueKey : table.getUniqueKeys()) {
                for (ColumnDefinition column : uniqueKey.getKeyColumns()) {
                    KeyColumnUsageRecordType record = new KeyColumnUsageRecordType();

                    if (schema != null) {
                        record.setCONSTRAINTSCHEMA(schema.getOutputName());
                        record.setTABLESCHEMA(schema.getOutputName());
                    }

                    record.setTABLENAME(table.getOutputName());
                    record.setCOLUMNNAME(column.getName());
                    record.setCONSTRAINTNAME(uniqueKey.getName());
                    record.setORDINALPOSITION(column.getPosition());

                    is.getKEYCOLUMNUSAGE().add(record);
                }
            }

            for (ForeignKeyDefinition foreignKey : table.getForeignKeys()) {
                for (ColumnDefinition column : foreignKey.getKeyColumns()) {
                    KeyColumnUsageRecordType record = new KeyColumnUsageRecordType();

                    if (schema != null) {
                        record.setCONSTRAINTSCHEMA(schema.getOutputName());
                        record.setTABLESCHEMA(schema.getOutputName());
                    }

                    record.setTABLENAME(table.getOutputName());
                    record.setCOLUMNNAME(column.getName());
                    record.setCONSTRAINTNAME(foreignKey.getName());

                    is.getKEYCOLUMNUSAGE().add(record);
                }
            }

            // -----------------------------------------------------------------
            // XXX: INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS
            // -----------------------------------------------------------------
            for (ForeignKeyDefinition foreignKey : table.getForeignKeys()) {
                UniqueKeyDefinition uniqueKey = foreignKey.getReferencedKey();
                ReferentialConstraintsRecordType record = new ReferentialConstraintsRecordType();

                if (schema != null) {
                    record.setCONSTRAINTSCHEMA(schema.getOutputName());
                    record.setUNIQUECONSTRAINTSCHEMA(uniqueKey.getSchema().getOutputName());
                }

                record.setCONSTRAINTNAME(foreignKey.getName());
                record.setUNIQUECONSTRAINTNAME(uniqueKey.getName());

                is.getREFERENTIALCONSTRAINTS().add(record);
            }
        }

        // -----------------------------------------------------------------
        // XXX: INFORMATION_SCHEMA.SEQUENCES
        // -----------------------------------------------------------------
        for (SequenceDefinition sequence : database.getSequences(null)) {
            SequencesRecordType record = new SequencesRecordType();
            SchemaDefinition schema = sequence.getSchema();

            if (schema != null) {
                record.setSEQUENCESCHEMA(schema.getOutputName());
            }

            record.setSEQUENCENAME(sequence.getOutputName());

            // TODO set data type

            is.getSEQUENCES().add(record);
        }

        // ---------------------------------------------------------------------
        // XXX: INFORMATION_SCHEMA.ROUTINES
        // ---------------------------------------------------------------------

        List<RoutineDefinition> routines = new ArrayList<RoutineDefinition>();
        routines.addAll(database.getRoutines(null));
        for (PackageDefinition pkg : database.getPackages(null)) {
            routines.addAll(pkg.getRoutines());
        }

        for (RoutineDefinition routine : routines) {
            RoutinesRecordType rec = new RoutinesRecordType();
            SchemaDefinition schema = routine.getSchema();

            if (schema != null) {
                rec.setROUTINESCHEMA(schema.getOutputName());
                rec.setSPECIFICSCHEMA(schema.getOutputName());
            }

            if (routine.getPackage() != null) {
                if (schema != null) {
                    rec.setMODULESCHEMA(schema.getOutputName());
                }

                rec.setMODULENAME(routine.getPackage().getOutputName());
                rec.setSCHEMALEVELROUTINE(YesOrNoType.NO);
            }
            else {
                rec.setSCHEMALEVELROUTINE(YesOrNoType.YES);
            }

            rec.setROUTINENAME(routine.getOutputName());
            rec.setSPECIFICNAME(routine.getOutputName());

            // TODO deal with UDTs
            // TODO set data type

            int position = 1;
            for (ParameterDefinition parameter : routine.getAllParameters()) {
                ParametersRecordType record = new ParametersRecordType();

                if (schema != null) {
                    record.setSPECIFICSCHEMA(schema.getOutputName());
                }

                record.setSPECIFICNAME(routine.getName());
                record.setPARAMETERNAME(parameter.getName());
                record.setORDINALPOSITION(position++);

                if (routine.getInParameters().contains(parameter)) {
                    if (routine.getOutParameters().contains(parameter)) {
                        record.setPARAMETERMODE(ParameterModeType.INOUT);
                    }
                    else {
                        record.setPARAMETERMODE(ParameterModeType.IN);
                    }
                }
                else {
                    record.setPARAMETERMODE(ParameterModeType.OUT);
                }

                // TODO set data type
                // TODO [#1435] use ParameterDefinition.getPosition()
                // TODO [#1436] use ParameterDefinition.getMode()

                is.getPARAMETERS().add(record);
            }

            is.getROUTINES().add(rec);
        }

        store(is);
        watch.splitInfo("GENERATION FINISHED!");
    }

    protected void store(InformationSchemaType is) throws IOException {
        GenerationWriter out = new GenerationWriter(new File(getTargetDirectory(), "INFORMATION_SCHEMA.xml"));
        StringWriter writer = new StringWriter();
        JAXB.marshal(is, writer);
        out.print(writer.toString());
        out.close();
    }

    private YesOrNoType yn(boolean value) {
        return value ? YesOrNoType.YES : YesOrNoType.NO;
    }
}
