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
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.TableRecord;
import org.jooq.Truncate;

/**
 * @author Lukas Eder
 */
class TruncateImpl<R extends TableRecord<R>> extends AbstractQuery implements Truncate {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 8904572826501186329L;

    private final Table<R>    table;

    public TruncateImpl(Configuration configuration, Table<R> table) {
        super(configuration);

        this.table = table;
    }

    @Override
    public final String toSQLReference(Configuration configuration, boolean inlineParameters) {
        switch (configuration.getDialect()) {

            // These dialects don't implement the TRUNCATE statement
            case INGRES:
            case SQLITE: {
                return internal(create(configuration).delete(table)).toSQLReference(configuration, inlineParameters);
            }

            // All other dialects do
            default: {
                StringBuilder sb = new StringBuilder();

                sb.append("truncate table ");
                sb.append(internal(table).toSQLReference(configuration, inlineParameters));

                if (configuration.getDialect() == SQLDialect.DB2) {
                    sb.append(" immediate");
                }

                return sb.toString();
            }
        }
    }

    @Override
    public final int bindReference(Configuration configuration, PreparedStatement stmt, int initialIndex) throws SQLException {
        return internal(table).bindReference(configuration, stmt, initialIndex);
    }

    @Override
    public final List<Attachable> getAttachables() {
        return getAttachables(table);
    }
}
