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

import static org.jooq.ExecuteListener.onStart;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Year;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.BiConsumer;

import javax.sql.DataSource;

import org.jooq.DSLContext;
import org.jooq.example.jpa.embeddables.Title;
import org.jooq.example.jpa.entity.Actor;
import org.jooq.example.jpa.entity.Film;
import org.jooq.example.jpa.entity.Language;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.tools.jdbc.LoggingConnection;
import org.jooq.tools.jdbc.SingleConnectionDataSource;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.SharedCacheMode;
import jakarta.persistence.ValidationMode;
import jakarta.persistence.spi.ClassTransformer;
import jakarta.persistence.spi.PersistenceUnitInfo;
import jakarta.persistence.spi.PersistenceUnitTransactionType;

/**
 * @author Lukas Eder
 */
final class Setup {

    // This class sets up an EntityManager and configures the jOOQ DSLContext
    // ----------------------------------------------------------------------
    static void run(BiConsumer<EntityManager, DSLContext> consumer) throws Exception {
        Connection connection = null;
        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {

            // Bootstrapping JDBC:
            Class.forName("org.h2.Driver");
            connection = new LoggingConnection(DriverManager.getConnection("jdbc:h2:mem:jooq-jpa-example", "sa", ""));
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

            Map<Object, Object> props = new HashMap<>();

            DataSource ds = new SingleConnectionDataSource(connection);
            props.put("hibernate.connection.datasource", ds);
            props.put("hibernate.archive.autodetection", "");

            emf = new HibernatePersistenceProvider().createContainerEntityManagerFactory(pui(ds), props);
            em = emf.createEntityManager();

            final EntityManager e = em;

            // Run some Hibernate / jOOQ logic inside of a transaction
            em.getTransaction().begin();
            data(em);

            consumer.accept(
                em,
                new DefaultConfiguration()
                    .set(connection)
                    // Flush all changes from the EntityManager to the database for them to be visible in jOOQ
                    .set(onStart(ctx -> e.flush()))
                    .dsl()
            );
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

    private static PersistenceUnitInfo pui(DataSource ds) {
        return new PersistenceUnitInfo() {
            @Override
            public String getPersistenceUnitName() {
                return "ApplicationPersistenceUnit";
            }

            @Override
            public String getPersistenceProviderClassName() {
                return "org.hibernate.jpa.HibernatePersistenceProvider";
            }

            @Override
            public PersistenceUnitTransactionType getTransactionType() {
                return PersistenceUnitTransactionType.RESOURCE_LOCAL;
            }

            @Override
            public DataSource getJtaDataSource() {
                return null;
            }

            @Override
            public DataSource getNonJtaDataSource() {
                return ds;
            }

            @Override
            public List<String> getMappingFileNames() {
                return Collections.emptyList();
            }

            @Override
            public List<URL> getJarFileUrls() {
                try {
                    return Collections.list(this.getClass()
                                                .getClassLoader()
                                                .getResources(""));
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }

            @Override
            public URL getPersistenceUnitRootUrl() {
                return null;
            }

            @Override
            public List<String> getManagedClassNames() {
                return Arrays.asList(
                    Actor.class.getName(),
                    Film.class.getName(),
                    Language.class.getName()
                );
            }

            @Override
            public boolean excludeUnlistedClasses() {
                return true;
            }

            @Override
            public SharedCacheMode getSharedCacheMode() {
                return null;
            }

            @Override
            public ValidationMode getValidationMode() {
                return null;
            }

            @Override
            public Properties getProperties() {
                return new Properties();
            }

            @Override
            public String getPersistenceXMLSchemaVersion() {
                return null;
            }

            @Override
            public ClassLoader getClassLoader() {
                return null;
            }

            @Override
            public void addTransformer(ClassTransformer transformer) {

            }

            @Override
            public ClassLoader getNewTempClassLoader() {
                return null;
            }
        };
    }

    static void data(EntityManager em) {

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
        em.flush();
    }

    private Setup() {}
}
