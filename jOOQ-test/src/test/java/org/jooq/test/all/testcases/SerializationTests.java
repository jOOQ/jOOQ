/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq.test.all.testcases;

import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.Date;

import org.jooq.DSLContext;
import org.jooq.ExecuteContext;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record6;
import org.jooq.Result;
import org.jooq.Select;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.exception.DetachedException;
import org.jooq.impl.DefaultConnectionProvider;
import org.jooq.impl.DefaultExecuteListener;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

/**
 * @author Lukas Eder
 */
public class SerializationTests<
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
    CS   extends UpdatableRecord<CS>,
    I    extends TableRecord<I>,
    IPK  extends UpdatableRecord<IPK>,
    T725 extends UpdatableRecord<T725>,
    T639 extends UpdatableRecord<T639>,
    T785 extends TableRecord<T785>,
    CASE extends UpdatableRecord<CASE>>
extends BaseTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, CS, I, IPK, T725, T639, T785, CASE> {

    public SerializationTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, CS, I, IPK, T725, T639, T785, CASE> delegate) {
        super(delegate);
    }

    public void testSerialisation() throws Exception {
        jOOQAbstractTest.reset = false;

        Select<A> q = create().selectFrom(TAuthor()).orderBy(TAuthor_LAST_NAME());

        // Serialising the unexecuted query
        // ---------------------------------------------------------------------
        q = runSerialisation(q);

        try {
            q.execute();
            fail();
        } catch (DetachedException expected) {}

        // Serialising the executed query
        // ---------------------------------------------------------------------
        create().attach(q);
        assertEquals(2, q.execute());
        assertEquals("Coelho", q.getResult().getValue(0, TAuthor_LAST_NAME()));
        assertEquals("Orwell", q.getResult().getValue(1, TAuthor_LAST_NAME()));

        q = runSerialisation(q);
        assertEquals("Coelho", q.getResult().getValue(0, TAuthor_LAST_NAME()));
        assertEquals("Orwell", q.getResult().getValue(1, TAuthor_LAST_NAME()));

        Result<A> result = q.getResult();
        result = runSerialisation(result);
        assertEquals("Coelho", result.getValue(0, TAuthor_LAST_NAME()));
        assertEquals("Orwell", result.getValue(1, TAuthor_LAST_NAME()));

        try {
            result.get(1).setValue(TAuthor_FIRST_NAME(), "Georgie");
            result.get(1).store();
            fail();
        } catch (DetachedException expected) {}

        create().attach(result);
        assertEquals(1, result.get(1).store());
        assertEquals("Georgie", create()
                .fetchOne(TAuthor(), TAuthor_LAST_NAME().equal("Orwell"))
                .getValue(TAuthor_FIRST_NAME()));

        // [#1191] Check execution capabilities with new features in ExecuteListener
        ConnectionProviderListener.c = create().configuration().connectionProvider().acquire();
        try {
            DSLContext create = create(new ConnectionProviderListener());
            q = create
                    .selectFrom(TAuthor())
                    .orderBy(TAuthor_LAST_NAME());
            q = runSerialisation(q);
            q.execute();

            result = q.getResult();
            result = runSerialisation(result);
            assertEquals("Coelho", result.getValue(0, TAuthor_LAST_NAME()));
            assertEquals("Orwell", result.getValue(1, TAuthor_LAST_NAME()));

            result.get(1).setValue(TAuthor_FIRST_NAME(), "Gee-Gee");
            result.get(1).store();
        }
        finally {
            create().configuration().connectionProvider().release(ConnectionProviderListener.c);
            ConnectionProviderListener.c = null;
        }

        // [#1071] Check sequences
        if (cSequences() == null) {
            log.info("SKIPPING", "sequences test");
        }
        else {
            Select<?> s;

            s = create().select(SAuthorID().nextval(), SAuthorID().currval());
            s = runSerialisation(s);
        }
    }

    public static class ConnectionProviderListener extends DefaultExecuteListener {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 7399239846062763212L;

        static Connection c;

        @Override
        public void start(ExecuteContext ctx) {
            ctx.connectionProvider(new DefaultConnectionProvider(c));
        }
    }

    @SuppressWarnings("unchecked")
    private <Z> Z runSerialisation(Z value) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream o = new ObjectOutputStream(out);
        o.writeObject(value);
        o.flush();

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        ObjectInputStream i = new ObjectInputStream(in);
        return (Z) i.readObject();
    }
}
