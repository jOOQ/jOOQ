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
package org.jooq.meta;

import static org.jooq.meta.AbstractDatabase.fetchedSize;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.tools.JooqLogger;

/**
 * @author Lukas Eder
 */
public abstract class AbstractPackageDefinition extends AbstractDefinition implements PackageDefinition {

    private static final JooqLogger   log = JooqLogger.getLogger(AbstractPackageDefinition.class);

    private List<RoutineDefinition>   routines;
    private List<AttributeDefinition> constants;

    public AbstractPackageDefinition(SchemaDefinition schema, String name, String comment) {
        super(schema.getDatabase(), schema, name, comment);
    }

    @Override
    public List<Definition> getDefinitionPath() {
        List<Definition> result = new ArrayList<>();

        result.addAll(getSchema().getDefinitionPath());
        result.add(this);

        return result;
    }

    @Override
    public final List<RoutineDefinition> getRoutines() {
        if (routines == null) {
            routines = new ArrayList<>();

            if (getDatabase().getIncludePackageRoutines()) {
                try {
                    List<RoutineDefinition> r = getRoutines0();

                    // [#12093] Filter exclude / include also for package routines
                    if (getDatabase().getIncludeExcludePackageRoutines()) {
                        routines = getDatabase().filterExcludeInclude(r);
                        log.info("Columns fetched", fetchedSize(r, routines));
                    }
                    else
                        routines = r;
                }
                catch (Exception e) {
                    log.error("Error while initialising package", e);
                }
            }
        }

        return routines;
    }

    @Override
    public final List<UDTDefinition> getUDTs() {
        return getDatabase().getUDTs(this);
    }

    protected abstract List<RoutineDefinition> getRoutines0() throws SQLException;

    @Override
    public final List<AttributeDefinition> getConstants() {
        if (constants == null) {
            constants = new ArrayList<>();

            if (getDatabase().getIncludePackageConstants()) {
                try {
                    constants = getConstants0();
                }
                catch (Exception e) {
                    log.error("Error while initialising package", e);
                }
            }
        }

        return constants;
    }

    protected abstract List<AttributeDefinition> getConstants0() throws SQLException;
}
