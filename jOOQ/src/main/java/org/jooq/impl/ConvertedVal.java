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
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
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

import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Param;
import org.jooq.ParamMode;
import org.jooq.conf.ParamType;
import org.jooq.impl.QOM.NotYetImplementedException;
import org.jooq.impl.QOM.UNotYetImplemented;

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
final class ConvertedVal<T> extends AbstractParamX<T> implements UNotYetImplemented {

    final AbstractParamX<?> delegate;

    ConvertedVal(AbstractParamX<?> delegate, DataType<T> type) {
        super(delegate.getUnqualifiedName(), type);

        this.delegate = delegate instanceof ConvertedVal<?> c ? c.delegate : delegate;
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
    public final void setConverted0(Object value) {
        delegate.setConverted0(value);
    }

    @Override
    public final void setInline0(boolean inline) {
        delegate.setInline0(inline);
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

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final T $value() {
        return getValue();
    }

    @Override
    public final Param<T> $value(T value) {
        return ((AbstractParamX) delegate).$value(delegate.getDataType().convert(value));
    }

    @Override
    public final boolean $inline() {
        return delegate.$inline();
    }

    @Override
    public final Param<T> $inline(boolean inline) {
        throw new NotYetImplementedException();
    }

    // -------------------------------------------------------------------------
    // The Object API
    // -------------------------------------------------------------------------

    @Override
    public String toString() {
        return delegate.toString();
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public boolean equals(Object that) {
        return delegate.equals(that);
    }
}
