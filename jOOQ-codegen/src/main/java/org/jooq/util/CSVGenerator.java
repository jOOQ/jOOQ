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
import java.lang.reflect.Field;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import org.jooq.util.information_schema.ColumnsRecordType;
import org.jooq.util.information_schema.InformationSchemaType;
import org.jooq.util.information_schema.KeyColumnUsageRecordType;
import org.jooq.util.information_schema.ParametersRecordType;
import org.jooq.util.information_schema.ReferentialConstraintsRecordType;
import org.jooq.util.information_schema.RoutinesRecordType;
import org.jooq.util.information_schema.SchemataRecordType;
import org.jooq.util.information_schema.SequencesRecordType;
import org.jooq.util.information_schema.TableConstraintsRecordType;
import org.jooq.util.information_schema.TablesRecordType;

/**
 * A generator for the various TABLES.csv, COLUMNS.csv files
 * <p>
 * Use this generator to transform your schema meta information into a set of
 * CSV files that describe your schema according to the SQL standard
 * INFORMATION SCHEMA.
 *
 * @author Lukas Eder
 */
public class CSVGenerator extends XMLGenerator {

    @Override
    protected void store(InformationSchemaType is) throws IOException {
        print(is.getCOLUMNS(), ColumnsRecordType.class, "COLUMNS.csv");
        print(is.getKEYCOLUMNUSAGE(), KeyColumnUsageRecordType.class, "KEY_COLUMN_USAGE.csv");
        print(is.getPARAMETERS(), ParametersRecordType.class, "PARAMETERS.csv");
        print(is.getREFERENTIALCONSTRAINTS(), ReferentialConstraintsRecordType.class, "REFERENTIAL_CONSTRAINTS.csv");
        print(is.getROUTINES(), RoutinesRecordType.class, "ROUTINES.csv");
        print(is.getSCHEMATA(), SchemataRecordType.class, "SCHEMATA.csv");
        print(is.getSEQUENCES(), SequencesRecordType.class, "SEQUENCES.csv");
        print(is.getTABLECONSTRAINTS(), TableConstraintsRecordType.class, "TABLE_CONSTRAINTS.csv");
        print(is.getTABLES(), TablesRecordType.class, "TABLES.csv");
    }

    private <T> void print(List<T> records, Class<T> type, String fileName) throws IOException {
        GenerationWriter out = new GenerationWriter(new File(getTargetDirectory(), fileName));

        printHeader(out, type);
        for (T record : records) {
            printRow(out, record);
        }

        out.close();
    }

    private void printRow(GenerationWriter out, Object record) {
        String separator = "";

        for (Field field : record.getClass().getDeclaredFields()) {
            XmlElement annotation = field.getAnnotation(XmlElement.class);

            if (annotation != null) {
                try {
                    field.setAccessible(true);
                    Object object = field.get(record);

                    out.print(separator);
                    out.print("\"");
                    out.print(object == null ? "" : object.toString().replace("\"", "\"\""));
                    out.print("\"");

                    separator = ",";
                }
                catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        out.print("\n");
    }

    private void printHeader(GenerationWriter out, Class<?> type) {
        String separator = "";

        for (Field field : type.getDeclaredFields()) {
            XmlElement annotation = field.getAnnotation(XmlElement.class);

            if (annotation != null) {
                out.print(separator);
                out.print("\"");
                out.print(annotation.name().replace("\"", "\"\""));
                out.print("\"");

                separator = ",";
            }
        }

        out.print("\n");
    }
}
