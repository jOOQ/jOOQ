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

import static java.lang.Boolean.TRUE;
import static org.jooq.Clause.FIELD_ROW;
import static org.jooq.Clause.INSERT_SELECT;
import static org.jooq.Clause.INSERT_VALUES;
// ...
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.Keywords.K_DEFAULT_VALUES;
import static org.jooq.impl.Keywords.K_VALUES;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_EMULATE_BULK_INSERT_RETURNING;

import java.util.AbstractList;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Param;
import org.jooq.Record;
import org.jooq.Select;
import org.jooq.Table;
import org.jooq.impl.AbstractStoreQuery.UnknownField;

/**
 * @author Lukas Eder
 */
final class FieldMapsForInsert extends AbstractQueryPart {

    /**
     * Generated UID
     */
    private static final long           serialVersionUID = -6227074228534414225L;

    final Table<?>                      table;
    final Map<Field<?>, Field<?>>       empty;
    final Map<Field<?>, List<Field<?>>> values;
    int                                 rows;
    int                                 nextRow          = -1;

    FieldMapsForInsert(Table<?> table) {
        this.table = table;
        this.values = new LinkedHashMap<Field<?>, List<Field<?>>>();
        this.empty = new LinkedHashMap<Field<?>, Field<?>>();
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
        else if (rows == 1                                                ) {
            ctx.formatSeparator()
               .start(INSERT_VALUES)
               .visit(K_VALUES)
               .sql(' ');
            toSQL92Values(ctx);
            ctx.end(INSERT_VALUES);
        }

        // True SQL92 multi-record inserts aren't always supported
        else {
            switch (ctx.family()) {

                // Some dialects don't support multi-record inserts











































                case FIREBIRD: {
                    ctx.formatSeparator()
                       .start(INSERT_SELECT)
                       .visit(insertSelect())
                       .end(INSERT_SELECT);

                    break;
                }

                default: {
                    ctx.formatSeparator()
                       .start(INSERT_VALUES)
                       .visit(K_VALUES)
                       .sql(' ');
                    toSQL92Values(ctx);
                    ctx.end(INSERT_VALUES);

                    break;
                }
            }
        }
    }

















    final Select<Record> insertSelect() {
        Select<Record> select = null;

        for (int row = 0; row < rows; row++) {
            List<Field<?>> fields = new ArrayList<Field<?>>(values.size());

            for (List<Field<?>> list : values.values())
                fields.add(list.get(row));

            Select<Record> iteration = DSL.select(fields);

            if (select == null)
                select = iteration;
            else
                select = select.unionAll(iteration);
        }

        return select;
    }

    final void toSQL92Values(Context<?> ctx) {
        toSQL92Values(ctx, false);
    }

    final void toSQL92Values(Context<?> ctx, boolean emulateBulkInsertReturning) {
        boolean indent = (values.size() > 1);

        for (int row = 0; row < rows                                                                     ; row++) {
            if (row > 0)
                ctx.sql(", ");

            ctx.start(FIELD_ROW)
               .sql('(');

            if (indent)
                ctx.formatIndentStart();

            String separator = "";
            int i = 0;
            for (List<Field<?>> list : values.values()) {
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
                e = new LazyVal<Object>(null, (Field<Object>) f);
                empty.put(f, e);
            }

            if (!values.containsKey(f)) {
                values.put(f, rows > 0
                    ? new ArrayList<Field<?>>(Collections.nCopies(rows, e))
                    : new ArrayList<Field<?>>()
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
        for (Entry<?, ?> entry : map.entrySet()) {
            Field<?> field = Tools.tableField(table, entry.getKey());
            values.get(field)
                  .set(rows - 1, Tools.field(entry.getValue(), field));
        }
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

    final Collection<Field<?>> fields() {
        return values.keySet();
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
                for (List<Field<?>> list : values.values())
                    if (list.get(index).equals(value))
                        return true;

                return false;
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
                        Iterator<Entry<Field<?>, List<Field<?>>>> delegate = values.entrySet().iterator();

                        @Override
                        public boolean hasNext() {
                            return delegate.hasNext();
                        }

                        @Override
                        public Entry<Field<?>, Field<?>> next() {
                            Entry<Field<?>, List<Field<?>>> entry = delegate.next();
                            return new SimpleImmutableEntry<Field<?>, Field<?>>(entry.getKey(), entry.getValue().get(index));
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

    final void toSQLReferenceKeys(Context<?> ctx) {

        // [#1506] with DEFAULT VALUES, we might not have any columns to render
        if (!isExecutable())
            return;

        // [#2995] Do not generate empty column lists.
        if (values.size() == 0)
            return;

        // [#4629] Do not generate column lists for unknown columns
        unknownFields: {
            for (Field<?> field : values.keySet())
                if (!(field instanceof UnknownField))
                    break unknownFields;

            return;
        }

        boolean indent = (values.size() > 1);

        ctx.sql(" (");

        if (indent)
            ctx.formatIndentStart();

        // [#989] Avoid qualifying fields in INSERT field declaration
        boolean qualify = ctx.qualify();
        ctx.qualify(false);

        String separator = "";
        for (Field<?> field : values.keySet()) {
            ctx.sql(separator);

            if (indent)
                ctx.formatNewLine();

            ctx.visit(field);
            separator = ", ";
        }

        ctx.qualify(qualify);

        if (indent)
            ctx.formatIndentEnd()
               .formatNewLine();

        ctx.sql(')');
    }
}
