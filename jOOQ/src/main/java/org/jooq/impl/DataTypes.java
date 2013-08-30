/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is triple-licensed under ASL 2.0, AGPL 3.0, and jOOQ EULA
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   ASL 2.0 or jOOQ EULA.
 * - If you're using this work with at least one commercial database, you may
 *   choose AGPL 3.0 or jOOQ EULA.
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * AGPL 3.0
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 *
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details: http://www.jooq.org/eula
 */
package org.jooq.impl;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jooq.Converter;
import org.jooq.DataType;
import org.jooq.exception.DataTypeException;

/**
 * A central {@link DataType} registry
 *
 * @author Lukas Eder
 */
final class DataTypes {

    private static final Map<Class<?>, Converter<?, ?>> CONVERTERS  = new HashMap<Class<?>, Converter<?, ?>>();
    private static final Map<String, Class<?>>          UDT_RECORDS = new HashMap<String, Class<?>>();

    // ------------------------------------------------------------------------
    // XXX: Public API used for initialisation from generated artefacts
    // (this may be rendered public in the future)
    // ------------------------------------------------------------------------

    /**
     * Register a <code>Converter</code> for a custom type
     * <p>
     * This registers a {@link Converter} for a custom type. This converter will
     * be used by jOOQ to recognise custom types and to transform them back to
     * well-known database types (as defined in {@link Converter#fromType()}) in
     * rendering and binding steps
     * <p>
     * A custom type can be registered only once. Duplicate registrations will
     * be ignored
     * <p>
     * The converter class must provide a default constructor.
     *
     * @see #registerConverter(Class, Converter)
     */
    static final synchronized <U> void registerConverter(Class<U> customType,
        Class<? extends Converter<?, U>> converter) {

        try {
            converter.getConstructor().setAccessible(true);
            registerConverter(customType, converter.newInstance());
        }
        catch (Exception e) {
            throw new DataTypeException("Cannot register converter", e);
        }
    }

    /**
     * Register a <code>Converter</code> for a custom type
     * <p>
     * This registers a {@link Converter} for a custom type. This converter will
     * be used by jOOQ to recognise custom types and to transform them back to
     * well-known database types (as defined in {@link Converter#fromType()}) in
     * rendering and binding steps
     * <p>
     * A custom type can be registered only once. Duplicate registrations will
     * be ignored
     */
    static final synchronized <U> void registerConverter(Class<U> customType, Converter<?, U> converter) {

        // A converter can be registered only once
        if (!CONVERTERS.containsKey(customType)) {
            CONVERTERS.put(customType, converter);
        }
    }

    /**
     * Register a type mapping for a UDT
     * <p>
     * This registers a Java type for a given UDT as expected in various JDBC
     * methods, such as {@link Connection#setTypeMap(Map)},
     * {@link ResultSet#getObject(int, Map)}, {@link Array#getArray(Map)},
     * {@link CallableStatement#getObject(int, Map)}, etc.
     */
    static final synchronized void registerUDTRecord(String name, Class<?> type) {

        // A mapping can be registered only once
        if (!UDT_RECORDS.containsKey(name)) {
            UDT_RECORDS.put(name, type);
        }
    }


    // ------------------------------------------------------------------------
    // XXX: Internal API
    // ------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    static final <U> Converter<?, U> converter(Class<U> customType) {

        // TODO: Is synchronisation needed? How to implement it most efficiently?
        return (Converter<?, U>) CONVERTERS.get(customType);
    }

    static final Map<String, Class<?>> udtRecords() {
        return Collections.unmodifiableMap(UDT_RECORDS);
    }

    /**
     * No instances
     */
    private DataTypes() {}
}
