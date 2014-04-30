/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
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
package org.jooq.test._.testcases;

import static java.util.Arrays.asList;
import static org.jooq.tools.reflect.Reflect.on;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record6;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;
import org.jooq.tools.reflect.ReflectException;

import org.junit.Test;

public class DaoTests<
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
    T785 extends TableRecord<T785>,
    CASE extends UpdatableRecord<CASE>>
extends BaseTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785, CASE> {

    public DaoTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785, CASE> delegate) {
        super(delegate);
    }

    @SuppressWarnings("unchecked")

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

        // [#1768] Unknown records should return null
        assertNull(TAuthorDao().findById(3));
        assertNull(TAuthorDao().fetchOne(TAuthor_ID(), 3));

        AP id1 = TAuthorDao().findById(1);
        assertEquals(1, on(id1).get("id"));
        assertEquals("George", on(id1).get("firstName"));
        assertEquals("Orwell", on(id1).get("lastName"));
        assertTrue(TAuthorDao().exists(id1));
        assertTrue(TAuthorDao().existsById(1));
        assertNull(TAuthorDao().findById(17));

        List<AP> authors1 = on(TAuthorDao()).call("fetchByLastName", (Object) new String[] { "Orwell", "George"}).<List<AP>>get();
        assertEquals(1, authors1.size());
        assertEquals(1, on(authors1.get(0)).get("id"));
        assertEquals("George", on(authors1.get(0)).get("firstName"));
        assertEquals("Orwell", on(authors1.get(0)).get("lastName"));

        List<AP> authors2 = new ArrayList<AP>(on(TAuthorDao()).call("fetchByLastName", (Object) new String[] { "Orwell", "Coelho"}).<List<AP>>get());
        assertEquals(2, authors2.size());

        // Fetched records are not ordered deterministically
        Collections.sort(authors2, new Comparator<AP>() {
            @Override
            public int compare(AP o1, AP o2) {
                return on(o1).<Integer>get("id").compareTo(on(o2).<Integer>get("id"));
            }});
        assertEquals(1, on(authors2.get(0)).get("id"));
        assertEquals("George", on(authors2.get(0)).get("firstName"));
        assertEquals("Orwell", on(authors2.get(0)).get("lastName"));
        assertEquals(2, on(authors2.get(1)).get("id"));
        assertEquals("Paulo", on(authors2.get(1)).get("firstName"));
        assertEquals("Coelho", on(authors2.get(1)).get("lastName"));

        // Single insertion
        // ----------------
        AP author = null;

        // Mutable POJO with no-args constructor
        try {
            author = on(type).create().set("id", 3)
                             .set("lastName", "Hesse").<AP>get();
        }

        // [#1339] Immutable POJO
        catch (ReflectException e) {
            author = (AP) type.getConstructors()[0].newInstance(3, null, "Hesse", null, null, null);
        }

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
        List<AP> authors = null;

        // Mutable POJO with no-args constructor
        try {
            authors = asList(
                on(type).create().set("id", 4)
                                 .set("lastName", "Koontz").<AP>get(),
                on(type).create().set("id", 5)
                                 .set("lastName", "Hitchcock").<AP>get()
            );
        }

        // [#1339] Immutable POJO
        catch (ReflectException e) {
            authors = asList(
                (AP) type.getConstructors()[0].newInstance(4, null, "Koontz", null, null, null),
                (AP) type.getConstructors()[0].newInstance(5, null, "Hitchcock", null, null, null)
            );
        }

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
