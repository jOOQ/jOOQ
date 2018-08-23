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
 *
 *
 *
 */

package org.jooq.impl;

// ...
import static org.jooq.impl.DSL.function;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.SQLDataType.INTEGER;

import java.sql.Date;
import java.sql.Timestamp;

import org.jooq.Configuration;
import org.jooq.DatePart;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
final class Extract extends AbstractFunction<Integer> {

    private static final long serialVersionUID = 3748640920856031034L;

    private final Field<?>    field;
    private final DatePart    datePart;

    Extract(Field<?> field, DatePart datePart) {
        super("extract", INTEGER, field);

        this.field = field;
        this.datePart = datePart;
    }

    @Override
    final Field<Integer> getFunction0(Configuration configuration) {
        switch (configuration.family()) {
            case SQLITE:
                switch (datePart) {
                    case YEAR:
                        return DSL.field("{strftime}('%Y', {0})", INTEGER, field);
                    case MONTH:
                        return DSL.field("{strftime}('%m', {0})", INTEGER, field);
                    case DAY:
                        return DSL.field("{strftime}('%d', {0})", INTEGER, field);
                    case HOUR:
                        return DSL.field("{strftime}('%H', {0})", INTEGER, field);
                    case MINUTE:
                        return DSL.field("{strftime}('%M', {0})", INTEGER, field);
                    case SECOND:
                        return DSL.field("{strftime}('%S', {0})", INTEGER, field);

                    // See: https://www.sqlite.org/lang_datefunc.html
                    case EPOCH:
                        return DSL.field("{strftime}('%s', {0})", INTEGER, field);
                    case ISO_DAY_OF_WEEK:
                        return dowSun0ToISO(DSL.field("{strftime}('%w', {0})", INTEGER, field));
                    case DAY_OF_WEEK:
                        return DSL.field("{strftime}('%w', {0})", INTEGER, field).add(one());
                    case DAY_OF_YEAR:
                        return DSL.field("{strftime}('%j', {0})", INTEGER, field);
                    default:
                        return getNativeFunction();
                }






















            case DERBY:
                switch (datePart) {
                    case YEAR:
                        return function("year", INTEGER, field);
                    case MONTH:
                        return function("month", INTEGER, field);
                    case DAY:
                        return function("day", INTEGER, field);
                    case HOUR:
                        return function("hour", INTEGER, field);
                    case MINUTE:
                        return function("minute", INTEGER, field);
                    case SECOND:
                        return function("second", INTEGER, field);
                }


















                return getNativeFunction();























































































            case MARIADB:
            case MYSQL:
                switch (datePart) {
                    case EPOCH:
                        return DSL.field("{unix_timestamp}({0})", INTEGER, field);
                    case QUARTER:
                        return DSL.field("{quarter}({0})", INTEGER, field);
                    case ISO_DAY_OF_WEEK:
                        return DSL.field("{weekday}({0})", INTEGER, field).add(one());
                    case DAY_OF_WEEK:
                        return DSL.field("{dayofweek}({0})", INTEGER, field);
                    case DAY_OF_YEAR:
                        return DSL.field("{dayofyear}({0})", INTEGER, field);
                    default:
                        return getNativeFunction();
                }



            case POSTGRES:
                switch (datePart) {
                    case QUARTER:
                        return DSL.field("{extract}({quarter from} {0})", INTEGER, field);
                    case DAY_OF_WEEK:
                        return DSL.field("({extract}({dow from} {0}) + 1)", INTEGER, field);
                    case ISO_DAY_OF_WEEK:
                        return DSL.field("{extract}({isodow from} {0})", INTEGER, field);
                    case DAY_OF_YEAR:
                        return DSL.field("{extract}({doy from} {0})", INTEGER, field);
                    default:
                        return getNativeFunction();
                }

            case HSQLDB:
                switch (datePart) {
                    case QUARTER:
                        return DSL.field("{quarter}({0})", INTEGER, field);
                    case ISO_DAY_OF_WEEK:
                        return dowSun1ToISO(DSL.field("{extract}({day_of_week from} {0})", INTEGER, field));
                    default:
                        return getNativeFunction();
                }
            case H2:
                switch (datePart) {
                    case QUARTER:
                        return DSL.field("{quarter}({0})", INTEGER, field);
                    default:
                        return getNativeFunction();
                }

            default:
                return getNativeFunction();
        }
    }

    private final static Field<Integer> dowISOToSun1(Field<Integer> dow) {
        return dow.mod(inline(7)).add(one());
    }

    private final static Field<Integer> dowSun1ToISO(Field<Integer> dow) {
        return dow.add(inline(5)).mod(inline(7)).add(one());
    }

    private final static Field<Integer> dowSun0ToISO(Field<Integer> dow) {
        return dow.add(inline(6)).mod(inline(7)).add(one());
    }

    private final Field<Integer> getNativeFunction() {
        switch (datePart) {
            case QUARTER:
                return DSL.month(field).add(inline(2)).div(inline(3));
            default:
                return DSL.field("{extract}({0} {from} {1})", INTEGER, datePart.toKeyword(), field);
        }
    }
}
