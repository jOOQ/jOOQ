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

package org.jooq.meta.hsqldb;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Field;
import org.jooq.TableOptions.TableType;
import org.jooq.meta.AbstractTableDefinition;
import org.jooq.meta.ColumnDefinition;
import org.jooq.meta.DataTypeDefinition;
import org.jooq.meta.DefaultColumnDefinition;
import org.jooq.meta.DefaultDataTypeDefinition;
import org.jooq.meta.ParameterDefinition;
import org.jooq.meta.SchemaDefinition;

/**
 * @author Lukas Eder
 */
public class HSQLDBTableValuedFunction extends AbstractTableDefinition {

    private final HSQLDBRoutineDefinition routine;
    private final String                  source;

    public HSQLDBTableValuedFunction(SchemaDefinition schema, String name, String specificName, String comment, String source) {
        super(schema, name, comment, TableType.FUNCTION, source);

        this.routine = new HSQLDBRoutineDefinition(schema, name, specificName, "ROW", 0, 0);
        this.source = source;
    }

    @Override
    public List<ColumnDefinition> getElements0() throws SQLException {
        List<ColumnDefinition> result = new ArrayList<>();

        Field<?>[] fields = create()
                .meta(source.replaceAll(".*? RETURNS TABLE(.*)SPECIFIC .*", "CREATE TABLE X $1"))
                .getTables("X")
                .get(0)
                .fields();
        for (int i = 0; i < fields.length; i++) {
            Field<?> field = fields[i];

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                field.getDataType().getTypeName(create().configuration()),
                field.getDataType().length(),
                field.getDataType().precision(),
                field.getDataType().scale(),
                field.getDataType().nullable(),
                (String) null
            );

            result.add(new DefaultColumnDefinition(
                getDatabase().getTable(getSchema(), getName()),
                field.getName(),
                result.size() + 1,
                type,
                false,
                null
            ));
        }

        return result;
    }

    @Override
    protected List<ParameterDefinition> getParameters0() {
        return routine.getInParameters();
    }
}
