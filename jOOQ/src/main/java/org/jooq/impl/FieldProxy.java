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

import org.jooq.Binding;
import org.jooq.CharacterSet;
import org.jooq.Clause;
import org.jooq.Collation;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Nullability;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.TableField;

/**
 * A {@link Field} that acts as another field, allowing for the proxied field to
 * be replaced.
 *
 * @author Lukas Eder
 */
@SuppressWarnings("unchecked")
final class FieldProxy<T> extends AbstractField<T> implements TableField<Record, T> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 8311876498583467760L;

    private AbstractField<T>  delegate;
    private int               position;

    FieldProxy(AbstractField<T> delegate, int position) {
        super(
            delegate.getQualifiedName(),
            new DataTypeProxy<>((AbstractDataType<T>) delegate.getDataType()),
            delegate.getCommentPart(),
            delegate.getBinding()
        );

        this.delegate = delegate;
        this.position = position;
    }

    int position() {
        return position;
    }

    void delegate(AbstractField<T> newDelegate) {
        this.delegate = newDelegate;

        ((DataTypeProxy<T>) getDataType()).type = (AbstractDataType<T>) newDelegate.getDataType();
    }

    @Override
    public final Name getQualifiedName() {
        return delegate.getQualifiedName();
    }

    @Override
    public final int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public final boolean equals(Object that) {
        return delegate.equals(that);
    }

    @Override
    public final boolean declaresFields() {
        return delegate.declaresFields();
    }

    @Override
    public final boolean declaresTables() {
        return delegate.declaresTables();
    }

    @Override
    public final boolean declaresWindows() {
        return delegate.declaresWindows();
    }

    @Override
    public final boolean declaresCTE() {
        return delegate.declaresCTE();
    }

    @Override
    public final boolean generatesCast() {
        return delegate.generatesCast();
    }

    @Override
    public final boolean rendersContent(Context<?> ctx) {
        return delegate.rendersContent(ctx);
    }

    @Override
    public final void accept(Context<?> ctx) {
        delegate.accept(ctx);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return delegate.clauses(ctx);
    }

    @Override
    public final String toString() {
        return delegate.toString();
    }

    @Override
    public final Table<Record> getTable() {
        return delegate instanceof TableField ? ((TableField<Record, ?>) delegate).getTable() : null;
    }

    private static class DataTypeProxy<T> extends AbstractDataType<T> {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = -5283787691586689803L;
        AbstractDataType<T>       type;

        DataTypeProxy(AbstractDataType<T> type) {
            super(type.getQualifiedName(), type.getCommentPart());

            this.type = type;
        }

        @Override
        public final Name getQualifiedName() {
            return type.getQualifiedName();
        }

        @Override
        public final DataType<T> getSQLDataType() {
            return type.getSQLDataType();
        }

        @Override
        public final DataType<T> getDataType(Configuration configuration) {
            return type.getDataType(configuration);
        }

        @Override
        public final Binding<?, T> getBinding() {
            return type.getBinding();
        }

        @Override
        public final Class<T> getType() {
            return type.getType();
        }

        @Override
        public final SQLDialect getDialect() {
            return type.getDialect();
        }

        @Override
        public final Nullability nullability() {
            return type.nullability();
        }

        @Override
        public final Collation collation() {
            return type.collation();
        }

        @Override
        public final CharacterSet characterSet() {
            return type.characterSet();
        }

        @Override
        public final boolean identity() {
            return type.identity();
        }

        @Override
        public final Field<T> default_() {
            return type.default_();
        }

        @Override
        final AbstractDataType<T> construct(
            Integer newPrecision,
            Integer newScale,
            Integer newLength,
            Nullability newNullability,
            Collation newCollation,
            CharacterSet newCharacterSet,
            boolean newIdentity,
            Field<T> newDefaultValue
        ) {
            return type.construct(newPrecision, newScale, newLength, newNullability, newCollation, newCharacterSet, newIdentity, newDefaultValue);
        }

        @Override
        final String typeName0() {
            return type.typeName0();
        }

        @Override
        final String castTypeBase0() {
            return type.castTypeBase0();
        }

        @Override
        final String castTypeName0() {
            return type.castTypeName0();
        }

        @Override
        final Class<?> tType0() {
            return type.tType0();
        }

        @Override
        final Integer precision0() {
            return type.precision0();
        }

        @Override
        final Integer scale0() {
            return type.scale0();
        }

        @Override
        final Integer length0() {
            return type.length0();
        }
    }
}