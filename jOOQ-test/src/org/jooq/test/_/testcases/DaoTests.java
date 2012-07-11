/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
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

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.jooq.tools.reflect.Reflect.on;

import java.util.List;

import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

import org.junit.Test;

public class DaoTests<
    A    extends UpdatableRecord<A>,
    AP,
    B    extends UpdatableRecord<B>,
    S    extends UpdatableRecord<S>,
    B2S  extends UpdatableRecord<B2S>,
    BS   extends UpdatableRecord<BS>,
    L    extends TableRecord<L>,
    X    extends TableRecord<X>,
    DATE extends UpdatableRecord<DATE>,
    BOOL extends UpdatableRecord<BOOL>,
    D    extends UpdatableRecord<D>,
    T    extends UpdatableRecord<T>,
    U    extends TableRecord<U>,
    I    extends TableRecord<I>,
    IPK  extends UpdatableRecord<IPK>,
    T658 extends TableRecord<T658>,
    T725 extends UpdatableRecord<T725>,
    T639 extends UpdatableRecord<T639>,
    T785 extends TableRecord<T785>>
extends BaseTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, I, IPK, T658, T725, T639, T785> {

    public DaoTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, I, IPK, T658, T725, T639, T785> delegate) {
        super(delegate);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDAOMethods() throws Exception {
        if (TAuthorDao() == null) {
            log.info("SKIPPING", "DAO tests");
            return;
        }

        jOOQAbstractTest.reset = false;

        Class<AP> type = TAuthorDao().getType();

        // Selection
        // ---------
        assertEquals(2, TAuthorDao().count());
        assertEquals(2, TAuthorDao().findAll().size());

        AP id1 = TAuthorDao().findById(1);
        assertEquals(1, on(id1).get("id"));
        assertEquals("George", on(id1).get("firstName"));
        assertEquals("Orwell", on(id1).get("lastName"));
        assertTrue(TAuthorDao().exists(on(type).create().set("id", 1).<AP>get()));
        assertTrue(TAuthorDao().existsById(1));
        assertNull(TAuthorDao().findById(17));

        List<AP> authors1 = on(TAuthorDao()).call("fetchByLastName", (Object) new String[] { "Orwell", "George"}).<List<AP>>get();
        assertEquals(1, authors1.size());
        assertEquals(1, on(authors1.get(0)).get("id"));
        assertEquals("George", on(authors1.get(0)).get("firstName"));
        assertEquals("Orwell", on(authors1.get(0)).get("lastName"));

        List<AP> authors2 = on(TAuthorDao()).call("fetchByLastName", (Object) new String[] { "Orwell", "Coelho"}).<List<AP>>get();
        assertEquals(2, authors2.size());
        assertEquals(1, on(authors2.get(0)).get("id"));
        assertEquals("George", on(authors2.get(0)).get("firstName"));
        assertEquals("Orwell", on(authors2.get(0)).get("lastName"));
        assertEquals(2, on(authors2.get(1)).get("id"));
        assertEquals("Paulo", on(authors2.get(1)).get("firstName"));
        assertEquals("Coelho", on(authors2.get(1)).get("lastName"));

        // Single insertion
        // ----------------
        AP author =
        on(type).create().set("id", 3)
                         .set("lastName", "Hesse").<AP>get();
        TAuthorDao().insert(author);
        assertEquals(3, TAuthorDao().count());
        AP id3 = TAuthorDao().findById(3);
        assertEquals(3, on(id3).get("id"));
        assertEquals(null, on(id3).get("firstName"));
        assertEquals("Hesse", on(id3).get("lastName"));

        author = on(author).set("firstName", "Hermann").<AP>get();
        TAuthorDao().update(author);
        id3 = TAuthorDao().findById(3);
        assertEquals(3, TAuthorDao().count());
        assertEquals(3, on(id3).get("id"));
        assertEquals("Hermann", on(id3).get("firstName"));
        assertEquals("Hesse", on(id3).get("lastName"));

        TAuthorDao().delete(author);
        assertEquals(2, TAuthorDao().count());

        // Batch insertion
        // ---------------
        List<AP> authors = asList(
            on(type).create().set("id", 4)
                             .set("lastName", "Koontz").<AP>get(),
            on(type).create().set("id", 5)
                             .set("lastName", "Hitchcock").<AP>get()
        );
        TAuthorDao().insert(authors);
        AP id4 = TAuthorDao().findById(4);
        AP id5 = TAuthorDao().findById(5);

        assertEquals(4, TAuthorDao().count());
        assertEquals(4, on(id4).get("id"));
        assertEquals(null, on(id4).get("firstName"));
        assertEquals("Koontz", on(id4).get("lastName"));
        assertEquals(5, on(id5).get("id"));
        assertEquals(null, on(id5).get("firstName"));
        assertEquals("Hitchcock", on(id5).get("lastName"));

        id4 = on(id4).set("firstName", "Dean").<AP>get();
        id5 = on(id5).set("firstName", "Alfred").<AP>get();
        TAuthorDao().update(id4, id5);

        id4 = TAuthorDao().findById(4);
        id5 = TAuthorDao().findById(5);

        assertEquals(4, TAuthorDao().count());
        assertEquals(4, on(id4).get("id"));
        assertEquals("Dean", on(id4).get("firstName"));
        assertEquals("Koontz", on(id4).get("lastName"));
        assertEquals(5, on(id5).get("id"));
        assertEquals("Alfred", on(id5).get("firstName"));
        assertEquals("Hitchcock", on(id5).get("lastName"));
    }
}
