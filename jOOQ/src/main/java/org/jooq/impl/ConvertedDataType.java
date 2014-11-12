/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq.impl;

import org.jooq.Binding;
import org.jooq.Configuration;
import org.jooq.Converter;
import org.jooq.DataType;

/**
 * A <code>DataType</code> used for converted types using {@link Converter}
 *
 * @author Lukas Eder
 */
class ConvertedDataType<T, U> extends DefaultDataType<U> {

    /**
     * Generated UID
     */
    private static final long           serialVersionUID = -2321926692580974126L;

    private final DataType<T>           delegate;
    private final Binding<? super T, U> binding;

    ConvertedDataType(DataType<T> delegate, Binding<? super T, U> binding) {
        super(
            null,
            binding.converter().toType(),
            delegate.getTypeName(),
            delegate.getCastTypeName(),
            delegate.precision(),
            delegate.scale(),
            delegate.length(),
            delegate.nullable(),
            delegate.defaulted()
        );

        this.delegate = delegate;
        this.binding = binding;
    }

    @Override
    public int getSQLType() {
        return delegate.getSQLType();
    }

    @Override
    public String getTypeName(Configuration configuration) {
        return delegate.getTypeName(configuration);
    }

    @Override
    public String getCastTypeName(Configuration configuration) {
        return delegate.getCastTypeName(configuration);
    }

    @SuppressWarnings("unchecked")
    @Override
    public U convert(Object object) {
        if (binding.converter().toType().isInstance(object)) {
            return (U) object;
        }

        // [#3200] Try to convert arbitrary objects to T
        else {
            return binding.converter().from(delegate.convert(object));
        }
    }

    Binding<? super T, U> binding() {
        return binding;
    }

    Converter<? super T, U> converter() {
        return binding.converter();
    }
}
