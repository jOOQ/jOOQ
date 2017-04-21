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

import static org.jooq.JoinType.FULL_OUTER_JOIN;
import static org.jooq.JoinType.LEFT_OUTER_JOIN;
import static org.jooq.JoinType.RIGHT_OUTER_JOIN;
import static org.jooq.impl.DSL.table;

import java.util.Collection;

import org.jooq.Field;
import org.jooq.JoinType;
import org.jooq.Name;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.SQL;
import org.jooq.Table;
import org.jooq.TableLike;
import org.jooq.TableOnStep;
import org.jooq.TableOuterJoinStep;

/**
 * @author Lukas Eder
 */
final class PartitionJoinTable implements TableOuterJoinStep<Record> {

    private final Table<?>                       lhs;
    private final Collection<? extends Field<?>> lhsPartitionBy;

    PartitionJoinTable(Table<?> lhs, Collection<? extends Field<?>> lhsPartitionBy) {
        this.lhs = lhs;
        this.lhsPartitionBy = lhsPartitionBy;
    }

    @Override
    public final TableOnStep<Record> join(TableLike<?> table, JoinType type) {
        return new JoinTable(lhs, table, type, lhsPartitionBy);
    }

    @Override
    public final TableOnStep<Record> leftJoin(TableLike<?> table) {
        return leftOuterJoin(table);
    }

    @Override
    public final TableOnStep<Record> leftJoin(SQL sql) {
        return leftOuterJoin(sql);
    }

    @Override
    public final TableOnStep<Record> leftJoin(String sql) {
        return leftOuterJoin(sql);
    }

    @Override
    public final TableOnStep<Record> leftJoin(String sql, Object... bindings) {
        return leftOuterJoin(sql, bindings);
    }

    @Override
    public final TableOnStep<Record> leftJoin(String sql, QueryPart... parts) {
        return leftOuterJoin(sql, parts);
    }

    @Override
    public final TableOnStep<Record> leftJoin(Name name) {
        return leftOuterJoin(name);
    }

    @Override
    public final TableOnStep<Record> leftOuterJoin(TableLike<?> table) {
        return join(table, LEFT_OUTER_JOIN);
    }

    @Override
    public final TableOnStep<Record> leftOuterJoin(SQL sql) {
        return leftOuterJoin(table(sql));
    }

    @Override
    public final TableOnStep<Record> leftOuterJoin(String sql) {
        return leftOuterJoin(table(sql));
    }

    @Override
    public final TableOnStep<Record> leftOuterJoin(String sql, Object... bindings) {
        return leftOuterJoin(table(sql, bindings));
    }

    @Override
    public final TableOnStep<Record> leftOuterJoin(String sql, QueryPart... parts) {
        return leftOuterJoin(table(sql, parts));
    }

    @Override
    public final TableOnStep<Record> leftOuterJoin(Name name) {
        return leftOuterJoin(table(name));
    }

    @Override
    public final TableOnStep<Record> rightJoin(TableLike<?> table) {
        return rightOuterJoin(table);
    }

    @Override
    public final TableOnStep<Record> rightJoin(SQL sql) {
        return rightOuterJoin(sql);
    }

    @Override
    public final TableOnStep<Record> rightJoin(String sql) {
        return rightOuterJoin(sql);
    }

    @Override
    public final TableOnStep<Record> rightJoin(String sql, Object... bindings) {
        return rightOuterJoin(sql, bindings);
    }

    @Override
    public final TableOnStep<Record> rightJoin(String sql, QueryPart... parts) {
        return rightOuterJoin(sql, parts);
    }

    @Override
    public final TableOnStep<Record> rightJoin(Name name) {
        return rightOuterJoin(name);
    }

    @Override
    public final TableOnStep<Record> rightOuterJoin(TableLike<?> table) {
        return join(table, RIGHT_OUTER_JOIN);
    }

    @Override
    public final TableOnStep<Record> rightOuterJoin(SQL sql) {
        return rightOuterJoin(table(sql));
    }

    @Override
    public final TableOnStep<Record> rightOuterJoin(String sql) {
        return rightOuterJoin(table(sql));
    }

    @Override
    public final TableOnStep<Record> rightOuterJoin(String sql, Object... bindings) {
        return rightOuterJoin(table(sql, bindings));
    }

    @Override
    public final TableOnStep<Record> rightOuterJoin(String sql, QueryPart... parts) {
        return rightOuterJoin(table(sql, parts));
    }

    @Override
    public final TableOnStep<Record> rightOuterJoin(Name name) {
        return rightOuterJoin(table(name));
    }

    @Override
    public final TableOnStep<Record> fullJoin(TableLike<?> table) {
        return fullOuterJoin(table);
    }

    @Override
    public final TableOnStep<Record> fullJoin(SQL sql) {
        return fullOuterJoin(sql);
    }

    @Override
    public final TableOnStep<Record> fullJoin(String sql) {
        return fullOuterJoin(sql);
    }

    @Override
    public final TableOnStep<Record> fullJoin(String sql, Object... bindings) {
        return fullOuterJoin(sql, bindings);
    }

    @Override
    public final TableOnStep<Record> fullJoin(String sql, QueryPart... parts) {
        return fullOuterJoin(sql, parts);
    }

    @Override
    public final TableOnStep<Record> fullJoin(Name name) {
        return fullOuterJoin(name);
    }

    @Override
    public final TableOnStep<Record> fullOuterJoin(TableLike<?> table) {
        return join(table, FULL_OUTER_JOIN);
    }

    @Override
    public final TableOnStep<Record> fullOuterJoin(SQL sql) {
        return fullOuterJoin(table(sql));
    }

    @Override
    public final TableOnStep<Record> fullOuterJoin(String sql) {
        return fullOuterJoin(table(sql));
    }

    @Override
    public final TableOnStep<Record> fullOuterJoin(String sql, Object... bindings) {
        return fullOuterJoin(table(sql, bindings));
    }

    @Override
    public final TableOnStep<Record> fullOuterJoin(String sql, QueryPart... parts) {
        return fullOuterJoin(table(sql, parts));
    }

    @Override
    public final TableOnStep<Record> fullOuterJoin(Name name) {
        return fullOuterJoin(table(name));
    }
}
