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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
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
package org.jooq.impl;

import static org.jooq.ContextConverter.scoped;
import static org.jooq.impl.Tools.CONFIG;

import org.jooq.Configuration;
import org.jooq.ContextConverter;
import org.jooq.Converter;
import org.jooq.ConverterContext;
import org.jooq.exception.DataTypeException;

import org.jetbrains.annotations.NotNull;

/**
 * A base class for automatic conversion using
 * {@link Configuration#converterProvider()}.
 *
 * @author Lukas Eder
 */
public /* non-final */ class AutoConverter<T, U> extends AbstractContextConverter<T, U> {

    public AutoConverter(Class<T> fromType, Class<U> toType) {
        super(fromType, toType);
    }

    @Override
    public U from(T t, ConverterContext ctx) {
        return delegate(ctx).from(t, ctx);
    }

    @Override
    public T to(U u, ConverterContext ctx) {
        return delegate(ctx).to(u, ctx);
    }

    private final ContextConverter<T, U> delegate(ConverterContext ctx) {
        Converter<T, U> c = ctx.configuration().converterProvider().provide(fromType(), toType());

        if (c == null)
            c = CONFIG.get().converterProvider().provide(fromType(), toType());

        if (c == null)
            throw new DataTypeException("Cannot auto convert from " + fromType() + " to " + toType());
        else
            return scoped(c);
    }

    @Override
    public String toString() {
        return "AutoConverter [ " + fromType().getName() + " -> " + toType().getName() + " ]";
    }
}
