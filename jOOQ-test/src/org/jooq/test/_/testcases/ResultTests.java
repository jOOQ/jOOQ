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
import static junit.framework.Assert.assertTrue;

import java.sql.Date;
import java.util.Comparator;

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

public class ResultTests<
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

    public ResultTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785> delegate) {
        super(delegate);
    }

    @Test
    public void testResultSort() throws Exception {
        Result<B> result = create().fetch(TBook());

        assertTrue(result == result.sortAsc(TBook_ID()));
        assertEquals(asList(1, 2, 3, 4), result.getValues(TBook_ID()));

        assertTrue(result == result.sortDesc(TBook_ID()));
        assertEquals(asList(4, 3, 2, 1), result.getValues(TBook_ID()));

        class C1 implements Comparator<Integer> {

            @Override
            public int compare(Integer o1, Integer o2) {

                // Put 1 at the end of everything
                if (o1 == 1 && o2 != 1) return 1;
                if (o2 == 1 && o1 != 1) return -1;

                return o1.compareTo(o2);
            }
        }

        assertTrue(result == result.sortAsc(TBook_ID(), new C1()));
        assertEquals(asList(2, 3, 4, 1), result.getValues(TBook_ID()));

        assertTrue(result == result.sortDesc(TBook_ID(), new C1()));
        assertEquals(asList(1, 4, 3, 2), result.getValues(TBook_ID()));

        class C2 implements Comparator<B> {

            private final C1 c1 = new C1();

            @Override
            public int compare(B book1, B book2) {
                return c1.compare(book1.getValue(TBook_ID()), book2.getValue(TBook_ID()));
            }
        }

        assertTrue(result == result.sortAsc(new C2()));
        assertEquals(asList(2, 3, 4, 1), result.getValues(TBook_ID()));

        assertTrue(result == result.sortDesc(new C2()));
        assertEquals(asList(1, 4, 3, 2), result.getValues(TBook_ID()));
    }
}
