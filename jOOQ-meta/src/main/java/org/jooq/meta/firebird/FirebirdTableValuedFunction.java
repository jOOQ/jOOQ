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
package org.jooq.meta.firebird;

import static org.jooq.meta.firebird.FirebirdDatabase.CHARACTER_LENGTH;
import static org.jooq.meta.firebird.FirebirdDatabase.FIELD_SCALE;
import static org.jooq.meta.firebird.FirebirdDatabase.FIELD_TYPE;
import static org.jooq.meta.firebird.rdb.Tables.RDB$FIELDS;
import static org.jooq.meta.firebird.rdb.Tables.RDB$PROCEDURE_PARAMETERS;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Record;
import org.jooq.TableOptions.TableType;
import org.jooq.impl.DSL;
import org.jooq.meta.AbstractTableDefinition;
import org.jooq.meta.ColumnDefinition;
import org.jooq.meta.DefaultColumnDefinition;
import org.jooq.meta.DefaultDataTypeDefinition;
import org.jooq.meta.ParameterDefinition;
import org.jooq.meta.SchemaDefinition;
import org.jooq.meta.firebird.rdb.tables.Rdb$fields;
import org.jooq.meta.firebird.rdb.tables.Rdb$procedureParameters;

/**
 * @author Lukas Eder
 */
public class FirebirdTableValuedFunction extends AbstractTableDefinition {

    private final FirebirdRoutineDefinition routine;

    public FirebirdTableValuedFunction(SchemaDefinition schema, String name, String comment) {
        this(schema, name, comment, null);
    }

    public FirebirdTableValuedFunction(SchemaDefinition schema, String name, String comment, String source) {
        super(schema, name, comment, TableType.FUNCTION, source);

        routine = new FirebirdRoutineDefinition(schema, name);
    }

    @Override
    protected List<ColumnDefinition> getElements0() throws SQLException {
        List<ColumnDefinition> result = new ArrayList<>();

        Rdb$procedureParameters p = RDB$PROCEDURE_PARAMETERS.as("p");
        Rdb$fields f = RDB$FIELDS.as("f");

        // Inspiration for the below query was taken from jaybird's
        // DatabaseMetaData implementation
        for (Record record : create()
                .select(
                    p.RDB$PARAMETER_NUMBER,
                    p.RDB$PARAMETER_NAME.trim(),
                    p.RDB$DESCRIPTION,
                    p.RDB$DEFAULT_VALUE,
                    DSL.bitOr(p.RDB$NULL_FLAG.nvl((short) 0), f.RDB$NULL_FLAG.nvl((short) 0)).as(p.RDB$NULL_FLAG),
                    p.RDB$DEFAULT_SOURCE,

                    // [#3342] FIELD_LENGTH should be ignored for LOBs
                    CHARACTER_LENGTH(f).as("CHAR_LEN"),
                    f.RDB$FIELD_PRECISION,
                    FIELD_SCALE(f).as("FIELD_SCALE"),
                    FIELD_TYPE(f).as("FIELD_TYPE"),
                    f.RDB$FIELD_SUB_TYPE)
                .from(p)
                .leftOuterJoin(f).on(p.RDB$FIELD_SOURCE.eq(f.RDB$FIELD_NAME))
                .where(p.RDB$PROCEDURE_NAME.eq(getName()))
                .and(p.RDB$PARAMETER_TYPE.eq((short) 1))
                .orderBy(p.RDB$PARAMETER_NUMBER)) {

            DefaultDataTypeDefinition type = new DefaultDataTypeDefinition(
                    getDatabase(),
                    getSchema(),
                    record.get("FIELD_TYPE", String.class),
                    record.get("CHAR_LEN", short.class),
                    record.get(f.RDB$FIELD_PRECISION),
                    record.get("FIELD_SCALE", Integer.class),
                    record.get(p.RDB$NULL_FLAG) == 0,
                    record.get(p.RDB$DEFAULT_SOURCE)
            );

            ColumnDefinition column = new DefaultColumnDefinition(
                    getDatabase().getTable(getSchema(), getName()),
                    record.get(p.RDB$PARAMETER_NAME.trim()),
                    record.get(p.RDB$PARAMETER_NUMBER),
                    type,
                    false,
                    null
            );

            result.add(column);
        }

        return result;
    }

    @Override
    protected List<ParameterDefinition> getParameters0() {
        return routine.getInParameters();
    }
}
