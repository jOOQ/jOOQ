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

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static org.jooq.impl.Tools.CONFIG;
import static org.jooq.impl.Tools.newRecord;
import static org.jooq.impl.Tools.recordType;

import java.sql.Struct;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jooq.CharacterSet;
import org.jooq.Collation;
import org.jooq.ConverterContext;
import org.jooq.Field;
import org.jooq.Generator;
import org.jooq.Nullability;
import org.jooq.Record;
import org.jooq.Row;
import org.jooq.impl.QOM.GenerationLocation;
import org.jooq.impl.QOM.GenerationOption;

/**
 * A wrapper for anonymous row data types.
 *
 * @author Lukas Eder
 */
final class RecordDataType<R extends Record> extends DefaultDataType<R> {

    final AbstractRow<R> row;

    @SuppressWarnings("unchecked")
    RecordDataType(Row row) {
        this(row, (Class<R>) recordType(row.size()), "record");
    }

    @SuppressWarnings("unchecked")
    RecordDataType(Row row, Class<R> recordType, String name) {
        super(null, recordType, name, nullability(row));

        this.row = (AbstractRow<R>) row;
    }

    static final Nullability nullability(Row row) {
        return Tools.anyMatch(row.fields(), f -> f.getDataType().nullable())
             ? Nullability.NULL
             : Nullability.NOT_NULL;
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
        boolean hidden,
        boolean readonly,
        Generator<?, ?, R> generatedAlwaysAs,
        GenerationOption generationOption,
        GenerationLocation generationLocation,
        Collation collation,
        CharacterSet characterSet,
        boolean identity,
        Field<R> defaultValue
    ) {
        super(t, precision, scale, length, nullability, hidden, readonly, generatedAlwaysAs, generationOption, generationLocation, collation, characterSet, identity, defaultValue);

        this.row = row;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    DefaultDataType<R> construct(
        Integer newPrecision,
        Integer newScale,
        Integer newLength,
        Nullability newNullability,
        boolean newHidden,
        boolean newReadonly,
        Generator<?, ?, R> newGeneratedAlwaysAs,
        GenerationOption newGenerationOption,
        GenerationLocation newGenerationLocation,
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
            newHidden,
            newReadonly,
            newGeneratedAlwaysAs,
            newGenerationOption,
            newGenerationLocation,
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
    final R convert(Object object, ConverterContext cc) {

        // [#12269] [#13403] Don't re-copy perfectly fine results.
        if (object instanceof Record && ((Record) object).fieldsRow().equals(row))
            return (R) object;

        // [#12116] TODO: Move this logic into JSONReader to make it more generally useful
        else if (
            object instanceof Record
         || object instanceof Map
         || object instanceof List
         || object instanceof Struct
        ) {
            return newRecord(true, cc.configuration(), getRecordType(), row)
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
            return super.convert(object, cc);
    }
}
