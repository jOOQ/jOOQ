/**
 * Copyright (c) 2009-2016, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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

import java.sql.ResultSet;
import java.util.Map;

import org.jooq.BindingGetResultSetContext;
import org.jooq.Configuration;
import org.jooq.Converter;

/**
 * @author Lukas Eder
 */
class DefaultBindingGetResultSetContext<U> extends AbstractScope implements BindingGetResultSetContext<U> {

    private final ResultSet resultSet;
    private int             index;
    private U               value;

    DefaultBindingGetResultSetContext(Configuration configuration, Map<Object, Object> data, ResultSet resultSet, int index) {
        super(configuration, data);

        this.resultSet = resultSet;
        this.index = index;
    }

    @Override
    public final ResultSet resultSet() {
        return resultSet;
    }

    @Override
    public final int index() {
        return index;
    }

    final void index(int i) {
        this.index = i;
    }

    @Override
    public void value(U v) {
        this.value = v;
    }

    final U value() {
        return value;
    }

    @Override
    public final <T> BindingGetResultSetContext<T> convert(final Converter<? super T, ? extends U> converter) {
        return new DefaultBindingGetResultSetContext<T>(configuration, data, resultSet, index) {
            @Override
            public void value(T v) {
                value = converter.from(v);
            }
        };
    }

    @Override
    public String toString() {
        return "DefaultBindingGetResultSetContext [index=" + index + ", value=" + value + "]";
    }
}
