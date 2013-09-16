/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is triple-licensed under ASL 2.0, AGPL 3.0, and jOOQ EULA
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   ASL 2.0 or jOOQ EULA.
 * - If you're using this work with at least one commercial database, you may
 *   choose AGPL 3.0 or jOOQ EULA.
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * AGPL 3.0
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 *
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details: http://www.jooq.org/eula
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
        /* [com] */
        else if (operator == BIT_NOT && dialect == DB2) {
            context.keyword("bitnot(")
                   .visit(field)
                   .sql(")");
        }
        /* [/com] */
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
