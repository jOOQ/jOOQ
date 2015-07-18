/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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

import static org.jooq.impl.DSL.level;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.DSL.table;

import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.QueryPart;
import org.jooq.Record1;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.exception.SQLDialectNotSupportedException;

/**
 * @author Lukas Eder
 */
class GenerateSeries extends AbstractTable<Record1<Integer>> {

    /**
     * Generated UID
     */
    private static final long    serialVersionUID = 2385574114457239818L;

    private final Field<Integer> from;
    private final Field<Integer> to;
    private final Field<Integer> step;

    GenerateSeries(Field<Integer> from, Field<Integer> to) {
        this(from, to, null);
    }

    GenerateSeries(Field<Integer> from, Field<Integer> to, Field<Integer> step) {
        super("generate_series");

        this.from = from;
        this.to = to;
        this.step = step;
    }

    @Override
    public final void accept(Context<?> ctx) {
        ctx.visit(delegate(ctx.configuration()));
    }

    private final QueryPart delegate(Configuration configuration) {
        final SQLDialect dialect = configuration.family();
        switch (dialect) {
            case CUBRID:
            /* [pro] xx
            xxxx xxxxxxx
            xx [/pro] */

                // There is a bug in CUBRID preventing reuse of "level" in the
                // predicate http://jira.cubrid.org/browse/ENGINE-119
                Field<Integer> level = from.add(level()).sub(one());
                return table("({select} {0} {as} {1} {from} {2} {connect by} {level} <= {3})",
                    level,
                    name("generate_series"),
                    new Dual(),
                    to.add(one()).sub(from));

            case POSTGRES:
            default:

                if (step != null) {
                    if (dialect != SQLDialect.POSTGRES) {
                        throw new SQLDialectNotSupportedException("The " + getName() + " function with step is not supported in this dialect");
                    }
                    return table("{generate_series}({0}, {1}, {2})", from, to, step);
                }

                return table("{generate_series}({0}, {1})", from, to);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public final Class<? extends Record1<Integer>> getRecordType() {
        return (Class) RecordImpl.class;
    }

    @Override
    public final Table<Record1<Integer>> as(String alias) {
        return new TableAlias<Record1<Integer>>(this, alias);
    }

    @Override
    public final Table<Record1<Integer>> as(String alias, String... fieldAliases) {
        return new TableAlias<Record1<Integer>>(this, alias, fieldAliases);
    }

    @Override
    final Fields<Record1<Integer>> fields0() {
        return new Fields<Record1<Integer>>(DSL.field(name("generate_series"), Integer.class));
    }
}
