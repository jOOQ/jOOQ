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

import static org.jooq.Clause.UPDATE;
import static org.jooq.Clause.UPDATE_FROM;
import static org.jooq.Clause.UPDATE_RETURNING;
import static org.jooq.Clause.UPDATE_SET;
import static org.jooq.Clause.UPDATE_SET_ASSIGNMENT;
import static org.jooq.Clause.UPDATE_UPDATE;
import static org.jooq.Clause.UPDATE_WHERE;
import static org.jooq.SQLDialect.*;
import static org.jooq.conf.SettingsTools.getExecuteUpdateWithoutWhere;
import static org.jooq.impl.ConditionProviderImpl.extractCondition;
import static org.jooq.impl.DSL.insertInto;
import static org.jooq.impl.DSL.mergeInto;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.noCondition;
import static org.jooq.impl.DSL.noField;
import static org.jooq.impl.DSL.row;
// ...
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectFrom;
import static org.jooq.impl.DSL.trueCondition;
import static org.jooq.impl.DeleteQueryImpl.keyFieldsCondition;
import static org.jooq.impl.DeleteQueryImpl.mergeUsing;
import static org.jooq.impl.DeleteQueryImpl.traverseJoinsAndAddPathConditions;
import static org.jooq.impl.InlineDerivedTable.hasInlineDerivedTables;
import static org.jooq.impl.InlineDerivedTable.transformInlineDerivedTables;
import static org.jooq.impl.InlineDerivedTable.transformInlineDerivedTables0;
import static org.jooq.impl.Keywords.K_AND;
import static org.jooq.impl.Keywords.K_FROM;
import static org.jooq.impl.Keywords.K_ORDER_BY;
import static org.jooq.impl.Keywords.K_SET;
import static org.jooq.impl.Keywords.K_UPDATE;
import static org.jooq.impl.Keywords.K_WHERE;
import static org.jooq.impl.SQLDataType.INTEGER;
import static org.jooq.impl.SelectQueryImpl.addPathConditions;
import static org.jooq.impl.SelectQueryImpl.prependPathJoins;
import static org.jooq.impl.Tools.anyMatch;
import static org.jooq.impl.Tools.containsDeclaredTable;
import static org.jooq.impl.Tools.findAny;
import static org.jooq.impl.Tools.traverseJoins;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_UNQUALIFY_LOCAL_SCOPE;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import org.jooq.Clause;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.FieldOrRow;
import org.jooq.FieldOrRowOrSelect;
import org.jooq.Operator;
import org.jooq.OrderField;
// ...
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record10;
import org.jooq.Record11;
import org.jooq.Record12;
import org.jooq.Record13;
import org.jooq.Record14;
import org.jooq.Record15;
import org.jooq.Record16;
import org.jooq.Record17;
import org.jooq.Record18;
import org.jooq.Record19;
import org.jooq.Record2;
import org.jooq.Record20;
import org.jooq.Record21;
import org.jooq.Record22;
import org.jooq.Record3;
import org.jooq.Record4;
import org.jooq.Record5;
import org.jooq.Record6;
import org.jooq.Record7;
import org.jooq.Record8;
import org.jooq.Record9;
// ...
import org.jooq.Row;
import org.jooq.Row1;
import org.jooq.Row10;
import org.jooq.Row11;
import org.jooq.Row12;
import org.jooq.Row13;
import org.jooq.Row14;
import org.jooq.Row15;
import org.jooq.Row16;
import org.jooq.Row17;
import org.jooq.Row18;
import org.jooq.Row19;
import org.jooq.Row2;
import org.jooq.Row20;
import org.jooq.Row21;
import org.jooq.Row22;
import org.jooq.Row3;
import org.jooq.Row4;
import org.jooq.Row5;
import org.jooq.Row6;
import org.jooq.Row7;
import org.jooq.Row8;
import org.jooq.Row9;
import org.jooq.RowN;
import org.jooq.SQLDialect;
import org.jooq.Scope;
import org.jooq.Select;
import org.jooq.SortField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableLike;
// ...
import org.jooq.UpdateQuery;
import org.jooq.impl.DeleteQueryImpl.MergeUsing;
import org.jooq.impl.FieldMapForUpdate.SetClause;
import org.jooq.impl.QOM.UnmodifiableList;
import org.jooq.impl.QOM.UnmodifiableMap;
import org.jooq.impl.QOM.Update;

/**
 * @author Lukas Eder
 */
final class UpdateQueryImpl<R extends Record>
extends
    AbstractStoreQuery<R, FieldOrRow, FieldOrRowOrSelect>
implements
    UpdateQuery<R>,
    QOM.Update<R>
{

    private static final Clause[]       CLAUSES                       = { UPDATE };







    static final Set<SQLDialect>        REQUIRES_WHERE                = SQLDialect.supportedBy(CLICKHOUSE);
    static final Set<SQLDialect>        EMULATE_FROM_WITH_MERGE       = SQLDialect.supportedUntil(CUBRID, DERBY, FIREBIRD, H2, HSQLDB);
    static final Set<SQLDialect>        NO_SUPPORT_FROM               = SQLDialect.supportedUntil(CLICKHOUSE, IGNITE, MARIADB, MYSQL, TRINO);
    static final Set<SQLDialect>        EMULATE_RETURNING_WITH_UPSERT = SQLDialect.supportedBy(MARIADB);

    // LIMIT is not supported at all
    static final Set<SQLDialect>        NO_SUPPORT_LIMIT              = SQLDialect.supportedUntil(CLICKHOUSE, CUBRID, DERBY, DUCKDB, H2, HSQLDB, POSTGRES, SQLITE, YUGABYTEDB);

    // LIMIT is supported but not ORDER BY
    static final Set<SQLDialect>        NO_SUPPORT_ORDER_BY_LIMIT     = SQLDialect.supportedBy(IGNITE);
    static final Set<SQLDialect>        NO_SUPPORT_UPDATE_JOIN        = SQLDialect.supportedBy(CLICKHOUSE, CUBRID, DERBY, DUCKDB, FIREBIRD, H2, HSQLDB, IGNITE, POSTGRES, SQLITE, YUGABYTEDB);
    // https://github.com/ClickHouse/ClickHouse/issues/61020
    static final Set<SQLDialect>        NO_SUPPORT_QUALIFY_IN_WHERE   = SQLDialect.supportedBy(CLICKHOUSE);

    private final FieldMapForUpdate     updateMap;
    private final TableList             from;
    private final ConditionProviderImpl condition;
    private final SortFieldList         orderBy;
    private Field<? extends Number>     limit;

    UpdateQueryImpl(Configuration configuration, WithImpl with, Table<R> table) {
        super(configuration, with, table);

        this.updateMap = new FieldMapForUpdate(table, SetClause.UPDATE, UPDATE_SET_ASSIGNMENT);
        this.from = new TableList();
        this.condition = new ConditionProviderImpl();
        this.orderBy = new SortFieldList();
    }

    @Override
    protected final FieldMapForUpdate getValues() {
        return updateMap;
    }



    @Override
    public final void addValues(RowN row, RowN value) {
        addValues0(row, value);
    }

    @Override
    public final <T1> void addValues(Row1<T1> row, Row1<T1> value) {
        addValues0(row, value);
    }

    @Override
    public final <T1, T2> void addValues(Row2<T1, T2> row, Row2<T1, T2> value) {
        addValues0(row, value);
    }

    @Override
    public final <T1, T2, T3> void addValues(Row3<T1, T2, T3> row, Row3<T1, T2, T3> value) {
        addValues0(row, value);
    }

    @Override
    public final <T1, T2, T3, T4> void addValues(Row4<T1, T2, T3, T4> row, Row4<T1, T2, T3, T4> value) {
        addValues0(row, value);
    }

    @Override
    public final <T1, T2, T3, T4, T5> void addValues(Row5<T1, T2, T3, T4, T5> row, Row5<T1, T2, T3, T4, T5> value) {
        addValues0(row, value);
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6> void addValues(Row6<T1, T2, T3, T4, T5, T6> row, Row6<T1, T2, T3, T4, T5, T6> value) {
        addValues0(row, value);
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7> void addValues(Row7<T1, T2, T3, T4, T5, T6, T7> row, Row7<T1, T2, T3, T4, T5, T6, T7> value) {
        addValues0(row, value);
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8> void addValues(Row8<T1, T2, T3, T4, T5, T6, T7, T8> row, Row8<T1, T2, T3, T4, T5, T6, T7, T8> value) {
        addValues0(row, value);
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9> void addValues(Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> row, Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> value) {
        addValues0(row, value);
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> void addValues(Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> row, Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> value) {
        addValues0(row, value);
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> void addValues(Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> row, Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> value) {
        addValues0(row, value);
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> void addValues(Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> row, Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> value) {
        addValues0(row, value);
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> void addValues(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> row, Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> value) {
        addValues0(row, value);
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> void addValues(Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> row, Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> value) {
        addValues0(row, value);
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> void addValues(Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> row, Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> value) {
        addValues0(row, value);
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> void addValues(Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> row, Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> value) {
        addValues0(row, value);
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> void addValues(Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> row, Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> value) {
        addValues0(row, value);
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> void addValues(Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> row, Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> value) {
        addValues0(row, value);
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> void addValues(Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> row, Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> value) {
        addValues0(row, value);
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> void addValues(Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> row, Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> value) {
        addValues0(row, value);
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> void addValues(Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> row, Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> value) {
        addValues0(row, value);
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> void addValues(Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> row, Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> value) {
        addValues0(row, value);
    }

    @Override
    public final void addValues(RowN row, Select<? extends Record> select) {
        addValues0(row, select);
    }

    @Override
    public final <T1> void addValues(Row1<T1> row, Select<? extends Record1<T1>> select) {
        addValues0(row, select);
    }

    @Override
    public final <T1, T2> void addValues(Row2<T1, T2> row, Select<? extends Record2<T1, T2>> select) {
        addValues0(row, select);
    }

    @Override
    public final <T1, T2, T3> void addValues(Row3<T1, T2, T3> row, Select<? extends Record3<T1, T2, T3>> select) {
        addValues0(row, select);
    }

    @Override
    public final <T1, T2, T3, T4> void addValues(Row4<T1, T2, T3, T4> row, Select<? extends Record4<T1, T2, T3, T4>> select) {
        addValues0(row, select);
    }

    @Override
    public final <T1, T2, T3, T4, T5> void addValues(Row5<T1, T2, T3, T4, T5> row, Select<? extends Record5<T1, T2, T3, T4, T5>> select) {
        addValues0(row, select);
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6> void addValues(Row6<T1, T2, T3, T4, T5, T6> row, Select<? extends Record6<T1, T2, T3, T4, T5, T6>> select) {
        addValues0(row, select);
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7> void addValues(Row7<T1, T2, T3, T4, T5, T6, T7> row, Select<? extends Record7<T1, T2, T3, T4, T5, T6, T7>> select) {
        addValues0(row, select);
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8> void addValues(Row8<T1, T2, T3, T4, T5, T6, T7, T8> row, Select<? extends Record8<T1, T2, T3, T4, T5, T6, T7, T8>> select) {
        addValues0(row, select);
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9> void addValues(Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> row, Select<? extends Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> select) {
        addValues0(row, select);
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> void addValues(Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> row, Select<? extends Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>> select) {
        addValues0(row, select);
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> void addValues(Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> row, Select<? extends Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>> select) {
        addValues0(row, select);
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> void addValues(Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> row, Select<? extends Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>> select) {
        addValues0(row, select);
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> void addValues(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> row, Select<? extends Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> select) {
        addValues0(row, select);
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> void addValues(Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> row, Select<? extends Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>> select) {
        addValues0(row, select);
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> void addValues(Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> row, Select<? extends Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>> select) {
        addValues0(row, select);
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> void addValues(Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> row, Select<? extends Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>> select) {
        addValues0(row, select);
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> void addValues(Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> row, Select<? extends Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>> select) {
        addValues0(row, select);
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> void addValues(Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> row, Select<? extends Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>> select) {
        addValues0(row, select);
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> void addValues(Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> row, Select<? extends Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>> select) {
        addValues0(row, select);
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> void addValues(Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> row, Select<? extends Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>> select) {
        addValues0(row, select);
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> void addValues(Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> row, Select<? extends Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>> select) {
        addValues0(row, select);
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> void addValues(Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> row, Select<? extends Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>> select) {
        addValues0(row, select);
    }



    @SuppressWarnings("rawtypes")
    final void addValues0(Row row, Row value) {
        updateMap.put(row, ((AbstractRow) value).convertTo(row));
    }

    final void addValues0(Row row, Select<?> select) {
        updateMap.put(row, select);
    }

    @Override
    public final void addValues(Map<?, ?> map) {
        updateMap.set(map);
    }

    @Override
    public final void addFrom(Collection<? extends TableLike<?>> f) {
        for (TableLike<?> provider : f)
            from.add(provider.asTable());
    }

    @Override
    public final void addFrom(TableLike<?> f) {
        addFrom(Arrays.asList(f));
    }

    @Override
    public final void addFrom(TableLike<?>... f) {
        addFrom(Arrays.asList(f));
    }

    @Override
    public final void addConditions(Collection<? extends Condition> conditions) {
        condition.addConditions(conditions);
    }

    @Override
    public final void addConditions(Condition conditions) {
        condition.addConditions(conditions);
    }

    @Override
    public final void addConditions(Condition... conditions) {
        condition.addConditions(conditions);
    }

    @Override
    public final void addConditions(Operator operator, Condition conditions) {
        condition.addConditions(operator, conditions);
    }

    @Override
    public final void addConditions(Operator operator, Condition... conditions) {
        condition.addConditions(operator, conditions);
    }

    @Override
    public final void addConditions(Operator operator, Collection<? extends Condition> conditions) {
        condition.addConditions(operator, conditions);
    }

    @Override
    public final void addOrderBy(OrderField<?>... fields) {
        addOrderBy(Arrays.asList(fields));
    }

    @Override
    public final void addOrderBy(Collection<? extends OrderField<?>> fields) {
        orderBy.addAll(Tools.sortFields(fields));
    }

    @Override
    public final void addLimit(Number l) {
        addLimit(DSL.val(l));
    }

    @Override
    public final void addLimit(Field<? extends Number> l) {
        if (l instanceof NoField)
            return;

        limit = l;
    }

    final Condition getWhere() {
        return condition.getWhere();
    }

    final boolean hasWhere() {
        return condition.hasWhere();
    }

    final TableList getFrom() {
        return from;
    }

    @Override
    public final void accept(Context<?> ctx) {

        // [#15506] Transform the statement if UDT paths have to be emulated
        FieldMapForUpdate e = updateMap.emulateUDTPaths(ctx);
        if (e != null) {
            ctx.visit($set(e));
            return;
        }

        ctx.scopeStart(this);

        // [#2682] [#15632] Apply inline derived tables to the target table
        // [#15632] TODO: Refactor this logic with DeleteQueryImpl
        Table<?> t = table(ctx);
        if (hasInlineDerivedTables(ctx, t) || hasInlineDerivedTables(ctx, from)) {
            ConditionProviderImpl where = new ConditionProviderImpl();
            TableList f = transformInlineDerivedTables(ctx, from, where);

            copy(
                d -> {
                    if (f != from) {
                        d.from.clear();
                        d.from.addAll(f);
                    }

                    if (where.hasWhere())
                        d.addConditionsForInlineDerivedTable(where.getWhere());
                },
                transformInlineDerivedTables0(ctx, t, where, false)
            ).accept0(ctx);
        }
        else
            accept0(ctx);

        ctx.scopeEnd();
    }

    private final void addConditionsForInlineDerivedTable(Condition c) {
        addConditions(c);






    }

    @Override
    final void accept1(Context<?> ctx) {
        if ((!from.isEmpty() || table(ctx) instanceof JoinTable) && EMULATE_FROM_WITH_MERGE.contains(ctx.dialect())) {
            acceptFromAsMerge(ctx);
            return;
        }
        else if (!returning.isEmpty()
                && EMULATE_RETURNING_WITH_UPSERT.contains(ctx.dialect())
                && table instanceof TableImpl

                // [#15582] This implementation should be done only for plain SQL templates
                //          or generated code with at least one known unique key
                && (((TableImpl<?>) table).fields.fields.length == 0 || !table.getKeys().isEmpty())) {
            acceptReturningAsUpsert(ctx);
            return;
        }












        accept2(ctx);
    }

    private final void acceptReturningAsUpsert(Context<?> ctx) {
        ctx.visit(
            insertInto(table)
            .select(
                selectFrom(table)
                .where(hasWhere() ? getWhere() : noCondition())
                .orderBy(orderBy)
                .limit(limit != null ? limit : noField(INTEGER)))
            .onDuplicateKeyUpdate()
            .set(updateMap)
            .returning(returning)
        );
    }

    private final void acceptFromAsMerge(Context<?> ctx) {
        // TODO: What about RETURNING?
        // TODO: What if there are multiple FROM tables?
        // TODO: What if there are SET ROW = ROW assignment(s)?
        // TODO: What if there are SET ROW = (SELECT ..) assignment(s)?

        ConditionProviderImpl c = new ConditionProviderImpl(condition);
        Table<?> t = table(ctx);
        TableList from0 = new TableList(from);
        t = emulateUpdateJoin(t, from0, c);
        FieldMapForUpdate um = updateMap;
        MergeUsing mu = mergeUsing(from0, t, c, orderBy, limit);

        if (!mu.lookup().isEmpty() && ctx.configuration().requireCommercial(() -> "The UPDATE .. FROM to MERGE transformation requires commercial only logic for non-trivial FROM clauses. Please upgrade to the jOOQ Professional Edition or jOOQ Enterprise Edition")) {















        }

        ctx.visit(mergeInto(t).using(mu.table()).on(mu.lookup().isEmpty() ? c : keyFieldsCondition(ctx, t, mu)).whenMatchedThenUpdate().set(um));
    }

    final boolean updatesField(Field<?> field) {
        return anyMatch(updateMap.keySet(), fr -> field.equals(fr) || fr instanceof Row && ((Row) fr).field(field) != null);
    }

    final boolean updatesAnyField(Collection<? extends Field<?>> fields) {
        return anyMatch(fields, this::updatesField);
    }

    final UpdateQueryImpl<R> copy(Consumer<? super UpdateQueryImpl<R>> finisher) {
        return copy(finisher, table);
    }

    final <O extends Record> UpdateQueryImpl<O> copy(Consumer<? super UpdateQueryImpl<O>> finisher, Table<O> t) {
        UpdateQueryImpl<O> u = new UpdateQueryImpl<>(configuration(), with, t);

        if (!returning.isEmpty())
            u.setReturning(returning);

        u.updateMap.putAll(updateMap);
        u.from.addAll(from);
        u.condition.setWhere(condition.getWhere());
        u.orderBy.addAll(orderBy);
        u.limit = limit;
        finisher.accept(u);
        return u;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    final void accept2(Context<?> ctx) {
        boolean declareTables = ctx.declareTables();

        Table<?> t = table(ctx);
        ctx.scopeRegister(t, true);

        ConditionProviderImpl where0 = new ConditionProviderImpl();
        TableList f = getFrom(ctx, where0);
        Table<?> t0 = t;

        // [#16732] Emulate UPDATE .. FROM with UPDATE .. JOIN where possible
        if (!f.isEmpty() && NO_SUPPORT_FROM.contains(ctx.dialect()) && !NO_SUPPORT_UPDATE_JOIN.contains(ctx.dialect())) {
            for (Table<?> x : f)
                t0 = t0.crossJoin(x);
        }

        // [#11158] Emulate UPDATE .. JOIN with UPDATE .. FROM where possible
        else if (NO_SUPPORT_UPDATE_JOIN.contains(ctx.dialect()) && !NO_SUPPORT_FROM.contains(ctx.dialect())) {
            t0 = emulateUpdateJoin(t0, f, where0);
        }

        ctx.start(UPDATE_UPDATE)
           .visit(K_UPDATE)
           .sql(' ')

           // [#4314] Not all SQL dialects support declaring aliased tables in
           // UPDATE statements
           .declareTables(
               true




           )
           .visit(t0)
           .declareTables(declareTables)
           .end(UPDATE_UPDATE);







        ctx.formatSeparator()
           .start(UPDATE_SET)
           .visit(K_SET)
           .separatorRequired(true)
           .formatIndentStart()
           .formatSeparator()
           .visit(updateMap)
           .formatIndentEnd()
           .end(UPDATE_SET);







        acceptFrom(ctx, where0, f);

        limitEmulation:
        if (limitEmulation(ctx)) {









            // [#16632] Push down USING table list here
            TableList t1 = new TableList();
            if (!containsDeclaredTable(from, t))
                t1.add(t);
            t1.addAll(from);

            Field<?>[] keyFields = DeleteQueryImpl.keyFields(ctx, t);

            if (keyFields.length == 1)
                where0.addConditions(keyFields[0].in(select((Field) keyFields[0]).from(t1).where(getWhere()).orderBy(orderBy).limit(limit)));
            else
                where0.addConditions(row(keyFields).in(select(keyFields).from(t1).where(getWhere()).orderBy(orderBy).limit(limit)));
        }

        // [#16733] Repeat the WHERE clause in case we have a non-empty FROM clause
        if (hasWhere() && (from.isEmpty() || supportFromOrUpdateJoin(ctx)))
            where0.addConditions(getWhere());
        else if (!where0.hasWhere() && REQUIRES_WHERE.contains(ctx.dialect()))
            where0.addConditions(trueCondition());

        ctx.start(UPDATE_WHERE);

        if (where0.hasWhere()) {
            boolean noQualifyInWhere = NO_SUPPORT_QUALIFY_IN_WHERE.contains(ctx.dialect());

            if (noQualifyInWhere)
                ctx.data(DATA_UNQUALIFY_LOCAL_SCOPE, true);

            ctx.formatSeparator()
               .visit(K_WHERE).sql(' ').visit(where0.getWhere());

            if (noQualifyInWhere)
                ctx.data(DATA_UNQUALIFY_LOCAL_SCOPE, false);
        }

        ctx.end(UPDATE_WHERE);

        if (!limitEmulation(ctx)) {
            if (!orderBy.isEmpty())
                ctx.formatSeparator()
                   .visit(K_ORDER_BY).sql(' ')
                   .visit(orderBy);

            DeleteQueryImpl.acceptLimit(ctx, limit);
        }

        ctx.start(UPDATE_RETURNING);
        toSQLReturning(ctx);
        ctx.end(UPDATE_RETURNING);
    }

    private final Table<?> emulateUpdateJoin(Table<?> t0, TableList from0, ConditionProviderImpl where0) {
        if (t0 instanceof CrossJoin j) {
            t0 = j.$table1();
            from0.add(j.$table2());
        }
        else if (t0 instanceof Join j) {
            t0 = j.$table1();
            from0.add(j.$table2());
            where0.addConditions(((JoinTable<?>) j).condition());
        }

        // [#11158] TODO: We could also emulate LeftSemiJoin and LeftAntiJoin here, though it's unlikely anyone does that.

        return t0;
    }

    private final boolean limitEmulation(Context<?> ctx) {
        if (limit != null && NO_SUPPORT_LIMIT.contains(ctx.dialect()))
            return true;

        if (!orderBy.isEmpty() && NO_SUPPORT_ORDER_BY_LIMIT.contains(ctx.dialect()))
            return true;

        if (!from.isEmpty() && !supportFromOrUpdateJoin(ctx))
            return true;

        return false;
    }

    private final TableList getFrom(Context<?> ctx, ConditionProviderImpl where0) {
        TableList f = new TableList();

        // [#16634] Prevent unnecessary FROM clause in some dialects, e.g. HANA
        if (supportFromOrUpdateJoin(ctx)) {










            f.addAll(from);








            if (!f.isEmpty())
                f = traverseJoinsAndAddPathConditions(ctx, where0, f);
        }

        return f;
    }

    private final boolean supportFromOrUpdateJoin(Context<?> ctx) {
        return !NO_SUPPORT_FROM.contains(ctx.dialect()) || !NO_SUPPORT_UPDATE_JOIN.contains(ctx.dialect());
    }

    private final void acceptFrom(Context<?> ctx, ConditionProviderImpl where0, TableList f) {
        ctx.start(UPDATE_FROM);

        // [#16634] Prevent unnecessary FROM clause in some dialects, e.g. HANA
        if (!NO_SUPPORT_FROM.contains(ctx.dialect())) {
            if (!f.isEmpty())
                ctx.formatSeparator()
                   .visit(K_FROM).sql(' ')
                   .declareTables(true, c -> c.visit(f));
        }

        ctx.end(UPDATE_FROM);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }

    @Override
    public final boolean isExecutable() {

        // [#6771] Take action when UPDATE query has no WHERE clause
        if (!condition.hasWhere())
            executeWithoutWhere("UPDATE without WHERE", getExecuteUpdateWithoutWhere(configuration().settings()));

        return updateMap.size() > 0;
    }

    @Override
    final int estimatedRowCount(Scope ctx) {
        return Integer.MAX_VALUE;
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final WithImpl $with() {
        return with;
    }

    @Override
    public final Table<R> $table() {
        return table;
    }

    @Override
    public final Update<?> $table(Table<?> newTable) {
        if ($table() == newTable)
            return this;
        else
            return copy(d -> {}, newTable);
    }

    @Override
    public final UnmodifiableList<? extends Table<?>> $from() {
        return QOM.unmodifiable(from);
    }

    @Override
    public final Update<R> $from(Collection<? extends Table<?>> newFrom) {
        return copy(d -> {
            d.from.clear();
            d.from.addAll(newFrom);
        });
    }

    @Override
    public final UnmodifiableMap<? extends FieldOrRow, ? extends FieldOrRowOrSelect> $set() {
        return QOM.unmodifiable(updateMap);
    }

    @Override
    public final Update<R> $set(Map<? extends FieldOrRow, ? extends FieldOrRowOrSelect> newSet) {
        return copy(u -> {
            u.updateMap.clear();
            u.updateMap.putAll(newSet);
        });
    }

    @Override
    public final Condition $where() {
        return condition.getWhereOrNull();
    }

    @Override
    public final Update<R> $where(Condition newWhere) {
        if ($where() == newWhere)
            return this;
        else
            return copy(u -> u.condition.setWhere(newWhere));
    }

    @Override
    public final UnmodifiableList<? extends SortField<?>> $orderBy() {
        return QOM.unmodifiable(orderBy);
    }

    @Override
    public final Update<R> $orderBy(Collection<? extends SortField<?>> newOrderBy) {
        return copy(u -> {
            u.orderBy.clear();
            u.orderBy.addAll(newOrderBy);
        });
    }

    @Override
    public final Field<? extends Number> $limit() {
        return limit;
    }

    @Override
    public final Update<R> $limit(Field<? extends Number> newLimit) {
        if ($limit() == newLimit)
            return this;
        else
            return copy(s -> s.limit = newLimit);
    }















































}
