/**
 * Copyright (c) 2009-2011, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package org.jooq.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.jooq.Configuration;
import org.jooq.ConfigurationRegistry;
import org.jooq.Query;
import org.jooq.exception.DetachedException;

/**
 * @author Lukas Eder
 */
abstract class AbstractQuery extends AbstractQueryPart implements Query {

    private static final long       serialVersionUID = -8046199737354507547L;
    private static final JooqLogger log              = JooqLogger.getLogger(AbstractQuery.class);

    final AttachableImpl            attachable;

    AbstractQuery(Configuration configuration) {
        this.attachable = new AttachableImpl(this, configuration);
    }

    @Override
    public final void attach(Configuration configuration) {
        attachable.attach(configuration);
    }

    @Override
    public final int execute() throws SQLException, DetachedException {
        if (isExecutable()) {
            StopWatch watch = new StopWatch();

            // Let listeners provide a configuration to this query
            Configuration configuration = ConfigurationRegistry.provideFor(attachable.getConfiguration());
            if (configuration == null) {
                configuration = attachable.getConfiguration();
            }

            Connection connection = configuration.getConnection();
            if (connection == null) {
                throw new DetachedException("Cannot execute query. No Connection configured");
            }

            // Ensure that all depending Attachables are attached
            attach(configuration);
            watch.splitTrace("Parts attached");

            PreparedStatement statement = null;
            int result = 0;
            try {
                String sql = create(configuration).render(this);
                watch.splitTrace("SQL rendered");

                if (log.isDebugEnabled())
                    log.debug("Executing query", create(configuration).renderInlined(this));
                if (log.isTraceEnabled())
                    log.trace("Preparing statement", sql);

                statement = connection.prepareStatement(sql);
                watch.splitTrace("Statement prepared");

                create(configuration).bind(this, statement);
                watch.splitTrace("Variables bound");

                result = execute(configuration, statement);
                watch.splitTrace("Statement executed");

                return result;
            }
            finally {
                if (!keepStatementOpen()) {
                    JooqUtil.safeClose(statement);
                }

                watch.splitDebug("Statement executed");
            }
        }
        else {
            if (log.isDebugEnabled()) {
                log.debug("Query is not executable", this);
            }

            return 0;
        }
    }

    /**
     * Default implementation to indicate whether this query should close the
     * statement after execution. Subclasses may override this method.
     */
    protected boolean keepStatementOpen() {
        return false;
    }

    /**
     * Default implementation for query execution. Subclasses may override this
     * method.
     *
     * @param configuration The configuration that is used for this query's
     *            execution.
     * @param statement The statement to be executed.
     */
    protected int execute(Configuration configuration, PreparedStatement statement) throws SQLException {
        return statement.executeUpdate();
    }

    /**
     * Default implementation for executable check. Subclasses may override this
     * method.
     */
    protected boolean isExecutable() {
        return true;
    }
}
