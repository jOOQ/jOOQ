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
import org.jooq.Name;
import org.jooq.QueryPart;
import org.jooq.QueryPartInternal;
// ...
import org.jooq.Schema;
// ...

/**
 * A qualified {@link Name} that is evaluated lazily, e.g. to prevent cycles in
 * static initialisation of generated code, when the {@link Schema} instance may
 * not yet be available.
 *
 * @author Lukas Eder
 */
final class LazyName extends AbstractName {

    final LazySupplier<Name> supplier;
    transient Name           name;

    LazyName(LazySupplier<Name> supplier) {
        this.supplier = supplier;
    }

    private final Name name() {
        if (name == null)
            name = supplier.get();

        return name;
    }

    // -------------------------------------------------------------------------
    // XXX: Name API
    // -------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        ((QueryPartInternal) name()).accept(ctx);
    }

    @Override
    public final String first() {
        return name().first();
    }

    @Override
    public final String last() {
        return name().last();
    }

    @Override
    public final boolean empty() {
        return name().empty();
    }

    @Override
    public final boolean qualified() {
        return name().qualified();
    }

    @Override
    public final boolean qualifierQualified() {
        return name().qualifierQualified();
    }

    @Override
    public final Name qualifier() {
        return name().qualifier();
    }

    @Override
    public final Name unqualifiedName() {
        return name().unqualifiedName();
    }

    @Override
    public final Quoted quoted() {
        return name().quoted();
    }

    @Override
    public final Name quotedName() {
        return name().quotedName();
    }

    @Override
    public final Name unquotedName() {
        return name().unquotedName();
    }

    @Override
    public final Name[] parts() {
        return name().parts();
    }

    @Override
    public final String[] getName() {
        return name().getName();
    }















    // ------------------------------------------------------------------------
    // XXX: Object API
    // ------------------------------------------------------------------------

    @Override
    public int hashCode() {
        return name().hashCode();
    }
}
