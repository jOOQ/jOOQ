/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
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
