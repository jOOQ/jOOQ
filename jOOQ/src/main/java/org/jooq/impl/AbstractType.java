/**
 * Copyright (c) 2009-2011, Lukas Eder, lukas.eder@gmail.com
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
package org.jooq.impl;

import org.jooq.AliasProvider;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.Type;

abstract class AbstractType<R extends Record> extends AbstractSchemaProviderQueryPart implements Type<R> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -4629861305735726005L;

    /**
     * @deprecated - 1.6.1 [#453] - Regenerate your schema
     */
    @SuppressWarnings("unused")
    @Deprecated
    AbstractType(Configuration configuration, String name) {
        this(name, null);
    }

    /**
     * @deprecated - 1.6.1 [#453] - Regenerate your schema
     */
    @SuppressWarnings("unused")
    @Deprecated
    AbstractType(Configuration configuration, String name, Schema schema) {
        this(name, schema);
    }

    AbstractType(String name) {
        this(name, null);
    }

    AbstractType(String name, Schema schema) {
        super(name, schema);
    }

    @Override
    public final FieldList getFields() {
        return getFieldList();
    }

    @Override
    public final <T> Field<T> getField(Field<T> field) {
        return getFields().getField(field);
    }

    @Override
    public final Field<?> getField(String name) {
        return getFields().getField(name);
    }

    @Override
    public final Field<?> getField(int index) {
        return getFields().getField(index);
    }

    @Override
    public final int getIndex(Field<?> field) throws IllegalArgumentException {
        return getFields().getIndex(field);
    }

    /**
     * Subclasses should override this method to provide the set of fields
     * contained in the concrete table implementation. For example, a
     * {@link TableAlias} contains aliased fields of its {@link AliasProvider}
     * table.
     */
    protected abstract FieldList getFieldList();
}
