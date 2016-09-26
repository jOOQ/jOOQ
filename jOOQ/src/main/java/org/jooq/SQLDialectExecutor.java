/**
 * Copyright (c) 2009-2016, Data Geekery GmbH (http://www.datageekery.com)
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
package org.jooq;

/**
 * Run a piece of code in the context of a {@link SQLDialect}.
 *
 * @author Lukas Eder
 */

@FunctionalInterface

public interface SQLDialectExecutor {

    /**
     * The code run in the context of a {@link SQLDialect}.
     */
    <T> T submit(SQLDialectSupplier<T> supplier);
}

/**
 * @author Lukas Eder
 */
final class SQLDialectExecutors {

    static class SQL99 implements SQLDialectExecutor {
        @Override
        public final <T> T submit(SQLDialectSupplier<T> supplier) {
            return supplier.get();
        }
    }

    static class Default implements SQLDialectExecutor {
        @Override
        public final <T> T submit(SQLDialectSupplier<T> supplier) {
            return supplier.get();
        }
    }

    static class Cubrid implements SQLDialectExecutor {
        @Override
        public final <T> T submit(SQLDialectSupplier<T> supplier) {
            return supplier.get();
        }
    }

    static class Derby implements SQLDialectExecutor {
        @Override
        public final <T> T submit(SQLDialectSupplier<T> supplier) {
            return supplier.get();
        }
    }

    static class Firebird implements SQLDialectExecutor {
        @Override
        public final <T> T submit(SQLDialectSupplier<T> supplier) {
            return supplier.get();
        }
    }

    static class Firebird_2_5 implements SQLDialectExecutor {
        @Override
        public final <T> T submit(SQLDialectSupplier<T> supplier) {
            return supplier.get();
        }
    }

    static class Firebird_3_0 implements SQLDialectExecutor {
        @Override
        public final <T> T submit(SQLDialectSupplier<T> supplier) {
            return supplier.get();
        }
    }

    static class H2 implements SQLDialectExecutor {
        @Override
        public final <T> T submit(SQLDialectSupplier<T> supplier) {
            return supplier.get();
        }
    }

    static class HSQLDB implements SQLDialectExecutor {
        @Override
        public final <T> T submit(SQLDialectSupplier<T> supplier) {
            return supplier.get();
        }
    }

    static class MariaDB implements SQLDialectExecutor {
        @Override
        public final <T> T submit(SQLDialectSupplier<T> supplier) {
            return supplier.get();
        }
    }

    static class MySQL implements SQLDialectExecutor {
        @Override
        public final <T> T submit(SQLDialectSupplier<T> supplier) {
            return supplier.get();
        }
    }

    static class Postgres implements SQLDialectExecutor {
        @Override
        public final <T> T submit(SQLDialectSupplier<T> supplier) {
            return supplier.get();
        }
    }

    static class Postgres_9_3 implements SQLDialectExecutor {
        @Override
        public final <T> T submit(SQLDialectSupplier<T> supplier) {
            return supplier.get();
        }
    }

    static class Postgres_9_4 implements SQLDialectExecutor {
        @Override
        public final <T> T submit(SQLDialectSupplier<T> supplier) {
            return supplier.get();
        }
    }

    static class Postgres_9_5 implements SQLDialectExecutor {
        @Override
        public final <T> T submit(SQLDialectSupplier<T> supplier) {
            return supplier.get();
        }
    }

    static class SQLite implements SQLDialectExecutor {
        @Override
        public final <T> T submit(SQLDialectSupplier<T> supplier) {
            return supplier.get();
        }
    }























































































































































}
