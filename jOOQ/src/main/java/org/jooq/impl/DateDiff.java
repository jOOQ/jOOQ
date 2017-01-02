/*
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
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package org.jooq.impl;

import static org.jooq.impl.DSL.function;

import java.sql.Date;

import org.jooq.Configuration;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
final class DateDiff extends AbstractFunction<Integer> {

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
        switch (configuration.family()) {
            case MARIADB:
            case MYSQL:
                return function("datediff", getDataType(), date1, date2);

            case DERBY:
                return DSL.field("{fn {timestampdiff}({sql_tsi_day}, {0}, {1}) }", getDataType(), date2, date1);

            case FIREBIRD:
                return DSL.field("{datediff}(day, {0}, {1})", getDataType(), date2, date1);

            case H2:
            case HSQLDB:



                return DSL.field("{datediff}('day', {0}, {1})", getDataType(), date2, date1);

            case SQLITE:
                return DSL.field("({strftime}('%s', {0}) - {strftime}('%s', {1})) / 86400", getDataType(), date1, date2);




            case CUBRID:
            case POSTGRES:

                // [#4481] Parentheses are important in case this expression is
                //         placed in the context of other arithmetic
                return DSL.field("({0} - {1})", getDataType(), date1, date2);





















        }

        // Default implementation for equals() and hashCode()
        return date1.sub(date2).cast(Integer.class);
    }
}
