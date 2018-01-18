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
import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Converter;
import org.jooq.DataType;
import org.jooq.Parameter;

/**
 * A common base class for stored procedure parameters
 *
 * @author Lukas Eder
 */
final class ParameterImpl<T> extends AbstractNamed implements Parameter<T> {

    private static final long serialVersionUID = -5277225593751085577L;

    private final DataType<T> type;
    private final boolean     isDefaulted;
    private final boolean     isUnnamed;

    @SuppressWarnings("unchecked")
    ParameterImpl(String name, DataType<T> type, Binding<?, T> binding, boolean isDefaulted, boolean isUnnamed) {
        super(DSL.name(name), CommentImpl.NO_COMMENT);

        this.type = type.asConvertedDataType((Binding<T, T>) binding);
        this.isDefaulted = isDefaulted;
        this.isUnnamed = isUnnamed;
    }

    @Override
    public final Converter<?, T> getConverter() {
        return type.getBinding().converter();
    }

    @Override
    public final Binding<?, T> getBinding() {
        return type.getBinding();
    }

    @Override
    public final DataType<T> getDataType() {
        return type;
    }

    @Override
    public final DataType<T> getDataType(Configuration configuration) {
        return type.getDataType(configuration);
    }

    @Override
    public final Class<T> getType() {
        return type.getType();
    }

    @Override
    public final void accept(Context<?> ctx) {
        ctx.visit(getUnqualifiedName());
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return null;
    }

    @Override
    public final boolean isDefaulted() {
        return isDefaulted;
    }

    @Override
    public final boolean isUnnamed() {
        return isUnnamed;
    }
}
