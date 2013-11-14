package org.jooq.test._.testcases;

import org.jooq.*;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;
import org.junit.Test;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Arrays;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.jooq.impl.DSL.count;

/**
 *
 */
public abstract class AbstractLoaderTests<
        A extends UpdatableRecord<A> & Record6<Integer, String, String, Date, Integer, ?>,
        AP,
        B extends UpdatableRecord<B>,
        S extends UpdatableRecord<S> & Record1<String>,
        B2S extends UpdatableRecord<B2S> & Record3<String, Integer, Integer>,
        BS extends UpdatableRecord<BS>,
        L extends TableRecord<L> & Record2<String, String>,
        X extends TableRecord<X>,
        DATE extends UpdatableRecord<DATE>,
        BOOL extends UpdatableRecord<BOOL>,
        D extends UpdatableRecord<D>,
        T extends UpdatableRecord<T>,
        U extends TableRecord<U>,
        UU extends UpdatableRecord<UU>,
        I extends TableRecord<I>,
        IPK extends UpdatableRecord<IPK>,
        T725 extends UpdatableRecord<T725>,
        T639 extends UpdatableRecord<T639>,
        T785 extends TableRecord<T785>> extends BaseTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785> {
    public AbstractLoaderTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785> delegate) {
        super(delegate);
    }

    @Test
    public void testLoader() throws Exception {
        jOOQAbstractTest.reset = false;
        jOOQAbstractTest.connection.setAutoCommit(false);

        Field<Integer> count = count();

        // Empty CSV file
        // --------------
        Loader<A> loader = createLoader1();

        assertEquals(0, loader.processed());
        assertEquals(0, loader.errors().size());
        assertEquals(0, loader.stored());
        assertEquals(0, loader.ignored());
        assertEquals(2, (int) create().select(count).from(TAuthor()).fetchOne(count));

        // Constraint violations (LAST_NAME is NOT NULL)
        // Loading is aborted
        // ---------------------------------------------
        loader =
                createLoader2();

        // [#812] Reset stale connection. Seems to be necessary in Postgres
        resetLoaderConnection();

        assertEquals(1, loader.processed());
        assertEquals(1, loader.errors().size());
        assertNotNull(loader.errors().get(0));
        assertEquals(0, loader.stored());
        assertEquals(1, loader.ignored());
        assertEquals(2, (int) create().select(count).from(TAuthor()).fetchOne(count));

        // Constraint violations (LAST_NAME is NOT NULL)
        // Errors are ignored
        // ---------------------------------------------
        loader = createLoader3();

        // [#812] Reset stale connection. Seems to be necessary in Postgres
        resetLoaderConnection();

        assertEquals(2, loader.processed());
        assertEquals(2, loader.errors().size());
        assertNotNull(loader.errors().get(0));
        assertNotNull(loader.errors().get(1));
        assertEquals(0, loader.stored());
        assertEquals(2, loader.ignored());
        assertEquals(2, (int) create().select(count).from(TAuthor()).fetchOne(count));

        // Constraint violations (Duplicate records)
        // Loading is aborted
        // -----------------------------------------
        loader =
                createLoader4();

        // [#812] Reset stale connection. Seems to be necessary in Postgres
        resetLoaderConnection();

        assertEquals(1, loader.processed());
        assertEquals(1, loader.errors().size());
        assertNotNull(loader.errors().get(0));
        assertEquals(0, loader.stored());
        assertEquals(1, loader.ignored());
        assertEquals(2, (int) create().select(count).from(TAuthor()).fetchOne(count));

        // Constraint violations (Duplicate records)
        // Errors are ignored
        // -----------------------------------------
        loader = createLoader5();

        assertEquals(2, loader.processed());
        assertEquals(0, loader.errors().size());
        assertEquals(2, loader.ignored());
        assertEquals(2, (int) create().select(count).from(TAuthor()).fetchOne(count));

        // Two records with different NULL representations for FIRST_NAME
        // --------------------------------------------------------------
        loader =
                createLoader6();

        assertEquals(2, loader.processed());
        assertEquals(2, loader.stored());
        assertEquals(0, loader.ignored());
        assertEquals(0, loader.errors().size());


        boolean oracle = false;
        /* [pro] xx
        xx xxxxxxxxxxxxxxxxxxx xx xxxxxxx
            xxxxxx x xxxxx
        xx [/pro] */

        assertEquals(2, (int) create().select(count)
                .from(TAuthor())
                .where(TAuthor_ID().in(3, 4))
                .and(TAuthor_LAST_NAME().in("Hesse", "Frisch"))
                .and(oracle ?
                        TAuthor_FIRST_NAME().isNull() :
                        TAuthor_FIRST_NAME().equal(""))
                .fetchOne(count));

        assertEquals(2, create().delete(TAuthor()).where(TAuthor_ID().in(3, 4)).execute());

        // Two records but don't load one column, and specify a value for NULL
        // -------------------------------------------------------------------
        loader = createLoader7();

        assertEquals(2, loader.processed());
        assertEquals(2, loader.stored());
        assertEquals(0, loader.ignored());
        assertEquals(0, loader.errors().size());

        Result<A> result =
                create().selectFrom(TAuthor())
                        .where(TAuthor_ID().in(5, 6))
                        .and(TAuthor_LAST_NAME().in("Hesse", "Frisch"))
                        .orderBy(TAuthor_ID())
                        .fetch();

        assertEquals(2, result.size());
        assertEquals(5, (int) result.getValue(0, TAuthor_ID()));
        assertEquals(6, (int) result.getValue(1, TAuthor_ID()));
        assertEquals("Hesse", result.getValue(0, TAuthor_LAST_NAME()));
        assertEquals("Frisch", result.getValue(1, TAuthor_LAST_NAME()));
        assertNull(result.getValue(0, TAuthor_FIRST_NAME()));
        assertEquals(oracle ? null : "", result.getValue(1, TAuthor_FIRST_NAME()));

        assertEquals(2, create().delete(TAuthor()).where(TAuthor_ID().in(5, 6)).execute());

        // Update duplicate records
        // ------------------------
        switch (dialect()) {
            /* [pro] xx
            xxxx xxxx
            xxxx xxxxxxx
            xx [/pro] */
            case DERBY:
            case H2:
            case POSTGRES:
            case SQLITE:
                // TODO [#558] Simulate this
                log.info("SKIPPING", "Duplicate record insertion");
                break;

            default: {
                loader = createLoader8();

                assertEquals(2, loader.processed());
                assertEquals(2, loader.stored());
                assertEquals(0, loader.ignored());
                assertEquals(0, loader.errors().size());

                result =
                        create().selectFrom(TAuthor())
                                .where(TAuthor_LAST_NAME().in("Hesse", "Frisch"))
                                .orderBy(TAuthor_ID())
                                .fetch();

                assertEquals(2, result.size());
                assertEquals(1, (int) result.getValue(0, TAuthor_ID()));
                assertEquals(7, (int) result.getValue(1, TAuthor_ID()));
                assertEquals("Hesse", result.getValue(0, TAuthor_LAST_NAME()));
                assertEquals("Frisch", result.getValue(1, TAuthor_LAST_NAME()));
                assertEquals("George", result.getValue(0, TAuthor_FIRST_NAME()));
                assertEquals(null, result.getValue(1, TAuthor_FIRST_NAME()));

                assertEquals(1, create().delete(TAuthor()).where(TAuthor_ID().in(7)).execute());
            }
        }

        // [#812] Reset stale connection. Seems to be necessary in Postgres
        resetLoaderConnection();

        // Rollback on duplicate keys
        // --------------------------
        loader = createLoader9();

        assertEquals(2, loader.processed());
        assertEquals(0, loader.stored());
        assertEquals(1, loader.ignored());
        assertEquals(1, loader.errors().size());
        assertEquals(1, loader.errors().get(0).rowIndex());
        assertEquals(
                Arrays.asList("1", "Max", "Frisch"),
                Arrays.asList(loader.errors().get(0).row()));

        result =
                create().selectFrom(TAuthor())
                        .where(TAuthor_ID().in(8))
                        .orderBy(TAuthor_ID())
                        .fetch();

        assertEquals(0, result.size());

        // Commit and ignore duplicates
        // ----------------------------
        loader = createLoader10();

        assertEquals(3, loader.processed());
        assertEquals(1, loader.stored());
        assertEquals(2, loader.ignored());
        assertEquals(0, loader.errors().size());

        result =
                create().selectFrom(TAuthor())
                        .where(TAuthor_ID().in(1, 2, 8))
                        .orderBy(TAuthor_ID())
                        .fetch();

        assertEquals(3, result.size());
        assertEquals(8, (int) result.getValue(2, TAuthor_ID()));
        assertNull(result.getValue(2, TAuthor_FIRST_NAME()));
        assertEquals("Hesse", result.getValue(2, TAuthor_LAST_NAME()));
        assertEquals("Coelho", result.getValue(1, TAuthor_LAST_NAME()));
    }

    protected abstract Loader<A> createLoader9() throws java.io.IOException;

    protected abstract Loader<A> createLoader8() throws java.io.IOException;

    protected abstract Loader<A> createLoader7() throws java.io.IOException;

    protected abstract Loader<A> createLoader6() throws java.io.IOException;

    protected abstract Loader<A> createLoader5() throws java.io.IOException;

    protected abstract Loader<A> createLoader4() throws java.io.IOException;

    protected abstract Loader<A> createLoader3() throws java.io.IOException;

    protected abstract Loader<A> createLoader10() throws java.io.IOException;

    protected abstract Loader<A> createLoader2() throws java.io.IOException;

    protected abstract Loader<A> createLoader1() throws java.io.IOException;

    private void resetLoaderConnection() throws SQLException {
        jOOQAbstractTest.connection.rollback();
        jOOQAbstractTest.connection.close();
        jOOQAbstractTest.connection = null;
        jOOQAbstractTest.connectionInitialised = false;
        jOOQAbstractTest.connection = delegate.getConnection();
        jOOQAbstractTest.connection.setAutoCommit(false);
    }
}
