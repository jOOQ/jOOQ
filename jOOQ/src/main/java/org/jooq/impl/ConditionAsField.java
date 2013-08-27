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

import static org.jooq.impl.DSL.inline;

import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.QueryPart;

/**
 * @author Lukas Eder
 */
class ConditionAsField extends AbstractFunction<Boolean> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -5921673852489483721L;
    private final Condition   condition;

    ConditionAsField(Condition condition) {
        super(condition.toString(), SQLDataType.BOOLEAN);

        this.condition = condition;
    }

    @Override
    final QueryPart getFunction0(Configuration configuration) {
        switch (configuration.dialect().family()) {

            // Some databases don't accept predicates where column expressions
            // are expected.
            case CUBRID:
            case DB2:
            case FIREBIRD:
            case ORACLE:
            case SQLSERVER:
            case SYBASE:
                return DSL.decode().when(condition, inline(true)).otherwise(inline(false));

            // These databases can inline predicates in column expression contexts
            case DERBY:
            case H2:
            case HSQLDB:
            case MARIADB:
            case MYSQL:
            case POSTGRES:
            case SQLITE:

            // Unknown (to be evaluated):
            case ASE:
            case INGRES:
                return condition;
        }

        // The default, for new dialects
        return condition;
    }
}
