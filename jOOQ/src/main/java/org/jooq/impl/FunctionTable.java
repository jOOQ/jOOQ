/*
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
