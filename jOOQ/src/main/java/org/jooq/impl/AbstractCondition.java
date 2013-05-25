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

import static org.jooq.impl.DSL.condition;
import static org.jooq.impl.DSL.exists;
import static org.jooq.impl.DSL.notExists;

import java.util.Arrays;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Operator;
import org.jooq.QueryPart;
import org.jooq.Select;

/**
 * @author Lukas Eder
 */
abstract class AbstractCondition extends AbstractQueryPart implements Condition {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -6683692251799468624L;

    AbstractCondition() {}

    @Override
    public final Condition and(Condition other) {
        return new CombinedCondition(Operator.AND, Arrays.asList(this, other));
    }

    /*
     * Subclasses may override this implementation when implementing
     * A BETWEEN B AND C
     */
    @Override
    public Condition and(Field<Boolean> other) {
        return and(condition(other));
    }

    @Override
    public final Condition or(Condition other) {
        return new CombinedCondition(Operator.OR, Arrays.asList(this, other));
    }

    @Override
    public final Condition or(Field<Boolean> other) {
        return or(condition(other));
    }

    @Override
    public final Condition and(String sql) {
        return and(condition(sql));
    }

    @Override
    public final Condition and(String sql, Object... bindings) {
        return and(condition(sql, bindings));
    }

    @Override
    public final Condition and(String sql, QueryPart... parts) {
        return and(condition(sql, parts));
    }

    @Override
    public final Condition or(String sql) {
        return or(condition(sql));
    }

    @Override
    public final Condition or(String sql, Object... bindings) {
        return or(condition(sql, bindings));
    }

    @Override
    public final Condition or(String sql, QueryPart... parts) {
        return or(condition(sql, parts));
    }

    @Override
    public final Condition andNot(Condition other) {
        return and(other.not());
    }

    @Override
    public final Condition andNot(Field<Boolean> other) {
        return andNot(condition(other));
    }

    @Override
    public final Condition orNot(Condition other) {
        return or(other.not());
    }

    @Override
    public final Condition orNot(Field<Boolean> other) {
        return orNot(condition(other));
    }

    @Override
    public final Condition andExists(Select<?> select) {
        return and(exists(select));
    }

    @Override
    public final Condition andNotExists(Select<?> select) {
        return and(notExists(select));
    }

    @Override
    public final Condition orExists(Select<?> select) {
        return or(exists(select));
    }

    @Override
    public final Condition orNotExists(Select<?> select) {
        return or(notExists(select));
    }

    @Override
    public final Condition not() {
        return new NotCondition(this);
    }
}
