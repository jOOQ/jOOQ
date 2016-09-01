/**
 * Copyright (c) 2009-2016, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package org.jooq.impl;

import static org.jooq.Clause.SEQUENCE;
import static org.jooq.Clause.SEQUENCE_REFERENCE;
import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.FIREBIRD;
// ...
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.select;

import org.jooq.Catalog;
import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.Context;
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
public class SequenceImpl<T extends Number> extends AbstractQueryPart implements Sequence<T> {

    /**
     * Generated UID
     */
    private static final long     serialVersionUID = 6224349401603636427L;
    private static final Clause[] CLAUSES          = { SEQUENCE, SEQUENCE_REFERENCE };

    final String                  name;
    final boolean                 nameIsPlainSQL;
    final Schema                  schema;
    final DataType<T>             type;

    public SequenceImpl(String name, Schema schema, DataType<T> type) {
        this(name, schema, type, false);
    }

    SequenceImpl(String name, Schema schema, DataType<T> type, boolean nameIsPlainSQL) {
        this.name = name;
        this.schema = schema;
        this.type = type;
        this.nameIsPlainSQL = nameIsPlainSQL;
    }

    @Override
    public final String getName() {
        return name;
    }

    @Override
    public final Catalog getCatalog() {
        return getSchema() == null ? null : getSchema().getCatalog();
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
        return new SequenceFunction("currval");
    }

    @Override
    public final Field<T> nextval() {
        return new SequenceFunction("nextval");
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
            SQLDialect family = configuration.family();

            switch (family) {










                case POSTGRES: {
                    String field = method + "('" + getQualifiedName(configuration) + "')";
                    return DSL.field(field, getDataType());
                }

                case H2: {
                    String field = method + "(" + getQualifiedName(configuration, true) + ")";
                    return DSL.field(field, getDataType());
                }




                case FIREBIRD:
                case DERBY:
                case HSQLDB: {
                    if ("nextval".equals(method)) {
                        String field = "next value for " + getQualifiedName(configuration);
                        return DSL.field(field, getDataType());
                    }
                    else if (family == FIREBIRD) {
                        return DSL.field("gen_id(" + getQualifiedName(configuration) + ", 0)", getDataType());
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

                    return DSL.field(field, getDataType());
                }

                // Default is needed for hashCode() and toString()
                default: {
                    String field = getQualifiedName(configuration) + "." + method;
                    return DSL.field(field, getDataType());
                }
            }
        }

        private final String getQualifiedName(Configuration configuration) {
            return getQualifiedName(configuration, false);
        }

        private final String getQualifiedName(Configuration configuration, boolean asStringLiterals) {
            RenderContext local = create(configuration).renderContext();
            accept0(local, asStringLiterals);
            return local.render();
        }
    }

    // ------------------------------------------------------------------------
    // XXX: QueryPart API
    // ------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        accept0(ctx, false);
    }

    private final void accept0(Context<?> ctx, boolean asStringLiterals) {
        Schema mappedSchema = Tools.getMappedSchema(ctx.configuration(), schema);

        if (mappedSchema != null && ctx.family() != CUBRID) {
            if (asStringLiterals) {
                ctx.visit(inline(mappedSchema.getName()))
                   .sql(", ");
            }
            else {
                ctx.visit(mappedSchema)
                   .sql('.');
            }
        }

        if (asStringLiterals)
            ctx.visit(inline(name));
        else if (nameIsPlainSQL)
            ctx.sql(name);
        else
            ctx.literal(name);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
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
