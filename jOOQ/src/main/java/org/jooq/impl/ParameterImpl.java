/*
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */

package org.jooq.impl;

import org.jooq.Binding;
import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Converter;
import org.jooq.DataType;
import org.jooq.Parameter;
import org.jooq.tools.StringUtils;

/**
 * A common base class for stored procedure parameters
 *
 * @author Lukas Eder
 */
final class ParameterImpl<T> extends AbstractQueryPart implements Parameter<T> {

    private static final long serialVersionUID = -5277225593751085577L;

    private final String      name;
    private final DataType<T> type;
    private final boolean     isDefaulted;
    private final boolean     isUnnamed;

    @SuppressWarnings("unchecked")
    ParameterImpl(String name, DataType<T> type, Binding<?, T> binding, boolean isDefaulted, boolean isUnnamed) {
        this.name = name;
        this.type = type.asConvertedDataType((Binding<T, T>) binding);
        this.isDefaulted = isDefaulted;
        this.isUnnamed = isUnnamed;
    }

    @Override
    public final String getName() {
        return name;
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
        ctx.literal(getName());
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

    // ------------------------------------------------------------------------
    // XXX: Object API
    // ------------------------------------------------------------------------

    @Override
    public int hashCode() {

        // [#1938] This is a much more efficient hashCode() implementation
        // compared to that of standard QueryParts
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }

        // [#1626] ParameterImpl equality can be decided without executing the
        // rather expensive implementation of AbstractQueryPart.equals()
        if (that instanceof ParameterImpl) {
            return StringUtils.equals(name, ((ParameterImpl<?>) that).name);
        }

        return super.equals(that);
    }
}
