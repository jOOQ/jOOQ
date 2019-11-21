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
package org.jooq.tools.jdbc;

import java.sql.Connection;
import java.time.Clock;
import java.util.Map;
import java.util.concurrent.Executor;

import javax.sql.DataSource;

import org.jooq.Configuration;
import org.jooq.ConnectionProvider;
import org.jooq.ConverterProvider;
import org.jooq.DSLContext;
import org.jooq.DiagnosticsListener;
import org.jooq.DiagnosticsListenerProvider;
import org.jooq.ExecuteListener;
import org.jooq.ExecuteListenerProvider;
import org.jooq.ExecutorProvider;
import org.jooq.MetaProvider;
import org.jooq.MigrationListener;
import org.jooq.MigrationListenerProvider;
import org.jooq.RecordListener;
import org.jooq.RecordListenerProvider;
import org.jooq.RecordMapper;
import org.jooq.RecordMapperProvider;
import org.jooq.RecordUnmapper;
import org.jooq.RecordUnmapperProvider;
import org.jooq.SQLDialect;
import org.jooq.TransactionListener;
import org.jooq.TransactionListenerProvider;
import org.jooq.TransactionProvider;
import org.jooq.Unwrapper;
import org.jooq.UnwrapperProvider;
import org.jooq.VersionProvider;
import org.jooq.VisitListener;
import org.jooq.VisitListenerProvider;
import org.jooq.conf.Settings;

/**
 * A mock configuration.
 * <p>
 * This {@link Configuration} wraps a delegate <code>Configuration</code> and
 * wraps all {@link ConnectionProvider} references in
 * {@link MockConnectionProvider}.
 *
 * @author Lukas Eder
 */
@SuppressWarnings("deprecation")
public class MockConfiguration implements Configuration {

    /**
     * Generated UID
     */
    private static final long      serialVersionUID = 2600901130544049995L;

    private final Configuration    delegate;
    private final MockDataProvider provider;

    public MockConfiguration(Configuration delegate, MockDataProvider provider) {
        this.delegate = delegate;
        this.provider = provider;
    }

    @Override
    public DSLContext dsl() {
        return delegate.dsl();
    }

    @Override
    public Map<Object, Object> data() {
        return delegate.data();
    }

    @Override
    public Object data(Object key) {
        return delegate.data(key);
    }

    @Override
    public Object data(Object key, Object value) {
        return delegate.data(key, value);
    }


    @Override
    public Clock clock() {
        return delegate.clock();
    }


    @Override
    public ConnectionProvider connectionProvider() {
        return new MockConnectionProvider(delegate.connectionProvider(), provider);
    }

    @Override
    public ConnectionProvider interpreterConnectionProvider() {
        return new MockConnectionProvider(delegate.interpreterConnectionProvider(), provider);
    }

    @Override
    public ConnectionProvider systemConnectionProvider() {
        return new MockConnectionProvider(delegate.systemConnectionProvider(), provider);
    }

    @Override
    public MetaProvider metaProvider() {
        return delegate.metaProvider();
    }

    @Override
    public VersionProvider versionProvider() {
        return delegate.versionProvider();
    }

    @Override
    public ExecutorProvider executorProvider() {
        return delegate.executorProvider();
    }

    @Override
    public TransactionProvider transactionProvider() {
        return delegate.transactionProvider();
    }

    @Override
    public RecordMapperProvider recordMapperProvider() {
        return delegate.recordMapperProvider();
    }

    @Override
    public RecordUnmapperProvider recordUnmapperProvider() {
        return delegate.recordUnmapperProvider();
    }

    @Override
    public RecordListenerProvider[] recordListenerProviders() {
        return delegate.recordListenerProviders();
    }

    @Override
    public ExecuteListenerProvider[] executeListenerProviders() {
        return delegate.executeListenerProviders();
    }

    @Override
    public MigrationListenerProvider[] migrationListenerProviders() {
        return delegate.migrationListenerProviders();
    }

    @Override
    public VisitListenerProvider[] visitListenerProviders() {
        return delegate.visitListenerProviders();
    }

    @Override
    public TransactionListenerProvider[] transactionListenerProviders() {
        return delegate.transactionListenerProviders();
    }

    @Override
    public DiagnosticsListenerProvider[] diagnosticsListenerProviders() {
        return delegate.diagnosticsListenerProviders();
    }

    @Override
    public UnwrapperProvider unwrapperProvider() {
        return delegate.unwrapperProvider();
    }

    @Override
    public ConverterProvider converterProvider() {
        return delegate.converterProvider();
    }

    @Override
    public org.jooq.SchemaMapping schemaMapping() {
        return delegate.schemaMapping();
    }

    @Override
    public SQLDialect dialect() {
        return delegate.dialect();
    }

    @Override
    public SQLDialect family() {
        return delegate.family();
    }

    @Override
    public Settings settings() {
        return delegate.settings();
    }


    @Override
    public Configuration set(Clock newClock) {
        return delegate.set(newClock);
    }


    @Override
    public Configuration set(ConnectionProvider newConnectionProvider) {
        return delegate.set(newConnectionProvider);
    }

    @Override
    public Configuration set(MetaProvider newMetaProvider) {
        return delegate.set(newMetaProvider);
    }

    @Override
    public Configuration set(VersionProvider newVersionProvider) {
        return delegate.set(newVersionProvider);
    }

    @Override
    public Configuration set(Connection newConnection) {
        return delegate.set(newConnection);
    }

    @Override
    public Configuration set(DataSource newDataSource) {
        return delegate.set(newDataSource);
    }

    @Override
    public Configuration set(Executor newExecutor) {
        return delegate.set(newExecutor);
    }

    @Override
    public Configuration set(ExecutorProvider newExecutorProvider) {
        return delegate.set(newExecutorProvider);
    }

    @Override
    public Configuration set(TransactionProvider newTransactionProvider) {
        return delegate.set(newTransactionProvider);
    }

    @Override
    public Configuration set(RecordMapper<?, ?> newRecordMapper) {
        return delegate.set(newRecordMapper);
    }

    @Override
    public Configuration set(RecordMapperProvider newRecordMapperProvider) {
        return delegate.set(newRecordMapperProvider);
    }

    @Override
    public Configuration set(RecordUnmapper<?, ?> newRecordUnmapper) {
        return delegate.set(newRecordUnmapper);
    }

    @Override
    public Configuration set(RecordUnmapperProvider newRecordUnmapperProvider) {
        return delegate.set(newRecordUnmapperProvider);
    }

    @Override
    public Configuration set(RecordListener... newRecordListeners) {
        return delegate.set(newRecordListeners);
    }

    @Override
    public Configuration set(RecordListenerProvider... newRecordListenerProviders) {
        return delegate.set(newRecordListenerProviders);
    }

    @Override
    public Configuration set(ExecuteListener... newExecuteListeners) {
        return delegate.set(newExecuteListeners);
    }

    @Override
    public Configuration set(ExecuteListenerProvider... newExecuteListenerProviders) {
        return delegate.set(newExecuteListenerProviders);
    }

    @Override
    public Configuration set(MigrationListener... newMigrationListeners) {
        return delegate.set(newMigrationListeners);
    }

    @Override
    public Configuration set(MigrationListenerProvider... newMigrationListenerProviders) {
        return delegate.set(newMigrationListenerProviders);
    }

    @Override
    public Configuration set(VisitListener... newVisitListeners) {
        return delegate.set(newVisitListeners);
    }

    @Override
    public Configuration set(VisitListenerProvider... newVisitListenerProviders) {
        return delegate.set(newVisitListenerProviders);
    }

    @Override
    public Configuration set(TransactionListener... newTransactionListeners) {
        return delegate.set(newTransactionListeners);
    }

    @Override
    public Configuration set(TransactionListenerProvider... newTransactionListenerProviders) {
        return delegate.set(newTransactionListenerProviders);
    }

    @Override
    public Configuration set(DiagnosticsListener... newDiagnosticsListeners) {
        return delegate.set(newDiagnosticsListeners);
    }

    @Override
    public Configuration set(DiagnosticsListenerProvider... newDiagnosticsListenerProviders) {
        return delegate.set(newDiagnosticsListenerProviders);
    }

    @Override
    public Configuration set(Unwrapper newUnwrapper) {
        return delegate.set(newUnwrapper);
    }

    @Override
    public Configuration set(UnwrapperProvider newUnwrapperProvider) {
        return delegate.set(newUnwrapperProvider);
    }

    @Override
    public Configuration set(ConverterProvider newConverterProvider) {
        return delegate.set(newConverterProvider);
    }

    @Override
    public Configuration set(SQLDialect newDialect) {
        return delegate.set(newDialect);
    }

    @Override
    public Configuration set(Settings newSettings) {
        return delegate.set(newSettings);
    }

    @Override
    public Configuration derive() {
        return delegate.derive();
    }


    @Override
    public Configuration derive(Clock newClock) {
        return delegate.derive(newClock);
    }


    @Override
    public Configuration derive(Connection newConnection) {
        return delegate.derive(newConnection);
    }

    @Override
    public Configuration derive(DataSource newDataSource) {
        return delegate.derive(newDataSource);
    }

    @Override
    public Configuration derive(ConnectionProvider newConnectionProvider) {
        return delegate.derive(newConnectionProvider);
    }

    @Override
    public Configuration derive(MetaProvider newMetaProvider) {
        return delegate.derive(newMetaProvider);
    }

    @Override
    public Configuration derive(VersionProvider newVersionProvider) {
        return delegate.derive(newVersionProvider);
    }

    @Override
    public Configuration derive(Executor newExecutor) {
        return delegate.derive(newExecutor);
    }

    @Override
    public Configuration derive(ExecutorProvider newExecutorProvider) {
        return delegate.derive(newExecutorProvider);
    }

    @Override
    public Configuration derive(TransactionProvider newTransactionProvider) {
        return delegate.derive(newTransactionProvider);
    }

    @Override
    public Configuration derive(RecordMapper<?, ?> newRecordMapper) {
        return delegate.derive(newRecordMapper);
    }

    @Override
    public Configuration derive(RecordMapperProvider newRecordMapperProvider) {
        return delegate.derive(newRecordMapperProvider);
    }

    @Override
    public Configuration derive(RecordUnmapper<?, ?> newRecordUnmapper) {
        return delegate.derive(newRecordUnmapper);
    }

    @Override
    public Configuration derive(RecordUnmapperProvider newRecordUnmapperProvider) {
        return delegate.derive(newRecordUnmapperProvider);
    }

    @Override
    public Configuration derive(RecordListener... newRecordListeners) {
        return delegate.derive(newRecordListeners);
    }

    @Override
    public Configuration derive(RecordListenerProvider... newRecordListenerProviders) {
        return delegate.derive(newRecordListenerProviders);
    }

    @Override
    public Configuration derive(ExecuteListener... newExecuteListeners) {
        return delegate.derive(newExecuteListeners);
    }

    @Override
    public Configuration derive(ExecuteListenerProvider... newExecuteListenerProviders) {
        return delegate.derive(newExecuteListenerProviders);
    }

    @Override
    public Configuration derive(MigrationListener... newMigrationListeners) {
        return delegate.derive(newMigrationListeners);
    }

    @Override
    public Configuration derive(MigrationListenerProvider... newMigrationListenerProviders) {
        return delegate.derive(newMigrationListenerProviders);
    }

    @Override
    public Configuration derive(VisitListener... newVisitListeners) {
        return delegate.derive(newVisitListeners);
    }

    @Override
    public Configuration derive(VisitListenerProvider... newVisitListenerProviders) {
        return delegate.derive(newVisitListenerProviders);
    }

    @Override
    public Configuration derive(TransactionListener... newTransactionListeners) {
        return delegate.derive(newTransactionListeners);
    }

    @Override
    public Configuration derive(TransactionListenerProvider... newTransactionListenerProviders) {
        return delegate.derive(newTransactionListenerProviders);
    }

    @Override
    public Configuration derive(DiagnosticsListener... newDiagnosticsListeners) {
        return delegate.derive(newDiagnosticsListeners);
    }

    @Override
    public Configuration derive(DiagnosticsListenerProvider... newDiagnosticsListenerProviders) {
        return delegate.derive(newDiagnosticsListenerProviders);
    }

    @Override
    public Configuration derive(Unwrapper newUnwrapper) {
        return delegate.derive(newUnwrapper);
    }

    @Override
    public Configuration derive(UnwrapperProvider newUnwrapperProvider) {
        return delegate.derive(newUnwrapperProvider);
    }

    @Override
    public Configuration derive(ConverterProvider newConverterProvider) {
        return delegate.derive(newConverterProvider);
    }

    @Override
    public Configuration derive(SQLDialect newDialect) {
        return delegate.derive(newDialect);
    }

    @Override
    public Configuration derive(Settings newSettings) {
        return delegate.derive(newSettings);
    }

}
