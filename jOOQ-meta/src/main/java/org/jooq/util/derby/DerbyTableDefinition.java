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

package org.jooq.util.derby;

import static org.jooq.impl.DSL.inline;
import static org.jooq.util.derby.sys.tables.Syscolumns.SYSCOLUMNS;

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
import org.jooq.util.derby.sys.tables.Syscolumns;

/**
 * @author Lukas Eder
 */
public class DerbyTableDefinition extends AbstractTableDefinition {

    private final String         tableid;

    public DerbyTableDefinition(SchemaDefinition schema, String name, String tableid) {
		super(schema, name, "");

		this.tableid = tableid;
	}

	@Override
	public List<ColumnDefinition> getElements0() throws SQLException {
		List<ColumnDefinition> result = new ArrayList<ColumnDefinition>();

        for (Record record : create().select(
                Syscolumns.COLUMNNAME,
                Syscolumns.COLUMNNUMBER,
                Syscolumns.COLUMNDATATYPE,
                Syscolumns.COLUMNDEFAULT,
                Syscolumns.AUTOINCREMENTINC)
            .from(SYSCOLUMNS)
            // [#1241] Suddenly, bind values didn't work any longer, here...
            .where(Syscolumns.REFERENCEID.equal(inline(tableid)))
            .orderBy(Syscolumns.COLUMNNUMBER)
            .fetch()) {

            String typeName = record.get(Syscolumns.COLUMNDATATYPE, String.class);
            Number precision = parsePrecision(typeName);
            Number scale = parseScale(typeName);

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                parseTypeName(typeName),
                precision,
                precision,
                scale,
                !parseNotNull(typeName),
                record.get(Syscolumns.COLUMNDEFAULT)
            );

			ColumnDefinition column = new DefaultColumnDefinition(
				getDatabase().getTable(getSchema(), getName()),
			    record.get(Syscolumns.COLUMNNAME),
			    record.get(Syscolumns.COLUMNNUMBER),
			    type,
                null != record.get(Syscolumns.AUTOINCREMENTINC),
                null
            );

			result.add(column);
		}

		return result;
	}
}
