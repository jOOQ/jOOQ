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
package org.jooq.util.jpa;

import static org.jooq.tools.StringUtils.defaultIfBlank;
import static org.jooq.tools.StringUtils.isBlank;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.UUID;

import javax.persistence.AttributeConverter;
import javax.persistence.Entity;

import org.jooq.DSLContext;
import org.jooq.Name;
import org.jooq.SQLDialect;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.jooq.impl.JPAConverter;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.jdbc.JDBCUtils;
import org.jooq.util.SchemaDefinition;
import org.jooq.util.h2.H2Database;
import org.jooq.util.jaxb.ForcedType;

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
 * The JPA database.
 * <p>
 * This jOOQ-meta schema source works on an undisclosed in-memory
 * {@link H2Database}, which is constructed from a set of JPA-annotated entities
 * using Spring and Hibernate:
 * <p>
 * <ul>
 * <li>Spring discovers all the JPA-annotated entities in the comma-separated
 * list of <code>packages</code> (configured in the code generator)</li>
 * <li>Those entities are passed to Hibernate's {@link SchemaExport} to generate
 * an empty database schema in the in-memory H2 database</li>
 * <li>A jOOQ {@link H2Database} is used to reverse-engineer this schema
 * again</li>
 * </ul>
 *
 * @author Lukas Eder
 */
public class JPADatabase extends H2Database {

    static final String     HIBERNATE_DIALECT = SQLDialect.H2.thirdParty().hibernateDialect();
    static final JooqLogger log               = JooqLogger.getLogger(JPADatabase.class);

    private Connection      connection;

    @Override
    public void close() {
        JDBCUtils.safeClose(connection);
        super.close();
    }

    @Override
    protected DSLContext create0() {
        if (connection == null) {
            String packages = getProperties().getProperty("packages");

            if (isBlank(packages)) {
                packages = "";
                log.warn("No packages defined", "It is highly recommended that you provide explicit packages to scan");
            }

            boolean useAttributeConverters = Boolean.valueOf(getProperties().getProperty("use-attribute-converters", "true"));

            try {
                Properties info = new Properties();
                info.put("user", "sa");
                info.put("password", "");
                connection = new org.h2.Driver().connect("jdbc:h2:mem:jooq-meta-extensions-" + UUID.randomUUID(), info);

                MetadataSources metadata = new MetadataSources(
                    new StandardServiceRegistryBuilder()
                        .applySetting("hibernate.dialect", HIBERNATE_DIALECT)
                        .applySetting("javax.persistence.schema-generation-connection", connection)
                        .applySetting("javax.persistence.create-database-schemas", true)

                        // [#5607] JPADatabase causes warnings - This prevents them
                        .applySetting(AvailableSettings.CONNECTION_PROVIDER, connectionProvider())
                        .build()
                );

                ClassPathScanningCandidateComponentProvider scanner =
                    new ClassPathScanningCandidateComponentProvider(true);

                scanner.addIncludeFilter(new AnnotationTypeFilter(Entity.class));

                // [#5845] Use the correct ClassLoader to load the jpa entity classes defined in the user project
                ClassLoader cl = Thread.currentThread().getContextClassLoader();

                for (String pkg : packages.split(","))
                    for (BeanDefinition def : scanner.findCandidateComponents(defaultIfBlank(pkg, "").trim()))
                        metadata.addAnnotatedClass(Class.forName(def.getBeanClassName(), true, cl));

                // This seems to be the way to do this in idiomatic Hibernate 5.0 API
                // See also: http://stackoverflow.com/q/32178041/521799
                // SchemaExport export = new SchemaExport((MetadataImplementor) metadata.buildMetadata(), connection);
                // export.create(true, true);

                // Hibernate 5.2 broke 5.0 API again. Here's how to do this now:
                SchemaExport export = new SchemaExport();
                export.create(EnumSet.of(TargetType.DATABASE), metadata.buildMetadata());

                if (useAttributeConverters)
                    loadAttributeConverters(metadata.getAnnotatedClasses());
            }
            catch (Exception e) {
                throw new DataAccessException("Error while exporting schema", e);
            }
        }

        return DSL.using(connection);
    }

    @SuppressWarnings("serial")
    ConnectionProvider connectionProvider() {
        return new ConnectionProvider() {
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
            public void closeConnection(Connection conn) {}

            @Override
            public boolean supportsAggressiveRelease() {
                return true;
            }
        };
    }

    private final void loadAttributeConverters(Collection<? extends Class<?>> classes) {
        try {
            AttributeConverterExtractor extractor = new AttributeConverterExtractor(this, classes);

            attributesLoop:
            for (Entry<Name, AttributeConverter<?, ?>> entry : extractor.extract().entrySet()) {
                Class<?> convertToEntityAttribute = null;

                for (Method method : entry.getValue().getClass().getMethods())
                    if ("convertToEntityAttribute".equals(method.getName()))
                        convertToEntityAttribute = method.getReturnType();

                if (convertToEntityAttribute == null) {
                    log.info("AttributeConverter", "Cannot use AttributeConverter: " + entry.getValue().getClass().getName());
                    continue attributesLoop;
                }

                // Tables can be fully or partially or not at all qualified. Let's just accept any prefix
                // to the available qualification
                String regex = "(.*?\\.)?" + entry.getKey().unquotedName().toString().replace(".", "\\.");
                ForcedType forcedType = new ForcedType()
                    .withExpression("(?i:" + regex + ")")
                    .withUserType(convertToEntityAttribute.getName())
                    .withConverter(String.format("new %s(%s.class)",
                        JPAConverter.class.getName(),
                        entry.getValue().getClass().getName()
                    ));

                log.info("AttributeConverter", "Configuring JPA AttributeConverter: " + forcedType);
                getConfiguredForcedTypes().add(forcedType);
            }
        }

        // AttributeConverter is part of JPA 2.1. Older JPA providers may not have this type, yet
        catch (NoClassDefFoundError e) {
            log.info("AttributeConverter", "Cannot load AttributeConverters: " + e.getMessage());
        }
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
