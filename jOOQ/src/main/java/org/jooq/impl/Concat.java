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

import static org.jooq.impl.DSL.castAll;
import static org.jooq.impl.DSL.function;
import static org.jooq.impl.ExpressionOperator.ADD;
import static org.jooq.impl.ExpressionOperator.CONCAT;

import org.jooq.Configuration;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
class Concat extends AbstractFunction<String> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -7273879239726265322L;

    Concat(Field<?>... arguments) {
        super("concat", SQLDataType.VARCHAR, arguments);
    }

    @SuppressWarnings("unchecked")
    @Override
    final Field<String> getFunction0(Configuration configuration) {

        // [#461] Type cast the concat expression, if this isn't a VARCHAR field
        Field<String>[] cast = castAll(String.class, getArguments());

        // If there is only one argument, return it immediately
        if (cast.length == 1) {
            return cast[0];
        }

        Field<String> first = cast[0];
        Field<String>[] others = new Field[cast.length - 1];
        System.arraycopy(cast, 1, others, 0, others.length);

        switch (configuration.dialect().family()) {
            case MARIADB:
            case MYSQL:
                return function("concat", SQLDataType.VARCHAR, cast);

            case SQLSERVER:
                return new Expression<String>(ADD, first, others);

            default:
                return new Expression<String>(CONCAT, first, others);
        }
    }
}
