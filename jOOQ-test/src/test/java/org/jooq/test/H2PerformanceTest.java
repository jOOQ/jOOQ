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

package org.jooq.test;

import static org.jooq.SQLDialect.H2;
import static org.jooq.lambda.Unchecked.intConsumer;
import static org.jooq.lambda.Unchecked.runnable;
import static org.jooq.test.h2.generatedclasses.Tables.T_PERFORMANCE_JDBC;
import static org.jooq.test.h2.generatedclasses.Tables.T_PERFORMANCE_JOOQ;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.jooq.DSLContext;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;
import org.jooq.lambda.fi.lang.CheckedRunnable;
import org.jooq.lambda.fi.util.function.CheckedIntConsumer;
import org.jooq.tools.StopWatch;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Performance test for the H2 database
 */
public class H2PerformanceTest {

    static Connection connection;
    static DSLContext ctx;

    @BeforeClass
    public static void before() throws Exception {
        connection = DriverManager.getConnection(
            jOOQAbstractTest.getURL(H2),
            jOOQAbstractTest.getUsername(H2),
            jOOQAbstractTest.getPassword(H2)
        );

        ctx = DSL.using(connection, new Settings().withExecuteLogging(false));
        System.in.read();
        System.in.read();
    }

    @AfterClass
    public static void after() throws Exception {
        connection.close();
        System.in.read();
        System.in.read();
    }

    @Test
    public void testPerformance_INSERT() {
        compareWithJDBC(
            1000000,
            i -> {
                try (PreparedStatement stmt = connection.prepareStatement("insert into t_performance_jdbc (value_int, value_string) values (?, ?)")) {
                    stmt.setInt(1, i);
                    stmt.setString(2, "" + i);
                    stmt.executeUpdate();
                }
            },

            i -> {
                ctx.insertInto(T_PERFORMANCE_JOOQ, T_PERFORMANCE_JOOQ.VALUE_INT, T_PERFORMANCE_JOOQ.VALUE_STRING)
                   .values(i, "" + i)
                   .execute();
            },

            () -> {},
            this::cleanup
        );
    }

    @Test
    public void testPerformance_SELECT() {
        compareWithJDBC(
            50000,
            i -> {
                try (PreparedStatement stmt = connection.prepareStatement("select id, value_int, value_string from t_performance_jdbc");
                    ResultSet rs = stmt.executeQuery()) {
                    Object[] o;

                    while (rs.next()) {
                        o = new Object[3];
                        o[0] = rs.getInt(1); rs.wasNull();
                        o[1] = rs.getInt(2); rs.wasNull();
                        o[2] = rs.getString(3); rs.wasNull();
                    }
                }
            },

            i -> {
                ctx.select(T_PERFORMANCE_JOOQ.ID, T_PERFORMANCE_JOOQ.VALUE_INT, T_PERFORMANCE_JOOQ.VALUE_STRING)
                   .from(T_PERFORMANCE_JOOQ)
                   .fetch();
            },

            this::init,
            this::cleanup
        );
    }

    private void init() {
        for (int i = 0; i < 1000; i++) {
            ctx.insertInto(T_PERFORMANCE_JDBC, T_PERFORMANCE_JDBC.VALUE_INT, T_PERFORMANCE_JDBC.VALUE_STRING).values(i, "" + i).execute();
            ctx.insertInto(T_PERFORMANCE_JOOQ, T_PERFORMANCE_JOOQ.VALUE_INT, T_PERFORMANCE_JOOQ.VALUE_STRING).values(i, "" + i).execute();
        }
    }

    private void cleanup() {
        ctx.delete(T_PERFORMANCE_JDBC).execute();
        ctx.delete(T_PERFORMANCE_JOOQ).execute();
    }

    private void compareWithJDBC(
        int repetitions,
        CheckedIntConsumer jdbc,
        CheckedIntConsumer jooq,
        CheckedRunnable prepare,
        CheckedRunnable cleanup
    ) {

        runnable(prepare).run();

        try {

            // Bootstrapping
            intConsumer(jdbc).accept(-1);
            intConsumer(jooq).accept(-1);

            runnable(cleanup).run();
            StopWatch watch;

            runnable(prepare).run();
            watch = new StopWatch();
            watch.splitInfo("JDBC start");
            for (int i = 0; i < repetitions; i++) {
                intConsumer(jdbc).accept(i);
            }
            watch.splitInfo("JDBC stop");
            double jdbcDuration = watch.split();
            runnable(cleanup).run();

            runnable(prepare).run();
            watch = new StopWatch();
            watch.splitInfo("jOOQ start");
            for (int i = 0; i < repetitions; i++) {
                intConsumer(jooq).accept(i);
            }
            watch.splitInfo("jOOQ stop");
            runnable(cleanup).run();

            double jooqDuration = watch.split();
            assertTrue("JDBC vs. jOOQ : " + (jooqDuration / jdbcDuration), jooqDuration / jdbcDuration < 1.1);
        }
        finally {
            runnable(cleanup).run();
        }
    }
}
