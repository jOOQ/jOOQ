/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jooq.test._.testcases;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.INGRES;
import static org.jooq.SQLDialect.SQLITE;

import java.sql.Date;
import java.util.Arrays;

import org.jooq.InsertResultStep;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record6;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

import org.junit.Test;

public class TruncateTests<
    A    extends UpdatableRecord<A> & Record6<Integer, String, String, Date, Integer, ?>,
    AP,
    B    extends UpdatableRecord<B>,
    S    extends UpdatableRecord<S> & Record1<String>,
    B2S  extends UpdatableRecord<B2S> & Record3<String, Integer, Integer>,
    BS   extends UpdatableRecord<BS>,
    L    extends TableRecord<L> & Record2<String, String>,
    X    extends TableRecord<X>,
    DATE extends UpdatableRecord<DATE>,
    BOOL extends UpdatableRecord<BOOL>,
    D    extends UpdatableRecord<D>,
    T    extends UpdatableRecord<T>,
    U    extends TableRecord<U>,
    UU   extends UpdatableRecord<UU>,
    I    extends TableRecord<I>,
    IPK  extends UpdatableRecord<IPK>,
    T725 extends UpdatableRecord<T725>,
    T639 extends UpdatableRecord<T639>,
    T785 extends TableRecord<T785>>
extends BaseTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785> {

    public TruncateTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785> delegate) {
        super(delegate);
    }

    @Test
    public void testTruncate() throws Exception {
        jOOQAbstractTest.reset = false;

        try {
            create().truncate(TAuthor()).execute();

            // The above should fail if foreign keys are supported
            if (!Arrays.asList(CUBRID, FIREBIRD, INGRES, SQLITE).contains(dialect())) {
                fail();
            }
        } catch (Exception expected) {
        }

        // This is being tested with an unreferenced table as some RDBMS don't
        // Allow this
        create().truncate(TDates()).execute();
        assertEquals(0, create().fetch(TDates()).size());
    }

    @Test
    public void testTruncateCascade() throws Exception {
        switch (dialect()) {
            case ASE:
            case CUBRID:
            case DB2:
            case DERBY:
            case FIREBIRD:
            case H2:
            case HSQLDB:
            case INGRES:
            case MYSQL:
            case ORACLE:
            case SQLITE:
            case SQLSERVER:
            case SYBASE:
                log.info("SKIPPING", "TRUNCATE CASCADE tests");
                return;
        }

        jOOQAbstractTest.reset = false;

        try {
            create().truncate(TAuthor())
                    .restrict()
                    .execute();
        } catch (Exception expected) {
        }

        // This is being tested with an unreferenced table as some RDBMS don't
        // Allow this
        create().truncate(TAuthor())
                .cascade()
                .execute();
        assertEquals(0, create().fetch(TAuthor()).size());
        assertEquals(0, create().fetch(TBook()).size());
    }

    @Test
    public void testTruncateRestartIdentity() throws Exception {
        switch (dialect()) {
            case ASE:
            case CUBRID:
            case DB2:
            case DERBY:
            case FIREBIRD:
            case H2:
            case INGRES:
            case MYSQL:
            case ORACLE:
            case SQLITE:
            case SQLSERVER:
            case SYBASE:
                log.info("SKIPPING", "RESTART IDENTITY tests");
                return;
        }

        jOOQAbstractTest.reset = false;

        InsertResultStep<I> insert =
        create().insertInto(TIdentity(), TIdentity_VAL())
                .values(1)
                .returning(TIdentity_ID());
        int id1 = insert.fetchOne().getValue(TIdentity_ID());


        create().truncate(TIdentity())
                .continueIdentity()
                .execute();
        int id2 = insert.fetchOne().getValue(TIdentity_ID());
        assertEquals(id1 + 1, id2);


        create().truncate(TIdentity())
                .restartIdentity()
                .execute();
        int id3 = insert.fetchOne().getValue(TIdentity_ID());
        assertEquals(1, id3);
    }
}
