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
package org.jooq;

import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.HSQLDB;
// ...
// ...
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.jooq.exception.DataAccessException;
import org.jooq.util.xml.jaxb.InformationSchema;

/**
 * A wrapping object for {@link DatabaseMetaData} or for other sources of
 * database meta information (e.g. {@link InformationSchema})
 * <p>
 * This object can be obtained through {@link DSLContext#meta()} in order to
 * provide convenient access to your database meta data. This abstraction has
 * two purposes:
 * <p>
 * <ol>
 * <li>To increase API convenience, as no checked {@link SQLException} is
 * thrown, only the unchecked {@link DataAccessException}</li>
 * <li>To increase API convenience, as the returned objects are always jOOQ
 * objects, not JDBC {@link ResultSet} objects with hard-to-remember API
 * constraints</li>
 * </ol>
 *
 * @author Lukas Eder
 */
public interface Meta {

    /**
     * Get all catalog objects from the underlying {@link DatabaseMetaData}.
     * <p>
     * For those databases that don't really support JDBC meta data catalogs, a
     * single empty catalog (named <code>""</code>) will be returned. In other
     * words, there is always at least one catalog in a database.
     * <p>
     * NOTE: Catalogs are experimental in jOOQ 3.0
     *
     * @throws DataAccessException If something went wrong fetching the meta
     *             objects
     */
    @Support
    List<Catalog> getCatalogs() throws DataAccessException;

    /**
     * Get all schema objects from the underlying {@link DatabaseMetaData}.
     *
     * @throws DataAccessException If something went wrong fetching the meta
     *             objects
     */
    @Support
    List<Schema> getSchemas() throws DataAccessException;

    /**
     * Get all table objects from the underlying {@link DatabaseMetaData}.
     *
     * @throws DataAccessException If something went wrong fetching the meta
     *             objects
     */
    @Support
    List<Table<?>> getTables() throws DataAccessException;

    /**
     * Get all sequence objects from the underlying {@link DatabaseMetaData}.
     *
     * @throws DataAccessException If something went wrong fetching the meta
     *             objects
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, POSTGRES })
    List<Sequence<?>> getSequences() throws DataAccessException;

    /**
     * Get all primary keys from the underlying {@link DatabaseMetaData}.
     *
     * @throws DataAccessException If something went wrong fetching the meta
     *             objects
     */
    @Support
    List<UniqueKey<?>> getPrimaryKeys() throws DataAccessException;
}
