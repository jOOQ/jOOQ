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

import static org.jooq.test.postgresjavatime.generatedclasses.Tables.T_DATES;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;

import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record6;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;
import org.jooq.test.postgresjavatime.generatedclasses.tables.records.TDatesRecord;

/**
 * @author Lukas Eder
 */
public class Java8TimeTests<
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

    public Java8TimeTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, CS, I, IPK, T725, T639, T785, CASE> delegate) {
        super(delegate);
    }

    public void testJava8TimeAPI() {
        clean(T_DATES);

        LocalDate date = LocalDate.of(2015, 02, 10);

        LocalTime time = LocalTime.of(13, 45);
        OffsetTime time_tz = OffsetTime.of(time, ZoneOffset.UTC);

        LocalDateTime ts = LocalDateTime.of(date, time);
        OffsetDateTime ts_tz = OffsetDateTime.of(date, time, ZoneOffset.UTC);

        assertEquals(1,
        create().insertInto(T_DATES)
                .columns(
                    T_DATES.ID,
                    T_DATES.D,
                    T_DATES.T,
                    T_DATES.T_TZ,
                    T_DATES.TS,
                    T_DATES.TS_TZ
                )
                .values(
                    1,
                    date,
                    time,
                    time_tz,
                    ts,
                    ts_tz
                )
                .execute()
        );

        TDatesRecord record = create().fetchOne(T_DATES);

        assertEquals(date, record.getD());
        assertEquals(time, record.getT());
        assertEquals(time_tz, record.getTTz());
        assertEquals(ts, record.getTs());

        // Oracle stores time zones / offsets. PostgreSQL doesn't.
        assertEquals(ts_tz, record.getTsTz().atZoneSameInstant(ZoneOffset.UTC.normalized()).toOffsetDateTime());

        // Tests:
        // - Different time zones
        // - Time zone conversions
        // - Bind variables in filters
        // - Intervals / arithmetic
        // - Inlined bind variables
        // - Stored procedures
        // - Oracle UDTs

    }
}
