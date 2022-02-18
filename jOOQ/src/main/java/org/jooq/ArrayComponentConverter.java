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
package org.jooq;

import java.lang.reflect.Array;

import org.jooq.impl.AbstractConverter;

/**
 * @author Lukas Eder
 */
@SuppressWarnings("unchecked")
final class ArrayComponentConverter<T, U> extends AbstractConverter<T, U> {

    final Converter<T[], U[]> converter;

    public ArrayComponentConverter(Converter<T[], U[]> converter) {
        super((Class<T>) converter.fromType().getComponentType(), (Class<U>) converter.toType().getComponentType());

        this.converter = converter;
    }

    @Override
    public final U from(T t) {
        if (t == null)
            return null;

        T[] a = (T[]) Array.newInstance(fromType(), 1);
        a[0] = t;
        return converter.from(a)[0];
    }

    @Override
    public final T to(U u) {
        if (u == null)
            return null;

        U[] a = (U[]) Array.newInstance(fromType(), 1);
        a[0] = u;
        return converter.to(a)[0];
    }
}
