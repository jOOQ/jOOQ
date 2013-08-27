/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under LGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 * 
 * LGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
 */
package org.jooq.test._.testcases;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

import java.sql.Date;

import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record6;
import org.jooq.Result;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

import org.junit.Test;

public class ReferentialTests<
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

    public ReferentialTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785> delegate) {
        super(delegate);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFetchParentAndChildren() throws Exception {
        Result<A> authors = create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch();
        Result<B> books = create().selectFrom(TBook()).orderBy(TBook_ID()).fetch();

        A a1 = authors.get(0);
        A a2 = authors.get(1);
        B b1 = books.get(0);
        B b2 = books.get(1);
        B b3 = books.get(2);
        B b4 = books.get(3);

        // Fetching parents
        assertEquals(a1, b1.fetchParent(FK_T_BOOK_AUTHOR_ID()));
        assertEquals(a1, FK_T_BOOK_AUTHOR_ID().fetchParent(b1));
        assertSame(asList(a1), FK_T_BOOK_AUTHOR_ID().fetchParents(b1, b2));
        assertSame(asList(a1, a2), FK_T_BOOK_AUTHOR_ID().fetchParents(b1, b3));
        assertSame(asList(a1, a2), FK_T_BOOK_AUTHOR_ID().fetchParents(b1, b2, b3, b4));

        // Fetching children
        assertSame(asList(b1, b2), a1.fetchChildren(FK_T_BOOK_AUTHOR_ID()));
        assertSame(asList(b1, b2), FK_T_BOOK_AUTHOR_ID().fetchChildren(a1));
        assertSame(asList(b3, b4), a2.fetchChildren(FK_T_BOOK_AUTHOR_ID()));
        assertSame(asList(b3, b4), FK_T_BOOK_AUTHOR_ID().fetchChildren(a2));
        assertSame(asList(b1, b2, b3, b4), FK_T_BOOK_AUTHOR_ID().fetchChildren(a1, a2));

        // No co-authors available
        assertNull(b1.fetchParent(FK_T_BOOK_CO_AUTHOR_ID()));
        assertNull(a1.fetchChild(FK_T_BOOK_CO_AUTHOR_ID()));
    }
}
