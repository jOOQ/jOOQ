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
 */
package org.jooq.impl;

import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.exception.SQLDialectNotSupportedException;

/**
 * @author Lukas Eder
 */
final class FunctionTable<R extends Record> extends AbstractTable<R> {

    /**
     * Generated UID
     */
    private static final long    serialVersionUID = 2380426377794577041L;

    private final Field<?>       function;

    FunctionTable(Field<?> function) {
        super("function_table");

        this.function = function;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Class<? extends R> getRecordType() {
        return (Class<? extends R>) RecordImpl.class;
    }

    @Override
    public final Table<R> as(String as) {
        return new TableAlias<R>(new FunctionTable<R>(function), as);
    }

    @Override
    public final Table<R> as(String as, String... fieldAliases) {
        return new TableAlias<R>(new FunctionTable<R>(function), as, fieldAliases);
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.configuration().dialect()) {
            case HSQLDB: {
                ctx.keyword("table(").visit(function).sql(')');
                break;
            }

            // [#4254] This is required to enable using PostgreSQL functions
            // with defaulted parameters.
            case POSTGRES: {
                ctx.visit(function);
                break;
            }

            default:
                throw new SQLDialectNotSupportedException("FUNCTION TABLE is not supported for " + ctx.configuration().dialect());
        }
    }

    @Override
    final Fields<R> fields0() {
        return new Fields<R>();
    }
}
