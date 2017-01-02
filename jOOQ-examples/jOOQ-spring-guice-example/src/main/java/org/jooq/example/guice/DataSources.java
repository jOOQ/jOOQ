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
package org.jooq.example.guice;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.jolbox.bonecp.BoneCPDataSource;

/**
 * A static utility providing a {@link DataSource} for this example.
 * <p>
 * Your actual data source might originate from a container, or from Spring.
 * Also, your transaction manager might be more sophisticated than this.
 *
 * @author Lukas Eder
 */
public class DataSources extends AbstractModule {

    @Override
    protected void configure() {}

    @Provides
    @Singleton
    DataSource provideDataSource() {
        return DATA_SOURCE;
    }

    @Provides
    @Singleton
    DataSourceTransactionManager provideDataSourceTransactionManager() {
        return new DataSourceTransactionManager(new TransactionAwareDataSourceProxy(DATA_SOURCE));
    }

    /**
     * The singleton instance
     */
    private static final DataSource DATA_SOURCE;

    static {
        try {
            // We're using BoneCP here to configure a connection pool
            Properties p = new Properties();
            p.load(DataSources.class.getResourceAsStream("/config.properties"));

            BoneCPDataSource result = new BoneCPDataSource();
            result.setDriverClass(p.getProperty("db.driver"));
            result.setJdbcUrl(p.getProperty("db.url"));
            result.setUsername(p.getProperty("db.username"));
            result.setPassword(p.getProperty("db.password"));
            result.setDefaultAutoCommit(false);

            DATA_SOURCE = result;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
