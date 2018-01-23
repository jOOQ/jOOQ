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
package org.jooq.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.jooq.tools.jdbc.DefaultResultSet;

/**
 * @author Lukas Eder
 */
final class DiagnosticsResultSet extends DefaultResultSet {

    final DiagnosticsConnection connection;
    int                         current;
    int                         rows;

    public DiagnosticsResultSet(ResultSet delegate, Statement creator, DiagnosticsConnection connection) {
        super(delegate, creator);

        this.connection = connection;
    }

    // ------------------------------------------------------------------------
    // XXX Navigational methods
    // ------------------------------------------------------------------------

    @Override
    public void beforeFirst() throws SQLException {
        super.beforeFirst();
        moveAbsolute(true, super.getRow());
    }

    @Override
    public void afterLast() throws SQLException {
        super.afterLast();
        moveAbsolute(true, super.getRow());
    }

    @Override
    public boolean first() throws SQLException {
        return moveAbsolute(super.first(), super.getRow());
    }

    @Override
    public boolean last() throws SQLException {
        return moveAbsolute(super.last(), super.getRow());
    }

    @Override
    public boolean absolute(int row) throws SQLException {
        return moveAbsolute(super.absolute(row), super.getRow());
    }

    @Override
    public boolean relative(int relative) throws SQLException {
        return moveRelative(super.relative(relative), relative);
    }

    @Override
    public boolean next() throws SQLException {
        return moveRelative(super.next(), 1);
    }

    @Override
    public boolean previous() throws SQLException {
        return moveRelative(super.previous(), -1);
    }

    private final boolean moveRelative(boolean success, int relative) {
        if (success) {
            current = current + relative;
            rows = Math.max(rows, current);
        }

        return success;
    }

    private final boolean moveAbsolute(boolean success, int absolute) {
        if (success) {
            current = absolute;
            rows = Math.max(rows, current);
        }

        return success;
    }

    @Override
    public void close() throws SQLException {
        try {
            if (current < rows)
                super.absolute(current = rows);

            if (super.next()) {
                DefaultDiagnosticsContext ctx = new DefaultDiagnosticsContext();

                ctx.resultSet = super.getDelegate();
                ctx.resultSetFetchedRows = current;
                ctx.resultSetActualRows = current + 1;

                connection.listeners.resultSetTooLarge(ctx);
            }
        }
        catch (SQLException ignore) {}

        super.close();
    }
}
