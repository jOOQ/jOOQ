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
package org.jooq.impl;

import java.sql.PreparedStatement;
import java.util.Map;

import org.jooq.BindingSetStatementContext;
import org.jooq.Configuration;
import org.jooq.Converter;

/**
 * @author Lukas Eder
 */
class DefaultBindingSetStatementContext<U> extends AbstractScope implements BindingSetStatementContext<U> {

    private final PreparedStatement statement;
    private final int               index;
    private final U                 value;

    DefaultBindingSetStatementContext(Configuration configuration, Map<Object, Object> data, PreparedStatement statement, int index, U value) {
        super(configuration, data);

        this.statement = statement;
        this.index = index;
        this.value = value;
    }

    @Override
    public final PreparedStatement statement() {
        return statement;
    }

    @Override
    public final int index() {
        return index;
    }

    @Override
    public final U value() {
        return value;
    }

    @Override
    public final <T> BindingSetStatementContext<T> convert(Converter<? extends T, ? super U> converter) {
        return new DefaultBindingSetStatementContext<T>(configuration, data, statement, index, converter.to(value));
    }

    @Override
    public String toString() {
        return "DefaultBindingSetStatementContext [index=" + index + ", value=" + value + "]";
    }
}
