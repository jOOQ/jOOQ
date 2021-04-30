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

import static org.jooq.impl.DSL.*;
import static org.jooq.impl.Internal.*;
import static org.jooq.impl.Keywords.*;
import static org.jooq.impl.Names.*;
import static org.jooq.impl.SQLDataType.*;
import static org.jooq.impl.Tools.*;
import static org.jooq.impl.Tools.BooleanDataKey.*;
import static org.jooq.impl.Tools.DataExtendedKey.*;
import static org.jooq.impl.Tools.DataKey.*;
import static org.jooq.SQLDialect.*;

import org.jooq.*;
import org.jooq.Record;
import org.jooq.conf.*;
import org.jooq.impl.*;
import org.jooq.tools.*;

import java.util.*;


/**
 * The <code>CREATE INDEX</code> statement.
 */
@SuppressWarnings({ "hiding", "rawtypes", "unused" })
final class CreateIndexImpl
extends
    AbstractDDLQuery
implements
    CreateIndexStep,
    CreateIndexIncludeStep,
    CreateIndexWhereStep,
    CreateIndexFinalStep
{

    private final Boolean                             unique;
    private final Index                               index;
    private final boolean                             createIndexIfNotExists;
    private       Table<?>                            table;
    private       Collection<? extends OrderField<?>> on;
    private       Collection<? extends Field<?>>      include;
    private       Condition                           where;
    private       boolean                             excludeNullKeys;

    CreateIndexImpl(
        Configuration configuration,
        Boolean unique,
        Index index,
        boolean createIndexIfNotExists
    ) {
        this(
            configuration,
            unique,
            index,
            createIndexIfNotExists,
            null,
            null,
            null,
            null,
            false
        );
    }

    CreateIndexImpl(
        Configuration configuration,
        Boolean unique,
        boolean createIndexIfNotExists
    ) {
        this(
            configuration,
            unique,
            null,
            createIndexIfNotExists
        );
    }

    CreateIndexImpl(
        Configuration configuration,
        Boolean unique,
        Index index,
        boolean createIndexIfNotExists,
        Table<?> table,
        Collection<? extends OrderField<?>> on,
        Collection<? extends Field<?>> include,
        Condition where,
        boolean excludeNullKeys
    ) {
        super(configuration);

        this.unique = unique;
        this.index = index;
        this.createIndexIfNotExists = createIndexIfNotExists;
        this.table = table;
        this.on = on;
        this.include = include;
        this.where = where;
        this.excludeNullKeys = excludeNullKeys;
    }

    final Boolean                             $unique()                 { return unique; }
    final Index                               $index()                  { return index; }
    final boolean                             $createIndexIfNotExists() { return createIndexIfNotExists; }
    final Table<?>                            $table()                  { return table; }
    final Collection<? extends OrderField<?>> $on()                     { return on; }
    final Collection<? extends Field<?>>      $include()                { return include; }
    final Condition                           $where()                  { return where; }
    final boolean                             $excludeNullKeys()        { return excludeNullKeys; }

    // -------------------------------------------------------------------------
    // XXX: DSL API
    // -------------------------------------------------------------------------

    @Override
    public final CreateIndexImpl on(String table, String... on) {
        return on(DSL.table(DSL.name(table)), Tools.fieldsByName(on));
    }

    @Override
    public final CreateIndexImpl on(Name table, Name... on) {
        return on(DSL.table(table), Tools.fieldsByName(on));
    }

    @Override
    public final CreateIndexImpl on(Table<?> table, OrderField<?>... on) {
        return on(table, Arrays.asList(on));
    }

    @Override
    public final CreateIndexImpl on(String table, Collection<? extends String> on) {
        return on(DSL.table(DSL.name(table)), Tools.fieldsByName(on.toArray(EMPTY_STRING)));
    }

    @Override
    public final CreateIndexImpl on(Name table, Collection<? extends Name> on) {
        return on(DSL.table(table), Tools.fieldsByName(on.toArray(EMPTY_NAME)));
    }

    @Override
    public final CreateIndexImpl on(Table<?> table, Collection<? extends OrderField<?>> on) {
        this.table = table;
        this.on = on;
        return this;
    }

    @Override
    public final CreateIndexImpl include(String... include) {
        return include(Tools.fieldsByName(include));
    }

    @Override
    public final CreateIndexImpl include(Name... include) {
        return include(Tools.fieldsByName(include));
    }

    @Override
    public final CreateIndexImpl include(Field<?>... include) {
        return include(Arrays.asList(include));
    }

    @Override
    public final CreateIndexImpl include(Collection<? extends Field<?>> include) {
        this.include = include;
        return this;
    }

    @Override
    public final CreateIndexImpl where(Field<Boolean> where) {
        return where(DSL.condition(where));
    }

    @Override
    public final CreateIndexImpl where(Condition... where) {
        return where(DSL.condition(Operator.AND, where));
    }

    @Override
    public final CreateIndexImpl where(Collection<? extends Condition> where) {
        return where(DSL.condition(Operator.AND, where));
    }

    @Override
    public final CreateIndexImpl where(Condition where) {
        this.where = where;
        return this;
    }

    @Override
    public final CreateIndexImpl where(String where, QueryPart... parts) {
        return where(DSL.condition(where, parts));
    }

    @Override
    public final CreateIndexImpl where(String where, Object... bindings) {
        return where(DSL.condition(where, bindings));
    }

    @Override
    public final CreateIndexImpl where(String where) {
        return where(DSL.condition(where));
    }

    @Override
    public final CreateIndexImpl where(SQL where) {
        return where(DSL.condition(where));
    }

    @Override
    public final CreateIndexImpl excludeNullKeys() {
        this.excludeNullKeys = true;
        return this;
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    private static final Clause[]        CLAUSES                  = { Clause.CREATE_INDEX };
    private static final Set<SQLDialect> NO_SUPPORT_IF_NOT_EXISTS = SQLDialect.supportedBy(DERBY, FIREBIRD);
    private static final Set<SQLDialect> SUPPORT_UNNAMED_INDEX    = SQLDialect.supportedBy(POSTGRES);
    private static final Set<SQLDialect> SUPPORT_INCLUDE          = SQLDialect.supportedBy(POSTGRES);
    private static final Set<SQLDialect> SUPPORT_UNIQUE_INCLUDE   = SQLDialect.supportedBy(POSTGRES);

    private final boolean supportsIfNotExists(Context<?> ctx) {
        return !NO_SUPPORT_IF_NOT_EXISTS.contains(ctx.dialect());
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (createIndexIfNotExists && !supportsIfNotExists(ctx))
            tryCatch(ctx, DDLStatementType.CREATE_INDEX, c -> accept0(c));
        else
            accept0(ctx);
    }

    private final void accept0(Context<?> ctx) {
        ctx.visit(K_CREATE);

        if (unique)
            ctx.sql(' ')
               .visit(K_UNIQUE);

        ctx.sql(' ')
           .visit(K_INDEX)
           .sql(' ');

        if (createIndexIfNotExists && supportsIfNotExists(ctx))
            ctx.visit(K_IF_NOT_EXISTS)
               .sql(' ');

        if (index != null)
            ctx.visit(index)
               .sql(' ');
        else if (!SUPPORT_UNNAMED_INDEX.contains(ctx.dialect()))
            ctx.visit(generatedName())
               .sql(' ');

        boolean supportsInclude = unique
            ? SUPPORT_UNIQUE_INCLUDE.contains(ctx.dialect())
            : SUPPORT_INCLUDE.contains(ctx.dialect());
        boolean supportsFieldsBeforeTable = false ;

        QueryPartList<QueryPart> list = new QueryPartList<>().qualify(false);
        list.addAll(on);

        // [#11284] Don't emulate the clause for UNIQUE indexes
        if (!supportsInclude && !unique && include != null)
            list.addAll(include);






        ctx.visit(K_ON)
           .sql(' ')
           .visit(table);




            ctx.sql('(').visit(list).sql(')');

        if (supportsInclude && include != null) {
            Keyword keyword = K_INCLUDE;






            ctx.formatSeparator()
               .visit(keyword)
               .sql(" (")
               .visit(QueryPartCollectionView.wrap(include).qualify(false))
               .sql(')');
        }

        Condition condition;

        if (excludeNullKeys && where == null)
            condition = on.size() == 1
                ? field(Tools.first(on)).isNotNull()
                : row(Tools.fields(on)).isNotNull();
        else
            condition = where;

        if (condition != null && ctx.configuration().data("org.jooq.ddl.ignore-storage-clauses") == null)
            ctx.formatSeparator()
               .visit(K_WHERE)
               .sql(' ')
               .qualify(false, c -> c.visit(condition));





    }

    private final Name generatedName() {
        Name t = table.getQualifiedName();

        StringBuilder sb = new StringBuilder(table.getName());
        for (OrderField<?> f : on)
            sb.append('_').append(Tools.field(f).getName());
        sb.append("_idx");

        if (t.qualified())
            return t.qualifier().append(sb.toString());
        else
            return name(sb.toString());
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }


}
