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

package org.jooq.util.hana;

import static org.jooq.impl.DSL.decode;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.select;
import static org.jooq.util.hana.sys.Tables.ELEMENT_TYPES;
import static org.jooq.util.hana.sys.Tables.TABLE_COLUMNS;
import static org.jooq.util.hana.sys.Tables.VIEW_COLUMNS;

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
import org.jooq.util.hana.sys.tables.ElementTypes;
import org.jooq.util.hana.sys.tables.TableColumns;
import org.jooq.util.hana.sys.tables.ViewColumns;

/**
 * @author Lukas Eder
 */
public class HanaTableDefinition extends AbstractTableDefinition {

	public HanaTableDefinition(SchemaDefinition schema, String name, String comment) {
		super(schema, name, comment);
	}

	@Override
	public List<ColumnDefinition> getElements0() throws SQLException {
		List<ColumnDefinition> result = new ArrayList<ColumnDefinition>();

        TableColumns tc = TABLE_COLUMNS;
        ViewColumns vc = VIEW_COLUMNS;
        ElementTypes et = ELEMENT_TYPES;

        for (Record record : create()
            .select(
                tc.COLUMN_NAME,
                tc.POSITION,
                decode()
                    .when(et.DATA_TYPE_NAME.isNotNull(), et.DATA_TYPE_NAME.concat(inline(" ARRAY")))
                    .otherwise(tc.DATA_TYPE_NAME).as(tc.DATA_TYPE_NAME),
                tc.GENERATED_ALWAYS_AS,
                tc.IS_NULLABLE,
                tc.DEFAULT_VALUE,
                tc.LENGTH,
                tc.SCALE
            )
            .from(tc
                .leftOuterJoin(et)
                .on(et.SCHEMA_NAME.eq(tc.SCHEMA_NAME))
                .and(et.OBJECT_NAME.eq(tc.TABLE_NAME))
                .and(et.OBJECT_OID.eq(tc.TABLE_OID))
                .and(et.ELEMENT_NAME.eq(tc.COLUMN_NAME)))
            .where(tc.SCHEMA_NAME.equal(getSchema().getName()))
            .and(tc.TABLE_NAME.equal(getName()))
            .unionAll(
                 select(
                    vc.COLUMN_NAME,
                    vc.POSITION,
                    decode()
                        .when(et.DATA_TYPE_NAME.isNotNull(), et.DATA_TYPE_NAME.concat(inline(" ARRAY")))
                        .otherwise(vc.DATA_TYPE_NAME).as(vc.DATA_TYPE_NAME),
                    vc.GENERATED_ALWAYS_AS,
                    vc.IS_NULLABLE,
                    vc.DEFAULT_VALUE,
                    vc.LENGTH,
                    vc.SCALE
                )
                .from(vc
                    .leftOuterJoin(et)
                    .on(et.SCHEMA_NAME.eq(vc.SCHEMA_NAME))
                    .and(et.OBJECT_NAME.eq(vc.VIEW_NAME))
                    .and(et.OBJECT_OID.eq(vc.VIEW_OID))
                    .and(et.ELEMENT_NAME.eq(vc.COLUMN_NAME)))
                .where(vc.SCHEMA_NAME.equal(getSchema().getName()))
                .and(vc.VIEW_NAME.equal(getName()))
            )
            .orderBy(field(name(tc.POSITION.getName())))) {

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                record.getValue(tc.DATA_TYPE_NAME),
                record.getValue(tc.LENGTH),
                record.getValue(tc.LENGTH),
                record.getValue(tc.SCALE),
                record.getValue(tc.IS_NULLABLE, boolean.class),
                record.getValue(tc.DEFAULT_VALUE) != null
            );

			ColumnDefinition column = new DefaultColumnDefinition(
			    getDatabase().getTable(getSchema(), getName()),
			    record.getValue(tc.COLUMN_NAME, String.class),
			    record.getValue(tc.POSITION, int.class),
			    type,
			    null != record.getValue(tc.GENERATED_ALWAYS_AS),
			    null
		    );

			result.add(column);
		}

		return result;
	}
}
