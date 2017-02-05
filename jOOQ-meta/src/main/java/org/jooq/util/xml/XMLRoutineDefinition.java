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
package org.jooq.util.xml;

import static org.jooq.tools.StringUtils.isBlank;

import org.jooq.util.AbstractRoutineDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.DefaultDataTypeDefinition;
import org.jooq.util.DefaultParameterDefinition;
import org.jooq.util.PackageDefinition;
import org.jooq.util.SchemaDefinition;
import org.jooq.util.xml.jaxb.InformationSchema;
import org.jooq.util.xml.jaxb.Routine;

/**
 * @author Lukas Eder
 */
public class XMLRoutineDefinition extends AbstractRoutineDefinition {

    private final InformationSchema info;
    private final Routine           routine;

    public XMLRoutineDefinition(SchemaDefinition schema, PackageDefinition pkg, InformationSchema info, Routine routine) {
        super(schema, pkg, routine.getRoutineName(), "", null);

        this.info = info;
        this.routine = routine;

        if (!isBlank(routine.getDataType())) {
            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                schema,
                routine.getDataType(),
                routine.getCharacterMaximumLength(),
                routine.getNumericPrecision(),
                routine.getNumericScale(),
                null,
                (String) null
            );

            this.returnValue = new DefaultParameterDefinition(this, "RETURN_VALUE", -1, type);
        }
    }

    @Override
    protected void init0() {
        for (Object parameter : info.getParameters()) {

        }
    }
}
