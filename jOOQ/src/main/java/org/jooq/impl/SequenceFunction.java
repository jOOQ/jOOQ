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
package org.jooq.impl;

import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.impl.Factory.field;

import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.RenderContext;
import org.jooq.exception.SQLDialectNotSupportedException;

/**
 * @author Lukas Eder
 */
class SequenceFunction<T extends Number> extends AbstractFunction<T> {

    /**
     * Generated UID
     */
    private static final long     serialVersionUID = 2292275568395094887L;

    private final SequenceImpl<T> sequence;
    private final String          method;

    SequenceFunction(SequenceImpl<T> sequence, String method) {
        super(method, sequence.type);

        this.sequence = sequence;
        this.method = method;
    }

    @Override
    final Field<T> getFunction0(Configuration configuration) {
        switch (configuration.getDialect()) {
            case DB2:
            case INGRES:
            case ORACLE:
            case SYBASE: {
                String field = getQualifiedName(configuration) + "." + method;
                return field(field, getDataType());
            }
            
            case H2:
            case POSTGRES: {
                String field = method + "('" + getQualifiedName(configuration) + "')";
                return field(field, getDataType());
            }

            case DERBY:
            case HSQLDB: {
                if ("nextval".equals(method)) {
                    String field = "next value for " + getQualifiedName(configuration);
                    return field(field, getDataType());
                }
                else {
                    throw new SQLDialectNotSupportedException("The sequence's current value functionality is not supported for the " + configuration.getDialect() + " dialect.");
                }
            }

            case CUBRID: {
                String field = getQualifiedName(configuration) + ".";

                if ("nextval".equals(method)) {
                    field += "next_value";
                }
                else {
                    field += "current_value";
                }

                return field(field, getDataType());
            }

            // Default is needed for hashCode() and toString()
            default: {
                String field = getQualifiedName(configuration) + "." + method;
                return field(field, getDataType());
            }
        }
    }

    private final String getQualifiedName(Configuration configuration) {
        RenderContext local = create(configuration).renderContext();

        if (sequence.schema != null && configuration.getDialect() != CUBRID) {
            local.sql(Util.getMappedSchema(configuration, sequence.schema));
            local.sql(".");
        }

        local.literal(sequence.name);
        return local.render();
    }
}
