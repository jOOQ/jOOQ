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

import static org.jooq.Clause.TABLE;
import static org.jooq.JoinType.CROSS_APPLY;
import static org.jooq.JoinType.CROSS_JOIN;
import static org.jooq.JoinType.FULL_OUTER_JOIN;
import static org.jooq.JoinType.JOIN;
import static org.jooq.JoinType.LEFT_ANTI_JOIN;
import static org.jooq.JoinType.LEFT_OUTER_JOIN;
import static org.jooq.JoinType.LEFT_SEMI_JOIN;
import static org.jooq.JoinType.NATURAL_FULL_OUTER_JOIN;
import static org.jooq.JoinType.NATURAL_JOIN;
import static org.jooq.JoinType.NATURAL_LEFT_OUTER_JOIN;
import static org.jooq.JoinType.NATURAL_RIGHT_OUTER_JOIN;
import static org.jooq.JoinType.OUTER_APPLY;
import static org.jooq.JoinType.RIGHT_OUTER_JOIN;
import static org.jooq.JoinType.STRAIGHT_JOIN;
import static org.jooq.impl.DSL.condition;
import static org.jooq.impl.DSL.exists;
// ...
import static org.jooq.impl.DSL.notExists;
import static org.jooq.impl.DSL.selectFrom;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.val;
import static org.jooq.impl.Tools.EMPTY_FIELD;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import org.jooq.Binding;
import org.jooq.Catalog;
import org.jooq.Check;
import org.jooq.Clause;
import org.jooq.Comment;
import org.jooq.Comparator;
import org.jooq.Condition;
import org.jooq.Context;
import org.jooq.Converter;
import org.jooq.DataType;
import org.jooq.DivideByOnStep;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.JoinType;
// ...
import org.jooq.Name;
// ...
// ...
import org.jooq.QualifiedAsterisk;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.RecordType;
import org.jooq.Row;
import org.jooq.RowId;
import org.jooq.SQL;
import org.jooq.Schema;
import org.jooq.Select;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableLike;
import org.jooq.TableOnStep;
import org.jooq.TableOptionalOnStep;
import org.jooq.TableOptions;
import org.jooq.TableOptions.TableType;
import org.jooq.TableOuterJoinStep;
import org.jooq.TablePartitionByStep;
import org.jooq.UniqueKey;
// ...
// ...

/**
 * @author Lukas Eder
 */
abstract class AbstractTable<R extends Record> extends AbstractNamed implements Table<R> {

    /**
     * Generated UID
     */
    private static final long     serialVersionUID = 3155496238969274871L;
    private static final Clause[] CLAUSES          = { TABLE };

    private final TableOptions    options;
    private Schema                tableschema;
    private transient DataType<R> tabletype;

    AbstractTable(TableOptions options, Name name) {
        this(options, name, null, null);
    }

    AbstractTable(TableOptions options, Name name, Schema schema) {
        this(options, name, schema, null);
    }

    AbstractTable(TableOptions options, Name name, Schema schema, Comment comment) {
        super(qualify(schema, name), comment);

        this.options = options;
        this.tableschema = schema;
    }

    // ------------------------------------------------------------------------
    // XXX: QueryPart API
    // ------------------------------------------------------------------------

    @Override
    public Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }

    // ------------------------------------------------------------------------
    // [#5518] Record method inversions, e.g. for use as method references
    // ------------------------------------------------------------------------

    @Override
    public final R from(Record record) {
        return record.into(this);
    }

    // -------------------------------------------------------------------------
    // XXX: Expressions based on this table
    // -------------------------------------------------------------------------

    @Override
    public final QualifiedAsterisk asterisk() {
        return new QualifiedAsteriskImpl(this);
    }

    @Override
    public final Field<RowId> rowid() {
        return new RowIdField(this);
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
    public final DataType<R> getDataType() {
        if (tabletype == null)
            tabletype = new TableDataType<>(this);

        return tabletype;
    }

    @Override
    public final RecordType<R> recordType() {
        return fields0();
    }

    @Override
    public final R newRecord() {
        return DSL.using(new DefaultConfiguration()).newRecord(this);
    }

    /*
     * Subclasses may override this method
     */
    @SuppressWarnings({ "rawtypes" })
    @Override
    public Row fieldsRow() {
        return new RowImpl(fields0());
    }


    @Override
    public final Stream<Field<?>> fieldStream() {
        return Stream.of(fields());
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
    public final <T> Field<T> field(String name, Class<T> type) {
        return fieldsRow().field(name, type);
    }

    @Override
    public final <T> Field<T> field(String name, DataType<T> dataType) {
        return fieldsRow().field(name, dataType);
    }

    @Override
    public final Field<?> field(Name name) {
        return fieldsRow().field(name);
    }

    @Override
    public final <T> Field<T> field(Name name, Class<T> type) {
        return fieldsRow().field(name, type);
    }

    @Override
    public final <T> Field<T> field(Name name, DataType<T> dataType) {
        return fieldsRow().field(name, dataType);
    }

    @Override
    public final Field<?> field(int index) {
        return fieldsRow().field(index);
    }

    @Override
    public final <T> Field<T> field(int index, Class<T> type) {
        return fieldsRow().field(index, type);
    }

    @Override
    public final <T> Field<T> field(int index, DataType<T> dataType) {
        return fieldsRow().field(index, dataType);
    }

    @Override
    public final Field<?>[] fields() {
        return fieldsRow().fields();
    }

    @Override
    public final Field<?>[] fields(Field<?>... fields) {
        return fieldsRow().fields(fields);
    }

    @Override
    public final Field<?>[] fields(String... fieldNames) {
        return fieldsRow().fields(fieldNames);
    }

    @Override
    public final Field<?>[] fields(Name... fieldNames) {
        return fieldsRow().fields(fieldNames);
    }

    @Override
    public final Field<?>[] fields(int... fieldIndexes) {
        return fieldsRow().fields(fieldIndexes);
    }

    @Override
    public final int indexOf(Field<?> field) {
        return fieldsRow().indexOf(field);
    }

    @Override
    public final int indexOf(String fieldName) {
        return fieldsRow().indexOf(fieldName);
    }

    @Override
    public final int indexOf(Name fieldName) {
        return fieldsRow().indexOf(fieldName);
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


    @Override
    public final Table<R> asTable(String alias, Function<? super Field<?>, ? extends String> aliasFunction) {
        return as(alias, aliasFunction);
    }

    @Override
    public final Table<R> asTable(String alias, BiFunction<? super Field<?>, ? super Integer, ? extends String> aliasFunction) {
        return as(alias, aliasFunction);
    }


    @Override
    public /* non-final for covariant overriding */ Table<R> as(String alias) {
        return as(DSL.name(alias));
    }

    @Override
    public /* non-final for covariant overriding */ Table<R> as(String alias, String... fieldAliases) {
        return as(DSL.name(alias), Tools.names(fieldAliases));
    }


    @Override
    public final Table<R> as(String alias, Function<? super Field<?>, ? extends String> aliasFunction) {
        return as(alias, Stream.of(fields()).map(aliasFunction).toArray(String[]::new));
    }

    @Override
    public final Table<R> as(String alias, BiFunction<? super Field<?>, ? super Integer, ? extends String> aliasFunction) {
        Field<?>[] fields = fields();
        String[] names = new String[fields.length];
        for (int i = 0; i < fields.length; i++)
            names[i] = aliasFunction.apply(fields[i], i);
        return as(alias, names);
    }


    @Override
    public /* non-final for covariant overriding */ Table<R> as(Name alias) {
        throw new UnsupportedOperationException("Subtypes should override this method. If using the jOOQ code generator, please re-generate your code");
    }

    @Override
    public /* non-final for covariant overriding */ Table<R> as(Name alias, Name... fieldAliases) {
        throw new UnsupportedOperationException("Subtypes should override this method. If using the jOOQ code generator, please re-generate your code");
    }


    @Override
    public final Table<R> as(Name alias, Function<? super Field<?>, ? extends Name> aliasFunction) {
        return as(alias, Stream.of(fields()).map(aliasFunction).toArray(Name[]::new));
    }

    @Override
    public final Table<R> as(Name alias, BiFunction<? super Field<?>, ? super Integer, ? extends Name> aliasFunction) {
        Field<?>[] fields = fields();
        Name[] names = new Name[fields.length];
        for (int i = 0; i < fields.length; i++)
            names[i] = aliasFunction.apply(fields[i], i);
        return as(alias, names);
    }


    // ------------------------------------------------------------------------
    // XXX: Table API
    // ------------------------------------------------------------------------

    @Override
    public final TableType getType() {
        return options.type();
    }

    @Override
    public final TableOptions getOptions() {
        return options;
    }

    @Override
    public final Catalog getCatalog() {
        return getSchema() == null ? null : getSchema().getCatalog();
    }

    @Override
    public /* non-final */ Schema getSchema() {
        if (tableschema == null)
            tableschema = getQualifiedName().qualified()
                        ? DSL.schema(getQualifiedName().qualifier())
                        : null;

        return tableschema;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Subclasses should override this method
     */
    @Override
    public Identity<R, ?> getIdentity() {
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
    public TableField<R, ?> getRecordVersion() {
        return null;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Subclasses may override this method
     */
    @Override
    public TableField<R, ?> getRecordTimestamp() {
        return null;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Subclasses should override this method
     */
    @Override
    public List<Index> getIndexes() {
        return Collections.emptyList();
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

    @SuppressWarnings("unchecked")
    @Override
    public final <O extends Record> List<ForeignKey<R, O>> getReferencesTo(Table<O> other) {
        List<ForeignKey<R, O>> result = new ArrayList<>();

        for (ForeignKey<R, ?> reference : getReferences()) {
            if (other.equals(reference.getKey().getTable())) {
                result.add((ForeignKey<R, O>) reference);
            }

            // [#1460] [#6304] In case the other table was aliased
            else {
                Table<O> aliased = Tools.aliased(other);

                if (aliased != null && aliased.equals(reference.getKey().getTable())) {
                    result.add((ForeignKey<R, O>) reference);
                }
            }
        }

        return Collections.unmodifiableList(result);
    }
    /**
     * {@inheritDoc}
     * <p>
     * Subclasses should override this method
     */
    @Override
    public List<Check<R>> getChecks() {
        return Collections.emptyList();
    }

    /**
     * Subclasses may call this method to create {@link TableField} objects that
     * are linked to this table.
     *
     * @param name The name of the field (case-sensitive!)
     * @param type The data type of the field
     * @deprecated - 3.12.0 - [#8000] - Use
     *             {@link AbstractTable#createField(Name, DataType, Table)} instead.
     */
    @Deprecated
    protected static final <R extends Record, T> TableField<R, T> createField(String name, DataType<T> type, Table<R> table) {
        return createField(DSL.name(name), type, table, null, null, null);
    }

    /**
     * Subclasses may call this method to create {@link TableField} objects that
     * are linked to this table.
     *
     * @param name The name of the field (case-sensitive!)
     * @param type The data type of the field
     * @deprecated - 3.12.0 - [#8000] - Use
     *             {@link AbstractTable#createField(Name, DataType, Table, String)} instead.
     */
    @Deprecated
    protected static final <R extends Record, T> TableField<R, T> createField(String name, DataType<T> type, Table<R> table, String comment) {
        return createField(DSL.name(name), type, table, comment, null, null);
    }

    /**
     * Subclasses may call this method to create {@link TableField} objects that
     * are linked to this table.
     *
     * @param name The name of the field (case-sensitive!)
     * @param type The data type of the field
     * @deprecated - 3.12.0 - [#8000] - Use
     *             {@link AbstractTable#createField(Name, DataType, Table, String, Converter)}
     *             instead.
     */
    @Deprecated
    protected static final <R extends Record, T, U> TableField<R, U> createField(String name, DataType<T> type, Table<R> table, String comment, Converter<T, U> converter) {
        return createField(DSL.name(name), type, table, comment, converter, null);
    }

    /**
     * Subclasses may call this method to create {@link TableField} objects that
     * are linked to this table.
     *
     * @param name The name of the field (case-sensitive!)
     * @param type The data type of the field
     * @deprecated - 3.12.0 - [#8000] - Use
     *             {@link AbstractTable#createField(Name, DataType, Table, String, Binding)}
     *             instead.
     */
    @Deprecated
    protected static final <R extends Record, T, U> TableField<R, U> createField(String name, DataType<T> type, Table<R> table, String comment, Binding<T, U> binding) {
        return createField(DSL.name(name), type, table, comment, null, binding);
    }

    /**
     * Subclasses may call this method to create {@link TableField} objects that
     * are linked to this table.
     *
     * @param name The name of the field (case-sensitive!)
     * @param type The data type of the field
     * @deprecated - 3.12.0 - [#8000] - Use
     *             {@link AbstractTable#createField(Name, DataType, Table, String, Converter, Binding)}
     *             instead.
     */
    @Deprecated
    protected static final <R extends Record, T, X, U> TableField<R, U> createField(String name, DataType<T> type, Table<R> table, String comment, Converter<X, U> converter, Binding<T, X> binding) {
        return createField(DSL.name(name), type, table, comment, converter, binding);
    }

    /**
     * Subclasses may call this method to create {@link TableField} objects that
     * are linked to this table.
     *
     * @param name The name of the field (case-sensitive!)
     * @param type The data type of the field
     * @deprecated - 3.12.0 - [#8000] - Use
     *             {@link #createField(Name, DataType)}
     *             instead.
     */
    @Deprecated
    protected final <T> TableField<R, T> createField(String name, DataType<T> type) {
        return createField(DSL.name(name), type, this, null, null, null);
    }

    /**
     * Subclasses may call this method to create {@link TableField} objects that
     * are linked to this table.
     *
     * @param name The name of the field (case-sensitive!)
     * @param type The data type of the field
     * @deprecated - 3.12.0 - [#8000] - Use
     *             {@link #createField(Name, DataType, String)}
     *             instead.
     */
    @Deprecated
    protected final <T> TableField<R, T> createField(String name, DataType<T> type, String comment) {
        return createField(DSL.name(name), type, this, comment, null, null);
    }

    /**
     * Subclasses may call this method to create {@link TableField} objects that
     * are linked to this table.
     *
     * @param name The name of the field (case-sensitive!)
     * @param type The data type of the field
     * @deprecated - 3.12.0 - [#8000] - Use
     *             {@link #createField(Name, DataType, String, Converter)}
     *             instead.
     */
    @Deprecated
    protected final <T, U> TableField<R, U> createField(String name, DataType<T> type, String comment, Converter<T, U> converter) {
        return createField(DSL.name(name), type, this, comment, converter, null);
    }

    /**
     * Subclasses may call this method to create {@link TableField} objects that
     * are linked to this table.
     *
     * @param name The name of the field (case-sensitive!)
     * @param type The data type of the field
     * @deprecated - 3.12.0 - [#8000] - Use
     *             {@link #createField(Name, DataType, String, Binding)}
     *             instead.
     */
    @Deprecated
    protected final <T, U> TableField<R, U> createField(String name, DataType<T> type, String comment, Binding<T, U> binding) {
        return createField(DSL.name(name), type, this, comment, null, binding);
    }

    /**
     * Subclasses may call this method to create {@link TableField} objects that
     * are linked to this table.
     *
     * @param name The name of the field (case-sensitive!)
     * @param type The data type of the field
     * @deprecated - 3.12.0 - [#8000] - Use
     *             {@link #createField(Name, DataType, String, Converter, Binding)}
     *             instead.
     */
    @Deprecated
    protected final <T, X, U> TableField<R, U> createField(String name, DataType<T> type, String comment, Converter<X, U> converter, Binding<T, X> binding) {
        return createField(DSL.name(name), type, this, comment, converter, binding);
    }

    /**
     * Subclasses may call this method to create {@link TableField} objects that
     * are linked to this table.
     *
     * @param name The name of the field (case-sensitive!)
     * @param type The data type of the field
     */
    protected static final <R extends Record, T> TableField<R, T> createField(Name name, DataType<T> type, Table<R> table) {
        return createField(name, type, table, null, null, null);
    }

    /**
     * Subclasses may call this method to create {@link TableField} objects that
     * are linked to this table.
     *
     * @param name The name of the field (case-sensitive!)
     * @param type The data type of the field
     */
    protected static final <R extends Record, T> TableField<R, T> createField(Name name, DataType<T> type, Table<R> table, String comment) {
        return createField(name, type, table, comment, null, null);
    }

    /**
     * Subclasses may call this method to create {@link TableField} objects that
     * are linked to this table.
     *
     * @param name The name of the field (case-sensitive!)
     * @param type The data type of the field
     */
    protected static final <R extends Record, T, U> TableField<R, U> createField(Name name, DataType<T> type, Table<R> table, String comment, Converter<T, U> converter) {
        return createField(name, type, table, comment, converter, null);
    }

    /**
     * Subclasses may call this method to create {@link TableField} objects that
     * are linked to this table.
     *
     * @param name The name of the field (case-sensitive!)
     * @param type The data type of the field
     */
    protected static final <R extends Record, T, U> TableField<R, U> createField(Name name, DataType<T> type, Table<R> table, String comment, Binding<T, U> binding) {
        return createField(name, type, table, comment, null, binding);
    }

    /**
     * Subclasses may call this method to create {@link TableField} objects that
     * are linked to this table.
     *
     * @param name The name of the field (case-sensitive!)
     * @param type The data type of the field
     */
    @SuppressWarnings("unchecked")
    protected static final <R extends Record, T, X, U> TableField<R, U> createField(Name name, DataType<T> type, Table<R> table, String comment, Converter<X, U> converter, Binding<T, X> binding) {
        final Binding<T, U> actualBinding = DefaultBinding.newBinding(converter, type, binding);
        final DataType<U> actualType =
            converter == null && binding == null
          ? (DataType<U>) type
          : type.asConvertedDataType(actualBinding);

        // [#5999] TODO: Allow for user-defined Names
        final TableFieldImpl<R, U> tableField = new TableFieldImpl<>(name, actualType, table, DSL.comment(comment), actualBinding);

        // [#1199] The public API of Table returns immutable field lists
        if (table instanceof TableImpl) {
            ((TableImpl<?>) table).fields0().add(tableField);
        }

        return tableField;
    }

    /**
     * Subclasses may call this method to create {@link TableField} objects that
     * are linked to this table.
     *
     * @param name The name of the field (case-sensitive!)
     * @param type The data type of the field
     */
    protected final <T> TableField<R, T> createField(Name name, DataType<T> type) {
        return createField(name, type, this, null, null, null);
    }

    /**
     * Subclasses may call this method to create {@link TableField} objects that
     * are linked to this table.
     *
     * @param name The name of the field (case-sensitive!)
     * @param type The data type of the field
     */
    protected final <T> TableField<R, T> createField(Name name, DataType<T> type, String comment) {
        return createField(name, type, this, comment, null, null);
    }

    /**
     * Subclasses may call this method to create {@link TableField} objects that
     * are linked to this table.
     *
     * @param name The name of the field (case-sensitive!)
     * @param type The data type of the field
     */
    protected final <T, U> TableField<R, U> createField(Name name, DataType<T> type, String comment, Converter<T, U> converter) {
        return createField(name, type, this, comment, converter, null);
    }

    /**
     * Subclasses may call this method to create {@link TableField} objects that
     * are linked to this table.
     *
     * @param name The name of the field (case-sensitive!)
     * @param type The data type of the field
     */
    protected final <T, U> TableField<R, U> createField(Name name, DataType<T> type, String comment, Binding<T, U> binding) {
        return createField(name, type, this, comment, null, binding);
    }

    /**
     * Subclasses may call this method to create {@link TableField} objects that
     * are linked to this table.
     *
     * @param name The name of the field (case-sensitive!)
     * @param type The data type of the field
     */
    protected final <T, X, U> TableField<R, U> createField(Name name, DataType<T> type, String comment, Converter<X, U> converter, Binding<T, X> binding) {
        return createField(name, type, this, comment, converter, binding);
    }

    // ------------------------------------------------------------------------
    // XXX: Convenience methods and synthetic methods
    // ------------------------------------------------------------------------

    @Override
    public final Condition eq(Table<R> that) {
        return equal(that);
    }

    @Override
    public final Condition equal(Table<R> that) {
        return new TableComparison<>(this, that, Comparator.EQUALS);
    }

    @Override
    public final Condition ne(Table<R> that) {
        return notEqual(that);
    }

    @Override
    public final Condition notEqual(Table<R> that) {
        return new TableComparison<>(this, that, Comparator.NOT_EQUALS);
    }

    // ------------------------------------------------------------------------
    // XXX: Other API
    // ------------------------------------------------------------------------

    @Override
    public final Table<R> useIndex(String... indexes) {
        return new HintedTable<>(this, "use index", indexes);
    }

    @Override
    public final Table<R> useIndexForJoin(String... indexes) {
        return new HintedTable<>(this, "use index for join", indexes);
    }

    @Override
    public final Table<R> useIndexForOrderBy(String... indexes) {
        return new HintedTable<>(this, "use index for order by", indexes);
    }

    @Override
    public final Table<R> useIndexForGroupBy(String... indexes) {
        return new HintedTable<>(this, "use index for group by", indexes);
    }

    @Override
    public final Table<R> ignoreIndex(String... indexes) {
        return new HintedTable<>(this, "ignore index", indexes);
    }

    @Override
    public final Table<R> ignoreIndexForJoin(String... indexes) {
        return new HintedTable<>(this, "ignore index for join", indexes);
    }

    @Override
    public final Table<R> ignoreIndexForOrderBy(String... indexes) {
        return new HintedTable<>(this, "ignore index for order by", indexes);
    }

    @Override
    public final Table<R> ignoreIndexForGroupBy(String... indexes) {
        return new HintedTable<>(this, "ignore index for group by", indexes);
    }

    @Override
    public final Table<R> forceIndex(String... indexes) {
        return new HintedTable<>(this, "force index", indexes);
    }

    @Override
    public final Table<R> forceIndexForJoin(String... indexes) {
        return new HintedTable<>(this, "force index for join", indexes);
    }

    @Override
    public final Table<R> forceIndexForOrderBy(String... indexes) {
        return new HintedTable<>(this, "force index for order by", indexes);
    }

    @Override
    public final Table<R> forceIndexForGroupBy(String... indexes) {
        return new HintedTable<>(this, "force index for group by", indexes);
    }

    // ------------------------------------------------------------------------
    // XXX: aliasing API
    // ------------------------------------------------------------------------

    @Override
    public final Table<R> as(Table<?> otherTable) {
        return as(otherTable.getUnqualifiedName());
    }

    @Override
    public final Table<R> as(Table<?> otherTable, Field<?>... otherFields) {
        return as(otherTable.getUnqualifiedName(), Tools.fieldNames(otherFields));
    }


    @Override
    public final Table<R> as(Table<?> otherTable, Function<? super Field<?>, ? extends Field<?>> aliasFunction) {
        return as(otherTable.getUnqualifiedName(), f -> aliasFunction.apply(f).getUnqualifiedName());
    }

    @Override
    public final Table<R> as(Table<?> otherTable, BiFunction<? super Field<?>, ? super Integer, ? extends Field<?>> aliasFunction) {
        return as(otherTable.getUnqualifiedName(), (f, i) -> aliasFunction.apply(f, i).getUnqualifiedName());
    }












































































































    // ------------------------------------------------------------------------
    // XXX: DIVISION API
    // ------------------------------------------------------------------------

    @Override
    public final DivideByOnStep divideBy(Table<?> divisor) {
        return new DivideBy(this, divisor);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public final TableOnStep<R> leftSemiJoin(TableLike<?> table) {
        return (TableOnStep) join(table, LEFT_SEMI_JOIN);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public final TableOnStep<R> leftAntiJoin(TableLike<?> table) {
        return (TableOnStep) join(table, LEFT_ANTI_JOIN);
    }

    // ------------------------------------------------------------------------
    // XXX: WHERE API
    // ------------------------------------------------------------------------

    @Override
    public /* non-final */ Table<R> where(Condition condition) {
        return table(selectFrom(this).where(condition)).as(this);
    }

    @Override
    public /* non-final */ Table<R> where(Condition... conditions) {
        return table(selectFrom(this).where(conditions)).as(this);
    }

    @Override
    public /* non-final */ Table<R> where(Collection<? extends Condition> conditions) {
        return table(selectFrom(this).where(conditions)).as(this);
    }

    @Override
    public /* non-final */ Table<R> where(Field<Boolean> field) {
        return where(condition(field));
    }

    @Override
    public /* non-final */ Table<R> where(SQL sql) {
        return where(condition(sql));
    }

    @Override
    public /* non-final */ Table<R> where(String sql) {
        return where(condition(sql));
    }

    @Override
    public /* non-final */ Table<R> where(String sql, Object... bindings) {
        return where(condition(sql, bindings));
    }

    @Override
    public /* non-final */ Table<R> where(String sql, QueryPart... parts) {
        return where(condition(sql, parts));
    }

    @Override
    public /* non-final */ Table<R> whereExists(Select<?> select) {
        return where(exists(select));
    }

    @Override
    public /* non-final */ Table<R> whereNotExists(Select<?> select) {
        return where(notExists(select));
    }

    // ------------------------------------------------------------------------
    // XXX: JOIN API
    // ------------------------------------------------------------------------

    @Override
    public final TableOptionalOnStep<Record> join(TableLike<?> table, JoinType type) {
        return new JoinTable(this, table, type);
    }

    @Override
    public final TableOnStep<Record> join(TableLike<?> table) {
        return innerJoin(table);
    }

    @Override
    public final TableOnStep<Record> join(SQL sql) {
        return innerJoin(sql);
    }

    @Override
    public final TableOnStep<Record> join(String sql) {
        return innerJoin(sql);
    }

    @Override
    public final TableOnStep<Record> join(String sql, Object... bindings) {
        return innerJoin(sql, bindings);
    }

    @Override
    public final TableOnStep<Record> join(String sql, QueryPart... parts) {
        return innerJoin(sql, parts);
    }

    @Override
    public final TableOnStep<Record> join(Name name) {
        return innerJoin(table(name));
    }

    @Override
    public final TableOnStep<Record> innerJoin(TableLike<?> table) {
        return join(table, JOIN);
    }

    @Override
    public final TableOnStep<Record> innerJoin(SQL sql) {
        return innerJoin(table(sql));
    }

    @Override
    public final TableOnStep<Record> innerJoin(String sql) {
        return innerJoin(table(sql));
    }

    @Override
    public final TableOnStep<Record> innerJoin(String sql, Object... bindings) {
        return innerJoin(table(sql, bindings));
    }

    @Override
    public final TableOnStep<Record> innerJoin(String sql, QueryPart... parts) {
        return innerJoin(table(sql, parts));
    }

    @Override
    public final TableOnStep<Record> innerJoin(Name name) {
        return innerJoin(table(name));
    }















    @Override
    public final TablePartitionByStep<Record> leftJoin(TableLike<?> table) {
        return leftOuterJoin(table);
    }

    @Override
    public final TablePartitionByStep<Record> leftJoin(SQL sql) {
        return leftOuterJoin(sql);
    }

    @Override
    public final TablePartitionByStep<Record> leftJoin(String sql) {
        return leftOuterJoin(sql);
    }

    @Override
    public final TablePartitionByStep<Record> leftJoin(String sql, Object... bindings) {
        return leftOuterJoin(sql, bindings);
    }

    @Override
    public final TablePartitionByStep<Record> leftJoin(String sql, QueryPart... parts) {
        return leftOuterJoin(sql, parts);
    }

    @Override
    public final TablePartitionByStep<Record> leftJoin(Name name) {
        return leftOuterJoin(table(name));
    }

    @SuppressWarnings("unchecked")
    @Override
    public final TablePartitionByStep<Record> leftOuterJoin(TableLike<?> table) {
        return (TablePartitionByStep<Record>) join(table, LEFT_OUTER_JOIN);
    }

    @Override
    public final TablePartitionByStep<Record> leftOuterJoin(SQL sql) {
        return leftOuterJoin(table(sql));
    }

    @Override
    public final TablePartitionByStep<Record> leftOuterJoin(String sql) {
        return leftOuterJoin(table(sql));
    }

    @Override
    public final TablePartitionByStep<Record> leftOuterJoin(String sql, Object... bindings) {
        return leftOuterJoin(table(sql, bindings));
    }

    @Override
    public final TablePartitionByStep<Record> leftOuterJoin(String sql, QueryPart... parts) {
        return leftOuterJoin(table(sql, parts));
    }

    @Override
    public final TablePartitionByStep<Record> leftOuterJoin(Name name) {
        return leftOuterJoin(table(name));
    }

    @Override
    public final TablePartitionByStep<Record> rightJoin(TableLike<?> table) {
        return rightOuterJoin(table);
    }

    @Override
    public final TablePartitionByStep<Record> rightJoin(SQL sql) {
        return rightOuterJoin(sql);
    }

    @Override
    public final TablePartitionByStep<Record> rightJoin(String sql) {
        return rightOuterJoin(sql);
    }

    @Override
    public final TablePartitionByStep<Record> rightJoin(String sql, Object... bindings) {
        return rightOuterJoin(sql, bindings);
    }

    @Override
    public final TablePartitionByStep<Record> rightJoin(String sql, QueryPart... parts) {
        return rightOuterJoin(sql, parts);
    }

    @Override
    public final TablePartitionByStep<Record> rightJoin(Name name) {
        return rightOuterJoin(table(name));
    }

    @SuppressWarnings("unchecked")
    @Override
    public final TablePartitionByStep<Record> rightOuterJoin(TableLike<?> table) {
        return (TablePartitionByStep<Record>) join(table, RIGHT_OUTER_JOIN);
    }

    @Override
    public final TablePartitionByStep<Record> rightOuterJoin(SQL sql) {
        return rightOuterJoin(table(sql));
    }

    @Override
    public final TablePartitionByStep<Record> rightOuterJoin(String sql) {
        return rightOuterJoin(table(sql));
    }

    @Override
    public final TablePartitionByStep<Record> rightOuterJoin(String sql, Object... bindings) {
        return rightOuterJoin(table(sql, bindings));
    }

    @Override
    public final TablePartitionByStep<Record> rightOuterJoin(String sql, QueryPart... parts) {
        return rightOuterJoin(table(sql, parts));
    }

    @Override
    public final TablePartitionByStep<Record> rightOuterJoin(Name name) {
        return rightOuterJoin(table(name));
    }

    @Override
    public final TableOnStep<Record> fullOuterJoin(TableLike<?> table) {
        return join(table, FULL_OUTER_JOIN);
    }

    @Override
    public final TableOnStep<Record> fullOuterJoin(SQL sql) {
        return fullOuterJoin(table(sql));
    }

    @Override
    public final TableOnStep<Record> fullOuterJoin(String sql) {
        return fullOuterJoin(table(sql));
    }

    @Override
    public final TableOnStep<Record> fullOuterJoin(String sql, Object... bindings) {
        return fullOuterJoin(table(sql, bindings));
    }

    @Override
    public final TableOnStep<Record> fullOuterJoin(String sql, QueryPart... parts) {
        return fullOuterJoin(table(sql, parts));
    }

    @Override
    public final TableOnStep<Record> fullOuterJoin(Name name) {
        return fullOuterJoin(table(name));
    }

    @Override
    public final TableOnStep<Record> fullJoin(TableLike<?> table) {
        return fullOuterJoin(table);
    }

    @Override
    public final TableOnStep<Record> fullJoin(SQL sql) {
        return fullOuterJoin(sql);
    }

    @Override
    public final TableOnStep<Record> fullJoin(String sql) {
        return fullOuterJoin(sql);
    }

    @Override
    public final TableOnStep<Record> fullJoin(String sql, Object... bindings) {
        return fullOuterJoin(sql, bindings);
    }

    @Override
    public final TableOnStep<Record> fullJoin(String sql, QueryPart... parts) {
        return fullOuterJoin(sql, parts);
    }

    @Override
    public final TableOnStep<Record> fullJoin(Name name) {
        return fullOuterJoin(name);
    }

    @Override
    public final Table<Record> crossJoin(TableLike<?> table) {
        return join(table, CROSS_JOIN);
    }

    @Override
    public final Table<Record> crossJoin(SQL sql) {
        return crossJoin(table(sql));
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
    public final Table<Record> crossJoin(Name name) {
        return crossJoin(table(name));
    }

    @Override
    public final Table<Record> naturalJoin(TableLike<?> table) {
        return join(table, NATURAL_JOIN);
    }

    @Override
    public final Table<Record> naturalJoin(SQL sql) {
        return naturalJoin(table(sql));
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
    public final Table<Record> naturalJoin(Name name) {
        return naturalJoin(table(name));
    }

    @Override
    public final Table<Record> naturalLeftOuterJoin(TableLike<?> table) {
        return join(table, NATURAL_LEFT_OUTER_JOIN);
    }

    @Override
    public final Table<Record> naturalLeftOuterJoin(SQL sql) {
        return naturalLeftOuterJoin(table(sql));
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
    public final Table<Record> naturalLeftOuterJoin(Name name) {
        return naturalLeftOuterJoin(table(name));
    }

    @Override
    public final Table<Record> naturalRightOuterJoin(TableLike<?> table) {
        return join(table, NATURAL_RIGHT_OUTER_JOIN);
    }

    @Override
    public final Table<Record> naturalRightOuterJoin(SQL sql) {
        return naturalRightOuterJoin(table(sql));
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

    @Override
    public final Table<Record> naturalRightOuterJoin(Name name) {
        return naturalRightOuterJoin(table(name));
    }

    @Override
    public final Table<Record> naturalFullOuterJoin(TableLike<?> table) {
        return join(table, NATURAL_FULL_OUTER_JOIN);
    }

    @Override
    public final Table<Record> naturalFullOuterJoin(SQL sql) {
        return naturalFullOuterJoin(table(sql));
    }

    @Override
    public final Table<Record> naturalFullOuterJoin(String sql) {
        return naturalFullOuterJoin(table(sql));
    }

    @Override
    public final Table<Record> naturalFullOuterJoin(String sql, Object... bindings) {
        return naturalFullOuterJoin(table(sql, bindings));
    }

    @Override
    public final Table<Record> naturalFullOuterJoin(String sql, QueryPart... parts) {
        return naturalFullOuterJoin(table(sql, parts));
    }

    @Override
    public final Table<Record> naturalFullOuterJoin(Name name) {
        return naturalFullOuterJoin(table(name));
    }

    @Override
    public final Table<Record> crossApply(TableLike<?> table) {
        return join(table, CROSS_APPLY);
    }

    @Override
    public final Table<Record> crossApply(SQL sql) {
        return crossApply(table(sql));
    }

    @Override
    public final Table<Record> crossApply(String sql) {
        return crossApply(table(sql));
    }

    @Override
    public final Table<Record> crossApply(String sql, Object... bindings) {
        return crossApply(table(sql, bindings));
    }

    @Override
    public final Table<Record> crossApply(String sql, QueryPart... parts) {
        return crossApply(table(sql, parts));
    }

    @Override
    public final Table<Record> crossApply(Name name) {
        return crossApply(table(name));
    }

    @Override
    public final Table<Record> outerApply(TableLike<?> table) {
        return join(table, OUTER_APPLY);
    }

    @Override
    public final Table<Record> outerApply(SQL sql) {
        return outerApply(table(sql));
    }

    @Override
    public final Table<Record> outerApply(String sql) {
        return outerApply(table(sql));
    }

    @Override
    public final Table<Record> outerApply(String sql, Object... bindings) {
        return outerApply(table(sql, bindings));
    }

    @Override
    public final Table<Record> outerApply(String sql, QueryPart... parts) {
        return outerApply(table(sql, parts));
    }

    @Override
    public final Table<Record> outerApply(Name name) {
        return outerApply(table(name));
    }

    @Override
    public final TableOptionalOnStep<Record> straightJoin(TableLike<?> table) {
        return join(table, STRAIGHT_JOIN);
    }

    @Override
    public final TableOptionalOnStep<Record> straightJoin(SQL sql) {
        return straightJoin(table(sql));
    }

    @Override
    public final TableOptionalOnStep<Record> straightJoin(String sql) {
        return straightJoin(table(sql));
    }

    @Override
    public final TableOptionalOnStep<Record> straightJoin(String sql, Object... bindings) {
        return straightJoin(table(sql, bindings));
    }

    @Override
    public final TableOptionalOnStep<Record> straightJoin(String sql, QueryPart... parts) {
        return straightJoin(table(sql, parts));
    }

    @Override
    public final TableOptionalOnStep<Record> straightJoin(Name name) {
        return straightJoin(table(name));
    }
}
