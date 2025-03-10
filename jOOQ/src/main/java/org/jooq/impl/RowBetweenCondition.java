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

import static org.jooq.Clause.CONDITION;
import static org.jooq.Clause.CONDITION_BETWEEN;
import static org.jooq.Clause.CONDITION_BETWEEN_SYMMETRIC;
import static org.jooq.Clause.CONDITION_NOT_BETWEEN;
import static org.jooq.Clause.CONDITION_NOT_BETWEEN_SYMMETRIC;
import static org.jooq.SQLDialect.*;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.Keywords.K_AND;
import static org.jooq.impl.Keywords.K_BETWEEN;
import static org.jooq.impl.Keywords.K_NOT;
import static org.jooq.impl.Keywords.K_SYMMETRIC;

import java.util.Set;

import org.jooq.BetweenAndStep1;
import org.jooq.BetweenAndStep2;
import org.jooq.BetweenAndStep3;
import org.jooq.BetweenAndStep4;
import org.jooq.BetweenAndStep5;
import org.jooq.BetweenAndStep6;
import org.jooq.BetweenAndStep7;
import org.jooq.BetweenAndStep8;
import org.jooq.BetweenAndStep9;
import org.jooq.BetweenAndStep10;
import org.jooq.BetweenAndStep11;
import org.jooq.BetweenAndStep12;
import org.jooq.BetweenAndStep13;
import org.jooq.BetweenAndStep14;
import org.jooq.BetweenAndStep15;
import org.jooq.BetweenAndStep16;
import org.jooq.BetweenAndStep17;
import org.jooq.BetweenAndStep18;
import org.jooq.BetweenAndStep19;
import org.jooq.BetweenAndStep20;
import org.jooq.BetweenAndStep21;
import org.jooq.BetweenAndStep22;
import org.jooq.BetweenAndStepN;
import org.jooq.Clause;
import org.jooq.Comparator;
import org.jooq.Condition;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record4;
import org.jooq.Record5;
import org.jooq.Record6;
import org.jooq.Record7;
import org.jooq.Record8;
import org.jooq.Record9;
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
import org.jooq.Record20;
import org.jooq.Record21;
import org.jooq.Record22;
import org.jooq.Row;
import org.jooq.Row1;
import org.jooq.Row2;
import org.jooq.Row3;
import org.jooq.Row4;
import org.jooq.Row5;
import org.jooq.Row6;
import org.jooq.Row7;
import org.jooq.Row8;
import org.jooq.Row9;
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
import org.jooq.Row20;
import org.jooq.Row21;
import org.jooq.Row22;
import org.jooq.RowN;
import org.jooq.SQLDialect;
import org.jooq.impl.QOM.UNotYetImplemented;

/**
 * @author Lukas Eder
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
final class RowBetweenCondition<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> extends AbstractCondition
implements

    // This BetweenAndStep implementation implements all types. Type-safety is
    // being checked through the type-safe API. No need for further checks here
    BetweenAndStep1,
    BetweenAndStep2,
    BetweenAndStep3,
    BetweenAndStep4,
    BetweenAndStep5,
    BetweenAndStep6,
    BetweenAndStep7,
    BetweenAndStep8,
    BetweenAndStep9,
    BetweenAndStep10,
    BetweenAndStep11,
    BetweenAndStep12,
    BetweenAndStep13,
    BetweenAndStep14,
    BetweenAndStep15,
    BetweenAndStep16,
    BetweenAndStep17,
    BetweenAndStep18,
    BetweenAndStep19,
    BetweenAndStep20,
    BetweenAndStep21,
    BetweenAndStep22,
    BetweenAndStepN,
    UNotYetImplemented {

    private static final Clause[]            CLAUSES_BETWEEN               = { CONDITION, CONDITION_BETWEEN };
    private static final Clause[]            CLAUSES_BETWEEN_SYMMETRIC     = { CONDITION, CONDITION_BETWEEN_SYMMETRIC };
    private static final Clause[]            CLAUSES_NOT_BETWEEN           = { CONDITION, CONDITION_NOT_BETWEEN };
    private static final Clause[]            CLAUSES_NOT_BETWEEN_SYMMETRIC = { CONDITION, CONDITION_NOT_BETWEEN_SYMMETRIC };
    private static final Set<SQLDialect>     NO_SUPPORT_SYMMETRIC          = SQLDialect.supportedBy(CLICKHOUSE, CUBRID, DERBY, DUCKDB, FIREBIRD, H2, IGNITE, MARIADB, MYSQL, SQLITE, TRINO);
    private static final Set<SQLDialect>     EMULATE_BETWEEN               = SQLDialect.supportedBy(CUBRID, DERBY, DUCKDB, FIREBIRD, MARIADB, MYSQL);

    private final boolean                    symmetric;
    private final boolean                    not;
    private final Row                        row;
    private final Row                        minValue;
    private Row                              maxValue;

    RowBetweenCondition(Row row, Row minValue, boolean not, boolean symmetric) {
        this.row = ((AbstractRow<?>) row).convertTo(minValue);
        this.minValue = ((AbstractRow<?>) minValue).convertTo(row);
        this.not = not;
        this.symmetric = symmetric;
    }

    RowBetweenCondition(Row row, Row minValue, boolean not, boolean symmetric, Row maxValue) {
        this(row, minValue, not, symmetric);

        this.maxValue = ((AbstractRow<?>) maxValue).convertTo(row);
    }

    // ------------------------------------------------------------------------
    // XXX: BetweenAndStep API
    // ------------------------------------------------------------------------

    @Override
    public final Condition and(Field f) {
        if (maxValue == null)
            return and(row(f));
        else
            return super.and(f);
    }

    @Override
    public final Condition and(Field field1, Field field2) {
        return and(row(field1, field2));
    }

    @Override
    public final Condition and(Field field1, Field field2, Field field3) {
        return and(row(field1, field2, field3));
    }

    @Override
    public final Condition and(Field field1, Field field2, Field field3, Field field4) {
        return and(row(field1, field2, field3, field4));
    }

    @Override
    public final Condition and(Field field1, Field field2, Field field3, Field field4, Field field5) {
        return and(row(field1, field2, field3, field4, field5));
    }

    @Override
    public final Condition and(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6) {
        return and(row(field1, field2, field3, field4, field5, field6));
    }

    @Override
    public final Condition and(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7) {
        return and(row(field1, field2, field3, field4, field5, field6, field7));
    }

    @Override
    public final Condition and(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8) {
        return and(row(field1, field2, field3, field4, field5, field6, field7, field8));
    }

    @Override
    public final Condition and(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9) {
        return and(row(field1, field2, field3, field4, field5, field6, field7, field8, field9));
    }

    @Override
    public final Condition and(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10) {
        return and(row(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10));
    }

    @Override
    public final Condition and(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11) {
        return and(row(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11));
    }

    @Override
    public final Condition and(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12) {
        return and(row(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12));
    }

    @Override
    public final Condition and(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13) {
        return and(row(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13));
    }

    @Override
    public final Condition and(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14) {
        return and(row(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14));
    }

    @Override
    public final Condition and(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15) {
        return and(row(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15));
    }

    @Override
    public final Condition and(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15, Field field16) {
        return and(row(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16));
    }

    @Override
    public final Condition and(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15, Field field16, Field field17) {
        return and(row(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17));
    }

    @Override
    public final Condition and(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15, Field field16, Field field17, Field field18) {
        return and(row(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18));
    }

    @Override
    public final Condition and(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15, Field field16, Field field17, Field field18, Field field19) {
        return and(row(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19));
    }

    @Override
    public final Condition and(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15, Field field16, Field field17, Field field18, Field field19, Field field20) {
        return and(row(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20));
    }

    @Override
    public final Condition and(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15, Field field16, Field field17, Field field18, Field field19, Field field20, Field field21) {
        return and(row(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21));
    }

    @Override
    public final Condition and(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15, Field field16, Field field17, Field field18, Field field19, Field field20, Field field21, Field field22) {
        return and(row(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21, field22));
    }

    @Override
    public final Condition and(Field<?>... fields) {
        return and(row(fields));
    }

    @Override
    public final Condition and(Object t1) {
        return and(new Object[] { t1 });
    }

    @Override
    public final Condition and(Object t1, Object t2) {
        return and(new Object[] { t1, t2 });
    }

    @Override
    public final Condition and(Object t1, Object t2, Object t3) {
        return and(new Object[] { t1, t2, t3 });
    }

    @Override
    public final Condition and(Object t1, Object t2, Object t3, Object t4) {
        return and(new Object[] { t1, t2, t3, t4 });
    }

    @Override
    public final Condition and(Object t1, Object t2, Object t3, Object t4, Object t5) {
        return and(new Object[] { t1, t2, t3, t4, t5 });
    }

    @Override
    public final Condition and(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6) {
        return and(new Object[] { t1, t2, t3, t4, t5, t6 });
    }

    @Override
    public final Condition and(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7) {
        return and(new Object[] { t1, t2, t3, t4, t5, t6, t7 });
    }

    @Override
    public final Condition and(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8) {
        return and(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8 });
    }

    @Override
    public final Condition and(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9) {
        return and(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9 });
    }

    @Override
    public final Condition and(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10) {
        return and(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10 });
    }

    @Override
    public final Condition and(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11) {
        return and(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11 });
    }

    @Override
    public final Condition and(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12) {
        return and(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12 });
    }

    @Override
    public final Condition and(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13) {
        return and(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13 });
    }

    @Override
    public final Condition and(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14) {
        return and(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14 });
    }

    @Override
    public final Condition and(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15) {
        return and(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15 });
    }

    @Override
    public final Condition and(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15, Object t16) {
        return and(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16 });
    }

    @Override
    public final Condition and(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15, Object t16, Object t17) {
        return and(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17 });
    }

    @Override
    public final Condition and(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15, Object t16, Object t17, Object t18) {
        return and(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18 });
    }

    @Override
    public final Condition and(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15, Object t16, Object t17, Object t18, Object t19) {
        return and(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19 });
    }

    @Override
    public final Condition and(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15, Object t16, Object t17, Object t18, Object t19, Object t20) {
        return and(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20 });
    }

    @Override
    public final Condition and(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15, Object t16, Object t17, Object t18, Object t19, Object t20, Object t21) {
        return and(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21 });
    }

    @Override
    public final Condition and(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15, Object t16, Object t17, Object t18, Object t19, Object t20, Object t21, Object t22) {
        return and(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22 });
    }

    @Override
    public final Condition and(Object... values) {
        return and(row(Tools.fieldsArray(values, row.fields())));
    }

    @Override
    public final Condition and(Row1 r) {
        return and0(r);
    }

    @Override
    public final Condition and(Row2 r) {
        return and0(r);
    }

    @Override
    public final Condition and(Row3 r) {
        return and0(r);
    }

    @Override
    public final Condition and(Row4 r) {
        return and0(r);
    }

    @Override
    public final Condition and(Row5 r) {
        return and0(r);
    }

    @Override
    public final Condition and(Row6 r) {
        return and0(r);
    }

    @Override
    public final Condition and(Row7 r) {
        return and0(r);
    }

    @Override
    public final Condition and(Row8 r) {
        return and0(r);
    }

    @Override
    public final Condition and(Row9 r) {
        return and0(r);
    }

    @Override
    public final Condition and(Row10 r) {
        return and0(r);
    }

    @Override
    public final Condition and(Row11 r) {
        return and0(r);
    }

    @Override
    public final Condition and(Row12 r) {
        return and0(r);
    }

    @Override
    public final Condition and(Row13 r) {
        return and0(r);
    }

    @Override
    public final Condition and(Row14 r) {
        return and0(r);
    }

    @Override
    public final Condition and(Row15 r) {
        return and0(r);
    }

    @Override
    public final Condition and(Row16 r) {
        return and0(r);
    }

    @Override
    public final Condition and(Row17 r) {
        return and0(r);
    }

    @Override
    public final Condition and(Row18 r) {
        return and0(r);
    }

    @Override
    public final Condition and(Row19 r) {
        return and0(r);
    }

    @Override
    public final Condition and(Row20 r) {
        return and0(r);
    }

    @Override
    public final Condition and(Row21 r) {
        return and0(r);
    }

    @Override
    public final Condition and(Row22 r) {
        return and0(r);
    }

    @Override
    public final Condition and(RowN r) {
        return and0(r);
    }

    @Override
    public final Condition and(Record1 record) {
        return and(record.valuesRow());
    }

    @Override
    public final Condition and(Record2 record) {
        return and(record.valuesRow());
    }

    @Override
    public final Condition and(Record3 record) {
        return and(record.valuesRow());
    }

    @Override
    public final Condition and(Record4 record) {
        return and(record.valuesRow());
    }

    @Override
    public final Condition and(Record5 record) {
        return and(record.valuesRow());
    }

    @Override
    public final Condition and(Record6 record) {
        return and(record.valuesRow());
    }

    @Override
    public final Condition and(Record7 record) {
        return and(record.valuesRow());
    }

    @Override
    public final Condition and(Record8 record) {
        return and(record.valuesRow());
    }

    @Override
    public final Condition and(Record9 record) {
        return and(record.valuesRow());
    }

    @Override
    public final Condition and(Record10 record) {
        return and(record.valuesRow());
    }

    @Override
    public final Condition and(Record11 record) {
        return and(record.valuesRow());
    }

    @Override
    public final Condition and(Record12 record) {
        return and(record.valuesRow());
    }

    @Override
    public final Condition and(Record13 record) {
        return and(record.valuesRow());
    }

    @Override
    public final Condition and(Record14 record) {
        return and(record.valuesRow());
    }

    @Override
    public final Condition and(Record15 record) {
        return and(record.valuesRow());
    }

    @Override
    public final Condition and(Record16 record) {
        return and(record.valuesRow());
    }

    @Override
    public final Condition and(Record17 record) {
        return and(record.valuesRow());
    }

    @Override
    public final Condition and(Record18 record) {
        return and(record.valuesRow());
    }

    @Override
    public final Condition and(Record19 record) {
        return and(record.valuesRow());
    }

    @Override
    public final Condition and(Record20 record) {
        return and(record.valuesRow());
    }

    @Override
    public final Condition and(Record21 record) {
        return and(record.valuesRow());
    }

    @Override
    public final Condition and(Record22 record) {
        return and(record.valuesRow());
    }

    @Override
    public final Condition and(Record record) {
        return and(new RowImplN(Tools.fieldsArray(record.intoArray(), record.fields())));
    }

    private final Condition and0(Row r) {
        this.maxValue = ((AbstractRow<?>) r).convertTo(row);
        return this;
    }

    // ------------------------------------------------------------------------
    // XXX: QueryPart API
    // ------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {

        // These dialects don't support the SYMMETRIC keyword at all
        if (symmetric && NO_SUPPORT_SYMMETRIC.contains(ctx.dialect())) {
            ctx.visit(not
                ? new RowBetweenCondition<>(row, minValue, true, false, maxValue).and(new RowBetweenCondition<>(row, maxValue, true, false, minValue))
                : new RowBetweenCondition<>(row, minValue, false, false, maxValue).or(new RowBetweenCondition<>(row, maxValue, false, false, minValue)));
        }

        // These dialects either don't support row value expressions, or they
        // Can't handle row value expressions with the BETWEEN predicate
        else if (row.size() > 1 && EMULATE_BETWEEN.contains(ctx.dialect())) {
            Condition result = AbstractRow.compare(row, Comparator.GREATER_OR_EQUAL, minValue).and(AbstractRow.compare(row, Comparator.LESS_OR_EQUAL, maxValue));

            if (not)
                result = result.not();

            ctx.visit(result);
        }










        else {
                           ctx.visit(row);
            if (not)       ctx.sql(" ").visit(K_NOT);
                           ctx.sql(" ").visit(K_BETWEEN);
            if (symmetric) ctx.sql(" ").visit(K_SYMMETRIC);
                           ctx.sql(" ").visit(minValue);
                           ctx.sql(" ").visit(K_AND);
                           ctx.sql(" ").visit(maxValue);
        }
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return not ? symmetric ? CLAUSES_NOT_BETWEEN_SYMMETRIC
                               : CLAUSES_NOT_BETWEEN
                   : symmetric ? CLAUSES_BETWEEN_SYMMETRIC
                               : CLAUSES_BETWEEN;
    }
}
