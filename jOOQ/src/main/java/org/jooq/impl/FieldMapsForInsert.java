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

import static java.lang.Boolean.TRUE;
import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;
import static java.util.Collections.nCopies;
import static org.jooq.Clause.FIELD_ROW;
import static org.jooq.Clause.INSERT_SELECT;
import static org.jooq.Clause.INSERT_VALUES;
// ...
// ...
// ...
import static org.jooq.SQLDialect.DUCKDB;
// ...
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
import static org.jooq.SQLDialect.TRINO;
import static org.jooq.SQLDialect.YUGABYTEDB;
import static org.jooq.conf.WriteIfReadonly.IGNORE;
import static org.jooq.conf.WriteIfReadonly.THROW;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectFrom;
import static org.jooq.impl.Default.patchDefault;
import static org.jooq.impl.Default.patchDefaultForInsert;
import static org.jooq.impl.Keywords.K_DEFAULT_VALUES;
import static org.jooq.impl.Keywords.K_VALUES;
import static org.jooq.impl.Names.N_T;
import static org.jooq.impl.QueryPartCollectionView.wrap;
import static org.jooq.impl.Tools.EMPTY_FIELD;
import static org.jooq.impl.Tools.anyMatch;
import static org.jooq.impl.Tools.fieldNames;
import static org.jooq.impl.Tools.filter;
import static org.jooq.impl.Tools.flatten;
import static org.jooq.impl.Tools.flattenCollection;
import static org.jooq.impl.Tools.flattenFieldOrRows;
import static org.jooq.impl.Tools.lazy;
import static org.jooq.impl.Tools.row0;
import static org.jooq.impl.Tools.selectQueryImpl;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_STORE_ASSIGNMENT;
import static org.jooq.impl.UDTPathFieldImpl.construct;
import static org.jooq.impl.UDTPathFieldImpl.patchUDTConstructor;

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
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

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
import org.jooq.Row;
import org.jooq.SQLDialect;
import org.jooq.Select;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UDT;
import org.jooq.UDTPathField;
import org.jooq.UDTPathTableField;
import org.jooq.conf.WriteIfReadonly;
import org.jooq.exception.DataTypeException;
import org.jooq.impl.AbstractStoreQuery.UnknownField;
import org.jooq.impl.QOM.UNotYetImplemented;
import org.jooq.impl.QOM.UnmodifiableList;
import org.jooq.impl.Tools.BooleanDataKey;
import org.jooq.impl.Tools.ExtendedDataKey;
import org.jooq.tools.StringUtils;


/**
 * @author Lukas Eder
 */
final class FieldMapsForInsert extends AbstractQueryPart implements UNotYetImplemented {
    static final Set<SQLDialect>        CASTS_NEEDED           = SQLDialect.supportedBy(POSTGRES, TRINO, YUGABYTEDB);
    static final Set<SQLDialect>        CASTS_NEEDED_FOR_MERGE = SQLDialect.supportedBy(POSTGRES, YUGABYTEDB);
    static final Set<SQLDialect>        EMULATE_UDT_PATHS      = SQLDialect.supportedBy(DUCKDB);

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

    final void clear() {
        empty.clear();
        values.clear();
        rows = 0;
        nextRow = -1;
    }

    final void from(FieldMapsForInsert i) {
        empty.putAll(i.empty);
        for (Entry<Field<?>, List<Field<?>>> e : i.values.entrySet())
            values.put(e.getKey(), new ArrayList<>(e.getValue()));
        rows = i.rows;
        nextRow = i.nextRow;
    }

    static final record OrSelect(FieldMapsForInsert values, Select<?> select) {}

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




























                case TRINO:
                case MARIADB: {
                    if (supportsValues(ctx))
                        toSQLValues(ctx);
                    else
                        toSQLInsertSelect(ctx, insertSelect(ctx, GeneratorStatementType.INSERT));

                    break;
                }






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

    final FieldMapsForInsert.OrSelect emulateUDTPaths(Context<?> ctx, Select<?> s) {
        ThrowingPredicate<? super Field<?>, RuntimeException> isPath = f ->
            f instanceof UDTPathField
            && !(((UDTPathField<?, ?, ?>) f).getQualifier() instanceof Table)
            && table.indexOf(f) == -1;

        if (EMULATE_UDT_PATHS.contains(ctx.dialect()) && anyMatch(values.keySet(), isPath)) {
            FieldMapsForInsert result = new FieldMapsForInsert(table);
            result.nextRow = nextRow;
            result.rows = rows;
            BiFunction<UDT<?>, Field<?>, Field<?>> init = (u, f) -> DSL.inline(null, f.getDataType());
            Table<?> t = s != null ? s.asTable(N_T, fieldNames(values.size())) : null;

            int j = 0;
            for (Entry<Field<?>, List<Field<?>>> e : values.entrySet()) {
                int j0 = j;
                Field<?> key = e.getKey();
                List<Field<?>> value = e.getValue();

                if (isPath.test(key)) {
                    UDTPathField<?, ?, ?> u = (UDTPathField<?, ?, ?>) key;
                    UDTPathTableField<?, ?, ?> f = u.getTableField();

                    // [#9666] [#18777] TODO: Offer throwing a MetaDataUnavailableException.
                    if (f.getUDT() == null)
                        result.values.put(key, value);
                    else
                        result.values.compute(f, (k, v) -> {
                            List<Field<?>> v0 = v;

                            if (v0 == null) {
                                v0 = new ArrayList<>();

                                for (int i = 0; i < value.size(); i++)
                                    v0.add(construct(f.getUDT(), init));
                            }

                            for (int i = 0; i < value.size(); i++)
                                patchUDTConstructor(u,
                                    (UDTConstructor<?>) v0.get(i),
                                    t == null ? value.get(i) : t.field(j0),
                                    init
                                );

                            return v0;
                        });
                }
                else
                    result.values.put(key, value);

                j++;
            }

            return new OrSelect(result, t == null
                ? null
                : select(Tools.map(
                    result.values.entrySet(),
                    (e, i) -> e.getValue().get(0) instanceof UDTConstructor ? e.getValue().get(0) : t.field(i)))
                  .from(t)
            );
        }

        return null;
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
































            // See https://github.com/trinodb/trino/issues/10161
            case TRINO:
                for (List<Field<?>> row : values.values())
                    for (Field<?> value : row)
                        if (value instanceof ScalarSubquery)
                            return false;

                return true;

            // [#14742] MariaDB can't have (unaliased!) self-references of the INSERT
            //          target table in INSERT INTO t VALUES ((SELECT .. FROM t)),
            //          though other subqueries are possible
            // [#6583]  While MySQL also has this limitation, it is already covered
            //          for all DML statements, elsewhere
            case MARIADB:
                for (List<Field<?>> row : values.values())
                    for (Field<?> value : row)
                        if (value instanceof ScalarSubquery<?> s)
                            if (Tools.containsTable(s.query.$from(), table, false))
                                return false;

                return true;

            default:
                return true;
        }
    }

    final Select<Record> insertSelect(Context<?> ctx, GeneratorStatementType statementType) {
        Select<Record> select = null;

        Map<Field<?>, List<Field<?>>> v = valuesFlattened(ctx, statementType);
        boolean needsCast = CASTS_NEEDED_FOR_MERGE.contains(ctx.dialect())
            && ctx.data(ExtendedDataKey.DATA_INSERT_ON_DUPLICATE_KEY_UPDATE) != null;

        for (int i = 0; i < rows; i++) {
            int row = i;
            Select<Record> iteration = DSL.select(Tools.map(
                v.entrySet(), e -> patchDefault(castNullsIfNeeded(ctx, needsCast, e.getValue().get(row)), e.getKey())
            ));

            if (select == null)
                select = iteration;
            else
                select = select.unionAll(iteration);
        }

        return select;
    }

    /**
     * [#15412] The <code>SELECT</code> representation of the
     * <code>VALUES</code> clause may need some extra casts in some RDBMS, when
     * <code>INSERT … ON DUPLICATE KEY UPDATE</code> is emulated using
     * <code>MERGE</code>.
     */
    final Field<?> castNullsIfNeeded(Context<?> ctx, boolean needsCast, Field<?> f) {
        if (needsCast && f instanceof Val<?> val) {
            if (val.isInline(ctx) && val.getValue() == null)
                return f.cast(f.getDataType());
        }

        return f;
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
            for (Entry<Field<?>, List<Field<?>>> e : valuesFlattened(ctx, GeneratorStatementType.INSERT).entrySet()) {
                List<Field<?>> list = e.getValue();
                ctx.sql(separator);

                if (indent)
                    ctx.formatNewLine();














                ctx.visit(patchDefaultForInsert(ctx, list.get(row), e.getKey()));
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
            Field<?> e = empty.computeIfAbsent(f, LazyVal::new);

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

    final void set(
        Collection<? extends Field<?>> newColumns,
        Collection<? extends Row> newValues
    ) {
        if (newColumns != null) {
            Map<Field<?>, List<Field<?>>> v = new LinkedHashMap<>();

            for (Field<?> c : newColumns)
                if (values.get(c) == null)
                    v.put(c, new ArrayList<>(nCopies(rows, inline(null, c))));
                else
                    v.put(c, values.get(c));

            values.clear();
            values.putAll(v);
        }

        if (newValues != null) {
            rows = newValues.size();
            Iterator<Entry<Field<?>, List<Field<?>>>> it = values.entrySet().iterator();
            int index = 0;

            while (it.hasNext()) {
                int c = index;
                Entry<Field<?>, List<Field<?>>> e = it.next();
                Field<?> n = inline(null, e.getKey());
                e.getValue().clear();
                e.getValue().addAll(Tools.map(newValues, v -> (Field<?>) StringUtils.defaultIfNull(v.field(c), n)));
                index++;
            }
        }
    }

    final UnmodifiableList<? extends Field<?>> $columns() {
        return QOM.unmodifiable(new ArrayList<>(values.keySet()));
    }

    final UnmodifiableList<? extends Row> $values() {
        return QOM.unmodifiable(rows());
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

    final List<Row> rows() {
        List<Map<Field<?>, Field<?>>> maps = maps();

        return new AbstractList<Row>() {
            @Override
            public Row get(int index) {
                return row0(maps.get(index).values().toArray(EMPTY_FIELD));
            }

            @Override
            public int size() {
                return rows;
            }
        };
    }

    final List<Map<Field<?>, Field<?>>> maps() {
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
            ctx.data(DATA_STORE_ASSIGNMENT, true, c -> c.sql(" (").visit(wrap(fields).qualify(false)).sql(')'));

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
