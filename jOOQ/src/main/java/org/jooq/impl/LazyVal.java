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
import org.jooq.Field;
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
 */
final class LazyVal<T> extends AbstractField<T> implements Param<T> {

    private static final long  serialVersionUID = 1258437916133900173L;
    private final T            value;
    private final Field<T>     field;
    private transient Param<T> delegate;

    LazyVal(T value, Field<T> field) {
        super(AbstractParam.name(value, null), field.getDataType());

        this.value = value;
        this.field = field;
    }

    private final void init() {
        if (delegate == null)
            delegate = DSL.val(value, field);
    }

    // ------------------------------------------------------------------------
    // XXX: Field API
    // ------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        init();
        ctx.visit(delegate);
    }

    // ------------------------------------------------------------------------
    // XXX: Param API
    // ------------------------------------------------------------------------

    @Override
    public final String getParamName() {
        if (delegate == null)
            return null;

        init();
        return delegate.getParamName();
    }

    @Override
    public final T getValue() {
        if (delegate == null)
            return value;

        init();
        return delegate.getValue();
    }

    @Override
    public final void setValue(T value) {
        init();
        delegate.setValue(value);
    }

    @Override
    public final void setConverted(Object value) {
        init();
        delegate.setConverted(value);
    }

    @Override
    public final void setInline(boolean inline) {
        init();
        delegate.setInline(inline);
    }

    @Override
    public final boolean isInline() {
        if (delegate == null)
            return false;

        init();
        return delegate.isInline();
    }

    @Override
    public final ParamType getParamType() {
        if (delegate == null)
            return ParamType.INDEXED;

        init();
        return delegate.getParamType();
    }

    @Override
    public final ParamMode getParamMode() {
        if (delegate == null)
            return ParamMode.IN;

        init();
        return delegate.getParamMode();
    }
}
