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

import static org.jooq.Clause.TABLE_VALUES;

import org.jooq.Context;
import org.jooq.Record;
import org.jooq.Row;
import org.jooq.Select;
import org.jooq.Table;

/**
 * An implementation for the <code>VALUES(...)</code> table constructor
 *
 * @author Lukas Eder
 */
final class Values<R extends Record> extends AbstractTable<R> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -637982217747670311L;

    private final Row[]       rows;

    Values(Row[] rows) {
        super("values");

        this.rows = assertNotEmpty(rows);
    }

    static Row[] assertNotEmpty(Row[] rows) {
        if (rows == null || rows.length == 0)
            throw new IllegalArgumentException("Cannot create a VALUES() constructor with an empty set of rows");

        return rows;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Class<? extends R> getRecordType() {
        return (Class<? extends R>) RecordImpl.class;
    }

    @Override
    public final Table<R> as(String alias) {
        return new TableAlias<R>(this, alias, true);
    }

    @Override
    public final Table<R> as(String alias, String... fieldAliases) {
        return new TableAlias<R>(this, alias, fieldAliases, true);
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {

            // [#915] Emulate VALUES(..) with SELECT .. UNION ALL SELECT ..
            // for those dialects that do not support a VALUES() constructor










            case FIREBIRD:
            case MARIADB:
            case MYSQL:
            case SQLITE: {
                Select<Record> selects = null;
                boolean subquery = ctx.subquery();

                for (Row row : rows) {
                    Select<Record> select = create().select(row.fields());

                    if (selects == null) {
                        selects = select;
                    }
                    else {
                        selects = selects.unionAll(select);
                    }
                }

                ctx.formatIndentStart()
                   .formatNewLine()
                   .subquery(true)
                   .visit(selects)
                   .subquery(subquery)
                   .formatIndentEnd()
                   .formatNewLine();
                break;
            }

            // [#915] Native support of VALUES(..)
            case CUBRID:
            case DERBY:
            case H2:
            case HSQLDB:
            case POSTGRES:






            default: {
                ctx.start(TABLE_VALUES)
                   .keyword("values")
                   .formatIndentLockStart();

                boolean firstRow = true;
                for (Row row : rows) {
                    if (!firstRow) {
                        ctx.sql(',').formatSeparator();
                    }

                    ctx.visit(row);
                    firstRow = false;
                }

                ctx.formatIndentLockEnd()
                   .end(TABLE_VALUES);
                break;
            }
        }
    }

    @Override
    final Fields<R> fields0() {
        return new Fields<R>(rows[0].fields());
    }
}
