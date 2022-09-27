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
package org.jooq.impl;

import static org.jooq.ContextConverter.scoped;

import org.jooq.Configuration;
import org.jooq.ConverterContext;

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
        return scoped(ctx.configuration().converterProvider().provide(fromType(), toType())).from(t, ctx);
    }

    @Override
    public T to(U u, ConverterContext ctx) {
        return scoped(ctx.configuration().converterProvider().provide(fromType(), toType())).to(u, ctx);
    }

    @Override
    public String toString() {
        return "AutoConverter [ " + fromType().getName() + " -> " + toType().getName() + " ]";
    }
}
