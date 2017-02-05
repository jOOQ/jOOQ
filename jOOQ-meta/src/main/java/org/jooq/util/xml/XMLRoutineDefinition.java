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

import static org.jooq.impl.DSL.name;
import static org.jooq.tools.StringUtils.isBlank;

import org.jooq.Name;
import org.jooq.tools.StringUtils;
import org.jooq.util.AbstractRoutineDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.DefaultDataTypeDefinition;
import org.jooq.util.DefaultParameterDefinition;
import org.jooq.util.InOutDefinition;
import org.jooq.util.PackageDefinition;
import org.jooq.util.ParameterDefinition;
import org.jooq.util.SchemaDefinition;
import org.jooq.util.xml.jaxb.InformationSchema;
import org.jooq.util.xml.jaxb.Parameter;
import org.jooq.util.xml.jaxb.Routine;

/**
 * @author Lukas Eder
 */
public class XMLRoutineDefinition extends AbstractRoutineDefinition {

    private final InformationSchema info;

    public XMLRoutineDefinition(SchemaDefinition schema, PackageDefinition pkg, InformationSchema info, Routine routine) {
        super(schema, pkg, routine.getRoutineName(), "", overload(info, routine));

        this.info = info;

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

    private static String overload(InformationSchema info, Routine routine) {
        Name routineName = name(
            routine.getRoutineCatalog(),
            routine.getRoutineSchema(),
            routine.getRoutinePackage(),
            routine.getRoutineName()
        );

        String result = null;
        int count = 0;

        // TODO: Better algorithm by pre-calculating
        for (Routine r : info.getRoutines()) {
            Name rName = name(
                r.getRoutineCatalog(),
                r.getRoutineSchema(),
                r.getRoutinePackage(),
                r.getRoutineName()
            );

            if (routineName.equals(rName)) {
                count++;

                if (routine == r) {
                    result = "" + count;
                }

                if (count > 1 && result != null)
                    break;
            }
        }

        return count > 1 ? result : null;
    }

    @Override
    protected void init0() {
        for (Parameter parameter : info.getParameters()) {
            Name parameterRoutineName = name(
                parameter.getSpecificCatalog(),
                parameter.getSpecificSchema(),
                parameter.getSpecificPackage(),
                parameter.getSpecificName()
            );

            if (getQualifiedNamePart().equals(parameterRoutineName)) {
                DataTypeDefinition type = new DefaultDataTypeDefinition(
                    getDatabase(),
                    getSchema(),
                    parameter.getDataType(),
                    parameter.getCharacterMaximumLength(),
                    parameter.getNumericPrecision(),
                    parameter.getNumericScale(),
                    null,
                    parameter.getParameterDefault()
                );

                ParameterDefinition p = new DefaultParameterDefinition(
                    this,
                    parameter.getParameterName(),
                    parameter.getOrdinalPosition(),
                    type,
                    !StringUtils.isBlank(parameter.getParameterDefault()),
                    StringUtils.isBlank(parameter.getParameterName())
                );

                switch (parameter.getParameterMode()) {
                    case IN:
                        addParameter(InOutDefinition.IN, p);
                        break;
                    case INOUT:
                        addParameter(InOutDefinition.INOUT, p);
                        break;
                    case OUT:
                        addParameter(InOutDefinition.OUT, p);
                        break;
                }
            }
        }
    }
}
