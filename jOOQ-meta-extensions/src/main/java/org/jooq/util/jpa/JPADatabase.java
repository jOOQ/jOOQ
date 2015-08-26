/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq.util.jpa;

import static org.jooq.tools.StringUtils.defaultIfBlank;
import static org.jooq.tools.StringUtils.isBlank;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.persistence.Entity;

import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.jooq.tools.JooqLogger;
import org.jooq.util.h2.H2Database;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.tool.hbm2ddl.SchemaExport;
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
                        .build()
                );

                ClassPathScanningCandidateComponentProvider scanner =
                    new ClassPathScanningCandidateComponentProvider(true);

                scanner.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
                for (String pkg : packages.split(",")) {
                    for (BeanDefinition def : scanner.findCandidateComponents(defaultIfBlank(pkg, "").trim())) {
                        metadata.addAnnotatedClass(Class.forName(def.getBeanClassName()));
                    }
                }

                // This seems to be the way to do this in idiomatic Hibernate 5.0 API
                // See also: http://stackoverflow.com/q/32178041/521799
                SchemaExport export = new SchemaExport((MetadataImplementor) metadata.buildMetadata(), connection);
                export.create(true, true);
            }
            catch (Exception e) {
                throw new DataAccessException("Error while exporting schema", e);
            }
        }

        return DSL.using(connection);
    }
}
