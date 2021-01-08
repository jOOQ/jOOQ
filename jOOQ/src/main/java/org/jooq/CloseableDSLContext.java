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
package org.jooq;

import java.sql.Connection;
import java.util.Properties;

import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;

/**
 * A resourceful {@link DSLContext} that should be closed in a
 * try-with-resources statement.
 */
public interface CloseableDSLContext extends DSLContext, AutoCloseable {

    // -------------------------------------------------------------------------
    // XXX AutoCloseable API
    // -------------------------------------------------------------------------

    /**
     * Close the underlying resources, if any resources have been allocated when
     * constructing this <code>DSLContext</code>.
     * <p>
     * Some {@link DSLContext} constructors, such as {@link DSL#using(String)},
     * {@link DSL#using(String, Properties)}, or
     * {@link DSL#using(String, String, String)} allocate a {@link Connection}
     * resource, which is inaccessible to the outside of the {@link DSLContext}
     * implementation. Proper resource management must thus be done via this
     * {@link #close()} method.
     *
     * @throws DataAccessException When something went wrong closing the
     *             underlying resources.
     */
    @Override
    void close() throws DataAccessException;

}
