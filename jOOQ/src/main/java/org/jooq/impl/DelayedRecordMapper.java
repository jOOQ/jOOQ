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

import java.util.function.Function;

import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.RecordType;

/**
 * A {@link RecordMapper} implementation offering access to a delegate record
 * mapper whose initialisation is delayed until the first record, e.g. for
 * {@link ResultQuery} implementations whose {@link RecordType} is unknown prior
 * to execution.
 *
 * @author Lukas Eder
 */
final class DelayedRecordMapper<R extends Record, E> implements RecordMapper<R, E> {

    final Function<FieldsImpl<R>, RecordMapper<R, E>> init;
    RecordMapper<R, E>                                delegate;

    DelayedRecordMapper(Function<FieldsImpl<R>, RecordMapper<R, E>> init) {
        this.init = init;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final E map(R record) {
        if (delegate == null)
            delegate = init.apply((FieldsImpl<R>) ((AbstractRecord) record).fields.fields);

        return delegate.map(record);
    }
}
