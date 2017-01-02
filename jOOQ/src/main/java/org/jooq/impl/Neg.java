/*
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq.impl;

import static java.util.Arrays.asList;
// ...
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HSQLDB;
// ...
// ...
// ...
import static org.jooq.impl.ExpressionOperator.BIT_NOT;

import org.jooq.Context;
import org.jooq.Field;
import org.jooq.SQLDialect;

/**
 * @author Lukas Eder
 */
final class Neg<T> extends AbstractField<T> {

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
    public final void accept(Context<?> ctx) {
        SQLDialect family = ctx.configuration().dialect().family();

        if (operator == BIT_NOT && asList(H2, HSQLDB).contains(family)) {
            ctx.sql("(0 - ")
               .visit(field)
               .sql(" - 1)");
        }







        else if (operator == BIT_NOT && family == FIREBIRD) {
            ctx.keyword("bin_not(")
               .visit(field)
               .sql(')');
        }
        else {
            ctx.sql(operator.toSQL())
               .sql('(')
               .visit(field)
               .sql(')');
        }
    }
}
