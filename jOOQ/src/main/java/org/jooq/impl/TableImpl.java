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
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
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

import static java.util.stream.Collectors.joining;
import static org.jooq.Clause.TABLE;
import static org.jooq.Clause.TABLE_ALIAS;
import static org.jooq.Clause.TABLE_REFERENCE;
// ...
// ...
// ...
import static org.jooq.SQLDialect.DUCKDB;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.HSQLDB;
// ...
// ...
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.YUGABYTEDB;
import static org.jooq.impl.DSL.noCondition;
import static org.jooq.impl.DefaultMetaProvider.meta;
import static org.jooq.impl.Internal.createPathAlias;
import static org.jooq.impl.Keywords.K_TABLE;
import static org.jooq.impl.QueryPartListView.wrap;
import static org.jooq.impl.SchemaImpl.DEFAULT_SCHEMA;
import static org.jooq.impl.Tools.EMPTY_OBJECT;
import static org.jooq.impl.Tools.getMappedTable;
import static org.jooq.tools.StringUtils.defaultIfNull;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Stream;

import org.jooq.Clause;
import org.jooq.Comment;
import org.jooq.Condition;
import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.InverseForeignKey;
import org.jooq.JoinType;
import org.jooq.Name;
// ...
import org.jooq.Record;
import org.jooq.Row;
import org.jooq.SQLDialect;
import org.jooq.Schema;
import org.jooq.Select;
import org.jooq.Table;
import org.jooq.TableLike;
import org.jooq.TableOptionalOnStep;
import org.jooq.TableOptions;
import org.jooq.impl.QOM.UEmpty;
import org.jooq.tools.StringUtils;

import org.jetbrains.annotations.Nullable;

/**
 * A common base type for tables
 * <p>
 * This type is for JOOQ INTERNAL USE only. Do not reference directly
 *
 * @author Lukas Eder
 */
@org.jooq.Internal
public class TableImpl<R extends Record>
extends
    AbstractTable<R>
implements
    ScopeMappable,
    ScopeNestable,
    SimpleCheckQueryPart,
    UEmpty
{

    private static final Clause[]        CLAUSES_TABLE_REFERENCE           = { TABLE, TABLE_REFERENCE };
    private static final Clause[]        CLAUSES_TABLE_ALIAS               = { TABLE, TABLE_ALIAS };
    private static final Set<SQLDialect> NO_SUPPORT_QUALIFIED_TVF_CALLS    = SQLDialect.supportedBy(HSQLDB, POSTGRES, YUGABYTEDB);
    private static final Set<SQLDialect> REQUIRES_TVF_TABLE_CONSTRUCTOR    = SQLDialect.supportedBy(HSQLDB);





    final FieldsImpl<R>                  fields;
    final Alias<Table<R>>                alias;
    final Condition                      where;

    protected final Field<?>[]           parameters;
    final Table<?>                       path;
    final ForeignKey<?, R>               childPath;
    final InverseForeignKey<?, R>        parentPath;

    /**
     * @deprecated - 3.10 - [#5996] - Use {@link #TableImpl(Name)} instead (or
     *             re-generated your code).
     */
    @Deprecated
    public TableImpl(String name) {
        this(DSL.name(name));
    }

    /**
     * @deprecated - 3.10 - [#5996] - Use {@link #TableImpl(Name, Schema)}
     *             instead (or re-generated your code).
     */
    @Deprecated
    public TableImpl(String name, Schema schema) {
        this(DSL.name(name), schema);
    }

    /**
     * @deprecated - 3.10 - [#5996] - Use {@link #TableImpl(Name, Schema, Table)}
     *             instead (or re-generated your code).
     */
    @Deprecated
    public TableImpl(String name, Schema schema, Table<R> aliased) {
        this(DSL.name(name), schema, aliased);
    }

    /**
     * @deprecated - 3.10 - [#5996] - Use {@link #TableImpl(Name, Schema, Table, Field[])}
     *             instead (or re-generated your code).
     */
    @Deprecated
    public TableImpl(String name, Schema schema, Table<R> aliased, Field<?>[] parameters) {
        this(DSL.name(name), schema, aliased, parameters);
    }

    /**
     * @deprecated - 3.10 - [#5996] - Use {@link #TableImpl(Name, Schema, Table, Field[], String)}
     *             instead (or re-generated your code).
     */
    @Deprecated
    public TableImpl(String name, Schema schema, Table<R> aliased, Field<?>[] parameters, String comment) {
        this(DSL.name(name), schema, aliased, parameters, comment);
    }

    public TableImpl(Name name) {
        this(name, null, null, null, null, null, null, (Comment) null);
    }

    public TableImpl(Name name, Schema schema) {
        this(name, schema, null, null, null, null, null, (Comment) null);
    }

    public TableImpl(Name name, Schema schema, Table<R> aliased) {
        this(name, schema, null, null, null, aliased, null, (Comment) null);
    }

    public TableImpl(Name name, Schema schema, Table<R> aliased, Field<?>[] parameters) {
        this(name, schema, null, null, null, aliased, parameters, (Comment) null);
    }

    /**
     * @deprecated - 3.11 - [#7027] - Use {@link #TableImpl(Name, Schema, Table, Field[], Comment)} instead.
     */
    @Deprecated
    public TableImpl(Name name, Schema schema, Table<R> aliased, Field<?>[] parameters, String comment) {
        this(name, schema, null, null, null, aliased, parameters, DSL.comment(comment));
    }

    public TableImpl(Name name, Schema schema, Table<R> aliased, Field<?>[] parameters, Comment comment) {
        this(name, schema, null, null, null, aliased, parameters, comment);
    }

    public TableImpl(Name name, Schema schema, Table<R> aliased, Field<?>[] parameters, Comment comment, TableOptions options) {
        this(name, schema, null, null, null, aliased, parameters, comment, options, null);
    }

    public TableImpl(Name name, Schema schema, Table<R> aliased, Field<?>[] parameters, Comment comment, TableOptions options, Condition where) {
        this(name, schema, null, null, null, aliased, parameters, comment, options, where);
    }

    /**
     * @deprecated - [#13639] [#14985] [#15005] - 3.19.0 - Please re-generate your code.
     */
    @Deprecated
    public TableImpl(Table<?> path, ForeignKey<?, R> childPath, Table<R> aliased) {
        this(path, childPath, null, aliased);
    }

    public TableImpl(Table<?> path, ForeignKey<?, R> childPath, InverseForeignKey<?, R> parentPath, Table<R> aliased) {
        this(createPathAlias(path, childPath, parentPath), null, path, childPath, parentPath, aliased, null, aliased.getCommentPart());
    }

    /**
     * @deprecated - [#13639] [#14985] [#15005] - 3.19.0 - Please re-generate your code.
     */
    @Deprecated
    public TableImpl(Name name, Schema schema, Table<?> path, ForeignKey<?, R> childPath, Table<R> aliased, Field<?>[] parameters, Comment comment) {
        this(name, schema, path, childPath, aliased, parameters, comment, TableOptions.table());
    }

    public TableImpl(Name name, Schema schema, Table<?> path, ForeignKey<?, R> childPath, InverseForeignKey<?, R> parentPath, Table<R> aliased, Field<?>[] parameters, Comment comment) {
        this(name, schema, path, childPath, parentPath, aliased, parameters, comment, TableOptions.table(), null);
    }

    /**
     * @deprecated - [#13639] [#14985] [#15005] - 3.19.0 - Please re-generate your code.
     */
    @Deprecated
    public TableImpl(Name name, Schema schema, Table<?> path, ForeignKey<?, R> childPath, Table<R> aliased, Field<?>[] parameters, Comment comment, TableOptions options) {
        this(name, schema, path, childPath, null, aliased, parameters, comment, options);
    }

    /**
     * @deprecated - [#8012] - 3.19.0 - Please re-generate your code.
     */
    @Deprecated
    public TableImpl(
        Name name,
        Schema schema,
        Table<?> path,
        ForeignKey<?, R> childPath,
        InverseForeignKey<?, R> parentPath,
        Table<R> aliased,
        Field<?>[] parameters,
        Comment comment,
        TableOptions options
    ) {
        this(name, schema, path, childPath, parentPath, aliased, parameters, comment, options, null);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public TableImpl(
        Name name,
        Schema schema,
        Table<?> path,
        ForeignKey<?, R> childPath,
        InverseForeignKey<?, R> parentPath,
        Table<R> aliased,
        Field<?>[] parameters,
        Comment comment,
        TableOptions options,
        Condition where
    ) {
        super(options, name, schema, comment);

        this.fields = new FieldsImpl<>();

        if (childPath != null) {
            this.path = path;
            this.childPath = Tools.aliasedKey((ForeignKey) childPath, path, this);
            this.parentPath = null;
        }
        else if (parentPath != null) {
            this.path = path;
            this.childPath = null;
            this.parentPath = Tools.aliasedKey((ForeignKey) parentPath.getForeignKey(), this, path).getInverseKey();
        }
        else if (aliased instanceof TableImpl t) {
            this.path = t.path;
            this.childPath = t.childPath == null ? null : Tools.aliasedKey(t.childPath, t.path, this);
            this.parentPath = t.parentPath == null ? null : Tools.aliasedKey(t.parentPath.getForeignKey(), this, t.path).getInverseKey();
        }
        else {
            this.path = null;
            this.childPath = null;
            this.parentPath = null;
        }

        if (aliased != null) {

            // [#7115] Allow for aliased expressions (e.g. derived tables) to be passed to TableImpl
            //         in order to support "type safe views"
            Alias<Table<R>> existingAlias = Tools.alias(aliased);

            if (existingAlias != null)
                this.alias = new Alias<>(existingAlias.wrapped, this, name, existingAlias.fieldAliases, existingAlias.wrapInParentheses);
            else
                this.alias = new Alias<>(aliased, this, name);
        }
        else
            this.alias = null;

        this.parameters = parameters;
        this.where = where;
    }

    static final Table<?> path(Table<?> t) {
        if (t instanceof TableImpl<?> ti)
            if (ti.path != null)
                return ti.path;

        return null;
    }

    final Condition pathCondition() {
        if (childPath != null)
            return new Join(path, this).onKey(childPath).condition.getWhere();
        else if (parentPath != null)
            return new Join(this, path).onKey(parentPath.getForeignKey()).condition.getWhere();
        else
            return noCondition();
    }

    /**
     * Get the root child if this is an implicit join path expression, or
     * <code>null</code> if not
     */
    @Nullable
    final Table<?> pathRoot() {
        if (path instanceof TableImpl<?> t) {
            if (t.path != null)
                return t.pathRoot();
            else
                return t;
        }
        else
            return null;
    }

    /**
     * Get the aliased table wrapped by this table.
     */
    @Nullable
    Table<R> getAliasedTable() {
        if (alias != null)
            return alias.wrapped();

        return null;
    }

    /**
     * Check if this table already aliases another one.
     * <p>
     * This method is used by generated code of table valued functions. Do not
     * call this method directly.
     */
    @org.jooq.Internal
    protected boolean aliased() {
        return getAliasedTable() != null;
    }

    /**
     * Check if this table is a synthetic table.
     * <p>
     * This method is used by generated code of synthetic views. Do not
     * call this method directly.
     */
    @org.jooq.Internal
    protected boolean isSynthetic() {
        return false;
    }

    @Override
    @org.jooq.Internal
    public final boolean isSimple(Context<?> ctx) {
        return alias == null && (parameters == null || parameters.length < 2);
    }

    @Override
    final FieldsImpl<R> fields0() {

        // [#8012] Implement the same behaviour as InlineDerivedTable to make
        //         sure we produce the proper qualification of fields also when
        //         dereferencing a field using Table.field(String), for example
        if (where != null)
            return new InlineDerivedTable<>(this, where).fields0();
        else
            return fields;
    }

    // [#8489] this override is necessary due to a Scala compiler bug (versions 2.10 and 2.11)
    @Override
    public Row fieldsRow() {
        return super.fieldsRow();
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return alias != null ? CLAUSES_TABLE_ALIAS : CLAUSES_TABLE_REFERENCE;
    }

    @Override
    public final void accept(Context<?> ctx) {













        if ((getTableType().isView() || getTableType().isFunction()) && isSynthetic() && ctx.declareTables()) {
            Select<?> s = getOptions().select();

            // TODO: Avoid parsing this every time
            ctx.visit(
                s != null ? s : new DerivedTable<>(
                    ctx.dsl().parser().parseSelect(getOptions().source(), parameters == null ? EMPTY_OBJECT : parameters)
                ).as(getUnqualifiedName())
            );
            return;
        }

        if (path != null)
            ctx.scopeRegister(this);

        if (alias != null) {
            ctx.visit(alias);
        }
        else {
            if (parameters != null && REQUIRES_TVF_TABLE_CONSTRUCTOR.contains(ctx.dialect()) && ctx.declareTables()) {
                ctx.visit(K_TABLE)
                   .sql('(');

                accept0(ctx);
                ctx.sql(')');

                // [#4834] Generate alias only if allowed to do so
                if (ctx.declareAliases())
                    ctx.sql(' ')
                       .visit(getMappedTable(ctx, this).getUnqualifiedName());
            }
            else
                accept0(ctx);
        }





    }

    private final void accept0(Context<?> ctx) {
        if (ctx.declareTables())
            ctx.scopeMarkStart(this);

        if (ctx.qualify() && (ctx.declareTables()
            || (!NO_SUPPORT_QUALIFIED_TVF_CALLS.contains(ctx.dialect()) || parameters == null)



        )) {
            QualifiedImpl.acceptMappedSchemaPrefix(ctx, getSchema());
        }

        ctx.visit(getMappedTable(ctx, this).getUnqualifiedName());

        if (parameters != null && ctx.declareTables()) {

            // [#2925] Some dialects don't like empty parameter lists
            if (ctx.family() == FIREBIRD && parameters.length == 0)
                ctx.visit(wrap(parameters));
            else
                ctx.sql('(')
                   .visit(wrap(parameters))
                   .sql(')');
        }

        if (ctx.declareTables())
            ctx.scopeMarkEnd(this);
    }

    /**
     * Subclasses may override this method to provide custom aliasing
     * implementations
     * <p>
     * {@inheritDoc}
     */
    @Override
    public Table<R> as(Name as) {
        if (alias != null)
            return alias.wrapped().as(as);
        else
            return new TableAlias<>(this, as);
    }

    /**
     * Subclasses may override this method to provide custom aliasing
     * implementations
     * <p>
     * {@inheritDoc}
     */
    @Override
    public Table<R> as(Name as, Name... fieldAliases) {
        if (alias != null)
            return alias.wrapped().as(as, fieldAliases);
        else
            return new TableAlias<>(this, as, fieldAliases);
    }

    public Table<R> rename(String rename) {
        return rename(DSL.name(rename));
    }

    public Table<R> rename(Name rename) {
        return new TableImpl<>(rename, getSchema());
    }

    public Table<R> rename(Table<?> rename) {
        return rename(rename.getQualifiedName());
    }

    /**
     * Subclasses must override this method if they use the generic type
     * parameter <code>R</code> for other types than {@link Record}
     * <p>
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public Class<? extends R> getRecordType() {

        // TODO: [#4695] Calculate the correct Record[B] type
        return (Class<? extends R>) RecordImplN.class;
    }

    @Override
    public boolean declaresTables() {
        return true;
    }

    @Override
    public final TableOptionalOnStep<Record> join(TableLike<?> table, JoinType type) {
        return super.join(table, type);
    }

    // -------------------------------------------------------------------------
    // XXX: FieldsTrait "undeprecations" for generated code
    // -------------------------------------------------------------------------

    @Override
    public Field<?> field(String name) {
        return super.field(name);
    }

    @Override
    public <T> Field<T> field(String name, Class<T> type) {
        return super.field(name, type);
    }

    @Override
    public <T> Field<T> field(String name, DataType<T> dataType) {
        return super.field(name, dataType);
    }

    @Override
    public Field<?> field(Name name) {
        return super.field(name);
    }

    @Override
    public <T> Field<T> field(Name name, Class<T> type) {
        return super.field(name, type);
    }

    @Override
    public <T> Field<T> field(Name name, DataType<T> dataType) {
        return super.field(name, dataType);
    }



















    // ------------------------------------------------------------------------
    // XXX: Object API
    // ------------------------------------------------------------------------

    @Override
    public int hashCode() {

        // [#7172] [#10274] [#14875] Cannot use getQualifiedName() based super implementation yet here
        return defaultIfNull(getSchema(), DEFAULT_SCHEMA.get()).getQualifiedName().append(getUnqualifiedName()).hashCode();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that)
            return true;

        // [#2144] TableImpl equality can be decided without executing the
        // rather expensive implementation of AbstractQueryPart.equals()
        if (that instanceof TableImpl<?> t) {
            return

                // [#7172] [#10274] Cannot use getQualifiedName() yet here
                StringUtils.equals(
                    defaultIfNull(getSchema(), DEFAULT_SCHEMA.get()),
                    defaultIfNull(t.getSchema(), DEFAULT_SCHEMA.get())
                ) &&
                StringUtils.equals(getName(), t.getName()) &&
                Arrays.equals(parameters, t.parameters);
        }

        // [#14371] Unqualified TableImpls can be equal to TableAlias
        else if (that instanceof TableAlias<?> t) {

            if ($alias() != null)
                return t.getUnqualifiedName().equals($alias());

            // [#7172] [#10274] Cannot use getQualifiedName() yet here
            else if (getSchema() == null)
                return t.getUnqualifiedName().equals(getQualifiedName());
        }

        return super.equals(that);
    }
}
