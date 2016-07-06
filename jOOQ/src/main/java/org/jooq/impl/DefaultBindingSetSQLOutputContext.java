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

import java.sql.SQLOutput;
import java.util.Map;

import org.jooq.BindingSetSQLOutputContext;
import org.jooq.Configuration;
import org.jooq.Converter;

/**
 * @author Lukas Eder
 */
class DefaultBindingSetSQLOutputContext<U> extends AbstractScope implements BindingSetSQLOutputContext<U> {

    private final SQLOutput output;
    private final U         value;

    DefaultBindingSetSQLOutputContext(Configuration configuration, Map<Object, Object> data, SQLOutput output, U value) {
        super(configuration, data);

        this.output = output;
        this.value = value;
    }

    @Override
    public final SQLOutput output() {
        return output;
    }

    @Override
    public final U value() {
        return value;
    }

    @Override
    public final <T> BindingSetSQLOutputContext<T> convert(Converter<? extends T, ? super U> converter) {
        return new DefaultBindingSetSQLOutputContext<T>(configuration, data, output, converter.to(value));
    }

    @Override
    public String toString() {
        return "DefaultBindingSetSQLOutputContext [value=" + value + "]";
    }
}
