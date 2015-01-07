/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
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

import static org.jooq.impl.DSL.field;
import static org.jooq.util.informix.sys.Tables.SYSCOLUMNS;
import static org.jooq.util.informix.sys.Tables.SYSDEFAULTS;
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
import org.jooq.util.informix.sys.tables.Syscolumns;
import org.jooq.util.informix.sys.tables.Sysdefaults;
import org.jooq.util.informix.sys.tables.Systables;
import org.jooq.util.informix.sys.tables.Sysxtdtypes;

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

        Syscolumns c = SYSCOLUMNS.as("c");
        Systables t = SYSTABLES.as("t");
        Sysxtdtypes x = SYSXTDTYPES.as("x");
        Sysdefaults d = SYSDEFAULTS.as("d");

        for (Record record : create().select(
                    c.COLNAME.trim().as(c.COLNAME),
                    c.COLNO,

                    // http://publib.boulder.ibm.com/infocenter/idshelp/v10/index.jsp?topic=/com.ibm.sqlr.doc/sqlrmst41.htm
                    field("informix.schema_coltypename({0}, {1})", String.class, c.COLTYPE, c.EXTENDED_ID).trim().as("coltype"),
                    field("informix.schema_charlen({0}, {1}, {2})", int.class, c.COLTYPE, c.EXTENDED_ID, c.COLLENGTH).as("collength"),
                    field("informix.schema_precision({0}, {1}, {2})", int.class, c.COLTYPE, c.EXTENDED_ID, c.COLLENGTH).as("precision"),
                    field("informix.schema_numscale({0}, {1})", int.class, c.COLTYPE, c.COLLENGTH).as("scale"),
                    field("informix.schema_isnullable({0})", boolean.class, c.COLTYPE).as("nullable"),
                    field("informix.schema_isautoincr({0})", String.class, c.COLTYPE).as("identity"),
                    d.TYPE
                )
                .from(t)
                .join(c).on(t.TABID.eq(c.TABID))
                .leftOuterJoin(x).on(c.EXTENDED_ID.eq(x.EXTENDED_ID))
                .leftOuterJoin(d).on(d.TABID.eq(c.TABID).and(d.COLNO.eq(c.COLNO)))
                .where(t.OWNER.eq(getSchema().getInputName()))
                .and(t.TABNAME.eq(getInputName()))
                .orderBy(c.COLNO)
                .fetch()) {

            // Informix reports TEXT, BLOB types and similar to be of length Integer.MAX_VALUE
            // We shouldn't use this information in jOOQ, though.
            Integer collength = record.getValue("collength", Integer.class);
            if (Integer.valueOf(Integer.MAX_VALUE).equals(collength))
                collength = 0;

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                record.getValue("coltype", String.class),
                collength,
                record.getValue("precision", Integer.class),
                record.getValue("scale", Integer.class),
                record.getValue("nullable", boolean.class),
                record.getValue(d.TYPE) != null
            );

            ColumnDefinition column = new DefaultColumnDefinition(
            	getDatabase().getTable(getSchema(), getName()),
                record.getValue(c.COLNAME),
                record.getValue(c.COLNO),
                type,
                record.getValue("identity", boolean.class),
                null
            );

            result.add(column);
        }

        return result;
    }
}
