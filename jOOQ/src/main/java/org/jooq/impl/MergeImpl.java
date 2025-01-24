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

import static java.lang.Boolean.FALSE;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.jooq.Clause.MERGE;
import static org.jooq.Clause.MERGE_MERGE_INTO;
import static org.jooq.Clause.MERGE_ON;
import static org.jooq.Clause.MERGE_SET;
import static org.jooq.Clause.MERGE_SET_ASSIGNMENT;
import static org.jooq.Clause.MERGE_USING;
import static org.jooq.Clause.MERGE_VALUES;
import static org.jooq.Clause.MERGE_WHEN_MATCHED_THEN_UPDATE;
import static org.jooq.Clause.MERGE_WHEN_NOT_MATCHED_THEN_INSERT;
// ...
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.CLICKHOUSE;
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.DUCKDB;
// ...
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.HSQLDB;
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
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
// ...
import static org.jooq.SQLDialect.TRINO;
// ...
import static org.jooq.SQLDialect.YUGABYTEDB;
import static org.jooq.impl.ConditionProviderImpl.extractCondition;
import static org.jooq.impl.DSL.condition;
import static org.jooq.impl.DSL.exists;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.noCondition;
import static org.jooq.impl.DSL.notExists;
import static org.jooq.impl.DSL.trueCondition;
import static org.jooq.impl.DSL.when;
import static org.jooq.impl.Keywords.K_AND;
import static org.jooq.impl.Keywords.K_AS;
import static org.jooq.impl.Keywords.K_BY;
import static org.jooq.impl.Keywords.K_DELETE;
import static org.jooq.impl.Keywords.K_INSERT;
import static org.jooq.impl.Keywords.K_MATCHED;
import static org.jooq.impl.Keywords.K_MERGE_INTO;
import static org.jooq.impl.Keywords.K_NOT;
import static org.jooq.impl.Keywords.K_ON;
import static org.jooq.impl.Keywords.K_SET;
import static org.jooq.impl.Keywords.K_SOURCE;
import static org.jooq.impl.Keywords.K_TARGET;
import static org.jooq.impl.Keywords.K_THEN;
import static org.jooq.impl.Keywords.K_UPDATE;
import static org.jooq.impl.Keywords.K_USING;
import static org.jooq.impl.Keywords.K_VALUES;
import static org.jooq.impl.Keywords.K_WHEN;
import static org.jooq.impl.Keywords.K_WHERE;
import static org.jooq.impl.Tools.EMPTY_FIELD;
import static org.jooq.impl.Tools.concat;
import static org.jooq.impl.Tools.isEmpty;
import static org.jooq.impl.Tools.nullSafe;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_WRAP_DERIVED_TABLES_IN_PARENTHESES;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Consumer;

import org.jooq.Clause;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.FieldOrRow;
import org.jooq.FieldOrRowOrSelect;
import org.jooq.MergeKeyStep1;
import org.jooq.MergeKeyStep10;
import org.jooq.MergeKeyStep11;
import org.jooq.MergeKeyStep12;
import org.jooq.MergeKeyStep13;
import org.jooq.MergeKeyStep14;
import org.jooq.MergeKeyStep15;
import org.jooq.MergeKeyStep16;
import org.jooq.MergeKeyStep17;
import org.jooq.MergeKeyStep18;
import org.jooq.MergeKeyStep19;
import org.jooq.MergeKeyStep2;
import org.jooq.MergeKeyStep20;
import org.jooq.MergeKeyStep21;
import org.jooq.MergeKeyStep22;
import org.jooq.MergeKeyStep3;
import org.jooq.MergeKeyStep4;
import org.jooq.MergeKeyStep5;
import org.jooq.MergeKeyStep6;
import org.jooq.MergeKeyStep7;
import org.jooq.MergeKeyStep8;
import org.jooq.MergeKeyStep9;
import org.jooq.MergeMatchedDeleteStep;
import org.jooq.MergeMatchedSetMoreStep;
import org.jooq.MergeMatchedThenStep;
import org.jooq.MergeNotMatchedSetMoreStep;
import org.jooq.MergeNotMatchedThenStep;
import org.jooq.MergeNotMatchedValuesStep1;
import org.jooq.MergeNotMatchedValuesStep10;
import org.jooq.MergeNotMatchedValuesStep11;
import org.jooq.MergeNotMatchedValuesStep12;
import org.jooq.MergeNotMatchedValuesStep13;
import org.jooq.MergeNotMatchedValuesStep14;
import org.jooq.MergeNotMatchedValuesStep15;
import org.jooq.MergeNotMatchedValuesStep16;
import org.jooq.MergeNotMatchedValuesStep17;
import org.jooq.MergeNotMatchedValuesStep18;
import org.jooq.MergeNotMatchedValuesStep19;
import org.jooq.MergeNotMatchedValuesStep2;
import org.jooq.MergeNotMatchedValuesStep20;
import org.jooq.MergeNotMatchedValuesStep21;
import org.jooq.MergeNotMatchedValuesStep22;
import org.jooq.MergeNotMatchedValuesStep3;
import org.jooq.MergeNotMatchedValuesStep4;
import org.jooq.MergeNotMatchedValuesStep5;
import org.jooq.MergeNotMatchedValuesStep6;
import org.jooq.MergeNotMatchedValuesStep7;
import org.jooq.MergeNotMatchedValuesStep8;
import org.jooq.MergeNotMatchedValuesStep9;
import org.jooq.MergeNotMatchedValuesStepN;
import org.jooq.MergeOnConditionStep;
import org.jooq.MergeOnStep;
import org.jooq.MergeUsingStep;
import org.jooq.Operator;
// ...
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.Record1;
// ...
import org.jooq.Row;
import org.jooq.SQL;
import org.jooq.SQLDialect;
import org.jooq.Select;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.TableLike;
// ...
import org.jooq.impl.FieldMapForUpdate.SetClause;
import org.jooq.impl.QOM.Merge;
import org.jooq.impl.QOM.MergeMatched;
import org.jooq.impl.QOM.MergeNotMatched;
import org.jooq.impl.QOM.MergeNotMatchedBySource;
import org.jooq.impl.QOM.UNotYetImplemented;
import org.jooq.impl.QOM.UnmodifiableList;
import org.jooq.impl.QOM.UnmodifiableMap;
import org.jooq.impl.QOM.With;
import org.jooq.impl.Tools.ExtendedDataKey;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.StringUtils;

/**
 * The SQL standard MERGE statement
 *
 * @author Lukas Eder
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
final class MergeImpl<R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>
extends AbstractRowCountQuery
implements

    // Cascading interface implementations for Merge behaviour
    UNotYetImplemented,
    MergeUsingStep<R>,



    MergeKeyStep1<R, T1>,
    MergeKeyStep2<R, T1, T2>,
    MergeKeyStep3<R, T1, T2, T3>,
    MergeKeyStep4<R, T1, T2, T3, T4>,
    MergeKeyStep5<R, T1, T2, T3, T4, T5>,
    MergeKeyStep6<R, T1, T2, T3, T4, T5, T6>,
    MergeKeyStep7<R, T1, T2, T3, T4, T5, T6, T7>,
    MergeKeyStep8<R, T1, T2, T3, T4, T5, T6, T7, T8>,
    MergeKeyStep9<R, T1, T2, T3, T4, T5, T6, T7, T8, T9>,
    MergeKeyStep10<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>,
    MergeKeyStep11<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>,
    MergeKeyStep12<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>,
    MergeKeyStep13<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>,
    MergeKeyStep14<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>,
    MergeKeyStep15<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>,
    MergeKeyStep16<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>,
    MergeKeyStep17<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>,
    MergeKeyStep18<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>,
    MergeKeyStep19<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>,
    MergeKeyStep20<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>,
    MergeKeyStep21<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>,
    MergeKeyStep22<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>,



    MergeOnStep<R>,
    MergeOnConditionStep<R>,
    MergeMatchedSetMoreStep<R>,
    MergeMatchedThenStep<R>,
    MergeNotMatchedSetMoreStep<R>,
    MergeNotMatchedThenStep<R>,



    MergeNotMatchedValuesStep1<R, T1>,
    MergeNotMatchedValuesStep2<R, T1, T2>,
    MergeNotMatchedValuesStep3<R, T1, T2, T3>,
    MergeNotMatchedValuesStep4<R, T1, T2, T3, T4>,
    MergeNotMatchedValuesStep5<R, T1, T2, T3, T4, T5>,
    MergeNotMatchedValuesStep6<R, T1, T2, T3, T4, T5, T6>,
    MergeNotMatchedValuesStep7<R, T1, T2, T3, T4, T5, T6, T7>,
    MergeNotMatchedValuesStep8<R, T1, T2, T3, T4, T5, T6, T7, T8>,
    MergeNotMatchedValuesStep9<R, T1, T2, T3, T4, T5, T6, T7, T8, T9>,
    MergeNotMatchedValuesStep10<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>,
    MergeNotMatchedValuesStep11<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>,
    MergeNotMatchedValuesStep12<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>,
    MergeNotMatchedValuesStep13<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>,
    MergeNotMatchedValuesStep14<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>,
    MergeNotMatchedValuesStep15<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>,
    MergeNotMatchedValuesStep16<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>,
    MergeNotMatchedValuesStep17<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>,
    MergeNotMatchedValuesStep18<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>,
    MergeNotMatchedValuesStep19<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>,
    MergeNotMatchedValuesStep20<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>,
    MergeNotMatchedValuesStep21<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>,
    MergeNotMatchedValuesStep22<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>,



    MergeNotMatchedValuesStepN<R>,
    QOM.Merge<R>
{

    private static final JooqLogger      log                                     = JooqLogger.getLogger(MergeImpl.class);

    private static final Clause[]        CLAUSES                                 = { MERGE };






    private static final Set<SQLDialect> NO_SUPPORT_MULTI                        = SQLDialect.supportedBy(HSQLDB);
    private static final Set<SQLDialect> NO_SUPPORT_BY_TARGET                    = SQLDialect.supportedUntil(CLICKHOUSE, CUBRID, DERBY, DUCKDB, FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, SQLITE, TRINO, YUGABYTEDB);
    private static final Set<SQLDialect> REQUIRE_NEGATION                        = SQLDialect.supportedBy(H2, HSQLDB);
    private static final Set<SQLDialect> NO_SUPPORT_CONDITION_AFTER_NO_CONDITION = SQLDialect.supportedBy(FIREBIRD, POSTGRES);

    private final WithImpl               with;
    private final Table<R>               table;
    private final ConditionProviderImpl  on;
    private TableLike<?>                 using;
    private boolean                      usingDual;

    // Flags to keep track of DSL object creation state
    private transient boolean            matchedClause;
    private final List<MatchedClause>    matched;
    private transient boolean            notMatchedBySourceClause;
    private final List<MatchedClause>    notMatchedBySource;
    private transient boolean            notMatchedClause;
    private final List<NotMatchedClause> notMatched;

    MergeImpl(Configuration configuration, WithImpl with, Table<R> table) {
        super(configuration);

        this.with = with;
        this.table = table;
        this.on = new ConditionProviderImpl();
        this.matched = new ArrayList<>();
        this.notMatchedBySource = new ArrayList<>();
        this.notMatched = new ArrayList<>();
    }

    // -------------------------------------------------------------------------
    // UPSERT API
    // -------------------------------------------------------------------------

    final Table<R> table() {
        return table;
    }

    final MatchedClause getLastMatched() {
        return matched.get(matched.size() - 1);
    }

    final MatchedClause getLastNotMatchedBySource() {
        return notMatchedBySource.get(notMatchedBySource.size() - 1);
    }

    final NotMatchedClause getLastNotMatched() {
        return notMatched.get(notMatched.size() - 1);
    }

    @Override
    public final MergeUpsert columns(Field... fields) {
        return columns(Arrays.asList(fields));
    }

    @Override
    public final MergeUpsert columns(Collection fields) {
        return new MergeUpsert(configuration(), with, table, fields);
    }



    @Override
    public final MergeUpsert columns(Field field1) {
        return columns(Arrays.asList(field1));
    }

    @Override
    public final MergeUpsert columns(Field field1, Field field2) {
        return columns(Arrays.asList(field1, field2));
    }

    @Override
    public final MergeUpsert columns(Field field1, Field field2, Field field3) {
        return columns(Arrays.asList(field1, field2, field3));
    }

    @Override
    public final MergeUpsert columns(Field field1, Field field2, Field field3, Field field4) {
        return columns(Arrays.asList(field1, field2, field3, field4));
    }

    @Override
    public final MergeUpsert columns(Field field1, Field field2, Field field3, Field field4, Field field5) {
        return columns(Arrays.asList(field1, field2, field3, field4, field5));
    }

    @Override
    public final MergeUpsert columns(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6) {
        return columns(Arrays.asList(field1, field2, field3, field4, field5, field6));
    }

    @Override
    public final MergeUpsert columns(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7) {
        return columns(Arrays.asList(field1, field2, field3, field4, field5, field6, field7));
    }

    @Override
    public final MergeUpsert columns(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8) {
        return columns(Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8));
    }

    @Override
    public final MergeUpsert columns(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9) {
        return columns(Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9));
    }

    @Override
    public final MergeUpsert columns(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10) {
        return columns(Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10));
    }

    @Override
    public final MergeUpsert columns(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11) {
        return columns(Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11));
    }

    @Override
    public final MergeUpsert columns(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12) {
        return columns(Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12));
    }

    @Override
    public final MergeUpsert columns(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13) {
        return columns(Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13));
    }

    @Override
    public final MergeUpsert columns(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14) {
        return columns(Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14));
    }

    @Override
    public final MergeUpsert columns(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15) {
        return columns(Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15));
    }

    @Override
    public final MergeUpsert columns(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15, Field field16) {
        return columns(Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16));
    }

    @Override
    public final MergeUpsert columns(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15, Field field16, Field field17) {
        return columns(Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17));
    }

    @Override
    public final MergeUpsert columns(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15, Field field16, Field field17, Field field18) {
        return columns(Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18));
    }

    @Override
    public final MergeUpsert columns(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15, Field field16, Field field17, Field field18, Field field19) {
        return columns(Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19));
    }

    @Override
    public final MergeUpsert columns(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15, Field field16, Field field17, Field field18, Field field19, Field field20) {
        return columns(Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20));
    }

    @Override
    public final MergeUpsert columns(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15, Field field16, Field field17, Field field18, Field field19, Field field20, Field field21) {
        return columns(Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21));
    }

    @Override
    public final MergeUpsert columns(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15, Field field16, Field field17, Field field18, Field field19, Field field20, Field field21, Field field22) {
        return columns(Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21, field22));
    }



    @Override
    public final MergeUpsert select(Select select) {
        MergeUpsert result = new MergeUpsert(configuration(), with, table);
        result.upsertSelect = select;
        return result;
    }

    @Override
    public final MergeUpsert key(Field<?>... k) {
        return key(Arrays.asList(k));
    }

    @Override
    public final MergeUpsert key(Collection<? extends Field<?>> keys) {
        return new MergeUpsert(configuration(), with, table).key(keys);
    }

    // -------------------------------------------------------------------------
    // Shared MERGE API
    // -------------------------------------------------------------------------



    @Override
    public final MergeImpl values(Object value1) {
        return values(new Object[] { value1 });
    }

    @Override
    public final MergeImpl values(Object value1, Object value2) {
        return values(new Object[] { value1, value2 });
    }

    @Override
    public final MergeImpl values(Object value1, Object value2, Object value3) {
        return values(new Object[] { value1, value2, value3 });
    }

    @Override
    public final MergeImpl values(Object value1, Object value2, Object value3, Object value4) {
        return values(new Object[] { value1, value2, value3, value4 });
    }

    @Override
    public final MergeImpl values(Object value1, Object value2, Object value3, Object value4, Object value5) {
        return values(new Object[] { value1, value2, value3, value4, value5 });
    }

    @Override
    public final MergeImpl values(Object value1, Object value2, Object value3, Object value4, Object value5, Object value6) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6 });
    }

    @Override
    public final MergeImpl values(Object value1, Object value2, Object value3, Object value4, Object value5, Object value6, Object value7) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7 });
    }

    @Override
    public final MergeImpl values(Object value1, Object value2, Object value3, Object value4, Object value5, Object value6, Object value7, Object value8) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8 });
    }

    @Override
    public final MergeImpl values(Object value1, Object value2, Object value3, Object value4, Object value5, Object value6, Object value7, Object value8, Object value9) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9 });
    }

    @Override
    public final MergeImpl values(Object value1, Object value2, Object value3, Object value4, Object value5, Object value6, Object value7, Object value8, Object value9, Object value10) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10 });
    }

    @Override
    public final MergeImpl values(Object value1, Object value2, Object value3, Object value4, Object value5, Object value6, Object value7, Object value8, Object value9, Object value10, Object value11) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11 });
    }

    @Override
    public final MergeImpl values(Object value1, Object value2, Object value3, Object value4, Object value5, Object value6, Object value7, Object value8, Object value9, Object value10, Object value11, Object value12) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12 });
    }

    @Override
    public final MergeImpl values(Object value1, Object value2, Object value3, Object value4, Object value5, Object value6, Object value7, Object value8, Object value9, Object value10, Object value11, Object value12, Object value13) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13 });
    }

    @Override
    public final MergeImpl values(Object value1, Object value2, Object value3, Object value4, Object value5, Object value6, Object value7, Object value8, Object value9, Object value10, Object value11, Object value12, Object value13, Object value14) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14 });
    }

    @Override
    public final MergeImpl values(Object value1, Object value2, Object value3, Object value4, Object value5, Object value6, Object value7, Object value8, Object value9, Object value10, Object value11, Object value12, Object value13, Object value14, Object value15) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15 });
    }

    @Override
    public final MergeImpl values(Object value1, Object value2, Object value3, Object value4, Object value5, Object value6, Object value7, Object value8, Object value9, Object value10, Object value11, Object value12, Object value13, Object value14, Object value15, Object value16) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16 });
    }

    @Override
    public final MergeImpl values(Object value1, Object value2, Object value3, Object value4, Object value5, Object value6, Object value7, Object value8, Object value9, Object value10, Object value11, Object value12, Object value13, Object value14, Object value15, Object value16, Object value17) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17 });
    }

    @Override
    public final MergeImpl values(Object value1, Object value2, Object value3, Object value4, Object value5, Object value6, Object value7, Object value8, Object value9, Object value10, Object value11, Object value12, Object value13, Object value14, Object value15, Object value16, Object value17, Object value18) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17, value18 });
    }

    @Override
    public final MergeImpl values(Object value1, Object value2, Object value3, Object value4, Object value5, Object value6, Object value7, Object value8, Object value9, Object value10, Object value11, Object value12, Object value13, Object value14, Object value15, Object value16, Object value17, Object value18, Object value19) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17, value18, value19 });
    }

    @Override
    public final MergeImpl values(Object value1, Object value2, Object value3, Object value4, Object value5, Object value6, Object value7, Object value8, Object value9, Object value10, Object value11, Object value12, Object value13, Object value14, Object value15, Object value16, Object value17, Object value18, Object value19, Object value20) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17, value18, value19, value20 });
    }

    @Override
    public final MergeImpl values(Object value1, Object value2, Object value3, Object value4, Object value5, Object value6, Object value7, Object value8, Object value9, Object value10, Object value11, Object value12, Object value13, Object value14, Object value15, Object value16, Object value17, Object value18, Object value19, Object value20, Object value21) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17, value18, value19, value20, value21 });
    }

    @Override
    public final MergeImpl values(Object value1, Object value2, Object value3, Object value4, Object value5, Object value6, Object value7, Object value8, Object value9, Object value10, Object value11, Object value12, Object value13, Object value14, Object value15, Object value16, Object value17, Object value18, Object value19, Object value20, Object value21, Object value22) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17, value18, value19, value20, value21, value22 });
    }


    @Override
    public final MergeImpl values(Field value1) {
        return values(new Field[] { value1 });
    }

    @Override
    public final MergeImpl values(Field value1, Field value2) {
        return values(new Field[] { value1, value2 });
    }

    @Override
    public final MergeImpl values(Field value1, Field value2, Field value3) {
        return values(new Field[] { value1, value2, value3 });
    }

    @Override
    public final MergeImpl values(Field value1, Field value2, Field value3, Field value4) {
        return values(new Field[] { value1, value2, value3, value4 });
    }

    @Override
    public final MergeImpl values(Field value1, Field value2, Field value3, Field value4, Field value5) {
        return values(new Field[] { value1, value2, value3, value4, value5 });
    }

    @Override
    public final MergeImpl values(Field value1, Field value2, Field value3, Field value4, Field value5, Field value6) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6 });
    }

    @Override
    public final MergeImpl values(Field value1, Field value2, Field value3, Field value4, Field value5, Field value6, Field value7) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7 });
    }

    @Override
    public final MergeImpl values(Field value1, Field value2, Field value3, Field value4, Field value5, Field value6, Field value7, Field value8) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8 });
    }

    @Override
    public final MergeImpl values(Field value1, Field value2, Field value3, Field value4, Field value5, Field value6, Field value7, Field value8, Field value9) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9 });
    }

    @Override
    public final MergeImpl values(Field value1, Field value2, Field value3, Field value4, Field value5, Field value6, Field value7, Field value8, Field value9, Field value10) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10 });
    }

    @Override
    public final MergeImpl values(Field value1, Field value2, Field value3, Field value4, Field value5, Field value6, Field value7, Field value8, Field value9, Field value10, Field value11) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11 });
    }

    @Override
    public final MergeImpl values(Field value1, Field value2, Field value3, Field value4, Field value5, Field value6, Field value7, Field value8, Field value9, Field value10, Field value11, Field value12) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12 });
    }

    @Override
    public final MergeImpl values(Field value1, Field value2, Field value3, Field value4, Field value5, Field value6, Field value7, Field value8, Field value9, Field value10, Field value11, Field value12, Field value13) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13 });
    }

    @Override
    public final MergeImpl values(Field value1, Field value2, Field value3, Field value4, Field value5, Field value6, Field value7, Field value8, Field value9, Field value10, Field value11, Field value12, Field value13, Field value14) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14 });
    }

    @Override
    public final MergeImpl values(Field value1, Field value2, Field value3, Field value4, Field value5, Field value6, Field value7, Field value8, Field value9, Field value10, Field value11, Field value12, Field value13, Field value14, Field value15) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15 });
    }

    @Override
    public final MergeImpl values(Field value1, Field value2, Field value3, Field value4, Field value5, Field value6, Field value7, Field value8, Field value9, Field value10, Field value11, Field value12, Field value13, Field value14, Field value15, Field value16) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16 });
    }

    @Override
    public final MergeImpl values(Field value1, Field value2, Field value3, Field value4, Field value5, Field value6, Field value7, Field value8, Field value9, Field value10, Field value11, Field value12, Field value13, Field value14, Field value15, Field value16, Field value17) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17 });
    }

    @Override
    public final MergeImpl values(Field value1, Field value2, Field value3, Field value4, Field value5, Field value6, Field value7, Field value8, Field value9, Field value10, Field value11, Field value12, Field value13, Field value14, Field value15, Field value16, Field value17, Field value18) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17, value18 });
    }

    @Override
    public final MergeImpl values(Field value1, Field value2, Field value3, Field value4, Field value5, Field value6, Field value7, Field value8, Field value9, Field value10, Field value11, Field value12, Field value13, Field value14, Field value15, Field value16, Field value17, Field value18, Field value19) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17, value18, value19 });
    }

    @Override
    public final MergeImpl values(Field value1, Field value2, Field value3, Field value4, Field value5, Field value6, Field value7, Field value8, Field value9, Field value10, Field value11, Field value12, Field value13, Field value14, Field value15, Field value16, Field value17, Field value18, Field value19, Field value20) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17, value18, value19, value20 });
    }

    @Override
    public final MergeImpl values(Field value1, Field value2, Field value3, Field value4, Field value5, Field value6, Field value7, Field value8, Field value9, Field value10, Field value11, Field value12, Field value13, Field value14, Field value15, Field value16, Field value17, Field value18, Field value19, Field value20, Field value21) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17, value18, value19, value20, value21 });
    }

    @Override
    public final MergeImpl values(Field value1, Field value2, Field value3, Field value4, Field value5, Field value6, Field value7, Field value8, Field value9, Field value10, Field value11, Field value12, Field value13, Field value14, Field value15, Field value16, Field value17, Field value18, Field value19, Field value20, Field value21, Field value22) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17, value18, value19, value20, value21, value22 });
    }



    @Override
    public final MergeImpl values(Object... values) {
        getLastNotMatched().insertMap.set(Tools.fields(values, getLastNotMatched().insertMap.values.keySet().toArray(EMPTY_FIELD)));
        return this;
    }

    @Override
    public final MergeImpl values(Field<?>... values) {
        return values((Object[]) values);
    }

    @Override
    public final MergeImpl values(Collection<?> values) {
        return values(values.toArray());
    }

    // -------------------------------------------------------------------------
    // Merge API
    // -------------------------------------------------------------------------

    @Override
    public final MergeImpl using(TableLike<?> u) {
        this.using = u;
        return this;
    }

    @Override
    public final MergeImpl usingDual() {
        this.usingDual = true;
        return this;
    }

    @Override
    public final MergeImpl on(Condition conditions) {
        on.addConditions(conditions);
        return this;
    }

    @Override
    public final MergeImpl on(Condition... conditions) {
        on.addConditions(conditions);
        return this;
    }

    @Override
    public final MergeOnConditionStep<R> on(Field<Boolean> condition) {
        return on(condition(condition));
    }

    @Override
    public final MergeImpl on(SQL sql) {
        return on(condition(sql));
    }

    @Override
    public final MergeImpl on(String sql) {
        return on(condition(sql));
    }

    @Override
    public final MergeImpl on(String sql, Object... bindings) {
        return on(condition(sql, bindings));
    }

    @Override
    public final MergeImpl on(String sql, QueryPart... parts) {
        return on(condition(sql, parts));
    }

    @Override
    public final MergeImpl and(Condition condition) {
        on.addConditions(condition);
        return this;
    }

    @Override
    public final MergeImpl and(Field<Boolean> condition) {
        return and(condition(condition));
    }

    @Override
    public final MergeImpl and(SQL sql) {
        return and(condition(sql));
    }

    @Override
    public final MergeImpl and(String sql) {
        return and(condition(sql));
    }

    @Override
    public final MergeImpl and(String sql, Object... bindings) {
        return and(condition(sql, bindings));
    }

    @Override
    public final MergeImpl and(String sql, QueryPart... parts) {
        return and(condition(sql, parts));
    }

    @Override
    public final MergeImpl andNot(Condition condition) {
        return and(condition.not());
    }

    @Override
    public final MergeImpl andNot(Field<Boolean> condition) {
        return andNot(condition(condition));
    }

    @Override
    public final MergeImpl andExists(Select<?> select) {
        return and(exists(select));
    }

    @Override
    public final MergeImpl andNotExists(Select<?> select) {
        return and(notExists(select));
    }

    @Override
    public final MergeImpl or(Condition condition) {
        on.addConditions(Operator.OR, condition);
        return this;
    }

    @Override
    public final MergeImpl or(Field<Boolean> condition) {
        return and(condition(condition));
    }

    @Override
    public final MergeImpl or(SQL sql) {
        return or(condition(sql));
    }

    @Override
    public final MergeImpl or(String sql) {
        return or(condition(sql));
    }

    @Override
    public final MergeImpl or(String sql, Object... bindings) {
        return or(condition(sql, bindings));
    }

    @Override
    public final MergeImpl or(String sql, QueryPart... parts) {
        return or(condition(sql, parts));
    }

    @Override
    public final MergeImpl orNot(Condition condition) {
        return or(condition.not());
    }

    @Override
    public final MergeImpl orNot(Field<Boolean> condition) {
        return orNot(condition(condition));
    }

    @Override
    public final MergeImpl orExists(Select<?> select) {
        return or(exists(select));
    }

    @Override
    public final MergeImpl orNotExists(Select<?> select) {
        return or(notExists(select));
    }

    @Override
    public final MergeImpl whenMatchedThenUpdate() {
        return whenMatchedAnd(noCondition()).thenUpdate();
    }

    @Override
    public final MergeImpl whenMatchedThenDelete() {
        return whenMatchedAnd(noCondition()).thenDelete();
    }

    @Override
    public final MergeImpl whenMatchedAnd(Condition condition) {
        matchedClause = true;
        matched.add(new MatchedClause(table, condition));

        notMatchedBySourceClause = false;
        notMatchedClause = false;
        return this;
    }

    @Override
    public final MergeImpl whenMatchedAnd(Field<Boolean> condition) {
        return whenMatchedAnd(condition(condition));
    }

    @Override
    public final MergeImpl whenMatchedAnd(SQL sql) {
        return whenMatchedAnd(condition(sql));
    }

    @Override
    public final MergeImpl whenMatchedAnd(String sql) {
        return whenMatchedAnd(condition(sql));
    }

    @Override
    public final MergeImpl whenMatchedAnd(String sql, Object... bindings) {
        return whenMatchedAnd(condition(sql, bindings));
    }

    @Override
    public final MergeImpl whenMatchedAnd(String sql, QueryPart... parts) {
        return whenMatchedAnd(condition(sql, parts));
    }

    @Override
    public final MergeImpl thenUpdate() {
        return this;
    }

    @Override
    public final MergeImpl thenDelete() {
        if (matchedClause)
            getLastMatched().delete = true;
        else if (notMatchedBySourceClause)
            getLastNotMatchedBySource().delete = true;
        else
            throw new IllegalStateException("Cannot call where() on the current state of the MERGE statement");

        return this;
    }

    @Override
    public final <T> MergeImpl set(Field<T> field, T value) {
        return set(field, Tools.field(value, field));
    }

    @Override
    public final <T> MergeImpl set(Field<T> field, Field<T> value) {
        if (matchedClause)
            getLastMatched().updateMap.put(field, nullSafe(value));
        else if (notMatchedBySourceClause)
            getLastNotMatchedBySource().updateMap.put(field, nullSafe(value));
        else if (notMatchedClause)
            getLastNotMatched().insertMap.set(field, nullSafe(value));
        else
            throw new IllegalStateException("Cannot call where() on the current state of the MERGE statement");

        return this;
    }

    @Override
    public final <T> MergeImpl setNull(Field<T> field) {
        return set(field, (T) null);
    }

    @Override
    public final <T> MergeImpl set(Field<T> field, Select<? extends Record1<T>> value) {
        if (value == null)
            return set(field, (T) null);
        else
            return set(field, value.asField());
    }

    @Override
    public final MergeImpl set(Map<?, ?> map) {
        if (matchedClause)
            getLastMatched().updateMap.set(map);
        else if (notMatchedBySourceClause)
            getLastNotMatchedBySource().updateMap.set(map);
        else if (notMatchedClause)
            getLastNotMatched().insertMap.set(map);
        else
            throw new IllegalStateException("Cannot call where() on the current state of the MERGE statement");

        return this;
    }

    @Override
    public final MergeImpl set(Record record) {
        return set(Tools.mapOfTouchedValues(this, record));
    }

    @Override
    public final MergeImpl whenNotMatchedThenInsert() {
        return whenNotMatchedThenInsert(emptyList());
    }



    @Override
    public final MergeImpl whenNotMatchedThenInsert(Field field1) {
        return whenNotMatchedThenInsert(new Field[] { field1 });
    }

    @Override
    public final MergeImpl whenNotMatchedThenInsert(Field field1, Field field2) {
        return whenNotMatchedThenInsert(new Field[] { field1, field2 });
    }

    @Override
    public final MergeImpl whenNotMatchedThenInsert(Field field1, Field field2, Field field3) {
        return whenNotMatchedThenInsert(new Field[] { field1, field2, field3 });
    }

    @Override
    public final MergeImpl whenNotMatchedThenInsert(Field field1, Field field2, Field field3, Field field4) {
        return whenNotMatchedThenInsert(new Field[] { field1, field2, field3, field4 });
    }

    @Override
    public final MergeImpl whenNotMatchedThenInsert(Field field1, Field field2, Field field3, Field field4, Field field5) {
        return whenNotMatchedThenInsert(new Field[] { field1, field2, field3, field4, field5 });
    }

    @Override
    public final MergeImpl whenNotMatchedThenInsert(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6) {
        return whenNotMatchedThenInsert(new Field[] { field1, field2, field3, field4, field5, field6 });
    }

    @Override
    public final MergeImpl whenNotMatchedThenInsert(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7) {
        return whenNotMatchedThenInsert(new Field[] { field1, field2, field3, field4, field5, field6, field7 });
    }

    @Override
    public final MergeImpl whenNotMatchedThenInsert(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8) {
        return whenNotMatchedThenInsert(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8 });
    }

    @Override
    public final MergeImpl whenNotMatchedThenInsert(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9) {
        return whenNotMatchedThenInsert(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9 });
    }

    @Override
    public final MergeImpl whenNotMatchedThenInsert(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10) {
        return whenNotMatchedThenInsert(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10 });
    }

    @Override
    public final MergeImpl whenNotMatchedThenInsert(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11) {
        return whenNotMatchedThenInsert(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11 });
    }

    @Override
    public final MergeImpl whenNotMatchedThenInsert(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12) {
        return whenNotMatchedThenInsert(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12 });
    }

    @Override
    public final MergeImpl whenNotMatchedThenInsert(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13) {
        return whenNotMatchedThenInsert(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13 });
    }

    @Override
    public final MergeImpl whenNotMatchedThenInsert(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14) {
        return whenNotMatchedThenInsert(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14 });
    }

    @Override
    public final MergeImpl whenNotMatchedThenInsert(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15) {
        return whenNotMatchedThenInsert(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15 });
    }

    @Override
    public final MergeImpl whenNotMatchedThenInsert(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15, Field field16) {
        return whenNotMatchedThenInsert(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16 });
    }

    @Override
    public final MergeImpl whenNotMatchedThenInsert(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15, Field field16, Field field17) {
        return whenNotMatchedThenInsert(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17 });
    }

    @Override
    public final MergeImpl whenNotMatchedThenInsert(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15, Field field16, Field field17, Field field18) {
        return whenNotMatchedThenInsert(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18 });
    }

    @Override
    public final MergeImpl whenNotMatchedThenInsert(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15, Field field16, Field field17, Field field18, Field field19) {
        return whenNotMatchedThenInsert(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19 });
    }

    @Override
    public final MergeImpl whenNotMatchedThenInsert(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15, Field field16, Field field17, Field field18, Field field19, Field field20) {
        return whenNotMatchedThenInsert(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20 });
    }

    @Override
    public final MergeImpl whenNotMatchedThenInsert(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15, Field field16, Field field17, Field field18, Field field19, Field field20, Field field21) {
        return whenNotMatchedThenInsert(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21 });
    }

    @Override
    public final MergeImpl whenNotMatchedThenInsert(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15, Field field16, Field field17, Field field18, Field field19, Field field20, Field field21, Field field22) {
        return whenNotMatchedThenInsert(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21, field22 });
    }




    @Override
    public final MergeImpl whenNotMatchedThenInsert(Field<?>... fields) {
        return whenNotMatchedThenInsert(Arrays.asList(fields));
    }

    @Override
    public final MergeImpl whenNotMatchedThenInsert(Collection<? extends Field<?>> fields) {
        return whenNotMatched().thenInsert(fields);
    }




    @Override
    public final MergeImpl whenNotMatched() {
        return whenNotMatchedAnd0(noCondition());
    }

    @Override
    public final MergeImpl whenNotMatchedAnd(Condition condition) {
        return whenNotMatchedAnd0(condition);
    }

    final MergeImpl whenNotMatchedAnd0(Condition condition) {
        notMatchedClause = true;
        notMatched.add(new NotMatchedClause(table, condition, false));

        notMatchedBySourceClause = false;
        matchedClause = false;
        return this;
    }

    @Override
    public final MergeImpl whenNotMatchedAnd(Field<Boolean> condition) {
        return whenNotMatchedAnd(condition(condition));
    }

    @Override
    public final MergeImpl whenNotMatchedAnd(SQL sql) {
        return whenNotMatchedAnd(condition(sql));
    }

    @Override
    public final MergeImpl whenNotMatchedAnd(String sql) {
        return whenNotMatchedAnd(condition(sql));
    }

    @Override
    public final MergeImpl whenNotMatchedAnd(String sql, Object... bindings) {
        return whenNotMatchedAnd(condition(sql, bindings));
    }

    @Override
    public final MergeImpl whenNotMatchedAnd(String sql, QueryPart... parts) {
        return whenNotMatchedAnd(condition(sql, parts));
    }

    @Override
    public final MergeImpl whenNotMatchedBySource() {
        return whenNotMatchedBySourceAnd0(noCondition());
    }

    @Override
    public final MergeImpl whenNotMatchedBySourceAnd(Condition condition) {
        return whenNotMatchedBySourceAnd0(condition);
    }

    final MergeImpl whenNotMatchedBySourceAnd0(Condition condition) {
        notMatchedBySourceClause = true;
        notMatchedBySource.add(new MatchedClause(table, condition, false, true));

        notMatchedClause = false;
        matchedClause = false;
        return this;
    }

    @Override
    public final MergeImpl whenNotMatchedBySourceAnd(Field<Boolean> condition) {
        return whenNotMatchedBySourceAnd(condition(condition));
    }

    @Override
    public final MergeImpl whenNotMatchedBySourceAnd(SQL sql) {
        return whenNotMatchedBySourceAnd(condition(sql));
    }

    @Override
    public final MergeImpl whenNotMatchedBySourceAnd(String sql) {
        return whenNotMatchedBySourceAnd(condition(sql));
    }

    @Override
    public final MergeImpl whenNotMatchedBySourceAnd(String sql, Object... bindings) {
        return whenNotMatchedBySourceAnd(condition(sql, bindings));
    }

    @Override
    public final MergeImpl whenNotMatchedBySourceAnd(String sql, QueryPart... parts) {
        return whenNotMatchedBySourceAnd(condition(sql, parts));
    }

    @Override
    public final MergeImpl whenNotMatchedByTarget() {
        return whenNotMatchedByTargetAnd0(noCondition());
    }

    @Override
    public final MergeImpl whenNotMatchedByTargetAnd(Condition condition) {
        return whenNotMatchedByTargetAnd0(condition);
    }

    final MergeImpl whenNotMatchedByTargetAnd0(Condition condition) {
        notMatchedClause = true;
        notMatched.add(new NotMatchedClause(table, condition, true));

        notMatchedBySourceClause = false;
        matchedClause = false;
        return this;
    }

    @Override
    public final MergeImpl whenNotMatchedByTargetAnd(Field<Boolean> condition) {
        return whenNotMatchedByTargetAnd(condition(condition));
    }

    @Override
    public final MergeImpl whenNotMatchedByTargetAnd(SQL sql) {
        return whenNotMatchedByTargetAnd(condition(sql));
    }

    @Override
    public final MergeImpl whenNotMatchedByTargetAnd(String sql) {
        return whenNotMatchedByTargetAnd(condition(sql));
    }

    @Override
    public final MergeImpl whenNotMatchedByTargetAnd(String sql, Object... bindings) {
        return whenNotMatchedByTargetAnd(condition(sql, bindings));
    }

    @Override
    public final MergeImpl whenNotMatchedByTargetAnd(String sql, QueryPart... parts) {
        return whenNotMatchedByTargetAnd(condition(sql, parts));
    }



    @Override
    public final MergeImpl thenInsert() {
        return thenInsert(emptyList());
    }



    @Override
    public final MergeImpl thenInsert(Field field1) {
        return thenInsert(new Field[] { field1 });
    }

    @Override
    public final MergeImpl thenInsert(Field field1, Field field2) {
        return thenInsert(new Field[] { field1, field2 });
    }

    @Override
    public final MergeImpl thenInsert(Field field1, Field field2, Field field3) {
        return thenInsert(new Field[] { field1, field2, field3 });
    }

    @Override
    public final MergeImpl thenInsert(Field field1, Field field2, Field field3, Field field4) {
        return thenInsert(new Field[] { field1, field2, field3, field4 });
    }

    @Override
    public final MergeImpl thenInsert(Field field1, Field field2, Field field3, Field field4, Field field5) {
        return thenInsert(new Field[] { field1, field2, field3, field4, field5 });
    }

    @Override
    public final MergeImpl thenInsert(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6) {
        return thenInsert(new Field[] { field1, field2, field3, field4, field5, field6 });
    }

    @Override
    public final MergeImpl thenInsert(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7) {
        return thenInsert(new Field[] { field1, field2, field3, field4, field5, field6, field7 });
    }

    @Override
    public final MergeImpl thenInsert(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8) {
        return thenInsert(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8 });
    }

    @Override
    public final MergeImpl thenInsert(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9) {
        return thenInsert(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9 });
    }

    @Override
    public final MergeImpl thenInsert(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10) {
        return thenInsert(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10 });
    }

    @Override
    public final MergeImpl thenInsert(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11) {
        return thenInsert(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11 });
    }

    @Override
    public final MergeImpl thenInsert(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12) {
        return thenInsert(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12 });
    }

    @Override
    public final MergeImpl thenInsert(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13) {
        return thenInsert(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13 });
    }

    @Override
    public final MergeImpl thenInsert(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14) {
        return thenInsert(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14 });
    }

    @Override
    public final MergeImpl thenInsert(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15) {
        return thenInsert(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15 });
    }

    @Override
    public final MergeImpl thenInsert(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15, Field field16) {
        return thenInsert(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16 });
    }

    @Override
    public final MergeImpl thenInsert(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15, Field field16, Field field17) {
        return thenInsert(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17 });
    }

    @Override
    public final MergeImpl thenInsert(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15, Field field16, Field field17, Field field18) {
        return thenInsert(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18 });
    }

    @Override
    public final MergeImpl thenInsert(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15, Field field16, Field field17, Field field18, Field field19) {
        return thenInsert(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19 });
    }

    @Override
    public final MergeImpl thenInsert(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15, Field field16, Field field17, Field field18, Field field19, Field field20) {
        return thenInsert(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20 });
    }

    @Override
    public final MergeImpl thenInsert(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15, Field field16, Field field17, Field field18, Field field19, Field field20, Field field21) {
        return thenInsert(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21 });
    }

    @Override
    public final MergeImpl thenInsert(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15, Field field16, Field field17, Field field18, Field field19, Field field20, Field field21, Field field22) {
        return thenInsert(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21, field22 });
    }




    @Override
    public final MergeImpl thenInsert(Field<?>... fields) {
        return thenInsert(Arrays.asList(fields));
    }

    @Override
    public final MergeImpl thenInsert(Collection<? extends Field<?>> fields) {
        getLastNotMatched().insertMap.addFields(fields);

        return this;
    }

    @Override
    public final MergeImpl where(Condition condition) {
        if (matchedClause)
            getLastMatched().condition = condition;
        else if (notMatchedBySourceClause)
            getLastNotMatchedBySource().condition = condition;
        else if (notMatchedClause)
            getLastNotMatched().condition = condition;
        else
            throw new IllegalStateException("Cannot call where() on the current state of the MERGE statement");

        return this;
    }

    @Override
    public final MergeMatchedDeleteStep<R> where(Field<Boolean> condition) {
        return where(condition(condition));
    }

    @Override
    public final MergeImpl deleteWhere(Condition condition) {
        // The ordering is to run deletions *before* updates in order to prevent
        // constraint violations that may occur from updates, otherwise.
        // See https://github.com/jOOQ/jOOQ/issues/7291#issuecomment-610833303
        if (matchedClause)
            matched.add(matched.size() - 1, new MatchedClause(table, condition, true, false));
        else if (notMatchedBySourceClause)
            notMatchedBySource.add(matched.size() - 1, new MatchedClause(table, condition, true, true));
        else
            throw new IllegalStateException("Cannot call where() on the current state of the MERGE statement");

        return this;
    }

    @Override
    public final MergeImpl deleteWhere(Field<Boolean> condition) {
        return deleteWhere(condition(condition));
    }

    // -------------------------------------------------------------------------
    // QueryPart API
    // -------------------------------------------------------------------------

    final MergeImpl<R, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> copy(Consumer<? super MergeImpl<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>> finisher) {
        return copy(finisher, table);
    }

    final <O extends Record> MergeImpl<O, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> copy(Consumer<? super MergeImpl<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>> finisher, Table<O> t) {
        MergeImpl<O, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> r
            = new MergeImpl<>(configuration(), with, t);

        r.using = using;
        r.usingDual = usingDual;
        r.on.addConditions(extractCondition(on));
        for (MatchedClause m : concat(matched, notMatchedBySource)) {
            MatchedClause m2 = new MatchedClause(t, m.condition, m.delete, m.notMatchedBySource, new FieldMapForUpdate(t, m.updateMap.setClause, m.updateMap.assignmentClause));
            m2.updateMap.putAll(m.updateMap);

            if (m.notMatchedBySource)
                r.notMatchedBySource.add(m2);
            else
                r.matched.add(m2);
        }
        for (NotMatchedClause m : notMatched) {
            NotMatchedClause m2 = new NotMatchedClause(t, m.condition, m.byTarget);
            m2.insertMap.from(m.insertMap);
            r.notMatched.add(m2);
        }

        finisher.accept(r);
        return r;
    }

    @Override
    public final void accept(Context<?> ctx) {
        Table<?> t = InlineDerivedTable.inlineDerivedTable(ctx, table);

        if (t instanceof InlineDerivedTable<?> i) {
            ctx.configuration().requireCommercial(() -> "InlineDerivedTable emulation for MERGE clauses is available in the commercial jOOQ editions only");

























        }
        else
            accept0(ctx);
    }

    private final void accept0(Context<?> ctx) {














        if (with != null)
            ctx.visit(with);

        Table<?> t = InlineDerivedTable.inlineDerivedTable(ctx, table);

        ctx.start(MERGE_MERGE_INTO)
           .visit(K_MERGE_INTO).sql(' ')
           .declareTables(true, c -> c.visit(table))
           .end(MERGE_MERGE_INTO)
           .formatSeparator()
           .start(MERGE_USING)
           .visit(K_USING).sql(' ');

        ctx.declareTables(true, c1 -> c1.data(DATA_WRAP_DERIVED_TABLES_IN_PARENTHESES, true, c2 -> {

            // [#5110] As of version 10.14, Derby only supports direct table references
            //         in its MERGE statement.
            if (usingDual) {
                switch (c2.family()) {
                    case DERBY:

                        c2.visit(new Dual());
                        break;
                    default:
                        c2.visit(DSL.selectOne());
                        break;
                }
            }
            else
                c2.visit(using);
        }));

        switch (ctx.family()) {
            case POSTGRES:



{
                if (using instanceof Select) {
                    int hash = Internal.hash(using);

                    ctx.sql(' ').visit(K_AS).sql(' ')
                       .sql("dummy_")
                       .sql(hash)
                       .sql('(');

                    String separator = "";
                    for (Field<?> field : using.asTable("t").fields()) {

                        // Some fields are unnamed
                        // [#579] Correct this
                        String name = StringUtils.isBlank(field.getName())
                            ? "dummy_" + hash + "_" + Internal.hash(field)
                            : field.getName();

                        ctx.sql(separator).literal(name);
                        separator = ", ";
                    }

                    ctx.sql(')');
                }

                else if (usingDual)
                    ctx.sql(" t");

                break;
            }
        }

        ctx.end(MERGE_USING)
           .formatSeparator()
           .start(MERGE_ON)
           .visit(K_ON).sql(' ');






















        ctx.visit(on);






        ctx.end(MERGE_ON)
           .start(MERGE_WHEN_MATCHED_THEN_UPDATE)
           .start(MERGE_SET);

        // [#7291] Multi MATCHED emulation
        boolean emulateMatched = false;
        boolean emulateNotMatched = false;
        boolean requireMatchedConditions = false;

        // Prevent error 5324 "In a MERGE statement, a 'WHEN MATCHED' clause with a search condition
        // cannot appear after a 'WHEN MATCHED' clause with no search condition."
        // This can also happen in Firebird: http://tracker.firebirdsql.org/browse/JDBC-621

        // [#10054] TODO: Skip all WHEN MATCHED clauses after a WHEN MATCHED clause with no search condition
        if (NO_SUPPORT_CONDITION_AFTER_NO_CONDITION.contains(ctx.dialect())) {
            boolean withoutMatchedConditionFound = false;

            for (MatchedClause m : concat(matched, notMatchedBySource)) {
                if (requireMatchedConditions |= withoutMatchedConditionFound)
                    break;

                withoutMatchedConditionFound |= m.condition instanceof NoCondition;
            }
        }

        emulateCheck:
        if ((NO_SUPPORT_MULTI.contains(ctx.dialect()) && (matched.size() + notMatchedBySource.size()) > 1)) {
            boolean matchUpdate = false;
            boolean matchDelete = false;

            for (MatchedClause m : concat(matched, notMatchedBySource)) {
                if (m.delete) {
                    if (emulateMatched |= matchDelete)
                        break emulateCheck;

                    matchDelete = true;
                }
                else {
                    if (emulateMatched |= matchUpdate)
                        break emulateCheck;

                    matchUpdate = true;
                }
            }
        }

        if (emulateMatched) {
            MatchedClause update = null;
            MatchedClause delete = null;
            NotMatchedClause insert = null;

            Condition negate = noCondition();

            for (MatchedClause m : concat(matched, notMatchedBySource)) {
                Condition condition = negate.and(m.condition);

                if (m.delete) {
                    if (delete == null)
                        delete = new MatchedClause(table, noCondition(), true, false);

                    delete.condition = delete.condition.or(condition);
                }
                else {
                    if (update == null)
                        update = new MatchedClause(table, noCondition());

                    for (Entry<FieldOrRow, FieldOrRowOrSelect> e : m.updateMap.entrySet()) {
                        FieldOrRowOrSelect exp = update.updateMap.get(e.getKey());

                        if (exp instanceof CaseSearched c)
                            c.when(negate.and(condition), e.getValue());

                        // [#10523] [#13325] TODO: ClassCastException when we're using Row here, once supported
                        else
                            update.updateMap.put(e.getKey(), when(negate.and(condition), (Field) e.getValue()).else_(e.getKey()));
                    }

                    update.condition = update.condition.or(condition);
                }

                if (REQUIRE_NEGATION.contains(ctx.dialect()))
                    negate = negate.andNot(m.condition instanceof NoCondition ? trueCondition() : m.condition);
            }

            for (NotMatchedClause m : notMatched) {
                Condition condition = negate.and(m.condition);

                if (insert == null)
                    insert = new NotMatchedClause(table, noCondition(), m.byTarget);

                for (Entry<Field<?>, List<Field<?>>> e : m.insertMap.values.entrySet()) {
                    List<Field<?>> l = insert.insertMap.values.get(e.getKey());
                    Field<?> exp = !isEmpty(l) ? l.get(0) : null;

                    if (exp instanceof CaseSearched c)
                        c.when(negate.and(condition), e.getValue().get(0));
                    else
                        insert.insertMap.values.put(e.getKey(), asList(when(negate.and(condition), (Field) e.getValue().get(0)).else_(e.getKey())));
                }

                insert.condition = insert.condition.or(condition);

                if (REQUIRE_NEGATION.contains(ctx.dialect()))
                    negate = negate.andNot(m.condition instanceof NoCondition ? trueCondition() : m.condition);
            }














            {
                if (delete != null)
                    toSQLMatched(ctx, delete, requireMatchedConditions);

                if (update != null)
                    toSQLMatched(ctx, update, requireMatchedConditions);
            }
        }

        // [#7291] Workaround for https://github.com/h2database/h2database/issues/2552
        else if (REQUIRE_NEGATION.contains(ctx.dialect())) {
            Condition negate = noCondition();

            for (MatchedClause m : concat(matched, notMatchedBySource)) {
                toSQLMatched(ctx, new MatchedClause(table, negate.and(m.condition), m.delete, m.notMatchedBySource, m.updateMap), requireMatchedConditions);
                negate = negate.andNot(m.condition instanceof NoCondition ? trueCondition() : m.condition);
            }
        }
        else {
            for (MatchedClause m : concat(matched, notMatchedBySource))
                toSQLMatched(ctx, m, requireMatchedConditions);
        }

        ctx.end(MERGE_SET)
           .end(MERGE_WHEN_MATCHED_THEN_UPDATE)
           .start(MERGE_WHEN_NOT_MATCHED_THEN_INSERT);

        if ((NO_SUPPORT_MULTI.contains(ctx.dialect()) && notMatched.size() > 1)) {
            emulateNotMatched = notMatched.size() > 1;
        }

        if (emulateNotMatched) {
            NotMatchedClause insert = null;

            Condition negate = noCondition();

            for (NotMatchedClause m : notMatched) {
                Condition condition = negate.and(m.condition);

                if (insert == null)
                    insert = new NotMatchedClause(table, noCondition(), m.byTarget);

                for (Entry<Field<?>, List<Field<?>>> e : m.insertMap.values.entrySet()) {
                    List<Field<?>> l = insert.insertMap.values.get(e.getKey());
                    Field<?> exp = !isEmpty(l) ? l.get(0) : null;

                    if (exp instanceof CaseSearched c)
                        c.when(negate.and(condition), e.getValue().get(0));
                    else
                        insert.insertMap.set(e.getKey(), when(negate.and(condition), (Field) e.getValue().get(0)));
                }

                insert.condition = insert.condition.or(condition);

                if (REQUIRE_NEGATION.contains(ctx.dialect()))
                    negate = negate.andNot(m.condition instanceof NoCondition ? trueCondition() : m.condition);
            }

            if (insert != null)
                toSQLNotMatched(ctx, insert);
        }

        else {
            for (NotMatchedClause m : notMatched)
                toSQLNotMatched(ctx, m);
        }

        ctx.end(MERGE_WHEN_NOT_MATCHED_THEN_INSERT);




















    }

    static final void toSQLMatched(Context<?> ctx, MatchedClause m, boolean requireMatchedConditions) {
        if (m.delete)
            toSQLMatched(ctx, null, m, requireMatchedConditions);
        else
            toSQLMatched(ctx, m, null, requireMatchedConditions);
    }

    static final void toSQLMatched(Context<?> ctx, MatchedClause update, MatchedClause delete, boolean requireMatchedConditions) {
        MatchedClause m = update != null ? update : delete;

        if (m.notMatchedBySource)
            ctx.formatSeparator()
               .visit(K_WHEN).sql(' ').visit(K_NOT).sql(' ').visit(K_MATCHED).sql(' ').visit(K_BY).sql(' ').visit(K_SOURCE);
        else
            ctx.formatSeparator()
               .visit(K_WHEN).sql(' ').visit(K_MATCHED);

        // [#7291] Standard SQL AND clause in updates
        if ((requireMatchedConditions || !(m.condition instanceof NoCondition)))
            ctx.sql(' ').visit(K_AND).sql(' ').visit(m.condition);

        ctx.sql(' ').visit(K_THEN);

        if (update != null) {
            ctx.sql(' ').visit(K_UPDATE).sql(' ').visit(K_SET)
               .formatIndentStart()
               .formatSeparator()
               .visit(update.updateMap)
               .formatIndentEnd();





        }

        if (delete != null) {






            ctx.sql(' ').visit(K_DELETE);
        }
    }

    static final void toSQLNotMatched(Context<?> ctx, NotMatchedClause m) {
        ctx.formatSeparator()
           .visit(K_WHEN).sql(' ')
           .visit(K_NOT).sql(' ')
           .visit(K_MATCHED).sql(' ');

        if (m.byTarget && !NO_SUPPORT_BY_TARGET.contains(ctx.dialect()))
            ctx.visit(K_BY).sql(' ').visit(K_TARGET).sql(' ');

        if (!(m.condition instanceof NoCondition))
            ctx.visit(K_AND).sql(' ').visit(m.condition).sql(' ');

        ctx.visit(K_THEN).sql(' ')
           .visit(K_INSERT);
        m.insertMap.toSQLReferenceKeys(ctx);
        ctx.formatSeparator()
           .start(MERGE_VALUES)
           .visit(K_VALUES).sql(' ');
        m.insertMap.toSQL92Values(ctx);
        ctx.end(MERGE_VALUES);





    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }

    private static final class MatchedClause
    extends
        AbstractQueryPart
    implements
        QOM.MergeMatched,
        QOM.MergeNotMatchedBySource
    {

        FieldMapForUpdate updateMap;
        boolean           delete;
        boolean           notMatchedBySource;
        Condition         condition;

        MatchedClause(Table<?> table, Condition condition) {
            this(table, condition, false, false);
        }

        MatchedClause(Table<?> table, Condition condition, boolean delete, boolean notMatchedBySource) {
            this(table, condition, delete, notMatchedBySource, new FieldMapForUpdate(table, SetClause.MERGE, MERGE_SET_ASSIGNMENT));
        }

        MatchedClause(Table<?> table, Condition condition, boolean delete, boolean notMatchedBySource, FieldMapForUpdate updateMap) {
            this.updateMap = updateMap;
            this.condition = condition == null ? noCondition() : condition;
            this.delete = delete;
            this.notMatchedBySource = notMatchedBySource;
        }

        @Override
        public final void accept(Context<?> ctx) {
            toSQLMatched(ctx, this, false);
        }

        private final MatchedClause create(
            Map<? extends FieldOrRow, ? extends FieldOrRowOrSelect> u,
            boolean d,
            Condition w
        ) {
            MatchedClause result = new MatchedClause(
                updateMap.table,
                w,
                d,
                notMatchedBySource,
                new FieldMapForUpdate(updateMap.table, SetClause.MERGE, MERGE_SET_ASSIGNMENT)
            );
            result.updateMap.putAll(u);
            return result;
        }

        private final MatchedClause copy(Consumer<? super MatchedClause> finisher) {
            MatchedClause result = create(updateMap, delete, condition);
            finisher.accept(result);
            return result;
        }

        @Override
        public final UnmodifiableMap<? extends FieldOrRow, ? extends FieldOrRowOrSelect> $updateSet() {
            return QOM.unmodifiable(updateMap);
        }

        @Override
        public final MergeMatched $updateSet(Map<? extends FieldOrRow, ? extends FieldOrRowOrSelect> updateSet) {
            return copy(c -> {
                c.delete = false;
                c.updateMap.clear();
                c.updateMap.putAll(updateSet);
            });
        }

        @Override
        public final boolean $delete() {
            return delete;
        }

        @Override
        public final MergeMatched $delete(boolean newDelete) {
            return copy(c -> {
                c.delete = newDelete;
                c.updateMap.clear();
            });
        }

        @Override
        public final Condition $where() {
            return condition instanceof NoCondition ? null : condition;
        }

        @Override
        public final MergeMatched $where(Condition newWhere) {
            return copy(c -> c.condition = newWhere == null ? noCondition() : newWhere);
        }





















    }

    private static final class NotMatchedClause
    extends
        AbstractQueryPart
    implements
        QOM.MergeNotMatched
    {

        FieldMapsForInsert insertMap;
        Condition          condition;
        boolean            byTarget;

        NotMatchedClause(Table<?> table, Condition condition, boolean byTarget) {
            this.insertMap = new FieldMapsForInsert(table);
            this.condition = condition == null ? noCondition() : condition;
            this.byTarget = byTarget;
        }

        @Override
        public final void accept(Context<?> ctx) {
            toSQLNotMatched(ctx, this);
        }

        private final NotMatchedClause create(
            Collection<? extends Field<?>> c,
            Collection<? extends Row> v,
            Condition w
        ) {
            NotMatchedClause result = new NotMatchedClause(
                insertMap.table,
                w,
                byTarget
            );
            result.insertMap.set(c, v);
            return result;
        }

        private final NotMatchedClause copy(Consumer<? super NotMatchedClause> finisher) {
            NotMatchedClause result = create($columns(), $values(), condition);
            finisher.accept(result);
            return result;
        }

        @Override
        public final UnmodifiableList<? extends Field<?>> $columns() {
            return insertMap.$columns();
        }

        @Override
        public final MergeNotMatched $columns(Collection<? extends Field<?>> newColumns) {
            return copy(c -> c.insertMap.set(newColumns, null));
        }

        @Override
        public final UnmodifiableList<? extends Row> $values() {
            return insertMap.$values();
        }

        @Override
        public final MergeNotMatched $values(Collection<? extends Row> newValues) {
            return copy(c -> c.insertMap.set(null, newValues));
        }

        @Override
        public final Condition $where() {
            return condition;
        }

        @Override
        public final MergeNotMatched $where(Condition newWhere) {
            return copy(c -> c.condition = newWhere);
        }





















    }

    // -------------------------------------------------------------------------
    // QOM API
    // -------------------------------------------------------------------------

    @Override
    public final With $with() {
        return with;
    }

    @Override
    public final Table<R> $into() {
        return table;
    }

    @Override
    public final Merge<?> $into(Table<?> newInto) {
        return copy(m -> {}, newInto);
    }

    @Override
    public final Condition $on() {
        return on.getWhereOrNull();
    }

    @Override
    public final Merge<R> $on(Condition newOn) {
        return copy(m -> {
            m.on.setWhere(newOn);
        });
    }

    @Override
    public final TableLike<?> $using() {
        return using;
    }

    @Override
    public final Merge<R> $using(TableLike<?> newUsing) {
        return copy(m -> {
            m.using = newUsing;
            m.usingDual = newUsing == null;
        });
    }

    @Override
    public final UnmodifiableList<? extends MergeMatched> $whenMatched() {
        return QOM.unmodifiable(matched);
    }

    @Override
    public final Merge<R> $whenMatched(Collection<? extends MergeMatched> newWhenMatched) {
        return copy(m -> m.whenMatched0(newWhenMatched));
    }

    private final void whenMatched0(Collection<? extends MergeMatched> newWhenMatched) {
        matched.clear();

        for (MergeMatched e : newWhenMatched)
            if (e instanceof MatchedClause c)
                matched.add(c);
            else
                throw new IllegalArgumentException("Custom MergeMatched implementations are not yet supported.");
    }

    @Override
    public final UnmodifiableList<? extends MergeNotMatched> $whenNotMatched() {
        return QOM.unmodifiable(notMatched);
    }

    @Override
    public final Merge<R> $whenNotMatched(Collection<? extends MergeNotMatched> newWhenNotMatched) {
        return copy(m -> m.whenNotMatched0(newWhenNotMatched));
    }

    private final void whenNotMatched0(Collection<? extends MergeNotMatched> newWhenNotMatched) {
        notMatched.clear();

        for (MergeNotMatched e : newWhenNotMatched)
            if (e instanceof NotMatchedClause c)
                notMatched.add(c);
            else
                throw new IllegalArgumentException("Custom MergeNotMatched implementations are not yet supported.");
    }

    @Override
    public final UnmodifiableList<? extends MergeNotMatchedBySource> $whenNotMatchedBySource() {
        return QOM.unmodifiable(notMatchedBySource);
    }

    @Override
    public final Merge<R> $whenNotMatchedBySource(Collection<? extends MergeNotMatchedBySource> newWhenNotMatchedBySource) {
        return copy(m -> m.whenNotMatchedBySource0(newWhenNotMatchedBySource));
    }

    private final void whenNotMatchedBySource0(Collection<? extends MergeNotMatchedBySource> newWhenNotMatchedBySource) {
        notMatchedBySource.clear();

        for (MergeNotMatchedBySource e : newWhenNotMatchedBySource)
            if (e instanceof MatchedClause c)
                notMatchedBySource.add(c);
            else
                throw new IllegalArgumentException("Custom MergeNotMatchedBySource implementations are not yet supported.");
    }

































}
