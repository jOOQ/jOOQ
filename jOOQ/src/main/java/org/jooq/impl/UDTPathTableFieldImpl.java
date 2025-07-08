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
import org.jooq.DataType;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.RecordQualifier;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UDT;
import org.jooq.UDTPathField;
import org.jooq.UDTPathTableField;
import org.jooq.UDTRecord;

import org.jetbrains.annotations.NotNull;

/**
 * A common base type for table fields that are also {@link UDTPathField}.
 *
 * @author Lukas Eder
 */
@org.jooq.Internal
public /* non-final */ class UDTPathTableFieldImpl<R extends Record, U extends UDTRecord<U>, T>
extends
    UDTPathFieldImpl<R, U, T>
implements
    UDTPathTableField<R, U, T>
{

    public UDTPathTableFieldImpl(Name name, DataType<T> type, RecordQualifier<R> qualifier, UDT<U> udt, Comment comment, Binding<?, T> binding) {
        super(name, type, qualifier, udt, comment, binding);
    }

    @Override
    public final Table<R> getTable() {
        return getQualifier() instanceof Table<R> t ? t : null;
    }
}
