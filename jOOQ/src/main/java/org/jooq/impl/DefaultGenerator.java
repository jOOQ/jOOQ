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
import java.util.concurrent.ConcurrentHashMap;

import org.jooq.Field;
import org.jooq.Generator;
import org.jooq.GeneratorContext;
import org.jooq.GeneratorStatementType;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.Tools.SimpleDataKey;

/**
 * A {@link Generator} proxy implementing "exactly once" computation semantics.
 *
 * @author Lukas Eder
 */
final class DefaultGenerator<R extends Record, X extends Table<R>, T> implements Generator<R, X, T> {

    final Generator<R, X, T> delegate;

    DefaultGenerator(Generator<R, X, T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public final Field<T> apply(GeneratorContext<R, X, T> t) {












        return delegate.apply(t);
    }

    @Override
    public final boolean supports(GeneratorStatementType statementType) {
        return delegate.supports(statementType);
    }
}
