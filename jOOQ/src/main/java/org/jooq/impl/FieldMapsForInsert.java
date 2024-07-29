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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
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

import static java.lang.Boolean.TRUE;
import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;
import static org.jooq.Clause.FIELD_ROW;
import static org.jooq.Clause.INSERT_SELECT;
import static org.jooq.Clause.INSERT_VALUES;
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
import static org.jooq.SQLDialect.YUGABYTEDB;
import static org.jooq.conf.WriteIfReadonly.IGNORE;
import static org.jooq.conf.WriteIfReadonly.THROW;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.FieldMapsForInsert.toSQLInsertSelect;
import static org.jooq.impl.DSL.selectFrom;
import static org.jooq.impl.Keywords.K_DEFAULT_VALUES;
import static org.jooq.impl.Keywords.K_VALUES;
import static org.jooq.impl.Names.N_T;
import static org.jooq.impl.QueryPartCollectionView.wrap;
import static org.jooq.impl.Tools.anyMatch;
import static org.jooq.impl.Tools.filter;
import static org.jooq.impl.Tools.flatten;
import static org.jooq.impl.Tools.flattenCollection;
import static org.jooq.impl.Tools.flattenFieldOrRows;
import static org.jooq.impl.Tools.lazy;
import static org.jooq.impl.Tools.row0;
import static org.jooq.impl.Tools.selectQueryImpl;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_STORE_ASSIGNMENT;

import java.util.AbstractList;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;

import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.FieldOrRow;
import org.jooq.GeneratorStatementType;
import org.jooq.Param;
// ...
import org.jooq.Record;
import org.jooq.RenderContext.CastMode;
import org.jooq.SQLDialect;
import org.jooq.Select;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.conf.WriteIfReadonly;
import org.jooq.exception.DataTypeException;
import org.jooq.impl.AbstractStoreQuery.UnknownField;
import org.jooq.impl.QOM.UNotYetImplemented;
import org.jooq.impl.Tools.BooleanDataKey;

/**
 * @author Lukas Eder
 */
final class FieldMapsForInsert extends AbstractQueryPart implements UNotYetImplemented {
    static final Set<SQLDialect>        CASTS_NEEDED = SQLDialect.supportedBy(POSTGRES, YUGABYTEDB);

    final Table<?>                      table;
    final Map<Field<?>, Field<?>>       empty;
    // Depending on whether embeddable types are allowed, this data structure
    // needs to be flattened with duplicates removed, prior to consumption
    // [#2530] [#6124] [#10481] TODO: Refactor and optimise these flattening algorithms
    final Map<Field<?>, List<Field<?>>> values;
    int                                 rows;
    int                                 nextRow      = -1;

    FieldMapsForInsert(Table<?> table) {
        this.table = table;
        this.values = new LinkedHashMap<>();
        this.empty = new LinkedHashMap<>();
    }

    // -------------------------------------------------------------------------
    // The QueryPart API
    // -------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        if (!isExecutable()) {
            ctx.formatSeparator()
               .start(INSERT_VALUES)
               .visit(K_DEFAULT_VALUES)
               .end(INSERT_VALUES);
        }





















        // Single record inserts can use the standard syntax in any dialect
        else if (rows == 1 && supportsValues(ctx)) {
            toSQLValues(ctx);
        }

        // True SQL92 multi-record inserts aren't always supported
        else {
            switch (ctx.family()) {

                // Some dialects don't support multi-record inserts


































                case FIREBIRD: {
                    toSQLInsertSelect(ctx, insertSelect(ctx, GeneratorStatementType.INSERT));
                    break;
                }

                default: {
                    toSQLValues(ctx);
                    break;
                }
            }
        }
    }

    private final void toSQLValues(Context<?> ctx) {
        ctx.formatSeparator()
           .start(INSERT_VALUES)
           .visit(K_VALUES)
           .sql(' ');
        toSQL92Values(ctx);
        ctx.end(INSERT_VALUES);
    }

    static final void toSQLInsertSelect(Context<?> ctx, Select<?> select) {
        ctx.formatSeparator()
           .start(INSERT_SELECT)
           .visit(patchSelectWithUnions(ctx, select))
           .end(INSERT_SELECT);
    }

    private static final Select<?> patchSelectWithUnions(Context<?> ctx, Select<?> select) {
        switch (ctx.family()) {










            default:
                return select;
        }
    }


























































































    private final boolean supportsValues(Context<?> ctx) {
        switch (ctx.family()) {























            default:
                return true;
        }
    }

    final Select<Record> insertSelect(Context<?> ctx, GeneratorStatementType statementType) {
        Select<Record> select = null;

        Map<Field<?>, List<Field<?>>> v = valuesFlattened(ctx, statementType);
        for (int i = 0; i < rows; i++) {
            int row = i;
            Select<Record> iteration = DSL.select(Tools.map(v.values(), l -> l.get(row)));

            if (select == null)
                select = iteration;
            else
                select = select.unionAll(iteration);
        }

        return select;
    }

    final void toSQL92Values(Context<?> ctx) {
        boolean indent = (values.size() > 1);




        // [#2823] [#10033] Few dialects need bind value casts for INSERT .. VALUES(?, ?)
        //                  Some regressions have been observed e.g. in PostgreSQL with JSON types, so let's be careful.
        CastMode previous = ctx.castMode();
        if (!CASTS_NEEDED.contains(ctx.dialect()))
            ctx.castMode(CastMode.NEVER);

        for (int row = 0; row < rows; row++) {
            if (row > 0)
                ctx.sql(", ");

            ctx.start(FIELD_ROW)
               .sql('(');

            if (indent)
                ctx.formatIndentStart();

            String separator = "";
            for (List<Field<?>> list : valuesFlattened(ctx, GeneratorStatementType.INSERT).values()) {
                ctx.sql(separator);

                if (indent)
                    ctx.formatNewLine();














                ctx.visit(list.get(row));
                separator = ", ";
            }

            if (indent)
                ctx.formatIndentEnd()
                   .formatNewLine();

            ctx.sql(')')
               .end(FIELD_ROW);
        }

        if (!CASTS_NEEDED.contains(ctx.dialect()))
            ctx.castMode(previous);
    }














































    // -------------------------------------------------------------------------
    // The FieldMapsForInsert API
    // -------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    final void addFields(Collection<?> fields) {
        if (rows == 0)
            newRecord();

        initNextRow();
        for (Object field : fields) {
            Field<?> f = Tools.tableField(table, field);
            Field<?> e = empty.get(f);

            if (e == null) {
                e = new LazyVal<>((Field<Object>) f);
                empty.put(f, e);
            }

            if (!values.containsKey(f)) {
                values.put(f, rows > 0
                    ? new ArrayList<>(Collections.nCopies(rows, e))
                    : new ArrayList<>()
                );
            }
        }
    }

    final void set(Collection<? extends Field<?>> fields) {
        initNextRow();

        Iterator<? extends Field<?>> it1 = fields.iterator();
        Iterator<List<Field<?>>> it2 = values.values().iterator();

        while (it1.hasNext() && it2.hasNext())
            it2.next().set(rows - 1, it1.next());

        if (it1.hasNext() || it2.hasNext())
            throw new IllegalArgumentException("Added record size (" + fields.size() + ") must match fields size (" + values.size() + ")");
    }

    @SuppressWarnings("unchecked")
    final <T> Field<T> set(Field<T> field, Field<T> value) {
        addFields(Collections.singletonList(field));
        return (Field<T>) values.get(field).set(rows - 1, value);
    }

    final void set(Map<?, ?> map) {
        addFields(map.keySet());

        map.forEach((k, v) -> {
            Field<?> field = Tools.tableField(table, k);
            values.get(field).set(rows - 1, Tools.field(v, field));
        });
    }

    private final void initNextRow() {
        if (rows == nextRow) {
            Iterator<List<Field<?>>> v = values.values().iterator();
            Iterator<Field<?>> e = empty.values().iterator();

            while (v.hasNext() && e.hasNext())
                v.next().add(e.next());

            rows++;
        }
    }

    final void newRecord() {
        if (nextRow < rows)
            nextRow++;
    }

    final List<Map<Field<?>, Field<?>>> maps() {
        initNextRow();

        return new AbstractList<Map<Field<?>, Field<?>>>() {
            @Override
            public Map<Field<?>, Field<?>> get(int index) {
                return map(index);
            }

            @Override
            public int size() {
                return rows;
            }
        };
    }

    final Map<Field<?>, Field<?>> map(final int index) {
        initNextRow();

        return new AbstractMap<Field<?>, Field<?>>() {
            transient Set<Entry<Field<?>, Field<?>>> entrySet;

            @Override
            public Set<Entry<Field<?>, Field<?>>> entrySet() {
                if (entrySet == null)
                    entrySet = new EntrySet();

                return entrySet;
            }

            @Override
            public boolean containsKey(Object key) {
                return values.containsKey(key);
            }

            @Override
            public boolean containsValue(Object value) {
                return anyMatch(values.values(), list -> list.get(index).equals(value));
            }

            @Override
            public Field<?> get(Object key) {
                List<Field<?>> list = values.get(key);
                return list == null ? null : list.get(index);
            }

            @SuppressWarnings({ "unchecked", "rawtypes" })
            @Override
            public Field<?> put(Field<?> key, Field<?> value) {
                return FieldMapsForInsert.this.set((Field) key, (Field) value);
            }

            @Override
            public Field<?> remove(Object key) {
                List<Field<?>> list = values.get(key);
                values.remove(key);
                return list == null ? null : list.get(index);
            }

            @Override
            public Set<Field<?>> keySet() {
                return values.keySet();
            }

            final class EntrySet extends AbstractSet<Entry<Field<?>, Field<?>>> {
                @Override
                public final int size() {
                    return values.size();
                }

                @Override
                public final void clear() {
                    values.clear();
                }

                @Override
                public final Iterator<Entry<Field<?>, Field<?>>> iterator() {
                    return new Iterator<Entry<Field<?>, Field<?>>>() {
                        final Iterator<Entry<Field<?>, List<Field<?>>>> delegate = values.entrySet().iterator();

                        @Override
                        public boolean hasNext() {
                            return delegate.hasNext();
                        }

                        @Override
                        public Entry<Field<?>, Field<?>> next() {
                            Entry<Field<?>, List<Field<?>>> entry = delegate.next();
                            return new SimpleImmutableEntry<>(entry.getKey(), entry.getValue().get(index));
                        }

                        @Override
                        public void remove() {
                            delegate.remove();
                        }
                    };
                }
            }
        };
    }

    final Map<Field<?>, Field<?>> lastMap() {
        return map(rows - 1);
    }

    final boolean isExecutable() {
        return rows > 0;
    }

    final Set<Field<?>> toSQLReferenceKeys(Context<?> ctx) {

        // [#1506] with DEFAULT VALUES, we might not have any columns to render
        if (!isExecutable())
            return emptySet();

        // [#2995] Do not generate empty column lists.
        if (values.isEmpty())
            return emptySet();

        // [#4629] Do not generate column lists for unknown columns
        unknownFields: {
            for (Field<?> field : values.keySet())
                if (!(field instanceof UnknownField))
                    break unknownFields;

            return emptySet();
        }

        // [#989] Avoid qualifying fields in INSERT field declaration
        Set<Field<?>> fields;






        fields = keysFlattened(ctx, GeneratorStatementType.INSERT);

        if (!fields.isEmpty())
            ctx.sql(" (").visit(wrap(fields).qualify(false)).sql(')');

        return fields;
    }

    private static final <E> Iterable<E> removeReadonly(Context<?> ctx, Iterable<E> it, Function<? super E, ? extends Field<?>> f) {





        return it;
    }

    final Set<Field<?>> keysFlattened(Context<?> ctx, GeneratorStatementType statementType) {

        // [#9864] TODO: Refactor and optimise these flattening algorithms
        return valuesFlattened(ctx, statementType).keySet();
    }

    final Map<Field<?>, List<Field<?>>> valuesFlattened(Context<?> ctx, GeneratorStatementType statementType) {
        Map<Field<?>, List<Field<?>>> result = new LinkedHashMap<>();

        // [#2530] [#6124] [#10481] TODO: Shortcut for performance, when there are no embeddables
        Set<Field<?>> overlapping = null;
        for (Entry<Field<?>, List<Field<?>>> entry : removeReadonly(ctx, values.entrySet(), Entry::getKey)) {
            Field<?> key = entry.getKey();
            DataType<?> keyType = key.getDataType();
            List<Field<?>> value = entry.getValue();

            // [#2530] [#6124] [#10481] TODO: Refactor and optimise these flattening algorithms
            if (keyType.isEmbeddable()) {
                List<Iterator<? extends Field<?>>> valueFlattened = new ArrayList<>(value.size());

                for (Field<?> v : value)
                    valueFlattened.add(flatten(v).iterator());

                for (Field<?> k : flatten(key)) {

















                    {
                        List<Field<?>> list = new ArrayList<>(value.size());

                        for (Iterator<? extends Field<?>> v : valueFlattened)
                            list.add(v.hasNext() ? v.next() : null);

                        result.put(k, list);
                    }
                }
            }







            else
                result.put(key, value);
        }

        return result;
    }
}
