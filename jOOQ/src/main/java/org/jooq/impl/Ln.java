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

import static org.jooq.impl.DSL.function;
import static org.jooq.impl.DSL.inline;

import java.math.BigDecimal;

import org.jooq.Configuration;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
class Ln extends AbstractFunction<BigDecimal> {

    /**
     * Generated UID
     */
    private static final long             serialVersionUID = -7273879239726265322L;

    private final Field<? extends Number> argument;
    private final Integer                 base;

    Ln(Field<? extends Number> argument) {
        this(argument, null);
    }

    Ln(Field<? extends Number> argument, Integer base) {
        super("ln", SQLDataType.NUMERIC, argument);

        this.argument = argument;
        this.base = base;
    }

    @Override
    final Field<BigDecimal> getFunction0(Configuration configuration) {
        if (base == null) {
            switch (configuration.dialect().family()) {
                case ASE:
                case H2:
                case SQLSERVER:
                    return function("log", SQLDataType.NUMERIC, argument);

                default:
                    return function("ln", SQLDataType.NUMERIC, argument);
            }
        }
        else {
            switch (configuration.dialect().family()) {
                case ASE:
                case DB2:
                case DERBY:
                case H2:
                case HSQLDB:
                case INGRES:
                case SQLSERVER:
                case SYBASE:
                    return DSL.ln(argument).div(DSL.ln(inline(base)));

                default:
                    return function("log", SQLDataType.NUMERIC, inline(base), argument);
            }
        }
    }
}
