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
package org.jooq.impl;

import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.FormatterContext;
import org.jooq.Scope;

/**
 * @author Lukas Eder
 */
final class DefaultFormatterContext extends AbstractLazyScope implements FormatterContext {

    final Field<?> original;
    final boolean  multiset;
    Field<?>       formatted;

    DefaultFormatterContext(Scope scope, boolean multiset, Field<?> original) {
        super(scope.configuration(), null, scope.creationTime());

        this.multiset = multiset;
        this.original = original;
        this.formatted = original;
    }

    @Override
    public final DataType<?> type() {
        return original.getDataType();
    }

    @Override
    public final boolean multiset() {
        return multiset;
    }

    @Override
    public final Field<?> field() {
        return original;
    }

    @Override
    public final void field(Field<?> f) {
        this.formatted = f;
    }
}
