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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
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

import static java.lang.Boolean.TRUE;
import static org.jooq.Clause.FIELD;
import static org.jooq.Clause.FIELD_ALIAS;
import static org.jooq.Clause.FIELD_REFERENCE;
import static org.jooq.Clause.TABLE;
import static org.jooq.Clause.TABLE_ALIAS;
import static org.jooq.Clause.TABLE_REFERENCE;
// ...
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.CLICKHOUSE;
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.DUCKDB;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.IGNITE;
// ...
// ...
// ...
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.SQLDialect.MYSQL;
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
// ...
import static org.jooq.SQLDialect.YUGABYTEDB;
import static org.jooq.impl.DSL.asterisk;
import static org.jooq.impl.DSL.falseCondition;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.Keywords.K_AS;
import static org.jooq.impl.NoAutoAlias.noAutoAlias;
import static org.jooq.impl.QueryPartListView.wrap;
import static org.jooq.impl.SubqueryCharacteristics.DERIVED_TABLE;
import static org.jooq.impl.Tools.EMPTY_NAME;
import static org.jooq.impl.Tools.combine;
import static org.jooq.impl.Tools.isNotEmpty;
import static org.jooq.impl.Tools.map;
import static org.jooq.impl.Tools.visitSubquery;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_AS_REQUIRED;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_UNALIAS_ALIASED_EXPRESSIONS;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_WRAP_DERIVED_TABLES_IN_PARENTHESES;
import static org.jooq.impl.Tools.SimpleDataKey.DATA_SELECT_ALIASES;
import static org.jooq.impl.Values.NO_SUPPORT_VALUES;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

import org.jooq.Clause;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Name.Quoted;
// ...
import org.jooq.QueryPart;
import org.jooq.SQLDialect;
import org.jooq.Select;
import org.jooq.Table;
import org.jooq.conf.RenderOptionalKeyword;
import org.jooq.impl.QOM.UEmpty;
import org.jooq.impl.Tools.BooleanDataKey;

/**
 * @author Lukas Eder
 */
final class Alias<Q extends QueryPart> extends AbstractQueryPart implements UEmpty, SimpleCheckQueryPart {

    private static final Clause[] CLAUSES_TABLE_REFERENCE               = { TABLE, TABLE_REFERENCE };
    private static final Clause[] CLAUSES_TABLE_ALIAS                   = { TABLE, TABLE_ALIAS };
    private static final Clause[] CLAUSES_FIELD_REFERENCE               = { FIELD, FIELD_REFERENCE };
    private static final Clause[] CLAUSES_FIELD_ALIAS                   = { FIELD, FIELD_ALIAS };
    static final Set<SQLDialect>  NO_SUPPORT_ALIASED_JOIN_TABLES        = SQLDialect.supportedBy(DERBY, DUCKDB, FIREBIRD, MARIADB, MYSQL, SQLITE);
    static final Set<SQLDialect>  SUPPORT_AS_REQUIRED                   = SQLDialect.supportedBy(DERBY, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB);
    static final Set<SQLDialect>  SUPPORT_DERIVED_COLUMN_NAMES_SPECIAL1 = SQLDialect.supportedBy(CUBRID, FIREBIRD, MYSQL);
    static final Set<SQLDialect>  SUPPORT_DERIVED_COLUMN_NAMES_SPECIAL2 = SQLDialect.supportedUntil(CLICKHOUSE, IGNITE, MARIADB, SQLITE);







    final Q                              wrapped;
    final Q                              wrapping;
    final Name                           alias;
    final Name[]                         fieldAliases;
    final boolean                        wrapInParentheses;

    Alias(Q wrapped, Q wrapping, Name alias) {
        this(wrapped, wrapping, alias, null, false);
    }

    Alias(Q wrapped, Q wrapping, Name alias, Name[] fieldAliases, boolean wrapInParentheses) {
        this.wrapped = wrapped;
        this.wrapping = wrapping;
        this.alias = alias;
        this.fieldAliases = fieldAliases;
        this.wrapInParentheses = wrapInParentheses;
    }

    final Q wrapped() {
        return wrapped;
    }

    final boolean hasFieldAliases() {
        return isNotEmpty(fieldAliases);
    }

    @Override
    public final boolean isSimple(Context<?> ctx) {
        return wrapped instanceof Table && !ctx.declareTables()
            || wrapped instanceof Field && !ctx.declareFields();
    }

    @Override
    public final void accept(Context<?> ctx) {













        if (ctx.declareAliases() && (ctx.declareFields() || ctx.declareTables())) {

            // [#14310] Support nesting of alias declaring parts where this
            //          makes sense. The below declareAliases(false) call was
            //          added for cases where x.as("a").as("b") leaks into the
            //          expression tree, to prevent doubly declaring an alias.
            boolean aliasedJoinTable = wrapped instanceof JoinTable;

            if (!aliasedJoinTable)
                ctx.declareAliases(false);













            acceptDeclareAliasStandard(ctx);

            if (!aliasedJoinTable)
                ctx.declareAliases(true);
        }
        else
            ctx.qualify(false, c -> c.visit(alias));
    }

    private final void acceptDeclareAliasTSQL(Context<?> ctx) {
        ctx.visit(alias).sql(" = ");
        toSQLWrapped(ctx);
    }

    private final void acceptDeclareAliasStandard(Context<?> context) {
        if (wrapped instanceof TableImpl)
            context.scopeMarkStart(wrapping);

        SQLDialect dialect = context.dialect();
        SQLDialect family = context.family();
        boolean emulatedDerivedColumnList = false;








        // [#454] [#1801] Some databases don't allow "derived column names" in
        // "simple class specifications", or "common table expression references".
        // Hence, wrap the table reference in a subselect
        if (hasFieldAliases()
                && (SUPPORT_DERIVED_COLUMN_NAMES_SPECIAL1.contains(dialect))
                && (wrapped instanceof TableImpl || wrapped instanceof CommonTableExpressionImpl)) {

            visitSubquery(context, select(asterisk()).from(noAutoAlias((Table<?>) wrapped).as(alias)), DERIVED_TABLE);
        }

        // [#1801] Some databases do not support "derived column names".
        // They can be emulated by concatenating a dummy SELECT with no
        // results using UNION ALL
        else if (hasFieldAliases() && (
                emulatedDerivedColumnList
             || SUPPORT_DERIVED_COLUMN_NAMES_SPECIAL2.contains(dialect)




        )) {

            emulatedDerivedColumnList = true;

            if (wrapped instanceof Values && NO_SUPPORT_VALUES.contains(dialect)) {
                context.data(DATA_SELECT_ALIASES, fieldAliases, t -> toSQLWrapped(t));
            }
            else {

                // [#3156] Do not SELECT * from derived tables to prevent ambiguously defined columns
                // in those derived tables
                Select<?> wrappedAsSelect =
                    wrapped instanceof Select<?> s
                  ? s
                  : wrapped instanceof DerivedTable<?> d
                  ? d.query()
                  : select(asterisk()).from(noAutoAlias((Table<?>) wrapped).as(alias));

                List<Field<?>> select = wrappedAsSelect.getSelect();














                if (emulatedDerivedColumnList) {
                    if (AliasedSelect.avoidAliasPushdown(context, wrappedAsSelect)

                        // [#10521] If the object being aliased does not allow for alias pushdowns
                        //          (e.g. VALUES()), then we must avoid the pushdown and use the
                        //          classic derived column list emulation using UNION ALL
                        || !(wrapped instanceof Select || wrapped instanceof DerivedTable)
                    ) {
                        SelectFieldList<Field<?>> fields = new SelectFieldList<>();
                        for (int i = 0; i < fieldAliases.length; i++) {
                            switch (family) {


















                                default:
                                    fields.add(field("null").as(fieldAliases[i]));
                                    break;
                            }
                        }

                        visitSubquery(context, select(fields).where(falseCondition()).unionAll(wrappedAsSelect), DERIVED_TABLE);
                    }

                    // [#10521] Avoid the clumsy UNION ALL emulation if possible
                    //          by pushing down projection aliases into the
                    //          derived table
                    else
                        context.sql('(').visit(new AliasedSelect<>(wrappedAsSelect, true, false, false, fieldAliases)).sql(')');
                }
            }
        }

        // The default behaviour
        else
            toSQLWrapped(context);

        // [#291] some aliases cause trouble, if they are not explicitly marked using "as"
        toSQLAs(context);

        context.sql(' ')
               .qualify(false, c -> c.visit(alias));

        // [#1801] Add field aliases to the table alias, if applicable
        if (hasFieldAliases() && !emulatedDerivedColumnList) {
            toSQLDerivedColumnList(context);
        }

        else {
            // [#756] If the aliased object is an anonymous table (usually an
            // unnested array), then field names must be part of the alias
            // declaration. For example:
            //
            // SELECT t.column_value FROM UNNEST(ARRAY[1, 2]) AS t(column_value)

            // TODO: Is this still needed?
            switch (family) {


                case HSQLDB:
                case POSTGRES:
                case YUGABYTEDB: {
                    // The javac compiler doesn't like casting of generics
                    Object o = wrapped;

                    if (context.declareTables() && o instanceof ArrayTable)
                        context.sql('(')
                               .visit(wrap(((ArrayTable) o).fields()).qualify(false))
                               .sql(')');

                    break;
                }
            }
        }

        if (wrapped instanceof TableImpl)
            context.scopeMarkEnd(wrapping);
    }

    final void toSQLAs(Context<?> ctx) {

        // [#9925] In some cases, AS is always required, regardless
        //         of the dialect or settings (e.g. XMLATTRIBUTES).
        if (TRUE.equals(ctx.data(DATA_AS_REQUIRED))) {
            ctx.sql(' ').visit(K_AS);
        }
        else if (wrapped instanceof Field) {
            if (ctx.settings().getRenderOptionalAsKeywordForFieldAliases() == RenderOptionalKeyword.DEFAULT && SUPPORT_AS_REQUIRED.contains(ctx.dialect()))
                ctx.sql(' ').visit(K_AS);
            else if (ctx.settings().getRenderOptionalAsKeywordForFieldAliases() == RenderOptionalKeyword.ON)
                ctx.sql(' ').visit(K_AS);
        }
        else {
            if (ctx.settings().getRenderOptionalAsKeywordForTableAliases() == RenderOptionalKeyword.DEFAULT && SUPPORT_AS_REQUIRED.contains(ctx.dialect()))
                ctx.sql(' ').visit(K_AS);







            else if (ctx.settings().getRenderOptionalAsKeywordForTableAliases() == RenderOptionalKeyword.ON)
                ctx.sql(' ').visit(K_AS);
        }
    }

    private final void toSQLWrapped(Context<?> ctx) {
        if (wrapInParentheses)
            ctx.data(DATA_WRAP_DERIVED_TABLES_IN_PARENTHESES, false, c -> toSQLWrapped(c, wrapInParentheses));
        else
            toSQLWrapped(ctx, wrapInParentheses);
    }

    private final void toSQLWrapped(Context<?> ctx, boolean wrap) {
        boolean nestedJoinTable = wrapped instanceof JoinTable;

        if (wrap)
            if (nestedJoinTable)
                ctx.sqlIndentStart('(');
            else
                ctx.sql('(');

        if (nestedJoinTable && NO_SUPPORT_ALIASED_JOIN_TABLES.contains(ctx.dialect())) {
            ctx.visit(select(asterisk()).from((Table<?>) wrapped));
        }













        else
            ctx.visit(wrapped);

        if (wrap)
            if (nestedJoinTable)
                ctx.sqlIndentEnd(')');
            else
                ctx.sql(')');
    }

    private final void toSQLDerivedColumnList(Context<?> ctx) {
        ctx.sql(" (").visit(wrap(fieldAliases)).sql(')');
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

    // -------------------------------------------------------------------------
    // The Object API
    // -------------------------------------------------------------------------

    @Override
    public int hashCode() {
        return alias.hashCode();
    }

    @Override
    public boolean equals(Object that) {
        if (that instanceof Alias o) {

            // [#18939] Historically, we did not compare the wrapped contents.
            return Objects.equals(alias, o.alias)
                && Arrays.equals(fieldAliases, o.fieldAliases);
        }
        else
            return super.equals(that);
    }
}
