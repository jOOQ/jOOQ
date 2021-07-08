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

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static org.jooq.impl.Tools.CTX;
import static org.jooq.impl.Tools.newRecord;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jooq.CharacterSet;
import org.jooq.Collation;
import org.jooq.Field;
import org.jooq.Nullability;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Row;

/**
 * A wrapper for anonymous multiset data types.
 *
 * @author Lukas Eder
 */
final class MultisetDataType<R extends Record> extends DefaultDataType<Result<R>> {

    final AbstractRow<R>     row;
    final Class<? extends R> recordType;

    @SuppressWarnings("unchecked")
    public MultisetDataType(AbstractRow<R> row, Class<? extends R> recordType) {
        // [#11829] TODO: Implement this correctly for ArrayRecord
        super(null, (Class) Result.class, "multiset", "multiset");

        this.row = row;
        this.recordType = recordType != null ? recordType : (Class<? extends R>) Record.class;
    }

    /**
     * [#3225] Performant constructor for creating derived types.
     */
    MultisetDataType(
        DefaultDataType<Result<R>> t,
        AbstractRow<R> row,
        Class<? extends R> recordType,
        Integer precision,
        Integer scale,
        Integer length,
        Nullability nullability,
        Collation collation,
        CharacterSet characterSet,
        boolean identity,
        Field<Result<R>> defaultValue
    ) {
        super(t, precision, scale, length, nullability, collation, characterSet, identity, defaultValue);

        this.row = row;
        this.recordType = recordType;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    DefaultDataType<Result<R>> construct(
        Integer newPrecision,
        Integer newScale,
        Integer newLength,
        Nullability
        newNullability,
        Collation newCollation,
        CharacterSet newCharacterSet,
        boolean newIdentity,
        Field<Result<R>> newDefaultValue
    ) {
        return new MultisetDataType<>(
            this,
            row,
            recordType,
            newPrecision,
            newScale,
            newLength,
            newNullability,
            newCollation,
            newCharacterSet,
            newIdentity,
            (Field) newDefaultValue
        );
    }

    @Override
    public final Row getRow() {
        return row;
    }

    @Override
    public final Class<? extends R> getRecordType() {
        return recordType;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Result<R> convert(Object object) {

        // [#3884] TODO: Move this logic into JSONReader to make it more generally useful
        if (object instanceof List) {
            ResultImpl<R> result = new ResultImpl<>(CTX.configuration(), row);

            for (Object record : (List) object)
                result.add(newRecord(true, recordType, row, CTX.configuration())
                    .operate(r -> {

                        // [#12014] TODO: Fix this and remove workaround
                        if (record instanceof Record)
                            ((AbstractRecord) r).fromArray(((Record) record).intoArray());

                        // This sort is required if we use the JSONFormat.RecordFormat.OBJECT encoding (e.g. in SQL Server)
                        else if (record instanceof Map)
                            r.from(((Map<String, ?>) record).entrySet().stream().sorted(comparing(Entry::getKey)).map(Entry::getValue).collect(toList()));
                        else
                            r.from(record);

                        return r;
                    }));

            return result;
        }
        else if (object == null)
            return new ResultImpl<>(CTX.configuration(), row);
        else
            return super.convert(object);
    }
}
