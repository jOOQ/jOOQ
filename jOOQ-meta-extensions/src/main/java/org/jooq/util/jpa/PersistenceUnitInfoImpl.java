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


import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;

/**
 * A programmatic implementation of the persistence unit information.
 * <p>
 * See also: <a href=
 * "https://vladmihalcea.com/2015/11/26/how-to-bootstrap-hibernate-without-the-persistence-xml-file/">https://vladmihalcea.com/2015/11/26/how-to-bootstrap-hibernate-without-the-persistence-xml-file/</a>
 *
 * @author Vlad Mihalcea
 * @author Lukas Eder
 */
final class PersistenceUnitInfoImpl implements PersistenceUnitInfo {

    private final String                   persistenceUnitName;
    private PersistenceUnitTransactionType transactionType  = PersistenceUnitTransactionType.RESOURCE_LOCAL;
    private final List<String>             managedClassNames;
    private final List<String>             mappingFileNames = new ArrayList<String>();
    private final Properties               properties;
    private DataSource                     jtaDataSource;
    private DataSource                     nonJtaDataSource;

    PersistenceUnitInfoImpl(String persistenceUnitName, List<String> managedClassNames, Properties properties) {
        this.persistenceUnitName = persistenceUnitName;
        this.managedClassNames = managedClassNames;
        this.properties = properties;
    }

    @Override
    public final String getPersistenceUnitName() {
        return persistenceUnitName;
    }

    @Override
    public final String getPersistenceProviderClassName() {
        return HibernatePersistenceProvider.class.getName();
    }

    @Override
    public final PersistenceUnitTransactionType getTransactionType() {
        return transactionType;
    }

    @Override
    public final DataSource getJtaDataSource() {
        return jtaDataSource;
    }

    public final PersistenceUnitInfoImpl setJtaDataSource(DataSource jtaDataSource) {
        this.jtaDataSource = jtaDataSource;
        this.nonJtaDataSource = null;
        this.transactionType = PersistenceUnitTransactionType.JTA;

        return this;
    }

    @Override
    public final DataSource getNonJtaDataSource() {
        return nonJtaDataSource;
    }

    public final PersistenceUnitInfoImpl setNonJtaDataSource(DataSource nonJtaDataSource) {
        this.nonJtaDataSource = nonJtaDataSource;
        this.jtaDataSource = null;
        this.transactionType = PersistenceUnitTransactionType.RESOURCE_LOCAL;

        return this;
    }

    @Override
    public final List<String> getMappingFileNames() {
        return mappingFileNames;
    }

    @Override
    public final List<URL> getJarFileUrls() {
        return Collections.emptyList();
    }

    @Override
    public final URL getPersistenceUnitRootUrl() {
        return null;
    }

    @Override
    public final List<String> getManagedClassNames() {
        return managedClassNames;
    }

    @Override
    public final boolean excludeUnlistedClasses() {
        return false;
    }

    @Override
    public final SharedCacheMode getSharedCacheMode() {
        return SharedCacheMode.UNSPECIFIED;
    }

    @Override
    public final ValidationMode getValidationMode() {
        return ValidationMode.AUTO;
    }

    @Override
    public final Properties getProperties() {
        return properties;
    }

    @Override
    public final String getPersistenceXMLSchemaVersion() {
        return "2.1";
    }

    @Override
    public final ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    @Override
    public final void addTransformer(ClassTransformer transformer) {}

    @Override
    public final ClassLoader getNewTempClassLoader() {
        return null;
    }
}