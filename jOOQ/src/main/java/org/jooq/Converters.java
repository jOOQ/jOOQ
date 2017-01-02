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
    public static <T> Converter<T, T> identity(final Class<T> type) {
        return new Converter<T, T>() {

            /**
             * Generated UID
             */
            private static final long serialVersionUID = -8331976721627671263L;

            @Override
            public final T from(T t) {
                return t;
            }

            @Override
            public final T to(T t) {
                return t;
            }

            @Override
            public final Class<T> fromType() {
                return type;
            }

            @Override
            public final Class<T> toType() {
                return type;
            }
        };
    }

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
    public static <T, X1, U> Converter<T, U> of(Converter<T, ? extends X1> c1, Converter<? super X1, U> c2) {
        return new Converters(c1, c2);
    }

    /**
     * Chain three converters.
     */
    public static <T, X1, X2, U> Converter<T, U> of(Converter<T, ? extends X1> c1, Converter<? super X1, ? extends X2> c2, Converter<? super X2, U> c3) {
        return new Converters(c1, c2, c3);
    }

    /**
     * Chain four converters.
     */
    public static <T, X1, X2, X3, U> Converter<T, U> of(Converter<T, ? extends X1> c1, Converter<? super X1, ? extends X2> c2, Converter<? super X2, ? extends X3> c3, Converter<? super X3, U> c4) {
        return new Converters(c1, c2, c3, c4);
    }

    /**
     * Inverse a converter.
     */
    public static <T, U> Converter<U, T> inverse(final Converter<T, U> converter) {
        return new Converter<U, T>() {

            /**
             * Generated UID
             */
            private static final long serialVersionUID = -4307758248063822630L;

            @Override
            public T from(U u) {
                return converter.to(u);
            }

            @Override
            public U to(T t) {
                return converter.from(t);
            }

            @Override
            public Class<U> fromType() {
                return converter.toType();
            }

            @Override
            public Class<T> toType() {
                return converter.fromType();
            }

            @Override
            public String toString() {
                return "InverseConverter [ " + fromType().getName() + " -> " + toType().getName() + " ]";
            }
        };
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String separator = " -> ";

        sb.append("Converters [ ");
        sb.append(chain[0].fromType().getName());

        for (Converter<?, ?> converter : chain) {
            sb.append(separator);
            sb.append(converter.toType().getName());
        }

        sb.append(" ]");
        return sb.toString();
    }
}
