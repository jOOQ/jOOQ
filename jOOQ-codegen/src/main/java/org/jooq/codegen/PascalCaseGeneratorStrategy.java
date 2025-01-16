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
package org.jooq.codegen;

import static java.util.Arrays.asList;
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.POSTGRES;
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
import static org.jooq.SQLDialect.YUGABYTEDB;

import org.jooq.meta.Definition;
import org.jooq.meta.EmbeddableDefinition;
import org.jooq.meta.ForeignKeyDefinition;
import org.jooq.meta.IndexDefinition;
import org.jooq.meta.InverseForeignKeyDefinition;
import org.jooq.meta.UniqueKeyDefinition;
import org.jooq.tools.StringUtils;

/**
 * An adaptation of the {@link DefaultGeneratorStrategy}, which works best with
 * schemas whose identifiers are declared in <code>PascalCase</code>.
 */
public class PascalCaseGeneratorStrategy extends DefaultGeneratorStrategy {

    @Override
    public String getJavaIdentifier(Definition definition) {
        return toPascalCase(getJavaIdentifier0(definition));
    }

    private String getJavaIdentifier0(Definition definition) {
        String identifier = getFixedJavaIdentifier(definition);

        if (identifier != null)
            return identifier;

        // [#6307] Some databases work with per-table namespacing for indexes, not per-schema namespacing.
        //         In order to have non-ambiguous identifiers, we need to include the table name.
        else if (definition instanceof IndexDefinition && asList(MARIADB, MYSQL).contains(definition.getDatabase().getDialect().family()))
            return ((IndexDefinition) definition).getTable().getOutputName() + definition.getOutputName();

        // [#5538] [#11286] [#12118] The same is true for unique keys, which are really indexes
        else if (definition instanceof UniqueKeyDefinition && asList(SQLITE).contains(definition.getDatabase().getDialect().family()))
            return ((UniqueKeyDefinition) definition).getTable().getOutputName() + definition.getOutputName();

        // [#9758] And then also for foreign keys
        else if (definition instanceof ForeignKeyDefinition && asList(POSTGRES, SQLITE, YUGABYTEDB).contains(definition.getDatabase().getDialect().family()))
            return ((ForeignKeyDefinition) definition).getTable().getOutputName() + definition.getOutputName();

        else if (definition instanceof InverseForeignKeyDefinition && asList(POSTGRES, SQLITE, YUGABYTEDB).contains(definition.getDatabase().getDialect().family()))
            return ((InverseForeignKeyDefinition) definition).getReferencingTable().getOutputName() + definition.getOutputName();

        // [#10481] Embeddables have a defining name (class name) and a referencing name (identifier name, member name).
        else if (definition instanceof EmbeddableDefinition)
            return ((EmbeddableDefinition) definition).getReferencingOutputName();






        else
            return definition.getOutputName();
    }

    @Override
    public String getJavaMemberName(Definition definition, Mode mode) {
        return StringUtils.toUC(super.getJavaMemberName(definition, mode), targetLocale);
    }

    @Override
    public String getJavaMethodName(Definition definition, Mode mode) {
        return StringUtils.toUC(super.getJavaMethodName(definition, mode), targetLocale);
    }

    @Override
    String toPascalCase(String name) {
        switch (getCase(name)) {
            case MIXED:
                return StringUtils.toUC(name, targetLocale);
            case LOWER:
                return super.toPascalCase(name);
            case UPPER:
            default:
                return super.toPascalCase(name);
        }
    }
}
