package org.jooq.example.flyway.ddl;

import static java.util.Arrays.asList;
import static org.jooq.example.flyway.ddl.db.h2.Tables.AUTHOR;
import static org.jooq.example.flyway.ddl.db.h2.Tables.BOOK;
import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Properties;
import java.util.UUID;

import org.flywaydb.core.Flyway;
import org.jooq.Record3;
import org.jooq.Result;
import org.jooq.impl.DSL;
import org.jooq.tools.jdbc.SingleConnectionDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FlywayTest {

	private Connection connection;

	@Before
	public void setup() throws SQLException {
        Properties info = new Properties();
        info.put("user", "sa");
        info.put("password", "");
		connection = new org.h2.Driver().connect("jdbc:h2:mem:jooq-example-flyway-ddl-" + UUID.randomUUID(), info);

		// This flyway migration should produce the same database as the DDLDatabase
		// used by the code generator, as it is configured using flyway file sorting
		Flyway flyway = Flyway.configure().dataSource(new SingleConnectionDataSource(connection)).load();
		flyway.migrate();
	}

	@After
	public void teardown() throws SQLException {
		connection.close();
	}

	@Test
	public void test() {
		Result<Record3<String, String, String>> result =
		DSL.using(connection)
		   .select(AUTHOR.FIRST_NAME, AUTHOR.LAST_NAME, BOOK.TITLE)
		   .from(AUTHOR)
		   .join(BOOK).on(AUTHOR.ID.eq(BOOK.AUTHOR_ID))
		   .orderBy(AUTHOR.ID, BOOK.ID)
		   .fetch();

		assertEquals(4, result.size());
		assertEquals(asList("George", "George", "Paulo", "Paulo"), result.getValues(AUTHOR.FIRST_NAME));
		assertEquals(asList("Orwell", "Orwell", "Coelho", "Coelho"), result.getValues(AUTHOR.LAST_NAME));
		assertEquals(asList("1984", "Animal Farm", "O Alquimista", "Brida"), result.getValues(BOOK.TITLE));
	}
}
