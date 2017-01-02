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
 */
package org.jooq.util.jpa;

import static org.jooq.tools.StringUtils.defaultIfBlank;
import static org.jooq.tools.StringUtils.isBlank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Entity;

import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.jooq.tools.JooqLogger;
import org.jooq.util.SchemaDefinition;
import org.jooq.util.h2.H2Database;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

/**
 * The JPA database
 *
 * @author Lukas Eder
 */
public class JPADatabase extends H2Database {

    private static final JooqLogger log = JooqLogger.getLogger(JPADatabase.class);

    private Connection              connection;

    @SuppressWarnings("serial")
    @Override
    protected DSLContext create0() {
        if (connection == null) {
            String packages = getProperties().getProperty("packages");

            if (isBlank(packages)) {
                packages = "";
                log.warn("No packages defined", "It is highly recommended that you provide explicit packages to scan");
            }

            try {
                connection = DriverManager.getConnection("jdbc:h2:mem:jooq-meta-extensions", "sa", "");

                MetadataSources metadata = new MetadataSources(
                    new StandardServiceRegistryBuilder()
                        .applySetting("hibernate.dialect", "org.hibernate.dialect.H2Dialect")
                        .applySetting("javax.persistence.schema-generation-connection", connection)

                        // [#5607] JPADatabase causes warnings - This prevents them
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
                                return connection;
                            }
                            @Override
                            public void closeConnection(Connection conn) throws SQLException {}

                            @Override
                            public boolean supportsAggressiveRelease() {
                                return true;
                            }
                        })
                        .build()
                );

                ClassPathScanningCandidateComponentProvider scanner =
                    new ClassPathScanningCandidateComponentProvider(true);

                scanner.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
                for (String pkg : packages.split(","))
                    for (BeanDefinition def : scanner.findCandidateComponents(defaultIfBlank(pkg, "").trim()))
                        metadata.addAnnotatedClass(Class.forName(def.getBeanClassName()));

                // This seems to be the way to do this in idiomatic Hibernate 5.0 API
                // See also: http://stackoverflow.com/q/32178041/521799
                // SchemaExport export = new SchemaExport((MetadataImplementor) metadata.buildMetadata(), connection);
                // export.create(true, true);

                // Hibernate 5.2 broke 5.0 API again. Here's how to do this now:
                SchemaExport export = new SchemaExport();
                export.create(EnumSet.of(TargetType.DATABASE), metadata.buildMetadata());
            }
            catch (Exception e) {
                throw new DataAccessException("Error while exporting schema", e);
            }
        }

        return DSL.using(connection);
    }

    @Override
    protected List<SchemaDefinition> getSchemata0() throws SQLException {
        List<SchemaDefinition> result = new ArrayList<SchemaDefinition>(super.getSchemata0());

        // [#5608] The H2-specific INFORMATION_SCHEMA is undesired in the JPADatabase's output
        //         we're explicitly omitting it here for user convenience.
        Iterator<SchemaDefinition> it = result.iterator();
        while (it.hasNext())
            if ("INFORMATION_SCHEMA".equals(it.next().getName()))
                it.remove();

        return result;
    }
}
