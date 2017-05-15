package org.jooq.impl;

import static org.jooq.impl.DSL.function;

import java.time.LocalDate;

import org.jooq.Configuration;
import org.jooq.Field;

final class LocalDateDiff extends AbstractFunction<Integer> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 6010434059957641551L;

    private final Field<LocalDate> date1;
    private final Field<LocalDate> date2;

    LocalDateDiff(Field<LocalDate> date1, Field<LocalDate> date2) {
        super("localdatediff", SQLDataType.INTEGER, date1, date2);

        this.date1 = date1;
        this.date2 = date2;
    }

    @SuppressWarnings("incomplete-switch")
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
