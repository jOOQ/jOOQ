/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jooq;

import javax.annotation.Generated;

/**
 * A model type for a records with degree <code>20</code>
 *
 * @see Row20
 * @author Lukas Eder
 */
@Generated("This class was generated using jOOQ-tools")
public interface Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> extends Record {

    // ------------------------------------------------------------------------
    // Row value expressions
    // ------------------------------------------------------------------------

    /**
     * Get this record's fields as a {@link Row20}
     */
    @Override
    Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> fieldsRow();

    /**
     * Get this record's values as a {@link Row20}
     */
    @Override
    Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> valuesRow();

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

}
