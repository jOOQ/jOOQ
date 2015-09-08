/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package org.jooq.test;

import static org.jooq.test.data.BoolTable.BOOL_TABLE;
import static org.junit.Assert.assertNull;

import java.sql.SQLException;

import org.jooq.Converter;
import org.jooq.Converters;
import org.jooq.Result;
import org.jooq.impl.AbstractConverter;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConverterProvider;
import org.jooq.test.data.BoolRecord;
import org.jooq.test.data.converter.Bool;
import org.jooq.tools.jdbc.MockConnection;
import org.jooq.tools.jdbc.MockDataProvider;
import org.jooq.tools.jdbc.MockExecuteContext;
import org.jooq.tools.jdbc.MockResult;

import org.junit.Test;

/**
 * Unit tests for data type conversion
 *
 * @author Lukas Eder
 */
public class ConverterTest extends AbstractTest {

    @Test
    public void testConverterInMockResult() {
        Result<BoolRecord> result =
        DSL.using(new MockConnection(new MockDataProvider() {

            @Override
            public MockResult[] execute(MockExecuteContext ctx) throws SQLException {
                Result<BoolRecord> r = create.newResult(BOOL_TABLE);

                r.add(create.newRecord(BOOL_TABLE, new Object[] { Bool.TRUE }));
                r.add(create.newRecord(BOOL_TABLE, new Object[] { Bool.FALSE }));
                r.add(create.newRecord(BOOL_TABLE, new Object[] { Bool.NULL }));

                return new MockResult[] { new MockResult(r.size(), r) };
            }
        })).selectFrom(BOOL_TABLE).fetch();

        assertEquals(3, result.size());
        assertEquals(1, result.fields().length);
        assertEquals(Bool.TRUE, result.getValue(0, 0));
        assertEquals(Bool.FALSE, result.getValue(1, 0));
        assertEquals(Bool.NULL, result.getValue(2, 0));
    }

    @Test
    public void testConverterInTableField() {
        assertEquals(Bool.NULL, BOOL_TABLE.BOOL.getDataType().convert((Boolean) null));
        assertEquals(Bool.TRUE, BOOL_TABLE.BOOL.getDataType().convert(true));
        assertEquals(Bool.TRUE, BOOL_TABLE.BOOL.getDataType().convert(1));
        assertEquals(Bool.TRUE, BOOL_TABLE.BOOL.getDataType().convert("true"));
        assertEquals(Bool.FALSE, BOOL_TABLE.BOOL.getDataType().convert(false));
        assertEquals(Bool.FALSE, BOOL_TABLE.BOOL.getDataType().convert(0));
        assertEquals(Bool.FALSE, BOOL_TABLE.BOOL.getDataType().convert("false"));
    }

    @SuppressWarnings("serial")
    @Test
    public void testChainedConverters() {
        Converter<Integer, Integer> c1 = Converters.of();

        assertEquals(1, (int) c1.from(1));
        assertEquals(1, (int) c1.to(1));
        assertEquals(1, (int) c1.from(c1.to(1)));

        Converter<Integer, Integer> add = new Converter<Integer, Integer>() {
            @Override
            public Integer from(Integer t) {
                return t + 1;
            }

            @Override
            public Integer to(Integer u) {
                return u - 1;
            }

            @Override
            public Class<Integer> fromType() {
                return Integer.class;
            }

            @Override
            public Class<Integer> toType() {
                return Integer.class;
            }
        };

        Converter<Integer, Integer> c2 = Converters.of(add);
        Converter<Integer, Integer> c3 = Converters.of(add, add);
        Converter<Integer, Integer> c4 = Converters.of(add, add, add);
        Converter<Integer, Integer> c5 = Converters.of(add, add, add, add);

        assertEquals(2, (int) c2.from(1));
        assertEquals(3, (int) c3.from(1));
        assertEquals(4, (int) c4.from(1));
        assertEquals(5, (int) c5.from(1));

        assertEquals(1, (int) c2.to(2));
        assertEquals(1, (int) c3.to(3));
        assertEquals(1, (int) c4.to(4));
        assertEquals(1, (int) c5.to(5));

        assertEquals(1, (int) c2.from(c2.to(1)));
        assertEquals(1, (int) c3.from(c3.to(1)));
        assertEquals(1, (int) c4.from(c4.to(1)));
        assertEquals(1, (int) c5.from(c5.to(1)));
    }

    @SuppressWarnings({ "serial", "deprecation" })
    @Test
    public void testConverterGraph() {
        Converter<A, B> ab = new AbstractConverter<A, B>(A.class, B.class) {
            @Override
            public B from(A v) {
                return new B(v.v);
            }

            @Override
            public A to(B v) {
                return new A(v.v);
            }
        };


        Converter<A, C> ac = new AbstractConverter<A, C>(A.class, C.class) {
            @Override
            public C from(A v) {
                return new C(v.v);
            }

            @Override
            public A to(C v) {
                return new A(v.v);
            }
        };

        Converter<C, D> cd = new AbstractConverter<C, D>(C.class, D.class) {
            @Override
            public D from(C v) {
                return new D(v.v);
            }

            @Override
            public C to(D v) {
                return new C(v.v);
            }
        };

        Converter<C, E> ce = new AbstractConverter<C, E>(C.class, E.class) {
            @Override
            public E from(C v) {
                return new E(v.v);
            }

            @Override
            public C to(E v) {
                return new C(v.v);
            }
        };

        DefaultConverterProvider provider = new DefaultConverterProvider();
        provider.add(ab);
        provider.add(ac);
        provider.add(cd);
        provider.add(ce);

        // F is not part of the graph
        assertNull(provider.provide(A.class, F.class));
        assertNull(provider.provide(B.class, F.class));
        assertNull(provider.provide(C.class, F.class));
        assertNull(provider.provide(D.class, F.class));
        assertNull(provider.provide(E.class, F.class));
        assertNull(provider.provide(F.class, A.class));
        assertNull(provider.provide(F.class, A.class));
        assertNull(provider.provide(F.class, A.class));
        assertNull(provider.provide(F.class, A.class));
        assertNull(provider.provide(F.class, A.class));

        // Identity conversion
        assertEquals("a", provider.provide(A.class, A.class).from(new A("")).v);
        assertEquals("a", provider.provide(A.class, A.class).to(new A("")).v);
        assertEquals("b", provider.provide(B.class, B.class).from(new B("")).v);
        assertEquals("b", provider.provide(B.class, B.class).to(new B("")).v);
        assertEquals("c", provider.provide(C.class, C.class).from(new C("")).v);
        assertEquals("c", provider.provide(C.class, C.class).to(new C("")).v);
        assertEquals("d", provider.provide(D.class, D.class).from(new D("")).v);
        assertEquals("d", provider.provide(D.class, D.class).to(new D("")).v);
        assertEquals("e", provider.provide(E.class, E.class).from(new E("")).v);
        assertEquals("e", provider.provide(E.class, E.class).to(new E("")).v);
        assertEquals("f", provider.provide(F.class, F.class).from(new F("")).v);
        assertEquals("f", provider.provide(F.class, F.class).to(new F("")).v);

        // Rest of the graph
        assertEquals("ab", provider.provide(A.class, B.class).from(new A("")).v);
        assertEquals("ba", provider.provide(A.class, B.class).to(new B("")).v);
        assertEquals("ac", provider.provide(A.class, C.class).from(new A("")).v);
        assertEquals("ca", provider.provide(A.class, C.class).to(new C("")).v);
        assertEquals("acd", provider.provide(A.class, D.class).from(new A("")).v);
        assertEquals("dca", provider.provide(A.class, D.class).to(new D("")).v);
        assertEquals("ace", provider.provide(A.class, E.class).from(new A("")).v);
        assertEquals("eca", provider.provide(A.class, E.class).to(new E("")).v);

        assertEquals("ba", provider.provide(B.class, A.class).from(new B("")).v);
        assertEquals("ab", provider.provide(B.class, A.class).to(new A("")).v);
        assertEquals("bac", provider.provide(B.class, C.class).from(new B("")).v);
        assertEquals("cab", provider.provide(B.class, C.class).to(new C("")).v);
        assertEquals("bacd", provider.provide(B.class, D.class).from(new B("")).v);
        assertEquals("dcab", provider.provide(B.class, D.class).to(new D("")).v);
        assertEquals("bace", provider.provide(B.class, E.class).from(new B("")).v);
        assertEquals("ecab", provider.provide(B.class, E.class).to(new E("")).v);

        assertEquals("ca", provider.provide(C.class, A.class).from(new C("")).v);
        assertEquals("ac", provider.provide(C.class, A.class).to(new A("")).v);
        assertEquals("cab", provider.provide(C.class, B.class).from(new C("")).v);
        assertEquals("bac", provider.provide(C.class, B.class).to(new B("")).v);
        assertEquals("cd", provider.provide(C.class, D.class).from(new C("")).v);
        assertEquals("dc", provider.provide(C.class, D.class).to(new D("")).v);
        assertEquals("ce", provider.provide(C.class, E.class).from(new C("")).v);
        assertEquals("ec", provider.provide(C.class, E.class).to(new E("")).v);

        assertEquals("dca", provider.provide(D.class, A.class).from(new D("")).v);
        assertEquals("acd", provider.provide(D.class, A.class).to(new A("")).v);
        assertEquals("dcab", provider.provide(D.class, B.class).from(new D("")).v);
        assertEquals("bacd", provider.provide(D.class, B.class).to(new B("")).v);
        assertEquals("dc", provider.provide(D.class, C.class).from(new D("")).v);
        assertEquals("cd", provider.provide(D.class, C.class).to(new C("")).v);
        assertEquals("dce", provider.provide(D.class, E.class).from(new D("")).v);
        assertEquals("ecd", provider.provide(D.class, E.class).to(new E("")).v);

        assertEquals("eca", provider.provide(E.class, A.class).from(new E("")).v);
        assertEquals("ace", provider.provide(E.class, A.class).to(new A("")).v);
        assertEquals("ecab", provider.provide(E.class, B.class).from(new E("")).v);
        assertEquals("bace", provider.provide(E.class, B.class).to(new B("")).v);
        assertEquals("ec", provider.provide(E.class, C.class).from(new E("")).v);
        assertEquals("ce", provider.provide(E.class, C.class).to(new C("")).v);
        assertEquals("ecd", provider.provide(E.class, D.class).from(new E("")).v);
        assertEquals("dce", provider.provide(E.class, D.class).to(new D("")).v);

        System.out.println(provider);
    }

    static class A {
        final String v;

        A(String v) {
            this.v = v + "a";
        }
    }

    static class B {
        final String v;

        B(String v) {
            this.v = v + "b";
        }
    }

    static class C {
        final String v;

        C(String v) {
            this.v = v + "c";
        }
    }

    static class D {
        final String v;

        D(String v) {
            this.v = v + "d";
        }
    }

    static class E {
        final String v;

        E(String v) {
            this.v = v + "e";
        }
    }

    static class F {
        final String v;

        F(String v) {
            this.v = v + "f";
        }
    }
}
