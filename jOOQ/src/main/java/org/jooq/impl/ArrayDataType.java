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

import org.jooq.CharacterSet;
import org.jooq.Collation;
import org.jooq.Configuration;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Nullability;

/**
 * A wrapper for anonymous array data types
 *
 * @author Lukas Eder
 */
final class ArrayDataType<T> extends DefaultDataType<T[]> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 7883229760246533448L;

    final DataType<T>         elementType;

    public ArrayDataType(DataType<T> elementType) {
        super(null, elementType.getArrayType(), elementType.getTypeName(), elementType.getCastTypeName());

        this.elementType = elementType;
    }

    /**
     * [#3225] Performant constructor for creating derived types.
     */
    ArrayDataType(
        DefaultDataType<T[]> t,
        DataType<T> elementType,
        int precision,
        int scale,
        int length,
        Nullability nullability,
        Collation collation,
        CharacterSet characterSet,
        boolean identity,
        Field<T[]> defaultValue
    ) {
        super(t, precision, scale, length, nullability, collation, characterSet, identity, defaultValue);

        this.elementType= elementType;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    DefaultDataType<T[]> construct(
        int newPrecision,
        int newScale,
        int newLength,
        Nullability
        newNullability,
        Collation newCollation,
        CharacterSet newCharacterSet,
        boolean newIdentity,
        Field<T[]> newDefaultValue
    ) {
        return new ArrayDataType<>(
            this,
            (DefaultDataType<T>) elementType,
            newPrecision,
            newScale,
            newLength,
            newNullability,
            newCollation,
            newCharacterSet,
            newIdentity,
            (Field) newDefaultValue
        );
    }

    @Override
    public final String getTypeName(Configuration configuration) {
        String typeName = elementType.getTypeName(configuration);
        return getArrayType(configuration, typeName);
    }

    @Override
    public final String getCastTypeName(Configuration configuration) {
        String castTypeName = elementType.getCastTypeName(configuration);
        return getArrayType(configuration, castTypeName);
    }

    private static String getArrayType(Configuration configuration, String dataType) {
        switch (configuration.family()) {
            case HSQLDB:
                return dataType + " array";





            case POSTGRES:
                return dataType + "[]";
            case H2:
                return "array";

            // Default implementation is needed for hash-codes and toString()
            default:
                return dataType + "[]";
        }
    }
}
