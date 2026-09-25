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

import static org.jooq.Nullability.NOT_NULL;
import static org.jooq.Nullability.NULL;

import org.jooq.CharacterSet;
import org.jooq.Collation;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Generator;
import org.jooq.Nullability;
import org.jooq.SQLDialect;
import org.jooq.impl.QOM.GenerationLocation;
import org.jooq.impl.QOM.GenerationMode;
import org.jooq.impl.QOM.GenerationOption;
import org.jooq.impl.QOM.LengthUnit;

import org.jetbrains.annotations.ApiStatus.Internal;

/**
 * An internal marker subtype of {@link DefaultDataType}, to be used only by
 * built-in data types in {@link SQLDataType} and dialect specific data type
 * classes.
 *
 * @author Lukas Eder
 */
@Internal
public class BuiltInDataType<T> extends DefaultDataType<T> {

    /**
     * Constructor for {@link SQLDataType} types.
     */
    public BuiltInDataType(Class<T> type, String typeName) {
        super(null, type, typeName);
    }

    /**
     * Constructor for dialect specific data types.
     */
    public BuiltInDataType(SQLDialect dialect, DataType<T> sqlDataType, String typeName) {
        super(dialect, sqlDataType, typeName);
    }

    /**
     * Constructor for dialect specific data types.
     */
    public BuiltInDataType(SQLDialect dialect, DataType<T> sqlDataType, String typeName, String castTypeName) {
        super(dialect, sqlDataType, typeName, castTypeName);
    }

    /**
     * Constructor for dialect specific data types.
     */
    public BuiltInDataType(SQLDialect dialect, DataType<T> sqlDataType, String typeName, String castTypeName, String ddlTypeName) {
        super(dialect, sqlDataType, typeName, castTypeName, ddlTypeName);
    }

    private BuiltInDataType(
        BuiltInDataType<T> t,
        Integer precision,
        Integer scale,
        Integer length,
        LengthUnit lengthUnit,
        Nullability nullability,
        boolean hidden,
        boolean redacted,
        boolean readonly,
        Generator<?, ?, T> generatedAlwaysAs,
        GenerationOption generationOption,
        GenerationLocation generationLocation,
        Collation collation,
        CharacterSet characterSet,
        GenerationMode identity,
        Field<T> defaultValue
    ) {
        super(
            t,
            precision,
            scale,
            length,
            lengthUnit,
            nullability,
            hidden,
            redacted,
            readonly,
            generatedAlwaysAs,
            generationOption,
            generationLocation,
            collation,
            characterSet,
            identity,
            defaultValue
        );
    }

    /**
     * [#17832] Types derived from built-in data types should still be
     * built-in data types.
     */
    @Override
    final BuiltInDataType<T> construct(
        Integer newPrecision,
        Integer newScale,
        Integer newLength,
        LengthUnit newLengthUnit,
        Nullability newNullability,
        boolean newHidden,
        boolean newRedacted,
        boolean newReadonly,
        Generator<?, ?, T> newGeneratedAlwaysAs,
        GenerationOption newGenerationOption,
        GenerationLocation newGenerationLocation,
        Collation newCollation,
        CharacterSet newCharacterSet,
        GenerationMode newIdentity,
        Field<T> newDefaultValue
    ) {
        return new BuiltInDataType<>(
            this,
            newPrecision,
            newScale,
            newLength,
            newLengthUnit,
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
        );
    }

    // [#11083] Nullability caches of built-in data types
    // [#17832] Lazy initialisation to prevent infinite recursion

    private transient DataType<T> cachedNull;
    private transient DataType<T> cachedNotNull;

    @Override
    public final DataType<T> nullability(Nullability n) {
        if (n == nullability())
            return this;

        if (n == NULL) {
            DataType<T> result = cachedNull;

            if (result == null)
                cachedNull = result = super.nullability(n);

            return result;
        }
        else if (n == NOT_NULL) {
            DataType<T> result = cachedNotNull;

            if (result == null)
                cachedNotNull = result = super.nullability(n);

            return result;
        }
        else
            return super.nullability(n);
    }
}
