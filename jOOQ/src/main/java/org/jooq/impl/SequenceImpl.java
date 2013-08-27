/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under LGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 * 
 * LGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
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
                local.visit(mappedSchema);
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
