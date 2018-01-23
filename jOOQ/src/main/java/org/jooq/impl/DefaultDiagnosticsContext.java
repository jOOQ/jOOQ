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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jooq.DiagnosticsContext;

/**
 * @author Lukas Eder
 */
final class DefaultDiagnosticsContext implements DiagnosticsContext {

    ResultSet          resultSet;
    int                resultSetFetchedColumns;
    int                resultSetActualColumns;
    int                resultSetFetchedRows;
    int                resultSetActualRows;
    final String       normalisedStatement;
    final List<String> duplicateStatements;

    DefaultDiagnosticsContext(String statement) {
        this(statement, Arrays.asList(statement));
    }

    DefaultDiagnosticsContext(String normalisedStatement, List<String> duplicateStatements) {
        this.normalisedStatement = normalisedStatement;
        this.duplicateStatements = duplicateStatements;
    }

    @Override
    public final ResultSet resultSet() {
        return resultSet;
    }

    @Override
    public final int resultSetFetchedRows() {
        return resultSet == null ? -1 : resultSetFetchedRows;
    }

    @Override
    public final int resultSetActualRows() {
        if (resultSet == null)
            return -1;

        try {
            while (resultSet.next())
                resultSetActualRows++;

            resultSet.absolute(resultSetFetchedRows);
        }
        catch (SQLException ignore) {}
        return resultSetActualRows;
    }

    @Override
    public final int resultSetFetchedColumns() {
        return resultSet == null ? -1 : resultSetFetchedColumns;
    }

    @Override
    public final int resultSetActualColumns() {
        return resultSet == null ? -1 : resultSetActualColumns;
    }

    @Override
    public final String normalisedStatement() {
        return normalisedStatement;
    }

    @Override
    public final List<String> duplicateStatements() {
        return Collections.unmodifiableList(duplicateStatements);
    }
}
