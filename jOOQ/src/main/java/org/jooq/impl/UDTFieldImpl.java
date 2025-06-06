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

import org.jooq.Binding;
import org.jooq.Comment;
import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Name;
import org.jooq.UDT;
import org.jooq.UDTField;
import org.jooq.UDTRecord;
import org.jooq.impl.QOM.UNotYetImplemented;

/**
 * A common base type for UDT attributes / fields
 *
 * @author Lukas Eder
 */
final class UDTFieldImpl<R extends UDTRecord<R>, T> extends AbstractField<T> implements UDTField<R, T>, UNotYetImplemented, TypedReference<T> {

    private final UDT<R> udt;

    UDTFieldImpl(Name name, DataType<T> type, UDT<R> udt, Comment comment, Binding<?, T> binding) {
        super(name, type, comment, binding);

        this.udt = udt;

        // [#1199] The public API of UDT returns immutable field lists
        if (udt instanceof UDTImpl<?> u)
            u.fields0().add(this);
    }

    @Override
    public final UDT<R> getUDT() {
        return udt;
    }

    @Override
    public final void accept(Context<?> ctx) {
        DefaultRenderContext.literal(ctx, getName());
    }
}
