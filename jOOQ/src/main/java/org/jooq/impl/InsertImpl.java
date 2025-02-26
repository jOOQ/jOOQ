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

import static org.jooq.impl.DSL.condition;
import static org.jooq.impl.DSL.excluded;
import static org.jooq.impl.DSL.exists;
import static org.jooq.impl.DSL.not;
import static org.jooq.impl.DSL.notExists;
import static org.jooq.impl.Tools.EMPTY_FIELD;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Constraint;
import org.jooq.Field;
import org.jooq.FieldLike;
import org.jooq.FieldOrRow;
import org.jooq.FieldOrRowOrSelect;
import org.jooq.InsertOnConflictConditionStep;
import org.jooq.InsertOnConflictWhereIndexPredicateStep;
import org.jooq.InsertOnDuplicateSetMoreStep;
import org.jooq.InsertResultStep;
import org.jooq.InsertSetMoreStep;
import org.jooq.InsertSetStep;
import org.jooq.InsertValuesStepN;
import org.jooq.InsertValuesStep1;
import org.jooq.InsertValuesStep2;
import org.jooq.InsertValuesStep3;
import org.jooq.InsertValuesStep4;
import org.jooq.InsertValuesStep5;
import org.jooq.InsertValuesStep6;
import org.jooq.InsertValuesStep7;
import org.jooq.InsertValuesStep8;
import org.jooq.InsertValuesStep9;
import org.jooq.InsertValuesStep10;
import org.jooq.InsertValuesStep11;
import org.jooq.InsertValuesStep12;
import org.jooq.InsertValuesStep13;
import org.jooq.InsertValuesStep14;
import org.jooq.InsertValuesStep15;
import org.jooq.InsertValuesStep16;
import org.jooq.InsertValuesStep17;
import org.jooq.InsertValuesStep18;
import org.jooq.InsertValuesStep19;
import org.jooq.InsertValuesStep20;
import org.jooq.InsertValuesStep21;
import org.jooq.InsertValuesStep22;
import org.jooq.Name;
import org.jooq.Operator;
import org.jooq.QueryPart;
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
// ...
import org.jooq.Row;
import org.jooq.RowN;
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
import org.jooq.SQL;
import org.jooq.Select;
import org.jooq.SelectField;
import org.jooq.SelectFieldOrAsterisk;
import org.jooq.Table;
// ...
import org.jooq.UniqueKey;
import org.jooq.impl.QOM.Insert;
import org.jooq.impl.QOM.UnmodifiableList;
import org.jooq.impl.QOM.UnmodifiableMap;

/**
 * @author Lukas Eder
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
final class InsertImpl<R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>
    extends AbstractDelegatingDMLQuery<R, InsertQueryImpl<R>>
    implements

    // Cascading interface implementations for Insert behaviour
    InsertValuesStep1<R, T1>,
    InsertValuesStep2<R, T1, T2>,
    InsertValuesStep3<R, T1, T2, T3>,
    InsertValuesStep4<R, T1, T2, T3, T4>,
    InsertValuesStep5<R, T1, T2, T3, T4, T5>,
    InsertValuesStep6<R, T1, T2, T3, T4, T5, T6>,
    InsertValuesStep7<R, T1, T2, T3, T4, T5, T6, T7>,
    InsertValuesStep8<R, T1, T2, T3, T4, T5, T6, T7, T8>,
    InsertValuesStep9<R, T1, T2, T3, T4, T5, T6, T7, T8, T9>,
    InsertValuesStep10<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>,
    InsertValuesStep11<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>,
    InsertValuesStep12<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>,
    InsertValuesStep13<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>,
    InsertValuesStep14<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>,
    InsertValuesStep15<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>,
    InsertValuesStep16<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>,
    InsertValuesStep17<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>,
    InsertValuesStep18<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>,
    InsertValuesStep19<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>,
    InsertValuesStep20<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>,
    InsertValuesStep21<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>,
    InsertValuesStep22<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>,
    InsertValuesStepN<R>,
    InsertSetStep<R>,
    InsertSetMoreStep<R>,
    InsertOnDuplicateSetMoreStep<R>,
    InsertOnConflictWhereIndexPredicateStep<R>,
    InsertOnConflictConditionStep<R>,
    QOM.Insert<R>
{

    private final Table<R>    into;
    private Field<?>[]        fields;
    private boolean           onDuplicateKeyUpdate;
    private boolean           returningResult;

    /**
     * Whether {@link #where(Condition)} adds conditions to the <code>DO UPDATE/code> clause.
     */
    private transient boolean doUpdateWhere;

    InsertImpl(Configuration configuration, WithImpl with, Table<R> into) {
        this(configuration, with, into, Collections.emptyList());
    }

    InsertImpl(Configuration configuration, WithImpl with, Table<R> into, Collection<? extends Field<?>> fields) {
        super(new InsertQueryImpl<>(configuration, with, into));

        this.into = into;
        columns(fields);
    }

    // -------------------------------------------------------------------------
    // The DSL API
    // -------------------------------------------------------------------------

    @Override
    public final InsertImpl select(Select select) {
        getDelegate().setSelect(fields, select);
        return this;
    }

    @Override
    public final InsertImpl values(T1 value1) {
        return values(new Object[] { value1 });
    }

    @Override
    public final InsertImpl values(T1 value1, T2 value2) {
        return values(new Object[] { value1, value2 });
    }

    @Override
    public final InsertImpl values(T1 value1, T2 value2, T3 value3) {
        return values(new Object[] { value1, value2, value3 });
    }

    @Override
    public final InsertImpl values(T1 value1, T2 value2, T3 value3, T4 value4) {
        return values(new Object[] { value1, value2, value3, value4 });
    }

    @Override
    public final InsertImpl values(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5) {
        return values(new Object[] { value1, value2, value3, value4, value5 });
    }

    @Override
    public final InsertImpl values(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6 });
    }

    @Override
    public final InsertImpl values(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6, T7 value7) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7 });
    }

    @Override
    public final InsertImpl values(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6, T7 value7, T8 value8) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8 });
    }

    @Override
    public final InsertImpl values(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6, T7 value7, T8 value8, T9 value9) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9 });
    }

    @Override
    public final InsertImpl values(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6, T7 value7, T8 value8, T9 value9, T10 value10) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10 });
    }

    @Override
    public final InsertImpl values(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6, T7 value7, T8 value8, T9 value9, T10 value10, T11 value11) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11 });
    }

    @Override
    public final InsertImpl values(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6, T7 value7, T8 value8, T9 value9, T10 value10, T11 value11, T12 value12) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12 });
    }

    @Override
    public final InsertImpl values(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6, T7 value7, T8 value8, T9 value9, T10 value10, T11 value11, T12 value12, T13 value13) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13 });
    }

    @Override
    public final InsertImpl values(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6, T7 value7, T8 value8, T9 value9, T10 value10, T11 value11, T12 value12, T13 value13, T14 value14) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14 });
    }

    @Override
    public final InsertImpl values(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6, T7 value7, T8 value8, T9 value9, T10 value10, T11 value11, T12 value12, T13 value13, T14 value14, T15 value15) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15 });
    }

    @Override
    public final InsertImpl values(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6, T7 value7, T8 value8, T9 value9, T10 value10, T11 value11, T12 value12, T13 value13, T14 value14, T15 value15, T16 value16) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16 });
    }

    @Override
    public final InsertImpl values(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6, T7 value7, T8 value8, T9 value9, T10 value10, T11 value11, T12 value12, T13 value13, T14 value14, T15 value15, T16 value16, T17 value17) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17 });
    }

    @Override
    public final InsertImpl values(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6, T7 value7, T8 value8, T9 value9, T10 value10, T11 value11, T12 value12, T13 value13, T14 value14, T15 value15, T16 value16, T17 value17, T18 value18) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17, value18 });
    }

    @Override
    public final InsertImpl values(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6, T7 value7, T8 value8, T9 value9, T10 value10, T11 value11, T12 value12, T13 value13, T14 value14, T15 value15, T16 value16, T17 value17, T18 value18, T19 value19) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17, value18, value19 });
    }

    @Override
    public final InsertImpl values(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6, T7 value7, T8 value8, T9 value9, T10 value10, T11 value11, T12 value12, T13 value13, T14 value14, T15 value15, T16 value16, T17 value17, T18 value18, T19 value19, T20 value20) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17, value18, value19, value20 });
    }

    @Override
    public final InsertImpl values(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6, T7 value7, T8 value8, T9 value9, T10 value10, T11 value11, T12 value12, T13 value13, T14 value14, T15 value15, T16 value16, T17 value17, T18 value18, T19 value19, T20 value20, T21 value21) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17, value18, value19, value20, value21 });
    }

    @Override
    public final InsertImpl values(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6, T7 value7, T8 value8, T9 value9, T10 value10, T11 value11, T12 value12, T13 value13, T14 value14, T15 value15, T16 value16, T17 value17, T18 value18, T19 value19, T20 value20, T21 value21, T22 value22) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17, value18, value19, value20, value21, value22 });
    }

    @Override
    public final InsertImpl values(RowN values) {
        return values(values.fields());
    }

    @Override
    public final InsertImpl valuesOfRows(RowN... values) {
    	return valuesOfRows(Arrays.asList(values));
    }

    @Override
    public final InsertImpl values(Row1<T1> values) {
        return values(values.fields());
    }

    @Override
    public final InsertImpl valuesOfRows(Row1<T1>... values) {
    	return valuesOfRows(Arrays.asList(values));
    }

    @Override
    public final InsertImpl values(Row2<T1, T2> values) {
        return values(values.fields());
    }

    @Override
    public final InsertImpl valuesOfRows(Row2<T1, T2>... values) {
    	return valuesOfRows(Arrays.asList(values));
    }

    @Override
    public final InsertImpl values(Row3<T1, T2, T3> values) {
        return values(values.fields());
    }

    @Override
    public final InsertImpl valuesOfRows(Row3<T1, T2, T3>... values) {
    	return valuesOfRows(Arrays.asList(values));
    }

    @Override
    public final InsertImpl values(Row4<T1, T2, T3, T4> values) {
        return values(values.fields());
    }

    @Override
    public final InsertImpl valuesOfRows(Row4<T1, T2, T3, T4>... values) {
    	return valuesOfRows(Arrays.asList(values));
    }

    @Override
    public final InsertImpl values(Row5<T1, T2, T3, T4, T5> values) {
        return values(values.fields());
    }

    @Override
    public final InsertImpl valuesOfRows(Row5<T1, T2, T3, T4, T5>... values) {
    	return valuesOfRows(Arrays.asList(values));
    }

    @Override
    public final InsertImpl values(Row6<T1, T2, T3, T4, T5, T6> values) {
        return values(values.fields());
    }

    @Override
    public final InsertImpl valuesOfRows(Row6<T1, T2, T3, T4, T5, T6>... values) {
    	return valuesOfRows(Arrays.asList(values));
    }

    @Override
    public final InsertImpl values(Row7<T1, T2, T3, T4, T5, T6, T7> values) {
        return values(values.fields());
    }

    @Override
    public final InsertImpl valuesOfRows(Row7<T1, T2, T3, T4, T5, T6, T7>... values) {
    	return valuesOfRows(Arrays.asList(values));
    }

    @Override
    public final InsertImpl values(Row8<T1, T2, T3, T4, T5, T6, T7, T8> values) {
        return values(values.fields());
    }

    @Override
    public final InsertImpl valuesOfRows(Row8<T1, T2, T3, T4, T5, T6, T7, T8>... values) {
    	return valuesOfRows(Arrays.asList(values));
    }

    @Override
    public final InsertImpl values(Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> values) {
        return values(values.fields());
    }

    @Override
    public final InsertImpl valuesOfRows(Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9>... values) {
    	return valuesOfRows(Arrays.asList(values));
    }

    @Override
    public final InsertImpl values(Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> values) {
        return values(values.fields());
    }

    @Override
    public final InsertImpl valuesOfRows(Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>... values) {
    	return valuesOfRows(Arrays.asList(values));
    }

    @Override
    public final InsertImpl values(Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> values) {
        return values(values.fields());
    }

    @Override
    public final InsertImpl valuesOfRows(Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>... values) {
    	return valuesOfRows(Arrays.asList(values));
    }

    @Override
    public final InsertImpl values(Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> values) {
        return values(values.fields());
    }

    @Override
    public final InsertImpl valuesOfRows(Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>... values) {
    	return valuesOfRows(Arrays.asList(values));
    }

    @Override
    public final InsertImpl values(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> values) {
        return values(values.fields());
    }

    @Override
    public final InsertImpl valuesOfRows(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>... values) {
    	return valuesOfRows(Arrays.asList(values));
    }

    @Override
    public final InsertImpl values(Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> values) {
        return values(values.fields());
    }

    @Override
    public final InsertImpl valuesOfRows(Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>... values) {
    	return valuesOfRows(Arrays.asList(values));
    }

    @Override
    public final InsertImpl values(Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> values) {
        return values(values.fields());
    }

    @Override
    public final InsertImpl valuesOfRows(Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>... values) {
    	return valuesOfRows(Arrays.asList(values));
    }

    @Override
    public final InsertImpl values(Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> values) {
        return values(values.fields());
    }

    @Override
    public final InsertImpl valuesOfRows(Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>... values) {
    	return valuesOfRows(Arrays.asList(values));
    }

    @Override
    public final InsertImpl values(Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> values) {
        return values(values.fields());
    }

    @Override
    public final InsertImpl valuesOfRows(Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>... values) {
    	return valuesOfRows(Arrays.asList(values));
    }

    @Override
    public final InsertImpl values(Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> values) {
        return values(values.fields());
    }

    @Override
    public final InsertImpl valuesOfRows(Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>... values) {
    	return valuesOfRows(Arrays.asList(values));
    }

    @Override
    public final InsertImpl values(Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> values) {
        return values(values.fields());
    }

    @Override
    public final InsertImpl valuesOfRows(Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>... values) {
    	return valuesOfRows(Arrays.asList(values));
    }

    @Override
    public final InsertImpl values(Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> values) {
        return values(values.fields());
    }

    @Override
    public final InsertImpl valuesOfRows(Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>... values) {
    	return valuesOfRows(Arrays.asList(values));
    }

    @Override
    public final InsertImpl values(Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> values) {
        return values(values.fields());
    }

    @Override
    public final InsertImpl valuesOfRows(Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>... values) {
    	return valuesOfRows(Arrays.asList(values));
    }

    @Override
    public final InsertImpl values(Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> values) {
        return values(values.fields());
    }

    @Override
    public final InsertImpl valuesOfRows(Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>... values) {
    	return valuesOfRows(Arrays.asList(values));
    }

    @Override
    public final InsertImpl values(Record values) {
        return values(values.intoArray());
    }

    @Override
    public final InsertImpl valuesOfRecords(Record... values) {
    	return valuesOfRecords(Arrays.asList(values));
    }

    @Override
    public final InsertImpl values(Record1<T1> values) {
        return values(values.intoArray());
    }

    @Override
    public final InsertImpl valuesOfRecords(Record1<T1>... values) {
    	return valuesOfRecords(Arrays.asList(values));
    }

    @Override
    public final InsertImpl values(Record2<T1, T2> values) {
        return values(values.intoArray());
    }

    @Override
    public final InsertImpl valuesOfRecords(Record2<T1, T2>... values) {
    	return valuesOfRecords(Arrays.asList(values));
    }

    @Override
    public final InsertImpl values(Record3<T1, T2, T3> values) {
        return values(values.intoArray());
    }

    @Override
    public final InsertImpl valuesOfRecords(Record3<T1, T2, T3>... values) {
    	return valuesOfRecords(Arrays.asList(values));
    }

    @Override
    public final InsertImpl values(Record4<T1, T2, T3, T4> values) {
        return values(values.intoArray());
    }

    @Override
    public final InsertImpl valuesOfRecords(Record4<T1, T2, T3, T4>... values) {
    	return valuesOfRecords(Arrays.asList(values));
    }

    @Override
    public final InsertImpl values(Record5<T1, T2, T3, T4, T5> values) {
        return values(values.intoArray());
    }

    @Override
    public final InsertImpl valuesOfRecords(Record5<T1, T2, T3, T4, T5>... values) {
    	return valuesOfRecords(Arrays.asList(values));
    }

    @Override
    public final InsertImpl values(Record6<T1, T2, T3, T4, T5, T6> values) {
        return values(values.intoArray());
    }

    @Override
    public final InsertImpl valuesOfRecords(Record6<T1, T2, T3, T4, T5, T6>... values) {
    	return valuesOfRecords(Arrays.asList(values));
    }

    @Override
    public final InsertImpl values(Record7<T1, T2, T3, T4, T5, T6, T7> values) {
        return values(values.intoArray());
    }

    @Override
    public final InsertImpl valuesOfRecords(Record7<T1, T2, T3, T4, T5, T6, T7>... values) {
    	return valuesOfRecords(Arrays.asList(values));
    }

    @Override
    public final InsertImpl values(Record8<T1, T2, T3, T4, T5, T6, T7, T8> values) {
        return values(values.intoArray());
    }

    @Override
    public final InsertImpl valuesOfRecords(Record8<T1, T2, T3, T4, T5, T6, T7, T8>... values) {
    	return valuesOfRecords(Arrays.asList(values));
    }

    @Override
    public final InsertImpl values(Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9> values) {
        return values(values.intoArray());
    }

    @Override
    public final InsertImpl valuesOfRecords(Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9>... values) {
    	return valuesOfRecords(Arrays.asList(values));
    }

    @Override
    public final InsertImpl values(Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> values) {
        return values(values.intoArray());
    }

    @Override
    public final InsertImpl valuesOfRecords(Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>... values) {
    	return valuesOfRecords(Arrays.asList(values));
    }

    @Override
    public final InsertImpl values(Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> values) {
        return values(values.intoArray());
    }

    @Override
    public final InsertImpl valuesOfRecords(Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>... values) {
    	return valuesOfRecords(Arrays.asList(values));
    }

    @Override
    public final InsertImpl values(Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> values) {
        return values(values.intoArray());
    }

    @Override
    public final InsertImpl valuesOfRecords(Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>... values) {
    	return valuesOfRecords(Arrays.asList(values));
    }

    @Override
    public final InsertImpl values(Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> values) {
        return values(values.intoArray());
    }

    @Override
    public final InsertImpl valuesOfRecords(Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>... values) {
    	return valuesOfRecords(Arrays.asList(values));
    }

    @Override
    public final InsertImpl values(Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> values) {
        return values(values.intoArray());
    }

    @Override
    public final InsertImpl valuesOfRecords(Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>... values) {
    	return valuesOfRecords(Arrays.asList(values));
    }

    @Override
    public final InsertImpl values(Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> values) {
        return values(values.intoArray());
    }

    @Override
    public final InsertImpl valuesOfRecords(Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>... values) {
    	return valuesOfRecords(Arrays.asList(values));
    }

    @Override
    public final InsertImpl values(Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> values) {
        return values(values.intoArray());
    }

    @Override
    public final InsertImpl valuesOfRecords(Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>... values) {
    	return valuesOfRecords(Arrays.asList(values));
    }

    @Override
    public final InsertImpl values(Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> values) {
        return values(values.intoArray());
    }

    @Override
    public final InsertImpl valuesOfRecords(Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>... values) {
    	return valuesOfRecords(Arrays.asList(values));
    }

    @Override
    public final InsertImpl values(Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> values) {
        return values(values.intoArray());
    }

    @Override
    public final InsertImpl valuesOfRecords(Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>... values) {
    	return valuesOfRecords(Arrays.asList(values));
    }

    @Override
    public final InsertImpl values(Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> values) {
        return values(values.intoArray());
    }

    @Override
    public final InsertImpl valuesOfRecords(Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>... values) {
    	return valuesOfRecords(Arrays.asList(values));
    }

    @Override
    public final InsertImpl values(Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> values) {
        return values(values.intoArray());
    }

    @Override
    public final InsertImpl valuesOfRecords(Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>... values) {
    	return valuesOfRecords(Arrays.asList(values));
    }

    @Override
    public final InsertImpl values(Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> values) {
        return values(values.intoArray());
    }

    @Override
    public final InsertImpl valuesOfRecords(Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>... values) {
    	return valuesOfRecords(Arrays.asList(values));
    }

    @Override
    public final InsertImpl values(Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> values) {
        return values(values.intoArray());
    }

    @Override
    public final InsertImpl valuesOfRecords(Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>... values) {
    	return valuesOfRecords(Arrays.asList(values));
    }

    @Override
    public final InsertImpl valuesOfRows(Collection values) {
    	for (Object row : values)
    	    values(((Row) row).fields());

        return this;
    }

    @Override
    public final InsertImpl valuesOfRecords(Collection values) {
    	for (Object record : values)
    	    values(((Record) record).intoArray());

        return this;
    }

    @Override
    public final InsertImpl values(Object... values) {

        // [#10655] Empty INSERT INTO t VALUES () clause
        if (values.length == 0)
            return defaultValues();

        // [#4629] Plain SQL INSERT INTO t VALUES (a, b, c) statements don't know the insert columns
        else if (!Tools.isEmpty(fields) && fields.length != values.length)
            throw new IllegalArgumentException("The number of values (" + values.length + ") must match the number of fields (" + fields.length + ")");

        getDelegate().newRecord();
        if (Tools.isEmpty(fields))
            for (int i = 0; i < values.length; i++)
                addValue(getDelegate(), null, i, values[i]);
        else
            for (int i = 0; i < fields.length; i++)
                addValue(getDelegate(), fields.length > 0 ? fields[i] : null, i, values[i]);

        return this;
    }

    @Override
    public final InsertImpl values(Collection<?> values) {
        return values(values.toArray());
    }

    private final <T> void addValue(InsertQueryImpl<R> delegate, Field<T> field, int index, Object object) {

        // [#1343] Only convert non-jOOQ objects
        // [#8606] The column index is relevant when adding a value to a plain SQL multi row INSERT
        //         statement that does not have any field list.
        if (object instanceof Field f)
            delegate.addValue(field, index, f);
        else if (object instanceof FieldLike f)
            delegate.addValue(field, index, f.asField());
        else if (field != null)
            delegate.addValue(field, index, field.getDataType().convert(object));

        // [#4629] Plain SQL INSERT INTO t VALUES (a, b, c) statements don't know the insert columns
        else
            delegate.addValue(field, index, (T) object);
    }

    @Override
    public final InsertImpl values(Field<T1> value1) {
        return values(new Field[] { value1 });
    }

    @Override
    public final InsertImpl values(Field<T1> value1, Field<T2> value2) {
        return values(new Field[] { value1, value2 });
    }

    @Override
    public final InsertImpl values(Field<T1> value1, Field<T2> value2, Field<T3> value3) {
        return values(new Field[] { value1, value2, value3 });
    }

    @Override
    public final InsertImpl values(Field<T1> value1, Field<T2> value2, Field<T3> value3, Field<T4> value4) {
        return values(new Field[] { value1, value2, value3, value4 });
    }

    @Override
    public final InsertImpl values(Field<T1> value1, Field<T2> value2, Field<T3> value3, Field<T4> value4, Field<T5> value5) {
        return values(new Field[] { value1, value2, value3, value4, value5 });
    }

    @Override
    public final InsertImpl values(Field<T1> value1, Field<T2> value2, Field<T3> value3, Field<T4> value4, Field<T5> value5, Field<T6> value6) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6 });
    }

    @Override
    public final InsertImpl values(Field<T1> value1, Field<T2> value2, Field<T3> value3, Field<T4> value4, Field<T5> value5, Field<T6> value6, Field<T7> value7) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7 });
    }

    @Override
    public final InsertImpl values(Field<T1> value1, Field<T2> value2, Field<T3> value3, Field<T4> value4, Field<T5> value5, Field<T6> value6, Field<T7> value7, Field<T8> value8) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8 });
    }

    @Override
    public final InsertImpl values(Field<T1> value1, Field<T2> value2, Field<T3> value3, Field<T4> value4, Field<T5> value5, Field<T6> value6, Field<T7> value7, Field<T8> value8, Field<T9> value9) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9 });
    }

    @Override
    public final InsertImpl values(Field<T1> value1, Field<T2> value2, Field<T3> value3, Field<T4> value4, Field<T5> value5, Field<T6> value6, Field<T7> value7, Field<T8> value8, Field<T9> value9, Field<T10> value10) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10 });
    }

    @Override
    public final InsertImpl values(Field<T1> value1, Field<T2> value2, Field<T3> value3, Field<T4> value4, Field<T5> value5, Field<T6> value6, Field<T7> value7, Field<T8> value8, Field<T9> value9, Field<T10> value10, Field<T11> value11) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11 });
    }

    @Override
    public final InsertImpl values(Field<T1> value1, Field<T2> value2, Field<T3> value3, Field<T4> value4, Field<T5> value5, Field<T6> value6, Field<T7> value7, Field<T8> value8, Field<T9> value9, Field<T10> value10, Field<T11> value11, Field<T12> value12) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12 });
    }

    @Override
    public final InsertImpl values(Field<T1> value1, Field<T2> value2, Field<T3> value3, Field<T4> value4, Field<T5> value5, Field<T6> value6, Field<T7> value7, Field<T8> value8, Field<T9> value9, Field<T10> value10, Field<T11> value11, Field<T12> value12, Field<T13> value13) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13 });
    }

    @Override
    public final InsertImpl values(Field<T1> value1, Field<T2> value2, Field<T3> value3, Field<T4> value4, Field<T5> value5, Field<T6> value6, Field<T7> value7, Field<T8> value8, Field<T9> value9, Field<T10> value10, Field<T11> value11, Field<T12> value12, Field<T13> value13, Field<T14> value14) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14 });
    }

    @Override
    public final InsertImpl values(Field<T1> value1, Field<T2> value2, Field<T3> value3, Field<T4> value4, Field<T5> value5, Field<T6> value6, Field<T7> value7, Field<T8> value8, Field<T9> value9, Field<T10> value10, Field<T11> value11, Field<T12> value12, Field<T13> value13, Field<T14> value14, Field<T15> value15) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15 });
    }

    @Override
    public final InsertImpl values(Field<T1> value1, Field<T2> value2, Field<T3> value3, Field<T4> value4, Field<T5> value5, Field<T6> value6, Field<T7> value7, Field<T8> value8, Field<T9> value9, Field<T10> value10, Field<T11> value11, Field<T12> value12, Field<T13> value13, Field<T14> value14, Field<T15> value15, Field<T16> value16) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16 });
    }

    @Override
    public final InsertImpl values(Field<T1> value1, Field<T2> value2, Field<T3> value3, Field<T4> value4, Field<T5> value5, Field<T6> value6, Field<T7> value7, Field<T8> value8, Field<T9> value9, Field<T10> value10, Field<T11> value11, Field<T12> value12, Field<T13> value13, Field<T14> value14, Field<T15> value15, Field<T16> value16, Field<T17> value17) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17 });
    }

    @Override
    public final InsertImpl values(Field<T1> value1, Field<T2> value2, Field<T3> value3, Field<T4> value4, Field<T5> value5, Field<T6> value6, Field<T7> value7, Field<T8> value8, Field<T9> value9, Field<T10> value10, Field<T11> value11, Field<T12> value12, Field<T13> value13, Field<T14> value14, Field<T15> value15, Field<T16> value16, Field<T17> value17, Field<T18> value18) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17, value18 });
    }

    @Override
    public final InsertImpl values(Field<T1> value1, Field<T2> value2, Field<T3> value3, Field<T4> value4, Field<T5> value5, Field<T6> value6, Field<T7> value7, Field<T8> value8, Field<T9> value9, Field<T10> value10, Field<T11> value11, Field<T12> value12, Field<T13> value13, Field<T14> value14, Field<T15> value15, Field<T16> value16, Field<T17> value17, Field<T18> value18, Field<T19> value19) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17, value18, value19 });
    }

    @Override
    public final InsertImpl values(Field<T1> value1, Field<T2> value2, Field<T3> value3, Field<T4> value4, Field<T5> value5, Field<T6> value6, Field<T7> value7, Field<T8> value8, Field<T9> value9, Field<T10> value10, Field<T11> value11, Field<T12> value12, Field<T13> value13, Field<T14> value14, Field<T15> value15, Field<T16> value16, Field<T17> value17, Field<T18> value18, Field<T19> value19, Field<T20> value20) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17, value18, value19, value20 });
    }

    @Override
    public final InsertImpl values(Field<T1> value1, Field<T2> value2, Field<T3> value3, Field<T4> value4, Field<T5> value5, Field<T6> value6, Field<T7> value7, Field<T8> value8, Field<T9> value9, Field<T10> value10, Field<T11> value11, Field<T12> value12, Field<T13> value13, Field<T14> value14, Field<T15> value15, Field<T16> value16, Field<T17> value17, Field<T18> value18, Field<T19> value19, Field<T20> value20, Field<T21> value21) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17, value18, value19, value20, value21 });
    }

    @Override
    public final InsertImpl values(Field<T1> value1, Field<T2> value2, Field<T3> value3, Field<T4> value4, Field<T5> value5, Field<T6> value6, Field<T7> value7, Field<T8> value8, Field<T9> value9, Field<T10> value10, Field<T11> value11, Field<T12> value12, Field<T13> value13, Field<T14> value14, Field<T15> value15, Field<T16> value16, Field<T17> value17, Field<T18> value18, Field<T19> value19, Field<T20> value20, Field<T21> value21, Field<T22> value22) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17, value18, value19, value20, value21, value22 });
    }

    @Override
    public final InsertImpl values(Field<?>... values) {

        // [#10655] Empty INSERT INTO t VALUES () clause
        if (values.length == 0)
            return defaultValues();

        // [#4629] Plain SQL INSERT INTO t VALUES (a, b, c) statements don't know the insert columns
        else if (!Tools.isEmpty(fields) && fields.length != values.length)
            throw new IllegalArgumentException("The number of values (" + values.length + ") must match the number of fields (" + fields.length + ")");

        getDelegate().newRecord();

        // javac has trouble when inferring Object for T. Use Void instead
        if (Tools.isEmpty(fields))
            for (int i = 0; i < values.length; i++)
                addValue(getDelegate(), (Field<Void>) null, i, (Field<Void>) values[i]);
        else
            for (int i = 0; i < fields.length; i++)
                addValue(getDelegate(), (Field<Void>) fields[i], i, (Field<Void>) values[i]);

        return this;
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1> InsertImpl columns(Field<T1> field1) {
        return columns(new Field[] { field1 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2> InsertImpl columns(Field<T1> field1, Field<T2> field2) {
        return columns(new Field[] { field1, field2 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3> InsertImpl columns(Field<T1> field1, Field<T2> field2, Field<T3> field3) {
        return columns(new Field[] { field1, field2, field3 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4> InsertImpl columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4) {
        return columns(new Field[] { field1, field2, field3, field4 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4, T5> InsertImpl columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5) {
        return columns(new Field[] { field1, field2, field3, field4, field5 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4, T5, T6> InsertImpl columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6) {
        return columns(new Field[] { field1, field2, field3, field4, field5, field6 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4, T5, T6, T7> InsertImpl columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7) {
        return columns(new Field[] { field1, field2, field3, field4, field5, field6, field7 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4, T5, T6, T7, T8> InsertImpl columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8) {
        return columns(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9> InsertImpl columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9) {
        return columns(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> InsertImpl columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10) {
        return columns(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> InsertImpl columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11) {
        return columns(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> InsertImpl columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12) {
        return columns(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> InsertImpl columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13) {
        return columns(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> InsertImpl columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14) {
        return columns(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> InsertImpl columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15) {
        return columns(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> InsertImpl columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16) {
        return columns(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> InsertImpl columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17) {
        return columns(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> InsertImpl columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18) {
        return columns(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> InsertImpl columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19) {
        return columns(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> InsertImpl columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20) {
        return columns(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> InsertImpl columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21) {
        return columns(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> InsertImpl columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21, Field<T22> field22) {
        return columns(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21, field22 });
    }

    @Override
    public final InsertImpl columns(Field<?>... f) {
        this.fields = Tools.isEmpty(f) ? into.fields() : f;
        return this;
    }

    @Override
    public final InsertImpl columns(Collection<? extends Field<?>> f) {
        return columns(f.toArray(EMPTY_FIELD));
    }

    /**
     * Add an empty record with default values.
     */
    @Override
    public final InsertImpl defaultValues() {
        getDelegate().setDefaultValues();
        return this;
    }

    @Override
    public final InsertImpl doUpdate() {
        doUpdateWhere = true;
        return onDuplicateKeyUpdate();
    }

    @Override
    public final InsertImpl doNothing() {
        doUpdateWhere = true;
        return onDuplicateKeyIgnore();
    }

    @Override
    public final InsertImpl onConflictOnConstraint(Constraint constraint) {
        getDelegate().onConflictOnConstraint(constraint);
        return this;
    }

    @Override
    public final InsertImpl onConflictOnConstraint(Name constraint) {
        getDelegate().onConflictOnConstraint(constraint);
        return this;
    }

    @Override
    public final InsertImpl onConflictOnConstraint(UniqueKey<R> constraint) {
        getDelegate().onConflictOnConstraint(constraint);
        return this;
    }

    @Override
    public final InsertImpl onConflict(Field<?>... keys) {
        return onConflict(Arrays.asList(keys));
    }

    @Override
    public final InsertImpl onConflict(Collection<? extends Field<?>> keys) {
        getDelegate().onConflict(keys);
        return this;
    }

    @Override
    public final InsertImpl onConflictDoNothing() {
        doUpdateWhere = true;
        onConflict().doNothing();
        return this;
    }

    @Override
    public final InsertImpl onDuplicateKeyUpdate() {
        doUpdateWhere = true;
        onDuplicateKeyUpdate = true;
        getDelegate().onDuplicateKeyUpdate(true);
        return this;
    }

    @Override
    public final InsertImpl onDuplicateKeyIgnore() {
        doUpdateWhere = true;
        getDelegate().onDuplicateKeyIgnore(true);
        return this;
    }

    @Override
    public final <T> InsertImpl set(Field<T> field, T value) {
        if (onDuplicateKeyUpdate)
            getDelegate().addValueForUpdate(field, value);
        else
            getDelegate().addValue(field, value);

        return this;
    }

    @Override
    public final <T> InsertImpl set(Field<T> field, Field<T> value) {
        if (onDuplicateKeyUpdate)
            getDelegate().addValueForUpdate(field, value);
        else
            getDelegate().addValue(field, value);

        return this;
    }

    @Override
    public final <T> InsertImpl set(Field<T> field, Select<? extends Record1<T>> value) {
        return set(field, value.asField());
    }

    @Override
    public final <T> InsertImpl setNull(Field<T> field) {
        return set(field, (T) null);
    }

    @Override
    public final InsertImpl set(Map<?, ?> map) {
        if (onDuplicateKeyUpdate)
            getDelegate().addValuesForUpdate(map);
        else
            getDelegate().addValues(map);

        return this;
    }

    @Override
    public final InsertImpl set(Record record) {
        return set(Tools.mapOfChangedValues(record));
    }

    @Override
    public final InsertImpl set(Record... records) {
        return set(Arrays.asList(records));
    }

    @Override
    public final InsertImpl set(Collection<? extends Record> records) {
        for (Record record : records)

            // [#6373] [#7322] [#15455]
            // A trailing newRecord() call is a no-op, but if users call set() twice with
            // collections of records, then the expectation is for the two calls to be
            // complete, i.e. no additional newRecord() calls should be needed in between
            // the two
            set(record).newRecord();

        return this;
    }

    @Override
    public final InsertImpl setAllToExcluded() {

        // [#14599] Don't use this.fields here, because in the INSERT .. SET case, that
        //          auxiliary array doesn't contain the actual columns
        for (Field<?> field : getDelegate().$columns())
            set(field, (Field) excluded(field));

        return this;
    }

    @Override
    public final InsertImpl and(Condition condition) {
        getDelegate().addConditions(condition);
        return this;
    }

    @Override
    public final InsertImpl and(Field<Boolean> condition) {
        return and(condition(condition));
    }

    @Override
    public final InsertImpl and(SQL sql) {
        return and(condition(sql));
    }

    @Override
    public final InsertImpl and(String sql) {
        return and(condition(sql));
    }

    @Override
    public final InsertImpl and(String sql, Object... bindings) {
        return and(condition(sql, bindings));
    }

    @Override
    public final InsertImpl and(String sql, QueryPart... parts) {
        return and(condition(sql, parts));
    }

    @Override
    public final InsertImpl andNot(Condition condition) {
        return and(not(condition));
    }

    @Override
    public final InsertImpl andNot(Field<Boolean> condition) {
        return and(not(condition(condition)));
    }

    @Override
    public final InsertImpl andExists(Select<?> select) {
        return and(exists(select));
    }

    @Override
    public final InsertImpl andNotExists(Select<?> select) {
        return and(notExists(select));
    }

    @Override
    public final InsertImpl or(Condition condition) {
        getDelegate().addConditions(Operator.OR, condition);
        return this;
    }

    @Override
    public final InsertImpl or(Field<Boolean> condition) {
        return or(condition(condition));
    }

    @Override
    public final InsertImpl or(SQL sql) {
        return or(condition(sql));
    }

    @Override
    public final InsertImpl or(String sql) {
        return or(condition(sql));
    }

    @Override
    public final InsertImpl or(String sql, Object... bindings) {
        return or(condition(sql, bindings));
    }

    @Override
    public final InsertImpl or(String sql, QueryPart... parts) {
        return or(condition(sql, parts));
    }

    @Override
    public final InsertImpl orNot(Condition condition) {
        return or(not(condition));
    }

    @Override
    public final InsertImpl orNot(Field<Boolean> condition) {
        return or(not(condition(condition)));
    }

    @Override
    public final InsertImpl orExists(Select<?> select) {
        return or(exists(select));
    }

    @Override
    public final InsertImpl orNotExists(Select<?> select) {
        return or(notExists(select));
    }

    @Override
    public final InsertImpl where(Condition condition) {
        if (doUpdateWhere)
            getDelegate().addConditions(condition);
        else
            getDelegate().onConflictWhere(condition);

        return this;
    }

    @Override
    public final InsertImpl where(Condition... conditions) {
        if (doUpdateWhere)
            getDelegate().addConditions(conditions);
        else
            getDelegate().onConflictWhere(DSL.and(conditions));

        return this;
    }

    @Override
    public final InsertImpl where(Collection<? extends Condition> conditions) {
        if (doUpdateWhere)
            getDelegate().addConditions(conditions);
        else
            getDelegate().onConflictWhere(DSL.and(conditions));

        return this;
    }

    @Override
    public final InsertImpl where(Field<Boolean> field) {
        return where(condition(field));
    }

    @Override
    public final InsertImpl where(SQL sql) {
        return where(condition(sql));
    }

    @Override
    public final InsertImpl where(String sql) {
        return where(condition(sql));
    }

    @Override
    public final InsertImpl where(String sql, Object... bindings) {
        return where(condition(sql, bindings));
    }

    @Override
    public final InsertImpl where(String sql, QueryPart... parts) {
        return where(condition(sql, parts));
    }

    @Override
    public final InsertImpl whereExists(Select<?> select) {
        return where(exists(select));
    }

    @Override
    public final InsertImpl whereNotExists(Select<?> select) {
        return where(notExists(select));
    }

    @Override
    public final InsertImpl newRecord() {
        getDelegate().newRecord();
        return this;
    }

    @Override
    public final InsertResultStep<R> returning() {
        getDelegate().setReturning();
        return new InsertAsResultQuery<>(getDelegate(), returningResult);
    }

    @Override
    public final InsertResultStep<R> returning(SelectFieldOrAsterisk... f) {
        getDelegate().setReturning(f);
        return new InsertAsResultQuery<>(getDelegate(), returningResult);
    }

    @Override
    public final InsertResultStep<R> returning(Collection<? extends SelectFieldOrAsterisk> f) {
        getDelegate().setReturning(f);
        return new InsertAsResultQuery<>(getDelegate(), returningResult);
    }

    @Override
    public final InsertResultStep<Record> returningResult(SelectFieldOrAsterisk... f) {
        returningResult = true;
        getDelegate().setReturning(f);
        return new InsertAsResultQuery(getDelegate(), returningResult);
    }

    @Override
    public final InsertResultStep<Record> returningResult(Collection<? extends SelectFieldOrAsterisk> f) {
        returningResult = true;
        getDelegate().setReturning(f);
        return new InsertAsResultQuery(getDelegate(), returningResult);
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1> InsertResultStep<Record1<T1>> returningResult(SelectField<T1> field1) {
        return (InsertResultStep) returningResult(new SelectField[] { field1 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2> InsertResultStep<Record2<T1, T2>> returningResult(SelectField<T1> field1, SelectField<T2> field2) {
        return (InsertResultStep) returningResult(new SelectField[] { field1, field2 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3> InsertResultStep<Record3<T1, T2, T3>> returningResult(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3) {
        return (InsertResultStep) returningResult(new SelectField[] { field1, field2, field3 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4> InsertResultStep<Record4<T1, T2, T3, T4>> returningResult(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4) {
        return (InsertResultStep) returningResult(new SelectField[] { field1, field2, field3, field4 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4, T5> InsertResultStep<Record5<T1, T2, T3, T4, T5>> returningResult(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5) {
        return (InsertResultStep) returningResult(new SelectField[] { field1, field2, field3, field4, field5 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4, T5, T6> InsertResultStep<Record6<T1, T2, T3, T4, T5, T6>> returningResult(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6) {
        return (InsertResultStep) returningResult(new SelectField[] { field1, field2, field3, field4, field5, field6 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4, T5, T6, T7> InsertResultStep<Record7<T1, T2, T3, T4, T5, T6, T7>> returningResult(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7) {
        return (InsertResultStep) returningResult(new SelectField[] { field1, field2, field3, field4, field5, field6, field7 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4, T5, T6, T7, T8> InsertResultStep<Record8<T1, T2, T3, T4, T5, T6, T7, T8>> returningResult(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8) {
        return (InsertResultStep) returningResult(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9> InsertResultStep<Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> returningResult(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9) {
        return (InsertResultStep) returningResult(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> InsertResultStep<Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>> returningResult(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10) {
        return (InsertResultStep) returningResult(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> InsertResultStep<Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>> returningResult(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11) {
        return (InsertResultStep) returningResult(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> InsertResultStep<Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>> returningResult(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12) {
        return (InsertResultStep) returningResult(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> InsertResultStep<Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> returningResult(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13) {
        return (InsertResultStep) returningResult(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> InsertResultStep<Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>> returningResult(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14) {
        return (InsertResultStep) returningResult(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> InsertResultStep<Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>> returningResult(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15) {
        return (InsertResultStep) returningResult(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> InsertResultStep<Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>> returningResult(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16) {
        return (InsertResultStep) returningResult(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> InsertResultStep<Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>> returningResult(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16, SelectField<T17> field17) {
        return (InsertResultStep) returningResult(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> InsertResultStep<Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>> returningResult(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16, SelectField<T17> field17, SelectField<T18> field18) {
        return (InsertResultStep) returningResult(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> InsertResultStep<Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>> returningResult(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16, SelectField<T17> field17, SelectField<T18> field18, SelectField<T19> field19) {
        return (InsertResultStep) returningResult(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> InsertResultStep<Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>> returningResult(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16, SelectField<T17> field17, SelectField<T18> field18, SelectField<T19> field19, SelectField<T20> field20) {
        return (InsertResultStep) returningResult(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> InsertResultStep<Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>> returningResult(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16, SelectField<T17> field17, SelectField<T18> field18, SelectField<T19> field19, SelectField<T20> field20, SelectField<T21> field21) {
        return (InsertResultStep) returningResult(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21 });
    }

    @Override
    @SuppressWarnings("hiding")
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> InsertResultStep<Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>> returningResult(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16, SelectField<T17> field17, SelectField<T18> field18, SelectField<T19> field19, SelectField<T20> field20, SelectField<T21> field21, SelectField<T22> field22) {
        return (InsertResultStep) returningResult(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21, field22 });
    }


    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final WithImpl $with() {
        return getDelegate().$with();
    }

    @Override
    public final Table<R> $into() {
        return getDelegate().$into();
    }

    @Override
    public final Insert<?> $into(Table<?> newInto) {
        return getDelegate().$into(newInto);
    }

    @Override
    public final UnmodifiableList<? extends Field<?>> $columns() {
        return getDelegate().$columns();
    }

    @Override
    public final Insert<?> $columns(Collection<? extends Field<?>> columns) {
        return getDelegate().$columns(columns);
    }

    @Override
    public final Select<?> $select() {
        return getDelegate().$select();
    }

    @Override
    public final Insert<?> $select(Select<?> select) {
        return getDelegate().$select(select);
    }

    @Override
    public final boolean $defaultValues() {
        return getDelegate().$defaultValues();
    }

    @Override
    public final Insert<?> $defaultValues(boolean defaultValues) {
        return getDelegate().$defaultValues(defaultValues);
    }

    @Override
    public final UnmodifiableList<? extends Row> $values() {
        return getDelegate().$values();
    }

    @Override
    public final Insert<?> $values(Collection<? extends Row> values) {
        return getDelegate().$values(values);
    }

    @Override
    public final boolean $onDuplicateKeyIgnore() {
        return getDelegate().$onDuplicateKeyIgnore();
    }

    @Override
    public final Insert<?> $onDuplicateKeyIgnore(boolean onDuplicateKeyIgnore) {
        return getDelegate().$onDuplicateKeyIgnore(onDuplicateKeyIgnore);
    }

    @Override
    public boolean $onDuplicateKeyUpdate() {
        return getDelegate().$onDuplicateKeyUpdate();
    }

    @Override
    public final Insert<?> $onDuplicateKeyUpdate(boolean onDuplicateKeyUpdate) {
        return getDelegate().$onDuplicateKeyUpdate(onDuplicateKeyUpdate);
    }

    @Override
    public final UnmodifiableList<? extends Field<?>> $onConflict() {
        return getDelegate().$onConflict();
    }

    @Override
    public final Insert<?> $onConflict(Collection<? extends Field<?>> onConflictFields) {
        return getDelegate().$onConflict(onConflictFields);
    }

    @Override
    public final Condition $onConflictWhere() {
        return getDelegate().$onConflictWhere();
    }

    @Override
    public final Insert<?> $onConflictWhere(Condition where) {
        return getDelegate().$onConflictWhere(where);
    }

    @Override
    public final UnmodifiableMap<? extends FieldOrRow, ? extends FieldOrRowOrSelect> $updateSet() {
        return getDelegate().$updateSet();
    }

    @Override
    public final Insert<?> $updateSet(Map<? extends FieldOrRow, ? extends FieldOrRowOrSelect> updateSet) {
        return getDelegate().$updateSet(updateSet);
    }

    @Override
    public final Condition $updateWhere() {
        return getDelegate().$updateWhere();
    }

    @Override
    public final Insert<?> $updateWhere(Condition where) {
        return getDelegate().$updateWhere(where);
    }














}
