package org.jooq.impl;

import org.jooq.SQLDialect;
import org.jooq.util.cubrid.CUBRIDDataType;
import org.jooq.util.firebird.FirebirdDataType;
import org.jooq.util.h2.H2DataType;
import org.jooq.util.hsqldb.HSQLDBDataType;
import org.jooq.util.mariadb.MariaDBDataType;
import org.jooq.util.mysql.MySQLDataType;
import org.jooq.util.postgres.PostgresDataType;
import org.jooq.util.sqlite.SQLiteDataType;

import java.util.HashMap;
import java.util.Map;

class SQLDialectRegistry {

    private static Map<SQLDialect, Class<?>> dataTypeClasses = new HashMap<>();

    static {
        dataTypeClasses.put(SQLDialect.SQL99, SQLDataType.class);
        dataTypeClasses.put(SQLDialect.DEFAULT, SQLDataType.class);
        dataTypeClasses.put(SQLDialect.CUBRID, CUBRIDDataType.class);
        dataTypeClasses.put(SQLDialect.FIREBIRD, FirebirdDataType.class);
        dataTypeClasses.put(SQLDialect.H2, H2DataType.class);
        dataTypeClasses.put(SQLDialect.HSQLDB, HSQLDBDataType.class);
        dataTypeClasses.put(SQLDialect.MARIADB, MariaDBDataType.class);
        dataTypeClasses.put(SQLDialect.MYSQL, MySQLDataType.class);
        dataTypeClasses.put(SQLDialect.POSTGRES, PostgresDataType.class);
        dataTypeClasses.put(SQLDialect.SQLITE, SQLiteDataType.class);
    }

    static void initializeDialect(SQLDialect dialect) {
        Class<?> dataTypeClass = dataTypeClasses.get(dialect.family());
        try {
            Class.forName(dataTypeClass.getName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
