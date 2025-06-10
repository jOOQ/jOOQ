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

import static org.jooq.impl.Convert.convert;
import static org.jooq.tools.reflect.Reflect.wrapper;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import org.jooq.EnumType;

/**
 * A base class for enum conversion.
 *
 * @author Lukas Eder
 */
public /* non-final */ class EnumConverter<T, U extends Enum<U>> extends AbstractConverter<T, U> {

    private final Map<T, U>                        lookup;
    private final Function<? super U, ? extends T> to;

    public EnumConverter(Class<T> fromType, Class<U> toType) {
        this(
            fromType,
            toType,

            // [#8045] Also support Kotlin Int type (which translates to int.class)
            Number.class.isAssignableFrom(wrapper(fromType))
                ? u -> convert(u.ordinal(), fromType)
                : EnumType.class.isAssignableFrom(toType)
                ? u -> convert(((EnumType) u).getLiteral(), fromType)
                : u -> convert(u.name(), fromType)
        );
    }

    public EnumConverter(Class<T> fromType, Class<U> toType, Function<? super U, ? extends T> to) {
        super(fromType, toType);

        this.to = to;
        this.lookup = new LinkedHashMap<>();

        for (U u : Internal.enums(toType)) {
            T key = to(u);

            if (key != null)
                this.lookup.put(key, u);
        }
    }

    @Override
    public final U from(T t) {
        if (t == null)
            return null;
        else
            return lookup.get(t);
    }

    /**
     * Subclasses may override this method to provide a custom reverse mapping
     * implementation
     * <p>
     * {@inheritDoc}
     */
    @Override
    public T to(U u) {
        if (u == null)
            return null;
        else
            return to.apply(u);
    }

    @Override
    public String toString() {
        return "EnumConverter [ " + fromType().getName() + " -> " + toType().getName() + " ]";
    }
}
