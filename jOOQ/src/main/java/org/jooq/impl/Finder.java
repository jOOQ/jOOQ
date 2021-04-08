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

import java.sql.SQLException;
import java.util.function.Predicate;

import org.jooq.BindContext;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.Param;
import org.jooq.QueryPart;
import org.jooq.QueryPartInternal;

/**
 * A stub {@link BindContext} that acts as a collector of {@link Param}
 * {@link QueryPart}'s
 *
 * @author Lukas Eder
 */
final class Finder extends AbstractBindContext {

    private final Predicate<? super QueryPart> find;
    private final Predicate<? super QueryPart> enter;
    private boolean                            found;

    Finder(
        Configuration configuration,
        Predicate<? super QueryPart> find,
        Predicate<? super QueryPart> enter
    ) {
        super(configuration, null);

        this.find = find;
        this.enter = enter;
    }

    @Override
    protected final void bindInternal(QueryPartInternal internal) {
        if (found |= find.test(internal))
            return;

        if (enter.test(internal))
            super.bindInternal(internal);
    }

    final boolean found() {
        return found;
    }

    @Override
    protected final BindContext bindValue0(Object value, Field<?> field) throws SQLException {
        throw new UnsupportedOperationException();
    }
}
