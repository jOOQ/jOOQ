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
// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.DERBY;
// ...
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.IGNITE;
// ...
// ...
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.YUGABYTEDB;
import static org.jooq.conf.WriteIfReadonly.IGNORE;
import static org.jooq.conf.WriteIfReadonly.THROW;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.when;
import static org.jooq.impl.Keywords.K_ROW;
import static org.jooq.impl.Tools.anyMatch;
import static org.jooq.impl.Tools.apply;
import static org.jooq.impl.Tools.collect;
import static org.jooq.impl.Tools.fieldName;
import static org.jooq.impl.Tools.filter;
import static org.jooq.impl.Tools.flattenEntrySet;
import static org.jooq.impl.Tools.map;
import static org.jooq.impl.Tools.row0;
import static org.jooq.impl.Tools.unqualified;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_FORCE_LIMIT_WITH_ORDER_BY;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_STORE_ASSIGNMENT;
import static org.jooq.impl.Tools.SimpleDataKey.DATA_ON_DUPLICATE_KEY_WHERE;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.jooq.Clause;
import org.jooq.Condition;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.FieldOrRow;
import org.jooq.FieldOrRowOrSelect;
import org.jooq.GeneratorStatementType;
// ...
import org.jooq.QueryPart;
import org.jooq.RenderContext.CastMode;
import org.jooq.Row;
import org.jooq.SQLDialect;
import org.jooq.Select;
import org.jooq.Table;
import org.jooq.exception.DataTypeException;
import org.jooq.impl.QOM.UNotYetImplemented;

/**
 * @author Lukas Eder
 */
final class FieldMapForUpdate
extends
    AbstractQueryPartMap<FieldOrRow, FieldOrRowOrSelect>
{

    static final Set<SQLDialect> CASTS_NEEDED                      = SQLDialect.supportedBy(POSTGRES, YUGABYTEDB);
    static final Set<SQLDialect> NO_SUPPORT_QUALIFY                = SQLDialect.supportedBy(POSTGRES, SQLITE, YUGABYTEDB);
    static final Set<SQLDialect> EMULATE_RVE_SET_QUERY             = SQLDialect.supportedBy(CUBRID, DERBY, FIREBIRD, IGNITE, MARIADB, MYSQL, SQLITE);








    static final Set<SQLDialect> NO_SUPPORT_RVE_SET                = SQLDialect.supportedBy(CUBRID, DERBY, FIREBIRD, IGNITE, MARIADB, MYSQL, SQLITE);
    static final Set<SQLDialect> NO_SUPPORT_RVE_SET_IN_MERGE       = SQLDialect.supportedBy(CUBRID, DERBY, FIREBIRD, IGNITE, MARIADB, MYSQL, SQLITE);
    static final Set<SQLDialect> REQUIRE_RVE_ROW_CLAUSE            = SQLDialect.supportedBy(POSTGRES, YUGABYTEDB);

    final Table<?>               table;
    final SetClause              setClause;
    final Clause                 assignmentClause;

    FieldMapForUpdate(FieldMapForUpdate um, SetClause setClause) {
        this(um.table, setClause, um.assignmentClause);

        putAll(um);
    }

    FieldMapForUpdate(Table<?> table, SetClause setClause, Clause assignmentClause) {
        this.table = table;
        this.setClause = setClause;
        this.assignmentClause = assignmentClause;
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (size() > 0) {
            String separator = "";

            // [#989] Some dialects do not support qualified column references
            // in the UPDATE statement's SET clause

            // [#2055] Other dialects require qualified column references to
            // disambiguated columns in queries like
            // UPDATE t1 JOIN t2 .. SET t1.val = ..., t2.val = ...
            boolean supportsQualify = !NO_SUPPORT_QUALIFY.contains(ctx.dialect()) && ctx.qualify();

            // [#2823] [#10034] Few dialects need bind value casts for UPDATE .. SET
            //                  Some regressions have been observed e.g. in PostgreSQL with JSON types, so let's be careful.
            CastMode previous = ctx.castMode();
            if (!CASTS_NEEDED.contains(ctx.dialect()))
                ctx.castMode(CastMode.NEVER);

            entryLoop:
            for (Entry<FieldOrRow, FieldOrRowOrSelect> entry : flattenEntrySet(entrySet(), true)) {
                FieldOrRow key = entry.getKey();
                FieldOrRowOrSelect value = entry.getValue();














                separator = acceptAssignmentClause(ctx, supportsQualify, key, value, separator);
            }

            if (!CASTS_NEEDED.contains(ctx.dialect()))
                ctx.castMode(previous);
        }
        else
            ctx.sql("[ no fields are updated ]");
    }

    @SuppressWarnings("unchecked")
    private final String acceptAssignmentClause(
        Context<?> ctx,
        boolean supportsQualify,
        FieldOrRow key,
        FieldOrRowOrSelect value,
        String separator
    ) {
        if (!"".equals(separator))
            ctx.sql(separator)
               .formatSeparator();

        if (assignmentClause != null)
            ctx.start(assignmentClause);

        // A multi-row update was specified
        if (key instanceof Row multiRow) {
            Row multiValue = value instanceof Row ? (Row) value : null;
            Select<?> multiSelect = value instanceof Select ? (Select<?>) value : null;

            // [#6884] This syntax can be emulated trivially, if the RHS is not a SELECT subquery
            if (multiValue != null
                && (NO_SUPPORT_RVE_SET.contains(ctx.dialect())
                        || (NO_SUPPORT_RVE_SET_IN_MERGE.contains(ctx.dialect()) && setClause == SetClause.MERGE))
            ) {
                FieldMapForUpdate map = new FieldMapForUpdate(table(), setClause, null);

                for (int i = 0; i < multiRow.size(); i++) {
                    Field<?> k = multiRow.field(i);
                    Field<?> v = multiValue.field(i);

                    map.put(k, Tools.field(v, k));
                }

                ctx.visit(map);
            }



















            // [#10523] Generic SET ROW = (SELECT ..) emulation that works
            //          everywhere, but inefficiently duplicates the subquery
            else if (multiSelect != null
                && (EMULATE_RVE_SET_QUERY.contains(ctx.dialect())
                        || (NO_SUPPORT_RVE_SET_IN_MERGE.contains(ctx.dialect()) && setClause == SetClause.MERGE))
            ) {
                Row row = removeReadonly(ctx, multiRow);
                int size = row.size();
                Select<?> select;






                select = multiSelect;

                // [#10523] Simplify special case
                if (size == 1) {
                    acceptStoreAssignment(ctx, false, row.field(0));
                    visitSubquery(ctx, select);
                }
                else {
                    for (int i = 0; i < size; i++) {
                        FieldMapForUpdate mu = new FieldMapForUpdate(table, setClause, null);
                        separator = mu.acceptAssignmentClause(ctx,
                            supportsQualify,
                            row.field(i),
                            new ProjectSingleScalarSubquery<>(select, i),
                            separator
                        );
                    }
                }
            }
            else {
                Row row = removeReadonly(ctx, multiRow);
                acceptStoreAssignment(ctx, false, row);

                if (multiValue != null) {






                    {

                        // [#6763] Incompatible change in PostgreSQL 10 requires ROW() constructor for
                        //         single-degree rows. Let's just always render it, here.
                        if (REQUIRE_RVE_ROW_CLAUSE.contains(ctx.dialect()))
                            ctx.visit(K_ROW).sql(" ");

                        ctx.visit(removeReadonly(ctx, multiRow, multiValue));
                    }
                }

                // Subselects or subselect emulations of row value expressions
                else if (multiSelect != null) {







                    visitSubquery(ctx, multiSelect);
                }
            }
        }

        // A regular (non-multi-row) update was specified
        else {
            acceptStoreAssignment(ctx, supportsQualify, key);

            // [#8479] Emulate WHERE clause using CASE
            Condition condition = (Condition) ctx.data(DATA_ON_DUPLICATE_KEY_WHERE);
            if (condition != null)
                ctx.visit(when(condition, (Field) value).else_(key));




            else
                ctx.visit(value);
        }

        if (assignmentClause != null)
            ctx.end(assignmentClause);

        return ",";
    }

    private static final void acceptStoreAssignment(Context<?> ctx, boolean qualify, QueryPart target) {
        ctx.qualify(qualify, c1 -> c1.data(DATA_STORE_ASSIGNMENT, true, c2 -> c2.visit(target)))
           .sql(" = ");
    }

    private static final void visitSubquery(Context<?> ctx, Select<?> select) {





        Tools.visitSubquery(ctx, select);
    }

    static final Row removeReadonly(Context<?> ctx, Row row) {
        return removeReadonly(ctx, row, row);
    }

    static final Row removeReadonly(Context<?> ctx, Row checkRow, Row removeRow) {




        return removeRow;
    }

    final void set(Map<?, ?> map) {
        map.forEach((k, v) -> {
            if (k instanceof Row r) {
                put(r, (FieldOrRowOrSelect) v);
            }
            else {
                Field<?> field = Tools.tableField(table, k);
                put(field, Tools.field(v, field));
            }
        });
    }

    @Override
    public FieldOrRowOrSelect put(FieldOrRow key, FieldOrRowOrSelect value) {
        if (key instanceof NoField || value instanceof NoField)
            return null;
        else
            return super.put(key, value);
    }






















































































































    enum SetClause {
        UPDATE,
        INSERT,
        MERGE
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    final Function<? super Map<FieldOrRow, FieldOrRowOrSelect>, ? extends AbstractQueryPartMap<FieldOrRow, FieldOrRowOrSelect>> $construct() {
        return m -> {
            FieldMapForUpdate r = new FieldMapForUpdate(table, setClause, assignmentClause);
            r.putAll(m);
            return r;
        };
    }
}
