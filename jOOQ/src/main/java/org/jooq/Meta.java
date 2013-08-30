/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is triple-licensed under ASL 2.0, AGPL 3.0, and jOOQ EULA
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   ASL 2.0 or jOOQ EULA.
 * - If you're using this work with at least one commercial database, you may
 *   choose AGPL 3.0 or jOOQ EULA.
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
 * AGPL 3.0
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 *
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details: http://www.jooq.org/eula
 */
package org.jooq;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.jooq.exception.DataAccessException;

/**
 * A wrapping object for {@link DatabaseMetaData}
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
     * Get all primary keys from the underlying {@link DatabaseMetaData}.
     *
     * @throws DataAccessException If something went wrong fetching the meta
     *             objects
     */
    @Support
    List<UniqueKey<?>> getPrimaryKeys() throws DataAccessException;
}
