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

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.function;

import org.jooq.Configuration;
import org.jooq.DatePart;
import org.jooq.Field;
import org.jooq.exception.SQLDialectNotSupportedException;

/**
 * @author Lukas Eder
 */
class Extract extends AbstractFunction<Integer> {

    private static final long serialVersionUID = 3748640920856031034L;

    private final Field<?>    field;
    private final DatePart    datePart;

    Extract(Field<?> field, DatePart datePart) {
        super("extract", SQLDataType.INTEGER, field);

        this.field = field;
        this.datePart = datePart;
    }

    @Override
    final Field<Integer> getFunction0(Configuration configuration) {
        switch (configuration.dialect().family()) {
            case SQLITE:
                switch (datePart) {
                    case YEAR:
                        return field("{strftime}('%Y', {0})", SQLDataType.INTEGER, field);
                    case MONTH:
                        return field("{strftime}('%m', {0})", SQLDataType.INTEGER, field);
                    case DAY:
                        return field("{strftime}('%d', {0})", SQLDataType.INTEGER, field);
                    case HOUR:
                        return field("{strftime}('%H', {0})", SQLDataType.INTEGER, field);
                    case MINUTE:
                        return field("{strftime}('%M', {0})", SQLDataType.INTEGER, field);
                    case SECOND:
                        return field("{strftime}('%S', {0})", SQLDataType.INTEGER, field);
                    default:
                        throw new SQLDialectNotSupportedException("DatePart not supported: " + datePart);
                }

            case DERBY:
            case DB2:
                switch (datePart) {
                    case YEAR:
                        return function("year", SQLDataType.INTEGER, field);
                    case MONTH:
                        return function("month", SQLDataType.INTEGER, field);
                    case DAY:
                        return function("day", SQLDataType.INTEGER, field);
                    case HOUR:
                        return function("hour", SQLDataType.INTEGER, field);
                    case MINUTE:
                        return function("minute", SQLDataType.INTEGER, field);
                    case SECOND:
                        return function("second", SQLDataType.INTEGER, field);
                    default:
                        throw new SQLDialectNotSupportedException("DatePart not supported: " + datePart);
                }

            case ORACLE:
                switch (datePart) {
                    case YEAR:
                        return field("{to_char}({0}, 'YYYY')", SQLDataType.INTEGER, field);
                    case MONTH:
                        return field("{to_char}({0}, 'MM')", SQLDataType.INTEGER, field);
                    case DAY:
                        return field("{to_char}({0}, 'DD')", SQLDataType.INTEGER, field);
                    case HOUR:
                        return field("{to_char}({0}, 'HH24')", SQLDataType.INTEGER, field);
                    case MINUTE:
                        return field("{to_char}({0}, 'MI')", SQLDataType.INTEGER, field);
                    case SECOND:
                        return field("{to_char}({0}, 'SS')", SQLDataType.INTEGER, field);
                    default:
                        throw new SQLDialectNotSupportedException("DatePart not supported: " + datePart);
                }

            case ASE:
            case SQLSERVER:
            case SYBASE:
                switch (datePart) {
                    case YEAR:
                        return field("{datepart}(yy, {0})", SQLDataType.INTEGER, field);
                    case MONTH:
                        return field("{datepart}(mm, {0})", SQLDataType.INTEGER, field);
                    case DAY:
                        return field("{datepart}(dd, {0})", SQLDataType.INTEGER, field);
                    case HOUR:
                        return field("{datepart}(hh, {0})", SQLDataType.INTEGER, field);
                    case MINUTE:
                        return field("{datepart}(mi, {0})", SQLDataType.INTEGER, field);
                    case SECOND:
                        return field("{datepart}(ss, {0})", SQLDataType.INTEGER, field);
                    default:
                        throw new SQLDialectNotSupportedException("DatePart not supported: " + datePart);
                }

            case INGRES:
            case MARIADB:
            case MYSQL:
            case POSTGRES:
            case HSQLDB:
            case H2:

            // A default implementation is necessary for hashCode() and toString()
            default:
                return field("{extract}({" + datePart.toSQL() + " from} {0})", SQLDataType.INTEGER, field);
        }
    }
}
