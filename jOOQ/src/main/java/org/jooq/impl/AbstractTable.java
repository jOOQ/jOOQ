/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq.impl;

import static org.jooq.Clause.TABLE;
import static org.jooq.JoinType.CROSS_APPLY;
import static org.jooq.JoinType.CROSS_JOIN;
import static org.jooq.JoinType.FULL_OUTER_JOIN;
import static org.jooq.JoinType.JOIN;
import static org.jooq.JoinType.LEFT_OUTER_JOIN;
import static org.jooq.JoinType.NATURAL_JOIN;
import static org.jooq.JoinType.NATURAL_LEFT_OUTER_JOIN;
import static org.jooq.JoinType.NATURAL_RIGHT_OUTER_JOIN;
import static org.jooq.JoinType.OUTER_APPLY;
import static org.jooq.JoinType.RIGHT_OUTER_JOIN;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.val;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.jooq.Binding;
import org.jooq.Clause;
import org.jooq.Context;
import org.jooq.Converter;
import org.jooq.DataType;
import org.jooq.DivideByOnStep;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.JoinType;
import org.jooq.PivotForStep;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.RecordType;
import org.jooq.Row;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableLike;
import org.jooq.TableOnStep;
import org.jooq.TableOptionalOnStep;
import org.jooq.TablePartitionByStep;
import org.jooq.UniqueKey;
// ...
// ...
import org.jooq.tools.StringUtils;

/**
 * @author Lukas Eder
 */
abstract class AbstractTable<R extends Record> extends AbstractQueryPart implements Table<R> {

    /**
     * Generated UID
     */
    private static final long     serialVersionUID = 3155496238969274871L;
    private static final Clause[] CLAUSES          = { TABLE };

    private final Schema          schema;
    private final String          name;
    private final String          comment;

    AbstractTable(String name) {
        this(name, null, null);
    }

    AbstractTable(String name, Schema schema) {
        this(name, schema, null);
    }

    AbstractTable(String name, Schema schema, String comment) {
        super();

        this.schema = schema;
        this.name = name;
        this.comment = comment;
    }

    // ------------------------------------------------------------------------
    // XXX: QueryPart API
    // ------------------------------------------------------------------------

    @Override
    public Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }

    // ------------------------------------------------------------------------
    // XXX: TableLike API
    // ------------------------------------------------------------------------

    /**
     * Subclasses should override this method to provide the set of fields
     * contained in the concrete table implementation. For example, a
     * <code>TableAlias</code> contains aliased fields of its
     * <code>AliasProvider</code> table.
     */
    abstract Fields<R> fields0();

    @Override
    public final RecordType<R> recordType() {
        return fields0();
    }

    @Override
    public final R newRecord() {
        return DSL.using(new DefaultConfiguration()).newRecord(this);
    }

    @SuppressWarnings({ "rawtypes" })
    @Override
    public final Row fieldsRow() {
        return new RowImpl(fields0());
    }

    @Override
    public final <T> Field<T> field(Field<T> field) {
        return fieldsRow().field(field);
    }

    @Override
    public final Field<?> field(String string) {
        return fieldsRow().field(string);
    }

    @Override
    public final Field<?> field(int index) {
        return fieldsRow().field(index);
    }

    @Override
    public final Field<?>[] fields() {
        return fieldsRow().fields();
    }

    @Override
    public final Table<R> asTable() {
        return this;
    }

    @Override
    public final Table<R> asTable(String alias) {
        return as(alias);
    }

    @Override
    public final Table<R> asTable(String alias, String... fieldAliases) {
        return as(alias, fieldAliases);
    }

    // ------------------------------------------------------------------------
    // XXX: Table API
    // ------------------------------------------------------------------------

    @Override
    public final Schema getSchema() {
        return schema;
    }

    @Override
    public final String getName() {
        return name;
    }

    @Override
    public final String getComment() {
        return comment;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Subclasses should override this method
     */
    @Override
    public Identity<R, ? extends Number> getIdentity() {
        return null;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Subclasses may override this method
     */
    @Override
    public UniqueKey<R> getPrimaryKey() {
        return null;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Subclasses may override this method
     */
    @Override
    public TableField<R, ? extends Number> getRecordVersion() {
        return null;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Subclasses may override this method
     */
    @Override
    public TableField<R, ? extends java.util.Date> getRecordTimestamp() {
        return null;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Subclasses should override this method
     */
    @Override
    public List<UniqueKey<R>> getKeys() {
        return Collections.emptyList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <O extends Record> List<ForeignKey<O, R>> getReferencesFrom(Table<O> other) {
        return other.getReferencesTo(this);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Subclasses should override this method
     */
    @Override
    public List<ForeignKey<R, ?>> getReferences() {
        return Collections.emptyList();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public final <O extends Record> List<ForeignKey<R, O>> getReferencesTo(Table<O> other) {
        List<ForeignKey<R, O>> result = new ArrayList<ForeignKey<R, O>>();

        for (ForeignKey<R, ?> reference : getReferences()) {
            if (other.equals(reference.getKey().getTable())) {
                result.add((ForeignKey<R, O>) reference);
            }

            // TODO: Refactor the following two blocks and make things more OO
            // [#1460] In case the other table was aliased using
            else if (other instanceof TableImpl) {
                Table<O> aliased = ((TableImpl<O>) other).getAliasedTable();

                if (aliased != null && aliased.equals(reference.getKey().getTable())) {
                    result.add((ForeignKey<R, O>) reference);
                }
            }

            // [#1460] In case the other table was aliased using
            else if (other instanceof TableAlias) {
                Table<O> aliased = ((TableAlias<O>) other).getAliasedTable();

                if (aliased != null && aliased.equals(reference.getKey().getTable())) {
                    result.add((ForeignKey<R, O>) reference);
                }
            }
        }

        return Collections.unmodifiableList(result);
    }

    /**
     * Subclasses may call this method to create {@link TableField} objects that
     * are linked to this table.
     *
     * @param name The name of the field (case-sensitive!)
     * @param type The data type of the field
     */
    protected static final <R extends Record, T> TableField<R, T> createField(String name, DataType<T> type, Table<R> table) {
        return createField(name, type, table, null, (Binding<T, T>) null);
    }

    /**
     * Subclasses may call this method to create {@link TableField} objects that
     * are linked to this table.
     *
     * @param name The name of the field (case-sensitive!)
     * @param type The data type of the field
     */
    protected static final <R extends Record, T> TableField<R, T> createField(String name, DataType<T> type, Table<R> table, String comment) {
        return createField(name, type, table, comment, (Binding<T, T>) null);
    }

    /**
     * Subclasses may call this method to create {@link TableField} objects that
     * are linked to this table.
     *
     * @param name The name of the field (case-sensitive!)
     * @param type The data type of the field
     */
    protected static final <R extends Record, T, U> TableField<R, U> createField(String name, DataType<T> type, Table<R> table, String comment, Converter<T, U> converter) {
        return createField(name, type, table, comment, converter == null ? null : new DefaultBinding<T, U>(converter, type.isLob()));
    }

    /**
     * Subclasses may call this method to create {@link TableField} objects that
     * are linked to this table.
     *
     * @param name The name of the field (case-sensitive!)
     * @param type The data type of the field
     */
    @SuppressWarnings("unchecked")
    protected static final <R extends Record, T, U> TableField<R, U> createField(String name, DataType<T> type, Table<R> table, String comment, Binding<T, U> binding) {
        final DataType<U> actualType = binding == null
            ? (DataType<U>) type
            : type.asConvertedDataType(binding);

        final TableFieldImpl<R, U> tableField = new TableFieldImpl<R, U>(name, actualType, table, comment, binding);

        // [#1199] The public API of Table returns immutable field lists
        if (table instanceof TableImpl) {
            ((TableImpl<?>) table).fields0().add(tableField);
        }

        return tableField;
    }

    // ------------------------------------------------------------------------
    // XXX: Other API
    // ------------------------------------------------------------------------

    @Override
    public final Table<R> useIndex(String... indexes) {
        return new HintedTable<R>(this, "use index", indexes);
    }

    @Override
    public final Table<R> useIndexForJoin(String... indexes) {
        return new HintedTable<R>(this, "use index for join", indexes);
    }

    @Override
    public final Table<R> useIndexForOrderBy(String... indexes) {
        return new HintedTable<R>(this, "use index for order by", indexes);
    }

    @Override
    public final Table<R> useIndexForGroupBy(String... indexes) {
        return new HintedTable<R>(this, "use index for group by", indexes);
    }

    @Override
    public final Table<R> ignoreIndex(String... indexes) {
        return new HintedTable<R>(this, "ignore index", indexes);
    }

    @Override
    public final Table<R> ignoreIndexForJoin(String... indexes) {
        return new HintedTable<R>(this, "ignore index for join", indexes);
    }

    @Override
    public final Table<R> ignoreIndexForOrderBy(String... indexes) {
        return new HintedTable<R>(this, "ignore index for order by", indexes);
    }

    @Override
    public final Table<R> ignoreIndexForGroupBy(String... indexes) {
        return new HintedTable<R>(this, "ignore index for group by", indexes);
    }

    @Override
    public final Table<R> forceIndex(String... indexes) {
        return new HintedTable<R>(this, "force index", indexes);
    }

    @Override
    public final Table<R> forceIndexForJoin(String... indexes) {
        return new HintedTable<R>(this, "force index for join", indexes);
    }

    @Override
    public final Table<R> forceIndexForOrderBy(String... indexes) {
        return new HintedTable<R>(this, "force index for order by", indexes);
    }

    @Override
    public final Table<R> forceIndexForGroupBy(String... indexes) {
        return new HintedTable<R>(this, "force index for group by", indexes);
    }

    /* [pro] xx
    xxxxxxxxx
    xxxxxx xxxxx xxxxxxxx xxxxxxxxxxx xxxxx x
        xxxxxx xxx xxxxxxxxxxxxxxxxxx xxxxxx
    x

    xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    xx xxxx xxxxx xxx
    xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxxxxxxx xxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxx x
        xxxxxx xxx xxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxxxxxxx xxxxxxxxxxxxxxxxxx xxxxxxx xxxxxxxxx xxxxxxxxxxxxxxxxxxx x
        xxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxx
    x

    xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    xx xxxx xxxxxxxxx xxxxx xxx
    xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxx xxxx x
        xxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxx xxxxxxx xxxx x
        xxxxxx xxx xxxxxxxxxxxxxxxxx xxxxxxxxxxxxx xxxxx xxxx xxxxxxxxxxxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxx x
        xxxxxx xxx xxxxxxxxxxxxxxxxx xxxxxxxxxxxxx xxxxx xxxxx xxxxxxxxxxxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxx x
        xxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxx x
        xxxxxx xxx xxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxx xxxxx xxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx x
        xxxxxx xxx xxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxx xxxxx xxxxx xxxxxxxxxxxxxxxxxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxxx xxxxxxxxxxxxxx xxxx x
        xxxxxx xxxxxxxxxxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxxx xxxxxxxxxxxxxxx xxxxxxx xxxxxxx xxxx x
        xxxxxx xxx xxxxxxxxxxxxxxxxx xxxxxxxxxxxxx xxxx xxxxx xxxxxxxxxxxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxxx xxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxx x
        xxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxx x
        xxxxxx xxx xxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxx xxxxxxxxxx xxxxx xxxxxxxxxxxxxxxxxxxxxxxxx
    x

    xx [/pro] */
    // ------------------------------------------------------------------------
    // XXX: DIVISION API
    // ------------------------------------------------------------------------

    @Override
    public final DivideByOnStep divideBy(Table<?> divisor) {
        return new DivideBy(this, divisor);
    }

    // ------------------------------------------------------------------------
    // XXX: JOIN API
    // ------------------------------------------------------------------------

    @Override
    public final TableOptionalOnStep join(TableLike<?> table, JoinType type) {
        return new JoinTable(this, table, type);
    }

    @Override
    public final TableOnStep join(TableLike<?> table) {
        return join(table, JOIN);
    }

    @Override
    public final TableOnStep join(String sql) {
        return join(table(sql));
    }

    @Override
    public final TableOnStep join(String sql, Object... bindings) {
        return join(table(sql, bindings));
    }

    @Override
    public final TableOnStep join(String sql, QueryPart... parts) {
        return join(table(sql, parts));
    }

    @Override
    public final TablePartitionByStep leftOuterJoin(TableLike<?> table) {
        return join(table, LEFT_OUTER_JOIN);
    }

    @Override
    public final TablePartitionByStep leftOuterJoin(String sql) {
        return leftOuterJoin(table(sql));
    }

    @Override
    public final TablePartitionByStep leftOuterJoin(String sql, Object... bindings) {
        return leftOuterJoin(table(sql, bindings));
    }

    @Override
    public final TablePartitionByStep leftOuterJoin(String sql, QueryPart... parts) {
        return leftOuterJoin(table(sql, parts));
    }

    @Override
    public final TablePartitionByStep rightOuterJoin(TableLike<?> table) {
        return join(table, RIGHT_OUTER_JOIN);
    }

    @Override
    public final TablePartitionByStep rightOuterJoin(String sql) {
        return rightOuterJoin(table(sql));
    }

    @Override
    public final TablePartitionByStep rightOuterJoin(String sql, Object... bindings) {
        return rightOuterJoin(table(sql, bindings));
    }

    @Override
    public final TablePartitionByStep rightOuterJoin(String sql, QueryPart... parts) {
        return rightOuterJoin(table(sql, parts));
    }

    @Override
    public final TableOnStep fullOuterJoin(TableLike<?> table) {
        return join(table, FULL_OUTER_JOIN);
    }

    @Override
    public final TableOnStep fullOuterJoin(String sql) {
        return fullOuterJoin(table(sql));
    }

    @Override
    public final TableOnStep fullOuterJoin(String sql, Object... bindings) {
        return fullOuterJoin(table(sql, bindings));
    }

    @Override
    public final TableOnStep fullOuterJoin(String sql, QueryPart... parts) {
        return fullOuterJoin(table(sql, parts));
    }

    @Override
    public final Table<Record> crossJoin(TableLike<?> table) {
        return join(table, CROSS_JOIN);
    }

    @Override
    public final Table<Record> crossJoin(String sql) {
        return crossJoin(table(sql));
    }

    @Override
    public final Table<Record> crossJoin(String sql, Object... bindings) {
        return crossJoin(table(sql, bindings));
    }

    @Override
    public final Table<Record> crossJoin(String sql, QueryPart... parts) {
        return crossJoin(table(sql, parts));
    }

    @Override
    public final Table<Record> naturalJoin(TableLike<?> table) {
        return join(table, NATURAL_JOIN);
    }

    @Override
    public final Table<Record> naturalJoin(String sql) {
        return naturalJoin(table(sql));
    }

    @Override
    public final Table<Record> naturalJoin(String sql, Object... bindings) {
        return naturalJoin(table(sql, bindings));
    }

    @Override
    public final Table<Record> naturalJoin(String sql, QueryPart... parts) {
        return naturalJoin(table(sql, parts));
    }

    @Override
    public final Table<Record> naturalLeftOuterJoin(TableLike<?> table) {
        return join(table, NATURAL_LEFT_OUTER_JOIN);
    }

    @Override
    public final Table<Record> naturalLeftOuterJoin(String sql) {
        return naturalLeftOuterJoin(table(sql));
    }

    @Override
    public final Table<Record> naturalLeftOuterJoin(String sql, Object... bindings) {
        return naturalLeftOuterJoin(table(sql, bindings));
    }

    @Override
    public final Table<Record> naturalLeftOuterJoin(String sql, QueryPart... parts) {
        return naturalLeftOuterJoin(table(sql, parts));
    }

    @Override
    public final Table<Record> naturalRightOuterJoin(TableLike<?> table) {
        return join(table, NATURAL_RIGHT_OUTER_JOIN);
    }

    @Override
    public final Table<Record> naturalRightOuterJoin(String sql) {
        return naturalRightOuterJoin(table(sql));
    }

    @Override
    public final Table<Record> naturalRightOuterJoin(String sql, Object... bindings) {
        return naturalRightOuterJoin(table(sql, bindings));
    }

    @Override
    public final Table<Record> naturalRightOuterJoin(String sql, QueryPart... parts) {
        return naturalRightOuterJoin(table(sql, parts));
    }

    /* [pro] xx

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxx xxxxxx x
        xxxxxx xxxxxxxxxxx xxxxxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxxxxxxxx xxxxxxxxxxxxxxxxx xxxx x
        xxxxxx xxxxxxxxxxxxxxxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxxxxxxxx xxxxxxxxxxxxxxxxx xxxx xxxxxxxxx xxxxxxxxx x
        xxxxxx xxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxxxxxxxx xxxxxxxxxxxxxxxxx xxxx xxxxxxxxxxxx xxxxxx x
        xxxxxx xxxxxxxxxxxxxxxxxxxxx xxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxx xxxxxx x
        xxxxxx xxxxxxxxxxx xxxxxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxxxxxxxx xxxxxxxxxxxxxxxxx xxxx x
        xxxxxx xxxxxxxxxxxxxxxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxxxxxxxx xxxxxxxxxxxxxxxxx xxxx xxxxxxxxx xxxxxxxxx x
        xxxxxx xxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxxxxxxxx xxxxxxxxxxxxxxxxx xxxx xxxxxxxxxxxx xxxxxx x
        xxxxxx xxxxxxxxxxxxxxxxxxxxx xxxxxxxx
    x

    xx [/pro] */

    // ------------------------------------------------------------------------
    // XXX: Object API
    // ------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }

        // [#2144] Non-equality can be decided early, without executing the
        // rather expensive implementation of AbstractQueryPart.equals()
        if (that instanceof AbstractTable) {
            if (StringUtils.equals(name, (((AbstractTable<?>) that).name))) {
                return super.equals(that);
            }

            return false;
        }

        return false;
    }

    @Override
    public int hashCode() {

        // [#1938] This is a much more efficient hashCode() implementation
        // compared to that of standard QueryParts
        return name.hashCode();
    }
}
