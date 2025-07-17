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

package org.jooq.meta.trino;

import java.util.ArrayList;
import java.util.List;

import org.jooq.Parameter;
import org.jooq.Query;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultDataType;
import org.jooq.impl.QOM;
import org.jooq.meta.AbstractRoutineDefinition;
import org.jooq.meta.DataTypeDefinition;
import org.jooq.meta.DefaultDataTypeDefinition;
import org.jooq.meta.DefaultParameterDefinition;
import org.jooq.meta.InOutDefinition;
import org.jooq.meta.Logging;
import org.jooq.meta.SchemaDefinition;
import org.jooq.tools.StringUtils;

/**
 * @author Lukas Eder
 */
public class TrinoRoutineDefinition extends AbstractRoutineDefinition {

    private final String paramTypes;

    public TrinoRoutineDefinition(
        SchemaDefinition schema,
        String name,
        String comment,
        String overload,
        String returnType,
        String paramTypes,
        String source
    ) {
        super(schema, null, name, comment, overload, false, source);

        if (!StringUtils.isBlank(returnType)) {
            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                returnType,
                0,
                0,
                0,
                null,
                (String) null
            );

            this.returnValue = new DefaultParameterDefinition(this, "RETURN_VALUE", -1, type);
        }

        this.paramTypes = paramTypes;
    }

    @Override
    protected void init0() {
        List<Parameter<?>> params = new ArrayList<>();














        if (params.isEmpty() && !StringUtils.isBlank(paramTypes)) {
            int i = 0;
            for (String p : paramTypes.split(",(?!\\s*\\d+\\s*\\))")) {
                i++;
                params.add(DSL.in("p" + i, DefaultDataType.getDataType(getDialect(), p)));
            }
        }

        int i = 0;
        for (Parameter<?> param : params) {
            i++;
            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                param.getDataType().getTypeName(),
                param.getDataType().length(),
                param.getDataType().precision(),
                param.getDataType().scale(),
                null,
                (String) null
            );

            addParameter(InOutDefinition.IN, new DefaultParameterDefinition(this, param.getName(), i, type));
        }
    }
}
