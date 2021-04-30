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

import static org.jooq.Nullability.NOT_NULL;

import org.jooq.CharacterSet;
import org.jooq.Collation;
import org.jooq.Comment;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Nullability;

/**
 * @author Lukas Eder
 */
abstract class AbstractDataTypeX<T> extends AbstractDataType<T> {

    AbstractDataTypeX(Name name, Comment comment) {
        super(name, comment);
    }

    /**
     * [#7811] Allow for subtypes to override the constructor
     */
    abstract AbstractDataTypeX<T> construct(
        Integer newPrecision,
        Integer newScale,
        Integer newLength,
        Nullability newNullability,
        Collation newCollation,
        CharacterSet newCharacterSet,
        boolean newIdentity,
        Field<T> newDefaultValue
    );

    @Override
    public final DataType<T> nullability(Nullability n) {
        return construct(precision0(), scale0(), length0(), n, collation(), characterSet(), !n.nullable() && identity(), defaultValue());
    }

    @Override
    public final DataType<T> collation(Collation c) {
        return construct(precision0(), scale0(), length0(), nullability(), c, characterSet(), identity(), defaultValue());
    }

    @Override
    public final DataType<T> characterSet(CharacterSet c) {
        return construct(precision0(), scale0(), length0(), nullability(), collation(), c, identity(), defaultValue());
    }

    @Override
    public final DataType<T> identity(boolean i) {
        return construct(precision0(), scale0(), length0(), i ? NOT_NULL : nullability(), collation(), characterSet(), i, defaultValue());
    }

    @Override
    public final DataType<T> default_(Field<T> d) {
        return construct(precision0(), scale0(), length0(), nullability(), collation(), characterSet(), identity(), d);
    }

    @Override
    final AbstractDataTypeX<T> precision1(Integer p, Integer s) {
        return construct(p, s, length0(), nullability(), collation(), characterSet(), identity(), defaultValue());
    }

    @Override
    final AbstractDataTypeX<T> scale1(Integer s) {
        return construct(precision0(), s, length0(), nullability(), collation(), characterSet(), identity(), defaultValue());
    }

    @Override
    final AbstractDataTypeX<T> length1(Integer l) {
        return construct(precision0(), scale0(), l, nullability(), collation(), characterSet(), identity(), defaultValue());
    }
}
