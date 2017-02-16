package org.jooq.example.flyway;

import static java.util.Arrays.asList;
import static org.jooq.example.flyway.j.db.h2.Tables.AUTHOR;
import static org.jooq.example.flyway.j.db.h2.Tables.BOOK;
import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.DriverManager;

import org.jooq.Result;
import org.jooq.impl.DSL;

import org.junit.Test;

/**
 * @author Lukas Eder
 */
public class AfterMigrationTestJava {

    @Test
    public void testQueryingAfterMigration() throws Exception {
        try (Connection c = DriverManager.getConnection("jdbc:h2:~/flyway-test", "sa", "")) {
            Result<?> result =
            DSL.using(c)
                .select(
                    AUTHOR.FIRST_NAME,
                    AUTHOR.LAST_NAME,
                    BOOK.ID,
                    BOOK.TITLE
                )
                .from(AUTHOR)
                .join(BOOK)
                .on(AUTHOR.ID.eq(BOOK.AUTHOR_ID))
                .orderBy(BOOK.ID.asc())
                .fetch();

            assertEquals(4, result.size());
            assertEquals(asList(1, 2, 3, 4), result.getValues(BOOK.ID));
        }
    }
}
