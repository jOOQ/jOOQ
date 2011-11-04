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

import java.util.List;

import org.jooq.Attachable;
import org.jooq.BindContext;
import org.jooq.Configuration;
import org.jooq.DataType;
import org.jooq.QueryPart;
import org.jooq.RenderContext;
import org.jooq.SQLDialect;

/**
 * @author Lukas Eder
 */
class Function<T> extends AbstractField<T> {

    private static final long serialVersionUID = 347252741712134044L;

    private final QueryPart[] arguments;
    private final Term        term;

    Function(String name, DataType<T> type, QueryPart... arguments) {
        super(name, type);

        this.arguments = arguments;
        this.term = null;
    }

    Function(Term term, DataType<T> type, QueryPart... arguments) {
        super(term.name().toLowerCase(), type);

        this.arguments = arguments;
        this.term = term;
    }

    @Override
    public final List<Attachable> getAttachables() {
        return getAttachables(arguments);
    }

    @Override
    public final void toSQL(RenderContext context) {
        context.sql(getFNPrefix());
        context.sql(getFNName(context.getDialect()));
        context.sql(getArgumentListDelimiter(context, "("));

        String separator = "";
        for (QueryPart field : arguments) {
            context.sql(separator);
            toSQLField(context, field);

            separator = ", ";
        }

        context.sql(getArgumentListDelimiter(context, ")"));
        context.sql(getFNSuffix());
    }

    private final String getFNName(SQLDialect dialect) {
        if (term != null) {
            return term.translate(dialect);
        }
        else {
            return getName();
        }
    }

    final Term getTerm() {
        return term;
    }

    /**
     * Subclasses may override this method to add an additional prefix in the
     * function SQL string
     */
    protected String getFNPrefix() {
        return "";
    }

    /**
     * Subclasses may override this method to add an additional suffix in the
     * function SQL string
     */
    protected String getFNSuffix() {
        return "";
    }

    private final String getArgumentListDelimiter(Configuration configuration, String delimiter) {
        switch (configuration.getDialect()) {
            case ORACLE:   // No break
            case DB2:      // No break

                // Empty argument lists do not have parentheses ()
                if (arguments.length == 0) {
                    return "";
                } else {
                    return delimiter;
                }

            case SQLITE:   // No break
            case DERBY:    // No break
            case H2:       // No break
            case HSQLDB:   // No break
            case INGRES:   // No break
            case MYSQL:    // No break
            case POSTGRES: // No break
            case SQLSERVER:// No break
            case SYBASE:

            // Default behaviour is needed for hashCode() and toString();
            default:
                return delimiter;
        }
    }

    /**
     * Render the argument field. This renders the field directly, by default.
     * Subclasses may override this method, if needed (e.g. to render
     * count(distinct [field])
     */
    protected void toSQLField(RenderContext context, QueryPart field) {
        context.sql(field);
    }

    @Override
    public final void bind(BindContext context) {
        context.bind(arguments);
    }

    @Override
    public final boolean isNullLiteral() {
        return false;
    }

    final QueryPart[] getArguments() {
        return arguments;
    }
}
