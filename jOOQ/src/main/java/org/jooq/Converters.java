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
package org.jooq;

import org.jooq.impl.SQLDataType;

/**
 * A chain of converters.
 *
 * @param <T> The database type - i.e. any type available from
 *            {@link SQLDataType}
 * @param <U> The user type
 * @author Lukas Eder
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class Converters<T, U> implements Converter<T, U> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -4307758248063822630L;

    final Converter[] chain;

    /**
     * Create an identity converter.
     */
    public static <T, U> Converter<T, U> of() {
        return new Converters();
    }

    /**
     * Create a single converter.
     */
    public static <T, U> Converter<T, U> of(Converter<T, U> converter) {
        return new Converters(converter);
    }

    /**
     * Chain two converters.
     */
    public static <T, X1, U> Converter<T, U> of(Converter<T, X1> c1, Converter<X1, U> c2) {
        return new Converters(c1, c2);
    }

    /**
     * Chain three converters.
     */
    public static <T, X1, X2, U> Converter<T, U> of(Converter<T, X1> c1, Converter<X1, X2> c2, Converter<X2, U> c3) {
        return new Converters(c1, c2, c3);
    }

    /**
     * Chain four converters.
     */
    public static <T, X1, X2, X3, U> Converter<T, U> of(Converter<T, X1> c1, Converter<X1, X2> c2, Converter<X2, X3> c3, Converter<X3, U> c4) {
        return new Converters(c1, c2, c3, c4);
    }

    Converters(Converter... chain) {
        this.chain = chain == null ? new Converter[0] : chain;
    }

    @Override
    public final U from(T t) {
        Object result = t;

        for (int i = 0; i < chain.length; i++)
            result = chain[i].from(result);

        return (U) result;
    }

    @Override
    public final T to(U u) {
        Object result = u;

        for (int i = chain.length - 1; i >= 0; i--)
            result = chain[i].to(result);

        return (T) result;
    }

    @Override
    public final Class<T> fromType() {
        return chain[0].fromType();
    }

    @Override
    public final Class<U> toType() {
        return chain[chain.length - 1].toType();
    }
}
