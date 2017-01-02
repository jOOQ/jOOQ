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
package org.jooq.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Lukas Eder
 */
public class DefaultDomainDefinition extends AbstractDefinition implements DomainDefinition {

    private final List<String>       checkClauses;
    private final DataTypeDefinition baseType;

    public DefaultDomainDefinition(SchemaDefinition schema, String name, DataTypeDefinition baseType) {
        super(schema.getDatabase(), schema, name, "");
        this.baseType = baseType;

        this.checkClauses = new ArrayList<String>();
    }

    @Override
    public List<Definition> getDefinitionPath() {
        List<Definition> result = new ArrayList<Definition>();

        result.addAll(getSchema().getDefinitionPath());
        result.add(this);

        return result;
    }

    public void addCheckClause(String checkClause) {
        checkClauses.add(checkClause);
    }

    public void addCheckClause(String... checkClause) {
        checkClauses.addAll(Arrays.asList(checkClause));
    }

    @Override
    public List<String> getCheckClauses() {
        return checkClauses;
    }

    @Override
    public DataTypeDefinition getBaseType() {
        return baseType;
    }
}
