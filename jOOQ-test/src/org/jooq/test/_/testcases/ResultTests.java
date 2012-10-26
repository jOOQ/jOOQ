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
import static junit.framework.Assert.assertTrue;

import java.util.Comparator;

import org.jooq.Result;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

import org.junit.Test;

public class ResultTests<
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
    T725 extends UpdatableRecord<T725>,
    T639 extends UpdatableRecord<T639>,
    T785 extends TableRecord<T785>>
extends BaseTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, I, IPK, T725, T639, T785> {

    public ResultTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, I, IPK, T725, T639, T785> delegate) {
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
