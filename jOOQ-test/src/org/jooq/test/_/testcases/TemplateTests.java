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

import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.queryPart;
import static org.jooq.impl.DSL.val;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.StringWriter;
import java.net.URL;
import java.sql.Date;

import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record6;
import org.jooq.TableRecord;
import org.jooq.Template;
import org.jooq.UpdatableRecord;
import org.jooq.impl.DSL;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.junit.Test;

public class TemplateTests<
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

    public TemplateTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785> delegate) {
        super(delegate);
    }

    @Test
    public void testTextTemplates() throws Exception {
        class TextTemplate implements Template {

            private final String sql;

            public TextTemplate(String sql) {
                this.sql = sql;
            }

            @Override
            public QueryPart transform(Object... input) {
                return queryPart(sql, input);
            }
        }

        Record r1 =
        create().resultQuery(new TextTemplate("SELECT ? as x FROM t_author a WHERE a.id IN (?, ?) ORDER BY a.id"), 1, 2, 3)
                .fetchOne();
        assertTextTemplateRecord(r1);

        Record r2 =
        create().resultQuery(new TextTemplate("SELECT {0} as x FROM t_author a WHERE a.id IN ({1}, {2}) ORDER BY a.id"), val(1), inline(2), inline(3))
                .fetchOne();
        assertTextTemplateRecord(r2);
    }

    private void assertTextTemplateRecord(Record record) {
        assertEquals(1, record.fields().length);
        assertEquals("x", record.field(0).getName().toLowerCase());
        assertEquals(1, (int) record.getValue(0, int.class));
    }

    @Test
    public void testVelocityTemplates() throws Exception {
        class VelocityTemplate implements Template {

            private final String file;

            public VelocityTemplate(String file) {
                this.file = file;
            }

            @Override
            public QueryPart transform(Object... input) {
                URL url = this.getClass().getResource("/org/jooq/test/_/templates/");

                VelocityEngine ve = new VelocityEngine();
                ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "file");
                ve.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, new File(url.getFile()).getAbsolutePath());
                ve.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_CACHE, "true");
                ve.init();

                VelocityContext context = new VelocityContext();
                context.put("p", input);

                StringWriter writer = new StringWriter();
                ve.getTemplate(file, "UTF-8").merge(context, writer);
                return DSL.queryPart(writer.toString(), input);
            }

        }

        create().resultQuery(new VelocityTemplate("authors-and-books.vm"), 1, 2, 3)
                .fetch();
    }
}
