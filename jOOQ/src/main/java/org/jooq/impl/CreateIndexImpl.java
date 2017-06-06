/**
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

import static java.util.Arrays.asList;
import static org.jooq.Clause.CREATE_INDEX;
// ...
// ...
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
// ...
// ...
// ...
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.Keywords.K_CREATE;
import static org.jooq.impl.Keywords.K_IF_NOT_EXISTS;
import static org.jooq.impl.Keywords.K_INDEX;
import static org.jooq.impl.Keywords.K_ON;
import static org.jooq.impl.Keywords.K_UNIQUE;
import static org.jooq.impl.Keywords.K_WHERE;
import static org.jooq.impl.Tools.EMPTY_SORTFIELD;

import java.util.Collection;

import org.jooq.Clause;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.CreateIndexStep;
import org.jooq.CreateIndexWhereStep;
import org.jooq.Field;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.OrderField;
import org.jooq.QueryPart;
import org.jooq.SQL;
import org.jooq.SortField;
import org.jooq.Table;

/**
 * @author Lukas Eder
 */
final class CreateIndexImpl extends AbstractQuery implements

    // Cascading interface implementations for CREATE INDEX behaviour
    CreateIndexStep,
    CreateIndexWhereStep {

    /**
     * Generated UID
     */
    private static final long     serialVersionUID = 8904572826501186329L;
    private static final Clause[] CLAUSES          = { CREATE_INDEX };

    private final Index           index;
    private final boolean         unique;
    private final boolean         ifNotExists;
    private Table<?>              table;
    private Field<?>[]            fields;
    private SortField<?>[]        sortFields;
    private Condition             where;

    CreateIndexImpl(Configuration configuration, Index index, boolean unique, boolean ifNotExists) {
        super(configuration);

        this.index = index;
        this.unique = unique;
        this.ifNotExists = ifNotExists;
        this.table = index.getTable();
        this.sortFields = index.getFields().toArray(EMPTY_SORTFIELD);
        this.where = index.getWhere();
    }

    // ------------------------------------------------------------------------
    // XXX: DSL API
    // ------------------------------------------------------------------------

    @Override
    public final CreateIndexImpl on(Table<?> t, OrderField<?>... f) {
        this.table = t;
        this.sortFields = Tools.sortFields(f);

        return this;
    }

    @Override
    public final CreateIndexImpl on(Name tableName, Name... fieldNames) {
        return on(table(tableName), Tools.fieldsByName(fieldNames));
    }

    @Override
    public final CreateIndexImpl on(String tableName, String... fieldNames) {
        return on(table(name(tableName)), Tools.fieldsByName(fieldNames));
    }

    @Override
    public final CreateIndexImpl where(Condition... conditions) {
        where = DSL.and(conditions);
        return this;
    }

    @Override
    public final CreateIndexImpl where(Collection<? extends Condition> conditions) {
        where = DSL.and(conditions);
        return this;
    }

    @Override
    public final CreateIndexImpl where(Field<Boolean> field) {
        return where(DSL.condition(field));
    }

    @Override
    public final CreateIndexImpl where(SQL sql) {
        return where(DSL.condition(sql));
    }

    @Override
    public final CreateIndexImpl where(String sql) {
        return where(DSL.condition(sql));
    }

    @Override
    public final CreateIndexImpl where(String sql, Object... bindings) {
        return where(DSL.condition(sql, bindings));
    }

    @Override
    public final CreateIndexImpl where(String sql, QueryPart... parts) {
        return where(DSL.condition(sql, parts));
    }

    // ------------------------------------------------------------------------
    // XXX: QueryPart API
    // ------------------------------------------------------------------------

    private final boolean supportsIfNotExists(Context<?> ctx) {
        return !asList(DERBY, FIREBIRD).contains(ctx.family());
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (ifNotExists && !supportsIfNotExists(ctx)) {
            Tools.executeImmediateBegin(ctx, DDLStatementType.CREATE_INDEX);
            accept0(ctx);
            Tools.executeImmediateEnd(ctx, DDLStatementType.CREATE_INDEX);
        }
        else {
            accept0(ctx);
        }
    }

    private final void accept0(Context<?> ctx) {
        ctx.visit(K_CREATE);

        if (unique)
            ctx.sql(' ')
               .visit(K_UNIQUE);

        ctx.sql(' ')
           .visit(K_INDEX)
           .sql(' ');

        if (ifNotExists && supportsIfNotExists(ctx))
            ctx.visit(K_IF_NOT_EXISTS)
               .sql(' ');

        ctx.visit(index)
           .sql(' ')
           .visit(K_ON)
           .sql(' ')
           .visit(table)
           .sql('(')
           .qualify(false)
           .visit(fields != null ? new QueryPartList<QueryPart>(fields) : new QueryPartList<QueryPart>(sortFields))
           .qualify(true)
           .sql(')');

        if (where != null)
            ctx.sql(' ')
               .visit(K_WHERE)
               .sql(' ')
               .qualify(false)
               .visit(where)
               .qualify(true);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }
}
