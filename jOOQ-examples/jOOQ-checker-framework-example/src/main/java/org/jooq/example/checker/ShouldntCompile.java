package org.jooq.example.checker;
import static org.jooq.example.checker.db.h2.Tables.AUTHOR;
import static org.jooq.example.checker.db.h2.Tables.BOOK;

import java.sql.Connection;
import java.sql.DriverManager;

import org.jooq.Require;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

/**
 * @author Lukas Eder
 */
@Require(
    value = { SQLDialect.H2, SQLDialect.MYSQL }
)
public class ShouldntCompile {

    @Require({SQLDialect.H2, SQLDialect.MYSQL})
    public static void main(String[] args) throws Exception {
        try (Connection c = DriverManager.getConnection("jdbc:h2:~/checker-test", "sa", "")) {
            Result<?> result =
            DSL.using(c)
               .select(
                   AUTHOR.FIRST_NAME,
                   AUTHOR.LAST_NAME,
                   BOOK.ID,
                   BOOK.TITLE,
                   DSL.array(1)
               )
               .from(AUTHOR)
               .join(BOOK)
               .on(AUTHOR.ID.eq(BOOK.AUTHOR_ID))
               .connectBy("abc")
               .orderBy(BOOK.ID.asc())
               .fetch();

            System.out.println(result);
        }
    }

    public static void x() {
        DSL.array(2);
    }
}
