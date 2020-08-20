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
 *
 *
 *
 */

package org.jooq.meta.derby;

import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.when;
import static org.jooq.impl.SQLDataType.VARCHAR;
import static org.jooq.meta.derby.sys.Tables.SYSCOLUMNS;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Record;
import org.jooq.TableOptions.TableType;
import org.jooq.meta.AbstractTableDefinition;
import org.jooq.meta.ColumnDefinition;
import org.jooq.meta.DataTypeDefinition;
import org.jooq.meta.DefaultColumnDefinition;
import org.jooq.meta.DefaultDataTypeDefinition;
import org.jooq.meta.SchemaDefinition;

/**
 * @author Lukas Eder
 */
public class DerbyTableDefinition extends AbstractTableDefinition {

    private final String         tableid;

    public DerbyTableDefinition(SchemaDefinition schema, String name, String tableid, TableType tableType, String source) {
		super(schema, name, "", tableType, source);

		this.tableid = tableid;
	}

	@Override
	public List<ColumnDefinition> getElements0() throws SQLException {
		List<ColumnDefinition> result = new ArrayList<>();

        for (Record record : create().select(
                SYSCOLUMNS.COLUMNNAME,
                SYSCOLUMNS.COLUMNNUMBER,
                SYSCOLUMNS.COLUMNDATATYPE,
                when(SYSCOLUMNS.AUTOINCREMENTINC.isNull(), SYSCOLUMNS.COLUMNDEFAULT).as(SYSCOLUMNS.COLUMNDEFAULT),
                SYSCOLUMNS.AUTOINCREMENTINC)
            .from(SYSCOLUMNS)
            // [#1241] Suddenly, bind values didn't work any longer, here...
            // [#6797] The cast is necessary if a non-standard collation is used
            .where(SYSCOLUMNS.REFERENCEID.cast(VARCHAR(32672)).equal(inline(tableid)))
            .orderBy(SYSCOLUMNS.COLUMNNUMBER)) {

            String columnDataType = record.get(SYSCOLUMNS.COLUMNDATATYPE, String.class);
            String typeName = parseTypeName(columnDataType);

            // [#9945] Derby timestamps always have a precision of 9
            Number precision = "TIMESTAMP".equalsIgnoreCase(typeName) ? 9 : parsePrecision(columnDataType);
            Number scale = parseScale(columnDataType);

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                typeName,
                precision,
                precision,
                scale,
                !parseNotNull(columnDataType),
                record.get(SYSCOLUMNS.COLUMNDEFAULT)
            );

			result.add(new DefaultColumnDefinition(
				getDatabase().getTable(getSchema(), getName()),
			    record.get(SYSCOLUMNS.COLUMNNAME),
			    result.size() + 1,
			    type,
                null != record.get(SYSCOLUMNS.AUTOINCREMENTINC),
                null
            ));
		}

		return result;
	}
}
