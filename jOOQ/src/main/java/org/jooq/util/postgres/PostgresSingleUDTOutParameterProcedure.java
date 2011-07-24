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
package org.jooq.util.postgres;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.jooq.Attachable;
import org.jooq.Configuration;
import org.jooq.DataType;
import org.jooq.Parameter;
import org.jooq.SQLDialect;
import org.jooq.Schema;
import org.jooq.impl.AbstractStoredProcedure;
import org.jooq.impl.StoredFunctionImpl;

/**
 * @author Lukas Eder
 */
public class PostgresSingleUDTOutParameterProcedure extends AbstractStoredProcedure {

    private static final long serialVersionUID = 2424820485339322794L;

    private Object            result;
    private DelegateFunction  delegate;

    public PostgresSingleUDTOutParameterProcedure(SQLDialect dialect, String name, Schema schema) {
        super(dialect, name, schema, null);
    }

    public PostgresSingleUDTOutParameterProcedure(Configuration configuration, String name, Schema schema) {
        super(configuration, name, schema, null);
    }

    @Override
    protected final List<Attachable> getAttachables2() {
        return Arrays.asList((Attachable) getDelegate());
    }

    @Override
    public final int execute() throws SQLException {
        int i = getDelegate().execute();
        result = getDelegate().getReturnValue();

        return i;
    }

    @Override
    public final String toSQLReference(Configuration configuration, boolean inlineParameters) {
        return getDelegate().toSQLReference(configuration, inlineParameters);
    }

    @Override
    public final int bindReference(Configuration configuration, PreparedStatement stmt, int initialIndex) throws SQLException {
        return getDelegate().bindReference(configuration, stmt, initialIndex);
    }

    private final StoredFunctionImpl<?> getDelegate() {
        if (delegate == null) {
            delegate = new DelegateFunction();
        }

        return delegate;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected final <T> T getValue(Parameter<T> parameter) {
        return (T) result;
    }

    private final class DelegateFunction extends StoredFunctionImpl<Object> {
        private static final long serialVersionUID = 5924709487050570519L;

        @SuppressWarnings("unchecked")
        public DelegateFunction() {
            super(
                SQLDialect.POSTGRES,
                PostgresSingleUDTOutParameterProcedure.this.getName(),
                PostgresSingleUDTOutParameterProcedure.this.getSchema(),
                (DataType<Object>) PostgresSingleUDTOutParameterProcedure.this.getOutParameters().get(0).getDataType());

            for (Parameter<?> p : PostgresSingleUDTOutParameterProcedure.this.getInParameters()) {
                addInParameter(p);
            }
        }
    }
}
