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

import static org.jooq.impl.Tools.CONFIG;

import java.lang.reflect.Array;
import java.util.Collection;

import org.jooq.CharacterSet;
import org.jooq.Collation;
import org.jooq.Configuration;
import org.jooq.ConverterContext;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Generator;
import org.jooq.Nullability;
import org.jooq.Record;
import org.jooq.Row;
import org.jooq.SQLDialect;
import org.jooq.impl.QOM.GenerationLocation;
import org.jooq.impl.QOM.GenerationMode;
import org.jooq.impl.QOM.GenerationOption;

/**
 * A wrapper for anonymous array data types
 *
 * @author Lukas Eder
 */
final class ArrayDataType<T> extends DefaultDataType<T[]> {

    final DataType<T> elementType;

    public ArrayDataType(DataType<T> elementType) {
        super(
            null,
            elementType.getArrayType(),
            elementType.getBinding().arrayBinding(),
            elementType.getTypeName() + " array",
            elementType.getCastTypeName() + " array",
            elementType.getDDLTypeName() + " array"
        );

        this.elementType = elementType;
    }

    /**
     * [#3225] Performant constructor for creating derived types.
     */
    ArrayDataType(
        AbstractDataType<T[]> t,
        DataType<T> elementType,
        Integer precision,
        Integer scale,
        Integer length,
        Nullability nullability,
        boolean hidden,
        boolean redacted,
        boolean readonly,
        Generator<?, ?, T[]> generatedAlwaysAs,
        GenerationOption generationOption,
        GenerationLocation generationLocation,
        Collation collation,
        CharacterSet characterSet,
        GenerationMode identity,
        Field<T[]> defaultValue
    ) {
        super(t, precision, scale, length, nullability, hidden, redacted, readonly, generatedAlwaysAs, generationOption, generationLocation, collation, characterSet, identity, defaultValue);

        this.elementType = elementType;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    DefaultDataType<T[]> construct(
        Integer newPrecision,
        Integer newScale,
        Integer newLength,
        Nullability newNullability,
        boolean newHidden,
        boolean newRedacted,
        boolean newReadonly,
        Generator<?, ?, T[]> newGeneratedAlwaysAs,
        GenerationOption newGenerationOption,
        GenerationLocation newGenerationLocation,
        Collation newCollation,
        CharacterSet newCharacterSet,
        GenerationMode newIdentity,
        Field<T[]> newDefaultValue
    ) {
        return new ArrayDataType<>(
            this,
            (AbstractDataType<T>) elementType,
            newPrecision,
            newScale,
            newLength,
            newNullability,
            newHidden,
            newRedacted,
            newReadonly,
            newGeneratedAlwaysAs,
            newGenerationOption,
            newGenerationLocation,
            newCollation,
            newCharacterSet,
            newIdentity,
            (Field) newDefaultValue
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    final T[] convert(Object object, ConverterContext cc) {

        // [#1441] Avoid unneeded type conversions to improve performance
        if (object == null)
            return null;
        else if (object.getClass() == getType())
            return (T[]) object;

        Object[] array =
            object instanceof Object[] ? (Object[]) object
          : object instanceof Collection ? ((Collection<?>) object).toArray()
          : null;

        if (array != null && elementType.getType() != Object.class) {
            T[] result = (T[]) Array.newInstance(elementType.getType(), array.length);

            for (int i = 0; i < array.length; i++)
                result[i] = convert0(elementType, array[i], cc);

            return result;
        }
        else
            return super.convert(object, cc);
    }

    @Override
    public final String getTypeName() {
        return getTypeName(CONFIG.get());
    }

    @Override
    public final String getTypeName(Configuration configuration) {
        return getArrayType(configuration, elementType.getTypeName(configuration));
    }

    @Override
    public final String getCastTypeName() {
        return getCastTypeName(CONFIG.get());
    }

    @Override
    public final String getCastTypeName(Configuration configuration) {
        return getArrayType(configuration, elementType.getCastTypeName(configuration));
    }

    @Override
    public final String getDDLTypeName() {
        return getDDLTypeName(CONFIG.get());
    }

    @Override
    public final String getDDLTypeName(Configuration configuration) {
        return getArrayType(configuration, elementType.getDDLTypeName(configuration));
    }

    @Override
    public final Row getRow() {
        return elementType.getRow();
    }

    @Override
    public final Class<? extends Record> getRecordType() {
        return elementType.getRecordType();
    }

    @Override
    public final Class<?> getArrayComponentType() {
        return elementType.getType();
    }

    @Override
    public final DataType<?> getArrayComponentDataType() {
        return elementType;
    }

    @Override
    public final Class<?> getArrayBaseType() {
        return getArrayBaseDataType().getType();
    }

    @Override
    public final DataType<?> getArrayBaseDataType() {
        DataType<?> result = this;
        DataType<?> t;

        while ((t = result.getArrayComponentDataType()) != null)
            result = t;

        return result;
    }

    private static String getArrayType(Configuration configuration, String dataType) {
        if (DefaultDataType.SUPPORT_POSTGRES_SUFFIX_ARRAY_NOTATION.contains(configuration.dialect()))
            return dataType + "[]";

        else if (DefaultDataType.SUPPORT_TRINO_ARRAY_NOTATION.contains(configuration.dialect()))
            return "Array(" + dataType + ")";









        else
            return dataType + " array";
    }
}
