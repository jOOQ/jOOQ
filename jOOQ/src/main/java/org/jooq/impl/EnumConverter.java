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

import static org.jooq.tools.Convert.convert;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A base class for enum conversion.
 *
 * @author Lukas Eder
 */
public class EnumConverter<T, U extends Enum<U>> extends AbstractConverter<T, U> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -6094337837408829491L;

    private final Map<T, U>   lookup;
    private final EnumType    enumType;

    public EnumConverter(Class<T> fromType, Class<U> toType) {
        super(fromType, toType);

        this.enumType = Number.class.isAssignableFrom(fromType) ? EnumType.ORDINAL : EnumType.STRING;
        this.lookup = new LinkedHashMap<T, U>();
        for (U u : toType.getEnumConstants()) {
            this.lookup.put(to(u), u);
        }
    }

    @Override
    public final U from(T databaseObject) {
        return lookup.get(databaseObject);
    }

    /**
     * Subclasses may override this method to provide a custom reverse mapping
     * implementation
     * <p>
     * {@inheritDoc}
     */
    @Override
    public T to(U userObject) {
        if (userObject == null) {
            return null;
        }
        else if (enumType == EnumType.ORDINAL) {
            return convert(userObject.ordinal(), fromType());
        }
        else {
            return convert(userObject.name(), fromType());
        }
    }

    /**
     * The type of the converted <code>Enum</code>.
     * <p>
     * This corresponds to JPA's <code>EnumType</code>
     */
    enum EnumType {

        /**
         * Ordinal enum type
         */
        ORDINAL,

        /**
         * String enum type
         */
        STRING
    }

    @Override
    public String toString() {
        return "EnumConverter [ " + fromType().getName() + " -> " + toType().getName() + " ]";
    }
}
