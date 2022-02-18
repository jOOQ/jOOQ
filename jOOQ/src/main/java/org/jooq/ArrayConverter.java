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

import static org.jooq.impl.Internal.arrayType;

import org.jooq.impl.AbstractConverter;
import org.jooq.tools.Convert;

/**
 * A {@link Converter} that can convert arrays based on a delegate converter
 * that can convert the array base types.
 *
 * @author Lukas Eder
 */
final class ArrayConverter<T, U> extends AbstractConverter<T[], U[]> {

    final Converter<T, U> converter;
    final Converter<U, T> inverse;

    public ArrayConverter(Converter<T, U> converter) {
        super(arrayType(converter.fromType()), arrayType(converter.toType()));

        this.converter = converter;
        this.inverse = Converters.inverse(converter);
    }

    @Override
    public final U[] from(T[] t) {
        return Convert.convertArray(t, converter);
    }

    @Override
    public final T[] to(U[] t) {
        return Convert.convertArray(t, inverse);
    }
}
