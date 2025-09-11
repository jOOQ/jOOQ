/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
 *
 * For more information, please visit: https://www.jooq.org/legal/licensing
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

import static org.jooq.impl.DSL.bitOr;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.lower;
import static org.jooq.impl.DSL.nvl;
import static org.jooq.impl.DSL.substring;
import static org.jooq.impl.DSL.trim;
import static org.jooq.impl.DSL.when;
import static org.jooq.meta.firebird.FirebirdDatabase.CHARACTER_LENGTH;
import static org.jooq.meta.firebird.FirebirdDatabase.FIELD_SCALE;
import static org.jooq.meta.firebird.FirebirdDatabase.FIELD_TYPE;
import static org.jooq.meta.firebird.rdb.Tables.RDB$FIELDS;
import static org.jooq.meta.firebird.rdb.Tables.RDB$FUNCTION_ARGUMENTS;
import static org.jooq.meta.firebird.rdb.Tables.RDB$PROCEDURE_PARAMETERS;

import java.sql.SQLException;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.TableField;
import org.jooq.impl.DSL;
import org.jooq.meta.AbstractRoutineDefinition;
import org.jooq.meta.DataTypeDefinition;
import org.jooq.meta.DefaultDataTypeDefinition;
import org.jooq.meta.DefaultParameterDefinition;
import org.jooq.meta.InOutDefinition;
import org.jooq.meta.ParameterDefinition;
import org.jooq.meta.SchemaDefinition;
import org.jooq.meta.firebird.rdb.tables.Rdb$fields;
import org.jooq.meta.firebird.rdb.tables.Rdb$functionArguments;
import org.jooq.meta.firebird.rdb.tables.Rdb$procedureParameters;
import org.jooq.tools.StringUtils;

/**
 * @author Lukas Eder
 */
public class FirebirdRoutineDefinition extends AbstractRoutineDefinition {

    public FirebirdRoutineDefinition(SchemaDefinition schema, String name) {
        this(schema, name, null, null, null);
    }

    public FirebirdRoutineDefinition(SchemaDefinition schema, String name, String dataType, Number precision, Number scale) {
        super(schema, null, name, null, null, false);

        if (!StringUtils.isBlank(dataType)) {
            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                dataType,
                precision,
                precision,
                scale,
                null,
                (String) null
            );

            this.returnValue = new DefaultParameterDefinition(this, "RETURN_VALUE", -1, type);
        }
    }

    @Override
    protected void init0() throws SQLException {
        Rdb$procedureParameters p = RDB$PROCEDURE_PARAMETERS.as("p");
        Rdb$functionArguments a = RDB$FUNCTION_ARGUMENTS.as("a");
        Rdb$fields f = RDB$FIELDS.as("f");
        int i = 0;

        for (Record record : returnValue == null
                ? create()
                    .select(
                        p.RDB$PARAMETER_NUMBER,
                        p.RDB$PARAMETER_TYPE,
                        trim(p.RDB$PARAMETER_NAME).as(p.RDB$PARAMETER_NAME),
                        FIELD_TYPE(f).as("FIELD_TYPE"),
                        CHARACTER_LENGTH(f).as("CHAR_LEN"),
                        f.RDB$FIELD_PRECISION,
                        FIELD_SCALE(f).as("FIELD_SCALE"),
                        bitOr(nvl(p.RDB$NULL_FLAG, inline((short) 0)), nvl(f.RDB$NULL_FLAG, inline((short) 0))).as(p.RDB$NULL_FLAG),
                        removeDefault(p.RDB$DEFAULT_SOURCE).as(p.RDB$DEFAULT_SOURCE))
                    .from(p)
                    .leftOuterJoin(f).on(p.RDB$FIELD_SOURCE.eq(f.RDB$FIELD_NAME))
                    .where(p.RDB$PROCEDURE_NAME.eq(getName()))
                    .orderBy(
                        p.RDB$PARAMETER_TYPE.desc(),
                        p.RDB$PARAMETER_NUMBER.asc())
                : create()
                    .select(
                        a.RDB$ARGUMENT_POSITION.as(p.RDB$PARAMETER_NUMBER),
                        inline(0).as(p.RDB$PARAMETER_TYPE),
                        trim(a.RDB$ARGUMENT_NAME).as(p.RDB$PARAMETER_NAME),
                        FIELD_TYPE(f).as("FIELD_TYPE"),
                        CHARACTER_LENGTH(f).as("CHAR_LEN"),
                        f.RDB$FIELD_PRECISION,
                        FIELD_SCALE(f).as("FIELD_SCALE"),
                        bitOr(nvl(a.RDB$NULL_FLAG, inline((short) 0)), nvl(f.RDB$NULL_FLAG, inline((short) 0))).as(p.RDB$NULL_FLAG),
                        removeDefault(a.RDB$DEFAULT_SOURCE).as(p.RDB$DEFAULT_SOURCE))
                    .from(a)
                    .leftOuterJoin(f).on(a.RDB$FIELD_SOURCE.eq(f.RDB$FIELD_NAME))
                    .where(a.RDB$FUNCTION_NAME.eq(getName()))
                    .and(a.RDB$ARGUMENT_POSITION.gt(inline((short) 0)))
                    .orderBy(a.RDB$ARGUMENT_POSITION)
            ) {

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                record.get("FIELD_TYPE", String.class),
                record.get("CHAR_LEN", short.class),
                record.get(f.RDB$FIELD_PRECISION),
                record.get("FIELD_SCALE", Integer.class),
                record.get(p.RDB$NULL_FLAG) == 0,
                record.get(p.RDB$DEFAULT_SOURCE)
            );

            ParameterDefinition parameter = new DefaultParameterDefinition(
                this,
                record.get(p.RDB$PARAMETER_NAME),
                i++,
                type
            );

            addParameter(record.get(p.RDB$PARAMETER_TYPE, int.class).equals(0) ? InOutDefinition.IN : InOutDefinition.OUT, parameter);
        }

    }

    private Field<String> removeDefault(Field<String> f) {
        return when(f.like(inline("=%")), trim(substring(f, inline(2))))
            .when(lower(f).like("default %"), trim(substring(f, inline(8))));
    }
}
