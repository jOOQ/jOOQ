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

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.function;

import java.sql.Date;

import org.jooq.Configuration;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
class DateDiff extends AbstractFunction<Integer> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -4813228000332771961L;

    private final Field<Date> date1;
    private final Field<Date> date2;

    DateDiff(Field<Date> date1, Field<Date> date2) {
        super("datediff", SQLDataType.INTEGER, date1, date2);

        this.date1 = date1;
        this.date2 = date2;
    }

    @Override
    final Field<Integer> getFunction0(Configuration configuration) {
        switch (configuration.dialect().family()) {
            case MARIADB:
            case MYSQL:
                return function("datediff", getDataType(), date1, date2);

            case DERBY:
                return field("{fn {timestampdiff}({sql_tsi_day}, {0}, {1}) }", getDataType(), date2, date1);

            case FIREBIRD:
                return field("{datediff}(day, {0}, {1})", getDataType(), date2, date1);

            case H2:
            case HSQLDB:
                return field("{datediff}('day', {0}, {1})", getDataType(), date2, date1);

            case SQLITE:
                return field("({strftime}('%s', {0}) - {strftime}('%s', {1})) / 86400", getDataType(), date1, date2);

            /* [com] */
            case ORACLE:
            /* [/com] */
            case CUBRID:
            case POSTGRES:
                return field("{0} - {1}", getDataType(), date1, date2);

            /* [com] */
            case ASE:
            case SQLSERVER:
            case SYBASE:
                return field("{datediff}(day, {0}, {1})", getDataType(), date2, date1);

            case DB2:
                return function("days", getDataType(), date1).sub(
                       function("days", getDataType(), date2));

            // Fall through to default
            case INGRES:
            /* [/com] */
        }

        // Default implementation for equals() and hashCode()
        return date1.sub(date2).cast(Integer.class);
    }
}
