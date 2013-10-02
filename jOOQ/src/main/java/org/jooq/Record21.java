/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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
package org.jooq;

import javax.annotation.Generated;

/**
 * A model type for a records with degree <code>21</code>
 *
 * @see Row21
 * @author Lukas Eder
 */
@Generated("This class was generated using jOOQ-tools")
public interface Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> extends Record {

    // ------------------------------------------------------------------------
    // Row value expressions
    // ------------------------------------------------------------------------

    /**
     * Get this record's fields as a {@link Row21}
     */
    @Override
    Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> fieldsRow();

    /**
     * Get this record's values as a {@link Row21}
     */
    @Override
    Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> valuesRow();

    // ------------------------------------------------------------------------
    // Field accessors
    // ------------------------------------------------------------------------

    /**
     * Get the first field
     */
    Field<T1> field1();

    /**
     * Get the second field
     */
    Field<T2> field2();

    /**
     * Get the third field
     */
    Field<T3> field3();

    /**
     * Get the fourth field
     */
    Field<T4> field4();

    /**
     * Get the fifth field
     */
    Field<T5> field5();

    /**
     * Get the sixth field
     */
    Field<T6> field6();

    /**
     * Get the seventh field
     */
    Field<T7> field7();

    /**
     * Get the eighth field
     */
    Field<T8> field8();

    /**
     * Get the ninth field
     */
    Field<T9> field9();

    /**
     * Get the tenth field
     */
    Field<T10> field10();

    /**
     * Get the eleventh field
     */
    Field<T11> field11();

    /**
     * Get the twelfth field
     */
    Field<T12> field12();

    /**
     * Get the thirteenth field
     */
    Field<T13> field13();

    /**
     * Get the fourteenth field
     */
    Field<T14> field14();

    /**
     * Get the fifteenth field
     */
    Field<T15> field15();

    /**
     * Get the sixteenth field
     */
    Field<T16> field16();

    /**
     * Get the seventeenth field
     */
    Field<T17> field17();

    /**
     * Get the eighteenth field
     */
    Field<T18> field18();

    /**
     * Get the ninteenth field
     */
    Field<T19> field19();

    /**
     * Get the twentieth field
     */
    Field<T20> field20();

    /**
     * Get the twenty-first field
     */
    Field<T21> field21();

    // ------------------------------------------------------------------------
    // Value accessors
    // ------------------------------------------------------------------------

    /**
     * Get the first value
     */
    T1 value1();

    /**
     * Get the second value
     */
    T2 value2();

    /**
     * Get the third value
     */
    T3 value3();

    /**
     * Get the fourth value
     */
    T4 value4();

    /**
     * Get the fifth value
     */
    T5 value5();

    /**
     * Get the sixth value
     */
    T6 value6();

    /**
     * Get the seventh value
     */
    T7 value7();

    /**
     * Get the eighth value
     */
    T8 value8();

    /**
     * Get the ninth value
     */
    T9 value9();

    /**
     * Get the tenth value
     */
    T10 value10();

    /**
     * Get the eleventh value
     */
    T11 value11();

    /**
     * Get the twelfth value
     */
    T12 value12();

    /**
     * Get the thirteenth value
     */
    T13 value13();

    /**
     * Get the fourteenth value
     */
    T14 value14();

    /**
     * Get the fifteenth value
     */
    T15 value15();

    /**
     * Get the sixteenth value
     */
    T16 value16();

    /**
     * Get the seventeenth value
     */
    T17 value17();

    /**
     * Get the eighteenth value
     */
    T18 value18();

    /**
     * Get the ninteenth value
     */
    T19 value19();

    /**
     * Get the twentieth value
     */
    T20 value20();

    /**
     * Get the twenty-first value
     */
    T21 value21();

}
