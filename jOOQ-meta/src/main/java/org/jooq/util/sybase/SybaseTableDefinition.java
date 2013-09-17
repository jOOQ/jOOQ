/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
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
 * and Maintenance Agreement for more details: http://www.jooq.org/eula
 */
package org.jooq.util.sybase;

import static org.jooq.util.sybase.sys.Tables.SYSDOMAIN;
import static org.jooq.util.sybase.sys.Tables.SYSTAB;
import static org.jooq.util.sybase.sys.Tables.SYSTABCOL;

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
 * Sybase table definition
 *
 * @author Espen Stromsnes
 */
public class SybaseTableDefinition extends AbstractTableDefinition {

    public SybaseTableDefinition(SchemaDefinition schema, String name, String comment) {
        super(schema, name, comment);
    }

    @Override
    public List<ColumnDefinition> getElements0() throws SQLException {
        List<ColumnDefinition> result = new ArrayList<ColumnDefinition>();

        for (Record record : create().select(
                SYSTABCOL.COLUMN_NAME,
                SYSTABCOL.COLUMN_ID,
                SYSDOMAIN.DOMAIN_NAME,
                SYSTABCOL.NULLS,
                SYSTABCOL.WIDTH,
                SYSTABCOL.SCALE,
                SYSTABCOL.DEFAULT)
             .from(SYSTABCOL)
             .join(SYSTAB)
             .on(SYSTABCOL.TABLE_ID.equal(SYSTAB.TABLE_ID))
             .join(SYSDOMAIN)
             .on(SYSTABCOL.DOMAIN_ID.equal(SYSDOMAIN.DOMAIN_ID))
             .where(SYSTAB.TABLE_NAME.equal(getName()))
             .orderBy(SYSTABCOL.COLUMN_ID)
             .fetch()) {

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                record.getValue(SYSDOMAIN.DOMAIN_NAME),
                record.getValue(SYSTABCOL.WIDTH),
                record.getValue(SYSTABCOL.WIDTH),
                record.getValue(SYSTABCOL.SCALE),
                record.getValue(SYSTABCOL.NULLS, boolean.class),
                record.getValue(SYSTABCOL.DEFAULT) != null
            );

            ColumnDefinition column = new DefaultColumnDefinition(
            	getDatabase().getTable(getSchema(), getName()),
                record.getValue(SYSTABCOL.COLUMN_NAME),
                record.getValue(SYSTABCOL.COLUMN_ID),
                type,
                "autoincrement".equalsIgnoreCase(record.getValue(SYSTABCOL.DEFAULT)),
                null
            );

            result.add(column);
        }

        return result;
    }
}
