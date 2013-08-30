/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is triple-licensed under ASL 2.0, AGPL 3.0, and jOOQ EULA
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   ASL 2.0 or jOOQ EULA.
 * - If you're using this work with at least one commercial database, you may
 *   choose AGPL 3.0 or jOOQ EULA.
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
 * AGPL 3.0
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 *
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details: http://www.jooq.org/eula
 */
package org.jooq.test._.testcases;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.jooq.SQLDialect.SQLITE;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.jooq.ExecuteContext;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record6;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DefaultExecuteListener;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

import org.junit.Test;

public class ResultSetTests<
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

    public ResultSetTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785> delegate) {
        super(delegate);
    }

    @Test
    public void testResultSetType() throws Exception {
        if (asList(SQLITE).contains(dialect())) {
            log.info("SKIPPING", "ResultSet type tests");
            return;
        }

        ResultSet rs =
        create().select(TBook_ID())
                .from(TBook())
                .where(TBook_ID().in(1, 2))
                .orderBy(TBook_ID())
                .resultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE)
                .fetchResultSet();

        assertTrue(rs.next());
        assertEquals(1, rs.getInt(1));
        assertTrue(rs.next());
        assertEquals(2, rs.getInt(1));
        assertTrue(rs.previous());
        assertEquals(1, rs.getInt(1));
        assertTrue(rs.last());
        assertEquals(2, rs.getInt(1));

        rs.close();
    }

    @SuppressWarnings("serial")
    @Test
    public void testResultSetTypeWithListener() throws Exception {
        if (asList(SQLITE).contains(dialect())) {
            log.info("SKIPPING", "ResultSet type tests");
            return;
        }

        assertEquals(
            asList(1, 1, 1, 2),
            create(new DefaultExecuteListener() {
                int repeat;

                @Override
                public void recordEnd(ExecuteContext ctx) {
                    try {

                        // Rewind the first record three times
                        if (++repeat < 3)
                            ctx.resultSet().previous();
                    }
                    catch (SQLException e) {
                        throw new DataAccessException("Exception", e);
                    }
                }
            })
            .select(TBook_ID())
            .from(TBook())
            .where(TBook_ID().in(1, 2))
            .orderBy(TBook_ID())
            .resultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE)
            .fetch(TBook_ID()));
    }

    @SuppressWarnings("serial")
    @Test
    public void testResultSetConcurrency() throws Exception {
        switch (dialect()) {
            case MARIADB:
            case SQLITE:
            case SYBASE:
                log.info("SKIPPING", "ResultSet concurrency tests");
                return;
        }

        jOOQAbstractTest.reset = false;

        assertEquals(
            asList("Title 1", "Title 2", "Title 3", "Title 4"),
            create(new DefaultExecuteListener() {
                int repeat;

                @Override
                public void recordStart(ExecuteContext ctx) {
                    try {

                        // Change values before reading a record
                        ctx.resultSet().updateString(TBook_TITLE().getName(), "Title " + (++repeat));
                        ctx.resultSet().updateRow();
                    }
                    catch (SQLException e) {
                        throw new DataAccessException("Exception", e);
                    }
                }
            })
            .select(TBook_ID(), TBook_TITLE())
            .from(TBook())

            // Derby doesn't support ORDER BY when using CONCUR_UPDATABLE
            // https://issues.apache.org/jira/browse/DERBY-4138
            // .orderBy(TBook_ID())

            // SQL Server doesn't support SCROLL INSENSITIVE and UPDATABLE
            // .resultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE)

            .resultSetConcurrency(ResultSet.CONCUR_UPDATABLE)
            .fetch(TBook_TITLE()));
    }
}
