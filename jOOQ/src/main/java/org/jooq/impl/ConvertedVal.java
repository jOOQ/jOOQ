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

import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Param;
import org.jooq.ParamMode;
import org.jooq.conf.ParamType;

/**
 * A {@link Param} wrapper object that allows for lazily initialising the value
 * and its potentially required calls to custom converters.
 * <p>
 * See [#7742] for details.
 *
 * @author Lukas Eder
 * @deprecated - [#11061] - 3.15.0 - This type has been introduced only because
 *             we have mutable {@link Param} types. Avoid reusing this type when
 *             not needed.
 */
@Deprecated
final class ConvertedVal<T> extends AbstractField<T> implements Param<T> {

    private static final long serialVersionUID = 1258437916133900173L;
    final Param<?>            delegate;

    ConvertedVal(Param<?> delegate, DataType<T> type) {
        super(delegate.getUnqualifiedName(), type);

        this.delegate = delegate instanceof ConvertedVal ? ((ConvertedVal<?>) delegate).delegate : delegate;
    }

    // ------------------------------------------------------------------------
    // XXX: Field API
    // ------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        ctx.visit(delegate);
    }

    // ------------------------------------------------------------------------
    // XXX: Param API
    // ------------------------------------------------------------------------

    @Override
    public final String getParamName() {
        return delegate.getParamName();
    }

    @Override
    public final T getValue() {
        return getDataType().convert(delegate.getValue());
    }

    @Override
    public final void setValue(T value) {
        delegate.setConverted(value);
    }

    @Override
    public final void setConverted(Object value) {
        delegate.setConverted(value);
    }

    @Override
    public final void setInline(boolean inline) {
        delegate.setInline(inline);
    }

    @Override
    public final boolean isInline() {
        return delegate.isInline();
    }

    @Override
    public final ParamType getParamType() {
        return delegate.getParamType();
    }

    @Override
    public final ParamMode getParamMode() {
        return delegate.getParamMode();
    }
}
