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

import java.util.function.BiFunction;
import java.util.function.Predicate;

import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Function1;
import org.jooq.Param;
import org.jooq.ParamMode;
import org.jooq.QueryPart;
// ...
// ...
import org.jooq.conf.ParamType;

/**
 * A {@link Param} wrapper object that allows for lazily initialising the value
 * and its potentially required calls to custom converters.
 * <p>
 * See [#7742] for details.
 *
 * @author Lukas Eder
 */
final class LazyVal<T> extends AbstractParamX<T> {

    private final Field<T>              field;
    private transient AbstractParamX<T> delegate;

    LazyVal(Field<T> field) {
        super(Names.N_VALUE, field.getDataType());

        this.field = field;
    }

    private final void init() {
        if (delegate == null)
            delegate = (AbstractParamX<T>) DSL.val(null, field);
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
            return null;

        init();
        return delegate.getValue();
    }

    @Override
    public final void setConverted0(Object value) {
        init();
        delegate.setConverted0(value);
    }

    @Override
    public final void setInline0(boolean inline) {
        init();
        delegate.setInline0(inline);
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

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final T $value() {
        return delegate.$value();
    }

    @Override
    public final Param<T> $value(T value) {
        return delegate.$value(value);
    }

    @Override
    public final boolean $inline() {
        return delegate.$inline();
    }

    @Override
    public final Param<T> $inline(boolean inline) {
        return delegate.$inline(inline);
    }
















}
