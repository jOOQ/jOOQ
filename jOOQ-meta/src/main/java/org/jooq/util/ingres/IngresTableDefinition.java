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
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq.util.ingres;

import static org.jooq.impl.DSL.trim;
import static org.jooq.util.ingres.ingres.tables.Iicolumns.IICOLUMNS;
import static org.jooq.util.ingres.ingres.tables.IidbSubcomments.IIDB_SUBCOMMENTS;

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
import org.jooq.util.ingres.ingres.tables.Iicolumns;
import org.jooq.util.ingres.ingres.tables.IidbSubcomments;

/**
 * @author Lukas Eder
 */
public class IngresTableDefinition extends AbstractTableDefinition {

    public IngresTableDefinition(SchemaDefinition schema, String name, String comment) {
        super(schema, name, comment);
    }

    @Override
    protected List<ColumnDefinition> getElements0() throws SQLException {
        List<ColumnDefinition> result = new ArrayList<ColumnDefinition>();

        for (Record record : create().select(
                    Iicolumns.COLUMN_SEQUENCE,
                    trim(Iicolumns.COLUMN_NAME),
                    trim(Iicolumns.COLUMN_DATATYPE),
                    Iicolumns.COLUMN_LENGTH,
                    Iicolumns.COLUMN_SCALE,
                    Iicolumns.COLUMN_NULLS,
                    Iicolumns.COLUMN_HAS_DEFAULT,
                    Iicolumns.COLUMN_ALWAYS_IDENT,
                    Iicolumns.COLUMN_BYDEFAULT_IDENT,
                    trim(IidbSubcomments.LONG_REMARK))
                .from(IICOLUMNS)
                .leftOuterJoin(IIDB_SUBCOMMENTS)
                .on(IidbSubcomments.OBJECT_NAME.equal(Iicolumns.TABLE_NAME))
                .and(IidbSubcomments.OBJECT_OWNER.equal(Iicolumns.TABLE_OWNER))
                .and(IidbSubcomments.SUBOBJECT_NAME.equal(Iicolumns.COLUMN_NAME))
                .and(IidbSubcomments.SUBOBJECT_TYPE.equal("C"))
                .and(IidbSubcomments.TEXT_SEQUENCE.equal(1L))
                .where(Iicolumns.TABLE_OWNER.equal(getSchema().getName()))
                .and(trim(Iicolumns.TABLE_NAME).equal(getName()))
                .orderBy(Iicolumns.COLUMN_SEQUENCE)
                .fetch()) {

            String typeName = record.getValue(trim(Iicolumns.COLUMN_DATATYPE));

            // [#664] INTEGER types come with a COLUMN_LENGTH in bytes
            // This is important to distinguish BIGINT, INT, SMALLINT, TINYINT
            if (IngresDataType.INTEGER.getTypeName().equalsIgnoreCase(typeName)) {
                switch (record.getValue(Iicolumns.COLUMN_LENGTH)) {
                    case 8:
                        typeName = IngresDataType.BIGINT.getTypeName();
                        break;
                    case 2:
                        typeName = IngresDataType.SMALLINT.getTypeName();
                        break;
                    case 1:
                        typeName = IngresDataType.TINYINT.getTypeName();
                        break;
                }
            }

            // [#744] FLOAT types have the same issue
            else if (IngresDataType.FLOAT.getTypeName().equalsIgnoreCase(typeName)) {
                switch (record.getValue(Iicolumns.COLUMN_LENGTH)) {
                    case 8:
                        typeName = IngresDataType.FLOAT8.getTypeName();
                        break;
                    case 4:
                        typeName = IngresDataType.FLOAT4.getTypeName();
                        break;
                }
            }

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                typeName,
                record.getValue(Iicolumns.COLUMN_LENGTH),
                record.getValue(Iicolumns.COLUMN_LENGTH),
                record.getValue(Iicolumns.COLUMN_SCALE),
                record.getValue(Iicolumns.COLUMN_NULLS, boolean.class),
                record.getValue(Iicolumns.COLUMN_HAS_DEFAULT, boolean.class)
            );

            ColumnDefinition column = new DefaultColumnDefinition(
                getDatabase().getTable(getSchema(), getName()),
                record.getValue(trim(Iicolumns.COLUMN_NAME)),
                record.getValue(Iicolumns.COLUMN_SEQUENCE),
                type,
                record.getValue(Iicolumns.COLUMN_ALWAYS_IDENT, boolean.class) ||
                record.getValue(Iicolumns.COLUMN_BYDEFAULT_IDENT, boolean.class),
                record.getValue(trim(IidbSubcomments.LONG_REMARK)));
            result.add(column);
        }

        return result;
    }
}
