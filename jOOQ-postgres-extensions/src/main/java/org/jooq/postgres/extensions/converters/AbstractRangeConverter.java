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
package org.jooq.postgres.extensions.converters;

import static org.jooq.tools.StringUtils.isBlank;

import org.jooq.impl.AbstractConverter;
import org.jooq.postgres.extensions.types.AbstractRange;

/**
 * A converter for {@link AbstractRange} types.
 *
 * @author Lukas Eder
 */
abstract class AbstractRangeConverter<X, U extends AbstractRange<X>> extends AbstractConverter<Object, U> {

    public AbstractRangeConverter(Class<U> toType) {
        super(Object.class, toType);
    }

    abstract U construct(String lower, boolean lowerIncluding, String upper, boolean upperIncluding);
    abstract U empty();

    @Override
    public U from(Object t) {
        if (t == null)
            return null;

        String s = t.toString();
        if ("empty".equals(s))
            return empty();

        String[] a = s.split(",");
        String s0 = a[0];
        String s1 = a[1];

        String lower = s0.substring(1);
        if (lower.startsWith("\""))
            lower = lower.substring(1, lower.length() - 1);
        boolean lowerIncluding = s0.charAt(0) == '[';
        int l = s1.length() - 1;
        String upper = s1.substring(0, l);
        if (upper.startsWith("\""))
            upper = upper.substring(1, upper.length() - 1);
        boolean upperIncluding = s1.charAt(l) == ']';

        return construct(
            isBlank(lower) ? null : lower,
            lowerIncluding,
            isBlank(upper) ? null : upper,
            upperIncluding
        );
    }

    @Override
    public Object to(U u) {
        if (u == null)
            return null;
        else
            return u.toString();
    }
}
