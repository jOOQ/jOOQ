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

// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.DERBY;
// ...
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.HSQLDB;
// ...
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.YUGABYTEDB;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.DSL.rowNumber;
// ...
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.Keywords.K_ORDINALITY;
import static org.jooq.impl.Keywords.K_WITH;
import static org.jooq.impl.Names.N_OFFSET;
import static org.jooq.impl.Names.N_ORDINAL;
import static org.jooq.impl.SQLDataType.BIGINT;
import static org.jooq.impl.SubqueryCharacteristics.DERIVED_TABLE;
// ...
import static org.jooq.impl.Tools.visitSubquery;

import java.util.List;
import java.util.Set;

import org.jooq.Context;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
// ...
import org.jooq.QueryPart;
import org.jooq.Record;
// ...
import org.jooq.SQLDialect;
import org.jooq.Select;
import org.jooq.Table;
// ...
import org.jooq.impl.QOM.Aliasable;

/**
 * @author Lukas Eder
 */
final class OrdinalityTable<R extends Record>
extends
    AbstractTable<R>
implements
    AutoAlias<Table<R>>,
    QOM.OrdinalityTable<R>
{

    static final Set<SQLDialect> NO_SUPPORT_STANDARD          = SQLDialect.supportedBy(DERBY, FIREBIRD, MARIADB, MYSQL, SQLITE);
    static final Set<SQLDialect> NO_SUPPORT_TVF               = SQLDialect.supportedBy(H2, HSQLDB);
    static final Set<SQLDialect> NO_SUPPORT_TABLE_EXPRESSIONS = SQLDialect.supportedBy(POSTGRES, YUGABYTEDB);

    static {
        NO_SUPPORT_TVF.addAll(NO_SUPPORT_STANDARD);
        NO_SUPPORT_TABLE_EXPRESSIONS.addAll(NO_SUPPORT_TVF);
    }

    final AbstractTable<?>       delegate;

    OrdinalityTable(AbstractTable<?> delegate) {
        super(delegate.getOptions(), delegate.getQualifiedName(), delegate.getSchema());

        this.delegate = delegate;
    }

    // -------------------------------------------------------------------------
    // XXX: Table API
    // -------------------------------------------------------------------------

    // [#5799] TODO: Maybe share some logic with AbstractDelegatingTable

    @Override
    public final boolean declaresTables() {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Class<? extends R> getRecordType() {
        // TODO: [#4695] Calculate the correct Record[B] type
        return (Class<? extends R>) RecordImplN.class;
    }

    @Override
    public final List<ForeignKey<R, ?>> getReferences() {
        return (List) delegate.getReferences();
    }

    @Override
    final FieldsImpl<R> fields0() {
        FieldsImpl<R> r = new FieldsImpl<>(delegate.fields0().fields);
        r.add(DSL.field(N_ORDINAL, BIGINT));
        return r;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Table<R> autoAlias(Context<?> ctx, Table<R> t) {
        if (t != this && t instanceof AutoAlias<?> a) {
            return ((AutoAlias<Table<R>>) a).autoAlias(ctx, t);
        }
        else if (t instanceof Aliasable<?> a) {
            Name alias = a.$alias();
            if (alias == null)
                alias = ((Table<?>) a.$aliased()).getUnqualifiedName();

            Field<?>[] fields = t.fields();
            if (Tools.isEmpty(fields))
                return t.as(alias);
            else
                return t.as(table(alias), fields);
        }
        else
            return null;
    }

    // ------------------------------------------------------------------------
    // XXX: QueryPart API
    // ------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        Select<?> s;

        if (delegate instanceof ArrayTable || delegate instanceof ArrayOfValues) {
            if (NO_SUPPORT_STANDARD.contains(ctx.dialect()))
                acceptEmulation(ctx);
            else
                acceptStandard(ctx);
        }
        else if (delegate instanceof FunctionTable && NO_SUPPORT_TVF.contains(ctx.dialect())) {
            if (NO_SUPPORT_TVF.contains(ctx.dialect()))
                acceptEmulation(ctx);
            else
                acceptStandard(ctx);
        }
        else if (delegate instanceof TableImpl && ((TableImpl<?>) delegate).parameters != null) {
            if (NO_SUPPORT_TVF.contains(ctx.dialect()))
                acceptEmulation(ctx);
            else
                acceptStandard(ctx);
        }
        else if (NO_SUPPORT_TABLE_EXPRESSIONS.contains(ctx.dialect()))
            acceptEmulation(ctx);








        else
            acceptStandard(ctx);
    }

    private final void acceptStandard(Context<?> ctx) {
        ctx.visit(delegate).sql(' ').visit(K_WITH).sql(' ').visit(K_ORDINALITY);
    }

    private final void acceptEmulation(Context<?> ctx) {
        Select<?> s;

        switch (ctx.family()) {






            default:
                s = select(delegate.fields()).select(rowNumber().over().as(N_ORDINAL)).from(delegate);
                break;
        }

        visitSubquery(ctx, s, DERIVED_TABLE, true);
    }












    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Table<?> $table() {
        return delegate;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final OrdinalityTable<?> $table(Table<?> newTable) {
        return new OrdinalityTable<>((AbstractTable<Record>) newTable);
    }



















    @Override
    public final Table<R> $aliased() {
        return new OrdinalityTable<>((AbstractTable<?>) ((Aliasable<? extends Table<?>>) delegate).$aliased());
    }

    @Override
    public final Name $alias() {
        return ((Aliasable<? extends Table<?>>) delegate).$alias();
    }
}
