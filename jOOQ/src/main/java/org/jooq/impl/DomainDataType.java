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

import org.jooq.CharacterSet;
import org.jooq.Collation;
import org.jooq.Configuration;
import org.jooq.ConverterContext;
import org.jooq.DataType;
import org.jooq.Domain;
import org.jooq.Field;
import org.jooq.Generator;
import org.jooq.Nullability;
import org.jooq.impl.QOM.GenerationLocation;
import org.jooq.impl.QOM.GenerationOption;

/**
 * A <code>DataType</code> used for {@link Domain} types.
 *
 * @author Lukas Eder
 */
final class DomainDataType<T> extends DefaultDataType<T> {

    private final Domain<T>   domain;
    private final DataType<T> baseType;

    DomainDataType(Domain<T> domain, DataType<T> baseType) {
        super(
            null,
            baseType.getSQLDataType(),
            baseType.getType(),
            baseType.getBinding(),
            baseType.getQualifiedName(),
            baseType.getTypeName(),
            baseType.getCastTypeName(),
            baseType.precisionDefined() ? baseType.precision() : null,
            baseType.scaleDefined() ? baseType.scale() : null,
            baseType.lengthDefined() ? baseType.length() : null,
            baseType.nullability(),
            baseType.hidden(),
            baseType.redacted(),
            baseType.readonly(),
            baseType.generatedAlwaysAsGenerator(),
            baseType.generationOption(),
            baseType.generationLocation(),
            null, // TODO: Collation
            null, // TODO: CharacterSet (?)
            false,
            baseType.default_()
        );

        this.domain = domain;
        this.baseType = baseType;
    }

    @Override
    DefaultDataType<T> construct(
        Integer newPrecision,
        Integer newScale,
        Integer newLength,
        Nullability newNullability,
        boolean newHidden,
        boolean newRedacted,
        boolean newReadonly,
        Generator<?, ?, T> newGeneratedAlwaysAs,
        GenerationOption newGenerationOption,
        GenerationLocation newGenerationLocation,
        Collation newCollation,
        CharacterSet newCharacterSet,
        boolean newIdentity,
        Field<T> newDefaultValue
    ) {
        return new DomainDataType<>(
            domain,
            ((AbstractDataTypeX<T>) baseType).construct(
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
                newDefaultValue
            )
        );
    }

    @Override
    final T convert(Object object, ConverterContext cc) {
        if (baseType instanceof AbstractDataType<T> a)
            return a.convert(object, cc);
        else
            return super.convert(object, cc);
    }

    @Override
    public final Domain<T> getDomain() {
        return domain;
    }

    @Override
    public final int getSQLType() {
        return baseType.getSQLType();
    }

    @Override
    public final String getTypeName(Configuration configuration) {
        return baseType.getTypeName(configuration);
    }

    @Override
    public final String getCastTypeName(Configuration configuration) {
        return baseType.getCastTypeName(configuration);
    }
}
