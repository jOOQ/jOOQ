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

package org.jooq.util.cubrid;

import static org.jooq.util.cubrid.dba.Tables.DB_ATTRIBUTE;
import static org.jooq.util.cubrid.dba.Tables.DB_SERIAL;

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
 * @author Lukas Eder
 */
public class CUBRIDTableDefinition extends AbstractTableDefinition {

    public CUBRIDTableDefinition(SchemaDefinition schema, String name, String comment) {
        super(schema, name, comment);
	}

	@Override
	public List<ColumnDefinition> getElements0() throws SQLException {
		List<ColumnDefinition> result = new ArrayList<ColumnDefinition>();

		 for (Record record : create()
		         .select(
        		     DB_ATTRIBUTE.ATTR_NAME,
		             DB_ATTRIBUTE.DEF_ORDER,
		             DB_ATTRIBUTE.DATA_TYPE,
		             DB_ATTRIBUTE.PREC,
		             DB_ATTRIBUTE.SCALE,
		             DB_ATTRIBUTE.IS_NULLABLE,
		             DB_ATTRIBUTE.DEFAULT_VALUE,
		             DB_SERIAL.NAME)
		        .from(DB_ATTRIBUTE)
		        .leftOuterJoin(DB_SERIAL).on(
		            DB_ATTRIBUTE.ATTR_NAME.equal(DB_SERIAL.ATT_NAME).and(
	                DB_ATTRIBUTE.CLASS_NAME.equal(DB_SERIAL.CLASS_NAME)))
		        .where(DB_ATTRIBUTE.CLASS_NAME.equal(getName()))
		        .orderBy(DB_ATTRIBUTE.DEF_ORDER)
		        .fetch()) {

		    String dataType = record.get(DB_ATTRIBUTE.DATA_TYPE);

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                dataType,
                record.get(DB_ATTRIBUTE.PREC),
                record.get(DB_ATTRIBUTE.PREC),
                record.get(DB_ATTRIBUTE.SCALE),
                record.get(DB_ATTRIBUTE.IS_NULLABLE, boolean.class),
                record.get(DB_ATTRIBUTE.DEFAULT_VALUE),
                getName() + "_" + record.get(DB_ATTRIBUTE.ATTR_NAME)
            );

			ColumnDefinition column = new DefaultColumnDefinition(
				getDatabase().getTable(getSchema(), getName()),
			    record.get(DB_ATTRIBUTE.ATTR_NAME),
			    record.get(DB_ATTRIBUTE.DEF_ORDER),
			    type,
			    record.get(DB_SERIAL.NAME) != null,
			    null
		    );

			result.add(column);
		}

		return result;
	}
}
