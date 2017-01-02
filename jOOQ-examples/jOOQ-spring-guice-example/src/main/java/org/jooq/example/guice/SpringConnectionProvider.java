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

import java.sql.Connection;

import javax.sql.DataSource;

import org.jooq.ConnectionProvider;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

/**
 * This connection provider uses Spring to extract the
 * {@link TransactionAwareDataSourceProxy} from our BoneCP pooled connection
 * {@link DataSource}.
 *
 * @author Lukas Eder
 */
public class SpringConnectionProvider implements ConnectionProvider {

    private final DataSource dataSource;

    public SpringConnectionProvider(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Connection acquire() throws DataAccessException {
        return DataSourceUtils.getConnection(dataSource);
    }

    @Override
    public void release(Connection connection) throws DataAccessException {
        DataSourceUtils.releaseConnection(connection, dataSource);
    }
}
