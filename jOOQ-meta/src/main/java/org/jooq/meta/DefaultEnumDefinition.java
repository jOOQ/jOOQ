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

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lukas Eder
 */
public class DefaultEnumDefinition extends AbstractDefinition implements EnumDefinition {

    private final List<EnumLiteralDefinition> literals;
    private final boolean                     isSynthetic;

    public DefaultEnumDefinition(SchemaDefinition schema, String name, String comment) {
        this(schema, name, comment, false);
    }

    public DefaultEnumDefinition(SchemaDefinition schema, String name, String comment, boolean isSynthetic) {
        super(schema.getDatabase(), schema, name, comment);

        this.literals = new ArrayList<>();
        this.isSynthetic = isSynthetic;
    }

    @Override
    public List<Definition> getDefinitionPath() {
        List<Definition> result = new ArrayList<>();

        result.addAll(getSchema().getDefinitionPath());
        result.add(this);

        return result;
    }

    public void addLiteral(String literal) {
        literals.add(new DefaultEnumLiteralDefinition(getDatabase(), this, literal, literal, literals.size() + 1));
    }

    public void addLiterals(String... l) {
        for (String literal : l)
            addLiteral(literal);
    }

    @Override
    public List<String> getLiterals() {
        return literals.stream().map(l -> l.getLiteral()).collect(toList());
    }

    @Override
    public List<EnumLiteralDefinition> getLiteralDefinitions() {
        return new ArrayList<>(literals);
    }

    @Override
    public boolean isSynthetic() {
        return isSynthetic;
    }
}
