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

import static org.jooq.impl.Tools.combine;

import java.util.function.Supplier;

import org.jooq.Configuration;
import org.jooq.DiagnosticsListener;
import org.jooq.DiagnosticsListenerProvider;
import org.jooq.ExecuteListener;
import org.jooq.ExecuteListenerProvider;
import org.jooq.MigrationListener;
import org.jooq.MigrationListenerProvider;
// ...
// ...
// ...
import org.jooq.RecordListener;
import org.jooq.RecordListenerProvider;
import org.jooq.TransactionListener;
import org.jooq.TransactionListenerProvider;
import org.jooq.VisitListener;
import org.jooq.VisitListenerProvider;
import org.jooq.exception.DataAccessException;
import org.jooq.tools.JooqLogger;

/**
 * A base implementation for {@link Configuration} classes, implementing the
 * usual convenience API.
 *
 * @author Lukas Eder
 */
public abstract class AbstractConfiguration implements Configuration {
    private static final JooqLogger log              = JooqLogger.getLogger(AbstractConfiguration.class);

    @Override
    public final Configuration set(RecordListener... newRecordListeners) {
        return set(DefaultRecordListenerProvider.providers(newRecordListeners));
    }

    @Override
    public final Configuration setAppending(RecordListener... newRecordListeners) {
        return setAppending(DefaultRecordListenerProvider.providers(newRecordListeners));
    }

    @Override
    public final Configuration setAppending(RecordListenerProvider... newRecordListenerProviders) {
        return set(combine(recordListenerProviders(), newRecordListenerProviders));
    }

    @Override
    public final Configuration set(ExecuteListener... newExecuteListeners) {
        return set(DefaultExecuteListenerProvider.providers(newExecuteListeners));
    }

    @Override
    public final Configuration setAppending(ExecuteListener... newExecuteListeners) {
        return setAppending(DefaultExecuteListenerProvider.providers(newExecuteListeners));
    }

    @Override
    public final Configuration setAppending(ExecuteListenerProvider... newExecuteListenerProviders) {
        return set(combine(executeListenerProviders(), newExecuteListenerProviders));
    }

    @Override
    public final Configuration set(MigrationListener... newMigrationListeners) {
        return set(DefaultMigrationListenerProvider.providers(newMigrationListeners));
    }

    @Override
    public final Configuration setAppending(MigrationListener... newMigrationListeners) {
        return setAppending(DefaultMigrationListenerProvider.providers(newMigrationListeners));
    }

    @Override
    public final Configuration setAppending(MigrationListenerProvider... newMigrationListenerProviders) {
        return set(combine(migrationListenerProviders(), newMigrationListenerProviders));
    }

    @Override
    public final Configuration set(VisitListener... newVisitListeners) {
        return set(DefaultVisitListenerProvider.providers(newVisitListeners));
    }

    @Override
    public final Configuration setAppending(VisitListener... newVisitListeners) {
        return setAppending(DefaultVisitListenerProvider.providers(newVisitListeners));
    }

    @Override
    public final Configuration setAppending(VisitListenerProvider... newVisitListenerProviders) {
        return set(combine(visitListenerProviders(), newVisitListenerProviders));
    }

    @Override
    public final Configuration set(TransactionListener... newTransactionListeners) {
        return set(DefaultTransactionListenerProvider.providers(newTransactionListeners));
    }

    @Override
    public final Configuration setAppending(TransactionListener... newTransactionListeners) {
        return setAppending(DefaultTransactionListenerProvider.providers(newTransactionListeners));
    }

    @Override
    public final Configuration setAppending(TransactionListenerProvider... newTransactionListenerProviders) {
        return set(combine(transactionListenerProviders(), newTransactionListenerProviders));
    }

    @Override
    public final Configuration set(DiagnosticsListener... newDiagnosticsListeners) {
        return set(DefaultDiagnosticsListenerProvider.providers(newDiagnosticsListeners));
    }

    @Override
    public final Configuration setAppending(DiagnosticsListener... newDiagnosticsListeners) {
        return setAppending(DefaultDiagnosticsListenerProvider.providers(newDiagnosticsListeners));
    }

    @Override
    public final Configuration setAppending(DiagnosticsListenerProvider... newDiagnosticsListenerProviders) {
        return set(combine(diagnosticsListenerProviders(), newDiagnosticsListenerProviders));
    }























    @Override
    public final Configuration derive(RecordListener... newRecordListeners) {
        return derive(DefaultRecordListenerProvider.providers(newRecordListeners));
    }

    @Override
    public final Configuration deriveAppending(RecordListener... newRecordListeners) {
        return deriveAppending(DefaultRecordListenerProvider.providers(newRecordListeners));
    }

    @Override
    public final Configuration deriveAppending(RecordListenerProvider... newRecordListenerProviders) {
        return derive(combine(recordListenerProviders(), newRecordListenerProviders));
    }

    @Override
    public final Configuration derive(ExecuteListener... newExecuteListeners) {
        return derive(DefaultExecuteListenerProvider.providers(newExecuteListeners));
    }

    @Override
    public final Configuration deriveAppending(ExecuteListener... newExecuteListeners) {
        return deriveAppending(DefaultExecuteListenerProvider.providers(newExecuteListeners));
    }

    @Override
    public final Configuration deriveAppending(ExecuteListenerProvider... newExecuteListenerProviders) {
        return derive(combine(executeListenerProviders(), newExecuteListenerProviders));
    }

    @Override
    public final Configuration derive(MigrationListener... newMigrationListeners) {
        return derive(DefaultMigrationListenerProvider.providers(newMigrationListeners));
    }

    @Override
    public final Configuration deriveAppending(MigrationListener... newMigrationListeners) {
        return deriveAppending(DefaultMigrationListenerProvider.providers(newMigrationListeners));
    }

    @Override
    public final Configuration deriveAppending(MigrationListenerProvider... newMigrationListenerProviders) {
        return derive(combine(migrationListenerProviders(), newMigrationListenerProviders));
    }

    @Override
    public final Configuration derive(VisitListener... newVisitListeners) {
        return derive(DefaultVisitListenerProvider.providers(newVisitListeners));
    }

    @Override
    public final Configuration deriveAppending(VisitListener... newVisitListeners) {
        return deriveAppending(DefaultVisitListenerProvider.providers(newVisitListeners));
    }

    @Override
    public final Configuration deriveAppending(VisitListenerProvider... newVisitListenerProviders) {
        return derive(combine(visitListenerProviders(), newVisitListenerProviders));
    }

    @Override
    public final Configuration derive(TransactionListener... newTransactionListeners) {
        return derive(DefaultTransactionListenerProvider.providers(newTransactionListeners));
    }

    @Override
    public final Configuration deriveAppending(TransactionListener... newTransactionListeners) {
        return deriveAppending(DefaultTransactionListenerProvider.providers(newTransactionListeners));
    }

    @Override
    public final Configuration deriveAppending(TransactionListenerProvider... newTransactionListenerProviders) {
        return derive(combine(transactionListenerProviders(), newTransactionListenerProviders));
    }

    @Override
    public final Configuration derive(DiagnosticsListener... newDiagnosticsListeners) {
        return derive(DefaultDiagnosticsListenerProvider.providers(newDiagnosticsListeners));
    }

    @Override
    public final Configuration deriveAppending(DiagnosticsListener... newDiagnosticsListeners) {
        return deriveAppending(DefaultDiagnosticsListenerProvider.providers(newDiagnosticsListeners));
    }

    @Override
    public final Configuration deriveAppending(DiagnosticsListenerProvider... newDiagnosticsListenerProviders) {
        return derive(combine(diagnosticsListenerProviders(), newDiagnosticsListenerProviders));
    }























    @Override
    public boolean commercial(Supplier<String> logMessage) {
        if (commercial())
            return true;

        log.warn(logMessage.get());
        return false;
    }

    @Override
    public boolean requireCommercial(Supplier<String> logMessage) throws DataAccessException {
        if (commercial())
            return true;

        throw new DataAccessException(logMessage.get());
    }
}
