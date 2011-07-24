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

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.jooq.Attachable;
import org.jooq.Configuration;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
class Position extends AbstractFunction<Integer> {

    private static final long   serialVersionUID = 3544690069533526544L;

    private final Field<String> search;
    private final Field<?>      in;

    Position(Field<String> search, Field<?> in) {
        super("position", SQLDataType.INTEGER, search, in);

        this.search = search;
        this.in = in;
    }

    @Override
    final Field<Integer> getFunction0(Configuration configuration) {
        switch (configuration.getDialect()) {
            case DB2:    // No break
            case DERBY:
                return new Function<Integer>("locate", SQLDataType.INTEGER, search, in);

            case INGRES: // No break
            case SYBASE:
                return new Function<Integer>("locate", SQLDataType.INTEGER, in, search);

            case ORACLE:
                return new Function<Integer>("instr", SQLDataType.INTEGER, in, search);

            case SQLSERVER:
                return new Function<Integer>("charindex", SQLDataType.INTEGER, search, in);

            default:
                return new SQLPosition();
        }
    }

    /**
     * The default implementation according to the SQL standard.
     *
     * @author Lukas Eder
     */
    private class SQLPosition extends AbstractField<Integer> {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 5979696080332410352L;

        SQLPosition() {
            super("position", SQLDataType.INTEGER);
        }

        @Override
        public final List<Attachable> getAttachables() {
            return getAttachables(Position.this.getArguments());
        }

        @Override
        public final String toSQLReference(Configuration configuration, boolean inlineParameters) {
            StringBuilder sb = new StringBuilder();

            sb.append("position(");
            sb.append(internal(search).toSQLReference(configuration, inlineParameters));
            sb.append(" in ");
            sb.append(internal(in).toSQLReference(configuration, inlineParameters));
            sb.append(")");

            return sb.toString();
        }

        @Override
        public final boolean isNullLiteral() {
            return false;
        }

        @Override
        public int bindReference(Configuration configuration, PreparedStatement stmt, int initialIndex) throws SQLException {
            int result = initialIndex;

            result = internal(search).bindReference(configuration, stmt, result);
            result = internal(in).bindReference(configuration, stmt, result);

            return result;
        }
    }
}
