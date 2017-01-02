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

import java.sql.SQLInput;
import java.util.Map;

import org.jooq.BindingGetSQLInputContext;
import org.jooq.Configuration;
import org.jooq.Converter;

/**
 * @author Lukas Eder
 */
class DefaultBindingGetSQLInputContext<U> extends AbstractScope implements BindingGetSQLInputContext<U> {

    private final SQLInput input;
    private U              value;

    DefaultBindingGetSQLInputContext(Configuration configuration, Map<Object, Object> data, SQLInput input) {
        super(configuration, data);

        this.input = input;
    }

    @Override
    public final SQLInput input() {
        return input;
    }

    @Override
    public void value(U v) {
        this.value = v;
    }

    final U value() {
        return value;
    }

    @Override
    public final <T> BindingGetSQLInputContext<T> convert(final Converter<? super T, ? extends U> converter) {
        return new DefaultBindingGetSQLInputContext<T>(configuration, data, input) {
            @Override
            public void value(T v) {
                value = converter.from(v);
            }
        };
    }

    @Override
    public String toString() {
        return "DefaultBindingGetSQLInputContext [value=" + value + "]";
    }
}
