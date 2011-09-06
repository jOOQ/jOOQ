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

import java.sql.SQLException;
import java.util.List;

import org.jooq.Attachable;
import org.jooq.BindContext;
import org.jooq.Field;
import org.jooq.RenderContext;
import org.jooq.SortField;
import org.jooq.SortOrder;

class SortFieldImpl<T> extends AbstractNamedTypeProviderQueryPart<T> implements SortField<T> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 1223739398544155873L;

    private final Field<T>    field;
    private final SortOrder   order;
    private boolean           nullsFirst;
    private boolean           nullsLast;

    SortFieldImpl(Field<T> field, SortOrder order) {
        super(field.getName(), field.getDataType());

        this.field = field;
        this.order = order;
    }

    @Override
    public final SortOrder getOrder() {
        return order;
    }

    @Override
    public final SortField<T> nullsFirst() {
        nullsFirst = true;
        nullsLast = false;
        return this;
    }

    @Override
    public final SortField<T> nullsLast() {
        nullsFirst = false;
        nullsLast = true;
        return this;
    }

    @Override
    public final List<Attachable> getAttachables() {
        return getAttachables(field);
    }

    @Override
    public final void toSQL(RenderContext context) {
        toSQLReference0(context, false);
    }

    final void toSQLInAnalyticClause(RenderContext context) {
        toSQLReference0(context, true);
    }

    private final void toSQLReference0(RenderContext context, boolean inAnalyticClause) {
        if (nullsFirst || nullsLast) {
            switch (context.getDialect()) {

                // DB2 supports NULLS FIRST/LAST only in OLAP (window) functions
                case DB2:

                // These dialects don't support this syntax at all
                case ASE:
                case INGRES:
                case MYSQL:
                case SQLITE:
                case SQLSERVER:
                case SYBASE: {
                    if (!inAnalyticClause) {
                        Field<Integer> zero = create(context).zero();
                        Field<Integer> one = create(context).one();

                        Field<Integer> ifNull = nullsFirst ? zero : one;
                        Field<Integer> ifNotNull = nullsFirst ? one : zero;

                        context.sql(field.nvl2(ifNotNull, ifNull));
                        context.sql(", ");

                        toSQLReference1(context, inAnalyticClause);
                        break;
                    }
                    else {
                        // Fall through to default
                    }
                }

                // DERBY, H2, HSQLDB, ORACLE, POSTGRES
                default: {
                    toSQLReference1(context, inAnalyticClause);

                    if (nullsFirst) {
                        context.sql(" nulls first");
                    }
                    else {
                        context.sql(" nulls last");
                    }

                    break;
                }
            }
        }
        else {
            toSQLReference1(context, inAnalyticClause);
        }
    }

    private final void toSQLReference1(RenderContext context, boolean inAnalyticClause) {
        switch (context.getDialect()) {
            case SQLSERVER:
            case SYBASE: {
                if (inAnalyticClause) {
                    context.literal(field.getName());
                    break;
                }
                else {
                    // Fall through to default
                }
            }
            default: {
                context.sql(field);
                break;
            }
        }

        context.sql(" ");
        context.sql(order.toSQL());
    }

    @Override
    public final void bind(BindContext context) throws SQLException {
        context.bind(field);
    }
}
