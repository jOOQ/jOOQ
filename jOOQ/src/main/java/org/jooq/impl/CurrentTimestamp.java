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

import java.sql.Timestamp;

import org.jooq.Configuration;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
class CurrentTimestamp extends AbstractFunction<Timestamp> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -7273879239726265322L;

    CurrentTimestamp() {
        super("current_timestamp", SQLDataType.TIMESTAMP);
    }

    @Override
    final Field<Timestamp> getFunction0(Configuration configuration) {
        switch (configuration.dialect().family()) {
            case ASE:
                return function("current_bigdatetime", SQLDataType.TIMESTAMP);

            case ORACLE:
                return field("sysdate", SQLDataType.TIMESTAMP);

            case DB2:
            case DERBY:
            case FIREBIRD:
            case HSQLDB:
            case INGRES:
            case POSTGRES:
            case SQLITE:
            case SQLSERVER:
                return field("current_timestamp", SQLDataType.TIMESTAMP);

            case SYBASE:
                return field("current timestamp", SQLDataType.TIMESTAMP);
        }

        return function("current_timestamp", SQLDataType.TIMESTAMP);
    }
}
