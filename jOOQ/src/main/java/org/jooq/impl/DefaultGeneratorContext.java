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
 *
 *
 *
 */
package org.jooq.impl;

import java.util.Map;

import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.GeneratorContext;
import org.jooq.GeneratorStatementType;
import org.jooq.Record;
import org.jooq.Table;

/**
 * @author Lukas Eder
 */
final class DefaultGeneratorContext<R extends Record, X extends Table<R>, T> extends AbstractScope implements GeneratorContext<R, X, T> {

    final X                      table;
    final Field<T>               field;
    final GeneratorStatementType statementType;

    DefaultGeneratorContext(
        Configuration configuration,
        Map<Object, Object> data,
        X table,
        Field<T> field,
        GeneratorStatementType statementType
    ) {
        super(configuration, data);

        this.table = table;
        this.field = field;
        this.statementType = statementType;
    }

    @Override
    public final X table() {
        return table;
    }

    @Override
    public final Field<T> field() {
        return field;
    }

    @Override
    public final GeneratorStatementType statementType() {
        return statementType;
    }
}
