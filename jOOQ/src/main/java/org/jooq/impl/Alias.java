/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is triple-licensed under ASL 2.0, AGPL 3.0, and jOOQ EULA
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   ASL 2.0 or jOOQ EULA.
 * - If you're using this work with at least one commercial database, you may
 *   choose AGPL 3.0 or jOOQ EULA.
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
 * AGPL 3.0
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 *
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details: http://www.jooq.org/eula
 */

package org.jooq.impl;

import static java.util.Arrays.asList;
import static org.jooq.Clause.FIELD;
import static org.jooq.Clause.FIELD_ALIAS;
import static org.jooq.Clause.FIELD_REFERENCE;
import static org.jooq.Clause.TABLE;
import static org.jooq.Clause.TABLE_ALIAS;
import static org.jooq.Clause.TABLE_REFERENCE;
import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.ORACLE;
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.SQLDialect.SQLSERVER;
import static org.jooq.SQLDialect.SYBASE;
import static org.jooq.impl.DSL.falseCondition;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.Utils.list;

import org.jooq.BindContext;
import org.jooq.Clause;
import org.jooq.Context;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.RenderContext;
import org.jooq.SQLDialect;
import org.jooq.Select;
import org.jooq.Table;

/**
 * @author Lukas Eder
 */
class Alias<Q extends QueryPart> extends AbstractQueryPart {

    private static final long     serialVersionUID        = -2456848365524191614L;
    private static final Clause[] CLAUSES_TABLE_REFERENCE = { TABLE, TABLE_REFERENCE };
    private static final Clause[] CLAUSES_TABLE_ALIAS     = { TABLE, TABLE_ALIAS };
    private static final Clause[] CLAUSES_FIELD_REFERENCE = { FIELD, FIELD_REFERENCE };
    private static final Clause[] CLAUSES_FIELD_ALIAS     = { FIELD, FIELD_ALIAS };

    private final Q               wrapped;
    private final String          alias;
    private final String[]        fieldAliases;
    private final boolean         wrapInParentheses;

    Alias(Q wrapped, String alias) {
        this(wrapped, alias, null, false);
    }

    Alias(Q wrapped, String alias, boolean wrapInParentheses) {
        this(wrapped, alias, null, wrapInParentheses);
    }

    Alias(Q wrapped, String alias, String[] fieldAliases) {
        this(wrapped, alias, fieldAliases, false);
    }

    Alias(Q wrapped, String alias, String[] fieldAliases, boolean wrapInParentheses) {
        this.wrapped = wrapped;
        this.alias = alias;
        this.fieldAliases = fieldAliases;
        this.wrapInParentheses = wrapInParentheses;
    }

    final Q wrapped() {
        return wrapped;
    }

    @Override
    public final void toSQL(RenderContext context) {
        if (context.declareFields() || context.declareTables()) {
            SQLDialect dialect = context.configuration().dialect();
            boolean simulateDerivedColumnList = false;

            // [#1801] Some databases don't allow "derived column names" in
            // "simple class specifications". Hence, wrap the table reference in
            // a subselect
            if (fieldAliases != null && asList(CUBRID, FIREBIRD, SQLSERVER, SYBASE).contains(dialect.family()) && wrapped instanceof TableImpl) {

                @SuppressWarnings("unchecked")
                Select<Record> select =
                    select(list(field("*"))).from(((Table<?>) wrapped).as(alias));

                context.sql("(").formatIndentStart().formatNewLine()
                       .visit(select).formatIndentEnd().formatNewLine()
                       .sql(")");
            }

            // [#1801] Some databases do not support "derived column names".
            // They can be simulated by concatenating a dummy SELECT with no
            // results using UNION ALL
            else if (fieldAliases != null && asList(H2, MARIADB, MYSQL, ORACLE, SQLITE).contains(dialect.family())) {
                simulateDerivedColumnList = true;

                SelectFieldList fields = new SelectFieldList();
                for (String fieldAlias : fieldAliases) {
                    fields.add(field("null").as(fieldAlias));
                }

                Select<Record> select =
                    select(fields).where(falseCondition()).unionAll(
                    select(field("*")).from(((Table<?>) wrapped).as(alias)));

                context.sql("(").formatIndentStart().formatNewLine()
                       .visit(select).formatIndentEnd().formatNewLine()
                       .sql(")");
            }

            // The default behaviour
            else {
                toSQLWrapped(context);
            }

            // [#291] some aliases cause trouble, if they are not explicitly marked using "as"
            toSQLAs(context);

            context.sql(" ");
            context.literal(alias);

            // [#1801] Add field aliases to the table alias, if applicable
            if (fieldAliases != null && !simulateDerivedColumnList) {
                toSQLDerivedColumnList(context);
            }

            else {
                // [#756] If the aliased object is an anonymous table (usually an
                // unnested array), then field names must be part of the alias
                // declaration. For example:
                //
                // SELECT t.column_value FROM UNNEST(ARRAY[1, 2]) AS t(column_value)

                // TODO: Is this still needed?
                switch (dialect) {
                    case HSQLDB:
                    case POSTGRES: {
                        // The javac compiler doesn't like casting of generics
                        Object o = wrapped;

                        if (context.declareTables() && o instanceof ArrayTable) {
                            ArrayTable table = (ArrayTable) o;

                            context.sql("(");
                            Utils.fieldNames(context, table.fields());
                            context.sql(")");
                        }

                        break;
                    }
                }
            }
        }
        else {
            context.literal(alias);
        }
    }

    private void toSQLAs(RenderContext context) {
        if (asList(DERBY, HSQLDB, MARIADB, MYSQL, POSTGRES).contains(context.configuration().dialect())) {
            context.sql(" ").keyword("as");
        }
    }

    private void toSQLWrapped(RenderContext context) {
        context.sql(wrapInParentheses ? "(" : "")
               .visit(wrapped)
               .sql(wrapInParentheses ? ")" : "");
    }

    private void toSQLDerivedColumnList(RenderContext context) {
        String separator = "";

        context.sql("(");

        for (int i = 0; i < fieldAliases.length; i++) {
            context.sql(separator);
            context.literal(fieldAliases[i]);

            separator = ", ";
        }

        context.sql(")");
    }

    @Override
    public final void bind(BindContext context) {
        if (context.declareFields() || context.declareTables()) {
            context.visit(wrapped);
        }
        else {
            // Don't bind any values
        }
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        if (ctx.declareFields() || ctx.declareTables()) {
            if (wrapped instanceof Table)
                return CLAUSES_TABLE_ALIAS;
            else
                return CLAUSES_FIELD_ALIAS;
        }
        else {
            if (wrapped instanceof Table)
                return CLAUSES_TABLE_REFERENCE;
            else
                return CLAUSES_FIELD_REFERENCE;
        }
    }

    @Override
    public final boolean declaresFields() {
        return true;
    }

    @Override
    public final boolean declaresTables() {
        return true;
    }
}
