package org.jooq;

import static org.jooq.SQLDialect.POSTGRES_9_3;

public interface SelectForShareKeyStep<R extends Record> extends SelectOptionStep<R> {

    @Support({ POSTGRES_9_3 })
    SelectOptionStep<R> key();

}
