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
 *
 *
 *
 */
package org.jooq.meta;

import org.jooq.SQLDialect;
// ...
// ...
import org.jooq.meta.cubrid.CUBRIDDatabase;
// ...
import org.jooq.meta.derby.DerbyDatabase;
import org.jooq.meta.firebird.FirebirdDatabase;
import org.jooq.meta.h2.H2Database;
// ...
import org.jooq.meta.hsqldb.HSQLDBDatabase;
// ...
// ...
import org.jooq.meta.jdbc.JDBCDatabase;
import org.jooq.meta.mariadb.MariaDBDatabase;
import org.jooq.meta.mysql.MySQLDatabase;
// ...
import org.jooq.meta.postgres.PostgresDatabase;
// ...
import org.jooq.meta.sqlite.SQLiteDatabase;
// ...
// ...
// ...
// ...

/**
 * A common utility class that provides access to various {@link Database}
 * implementations and {@link SQLDialect}s.
 *
 * @author Lukas Eder
 */
public class Databases {

    /**
     * Get a reference to a {@link Database} class for a given {@link SQLDialect}.
     */
    @SuppressWarnings("deprecation")
    public static final Class<? extends Database> databaseClass(SQLDialect dialect) {
        Class<? extends Database> result = JDBCDatabase.class;

        switch (dialect) {



























            case CUBRID:        result = CUBRIDDatabase.class;      break;
            case DERBY:         result = DerbyDatabase.class;       break;
            case FIREBIRD_2_5:
            case FIREBIRD_3_0:
            case FIREBIRD:      result = FirebirdDatabase.class;    break;
            case H2:            result = H2Database.class;          break;
            case HSQLDB:        result = HSQLDBDatabase.class;      break;
            case MARIADB:       result = MariaDBDatabase.class;     break;
            case MYSQL_5_7:
            case MYSQL_8_0:
            case MYSQL:         result = MySQLDatabase.class;       break;
            case POSTGRES_9_3:
            case POSTGRES_9_4:
            case POSTGRES_9_5:
            case POSTGRES_10:
            case POSTGRES:      result = PostgresDatabase.class;    break;
            case SQLITE:        result = SQLiteDatabase.class;      break;

            case DEFAULT:
            case SQL99:         result = JDBCDatabase.class;        break;
        }

        return result;
    }

    /**
     * Get a {@link Database} instance for a given {@link SQLDialect}.
     */
    public static final Database database(SQLDialect dialect) {
        try {
            return databaseClass(dialect).newInstance();
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Cannot create an Database instance for " + dialect, e);
        }
    }
}
