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
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
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
package org.jooq;

import java.lang.reflect.Array;

import org.jooq.impl.AbstractContextConverter;

/**
 * @author Lukas Eder
 */
@SuppressWarnings("unchecked")
final class ArrayComponentConverter<T, U> extends AbstractContextConverter<T, U> {

    final ContextConverter<T[], U[]> converter;

    public ArrayComponentConverter(ContextConverter<T[], U[]> converter) {
        super((Class<T>) converter.fromType().getComponentType(), (Class<U>) converter.toType().getComponentType());

        this.converter = converter;
    }

    @Override
    public final U from(T t, ConverterContext scope) {
        if (t == null)
            return null;

        T[] a = (T[]) Array.newInstance(fromType(), 1);
        a[0] = t;
        return converter.from(a, scope)[0];
    }

    @Override
    public final T to(U u, ConverterContext scope) {
        if (u == null)
            return null;

        U[] a = (U[]) Array.newInstance(fromType(), 1);
        a[0] = u;
        return converter.to(a, scope)[0];
    }
}
