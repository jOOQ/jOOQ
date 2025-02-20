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

import org.jooq.Row;
import org.jooq.UDT;
import org.jooq.UDTRecord;

import org.jetbrains.annotations.NotNull;

/**
 * A record implementation for a record originating from a single UDT
 * <p>
 * This type is for JOOQ INTERNAL USE only. Do not reference directly
 *
 * @author Lukas Eder
 */
@org.jooq.Internal
public class UDTRecordImpl<R extends UDTRecord<R>> extends AbstractQualifiedRecord<R> implements UDTRecord<R> {

    public UDTRecordImpl(UDT<R> udt) {
        super(udt);
    }

    @Override
    public final UDT<R> getUDT() {
        return (UDT<R>) getQualifier();
    }

    @Override
    public String toString() {
        return DSL.using(configuration()).renderInlined(DSL.inline(this));
    }

    // [#8489] [#18033] [#12180] these overrides are necessary due to a Scala compiler bug (versions 2.10, 2.11, 3.5, 3.6)
    // See:
    // - https://github.com/scala/bug/issues/7936
    // - https://github.com/scala/scala3/issues/22628

    @Override
    public /* non-final */ Row fieldsRow() {
        return super.fieldsRow();
    }

    @Override
    public /* non-final */ Row valuesRow() {
        return super.valuesRow();
    }
}
