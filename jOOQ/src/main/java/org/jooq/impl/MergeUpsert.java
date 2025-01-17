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

import static org.jooq.conf.WriteIfReadonly.IGNORE;
import static org.jooq.conf.WriteIfReadonly.THROW;
import static org.jooq.impl.DSL.insertInto;
import static org.jooq.impl.Keywords.K_KEY;
import static org.jooq.impl.Keywords.K_MERGE_INTO;
import static org.jooq.impl.Keywords.K_UPSERT;
import static org.jooq.impl.Keywords.K_VALUES;
import static org.jooq.impl.Keywords.K_WHERE;
import static org.jooq.impl.Keywords.K_WITH_PRIMARY_KEY;
import static org.jooq.impl.QueryPartListView.wrap;
import static org.jooq.impl.Tools.EMPTY_FIELD;
import static org.jooq.impl.Tools.collect;
import static org.jooq.impl.Tools.filter;
import static org.jooq.impl.Tools.map;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Merge;
import org.jooq.MergeColumnsStep;
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
import org.jooq.MergeValuesStep1;
import org.jooq.MergeValuesStep10;
import org.jooq.MergeValuesStep11;
import org.jooq.MergeValuesStep12;
import org.jooq.MergeValuesStep13;
import org.jooq.MergeValuesStep14;
import org.jooq.MergeValuesStep15;
import org.jooq.MergeValuesStep16;
import org.jooq.MergeValuesStep17;
import org.jooq.MergeValuesStep18;
import org.jooq.MergeValuesStep19;
import org.jooq.MergeValuesStep2;
import org.jooq.MergeValuesStep20;
import org.jooq.MergeValuesStep21;
import org.jooq.MergeValuesStep22;
import org.jooq.MergeValuesStep3;
import org.jooq.MergeValuesStep4;
import org.jooq.MergeValuesStep5;
import org.jooq.MergeValuesStep6;
import org.jooq.MergeValuesStep7;
import org.jooq.MergeValuesStep8;
import org.jooq.MergeValuesStep9;
import org.jooq.MergeValuesStepN;
// ...
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.Select;
import org.jooq.Table;
import org.jooq.UniqueKey;
import org.jooq.exception.DataTypeException;
import org.jooq.impl.QOM.UNotYetImplemented;

/**
 * The H2 specific <code>MERGE</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@Deprecated
final class MergeUpsert<R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>
extends AbstractRowCountQuery
implements

    Merge<R>,
    MergeColumnsStep<R>,
    // Cascading interface implementations for Merge behaviour


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




    MergeValuesStep1<R, T1>,
    MergeValuesStep2<R, T1, T2>,
    MergeValuesStep3<R, T1, T2, T3>,
    MergeValuesStep4<R, T1, T2, T3, T4>,
    MergeValuesStep5<R, T1, T2, T3, T4, T5>,
    MergeValuesStep6<R, T1, T2, T3, T4, T5, T6>,
    MergeValuesStep7<R, T1, T2, T3, T4, T5, T6, T7>,
    MergeValuesStep8<R, T1, T2, T3, T4, T5, T6, T7, T8>,
    MergeValuesStep9<R, T1, T2, T3, T4, T5, T6, T7, T8, T9>,
    MergeValuesStep10<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>,
    MergeValuesStep11<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>,
    MergeValuesStep12<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>,
    MergeValuesStep13<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>,
    MergeValuesStep14<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>,
    MergeValuesStep15<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>,
    MergeValuesStep16<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>,
    MergeValuesStep17<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>,
    MergeValuesStep18<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>,
    MergeValuesStep19<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>,
    MergeValuesStep20<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>,
    MergeValuesStep21<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>,
    MergeValuesStep22<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>,


    MergeValuesStepN<R>,
    UNotYetImplemented
{

    final WithImpl               with;
    final Table<?>               table;

    // Objects for the UPSERT syntax (including H2 MERGE, HANA UPSERT, etc.)
    QueryPartList<Field<?>>      upsertFields;
    QueryPartList<Field<?>>      upsertKeys;
    QueryPartList<Field<?>>      upsertValues;
    Select<?>                    upsertSelect;


    MergeUpsert(Configuration configuration, WithImpl with, Table<?> table) {
        this(configuration, with, table, null);
    }

    MergeUpsert(Configuration configuration, WithImpl with, Table<?> table, Collection<? extends Field<?>> fields) {
        super(configuration);

        this.with = with;
        this.table = table;

        if (fields != null)
            columns(fields);
    }

    final Table<?> table() {
        return table;
    }

    final QueryPartList<Field<?>> getUpsertFields() {
        if (upsertFields == null)
            upsertFields = new QueryPartList<>(table.fields());

        return upsertFields;
    }

    final QueryPartList<Field<?>> getUpsertKeys() {
        if (upsertKeys == null)
            upsertKeys = new QueryPartList<>();

        return upsertKeys;
    }

    final QueryPartList<Field<?>> getUpsertValues() {
        if (upsertValues == null)
            upsertValues = new QueryPartList<>();

        return upsertValues;
    }

    @Override
    public final MergeUpsert columns(Field... fields) {
        return columns(Arrays.asList(fields));
    }

    @Override
    public final MergeUpsert columns(Collection fields) {
        upsertFields = new QueryPartList<>(fields);
        return this;
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
    public final MergeUpsert values(Object value1) {
        return values(new Object[] { value1 });
    }

    @Override
    public final MergeUpsert values(Object value1, Object value2) {
        return values(new Object[] { value1, value2 });
    }

    @Override
    public final MergeUpsert values(Object value1, Object value2, Object value3) {
        return values(new Object[] { value1, value2, value3 });
    }

    @Override
    public final MergeUpsert values(Object value1, Object value2, Object value3, Object value4) {
        return values(new Object[] { value1, value2, value3, value4 });
    }

    @Override
    public final MergeUpsert values(Object value1, Object value2, Object value3, Object value4, Object value5) {
        return values(new Object[] { value1, value2, value3, value4, value5 });
    }

    @Override
    public final MergeUpsert values(Object value1, Object value2, Object value3, Object value4, Object value5, Object value6) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6 });
    }

    @Override
    public final MergeUpsert values(Object value1, Object value2, Object value3, Object value4, Object value5, Object value6, Object value7) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7 });
    }

    @Override
    public final MergeUpsert values(Object value1, Object value2, Object value3, Object value4, Object value5, Object value6, Object value7, Object value8) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8 });
    }

    @Override
    public final MergeUpsert values(Object value1, Object value2, Object value3, Object value4, Object value5, Object value6, Object value7, Object value8, Object value9) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9 });
    }

    @Override
    public final MergeUpsert values(Object value1, Object value2, Object value3, Object value4, Object value5, Object value6, Object value7, Object value8, Object value9, Object value10) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10 });
    }

    @Override
    public final MergeUpsert values(Object value1, Object value2, Object value3, Object value4, Object value5, Object value6, Object value7, Object value8, Object value9, Object value10, Object value11) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11 });
    }

    @Override
    public final MergeUpsert values(Object value1, Object value2, Object value3, Object value4, Object value5, Object value6, Object value7, Object value8, Object value9, Object value10, Object value11, Object value12) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12 });
    }

    @Override
    public final MergeUpsert values(Object value1, Object value2, Object value3, Object value4, Object value5, Object value6, Object value7, Object value8, Object value9, Object value10, Object value11, Object value12, Object value13) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13 });
    }

    @Override
    public final MergeUpsert values(Object value1, Object value2, Object value3, Object value4, Object value5, Object value6, Object value7, Object value8, Object value9, Object value10, Object value11, Object value12, Object value13, Object value14) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14 });
    }

    @Override
    public final MergeUpsert values(Object value1, Object value2, Object value3, Object value4, Object value5, Object value6, Object value7, Object value8, Object value9, Object value10, Object value11, Object value12, Object value13, Object value14, Object value15) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15 });
    }

    @Override
    public final MergeUpsert values(Object value1, Object value2, Object value3, Object value4, Object value5, Object value6, Object value7, Object value8, Object value9, Object value10, Object value11, Object value12, Object value13, Object value14, Object value15, Object value16) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16 });
    }

    @Override
    public final MergeUpsert values(Object value1, Object value2, Object value3, Object value4, Object value5, Object value6, Object value7, Object value8, Object value9, Object value10, Object value11, Object value12, Object value13, Object value14, Object value15, Object value16, Object value17) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17 });
    }

    @Override
    public final MergeUpsert values(Object value1, Object value2, Object value3, Object value4, Object value5, Object value6, Object value7, Object value8, Object value9, Object value10, Object value11, Object value12, Object value13, Object value14, Object value15, Object value16, Object value17, Object value18) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17, value18 });
    }

    @Override
    public final MergeUpsert values(Object value1, Object value2, Object value3, Object value4, Object value5, Object value6, Object value7, Object value8, Object value9, Object value10, Object value11, Object value12, Object value13, Object value14, Object value15, Object value16, Object value17, Object value18, Object value19) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17, value18, value19 });
    }

    @Override
    public final MergeUpsert values(Object value1, Object value2, Object value3, Object value4, Object value5, Object value6, Object value7, Object value8, Object value9, Object value10, Object value11, Object value12, Object value13, Object value14, Object value15, Object value16, Object value17, Object value18, Object value19, Object value20) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17, value18, value19, value20 });
    }

    @Override
    public final MergeUpsert values(Object value1, Object value2, Object value3, Object value4, Object value5, Object value6, Object value7, Object value8, Object value9, Object value10, Object value11, Object value12, Object value13, Object value14, Object value15, Object value16, Object value17, Object value18, Object value19, Object value20, Object value21) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17, value18, value19, value20, value21 });
    }

    @Override
    public final MergeUpsert values(Object value1, Object value2, Object value3, Object value4, Object value5, Object value6, Object value7, Object value8, Object value9, Object value10, Object value11, Object value12, Object value13, Object value14, Object value15, Object value16, Object value17, Object value18, Object value19, Object value20, Object value21, Object value22) {
        return values(new Object[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17, value18, value19, value20, value21, value22 });
    }


    @Override
    public final MergeUpsert values(Field value1) {
        return values(new Field[] { value1 });
    }

    @Override
    public final MergeUpsert values(Field value1, Field value2) {
        return values(new Field[] { value1, value2 });
    }

    @Override
    public final MergeUpsert values(Field value1, Field value2, Field value3) {
        return values(new Field[] { value1, value2, value3 });
    }

    @Override
    public final MergeUpsert values(Field value1, Field value2, Field value3, Field value4) {
        return values(new Field[] { value1, value2, value3, value4 });
    }

    @Override
    public final MergeUpsert values(Field value1, Field value2, Field value3, Field value4, Field value5) {
        return values(new Field[] { value1, value2, value3, value4, value5 });
    }

    @Override
    public final MergeUpsert values(Field value1, Field value2, Field value3, Field value4, Field value5, Field value6) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6 });
    }

    @Override
    public final MergeUpsert values(Field value1, Field value2, Field value3, Field value4, Field value5, Field value6, Field value7) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7 });
    }

    @Override
    public final MergeUpsert values(Field value1, Field value2, Field value3, Field value4, Field value5, Field value6, Field value7, Field value8) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8 });
    }

    @Override
    public final MergeUpsert values(Field value1, Field value2, Field value3, Field value4, Field value5, Field value6, Field value7, Field value8, Field value9) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9 });
    }

    @Override
    public final MergeUpsert values(Field value1, Field value2, Field value3, Field value4, Field value5, Field value6, Field value7, Field value8, Field value9, Field value10) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10 });
    }

    @Override
    public final MergeUpsert values(Field value1, Field value2, Field value3, Field value4, Field value5, Field value6, Field value7, Field value8, Field value9, Field value10, Field value11) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11 });
    }

    @Override
    public final MergeUpsert values(Field value1, Field value2, Field value3, Field value4, Field value5, Field value6, Field value7, Field value8, Field value9, Field value10, Field value11, Field value12) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12 });
    }

    @Override
    public final MergeUpsert values(Field value1, Field value2, Field value3, Field value4, Field value5, Field value6, Field value7, Field value8, Field value9, Field value10, Field value11, Field value12, Field value13) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13 });
    }

    @Override
    public final MergeUpsert values(Field value1, Field value2, Field value3, Field value4, Field value5, Field value6, Field value7, Field value8, Field value9, Field value10, Field value11, Field value12, Field value13, Field value14) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14 });
    }

    @Override
    public final MergeUpsert values(Field value1, Field value2, Field value3, Field value4, Field value5, Field value6, Field value7, Field value8, Field value9, Field value10, Field value11, Field value12, Field value13, Field value14, Field value15) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15 });
    }

    @Override
    public final MergeUpsert values(Field value1, Field value2, Field value3, Field value4, Field value5, Field value6, Field value7, Field value8, Field value9, Field value10, Field value11, Field value12, Field value13, Field value14, Field value15, Field value16) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16 });
    }

    @Override
    public final MergeUpsert values(Field value1, Field value2, Field value3, Field value4, Field value5, Field value6, Field value7, Field value8, Field value9, Field value10, Field value11, Field value12, Field value13, Field value14, Field value15, Field value16, Field value17) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17 });
    }

    @Override
    public final MergeUpsert values(Field value1, Field value2, Field value3, Field value4, Field value5, Field value6, Field value7, Field value8, Field value9, Field value10, Field value11, Field value12, Field value13, Field value14, Field value15, Field value16, Field value17, Field value18) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17, value18 });
    }

    @Override
    public final MergeUpsert values(Field value1, Field value2, Field value3, Field value4, Field value5, Field value6, Field value7, Field value8, Field value9, Field value10, Field value11, Field value12, Field value13, Field value14, Field value15, Field value16, Field value17, Field value18, Field value19) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17, value18, value19 });
    }

    @Override
    public final MergeUpsert values(Field value1, Field value2, Field value3, Field value4, Field value5, Field value6, Field value7, Field value8, Field value9, Field value10, Field value11, Field value12, Field value13, Field value14, Field value15, Field value16, Field value17, Field value18, Field value19, Field value20) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17, value18, value19, value20 });
    }

    @Override
    public final MergeUpsert values(Field value1, Field value2, Field value3, Field value4, Field value5, Field value6, Field value7, Field value8, Field value9, Field value10, Field value11, Field value12, Field value13, Field value14, Field value15, Field value16, Field value17, Field value18, Field value19, Field value20, Field value21) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17, value18, value19, value20, value21 });
    }

    @Override
    public final MergeUpsert values(Field value1, Field value2, Field value3, Field value4, Field value5, Field value6, Field value7, Field value8, Field value9, Field value10, Field value11, Field value12, Field value13, Field value14, Field value15, Field value16, Field value17, Field value18, Field value19, Field value20, Field value21, Field value22) {
        return values(new Field[] { value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17, value18, value19, value20, value21, value22 });
    }



    @Override
    public final MergeUpsert values(Object... values) {
        getUpsertValues().addAll(Tools.fields(values, getUpsertFields().toArray(EMPTY_FIELD)));
        return this;
    }

    @Override
    public final MergeUpsert values(Field<?>... values) {
        return values((Object[]) values);
    }

    @Override
    public final MergeUpsert values(Collection<?> values) {
        return values(values.toArray());
    }

    @Override
    public final MergeUpsert select(Select select) {
        upsertSelect = select;
        return this;
    }

    @Override
    public final MergeUpsert key(Field... k) {
        return key(Arrays.asList(k));
    }

    @Override
    public final MergeUpsert key(Collection keys) {
        getUpsertKeys().addAll(keys);
        return this;
    }

    // -------------------------------------------------------------------------
    // QueryPart API
    // -------------------------------------------------------------------------

    /**
     * Return a standard MERGE statement emulating the H2-specific syntax
     */
    private final QueryPart getStandardMerge(boolean usingSubqueries) {

        // The SRC for the USING() clause:
        // ------------------------------
        Table<?> src;
        List<Field<?>> srcFields;

        // [#5110] This is not yet supported by Derby
        if (upsertSelect != null) {
            Table<?> s = upsertSelect.asTable("s");

            // [#579] TODO: Currently, this syntax may require aliasing
            // on the call-site
            src = DSL.select(map(s.fieldsRow().fields(), (f, i) -> f.as("s" + (i + 1)))).from(s).asTable("src");
            srcFields = Arrays.asList(src.fields());
        }
        else if (usingSubqueries) {
            src = DSL.select(map(getUpsertValues(), (f, i) -> f.as("s" + (i + 1)))).asTable("src");
            srcFields = Arrays.asList(src.fields());
        }
        else {
            src = new Dual();
            srcFields = map(getUpsertValues(), f -> f);
        }

        // The condition for the ON clause:
        // --------------------------------
        Set<Field<?>> onFields = new HashSet<>();
        Condition condition = null;
        if (getUpsertKeys().isEmpty()) {
            UniqueKey<?> key = table.getPrimaryKey();

            if (key != null) {
                onFields.addAll(key.getFields());

                for (int i = 0; i < key.getFields().size(); i++) {
                    Condition rhs = key.getFields().get(i).equal((Field) srcFields.get(i));

                    if (condition == null)
                        condition = rhs;
                    else
                        condition = condition.and(rhs);
                }
            }

            // This should probably execute an INSERT statement
            else
                throw new IllegalStateException("Cannot omit KEY() clause on a non-Updatable Table");
        }
        else {
            for (int i = 0; i < getUpsertKeys().size(); i++) {
                int matchIndex = getUpsertFields().indexOf(getUpsertKeys().get(i));
                if (matchIndex == -1)
                    throw new IllegalStateException("Fields in KEY() clause must be part of the fields specified in MERGE INTO table (...)");

                onFields.addAll(getUpsertKeys());
                Condition rhs = getUpsertKeys().get(i).equal((Field) srcFields.get(matchIndex));

                if (condition == null)
                    condition = rhs;
                else
                    condition = condition.and(rhs);
            }
        }

        // INSERT and UPDATE clauses
        // -------------------------
        Map<Field<?>, Field<?>> update = new LinkedHashMap<>();
        Map<Field<?>, Field<?>> insert = new LinkedHashMap<>();

        for (int i = 0; i < srcFields.size(); i++) {

            // Oracle does not allow to update fields from the ON clause
            if (!onFields.contains(getUpsertFields().get(i)))
                update.put(getUpsertFields().get(i), srcFields.get(i));

            insert.put(getUpsertFields().get(i), srcFields.get(i));
        }

        return DSL.mergeInto(table)
                  .using(src)
                  .on(condition)
                  .whenMatchedThenUpdate()
                  .set(update)
                  .whenNotMatchedThenInsert()
                  .set(insert);
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (with != null)
            ctx.visit(with);

        switch (ctx.family()) {
            case H2:
                toSQLH2Merge(ctx);
                break;



            case MARIADB:
            case MYSQL:
                toSQLMySQLOnDuplicateKeyUpdate(ctx);
                break;



            case POSTGRES:
            case YUGABYTEDB:
                toPostgresInsertOnConflict(ctx);
                break;







            case DERBY:
                ctx.visit(getStandardMerge(false));
                break;

            default:
                ctx.visit(getStandardMerge(true));
                break;
        }
    }

    private final void toSQLMySQLOnDuplicateKeyUpdate(Context<?> ctx) {
        FieldsImpl<?> fields = new FieldsImpl<>(getUpsertFields());
        Map<Field<?>, Field<?>> map = new LinkedHashMap<>();
        for (Field<?> field : fields.fields)
            map.put(field, getUpsertValues().get(fields.indexOf(field)));

        if (upsertSelect != null) {
            ctx.sql("[ merge with select is not supported in MySQL / MariaDB ]");
        }
        else {
            ctx.visit(insertInto(table, getUpsertFields())
               .values(getUpsertValues())
               .onDuplicateKeyUpdate()
               .set(map));
        }
    }

    private final void toPostgresInsertOnConflict(Context<?> ctx) {
        if (upsertSelect != null) {
            ctx.visit(getStandardMerge(true));
        }
        else {
            FieldsImpl<?> fields = new FieldsImpl<>(getUpsertFields());
            Map<Field<?>, Field<?>> map = new LinkedHashMap<>();

            for (Field<?> field : fields.fields) {
                int i = fields.indexOf(field);

                if (i > -1 && i < getUpsertValues().size())
                    map.put(field, getUpsertValues().get(i));
            }

            ctx.visit(insertInto(table, getUpsertFields())
               .values(getUpsertValues())
               .onConflict(getUpsertKeys())
               .doUpdate()
               .set(map));
        }
    }

    private final void toSQLH2Merge(Context<?> ctx) {




        ctx.visit(K_MERGE_INTO)
           .sql(' ')
           .declareTables(true, c -> c.visit(table))
           .formatSeparator();

        ctx.sql('(')
           .visit(wrap(collect(removeReadonly(ctx, getUpsertFields()))).qualify(false))
           .sql(')');

        if (!getUpsertKeys().isEmpty())
            ctx.formatSeparator()
               .visit(K_KEY).sql(" (")
               .visit(wrap(getUpsertKeys()).qualify(false))
               .sql(')');

        if (upsertSelect != null)
            ctx.formatSeparator()
               .visit(upsertSelect);
        else
            ctx.formatSeparator()
               .visit(K_VALUES).sql(" (")
               .visit(wrap(collect(removeReadonly(ctx, getUpsertFields(), getUpsertValues()))))
               .sql(')');
    }

    static final Iterable<Field<?>> removeReadonly(Context<?> ctx, List<Field<?>> it) {
        return removeReadonly(ctx, it, it);
    }

    static final Iterable<Field<?>> removeReadonly(Context<?> ctx, List<Field<?>> checkIt, List<Field<?>> removeIt) {




        return removeIt;
    }





















































}
