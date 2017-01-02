/*
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
 */
package org.jooq.example;

import static java.sql.DriverManager.getConnection;
import static org.jooq.impl.DSL.using;

import java.sql.Connection;
import java.util.Properties;
import java.util.function.Consumer;

import org.jooq.DSLContext;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;

/**
 * @author Lukas Eder
 */
public class Utils {

    static Connection connection;
    static DSLContext dsl;

    @BeforeClass
    public static void start() throws Exception {
        Properties p = new Properties();
        p.load(OracleAQExamples.class.getResourceAsStream("/config.properties"));

        connection = getConnection(p.getProperty("db.url"), p.getProperty("db.username"), p.getProperty("db.password"));
        dsl = using(connection);
    }

    @AfterClass
    public static void end() throws Exception {
        connection.close();
    }

    /**
     * This is needed to allow for throwing Throwables from lambda expressions.
     */
    @FunctionalInterface
    public static interface ThrowableRunnable {
        void run() throws Throwable;
    }

    /**
     * Assert a Throwable type.
     */
    public static void assertThrows(Class<?> throwable, ThrowableRunnable runnable) {
        assertThrows(throwable, runnable, t -> {});
    }

    /**
     * Assert a Throwable type and implement more assertions in a consumer.
     */
    public static void assertThrows(Class<?> throwable, ThrowableRunnable runnable, Consumer<Throwable> exceptionConsumer) {
        boolean fail = false;
        try {
            runnable.run();
            fail = true;
        }
        catch (Throwable t) {
            if (!throwable.isInstance(t))
                throw new AssertionError("Bad exception type", t);

            exceptionConsumer.accept(t);
        }

        if (fail)
            Assert.fail("No exception was thrown");
    }
}
