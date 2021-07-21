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
import static org.jooq.impl.Tools.recordType;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jooq.CharacterSet;
import org.jooq.Collation;
import org.jooq.Field;
import org.jooq.Nullability;
import org.jooq.Record;
import org.jooq.Row;

/**
 * A wrapper for anonymous row data types.
 *
 * @author Lukas Eder
 */
final class RecordDataType<R extends Record> extends DefaultDataType<R> {

    final AbstractRow<R> row;

    @SuppressWarnings("unchecked")
    public RecordDataType(Row row) {
        this(row, (Class<R>) recordType(row.size()), "record");
    }

    @SuppressWarnings("unchecked")
    public RecordDataType(Row row, Class<R> recordType, String name) {
        super(null, recordType, name, name);

        this.row = (AbstractRow<R>) row;
    }

    /**
     * [#3225] Performant constructor for creating derived types.
     */
    RecordDataType(
        DefaultDataType<R> t,
        AbstractRow<R> row,
        Integer precision,
        Integer scale,
        Integer length,
        Nullability nullability,
        Collation collation,
        CharacterSet characterSet,
        boolean identity,
        Field<R> defaultValue
    ) {
        super(t, precision, scale, length, nullability, collation, characterSet, identity, defaultValue);

        this.row = row;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    DefaultDataType<R> construct(
        Integer newPrecision,
        Integer newScale,
        Integer newLength,
        Nullability
        newNullability,
        Collation newCollation,
        CharacterSet newCharacterSet,
        boolean newIdentity,
        Field<R> newDefaultValue
    ) {
        return new RecordDataType<>(
            this,
            row,
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
        return getType();
    }

    @SuppressWarnings("unchecked")
    @Override
    public R convert(Object object) {

        // [#12116] TODO: Move this logic into JSONReader to make it more generally useful
        if (object instanceof Record || object instanceof Map || object instanceof List) {
            return newRecord(true, getRecordType(), row, CTX.configuration())
                .operate(r -> {

                    // [#12014] TODO: Fix this and remove workaround
                    if (object instanceof Record)
                        ((AbstractRecord) r).fromArray(((Record) object).intoArray());

                    // This sort is required if we use the JSONFormat.RecordFormat.OBJECT encoding (e.g. in SQL Server)
                    else if (object instanceof Map)
                        r.from(((Map<String, ?>) object).entrySet().stream().sorted(comparing(Entry::getKey)).map(Entry::getValue).collect(toList()));
                    else
                        r.from(object);

                    return r;
                });
        }
        else
            return super.convert(object);
    }
}
