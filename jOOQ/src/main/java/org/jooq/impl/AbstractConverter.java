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
package org.jooq.impl;

import org.jooq.Converter;

/**
 * @author Lukas Eder
 */
public abstract class AbstractConverter<T, U> implements Converter<T, U> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 3739249904977790727L;

    private final Class<T>    fromType;
    private final Class<U>    toType;

    public AbstractConverter(Class<T> fromType, Class<U> toType) {
        this.fromType = fromType;
        this.toType = toType;
    }

    @Override
    public final Class<T> fromType() {
        return fromType;
    }

    @Override
    public final Class<U> toType() {
        return toType;
    }

    @Override
    public String toString() {
        return "Converter [ " + fromType().getName() + " -> " + toType().getName() + " ]";
    }
}
