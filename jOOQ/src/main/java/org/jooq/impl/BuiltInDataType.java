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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
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

import static org.jooq.Nullability.NOT_NULL;
import static org.jooq.Nullability.NULL;

import org.jooq.DataType;
import org.jooq.Nullability;
import org.jooq.SQLDialect;
import org.jooq.util.postgres.PostgresDataType;

import org.jetbrains.annotations.ApiStatus.Internal;

/**
 * An internal marker subtype of {@link DefaultDataType}, to be used only by
 * built-in data types in {@link SQLDataType} and dialect specific data type
 * classes, such as e.g. {@link PostgresDataType}.
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

    // [#11083] Nullability caches of built-in data types

    final DataType<T> cachedNull;
    final DataType<T> cachedNotNull;

    {
        cachedNull = super.nullability(NULL);
        cachedNotNull = super.nullability(NOT_NULL);
    }

    @Override
    public final DataType<T> nullability(Nullability n) {
        if (n == NULL)
            return cachedNull;
        else if (n == NOT_NULL)
            return cachedNotNull;
        else
            return super.nullability(n);
    }
}
