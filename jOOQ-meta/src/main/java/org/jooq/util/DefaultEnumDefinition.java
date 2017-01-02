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
public class DefaultEnumDefinition extends AbstractDefinition implements EnumDefinition {

    private final List<String> literals;
    private final boolean isSynthetic;

    public DefaultEnumDefinition(SchemaDefinition schema, String name, String comment) {
        this(schema, name, comment, false);
    }

    public DefaultEnumDefinition(SchemaDefinition schema, String name, String comment, boolean isSynthetic) {
        super(schema.getDatabase(), schema, name, comment);

        this.literals = new ArrayList<String>();
        this.isSynthetic = isSynthetic;
    }

    @Override
    public List<Definition> getDefinitionPath() {
        List<Definition> result = new ArrayList<Definition>();

        result.addAll(getSchema().getDefinitionPath());
        result.add(this);

        return result;
    }

    public void addLiteral(String literal) {
        literals.add(literal);
    }

    public void addLiterals(String... literal) {
        literals.addAll(Arrays.asList(literal));
    }

    @Override
    public List<String> getLiterals() {
        return literals;
    }

    @Override
    public boolean isSynthetic() {
        return isSynthetic;
    }
}
