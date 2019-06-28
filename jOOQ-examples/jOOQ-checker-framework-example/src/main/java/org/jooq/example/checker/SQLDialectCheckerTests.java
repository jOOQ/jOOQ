package org.jooq.example.checker;

import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...

import org.jooq.Allow;
import org.jooq.Require;
import org.jooq.impl.DSL;

// The class requires the H2, MYSQL, and POSTGRES_9_5 families
// The inherited @Allow annotation from the package allows only the MYSQL family, though.
@Require({ H2, MYSQL, POSTGRES_9_5 })
public class SQLDialectCheckerTests {

    // @Allow = MYSQL (inherited from package)
    // @Require = { H2, MYSQL, POSTGRES_9_5 }
    public static void doesntCompileBecauseOnlyMySQLIsAllowed() {
        DSL.array(2);
    }

    // @Allow = { MYSQL (inherited from package), POSTGRES_9_4 }
    // @Require = { POSTGRES_9_4 }
    @Allow(POSTGRES_9_4)
    @Require(POSTGRES_9_4)
    public static void doesntCompileBecausePostgres95IsNotAllowed() {
        DSL.cube(DSL.inline(1));
    }

    // @Allow = { MYSQL (inherited from package), POSTGRES_9_5 }
    // @Require = { POSTGRES_9_5 }
    @Allow(POSTGRES_9_5)
    @Require(POSTGRES_9_5)
    public static void compilesBecausePostgres95IsAllowed() {
        DSL.cube(DSL.inline(1));
    }

    // @Allow = { MYSQL (inherited from package), POSTGRES }
    // @Require = { POSTGRES }
    @Allow(POSTGRES)
    @Require(POSTGRES)
    public static void compilesBecausePostgresIsAllowedAndRequired() {
        DSL.cube(DSL.inline(1));
    }

    // @Allow = { MYSQL (inherited from package), POSTGRES }
    // @Require = { POSTGRES_9_4 }
    @Allow(POSTGRES)
    @Require(POSTGRES_9_4)
    public static void doesntCompileBecausePostgres94IsRequired() {
        DSL.cube(DSL.inline(1));
    }

    // @Allow = { MYSQL (inherited from package), POSTGRES_9_4 }
    // @Require = { POSTGRES }
    @Allow(POSTGRES_9_4)
    @Require(POSTGRES)
    public static void compilesBecausePostgres94IsAllowed() {
        DSL.lateral(DSL.dual());
    }

    // @Allow = { H2, MYSQL (inherited from package) }
    // @Require = { H2, MYSQL, POSTGRES_9_5 } (inherited from class)
    @Allow(H2)
    public static void doesntCompileBecauseMYSQLIsNotSupported() {
        DSL.array(2);
    }

    // @Allow = { H2, MYSQL (inherited from package) }
    // @Require = { H2 }
    @Allow(H2)
    @Require(H2)
    public static void compilesBecauseH2IsAllowedAndRequired() {
        DSL.array(2);
    }

    // @Allow = { H2, MYSQL (inherited from package) }
    // @Require = { H2 }
    @Allow(H2)
    @Require({ H2, ORACLE })
    public static void doesntCompileAsOracleCannotBeRequiredInScope() {
        DSL.array(2);
    }
}
