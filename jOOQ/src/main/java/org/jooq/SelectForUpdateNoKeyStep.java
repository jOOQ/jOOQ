package org.jooq;

import static org.jooq.SQLDialect.POSTGRES_9_3;

public interface SelectForUpdateNoKeyStep<R extends Record> extends SelectOptionStep<R> {

    /**
     * Add a <code>NO KEY</code> clause to the <code>FOR UPDATE</code> clause at
     * the end of the query.
     *
     * @see SelectQuery#setForUpdateSkipLocked() see LockProvider for more
     *      details
     */
    @Support({ POSTGRES_9_3 })
    SelectForUpdateWaitStep<R> noKey();

}
