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

import static org.jooq.impl.DSL.row;
import static org.jooq.impl.Tools.filterOne;
import static org.jooq.impl.Tools.first;
import static org.jooq.impl.Tools.list;
import static org.jooq.impl.Tools.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.jooq.ConstraintEnforcementStep;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.RowN;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.exception.DetachedException;

/**
 * @author Lukas Eder
 */
final class ReferenceImpl<R extends Record, O extends Record> extends AbstractKey<R> implements ForeignKey<R, O> {

    private final UniqueKey<O>       uk;
    private final TableField<O, ?>[] ukFields;

    ReferenceImpl(Table<R> table, Name name, TableField<R, ?>[] fkFields, UniqueKey<O> uk, TableField<O, ?>[] ukFields, boolean enforced) {
        super(table, name, fkFields, enforced);

        this.uk = uk;
        this.ukFields = ukFields;
    }

    @Override
    public final UniqueKey<O> getKey() {
        return uk;
    }

    @Override
    public final List<TableField<O, ?>> getKeyFields() {
        return Arrays.asList(ukFields);
    }

    @Override
    public final TableField<O, ?>[] getKeyFieldsArray() {
        return ukFields;
    }

    @Override
    public final O fetchParent(R record) {
        return filterOne(fetchParents(record));
    }

    @Override
    @SafeVarargs
    public final Result<O> fetchParents(R... records) {
        return fetchParents(list(records));
    }

    @Override
    public final Result<O> fetchParents(Collection<? extends R> records) {
        if (records == null || records.size() == 0)
            return new ResultImpl<>(new DefaultConfiguration(), uk.getFields());
        else
            return extractDSLContext(records).selectFrom(parents(records)).fetch();
    }

    @Override
    public final Result<R> fetchChildren(O record) {
        return fetchChildren(list(record));
    }

    @Override
    @SafeVarargs
    public final Result<R> fetchChildren(O... records) {
        return fetchChildren(list(records));
    }

    @Override
    public final Result<R> fetchChildren(Collection<? extends O> records) {
        if (records == null || records.size() == 0)
            return new ResultImpl<>(new DefaultConfiguration(), getFields());
        else
            return extractDSLContext(records).selectFrom(children(records)).fetch();
    }

    @Override
    public final Table<O> parent(R record) {
        return parents(list(record));
    }

    @SafeVarargs
    @Override
    public final Table<O> parents(R... records) {
        return parents(list(records));
    }

    @Override
    public final Table<O> parents(Collection<? extends R> records) {
        return table(records, uk.getTable(), uk.getFieldsArray(), getFieldsArray());
    }

    @Override
    public final Table<R> children(O record) {
        return children(list(record));
    }

    @SafeVarargs
    @Override
    public final Table<R> children(O... records) {
        return children(list(records));
    }

    @Override
    public final Table<R> children(Collection<? extends O> records) {
        return table(records, getTable(), getFieldsArray(), uk.getFieldsArray());
    }

    @SuppressWarnings("unchecked")
    private static <R1 extends Record, R2 extends Record> Table<R1> table(
        Collection<? extends R2> records,
        Table<R1> table,
        TableField<R1, ?>[] fields1,
        TableField<R2, ?>[] fields2
    ) {
        // [#11580] Some dialects support foreign keys with shorter column lists
        //          than their referenced primary keys
        TableField<R1, ?>[] f1 = truncate(fields1, fields2);
        TableField<R2, ?>[] f2 = truncate(fields2, fields1);

        return new InlineDerivedTable<>(
            table,
            f1.length == 1
                ? ((Field<Object>) f1[0]).in(extractValues(records, f2[0]))
                : row(f1).in(extractRows(records, f2))
        );
    }

    private static <R extends Record> TableField<R, ?>[] truncate(TableField<R, ?>[] fields1, TableField<?, ?>[] fields2) {
        if (fields1.length <= fields2.length)
            return fields1;
        else
            return Arrays.copyOf(fields1, fields2.length);
    }

    /**
     * Extract a list of values from a set of records given some fields
     */
    private static <R extends Record> List<Object> extractValues(Collection<? extends R> records, TableField<R, ?> field2) {
        return map(records, r -> r.get(field2));
    }

    /**
     * Extract a list of row value expressions from a set of records given some fields
     */
    private static <R extends Record> List<RowN> extractRows(Collection<? extends R> records, TableField<R, ?>[] fields) {
        return map(records, r -> {
            Object[] values = map(fields, f -> r.get(f), Object[]::new);
            return row(values);
        });
    }

    /**
     * Extract a configuration from the first record of a collection of records
     */
    private static <R extends Record> DSLContext extractDSLContext(Collection<? extends R> records) {
        R first = first(records);

        if (first != null)
            return DSL.using(first.configuration());
        else
            throw new DetachedException("Supply at least one attachable record");
    }

    @Override
    final ConstraintEnforcementStep constraint0() {
        return DSL.constraint(getName())
                  .foreignKey(getFieldsArray())
                  .references(uk.getTable(), getKeyFieldsArray());
    }
}
