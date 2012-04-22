/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
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

import java.util.List;

/**
 * An object (not necessarily a {@link QueryPart}) that holds a list of
 * {@link Field}s. Especially the {@link #getField(Field)} method is
 * interesting, as it may return an aliased version of the given field
 *
 * @author Lukas Eder
 */
public interface FieldProvider {

    /**
     * Get a specific field from this field provider.
     * <p>
     * Usually, this will return the field itself. However, if this is an
     * aliased table, the field will be aliased accordingly.
     *
     * @param <T> The generic field type
     * @param field The field to fetch
     * @return The field itself or an aliased field
     */
    <T> Field<T> getField(Field<T> field);

    /**
     * Get a specific field from this field provider.
     *
     * @param name The field to fetch
     * @return The field with the given name
     */
    Field<?> getField(String name);

    /**
     * Get a specific field from this field provider.
     *
     * @param index The field's index of the field to fetch
     * @return The field with the given name
     */
    Field<?> getField(int index);

    /**
     * @return All available fields
     */
    List<Field<?>> getFields();

    /**
     * Get a fields index from this field provider
     *
     * @param field The field to look for
     * @return The field's index
     * @throws IllegalArgumentException if the field is not contained in this
     *             provider.
     */
    int getIndex(Field<?> field) throws IllegalArgumentException;
}
