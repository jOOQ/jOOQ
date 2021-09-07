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

import static java.util.stream.Collectors.joining;
import static org.jooq.Clause.TABLE;
import static org.jooq.Clause.TABLE_ALIAS;
import static org.jooq.Clause.TABLE_REFERENCE;
// ...
// ...
// ...
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.HSQLDB;
// ...
// ...
import static org.jooq.SQLDialect.POSTGRES;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jooq.Clause;
import org.jooq.Comment;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
// ...
import org.jooq.Record;
import org.jooq.Row;
import org.jooq.SQLDialect;
import org.jooq.Schema;
import org.jooq.Select;
import org.jooq.Table;
import org.jooq.TableOptions;
import org.jooq.tools.StringUtils;

/**
 * A common base type for tables
 * <p>
 * This type is for JOOQ INTERNAL USE only. Do not reference directly
 *
 * @author Lukas Eder
 */
@org.jooq.Internal
public class TableImpl<R extends Record> extends AbstractTable<R> implements ScopeMappable, ScopeNestable, SimpleQueryPart {

    private static final Clause[]        CLAUSES_TABLE_REFERENCE           = { TABLE, TABLE_REFERENCE };
    private static final Clause[]        CLAUSES_TABLE_ALIAS               = { TABLE, TABLE_ALIAS };
    private static final Set<SQLDialect> NO_SUPPORT_QUALIFIED_TVF_CALLS    = SQLDialect.supportedBy(HSQLDB, POSTGRES);
    private static final Set<SQLDialect> REQUIRES_TVF_TABLE_CONSTRUCTOR    = SQLDialect.supportedBy(HSQLDB);





    final FieldsImpl<R>                  fields;
    final Alias<Table<R>>                alias;

    protected final Field<?>[]           parameters;
    final Table<?>                       child;
    final ForeignKey<?, R>               childPath;

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
        this(name, null, null, null, null, null, (Comment) null);
    }

    public TableImpl(Name name, Schema schema) {
        this(name, schema, null, null, null, null, (Comment) null);
    }

    public TableImpl(Name name, Schema schema, Table<R> aliased) {
        this(name, schema, null, null, aliased, null, (Comment) null);
    }

    public TableImpl(Name name, Schema schema, Table<R> aliased, Field<?>[] parameters) {
        this(name, schema, null, null, aliased, parameters, (Comment) null);
    }

    /**
     * @deprecated - 3.11 - [#7027] - Use {@link #TableImpl(Name, Schema, Table, Field[], Comment)} instead.
     */
    @Deprecated
    public TableImpl(Name name, Schema schema, Table<R> aliased, Field<?>[] parameters, String comment) {
        this(name, schema, null, null, aliased, parameters, DSL.comment(comment));
    }

    public TableImpl(Name name, Schema schema, Table<R> aliased, Field<?>[] parameters, Comment comment) {
        this(name, schema, null, null, aliased, parameters, comment);
    }

    public TableImpl(Name name, Schema schema, Table<R> aliased, Field<?>[] parameters, Comment comment, TableOptions options) {
        this(name, schema, null, null, aliased, parameters, comment, options);
    }

    public TableImpl(Table<?> child, ForeignKey<?, R> path, Table<R> parent) {
        this(createPathAlias(child, path), null, child, path, parent, null, parent.getCommentPart());
    }

    public TableImpl(Name name, Schema schema, Table<?> child, ForeignKey<?, R> path, Table<R> aliased, Field<?>[] parameters, Comment comment) {
        this(name, schema, child, path, aliased, parameters, comment, TableOptions.table());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public TableImpl(Name name, Schema schema, Table<?> child, ForeignKey<?, R> path, Table<R> aliased, Field<?>[] parameters, Comment comment, TableOptions options) {
        super(options, name, schema, comment);

        this.fields = new FieldsImpl<>();

        if (child != null) {
            this.child = child;
            this.childPath = path == null ? null : Tools.aliasedKey((ForeignKey) path, child, this);
        }
        else if (aliased instanceof TableImpl) {
            this.child = ((TableImpl<?>) aliased).child;
            this.childPath = ((TableImpl) aliased).childPath;
        }
        else {
            this.child = null;
            this.childPath = null;
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
    }

    /**
     * Get the aliased table wrapped by this table.
     */
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
    public final boolean isSimple() {
        return alias == null && (parameters == null || parameters.length < 2);
    }

    @Override
    final FieldsImpl<R> fields0() {
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













        if ((getType().isView() || getType().isFunction()) && isSynthetic() && ctx.declareTables()) {
            Select<?> s = getOptions().select();

            // TODO: Avoid parsing this every time
            ctx.visit(
                s != null ? s : new DerivedTable<>(
                    ctx.dsl().parser().parseSelect(getOptions().source(), parameters == null ? EMPTY_OBJECT : parameters)
                ).as(getUnqualifiedName())
            );
            return;
        }

        if (child != null)
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

    private void accept0(Context<?> ctx) {
        if (ctx.declareTables())
            ctx.scopeMarkStart(this);

        if (ctx.qualify() && (ctx.declareTables()
            || (!NO_SUPPORT_QUALIFIED_TVF_CALLS.contains(ctx.dialect()) || parameters == null)



        )) {
            Schema mappedSchema = Tools.getMappedSchema(ctx, getSchema());

            if (mappedSchema != null && !"".equals(mappedSchema.getName())) {
                ctx.visit(mappedSchema);
                ctx.sql('.');
            }
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
        return new TableImpl<>(rename, getSchema());
    }

    public Table<R> rename(Name rename) {
        return new TableImpl<>(rename, getSchema());
    }

    /**
     * Subclasses must override this method if they use the generic type
     * parameter <R> for other types than {@link Record}
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

    // ------------------------------------------------------------------------
    // XXX: Object API
    // ------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (this == that)
            return true;

        // [#2144] TableImpl equality can be decided without executing the
        // rather expensive implementation of AbstractQueryPart.equals()
        if (that instanceof TableImpl) {
            TableImpl<?> other = (TableImpl<?>) that;
            return

                // [#7172] [#10274] Cannot use getQualifiedName() yet here
                StringUtils.equals(
                    defaultIfNull(getSchema(), DEFAULT_SCHEMA),
                    defaultIfNull(other.getSchema(), DEFAULT_SCHEMA)
                ) &&
                StringUtils.equals(getName(), other.getName()) &&
                Arrays.equals(parameters, other.parameters);
        }

        return super.equals(that);
    }
}
