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
package org.jooq.xtend

import javax.annotation.Generated

import org.jooq.Condition
import org.jooq.Field
import org.jooq.QuantifiedSelect
import org.jooq.Record
import org.jooq.Record1
import org.jooq.Record2
import org.jooq.Record3
import org.jooq.Record4
import org.jooq.Record5
import org.jooq.Record6
import org.jooq.Record7
import org.jooq.Record8
import org.jooq.Record9
import org.jooq.Record10
import org.jooq.Record11
import org.jooq.Record12
import org.jooq.Record13
import org.jooq.Record14
import org.jooq.Record15
import org.jooq.Record16
import org.jooq.Record17
import org.jooq.Record18
import org.jooq.Record19
import org.jooq.Record20
import org.jooq.Record21
import org.jooq.Record22
import org.jooq.Row
import org.jooq.RowN
import org.jooq.Row1
import org.jooq.Row2
import org.jooq.Row3
import org.jooq.Row4
import org.jooq.Row5
import org.jooq.Row6
import org.jooq.Row7
import org.jooq.Row8
import org.jooq.Row9
import org.jooq.Row10
import org.jooq.Row11
import org.jooq.Row12
import org.jooq.Row13
import org.jooq.Row14
import org.jooq.Row15
import org.jooq.Row16
import org.jooq.Row17
import org.jooq.Row18
import org.jooq.Row19
import org.jooq.Row20
import org.jooq.Row21
import org.jooq.Row22
import org.jooq.Select

import org.jooq.impl.DSL

/**
 * jOOQ type conversions used to enhance the jOOQ Java API with Xtend operators.
 *
 * @author Lukas Eder
 * @see <a href="http://www.eclipse.org/xtend/documentation.html#operators">http://www.eclipse.org/xtend/documentation.html#operators</a>
 */
@Generated("This class was generated using jOOQ-tools")
class Conversions {

    def static <T> operator_or(Condition c1, Condition c2) {
        c1.or(c2);
    }

    def static <T> operator_and(Condition c1, Condition c2) {
        c1.and(c2);
    }

    def static <T> operator_tripleEquals(Field<T> f1, T f2) {
        f1.eq(f2)
    }

    def static <T> operator_tripleEquals(Field<T> f1, Field<T> f2) {
        f1.eq(f2)
    }

    def static <T> operator_tripleEquals(Field<T> f1, Select<? extends Record1<T>> f2) {
        f1.eq(f2)
    }

    def static <T> operator_tripleEquals(Field<T> f1, QuantifiedSelect<? extends Record1<T>> f2) {
        f1.eq(f2)
    }

    def static operator_tripleEquals(RowN r1, RowN r2) {
        r1.eq(r2)
    }

    def static operator_tripleEquals(RowN r1, Record r2) {
        r1.eq(r2)
    }

    def static operator_tripleEquals(RowN r1, Select<? extends Record> r2) {
        r1.eq(r2)
    }

    def static <T1> operator_tripleEquals(Row1<T1> r1, Row1<T1> r2) {
        r1.eq(r2)
    }

    def static <T1> operator_tripleEquals(Row1<T1> r1, Record1<T1> r2) {
        r1.eq(r2)
    }

    def static <T1> operator_tripleEquals(Row1<T1> r1, Select<? extends Record1<T1>> r2) {
        r1.eq(r2)
    }

    def static <T1, T2> operator_tripleEquals(Row2<T1, T2> r1, Row2<T1, T2> r2) {
        r1.eq(r2)
    }

    def static <T1, T2> operator_tripleEquals(Row2<T1, T2> r1, Record2<T1, T2> r2) {
        r1.eq(r2)
    }

    def static <T1, T2> operator_tripleEquals(Row2<T1, T2> r1, Select<? extends Record2<T1, T2>> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3> operator_tripleEquals(Row3<T1, T2, T3> r1, Row3<T1, T2, T3> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3> operator_tripleEquals(Row3<T1, T2, T3> r1, Record3<T1, T2, T3> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3> operator_tripleEquals(Row3<T1, T2, T3> r1, Select<? extends Record3<T1, T2, T3>> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4> operator_tripleEquals(Row4<T1, T2, T3, T4> r1, Row4<T1, T2, T3, T4> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4> operator_tripleEquals(Row4<T1, T2, T3, T4> r1, Record4<T1, T2, T3, T4> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4> operator_tripleEquals(Row4<T1, T2, T3, T4> r1, Select<? extends Record4<T1, T2, T3, T4>> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4, T5> operator_tripleEquals(Row5<T1, T2, T3, T4, T5> r1, Row5<T1, T2, T3, T4, T5> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4, T5> operator_tripleEquals(Row5<T1, T2, T3, T4, T5> r1, Record5<T1, T2, T3, T4, T5> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4, T5> operator_tripleEquals(Row5<T1, T2, T3, T4, T5> r1, Select<? extends Record5<T1, T2, T3, T4, T5>> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4, T5, T6> operator_tripleEquals(Row6<T1, T2, T3, T4, T5, T6> r1, Row6<T1, T2, T3, T4, T5, T6> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4, T5, T6> operator_tripleEquals(Row6<T1, T2, T3, T4, T5, T6> r1, Record6<T1, T2, T3, T4, T5, T6> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4, T5, T6> operator_tripleEquals(Row6<T1, T2, T3, T4, T5, T6> r1, Select<? extends Record6<T1, T2, T3, T4, T5, T6>> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7> operator_tripleEquals(Row7<T1, T2, T3, T4, T5, T6, T7> r1, Row7<T1, T2, T3, T4, T5, T6, T7> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7> operator_tripleEquals(Row7<T1, T2, T3, T4, T5, T6, T7> r1, Record7<T1, T2, T3, T4, T5, T6, T7> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7> operator_tripleEquals(Row7<T1, T2, T3, T4, T5, T6, T7> r1, Select<? extends Record7<T1, T2, T3, T4, T5, T6, T7>> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8> operator_tripleEquals(Row8<T1, T2, T3, T4, T5, T6, T7, T8> r1, Row8<T1, T2, T3, T4, T5, T6, T7, T8> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8> operator_tripleEquals(Row8<T1, T2, T3, T4, T5, T6, T7, T8> r1, Record8<T1, T2, T3, T4, T5, T6, T7, T8> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8> operator_tripleEquals(Row8<T1, T2, T3, T4, T5, T6, T7, T8> r1, Select<? extends Record8<T1, T2, T3, T4, T5, T6, T7, T8>> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9> operator_tripleEquals(Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> r1, Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9> operator_tripleEquals(Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> r1, Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9> operator_tripleEquals(Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> r1, Select<? extends Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> operator_tripleEquals(Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> r1, Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> operator_tripleEquals(Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> r1, Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> operator_tripleEquals(Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> r1, Select<? extends Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> operator_tripleEquals(Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> r1, Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> operator_tripleEquals(Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> r1, Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> operator_tripleEquals(Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> r1, Select<? extends Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> operator_tripleEquals(Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> r1, Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> operator_tripleEquals(Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> r1, Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> operator_tripleEquals(Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> r1, Select<? extends Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> operator_tripleEquals(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> r1, Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> operator_tripleEquals(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> r1, Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> operator_tripleEquals(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> r1, Select<? extends Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> operator_tripleEquals(Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> r1, Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> operator_tripleEquals(Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> r1, Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> operator_tripleEquals(Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> r1, Select<? extends Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> operator_tripleEquals(Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> r1, Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> operator_tripleEquals(Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> r1, Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> operator_tripleEquals(Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> r1, Select<? extends Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> operator_tripleEquals(Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> r1, Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> operator_tripleEquals(Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> r1, Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> operator_tripleEquals(Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> r1, Select<? extends Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> operator_tripleEquals(Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> r1, Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> operator_tripleEquals(Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> r1, Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> operator_tripleEquals(Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> r1, Select<? extends Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> operator_tripleEquals(Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> r1, Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> operator_tripleEquals(Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> r1, Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> operator_tripleEquals(Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> r1, Select<? extends Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> operator_tripleEquals(Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> r1, Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> operator_tripleEquals(Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> r1, Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> operator_tripleEquals(Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> r1, Select<? extends Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> operator_tripleEquals(Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> r1, Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> operator_tripleEquals(Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> r1, Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> operator_tripleEquals(Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> r1, Select<? extends Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> operator_tripleEquals(Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> r1, Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> operator_tripleEquals(Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> r1, Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> operator_tripleEquals(Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> r1, Select<? extends Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> operator_tripleEquals(Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> r1, Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> operator_tripleEquals(Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> r1, Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> r2) {
        r1.eq(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> operator_tripleEquals(Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> r1, Select<? extends Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>> r2) {
        r1.eq(r2)
    }

    def static <T> operator_tripleNotEquals(Field<T> f1, T f2) {
        f1.ne(f2)
    }

    def static <T> operator_tripleNotEquals(Field<T> f1, Field<T> f2) {
        f1.ne(f2)
    }

    def static <T> operator_tripleNotEquals(Field<T> f1, Select<? extends Record1<T>> f2) {
        f1.ne(f2)
    }

    def static <T> operator_tripleNotEquals(Field<T> f1, QuantifiedSelect<? extends Record1<T>> f2) {
        f1.ne(f2)
    }

    def static <T1> operator_tripleNotEquals(Row1<T1> r1, Row1<T1> r2) {
        r1.ne(r2)
    }

    def static <T1> operator_tripleNotEquals(Row1<T1> r1, Record1<T1> r2) {
        r1.ne(r2)
    }

    def static <T1> operator_tripleNotEquals(Row1<T1> r1, Select<? extends Record1<T1>> r2) {
        r1.ne(r2)
    }

    def static <T1, T2> operator_tripleNotEquals(Row2<T1, T2> r1, Row2<T1, T2> r2) {
        r1.ne(r2)
    }

    def static <T1, T2> operator_tripleNotEquals(Row2<T1, T2> r1, Record2<T1, T2> r2) {
        r1.ne(r2)
    }

    def static <T1, T2> operator_tripleNotEquals(Row2<T1, T2> r1, Select<? extends Record2<T1, T2>> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3> operator_tripleNotEquals(Row3<T1, T2, T3> r1, Row3<T1, T2, T3> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3> operator_tripleNotEquals(Row3<T1, T2, T3> r1, Record3<T1, T2, T3> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3> operator_tripleNotEquals(Row3<T1, T2, T3> r1, Select<? extends Record3<T1, T2, T3>> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4> operator_tripleNotEquals(Row4<T1, T2, T3, T4> r1, Row4<T1, T2, T3, T4> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4> operator_tripleNotEquals(Row4<T1, T2, T3, T4> r1, Record4<T1, T2, T3, T4> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4> operator_tripleNotEquals(Row4<T1, T2, T3, T4> r1, Select<? extends Record4<T1, T2, T3, T4>> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4, T5> operator_tripleNotEquals(Row5<T1, T2, T3, T4, T5> r1, Row5<T1, T2, T3, T4, T5> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4, T5> operator_tripleNotEquals(Row5<T1, T2, T3, T4, T5> r1, Record5<T1, T2, T3, T4, T5> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4, T5> operator_tripleNotEquals(Row5<T1, T2, T3, T4, T5> r1, Select<? extends Record5<T1, T2, T3, T4, T5>> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4, T5, T6> operator_tripleNotEquals(Row6<T1, T2, T3, T4, T5, T6> r1, Row6<T1, T2, T3, T4, T5, T6> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4, T5, T6> operator_tripleNotEquals(Row6<T1, T2, T3, T4, T5, T6> r1, Record6<T1, T2, T3, T4, T5, T6> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4, T5, T6> operator_tripleNotEquals(Row6<T1, T2, T3, T4, T5, T6> r1, Select<? extends Record6<T1, T2, T3, T4, T5, T6>> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7> operator_tripleNotEquals(Row7<T1, T2, T3, T4, T5, T6, T7> r1, Row7<T1, T2, T3, T4, T5, T6, T7> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7> operator_tripleNotEquals(Row7<T1, T2, T3, T4, T5, T6, T7> r1, Record7<T1, T2, T3, T4, T5, T6, T7> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7> operator_tripleNotEquals(Row7<T1, T2, T3, T4, T5, T6, T7> r1, Select<? extends Record7<T1, T2, T3, T4, T5, T6, T7>> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8> operator_tripleNotEquals(Row8<T1, T2, T3, T4, T5, T6, T7, T8> r1, Row8<T1, T2, T3, T4, T5, T6, T7, T8> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8> operator_tripleNotEquals(Row8<T1, T2, T3, T4, T5, T6, T7, T8> r1, Record8<T1, T2, T3, T4, T5, T6, T7, T8> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8> operator_tripleNotEquals(Row8<T1, T2, T3, T4, T5, T6, T7, T8> r1, Select<? extends Record8<T1, T2, T3, T4, T5, T6, T7, T8>> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9> operator_tripleNotEquals(Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> r1, Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9> operator_tripleNotEquals(Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> r1, Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9> operator_tripleNotEquals(Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> r1, Select<? extends Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> operator_tripleNotEquals(Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> r1, Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> operator_tripleNotEquals(Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> r1, Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> operator_tripleNotEquals(Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> r1, Select<? extends Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> operator_tripleNotEquals(Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> r1, Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> operator_tripleNotEquals(Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> r1, Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> operator_tripleNotEquals(Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> r1, Select<? extends Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> operator_tripleNotEquals(Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> r1, Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> operator_tripleNotEquals(Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> r1, Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> operator_tripleNotEquals(Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> r1, Select<? extends Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> operator_tripleNotEquals(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> r1, Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> operator_tripleNotEquals(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> r1, Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> operator_tripleNotEquals(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> r1, Select<? extends Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> operator_tripleNotEquals(Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> r1, Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> operator_tripleNotEquals(Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> r1, Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> operator_tripleNotEquals(Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> r1, Select<? extends Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> operator_tripleNotEquals(Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> r1, Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> operator_tripleNotEquals(Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> r1, Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> operator_tripleNotEquals(Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> r1, Select<? extends Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> operator_tripleNotEquals(Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> r1, Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> operator_tripleNotEquals(Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> r1, Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> operator_tripleNotEquals(Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> r1, Select<? extends Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> operator_tripleNotEquals(Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> r1, Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> operator_tripleNotEquals(Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> r1, Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> operator_tripleNotEquals(Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> r1, Select<? extends Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> operator_tripleNotEquals(Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> r1, Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> operator_tripleNotEquals(Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> r1, Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> operator_tripleNotEquals(Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> r1, Select<? extends Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> operator_tripleNotEquals(Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> r1, Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> operator_tripleNotEquals(Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> r1, Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> operator_tripleNotEquals(Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> r1, Select<? extends Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> operator_tripleNotEquals(Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> r1, Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> operator_tripleNotEquals(Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> r1, Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> operator_tripleNotEquals(Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> r1, Select<? extends Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> operator_tripleNotEquals(Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> r1, Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> operator_tripleNotEquals(Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> r1, Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> operator_tripleNotEquals(Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> r1, Select<? extends Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> operator_tripleNotEquals(Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> r1, Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> operator_tripleNotEquals(Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> r1, Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> r2) {
        r1.ne(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> operator_tripleNotEquals(Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> r1, Select<? extends Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>> r2) {
        r1.ne(r2)
    }

    def static <T> operator_lessThan(Field<T> f1, T f2) {
        f1.lt(f2)
    }

    def static <T> operator_lessThan(Field<T> f1, Field<T> f2) {
        f1.lt(f2)
    }

    def static <T> operator_lessThan(Field<T> f1, Select<? extends Record1<T>> f2) {
        f1.lt(f2)
    }

    def static <T> operator_lessThan(Field<T> f1, QuantifiedSelect<? extends Record1<T>> f2) {
        f1.lt(f2)
    }

    def static <T1> operator_lessThan(Row1<T1> r1, Row1<T1> r2) {
        r1.lt(r2)
    }

    def static <T1> operator_lessThan(Row1<T1> r1, Record1<T1> r2) {
        r1.lt(r2)
    }

    def static <T1> operator_lessThan(Row1<T1> r1, Select<? extends Record1<T1>> r2) {
        r1.lt(r2)
    }

    def static <T1, T2> operator_lessThan(Row2<T1, T2> r1, Row2<T1, T2> r2) {
        r1.lt(r2)
    }

    def static <T1, T2> operator_lessThan(Row2<T1, T2> r1, Record2<T1, T2> r2) {
        r1.lt(r2)
    }

    def static <T1, T2> operator_lessThan(Row2<T1, T2> r1, Select<? extends Record2<T1, T2>> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3> operator_lessThan(Row3<T1, T2, T3> r1, Row3<T1, T2, T3> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3> operator_lessThan(Row3<T1, T2, T3> r1, Record3<T1, T2, T3> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3> operator_lessThan(Row3<T1, T2, T3> r1, Select<? extends Record3<T1, T2, T3>> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4> operator_lessThan(Row4<T1, T2, T3, T4> r1, Row4<T1, T2, T3, T4> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4> operator_lessThan(Row4<T1, T2, T3, T4> r1, Record4<T1, T2, T3, T4> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4> operator_lessThan(Row4<T1, T2, T3, T4> r1, Select<? extends Record4<T1, T2, T3, T4>> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4, T5> operator_lessThan(Row5<T1, T2, T3, T4, T5> r1, Row5<T1, T2, T3, T4, T5> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4, T5> operator_lessThan(Row5<T1, T2, T3, T4, T5> r1, Record5<T1, T2, T3, T4, T5> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4, T5> operator_lessThan(Row5<T1, T2, T3, T4, T5> r1, Select<? extends Record5<T1, T2, T3, T4, T5>> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6> operator_lessThan(Row6<T1, T2, T3, T4, T5, T6> r1, Row6<T1, T2, T3, T4, T5, T6> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6> operator_lessThan(Row6<T1, T2, T3, T4, T5, T6> r1, Record6<T1, T2, T3, T4, T5, T6> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6> operator_lessThan(Row6<T1, T2, T3, T4, T5, T6> r1, Select<? extends Record6<T1, T2, T3, T4, T5, T6>> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7> operator_lessThan(Row7<T1, T2, T3, T4, T5, T6, T7> r1, Row7<T1, T2, T3, T4, T5, T6, T7> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7> operator_lessThan(Row7<T1, T2, T3, T4, T5, T6, T7> r1, Record7<T1, T2, T3, T4, T5, T6, T7> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7> operator_lessThan(Row7<T1, T2, T3, T4, T5, T6, T7> r1, Select<? extends Record7<T1, T2, T3, T4, T5, T6, T7>> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8> operator_lessThan(Row8<T1, T2, T3, T4, T5, T6, T7, T8> r1, Row8<T1, T2, T3, T4, T5, T6, T7, T8> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8> operator_lessThan(Row8<T1, T2, T3, T4, T5, T6, T7, T8> r1, Record8<T1, T2, T3, T4, T5, T6, T7, T8> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8> operator_lessThan(Row8<T1, T2, T3, T4, T5, T6, T7, T8> r1, Select<? extends Record8<T1, T2, T3, T4, T5, T6, T7, T8>> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9> operator_lessThan(Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> r1, Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9> operator_lessThan(Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> r1, Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9> operator_lessThan(Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> r1, Select<? extends Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> operator_lessThan(Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> r1, Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> operator_lessThan(Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> r1, Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> operator_lessThan(Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> r1, Select<? extends Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> operator_lessThan(Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> r1, Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> operator_lessThan(Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> r1, Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> operator_lessThan(Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> r1, Select<? extends Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> operator_lessThan(Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> r1, Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> operator_lessThan(Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> r1, Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> operator_lessThan(Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> r1, Select<? extends Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> operator_lessThan(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> r1, Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> operator_lessThan(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> r1, Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> operator_lessThan(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> r1, Select<? extends Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> operator_lessThan(Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> r1, Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> operator_lessThan(Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> r1, Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> operator_lessThan(Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> r1, Select<? extends Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> operator_lessThan(Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> r1, Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> operator_lessThan(Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> r1, Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> operator_lessThan(Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> r1, Select<? extends Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> operator_lessThan(Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> r1, Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> operator_lessThan(Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> r1, Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> operator_lessThan(Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> r1, Select<? extends Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> operator_lessThan(Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> r1, Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> operator_lessThan(Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> r1, Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> operator_lessThan(Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> r1, Select<? extends Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> operator_lessThan(Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> r1, Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> operator_lessThan(Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> r1, Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> operator_lessThan(Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> r1, Select<? extends Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> operator_lessThan(Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> r1, Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> operator_lessThan(Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> r1, Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> operator_lessThan(Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> r1, Select<? extends Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> operator_lessThan(Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> r1, Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> operator_lessThan(Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> r1, Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> operator_lessThan(Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> r1, Select<? extends Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> operator_lessThan(Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> r1, Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> operator_lessThan(Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> r1, Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> operator_lessThan(Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> r1, Select<? extends Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> operator_lessThan(Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> r1, Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> operator_lessThan(Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> r1, Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> r2) {
        r1.lt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> operator_lessThan(Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> r1, Select<? extends Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>> r2) {
        r1.lt(r2)
    }

    def static <T> operator_greaterThan(Field<T> f1, T f2) {
        f1.gt(f2)
    }

    def static <T> operator_greaterThan(Field<T> f1, Field<T> f2) {
        f1.gt(f2)
    }

    def static <T> operator_greaterThan(Field<T> f1, Select<? extends Record1<T>> f2) {
        f1.gt(f2)
    }

    def static <T> operator_greaterThan(Field<T> f1, QuantifiedSelect<? extends Record1<T>> f2) {
        f1.gt(f2)
    }

    def static <T1> operator_greaterThan(Row1<T1> r1, Row1<T1> r2) {
        r1.gt(r2)
    }

    def static <T1> operator_greaterThan(Row1<T1> r1, Record1<T1> r2) {
        r1.gt(r2)
    }

    def static <T1> operator_greaterThan(Row1<T1> r1, Select<? extends Record1<T1>> r2) {
        r1.gt(r2)
    }

    def static <T1, T2> operator_greaterThan(Row2<T1, T2> r1, Row2<T1, T2> r2) {
        r1.gt(r2)
    }

    def static <T1, T2> operator_greaterThan(Row2<T1, T2> r1, Record2<T1, T2> r2) {
        r1.gt(r2)
    }

    def static <T1, T2> operator_greaterThan(Row2<T1, T2> r1, Select<? extends Record2<T1, T2>> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3> operator_greaterThan(Row3<T1, T2, T3> r1, Row3<T1, T2, T3> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3> operator_greaterThan(Row3<T1, T2, T3> r1, Record3<T1, T2, T3> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3> operator_greaterThan(Row3<T1, T2, T3> r1, Select<? extends Record3<T1, T2, T3>> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4> operator_greaterThan(Row4<T1, T2, T3, T4> r1, Row4<T1, T2, T3, T4> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4> operator_greaterThan(Row4<T1, T2, T3, T4> r1, Record4<T1, T2, T3, T4> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4> operator_greaterThan(Row4<T1, T2, T3, T4> r1, Select<? extends Record4<T1, T2, T3, T4>> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4, T5> operator_greaterThan(Row5<T1, T2, T3, T4, T5> r1, Row5<T1, T2, T3, T4, T5> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4, T5> operator_greaterThan(Row5<T1, T2, T3, T4, T5> r1, Record5<T1, T2, T3, T4, T5> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4, T5> operator_greaterThan(Row5<T1, T2, T3, T4, T5> r1, Select<? extends Record5<T1, T2, T3, T4, T5>> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6> operator_greaterThan(Row6<T1, T2, T3, T4, T5, T6> r1, Row6<T1, T2, T3, T4, T5, T6> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6> operator_greaterThan(Row6<T1, T2, T3, T4, T5, T6> r1, Record6<T1, T2, T3, T4, T5, T6> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6> operator_greaterThan(Row6<T1, T2, T3, T4, T5, T6> r1, Select<? extends Record6<T1, T2, T3, T4, T5, T6>> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7> operator_greaterThan(Row7<T1, T2, T3, T4, T5, T6, T7> r1, Row7<T1, T2, T3, T4, T5, T6, T7> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7> operator_greaterThan(Row7<T1, T2, T3, T4, T5, T6, T7> r1, Record7<T1, T2, T3, T4, T5, T6, T7> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7> operator_greaterThan(Row7<T1, T2, T3, T4, T5, T6, T7> r1, Select<? extends Record7<T1, T2, T3, T4, T5, T6, T7>> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8> operator_greaterThan(Row8<T1, T2, T3, T4, T5, T6, T7, T8> r1, Row8<T1, T2, T3, T4, T5, T6, T7, T8> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8> operator_greaterThan(Row8<T1, T2, T3, T4, T5, T6, T7, T8> r1, Record8<T1, T2, T3, T4, T5, T6, T7, T8> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8> operator_greaterThan(Row8<T1, T2, T3, T4, T5, T6, T7, T8> r1, Select<? extends Record8<T1, T2, T3, T4, T5, T6, T7, T8>> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9> operator_greaterThan(Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> r1, Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9> operator_greaterThan(Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> r1, Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9> operator_greaterThan(Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> r1, Select<? extends Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> operator_greaterThan(Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> r1, Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> operator_greaterThan(Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> r1, Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> operator_greaterThan(Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> r1, Select<? extends Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> operator_greaterThan(Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> r1, Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> operator_greaterThan(Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> r1, Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> operator_greaterThan(Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> r1, Select<? extends Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> operator_greaterThan(Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> r1, Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> operator_greaterThan(Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> r1, Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> operator_greaterThan(Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> r1, Select<? extends Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> operator_greaterThan(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> r1, Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> operator_greaterThan(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> r1, Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> operator_greaterThan(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> r1, Select<? extends Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> operator_greaterThan(Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> r1, Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> operator_greaterThan(Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> r1, Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> operator_greaterThan(Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> r1, Select<? extends Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> operator_greaterThan(Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> r1, Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> operator_greaterThan(Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> r1, Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> operator_greaterThan(Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> r1, Select<? extends Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> operator_greaterThan(Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> r1, Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> operator_greaterThan(Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> r1, Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> operator_greaterThan(Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> r1, Select<? extends Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> operator_greaterThan(Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> r1, Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> operator_greaterThan(Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> r1, Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> operator_greaterThan(Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> r1, Select<? extends Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> operator_greaterThan(Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> r1, Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> operator_greaterThan(Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> r1, Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> operator_greaterThan(Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> r1, Select<? extends Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> operator_greaterThan(Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> r1, Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> operator_greaterThan(Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> r1, Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> operator_greaterThan(Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> r1, Select<? extends Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> operator_greaterThan(Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> r1, Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> operator_greaterThan(Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> r1, Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> operator_greaterThan(Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> r1, Select<? extends Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> operator_greaterThan(Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> r1, Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> operator_greaterThan(Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> r1, Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> operator_greaterThan(Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> r1, Select<? extends Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> operator_greaterThan(Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> r1, Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> operator_greaterThan(Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> r1, Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> r2) {
        r1.gt(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> operator_greaterThan(Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> r1, Select<? extends Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>> r2) {
        r1.gt(r2)
    }

    def static <T> operator_lessEqualsThan(Field<T> f1, T f2) {
        f1.le(f2)
    }

    def static <T> operator_lessEqualsThan(Field<T> f1, Field<T> f2) {
        f1.le(f2)
    }

    def static <T> operator_lessEqualsThan(Field<T> f1, Select<? extends Record1<T>> f2) {
        f1.le(f2)
    }

    def static <T> operator_lessEqualsThan(Field<T> f1, QuantifiedSelect<? extends Record1<T>> f2) {
        f1.le(f2)
    }

    def static <T1> operator_lessEqualsThan(Row1<T1> r1, Row1<T1> r2) {
        r1.le(r2)
    }

    def static <T1> operator_lessEqualsThan(Row1<T1> r1, Record1<T1> r2) {
        r1.le(r2)
    }

    def static <T1> operator_lessEqualsThan(Row1<T1> r1, Select<? extends Record1<T1>> r2) {
        r1.le(r2)
    }

    def static <T1, T2> operator_lessEqualsThan(Row2<T1, T2> r1, Row2<T1, T2> r2) {
        r1.le(r2)
    }

    def static <T1, T2> operator_lessEqualsThan(Row2<T1, T2> r1, Record2<T1, T2> r2) {
        r1.le(r2)
    }

    def static <T1, T2> operator_lessEqualsThan(Row2<T1, T2> r1, Select<? extends Record2<T1, T2>> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3> operator_lessEqualsThan(Row3<T1, T2, T3> r1, Row3<T1, T2, T3> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3> operator_lessEqualsThan(Row3<T1, T2, T3> r1, Record3<T1, T2, T3> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3> operator_lessEqualsThan(Row3<T1, T2, T3> r1, Select<? extends Record3<T1, T2, T3>> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4> operator_lessEqualsThan(Row4<T1, T2, T3, T4> r1, Row4<T1, T2, T3, T4> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4> operator_lessEqualsThan(Row4<T1, T2, T3, T4> r1, Record4<T1, T2, T3, T4> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4> operator_lessEqualsThan(Row4<T1, T2, T3, T4> r1, Select<? extends Record4<T1, T2, T3, T4>> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4, T5> operator_lessEqualsThan(Row5<T1, T2, T3, T4, T5> r1, Row5<T1, T2, T3, T4, T5> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4, T5> operator_lessEqualsThan(Row5<T1, T2, T3, T4, T5> r1, Record5<T1, T2, T3, T4, T5> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4, T5> operator_lessEqualsThan(Row5<T1, T2, T3, T4, T5> r1, Select<? extends Record5<T1, T2, T3, T4, T5>> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4, T5, T6> operator_lessEqualsThan(Row6<T1, T2, T3, T4, T5, T6> r1, Row6<T1, T2, T3, T4, T5, T6> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4, T5, T6> operator_lessEqualsThan(Row6<T1, T2, T3, T4, T5, T6> r1, Record6<T1, T2, T3, T4, T5, T6> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4, T5, T6> operator_lessEqualsThan(Row6<T1, T2, T3, T4, T5, T6> r1, Select<? extends Record6<T1, T2, T3, T4, T5, T6>> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7> operator_lessEqualsThan(Row7<T1, T2, T3, T4, T5, T6, T7> r1, Row7<T1, T2, T3, T4, T5, T6, T7> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7> operator_lessEqualsThan(Row7<T1, T2, T3, T4, T5, T6, T7> r1, Record7<T1, T2, T3, T4, T5, T6, T7> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7> operator_lessEqualsThan(Row7<T1, T2, T3, T4, T5, T6, T7> r1, Select<? extends Record7<T1, T2, T3, T4, T5, T6, T7>> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8> operator_lessEqualsThan(Row8<T1, T2, T3, T4, T5, T6, T7, T8> r1, Row8<T1, T2, T3, T4, T5, T6, T7, T8> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8> operator_lessEqualsThan(Row8<T1, T2, T3, T4, T5, T6, T7, T8> r1, Record8<T1, T2, T3, T4, T5, T6, T7, T8> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8> operator_lessEqualsThan(Row8<T1, T2, T3, T4, T5, T6, T7, T8> r1, Select<? extends Record8<T1, T2, T3, T4, T5, T6, T7, T8>> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9> operator_lessEqualsThan(Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> r1, Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9> operator_lessEqualsThan(Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> r1, Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9> operator_lessEqualsThan(Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> r1, Select<? extends Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> operator_lessEqualsThan(Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> r1, Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> operator_lessEqualsThan(Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> r1, Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> operator_lessEqualsThan(Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> r1, Select<? extends Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> operator_lessEqualsThan(Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> r1, Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> operator_lessEqualsThan(Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> r1, Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> operator_lessEqualsThan(Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> r1, Select<? extends Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> operator_lessEqualsThan(Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> r1, Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> operator_lessEqualsThan(Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> r1, Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> operator_lessEqualsThan(Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> r1, Select<? extends Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> operator_lessEqualsThan(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> r1, Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> operator_lessEqualsThan(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> r1, Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> operator_lessEqualsThan(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> r1, Select<? extends Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> operator_lessEqualsThan(Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> r1, Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> operator_lessEqualsThan(Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> r1, Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> operator_lessEqualsThan(Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> r1, Select<? extends Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> operator_lessEqualsThan(Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> r1, Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> operator_lessEqualsThan(Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> r1, Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> operator_lessEqualsThan(Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> r1, Select<? extends Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> operator_lessEqualsThan(Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> r1, Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> operator_lessEqualsThan(Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> r1, Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> operator_lessEqualsThan(Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> r1, Select<? extends Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> operator_lessEqualsThan(Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> r1, Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> operator_lessEqualsThan(Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> r1, Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> operator_lessEqualsThan(Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> r1, Select<? extends Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> operator_lessEqualsThan(Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> r1, Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> operator_lessEqualsThan(Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> r1, Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> operator_lessEqualsThan(Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> r1, Select<? extends Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> operator_lessEqualsThan(Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> r1, Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> operator_lessEqualsThan(Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> r1, Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> operator_lessEqualsThan(Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> r1, Select<? extends Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> operator_lessEqualsThan(Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> r1, Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> operator_lessEqualsThan(Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> r1, Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> operator_lessEqualsThan(Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> r1, Select<? extends Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> operator_lessEqualsThan(Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> r1, Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> operator_lessEqualsThan(Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> r1, Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> operator_lessEqualsThan(Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> r1, Select<? extends Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> operator_lessEqualsThan(Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> r1, Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> operator_lessEqualsThan(Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> r1, Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> r2) {
        r1.le(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> operator_lessEqualsThan(Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> r1, Select<? extends Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>> r2) {
        r1.le(r2)
    }

    def static <T> operator_greaterEqualsThan(Field<T> f1, T f2) {
        f1.ge(f2)
    }

    def static <T> operator_greaterEqualsThan(Field<T> f1, Field<T> f2) {
        f1.ge(f2)
    }

    def static <T> operator_greaterEqualsThan(Field<T> f1, Select<? extends Record1<T>> f2) {
        f1.ge(f2)
    }

    def static <T> operator_greaterEqualsThan(Field<T> f1, QuantifiedSelect<? extends Record1<T>> f2) {
        f1.ge(f2)
    }

    def static <T1> operator_greaterEqualsThan(Row1<T1> r1, Row1<T1> r2) {
        r1.ge(r2)
    }

    def static <T1> operator_greaterEqualsThan(Row1<T1> r1, Record1<T1> r2) {
        r1.ge(r2)
    }

    def static <T1> operator_greaterEqualsThan(Row1<T1> r1, Select<? extends Record1<T1>> r2) {
        r1.ge(r2)
    }

    def static <T1, T2> operator_greaterEqualsThan(Row2<T1, T2> r1, Row2<T1, T2> r2) {
        r1.ge(r2)
    }

    def static <T1, T2> operator_greaterEqualsThan(Row2<T1, T2> r1, Record2<T1, T2> r2) {
        r1.ge(r2)
    }

    def static <T1, T2> operator_greaterEqualsThan(Row2<T1, T2> r1, Select<? extends Record2<T1, T2>> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3> operator_greaterEqualsThan(Row3<T1, T2, T3> r1, Row3<T1, T2, T3> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3> operator_greaterEqualsThan(Row3<T1, T2, T3> r1, Record3<T1, T2, T3> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3> operator_greaterEqualsThan(Row3<T1, T2, T3> r1, Select<? extends Record3<T1, T2, T3>> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4> operator_greaterEqualsThan(Row4<T1, T2, T3, T4> r1, Row4<T1, T2, T3, T4> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4> operator_greaterEqualsThan(Row4<T1, T2, T3, T4> r1, Record4<T1, T2, T3, T4> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4> operator_greaterEqualsThan(Row4<T1, T2, T3, T4> r1, Select<? extends Record4<T1, T2, T3, T4>> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4, T5> operator_greaterEqualsThan(Row5<T1, T2, T3, T4, T5> r1, Row5<T1, T2, T3, T4, T5> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4, T5> operator_greaterEqualsThan(Row5<T1, T2, T3, T4, T5> r1, Record5<T1, T2, T3, T4, T5> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4, T5> operator_greaterEqualsThan(Row5<T1, T2, T3, T4, T5> r1, Select<? extends Record5<T1, T2, T3, T4, T5>> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4, T5, T6> operator_greaterEqualsThan(Row6<T1, T2, T3, T4, T5, T6> r1, Row6<T1, T2, T3, T4, T5, T6> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4, T5, T6> operator_greaterEqualsThan(Row6<T1, T2, T3, T4, T5, T6> r1, Record6<T1, T2, T3, T4, T5, T6> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4, T5, T6> operator_greaterEqualsThan(Row6<T1, T2, T3, T4, T5, T6> r1, Select<? extends Record6<T1, T2, T3, T4, T5, T6>> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7> operator_greaterEqualsThan(Row7<T1, T2, T3, T4, T5, T6, T7> r1, Row7<T1, T2, T3, T4, T5, T6, T7> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7> operator_greaterEqualsThan(Row7<T1, T2, T3, T4, T5, T6, T7> r1, Record7<T1, T2, T3, T4, T5, T6, T7> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7> operator_greaterEqualsThan(Row7<T1, T2, T3, T4, T5, T6, T7> r1, Select<? extends Record7<T1, T2, T3, T4, T5, T6, T7>> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8> operator_greaterEqualsThan(Row8<T1, T2, T3, T4, T5, T6, T7, T8> r1, Row8<T1, T2, T3, T4, T5, T6, T7, T8> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8> operator_greaterEqualsThan(Row8<T1, T2, T3, T4, T5, T6, T7, T8> r1, Record8<T1, T2, T3, T4, T5, T6, T7, T8> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8> operator_greaterEqualsThan(Row8<T1, T2, T3, T4, T5, T6, T7, T8> r1, Select<? extends Record8<T1, T2, T3, T4, T5, T6, T7, T8>> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9> operator_greaterEqualsThan(Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> r1, Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9> operator_greaterEqualsThan(Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> r1, Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9> operator_greaterEqualsThan(Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> r1, Select<? extends Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> operator_greaterEqualsThan(Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> r1, Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> operator_greaterEqualsThan(Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> r1, Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> operator_greaterEqualsThan(Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> r1, Select<? extends Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> operator_greaterEqualsThan(Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> r1, Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> operator_greaterEqualsThan(Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> r1, Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> operator_greaterEqualsThan(Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> r1, Select<? extends Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> operator_greaterEqualsThan(Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> r1, Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> operator_greaterEqualsThan(Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> r1, Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> operator_greaterEqualsThan(Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> r1, Select<? extends Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> operator_greaterEqualsThan(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> r1, Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> operator_greaterEqualsThan(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> r1, Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> operator_greaterEqualsThan(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> r1, Select<? extends Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> operator_greaterEqualsThan(Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> r1, Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> operator_greaterEqualsThan(Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> r1, Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> operator_greaterEqualsThan(Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> r1, Select<? extends Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> operator_greaterEqualsThan(Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> r1, Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> operator_greaterEqualsThan(Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> r1, Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> operator_greaterEqualsThan(Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> r1, Select<? extends Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> operator_greaterEqualsThan(Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> r1, Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> operator_greaterEqualsThan(Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> r1, Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> operator_greaterEqualsThan(Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> r1, Select<? extends Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> operator_greaterEqualsThan(Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> r1, Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> operator_greaterEqualsThan(Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> r1, Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> operator_greaterEqualsThan(Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> r1, Select<? extends Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> operator_greaterEqualsThan(Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> r1, Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> operator_greaterEqualsThan(Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> r1, Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> operator_greaterEqualsThan(Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> r1, Select<? extends Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> operator_greaterEqualsThan(Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> r1, Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> operator_greaterEqualsThan(Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> r1, Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> operator_greaterEqualsThan(Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> r1, Select<? extends Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> operator_greaterEqualsThan(Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> r1, Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> operator_greaterEqualsThan(Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> r1, Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> operator_greaterEqualsThan(Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> r1, Select<? extends Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> operator_greaterEqualsThan(Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> r1, Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> operator_greaterEqualsThan(Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> r1, Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> operator_greaterEqualsThan(Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> r1, Select<? extends Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> operator_greaterEqualsThan(Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> r1, Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> operator_greaterEqualsThan(Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> r1, Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> r2) {
        r1.ge(r2)
    }

    def static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> operator_greaterEqualsThan(Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> r1, Select<? extends Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>> r2) {
        r1.ge(r2)
    }

    def static operator_upTo(Field<Integer> f1, Integer f2) {
        DSL::generateSeries(f1, DSL::value(f2))
    }

    def static operator_upTo(Field<Integer> f1, Field<Integer> f2) {
        DSL::generateSeries(f1, f2)
    }

    def static <T extends Number> operator_doubleLessThan(Field<T> f1, T f2) {
        DSL::shl(f1, f2)
    }

    def static <T extends Number> operator_doubleLessThan(Field<T> f1, Field<T> f2) {
        DSL::shl(f1, f2)
    }

    def static <T extends Number> operator_doubleGreaterThan(Field<T> f1, T f2) {
        DSL::shr(f1, f2)
    }

    def static <T extends Number> operator_doubleGreaterThan(Field<T> f1, Field<T> f2) {
        DSL::shr(f1, f2)
    }

    def static <T> operator_diamond(Field<T> f1, T f2) {
        f1.ne(f2)
    }

    def static <T> operator_elvis(Field<T> f1, T f2) {
        DSL::nvl(f1, f2)
    }

    def static <T> operator_elvis(Field<T> f1, Field<T> f2) {
        DSL::nvl(f1, f2)
    }

    def static <T> operator_spaceship(Field<T> f1, T f2) {
        f1.isNotDistinctFrom(f2)
    }

    def static <T> operator_spaceship(Field<T> f1, Field<T> f2) {
        f1.isNotDistinctFrom(f2)
    }

    def static <T extends Number> operator_plus(Field<T> f1, T f2) {
        f1.add(f2)
    }

    def static <T extends Number> operator_plus(Field<T> f1, Field<T> f2) {
        f1.add(f2)
    }

    def static <T extends Number> operator_minus(Field<T> f1, T f2) {
        f1.sub(f2)
    }

    def static <T extends Number> operator_minus(Field<T> f1, Field<T> f2) {
        f1.sub(f2)
    }

    def static <T extends Number> operator_multiply(Field<T> f1, T f2) {
        f1.mul(f2)
    }

    def static <T extends Number> operator_multiply(Field<T> f1, Field<T> f2) {
        f1.mul(f2)
    }

    def static <T extends Number> operator_divide(Field<T> f1, T f2) {
        f1.div(f2)
    }

    def static <T extends Number> operator_divide(Field<T> f1, Field<T> f2) {
        f1.div(f2)
    }

    def static <T extends Number> operator_modulo(Field<T> f1, T f2) {
        f1.mod(f2)
    }

    def static <T extends Number> operator_modulo(Field<T> f1, Field<T> f2) {
        f1.mod(f2)
    }

    def static <T extends Number> operator_power(Field<T> f1, T f2) {
        DSL::power(f1, f2)
    }

    def static <T extends Number> operator_power(Field<T> f1, Field<T> f2) {
        DSL::power(f1, f2)
    }

    def static operator_not(Condition c) {
        c.not()
    }

    def static <T> operator_minus(Field<T> f) {
        f.neg();
    }
}
