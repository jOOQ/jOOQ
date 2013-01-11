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

/**
 * A model type for a row value expression.
 * <p>
 * Note: Not all databases support row value expressions, but many row value
 * expression operations can be simulated on all databases. See relevant row
 * value expression method Javadocs for details.
 *
 * @author Lukas Eder
 */
public interface Row extends QueryPart {

    /**
     * Get the degree of this row value expression
     */
    int size();

    /**
     * Get a specific field from this row.
     * <p>
     * Usually, this will return the field itself. However, if this is a row
     * from an aliased table, the field will be aliased accordingly.
     *
     * @param <T> The generic field type
     * @param field The field to fetch
     * @return The field itself or an aliased field
     */
    <T> Field<T> field(Field<T> field);

    /**
     * Get a specific field from this row.
     *
     * @param name The field to fetch
     * @return The field with the given name
     */
    Field<?> field(String name);

    /**
     * Get a specific field from this row.
     *
     * @param index The field's index of the field to fetch
     * @return The field with the given name
     */
    Field<?> field(int index);

    /**
     * Get all fields from this row.
     *
     * @return All available fields
     */
    Field<?>[] fields();

    /**
     * Get a field's index from this row.
     *
     * @param field The field to look for
     * @return The field's index or <code>-1</code> if the field is not
     *         contained in this <code>Row</code>
     */
    int indexOf(Field<?> field);

    /**
     * Get a field's index from this row.
     *
     * @param fieldName The field name to look for
     * @return The field's index or <code>-1</code> if the field is not
     *         contained in this <code>Row</code>
     */
    int indexOf(String fieldName);

    // ------------------------------------------------------------------------
    // [NOT] NULL predicates
    // ------------------------------------------------------------------------

    /**
     * Check if this row value expression contains only <code>NULL</code> values
     * <p>
     * Row NULL predicates can be simulated in those databases that do not
     * support such predicates natively: <code>(A, B) IS NULL</code> is
     * equivalent to <code>A IS NULL AND B IS NULL</code>
     */
    @Support
    Condition isNull();

    /**
     * Check if this row value expression contains no <code>NULL</code> values
     * <p>
     * Row NOT NULL predicates can be simulated in those databases that do not
     * support such predicates natively: <code>(A, B) IS NOT NULL</code> is
     * equivalent to <code>A IS NOT NULL AND B IS NOT NULL</code>
     * <p>
     * Note that the two following predicates are NOT equivalent:
     * <ul>
     * <li><code>(A, B) IS NOT NULL</code>, which is the same as
     * <code>(A IS NOT NULL) AND (B IS NOT NULL)</code></li>
     * <li><code>NOT((A, B) IS NULL)</code>, which is the same as
     * <code>(A IS NOT NULL) OR (B IS NOT NULL)</code></li>
     * </ul>
     */
    @Support
    Condition isNotNull();

}
