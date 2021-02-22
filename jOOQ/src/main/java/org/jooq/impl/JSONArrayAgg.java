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

// ...
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.impl.DSL.function;
import static org.jooq.impl.DSL.groupConcat;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.noCondition;
import static org.jooq.impl.JSONEntryImpl.jsonCast;
import static org.jooq.impl.JSONNull.JSONNullType.ABSENT_ON_NULL;
import static org.jooq.impl.JSONNull.JSONNullType.NULL_ON_NULL;
import static org.jooq.impl.Names.N_JSONB_AGG;
import static org.jooq.impl.Names.N_JSON_AGG;
import static org.jooq.impl.Names.N_JSON_ARRAYAGG;
import static org.jooq.impl.Names.N_JSON_MERGE;
import static org.jooq.impl.Names.N_JSON_QUOTE;
import static org.jooq.impl.SQLDataType.JSON;
import static org.jooq.impl.SQLDataType.VARCHAR;

import java.util.Collection;
import java.util.Set;

import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.JSONArrayAggOrderByStep;
import org.jooq.OrderField;
import org.jooq.SQLDialect;
import org.jooq.impl.JSONNull.JSONNullType;


/**
 * The JSON array constructor.
 *
 * @author Lukas Eder
 */
final class JSONArrayAgg<J>
extends AbstractAggregateFunction<J>
implements JSONArrayAggOrderByStep<J> {

    /**
     * Generated UID
     */
    private static final long    serialVersionUID          = 1772007627336725780L;
    static final Set<SQLDialect> EMULATE_WITH_GROUP_CONCAT = SQLDialect.supportedBy(MARIADB, MYSQL);

    private JSONNullType         nullType;

    JSONArrayAgg(DataType<J> type, Field<?> arg) {
        super(false, N_JSON_ARRAYAGG, type, arg);
    }

    @Override
    public void accept(Context<?> ctx) {
        switch (ctx.family()) {
            case MARIADB:
            case MYSQL: {
                // Workaround for https://jira.mariadb.org/browse/MDEV-21912,
                // https://jira.mariadb.org/browse/MDEV-21914, and other issues
                ctx.visit(N_JSON_MERGE).sql('(').visit(inline("[]")).sql(", ").visit(groupConcatEmulation(ctx)).sql(')');
                break;
            }













            case POSTGRES:
                ctx.visit(getDataType() == JSON ? N_JSON_AGG : N_JSONB_AGG).sql('(');
                ctx.visit(arguments.get(0));
                acceptOrderBy(ctx);
                ctx.sql(')');

                if (nullType == ABSENT_ON_NULL)
                    acceptFilterClause(ctx, (filter == null ? noCondition() : filter).and(arguments.get(0).isNotNull()));
                else
                    acceptFilterClause(ctx);

                acceptOverClause(ctx);
                break;

            default:
                acceptStandard(ctx);
                break;
        }
    }

    @SuppressWarnings({ "unchecked", "serial" })
    private final Field<?> groupConcatEmulation(Context<?> ctx) {
        Field<?> arg1 = arguments.get(0);

        if (arg1.getDataType().isString()) {
            switch (ctx.family()) {
                case MARIADB:
                case MYSQL:
                    arg1 = function(N_JSON_QUOTE, getDataType(), arg1);
                    break;






            }
        }

        Field<?> arg2 = arg1;
        return DSL.concat(
            inline('['),
            DSL.field("{0}", VARCHAR, new CustomQueryPart() {
                @Override
                public void accept(Context<?> c1) {
                    c1.visit(groupConcatEmulationWithoutArrayWrappers(
                        DSL.field("{0}", VARCHAR, new CustomQueryPart() {
                            @Override
                            public void accept(Context<?> c2) {
                                acceptArguments2(c2, QueryPartListView.<Field<?>>wrap(arg2));
                            }
                        }),
                        withinGroupOrderBy
                    ));

                    acceptFilterClause(c1);
                    acceptOverClause(c1);
                }
            }),
            inline(']')
        );
    }

    static final Field<?> groupConcatEmulationWithoutArrayWrappers(Field<?> field, SortFieldList orderBy) {
        return Tools.isEmpty(orderBy)
             ? groupConcat(field)
             : groupConcat(field).orderBy(orderBy);
    }

    private final void acceptStandard(Context<?> ctx) {
        ctx.visit(N_JSON_ARRAYAGG).sql('(');
        ctx.visit(jsonCast(ctx, arguments.get(0)));
        acceptOrderBy(ctx);

        JSONNull jsonNull = new JSONNull(nullType);
        if (jsonNull.rendersContent(ctx))
            ctx.sql(' ').visit(jsonNull);

        ctx.sql(')');

        acceptFilterClause(ctx);
        acceptOverClause(ctx);
    }

    @Override
    public final JSONArrayAgg<J> nullOnNull() {
        nullType = NULL_ON_NULL;
        return this;
    }

    @Override
    public final JSONArrayAgg<J> absentOnNull() {
        nullType = ABSENT_ON_NULL;
        return this;
    }

    @Override
    public final JSONArrayAgg<J> orderBy(OrderField<?>... fields) {
        return (JSONArrayAgg<J>) super.orderBy(fields);
    }

    @Override
    public final JSONArrayAgg<J> orderBy(Collection<? extends OrderField<?>> fields) {
        return (JSONArrayAgg<J>) super.orderBy(fields);
    }
}
