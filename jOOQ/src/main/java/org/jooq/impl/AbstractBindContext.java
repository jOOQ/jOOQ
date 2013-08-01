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

import static org.jooq.impl.Utils.visitAll;

import java.sql.SQLException;
import java.util.Collection;

import org.jooq.BindContext;
import org.jooq.Configuration;
import org.jooq.QueryPart;
import org.jooq.QueryPartInternal;

/**
 * A base class for {@link BindContext} implementations
 *
 * @author Lukas Eder
 */
abstract class AbstractBindContext extends AbstractContext<BindContext> implements BindContext {

    AbstractBindContext(Configuration configuration) {
        super(configuration);
    }

    // ------------------------------------------------------------------------
    // BindContext API
    // ------------------------------------------------------------------------

    @Override
    @Deprecated
    public final BindContext bind(Collection<? extends QueryPart> parts) {
        return visitAll(this, parts);
    }

    @Override
    @Deprecated
    public final BindContext bind(QueryPart[] parts) {
        return visitAll(this, parts);
    }

    @Override
    @Deprecated
    public final BindContext bind(QueryPart part) {
        return visit(part);
    }

    @Override
    protected void visit0(QueryPartInternal internal) {
        bindInternal(internal);
    }

    @Override
    public final BindContext bindValues(Object... values) {

        // [#724] When values is null, this is probably due to API-misuse
        // The user probably meant new Object[] { null }
        if (values == null) {
            bindValues(new Object[] { null });
        }
        else {
            for (Object value : values) {
                Class<?> type = (value == null) ? Object.class : value.getClass();
                bindValue(value, type);
            }
        }

        return this;
    }

    @Override
    public final BindContext bindValue(Object value, Class<?> type) {
        try {
            return bindValue0(value, type);
        }
        catch (SQLException e) {
            throw Utils.translate(null, e);
        }
    }

    // ------------------------------------------------------------------------
    // AbstractBindContext template methods
    // ------------------------------------------------------------------------

    /**
     * Subclasses may override this method to achieve different behaviour
     */
    protected void bindInternal(QueryPartInternal internal) {
        internal.bind(this);
    }

    /**
     * Subclasses may override this method to achieve different behaviour
     */
    @SuppressWarnings("unused")
    protected BindContext bindValue0(Object value, Class<?> type) throws SQLException {
        return this;
    }

    // ------------------------------------------------------------------------
    // Object API
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        toString(sb);
        return sb.toString();
    }
}
