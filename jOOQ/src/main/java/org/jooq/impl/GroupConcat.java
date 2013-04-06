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
package org.jooq.impl;

import static org.jooq.impl.DSL.inline;

import java.util.Arrays;
import java.util.Collection;

import org.jooq.AggregateFunction;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.GroupConcatOrderByStep;
import org.jooq.GroupConcatSeparatorStep;
import org.jooq.SortField;
import org.jooq.WindowBeforeOverStep;
import org.jooq.WindowPartitionByStep;

/**
 * @author Lukas Eder
 */
class GroupConcat extends AbstractFunction<String> implements GroupConcatOrderByStep {

    /**
     * Generated UID
     */
    private static final long   serialVersionUID = -6884415527559632960L;

    private final Field<?>      field;
    private final boolean       distinct;
    private final SortFieldList orderBy;
    private String              separator;

    GroupConcat(Field<?> field) {
        this(field, false);
    }

    GroupConcat(Field<?> field, boolean distinct) {
        super("group_concat", SQLDataType.VARCHAR);

        this.field = field;
        this.distinct = distinct;
        this.orderBy = new SortFieldList();
    }

    @Override
    final Field<String> getFunction0(Configuration configuration) {
        Function<String> result;

        if (separator == null) {
            result = new Function<String>(Term.LIST_AGG, distinct, SQLDataType.VARCHAR, field);
        }
        else {
            Field<String> literal = inline(separator);
            result = new Function<String>(Term.LIST_AGG, distinct, SQLDataType.VARCHAR, field, literal);
        }

        return result.withinGroupOrderBy(orderBy);
    }

    @Override
    public final WindowBeforeOverStep<String> keepDenseRankFirstOrderBy(Field<?>... fields) {
        throw new UnsupportedOperationException("KEEP() not supported on GROUP_CONCAT aggregate function");
    }

    @Override
    public final WindowBeforeOverStep<String> keepDenseRankFirstOrderBy(SortField<?>... fields) {
        throw new UnsupportedOperationException("KEEP() not supported on GROUP_CONCAT aggregate function");
    }

    @Override
    public final WindowBeforeOverStep<String> keepDenseRankFirstOrderBy(Collection<SortField<?>> fields) {
        throw new UnsupportedOperationException("KEEP() not supported on GROUP_CONCAT aggregate function");
    }

    @Override
    public final WindowBeforeOverStep<String> keepDenseRankLastOrderBy(Field<?>... fields) {
        throw new UnsupportedOperationException("KEEP() not supported on GROUP_CONCAT aggregate function");
    }

    @Override
    public final WindowBeforeOverStep<String> keepDenseRankLastOrderBy(SortField<?>... fields) {
        throw new UnsupportedOperationException("KEEP() not supported on GROUP_CONCAT aggregate function");
    }

    @Override
    public final WindowBeforeOverStep<String> keepDenseRankLastOrderBy(Collection<SortField<?>> fields) {
        throw new UnsupportedOperationException("KEEP() not supported on GROUP_CONCAT aggregate function");
    }

    @Override
    public final WindowPartitionByStep<String> over() {
        throw new UnsupportedOperationException("OVER() not supported on GROUP_CONCAT aggregate function");
    }

    @Override
    public final AggregateFunction<String> separator(String s) {
        this.separator = s;
        return this;
    }

    @Override
    public final GroupConcatSeparatorStep orderBy(Field<?>... fields) {
        orderBy.addAll(fields);
        return this;
    }

    @Override
    public final GroupConcatSeparatorStep orderBy(SortField<?>... fields) {
        orderBy.addAll(Arrays.asList(fields));
        return this;
    }

    @Override
    public final GroupConcatSeparatorStep orderBy(Collection<SortField<?>> fields) {
        orderBy.addAll(fields);
        return this;
    }
}
