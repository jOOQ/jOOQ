/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
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

/* [pro] */

import java.sql.Array;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

/**
 * A "record" that encapsulates an Oracle-style ARRAY (or VARRAY), additionally
 * providing some convenience methods
 *
 * @param <E> The array element type
 * @author Lukas Eder
 */
public interface ArrayRecord<E> extends Attachable, List<E> {

    /**
     * Get the contained array.
     *
     * @deprecated - 3.7.0 - [#4566] - Use {@link #toArray()} instead.
     */
    @Deprecated
    E[] get();

    /**
     * Get the contained array as a {@link List}.
     *
     * @deprecated - 3.7.0 - [#4566] - ArrayRecord already extends {@link List}.
     *             There is no need to call this any more.
     */
    @Deprecated
    List<E> getList();

    /**
     * Set the contained array.
     *
     * @deprecated - 3.4.0 - [#3127] - Do not use this method any more.
     */
    @Deprecated
    void set(Array array) throws SQLException;

    /**
     * Set the contained array.
     *
     * @deprecated - 3.7.0 - [#4566] - Use {@link List} methods instead.
     */
    @Deprecated
    @SuppressWarnings("unchecked")
    void set(E... array);

    /**
     * Set the contained array as a {@link List}.
     *
     * @deprecated - 3.7.0 - [#4566] - Use {@link List} methods instead.
     */
    @Deprecated
    void set(Collection<? extends E> list);

    /**
     * Set the contained array as a {@link List}.
     *
     * @deprecated - 3.4.0 - [#3128] Use {@link #set(Collection)} instead.
     */
    @Deprecated
    void setList(List<? extends E> list);

    /**
     * Get the record type's schema.
     */
    Schema getSchema();

    /**
     * Get the name of the array type.
     */
    String getName();

    /**
     * Get the data type of the array's base type.
     */
    DataType<E> getDataType();

    /**
     * Get the data type of the array.
     */
    DataType<?> getArrayType();
}
/* [/pro] */