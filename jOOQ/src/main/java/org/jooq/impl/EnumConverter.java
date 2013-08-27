/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under LGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 * 
 * LGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
 */
package org.jooq.impl;

import static org.jooq.tools.Convert.convert;

import java.util.LinkedHashMap;
import java.util.Map;

import org.jooq.Converter;

/**
 * A base class for enum conversion.
 *
 * @author Lukas Eder
 */
public class EnumConverter<T, U extends Enum<U>> implements Converter<T, U> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -6094337837408829491L;

    private final Class<T>    fromType;
    private final Class<U>    toType;
    private final Map<T, U>   lookup;
    private final EnumType    enumType;

    public EnumConverter(Class<T> fromType, Class<U> toType) {
        this.fromType = fromType;
        this.toType = toType;
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
            return convert(userObject.ordinal(), fromType);
        }
        else {
            return convert(userObject.name(), fromType);
        }
    }

    @Override
    public final Class<T> fromType() {
        return fromType;
    }

    @Override
    public final Class<U> toType() {
        return toType;
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
}
