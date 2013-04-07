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

import static java.util.Arrays.asList;
import static org.jooq.SQLDialect.DB2;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.INGRES;
import static org.jooq.SQLDialect.ORACLE;
import static org.jooq.impl.ExpressionOperator.BIT_NOT;

import org.jooq.BindContext;
import org.jooq.Field;
import org.jooq.RenderContext;
import org.jooq.SQLDialect;

/**
 * @author Lukas Eder
 */
class Neg<T> extends AbstractField<T> {

    /**
     * Generated UID
     */
    private static final long        serialVersionUID = 7624782102883057433L;

    private final ExpressionOperator operator;
    private final Field<T>           field;

    Neg(Field<T> field, ExpressionOperator operator) {
        super(operator.toSQL() + field.getName(), field.getDataType());

        this.operator = operator;
        this.field = field;
    }

    @Override
    public final void toSQL(RenderContext context) {
        SQLDialect dialect = context.configuration().dialect();

        if (operator == BIT_NOT && asList(H2, HSQLDB, INGRES, ORACLE).contains(dialect)) {
            context.sql("(0 -")
                   .sql(field)
                   .sql(" - 1)");
        }
        else if (operator == BIT_NOT && dialect == DB2) {
            context.keyword("bitnot(")
                   .sql(field)
                   .sql(")");
        }
        else if (operator == BIT_NOT && dialect == FIREBIRD) {
            context.keyword("bin_not(")
                   .sql(field)
                   .sql(")");
        }
        else {
            context.sql(operator.toSQL())
                   .sql("(")
                   .sql(field)
                   .sql(")");
        }
    }

    @Override
    public final void bind(BindContext context) {
        context.bind(field);
    }
}
