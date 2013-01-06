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
