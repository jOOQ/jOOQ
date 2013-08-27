/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under LGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 * 
 * LGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
 */
package org.jooq;

import javax.annotation.Generated;

/**
 * A model type for a records with degree <code>15</code>
 *
 * @see Row15
 * @author Lukas Eder
 */
@Generated("This class was generated using jOOQ-tools")
public interface Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> extends Record {

    // ------------------------------------------------------------------------
    // Row value expressions
    // ------------------------------------------------------------------------

    /**
     * Get this record's fields as a {@link Row15}
     */
    @Override
    Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> fieldsRow();

    /**
     * Get this record's values as a {@link Row15}
     */
    @Override
    Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> valuesRow();

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

}
