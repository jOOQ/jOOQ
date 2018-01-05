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
package org.jooq.example.jpa;

import static org.jooq.example.jpa.jooq.Tables.ACTOR;
import static org.jooq.example.jpa.jooq.Tables.FILM;
import static org.jooq.example.jpa.jooq.Tables.FILM_ACTOR;
import static org.jooq.example.jpa.jooq.Tables.LANGUAGE;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.min;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Year;
import java.util.Arrays;
import java.util.EnumSet;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.jooq.DSLContext;
import org.jooq.ExecuteContext;
import org.jooq.SQLDialect;
import org.jooq.example.jpa.embeddables.Title;
import org.jooq.example.jpa.entity.Actor;
import org.jooq.example.jpa.entity.Film;
import org.jooq.example.jpa.entity.Language;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultExecuteListener;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

/**
 * @author Lukas Eder
 */
class JPAExample {

    private static void run(EntityManager em, DSLContext ctx) {

        // Set up database
        // ---------------
        Language english = new Language("English");
        Language german = new Language("German");

        Actor umaThurman = new Actor("Uma", "Thurman");
        Actor davidCarradine = new Actor("David", "Carradine");
        Actor darylHannah = new Actor("Daryl", "Hannah");
        Actor michaelAngarano = new Actor("Michael", "Angarano");
        Actor reeceThompson = new Actor("Reece", "Thompson");

        Film killBill = new Film(
            Title.of("Kill Bill"),
            english,
            111,
            Year.of(2015)
        );
        Film meerjungfrauen = new Film(
            Title.of("Meerjungfrauen ticken anders"),
            german,
            89,
            Year.of(2017)
        );

        killBill.actors.addAll(Arrays.asList(umaThurman, davidCarradine, darylHannah));
        meerjungfrauen.actors.addAll(Arrays.asList(umaThurman, michaelAngarano, reeceThompson));

        em.persist(english);
        em.persist(german);

        em.persist(umaThurman);
        em.persist(davidCarradine);
        em.persist(darylHannah);
        em.persist(michaelAngarano);
        em.persist(reeceThompson);

        em.persist(killBill);
        em.persist(meerjungfrauen);

        // Flush your changes to the database to be sure that jOOQ can pick them up below
        // ------------------------------------------------------------------------------
        em.flush();

        System.out.println(
            ctx.select(
                    ACTOR.FIRSTNAME,
                    ACTOR.LASTNAME,
                    count().as("Total"),
                    count().filterWhere(LANGUAGE.NAME.eq("English")).as("English"),
                    count().filterWhere(LANGUAGE.NAME.eq("German")).as("German"),
                    min(FILM.RELEASE_YEAR),
                    max(FILM.RELEASE_YEAR))
               .from(ACTOR)
               .join(FILM_ACTOR).on(ACTOR.ACTORID.eq(FILM_ACTOR.ACTORS_ACTORID))
               .join(FILM).on(FILM.FILMID.eq(FILM_ACTOR.FILMS_FILMID))
               .join(LANGUAGE).on(FILM.LANGUAGE_LANGUAGEID.eq(LANGUAGE.LANGUAGEID))
               .groupBy(
                    ACTOR.ACTORID,
                    ACTOR.FIRSTNAME,
                    ACTOR.LASTNAME)
               .orderBy(ACTOR.FIRSTNAME, ACTOR.LASTNAME, ACTOR.ACTORID)
               .fetch()
        );
    }

    // Just ignore that enterprisish bootstrapping madness down there. The beef of the example is above this line
    // ----------------------------------------------------------------------------------------------------------

    @SuppressWarnings("serial")
    public static void main(String[] args) throws Exception {
        Connection connection = null;
        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {

            // Bootstrapping JDBC:
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection("jdbc:h2:mem:jooq-jpa-example", "sa", "");
            final Connection c = connection;

            // Creating an in-memory H2 database from our entities
            MetadataSources metadata = new MetadataSources(
                new StandardServiceRegistryBuilder()
                    .applySetting("hibernate.dialect", "org.hibernate.dialect.H2Dialect")
                    .applySetting("javax.persistence.schema-generation-connection", connection)
                    .applySetting("javax.persistence.create-database-schemas", true)

                    // [#5607] JPADatabase causes warnings - This prevents
                    // them
                    .applySetting(AvailableSettings.CONNECTION_PROVIDER, new ConnectionProvider() {
                        @SuppressWarnings("rawtypes")
                        @Override
                        public boolean isUnwrappableAs(Class unwrapType) {
                            return false;
                        }

                        @Override
                        public <T> T unwrap(Class<T> unwrapType) {
                            return null;
                        }

                        @Override
                        public Connection getConnection() {
                            return c;
                        }

                        @Override
                        public void closeConnection(Connection conn) throws SQLException {}

                        @Override
                        public boolean supportsAggressiveRelease() {
                            return true;
                        }
                    })
                    .build());

            metadata.addAnnotatedClass(Actor.class);
            metadata.addAnnotatedClass(Film.class);
            metadata.addAnnotatedClass(Language.class);

            SchemaExport export = new SchemaExport();
            export.create(EnumSet.of(TargetType.DATABASE), metadata.buildMetadata());

            // Setting up an EntityManager using Spring (much easier than out-of-the-box Hibernate)
            LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
            HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
            adapter.setDatabasePlatform(SQLDialect.H2.thirdParty().hibernateDialect());
            bean.setDataSource(new SingleConnectionDataSource(connection, true));
            bean.setPackagesToScan("org.jooq.example.jpa.entity");
            bean.setJpaVendorAdapter(adapter);
            bean.setPersistenceUnitName("test");
            bean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
            bean.afterPropertiesSet();

            emf = bean.getObject();
            em = emf.createEntityManager();

            final EntityManager e = em;

            // Run some Hibernate / jOOQ logic inside of a transaction
            em.getTransaction().begin();
            run(
                em,
                DSL.using(new DefaultConfiguration()
                    .set(connection)
                    .set(new DefaultExecuteListener() {
                        @Override
                        public void start(ExecuteContext ctx) {
                            // Flush all changes from the EntityManager to the database for them to be visible in jOOQ
                            e.flush();
                            super.start(ctx);
                        }
                    })
            ));
            em.getTransaction().commit();
        }
        finally {
            if (em != null)
                em.close();

            if (emf != null)
                emf.close();

            if (connection != null)
                connection.close();
        }
    }
}
