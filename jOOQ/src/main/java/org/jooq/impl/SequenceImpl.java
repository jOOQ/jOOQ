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

import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.SQLSERVER;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.select;

import org.jooq.Configuration;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.RenderContext;
import org.jooq.SQLDialect;
import org.jooq.Schema;
import org.jooq.Sequence;
import org.jooq.exception.SQLDialectNotSupportedException;

/**
 * A common base class for sequences
 * <p>
 * This type is for JOOQ INTERNAL USE only. Do not reference directly
 *
 * @author Lukas Eder
 */
public class SequenceImpl<T extends Number> implements Sequence<T> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 6224349401603636427L;

    final String              name;
    final Schema              schema;
    final DataType<T>         type;

    public SequenceImpl(String name, Schema schema, DataType<T> type) {
        this.name = name;
        this.schema = schema;
        this.type = type;
    }

    @Override
    public final String getName() {
        return name;
    }

    @Override
    public final Schema getSchema() {
        return schema;
    }

    @Override
    public final DataType<T> getDataType() {
        return type;
    }

    @Override
    public final Field<T> currval() {
        return getSequence("currval");
    }

    @Override
    public final Field<T> nextval() {
        return getSequence("nextval");
    }

    private final Field<T> getSequence(final String sequence) {
        return new SequenceFunction(sequence);
    }

    private class SequenceFunction extends AbstractFunction<T> {

        /**
         * Generated UID
         */
        private static final long     serialVersionUID = 2292275568395094887L;

        private final String          method;

        SequenceFunction(String method) {
            super(method, type);

            this.method = method;
        }

        @Override
        final Field<T> getFunction0(Configuration configuration) {
            SQLDialect family = configuration.dialect().family();

            switch (family) {
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

                case FIREBIRD:
                case DERBY:
                case HSQLDB:
                case SQLSERVER: {
                    if ("nextval".equals(method)) {
                        String field = "next value for " + getQualifiedName(configuration);
                        return field(field, getDataType());
                    }
                    else if (family == FIREBIRD) {
                        return field("gen_id(" + getQualifiedName(configuration) + ", 0)", getDataType());
                    }
                    else if (family == SQLSERVER) {
                        return select(field("current_value"))
                               .from("sys.sequences sq")
                               .join("sys.schemas sc")
                               .on("sq.schema_id = sc.schema_id")
                               .where("sq.name = ?", name)
                               .and("sc.name = ?", schema.getName())
                               .asField()
                               .cast(type);
                    }
                    else {
                        throw new SQLDialectNotSupportedException("The sequence's current value functionality is not supported for the " + family + " dialect.");
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
            Schema mappedSchema = Utils.getMappedSchema(configuration, schema);

            if (mappedSchema != null && configuration.dialect() != CUBRID) {
                local.sql(mappedSchema);
                local.sql(".");
            }

            local.literal(name);
            return local.render();
        }
    }

    // ------------------------------------------------------------------------
    // XXX: Object API
    // ------------------------------------------------------------------------

    @Override
    public int hashCode() {

        // [#1938] This is a much more efficient hashCode() implementation
        // compared to that of standard QueryParts
        return name.hashCode();
    }
}
