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
package org.jooq.meta.extensions.jpa;

import static org.jooq.impl.DSL.name;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.persistence.AttributeConverter;
import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceUnitInfo;

import org.jooq.Name;
import org.jooq.tools.JooqLogger;

import org.hibernate.boot.Metadata;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl;
import org.hibernate.jpa.boot.internal.PersistenceUnitInfoDescriptor;
import org.hibernate.jpa.boot.spi.IntegratorProvider;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.mapping.Table;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;
import org.hibernate.type.Type;
import org.hibernate.type.descriptor.converter.AttributeConverterTypeAdapter;

/**
 * A Hibernate {@link Integrator} that walks the meta model to discover all
 * {@link AttributeConverter} references and how they're tied to database
 * columns.
 * <p>
 * This class was implemented with the help of Vlad Mihalcea's excellent blog
 * post about walking the Hibernate meta model: <a href=
 * "https://vladmihalcea.com/2017/08/24/how-to-get-the-entity-mapping-to-database-table-binding-metadata-from-hibernate/">https://vladmihalcea.com/2017/08/24/how-to-get-the-entity-mapping-to-database-table-binding-metadata-from-hibernate/</a>
 *
 * @author Lukas Eder
 */
final class AttributeConverterExtractor implements Integrator {

    static final JooqLogger                log = JooqLogger.getLogger(JPADatabase.class);

    private Metadata                       meta;
    private JPADatabase                    database;
    private Collection<? extends Class<?>> classes;

    AttributeConverterExtractor(JPADatabase database, Collection<? extends Class<?>> classes) {
        this.database = database;
        this.classes = classes;
    }

    @Override
    public final void integrate(Metadata m, SessionFactoryImplementor f, SessionFactoryServiceRegistry r) {
        this.meta = m;
    }

    @Override
    public final void disintegrate(SessionFactoryImplementor f, SessionFactoryServiceRegistry r) {}

    @SuppressWarnings("unchecked")
    final Map<Name, AttributeConverter<?, ?>> extract() {
        Map<Name, AttributeConverter<?, ?>> result = new LinkedHashMap<>();

        initEntityManagerFactory();
        for (PersistentClass persistentClass : meta.getEntityBindings()) {
            Table table = persistentClass.getTable();

            Iterator<Property> propertyIterator = persistentClass.getPropertyIterator();

            propertyLoop:
            while (propertyIterator.hasNext()) {
                Property property = propertyIterator.next();
                Type type = property.getValue().getType();

                if (type instanceof AttributeConverterTypeAdapter) {
                    AttributeConverter<?, ?> converter = ((AttributeConverterTypeAdapter<?>) type).getAttributeConverter().getConverterBean().getBeanInstance();
                    Iterator<Column> columnIterator = property.getColumnIterator();

                    if (columnIterator.hasNext()) {
                        Column column = columnIterator.next();

                        if (columnIterator.hasNext()) {
                            log.info("AttributeConverter", "Cannot apply AttributeConverter of property " + property + " on several columns.");
                            continue propertyLoop;
                        }

                        result.put(name(table.getCatalog(), table.getSchema(), table.getName(), column.getName()), converter);
                    }
                }
            }
        }

        return result;
    }

    private final EntityManagerFactory initEntityManagerFactory() {
        PersistenceUnitInfo persistenceUnitInfo = persistenceUnitInfo(getClass().getSimpleName());
        Map<String, Object> configuration = new HashMap<>();
        configuration.put("hibernate.integrator_provider", integratorProvider());
        configuration.put(AvailableSettings.CONNECTION_PROVIDER, database.connectionProvider());
        PersistenceUnitInfoDescriptor descriptor = new PersistenceUnitInfoDescriptor(persistenceUnitInfo);
        return new EntityManagerFactoryBuilderImpl(descriptor, configuration).build();
    }

    private IntegratorProvider integratorProvider() {
        return new IntegratorProvider() {
            @Override
            public List<Integrator> getIntegrators() {
                return Collections.<Integrator>singletonList(AttributeConverterExtractor.this);
            }
        };
    }

    private final PersistenceUnitInfoImpl persistenceUnitInfo(String name) {
        return new PersistenceUnitInfoImpl(name, entityClassNames(), properties());
    }

    private final Properties properties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", JPADatabase.HIBERNATE_DIALECT);
        properties.put("hibernate.hbm2ddl.auto", "create-drop");
        return properties;
    }

    private final List<String> entityClassNames() {
        List<String> result = new ArrayList<>(classes.size());

        for (Class<?> klass : classes)
            result.add(klass.getName());

        return result;
    }
}
