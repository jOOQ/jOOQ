package org.jooq.example.testcontainersflyway;

import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.tools.JooqLogger;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testcontainers.jdbc.ContainerDatabaseDriver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Properties;

import static org.jooq.JSONFormat.RecordFormat.OBJECT;
import static org.jooq.Records.mapping;
import static org.jooq.XMLFormat.RecordFormat.COLUMN_NAME_ELEMENTS;
import static org.jooq.example.testcontainersflyway.db.Tables.*;
import static org.jooq.impl.DSL.*;

public class TestContainersTest {

    static JooqLogger log = JooqLogger.getLogger(TestContainersTest.class);
    static Connection connection;
    static DSLContext ctx;

    @BeforeClass
    public static void init() throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("username", "postgres");
        properties.setProperty("password", "postgres");

        log.info("Connecting");
        connection = DriverManager.getConnection(
            System.getProperty("db.url"),
            System.getProperty("db.username"),
            System.getProperty("db.password")
        );

        ctx = DSL.using(connection, SQLDialect.POSTGRES);

        // Use JDBC directly instead of jOOQ to avoid DEBUG logging all of this
        try (Statement s = connection.createStatement()) {

//            log.info("Inserting data to database");
//            s.execute(Source.of(TestContainersTest.class.getResourceAsStream("/postgres-sakila-insert-data.sql")).readString());

            log.info("Finished setup");
        }
    }

    record Film(String title) {}
    record Actor(String firstName, String lastName) {}
    record Category(String name) {}

    @Test
    public void testMultisetMappingIntoJavaRecords() {

        // Get films by title, and their actors and categories as nested collections, as well as
        // all the customers that have rented the film
        Result<?> result = println(ctx
            .select(
                FILM.TITLE.convertFrom(Film::new),
                multiset(
                    select(
                        FILM_ACTOR.actor().FIRST_NAME,
                        FILM_ACTOR.actor().LAST_NAME
                    )
                    .from(FILM_ACTOR)
                    .where(FILM_ACTOR.FILM_ID.eq(FILM.FILM_ID))
                ).convertFrom(r -> r.map(mapping(Actor::new))),
                multiset(
                    select(FILM_CATEGORY.category().NAME)
                    .from(FILM_CATEGORY)
                    .where(FILM_CATEGORY.FILM_ID.eq(FILM.FILM_ID))
                ).convertFrom(r -> r.map(mapping(Category::new)))
            )
            .from(FILM)
            .where(FILM.TITLE.like("A%"))
            .orderBy(FILM.TITLE)
            .limit(5))
            .fetch();

        System.out.println(result);
    }

    @Test
    public void testMultisetFormattingAsXMLorJSON() {

        // Get films by title, and their actors and categories as nested collections,
        // and all the customers that have rented the film, and their payments
        Result<?> result = println(ctx
            .select(
                FILM.TITLE,
                multiset(
                    select(
                        FILM_ACTOR.actor().FIRST_NAME,
                        FILM_ACTOR.actor().LAST_NAME
                    )
                    .from(FILM_ACTOR)
                    .where(FILM_ACTOR.FILM_ID.eq(FILM.FILM_ID))
                ).as("actors"),
                multiset(
                    select(FILM_CATEGORY.category().NAME)
                    .from(FILM_CATEGORY)
                    .where(FILM_CATEGORY.FILM_ID.eq(FILM.FILM_ID))
                ).as("categories"),
                multiset(
                    select(
                        PAYMENT.rental().customer().FIRST_NAME,
                        PAYMENT.rental().customer().LAST_NAME,
                        multisetAgg(
                            PAYMENT.PAYMENT_DATE,
                            PAYMENT.AMOUNT
                        ).as("payments"),
                        sum(PAYMENT.AMOUNT).as("total"))
                    .from(PAYMENT)
                    .where(PAYMENT.rental().inventory().FILM_ID.eq(FILM.FILM_ID))
                    .groupBy(
                        PAYMENT.rental().customer().CUSTOMER_ID,
                        PAYMENT.rental().customer().FIRST_NAME,
                        PAYMENT.rental().customer().LAST_NAME)
                ).as("customers")
            )
            .from(FILM)
            .where(FILM.TITLE.like("A%"))
            .orderBy(FILM.TITLE)
            .limit(5))
            .fetch();

        System.out.println(result.format(new TXTFormat()));
        System.out.println(result.formatXML(new XMLFormat().xmlns(false).format(true).header(false).recordFormat(COLUMN_NAME_ELEMENTS)));
        System.out.println(result.formatJSON(new JSONFormat().format(true).header(false).recordFormat(OBJECT)));
    }

    public static final <T> T println(T t) {
        if (t instanceof Object[])
            System.out.println(Arrays.asList(t));
        else
            System.out.println(t);

        return t;
    }
}
