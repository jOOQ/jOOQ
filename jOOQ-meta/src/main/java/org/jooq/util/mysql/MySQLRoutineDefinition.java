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

package org.jooq.util.mysql;

import static java.util.Arrays.asList;
import static org.jooq.util.hsqldb.information_schema.Tables.PARAMETERS;

import java.util.regex.Matcher;

import org.jooq.Record;
import org.jooq.tools.StringUtils;
import org.jooq.util.AbstractRoutineDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.DefaultDataTypeDefinition;
import org.jooq.util.DefaultParameterDefinition;
import org.jooq.util.InOutDefinition;
import org.jooq.util.ParameterDefinition;
import org.jooq.util.SchemaDefinition;
import org.jooq.util.mysql.information_schema.tables.Parameters;
import org.jooq.util.mysql.mysql.enums.ProcType;

/**
 * @author Lukas Eder
 */
public class MySQLRoutineDefinition extends AbstractRoutineDefinition {

    private Boolean is55;

    private final String params;
    private final String returns;
    private final ProcType procType;

    /**
     * @deprecated - This constructor was deprecated with jOOQ 3.2
     */
    @Deprecated
    public MySQLRoutineDefinition(SchemaDefinition schema, String name, String comment, String params, String returns) {
        this(schema, name, comment, params, returns, null, null);
    }

    public MySQLRoutineDefinition(SchemaDefinition schema, String name, String comment, String params, String returns, ProcType procType, String overload) {
        super(schema, null, name, comment, overload);

        this.params = params;
        this.returns = returns;
        this.procType = procType;
    }

    @Override
    protected void init0() {
        if (is55()) {
            init55();
        }
        else {
            init54();
        }
    }

    private void init55() {

        // [#742] In MySQL 5.5 and later, the INFORMATION_SCHEMA.PARAMETERS
        // table is available, which is much more reliable than mysql.proc
        for (Record record : create()
                .select(
                    Parameters.ORDINAL_POSITION,
                    Parameters.PARAMETER_NAME,
                    Parameters.PARAMETER_MODE,
                    Parameters.DATA_TYPE,
                    Parameters.DTD_IDENTIFIER,
                    Parameters.CHARACTER_MAXIMUM_LENGTH,
                    Parameters.NUMERIC_PRECISION,
                    Parameters.NUMERIC_SCALE
                )
                .from(PARAMETERS)
                .where(Parameters.SPECIFIC_SCHEMA.eq(getSchema().getInputName()))
                .and(Parameters.SPECIFIC_NAME.eq(getInputName()))
                .and(Parameters.ROUTINE_TYPE.eq(procType.name()))
                .orderBy(Parameters.ORDINAL_POSITION.asc())
                .fetch()) {

            String inOut = record.get(Parameters.PARAMETER_MODE);
            String dataType = record.get(Parameters.DATA_TYPE);

            // [#519] Some types have unsigned versions
            if (getDatabase().supportsUnsignedTypes()) {
                if (asList("tinyint", "smallint", "mediumint", "int", "bigint").contains(dataType.toLowerCase())) {
                    if (record.get(Parameters.DTD_IDENTIFIER).toLowerCase().contains("unsigned")) {
                        dataType += "unsigned";
                    }
                }
            }

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                dataType,
                record.get(Parameters.CHARACTER_MAXIMUM_LENGTH),
                record.get(Parameters.NUMERIC_PRECISION),
                record.get(Parameters.NUMERIC_SCALE),
                null,
                (String) null
            );

            if (inOut == null) {
                addParameter(InOutDefinition.RETURN, new DefaultParameterDefinition(this, "RETURN_VALUE", -1, type));
            }
            else {
                ParameterDefinition parameter = new DefaultParameterDefinition(
                    this,
                    record.get(Parameters.PARAMETER_NAME).replaceAll("@", ""),
                    record.get(Parameters.ORDINAL_POSITION, int.class),
                    type);

                addParameter(InOutDefinition.getFromString(inOut), parameter);
            }
        }
    }

    private void init54() {

        // [#742] Before MySQL 5.5, the INFORMATION_SCHEMA.PARAMETERS table was
        // not yet available. Resort to mysql.proc and regex-pattern matching.

        // [#738] Avoid matching commas that appear in types, for instance DECIMAL(2, 1)
        String[] split = params.split(",(?!\\s*\\d+\\s*\\))");

        Matcher matcher = TYPE_PATTERN.matcher(returns);
        if (matcher.find()) {
            addParameter(InOutDefinition.RETURN, createParameter(matcher, 0, -1, "RETURN_VALUE"));
        }

        for (int i = 0; i < split.length; i++) {
            String param = split[i];

            // TODO [#742] : Use the INFORMATION_SCHEMA.PARAMETERS dictionary view instead.
            // It's much more reliable, than mysql.proc pattern matching...

            param = param.trim();
            matcher = PARAMETER_PATTERN.matcher(param);
            while (matcher.find()) {
                InOutDefinition inOut = InOutDefinition.getFromString(matcher.group(2));
                addParameter(inOut, createParameter(matcher, 3, i + 1));
            }
        }
    }

    private ParameterDefinition createParameter(Matcher matcher, int group, int columnIndex) {
        return createParameter(matcher, group, columnIndex, matcher.group(group));
    }

    private ParameterDefinition createParameter(Matcher matcher, int group, int columnIndex, String paramName) {
        String paramType = matcher.group(group + 1);

        Number precision = 0;
        Number scale = 0;

        if (!StringUtils.isBlank(matcher.group(group + 2))) {
            precision = Integer.valueOf(matcher.group(group + 2));
        }
        if (!StringUtils.isBlank(matcher.group(group + 3))) {
            scale = Integer.valueOf(matcher.group(group + 3));
        }

        DataTypeDefinition type = new DefaultDataTypeDefinition(
            getDatabase(),
            getSchema(),
            paramType,
            precision,
            precision,
            scale,
            null,
            (String) null
        );

        return new DefaultParameterDefinition(this, paramName, columnIndex, type);
    }

    private boolean is55() {

        // Check if this is a MySQL 5.5 or later database
        if (is55 == null) {
            try {
                create().selectOne().from(PARAMETERS).limit(1).fetchOne();
                is55 = true;
            }
            catch (Exception e) {
                is55 = false;
            }
        }

        return is55;
    }
}
