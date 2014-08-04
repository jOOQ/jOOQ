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
package org.jooq.util.informix;

import static org.jooq.impl.DSL.decode;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.nvl;
import static org.jooq.util.informix.sys.Tables.SYSCOLUMNS;
import static org.jooq.util.informix.sys.Tables.SYSTABLES;
import static org.jooq.util.informix.sys.Tables.SYSXTDTYPES;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Record;
import org.jooq.util.AbstractTableDefinition;
import org.jooq.util.ColumnDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.DefaultColumnDefinition;
import org.jooq.util.DefaultDataTypeDefinition;
import org.jooq.util.SchemaDefinition;

/**
 * Informix table definition
 *
 * @author Lukas Eder
 */
public class InformixTableDefinition extends AbstractTableDefinition {

    public InformixTableDefinition(SchemaDefinition schema, String name, String comment) {
        super(schema, name, comment);
    }

    @Override
    public List<ColumnDefinition> getElements0() throws SQLException {
        List<ColumnDefinition> result = new ArrayList<ColumnDefinition>();

        for (Record record : create().select(
                    field("syscolumns.colname", String.class).trim().as("colname"),
                    field("syscolumns.colno", Integer.class).as("colno"),

                    // http://publib.boulder.ibm.com/infocenter/idshelp/v10/index.jsp?topic=/com.ibm.sqlr.doc/sqlrmst41.htm
                    nvl(SYSXTDTYPES.NAME,
                        decode().value(field("bitand(syscolumns.coltype, 255)", Integer.class))
                                .when(inline(0   ), inline("CHAR"      ))
                                .when(inline(1   ), inline("SMALLINT"  ))
                                .when(inline(2   ), inline("INTEGER"   ))
                                .when(inline(3   ), inline("FLOAT"     ))
                                .when(inline(4   ), inline("SMALLFLOAT"))
                                .when(inline(5   ), inline("DECIMAL"   ))
                                .when(inline(6   ), inline("SERIAL"    ))
                                .when(inline(7   ), inline("DATE"      ))
                                .when(inline(8   ), inline("MONEY"     ))
                                .when(inline(9   ), inline("NULL"      ))
                                .when(inline(10  ), inline("DATETIME"  ))
                                .when(inline(11  ), inline("BYTE"      ))
                                .when(inline(12  ), inline("TEXT"      ))
                                .when(inline(13  ), inline("VARCHAR"   ))
                                .when(inline(14  ), inline("INTERVAL"  ))
                                .when(inline(15  ), inline("NCHAR"     ))
                                .when(inline(16  ), inline("NVARCHAR"  ))
                                .when(inline(17  ), inline("INT8"      ))
                                .when(inline(18  ), inline("SERIAL8"   ))
                                .when(inline(19  ), inline("SET"       ))
                                .when(inline(20  ), inline("MULTISET"  ))
                                .when(inline(21  ), inline("LIST"      ))
                                .when(inline(22  ), inline("ROW"       ))
                                .when(inline(40  ), inline("OTHER"     ))
                                .when(inline(4118), inline("ROW"       ))
                                .otherwise(         inline("OTHER"     ))
                        ).as("coltype"),
                    field("syscolumns.collength", Integer.class).as("collength"),
                    field("bitand(syscolumns.coltype, 256) / 256", boolean.class).as("nullable")
                )
                .from(SYSTABLES)
                .join(SYSCOLUMNS).on(SYSTABLES.TABID.eq(SYSCOLUMNS.TABID))
                .leftOuterJoin(SYSXTDTYPES).on(SYSCOLUMNS.EXTENDED_ID.eq(SYSXTDTYPES.EXTENDED_ID))
                .where(SYSTABLES.OWNER.eq(getSchema().getInputName()))
                .and(SYSTABLES.TABNAME.eq(getInputName()))
                .orderBy(SYSCOLUMNS.COLNO)
                .fetch()) {

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                record.getValue("coltype", String.class),
                record.getValue("collength", Integer.class),
                record.getValue("collength", Integer.class),
                0,
                record.getValue("nullable", boolean.class),
                false
            );

            ColumnDefinition column = new DefaultColumnDefinition(
            	getDatabase().getTable(getSchema(), getName()),
                record.getValue("colname", String.class),
                record.getValue("colno", Integer.class),
                type,
                false, // identity
                null
            );

            result.add(column);
        }

        return result;
    }
}
