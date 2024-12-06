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

import java.util.ArrayList;
import java.util.List;

import org.jooq.tools.JooqLogger;

/**
 * @author Lukas Eder
 */
public abstract class AbstractContainerElementDefinition<T extends Definition>
extends
    AbstractDefinition
implements
    ContainerElementDefinition<T>
{

    private static final JooqLogger log = JooqLogger.getLogger(AbstractTypedElementDefinition.class);
    final T                         container;

    public AbstractContainerElementDefinition(T container, String name, int position, String comment) {
        this(container, name, position, comment, null);
    }

    public AbstractContainerElementDefinition(T container, String name, int position, String comment, String overload) {
        super(
            container.getDatabase(),
            container.getSchema(),
            protectName(container, name, position),
            comment,
            overload
        );

        this.container = container;
    }

    private static final String protectName(Definition container, String name, int position) {
        if (name == null) {

            // [#6654] Specific error messages per type
            if (container instanceof TableDefinition)
                log.info("Missing name", "Table " + container + " holds a column without a name at position " + position);
            else if (container instanceof UDTDefinition)
                log.info("Missing name", "UDT " + container + " holds an attribute without a name at position " + position);
            else if (container instanceof IndexDefinition)
                log.info("Missing name", "Index " + container + " holds a column without a name at position " + position);
            else if (container instanceof RoutineDefinition)
                log.info("Missing name", "Routine " + container + " holds a parameter without a name at position " + position);
            else if (container instanceof EnumDefinition)
                log.info("Missing name", "Enum " + container + " holds a literal without a name at position " + position);
            else
                log.info("Missing name", "Object " + container + " holds an element without a name at position " + position);

            return "_" + position;
        }

        return name;
    }

    @Override
    public final T getContainer() {
        return container;
    }

    @Override
    public List<Definition> getDefinitionPath() {
        List<Definition> result = new ArrayList<>();

        result.addAll(getContainer().getDefinitionPath());
        result.add(this);

        return result;
    }
}
