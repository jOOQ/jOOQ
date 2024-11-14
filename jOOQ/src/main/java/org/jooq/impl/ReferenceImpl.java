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

import static org.jooq.impl.DSL.row;
import static org.jooq.impl.Tools.filterOne;
import static org.jooq.impl.Tools.first;
import static org.jooq.impl.Tools.list;
import static org.jooq.impl.Tools.map;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.jooq.ConstraintEnforcementStep;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.InverseForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.RowN;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.exception.DetachedException;
import org.jooq.impl.QOM.ForeignKeyRule;
import org.jooq.impl.QOM.UEmpty;

/**
 * @author Lukas Eder
 */
final class ReferenceImpl<CHILD extends Record, PARENT extends Record>
extends
    AbstractKey<CHILD>
implements
    ForeignKey<CHILD, PARENT>,
    UEmpty
{

    private final UniqueKey<PARENT>       uk;
    private final TableField<PARENT, ?>[] ukFields;
    private final ForeignKeyRule          deleteRule;
    private final ForeignKeyRule          updateRule;

    ReferenceImpl(
        Table<CHILD> table,
        Name name,
        TableField<CHILD, ?>[] fkFields,
        UniqueKey<PARENT> uk,
        TableField<PARENT, ?>[] ukFields,
        boolean enforced,
        ForeignKeyRule deleteRule,
        ForeignKeyRule updateRule
    ) {
        super(table, name, fkFields, enforced);

        this.uk = uk;
        this.ukFields = ukFields;
        this.deleteRule = deleteRule;
        this.updateRule = updateRule;
    }

    @Override
    public final ForeignKeyRule getDeleteRule() {
        return deleteRule;
    }

    @Override
    public final ForeignKeyRule getUpdateRule() {
        return updateRule;
    }

    @Override
    public final InverseForeignKey<PARENT, CHILD> getInverseKey() {
        return new InverseReferenceImpl<>(this);
    }

    @Override
    public final UniqueKey<PARENT> getKey() {
        return uk;
    }

    @Override
    public final List<TableField<PARENT, ?>> getKeyFields() {
        return Arrays.asList(ukFields);
    }

    @Override
    public final TableField<PARENT, ?>[] getKeyFieldsArray() {
        return ukFields;
    }

    @Override
    public final PARENT fetchParent(CHILD record) {
        return filterOne(fetchParents(record));
    }

    @Override
    @SafeVarargs
    public final Result<PARENT> fetchParents(CHILD... records) {
        return fetchParents(list(records));
    }

    @Override
    public final Result<PARENT> fetchParents(Collection<? extends CHILD> records) {
        if (records == null || records.size() == 0)
            return new ResultImpl<>(new DefaultConfiguration(), uk.getFields());
        else
            return extractDSLContext(records).selectFrom(parents(records)).fetch();
    }

    @Override
    public final Result<CHILD> fetchChildren(PARENT record) {
        return fetchChildren(list(record));
    }

    @Override
    @SafeVarargs
    public final Result<CHILD> fetchChildren(PARENT... records) {
        return fetchChildren(list(records));
    }

    @Override
    public final Result<CHILD> fetchChildren(Collection<? extends PARENT> records) {
        if (records == null || records.size() == 0)
            return new ResultImpl<>(new DefaultConfiguration(), getFields());
        else
            return extractDSLContext(records).selectFrom(children(records)).fetch();
    }

    @Override
    public final Table<PARENT> parent(CHILD record) {
        return parents(list(record));
    }

    @SafeVarargs
    @Override
    public final Table<PARENT> parents(CHILD... records) {
        return parents(list(records));
    }

    @Override
    public final Table<PARENT> parents(Collection<? extends CHILD> records) {
        return table(records, uk.getTable(), uk.getFieldsArray(), getFieldsArray());
    }

    @Override
    public final Table<CHILD> children(PARENT record) {
        return children(list(record));
    }

    @SafeVarargs
    @Override
    public final Table<CHILD> children(PARENT... records) {
        return children(list(records));
    }

    @Override
    public final Table<CHILD> children(Collection<? extends PARENT> records) {
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
                : row(f1).in(extractRows(records, f2)),
            false
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
