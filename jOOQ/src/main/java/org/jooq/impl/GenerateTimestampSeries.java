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

import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.table;

import java.sql.Timestamp;

import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.QueryPart;
import org.jooq.Record1;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.exception.SQLDialectNotSupportedException;
import org.jooq.types.Interval;

/**
 * @author Laurent Pireyn
 */
class GenerateTimestampSeries extends AbstractTable<Record1<Timestamp>> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 899509143932230909L;

    private final Field<Timestamp> from;
    private final Field<Timestamp> to;
    private final Field<? extends Interval> interval;

    GenerateTimestampSeries(Field<Timestamp> from, Field<Timestamp> to, Field<? extends Interval> interval) {
        super("generate_series");

        this.from = from;
        this.to = to;
        this.interval = interval;
    }

    @Override
    public final void accept(Context<?> ctx) {
        ctx.visit(delegate(ctx.configuration()));
    }

    private QueryPart delegate(Configuration configuration) {
        final SQLDialect dialect = configuration.family();
        switch (dialect) {
            case POSTGRES:
                return table("{generate_series}({0}, {1}, {2})", from, to, interval);

            default:
                throw new SQLDialectNotSupportedException("The " + getName() + " function with timestamps is not supported in this dialect");
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public final Class<? extends Record1<Timestamp>> getRecordType() {
        return RecordImpl.class;
    }

    @Override
    public final Table<Record1<Timestamp>> as(String alias) {
        return new TableAlias<>(this, alias);
    }

    @Override
    public final Table<Record1<Timestamp>> as(String alias, String... fieldAliases) {
        return new TableAlias<>(this, alias, fieldAliases);
    }

    @Override
    final Fields<Record1<Timestamp>> fields0() {
        return new Fields<>(DSL.field(name("generate_series"), Integer.class));
    }
}
