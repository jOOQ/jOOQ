package org.jooq.example.checker;

import static org.jooq.SQLDialect.H2;

import org.jooq.Allow;
import org.jooq.Require;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

// The class requires both H2 and MySQL
// The inherited @Allow annotation from the package allows only MySQL, though.
@Require({ SQLDialect.H2, SQLDialect.MYSQL })
public class SQLDialectCheckerTests {

    // @Allow = MySQL (inherited from package)
    // @Require = { H2, MySQL } (inherited from class)
    public static void doesntCompileBecauseH2IsNotAllowedAndMySQLIsNotSupported() {
        DSL.array(2);
    }

    // @Allow = { H2, MySQL (inherited from package) }
    // @Require = { H2, MySQL } (inherited from class)
    @Allow(H2)
    public static void doesntCompileBecauseMySQLIsNotSupported() {
        DSL.array(2);
    }

    // @Allow = { H2, MySQL (inherited from package) }
    // @Require = { H2, MySQL } (inherited from class)
    @Allow(H2)
    @Require(H2)
    public static void compiles() {
        DSL.array(2);
    }
}
