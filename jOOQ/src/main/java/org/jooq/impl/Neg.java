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

        if (operator == BIT_NOT && asList(H2, HSQLDB, INGRES, ORACLE).contains(dialect.family())) {
            context.sql("(0 -")
                   .visit(field)
                   .sql(" - 1)");
        }
        else if (operator == BIT_NOT && dialect == DB2) {
            context.keyword("bitnot(")
                   .visit(field)
                   .sql(")");
        }
        else if (operator == BIT_NOT && dialect == FIREBIRD) {
            context.keyword("bin_not(")
                   .visit(field)
                   .sql(")");
        }
        else {
            context.sql(operator.toSQL())
                   .sql("(")
                   .visit(field)
                   .sql(")");
        }
    }

    @Override
    public final void bind(BindContext context) {
        context.visit(field);
    }
}
