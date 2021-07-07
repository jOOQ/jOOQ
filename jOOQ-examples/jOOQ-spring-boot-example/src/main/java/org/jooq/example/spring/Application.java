package org.jooq.example.spring;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;

/**
 * The spring boot application.
 * <p>
 * Starting from jOOQ 3.15, jOOQ supports {@link DSLContext} with a configured
 * R2DBC {@link Configuration#connectionFactory()} out of the box. Up until
 * Spring Boot 2.5, Spring Boot is not aware of this, and may auto configure an
 * R2DBC connection rather than a JDBC connection. To work around this, use
 * {@link SpringBootApplication#exclude()} to explicitly exclude the
 * {@link R2dbcAutoConfiguration}.
 *
 * @author Thomas Darimont
 * @author Lukas Eder
 */
@SpringBootApplication(exclude = { R2dbcAutoConfiguration.class })
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
