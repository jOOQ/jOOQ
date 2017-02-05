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

import static org.jooq.tools.StringUtils.defaultIfBlank;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.util.AbstractPackageDefinition;
import org.jooq.util.AttributeDefinition;
import org.jooq.util.RoutineDefinition;
import org.jooq.util.SchemaDefinition;
import org.jooq.util.xml.jaxb.InformationSchema;
import org.jooq.util.xml.jaxb.Routine;

/**
 * @author Lukas Eder
 */
public class XMLPackageDefinition extends AbstractPackageDefinition {

    private final InformationSchema info;

    public XMLPackageDefinition(SchemaDefinition schema, InformationSchema info, String packageName) {
        super(schema, packageName, "");

        this.info = info;
    }

    @Override
    protected List<RoutineDefinition> getRoutines0() throws SQLException {
        List<RoutineDefinition> result = new ArrayList<RoutineDefinition>();

        for (Routine routine : info.getRoutines()) {
            String routineName = defaultIfBlank(routine.getSpecificPackage(), routine.getRoutinePackage());

            if (getName().equals(routineName)) {
                result.add(new XMLRoutineDefinition(getSchema(), this, info, routine));
            }
        }

        return result;
    }

    @Override
    protected List<AttributeDefinition> getConstants0() throws SQLException {
        List<AttributeDefinition> result = new ArrayList<AttributeDefinition>();
        return result;
    }
}
